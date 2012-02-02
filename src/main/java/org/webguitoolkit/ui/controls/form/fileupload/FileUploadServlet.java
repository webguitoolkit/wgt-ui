/*
Copyright 2008 Endress+Hauser Infoserve GmbH&Co KG 
Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
implied. See the License for the specific language governing permissions 
and limitations under the License.
*/
package org.webguitoolkit.ui.controls.form.fileupload;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.ajax.DWRController;

/**
 * 
 * This code has to be placed in the web.xml:<br>
 * <!-- <servlet> <servlet-name>FileUpload</servlet-name>
 * <servlet-class>com.endress.infoserve.wgt.controls.form.fileupload.FileUploadServlet</servlet-class> <init-param>
 * <param-name>MAX_FILE_SIZE</param-name> <param-value>536870912</param-value> </init-param> </servlet> --> &lt;servlet&gt;<br>
 * &nbsp;&lt;servlet-name&gt;FileUpload&lt;/servlet-name&gt;<br>
 * &nbsp;&lt;servlet-class&gt;com.endress.infoserve.wgt.controls.form.fileupload.FileUploadServlet&lt;/servlet-class&gt;<br>
 * &nbsp;&lt;init-param&gt;<br>
 * &nbsp;&nbsp;&lt;param-name&gt;MAX_FILE_SIZE&lt;/param-name&gt;<br>
 * &nbsp;&nbsp;&lt;param-value&gt;536870912&lt;/param-value&gt;&lt;!-- 512 MB --&gt;<br>
 * &nbsp;&lt;/init-param&gt;<br>
 * &lt;/servlet&gt;<br>
 * 
 * 
 */
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 5698324675718685758L;

	public int maxFileSize = 1024 * 1024 * 512; // 512MB

	public static final String MAX_FILE_SIZE = "MAX_FILE_SIZE";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String maxFileSizeString = config.getInitParameter(MAX_FILE_SIZE);
		if (StringUtils.isNumeric(maxFileSizeString))
			maxFileSize = Integer.parseInt(maxFileSizeString);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int eventId = 0;
		Hashtable parameters = new Hashtable();

		String cssId = "";

		List eventParameters = null;
		try {
			cssId = request.getQueryString().replaceAll("cssId=", "");
			UploadListener listener = new UploadListener(request, cssId, 30);

			// Create a factory for disk-based file items
			FileItemFactory factory = new MonitoredDiskFileItemFactory(listener);
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			// currently only one file is uploaded
			List fileItems = new ArrayList();

			// process uploads ..
			int contentlength = request.getContentLength();
			if (contentlength > maxFileSize) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println("<html>");
				out.println("<head><title></title></head>");
				out.println("<body onLoad=\"window.parent.eventParam('" + cssId + "', new Array('File to large','" + contentlength
						+ "'));window.parent.fireWGTEvent('" + cssId + "','" + FileUpload.EVENT_FILE_TO_LARGE + "');\">");
				out.println("</body>");
				out.println("</html>");

				request.getSession().setAttribute("uploadInfo", new UploadInfo(1, 1, 1, 1, "error"));
				return;
			}
			// parsing request and generate tmp files
			List items = upload.parseRequest(request);
			for (Iterator iter = items.iterator(); iter.hasNext();) {
				FileItem item = (FileItem)iter.next();

				// if file item is a parameter, add to parameter map
				// eles add filename to parameter map and store fileitem in the
				// fileitem list
				String name = item.getFieldName();
				if (item.isFormField()) {
					parameters.put(name, item.getString());
				}
				else {
					parameters.put(name, item.getName());
					fileItems.add(item);
				}
			}
			IFileHandler fileHandler = null;

			// filehandler class specified in the fileupload tag.
			String fileHandlerClass = (String)parameters.get("fileHandler");

			// of the filehandler (rather than for a Classname)
			cssId = (String)parameters.get("cssId");
			if (StringUtils.isNotEmpty(cssId) && fileHandler == null) {
				// get access through component event mechanism
				FileUpload fu = null;
				try {
					fu = (FileUpload)DWRController.getInstance().getComponentById(cssId);
				}
				catch (NullPointerException e) {
					// not instance found, probably not the GuiSessionController
					// filter configured
					// to catch the requests for this Servlet
				}
				// this is only possible if the GuiSessionFilter is enabled for
				// this servlet
				if (fu != null) {
					fileHandler = fu.getFileHandler();
				}
			}
			else if (StringUtils.isNotEmpty(fileHandlerClass))
				fileHandler = (IFileHandler)Class.forName(fileHandlerClass).newInstance();

			if (fileItems == null || fileItems.isEmpty() || StringUtils.isEmpty(((FileItem)fileItems.get(0)).getName())) {
				eventId = FileUpload.EVENT_NO_FILE;
				eventParameters = new ArrayList();
				eventParameters.add("error.fileupload.nofile@No file specified!");
			}
			else if (fileHandler != null) {
				fileHandler.init(fileItems, parameters, request);

				// method to process the Upload
				fileHandler.processUpload();

				// get returnparameter of the filehandler to send them bag to
				// the fileupload action listener.
				eventParameters = fileHandler.getEventParameters();
			}
			else {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println("<html>");
				out.println("<head><title></title></head>");
				out.println("<body onLoad=\"window.parent.eventParam('" + cssId
						+ "', new Array('No File Handler found'));window.parent.fireWGTEvent('" + cssId + "','"
						+ FileUpload.EVENT_UPLOAD_ERROR + "');\">");
				out.println("</body>");
				out.println("</html>");

				request.getSession().setAttribute("uploadInfo", new UploadInfo(1, 1, 1, 1, "error"));
				return;
			}
		}
		catch (Exception e) {
			// event Id for errors
			eventId = FileUpload.EVENT_UPLOAD_ERROR;
			eventParameters = new ArrayList();
			eventParameters.add(cssId);
			eventParameters.add(e.toString());
			// e.printStackTrace(); // To change page of catch statement use
			// File | Settings | File Templates.
		}
		finally {
			// try to get cssId to send an event about the result of the upload
			// to the server
			cssId = (String)parameters.get("cssId");
		}

		// put the return parameters in a js array for sending them back to the
		// fileupload's action listener
		String eventParameterArray = "new Array(";
		if (eventParameters != null) {
			for (Iterator iter = eventParameters.iterator(); iter.hasNext();) {
				eventParameterArray += "'" + StringEscapeUtils.escapeJavaScript((String)iter.next()) + "'";
				if (iter.hasNext())
					eventParameterArray += ",";
			}
		}
		eventParameterArray += ")";

		// tell parrent page to do fire the event
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head><title></title></head>");
		out.println("<body onLoad=\"window.parent.eventParam('" + cssId + "', " + eventParameterArray + ");window.parent.fireWGTEvent('"
				+ cssId + "','" + eventId + "');\">");
		out.println("</body>");
		out.println("</html>");
	}
}

class UploadListener implements IOutputStreamListener {
	// UploadListener should not be serialized during upload
	transient HttpServletRequest request;
	private long delay = 0;
	private long startTime = 0;
	private int totalToRead = 0;
	private int totalBytesRead = 0;
	private int totalFiles = -1;
	private String cssId = null;
	
	private int secondCounter = 1; // Use this var to write the updated upload-information only every second to the session and not all the time

	public UploadListener(HttpServletRequest request, long debugDelay) {
		this.request = request;
		this.delay = debugDelay;
		totalToRead = request.getContentLength();
		this.startTime = System.currentTimeMillis();
	}

	public UploadListener(HttpServletRequest request, String cssId, long debugDelay) {
		this.cssId = cssId;
		this.request = request;
		this.delay = debugDelay;
		totalToRead = request.getContentLength();
		this.startTime = System.currentTimeMillis();
	}

	public void start() {
		totalFiles++;
		updateUploadInfo("start");
		secondCounter = 1;
	}

	public void bytesRead(int bytesRead) {
		totalBytesRead = totalBytesRead + bytesRead;
		updateUploadInfo("progress");

		// i102492: Commented out the following lines, because this results in very long lasting upload times.
		// When uploading a 20 mb file the fileupload is sleeping about 2.5 MINUTES without doing anything !!!

		// try {
		// Thread.sleep(delay);
		// }
		// catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

	public void error(String message) {
		updateUploadInfo("error");
	}

	public void done() {
		if (totalFiles == 0)
			updateUploadInfo("nofile");
		updateUploadInfo("done");
	}

	private void updateUploadInfo(String status) {
		long delta = (System.currentTimeMillis() - startTime) / 1000;

		// if (delta >= secondCounter) { // i102492: Write information only every second to the session.
			// Session information is retrieved only every 1.5 seconds, so this is sufficient.
			if (cssId != null) {
				request.getSession().setAttribute("uploadInfo-" + cssId,
						new UploadInfo(totalFiles, totalToRead, totalBytesRead, delta, status));
			}
			request.getSession().setAttribute("uploadInfo", new UploadInfo(totalFiles, totalToRead, totalBytesRead, delta, status));

			secondCounter++;
		// }
	}
}

/**
 * Created by IntelliJ IDEA.
 * 
 * @author Original : plosson on 05-janv.-2006 10:46:26 - Last modified by $Author: plosson $ on $Date: 2006/01/05 10:09:38 $
 * @version 1.0 - Rev. $Revision: 1.1 $
 */
class MonitoredDiskFileItemFactory extends DiskFileItemFactory {
	private IOutputStreamListener listener = null;

	public MonitoredDiskFileItemFactory(IOutputStreamListener listener) {
		super();
		this.listener = listener;
	}

	public MonitoredDiskFileItemFactory(int sizeThreshold, File repository, IOutputStreamListener listener) {
		super(sizeThreshold, repository);
		this.listener = listener;
	}

	public FileItem createItem(String fieldName, String contentType, boolean isFormField, String fileName) {
		return new MonitoredDiskFileItem(fieldName, contentType, isFormField, fileName, getSizeThreshold(), getRepository(), listener);
	}
}

/**
 * Created by IntelliJ IDEA.
 * 
 * @author Original : plosson on 05-janv.-2006 10:46:33 - Last modified by $Author: plosson $ on $Date: 2006/01/05 10:09:38 $
 * @version 1.0 - Rev. $Revision: 1.1 $
 */
class MonitoredDiskFileItem extends DiskFileItem {
	private static final long serialVersionUID = 8697036011267448899L;

	// should not be serialized during upload
	transient MonitoredOutputStream mos = null;
	transient IOutputStreamListener listener;

	public MonitoredDiskFileItem(String fieldName, String contentType, boolean isFormField, String fileName, int sizeThreshold,
			File repository, IOutputStreamListener listener) {
		super(fieldName, contentType, isFormField, fileName, sizeThreshold, repository);
		this.listener = listener;
	}

	public OutputStream getOutputStream() throws IOException {
		if (mos == null) {
			mos = new MonitoredOutputStream(super.getOutputStream(), listener);
		}
		return mos;
	}
}

class MonitoredOutputStream extends OutputStream {
	private OutputStream target;
	private IOutputStreamListener listener;

	public MonitoredOutputStream(OutputStream target, IOutputStreamListener listener) {
		this.target = target;
		this.listener = listener;
		this.listener.start();
	}

	public void write(byte b[], int off, int len) throws IOException {
		target.write(b, off, len);
		listener.bytesRead(len - off);
	}

	public void write(byte b[]) throws IOException {
		target.write(b);
		listener.bytesRead(b.length);
	}

	public void write(int b) throws IOException {
		target.write(b);
		listener.bytesRead(1);
	}

	public void close() throws IOException {
		target.close();
		listener.done();
	}

	public void flush() throws IOException {
		target.flush();
	}
}

interface IOutputStreamListener extends Serializable {
	public void start();

	public void bytesRead(int bytesRead);

	public void error(String message);

	public void done();
}

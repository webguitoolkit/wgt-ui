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
package org.webguitoolkit.ui.tools;

import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.AbstractView;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.container.IHtmlElement;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.event.IServerEventListener;
import org.webguitoolkit.ui.controls.event.ServerEvent;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.form.ILabel;
import org.webguitoolkit.ui.controls.layout.GridLayout;
import org.webguitoolkit.ui.http.ResourceServlet;

/**
 *
 */
public class EEPage extends Page {

	private static final int PREVIEW_TIMEOUT = 2000;
	
	private int xCells = 4;
	private int yCells = 4;
	private int imageWidth = 200;
	private int imageHeight = 200;
	private String image = "./" + ResourceServlet.SERVLET_URL_PATTERN
			+ "/html/logo.gif";

	private ICanvas canvas;
	private ILabel infoLabel;

	/**
	 * @see org.webguitoolkit.ui.controls.Page#pageInit()
	 */
	protected void pageInit() {
		intiPatameter();

		addWgtCSS("eh_theme.css");
		addHeaderLine(getStyles());
		
		canvas = getFactory().createCanvas(this);
		
		ImageView preview = new ImageView( getFactory(), canvas );
		preview.show();
		
		this.timer(PREVIEW_TIMEOUT, new IServerEventListener(){
			public void handle(ServerEvent event) {
				new PuzzelView( getFactory(), canvas ).show();
			}
		});
	}

	/**
	 * 
	 */
	private void intiPatameter() {
		String width = getServletRequest().getParameter("width");
		if (!StringUtils.isEmpty(width)) {
			imageWidth = Integer.parseInt(width);
		}
		String height = getServletRequest().getParameter("height");
		if (!StringUtils.isEmpty(height)) {
			imageHeight = Integer.parseInt(height);
		}
		String xCells = getServletRequest().getParameter("xCells");
		if (!StringUtils.isEmpty(xCells)) {
			this.xCells = Integer.parseInt(xCells);
		}
		String yCells = getServletRequest().getParameter("yCells");
		if (!StringUtils.isEmpty(yCells)) {
			this.yCells = Integer.parseInt(yCells);
		}
		String image = getServletRequest().getParameter("image");
		if (!StringUtils.isEmpty(image)) {
			this.image = image;
		}
	}

	/**
	 * @return the page styles
	 */
	private String getStyles() {
		StringBuffer styles = new StringBuffer();
		styles.append("<style type=\"text/css\">");
		int width = imageWidth / xCells;
		int height = imageHeight / yCells;
		styles.append("#BuBlank {width: " + width + "px;height: " + height
				+ "px; margin:0;} ");
		for (int x = 0; x < xCells; x++)
			for (int y = 0; y < yCells; y++)
				styles.append("#Bu" + x + "" + y + " {margin:0; width: "
						+ width + "px; height: " + height
						+ "px; background: transparent url(" + image + ") -"
						+ x * width + "px -" + y * height + "px no-repeat;} ");

		styles.append("</style>");
		return styles.toString();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.Page#title()
	 * @return the titel
	 */
	protected String title() {
		return "Sort the Items";
	}

	/**
	 * @author i102415
	 *
	 */
	class ImageView extends AbstractView {

		/**
		 * @param factory the factory
		 * @param viewConnector the viewConnector
		 */
		public ImageView(WebGuiFactory factory, ICanvas viewConnector) {
			super(factory, viewConnector);
		}

		/**
		 * @see org.webguitoolkit.ui.controls.AbstractView#createControls(org.webguitoolkit.ui.base.WebGuiFactory, org.webguitoolkit.ui.controls.container.ICanvas)
		 * @param factory the factory
		 * @param viewConnector the viewConnector
		 */
		protected void createControls(WebGuiFactory factory,
				ICanvas viewConnector) {
			IHtmlElement html = factory.createHtmlElement(getViewConnector(), "img");
			html.setAttribute("src", image);
		}
		
	}
	
	/**
	 * @author i102415
	 *
	 */
	class PuzzelView extends AbstractView {
		private IButton[][] buttons = null;
		private IButton[][] positions = null;

		/**
		 * @param factory the factory
		 * @param viewConnector the viewConnector
		 */
		public PuzzelView(WebGuiFactory factory, ICanvas viewConnector) {
			super(factory, viewConnector);

			buttons = new IButton[xCells][yCells];
			positions = new IButton[xCells][yCells];
		}

		/**
		 * @see org.webguitoolkit.ui.controls.AbstractView#createControls(org.webguitoolkit.ui.base.WebGuiFactory, org.webguitoolkit.ui.controls.container.ICanvas)
		 * @param factory the factory
		 * @param viewConnector the viewConnector
		 */
		protected void createControls(WebGuiFactory factory,
				ICanvas viewConnector) {
			GridLayout layout = new GridLayout();
			canvas.setLayout(new GridLayout());

			for (int x = 0; x < xCells; x++)
				for (int y = 0; y < yCells; y++)
					createButton(x, y);

			infoLabel = getFactory().createLabel(viewConnector, "");
			infoLabel.setLayoutData( GridLayout.getLayoutData(0, yCells).setCellColSpan(xCells));
			randomize();
		}

		/**
		 * 
		 */
		private void randomize() {
			int iconsToSort = xCells * yCells;
			Random rnd = new Random();

			for (int i = 0; i < xCells * yCells; i++) {
				int pos = rnd.nextInt(iconsToSort);
				iconsToSort--;
				int currentX = i % xCells;
				int currentY = (int) i / xCells;
				int counter = 0;
				for (int y = 0; y < yCells; y++) {
					for (int x = 0; x < xCells; x++) {
						if (buttons[x][y].getLayoutData() == null) {
							if (counter == pos) {
								buttons[x][y].setLayoutData(GridLayout
										.getLayoutData(currentX, currentY).setCellStyle("padding: 0px;"));
								positions[currentX][currentY] = buttons[x][y];
								positions[currentX][currentY]
										.setActionListener(new IconClickedListener(
												currentX, currentY));
							}
							counter++;
						}
					}
				}
			}
		}

		/**
		 * @param x
		 *            x position
		 * @param y
		 *            y position
		 */
		private void createButton(int x, int y) {
			if (x == 0 && y == 0) {
				buttons[x][y] = getFactory().newIconButton("BuBlank", getViewConnector(),
						"./images/wgt/1.gif", "&nbsp;&nbsp;&nbsp;&nbsp;", null);
			} else {
				buttons[x][y] = getFactory().newIconButton("Bu" + x + "" + y,
						getViewConnector(), "./images/wgt/1.gif", "x:" + (x+1) + " y:" + (y+1), null);
//				buttons[x][y].setDisplayMode3D(true);
			}
		}

		/**
		 * @author i102415
		 * 
		 */
		public class IconClickedListener implements IActionListener {

			private final int x;
			private final int y;

			/**
			 * @param x
			 *            x position
			 * @param y
			 *            y position
			 */
			public IconClickedListener(int x, int y) {
				this.x = x;
				this.y = y;
			}

			/**
			 * @see org.webguitoolkit.ui.controls.event.IActionListener#onAction(org.webguitoolkit.ui.controls.event.ClientEvent)
			 * @param event
			 *            the event
			 */
			public void onAction(ClientEvent event) {

				if (x > 0) {
					if (buttons[0][0] == positions[x - 1][y]) {
						switchButtons(x - 1, y);
						((BaseControl) canvas).redraw();
						check();
						return;
					}
				}
				if (y > 0) {
					if (buttons[0][0] == positions[x][y - 1]) {
						switchButtons(x, y - 1);
						((BaseControl) canvas).redraw();
						check();
						return;
					}
				}
				if (x < xCells - 1) {
					if (buttons[0][0] == positions[x + 1][y]) {
						switchButtons(x + 1, y);
						((BaseControl) canvas).redraw();
						check();
						return;
					}
				}
				if (y < yCells - 1) {
					if (buttons[0][0] == positions[x][y + 1]) {
						switchButtons(x, y + 1);
						((BaseControl) canvas).redraw();
						check();
						return;
					}
				}

			}

			/**
			 * 
			 */
			private void check() {
				for (int y = 0; y < yCells; y++) {
					for (int x = 0; x < xCells; x++) {
						if (buttons[x][y] != positions[x][y])
							return;
					}
				}
				infoLabel.setText("Well Done!");
			}

			/**
			 * @param newX
			 *            the new x position
			 * @param newY
			 *            the new y position
			 */
			private void switchButtons(int newX, int newY) {
				positions[newX][newY] = positions[x][y];
				positions[newX][newY].setLayoutData(GridLayout.getLayoutData(
						newX, newY).setCellStyle("padding: 0px;"));
				positions[newX][newY]
						.setActionListener(new IconClickedListener(newX, newY));
				positions[x][y] = buttons[0][0];
				positions[x][y].setLayoutData(GridLayout.getLayoutData(x, y).setCellStyle("padding: 0px;"));
				positions[x][y]
						.setActionListener(new IconClickedListener(x, y));
			}
		}
	}
}

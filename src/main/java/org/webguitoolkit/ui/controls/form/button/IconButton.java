package org.webguitoolkit.ui.controls.form.button;

import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.A;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.util.JSUtil;
/**
 * IconButton can only display icons, no text.
 * 
 * @author i102415
 *
 */
public class IconButton extends Button implements IButton {

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public IconButton() {
		super();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public IconButton(String id) {
		super(id);
	}

	protected void endHTML(PrintWriter out, String imgSrc, String text,
			boolean mode3D) {
    	String defaultStyle = "wgtIconButton";
    	if( mode3D )
    		defaultStyle = "wgtIconButton3D";
    	IMG input = new IMG();
    	input.setClass( defaultStyle );
    	//necessary to set cssClass. otherwise the addCss will always overwrite the current cssClass
    	setDefaultCssClass(defaultStyle);
    	input.setSrc( imgSrc );
//    	input.setType( "image" );
    	input.setID( getId() );
    	input.setTitle( getTooltip() );
    	input.setStyle( getStyle().getOutput() );
		if( isSimpleLink() ) {
			if (StringUtils.isBlank(getTarget())) {
				input.setOnClick("location='"+getLinkURL()+"'");
			} else {
				input.setOnClick("window.open('"+getLinkURL()+"','"+getTarget()+"')");
			}
		} else {
			if( StringUtils.isNotEmpty( getConfirmMsg() ) )
				input.setOnClick( JSUtil.jsConfirm( getConfirmMsg(), 
						JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION ) ) );
			else
				input.setOnClick( JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION ) );
		}

		if( mode3D ){
			input.setOnMouseOver( "classAdd( this, 'wgtIconButton3D_over' );" );
			input.setOnMouseOut( "classRemove( this, 'wgtIconButton3D_over' );" );
		}
		input.output( out );
	}

	public void setDisabled(boolean disabled) {
		String icon = getSrc();
		if(disabled){
			int filetyppoint = icon.lastIndexOf('.');
			if( icon.indexOf("_disabled.")==-1 ) {
				setSrc(icon.substring(0, filetyppoint) + "_disabled" 
					+ icon.substring(filetyppoint, icon.length()));
			}
		} else {
			setSrc( icon.replaceAll("_disabled", "") );
		}

		String cursorStyle = disabled ? "default" : "pointer";
		//keep styles assigned to this button
		if(hasStyle())
			getContext().setAttribute(getId(), "style", "cursor: "+cursorStyle + ";" + this.getStyleAsString());
		else
			getContext().setAttribute(getId(), "style", "cursor: "+cursorStyle);

		// if button is a input with no source this would be all
		String dis = disabled ? "true" : null;
		getContext().setAttribute(getId(), "disabled", dis);
	}

	public boolean isDisabled() {
		return getContext().getValueAsBool(getId()+".disabled", false);
	}

	protected String id4ImageSrc() {
		return getId()+".src";
	}
	protected String id4Image() {
		return getId();
	}

	public void setAlignment(int pos) {
		// NOTHING TO DO HERE
	}
}

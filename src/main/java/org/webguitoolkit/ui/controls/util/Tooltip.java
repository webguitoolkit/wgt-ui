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

package org.webguitoolkit.ui.controls.util;

import java.io.Serializable;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * 
 * <b>Tooltip object for jquery tooltip</b>
 * <p/>
 * prerequiste is the jquery.tooltip.js and jquery.tooltip.css
 * <p/>
 * 
 * The Tooltip object is used to create jquery events in the BaseControl.class and Select.class with object a tooltip div
 * container is displayed near the HTML Element in the browser. It can contain text, html formated text even images.
 * <p/>
 * HowTo: Add the Tooltip to a BaseControl element in your page <br>
 * <b>Creation of a Tooltip for a Text field</b>
 * <p/>
 * 
 * <pre>
 * // Creation of an jquery tooltip
 * IText field = factory.createText(layout, &quot;PROPERTYNAME&quot;);
 * Tooltip basicTooltip = new Tooltip(&quot;This is a tooltip - It has a header and a body part!&quot;);
 * basicTooltip.setTrackMouseMovement(true);
 * basicTooltip.setShowBody(&quot;-&quot;);
 * field.setTooltip(basicTooltip);
 * </pre>
 * <p/>
 * <b>CSS classes:</b> please inspect jquery.tooltip.css for more information
 * 
 * @author Lars
 * 
 */
public class Tooltip implements Serializable {

	private boolean fancy;
	private boolean showUrl;
	private String showBody;
	private boolean trackMouseMovement;
	private int delayTooltip;
	private String text;
	private String longHTMLText;
	private String jQueryParameter;
	private String imageSrc;

	public Tooltip(String text) {
		this.text = text;
	}

	/**
	 * This method checks all class attributes and creates based on them the parameter String which will be executed via jquery
	 * command in the BaseControl.class
	 * 
	 * @return jQuery Parameter
	 */
	public String getJQueryParameter() {
		StringBuffer tooltipParameter = new StringBuffer();
		if (fancy) {
			tooltipParameter.append("extraClass: 'fancy'");
		}
		if (!showUrl) {
			tooltipParameter.append(",");
			tooltipParameter.append("showURL: false");
		}
		if (trackMouseMovement) {
			tooltipParameter.append(",");
			tooltipParameter.append("track: true");
		}
		if (delayTooltip > 0) {
			tooltipParameter.append(",");
			tooltipParameter.append("delay: " + delayTooltip);
		}

		if (showBody != null && showBody.length() == 1 && !showBody.equals("'") && !showBody.equals("\"")) {
			tooltipParameter.append(",");
			tooltipParameter.append("showBody: '" + showBody + "'");
		}
		if ((longHTMLText != null)) {
			tooltipParameter.append(",");
			tooltipParameter.append("bodyHandler: function() {return '" + StringEscapeUtils.escapeJavaScript(longHTMLText) + "';}");
		}

		if ((imageSrc != null)) {
			tooltipParameter.append(",");
			tooltipParameter.append("bodyHandler: function() {return jQuery('<img/>').attr('src', '" + imageSrc + "');}");
		}

		String firstCharacter = "";
		String parameter = "showURL: false";
		if (tooltipParameter != null && tooltipParameter.length() > 0) {
			firstCharacter = tooltipParameter.toString().substring(0, 1);
		}
		if (firstCharacter.equals(",")) {
			parameter = "{" + tooltipParameter.toString().substring(1, tooltipParameter.length()) + "}";
		}
		else {
			parameter = "{" + tooltipParameter.toString() + "}";
		}

		return parameter;
	}

	/**
	 * the tooltip has the look and feel of an speech bubble
	 * 
	 * @return boolean
	 */
	public boolean isFancy() {
		return fancy;
	}

	/**
	 * @param fancy. it true the tooltip has the look and feel of an speech bubble
	 */
	public void setFancy(boolean fancy) {
		this.fancy = fancy;
	}

	/**
	 * the tooltip displays the src attribute of an html element in the tooltip text. E.g <a src='http://endress.com'
	 * title'Endress und Hausser'>E+H</a> will create a tooltip like
	 * 
	 * <pre>
	 * ------------------------------
	 * | <b>Endress und Hausser</b>  | 
	 * | http://endress.com 		 |
	 * ------------------------------
	 * </pre>
	 * 
	 * @return boolean.
	 */
	public boolean isShowUrl() {
		return showUrl;
	}

	/**
	 * @param showUrl. if true the tooltip displays the src attribute of an html element in the tooltip text. E.g <a
	 *            src='http://endress.com' title'Endress und Hausser'>E+H</a> will create a tooltip like
	 * 
	 *            <pre>
	 * ------------------------------
	 * | <b>Endress und Hausser</b>  | 
	 * | http://endress.com 		 |
	 * ------------------------------
	 * </pre>
	 */
	public void setShowUrl(boolean showUrl) {
		this.showUrl = showUrl;
	}

	/**
	 * the tooltip set in the html element title can be splitted with this delimiter E.g <div
	 * title="Endress und Hausser #People for Process Automation">, and showBody = "#" will create an tooltip like
	 * 
	 * <pre>
	 * --------------------------------------------------------------
	 * | <b>Endress und Hausser</b>                                  | 
	 * | People for Process Automation"> will create an tooltip like |
	 * --------------------------------------------------------------
	 * </pre>
	 * 
	 * @return String
	 */
	public String getShowBody() {
		return showBody;
	}

	/**
	 * @param showBody. set the delimiter which splits the tooltip text. the tooltip set in the html element title can be splitted
	 *            with this delimiter E.g <div title="Endress und Hausser #People for Process Automation">, and showBody = "#"
	 *            will create an tooltip like
	 * 
	 *            <pre>
	 * --------------------------------------------------------------
	 * | <b>Endress und Hausser</b>                                  | 
	 * | People for Process Automation"> will create an tooltip like |
	 * --------------------------------------------------------------
	 * </pre>
	 */
	public void setShowBody(String showBody) {
		this.showBody = showBody;
	}

	/**
	 * the tooltip follows the mouse movement as long as you remain over the object
	 * 
	 * @return boolean
	 */
	public boolean isTrackMouseMovement() {
		return trackMouseMovement;
	}

	/**
	 * @param trackMouseMovement. If true the tooltip follows the mouse movement as long as you remain over the object
	 */
	public void setTrackMouseMovement(boolean trackMouseMovement) {
		this.trackMouseMovement = trackMouseMovement;
	}

	/**
	 * the tooltip appears delayed. default value is 200 the value delaying the tooltip is set in milliseconds
	 * 
	 * @return int
	 */
	public int getDelayTooltip() {
		return delayTooltip;
	}

	/**
	 * @param delayTooltip. Sets the time delay after which the tooltip appears. default value is 200 milliseconds
	 */
	public void setDelayTooltip(int delayTooltip) {
		this.delayTooltip = delayTooltip;
	}

	/**
	 * this is the basic text displayed by the tooltip. can only contain unformated text.
	 * 
	 * @return String
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text. Sets the tooltip text. can only contain unformated text.
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @param textKey. Sets the tooltip text via the textservice and application property file
	 */
	public void setTextKey(String textKey) {
		this.text = TextService.getString(textKey);
	}

	/**
	 * this is the formated text displayed by the tooltip. it can contain html tag.
	 * 
	 * @return String
	 */
	public String getLongHTMLText() {
		return longHTMLText;
	}

	/**
	 * @param longHTMLText. Sets the tooltip text which may contain html tags
	 */
	public void setLongHTMLText(String longHTMLText) {
		this.longHTMLText = longHTMLText;
	}

	/**
	 * the tooltip can contain images. this is the source path to the image.
	 * 
	 * @return String.
	 */
	public String getImageSrc() {
		return imageSrc;
	}

	/**
	 * @param imageSrc. Sets the image (src attribute of the html tag) path and name.
	 */
	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}
}

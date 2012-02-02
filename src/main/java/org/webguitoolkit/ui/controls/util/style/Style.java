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
package org.webguitoolkit.ui.controls.util.style;

import org.webguitoolkit.ui.controls.util.style.selector.IStyleSelector;

/**
 * <pre>
 * Represents a style element of a StyleSheet or inline
 * 
 * The selector defines the representation.
 * 
 * The style keeps a collection of StyleAttributes defining the style behaviour
 * 
 * <code>
 * 		Style s = new Style();
 * 		s.setSelector(new ClassSelector("myclass"));
 * 		s.addStyleAttributes("width:100px");
 * 		s.addStyleAttributes("position:absolute;");
 * 		System.out.println(s.getOutput());
 * </code>
 * 
 * see http://de.selfhtml.org/navigation/css.htm for more options
 * </pre>
 * 
 * 
 * @author Benjamin Klug
 * 
 */
public class Style extends BaseStyle {

	public static final String POINT = "pt";
	public static final String PICA = "pc";
	public static final String INCH = "in";
	public static final String MILLIMETER = "mm";
	public static final String CENTIMETER = "cm";
	public static final String PIXEL = "px";
	public static final String RELATIVE_TO_FONTSIZE = "em";
	public static final String RELATIVE_TO_LOWER_CASE_HEIGHT = "ex";
	public static final String PERCENT = "%";

	public static final String DISPLAY = "display";
	public static final String DISPLAY_NONE = "none";
	public static final String DISPLAY_BLOCK = "block";
	public static final String DISPLAY_INLINE = "inline";

	public static final String VISIBILITY = "visibility";
	public static final String VISIBILITY_HIDDEN = "hidden";
	public static final String VISIBILITY_VISIBLE = "visible";

	public static final String POSITION = "position";
	public static final String POSITION_ABSOLUTE = "absolute";
	public static final String POSITION_FIXED = "fixed";
	public static final String POSITION_RELATIVE = "relative";
	public static final String POSITION_STATIC = "statci";

	public static final String OVERFLOW = "overflow";
	public static final String OVERFLOW_VISABLE = "visible";
	public static final String OVERFLOW_HIDDEN = "hidden";
	public static final String OVERFLOW_SCROLL = "scroll";
	public static final String OVERFLOW_AUTO = "auto";

	public static final String DIRECTION = "direction";
	public static final String DIRECTION_LEFT_TO_RIGHT = "ltr";
	public static final String DIRECTION_RIGHT_TO_LEFT = "rtl";

	public static final String FLOAT = "float";
	public static final String FLOAT_LEFT = "left";
	public static final String FLOAT_RIGHT = "right";
	public static final String FLOAT_NONE = "none";

	public static final String CLEAR = "clear";
	public static final String CLEAR_LEFT = "left";
	public static final String CLEAR_RIGHT = "right";
	public static final String CLEAR_BOTH = "both";
	public static final String CLEAR_NONE = "none";

	public static final String WIDTH = "width";
	public static final String TOP = "top";
	public static final String LEFT = "left";
	public static final String BOTTOM = "bottom";
	public static final String RIGHT = "right";
	public static final String MIN_WIDTH = "min-width";
	public static final String MAX_WIDTH = "max-width";
	public static final String HEIGHT = "height";
	public static final String MIN_HEIGHT = "min-height";
	public static final String MAX_HEIGHT = "max-height";
	public static final String Z_INDEX = "z-index";
	public static final String AUTO = "auto";

	public static final String CURSOR = "cursor";
	public static final String CURSOR_AUTO = "auto";
	public static final String CURSOR_DEFAULT = "default";
	public static final String CURSOR_CROSSHAIR = "crosshair";
	public static final String CURSOR_POINTER = "pointer";
	public static final String CURSOR_MOVE = "move";
	public static final String CURSOR_NORTH_RESIZE = "n-resize";
	public static final String CURSOR_NORTHEAST_RESIZE = "ne-resize";
	public static final String CURSOR_EAST_REZISE = "e-resize";
	public static final String CURSOR_SOUTHEAST_RESIZE = "se-resize";
	public static final String CURSOR_SOUTH_RESIZE = "s-resize";
	public static final String CURSOR_SOUTWEST_RESIZE = "sw-resize";
	public static final String CURSOR_WEST_RESIZE = "w-resize";
	public static final String CURSOR_NORTHWEST_RESIZE = "nw-resize";
	public static final String CURSOR_TEXT = "text";
	public static final String CURSOR_WAIT = "wait";
	public static final String CURSOR_HELP = "help";

	public static final String COLOR_BLACK = "#000000";
	public static final String COLOR_MAROON = "#800000";
	public static final String COLOR_GREEN = "#008000";
	public static final String COLOR_OLIVE = "#808000";
	public static final String COLOR_NAVY = "#000080";
	public static final String COLOR_PURPLE = "#800080";
	public static final String COLOR_TEAL = "#008080";
	public static final String COLOR_SILVER = "#C0C0C0";
	public static final String COLOR_GRAY = "#808080";
	public static final String COLOR_RED = "#FF0000";
	public static final String COLOR_LIME = "#00FF00";
	public static final String COLOR_YELLOW = "#FFFF00";
	public static final String COLOR_BLUE = "#0000FF";
	public static final String COLOR_FUCHSIA = "#FF00FF";
	public static final String COLOR_AQUA = "#00FFFF";
	public static final String COLOR_WHITE = "#FFFFFF";

	public static final String FONT_FAMILY = "font-family";
	public static final String FONT_STYLE = "font-style";
	public static final String FONT_STYLE_ITALIC = "italic";
	public static final String FONT_STYLE_OBLIUE = "oblique";
	public static final String FONT_STYLE_NORMAL = "normal";
	public static final String FONT_VARIANT = "font-variant";
	public static final String FONT_VARIANT_SMALL_CAPS = "small-capst";
	public static final String FONT_VARIANT_NORMAL = "normal";
	public static final String FONT_SIZE = "font-size";
	public static final String FONT_SIZE_XX_SMALL = "xx-small";
	public static final String FONT_SIZE_X_SMALL = "x-small";
	public static final String FONT_SIZE_SMALL = "small";
	public static final String FONT_SIZE_MEDIUM = "medium";
	public static final String FONT_SIZE_LARGE = "large";
	public static final String FONT_SIZE_X_LARGE = "x-large";
	public static final String FONT_SIZE_XX_LARGE = "xx-large";
	public static final String FONT_SIZE_SMALLER = "smaller";
	public static final String FONT_SIZE_LARGER = "larger";
	public static final String FONT_WEIGHT = "font-weight";
	public static final String FONT_WEIGHT_BOLD = "bold";
	public static final String FONT_WEIGHT_BOLDER = "bolder";
	public static final String FONT_WEIGHT_LIGHTER = "lighter";
	public static final String FONT_WEIGHT_NORMAL = "normal";

	public static final String TEXT_DECORATION = "text-decoration";
	public static final String TEXT_DECORATION_UNDERLINE = "underline";
	public static final String TEXT_DECORATION_OVERLINE = "overline";
	public static final String TEXT_DECORATION_LINE_THROUGH = "line-through";
	public static final String TEXT_DECORATION_BLINK = "blink";
	public static final String TEXT_DECORATION_NONE = "none";
	public static final String COLOR = "color";
	public static final String TEXT_TRANSFORM = "text-transform";
	public static final String TEXT_TRANSFORM_CAPITALIZE = "capitalize";
	public static final String TEXT_TRANSFORM_UPPERCASE = "uppercase";
	public static final String TEXT_TRANSFORM_LOWERCASE = "lowercase";
	public static final String TEXT_TRANSFORM_NONE = "none";

	public static final String VERTICAL_ALIGN = "vertical-align";
	public static final String VERTICAL_ALIGN_TOP = "top";
	public static final String VERTICAL_ALIGN_MIDDLE = "middle";
	public static final String VERTICAL_ALIGN_BOTTOM = "bottom";
	public static final String VERTICAL_ALIGN_BASELINE = "baseline";
	public static final String VERTICAL_ALIGN_SUB = "sub";
	public static final String VERTICAL_ALIGN_SUPER = "super";
	public static final String VERTICAL_ALIGN_TEXT_TOP = "text-top";
	public static final String VERTICAL_ALIGN_TEXT_BOTTOM = "text-bottom";

	public static final String TEXT_ALIGN = "text-align";
	public static final String TEXT_ALIGN_LEFT = "left";
	public static final String TEXT_ALIGN_CENTER = "center";
	public static final String TEXT_ALIGN_RIGHT = "right";
	public static final String TEXT_ALIGN_JUSTIFY = "justify";

	public static final String WHITE_SPACE = "white-space";
	public static final String WHITE_SPACE_NORMAL = "normal";
	public static final String WHITE_SPACE_PRE = "pre";
	public static final String WHITE_SPACE_NOWRAP = "nowrap";

	public static final String MARGIN = "marig";
	public static final String MARGIN_TOP = "marig-top";
	public static final String MARGIN_LEFT = "marig-left";
	public static final String MARGIN_RIGHT = "marig-right";
	public static final String MARGIN_BOTTOM = "marig-bottom";

	public static final String PADDING = "padding";
	public static final String PADDING_TOP = "padding-top";
	public static final String PADDING_LEFT = "padding-left";
	public static final String PADDING_RIGHT = "padding-right";
	public static final String PADDING_BOTTOM = "padding-bottom";

	public static final String BORDER_WIDTH = "border-width";
	public static final String BORDER_WIDTH_THIN = "thin";
	public static final String BORDER_WIDTH_MEDIUM = "medium";
	public static final String BORDER_WIDTH_THICK = "thick";
	public static final String BORDER_WIDTH_LEFT = "border-left-width";
	public static final String BORDER_WIDTH_RIGHT = "border-right-width";
	public static final String BORDER_WIDTH_TOP = "border-top-width";
	public static final String BORDER_WIDTH_BOTTOM = "border-bottom-width";

	public static final String BORDER_COLOR = "border-color";
	public static final String BORDER_COLOR_TOP = "border-top-color";
	public static final String BORDER_COLOR_BOTTOM = "border-bottom-color";
	public static final String BORDER_COLOR_LEFT = "border-left-color";
	public static final String BORDER_COLOR_RIGHT = "border-right-color";

	public static final String BORDER = "border";
	public static final String BORDER_TOP = "border-top";
	public static final String BORDER_BOTTOM = "border-bottom";
	public static final String BORDER_LEFT = "border-left";
	public static final String BORDER_RIGHT = "border-right";

	/**
	 * default constructor
	 */
	public Style() {
		super();
	}

	/**
	 * use this constructor to pass over the selector
	 * 
	 * @param newSelector
	 */
	public Style(IStyleSelector newSelector) {
		super(newSelector);
		this.setSelector(newSelector);
	}

	/**
	 * pass i.e. "100%"
	 * 
	 * @param cssBehaviour
	 * @return 
	 */
	public Style addWidth(String cssWidth) {
		this.addStyleAttribute(new StyleAttribute(WIDTH, cssWidth));
		return this;
	}
	
	/**
	 * pass i.e. "100%"
	 * 
	 * @param cssBehaviour
	 */
	public Style addHeight(String cssHeight) {
		this.addStyleAttribute(new StyleAttribute(HEIGHT, cssHeight));
		return this;
	}

	/**
	 * pass i.e. "100", Style.PERCENT
	 * 
	 * @param cssBehaviour
	 */
	public Style addWidth(int cssWidth, String cssUnit) {
		this.addStyleAttribute(new StyleAttribute(WIDTH, cssWidth + cssUnit));
		return this;
	}
	
	/**
	 * pass i.e. "100", Style.PERCENT
	 * 
	 * @param cssBehaviour
	 */
	public Style addHeight(int cssHeight, String cssUnit) {
		this.addStyleAttribute(new StyleAttribute(HEIGHT, cssHeight + cssUnit));
		return this;
	}

	/**
	 * pass i.e. "100", Style.PIXEL
	 * 
	 * @param cssBehaviour
	 */
	public Style addTop(int cssTop, String cssUnit) {
		this.addStyleAttribute(new StyleAttribute(TOP, cssTop + cssUnit));
		return this;
	}

	/**
	 * pass i.e. "100", Style.PIXEL
	 * 
	 * @param cssBehaviour
	 */
	public Style addBottom(int cssBottom, String cssUnit) {
		this.addStyleAttribute(new StyleAttribute(BOTTOM, cssBottom + cssUnit));
		return this;
	}

	/**
	 * pass i.e. "100", Style.PIXEL
	 * 
	 * @param cssBehaviour
	 */
	public Style addLeft(int cssLeft, String cssUnit) {
		this.addStyleAttribute(new StyleAttribute(LEFT, cssLeft + cssUnit));
		return this;
	}
	
	/**
	 * pass i.e. "100"
	 * 
	 * @param cssBehaviour
	 */
	public Style addZIndex(int cssZIndex) {
		this.addStyleAttribute(new StyleAttribute(Z_INDEX, String.valueOf(cssZIndex)));
		return this;
	}

	/**
	 * pass i.e. "100", Style.PIXEL
	 * 
	 * @param cssBehaviour
	 */
	public Style addRigth(int cssRight, String cssUnit) {
		this.addStyleAttribute(new StyleAttribute(RIGHT, cssRight + cssUnit));
		return this;
	}

	/**
	 * pass i.e. "100", Style.PIXEL
	 * 
	 * @param cssBehaviour
	 */
	public Style add(String cssName, String cssValue) {
		this.addStyleAttribute(new StyleAttribute(cssName, cssValue));
		return this;
	}

	/**
	 * pass i.e. Style.DISPLAY_NONE
	 * 
	 * @param cssBehaviour
	 */
	public Style addDisplay(String cssDiaplay) {
		this.addStyleAttribute(new StyleAttribute(DISPLAY, cssDiaplay));
		return this;
	}

	/**
	 * pass i.e. Style.POSTITION_ABSOLUTE
	 * 
	 * @param cssBehaviour
	 */
	public Style addPostition(String cssPosition) {
		this.addStyleAttribute(new StyleAttribute(POSITION, cssPosition));
		return this;
	}

	/**
	 * pass i.e. Style.TEXT_ALIGN_CENTER
	 * 
	 * @param cssBehaviour
	 */
	public Style addTextAlign(String cssTextAligh) {
		this.addStyleAttribute(new StyleAttribute(TEXT_ALIGN, cssTextAligh));
		return this;
	}

}

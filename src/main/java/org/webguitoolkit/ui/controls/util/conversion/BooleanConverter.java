package org.webguitoolkit.ui.controls.util.conversion;

import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;

public class BooleanConverter implements IConverter {

	public Object parse(String textRep) throws ConversionException {
		return new Boolean(textRep);
	}

	public Object convert(Class type, Object value) {
    	if (type!=String.class) throw new WGTException("can only convert into string");
    	if (value==null || !(value instanceof Boolean) ) return "false";
		return ((Boolean)value).toString();
	}
}

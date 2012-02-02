package org.webguitoolkit.ui.controls.util.conversion;

import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;

/**
 * Trim spaces from passed String.
 * 
 * @author peter@17sprints.de
 * 
 */
public class TrimConverter implements IConverter {

	private static final long serialVersionUID = 1L;

	public Object parse(String value) throws ConversionException {
		return value.trim();
	}

	public Object convert(Class type, Object value) {
		if (type == String.class && value != null)
			return String.valueOf(value);
		return value;
	}

}
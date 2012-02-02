package org.webguitoolkit.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

public abstract class AbstractDataBagComparator implements Comparator, Serializable {
	protected String property;
	protected boolean asc;

	/**
	 * @param property, date property to sort
	 * @param asc, direction
	 */
	public AbstractDataBagComparator(String property, boolean asc) {
		this.property = property;
		this.asc = asc;
	}

	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

}

package org.webguitoolkit.ui.util.comparator;

import org.webguitoolkit.ui.base.IDataBag;

/**
 * compare bags by gives property
 * Usage: Collections.sort(yourcollection, new DataBagNumberPropertyComparator("createdAt",true));
 * @author Ben
 * 
 */
public class DataBagNumberPropertyComparator extends AbstractDataBagComparator {

	public DataBagNumberPropertyComparator(String property, boolean asc) {
		super(property, asc);
	}

	public int compare(Object bag1, Object bag2) {
		Double one = new Double(String.valueOf(((IDataBag) bag1).get(property)));
		Double two = new Double(String.valueOf(((IDataBag) bag2).get(property)));

		if (asc)
			return two.compareTo(one);
		else
			return one.compareTo(two);
	}

}

package org.webguitoolkit.ui.util.comparator;

import java.util.Date;

import org.webguitoolkit.ui.base.IDataBag;

/**
 * compare bags by gives property 
 * Usage: Collections.sort(yourcollection, new DataBagDatePropertyComparator("createdAt",true));
 * 
 * @author Ben
 * 
 */
public class DataBagDatePropertyComparator extends AbstractDataBagComparator {
	public DataBagDatePropertyComparator(String property, boolean asc) {
		super(property, asc);
	}

	public int compare(Object bag1, Object bag2) {
		Date one = ((IDataBag) bag1).getDate(property);
		Date two = ((IDataBag) bag2).getDate(property);

		if (asc)
			return two.compareTo(one);
		else
			return one.compareTo(two);
	}

}

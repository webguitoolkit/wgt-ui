package org.webguitoolkit.ui.util.comparator;

import org.webguitoolkit.ui.base.IDataBag;

/**
 * compare bags by given property of type Boolean
 * Usage: Collections.sort(yourcollection, new DataBagBooleanPropertyComparator("confirmed", true));
 * @author Alexander Sattler
 *
 */
public class DataBagBooleanPropertyComparator extends AbstractDataBagComparator {

	public DataBagBooleanPropertyComparator(String property, boolean asc) {
		super(property, asc);
	}

	public int compare(Object bag1, Object bag2) {
		Boolean one = ((IDataBag) bag1).getBool(property);
		Boolean two = ((IDataBag) bag2).getBool(property);

		if (asc)
			return two.compareTo(one);
		else
			return one.compareTo(two);
	}

}
package org.webguitoolkit.ui.util.comparator;

import org.webguitoolkit.ui.base.IDataBag;

/**
 * compare bags by gives property
 * Usage: Collections.sort(yourcollection, new DataBagStringPropertyComparator("createdAt",true));
 * @author Ben
 *
 */
public class DataBagStringPropertyComparator extends AbstractDataBagComparator {
	public DataBagStringPropertyComparator(String property, boolean asc) {
		super(property, asc);
	}

	public int compare(Object bag1, Object bag2) {
		String one = ((IDataBag) bag1).getString(property);
		String two = ((IDataBag) bag2).getString(property);

		if (asc)
			return two.compareTo(one);
		else
			return one.compareTo(two);
	}

}

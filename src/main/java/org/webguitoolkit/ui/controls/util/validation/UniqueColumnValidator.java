package org.webguitoolkit.ui.controls.util.validation;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.controls.form.FormControl;
import org.webguitoolkit.ui.controls.form.ICompound;
import org.webguitoolkit.ui.controls.form.IFormControl;
import org.webguitoolkit.ui.controls.form.Text;
import org.webguitoolkit.ui.controls.table.ITable;
import org.webguitoolkit.ui.controls.table.Table;
/**
 * This validator check if the string to be validated is already present in a tables column.
 * It is expected, that the table and the form use the same instances of databags and
 * therefore the property of the compound and the property in the tables bags have the same anem.
 * These conditions are met iff you load the compound with the tables databags, whch is usually the case.
 * 
 * @author arno
 *
 */
public class UniqueColumnValidator implements IValidator {

	protected IFormControl field;
	protected ITable tbl;
	protected String customMessage;
	
	public UniqueColumnValidator(ITable tbl, IFormControl field, String customMessage) {
		super();
		this.field = field;
		this.tbl = tbl;
		this.customMessage = customMessage;
	}

	public void validate(String textRep) throws ValidationException {
		if (StringUtils.isBlank(textRep)) return;
		IDataBag bag;
		final IDataBag compBag = field.surroundingCompound().getBag();
		for (Iterator it = tbl.getDefaultModel().getTableData().iterator(); it.hasNext();) {
			bag = (IDataBag) it.next();
			if (bag == compBag) continue; // don't try to check ourselves
			if (textRep.equals(bag.get(field.getProperty())) ) {
				// some one is trying to change to existing name
				throw new ValidationException(customMessage);
			}
		} 
	}

	public String getTooltip() {
		// what the heck is this?
		return null;
	}

}

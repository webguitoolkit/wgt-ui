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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.form.AssociationSelectModel;
import org.webguitoolkit.ui.controls.form.ISelect;
import org.webguitoolkit.ui.controls.form.Select;
import org.webguitoolkit.ui.controls.form.SelectKVModel;
import org.webguitoolkit.ui.util.UnTranslatedTextCollector;

/**
 * <pre>
 * Helper to access the resource bundle.
 * 
 * Default name of the the file is "ApplicationResources"
 * it has to be in the classpath
 * </pre>
 */
@SuppressWarnings("unchecked")
public class TextService {

	public static final String DEFAULT_RESOURCE_BUNDLE_NAME = "ApplicationResources";

	public TextService() {
		super();
	}

	protected static ThreadLocal<Locale> threadLocales = new ThreadLocal<Locale>();
	protected static ThreadLocal<String> resourceBundleName = new ThreadLocal<String>();
	protected static TextService instance = null;

	public static TextService getInstance() {
		if (instance == null) {
			instance = new TextService();
		}
		return instance;
	}

	public static void setInstance(TextService my) {
		instance = my;
	}

	/**
	 * we are expecting a locale in the threadlocale.
	 * 
	 * There is a special notation: If you use @ in the text, this means: The text key is before the @-sign and the default
	 * textmessage after it It searches for the key, if it can't find it, it will use the default textmessage for output and
	 * stores the resource key in the UnTranslatedTextCollector.
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(final String transKey) {
		return getInstance().getString(transKey, getLocale());
	}

	public String getString(final String transKey, Locale locale) {
		if (locale == null)
			locale = Locale.getDefault();
		if (transKey == null)
			return null;
		if (transKey.equals(""))
			return transKey;
		String defText = transKey;
		String key = transKey;
		int pos = key.indexOf('@');
		if (pos > 0) {
			key = key.substring(0, pos);
			defText = defText.substring(pos + 1);
		}

		// if the translator choose the Zulu language ("zu")
		// they can see where the key are on the screen
		if (locale.getLanguage().equals("zu")) {
			// disable wgt translations
			return showKey(key);
		}

		try {
			return bundle(locale, key);
		}
		catch (MissingResourceException e) {
			UnTranslatedTextCollector.addKey(key, defText);
			// this will show the language keys of key which were not found
			// in the translation of the current language. (or default).
			HttpServletRequest req = Page.getServletRequest();
			if (req == null || req.getSession() == null)
				return defText;

			Boolean showKeys = (Boolean)req.getSession().getAttribute(getClass().getName());
			return (showKeys != null && showKeys) ? showKey(transKey) : defText;
		}
	}

	private String showKey(String transKey) {
		if (transKey == null)
			return null;
		return "#" + transKey;
	}

	/**
	 * @return
	 */
	protected String bundle(Locale loc, String key) {
		return bundle(loc).getString(key);
	}

	protected ResourceBundle bundle(Locale loc) {
		String resourceBundleName = getResourceBundleName();
		if (resourceBundleName == null)
			resourceBundleName = DEFAULT_RESOURCE_BUNDLE_NAME;

		ResourceBundle rs = ResourceBundle.getBundle(resourceBundleName, loc, TextService.class.getClassLoader());
		return rs;
	}

	/**
	 * parameterized translation, use {1} for first parameter. the '{1}' in the translated text will be replaced by the parameter
	 * passed to this method.
	 * 
	 * @param key
	 * @param par
	 * @return
	 */
	public static String getString(String key, String[] par) {
		return getInstance().getStringInt(key, par);
	}

	public String getStringInt(String key, String[] par) {
		// replace parameter inside the string...
		String translated = getString(key);
		if (translated == null)
			return null; // error condition.

		// if par is null return the translated text without parameter
		// replacement
		if (par == null)
			return translated;

		for (int i = 0; i < par.length; i++) {
			String spar = "{" + (i + 1) + "}";
			translated = StringUtils.replace(translated, spar, par[i]);
		}
		return translated;
	}

	/**
	 * translate string with one parameter
	 * 
	 * @see getString
	 * @param key
	 * @param par1
	 * @return
	 */
	public static String getString(String key, String par1) {
		return getInstance().getStringInt(key, new String[] { par1 });
	}

	public void setLocaleInt(Locale locale) {
		threadLocales.set(locale);
	}

	public Locale getLocaleInt() {
		return (Locale)threadLocales.get();
	}

	public static void setResourceBundleName(String resourceBundle) {
		getInstance().setResourceBundleNameInt(resourceBundle);
	}

	public void setResourceBundleNameInt(String resourceBundle) {
		resourceBundleName.set(resourceBundle);
	}

	public String getResourceBundleName() {
		return (String)resourceBundleName.get();
	}

	public String[] splitTextList(String commaSeperatedText) {
		String[] ret = StringUtils.split(commaSeperatedText, ',');
		if (ret == null)
			return new String[] {};
		for (int i = 0; i < ret.length; i++) {
			ret[i] = StringUtils.trimToEmpty(ret[i]);
		}
		return ret;
	}

	/**
	 * join a array of string wrapping the string with the escape-string (at the end an the beginning) and put a seperator in
	 * between. example: join(new String[]{"1","2"}, "'", ";") returns the string '1';'2'
	 */
	public String join(String[] singles, String escape, String seperator) {
		if (singles == null)
			return null;
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < singles.length; i++) {
			if (i > 0)
				ret.append(seperator);
			ret.append(escape);
			ret.append(singles[i]);
			ret.append(escape);
		}
		return ret.toString();
	}

	/**
	 * fill given Select with available locales if used this way persisting compound data needs to mark the locale-containing
	 * object as changed in persist()
	 */
	public static void loadLocales(ISelect lcSelect) {
		getInstance().loadLocalesInt(lcSelect);
	}

	public void loadLocalesInt(ISelect lcSelect) {
		Locale[] locs = Locale.getAvailableLocales();
		Map<String, String> loclist = new TreeMap<String, String>();

		for (int i = 0; i < locs.length; i++) {
			Locale l = locs[i];
			String displayName = l.getDisplayName(getLocale());
			if (StringUtils.isNotBlank(displayName) && !loclist.containsValue(displayName)) {
				loclist.put(l.toString(), displayName);
			}
		}
		lcSelect.setModel(new SelectKVModel().loadList(loclist));
		lcSelect.loadList();
		lcSelect.setValue(getLocale().toString());
	}

	public static void setLocale(String strLocale) {
		getInstance().setLocaleInt(strLocale);
	}

	public void setLocaleInt(String strLocale) {
		Locale[] locs = Locale.getAvailableLocales();
		for (int i = 0; i < locs.length; i++) {
			if (locs[i].toString().equals(strLocale)) {
				setLocale(locs[i]);
				return;
			}
		}
	}

	// unfortunately this methosd is not possible to implement easily.
	// public static void loadLocalesAsAssociationModel(ISelect lcSelect) {

	/**
	 * fill given Select with available timezones if used this way persisting compound data needs to mark the timezone-containing
	 * object as changed in persist()
	 */
	public void loadTimeZones(Select tzSelect) {
		String[] timezoneArray = TimeZone.getAvailableIDs();
		Arrays.sort(timezoneArray);
		List tzList = new ArrayList();

		for (int i = 0; i <= timezoneArray.length - 1; i++) {
			String[] toAdd = { timezoneArray[i], timezoneArray[i] };
			tzList.add(toAdd);
		}
		tzSelect.getDefaultModel().setOptions(tzList);
		tzSelect.loadList();
	}

	/**
	 * fill given Select with available timezones if loaded as AssociationModel no extra processing logic is needed in persist()
	 * to mark the timezone-containing object as changed
	 */
	public void loadTimeZonesAsAssociationModel(ISelect tzSelect) {
		String[] timezoneArray = TimeZone.getAvailableIDs();
		List tzList = new ArrayList();

		for (int i = 0; i <= timezoneArray.length - 1; i++) {
			tzList.add(TimeZone.getTimeZone(timezoneArray[i]));
		}
		AssociationSelectModel sModel = new AssociationSelectModel("ID", "ID", true);
		tzSelect.setModel(sModel);
		sModel.setOptions(tzList);
		tzSelect.loadList();
	}

	/**
	 * this returns a list of key/value pairs. The Keys are takenfrom the default[0] and the value are the translation for
	 * prefix+default[0], if that translation key ist not found, default[1] is taken.
	 * 
	 * This is being used for translatable select boxen with fixed valuelists.
	 * 
	 * @param prefix should end witha '.' for better readability of the property files
	 * @param defaults
	 * @return List of String[2], for selects. containing {kesy without prefix, translated value}
	 */
	public static List prefixKey(String prefix, String[][] defaults) {
		return getInstance().prefixKeyInt(prefix, defaults);
	}

	public List prefixKeyInt(String prefix, String[][] defaults) {
		if (defaults == null)
			return prefixKey(prefix);
		List list = new ArrayList(defaults.length);
		for (int i = 0; i < defaults.length; i++) {
			String key = defaults[i][0];
			String value = defaults[i][1];
			value = getString(prefix + key + '@' + value);
			list.add(new String[] { key, value });
		}
		return list;
	}

	/**
	 * this lists all translation where the key startWith 'prefix'.
	 * 
	 * @param prefix
	 * @return List od String[2], each holding (key, translation)
	 */
	public List prefixKey(String prefix) {
		List list = new LinkedList();
		String key;
		// the resource bundle holding the translation for current locale
		final ResourceBundle bundle = bundle(getLocale());
		// go through all key and find those with prefix
		for (Enumeration en = bundle.getKeys(); en.hasMoreElements();) {
			key = (String)en.nextElement();
			if ((key != null) && key.startsWith(prefix)) {
				// prefix found, add key and translation to list
				// so it can be loaded in select
				String[] entry = new String[] { key.substring(prefix.length()), getString(key) };
				list.add(entry);
			}
		}
		if (list.isEmpty()) {
			UnTranslatedTextCollector.addKey(prefix, "All keys for this prefix are missing.");
		}
		return list;
	}

	public static Locale getLocale() {
		return getInstance().getLocaleInt();
	}

	public static void setLocale(Locale locale) {
		getInstance().setLocaleInt(locale);
	}
}

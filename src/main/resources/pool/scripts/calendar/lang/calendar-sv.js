// ** I18N

// Calendar SV language (Swedish, svenska)
// Author: Mihai Bazon, <mihai_bazon@yahoo.com>
// Translation team: <sv@li.org>
// Translator: Leonard Norrg\u00E5rd <leonard.norrgard@refactor.fi>
// Last translator: Emil Ljungdahl <emil@kratern.se>
// Encoding: UTF-8
// Distributed under the same terms as the calendar itself.

// For translators: please use UTF-8 if possible.  We strongly believe that
// Unicode is the answer to a real internationalized world.  Also please
// include your contact information in the header, as can be seen above.

// full day names
Calendar._DN = new Array
("s\u00F6ndag",
 "m\u00E5ndag",
 "tisdag",
 "onsdag",
 "torsdag",
 "fredag",
 "l\u00F6rdag",
 "s\u00F6ndag");

// Please note that the following array of short day names (and the same goes
// for short month names, _SMN) isn't absolutely necessary.  We give it here
// for exemplification on how one can customize the short day names, but if
// they are simply the first N letters of the full name you can simply say:
//
//   Calendar._SDN_len = N; // short day name length
//   Calendar._SMN_len = N; // short month name length
//
// If N = 3 then this is not needed either since we assume a value of 3 if not
// present, to be compatible with translation files that were written before
// this feature.

// short day names
Calendar._SDN = new Array
("s\u00F6n",
 "m\u00E5n",
 "tis",
 "ons",
 "tor",
 "fre",
 "l\u00F6r",
 "s\u00F6n");

// First day of the week. "0" means display Sunday first, "1" means display
// Monday first, etc.
Calendar._FD = 0;

// full month names
Calendar._MN = new Array
("januari",
 "februari",
 "mars",
 "april",
 "maj",
 "juni",
 "juli",
 "augusti",
 "september",
 "oktober",
 "november",
 "december");

// short month names
Calendar._SMN = new Array
("jan",
 "feb",
 "mar",
 "apr",
 "maj",
 "jun",
 "jul",
 "aug",
 "sep",
 "okt",
 "nov",
 "dec");

// tooltips
Calendar._TT = {};
Calendar._TT["INFO"] = "Om kalendern";

Calendar._TT["ABOUT"] =
"DHTML Datum/tid-v\u00E4ljare\n" +
"(c) dynarch.com 2002-2005 / Author: Mihai Bazon\n" + // don't translate this this ;-)
"F\u00F6r senaste version g\u00E5 till: http://www.dynarch.com/projects/calendar/\n" +
"Distribueras under GNU LGPL.  Se http://gnu.org/licenses/lgpl.html f\u00F6r detaljer." +
"\n\n" +
"Val av datum:\n" +
"- Anv\u00E4nd knapparna \xab, \xbb f\u00F6r att v\u00E4lja \u00E5r\n" +
"- Anv\u00E4nd knapparna " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + " f\u00F6r att v\u00E4lja m\u00E5nad\n" +
"- H\u00E5ll musknappen nedtryckt p\u00E5 n\u00E5gon av ovanst\u00E5ende knappar f\u00F6r snabbare val.";
Calendar._TT["ABOUT_TIME"] = "\n\n" +
"Val av tid:\n" +
"- Klicka p\u00E5 en del av tiden f\u00F6r att \u00F6ka den delen\n" +
"- eller skift-klicka f\u00F6r att minska den\n" +
"- eller klicka och drag f\u00F6r snabbare val.";

Calendar._TT["PREV_YEAR"] = "F\u00F6reg\u00E5ende \u00E5r (h\u00E5ll f\u00F6r menu)";
Calendar._TT["PREV_MONTH"] = "F\u00F6reg\u00E5ende m\u00E5nad (h\u00E5ll f\u00F6r menu)";
Calendar._TT["GO_TODAY"] = "G\u00E5 till dagens datum";
Calendar._TT["NEXT_MONTH"] = "F\u00F6ljande m\u00E5nad (h\u00E5ll f\u00F6r menu)";
Calendar._TT["NEXT_YEAR"] = "F\u00F6ljande \u00E5r (h\u00E5ll f\u00F6r menu)";
Calendar._TT["SEL_DATE"] = "V\u00E4lj datum";
Calendar._TT["DRAG_TO_MOVE"] = "Drag f\u00F6r att flytta";
Calendar._TT["PART_TODAY"] = " (idag)";
Calendar._TT["MON_FIRST"] = "Visa m\u00E5ndag f\u00F6rst";
Calendar._TT["SUN_FIRST"] = "Visa s\u00F6ndag f\u00F6rst";

// the following is to inform that "%s" is to be the first day of week
// %s will be replaced with the day name.
Calendar._TT["DAY_FIRST"] = "Visa %s f\u00F6rst";

// This may be locale-dependent.  It specifies the week-end days, as an array
// of comma-separated numbers.  The numbers are from 0 to 6: 0 means Sunday, 1
// means Monday, etc.
Calendar._TT["WEEKEND"] = "0";

Calendar._TT["CLOSE"] = "St\u00E4ng";
Calendar._TT["TODAY"] = "Idag";
Calendar._TT["TIME_PART"] = "(Skift-)klicka eller drag f\u00F6r att \u00E4ndra tid";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%Y-%m-%d";
Calendar._TT["TT_DATE_FORMAT"] = "%A %d %b %Y";

Calendar._TT["WK"] = "vecka";
Calendar._TT["TIME"] = "Tid:";

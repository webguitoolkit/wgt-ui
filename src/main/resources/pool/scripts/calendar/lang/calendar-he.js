// ** I18N

// Calendar EN language
// Author: Idan Sofer, <idan@idanso.dyndns.org>
// Encoding: UTF-8
// Distributed under the same terms as the calendar itself.

// For translators: please use UTF-8 if possible.  We strongly believe that
// Unicode is the answer to a real internationalized world.  Also please
// include your contact information in the header, as can be seen above.

// full day names
Calendar._DN = new Array
("\u05E8\u05D0\u05E9\u05D5\u05DF",
 "\u05E9\u05E0\u05D9",
 "\u05E9\u05DC\u05D9\u05E9\u05D9",
 "\u05E8\u05D1\u05D9\u05E2\u05D9",
 "\u05D7\u05DE\u05D9\u05E9\u05D9",
 "\u05E9\u05D9\u05E9\u05D9",
 "\u05E9\u05D1\u05EA",
 "\u05E8\u05D0\u05E9\u05D5\u05DF");

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
("\u05D0",
 "\u05D1",
 "\u05D2",
 "\u05D3",
 "\u05D4",
 "\u05D5",
 "\u05E9",
 "\u05D0");

// full month names
Calendar._MN = new Array
("\u05D9\u05E0\u05D5\u05D0\u05E8",
 "\u05E4\u05D1\u05E8\u05D5\u05D0\u05E8",
 "\u05DE\u05E8\u05E5",
 "\u05D0\u05E4\u05E8\u05D9\u05DC",
 "\u05DE\u05D0\u05D9",
 "\u05D9\u05D5\u05E0\u05D9",
 "\u05D9\u05D5\u05DC\u05D9",
 "\u05D0\u05D5\u05D2\u05D5\u05E1\u05D8",
 "\u05E1\u05E4\u05D8\u05DE\u05D1\u05E8",
 "\u05D0\u05D5\u05E7\u05D8\u05D5\u05D1\u05E8",
 "\u05E0\u05D5\u05D1\u05DE\u05D1\u05E8",
 "\u05D3\u05E6\u05DE\u05D1\u05E8");

// short month names
Calendar._SMN = new Array
("\u05D9\u05E0\u05D0",
 "\u05E4\u05D1\u05E8",
 "\u05DE\u05E8\u05E5",
 "\u05D0\u05E4\u05E8",
 "\u05DE\u05D0\u05D9",
 "\u05D9\u05D5\u05E0",
 "\u05D9\u05D5\u05DC",
 "\u05D0\u05D5\u05D2",
 "\u05E1\u05E4\u05D8",
 "\u05D0\u05D5\u05E7",
 "\u05E0\u05D5\u05D1",
 "\u05D3\u05E6\u05DE");

// tooltips
Calendar._TT = {};
Calendar._TT["INFO"] = "\u05D0\u05D5\u05D3\u05D5\u05EA \u05D4\u05E9\u05E0\u05EA\u05D5\u05DF";

Calendar._TT["ABOUT"] =
"\u05D1\u05D7\u05E8\u05DF \u05EA\u05D0\u05E8\u05D9\u05DA/\u05E9\u05E2\u05D4 DHTML\n" +
"(c) dynarch.com 2002-2005 / Author: Mihai Bazon\n" + // don't translate this this ;-)
"\u05D4\u05D2\u05D9\u05E8\u05E1\u05D0 \u05D4\u05D0\u05D7\u05E8\u05D5\u05E0\u05D4 \u05D6\u05DE\u05D9\u05E0\u05D4 \u05D1: http://www.dynarch.com/projects/calendar/\n" +
"\u05DE\u05D5\u05E4\u05E5 \u05EA\u05D7\u05EA \u05D6\u05D9\u05DB\u05D9\u05D5\u05DF \u05D4 GNU LGPL.  \u05E2\u05D9\u05D9\u05DF \u05D1 http://gnu.org/licenses/lgpl.html \u05DC\u05E4\u05E8\u05D8\u05D9\u05DD \u05E0\u05D5\u05E1\u05E4\u05D9\u05DD." +
"\n\n" +
"\u05D1\u05D7\u05D9\u05E8\u05EA \u05EA\u05D0\u05E8\u05D9\u05DA:\n" +
"- \u05D4\u05E9\u05EA\u05DE\u05E9 \u05D1\u05DB\u05E4\u05EA\u05D5\u05E8\u05D9\u05DD \xab, \xbb \u05DC\u05D1\u05D7\u05D9\u05E8\u05EA \u05E9\u05E0\u05D4\n" +
"- \u05D4\u05E9\u05EA\u05DE\u05E9 \u05D1\u05DB\u05E4\u05EA\u05D5\u05E8\u05D9\u05DD " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + " \u05DC\u05D1\u05D7\u05D9\u05E8\u05EA \u05D7\u05D5\u05D3\u05E9\n" +
"- \u05D4\u05D7\u05D6\u05E7 \u05D4\u05E2\u05DB\u05D1\u05E8 \u05DC\u05D7\u05D5\u05E5 \u05DE\u05E2\u05DC \u05D4\u05DB\u05E4\u05EA\u05D5\u05E8\u05D9\u05DD \u05D4\u05DE\u05D5\u05D6\u05DB\u05E8\u05D9\u05DD \u05DC\u05E2\u05D9\u05DC \u05DC\u05D1\u05D7\u05D9\u05E8\u05D4 \u05DE\u05D4\u05D9\u05E8\u05D4 \u05D9\u05D5\u05EA\u05E8.";
Calendar._TT["ABOUT_TIME"] = "\n\n" +
"\u05D1\u05D7\u05D9\u05E8\u05EA \u05D6\u05DE\u05DF:\n" +
"- \u05DC\u05D7\u05E5 \u05E2\u05DC \u05DB\u05DC \u05D0\u05D7\u05D3 \u05DE\u05D7\u05DC\u05E7\u05D9 \u05D4\u05D6\u05DE\u05DF \u05DB\u05D3\u05D9 \u05DC\u05D4\u05D5\u05E1\u05D9\u05E3\n" +
"- \u05D0\u05D5 shift \u05D1\u05E9\u05D9\u05DC\u05D5\u05D1 \u05E2\u05DD \u05DC\u05D7\u05D9\u05E6\u05D4 \u05DB\u05D3\u05D9 \u05DC\u05D4\u05D7\u05E1\u05D9\u05E8\n" +
"- \u05D0\u05D5 \u05DC\u05D7\u05E5 \u05D5\u05D2\u05E8\u05D5\u05E8 \u05DC\u05E4\u05E2\u05D5\u05DC\u05D4 \u05DE\u05D4\u05D9\u05E8\u05D4 \u05D9\u05D5\u05EA\u05E8.";

Calendar._TT["PREV_YEAR"] = "\u05E9\u05E0\u05D4 \u05E7\u05D5\u05D3\u05DE\u05EA - \u05D4\u05D7\u05D6\u05E7 \u05DC\u05E7\u05D1\u05DC\u05EA \u05EA\u05E4\u05E8\u05D9\u05D8";
Calendar._TT["PREV_MONTH"] = "\u05D7\u05D5\u05D3\u05E9 \u05E7\u05D5\u05D3\u05DD - \u05D4\u05D7\u05D6\u05E7 \u05DC\u05E7\u05D1\u05DC\u05EA \u05EA\u05E4\u05E8\u05D9\u05D8";
Calendar._TT["GO_TODAY"] = "\u05E2\u05D1\u05D5\u05E8 \u05DC\u05D4\u05D9\u05D5\u05DD";
Calendar._TT["NEXT_MONTH"] = "\u05D7\u05D5\u05D3\u05E9 \u05D4\u05D1\u05D0 - \u05D4\u05D7\u05D6\u05E7 \u05DC\u05EA\u05E4\u05E8\u05D9\u05D8";
Calendar._TT["NEXT_YEAR"] = "\u05E9\u05E0\u05D4 \u05D4\u05D1\u05D0\u05D4 - \u05D4\u05D7\u05D6\u05E7 \u05DC\u05EA\u05E4\u05E8\u05D9\u05D8";
Calendar._TT["SEL_DATE"] = "\u05D1\u05D7\u05E8 \u05EA\u05D0\u05E8\u05D9\u05DA";
Calendar._TT["DRAG_TO_MOVE"] = "\u05D2\u05E8\u05D5\u05E8 \u05DC\u05D4\u05D6\u05D6\u05D4";
Calendar._TT["PART_TODAY"] = " )\u05D4\u05D9\u05D5\u05DD(";

// the following is to inform that "%s" is to be the first day of week
// %s will be replaced with the day name.
Calendar._TT["DAY_FIRST"] = "\u05D4\u05E6\u05D2 %s \u05E7\u05D5\u05D3\u05DD";

// This may be locale-dependent.  It specifies the week-end days, as an array
// of comma-separated numbers.  The numbers are from 0 to 6: 0 means Sunday, 1
// means Monday, etc.
Calendar._TT["WEEKEND"] = "6";

Calendar._TT["CLOSE"] = "\u05E1\u05D2\u05D5\u05E8";
Calendar._TT["TODAY"] = "\u05D4\u05D9\u05D5\u05DD";
Calendar._TT["TIME_PART"] = "(\u05E9\u05D9\u05E4\u05D8-)\u05DC\u05D7\u05E5 \u05D5\u05D2\u05E8\u05D5\u05E8 \u05DB\u05D3\u05D9 \u05DC\u05E9\u05E0\u05D5\u05EA \u05E2\u05E8\u05DA";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%Y-%m-%d";
Calendar._TT["TT_DATE_FORMAT"] = "%a, %b %e";

Calendar._TT["WK"] = "wk";
Calendar._TT["TIME"] = "\u05E9\u05E2\u05D4::";

//First day of the week. "0" means display Sunday first, "1" means display
//Monday first, etc.
Calendar._FD = 1;

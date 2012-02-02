// ** I18N

// Calendar LV language
// Author: Juris Valdovskis, <juris@dc.lv>
// Encoding: cp1257
// Distributed under the same terms as the calendar itself.

// For translators: please use UTF-8 if possible.  We strongly believe that
// Unicode is the answer to a real internationalized world.  Also please
// include your contact information in the header, as can be seen above.

// full day names
Calendar._DN = new Array
("Sv\uFFFDtdiena",
 "Pirmdiena",
 "Otrdiena",
 "Tre\uFFFDdiena",
 "Ceturdiena",
 "Piektdiena",
 "Sestdiena",
 "Sv\uFFFDtdiena");

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
("Sv",
 "Pr",
 "Ot",
 "Tr",
 "Ce",
 "Pk",
 "Se",
 "Sv");

// full month names
Calendar._MN = new Array
("Janv\uFFFDris",
 "Febru\uFFFDris",
 "Marts",
 "Apr\uFFFDlis",
 "Maijs",
 "J\uFFFDnijs",
 "J\uFFFDlijs",
 "Augusts",
 "Septembris",
 "Oktobris",
 "Novembris",
 "Decembris");

// short month names
Calendar._SMN = new Array
("Jan",
 "Feb",
 "Mar",
 "Apr",
 "Mai",
 "J\uFFFDn",
 "J\uFFFDl",
 "Aug",
 "Sep",
 "Okt",
 "Nov",
 "Dec");

//First day of the week. "0" means display Sunday first, "1" means display
//Monday first, etc.
Calendar._FD = 0;

// tooltips
Calendar._TT = {};
Calendar._TT["INFO"] = "Par kalend\uFFFDru";

Calendar._TT["ABOUT"] =
"DHTML Date/Time Selector\n" +
"(c) dynarch.com 2002-2005 / Author: Mihai Bazon\n" + // don't translate this this ;-)
"For latest version visit: http://www.dynarch.com/projects/calendar/\n" +
"Distributed under GNU LGPL.  See http://gnu.org/licenses/lgpl.html for details." +
"\n\n" +
"Datuma izv\uFFFDle:\n" +
"- Izmanto \xab, \xbb pogas, lai izv\uFFFDl\uFFFDtos gadu\n" +
"- Izmanto " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + "pogas, lai izv\uFFFDl\uFFFDtos m\uFFFDnesi\n" +
"- Turi nospiestu peles pogu uz jebkuru no augst\uFFFDk min\uFFFDtaj\uFFFDm pog\uFFFDm, lai pa\uFFFDtrin\uFFFDtu izv\uFFFDli.";
Calendar._TT["ABOUT_TIME"] = "\n\n" +
"Laika izv\uFFFDle:\n" +
"- Uzklik\uFFFD\uFFFDini uz jebkuru no laika da\uFFFD\uFFFDm, lai palielin\uFFFDtu to\n" +
"- vai Shift-klik\uFFFD\uFFFDis, lai samazin\uFFFDtu to\n" +
"- vai noklik\uFFFD\uFFFDini un velc uz attiec\uFFFDgo virzienu lai main\uFFFDtu \uFFFDtr\uFFFDk.";

Calendar._TT["PREV_YEAR"] = "Iepr. gads (turi izv\uFFFDlnei)";
Calendar._TT["PREV_MONTH"] = "Iepr. m\uFFFDnesis (turi izv\uFFFDlnei)";
Calendar._TT["GO_TODAY"] = "\uFFFDodien";
Calendar._TT["NEXT_MONTH"] = "N\uFFFDko\uFFFDais m\uFFFDnesis (turi izv\uFFFDlnei)";
Calendar._TT["NEXT_YEAR"] = "N\uFFFDko\uFFFDais gads (turi izv\uFFFDlnei)";
Calendar._TT["SEL_DATE"] = "Izv\uFFFDlies datumu";
Calendar._TT["DRAG_TO_MOVE"] = "Velc, lai p\uFFFDrvietotu";
Calendar._TT["PART_TODAY"] = " (\uFFFDodien)";

// the following is to inform that "%s" is to be the first day of week
// %s will be replaced with the day name.
Calendar._TT["DAY_FIRST"] = "Att\uFFFDlot %s k\uFFFD pirmo";

// This may be locale-dependent.  It specifies the week-end days, as an array
// of comma-separated numbers.  The numbers are from 0 to 6: 0 means Sunday, 1
// means Monday, etc.
Calendar._TT["WEEKEND"] = "1,7";

Calendar._TT["CLOSE"] = "Aizv\uFFFDrt";
Calendar._TT["TODAY"] = "\uFFFDodien";
Calendar._TT["TIME_PART"] = "(Shift-)Klik\uFFFD\uFFFDis vai p\uFFFDrvieto, lai main\uFFFDtu";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%d-%m-%Y";
Calendar._TT["TT_DATE_FORMAT"] = "%a, %e %b";

Calendar._TT["WK"] = "wk";
Calendar._TT["TIME"] = "Laiks:";

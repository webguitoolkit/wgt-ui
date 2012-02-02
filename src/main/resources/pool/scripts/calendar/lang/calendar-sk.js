// ** I18N

// Calendar SK language
// Author: Peter Valach (pvalach@gmx.net)
// Encoding: utf-8
// Last update: 2003/10/29
// Distributed under the same terms as the calendar itself.

// full day names
Calendar._DN = new Array
("Nede\u00C4\u013Ea",
 "Pondelok",
 "Utorok",
 "Streda",
 "\u0139\u00A0tvrtok",
 "Piatok",
 "Sobota",
 "Nede\u00C4\u013Ea");

// short day names
Calendar._SDN = new Array
("Ned",
 "Pon",
 "Uto",
 "Str",
 "\u0139\u00A0tv",
 "Pia",
 "Sob",
 "Ned");

// full month names
Calendar._MN = new Array
("Janu\u0102\u02C7r",
 "Febru\u0102\u02C7r",
 "Marec",
 "Apr\u0102\u00ADl",
 "M\u0102\u02C7j",
 "J\u0102\u015Fn",
 "J\u0102\u015Fl",
 "August",
 "September",
 "Okt\u0102\u0142ber",
 "November",
 "December");

// short month names
Calendar._SMN = new Array
("Jan",
 "Feb",
 "Mar",
 "Apr",
 "M\u0102\u02C7j",
 "J\u0102\u015Fn",
 "J\u0102\u015Fl",
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
Calendar._TT["INFO"] = "O kalend\u0102\u02C7ri";

Calendar._TT["ABOUT"] =
"DHTML Date/Time Selector\n" +
"(c) dynarch.com 2002-2005 / Author: Mihai Bazon\n" +
"Posledn\u0102\u015F verziu n\u0102\u02C7jdete na: http://www.dynarch.com/projects/calendar/\n" +
"Distribuovan\u0102\u00A9 pod GNU LGPL.  Vi\u00C4\u0179 http://gnu.org/licenses/lgpl.html pre detaily." +
"\n\n" +
"V\u0102\u02DDber d\u0102\u02C7tumu:\n" +
"- Pou\u0139\u013Eite tla\u00C4\u0164idl\u0102\u02C7 \xab, \xbb pre v\u0102\u02DDber roku\n" +
"- Pou\u0139\u013Eite tla\u00C4\u0164idl\u0102\u02C7 " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + " pre v\u0102\u02DDber mesiaca\n" +
"- Ak ktor\u0102\u00A9ko\u00C4\u013Evek z t\u0102\u02DDchto tla\u00C4\u0164idiel podr\u0139\u013E\u0102\u00ADte dlh\u0139\u02C7ie, zobraz\u0102\u00AD sa r\u0102\u02DDchly v\u0102\u02DDber.";
Calendar._TT["ABOUT_TIME"] = "\n\n" +
"V\u0102\u02DDber \u00C4\u0164asu:\n" +
"- Kliknutie na niektor\u0102\u015F polo\u0139\u013Eku \u00C4\u0164asu ju zv\u0102\u02DD\u0139\u02C7i\n" +
"- Shift-klik ju zn\u0102\u00AD\u0139\u013Ei\n" +
"- Ak podr\u0139\u013E\u0102\u00ADte tla\u00C4\u0164\u0102\u00ADtko stla\u00C4\u0164en\u0102\u00A9, pos\u0102\u015Fvan\u0102\u00ADm men\u0102\u00ADte hodnotu.";

Calendar._TT["PREV_YEAR"] = "Predo\u0139\u02C7l\u0102\u02DD rok (podr\u0139\u013Ete pre menu)";
Calendar._TT["PREV_MONTH"] = "Predo\u0139\u02C7l\u0102\u02DD mesiac (podr\u0139\u013Ete pre menu)";
Calendar._TT["GO_TODAY"] = "Prejs\u0139\u0104 na dne\u0139\u02C7ok";
Calendar._TT["NEXT_MONTH"] = "Nasl. mesiac (podr\u0139\u013Ete pre menu)";
Calendar._TT["NEXT_YEAR"] = "Nasl. rok (podr\u0139\u013Ete pre menu)";
Calendar._TT["SEL_DATE"] = "Zvo\u00C4\u013Ete d\u0102\u02C7tum";
Calendar._TT["DRAG_TO_MOVE"] = "Podr\u0139\u013Ean\u0102\u00ADm tla\u00C4\u0164\u0102\u00ADtka zmen\u0102\u00ADte polohu";
Calendar._TT["PART_TODAY"] = " (dnes)";
Calendar._TT["MON_FIRST"] = "Zobrazi\u0139\u0104 pondelok ako prv\u0102\u02DD";
Calendar._TT["SUN_FIRST"] = "Zobrazi\u0139\u0104 nede\u00C4\u013Eu ako prv\u0102\u015F";
Calendar._TT["CLOSE"] = "Zavrie\u0139\u0104";
Calendar._TT["TODAY"] = "Dnes";
Calendar._TT["TIME_PART"] = "(Shift-)klik/\u0139\u0104ahanie zmen\u0102\u00AD hodnotu";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "$d. %m. %Y";
Calendar._TT["TT_DATE_FORMAT"] = "%a, %e. %b";

Calendar._TT["WK"] = "t\u0102\u02DD\u0139\u013E";
Calendar._TT["WEEKEND"] = "0,6";
Calendar._TT["DAY_FIRST"] = "Display %s first";

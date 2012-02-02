// ** I18N

// Calendar FI language (Finnish, Suomi)
// Author: Jarno K\u00E4yhk\u00F6, <gambler@phnet.fi>
// Encoding: UTF-8
// Distributed under the same terms as the calendar itself.

// full day names
Calendar._DN = new Array
("Sunnuntai",
 "Maanantai",
 "Tiistai",
 "Keskiviikko",
 "Torstai",
 "Perjantai",
 "Lauantai",
 "Sunnuntai");

// short day names
Calendar._SDN = new Array
("Su",
 "Ma",
 "Ti",
 "Ke",
 "To",
 "Pe",
 "La",
 "Su");

// full month names
Calendar._MN = new Array
("Tammikuu",
 "Helmikuu",
 "Maaliskuu",
 "Huhtikuu",
 "Toukokuu",
 "Kes\u00E4kuu",
 "Hein\u00E4kuu",
 "Elokuu",
 "Syyskuu",
 "Lokakuu",
 "Marraskuu",
 "Joulukuu");

// short month names
Calendar._SMN = new Array
("Tam",
 "Hel",
 "Maa",
 "Huh",
 "Tou",
 "Kes",
 "Hei",
 "Elo",
 "Syy",
 "Lok",
 "Mar",
 "Jou");

// tooltips
Calendar._TT = {};
Calendar._TT["INFO"] = "Tietoja kalenterista";

Calendar._TT["ABOUT"] =
"DHTML Date/Time Selector\n" +
"(c) dynarch.com 2002-2005 / Author: Mihai Bazon\n" + // don't translate this this ;-)
"Uusin versio osoitteessa: http://www.dynarch.com/projects/calendar/\n" +
"Julkaistu GNU LGPL lisenssin alaisuudessa. Lis\u00E4tietoja osoitteessa http://gnu.org/licenses/lgpl.html" +
"\n\n" +
"P\u00E4iv\u00E4m\u00E4\u00E4r\u00E4 valinta:\n" +
"- K\u00E4yt\u00E4 \xab, \xbb painikkeita valitaksesi vuosi\n" +
"- K\u00E4yt\u00E4 " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + " painikkeita valitaksesi kuukausi\n" +
"- Pit\u00E4m\u00E4ll\u00E4 hiiren painiketta mink\u00E4 tahansa yll\u00E4 olevan painikkeen kohdalla, saat n\u00E4kyviin valikon nopeampaan siirtymiseen.";
Calendar._TT["ABOUT_TIME"] = "\n\n" +
"Ajan valinta:\n" +
"- Klikkaa kellonajan numeroita lis\u00E4t\u00E4ksesi aikaa\n" +
"- tai pit\u00E4m\u00E4ll\u00E4 Shift-n\u00E4pp\u00E4int\u00E4 pohjassa saat aikaa taaksep\u00E4in\n" +
"- tai klikkaa ja pid\u00E4 hiiren painike pohjassa sek\u00E4 liikuta hiirt\u00E4 muuttaaksesi aikaa nopeasti eteen- ja taaksep\u00E4in.";

Calendar._TT["PREV_YEAR"] = "Edell. vuosi (paina hetki, n\u00E4et valikon)";
Calendar._TT["PREV_MONTH"] = "Edell. kuukausi (paina hetki, n\u00E4et valikon)";
Calendar._TT["GO_TODAY"] = "Siirry t\u00E4h\u00E4n p\u00E4iv\u00E4\u00E4n";
Calendar._TT["NEXT_MONTH"] = "Seur. kuukausi (paina hetki, n\u00E4et valikon)";
Calendar._TT["NEXT_YEAR"] = "Seur. vuosi (paina hetki, n\u00E4et valikon)";
Calendar._TT["SEL_DATE"] = "Valitse p\u00E4iv\u00E4m\u00E4\u00E4r\u00E4";
Calendar._TT["DRAG_TO_MOVE"] = "Siirr\u00E4 kalenterin paikkaa";
Calendar._TT["PART_TODAY"] = " (t\u00E4n\u00E4\u00E4n)";
Calendar._TT["MON_FIRST"] = "N\u00E4yt\u00E4 maanantai ensimm\u00E4isen\u00E4";
Calendar._TT["SUN_FIRST"] = "N\u00E4yt\u00E4 sunnuntai ensimm\u00E4isen\u00E4";
Calendar._TT["CLOSE"] = "Sulje";
Calendar._TT["TODAY"] = "T\u00E4n\u00E4\u00E4n";
Calendar._TT["TIME_PART"] = "(Shift-) Klikkaa tai liikuta muuttaaksesi aikaa";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%d.%m.%Y";
Calendar._TT["TT_DATE_FORMAT"] = "%d.%m.%Y";

Calendar._TT["WK"] = "Vko";
Calendar._TT["WEEKEND"] = "0,6";
//First day of the week. "0" means display Sunday first, "1" means display
//Monday first, etc.
Calendar._FD = 1;
Calendar._TT["DAY_FIRST"] = "Display %s first";
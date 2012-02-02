// ** I18N

// Calendar LT language
// Author: Martynas Majeris, <martynas@solmetra.lt>
// Encoding: Windows-1257
// Distributed under the same terms as the calendar itself.

// For translators: please use UTF-8 if possible.  We strongly believe that
// Unicode is the answer to a real internationalized world.  Also please
// include your contact information in the header, as can be seen above.

// full day names
Calendar._DN = new Array
("Sekmadienis",
 "Pirmadienis",
 "Antradienis",
 "Tre\uFFFDiadienis",
 "Ketvirtadienis",
 "Pentadienis",
 "\uFFFDe\uFFFDtadienis",
 "Sekmadienis");

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
("Sek",
 "Pir",
 "Ant",
 "Tre",
 "Ket",
 "Pen",
 "\uFFFDe\uFFFD",
 "Sek");

// full month names
Calendar._MN = new Array
("Sausis",
 "Vasaris",
 "Kovas",
 "Balandis",
 "Gegu\uFFFD\uFFFD",
 "Bir\uFFFDelis",
 "Liepa",
 "Rugpj\uFFFDtis",
 "Rugs\uFFFDjis",
 "Spalis",
 "Lapkritis",
 "Gruodis");

// short month names
Calendar._SMN = new Array
("Sau",
 "Vas",
 "Kov",
 "Bal",
 "Geg",
 "Bir",
 "Lie",
 "Rgp",
 "Rgs",
 "Spa",
 "Lap",
 "Gru");

//First day of the week. "0" means display Sunday first, "1" means display
//Monday first, etc.
Calendar._FD = 0;

// tooltips
Calendar._TT = {};
Calendar._TT["INFO"] = "Apie kalendori\uFFFD";

Calendar._TT["ABOUT"] =
"DHTML Date/Time Selector\n" +
"(c) dynarch.com 2002-2005 / Author: Mihai Bazon\n" + // don't translate this this ;-)
"Naujausi\uFFFD versij\uFFFD rasite: http://www.dynarch.com/projects/calendar/\n" +
"Platinamas pagal GNU LGPL licencij\uFFFD. Aplankykite http://gnu.org/licenses/lgpl.html" +
"\n\n" +
"Datos pasirinkimas:\n" +
"- Met\uFFFD pasirinkimas: \xab, \xbb\n" +
"- M\uFFFDnesio pasirinkimas: " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + "\n" +
"- Nuspauskite ir laikykite pel\uFFFDs klavi\uFFFD\uFFFD greitesniam pasirinkimui.";
Calendar._TT["ABOUT_TIME"] = "\n\n" +
"Laiko pasirinkimas:\n" +
"- Spustelkite ant valand\uFFFD arba minu\uFFFDi\uFFFD - skai\uFFFDus padid\uFFFDs vienetu.\n" +
"- Jei spausite kartu su Shift, skai\uFFFDius suma\uFFFD\uFFFDs.\n" +
"- Greitam pasirinkimui spustelkite ir pajudinkite pel\uFFFD.";

Calendar._TT["PREV_YEAR"] = "Ankstesni metai (laikykite, jei norite meniu)";
Calendar._TT["PREV_MONTH"] = "Ankstesnis m\uFFFDnuo (laikykite, jei norite meniu)";
Calendar._TT["GO_TODAY"] = "Pasirinkti \uFFFDiandien\uFFFD";
Calendar._TT["NEXT_MONTH"] = "Kitas m\uFFFDnuo (laikykite, jei norite meniu)";
Calendar._TT["NEXT_YEAR"] = "Kiti metai (laikykite, jei norite meniu)";
Calendar._TT["SEL_DATE"] = "Pasirinkite dat\uFFFD";
Calendar._TT["DRAG_TO_MOVE"] = "Tempkite";
Calendar._TT["PART_TODAY"] = " (\uFFFDiandien)";
Calendar._TT["MON_FIRST"] = "Pirma savait\uFFFDs diena - pirmadienis";
Calendar._TT["SUN_FIRST"] = "Pirma savait\uFFFDs diena - sekmadienis";
Calendar._TT["CLOSE"] = "U\uFFFDdaryti";
Calendar._TT["TODAY"] = "\uFFFDiandien";
Calendar._TT["TIME_PART"] = "Spustelkite arba tempkite jei norite pakeisti";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%Y-%m-%d";
Calendar._TT["TT_DATE_FORMAT"] = "%A, %Y-%m-%d";

Calendar._TT["WK"] = "sav";
Calendar._TT["WEEKEND"] = "0,6";
Calendar._TT["DAY_FIRST"] = "Display %s first";

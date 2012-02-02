// ** I18N

// Calendar SR language Serbian (Latin) 
// Author: Mihai Bazon, <mishoo@infoiasi.ro>
// Translation: Nenad Nikolic <shone@europe.com>
// Encoding: UTF-8
// Feel free to use / redistribute under the GNU LGPL.

// full day names
Calendar._DN = new Array
("Nedelja",
 "Ponedeljak",
 "Utorak",
 "Sreda",
 "\u010Cetvrtak",
 "Petak",
 "Subota",
 "Nedelja");

// short day names
Calendar._SDN = new Array
("Ned",
 "Pon",
 "Uto",
 "Sre",
 "\u010Cet",
 "Pet",
 "Sub",
 "Ned");

// full month names
Calendar._MN = new Array
("Januar",
 "Februar",
 "Mart",
 "April",
 "Maj",
 "Jun",
 "Jul",
 "Avgust",
 "Septembar",
 "Oktobar",
 "Novembar",
 "Decembar");

// short month names
Calendar._SMN = new Array
("Jan",
 "Feb",
 "Mar",
 "Apr",
 "Maj",
 "Jun",
 "Jul",
 "Avg",
 "Sep",
 "Okt",
 "Nov",
 "Dec");

//First day of the week. "0" means display Sunday first, "1" means display
//Monday first, etc.
Calendar._FD = 0;

// tooltips
Calendar._TT = {};
Calendar._TT["INFO"] = "O kalendaru";

Calendar._TT["ABOUT"] =
"DHTML Kalendar\n" +
"(c) dynarch.com 2002-2003\n" + // don't translate this this ;-)
"Najnovija verzija kontrole nalazi se http://dynarch.com/mishoo/calendar.epl\n" +
"Distribuirano po GNU LGPL licencom.  Za detalje pogledaj http://gnu.org/licenses/lgpl.html." +
"\n\n" +
"Izbor datuma:\n" +
"- Koristi dugmi\u0107e \xab, \xbb za izbor godine\n" +
"- Koristi dugmi\u0107e " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + " za izbor meseca\n" +
"- Za br\u017Ei izbor, dr\u017Eati pritisnut taster mi\u0161a iznad bilo kog od pomenutih dugmi\u0107a";
Calendar._TT["ABOUT_TIME"] = "\n\n" +
"Izbor vremena:\n" +
"- Kliktaj na sate ili minute pove\u0107ava njihove vrednosti\n" +
"- Shift-klik smanjuje njihove vrednosti\n" +
"- klikni i vuci za br\u017Ei izbor.";

Calendar._TT["PREV_YEAR"] = "Prethodna godina (dugi pritisak za meni)";
Calendar._TT["PREV_MONTH"] = "Prethodni mesec (dugi pritisak za meni)";
Calendar._TT["GO_TODAY"] = "Idi na dana\u0161nji dan";
Calendar._TT["NEXT_MONTH"] = "Slede\u0107i mesec (dugi pritisak za meni)";
Calendar._TT["NEXT_YEAR"] = "Slede\u0107a godina (dugi pritisak za meni)";
Calendar._TT["SEL_DATE"] = "Izaberi datum";
Calendar._TT["DRAG_TO_MOVE"] = "Pritisni i vuci za promenu pozicije";
Calendar._TT["PART_TODAY"] = " (danas)";

// Choose first day of week.
Calendar._TT["DAY_FIRST"] = "%s kao prvi dan u nedelji"; 
Calendar._TT["MON_FIRST"] = "Prika\u017Ei ponedeljak kao prvi dan nedelje";
Calendar._TT["SUN_FIRST"] = "Prika\u017Ei nedelju kao prvi dan nedelje";

// Weekend is usual: Sunday (0) and Saturday (6).
Calendar._TT["WEEKEND"] = "0,6";

Calendar._TT["CLOSE"] = "Zatvori";
Calendar._TT["TODAY"] = "Danas";
Calendar._TT["TIME_PART"] = "(Shift-)klikni i vuci za promenu vrednosti";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%d-%m-%Y";
Calendar._TT["TT_DATE_FORMAT"] = "%A, %B %e";

Calendar._TT["WK"] = "wk";
Calendar._TT["TIME"] = "Time:";

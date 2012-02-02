/* 
calendar-cs-win.js
language: Czech
encoding: windows-1250
author: Lubos Jerabek (xnet@seznam.cz)
        Jan Uhlir (espinosa@centrum.cz)
*/

// ** I18N
Calendar._DN  = new Array('Ned\u011Ble','Pond\u011Bl\u00ED','\u00DAter\u00FD','St\u0159eda','\u010Ctvrtek','P\u00E1tek','Sobota','Ned\u011Ble');
Calendar._SDN = new Array('Ne','Po','\u00DAt','St','\u010Ct','P\u00E1','So','Ne');
Calendar._MN  = new Array('Leden','\u00DAnor','B\u0159ezen','Duben','Kv\u011Bten','\u010Cerven','\u010Cervenec','Srpen','Z\u00E1\u0159\u00ED','\u0158\u00EDjen','Listopad','Prosinec');
Calendar._SMN = new Array('Led','\u00DAno','B\u0159e','Dub','Kv\u011B','\u010Crv','\u010Cvc','Srp','Z\u00E1\u0159','\u0158\u00EDj','Lis','Pro');

// First day of the week. "0" means display Sunday first, "1" means display
// Monday first, etc.
Calendar._FD = 0;

// tooltips
Calendar._TT = {};
Calendar._TT["INFO"] = "O komponent\u011B kalend\u00E1\u0159";
Calendar._TT["TOGGLE"] = "Zm\u011Bna prvn\u00EDho dne v t\u00FDdnu";
Calendar._TT["PREV_YEAR"] = "P\u0159edchoz\u00ED rok (p\u0159idr\u017E pro menu)";
Calendar._TT["PREV_MONTH"] = "P\u0159edchoz\u00ED m\u011Bs\u00EDc (p\u0159idr\u017E pro menu)";
Calendar._TT["GO_TODAY"] = "Dne\u0161n\u00ED datum";
Calendar._TT["NEXT_MONTH"] = "Dal\u0161\u00ED m\u011Bs\u00EDc (p\u0159idr\u017E pro menu)";
Calendar._TT["NEXT_YEAR"] = "Dal\u0161\u00ED rok (p\u0159idr\u017E pro menu)";
Calendar._TT["SEL_DATE"] = "Vyber datum";
Calendar._TT["DRAG_TO_MOVE"] = "Chy\u0165 a t\u00E1hni, pro p\u0159esun";
Calendar._TT["PART_TODAY"] = " (dnes)";
Calendar._TT["MON_FIRST"] = "Uka\u017E jako prvn\u00ED Pond\u011Bl\u00ED";
//Calendar._TT["SUN_FIRST"] = "Uka\u017E jako prvn\u00ED Ned\u011Bli";

Calendar._TT["ABOUT"] =
"DHTML Kalend\u00E1\u0159\n" +
"(c) dynarch.com 2002-2005 / Autor: Mihai Bazon\n" + // don't translate this this ;-)
"Aktu\u00E1ln\u00ED verzi najdete na: http://www.dynarch.com/projects/calendar/\n" +
"Distribuov\u00E1no pod licenc\u00ED GNU LGPL.  Viz. http://gnu.org/licenses/lgpl.html" +
"\n\n" +
"V\u00FDb\u011Br datumu:\n" +
"- Pou\u017Eijte \xab, \xbb tla\u010D\u00EDtka k v\u00FDb\u011Bru roku\n" +
"- Pou\u017Eijte tla\u010D\u00EDtka " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + " k v\u00FDb\u011Bru m\u011Bs\u00EDce\n" +
"- Podr\u017Ete tla\u010D\u00EDtko my\u0161i na jak\u00E9mkoliv z t\u011Bch tla\u010D\u00EDtek pro rychlej\u0161\u00ED v\u00FDb\u011Br.";

Calendar._TT["ABOUT_TIME"] = "\n\n" +
"V\u00FDb\u011Br \u010Dasu:\n" +
"- Klikn\u011Bte na jakoukoliv z \u010D\u00E1st\u00ED v\u00FDb\u011Bru \u010Dasu pro zv\u00FD\u0161en\u00ED.\n" +
"- nebo Shift-click pro sn\u00ED\u017Een\u00ED\n" +
"- nebo klikn\u011Bte a t\u00E1hn\u011Bte pro rychlej\u0161\u00ED v\u00FDb\u011Br.";

// the following is to inform that "%s" is to be the first day of week
// %s will be replaced with the day name.
Calendar._TT["DAY_FIRST"] = "Zobraz %s prvn\u00ED";

// This may be locale-dependent.  It specifies the week-end days, as an array
// of comma-separated numbers.  The numbers are from 0 to 6: 0 means Sunday, 1
// means Monday, etc.
Calendar._TT["WEEKEND"] = "0,6";

Calendar._TT["CLOSE"] = "Zav\u0159\u00EDt";
Calendar._TT["TODAY"] = "Dnes";
Calendar._TT["TIME_PART"] = "(Shift-)Klikni nebo t\u00E1hni pro zm\u011Bnu hodnoty";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%d.%m.%y";
Calendar._TT["TT_DATE_FORMAT"] = "%a, %b %e";

Calendar._TT["WK"] = "wk";
Calendar._TT["TIME"] = "\u010Cas:";

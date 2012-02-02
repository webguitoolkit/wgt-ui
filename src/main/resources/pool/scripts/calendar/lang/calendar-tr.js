//////////////////////////////////////////////////////////////////////////////////////////////
//\u0009Turkish Translation by Nuri AKMAN
//\u0009Location: Ankara/TURKEY
//\u0009e-mail\u0009: nuriakman@hotmail.com
//\u0009Date\u0009: April, 9 2003
//
//\u0009Note: if Turkish Characters does not shown on you screen
//\u0009\u0009  please include falowing line your html code:
//
//\u0009\u0009  <meta http-equiv="Content-Type" content="text/html; charset=windows-1254">
//
//////////////////////////////////////////////////////////////////////////////////////////////

// ** I18N
Calendar._DN = new Array
("Pazar",
 "Pazartesi",
 "Sal\uFFFD",
 "\uFFFDar\uFFFDamba",
 "Per\uFFFDembe",
 "Cuma",
 "Cumartesi",
 "Pazar");
Calendar._MN = new Array
("Ocak",
 "\uFFFDubat",
 "Mart",
 "Nisan",
 "May\uFFFDs",
 "Haziran",
 "Temmuz",
 "A\uFFFDustos",
 "Eyl\uFFFDl",
 "Ekim",
 "Kas\uFFFDm",
 "Aral\uFFFDk");

//First day of the week. "0" means display Sunday first, "1" means display
//Monday first, etc.
Calendar._FD = 0;

// tooltips
Calendar._TT = {};
Calendar._TT["TOGGLE"] = "Haftan\uFFFDn ilk g\uFFFDn\uFFFDn\uFFFD kayd\uFFFDr";
Calendar._TT["PREV_YEAR"] = "\uFFFDnceki Y\uFFFDl (Men\uFFFD i\uFFFDin bas\uFFFDl\uFFFD tutunuz)";
Calendar._TT["PREV_MONTH"] = "\uFFFDnceki Ay (Men\uFFFD i\uFFFDin bas\uFFFDl\uFFFD tutunuz)";
Calendar._TT["GO_TODAY"] = "Bug\uFFFDn'e git";
Calendar._TT["NEXT_MONTH"] = "Sonraki Ay (Men\uFFFD i\uFFFDin bas\uFFFDl\uFFFD tutunuz)";
Calendar._TT["NEXT_YEAR"] = "Sonraki Y\uFFFDl (Men\uFFFD i\uFFFDin bas\uFFFDl\uFFFD tutunuz)";
Calendar._TT["SEL_DATE"] = "Tarih se\uFFFDiniz";
Calendar._TT["DRAG_TO_MOVE"] = "Ta\uFFFD\uFFFDmak i\uFFFDin s\uFFFDr\uFFFDkleyiniz";
Calendar._TT["PART_TODAY"] = " (bug\uFFFDn)";
Calendar._TT["MON_FIRST"] = "Takvim Pazartesi g\uFFFDn\uFFFDnden ba\uFFFDlas\uFFFDn";
Calendar._TT["SUN_FIRST"] = "Takvim Pazar g\uFFFDn\uFFFDnden ba\uFFFDlas\uFFFDn";
Calendar._TT["CLOSE"] = "Kapat";
Calendar._TT["TODAY"] = "Bug\uFFFDn";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%d.%m.%Y";
Calendar._TT["TT_DATE_FORMAT"] = "%a, %b %e";

Calendar._TT["WK"] = "Hafta";

Calendar._TT["WEEKEND"] = "0,6";
Calendar._TT["DAY_FIRST"] = "Display %s first";

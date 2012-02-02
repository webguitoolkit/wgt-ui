// ** I18N

// Calendar HU language
// Author: ???
// Modifier: KARASZI Istvan, <jscalendar@spam.raszi.hu>
// Encoding: any
// Distributed under the same terms as the calendar itself.

// For translators: please use UTF-8 if possible.  We strongly believe that
// Unicode is the answer to a real internationalized world.  Also please
// include your contact information in the header, as can be seen above.

// full day names
Calendar._DN = new Array
("Vas\uFFFDrnap",
 "H\uFFFDtf\uFFFD",
 "Kedd",
 "Szerda",
 "Cs\uFFFDt\uFFFDrt\uFFFDk",
 "P\uFFFDntek",
 "Szombat",
 "Vas\uFFFDrnap");

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
("v",
 "h",
 "k",
 "sze",
 "cs",
 "p",
 "szo",
 "v");

// full month names
Calendar._MN = new Array
("janu\uFFFDr",
 "febru\uFFFDr",
 "m\uFFFDrcius",
 "\uFFFDprilis",
 "m\uFFFDjus",
 "j\uFFFDnius",
 "j\uFFFDlius",
 "augusztus",
 "szeptember",
 "okt\uFFFDber",
 "november",
 "december");

// short month names
Calendar._SMN = new Array
("jan",
 "feb",
 "m\uFFFDr",
 "\uFFFDpr",
 "m\uFFFDj",
 "j\uFFFDn",
 "j\uFFFDl",
 "aug",
 "sze",
 "okt",
 "nov",
 "dec");

// tooltips
Calendar._TT = {};
Calendar._TT["INFO"] = "A kalend\uFFFDriumr\uFFFDl";

Calendar._TT["ABOUT"] =
"DHTML d\uFFFDtum/id\uFFFD kiv\uFFFDlaszt\uFFFD\n" +
"(c) dynarch.com 2002-2005 / Author: Mihai Bazon\n" + // don't translate this this ;-)
"a legfrissebb verzi\uFFFD megtal\uFFFDlhat\uFFFD: http://www.dynarch.com/projects/calendar/\n" +
"GNU LGPL alatt terjesztve.  L\uFFFDsd a http://gnu.org/licenses/lgpl.html oldalt a r\uFFFDszletekhez." +
"\n\n" +
"D\uFFFDtum v\uFFFDlaszt\uFFFDs:\n" +
"- haszn\uFFFDlja a \xab, \xbb gombokat az \uFFFDv kiv\uFFFDlaszt\uFFFDs\uFFFDhoz\n" +
"- haszn\uFFFDlja a " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + " gombokat a h\uFFFDnap kiv\uFFFDlaszt\uFFFDs\uFFFDhoz\n" +
"- tartsa lenyomva az eg\uFFFDrgombot a gyors v\uFFFDlaszt\uFFFDshoz.";
Calendar._TT["ABOUT_TIME"] = "\n\n" +
"Id\uFFFD v\uFFFDlaszt\uFFFDs:\n" +
"- kattintva n\uFFFDvelheti az id\uFFFDt\n" +
"- shift-tel kattintva cs\uFFFDkkentheti\n" +
"- lenyomva tartva \uFFFDs h\uFFFDzva gyorsabban kiv\uFFFDlaszthatja.";

Calendar._TT["PREV_YEAR"] = "El\uFFFDz\uFFFD \uFFFDv (tartsa nyomva a men\uFFFDh\uFFFDz)";
Calendar._TT["PREV_MONTH"] = "El\uFFFDz\uFFFD h\uFFFDnap (tartsa nyomva a men\uFFFDh\uFFFDz)";
Calendar._TT["GO_TODAY"] = "Mai napra ugr\uFFFDs";
Calendar._TT["NEXT_MONTH"] = "K\uFFFDv. h\uFFFDnap (tartsa nyomva a men\uFFFDh\uFFFDz)";
Calendar._TT["NEXT_YEAR"] = "K\uFFFDv. \uFFFDv (tartsa nyomva a men\uFFFDh\uFFFDz)";
Calendar._TT["SEL_DATE"] = "V\uFFFDlasszon d\uFFFDtumot";
Calendar._TT["DRAG_TO_MOVE"] = "H\uFFFDzza a mozgat\uFFFDshoz";
Calendar._TT["PART_TODAY"] = " (ma)";

// the following is to inform that "%s" is to be the first day of week
// %s will be replaced with the day name.
Calendar._TT["DAY_FIRST"] = "%s legyen a h\uFFFDt els\uFFFD napja";

// This may be locale-dependent.  It specifies the week-end days, as an array
// of comma-separated numbers.  The numbers are from 0 to 6: 0 means Sunday, 1
// means Monday, etc.
Calendar._TT["WEEKEND"] = "0,6";

Calendar._TT["CLOSE"] = "Bez\uFFFDr";
Calendar._TT["TODAY"] = "Ma";
Calendar._TT["TIME_PART"] = "(Shift-)Klikk vagy h\uFFFDz\uFFFDs az \uFFFDrt\uFFFDk v\uFFFDltoztat\uFFFDs\uFFFDhoz";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%Y.%m.%d.";
Calendar._TT["TT_DATE_FORMAT"] = "%b %e, %a";

Calendar._TT["WK"] = "h\uFFFDt";
Calendar._TT["TIME"] = "id\uFFFD:";

//First day of the week. "0" means display Sunday first, "1" means display
//Monday first, etc.
Calendar._FD = 1;
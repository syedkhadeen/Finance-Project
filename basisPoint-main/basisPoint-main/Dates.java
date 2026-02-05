

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

public class Dates {
    final int LESSTHAN = -1;
    final int GREATERTHAN = 1;
    final int EQUAL = 0;
    public int day;
    public int month;
    public int year;
    public long dtCode;
    public int daysFrom1900; // days from 1/1/1900
    Error error;
    int weekend1;
    int weekend2;
    public boolean isNull;
    String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    int[] daysOfWeekNum = {0, 1, 2, 3, 4, 5, 6};
    int[] cumDaysInYearNonLeap = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365};
    int[] cumDaysInYearLeap = {0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335, 366};
    int[] daysInLeapCycle = {0, 365, 730, 1095, 1461}; // day preceding the cycle year,index is in cycle year from beginning
    private String dtString;
    private String dayOfWeek;
    private int dayOfWeekNum;
    private String dtFormat; // set per security request

    Dates() {
        this.error = error;
        day = UnAvails.NOTAVAILDATE;
        month = UnAvails.NOTAVAILDATE;
        year = UnAvails.NOTAVAILDATE;
        dayOfWeek = UnAvails.NOTAVAILSTRING;
        dayOfWeekNum = UnAvails.NOTAVAILINT;
        // Format
        dtFormat = CalUnits.DEFAULTDATEFORMAT;
        dtString = UnAvails.NOTAVAILSTRING;
        dtCode = UnAvails.NOTAVAILLONG;
        daysFrom1900 = UnAvails.NOTAVAILINT;
        dtString = UnAvails.NOTAVAILSTRING;
        this.isNull = true;

    }


    Dates(Dates dtInput) {
        this.year = dtInput.year;
        this.month = dtInput.month;
        this.day = dtInput.day;
        this.dtString = dtInput.dtString;
        this.daysFrom1900 = dtInput.daysFrom1900;
        this.dtFormat = CalUnits.DEFAULTDATEFORMAT;

    }

    Dates(Error error) {
        this.error = error;
        day = UnAvails.NOTAVAILDATE;
        month = UnAvails.NOTAVAILDATE;
        year = UnAvails.NOTAVAILDATE;
        dayOfWeek = UnAvails.NOTAVAILSTRING;
        dayOfWeekNum = UnAvails.NOTAVAILINT;
        // Format
        dtFormat = CalUnits.DEFAULTDATEFORMAT;
        dtString = UnAvails.NOTAVAILSTRING;
        dtCode = UnAvails.NOTAVAILLONG;
        daysFrom1900 = UnAvails.NOTAVAILINT;
        weekend1 = CalUnits.SATURDAY;
        weekend2 = CalUnits.SUNDAY;

    }

    Dates(String dtString, String dtFormat) {
        this.dtString = dtString;
        this.dtFormat = dtFormat;
        setDate(dtString, dtFormat);
        weekend1 = CalUnits.SATURDAY;
        weekend2 = CalUnits.SUNDAY;
        this.error = new Error();
    }

    public void clear() {
        day = UnAvails.NOTAVAILDATE;
        month = UnAvails.NOTAVAILDATE;
        year = UnAvails.NOTAVAILDATE;
        dayOfWeek = UnAvails.NOTAVAILSTRING;
        dayOfWeekNum = UnAvails.NOTAVAILINT;
        // Format
        dtFormat = CalUnits.DEFAULTDATEFORMAT;
        dtString = UnAvails.NOTAVAILSTRING;
        dtCode = UnAvails.NOTAVAILLONG;
        daysFrom1900 = UnAvails.NOTAVAILINT;
        weekend1 = CalUnits.SATURDAY;
        weekend2 = CalUnits.SUNDAY;
    }

    public void setUnavail() {
        day = UnAvails.NOTAVAILDATE;
        month = UnAvails.NOTAVAILDATE;
        year = UnAvails.NOTAVAILDATE;
        dayOfWeek = UnAvails.NOTAVAILSTRING;
        dayOfWeekNum = UnAvails.NOTAVAILINT;
        // Format
        dtFormat = UnAvails.NOTAVAILSTRING;
        dtString = UnAvails.NOTAVAILSTRING;
        dtCode = UnAvails.NOTAVAILINT;
        daysFrom1900 = UnAvails.NOTAVAILINT;
        dtString = UnAvails.NOTAVAILSTRING;
        weekend1 = CalUnits.SATURDAY;
        weekend2 = CalUnits.SUNDAY;

    }

    Dates (String today) {
        if (today.equals("Today")) {
            String strNow = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
            String [] strParts = strNow.split("-");
            year = Integer.valueOf(strParts[0]);
            month = Integer.valueOf(strParts[1]);
            day = Integer.valueOf(strParts[2]);
            weekend1 = CalUnits.SATURDAY;
            weekend2 = CalUnits.SUNDAY;
            dtFormat = "YYYY-MM-DD";
            dtString = strNow;
        }
        else {
            setUnavail();
        }
    }

    public Dates getToday() {
        String strNow =  LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        Dates dtToday = new Dates(strNow, "YYYY-MM-DD");
        return dtToday;
    }
    public void setWeekend(boolean weekend) {
    }

    public Integer[] getYearMonthDay() {
        Integer[] date = {0, 0, 0};
        date[0] = this.year;
        date[1] = this.month;
        date[2] = this.day;
        return date;
    }


    public long getDateCode() {
        dtCode = 10000L * year + 100L * month + day;
        return dtCode;
    }

    public void setDate(String sdtString) {
        String uFormat = dtFormat.toUpperCase();
        boolean isValid = false;
        String[] parts;
        String sMon;

        switch (dtFormat) {
            case "DD/MM/YYYY":
                parts = sdtString.split("/");
                year = Integer.parseInt(parts[2]);
                month = Integer.parseInt(parts[1]);
                day = Integer.parseInt(parts[0]);
                dtString = sdtString;
                dtCode = 10000L * year + 100L * month + day;
                break;
            case "DD.MM.YYYY":
                parts = sdtString.split(".");
                year = Integer.parseInt(parts[2]);
                month = Integer.parseInt(parts[1]);
                day = Integer.parseInt(parts[0]);
                dtString = sdtString;
                dtCode = 10000L * year + 100L * month + day;
                break;
            case "MM/DD/YYYY":
                parts = sdtString.split("/");
                year = Integer.parseInt(parts[2]);
                month = Integer.parseInt(parts[0]);
                day = Integer.parseInt(parts[1]);
                dtString = sdtString;
                dtCode = 10000L * year + 100L * month + day;
                break;
            case "MM.DD.YYYY":
                parts = sdtString.split(".");
                year = Integer.parseInt(parts[2]);
                month = Integer.parseInt(parts[0]);
                day = Integer.parseInt(parts[1]);
                dtString = sdtString;
                dtCode = 10000L * year + 100L * month + day;
                break;
            case "YYYY/MM/DD":
                parts = sdtString.split("/");
                year = Integer.parseInt(parts[0]);
                month = Integer.parseInt(parts[1]);
                day = Integer.parseInt(parts[2]);
                dtString = sdtString;
                dtCode = 10000L * year + 100L * month + day;
                break;
            case "YYYY.MM.DD":
                parts = sdtString.split(".");
                year = Integer.parseInt(parts[0]);
                month = Integer.parseInt(parts[1]);
                day = Integer.parseInt(parts[2]);
                dtString = sdtString;
                dtCode = 10000L * year + 100L * month + day;
                break;
            case "MON-DAY-YEAR":
                parts = sdtString.split("-");
                year = Integer.parseInt(parts[2]);
                String sMonth = parts[0];
                day = Integer.parseInt(parts[1]);
                month = getMonthNumber(parts[0]);
                dtString = sdtString;
                dtCode = 10000L * year + 100L * month + day;
                break;
            case "DAY-MON-YEAR":
                parts = sdtString.split("-");
                year = Integer.parseInt(parts[2]);
                month = getMonthNumber(parts[1]);
                day = Integer.parseInt(parts[0]);
                dtString = sdtString;
                dtCode = 10000L * year + 100L * month + day;
                break;
            case "MON DAY YEAR":
                parts = sdtString.split(" ");
                year = Integer.parseInt(parts[2]);
                month = getMonthNumber(parts[0]);
                day = Integer.parseInt(parts[1]);
                dtString = sdtString;
                dtCode = 10000L * year + 100L * month + day;
                dtString = sdtString;
                break;
            case "DAY MON YEAR":
                parts = sdtString.split(" ");
                year = Integer.parseInt(parts[2]);
                month = getMonthNumber(parts[1]);
                day = Integer.parseInt(parts[0]);
                dtString = sdtString;
                break;
        }
        dtCode = 10000L * year + 100L * month + day;
    }


    public boolean setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        setUpdateDate(dtFormat);
        return true;
    }

    public Dates assignDate(int year, int month, int day) {
        Dates dtDate = new Dates();
        dtDate.year = year;
        dtDate.month = month;
        dtDate.day = day;
        setUpdateDate(dtFormat);
        return dtDate;
    }

    public void setUpdateDate(String dtFormat) {
        String uFormat = dtFormat.toUpperCase();
        boolean isValid = false;
        String[] parts;
        String sDay = String.format("%02d", day);
        String sMon = String.format("%02d", month);
        switch (dtFormat) {
            case "DD/MM/YYYY":
                dtString = sDay + "/" + sMon + "/" + year;
                dtCode = 10000L * year + 100L * month + day;
                break;
            case "DD.MM.YYYY":
                dtString = sDay + "." + month + "." + year;
                break;
            case "MM/DD/YYYY":
                dtString = sMon + "/" + sDay + "/" + year;
                break;
            case "MM.DD.YYYY":
                dtString = sMon + "." + sDay + "." + year;
                break;
            case "YYYY/MM/DD":
                dtString = year + "/" + sMon + "/" + sDay;
                break;
            case "YYYYMMDD":
                dtString = year + sMon + sDay;
                break;
            case "YYYY.MM.DD":
                dtString = year + "/" + sMon + "/" + sDay;
            case "MON-DAY-YEAR":
                sMon = getMonthName(month);
                dtString = sMon + "-" + sDay + "-" + year;
                break;
            case "DAY-MON-YEAR":
                sMon = getMonthName(month);
                dtString = sDay + "-" + sMon + "-" + year;
                break;
            case "MON DAY YEAR":
                sMon = getMonthName(month);
                dtString = sMon + " " + sDay + " " + year;
                break;
            case "DAY MON YEAR":
                sMon = getMonthName(month);
                dtString = sDay + "-" + sMon + "-" + year;
                break;
        }
        dtCode = 10000L * year + 100L * month + day;

    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public void setDateFormat(String dtFormat) {
        this.dtFormat = dtFormat;
    }

    public boolean setDate(Dates dtInput) {
        this.day = dtInput.day;
        this.month = dtInput.month;
        this.year = dtInput.year;
        this.dtString = dtInput.dtString;
        this.dtCode = dtInput.dtCode;
        setUpdateDate(dtFormat);
        return true;
    }

    public String getDateString() {
        setUpdateDate(dtFormat);
        return this.dtString.trim();
    }

    public String getDateString(String dtFormat) {
        setUpdateDate(dtFormat);
        return this.dtString.trim();
    }

    public int setDaysFrom1900(Dates dtDate) {

        int absDays = 0, monthDays;
        if (dtDate.month < 1) {
            error.setError(getClass().getName(), " an invalid month of " + month + " is specified for setAbsDays");
        }
        int cycles = dtDate.year / 4;
        int cycleYearNo = dtDate.year - cycles * 4;
        // cycle year 0 is first
        int noDaysInCycleYear = daysInLeapCycle[cycleYearNo];
        int yearDays = cycles * CalUnits.leapCycleDays + noDaysInCycleYear;
        if (isLeapYear(dtDate.year))
            monthDays = cumDaysInYearLeap[dtDate.month - 1];
        else
            monthDays = cumDaysInYearNonLeap[dtDate.month - 1];
        int dayDays = day - 1;
        daysFrom1900 = yearDays + monthDays + dayDays;
        daysFrom1900 = daysFrom1900 - CalUnits.DAYSIN1900;
        return daysFrom1900;

    }

    public boolean isValidMonth(int month) {
        return month >= 1 && month <= 12;
    }

    public boolean isValidYear(int year) {
        return year >= Limits.MINYEAR && year <= Limits.MAXYEAR;
    }

    public boolean isValidDay(int day, int month, int year) {
        boolean isValid = false;
        if (day < 1 || day > 31)
            return false;
        switch (month) {
            case 1: // Jan
                if (day <= 31)
                    isValid = true;
                break;
            case 2: //Feb
                if (isLeapYear(year)) {
                    if (day <= 29)
                        isValid = true;
                } else {
                    if (day <= 28)
                        isValid = true;
                }
                break;
            case 3:
                if (day <= 31)
                    isValid = true;
                break;
            case 4:
                if (day <= 30)
                    isValid = true;
                break;
            case 5:
                if (day <= 31)
                    isValid = true;
                break;
            case 6:
                if (day <= 30)
                    isValid = true;
                break;
            case 7:
                if (day <= 31)
                    isValid = true;
                break;
            case 8:
                if (day <= 31)
                    isValid = true;
                break;
            case 9:
                if (day <= 30)
                    isValid = true;
                break;
            case 10:
                if (day <= 31)
                    isValid = true;
                break;
            case 11:
                if (day <= 30)
                    isValid = true;
                break;
            case 12:
                if (day <= 31)
                    isValid = true;
                break;
            default:
                error.setError(getClass().getName(), "invalid month detected " + month);
                isValid = false;
                break;
        }
        return isValid;
    }


    public boolean setDate(String dtString, String dtFormat) {
        String uFormat = dtFormat.toUpperCase();
        boolean isValid = false;
        String[] parts;
        dtString = dtString.trim();
        dtFormat = dtFormat.trim();
        // are  there 3 parts?
        StringTokenizer stok = new StringTokenizer(dtString, "-/. ");
        int noParts = stok.countTokens();
        if (noParts != 3 && !dtFormat.equals("YYYYMMDD")) {
            String errmsg = "An invalid date " + dtString + " has been specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        switch (dtFormat) {
            case "YYYYMMDD":
                int[] dtParts = new int[3];
                dtParts = decodeDate(dtString);
                year = dtParts[0];
                month = dtParts[1];
                day = dtParts[2];
                break;
            case "DD/MM/YYYY":
                parts = dtString.split("/");
                day = Integer.valueOf(parts[0]);
                month = Integer.valueOf(parts[1]);
                year = Integer.valueOf(parts[2]);
                break;
            case "DD.MM.YYYY":
                parts = dtString.split(".");
                day = Integer.valueOf(parts[0]);
                month = Integer.valueOf(parts[1]);
                year = Integer.valueOf(parts[2]);
                break;
            case "MM/DD/YYYY":
                parts = dtString.split("/");
                month = Integer.valueOf(parts[0]);
                day = Integer.valueOf(parts[1]);
                year = Integer.valueOf(parts[2]);
                break;
            case "MM.DD.YYYY":
                parts = dtString.split(".");
                month = Integer.valueOf(parts[0]);
                day = Integer.valueOf(parts[1]);
                year = Integer.valueOf(parts[2]);
                break;
            case "YYYY/MM/DD":
                parts = dtString.split("/");
                year = Integer.valueOf(parts[2]);
                month = Integer.valueOf(parts[0]);
                day = Integer.valueOf(parts[1]);
                break;
            case "YYYY-MM-DD":
                parts = dtString.split("-");
                year = Integer.valueOf(parts[0]);
                month = Integer.valueOf(parts[1]);
                day = Integer.valueOf(parts[2]);
                break;
            case "YYYY.MM.DD":
                parts = dtString.split("/");
                year = Integer.valueOf(parts[2]);
                month = Integer.valueOf(parts[0]);
                day = Integer.valueOf(parts[1]);
                break;
            case "MON-DAY-YEAR":
                parts = dtString.split("-");
                month = getMonthNumber(parts[0]);
                day = Integer.valueOf(parts[1]);
                year = Integer.valueOf(parts[2]);
                break;
            case "DAY-MON-YEAR":
                parts = dtString.split("-");
                month = Integer.valueOf(parts[0]);
                day = getMonthNumber(parts[1]);
                year = Integer.valueOf(parts[2]);
                break;
            case "MON DAY YEAR":
                parts = dtString.split(" ");
                month = Integer.valueOf(parts[0]);
                day = getMonthNumber(parts[1]);
                year = Integer.valueOf(parts[2]);
                break;
            case "DAY MON YEAR":
                parts = dtString.split(" ");
                month = Integer.valueOf(parts[1]);
                day = getMonthNumber(parts[0]);
                year = Integer.valueOf(parts[2]);
                break;
            default:
                String errmsg = "invalid date format " + dtFormat + " detected";
                error.setError(getClass().getName(), errmsg);
                return false;
        }
        isValid = isValidYear(year);
        if (!isValid) {
            String errmsg = "An invalid year in " + dtString + " has been specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        isValid = isValidMonth(month);
        if (!isValid) {
            String errmsg = "An invalid month in " + dtString + " has been specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        isValid = isValidDay(day, month, year);
        if (!isValid) {
            String errmsg = "An invalid day in " + dtString + " has been specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        this.dtString = dtString;
        this.dtFormat = dtFormat;
        dtCode = 10000L * year + 100L * month + day;
        return true;
    }


    public String getDayOfWeek(Dates dtDate) {
        String dayOfWeekString = null;
        int dayDiff = daysFrom1900(dtDate);
        // we count from 1/1/1904 which is a friday
        int fridayOffset = (dayDiff) % 7;
        int dowFriday = 5;
        int dayOfWeekNum = (dowFriday + fridayOffset) % 7;
        dayOfWeekString = mapDayOfWeek(dayOfWeekNum);
        return dayOfWeekString;
    }

    boolean isWeekend(Dates dtDate) {
        int dayNum = 0;
        String dayOfWeekString = getDayOfWeek(dtDate);
        if (dayNum == weekend1) {
            dtDate.setWeekend(true);
            return true;
        }
        if (dayNum == weekend2) {
            dtDate.setWeekend(true);
            return true;
        }
        dtDate.setWeekend(false);
        return false;
    }

    String mapDayOfWeek(int dayOfWeekNum) {
        switch (dayOfWeekNum) {
            case 0:
                return "SUN";
            case 1:
                return "MON";
            case 2:
                return "TUE";
            case 3:
                return "WED";
            case 4:
                return "THU";
            case 5:
                return "FRI";
            case 6:
                return "SAT";
            default:
                error.setError(getClass().getName(), "invalid day of the week number " + dayOfWeekNum + " detected");
                return "NA";
        }

    }

    public boolean geDate(Dates dt1, Dates dt2) {
        if (dt1.year > dt2.year)
            return true;
        if (dt1.year < dt2.year)
            return false;
        if (dt1.month > dt2.month)
            return true;
        if (dt1.month < dt2.month)
            return false;
        return dt1.day >= dt2.day;
    }

    public boolean geDate(Dates dtCalc) {
        if (this.year > dtCalc.year)
            return true;
        if (this.year < dtCalc.year)
            return false;
        if (this.month > dtCalc.month)
            return true;
        if (this.month < dtCalc.month)
            return false;
        return this.day >= dtCalc.day;
    }

    public boolean gtDate(Dates dt1, Dates dt2) {
        if (dt1.year > dt2.year)
            return true;
        if (dt1.year < dt2.year)
            return false;
        if (dt1.month > dt2.month)
            return true;
        if (dt1.month < dt2.month)
            return false;
        return dt1.day > dt2.day;
    }

    public boolean gtDate(Dates dt2) {
        if (this.year > dt2.year)
            return true;
        if (this.year < dt2.year)
            return false;
        if (this.month > dt2.month)
            return true;
        if (this.month < dt2.month)
            return false;
        return this.day > dt2.day;
    }

    public boolean leDate(Dates dt1, Dates dt2) {
        if (dt1.year < dt2.year)
            return true;
        if (dt1.year > dt2.year)
            return false;
        if (dt1.month < dt2.month)
            return true;
        if (dt1.month > dt2.month)
            return false;
        return dt1.day <= dt2.day;
    }

    public boolean leDate(Dates dt2) {
        if (this.year < dt2.year)
            return true;
        if (this.year > dt2.year)
            return false;
        if (this.month < dt2.month)
            return true;
        if (this.month > dt2.month)
            return false;
        return this.day <= dt2.day;
    }

    public boolean ltDate(Dates dt1, Dates dt2) {
        if (dt1.year < dt2.year)
            return true;
        if (dt1.year > dt2.year)
            return false;
        if (dt1.month < dt2.month)
            return true;
        if (dt1.month > dt2.month)
            return false;
        return dt1.day < dt2.day;
    }

    public boolean ltDate(Dates dt2) {
        if (this.year < dt2.year)
            return true;
        if (this.year > dt2.year)
            return false;
        if (this.month < dt2.month)
            return true;
        if (this.month > dt2.month)
            return false;
        return this.day < dt2.day;
    }

    public boolean eqDate(Dates dt1, Dates dt2) {
        if (dt1.year != dt2.year)
            return false;
        if (dt1.month != dt2.month)
            return false;
        return dt1.day == dt2.day;
    }

    public boolean eqDate(Dates dt2) {
        if (this.year != dt2.year)
            return false;
        if (this.month != dt2.month)
            return false;
        return this.day == dt2.day;
    }

    public boolean neqDate(Dates dt1, Dates dt2) {
        if (dt1.year != dt2.year)
            return true;
        if (dt1.month != dt2.month)
            return true;
        return dt1.day != dt2.day;
    }

    public boolean neqDate(Dates dt2) {
        if (this.year != dt2.year)
            return true;
        if (this.month != dt2.month)
            return true;
        return this.day != dt2.day;
    }

    public boolean between(Dates dtBefore, Dates dtAfter) {
        if (geDate(dtBefore) && leDate(dtAfter))
            return true;
       return false;
    }

    public Dates minDate(Dates dtDate1, Dates dtDate2) {
        if (dtDate1.ltDate(dtDate1, dtDate2))
            return dtDate1;
        else
            return dtDate2;
    }

    public Dates minDate(Dates dt2) {
        if (this.ltDate(dt2))
            return this;
        else
            return dt2;
    }

    public Dates maxDate(Dates dtDate1, Dates dtDate2) {
        if (dtDate1.gtDate(dtDate2))
            return dtDate1;
        else
            return dtDate2;
    }

    public Dates maxDate(Dates dt2) {
        if (this.gtDate(dt2))
            return this;
        else
            return dt2;
    }

    public int getDaysInMonth(Dates dtDate) {
        int daysInMonth = 0;
        if (isLeapYear(dtDate.year))
            daysInMonth = CalUnits.daysInMonthLeap[dtDate.month + 1];
        else
            daysInMonth = daysInMonth = CalUnits.daysInMonthNonLeap[dtDate.month + 1];
        return daysInMonth;
    }

    private String getMonthName(int month) {

        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return "Na";
        }
    }

    private Integer getMonthNumber(String strMonth) {
        String uStrMonth = strMonth.toUpperCase();
        if (uStrMonth.equals("JAN"))
            return 1;
        if (uStrMonth.equals("FEB"))
            return 2;
        if (uStrMonth.equals("MAR"))
            return 3;
        if (uStrMonth.equals("APR"))
            return 4;
        if (uStrMonth.equals("MAY"))
            return 5;
        if (uStrMonth.equals("JUN"))
            return 6;
        if (uStrMonth.equals("JUL"))
            return 7;
        if (uStrMonth.equals("AUG"))
            return 8;
        if (uStrMonth.equals("SEP"))
            return 9;
        if (uStrMonth.equals("OCT"))
            return 10;
        if (uStrMonth.equals("NOV"))
            return 11;
        if (uStrMonth.equals("DEC"))
            return 12;
        // this.error.setError(getClass().getName(), "you have entered an invalid month name " + strMonth);
        return UnAvails.NOTAVAILINT;
    }

    public int getDaysInYear(Dates date) {
        if (date.isLeapYear(date.year))
            return 366;
        return 365;
    }

    public int getDaysToDate(Dates date) {
        int days = 0;
        if (date.isLeapYear(date.year))
            days = CalUnits.cumDaysInYearLeap[date.month - 1] + date.day - 1;
        else
            days = CalUnits.cumDaysInYearNonLeap[date.month - 1] + date.day - 1;
        return days;
    }

    public String getDateString(Dates dtDate, String dtFormat) {
        String dtString = null;
        if (day == UnAvails.NOTAVAILINT || dtDate.month == UnAvails.NOTAVAILINT || dtDate.year == UnAvails.NOTAVAILINT)
            return "Unavailable";
        String sDay = String.format("%02d", dtDate.day);
        String sMon = String.format("%02d", dtDate.month);
        String uFormat = dtFormat;
        if (uFormat.equals("DD/MM/YYYY")) {
            dtString = sDay + "/" + sMon + "/" + dtDate.year;
            return dtString;
        }
        if (uFormat.equals("DD.MM.YYYY")) {
            dtString = sDay + "." + sMon + "." + dtDate.year;
            return dtString;
        }
        if (uFormat.equals("MM/DD/YYYY")) {
            dtString = sMon + "/" + sDay + "/" + dtDate.year;
            return dtString;
        }
        if (uFormat.equals("MM.DD.YYYY")) {
            dtString = sMon + "." + sDay + "." + dtDate.year;
            return dtString;
        }
        if (uFormat.equals("YYYY/MM/DD")) {
            dtString = dtDate.year + "/" + sMon + "/" + sDay;
            return dtString;
        }
        if (uFormat.equals("YYYY.MM.DD")) {
            dtString = dtDate.year + "." + sMon + "." + sDay;
            return dtString;
        }
        if (uFormat.equals("MON-DAY-YEAR")) {
            String strMonth = getMonthName(dtDate.month);
            dtString = strMonth + "-" + sDay + "-" + dtDate.year;
            return dtString;
        }
        String strMonth;
        if (uFormat.equals("DAY-MON-YEAR"))
            strMonth = getMonthName(month);
        return dtString;
    }

    public void setDateCode() {
        this.dtCode = year * 10000L + 100L * month + day;
    }

    public boolean isLeapYear(int year) {
        if (0 == year % 4) {
            if (0 == year % 100) {
                return 0 == year % 400;

            }
            return true;
        }
        return false;
    }

    public boolean isEndOfMonth(Dates dtDate) {
        if (isLeapYear(dtDate.year)) {
            return this.day == CalUnits.daysInMonthLeap[month];
        }
        return this.day == CalUnits.daysInMonthNonLeap[month];
    }

    public int getDaycountDCACT(Dates dtFrom, Dates dtTo) {
        int dayC;
        dayC = daysFrom1900(dtTo) - daysFrom1900(dtFrom);
        return dayC;
    }

    public int getMonthCount(Dates dtFrom, Dates dtTo) {
        int monthCount = 0;
        monthCount = 12 * (dtTo.year - dtFrom.year) + dtTo.month - dtFrom.month;
        return monthCount;
    }


    public int daysFrom1900(Dates date) {
        int noDays;
        // fisrt leap year is in 1904
        int[] daysInCycle = {0, 366, 731, 1096};
        int noYrsDiff = (date.year - 1904);
        //  System.out.println("noYrsDiff = " + noYrsDiff);
        int noLeapCycles = noYrsDiff / 4;
        int noWholeCycleDays = noLeapCycles * 1461;
        int lastCycleYear = 1904 + noLeapCycles * 4;
        //  System.out.println("noLeap cycles = " + noLeapYrs);
        int cycleNo = (date.year - lastCycleYear);
        int intraCycleDays = daysInCycle[cycleNo];
        int daystoMonth = 0;
        //  System.out.println("rem years in cycle = " + remYrs);
        // get days to desired date
        if (date.isLeapYear(date.year))
            daystoMonth = cumDaysInYearLeap[date.month - 1];
        else
            daystoMonth = cumDaysInYearNonLeap[date.month - 1];
        int daysInMonth = date.day - 1;
        noDays = noWholeCycleDays + intraCycleDays + daystoMonth + daysInMonth;
        return noDays;
    }

    public boolean isValid(Dates dtDate) {
        if (dtDate.year <= 1900) {
            String errmsg = "Invalid year specified for date " + dtDate.getDateString() + " must be greater than 1900";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (dtDate.year > CalUnits.MAXYEAR) {
            String errmsg = "Invalid year specified for date " + dtDate.getDateString();
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (!isValidMonth(dtDate.month)) {
            String errmsg = "Invalid month specified for date " + dtDate.getDateString();
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (!isValidMonth(dtDate.day)) {
            String errmsg = "Invalid day specified for date " + dtDate.getDateString();
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        return true;

    }

    public int[] decodeDate(String dtString) {
        int[] dateParts = new int[3];
        int iDate = Integer.valueOf(dtString);
        year = iDate / 10000;
        month = (iDate - 10000 * year) / 100;
        day = iDate - 10000 * year - 100 * month;
        dateParts[0] = year;
        dateParts[1] = month;
        dateParts[2] = day;
        return dateParts;
    }



}
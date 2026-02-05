package app;

import java.time.LocalDate;

public class Time {
    // private DayCount dayCount;
    // private SchedRule schedRule = new SchedRule();
    public final double periodCount;
    public final double yearCount;
    /**
     * Contains the data and method calls to compute periods, years and time
     * between dates
     */
    Error error;
    // DayCount dayCount;
    Dates dtMaturity;
    Dates dt1900 = new Dates("01/01/1900", "MM/DD/YYYY");
    Dates dtInfinity = new Dates("10/31/3000", "MM/DD/YYYY");

    Time(Error error) {
        this.error = error;
        // this.dcNum = DayCount.DCMethod.DC_ACT;
        // dcDen = DayCount.DCMethod.DC_ACT;
        // tring dtFormat;
        // private DayCount.DCMethod dcNum;
        // private DayCount.DCMethod dcDen;
        int freq = UnAvails.NOTAVAILINT;
        periodCount = UnAvails.NOTAVAILDOUBLE;
        yearCount = UnAvails.NOTAVAILDOUBLE;

    }

    public double getYearCount() {
        return this.yearCount;
    }

    public Dates getDateFromDays(int days) {
        // how many leap year cycles
        Dates dtResult = new Dates();
        int yearNo, monthNo = 1, dayNo = 1;
        int noCyles = days / CalUnits.leapCycleDays; // four year sets
        yearNo = 4 * noCyles;
        // days in next leap year cycle
        int remDaysinCycle = days - noCyles * CalUnits.leapCycleDays;
        // getting year number in the next leap year cycle
        int yearInCycle = getCycleYearNo(remDaysinCycle);
        yearNo += yearInCycle;
        int remDaysInYear = remDaysinCycle - CalUnits.daysInLeapCycle[yearInCycle];
        // get month number in cycle year
        monthNo = getMonthFromCumDaysInYear(remDaysInYear, yearNo);
        yearNo += 1900;
        int daysWithinYear = getCumDaysInYear(monthNo, yearNo);
        dayNo = remDaysInYear - CalUnits.cumDaysInYearNonLeap[monthNo - 1] + 1;
        dtResult.setDate(yearNo, monthNo, dayNo);
        if (dtResult.isLeapYear(dtResult.year)) {
            dayNo = remDaysInYear - CalUnits.cumDaysInYearLeap[monthNo - 1] + 1;
            dtResult.setDate(yearNo, monthNo, dayNo);
        }
        return dtResult;
    }

    private int getCycleYearNo(int remDaysInLeapCycle) {
        int cycleNo = -1;
        while (++cycleNo < CalUnits.daysInLeapCycle.length) {
            if (CalUnits.daysInLeapCycle[cycleNo] > remDaysInLeapCycle) {
                return Math.max(0, cycleNo - 1);
            }
        }
        return 0;
    }

    public int getMonthFromCumDaysInYear(int remDays, int year) {
        int month = 0;
        Dates dt1900 = new Dates();
        dt1900 = dt1900.assignDate(1900, 1, 1);
        int mNo = -1;
        if (dt1900.isLeapYear(year)) {
            while (++mNo < CalUnits.monthsInYear) {
                if (CalUnits.cumDaysInYearLeap[mNo] > remDays)
                    return mNo;
            }
            return mNo;
        } else {
            mNo = -1;
            while (++mNo < CalUnits.monthsInYear) {
                if (CalUnits.cumDaysInYearNonLeap[mNo] > remDays)
                    return mNo;
            }
            return mNo;
        }
    }

    private int getCumDaysInYear(int monthNo, int year) {
        Dates dt1900 = new Dates();
        dt1900 = dt1900.assignDate(1900, 1, 1);
        if (dt1900.isLeapYear(year))
            return CalUnits.cumDaysInYearLeap[monthNo];
        return CalUnits.cumDaysInYearNonLeap[monthNo];
    }

    public int getDaysFromDate(Dates dtFrom) {
        int daysInDate = 0, daysToMonth;
        // int noCycles = dtFrom.getYear() / CalUnits.leapCycleDays; /
        Dates dt1900 = new Dates();
        dt1900 = dtFrom.assignDate(1900, 1, 1);
        int yearDiff = (dtFrom.year - dt1900.year);
        int fullCycles = yearDiff / 4;
        int cycleNo = yearDiff - 4 * fullCycles;
        int fullCycleDays = fullCycles * CalUnits.leapCycleDays;
        int cycleNoDays = CalUnits.daysInLeapCycle[cycleNo];
        if (dt1900.isLeapYear(dtFrom.year))
            daysToMonth = CalUnits.cumDaysInYearLeap[dtFrom.month - 1];
        else
            daysToMonth = CalUnits.cumDaysInYearNonLeap[dtFrom.month - 1];
        daysInDate = fullCycleDays + cycleNoDays + daysToMonth + dtFrom.day - 1;
        return daysInDate;
    }

    public Dates addMonthsToDate(Dates dtOccur, int noMonths, char useEndOfMonthRuleYn) {
        Dates dtResult = new Dates();
        // convert date to months, except for days
        int addYears = noMonths / CalUnits.MONTHSPERYEAR;
        int addMonths = noMonths - CalUnits.MONTHSPERYEAR * addYears;
        dtResult.year = dtOccur.year + addYears;
        int newMonths = dtOccur.month + addMonths;
        addYears = newMonths / CalUnits.MONTHSPERYEAR;
        dtResult.year += addYears;
        newMonths -= addYears * CalUnits.MONTHSPERYEAR;
        dtResult.month = newMonths;
        if (dtResult.month > Limits.MAXMONTHOFYEAR) {
            dtResult.month -= Limits.MAXMONTHOFYEAR;
            ++dtResult.year;
        }
        if (dtResult.month < Limits.MINMONTHOFYEAR) {
            dtResult.month += Limits.MAXMONTHOFYEAR;
            --dtResult.year;
        }
        int resultDay = dtOccur.day;

        // System.out.println("here is occurance date: " + dtOccur.getDateString());
        // System.out.println("dtOccur end of month: " +
        // dtOccur.isEndOfMonth(dtOccur.getMonth(), dtOccur.getYear()));
        if (dtOccur.isEndOfMonth(dtOccur) && useEndOfMonthRuleYn == error.BSYES) {
            if (dtOccur.isLeapYear(dtOccur.year))
                resultDay = CalUnits.daysInMonthLeap[dtResult.month];
            else
                resultDay = CalUnits.daysInMonthNonLeap[dtResult.month];

        }
        dtResult.day = resultDay;
        return dtResult;

    }

    public Dates addDaysToDateOrig(Dates dtFrom, int addDays, char use360Yn) {
        Dates dtTo = new Dates();

        int daysFromDate = getDaysFromDate(dtFrom);
        int daysTotal;
        if (use360Yn == 'N') {
            daysTotal = daysFromDate + addDays;
            dtTo = getDateFromDays(daysTotal);
        } else {
            char useEndOfMonthRule = 'N';
            int mo = -1;
            int addYears = addDays / 360;
            int remDays = addDays - 360 * addYears;
            int addMonths = remDays / 30;
            remDays -= 30 * addMonths;
            dtTo.setDate(dtFrom);
            dtTo.setYear(dtFrom.year + addYears);
            if (addMonths > 0)
                dtTo = addMonthsToDate(dtTo, addMonths, useEndOfMonthRule);
            int addDayToMonth = remDays;
            if (addDayToMonth > CalUnits.daysPer30DayMonth) {
                if (dtTo.month == 1) {
                    dtTo.setYear(dtTo.year + 1);
                    dtTo.setMonth(1);
                    dtTo.setDay(addDayToMonth - CalUnits.daysPer30DayMonth);
                } else {
                    dtTo.setMonth(dtTo.month + 1);
                    dtTo.setDay(addDayToMonth - CalUnits.daysPer30DayMonth);
                }
            } else
                dtTo.setDay(dtTo.day + addDayToMonth);
        }
        return dtTo;
    }

    public Dates addDaysToDate(Dates dtFrom, int addDays, char use360Yn) {
        Dates dtTo = new Dates();
        // ignore use360Yn as it is meaningless
        int daysFromDate = getDaysFromDate(dtFrom);
        int daysTotal;
        daysTotal = daysFromDate + addDays;
        dtTo = getDateFromDays(daysTotal);
        return dtTo;
    }

    public Dates addYearsToDate(Dates dtSettle, double years) {
        // get whole years
        int days = (int) (years * CalUnits.daysInNonLeapYear);
        Dates dtAdd = new Dates();
        char use360Yn = 'N';
        dtAdd = addDaysToDate(dtSettle, days, use360Yn);
        return dtAdd;
    }

    public double daysToYears(int days, Dates dtFrom, int calcOption) {
        double years;
        Dates dtNew = new Dates();
        dtNew.setDate(dtFrom);
        int cycles = days / CalUnits.leapCycleDays;
        switch (calcOption) {
            case CalUnits.calcYearFractional:
                years = days / CalUnits.averageDaysInYear;
                return years;
            case CalUnits.calcYear365:
                years = days / (double) CalUnits.daysInNonLeapYear;
                return years;
            case CalUnits.calcYearPrecise:
                int noCycles = days / CalUnits.leapCycleDays;
                int remdays = days - noCycles * CalUnits.leapCycleDays;
                int remYears = remdays / CalUnits.daysInNonLeapYear;
                int wholeYears = noCycles * 4 + remYears;
                int daysFraction = days - noCycles * CalUnits.leapCycleDays + remYears;
                dtNew.setYear(wholeYears);
                if (dtNew.isLeapYear(dtNew.year))
                    years = (double) wholeYears + daysFraction / CalUnits.daysInLeapYear;
                else
                    years = (double) wholeYears + daysFraction / CalUnits.daysInNonLeapYear;
            default:
                years = days / CalUnits.averageDaysInYear;
                return years;
        }

    }

    public Dates addWeeksToDate(Dates dtFrom, int noWeeks) {
        Dates dtNew = new Dates();
        int daysFrom = getDaysFromDate(dtFrom);
        int daysPlus = daysFrom + noWeeks * CalUnits.DAYSPERWEEK;
        dtNew = getDateFromDays(daysPlus);
        return dtNew;
    }

    public int weekCount(Dates dtFrom, Dates dtTo) {
        int noWeeks = 0;
        int fromDays = getDaysFromDate(dtFrom);
        int toDays = getDaysFromDate(dtTo);
        noWeeks = (toDays - fromDays) / CalUnits.DAYSPERWEEK;
        return noWeeks;
    }

    public int daysFrom1900(Dates date) {
        int days = date.setDaysFrom1900(date);
        return days;
    }

    public int getWeekOfMonth(Dates dtDate) {
        int dow1st = 1, dowDt = 1;
        Dates dt1st = new Dates();
        Dates dtLast = new Dates();
        dt1st.setYear(dtDate.year);
        dt1st.setMonth(dtDate.month);
        dt1st.setDay(1);
        String sDow1st = getDayOfWeek(dt1st);
        char use360Yn = 'N';
        Dates dtSat = addDaysToDate(dt1st, CalUnits.SATURDAY - dow1st, use360Yn);
        int weNo = 0;
        while (++weNo <= Limits.MAXWEEKOFMONTH) {
            if (dtDate.leDate(dtDate, dtSat))
                break;
            dtLast = dtSat;

            dtSat = addDaysToDate(dtLast, 7, use360Yn);
        }
        return weNo;
    }

    public String getDayOfWeek(Dates dtDate) {
        String dayOfWeekString;
        int dayOfWeekNum;
        int days = actualDayCount(dt1900, dtDate);
        int daysFromMonday = days % 7;
        dayOfWeekNum = daysFromMonday;
        dayOfWeekString = mapDayOfWeek(dayOfWeekNum);
        return dayOfWeekString;
    }

    public int actualDayCount(Dates dtFrom, Dates dtTo) {
        int dayC;
        dayC = dtTo.daysFrom1900(dtTo) - dtFrom.daysFrom1900(dtFrom);
        return dayC;
    }

    public Dates getToday() {
        Dates dtToday;
        LocalDate currentDate = LocalDate.now();
        dtToday = new Dates(currentDate.toString(), "YYYY-MM-DD");
        return dtToday;
    }

    public int getMonthsBetween(Dates dtFrom, Dates dtTo, boolean ignoreDays) {
        int noMonths = 0, noMonthsFrom, noMonthsTo;
        // calculate months between years
        noMonthsFrom = CalUnits.MONTHSPERYEAR * dtFrom.year + dtFrom.month;
        noMonthsTo = CalUnits.MONTHSPERYEAR * dtTo.year + dtTo.month;
        noMonths = noMonthsTo - noMonthsFrom;
        if (ignoreDays) {
            return noMonths;
        }
        double toMonFac = dtTo.day / CalUnits.daysInMonthNonLeap[dtTo.month];
        double fromMonFac = dtFrom.day / CalUnits.daysInMonthNonLeap[dtFrom.month];
        noMonths += toMonFac - fromMonFac;
        return noMonths;
    }

    public int getDaysBetweenDates(Dates dtFrom, Dates dtTo) {
        int daysBetween = 0;
        int daysFrom = getDaysFromDate(dtFrom);
        int daysTo = getDaysFromDate(dtTo);
        daysBetween = daysTo - daysFrom;
        return daysBetween;
    }

    public double getYearsBetweenDates(Dates dtFrom, Dates dtTo) {
        double diffYears = 0.;
        double yearsTo = dtTo.year + (dtTo.month / (double) CalUnits.monthsInYear) + dtTo.day / 365.25;
        double yearsFrom = dtFrom.year + (dtFrom.month / (double) CalUnits.monthsInYear) + dtFrom.day / 365.25;
        diffYears = yearsTo - yearsFrom;
        return diffYears;
    }

    public int getWeeksBetween(Dates dtFrom, Dates dtTo) {
        int noWeeks = 0;
        int fromDays = daysFrom1900(dtFrom);
        int toDays = daysFrom1900(dtTo);
        int daysDiff = toDays - fromDays;
        noWeeks = daysDiff / CalUnits.DAYSPERWEEK;
        return noWeeks;
    }

    public String mapDayOfWeek(int dayOfWeekNum) {
        String[] daysOfTheWeeknames = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        String sDow = daysOfTheWeeknames[dayOfWeekNum];
        return sDow;
    }

    public int mapDayOfWeek(String sDow) {
        switch (sDow) {
            case "SUN":
                return 0;
            case "MON":
                return 1;
            case "TUE":
                return 2;
            case "WED":
                return 3;
            case "THU":
                return 4;
            case "FRI":
                return 5;
            case "SAT":
                return 6;
            default:
                error.setError(getClass().getName(), "Invalid day of the the week " + sDow + " speciied");
        }
        return UnAvails.NOTAVAILINT;
    }

    String mapTimeUnit(String sVal) {
        int len = sVal.length();
        sVal.trim();
        String strTimeUnit = sVal.substring(len - 1);
        return strTimeUnit;
    }

    int mapRepeatFactor(String sVal) {
        int len = sVal.length();
        String strRepeatFactor = sVal.substring(0, len - 1);
        int repeatFactor = Integer.valueOf(strRepeatFactor);
        return repeatFactor;
    }

    public int mapCompoundFreq(String timeUnit, int repeatFactor) {
        int compFreq = 1;
        if (timeUnit.equals("D")) {
            return CalUnits.DAYSPERYEAR;
        }
        if (timeUnit.equals("W")) {
            return CalUnits.DAYSPERWEEK;
        }

        switch (repeatFactor) {
            case CalUnits.ANNUALREPEAT:
                compFreq = CalUnits.ANNUALFQ;
                break;
            case CalUnits.SEMIANNUALREPEAT:
                compFreq = CalUnits.SEMIANNUALFQ;
                break;
            case CalUnits.QUARTERLYREPEAT:
                compFreq = CalUnits.QUARTERLYFQ;
                break;
            case CalUnits.TRIANNUALREPEAT:
                compFreq = CalUnits.TRIANNUALFQ;
                break;
            case CalUnits.BIMONTHLYREPEAT:
                compFreq = CalUnits.BIMONTHLYFQ;
                break;
            case CalUnits.MONTHLYREPEAT:
                compFreq = CalUnits.MONTHLYFQ;
                break;
            default:
                compFreq = 0;
                error.setError(getClass().getName(), "Invalid periodicity " + repeatFactor + " was specified");
                return 0;
        }

        return compFreq;
    }

    public Dates getDateFromDaysBetween(Dates dtStart, int noDays) {
        // go to begininng of year
        Dates dtTo = new Dates();
        int noStartDays = getDaysFromDate(dtStart); // days from 1900 for dtStart
        int noTotalDays = noStartDays + noDays; // total days from 1900 for new dates
        dtTo = getDateFromDays(noTotalDays); // covert 1900 fromd ays to date
        return dtTo;
    }
}
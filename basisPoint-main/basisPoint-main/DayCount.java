
public class DayCount {
    Dates dtMature;
    Error error;
    Time time;
    Dates dt1900 = new Dates("01/01/1900", "MM/DD/YYYY");


    DayCount(Error error) {
        // Daycount initializer
        this.error = error;
        this.time = new Time(error);
    }



    public void setMaturity(Dates dtMature) {
        this.dtMature = dtMature;

    }


    public void setDayCounts(DCMethod dcNum, DCMethod dcDen) {
        dt1900 = new Dates(error);
        dt1900.setDate(1900, 1, 1);
    }

    public int getMonthCount(Dates dtFrom, Dates dtTo, char useDaysYn) {
        int count;
        double dayFac = 0.;

        count = 12 * (dtTo.year - dtFrom.year) + dtTo.month - dtFrom.month;
        if (useDaysYn == 'Y') {
            if (dtFrom.isLeapYear(dtFrom.year))
                dayFac = (dtTo.day - dtFrom.day) / CalUnits.daysInMonthLeap[dtFrom.month];
            else
                dayFac = (dtTo.day - dtFrom.day) / CalUnits.daysInMonthNonLeap[dtFrom.month];
            if (dayFac >= .5)
                ++count;
        }
        return count;
    }

    public int getDaycount(Dates dtFrom, Dates dtTo, Dates dtMature, DCMethod dcMethod) {
        int dayC = 0;
        switch (dcMethod) {
            case DC_30A:
                dayC = getDaycountDC30A(dtFrom, dtTo);
                break;
            case DC_30:
                dayC = getDayCountDC30(dtFrom, dtTo);
                break;
            case DC_364:
                dayC = getDaycountDCACT(dtFrom, dtTo);
                break;
            case DC_30E:
                dayC = getDayCountDC30E(dtFrom, dtTo);
                break;
            case DC_30ISDA:
                dayC = getDayCountDC30ISDA(dtFrom, dtTo, dtMature);
                break;
            case DC_ACT:
            case DC_ACT360:
                dayC = getDaycountDCACT(dtFrom, dtTo);
                break;
            case DC_365L:
                dayC = getDaycountDC365L(dtFrom, dtTo);
                break;
            case DC_365:
                dayC = getDaycountDC365(dtFrom, dtTo);
                break;
            case DC_BUS:
                dayC = getDaycountDCBus(dtFrom, dtTo);
                break;
            default:
                dayC = getDaycountDCACT(dtFrom, dtTo);
                break;
        }
        return dayC;
    }


    public int getDaysInYear(DCMethod dcMethod, Dates dtFrom, Dates dtTo, int freq) {
        int diy;
        switch (dcMethod) {
            case DC_365:
                diy = 365;
                break;
            case DC_30:
            case DC_30A:
            case DC_30E:
            case DC_30ISDA:
                diy = 360;
                break;
            case DC_ACT:
            case DC_ACTISDA:
                // go accross year
                Dates dtNext = new Dates();
                dtNext.setDate(dtFrom);
                dtNext.year = dtFrom.year + 1;
                diy = getDaycountDCACT(dtFrom, dtNext);
                break;
            case DC_365L:
                if (freq == 1) {
                    if (dtFrom.month < 2 && dtFrom.isLeapYear(dtFrom.year))
                        diy = 366;
                    else if (dtTo.month > 2)
                        diy = 366;
                    else
                        diy = 365;
                } else {
                    if (dtTo.isLeapYear(dtTo.year))
                        diy = 366;
                    else
                        diy = 365;
                }
                break;
            case DC_BUS:
                diy = 252;
                break;
            default:
                diy = 366;
                break;
        }
        return diy;
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
                error.setError(getClass().getName(), "Invalid day of the week number " + dayOfWeekNum + " detected");
                return "NA";
        }
    }

    public String getDayOfWeek(Dates dtDate) {
        String dayOfWeekString;
        int days = getDaycountDCACT(dt1900, dtDate);
        int daysFromMonday = days % 7;
        int dayOfWeekNum = daysFromMonday;
        dayOfWeekString = mapDayOfWeek(dayOfWeekNum);
        return dayOfWeekString;
    }

    public Dates getNextMonday(Dates dtDate) {
        String dayOfWeekString;
        int days = getDaycountDCACT(dt1900, dtDate);
        int daysFromMonday = days % 7; // monday is day 1 of the week
        int addDays = CalUnits.DAYSPERWEEK + 1 - daysFromMonday;
        addDays = addDays % 7;
        Dates dtNextMonday = time.addDaysToDate(dtDate, addDays, 'N');
        return dtNextMonday;
    }


    public double getYearsBetweenDates(Dates dtFrom, Dates dtTo) {

        // how many full years
        double fullDaysTo = 0., fullDaysFrom = 0.;
        double fullYrs = (dtTo.year - dtFrom.year);
        // how many days to begining of month in dtTo year
        if (dtTo.isLeapYear(dtTo.year)) {
            fullDaysTo = CalUnits.cumDaysInYearLeap[dtTo.month - 1];
            fullDaysTo += dtTo.day;
        } else {
            fullDaysTo = CalUnits.cumDaysInYearNonLeap[dtTo.month - 1];
            fullDaysTo += dtTo.day;
        }
        if (dtFrom.isLeapYear(dtFrom.year)) {
            fullDaysFrom = CalUnits.cumDaysInYearLeap[dtFrom.month - 1];
            fullDaysFrom += dtTo.day;
        } else {
            fullDaysFrom = CalUnits.cumDaysInYearNonLeap[dtFrom.month - 1];
            fullDaysFrom += dtTo.day;
        }
        double years = fullYrs + (fullDaysTo - fullDaysFrom) / 365.;
        return years;
    }

    public int getDayCountDC30(Dates dtFrom, Dates dtTo) {
        // this only applies to bonds whose pay dates follow the end of month rule
        int dayC = 0;
        if (dtTo.month == 2 && dtTo.isEndOfMonth(dtTo) && dtFrom.month == 2 && dtFrom.isEndOfMonth(dtFrom))
            dtTo.day = 30;
        if (dtFrom.month == 2 && isEndOfMonth(dtFrom))
            dtFrom.month = 30;
        if (dtTo.day == 31 && dtFrom.day >= 30)
            dtTo.day = 30;
        if (dtFrom.day == 31)
            dtFrom.day = 30;
        dayC = 360 * (dtTo.year - dtFrom.year) + 30 * (dtTo.month - dtFrom.month) + (dtTo.day - dtFrom.day);
        return dayC;
    }

    public int getDayCountDC30ISDA(Dates dtFrom, Dates dtTo, Dates dtMature) {
        int dayC;
        int Feb = 2;
        boolean isEOM = isEndOfMonth(dtMature);
        if (dtFrom.month == dtTo.month && dtTo.month == Feb && isEOM) {
            dtTo.setMonth(dtFrom.month);
            dayC = 360 * (dtTo.year - dtFrom.year) + 30 * (dtTo.month - dtFrom.month) + (dtTo.day - dtFrom.day);
            return dayC;
        }
        if (dtFrom.month == Feb) {
            if (isEndOfMonth(dtFrom) && isEOM) {
                dtFrom.setDay(30);
                dayC = 360 * (dtTo.year - dtFrom.year) + 30 * (dtTo.month - dtFrom.month) + dtTo.day - dtFrom.day;
                return dayC;
            }
        }
        dayC = getDaycountDC30A(dtFrom, dtTo);
        return dayC;
    }

    public int getDayCountDC30E(Dates dtFrom, Dates dtTo) {
        int dayC;

        if (dtFrom.day == 31)
            dtFrom.setDay(30);
        if (dtTo.day == 31)
            dtTo.setDay(30);
        dayC = 360 * (dtTo.year - dtFrom.year) + 30 * (dtTo.month - dtFrom.month) + dtTo.day - dtFrom.day;
        return dayC;
    }


    public double getCouponFactorISDA(Dates dtFrom, Dates dtTo) {
        double dNum, dDen;
        double cpnFac1, cpnFac2 = 0, remays2 = 0.;
        double cpnFactor = 0.;
        if (dtFrom.year == dtTo.year) {
            if (dtFrom.isLeapYear(dtFrom.year))
                cpnFactor = getDaycountDCACT(dtFrom, dtTo) / 366;
            else
                cpnFactor = getDaycountDCACT(dtFrom, dtTo) / 365;
        } else {
            cpnFac1 = getDaysToYearEnd(dtFrom) / dtFrom.getDaysInYear(dtFrom);
            cpnFac2 = dtTo.getDaysToDate(dtTo) / dtTo.getDaysInYear(dtTo);
            cpnFactor = cpnFac1 + cpnFac2;
        }
        return cpnFactor;
    }

    public double matLenYears(int calcNo, Dates dtFrom, Dates dtTo, Dates dtMaturity) {
        double matLen = 0;
        int days = getDaycount(dtFrom, dtTo, dtMaturity, DayCount.DCMethod.DC_ACT);
        double years = time.daysToYears(days, dtMaturity, CalcList.MATLENYEARS);
        return years;
    }

    public int matLenDays(int calcNo, Dates dtSettle, Dates dtRedeem) {
        int matLen = 0;
        int days = getDaycount(dtSettle, dtRedeem, dtMature, DayCount.DCMethod.DC_ACT);
        return days;
    }


    public double simplePeriodCount(Dates dtFrom, Dates dtTo, int repeatFactor) {
        int noMonths;
        double yPeriod = (12 * (dtTo.year - dtFrom.year)) / repeatFactor;
        double mPeriod = (dtTo.month - dtFrom.month) / (double) repeatFactor;
        double dPeriod = (dtTo.day - dtFrom.day) / (30. * repeatFactor);
        double pCount = yPeriod + mPeriod + dPeriod;
        return pCount;
    }

    public double matLenYearsWtd(int calcNo, Dates dtSettle, Dates dtRedeem) {
        double matLen = 0;
        int days = getDaycount(dtSettle, dtRedeem, dtMature, DayCount.DCMethod.DC_ACT);
        double years = time.daysToYears(days, dtSettle, CalcList.MATLENYEARS);
        return years;
    }

    public int matLenDaysWtd(int calcNo, Dates dtSettle, Dates dtRedeem) {
        int matLen = 0;
        int days = getDaycount(dtSettle, dtRedeem, dtRedeem, DayCount.DCMethod.DC_ACT);
        return days;
    }

    // period starts  with last  coupon date and ends with settle date dtettle
    // for past or backward looking coupon factor for accrued interest
    // dtNext is the coupon date prior to the from date
    public double getBackwardCouponFactor(Dates dtLastCpn, Dates dtSettle, Dates dtNextCpn, Dates dtMature, DCMethod dcNum, DCMethod dcDen) {
        double cpnFactor = 0.;
        double den = 0.0, num = 0.0;
        num = getDaycount(dtLastCpn, dtSettle, dtMature, dcNum);
        den = getDaycount(dtLastCpn, dtNextCpn, dtMature, dcDen);
        if (den == 0.0)
            cpnFactor = 0.;
        else
            cpnFactor = num / den;
        return cpnFactor;
    }

    // remaining accural period - period starts settle date and ends with next coupon date
    // for  cashflow - forward looking daycount
    // dtNext is the coupon date prior to the from date
    public double getForwardCouponFactor(Dates dtLastCpn, Dates dtSettle, Dates dtNextCpn, Dates dtMature, DCMethod dcNum, DCMethod dcDen) {
        double cpnFactor = 0.;
        double dayPeriod = 0.0, daysfrom = 0.0;
        dayPeriod = getDaycount(dtLastCpn, dtNextCpn, dtMature, dcDen);
        daysfrom = getDaycount(dtLastCpn, dtSettle, dtMature, dcNum);
        if (dayPeriod == 0.0)
            cpnFactor = 0.;
        else
            cpnFactor = (dayPeriod - daysfrom) / dayPeriod;
        return cpnFactor;
    }

    public int getDaysToYearEnd(Dates dtDate) {
        int daysUpToMonth = 0, daysToDay = 0, daysToYearEnd = 0, daysInYear;

        if (dtDate.isLeapYear(dtDate.year)) {
            daysUpToMonth = CalUnits.cumDaysInYearLeap[dtDate.month - 1];
            daysInYear = CalUnits.daysInLeapYear;
        } else {
            daysUpToMonth = CalUnits.cumDaysInYearNonLeap[dtDate.month - 1];
            daysInYear = CalUnits.daysInNonLeapYear;
        }

        if (dtDate.day != 1)
            daysToDay = dtDate.day - 1;
        else
            daysToDay = 0;

        daysToYearEnd = daysUpToMonth + daysToDay;
        return daysInYear - daysToYearEnd;
    }

    public int getDaysInYear(Dates dtDate) {
        int daysUpToMonth = 0, daysToDay = 0, daysInYear = 0;
        if (dtDate.isLeapYear(dtDate.year))
            daysUpToMonth = CalUnits.cumDaysInYearLeap[dtDate.month - 1];
        else
            daysUpToMonth = CalUnits.cumDaysInYearNonLeap[dtDate.month - 1];
        if (dtDate.day != 1)
            daysToDay = dtDate.day - 1;
        else
            daysToDay = 0;

        daysInYear = daysUpToMonth + daysToDay;
        return daysInYear;
    }

    public boolean isEndOfMonth(Dates date) {
        if (0 == date.year % 4 && 0 != date.year % 400) {
            return CalUnits.daysInMonthLeap[date.month] == date.day;
        } else {
            return CalUnits.daysInMonthNonLeap[date.month] == date.day;
        }
    }


    public int getDaycountDCACT(Dates dtFrom, Dates dtTo) {
        int dayC;
        int daysTo = dtTo.daysFrom1900(dtTo);
        int daysFrom = dtFrom.daysFrom1900(dtFrom);
        dayC = daysTo - daysFrom;
        return dayC;
    }


    public int getDaycountDC365(Dates dtFrom, Dates dtTo) {
        int dayC;
        int dayInYrs = 365 * (dtTo.year - dtFrom.year);
        int daysBetMon = CalUnits.cumDaysInYearNonLeap[dtTo.month - 1] - CalUnits.cumDaysInYearNonLeap[dtFrom.month - 1];
        int daysBetDays = dtTo.day - dtFrom.day;
        dayC = dayInYrs + daysBetMon + daysBetDays;
        return dayC;
    }

    public int getDaycountDC365L(Dates dtFrom, Dates dtTo) {
        int dayC;
        int dayInYrs = 365 * (dtTo.year - dtFrom.year);
        int daysBetMon = CalUnits.cumDaysInYearNonLeap[dtTo.month - 1] - CalUnits.cumDaysInYearNonLeap[dtFrom.month - 1];
        int daysBetDays = dtTo.day - dtFrom.day;
        dayC = dayInYrs + daysBetMon + daysBetDays;
        return dayC;
    }

    public int getDaycountDC30A(Dates dtFrom, Dates dtTo) {
        dtFrom.setDay(Math.min(dtFrom.day, 30));
        if (dtFrom.day == 30)
            dtTo.setDay(Math.min(dtTo.month, 30));
        int dayC = 360 * (dtTo.year - dtFrom.year) + 30 * (dtTo.month - dtFrom.month) + dtTo.day - dtFrom.day;
        return dayC;
    }


    public int getDaycountEE(Dates dtFrom, Dates dtTo) {
        int dayC = 0;
        Dates dtF = new Dates();
        Dates dtT = new Dates();
        dtF.setDate(dtFrom);
        dtT.setDate(dtTo);
        if (isEndOfMonth(dtF))
            dtF.day = 30;
        if (isEndOfMonth(dtT))
            dtT.day = 30;
        if (dtT.eqDate(dtT, dtMature) && dtT.month == 2 && isEndOfMonth(dtMature))
            dtT.day = dtTo.day;
        dayC = 360 * (dtT.year - dtF.year) + 30 * (dtT.month - dtF.month) + dtT.day - dtF.day;
        return dayC;
    }


    public int getDaycountDCBus(Dates dtFrom, Dates dtTo) {
        int dayC, noHols;// no holidays and weekends
        dayC = getDaycountDCACT(dtFrom, dtTo);
        noHols = getHolidays();
        dayC = dayC - noHols;
        return dayC;
    }

    public void setMaturityDate(Dates dtMature) {
        this.dtMature = dtMature;
    }

    public int getHolidays() {
        int dayC;
        dayC = 0;
        return dayC;
    }

    public enum MonthList { // List of months in the year
        FEB, JAN, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC
    }

    enum DCMethod { // list freq,ist of daycount options
        DC_30A, // Bond basis - coupon period
        DC_30, // 30/360US
        DC_30E, // ICMA, Special German
        DC_30EE,
        DC_30ISDA, // ISDA or 30ISDA
        DC_BUS, // Business days
        DC_ACT, // Actual days same as ACTICMA
        DC_ACT360,
        DC_ACTISDA, // split across years if one non-leap another leap
        DC_365,
        DC_365L,  // ignore leap year
        DC_364, // ISDA uses constant accrual
        DC_NA    // not available
    }

    public DCMethod mapAccrualBasis(String sVal) {
        DCMethod dcMethod = DCMethod.DC_NA;
        switch (sVal) {
            case "30/360A":
                dcMethod = DCMethod.DC_30A;
                break;
            case "30/360":
                dcMethod = DCMethod.DC_30;
                break;
            case "30E/360":
                dcMethod = DCMethod.DC_30E;
                break;
            case "30EE/360":
            case "30E/ISDA":
                dcMethod = DCMethod.DC_30EE;
                break;
            case "ACT/ACT":
                dcMethod = DCMethod.DC_ACT;
                break;
            case "ACT/ISDA":
                dcMethod = DCMethod.DC_ACTISDA;
                break;
            case "ACT/365L":
                dcMethod = DCMethod.DC_365L;
                break;
            case "ACT365":
            case "ACT365F":
                dcMethod = DCMethod.DC_365;
                break;
            case "ACT/364":
                dcMethod = DCMethod.DC_364;
                break;
            case "BUS/252":
                dcMethod = DCMethod.DC_BUS;
                break;
            default:
                dcMethod = DCMethod.DC_ACT;
        }
        return dcMethod;
    }

    int dayCounting(int noCalc, Dates dtStart, Dates dtEnd, Dates dtMaturity, DCMethod basAccInt) {
        int dayC = getDaycount(dtStart, dtEnd, dtMature, basAccInt);
        return dayC;

    }

    // hpw many period are there between two dates where the period len is freq time per year
    public double periodCount(Dates dtFrom, Dates dtTo, int freq) {
        // freq is n times per year
        // calculate years between dates
        double years = getYearsBetweenDates(dtFrom, dtTo);
        double noPeriods = years / freq;
        return noPeriods;
    }

    public double accrualPeriod(Dates dtFrom, Dates dtTo, Dates dtLastCpn, Dates dtNextCpn, DCMethod dcNum, DCMethod dcDen) {
        // within a  coupon period
        double perCount = 0.;
        perCount = getDaycount(dtFrom, dtTo, dtMature, dcNum) / getDaycount(dtLastCpn, dtNextCpn, dtMature, dcDen);
        return perCount;
    }


}





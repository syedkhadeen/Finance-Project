package app;

public class SchedRule {
    // results
    Error error;
    Holidays holidays;
    DayCount dayCount = new DayCount(error);
    Time time;
    Dates dtDated = new Dates();
    Dates dtMaturity = new Dates();
    String accrualBasis;
    char useEndOfMonthRuleYn = UnAvails.NOTAVAILCHAR;
    String dayOfWeek = UnAvails.NOTAVAILSTRING;
    String monthOfYear = UnAvails.NOTAVAILSTRING;
    int weekOfMonth = UnAvails.NOTAVAILINT;
    int dayOfMonth = UnAvails.NOTAVAILINT;
    int weekend1 = UnAvails.NOTAVAILINT;
    int weekend2 = UnAvails.NOTAVAILINT;
    Dates[] dtUserPays = new Dates[Limits.MAXUSERDATES];
    int noUserSchedDts = 0;
    char NOTRUCROUND = 'N';
    String schedType;
    String country = UnAvails.NOTAVAILSTRING;
    String timeUnit = UnAvails.NOTAVAILSTRING;
    Dates dtPriorPay = new Dates();
    Dates dt1stPay = new Dates(); // date of 1sr intrest payment defines odd first perios
    Dates dtLastOnlyCoupon = new Dates(); // last coupon payment before maturity day
    Dates dtOccur = new Dates();
    Dates dtNextUserCpn = new Dates();
    Dates dtLastUserCpn = new Dates(); // user specufued last coupon date to use in acrued interest
    DayCount.DCMethod dayCountMeth = DayCount.DCMethod.DC_NA;
    int repeatFactor = UnAvails.NOTAVAILINT;
    int compoundFreq = UnAvails.NOTAVAILINT;
    String holidayRule = UnAvails.NOTAVAILSTRING;

    // holiday

    public SchedRule(Holidays holidays, Error error, DayCount dayCount, Time time) {
        this.holidays = holidays;
        this.error = error;
        this.dayCount = dayCount;
        this.time = time;

    }

    public SchedRule() {
        this.dtOccur = new Dates(error);
        this.timeUnit = UnAvails.NOTAVAILSTRING;
        this.dtPriorPay.year = UnAvails.NOTAVAILINT;
        this.dtMaturity = new Dates(error);
        this.dtLastOnlyCoupon = new Dates(error);
        this.compoundFreq = UnAvails.NOTAVAILINT;
        this.dtLastOnlyCoupon = new Dates(error);
        this.repeatFactor = UnAvails.NOTAVAILINT;
        this.dt1stPay = new Dates(error);
        this.useEndOfMonthRuleYn = UnAvails.NOTAVAILCHAR;
        this.holidayRule = UnAvails.NOTAVAILSTRING;
    }

    public SchedRule(Time time) {
        this.time = time;
    }

    public void setDtMaturity(Dates dtMaturity) {
        this.dtMaturity = dtMaturity;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public void setDateOccur(Dates dtOccur) {
        this.dtOccur = dtOccur;
    }

    public void setSchedType(String schedType) {
        this.schedType = schedType;
    }

    public void setDtPrevCpn(Dates dtPrevCpn) {
        this.dtLastUserCpn = dtPrevCpn;
    }

    public void setDtNextCpn(Dates dtNextCpn) {
        this.dtNextUserCpn = dtNextCpn;
    }

    public void setEndOfMonthRule(char endOfMonthRuleYn) {
        this.useEndOfMonthRuleYn = endOfMonthRuleYn;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setDayCountMeth(DayCount.DCMethod dayCountMeth) {
        this.dayCountMeth = dayCountMeth;
    }

    public int setRepeatFactor(int compoundFreq) {
        int repeatFactor = 0;
        switch (compoundFreq) {
            case CalUnits.ANNUALFQ:
                repeatFactor = CalUnits.ANNUALREPEAT;
                break;
            case CalUnits.SEMIANNUALFQ:
                repeatFactor = CalUnits.SEMIANNUALREPEAT;
                break;
            case CalUnits.TRIANNUALFQ:
                repeatFactor = CalUnits.TRIANNUALREPEAT;
                break;
            case CalUnits.QUARTERLYFQ:
                repeatFactor = CalUnits.QUARTERLYREPEAT;
            case CalUnits.BIMONTHLYFQ:
                repeatFactor = CalUnits.BIMONTHLYREPEAT;
                break;
            case CalUnits.MONTHLYFQ:
                repeatFactor = CalUnits.MONTHLYREPEAT;
                break;
            default:
                error.setError(getClass().getName(), "Invalid compound frequency " + compoundFreq + " was specifieed");
                return 0;
        }
        return repeatFactor;
    }

    public void clear() {
        dtDated.clear();
        dtMaturity.clear();
        accrualBasis = UnAvails.NOTAVAILSTRING;
        useEndOfMonthRuleYn = UnAvails.NOTAVAILCHAR;
        String dayOfWeek = UnAvails.NOTAVAILSTRING;
        String monthOfYear = UnAvails.NOTAVAILSTRING;
        int weekOfMonth = UnAvails.NOTAVAILINT;
        int dayOfMonth = UnAvails.NOTAVAILINT;
        int weekend1 = UnAvails.NOTAVAILINT;
        int weekend2 = UnAvails.NOTAVAILINT;
        dtUserPays = new Dates[Limits.MAXUSERDATES];
        int noUserSchedDts = 0;
        char NOTRUCROUND = 'N';
        String schedType = UnAvails.NOTAVAILSTRING;
        String country = UnAvails.NOTAVAILSTRING;
        String timeUnit = UnAvails.NOTAVAILSTRING;
        dtPriorPay.clear();
        dt1stPay.clear(); // date of 1sr intrest payment defines odd first perios
        dtLastOnlyCoupon.clear(); // last coupon payment before maturity day
        dtOccur.clear();
        dtNextUserCpn.clear();
        dtLastUserCpn.clear(); // user specufued last coupon date to use in acrued interest
        DayCount.DCMethod dayCountMeth = DayCount.DCMethod.DC_NA;
        int repeatFactor = UnAvails.NOTAVAILINT;
        int compoundFreq = UnAvails.NOTAVAILINT;
        holidayRule = UnAvails.NOTAVAILSTRING;

    }

    public int mapCompoundFreq(String sCompoundFreq) {
        int freq = 0;
        switch (sCompoundFreq) {
            case "ANNUAL": // annual
                return 1;
            case "SEMI":
                return 2;
            case "TRI":
                return 3;
            case "QUARTERLY":
                return 4;
            case "BIMONTHLY":
                return 6;
            case "MONTHLY":
                return 12;
            default:
                this.error.setError(getClass().getName(), "Invalid frequncy " + sCompoundFreq + "detected");
        }
        return UnAvails.NOTAVAILINT;
    }

    private boolean isExDividend(Dates dtSettle, Dates dtExDiv) {
        if (UnAvails.isUnAvail(dtExDiv))
            return false;
        Dates dtLast = getNextSchedDate(BondConsts.BEFOREONDATE, dtSettle);
        Dates dtNext = getNextSchedDate(BondConsts.AFTERONDATE, dtSettle);
        return dtExDiv.geDate(dtExDiv, dtSettle) && dtExDiv.leDate(dtExDiv, dtNext);
    }

    public double periodPerYear() {
        double periodPerYear = UnAvails.NOTAVAILDOUBLE;
        switch (timeUnit) {
            case BondConsts.MONTHTIMEUNIT:
                periodPerYear = compoundFreq;
                break;
            case BondConsts.WEEKTIMEUNIT:
                periodPerYear = CalUnits.WEEKSPERYEAR / repeatFactor;
                break;
            case BondConsts.DAYTIMEUNIT:
                periodPerYear = dayCount.getDaysInYear(dtMaturity) / repeatFactor;
                break;
            default:
                error.setError(getClass().getName(), "Invalid time unit " + timeUnit + " detected");
                break;
        }
        return periodPerYear;
    }

    // The number of coupon periods between ant two arbitrary dates
    //
    public double getCouponFactor(Dates dt1, Dates dt2) {
        // dt1 = from date
        // dt2 = to date
        Dates dt1B = new Dates();
        Dates dt1A = new Dates();
        Dates dt2B = new Dates();
        Dates dt2A = new Dates();
        double daysNum = 0;
        double daysDen = 0;

        dt1B = getNextSchedDate(BondConsts.BEFOREONDATE, dt1); // last coupon date before start of range dt1
        dt1A = getNextSchedDate(BondConsts.AFTERDATE, dt1); // next coupon date after start of range dt1
        dt2B = getNextSchedDate(BondConsts.BEFOREONDATE, dt2); // last coupon date before start of range dt2
        dt2A = getNextSchedDate(BondConsts.AFTERDATE, dt2); // next coupon date after start of range dt1
        // are the dates equal
        if (dt2.eqDate(dt2, dt1))
            return 0.;
        // in the same period
        if (dt2.leDate(dt2, dt1A)) { // within a single coupon period
            daysNum = dayCount.getDaycount(dt1, dt2, dtMaturity, dayCountMeth);
            daysDen = dayCount.getDaycount(dt1B, dt1A, dtMaturity, dayCountMeth);
            double perCount = 0.;
            if (daysDen != 0.)
                perCount = daysNum / daysDen;
            return perCount;
        }
        // adjacent periods
        if (dt1A.eqDate(dt1A, dt2B)) { // end of first period coincides with start of second coupon period
            // dt1 -> dt1A + dt1A-> dt2
            daysNum = dayCount.getDaycount(dt1, dt1A, dtMaturity, dayCountMeth);
            daysDen = dayCount.getDaycount(dt1, dt1A, dtMaturity, dayCountMeth);
            double perCount1 = 0.;
            if (daysDen != 0.)
                perCount1 = daysNum / daysDen;
            daysNum = dayCount.getDaycount(dt2B, dt2, dtMaturity, dayCountMeth);
            daysDen = dayCount.getDaycount(dt2B, dt2A, dtMaturity, dayCountMeth);
            double perCount2 = 0.;
            if (daysDen != 0.)
                perCount2 = daysNum / daysDen;
            return perCount1 + perCount2;
        }
        // across multiple periods
        daysNum = dayCount.getDaycount(dt1, dt1A, dtMaturity, dayCountMeth);
        daysDen = dayCount.getDaycount(dt1, dt1A, dtMaturity, dayCountMeth);
        double perCount1 = 0.;
        if (daysDen != 0.)
            perCount1 = daysNum / daysDen;
        int wholePer = 0;
        Dates dtNext = new Dates();
        dtNext = dt1A;
        wholePer = getWholeCouponPeriods(dt1A, dt2B);
        daysNum = dayCount.getDaycount(dt2B, dt2, dtMaturity, dayCountMeth);
        daysDen = dayCount.getDaycount(dt2B, dt2A, dtMaturity, dayCountMeth);
        double perCount2 = 0.;
        if (daysDen != 0.)
            perCount2 = daysNum / daysDen;
        double perCount = perCount1 + wholePer + perCount2;
        return perCount;
    }

    public double getCouponFactorXX(DayCount.DCMethod dcMethod, Dates dtFrom, Dates dtTo) {
        double num = 0., den = 0., couponFac = 0.;
        Dates dtLastCpn = new Dates();
        Dates dtNextCpn = new Dates();
        dtLastCpn = getNextSchedDate(BondConsts.BEFOREONDATE, dtFrom);
        dtNextCpn = getNextSchedDate(BondConsts.AFTERDATE, dtLastCpn);
        switch (dcMethod) {
            case DC_30A:
                num = dayCount.getDaycountDC30A(dtFrom, dtTo);
                den = dayCount.getDaysInYear(DayCount.DCMethod.DC_30A, dtFrom, dtTo, compoundFreq) / compoundFreq;
            case DC_30:
                num = dayCount.getDayCountDC30(dtFrom, dtTo);
                den = dayCount.getDaysInYear(DayCount.DCMethod.DC_30, dtFrom, dtTo, compoundFreq) / compoundFreq;
                break;
            case DC_30E:// ICMA, Special German
                num = dayCount.getDayCountDC30E(dtFrom, dtTo);
                den = dayCount.getDaysInYear(DayCount.DCMethod.DC_30E, dtFrom, dtTo, compoundFreq) / compoundFreq;
                break;
            case DC_30EE:
                num = dayCount.getDaycountEE(dtFrom, dtTo);
                den = dayCount.getDaysInYear(DayCount.DCMethod.DC_30EE, dtFrom, dtTo, compoundFreq) / compoundFreq;
                break;
            case DC_30ISDA:
                num = dayCount.getDayCountDC30ISDA(dtFrom, dtTo, dtMaturity);
                den = dayCount.getDaysInYear(DayCount.DCMethod.DC_ACTISDA, dtFrom, dtTo, compoundFreq) / compoundFreq;
                break;
            case DC_BUS:
                num = dayCount.getDaycountDCBus(dtFrom, dtTo);
                den = dayCount.getDaycountDCBus(dtLastCpn, dtNextCpn);
                break;
            // Business days
            case DC_ACT: // same as ACt ICMA
                num = dayCount.getDaycountDCACT(dtFrom, dtTo);
                den = dayCount.getDaycountDCACT(dtLastCpn, dtNextCpn);
                break;// Actual days
            case DC_ACTISDA:
                num = dayCount.getCouponFactorISDA(dtFrom, dtTo);
                den = 1.;
                break;
            case DC_365:
                num = dayCount.getDaycountDC365(dtFrom, dtTo);
                den = 365. / compoundFreq;
                break;
            case DC_365L:
                Dates dtFeb29 = new Dates();
                dtFeb29.month = 2;
                dtFeb29.day = 29;
                dtFeb29.year = dtFrom.year;
                num = dayCount.getDaycountDCACT(dtFrom, dtTo) * compoundFreq;
                if (compoundFreq > 1) {
                    if (dtFrom.leDate(dtFrom, dtFeb29) && dtTo.gtDate(dtTo, dtFeb29)) {
                        den = 366;
                    } else
                        den = 365;
                } else {
                    if (dtTo.isLeapYear(dtTo.year))
                        den = 366;
                    else
                        den = 365;
                }
                den /= compoundFreq;
                break;// ignore leap year
            case DC_364:
                num = dayCount.getDaycountDCACT(dtFrom, dtTo);
                den = 364. / compoundFreq;
                break;// ISDA uses constant accrual
            case DC_ACT360:
                num = dayCount.getDaycountDCACT(dtFrom, dtTo);
                den = 360. / compoundFreq;
            default:
                String errmsg = "Invalid accrual basis detected";
                error.setError(getClass().getName(), errmsg);
                num = 0.;
                den = 1.;
                break;// not available
        }
        couponFac = num / den;
        return couponFac;
    }

    public int getWholeCouponPeriods(Dates dtFromCpn, Dates dtToCoupon) {
        Dates dtNextCpn = new Dates();
        Dates dtLastCpn = new Dates();
        int noPeriods = 0;
        dtNextCpn = dtFromCpn;
        while (dtNextCpn.ltDate(dtNextCpn, dtToCoupon)) {
            ++noPeriods;
            dtLastCpn = dtNextCpn;
            dtNextCpn = getNextSchedDate(BondConsts.AFTERDATE, dtLastCpn);
        }
        return noPeriods;
    }

    public double getTermInYearsFromSchedule() {
        double yrs = 0.;
        switch (timeUnit) {
            case BondConsts.MONTHTIMEUNIT:
                yrs = (double) repeatFactor / CalUnits.monthsInYear;
                break;
            case BondConsts.WEEKTIMEUNIT:
                yrs = (double) repeatFactor / CalUnits.WEEKSPERYEAR;
                break;
            case BondConsts.DAYTIMEUNIT:
                yrs = (double) repeatFactor / CalUnits.daysInNonLeapYear;
                break;
            default:
                String errmsg = "Invalid periodicity " + timeUnit + " specified";
                error.setError(getClass().getName(), errmsg);
                break;
        }
        return yrs;
    }

    public Dates getNextSchedDate(int beforeAfter, Dates dtFrom) {
        Dates dtSched = new Dates();
        switch (timeUnit) {
            case BondConsts.MONTHTIMEUNIT:
                dtSched = getNextMonthlyDate(beforeAfter, dtFrom);
                break;
            case BondConsts.WEEKTIMEUNIT:
                dtSched = getNextWeeklyDate(beforeAfter, dtFrom);
                break;
            case BondConsts.DAYTIMEUNIT:
                dtSched = getNextDailySchedule(beforeAfter, dtFrom);
                break;
            case BondConsts.ENUMERATEDTIMEUNIT:
                dtSched = getNextEnumeratedSchedule(beforeAfter, dtFrom);
                break;
            case BondConsts.NOPERIODICITY:
                dtSched = dtMaturity;
                break;
            default:
                error.setError(getClass().getName(),
                        "Could not obtain the next/last schedule date because of invalid DAYTIME unit");
                break;
        }
        return dtSched;
    }

    /*
     * Method: getNextMonthlyDate
     * Purpose: Returns the next or last schedule date around a given date
     * Author: AHR
     * Date: 5/21/2020
     * Comment: Calls getNextSchedMonthlyDate to get the next/last scheduled date
     * 
     **/
    private Dates getNextMonthlyDate(int beforeAfter, Dates dtFrom) {
        Dates dtSched = new Dates();
        dtSched = getNextSchedMonthDate(beforeAfter, dtFrom);
        dtSched = adjustSchedDateOddness(beforeAfter, dtSched, dtFrom);
        if (holidayRule == null || holidayRule == UnAvails.NOTAVAILSTRING) {
            dtPriorPay = dtSched;
            return dtSched;
        }
        dtSched = getNonHolidayDate(holidayRule, dtSched, country);
        dtPriorPay = dtSched;
        return dtSched;
    }

    private Dates getNextSchedMonthDate(int beforeAfter, Dates dtFrom) {
        Dates dtSched = new Dates();
        int noMonths = 0, noWholePer = 0, noFracPer = 0;
        int periodOffset = 0;
        boolean isOccurAfter = true;
        noMonths = (int) time.getMonthsBetween(dtFrom, dtOccur, true);
        noWholePer = noMonths / repeatFactor;
        noFracPer = Math.abs(noMonths) % repeatFactor;
        periodOffset = noWholePer * repeatFactor;
        dtSched = time.addMonthsToDate(dtOccur, -periodOffset, useEndOfMonthRuleYn);
        switch (beforeAfter) {
            case BondConsts.BEFOREDATE:
                if (dtSched.geDate(dtSched, dtFrom))
                    dtSched = time.addMonthsToDate(dtSched, -repeatFactor, useEndOfMonthRuleYn);
                break;
            case BondConsts.AFTERDATE:
                if (dtSched.leDate(dtSched, dtFrom))
                    dtSched = time.addMonthsToDate(dtSched, repeatFactor, useEndOfMonthRuleYn);
                break;
            case BondConsts.BEFOREONDATE:
                if (dtSched.gtDate(dtSched, dtFrom))
                    dtSched = time.addMonthsToDate(dtSched, -repeatFactor, useEndOfMonthRuleYn);
                break;
            case BondConsts.AFTERONDATE:
                if (dtSched.ltDate(dtSched, dtFrom))
                    dtSched = time.addMonthsToDate(dtSched, repeatFactor, useEndOfMonthRuleYn);
                break;
            default:
                String errmsg = "You cannot request an schedule date with the 'ON' option";
                error.setError(getClass().getName(), errmsg);
                break;
        }
        return dtSched;
    }

    public int getHolidayIndex(Dates dtDate, String countryExchange) {
        int nohol = holidays.noHolidays, ih = -1;

        while (++ih < nohol) {
            if (holidays.holidayCal[ih].countryExchange.equals(countryExchange))
                if (holidays.holidayCal[ih].dtHoliday.eqDate(dtDate, holidays.holidayCal[ih].dtHoliday))
                    return ih;
        }
        return UnAvails.NOTAVAILINT;
    }

    public Dates getNonHolidayBefore(int hIndex, Dates dtDate, String countryExchange) {
        Dates dtHol, dtNonHol, dtLast;
        int dayOfWeekNum = UnAvails.NOTAVAILDATE, dayGap = 0;
        String dayOfWeekString = null;
        boolean isWeekend = true;
        dtNonHol = new Dates(dtDate);
        // start loop to get next/last holidays and non weekends where there is a
        // business dat gap
        int ih = hIndex;
        dtLast = dtDate;
        char use360Yn = 'N';
        while (--ih < 0 && dayGap < 2 || isWeekend) {
            // public Dates getNextHoliday(int index, String holOffset, String
            // countryExchange) {
            dtHol = holidays.getNextHoliday(ih, BondConsts.AFTERHOLIDAY, countryExchange);
            if (dtHol.year == UnAvails.NOTAVAILDATE) {
                dtNonHol = time.addDaysToDate(dtLast, -1, use360Yn);
                dtNonHol = getNextNonWeekEnd(dtNonHol, error.BSYES);
                return dtNonHol;
            }
            dayGap = dayCount.getDaycount(dtDate, dtHol, dtMaturity, DayCount.DCMethod.DC_ACT);
            dtNonHol = time.addDaysToDate(dtLast, -1, use360Yn);
            isWeekend = dtNonHol.isWeekend(dtNonHol);
            dtLast = dtHol;
        }
        return dtNonHol;
    }

    public Dates getNonHolidayBeforeAfter(String holidayOffset, Dates dtDate, String countryExchange) {
        if (holidayOffset.equals(BondConsts.IGNOREHOLIDAYS))
            return dtDate;
        Dates dtNonHol;
        char use360Yn = 'N';
        int dayOfWeekNum = UnAvails.NOTAVAILDATE;
        String dayOfWeekString;
        int hIndex = getHolidayIndex(dtDate, countryExchange);
        if (hIndex == UnAvails.NOTAVAILINT)
            return dtDate;
        // start loop to get next/last holidays and non weekends where there is a
        // business dat gap
        dtNonHol = holidays.getNextHoliday(hIndex, holidayOffset, countryExchange);
        if (dtNonHol.year == UnAvails.NOTAVAILDATE)
            return time.addDaysToDate(dtNonHol, 1, use360Yn); // go the the next date as at is a non holiday be default
        // get holiday gap
        if (holidayOffset.equals(BondConsts.AFTERHOLIDAY))
            dtNonHol = getNonHolidayAfter(hIndex, dtDate, countryExchange);
        else
            dtNonHol = getNonHolidayBefore(hIndex, dtDate, countryExchange);
        return dtNonHol;
    }

    public Dates getNonHolidayAfter(int hIndex, Dates dtDate, String countryExchange) {
        Dates dtHol, dtLast, dtNonHol;
        int dayOfWeekNum = UnAvails.NOTAVAILDATE, dayGap = 0;
        String dayOfWeekString = null;
        boolean isWeekend = true;
        char use360Yn = 'N';
        dtNonHol = new Dates(dtDate);

        // start loop to get next/last holidays and non weekends where there is a
        // business dat gap
        int ih = hIndex;
        dtLast = dtDate;
        while (++ih < holidays.holidayCal.length && dayGap < 2 || isWeekend) {
            dtHol = holidays.getNextHoliday(ih, BondConsts.AFTERHOLIDAY, countryExchange);
            if (dtHol.year == UnAvails.NOTAVAILDATE) {
                dtNonHol = time.addDaysToDate(dtLast, 1, use360Yn);
                dtNonHol = getNextNonWeekEnd(dtNonHol, error.BSYES);
                return dtNonHol;
            }
            dayGap = dayCount.getDaycount(dtDate, dtHol, dtMaturity, DayCount.DCMethod.DC_ACT);
            dtNonHol = time.addDaysToDate(dtLast, 1, use360Yn);
            isWeekend = dtNonHol.isWeekend(dtNonHol);
            dtLast = dtHol;
        }
        return dtNonHol;
    }

    public Dates getNonHolidayDate(String holidayOffset, Dates dtDate, String countryExchange) {
        Dates dtNonHol = getNonHolidayBeforeAfter(holidayOffset, dtDate, countryExchange);
        if (holidayOffset.equals(BondConsts.AFTERHOLIDAYSAMEMONTH)) {
            if (dtDate.month == dtNonHol.month) {
                dtNonHol = getNonHolidayBeforeAfter(BondConsts.BEFOREHOLIDAY, dtDate, countryExchange);
            }
        }
        return dtNonHol;
    }

    public Dates getNextNonWeekEnd(Dates dtDate, char afterYn) {
        boolean isWE = dtDate.isWeekend(dtDate);
        Dates dtNonWe;
        char use360Yn = 'N';
        if (!isWE)
            return dtDate;
        int nextDayNum1;
        if (afterYn == error.BSYES) {
            nextDayNum1 = (weekend1 + 1) % 7;
            if (nextDayNum1 == weekend2)
                dtNonWe = time.addDaysToDate(dtDate, 2, use360Yn);
            else
                dtNonWe = time.addDaysToDate(dtDate, 1, use360Yn);
            return dtNonWe;
        }
        nextDayNum1 = (weekend1 + -1) % 7;
        if (nextDayNum1 == weekend2)
            dtNonWe = time.addDaysToDate(dtDate, -2, use360Yn);
        else
            dtNonWe = time.addDaysToDate(dtDate, -1, use360Yn);
        return dtNonWe;
    }

    private int getMonthCount(Dates dtFrom, Dates dtTo, char roundTrunc) {
        int noMonths, noDays, monthFraction;
        double fac;

        noMonths = CalUnits.MONTHSPERYEAR * (dtTo.year - dtFrom.year) + (dtTo.month - dtFrom.month);
        noDays = dtTo.day - dtFrom.day;
        if (roundTrunc == NOTRUCROUND) {
            if (noDays < 0)
                --noMonths;
            return noMonths;
        }
        fac = 0.5 + noDays / (double) CalUnits.daysInMonthLeap[dtFrom.day];
        if (roundTrunc == error.BSYES)
            monthFraction = (int) Math.round(fac);
        else
            monthFraction = (int) Math.floor(fac);
        noMonths += monthFraction;
        return noMonths;
    }

    private Dates adjustSchedDateOddness(int beforeAfter, Dates dtSchedUn, Dates dtFrom) {
        Dates dtSched = new Dates();
        // next schedule date is a short odd coupomn
        if (beforeAfter == BondConsts.BEFOREDATE && dt1stPay.year != UnAvails.NOTAVAILDATE) {
            if (dtSchedUn.ltDate(dt1stPay))
                return dt1stPay;
            else
                return dtSchedUn;
        }
        // next schedule date is a short odd last coupon
        if (beforeAfter == BondConsts.AFTERONDATE && dt1stPay.year != UnAvails.NOTAVAILDATE) {
            if (dtLastUserCpn.gtDate(dtSchedUn))
                return dtSchedUn;
            else
                return dtLastUserCpn;
        }
        // next schedule date is a short odd last coupon
        if (beforeAfter == BondConsts.AFTERONDATE && dtLastOnlyCoupon.year != UnAvails.NOTAVAILDATE) {
            if (dtLastOnlyCoupon.gtDate(dtSchedUn))
                return dtMaturity;
            else
                return dtSchedUn;
        }
        if (beforeAfter == BondConsts.ONDATE) {
            if (dtFrom.eqDate(dtSchedUn, dtFrom)) {
                return dtSchedUn;
            } else {
                dtSched.setUnavail();
            }
        }
        return dtSchedUn;
    }

    public Dates getNextDailySchedule(int beforeAfter, Dates dtFrom) {
        Dates dtSched = new Dates();
        int days = 0, noWholePer = 0;
        char use360Yn = 'N';
        int periodOffset = 0;
        days = time.getDaysBetweenDates(dtFrom, dtOccur);
        noWholePer = days / repeatFactor;
        periodOffset = noWholePer * repeatFactor;
        if (dtFrom.gtDate(dtFrom, dtOccur))
            periodOffset = -noWholePer * repeatFactor;
        dtSched = time.addDaysToDate(dtFrom, periodOffset, useEndOfMonthRuleYn);
        if (beforeAfter == BondConsts.AFTERDATE) {
            if (dtSched.gtDate(dtSched, dtFrom))
                return dtSched;
            else {
                dtSched = time.addDaysToDate(dtSched, repeatFactor, useEndOfMonthRuleYn);
                return dtSched;
            }
        }
        if (beforeAfter == BondConsts.AFTERONDATE) {
            if (dtSched.geDate(dtSched, dtFrom))
                return dtSched;
            else {
                dtSched = time.addDaysToDate(dtSched, repeatFactor, useEndOfMonthRuleYn);
                return dtSched;
            }
        }
        if (beforeAfter == BondConsts.BEFOREONDATE) {
            if (dtSched.leDate(dtSched, dtFrom))
                dtSched = time.addDaysToDate(dtSched, -repeatFactor, useEndOfMonthRuleYn);
            else {
                dtSched = time.addDaysToDate(dtSched, -repeatFactor, useEndOfMonthRuleYn);
                return dtSched;
            }
        }
        if (beforeAfter == BondConsts.BEFOREDATE) {
            if (dtSched.ltDate(dtSched, dtFrom))
                return dtSched;
            else {
                dtSched = time.addDaysToDate(dtSched, -repeatFactor, useEndOfMonthRuleYn);
                return dtSched;
            }
        }
        return dtSched;
    }

    private Dates getNextWeeklyDate(int beforeAfter, Dates dtFrom) {
        Dates dtSched = new Dates();
        int noWeeks = time.getWeeksBetween(dtFrom, dtOccur);
        int noWholePeriods = noWeeks / repeatFactor;
        int periodOffset = noWholePeriods;
        if (dtFrom.gtDate(dtFrom, dtOccur))
            periodOffset = -noWholePeriods;
        dtSched = time.addWeeksToDate(dtFrom, periodOffset);
        if (beforeAfter == BondConsts.AFTERDATE) {
            if (dtSched.gtDate(dtSched, dtFrom))
                return dtSched;
            else {
                dtSched = time.addWeeksToDate(dtSched, repeatFactor);
                return dtSched;
            }
        }
        if (beforeAfter == BondConsts.AFTERONDATE) {
            if (dtSched.geDate(dtSched, dtFrom))
                return dtSched;
            else {
                dtSched = time.addWeeksToDate(dtSched, repeatFactor);
                return dtSched;
            }
        }
        if (beforeAfter == BondConsts.BEFOREONDATE) {
            if (dtSched.leDate(dtSched, dtFrom))
                return dtSched;
            else {
                dtSched = time.addWeeksToDate(dtSched, -repeatFactor);
                return dtSched;
            }
        }
        if (beforeAfter == BondConsts.BEFOREDATE) {
            if (dtSched.ltDate(dtSched, dtFrom))
                return dtSched;
            else {
                dtSched = time.addWeeksToDate(dtSched, -repeatFactor);
                return dtSched;
            }
        }
        return dtSched;
    }

    private Dates getNextEnumeratedSchedule(int beforeAfter, Dates dtFrom) {
        Dates dtUnAvail = new Dates();
        boolean more = true;
        int ns = -1;
        if (beforeAfter == BondConsts.BEFOREDATE) {
            ns = noUserSchedDts;
            while (--ns >= 0) {
                if (dtUserPays[ns].ltDate(dtUserPays[ns], dtFrom)) {
                    return dtUserPays[ns];
                }
            }
            String errmsg = "no user payment date was found before " + dtFrom.getDateString();
            error.setError(getClass().getName(), errmsg);
            return dtUnAvail;
        }
        if (beforeAfter == BondConsts.AFTERDATE) {
            ns = -1;
            while (++ns < noUserSchedDts) {
                if (dtUserPays[ns].gtDate(dtUserPays[ns], dtFrom)) {
                    return dtUserPays[ns];
                }
            }
            String errmsg = "no user payment date was found after " + dtFrom.getDateString();
            error.setError(getClass().getName(), errmsg);
            return dtUnAvail;
        }
        if (beforeAfter == BondConsts.ONDATE) {
            ns = -1;
            while (++ns < noUserSchedDts) {
                if (dtUserPays[ns].eqDate(dtUserPays[ns], dtFrom))
                    return dtUserPays[ns];
            }
            String errmsg = "  no user payment date found on " + dtFrom.getDateString();
            error.setError(getClass().getName(), errmsg);
            return dtUnAvail;
        }
        if (beforeAfter == BondConsts.BEFOREONDATE) {
            ns = noUserSchedDts;
            while (--ns >= 0) {
                if (dtUserPays[ns].leDate(dtUserPays[ns], dtFrom)) {
                    return dtUserPays[ns];
                }
            }
            String errmsg = "  no user payment date found on or before " + dtFrom.getDateString();
            error.setError(getClass().getName(), errmsg);
            return dtUnAvail;
        }
        if (beforeAfter == BondConsts.AFTERONDATE) {
            ns = -1;
            while (++ns < noUserSchedDts) {
                if (dtUserPays[ns].geDate(dtUserPays[ns], dtFrom)) {
                    return dtUserPays[ns];
                }
            }
            String errmsg = "  no user payment date found on or before " + dtFrom.getDateString();
            error.setError(getClass().getName(), errmsg);
            return dtUnAvail;
        }
        return dtUnAvail;
    }

    public void initialize(Bond bond) {
        this.dtOccur = new Dates(error);
        this.timeUnit = UnAvails.NOTAVAILSTRING;
        this.dtPriorPay.year = UnAvails.NOTAVAILINT;
        this.dtMaturity = new Dates(error);
        this.dtLastOnlyCoupon = new Dates(error);
        this.compoundFreq = UnAvails.NOTAVAILINT;
        this.dtLastOnlyCoupon = new Dates(error);
        this.repeatFactor = UnAvails.NOTAVAILINT;
        this.dt1stPay = new Dates(error);
        this.useEndOfMonthRuleYn = UnAvails.NOTAVAILCHAR;
        this.holidayRule = UnAvails.NOTAVAILSTRING;
        this.schedType = UnAvails.NOTAVAILSTRING;
        this.dtDated = new Dates(error);
        this.monthOfYear = UnAvails.NOTAVAILSTRING;
        this.weekend1 = UnAvails.NOTAVAILINT;
        this.weekend2 = UnAvails.NOTAVAILINT;
        this.weekOfMonth = UnAvails.NOTAVAILINT;
        this.country = UnAvails.NOTAVAILSTRING;
        int ns = -1;
        while (++ns < bond.noUserPayDts) {
            this.dtUserPays[ns] = bond.dtUserPays[ns];
            ++noUserSchedDts;
        }
    }

}

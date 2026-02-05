package app;

import java.util.ArrayList;

public class Discount {
    Bond bond;
    CashflowResults cashflowResults;
    ArrayList<CashflowResults> cashflowList = new ArrayList<>();
    CalcResults calcResults;
    DayCount dayCount;
    CalcDef calcDef;
    Error error;
    // CalcResults calcResults;

    Discount(Bond bond, CashflowResults cashflowResults) {
        this.bond = bond;
        this.calcDef = new CalcDef();
        this.cashflowResults = cashflowResults;
        error = new Error();
        this.dayCount = new DayCount(error);
        String timeUnit = BondConsts.MONTHTIMEUNIT;
        int repeatFactor = CalUnits.SEMIANNUALREPEAT;
        int compoundFreq = CalUnits.SEMIANNUALFQ;
        Dates dtMature = bond.dtMaturity;
        Dates dtOccur = bond.dtMaturity;
        Dates dt1stPay = new Dates(bond.error);
        Dates dtOddLast = new Dates(bond.error);
        char useEndOfMonthRuleYn = 'Y';
        String holidayOffsetRule = BondConsts.IGNOREHOLIDAYS;
        // schedRule = new SchedRule(bond, timeUnit, repeatFactor, compoundFreq,
        // dtMature, dtOccur, dt1stPay, dtOddLast,
        // useEndOfMonthRuleYn, holidayOffsetRule);

    }

    public double accruedInterest(int calcNo, Dates settle) {
        return 0.;
    }

    public double priceYield(int calcNo, Dates dtSettle, double settleYld, Dates dtRedeem, double redeemPrin,
            double redeemInt) {
        double price = 0.;
        double perLen = dayCount.simplePeriodCount(dtSettle, dtRedeem, CalUnits.SEMIANNUALREPEAT);
        double dsm = dayCount.getDaycount(dtSettle, dtRedeem, dtRedeem, bond.dcAccruedInt);
        double dyr = dayCount.getDaysInYear(bond.dcAccruedInt, dtSettle, dtRedeem, 1);
        double factor = 1. + settleYld * dsm / dyr;
        perLen = dayCount.simplePeriodCount(dtSettle, dtRedeem, CalUnits.SEMIANNUALREPEAT);
        if (perLen > CalcEnums.USEDISCOUNTSIMPLEYIELDPERIOD) {
            price = redeemPrin / Math.pow(1. + settleYld / CalUnits.SEMIANNUALFQ, perLen);
            return price;
        } else {
            if (bond.yieldQuoteType.equals(CalcEnums.TREASURYYIELD))
                price = redeemPrin * (1. - settleYld * dsm / dyr);
            else if (bond.yieldQuoteType.equals(CalcEnums.SIMPLEYIELD))
                price = redeemPrin / (1 + settleYld * dsm / dyr);
        }
        return price;
    }

    public double yieldPrice(int calcNo, Dates dtSettle, double settlePrc, Dates dtRedeem, double redeemPrin,
            double redeemInt) {
        int quoteType = 0;
        double ytm = 0;
        int dayNum = 0;
        double dsm = dayCount.getDaycount(dtSettle, dtRedeem, bond.dtMaturity, bond.dcAccruedInt);
        double dyr = dayCount.getDaysInYear(bond.dcAccruedInt, dtSettle, dtRedeem, 1);
        double perLen = dayCount.simplePeriodCount(dtSettle, dtRedeem, CalUnits.SEMIANNUALREPEAT);
        if (perLen >= CalcEnums.USEDISCOUNTSIMPLEYIELDPERIOD) { // definition of short period must be before auto
                                                                // compounding
            ytm = CalUnits.SEMIANNUALFQ * (-1 + Math.pow(redeemPrin / settlePrc, 1. / perLen));
            return ytm;
        } else {
            if (bond.yieldQuoteType.equals(CalcEnums.TREASURYYIELD))
                ytm = (dyr / dsm) * (redeemInt - settlePrc) / redeemInt;
            else if (bond.yieldQuoteType.equals(CalcEnums.SIMPLEYIELD))
                ytm = (dyr / dsm) * (redeemInt - settlePrc) / settlePrc;
        }
        return ytm;

    }

    public double yieldWorst(int calcNo, Dates dtSettle, double settlePrc, Dates dtRedeem, double redeemPrin,
            double redeemInt) {
        double ytw, ytc;
        int cno = -1;
        int noCalls = bond.noCallDts;
        ytw = 9999999999.99;
        ytw = yieldPrice(calcNo, dtSettle, settlePrc, dtRedeem, redeemPrin, redeemInt);
        while (++cno < noCalls) {
            ytc = yieldPrice(calcNo, dtSettle, settlePrc, bond.dtCall[cno], bond.callPrc[cno], bond.redeemInt);
            if (ytc < ytw) {
                ytw = ytc;
                calcResults.dtWorst = bond.dtCall[cno];
                calcResults.yieldToWorst = ytw;
            }
        }
        return ytw;
    }

    public double matLenYearsWtd(int calcNo, Dates dtSettle, Dates dtRedeem) {
        double matLen = 0;
        int days = dayCount.getDaycount(dtSettle, dtRedeem, bond.dtMaturity, DayCount.DCMethod.DC_ACT);
        double years = bond.time.daysToYears(days, dtSettle, CalcList.MATLENYEARS);
        return years;
    }

    public int matLenDaysWtd(int calcNo, Dates dtSettle, Dates dtRedeem) {
        int matLen = 0;
        int days = dayCount.getDaycount(dtSettle, dtRedeem, bond.dtMaturity, DayCount.DCMethod.DC_ACT);
        return days;
    }

    public double macDuration(int calcNo, Dates dtSettle, Dates dtRedeem) {
        int matDays = dayCount.getDaycount(dtSettle, dtRedeem, bond.dtMaturity, DayCount.DCMethod.DC_ACT);
        double years = bond.time.daysToYears(matDays, dtSettle, CalUnits.calcYearPrecise);
        return years;
    }

    public double modDurationSIA(int calcNo, Dates dtSettle, Dates dtRedeem) {
        int matDays = dayCount.getDaycount(dtSettle, dtRedeem, dtRedeem, DayCount.DCMethod.DC_ACT);
        double years = bond.time.daysToYears(matDays, dtSettle, CalUnits.calcYearPrecise);
        return years;
    }

    public double modDurationSTD(int calcNo, Dates dtSettle, double yield, Dates dtRedeem, double redeemPrin,
            double redeemInt) {
        double prcUp, prcDn, prc, yldShift = .5 * CalUnits.durationYieldShift;
        prc = priceYield(calcNo, dtSettle, yield, dtRedeem, redeemPrin, redeemInt);
        prcDn = priceYield(calcNo, dtSettle, yield + yldShift, dtRedeem, redeemPrin, redeemInt);
        prcUp = priceYield(calcNo, dtSettle, yield - yldShift, dtRedeem, redeemPrin, redeemInt);
        double modDur = (prcUp - prcDn) / (CalUnits.durationYieldShift * prc);
        return modDur;
    }

    public double convexity(int calcNo, Dates dtSettle, double yield, Dates dtRedeem, double redeemPrin,
            double redeemInt, String yieldQuoteType) {
        double cvex = 0., yldShift = CalUnits.durationYieldShift;
        double prcUp, prcDn, prc;
        prc = priceYield(calcNo, dtSettle, yield, dtRedeem, redeemPrin, redeemInt);
        prcDn = priceYield(calcNo, dtSettle, yield + yldShift, dtRedeem, redeemPrin, redeemInt);
        prcUp = priceYield(calcNo, dtSettle, yield - yldShift, dtRedeem, redeemPrin, redeemInt);
        cvex = (prcDn + prcUp - 2. * prc) / (2. * prc * yldShift * yldShift);
        return cvex; // need tp scale by 10000
    }

    public double interestIncome(int calcNo, Dates dtFrom, Dates dtTo) {
        double incInc = 0.;
        incInc = (bond.redeemPrin - bond.settlePrc) * bond.intSchedRule.getCouponFactor(dtFrom, dtTo);
        return incInc;
    }

    public double futureCashflow(int calcNo, Dates dtSettle, double redeemPrin) {
        return redeemPrin;
    }

    public ArrayList<CashflowResults> cashflow(int calcNo, int securityNo, Dates dtSettle, Dates dtRedeem,
            double redeemPrin) {
        int dayNum = 0;
        double intFlow = 0.;
        double totPrinflow = 0.;
        double totCashflow = 0.;
        double intInc = 0.;
        Dates dtLast = new Dates();
        Dates dtNext = new Dates();
        double fedTaxPay = 0.;
        double stateTaxPay = 0.;
        // todo check if cashflow might already be calculated
        SchedTrack schedTrack = new SchedTrack();
        dtLast = dtSettle;
        if (bond.afterTaxCalcsYn == 'N') {
            // is it tax time
            SchedTrack taxTrack = bond.schedTracks.getSchedTrackOnAfter(dtRedeem, SchedTrack.FEDTAXPAYMENTTRACK);
            if (taxTrack.found) {
                fedTaxPay = (bond.redeemPrin - bond.purPrc) * bond.taxRtFedIncome;
                stateTaxPay = (bond.redeemPrin - bond.purPrc) * bond.taxRtStateIncome;
            } else {
                String warnMesg = "No tax dates were specified when rewuesting after tax cashflows";
                bond.error.setWarning(getClass().getName(), warnMesg);
                bond.error.clearError();
            }
        }
        double years = dayCount.matLenYears(calcNo, dtSettle, dtRedeem, bond.dtMaturity);
        char isHolidayYn;
        double perLen = dayCount.simplePeriodCount(dtSettle, dtRedeem, CalUnits.SEMIANNUALREPEAT);
        double schedPrinflow = redeemPrin;
        totPrinflow = redeemPrin;
        double prePayflow = 0.;
        double intflow = 0.;
        schedTrack = new SchedTrack();
        schedTrack = bond.schedTracks.getSchedTrackOn(bond.dtMaturity, SchedTrack.FEDTAXPAYMENTTRACK);
        if (schedTrack.found) {
            dtNext = schedTrack.schedDate;
            intInc = interestIncome(calcNo, dtLast, dtNext);
            fedTaxPay = -intInc * bond.taxRtFedIncome;
            stateTaxPay = -intInc * bond.taxRtStateIncome;
        } else {
            fedTaxPay = 0.;
            stateTaxPay = 0.;
        }
        totCashflow = redeemPrin;
        Dates dtActRedeem = bond.holidays.getNonHolidayBeforeAfter(bond.intSchedRule.holidayRule, dtRedeem,
                bond.country);
        String strDow = dayCount.getDayOfWeek(dtRedeem);
        cashflowResults.sumTotCashflow = totCashflow;
        cashflowResults.sumTotIntCashflow = 0.;
        cashflowResults.sumTotPrinCashflow = schedPrinflow;
        cashflowResults.sumTotPrePayCashflow = 0.;
        cashflowResults.sumTotSchedCashflow = schedPrinflow;
        cashflowResults.securityNo = securityNo;
        cashflowResults.dtCashflow = dtActRedeem;
        cashflowResults.strDayOfWeek = strDow;
        cashflowResults.perLen = perLen;
        cashflowResults.years = years;
        cashflowResults.totCashflow = totCashflow;
        cashflowResults.totPrinCash = totPrinflow;
        cashflowResults.schedPrinCash = schedPrinflow;
        cashflowResults.prePaycash = prePayflow;
        cashflowResults.intCash = intFlow;
        cashflowResults.fedTaxPaid = fedTaxPay;
        cashflowResults.stateTaxPaid = stateTaxPay;
        cashflowResults.matLen = dayCount.getMonthCount(dtSettle, dtActRedeem, 'Y');
        ++cashflowResults.count;
        ++cashflowResults.cumCount;
        cashflowList.add(0, new CashflowResults(cashflowResults));
        // calcResults.cashflowResults.reCalculateYn = 'N';
        cashflowResults.count = 1;
        return cashflowList;

    }

    public double futureValue(int calcNo, int securityNo, String instype, double faceAmt, Dates dtFuture,
            Dates dtRedeem, double redeemPrin) {
        double fVal = 0.;
        if (dtFuture.eqDate(dtRedeem))
            return faceAmt * redeemPrin;
        double discFac = 1.;
        int freq = CalUnits.SEMIANNUALFQ;
        double matLen = dayCount.getMonthCount(dtFuture, dtRedeem, 'Y');
        double noPer = matLen / CalUnits.SEMIANNUALREPEAT;
        double useYield = 0.;
        if (dtFuture.ltDate(dtRedeem)) { // discount maturity cash to future date
            useYield = bond.discCurve.getSpotRate(matLen, bond.yieldCurveInterpMeth);
            discFac = 1. / Math.pow(1. + useYield / freq, noPer);
            fVal = redeemPrin * discFac;
            return faceAmt * fVal;
        }
        if (dtFuture.gtDate(dtRedeem)) {
            matLen = dayCount.getMonthCount(dtRedeem, dtFuture, 'Y');
            noPer = matLen / CalUnits.SEMIANNUALREPEAT;
            useYield = bond.discCurve.getSpotRate(matLen, bond.yieldCurveInterpMeth);
            discFac = Math.pow(1. + useYield / freq, noPer);
            fVal = redeemPrin * discFac;
            return faceAmt * fVal;
        }
        return faceAmt * fVal;
    }

    public FutureValueResults futureValueRisk(int calcNo, int securityNo, String instype, Dates dtFuture,
            Dates dtRedeem, double redeemPrin) {
        FutureValueResults futureValueResults = new FutureValueResults();
        int matLen = 0;
        double noPer, useYield = 0.;
        double fValExp = 0., fValDn = 0., fValUp = 0.;
        int freq = CalUnits.SEMIANNUALFQ;
        noPer = dayCount.getYearsBetweenDates(dtRedeem, dtFuture);
        double discFac = 1., discFacDn = 1., discFacUp = 1.;
        if (dtFuture.gtDate(dtRedeem)) {
            matLen = dayCount.getMonthCount(dtRedeem, dtFuture, 'Y');
            noPer = (matLen / CalUnits.MONTHSPERYEAR) * freq; // o semi annual periods
            useYield = bond.discCurve.getSpotRate(matLen, bond.yieldCurveInterpMeth);
            discFac = Math.pow(1. + useYield / freq, noPer);
            useYield = bond.discCurve.getSpotRate(matLen, CalcList.SPOTDN);
            discFacDn = Math.pow(1. + useYield / freq, noPer);
            useYield = bond.discCurve.getSpotRate(matLen, CalcList.SPOTUP);
            discFacUp = Math.pow(1. + useYield / freq, noPer);
            futureValueResults.futVal = discFac * redeemPrin;
            futureValueResults.futValDn = discFacUp * redeemPrin;
            futureValueResults.futValUp = discFacDn * redeemPrin;
        }
        if (dtFuture.ltDate(dtFuture)) {
            matLen = dayCount.getMonthCount(dtFuture, dtRedeem, 'Y');
            noPer = (matLen / CalUnits.MONTHSPERYEAR) * freq; // o semi annual periods
            useYield = bond.discCurve.getSpotRate(matLen, bond.yieldCurveInterpMeth);
            discFac = 1. / Math.pow(1. + useYield / freq, noPer);
            useYield = bond.discCurve.getSpotRate(matLen, CalcList.SPOTDN);
            discFacDn = 1. / Math.pow(1. + useYield / freq, noPer);
            useYield = bond.discCurve.getSpotRate(matLen, CalcList.SPOTUP);
            discFacUp = 1. / Math.pow(1. + useYield / freq, noPer);
            futureValueResults.futVal = discFac * redeemPrin;
            futureValueResults.futValDn = discFacUp * redeemPrin;
            futureValueResults.futValUp = discFacDn * redeemPrin;
        }
        return futureValueResults;
    }

    public int accreAmortSchedule(int calcNo, String instype, Dates dtStart, Dates dtRedeem, double redeemPrin,
            double redeemInt, String bookCalcMeth) {
        double bookStart = 0., bookEnd = 0.;
        bookStart = bookValue(calcNo, dtStart, dtRedeem, redeemPrin, redeemInt, bookCalcMeth);
        bookEnd = bond.redeemPrin * bond.faceAmt;
        double dailyAccreAmort = 0.;
        double accreAmort = bookEnd - bookStart;
        int days = dayCount.getDaycount(dtStart, bond.dtMaturity, bond.dtMaturity, bond.dcAccruedInt);
        if (days > 0)
            dailyAccreAmort = accreAmort / days;
        else {
            String errmsg = "attempt to divide by zero in day counting";
            bond.error.setError(getClass().getName(), errmsg);
            return 0;
        }
        calcResults.addAccreAmortSchedule(0, dtStart, bond.dtMaturity, bookStart, bookEnd, accreAmort,
                dailyAccreAmort, days);
        return 1;
    }

    public double bookValue(int calcNo, Dates dtSettle, Dates dtRedeem, double redeemPrin, double redeemInt,
            String bookCalcMeth) {
        double bookVal = 0.;
        if (dtSettle.geDate(dtSettle, bond.dtMaturity))
            return bond.redeemPrin;
        switch (bookCalcMeth) {
            case Choices.HOLDATCOST:
                bookVal = bond.purPrc;
                break;
            case Choices.STRIGHTLINE:
                bookVal = bookValueStraightLine(calcNo, dtSettle, dtRedeem, redeemPrin, redeemInt);
                break;
            case Choices.CONSTYIELD:
            case Choices.CONSTYIELDP:
                bookVal = bookValueConstYield(calcNo, dtSettle, dtRedeem, redeemPrin, redeemInt);
                break;
            case Choices.EFFINCOME:
            case Choices.EFFINCOMEP:
                bookVal = bookValueEffIncome(calcNo, dtSettle, dtRedeem, redeemPrin, redeemInt);
                break;
            case Choices.PROSPECTYIELD:
                // bookVal = bookValueProspectYield(calcNo, dtSettle);
                break;
            default:
                String calcName = calcDef.calcDefs.get(calcNo).calcName;
                bond.error.setCalcName(calcName);
                String errmsg = "Invalid book value option (" + bond.bookCalcMeth + ") was specified";
                bond.error.setError(getClass().getName(), errmsg);
                bookVal = 0.;
                break;
        }
        return bookVal * bond.faceAmt;
    }

    public double bookValueStraightLine(int calcNo, Dates dtSettle, Dates dtRedeem, double redeemPrin,
            double redeenInt) {
        double bookVal = 0;
        double daysToMaturity = dayCount.getDaycount(bond.dtPur, dtRedeem, dtRedeem, bond.dcAccruedInt);
        double daysHeld = dayCount.getDaycount(bond.dtPur, bond.dtSettle, dtRedeem, bond.dcAccruedInt);
        double accreRate = 0.;
        if (daysToMaturity > 0)
            accreRate = (redeemPrin - bond.purPrc) / daysToMaturity;
        else {
            String errmsg = "attempt to divide by zero when calculating days from " + bond.dtPur.getDateString()
                    + " to " + bond.dtMaturity.getDateString();
            bond.error.setError(getClass().getName(), errmsg);
            return 0.;
        }
        bookVal = bond.purPrc + accreRate * daysHeld;
        return bookVal;
    }

    public double bookValueConstYield(int calcNo, Dates dtSettle, Dates dtReedem, double redeemPrin, double redeemInt) {
        double bookVal = 0.;
        bookVal = priceYield(calcNo, dtSettle, bond.purYld, dtReedem, redeemPrin, redeemInt);
        return bookVal;
    }

    private double bookValueEffIncome(int calcNo, Dates dtSettle, Dates dtRedeem, double redeemPrin, double redeemInt) {
        double bookStart = bond.purPrc;
        double usePurYld = bond.purYld;
        if (UnAvails.isUnAvail(bond.purYld)) {
            usePurYld = yieldPrice(calcNo, dtSettle, bond.purPrc, dtRedeem, redeemPrin, redeemInt);
        }
        double perYld = usePurYld / bond.intSchedRule.compoundFreq;
        double perCount = bond.schedTracks.getCouponFactor(calcNo, bond.dtPur, dtSettle);
        double bookEnd = bookStart * Math.pow(1. + perYld, perCount);
        return bookEnd;
    }
}
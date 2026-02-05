import java.util.ArrayList;

public class AtMaturity {
    Bond bond;
    Error error = new Error();
    CashflowResults cashflowResults;
    DayCount dayCount = new DayCount(error);
    ArrayList<CashflowResults> cashflowList = new ArrayList<>();


    AtMaturity(Bond bond, CashflowResults cashflowResults) {
        this.bond = bond;
        this.cashflowResults = cashflowResults;

    }


    public double accruedInterest(int calcNo, Dates dtSettle) {
        double accInt = 0.0;
        int dis = dayCount.getDaycount(bond.dtDated, dtSettle, bond.dtMaturity, bond.dcAccruedInt);
        int diy = dayCount.getDaysInYear(bond.dcAccruedInt, bond.dtDated, bond.dtMaturity, 1);
        accInt = bond.faceAmt * bond.cpnRt * (double) dis / diy;
        return accInt;
    }

    public double interestIncome(int calcNo, Dates dtFrom, Dates dtTo) {
        double intInc = 0.0;
        intInc = bond.faceAmt * getInterestIncome(calcNo, dtFrom, dtTo);
        return intInc;
    }

    public double getInterestIncome(int calcNo, Dates dtFrom, Dates dtTo) {
        double inc = 0.;
        int dFrom = dayCount.getDaycount(bond.dtDated, dtFrom, bond.dtMaturity, bond.dcAccruedInt);
        int dTo = dayCount.getDaycount(bond.dtDated, dtTo, bond.dtMaturity, bond.dcAccruedInt);
        int diy = dayCount.getDaysInYear(bond.dcAccruedInt, bond.dtDated, bond.dtMaturity, 1);
        inc = bond.cpnRt * (dTo - dFrom) / (double) diy;
        return inc;
    }

    public double matureLengthYears(int calcNo, Dates dtSettle) {
        double matLen = 0;
        int days = dayCount.getDaycount(dtSettle, bond.dtMaturity, bond.dtMaturity, DayCount.DCMethod.DC_ACT);
        double years = bond.time.daysToYears(days, dtSettle, CalcList.MATLENYEARS);
        return years;
    }

    public double matureLengthDays(int calcNo, Dates dtSettle) {
        double matLen = 0;
        double days = dayCount.getDaycount(dtSettle, bond.dtMaturity, bond.dtMaturity, DayCount.DCMethod.DC_ACT);
        return days;
    }

    public double macDuration(int calcNo, Dates dtSettle, Dates dtRedeem) {
        int matDays = dayCount.getDaycount(dtSettle, dtRedeem, bond.dtMaturity, DayCount.DCMethod.DC_ACT);
        double years = bond.time.daysToYears(matDays, dtSettle, CalUnits.calcYearPrecise);
        return years;
    }

    public double priceYield(int calcNo, Dates dtSettle, double yield, Dates dtRedeem, double redeemPrin, double redeemInt) {
        double price = 0., accInt = 0.;
        double dis = dayCount.getDaycount(bond.dtDated, dtSettle, dtRedeem, bond.dcAccruedInt);
        double diy = dayCount.getDaysInYear(bond.dcAccruedInt, dtSettle, dtRedeem, 1);
        double dim = dayCount.getDaycount(bond.dtDated, bond.dtRedeem, dtRedeem, bond.dcAccruedInt);
        double dsm = dayCount.getDaycount(bond.dtSettle, bond.dtRedeem, dtRedeem, bond.dcAccruedInt);
        double matCash = redeemPrin + bond.cpnRt * dim / diy;
        if (bond.cleanPrcYn == 'Y')
            accInt = bond.cpnRt * dis / diy;
        double yieldFac = (1. + yield * dsm / diy);
        price = (matCash / yieldFac) - accInt;
        return price;
    }

    public double yieldPrice(int calcNo, Dates dtSettle, double price) {
        double yield = 0., accInt = 0.;
        double dis = dayCount.getDaycount(bond.dtDated, dtSettle, bond.dtMaturity, bond.dcAccruedInt);
        double diy = dayCount.getDaysInYear(bond.dcYldPrc, bond.dtDated, bond.dtMaturity, 1);
        double dim = dayCount.getDaycount(bond.dtDated, bond.dtRedeem, bond.dtMaturity, bond.dcAccruedInt);
        double dsm = dayCount.getDaycount(bond.dtSettle, bond.dtRedeem, bond.dtRedeem, bond.dcAccruedInt);
        double intInc = bond.cpnRt * dim / diy;
        double matCash = bond.redeemPrin + intInc;
        if (bond.cleanPrcYn == 'Y')
            accInt = bond.cpnRt * dis / diy;
        double stateTax = 0., stateTaxPay = 0.;
        double fedTax = 0., fedTaxPay = 0.;
        if (bond.afterTaxCalcsYn == 'Y') {
            stateTax = bond.taxes.calcStateIncTaxes(intInc);
            stateTaxPay = stateTax;

            fedTax = bond.taxes.calcFedIncTaxes(intInc);
            fedTaxPay = fedTax;

            stateTax = bond.taxes.calcStateCapGLTaxes(bond.redeemPrin, bond.purPrc, bond.dtRedeem, bond.dtPur);
            stateTaxPay += stateTax;

            fedTax = bond.taxes.calcFedCapGLTaxes(bond.redeemPrin, bond.purPrc, bond.dtRedeem, bond.dtPur, stateTax);
            fedTaxPay += fedTax;
            matCash -= (fedTaxPay + stateTaxPay);
        }
        double netPay = price + accInt;
        if (dim == 0)
            return 0.;
        yield = (-1 + matCash / netPay) * (diy / dsm);
        return yield;
    }


    public double modDurationSIA(int calcNo, Dates dtSettle, Dates dtRedeem) {
        int matDays = dayCount.getDaycount(dtSettle, dtRedeem, bond.dtMaturity, DayCount.DCMethod.DC_ACT);
        double years = bond.time.daysToYears(matDays, dtSettle, CalUnits.calcYearPrecise);
        return years;
    }

    public double modDurationStd(int calcNo, Dates dtSettle, double yield, Dates dtRedeem, double redeemPrin, double redeenInt) {
        double prcUp, prcDn, prc, yldShift = .5 * CalUnits.durationYieldShift;
        prc = priceYield(calcNo, dtSettle, yield, dtRedeem, redeemPrin, redeenInt);
        prcDn = priceYield(calcNo, dtSettle, yield + yldShift, dtRedeem, redeemPrin, redeenInt);
        prcUp = priceYield(calcNo, dtSettle, yield - yldShift, dtRedeem, redeemPrin, redeenInt);
        double modDur = (prcDn - prcUp) / prc;
        return modDur;
    }

    public double conVexity(int calcNo, Dates dtSettle, double yield, Dates dtRedeem, double redeemPrin, double redeemInt) {
        double cvex = 0., yldShift = CalUnits.durationYieldShift;
        double prcUp, prcDn, prc;
        prc = priceYield(calcNo, dtSettle, yield, dtRedeem, redeemPrin, redeemInt);
        prcDn = priceYield(calcNo, dtSettle, yield + yldShift, dtRedeem, redeemPrin, redeemInt);
        prcUp = priceYield(calcNo, dtSettle, yield - yldShift, dtRedeem, redeemPrin, redeemInt);
        cvex = (prcDn + prcUp - 2. * prc) / (2. * prc * yldShift * yldShift);
        return cvex; // need tp scale by 10000
    }

    public int couponDays(int calcNo, Dates dtSettle) {
        int cpnDays = dayCount.getDaycount(bond.dtDated, dtSettle, bond.dtMaturity, bond.dcAccruedInt);
        return cpnDays;
    }

    public ArrayList<CashflowResults> cashflow(int calcNo, int securityNo, Dates dtSettle, Dates dtRedeem, double redeemPrin) {
        int dayNum = 0;
        double totPrinflow = redeemPrin;
        char isHolidayYn;
        double perLen = dayCount.simplePeriodCount(dtSettle, bond.dtMaturity, CalUnits.SEMIANNUALREPEAT);
        double years = dayCount.matLenYears(calcNo, dtSettle, bond.dtMaturity, bond.dtMaturity);
        double schedPrinflow = bond.redeemPrin;
        double prePayflow = 0.;
        Dates dtLast = new Dates();
        dtLast = dtSettle;
        Dates dtNext = new Dates();
        SchedTrack schedTrack = new SchedTrack();
        double fedTax = 0., fedTaxPay = 0., stateTax = 0., stateTaxPay = 0.;
        double intInc = 0.;
        double matCash = 0.;
        matCash = maturityCash(calcNo, securityNo, dtSettle, dtRedeem, redeemPrin);
        intInc = getInterestIncome(calcNo, dtSettle, dtRedeem);
        if (bond.afterTaxCalcsYn == 'Y') {
            // is it tax time
            stateTax = bond.taxes.calcStateIncTaxes(intInc);
            stateTaxPay = stateTax;
            fedTax = bond.taxes.calcFedIncTaxes(intInc);
            fedTaxPay = fedTax;
            stateTax = bond.taxes.calcStateCapGLTaxes(bond.redeemPrin, bond.purPrc, bond.dtMaturity, bond.dtPur);
            stateTaxPay += stateTax;
            fedTax = bond.taxes.calcFedCapGLTaxes(bond.redeemPrin, bond.purPrc, bond.dtMaturity, bond.dtPur, stateTax);
            fedTaxPay += fedTax;
            matCash -= (stateTaxPay + fedTaxPay);
        }

        cashflowResults.securityNo = securityNo;
        cashflowResults.dtCashflow = dtRedeem;
        String dow = dayCount.getDayOfWeek(bond.dtMaturity);
        cashflowResults.strDayOfWeek = dow;
        perLen = dayCount.simplePeriodCount(dtSettle, dtRedeem, CalUnits.SEMIANNUALREPEAT);
        cashflowResults.perLen = perLen;
        cashflowResults.years = years;
        cashflowResults.totCashflow = matCash;
        cashflowResults.totPrinCash = redeemPrin;
        cashflowResults.schedPrinCash = redeemPrin;
        cashflowResults.prePaycash = 0.;
        cashflowResults.intCash = intInc;
        if (bond.afterTaxCalcsYn == 'Y') {
            cashflowResults.fedTaxPaid = -fedTaxPay;
            cashflowResults.stateTaxPaid = -stateTaxPay;
        }
        else {
            cashflowResults.fedTaxPaid = 0.;
            cashflowResults.stateTaxPaid = 0.;
        }
        cashflowResults.matLen = dayCount.getMonthCount(dtSettle, dtRedeem, 'Y');
        cashflowList.add(0, new CashflowResults(cashflowResults));
        ++cashflowResults.count;
        ++cashflowResults.cumCount;
        return cashflowList;
    }

    public double maturityCash(int calcNo, int securityNo, Dates dtSettle, Dates dtRedeem, double redeemPrin) {
        int diy = dayCount.getDaysInYear(bond.dcAccruedInt, bond.dtDated, dtRedeem, 1);
        int dim = dayCount.getDaycount(bond.dtDated, bond.dtRedeem, dtRedeem, bond.dcAccruedInt);
        double matCash = bond.redeemPrin + bond.cpnRt * (double) dim / diy;
        return matCash;
    }

    public double futureValue(int calcNo, int securityNo, String instype, double faceAmt, Dates dtFuture, Dates dtRedeem, double redeemPrin) {
        double fVal = 0.;
        if (dtFuture.eqDate(dtRedeem))
            return faceAmt * redeemPrin;
        double discFac = 1.;
        int freq = CalUnits.SEMIANNUALFQ;
        double matLen = dayCount.getMonthCount(dtFuture, dtRedeem, 'Y');
        double noPer = matLen / CalUnits.SEMIANNUALREPEAT;
        double useYield = 0.;
        double matCash = faceAmt * maturityCash(calcNo, securityNo, dtRedeem, dtRedeem, redeemPrin);
        double accInt = 0.;
        if (calcNo == CalcList.FUTUREVALUE)
            accInt = accruedInterest(CalcList.ACCUREDINT, dtFuture);
        if (dtFuture.ltDate(dtRedeem)) { // discount maturity cash to future date
            useYield = bond.discCurve.getSpotRate(matLen, bond.yieldCurveInterpMeth);
            discFac = 1. / Math.pow(1. + useYield / freq, noPer);
            fVal = matCash * discFac + accInt;
        } else {
            useYield = bond.discCurve.getSpotRate(-matLen, bond.yieldCurveInterpMeth);
            matLen = dayCount.getMonthCount(dtRedeem, dtFuture, 'Y');
            noPer = matLen / CalUnits.SEMIANNUALREPEAT;
            useYield = bond.discCurve.getSpotRate(matLen, bond.yieldCurveInterpMeth);
            discFac = Math.pow(1. + useYield / freq, noPer);
            fVal = matCash * discFac + accInt;
        }
        return fVal;
    }

    public FutureValueResults futureValueRisk(int calcNo, int securityNo, String instype, Dates dtFuture, Dates dtRedeem, double redeemPrin) {
        FutureValueResults futureValueResults = new FutureValueResults();
        int matLen = 0;
        double noPer, useYield = 0.;
        double fValExp = 0., fValDn = 0., fValUp = 0.;
        int freq = CalUnits.SEMIANNUALFQ;
        noPer = dayCount.getYearsBetweenDates(dtRedeem, dtFuture);
        double discFac = 1., discFacDn = 1., discFacUp = 1.;
        double matCash = maturityCash(calcNo, securityNo, dtRedeem, dtRedeem, redeemPrin);
        double accInt = accruedInterest(CalcList.ACCUREDINT, dtFuture);
        if (dtFuture.geDate(dtRedeem)) {
            matLen = dayCount.getMonthCount(dtRedeem, dtFuture, 'Y');
            noPer = (matLen / CalUnits.MONTHSPERYEAR) * freq; //o semi annual periods
            useYield = bond.discCurve.getSpotRate(matLen, bond.yieldCurveInterpMeth);
            discFac = Math.pow(1. + useYield / freq, noPer);
            useYield = bond.discCurve.getSpotRate(matLen, CalcList.SPOTDN);
            discFacUp = Math.pow(1. + useYield / freq, noPer);
            useYield = bond.discCurve.getSpotRate(matLen, CalcList.SPOTUP);
            discFacDn = Math.pow(1. + useYield / freq, noPer);
            futureValueResults.futVal = discFac * matCash + accInt;
            futureValueResults.futValDn = discFacDn * matCash + accInt;
            futureValueResults.futValUp = discFacUp * matCash + accInt;
        }
        if (dtFuture.ltDate(dtRedeem)) {
            matLen = dayCount.getMonthCount(dtFuture, dtRedeem, 'Y');
            noPer = (matLen / CalUnits.MONTHSPERYEAR) * freq; //o semi annual periods
            useYield = bond.discCurve.getSpotRate(matLen, CalcList.SPOTON);
            discFac = 1. / Math.pow(1. + useYield / freq, noPer);
            useYield = bond.discCurve.getSpotRate(matLen, CalcList.SPOTDN);
            discFacUp = 1. / Math.pow(1. + useYield / freq, noPer);
            useYield = bond.discCurve.getSpotRate(matLen, CalcList.SPOTUP);
            discFacDn = 1. / Math.pow(1. + useYield / freq, noPer);
            futureValueResults.futVal = discFac * matCash;
            futureValueResults.futValDn = discFacUp * matCash;
            futureValueResults.futValUp = discFacDn * matCash;
        }
        return futureValueResults;
    }


}

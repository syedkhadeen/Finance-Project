package app;

/*
    Floating bond calculation
    Copyright: DataVerse

**/

public class Floater {
    Bond bond;
    Error error;
    CalcDef calcDef;
    DayCount dayCount;
    // BondDefault bondDefault;
    double totCash = 0.;
    double totPrin = 0.;
    double totSchedPrin = 0.;
    double totPrePayPrin = 0.;
    double totInt = 0.;
    double cumIntInc = 0.;
    YieldCurves yieldCurves;

    Floater(Bond bond) {
        this.bond = bond;
        error = new Error();
        calcDef = new CalcDef();
        dayCount = new DayCount(error);
        // schedRule = new SchedRule(bond.intSched, bond.dayCount, bond.time,
        // bond.error);
    }

    private double getResetRate(int calcNo, Dates dtReset) {
        int noRates = bond.noResetDts, rateNo = -1, idxReset = UnAvails.NOTAVAILINT;
        SchedTrack schedTrack = new SchedTrack();
        double resetRt = 0.;
        String errmsg = null;
        int trackNo = bond.schedTracks.currentTrackNo;
        String errSuffix = " for calculation " + calcDef.calcDefs.get(calcNo).calcName;
        if (bond.stripType.equals(BondConsts.STRIPPO))
            return 0.;
        if (bond.noResetDts == bond.noResetRts && bond.noResetDts > 0) {
            // get these date from the scheduleTrack
            schedTrack = bond.schedTracks.getNextSchedTrack(BondConsts.BEFOREONDATE, dtReset,
                    SchedTrack.RESETRATETRACK);
            if (!schedTrack.found) {
                bond.error.setError(getClass().getName(), schedTrack.errmsg);
                return 0.;
            }
        }
        // ignore non-equal reset date rate entries if user specifies a fixing curve
        // has the user specified as many reset rates and reset dates
        if (bond.noFixingRate == bond.noFixingTenor && bond.noFixingRate > 0) {
            double matlen1 = CalUnits.MONTHSPERYEAR * dayCount.time.getYearsBetweenDates(bond.dtSettle, dtReset);
            double spotRt1 = bond.fixingCurve.getSpotRateLinear(matlen1);
            double matlen2 = matlen1 + bond.floaterTerm;
            double spotRt2 = bond.fixingCurve.getSpotRateLinear(matlen2);
            resetRt = bond.fixingCurve.getForwardRate(spotRt1, spotRt2, matlen1, matlen2);
            return resetRt;
        }
        if (UnAvails.isUnAvail(bond.cpnRt)) {
            errmsg = "No reset rates were specified";
            bond.error.setError(getClass().getName(), errmsg);
            return 0.;
        }
        return 0.;
    }

    public boolean isOnCouponDate(SchedTrack schedTrack) {
        boolean isOk = false;
        Dates dtCpn = new Dates();
        Dates dtPay = new Dates();
        SchedTrack cpnTrack = new SchedTrack();

        dtPay = schedTrack.schedDate;
        cpnTrack = bond.schedTracks.getSchedTrackOn(dtPay, SchedTrack.INTERESTPAYTRACK);
        if (!cpnTrack.found)
            return false;
        dtCpn = cpnTrack.schedDate;
        isOk = dtCpn.eqDate(dtCpn, dtPay);
        return isOk;
    }

    // calculates the interest payment for a floater on a given coupon date
    // from last coupon da// te to current coupon date dtPay
    public double getInterestCash(int calcNo, SchedTrack cpnTrack) {
        double intCash = 0.0;
        Dates dtNextCpn = new Dates();
        Dates dtLastCpn = new Dates();
        dtNextCpn = cpnTrack.schedDate;
        SchedTrack schedTrack = new SchedTrack();
        if (!isOnCouponDate(cpnTrack))
            return 0.;
        if (UnAvails.isAvail(bond.cpnRt)) {
            intCash = bond.cpnRt / bond.intSchedRule.compoundFreq;
            return intCash;
        }
        // get the prior coupon date
        SchedTrack lastPayTrack = bond.schedTracks.getSchedTrackBefore(dtNextCpn, SchedTrack.INTERESTPAYTRACK);
        if (!lastPayTrack.found) {
            return 0;
        }
        dtLastCpn = lastPayTrack.schedDate;
        Dates dtLastReset = dtLastCpn;
        schedTrack = bond.schedTracks.getSchedTrackOnBefore(dtLastCpn, SchedTrack.RESETRATETRACK);
        double lastResetRt = schedTrack.schedValue;

        while (dtLastReset.ltDate(dtNextCpn)) {
            schedTrack = bond.schedTracks.getSchedTrackAfter(dtLastReset, SchedTrack.RESETRATETRACK);
            lastResetRt = schedTrack.schedValue;
            if (!schedTrack.found) {
                return 0.;
            }
            Dates dtNextReset = schedTrack.schedDate;
            double cpnPeriod = bond.intSchedRule.getCouponFactor(dtLastReset, dtNextReset);
            intCash += lastResetRt * bond.redeemPrin * cpnPeriod / bond.intSchedRule.compoundFreq;
            dtLastReset = dtNextReset;
        }
        double cpnPeriod = bond.intSchedRule.getCouponFactor(dtLastReset, dtNextCpn);
        intCash += lastResetRt * bond.redeemPrin * cpnPeriod / bond.intSchedRule.compoundFreq;
        return intCash;
    }

    // get .... means per unit par of $1
    // dates can be any dates and cross over coupon periods
    public double getInterestAccrual(int calcNo, Dates dtFrom, Dates dtTo) {
        SchedTrack resetTrack = new SchedTrack();
        double intAccrue = 0.0;
        Dates dtLastReset = new Dates();
        Dates dtNextReset = new Dates();
        double cpnPeriod = 0.;
        SchedTrack cpnTrack = new SchedTrack();
        if (UnAvails.isAvail(bond.cpnRt)) {
            cpnTrack = bond.schedTracks.getSchedTrackOnBefore(dtFrom, SchedTrack.INTERESTPAYTRACK);
            Dates dtLastCpn = cpnTrack.schedDate;
            cpnPeriod = bond.intSchedRule.getCouponFactor(dtFrom, dtTo);
            return cpnPeriod * bond.cpnRt;
        }
        // get coupon payment date prior to dtFrom
        // get the reset date in effect or prior coupon date
        Dates dtLastCpn = bond.intSchedRule.getNextSchedDate(SchedTracks.ONBEFOREDATE, dtFrom);
        resetTrack = bond.schedTracks.getSchedTrackOnBefore(dtLastCpn, SchedTrack.RESETRATETRACK);
        if (!resetTrack.found)
            return 0.;
        dtLastReset = dtLastCpn;
        double lastResetRt = resetTrack.schedValue;
        lastResetRt = getAbsResetRt(dtLastReset, lastResetRt);
        resetTrack = bond.schedTracks.getSchedTrackAfter(dtFrom, SchedTrack.RESETRATETRACK);
        if (!resetTrack.found) {
            return 0.;
        }
        cpnTrack = bond.schedTracks.getSchedTrackAfter(dtLastCpn, SchedTrack.INTERESTPAYTRACK);
        Dates dtNextCpn = cpnTrack.schedDate;
        dtNextReset = resetTrack.schedDate;
        while (dtNextReset.ltDate(dtNextReset, dtTo)) {
            cpnPeriod = bond.intSchedRule.getCouponFactor(dtLastReset, dtNextReset);
            intAccrue += lastResetRt * bond.redeemPrin * cpnPeriod / bond.intSchedRule.compoundFreq;
            dtLastReset = dtNextReset;
            resetTrack = bond.schedTracks.getSchedTrackAfter(dtLastReset, SchedTrack.RESETRATETRACK);
            lastResetRt = resetTrack.schedValue;
            dtNextReset = resetTrack.schedDate;
        }
        // do tail from last rewet to dttO
        cpnPeriod = bond.intSchedRule.getCouponFactor(dtLastReset, dtTo);
        intAccrue += lastResetRt * bond.redeemPrin * cpnPeriod / bond.intSchedRule.compoundFreq;
        return intAccrue;
    }

    public double getAbsResetRt(Dates dtReset, double absResetRt) {
        double multiplier = 0.;
        double spread = 0.;
        double cap = UnAvails.NOTAVAILDOUBLE;
        double floor = UnAvails.NOTAVAILDOUBLE;
        SchedTrack schedTrack = new SchedTrack();
        if (bond.noCaps > 0) {
            schedTrack = bond.schedTracks.getSchedTrackOnBefore(dtReset, SchedTrack.RESETCAPTRACK);
            if (!schedTrack.found)
                return 0.;
            cap = schedTrack.schedValue;
        }
        if (bond.noResetFloors > 0) {
            schedTrack = bond.schedTracks.getSchedTrackOnBefore(dtReset, SchedTrack.RESETFLOORTRACK);
            if (!schedTrack.found)
                return 0.;
            floor = schedTrack.schedValue;
        }
        if (UnAvails.isAvail(cap))
            absResetRt = Math.min(absResetRt, cap);
        if (UnAvails.isAvail(floor))
            absResetRt = Math.max(absResetRt, floor);
        return absResetRt;
    }

}
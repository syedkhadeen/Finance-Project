package app;

public class Indexed {
    Bond bond;
    Error error;
    DayCount dayCount;
    Utility utility;

    /*
     * Initialize for mortgage issues
     **/
    Indexed(Bond bond) {
        this.bond = bond;
        this.error = new Error();
        this.dayCount = new DayCount(error);
        this.utility = new Utility(error, dayCount);
        // bondDefault = new BondDefault(bond);
    }

    // this method is used when no index ratio at settle is entered - accrued
    // interest
    // this method is used if future index ratios are used
    //
    public double getIndexRatio(Dates dtCalc, String idxInterpMeth, double inflationRt) {
        double idxRatioEstimate = 0.;
        double idxValueEstimate = 0.;
        boolean allowEstimation = false;
        double idxCPIValue = 0.;
        double idxRatio = 0.;

        return bond.idxCPIRatio;
        /*
         * //bondCheck made sure index ratio at settle was calculated
         * if (dtCalc.eqDate(dtCalc, bond.dtSettle)) {
         * if (UnAvails.isAvail(bond.idxCPIAtSettle)) {
         * idxRatio = bond.idxCPIAtSettle;
         * return idxRatio;
         * }
         * 
         * }
         * idxCPIValue = getIndexValue(dtCalc, idxInterpMeth, inflationRt);
         * idxRatio = idxCPIValue / bond.idxCPIAtIssue;
         * idxRatio = Utility.truncRound(idxRatio, bond.sfigIdxRatio,
         * bond.idxRatioRndYn);
         * return idxRatio;
         * 
         */
    }

    // use calculate the index value at settle which is needed to calculate the
    // index ratio at settle when no index raio at settle is entered
    // also called to get future index ratios by estimating the index value
    // (dividing by index at issue)
    // called to get inde ratio at settle wheb index ratio as settle is nt supplied
    // disallow estimation when date is settle date becuase we hace tighter
    // resrtictions
    public double getIndexValue(Dates dtSettle, String idxInterpMeth, double inflationRt) {
        double idxValue = UnAvails.NOTAVAILDOUBLE;
        Dates dtIndexPublish = new Dates(); // this is date of index value to use.
        char useEndMonthRuleYn = 'Y';
        char use360Yn = 'Y';
        if (dtSettle.eqDate(dtSettle, bond.dtSettle))
            return bond.idxCPIAtSettle;
        // get index value public date usinf delay
        if (UnAvails.isAvail(bond.idxDelayMonths))
            dtIndexPublish = dayCount.time.addMonthsToDate(dtSettle, -bond.idxDelayMonths, useEndMonthRuleYn);
        else if (UnAvails.isUnAvail(bond.idxDelayWeeks))
            dtIndexPublish = dayCount.time.addWeeksToDate(dtSettle, -bond.idxDelayWeeks);
        else if (UnAvails.isAvail(bond.idxDelayDays))
            dtIndexPublish = dayCount.time.addDaysToDate(dtSettle, -bond.idxDelayDays, use360Yn);
        else {
            String errmsg = "No index delay was specified for the index linked bond";
            bond.error.setError(getClass().getName(), errmsg);
            return idxValue;
        }
        SchedTrack schedTrack = new SchedTrack();
        double idxValuePrior = 0.;
        double idxValueAfter = 0.;
        Dates dtIdxValuePrior = new Dates();
        Dates dtIdxValueAfter = new Dates();
        int daysIndex = 0;
        int daysTotal = 0;
        int trackNo = bond.schedTracks.currentTrackNo;
        switch (idxInterpMeth) {
            case BondConsts.INDEXINTERPNONE:
                // get index value on publish date using first day of month
                schedTrack = bond.schedTracks.getSchedTrackOnBefore(dtIndexPublish, SchedTrack.INDEXCPITRACK);
                schedTrack = estIndexValue(schedTrack, dtIndexPublish, inflationRt);
                if (!schedTrack.found) {
                    String errmsg = "no index value found on or before " + dtIndexPublish.getDateString();
                    bond.error.setError(getClass().getName(), errmsg);
                    return 0.;
                }
                // if schedule nt found, and estimate made then no error
                bond.error.clearError();
                idxValue = schedTrack.schedValue;
                break;
            case BondConsts.INDEXINTERPLINEAR:
                schedTrack = bond.schedTracks.getSchedTrackOnBefore(dtIndexPublish, SchedTrack.INDEXCPITRACK);
                schedTrack = estIndexValue(schedTrack, dtSettle, bond.inflationRt);
                if (!schedTrack.found) {
                    String errmsg = "no index value found on or before " + dtIndexPublish.getDateString();
                    bond.error.setError(getClass().getName(), errmsg);
                    return 0.;
                }
                idxValuePrior = schedTrack.schedValue;
                dtIdxValuePrior = schedTrack.schedDate;
                schedTrack = bond.schedTracks.getSchedTrackAfter(dtIndexPublish, SchedTrack.INDEXCPITRACK);
                schedTrack = estIndexValue(schedTrack, dtIndexPublish, bond.inflationRt);
                if (!schedTrack.found) {
                    String errmsg = "no index value found on or before " + dtIndexPublish.getDateString();
                    bond.error.setError(getClass().getName(), errmsg);
                    return 0.;
                }
                idxValueAfter = schedTrack.schedValue;
                dtIdxValueAfter = schedTrack.schedDate;
                daysTotal = dayCount.getDaycount(dtSettle, dtIdxValueAfter, bond.dtMaturity, bond.dcIdxInterp);
                daysIndex = dayCount.getDaycount(dtSettle, dtIndexPublish, bond.dtMaturity, bond.dcIdxInterp);
                double indexValuePlus; // how much index value grew
                if (daysTotal == 0) {
                    indexValuePlus = 0.;
                } else {
                    double ratio = (double) daysIndex / daysTotal;
                    indexValuePlus = (idxValueAfter - idxValuePrior) * ratio;
                }
                idxValue = idxValuePrior + indexValuePlus;
                break;
            case BondConsts.INDEXINTERPJAPAN:
                if (dtSettle.day == 10) {
                    idxValue = idxValuePrior;
                    break;
                }
                if (dtSettle.day < 10) {
                    schedTrack = bond.schedTracks.getNextSchedTrack(BondConsts.BEFOREDATE, dtIndexPublish,
                            SchedTrack.INDEXCPITRACK);
                    schedTrack = estIndexValue(schedTrack, dtSettle, bond.inflationRt);
                    if (!schedTrack.found) {
                        String errmsg = "no index value found before " + dtIndexPublish.getDateString();
                        bond.error.setError(getClass().getName(), errmsg);
                        return 0.;
                    }
                    schedTrack = bond.schedTracks.getNextSchedTrack(BondConsts.AFTERDATE, dtIndexPublish,
                            SchedTrack.INDEXCPITRACK);
                    schedTrack = estIndexValue(schedTrack, dtSettle, bond.inflationRt);
                    if (!schedTrack.found) {
                        String errmsg = "no index value found after " + dtIndexPublish.getDateString();
                        bond.error.setError(getClass().getName(), errmsg);
                        return 0.;
                    }
                    dtIdxValuePrior = dayCount.time.addMonthsToDate(dtSettle, -1, useEndMonthRuleYn);
                    --dtIdxValuePrior.month;
                    dtIdxValuePrior.day = 11;
                    dtIdxValueAfter.day = 10;
                    daysIndex = dayCount.getDaycount(dtIdxValuePrior, dtSettle, bond.dtMaturity, bond.dcIdxInterp);
                    daysTotal = dayCount.getDaycount(dtIdxValuePrior, dtIdxValueAfter, bond.dtMaturity,
                            bond.dcIdxInterp);
                    double ratio = 0.;
                    if (daysTotal <= 0) {
                        ratio = 0.;
                    } else {
                        ratio = (double) daysIndex / daysTotal;
                        idxValue = idxValuePrior + (idxValueAfter - idxValuePrior) * ratio;
                    }
                }
                if (dtSettle.day > 10) {
                    dtIdxValueAfter = dayCount.time.addMonthsToDate(dtSettle, 1, useEndMonthRuleYn);
                    dtIdxValueAfter.day = 10;
                    dtIdxValuePrior = dtSettle;
                    dtIdxValuePrior.day = 11;
                    double ratio = 0.;
                    daysIndex = dayCount.getDaycount(dtIdxValuePrior, dtIdxValueAfter, bond.dtMaturity,
                            bond.dcIdxInterp);
                    if (daysIndex == 0) {
                        ratio = 0.;
                    } else {
                        ratio = (double) (dtSettle.day - 10) / daysIndex;
                        idxValue = idxValuePrior + (idxValueAfter - idxValuePrior) * ratio;
                    }
                }
                break;
            case BondConsts.INDEXINTERPBBG:
                double ratio = -0.;
                // daysTotal = days_in_month(settdt); // Note that we use the settdt, not the
                // adjSettleDt to compute
                if (dtSettle.isLeapYear(dtSettle.year))
                    daysTotal = CalUnits.daysInMonthLeap[dtSettle.month + 1];
                else
                    daysTotal = CalUnits.daysInMonthNonLeap[dtSettle.month + 1];
                if (daysTotal == 0) {
                    ratio = 0.;
                }
                ratio = (idxValueAfter - idxValuePrior) / daysTotal;
                if (dtSettle.day > 1)
                    daysIndex = dtSettle.day - 1; // count from the beginning of the month
                else
                    daysIndex = dtSettle.day;
                idxValue = idxValuePrior * ratio * daysIndex;
                break;
            case BondConsts.INDEXINTERPISRAEL:
                if (dtSettle.day < 17)
                    idxValue = idxValuePrior;
                else
                    idxValue = idxValueAfter;
                break;
            case BondConsts.INDEXINTERPBRAZIL:
                // compute the number of business days from lindexdt to nindexdt
                if (bond.noIdxAssumeDts == 0) {
                    daysTotal = dayCount.getDaycount(dtIdxValuePrior, dtIdxValueAfter, bond.dtMaturity,
                            bond.dcIdxInterp);
                    daysIndex = dayCount.getDaycount(dtIdxValuePrior, dtIdxValueAfter, bond.dtMaturity,
                            bond.dcIdxInterp);
                    if (bond.holidays.isHolidayDate(dtSettle)) {
                        if (daysIndex > 1)
                            --daysIndex;
                    }
                    if (daysTotal == 0)
                        ratio = 0.;
                    else
                        ratio = daysIndex / (double) daysTotal;
                    idxValue = idxValuePrior + (idxValueAfter - idxValuePrior) * ratio;
                    break;
                } else {
                    if (daysTotal == 0)
                        ratio = 0.;
                    else
                        ratio = daysIndex / (double) (daysTotal);
                    schedTrack = bond.schedTracks.getSchedTrackBefore(dtIndexPublish, SchedTrack.INDEXASSUMPTIONTRACK);
                    schedTrack = estIndexValue(schedTrack, dtSettle, bond.inflationRt);
                    if (!schedTrack.found) {
                        String errmsg = "no index value found before " + dtIndexPublish.getDateString();
                        bond.error.setError(getClass().getName(), errmsg);
                        return 0.;
                    }
                    double idxAssumption = schedTrack.schedValue;
                    ratio = Math.pow((1. + idxAssumption), ratio);
                    idxValue = idxValuePrior * ratio;
                }
                break;
            default:
                String errmsg = " an invalid index interpolation method of " + bond.idxInterpMeth + " was specified";
                bond.error.setError(getClass().getName(), errmsg);
                idxValue = 0.;
                break;
        }
        double preIdxValue = idxValue;
        idxValue = Utility.truncRound(preIdxValue, bond.sfigIdxValue, bond.idxValueRoundYn);
        return idxValue;
    }

    public SchedTrack estIndexValue(SchedTrack schedTrack, Dates dtDesired, double inflationRt) {
        if (schedTrack.found)
            return schedTrack;
        if (UnAvails.isUnAvail(inflationRt))
            schedTrack.schedValue = bond.idxCPIAtSettle;
        else {
            double idxCPIValue = 0.;
            double inflationRatio = utility.growthRate(inflationRt, bond.intSchedRule.compoundFreq, bond.dtSettle,
                    dtDesired);
            idxCPIValue = bond.idxCPIAtSettle * inflationRatio;
            idxCPIValue = Utility.truncRound(idxCPIValue, bond.sfigIdxValue, bond.idxValueRoundYn);
            schedTrack.schedValue = idxCPIValue;
        }
        schedTrack.schedDate = dtDesired;
        schedTrack.found = true;
        return schedTrack;
    }

}

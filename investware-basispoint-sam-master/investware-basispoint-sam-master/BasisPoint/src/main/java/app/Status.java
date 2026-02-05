package app;

class Status {
    // default status
    Bond bond;
    boolean useExistingCashflow;
    Choices choices = new Choices();
    int lastCalcNo; // . the calculation that ran a cashflow
    int noCashflows;
    boolean useIndexRatios; // false then use index value
    int dumpCount; // number of times dumped input to dump file
    boolean isSinkPctDefaulted;
    boolean isIndexDelayDefaulted;
    boolean isSwapLegConvexityCalculated; /* indicates whether the swapLegConvexities function was called already */
    boolean isSwapLegModDurCalculated; /* indicates whether the swapLegModuration function was called already */
    boolean isSwapLegPriceCalculated; /* indicates whether swap prices are calculated */
    boolean isSwapLegYieldCalculated;
    boolean isCubicSplineInitialized; /* has the cubic spline function been initialized */
    boolean isZeroYieldCalculated; /* indicates whether the zero yield has been calculated */
    boolean forecastIdxRatios;
    // double prepaySpeedOnFactorDate;
    boolean forecastIdxValues;

    boolean forcastIdxRatioAtSettle;
    boolean forcastIdxValueAtSettle;
    boolean useUserResetRates; // user entered set of reset rates
    boolean usefixingCurve; // user entered fixing rates and matlens
    boolean useCpnRtAsResetRt;
    boolean useUserResetDates;
    boolean useResetDateSchedRule;
    boolean useDiscCurveForFutValue;

    boolean useReinvestRts;

    Status(Bond bond) {
        this.bond = bond;
        useExistingCashflow = false;
        noCashflows = 0;
        useIndexRatios = false;
        dumpCount = 0;
        isSinkPctDefaulted = false;
        isSwapLegConvexityCalculated = false;
        isSwapLegModDurCalculated = false;
        isSwapLegPriceCalculated = false;
        isSwapLegYieldCalculated = false;
        isCubicSplineInitialized = false; /* has the cubic spline function been initialized */
        isZeroYieldCalculated = false;
        forecastIdxRatios = false; // means if inflation specified forecast using index ratio other wise indexalue
        forecastIdxValues = false;
        forcastIdxValueAtSettle = false;
        useResetDateSchedRule = false;
        useCpnRtAsResetRt = false;
        usefixingCurve = false;
        useUserResetRates = false;
        useUserResetDates = false;
        useDiscCurveForFutValue = false;
        forcastIdxRatioAtSettle = false;
    }

    public void setCashflowStatus(int calcNo, int noFlows, Dates dtLastSettle, Dates dtLastPaymemnt) {
        this.noCashflows = noFlows;
        this.lastCalcNo = calcNo;

    }

    public boolean isCashflowValid(int calcNo, Dates dtSettle, Dates dtLastPayment) {
        boolean isOk = false;
        if (!useExistingCashflow)
            return false;
        isOk = compatableCashflowCalc(calcNo, lastCalcNo);
        return false;
    }

    public boolean compatableCashflowCalc(int calcNo, int lastCalcNo) {
        boolean isOk = false;
        if (calcNo == lastCalcNo)
            return true;
        if (!choices.inCompatableCalcList(calcNo))
            return false;
        return choices.inCompatableCalcList(lastCalcNo);
    }

    public void clear() {
        useExistingCashflow = false;
        noCashflows = 0;
        useIndexRatios = false;
        dumpCount = 0;
        isSinkPctDefaulted = false;
        isSwapLegConvexityCalculated = false;
        isSwapLegModDurCalculated = false;
        isSwapLegPriceCalculated = false;
        isSwapLegYieldCalculated = false;
        isCubicSplineInitialized = false; /* has the cubic spline function been initialized */
        isZeroYieldCalculated = false;
        forecastIdxRatios = false; // means if inflation specified forecast using index ratio other wise indexalue
        forecastIdxValues = false;
        forcastIdxValueAtSettle = false;
        useResetDateSchedRule = false;
        useCpnRtAsResetRt = false;
        usefixingCurve = false;
        useUserResetRates = false;
        useUserResetDates = false;
        forcastIdxRatioAtSettle = false;
    }

}

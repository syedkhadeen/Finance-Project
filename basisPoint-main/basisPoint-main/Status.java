
class Status {
    // default status
    Choices choices = new Choices();
    int lastCalcNo; //. the calculation that ran a cashflow
    int noCashflows;
    Dates dtCashflowStart = new Dates();
    Dates dtLastCashflow = new Dates();
 //   boolean useIndexRatios; // false then use index value
    int dumpCount;     // number	of	times	dumped input to dump file
//boolean isSinkPctDefaulted;
    boolean isIndexDelayDefaulted;
    boolean isSwapLegConvexityCalculated;     /*	indicates	whether	the	swapLegConvexities	function	was	called	already	*/
    boolean isSwapLegModDurCalculated;    /*	indicates	whether	the	swapLegModuration	function	was	called	already	*/
    boolean isSwapLegPriceCalculated;          /* indicates whether swap prices are calculated */
    boolean isSwapLegYieldCalculated;
    boolean isCubicSplineInitialized;    /* has the cubic spline function been initialized */
    boolean isZeroYieldCalculated;     /* indicates whether the zero yield has been calculated */
    boolean forecastIdxRatios;
    //double prepaySpeedOnFactorDate;
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


    Status() {
        noCashflows = 0;
        dtCashflowStart =new Dates();
        dtLastCashflow = new Dates();
        dumpCount = 0;
        isSwapLegConvexityCalculated = false;
        isSwapLegModDurCalculated = false;
        isSwapLegPriceCalculated = false;
        isSwapLegYieldCalculated = false;
        isCubicSplineInitialized = false;    /* has the cubic spline function been initialized */
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

    public void setCashflowStatus(int calcNo, int noFlows, Dates dtCashflowStart, Dates dtLastCashflow) {
        this.noCashflows = noFlows;
        this.lastCalcNo = calcNo;
        this.dtCashflowStart = dtCashflowStart;
        this.dtLastCashflow = dtLastCashflow;

    }

    public boolean isCashflowValid(int calcNo, int noFlows, Dates dtCashflowStart, Dates dtLastCashflow) {
        boolean isOk = false;
        if (this.noCashflows == noFlows && this.dtCashflowStart.eqDate(dtCashflowStart) && this.dtLastCashflow.eqDate(dtLastCashflow))
           return true;
        else
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
        noCashflows = 0;
        dumpCount = 0;
        dtCashflowStart.setDate("1/1/1900");
        dtLastCashflow.setDate("12/31/3000");
        isSwapLegModDurCalculated = false;
        isSwapLegPriceCalculated = false;
        isSwapLegYieldCalculated = false;
        isCubicSplineInitialized = false;    /* has the cubic spline function been initialized */
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





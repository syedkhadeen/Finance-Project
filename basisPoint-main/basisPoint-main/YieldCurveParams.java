
public class YieldCurveParams{
    CommandLine cmdLine;
    int spotCurveInterval;
    String yieldCurveInterpMeth;
    String spotFileName;
    int lastSpotRecNo = 0;
    char isDiscYn;
    char isParYn;
    char isRiskFreeYn;
    char isFutureYn;
    String yieldType;
    String benchmark; // for floaters
    String creditAgency; // for discount rates
    String creditRating;
    int noPoints;
    double[] rt; // user entered rates
    int[] tenor;
    double[] rtAvg;
    double[] stdDev;
    double[] rtMin;
    double[] rtMax;
    double[] rtDn;
    double[] rtUp;
    Dates dtCurve;
    Dates dtForward;
    Dates dtAnalysis;
    int analysisPeriod;
    YieldCurveParams() {

    }



    public void setCredit(String yieldType, String spotFileName, String creditAgency, String creditRating, String benchmark) {
        this.analysisPeriod = Defaults.ANALYSISPERIOD;
        this.spotCurveInterval = BondConsts.SPOTINTERVAl;
        this.spotFileName = spotFileName;
        this.yieldType = yieldType;
        this.creditAgency = creditAgency.toUpperCase();
        this.creditRating = creditRating.toUpperCase();
        this.benchmark = benchmark.toUpperCase();
    }

    public void setDates(Dates dtAnalysis, Dates dtCurve, Dates dtForward) {
        this.dtAnalysis = dtAnalysis;
        this.dtCurve = dtCurve;
        this.dtForward = dtForward;
    }

    public void setTypes(CommandLine cmdLine, char discYn, char parYn, char riskFreeYn, char futureYn) {
        this.cmdLine = cmdLine;
        this.isDiscYn = discYn;
        this.isParYn = parYn;
        this.isRiskFreeYn = riskFreeYn;
        this.isFutureYn = futureYn;
        this.spotCurveInterval = BondConsts.SPOTINTERVAl;
    }

    public void setRates(int noRates, double[] rt, int[] tenor, double[] rtAvg, double[] stdDev, double[] rtMin, double[] rtMax, double[] rtDn, double[] rtUp) {
        this.noPoints = noRates;
        this.rt = rt;
        this.tenor = tenor;
        this.rtAvg = rtAvg;
        this.stdDev = stdDev;
        this.rtMin = rtMin;
        this.rtMax = rtMax;
        this.rtDn = rtDn;
        this.rtUp = rtUp;
    }

    public void setOptions(String yCMethod, int spotInterval, Dates dtCurve) {
        this.yieldCurveInterpMeth = yCMethod;
        this.spotCurveInterval = spotInterval;
        this.dtCurve= dtCurve;
    }
}







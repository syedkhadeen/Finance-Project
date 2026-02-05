package app;

public class YieldPoint {
    String yieldType; // risk-free or credit spread
    String curveType; // par, spot or forward
    String creditAgency;
    String creditRating;
    Dates dtAnalysisStart;
    Dates dtCurve;
    Dates dtForward;
    int analysisDays;
    String matName;
    int matLen;
    double spotRate; // actual spot rate on analysis date
    double forwardRate;
    int lookAhead;
    double spotAvg; // average spot over analysis period
    double spotStdDev; // standard deviation of spot rate over analysis period
    double spotMin;
    double spotMax;
    double spotUpSide;
    double spotDnSide;

    YieldPoint() {

    }

    YieldPoint(YieldPoint yieldPoint) {
        this.yieldType = yieldPoint.yieldType;
        this.curveType = yieldPoint.curveType;
        this.creditAgency = yieldPoint.creditAgency;
        this.creditRating = yieldPoint.creditRating;
        this.dtCurve = yieldPoint.dtCurve;
        this.dtForward = yieldPoint.dtForward;
        this.matLen = yieldPoint.matLen;
        this.matName = yieldPoint.matName;
        this.dtAnalysisStart = yieldPoint.dtAnalysisStart;
        this.analysisDays = yieldPoint.analysisDays;
        this.forwardRate = yieldPoint.forwardRate;
        this.lookAhead = yieldPoint.lookAhead;
        this.spotRate = yieldPoint.spotRate;
        this.spotAvg = yieldPoint.spotAvg;
        this.spotStdDev = yieldPoint.spotStdDev;
        this.spotDnSide = yieldPoint.spotDnSide;
        this.spotUpSide = yieldPoint.spotUpSide;
        this.spotMin = yieldPoint.spotMin;
        this.spotMax = yieldPoint.spotMax;
    }
}

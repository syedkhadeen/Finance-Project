package app;

public class HorizonResults {
    String securityDesc;
    Dates dtHorizon;
    double horzMatLen;
    double horzValue;
    double horzPrc;
    double horzGainLoss;
    double horzYield;
    double horzDur; // horizon duration
    double horzReturn;
    double cashGenRatio; // cash raised per unit cash spent

    HorizonResults() {
        securityDesc = UnAvails.NOTAVAILSTRING;
        horzValue = 0.;
        dtHorizon = new Dates();
        horzMatLen = 0.;
        horzPrc = 0.;
        horzGainLoss = 0.;
        horzYield = 0.;
        horzDur = 0.;
        horzReturn = 0.;
        cashGenRatio = 0.;

    }

    HorizonResults(HorizonResults horzResults) {
        this.securityDesc = horzResults.securityDesc;
        this.dtHorizon = horzResults.dtHorizon;
        this.horzValue = horzResults.horzValue;
        this.horzPrc = horzResults.horzPrc;
        this.horzMatLen = horzResults.horzMatLen;
        this.horzGainLoss = horzResults.horzGainLoss;
        this.horzYield = horzResults.horzYield;
        this.horzDur = horzResults.horzDur;
        this.horzReturn = horzResults.horzReturn;
        this.cashGenRatio = horzResults.cashGenRatio;
    }

    HorizonResults(Dates dtHorizon, double horzValue) {
        this.securityDesc = securityDesc;
        this.dtHorizon = dtHorizon;
        this.horzValue = horzValue;
        this.horzPrc = 100.;
        this.horzMatLen = 0.;
        this.horzGainLoss = 0.;
        this.horzYield = 0.;
        this.horzDur = 0.;
        this.horzReturn = 0.;
        this.cashGenRatio = 1;
    }
}

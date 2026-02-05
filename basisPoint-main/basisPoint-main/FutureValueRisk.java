
import java.util.ArrayList;

class FutureValueResults {
    double mktValue = 0.;
    double futVal = 0.;
    double futValUp = 0.;
    double futValDn = 0.;
    double futValDevPos = 0.;
    double futValDevNeg = 0.;


    FutureValueResults() {

    }
    FutureValueResults(FutureValueResults futureValueResults) {
        this.mktValue = futureValueResults.mktValue;
        this.futVal = futureValueResults.futVal;
        this.futValDn = futureValueResults.futValDn;
        this.futValUp = futureValueResults.futValUp;
        this.futValDevPos = futureValueResults.futValDevPos;
        this.futValDevNeg = futureValueResults.futValDevNeg;
    }

    public void calcReturnRisk(Bond bond, FutureValueResults futureValueResults) {
        double mktValue = bond.settlePrc * bond.faceAmt;
        double futVal = futureValueResults.futVal;
        double futValDn = futureValueResults.futValDn;
        double futValUp = futureValueResults.futValUp;
        double scale = 100.;
        if (mktValue != 0.) {
            double noPer = bond.intSchedRule.getCouponFactor(bond.dtSettle, bond.dtFuture);
            this.futVal = scale * bond.intSchedRule.compoundFreq * (-1 + Math.pow(futVal / mktValue, 1. / noPer));
            this.futValDn = scale * bond.intSchedRule.compoundFreq * (-1 + Math.pow(futValDn / mktValue, 1. / noPer));
            this.futValUp = scale * bond.intSchedRule.compoundFreq * (-1 + Math.pow(futValUp / mktValue, 1. / noPer));
            this.futValDevPos = scale * bond.intSchedRule.compoundFreq * (-1 + Math.pow(futValDevPos / mktValue, 1. / noPer));
            this.futValDevNeg = scale * bond.intSchedRule.compoundFreq * (-1 + Math.pow(futValDevNeg / mktValue, 1. / noPer));

        } else {
            this.futVal = 0.;
            this.futValDn = 0.;
            this.futValUp = 0.;
            this.futValDevPos = 0.;
            this.futValDevNeg = 0.;
        }
    }

}

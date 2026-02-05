public class SecurityResults {

        final int securityNo; // security sequence number
        final int calcNo;  // calcullatioon number as in CalcDefs
        final String calcName; //
        // ame of the calculations as in CalcDefs
        final double dValue;// calulatedvalue
        final double mktPrc;
        final double faceAmt;

        SecurityResults() {
            this.securityNo = UnAvails.NOTAVAILINT;
            this.calcNo = UnAvails.NOTAVAILINT;
            this.calcName = UnAvails.NOTAVAILSTRING;
            this.dValue = UnAvails.NOTAVAILDOUBLE;
            this.mktPrc = UnAvails.NOTAVAILDOUBLE;
            this.faceAmt = UnAvails.NOTAVAILDOUBLE;

        }

        SecurityResults(int securityNo, int calcNo, String calcName, double dValue, double mkyPrc, double faceAmt) {
            this.securityNo = securityNo;
            this.calcNo = calcNo;
            this.calcName = calcName;
            this.dValue = dValue;
            this.mktPrc = mkyPrc;
            this.faceAmt = faceAmt;

        }
    }



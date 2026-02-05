
public class SchedTrack {
    // cashflow types
    String schedName;
    Dates schedDate;
    char schedOptionalYn;
    double schedPrice;
    long schedSequence;
    int schedType; /* call, put, sink, etc */
    double schedValue;
    int trackNo;
    int lastSchedType;
    boolean found;
    String errmsg;

    static final int ANYTYPETRACK = 0;
    static final String ANYTYPETRACKNAME = "any schedule";

    static final int ACCRUETRACK = 1;
    static final String ACCRUETRACKNAME = "Accrual start Dates";

    static final int RESETRATETRACK = 2;
    static final String RESETRATETRACKNAME = "Reset rates";

    static final int INTERESTPAYTRACK = 3;
    static final String INTERESTPAYTRACKNAME = "Interest payment";



    static final int PAYLEGRESETRATETRACK = 4;
    static final String PAYLEGRESETRATETRACKNAME = "Payleg reset rate";

    static final int RECLEGRESETRATETRACK = 5;
    static final String RECLEGRESETRATETRACKNAME = "Receiveleg reset rate";

    static final int PAYLEGTRACK = 6;
    static final String PAYLEGTRACKNAME = "Payleg payment date";

    static final int RECLEGTRACK = 7;
    static final String RECLEGTRACKNAME = "Receiveleg payment date";

    static final int SINKTRACK = 8;
    static final String SINKTRACKNAME = "sinking fund payment";

    static final int PRINCIPALPAYTRACK = 9;
    static final String PRINCIPALPAYTRACKNAME = "principal payment";

    static final int PREPRINPAYTRACK = 10;
    static final String PREPRINPAYTRACKNAME = "Principal pre-payment";

    static final int PRINFACTORTRACK = 11;
    static final String PRINFACTORTRACKNAME = "Principal factor payment";

    static final int PIKTRACK = 12;
    static final String PIKTRACKNAME = "Payment-in-KInd";

    static final int CASHPAYTRACK = 13;
    static final String CASHPAYTRACKNAME = "Total Cash payment";

    static final int CALLTRACK = 14;
    static final String CALLTRACKNAME = "Call a bond";

    static final int PUTTRACK = 15;
    static final String PUTTRACKNAME = "Put a bond";

    static final int INDEXRATIOTRACK = 16;
    static final String INDEXRATIOTRACKNAME = "Index ratio";

    static final int INDEXCPITRACK = 17;
    static final String INDEXCPITRACKNAME = "Index Value(CPI)";

    static final int FXRATETRACK = 18;
    static final String FXRATETRACKNAME = "FX Rates";

    static final int USERCASHFLOWTRACK = 19;
    static final String USERCASHFLOWTRACKNAME = "User specified bond cashflows";

    static final int INDEXASSUMPTIONTRACK = 20;
    static final String INDEXASSUMPTIONTRACKNAME = "Index assumption values";

    static final int CASHPCTTRACK = 21; // toggle bonds
    static final String CASHPCTTRACKNAME = "Pct Cash toggle bond payment";

    static final int RESETCAPTRACK = 22;
    static final String RESETCAPTRACKNAME = "Reset rate cap";

    static final int RESETFLOORTRACK = 23;
    static final String RESETFLOORTRACKNAME = "Reset rate floor";

    static final int REINVESTRATETRACK = 24;
    static final String REINVESTRATETRACKNAME = "Reinvestment rate";

    static final int FEDTAXPAYMENTTRACK = 25;
    static final String FEDTAXPAYMENTTRACKNAME = "Fed Tax payment";

    static final int STATETAXPAYMENTTRACK = 26;
    static final String STATETAXPAYMENTTRACKNAME = "State Tax payment";

    static final int REDEEMTRACK = 27;
    static final String REDEEMTRACKNAME = "Redemption payment";




    SchedTrack() {
        this.schedDate = new Dates();
        this.schedOptionalYn = UnAvails.NOTAVAILCHAR;
        this.schedPrice = UnAvails.NOTAVAILDOUBLE;
        this.schedSequence = UnAvails.NOTAVAILLONG;
        this.schedType = UnAvails.NOTAVAILINT; /* call, put, sink, etc */
        this.schedName = UnAvails.NOTAVAILSTRING;
        this.schedValue = UnAvails.NOTAVAILDOUBLE;
        this.trackNo = 0;
        this.lastSchedType = UnAvails.NOTAVAILINT;
        this.found = false;
        this.errmsg = null;
    }

    SchedTrack(int idx, int schedType, String schedTrackName, Dates schedDate, char schedOptionalYn, double schedValue, double schedPrice, long schedSequence) {
        this.schedType = schedType;
        this.schedDate = schedDate;
        this.schedName = schedTrackName;
        this.schedOptionalYn = schedOptionalYn;
        this.schedValue = schedValue;
        this.schedPrice = schedPrice;
        this.schedSequence = schedSequence;
        this.trackNo = idx;
        this.lastSchedType = schedType;
        this.found = false;
        this.errmsg = null;
    }

    public long createSequence(Dates dtDate, int schedType) {
        long seq = 100 * dtDate.getDateCode() + schedType;
        return seq;
    }


}





public class FixedRate {
    Bond bond;
    double totCash = 0.;
    double totPrin = 0.;
    double totSchedPrin = 0.;
    double totPrePayPrin = 0.;
    double totInt = 0.;
    CalcResults calcResults;


    FixedRate(Bond bond) {
        this.bond = bond;
        //this.bondDefault = new BondDefault(bond);
        // bondDefault.setDefaults(BondConsts.ALLCALCS, Instype.FIXEDRATE);
        // bond.bondDefault.setDefaults(BondConsts.ALLCALCS, Instype.FIXEDRATE);
    }


    private boolean isPaymentDate(Dates dtSettle) {
        Dates dtPay = bond.intSchedRule.getNextSchedDate(BondConsts.BEFOREONDATE, dtSettle);
        boolean onPayDt = dtSettle.eqDate(dtSettle, dtPay);
        return onPayDt;
    }


    private double getPrinCash(Dates dtSettle, Dates dtRedeem, double redeemPrin) {
        if (bond.stripType.equals(BondConsts.STRIPIO))
            return 0.;
        if (dtSettle.eqDate(dtSettle, dtRedeem))
            return redeemPrin;
        return 0.;
    }



/*
    class SortBySequence implements Comparator<SchedTracks> {
        public int compare(SchedTracks left, SchedTracks right) {
            if (left.schedTrack.schedSequence > right.schedTrack.schedSequence)
                return 1;
            if (left.schedTrack.schedSequence == right.schedTrack.schedSequence)
                return 0;
            return -1;
        }

    }
    */


}

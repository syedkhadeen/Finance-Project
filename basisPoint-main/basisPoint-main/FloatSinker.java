
public class FloatSinker {
    Bond bond;
    SchedRule schedRule;
    //BondDefault bondDefault;
    double totCash = 0.;
    double totPrin = 0.;
    double totSchedPrin = 0.;
    double totPrePayPrin = 0.;
    double totInt = 0.;
    double cumIntInc = 0.;

    FloatSinker(Bond bond) {
        this.bond = bond;
        //schedRule = new SchedRule(bond.intSched, bond.dayCount, bond.time, bond.er
    }

    // create set of schedtrack array for sinks and resets
    // BondDefaults creates the full set of schedule
    // default sinks add sinking funds to schedTrack
}


public class BondSchedule {

    Time time;
    Error error;

    SchedRule schedRule;
    Dates dtOccur;

    Dates dtFrom;

    String periodicity;
    String timeUnit;
    int repeatFactor;

    Dates dtBefore;
    Dates dtAfter;

    BondSchedule() {

    }

    BondSchedule(String args) {
        Error error = new Error();
        String str1900 = "01/01/1900";
        Dates dt1900 = new Dates(str1900, "MM/DD/YYYY");
        String[] strParts = args.split(";");
        periodicity = strParts[0];
        dtOccur = new Dates(strParts[1], "MM/DD/YYYY");
        dtFrom = new Dates(strParts[2], "MM/DD/YYYY");
        this.time = new Time(error);
        timeUnit = time.mapTimeUnit(periodicity);
        repeatFactor = time.mapRepeatFactor(periodicity);
        schedRule = new SchedRule(this.time);
        schedRule.dtOccur = dtOccur;
        schedRule.timeUnit = timeUnit;
        schedRule.repeatFactor = repeatFactor;
        schedRule.useEndOfMonthRuleYn = 'Y';
        schedRule.dayCountMeth = DayCount.DCMethod.DC_ACT;

    }


    public Dates calcNextSchedule(int beforeAfter) {
        Dates dtNext = new Dates();
        dtNext = schedRule.getNextSchedDate(beforeAfter, dtFrom);
        return dtNext;
    }

}

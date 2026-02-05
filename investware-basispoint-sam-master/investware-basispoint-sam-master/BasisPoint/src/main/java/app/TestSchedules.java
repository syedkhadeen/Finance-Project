package app;

public class TestSchedules {
    public static void main(String[] args) {
        String resultStr = "";
        BondSchedule bondSchedule = new BondSchedule(args[0]);
        Dates dtSchedB = bondSchedule.calcNextSchedule(BondConsts.BEFOREDATE);
        Dates dtSchedA = bondSchedule.calcNextSchedule(BondConsts.AFTERDATE);
        Dates dtSchedBO = bondSchedule.calcNextSchedule(BondConsts.BEFOREONDATE);
        Dates dtSchedAO = bondSchedule.calcNextSchedule(BondConsts.AFTERONDATE);
        resultStr = "Input: " + args[0] + "\n";
        resultStr = resultStr + "Last Payment: " + dtSchedB.getDateString() + " Next Payment: "
                + dtSchedA.getDateString() + "\n";
        resultStr = resultStr + "OnBeforeDate: " + dtSchedBO.getDateString() + " OnAfterDates: "
                + dtSchedAO.getDateString();
        System.out.println(resultStr);
    }
}
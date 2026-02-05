package app;

import java.util.ArrayList;
/*
Dictionary:

  cashflowResultsList  : a list of cashflow array of Total cashfloe, Interest Cashflow, Total Prin cashflow etc
  calcResultsList : array of individual analytical calc results: yieldTo maturity, accruedInt, convexity, etc
  totalCashflowResultList : cumulatuve array of cashflow summed up across multiple security calcs
  calcResultsMap = hashmap of calculated values for every security requested in portfoli

**/

public class CalcResults {
    // Output outputData;
    CalcDef calcDef = new CalcDef();
    double accruedInterest;
    double interestIncome;
    double yieldToMaturity;
    double priceToMaturity;
    double macDuration;
    double modDurationSIA;
    double modDurationSTD;
    double convexity;
    double cashflows;
    int couponDays;
    double yieldToWorst;
    double callPrcWorst;
    double maturityYears;
    int maturityDays;
    double wtdAvgMaturityYears;
    int wtdAvgMaturityDays;
    // String version;
    String parameters;
    String calculations;
    int daycount;
    double bookValue;
    double accreAmort;
    double zSpread;
    String choices;
    double principalPayment;
    double netPayment;
    double futureValue;
    double gainLoss;
    double maturityCash;
    double estimatedPrice;
    double estimatedMarkerValue;
    int parentCalc;
    int currentCalc;
    String resultStr;
    Dates dtWorst;
    double modDurationWorst;
    int paySchedCount;
    AccreAmortResults accreAmortResults;
    String[] cashflowNames = { "Date", "DayOfWeek", "Period", "Years", "TotalCash", "TotalPrin",
            "SchedPrin", "PrepayPrin", "Interest", "FedTaxes", "StateTaxes" };
    String[] paySchedNames = { "AccrueDt", "PayDt", "DayOfWeek", "Days", "Holiday" };
    String[] accreAmortSchedNames = { "Begin", "End", "BookStart", "BookEnd", "Accre/Amort", "DailyAccre/Amort",
            "Days" };
    ArrayList<PaySchedule> payScheduleList = new ArrayList<PaySchedule>();
    ArrayList<AccreAmortResults> accreAmortScheduleList = new ArrayList<AccreAmortResults>();
    // List of requested calculation results for a given security

    CalcResults() {
        calcDef.setCalcResultsDefinition();
        accruedInterest = UnAvails.NOTAVAILDOUBLE;
        interestIncome = UnAvails.NOTAVAILDOUBLE;
        yieldToMaturity = UnAvails.NOTAVAILDOUBLE;
        priceToMaturity = UnAvails.NOTAVAILDOUBLE;
        macDuration = UnAvails.NOTAVAILDOUBLE;
        modDurationSIA = UnAvails.NOTAVAILDOUBLE;
        modDurationSTD = UnAvails.NOTAVAILDOUBLE;
        convexity = UnAvails.NOTAVAILDOUBLE;
        cashflows = UnAvails.NOTAVAILDOUBLE;
        couponDays = UnAvails.NOTAVAILINT;
        yieldToWorst = UnAvails.NOTAVAILDOUBLE;
        callPrcWorst = UnAvails.NOTAVAILDOUBLE;
        modDurationWorst = UnAvails.NOTAVAILDOUBLE;
        maturityYears = UnAvails.NOTAVAILDOUBLE;
        maturityDays = UnAvails.NOTAVAILINT;
        wtdAvgMaturityYears = UnAvails.NOTAVAILDOUBLE;
        wtdAvgMaturityDays = UnAvails.NOTAVAILINT;
        parameters = "";
        calculations = "";
        daycount = UnAvails.NOTAVAILINT;
        bookValue = UnAvails.NOTAVAILDOUBLE;
        accreAmort = UnAvails.NOTAVAILDOUBLE;
        zSpread = UnAvails.NOTAVAILDOUBLE;
        choices = "";
        principalPayment = UnAvails.NOTAVAILDOUBLE;
        netPayment = UnAvails.NOTAVAILDOUBLE;
        futureValue = UnAvails.NOTAVAILDOUBLE;
        gainLoss = UnAvails.NOTAVAILDOUBLE;
        maturityCash = UnAvails.NOTAVAILDOUBLE;
        paySchedCount = UnAvails.NOTAVAILINT;
        resultStr = "";
        AccreAmortResults accreAmortResults = new AccreAmortResults();
        parentCalc = UnAvails.NOTAVAILINT;
        currentCalc = UnAvails.NOTAVAILINT;
        String headerDelimiter = "   ";
        calculations = "";
    }

    public char isCalcMoney(int calcNo) {
        char isMoney = 'N';
        if ('Y' == calcDef.calcDefs.get(calcNo).isMoney)
            isMoney = 'Y';
        return isMoney;
    }

    String setItemName(String itemName, int value, String comma, String format) {
        String itemString;
        itemString = "\"" + itemName + "\"" + ":" + String.format(format, value) + comma;
        return itemString;
    }

    String setItemName(String itemName, String str, String comma) {
        String itemString;
        itemString = "\"" + itemName + "\"" + ":" + "\"" + str + "\"" + comma;
        itemString.trim();
        return itemString;
    }

    String setItemName(String itemName, char sChar, String comma) {
        String itemString;
        itemString = "\"" + itemName + "\"" + ":" + "\"" + sChar + "\"" + comma;
        itemString.trim();
        return itemString;
    }

    String setItemName(String itemName, Dates date, String comma) {
        String itemString;
        itemString = "\"" + itemName + "\"" + ":" + "\"" + date.getDateString() + "\"" + comma;
        return itemString;
    }

    public void addPaySchedule(int index, Dates dtAccrue, Dates dtPay, String dow, int days, char isHoliday) {
        payScheduleList.add(new PaySchedule(index, dtAccrue, dtPay, dow, days, isHoliday));
    }

    public void addAccreAmortSchedule(int index, Dates dtStart, Dates dtEnd, double bkStart, double bkEnd,
            double accreAmort,
            double dailyAccreAmort, int days) {
        accreAmortScheduleList
                .add(new AccreAmortResults(index, dtStart, dtEnd, bkStart, bkEnd, accreAmort, dailyAccreAmort,
                        days));
        ++accreAmortResults.count;
    }

    class PaySchedule {
        Dates dtAccrue;
        Dates dtPay;
        String dow;
        int days;
        char isHolidayYn;

        PaySchedule(int index, Dates dtAccrue, Dates dtPay, String dow, int days, char isHolidayYn) {
            this.dtAccrue = dtAccrue;
            this.dtPay = dtPay;
            this.dow = dow;
            this.days = days;
            this.isHolidayYn = isHolidayYn;

        }
    }

    class AccreAmortResults {
        int count;
        Dates dtStart;
        Dates dtEnd;
        double bkStart;
        double bkEnd;
        double accreAmort;
        double dailyAccreAmort;
        int days;
        double sumAccreAmort;
        double sumDailyAccrreAmort;
        int sumDays;

        AccreAmortResults(int index, Dates dtStart, Dates dtEnd, double bkStart, double bkEnd, double accreAmort,
                double dailyAccreAmort, int days) {
            ++this.count;
            this.dtStart = dtStart;
            this.dtEnd = dtEnd;
            this.bkStart = bkStart;
            this.bkEnd = bkEnd;
            this.accreAmort = accreAmort;
            this.dailyAccreAmort = dailyAccreAmort;
            this.days = days;
            this.sumAccreAmort += accreAmort;
            this.sumDailyAccrreAmort += dailyAccreAmort;
            this.sumDays += days;
        }

        AccreAmortResults() {
            this.count = 0;
            this.dtStart = new Dates();
            this.dtEnd = new Dates();
            this.bkStart = 0;
            this.bkEnd = 0.;
            this.accreAmort = 0.;
            this.dailyAccreAmort = 0.;
            this.days = 0;
            this.sumAccreAmort = 0.;
            this.sumDailyAccrreAmort = 0.;
            this.sumDays = 0;

        }

        public void zeroAccreAmortResultList() {
            this.count = 0;
            this.dtStart = new Dates();
            this.dtEnd = new Dates();
            this.bkStart = 0;
            this.bkEnd = 0.;
            this.accreAmort = 0.;
            this.dailyAccreAmort = 0.;
            this.days = 0;
            this.sumAccreAmort = 0.;
            this.sumDailyAccrreAmort = 0.;
            accreAmortScheduleList.removeAll(accreAmortScheduleList);

        }
    }

    class PassthruFactorSched {
        int nofactors;
        String securityName;
        Dates dtData = new Dates();
        Dates dtRun = new Dates();
    }

    class PassthruFactor {
        Dates dtFactor = new Dates();
        double factor;
        double remPrincipal;
    }

    class AccreAmort {
        double accreAmortAmt; /* total accretion/amortization between coupons */
        double dailyAccreAmortAmt; /* daily accreAmort between coupon periods for accreamort schedule */
        Dates dtaccreAmort = new Dates(); /* start and coupon dates for accreamort from start to enddt */
        Dates dtaccreAmortStart = new Dates(); // start date of accreAmort period
        Dates dtaccreAmortEnd = new Dates();
        double bookValueStart;
        double bookValueEnd;
        int accrueDays; // for pay period schedule
    }

    class AccreAmortSched {
        int noAccreAmorts;
        String account;
        String bookMethod;
        Dates dtData = new Dates();
        Dates dtRun = new Dates();
        AccreAmort[] accreAmort = new AccreAmort[Limits.MAXACCREAMORTS];

    }

    // This structure contains the calculation results for bonds in a portfolio
    // and also the totals (weighted or not) for each calculated it requested

    class MontecarloResults {
        double avgMarketValue; // BPMAXPORTFOLIOS
        double avgRateReturn;
        double stdDevMarketValue; // BPMAXPORTFOLIOS
        double stdDevRateReturn;
        double avgDeltaReturn; // difference in return between portfolio and index
        double deltaStdDev; // std deviation of difference in return between index and portfolio
        double optionPrice; // price of embedded option in security
        double stdDevOptionPrice; // std deviation of embedded option price in security
        int noMonteCarloRuns;
    }
}
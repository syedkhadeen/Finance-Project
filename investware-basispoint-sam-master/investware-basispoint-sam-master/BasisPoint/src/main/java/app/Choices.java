package app;

import java.util.ArrayList;
import java.util.List;

public class Choices {
    // for strip choices
    Error error;
    static final String HOLDATCOST = "COST";
    static final String STRIGHTLINE = "STLINE";
    static final String CONSTYIELD = "CONSTYLD";
    static final String CONSTYIELDP = "CONSTYLD+";
    static final String EFFINCOME = "EFFINCOME";
    static final String EFFINCOMEP = "EFFINCOME+";
    static final String PROSPECTYIELD = "PROSPECTYLD";
    public ArrayList<String> noList = new ArrayList<String>();
    public ArrayList<String> floatSinkList = new ArrayList<String>();
    public Integer[] MoneyList = new Integer[Limits.MAXCALCS];
    //
    public ArrayList<String> instypeList = new ArrayList<>();
    public ArrayList<String> swapList = new ArrayList<String>();
    public ArrayList<String> fixedRateList = new ArrayList<>();
    public ArrayList<Integer> schedTypeList = new ArrayList<Integer>();
    public ArrayList<Integer> payList = new ArrayList<Integer>();
    public ArrayList<Integer> paySinkList = new ArrayList<Integer>();
    public ArrayList<Integer> resetRateList = new ArrayList<Integer>();
    public ArrayList<String> floatList = new ArrayList<String>();
    public ArrayList<String> indexList = new ArrayList<String>();
    public ArrayList<String> outputStringList = new ArrayList<>();
    public ArrayList<String> sinkList = new ArrayList<String>();
    public ArrayList<Integer> cashflowSchedList = new ArrayList<Integer>();
    public ArrayList<Integer> interestCashList = new ArrayList<Integer>();
    public ArrayList<String> accrueBasisList = new ArrayList<String>();
    public ArrayList<String> compoundFreqList = new ArrayList<String>();
    public ArrayList<String> bondEqvList = new ArrayList<String>();
    public ArrayList<String> bookCalcList = new ArrayList<String>();
    public ArrayList<Integer> accrualAccountingList = new ArrayList<Integer>(3);
    public ArrayList<Integer> documentList = new ArrayList<Integer>();
    public ArrayList<String> conversionOptionList = new ArrayList<String>();
    public ArrayList<String> dayOfWeekList = new ArrayList<String>();
    public ArrayList<String> countryList = new ArrayList<String>();
    public ArrayList<String> callTypeList = new ArrayList<String>();
    public ArrayList callPutPriceTypeList = new ArrayList<String>();
    public ArrayList<String> schedRuleList = new ArrayList<String>();
    public ArrayList<String> indexInterpList = new ArrayList<String>();

    public ArrayList<Integer> yieldCalcList = new ArrayList<>();
    public ArrayList<String> currencyList = new ArrayList<>();
    public ArrayList<String> passthruList = new ArrayList<String>();
    public ArrayList<String> passthruProjectList = new ArrayList<String>();
    public ArrayList<String> timeUnitList = new ArrayList<String>();
    public ArrayList intPayList = new ArrayList<String>();
    public ArrayList delimiterList = new ArrayList<String>();
    public ArrayList holidayOffSetList = new ArrayList();
    public ArrayList perpetualList = new ArrayList();
    public ArrayList compatableCalcList = new ArrayList();
    public ArrayList noSecurityCalcList = new ArrayList();
    public ArrayList<Integer> reinvestCalcList = new ArrayList();
    public ArrayList<Integer> calcPriceList = new ArrayList<>();
    public ArrayList<Integer> discountCurveList = new ArrayList<>();
    public ArrayList<Integer> futureCalcList = new ArrayList<>();
    public ArrayList<String> reinVestList = new ArrayList<>();
    public ArrayList<String> yieldForecastList = new ArrayList<>();
    public ArrayList<Integer> bypassDefaultList = new ArrayList<>();

    // SchedTracks schedTracks;
    int noChoices = 0;
    List<Choice> choiceList = new ArrayList<Choice>();
    ArrayList<Integer> payResetList = new ArrayList<Integer>();
    ArrayList<Integer> cashflowCalcList = new ArrayList<>();
    ArrayList<String> passModelList = new ArrayList<String>();
    ArrayList<String> stripTypeList = new ArrayList<String>();
    ArrayList<String> discountList = new ArrayList<String>();
    ArrayList<String> dateFormatList = new ArrayList<String>();
    ArrayList<String> couponQuoteList = new ArrayList<String>();
    ArrayList<String> holidayOffsetList = new ArrayList<String>();
    ArrayList<String> yieldQuoteList = new ArrayList<String>();
    ArrayList<String> accruedAdjustList = new ArrayList<String>();
    ArrayList<String> yieldCurveInterpList = new ArrayList<String>();
    ArrayList<String> accreAmortControlList = new ArrayList<String>(4);
    ArrayList<String> creditDefaultModelList = new ArrayList<String>();

    ArrayList<String> matNameList = new ArrayList<>();
    ArrayList<String> monthOfYearList = new ArrayList<String>();
    ArrayList<String> matureLengthList = new ArrayList<String>();
    ArrayList<Integer> yieldCurveList = new ArrayList<Integer>(); // calcs needing a yield curve
    ArrayList<String> swapLegTypeList = new ArrayList<String>();
    ArrayList<String> swapPayTypeList = new ArrayList<String>();
    ArrayList<String> bookYieldOptList = new ArrayList<String>();

    ArrayList<String> spotIntervalList = new ArrayList<>();
    ArrayList<String> accIntMethod = new ArrayList<>();
    ArrayList<String> tradeTypeList = new ArrayList<>();
    ArrayList<Integer> futureDiscountCurveList = new ArrayList<>();
    ArrayList<Integer> bypassMoreDefaultList = new ArrayList<>();
    ArrayList<String> yieldTypeList = new ArrayList<>(); // Risk free, credit spread, Net RF and Sppread, Fixing
    ArrayList<String> portfolioParamsList = new ArrayList<>();
    ArrayList<String> riskReturnList = new ArrayList<>();
    ArrayList<String> benchmarkList = new ArrayList<>();
    ArrayList<String> swapSideList = new ArrayList<>();

    Choices() {

    }

    public Choices(Error error) {
        this.error = error;
    }

    public void addChoiceList(int idx, String paramName, String domain, String choice, String define) {
        choiceList.add(new Choice(idx, paramName, domain, choice, define));
        ++this.noChoices;
    }

    public void setNoList() {
        this.noList.add("");
    }

    public void setInstypeList() {
        int i = 0;
        String parameter = "Instype";
        this.instypeList.add(i, "ARM");
        addChoiceList(noChoices, parameter, "String", "Arm", "Adjustable rate mortgage");
        this.instypeList.add(++i, "ATMATURITY");
        addChoiceList(noChoices, parameter, "String", "AtMaturity", "One interest payment at maturity like a CD");
        this.instypeList.add(++i, "CURRENCY");
        addChoiceList(noChoices, parameter, "String", "Currency", "Pure cash or cash equivalent");
        this.instypeList.add(++i, "FACTOR");
        addChoiceList(noChoices, parameter, "String", "Factor",
                "Many principal payment expresses as a sequence of redemption factors");
        this.instypeList.add(++i, "FACTORFLOAT");
        addChoiceList(noChoices, parameter, "String", "FactorFloat",
                "A floating interest rates combined with principal payment factors");
        this.instypeList.add(++i, "FIXEDRATE");
        addChoiceList(noChoices, parameter, "String", "FixedRate",
                "A fixed rate coupon bond with more than one interest payment");
        this.instypeList.add(++i, "FLOATER");
        addChoiceList(noChoices, parameter, "String", "Floater",
                "A floating rate bond tied to a benchmark index or a stepped rate coupon bond");
        this.instypeList.add(++i, "FUTURE");
        addChoiceList(noChoices, parameter, "String", "Future", "A financial future");
        this.instypeList.add(++i, "INDEXED");
        addChoiceList(noChoices, parameter, "String", "Indexed",
                "An inflation protected bond with face value increasing or decreasing with inflation");
        this.instypeList.add(++i, "INDEXSINK");
        addChoiceList(noChoices, parameter, "String", "IndexSink",
                "An inflation protected bond combined with sinking fund schedule");
        this.instypeList.add(++i, "IRSSWAP");
        addChoiceList(noChoices, parameter, "String", "IRSSwap",
                "An interest rate swap with two legs one fixed another variable or both legs variable");
        this.instypeList.add(++i, "MORTGAGE");
        addChoiceList(noChoices, parameter, "String", "Mortgage",
                "A mortgage pass-thru bond containing a large pool of mortgages");
        this.instypeList.add(++i, "OPTION");
        addChoiceList(noChoices, parameter, "String", "Option", "An option on a swap or future");
        this.instypeList.add(++i, "PERPETUAL");
        addChoiceList(noChoices, parameter, "String", "Perpet", "A perpetual bond with no principal payment");
        this.instypeList.add(++i, "PERPETUALFLOAT");
        addChoiceList(noChoices, parameter, "String", "PerpetualFloat",
                "A perpetual bond combined with floating interest rates");
        this.instypeList.add(++i, "PIK");
        addChoiceList(noChoices, parameter, "String", "Pik",
                "A payment-in-kind bond where baby bonds are paid in lieu of interest payments");
        this.instypeList.add(++i, "PIKFLOAT");
        addChoiceList(noChoices, parameter, "String", "PikFloat",
                "A payment-in-kind combined with floating interest rates");
        this.instypeList.add(++i, "PIKSINKER");
        addChoiceList(noChoices, parameter, "String", "PikSinker",
                "A payment-in-kind bond combined with a sinking fund schedule");
        this.instypeList.add(++i, "SINKER");
        addChoiceList(noChoices, parameter, "String", "Sinker",
                " A sinking fund bond containing a schedule of sinking fund payments");
        this.instypeList.add(++i, "FLOATSINKER");
        addChoiceList(noChoices, parameter, "String", "FloatSinked",
                "A sinking fund bond combined with floating interest rates");
        this.instypeList.add(++i, "TOGGLE");
        addChoiceList(noChoices, parameter, "String", "Toggle",
                "A bond where each interest payment can be made as percent cash and a percent of baby bonds");
        this.instypeList.add(++i, "DISCOUNT");
        addChoiceList(noChoices, parameter, "String", "Discount",
                "A zero coupon bond with only a principal paymetn at maturity");
        this.instypeList.add(++i, "DISCOUNTSINKER");
        addChoiceList(noChoices, parameter, "String", "DiscountSinker", "A discount bond combined with a sinking fund");
        this.instypeList.add(++i, "CDSSWAP");
        addChoiceList(noChoices, parameter, "String", "CDSwap", "A credit default swap");
    }

    public boolean checkPeriodicity(String sVal) {
        boolean isOk = false;
        char ch = ' ';
        String repeatStr;
        // get last character
        int noChars = sVal.length();
        ch = sVal.charAt(noChars - 1);
        if (ch != 'M' && ch != 'W' && ch != 'D') {
            this.error.hasErrorYn = 'Y';
            error.setError(getClass().getName(), "an invalid periodocity of " + sVal + " has been entered");
            return false;
        }
        repeatStr = sVal.substring(0, noChars - 1);
        try {
            int repeatFac = Integer.parseInt(repeatStr);
        } catch (Exception ex) {
            error.setError(getClass().getName(), "an invalid periodocity of " + sVal + " has been entered");
            return false;
        }
        return true;
    }

    public void setFloatSinkList() {
        this.floatSinkList.add(0, "SINKER");
        this.floatSinkList.add(1, "FLOATER");
    }

    public boolean inFloatSinkList(String instype) {
        return floatSinkList.contains(instype);
    }

    public void setSwapList() {
        String parameter = "Swap Type";
        this.swapList.add(0, "IRSSWAP");
        addChoiceList(noChoices, parameter, "String", "IRSSwap",
                "An exchange of fixed rate coupons and floating rate coupon payments");
        this.swapList.add(1, "CDSSWAP");
        addChoiceList(noChoices, parameter, "String", "IRSSwap",
                "An exchange of a premium insurance payment and a payout if a default or credit drop occurs");
    }

    public void setBypassDefaultList() {
        bypassDefaultList.add(0, CalcList.VERSION);
        bypassDefaultList.add(1, CalcList.PARAMETERS);
        bypassDefaultList.add(2, CalcList.CALCULATIONS);
        bypassDefaultList.add(3, CalcList.CHOICES);
        bypassDefaultList.add(4, CalcList.BONDPARAMS);
        bypassDefaultList.add(5, CalcList.SPOTCURVE);
        return;
    }

    public void setDelimiterList() {
        String parameter = "Calc Delimiter";
        this.delimiterList.add(0, "=");
        addChoiceList(noChoices, parameter, "String", "=",
                "The separation string for plain text non-JSON calculation output");
        this.delimiterList.add(1, ",");
        addChoiceList(noChoices, parameter, "String", "=",
                "The separation string for Excel CSV files for non-JSON calculation output");
        this.delimiterList.add(2, ";");
        addChoiceList(noChoices, parameter, "String", ":",
                "The separation string for plain text for non-JSON calculation output");
    }

    public boolean inDelimiterList(String delimiter) {
        return delimiterList.contains(delimiter);
    }

    public void setBenchmarkList() {
        String parameter = "Benchmarks";
        int i = 0;
        this.benchmarkList.add(i++, "SOFR");
        addChoiceList(noChoices, parameter, "String", "SOFR", "Secured Overnight Financing Rate");
        this.benchmarkList.add(i++, "FEDFUNDS");
        addChoiceList(noChoices, parameter, "String", "FEDFUNDS", "US Fed Funds rate");
        this.benchmarkList.add(i++, "IBOR");
        addChoiceList(noChoices, parameter, "String", "IBOR", "Interbank offer rate");
    }

    public boolean inBenchmarkList(String benchmark) {
        return benchmarkList.contains(benchmark);
    }

    public void setSwapSideList() {
        String parameter = "Swap Sides";
        int i = 0;
        this.swapSideList.add(i++, Codes.PAYLEG);
        addChoiceList(noChoices, parameter, "String", Codes.PAYLEG, "Pay leg of an IR Swap");
        this.swapSideList.add(i++, Codes.RECLEG);
        addChoiceList(noChoices, parameter, "String", Codes.RECLEG, "Receive leg of an IR Swap");
        this.swapSideList.add(i++, Codes.NETLEG);
        addChoiceList(noChoices, parameter, "String", Codes.NETLEG, "The receive minus the pay leg of an IRSWP");
    }

    boolean inSwapSideList(String swapSide) {
        return swapSideList.contains(swapSide);
    }

    public void setTradeTypeList() {
        String parameter = "Trade Type";
        this.tradeTypeList.add(0, "B");
        addChoiceList(noChoices, parameter, "String", "B", "Indicates a buy");
        this.tradeTypeList.add(1, "S");
        addChoiceList(noChoices, parameter, "String", "S", "Indicates a sell");
    }

    public boolean inTradeTypeList(String tradeType) {
        return tradeTypeList.contains(tradeType);
    }

    public void setRiskReturnList() {
        String parameter = "Risk/Return Goal";
        int i = 0;
        this.riskReturnList.add(i++, "MAXUPSIDERETURN");
        addChoiceList(noChoices, parameter, "String", "MAXUPSIDERATE", "Maximize up side return");
        this.riskReturnList.add(i++, "MAXDNSIDERETURN");
        addChoiceList(noChoices, parameter, "String", "S", "Maximize down side return");
        this.riskReturnList.add(i++, "MAXEXPRETURN");
        addChoiceList(noChoices, parameter, "String", "S", "Maximize expected return");
        this.riskReturnList.add(i++, "MAXSHARPRATIO");
        addChoiceList(noChoices, parameter, "String", "S", "Maximize sharp ration");
        this.riskReturnList.add(i++, Codes.MAXFUTUREVALUE);
        addChoiceList(noChoices, parameter, "String", Codes.MAXFUTUREVALUE,
                "Maximize the future value when evaluating buys");
        this.riskReturnList.add(i++, Codes.MINFUTUREVALUELOSS);
        addChoiceList(noChoices, parameter, "String", Codes.MINFUTUREVALUELOSS,
                "Minimize the loss of future value when evaluating sells");
        return;
    }

    public boolean inRiskRetunList(String riskReturnGoal) {
        return riskReturnList.contains(riskReturnGoal);
    }

    public void setFutureDiscountCurveList() {
        this.futureDiscountCurveList.add(0, CalcList.HORIZONANALYSIS);
        this.futureDiscountCurveList.add(1, CalcList.ATTRIBUTION);
        this.futureDiscountCurveList.add(2, CalcList.SWAPANALYSIS);
        this.futureDiscountCurveList.add(3, CalcList.SHARPRATIO);
    }

    public boolean inFutureDiscountCurveList(int calcNo) {
        return futureDiscountCurveList.contains(calcNo);
    }

    public boolean inFutureDiscountCurveList(ArrayList<Integer> calculationList) {
        int count = calculationList.size();
        int no = -1;
        while (++no < count) {
            int calcNo = calculationList.get(no);
            if (futureCalcList.contains(calcNo))
                return true;
        }
        return true;
    }

    public void setFixedRateList() {
        this.fixedRateList.add("FACTORED");
        this.fixedRateList.add("ATMATURITY");
        this.fixedRateList.add("FIXEDRATE");
        this.fixedRateList.add("FUTURE");
        this.fixedRateList.add("INDEXED");
        this.fixedRateList.add("INDEXSINK");
        this.fixedRateList.add("MORTGAGE");
        this.fixedRateList.add("OPTION");
        this.fixedRateList.add("PERPETUAL");
        this.fixedRateList.add("PIK");
        this.fixedRateList.add("PIKSINKER");
        this.fixedRateList.add("SINKER");
    }

    public void setSchedTypeList() {
        int i = 0;
        /*
         * this.schedTypeList.add(i, SchedTrack.ANYTYPETRACK);
         * this.schedTypeList.add(++i, SchedTrack.INTERESTPAYTRACK);
         * this.schedTypeList.add(++i, SchedTrack.PRINCIPALPAYTRACK);
         * this.schedTypeList.add(++i, SchedTrack.TOTALCASHPAYTRACK);
         * this.schedTypeList.add(++i, SchedTrack.SCHEDULEPRINPAYTRACK);
         * this.schedTypeList.add(++i, SchedTrack.PREPRINPAYTRACK);
         * this.schedTypeList.add(++i, SchedTrack.TOTPRINPAYTRACK);
         * this.schedTypeList.add(++i, SchedTrack.CALLTRACK);
         * this.schedTypeList.add(++i, SchedTrack.PUTTRACK);
         * this.schedTypeList.add(++i, SchedTrack.INDEXRATIOTRACK);
         * this.schedTypeList.add(++i, SchedTrack.INDEXCPITRACK);
         * this.schedTypeList.add(++i, SchedTrack.SINKTRACK);
         * this.schedTypeList.add(++i, SchedTrack.PRINFACTORTRACK);
         * this.schedTypeList.add(++i, SchedTrack.PIKTRACK);
         * this.schedTypeList.add(++i, SchedTrack.RESETRATETRACK);
         * this.schedTypeList.add(++i, SchedTrack.FXRATETRACK);
         * this.schedTypeList.add(++i, SchedTrack.ACCRUETRACK);
         * this.schedTypeList.add(++i, SchedTrack.USERCASHFLOWTRACK);
         * this.schedTypeList.add(++i, SchedTrack.INDEXASSUMPTIONTRACK);
         * 
         */
    }

    public void setHolidayOffsetList() {
        int i = 0;
        String parameter = "Holiday Offset";
        this.holidayOffsetList.add(i, BondConsts.IGNOREHOLIDAYS);
        addChoiceList(noChoices, parameter, "String", BondConsts.IGNOREHOLIDAYS, "Ignore Hokidays");
        this.holidayOffsetList.add(++i, BondConsts.BEFOREHOLIDAY);
        addChoiceList(noChoices, parameter, "String", BondConsts.BEFOREHOLIDAY, "Last business days prior to holiday");
        this.holidayOffsetList.add(++i, BondConsts.AFTERHOLIDAY);
        addChoiceList(noChoices, parameter, "String", BondConsts.AFTERHOLIDAY, "First business days after holiday");
        this.holidayOffsetList.add(++i, BondConsts.AFTERHOLIDAYSAMEMONTH);
        addChoiceList(noChoices, parameter, "String", BondConsts.AFTERHOLIDAYSAMEMONTH,
                "First business days after holiday but in same month as scheduled payment");
    }

    public void setPayList() {
        this.payList.add(SchedTrack.INTERESTPAYTRACK);
    }

    public boolean inPayList(int schedType) {
        return payList.contains(schedType);
    }

    public void setPerpetualList() {
        this.perpetualList.add(0, Instype.PERPETUAL);
        this.perpetualList.add(1, Instype.PERPETUALFLOAT);
    }

    public boolean inPerpetualList(String instype) {
        return perpetualList.contains(instype);
    }

    public void setMatNameList() {
        int i = 0;
        String parameter = "Curve Maturity";
        this.matNameList.add(i++, "1m");
        addChoiceList(noChoices, parameter, "String", "1m", "Yield maturity of 1 month");
        this.matNameList.add(i++, "2m");
        addChoiceList(noChoices, parameter, "String", "2m", "Yield maturity of 2 months");
        this.matNameList.add(i++, "3m");
        addChoiceList(noChoices, parameter, "String", "3m", "Yield maturity of 3 months");
        this.matNameList.add(i++, "4m");
        addChoiceList(noChoices, parameter, "String", "4m", "Yield maturity of 4 months");
        this.matNameList.add(i++, "6m");
        addChoiceList(noChoices, parameter, "String", "6m", "Yield maturity of 6 months");
        this.matNameList.add(i++, "1y");
        addChoiceList(noChoices, parameter, "String", "1y", "Yield maturity of 1 year");
        this.matNameList.add(i++, "2y");
        addChoiceList(noChoices, parameter, "String", "2y", "Yield maturity of 2 years");
        this.matNameList.add(i++, "3y");
        addChoiceList(noChoices, parameter, "String", "3y", "Yield maturity of 3 years");
        this.matNameList.add(i++, "4y");
        addChoiceList(noChoices, parameter, "String", "4y", "Yield maturity of 4 years");
        this.matNameList.add(i++, "5y");
        addChoiceList(noChoices, parameter, "String", "5y", "Yield maturity of 5 years");
        this.matNameList.add(i++, "7y");
        addChoiceList(noChoices, parameter, "String", "7y", "Yield maturity of 7 years");
        this.matNameList.add(i++, "10y");
        addChoiceList(noChoices, parameter, "String", "10y", "Yield maturity of 10 years");
        this.matNameList.add(i++, "15y");
        addChoiceList(noChoices, parameter, "String", "15y", "Yield maturity of 15 years");
        this.matNameList.add(i++, "20y");
        addChoiceList(noChoices, parameter, "String", "20y", "Yield maturity of 20 years");
        this.matNameList.add(i++, "25");
        addChoiceList(noChoices, parameter, "String", "25y", "Yield maturity of 25 years");
        this.matNameList.add(i++, "30y");
        addChoiceList(noChoices, parameter, "String", "30y", "Yield maturity of 30 years");
        this.matNameList.add(i++, "40y");
        addChoiceList(noChoices, parameter, "String", "40y", "Yield maturity of 40 years");
        this.matNameList.add(i++, "50y");
        addChoiceList(noChoices, parameter, "String", "50y", "Yield maturity of 50 years");
        this.matNameList.add(i++, "60y");
        addChoiceList(noChoices, parameter, "String", "60y", "Yield maturity of 60 years");
    }

    boolean inMatLenList(String matStr) {
        return matNameList.contains(matStr);
    }

    public void setPaySinkList() {
        paySinkList.add(SchedTrack.INTERESTPAYTRACK);
        paySinkList.add(SchedTrack.SINKTRACK);
    }

    public boolean inPaySinkList(int schedType) {
        return paySinkList.contains(schedType);
    }

    public void setPayResetList() {
        this.payResetList.add(SchedTrack.INTERESTPAYTRACK);
        this.payResetList.add(SchedTrack.RESETRATETRACK);
    }

    public boolean inPayResetList(int schedType) {
        return payResetList.contains(schedType);
    }

    public void setResetRateList() {
        resetRateList.add(SchedTrack.RESETRATETRACK);
    }

    public boolean inResetRateList(int schedType) {
        return resetRateList.contains(schedType);
    }

    public void setFloatList() {
        this.floatList.add("FLOATER");
        this.floatList.add("SINKFLOATER");
        this.floatList.add("FACTORFLOAT");
        this.floatList.add("IRSSWAP");
    }

    public boolean inFloatList(String instype) {
        return floatList.contains(instype);
    }

    public void setIndexList() {
        indexList.add(Instype.INDEXED);
        indexList.add(Instype.INDEXSINK);

    }

    public void setYieldForecastList() {
        String parameter = "YieldForecastMeth";
        yieldForecastList.add(CalcList.FORECASTFORWARD);
        addChoiceList(noChoices, parameter, "String", CalcList.FORECASTFORWARD,
                "Use the forward curve for future value calculations");
        yieldForecastList.add(CalcList.FORECASTFORWARD);
        addChoiceList(noChoices, parameter, "String", CalcList.FORECASTFORWARD,
                "Use the forward curve for future value calculations");
        yieldForecastList.add(CalcList.FORECASTSPOT);
        addChoiceList(noChoices, parameter, "String", CalcList.FORECASTSPOT,
                "Use the spot curve for future value calculations");

    }

    public boolean inYieldForecastList(String yieldCurveForecast) {
        return yieldForecastList.contains(yieldCurveForecast);
    }

    public boolean inSwapList(String instype) {
        return swapList.contains(instype);
    }

    public void setOutputStringList() {
        String parameter = "OutputFormats";
        outputStringList.add(BondConsts.JSON);
        addChoiceList(noChoices, parameter, "String", BondConsts.KEYVALUE, "Output string results in JSON format");
        outputStringList.add(BondConsts.KEYVALUE);
        addChoiceList(noChoices, parameter, "String", BondConsts.KEYVALUE,
                "Output string results in in key-value format with = separator and ; separating key-values");
        outputStringList.add(BondConsts.NONE);
        addChoiceList(noChoices, parameter, "String", BondConsts.NONE, "Do not output any string results");
    }

    public void setSinkList() {
        this.sinkList.add("SINKER");
        this.sinkList.add("INDEXSINKER");
        this.sinkList.add("PIKSINKER");
        this.sinkList.add("SINKFLOATER");
        this.sinkList.add("DISCOUNTSINKER");
    }

    public boolean inSinkList(String instype) {
        return sinkList.contains(instype);
    }

    public boolean instypeList(String instype) {
        String upper = instype.toUpperCase();
        return this.instypeList.contains(upper);
    }

    public boolean inIndexList(String instype) {
        return indexList.contains(instype);
    }

    public String getInstypeName(String instype) {
        int idx = instypeList.indexOf(instype);
        String sInstyp = instypeList.get(idx);
        return sInstyp;
    }

    public void setCashflowSchedList() {
        this.cashflowSchedList.add(0, SchedTrack.INTERESTPAYTRACK);
        this.cashflowSchedList.add(1, SchedTrack.SINKTRACK);
        this.cashflowSchedList.add(2, SchedTrack.PREPRINPAYTRACK);
        this.cashflowSchedList.add(3, SchedTrack.USERCASHFLOWTRACK);
        this.cashflowSchedList.add(4, SchedTrack.PRINCIPALPAYTRACK);
    }

    public void setInterestCashList() {
        this.interestCashList.add(0, SchedTrack.INTERESTPAYTRACK);
        this.interestCashList.add(1, SchedTrack.RESETRATETRACK);
    }

    public void setAccrueBasisList() {
        String parameter = "Accrual Basis";
        this.accrueBasisList.add("30/360A");
        addChoiceList(noChoices, parameter, "String", "30/360A",
                "Assumes 31 day months changed to 30 day months only if start month was a 30 day month and Feb unchanged with 360 days per year");
        this.accrueBasisList.add("30E/360"); // 30E is ISMA
        addChoiceList(noChoices, parameter, "String", "30E/360",
                "Assumes 30 day months with Feb unchanged with 360 days per year");
        this.accrueBasisList.add("30E+/360"); // 30E is ISMA
        addChoiceList(noChoices, parameter, "String", "30E+/360",
                "Assumes 30 day months with Feb changed to 30 if maturity is on last day in Feb days per year");
        this.accrueBasisList.add("30EE/360"); // 30E is ISMA
        addChoiceList(noChoices, parameter, "String", "30E+/360",
                "Assumes 30 day months with Feb changed to 30 if maturity is on last day in Feb days per year");
        this.accrueBasisList.add("30/360");
        addChoiceList(noChoices, parameter, "String", "30/360",
                "Assumes 30 day months including Feb with 360 days per year");
        this.accrueBasisList.add("30/ISDA");
        addChoiceList(noChoices, parameter, "String", "30/ISDA",
                "Assumes 30 day months with Feb unchanged and days in maturity month unchanged with 360 days per year");
        this.accrueBasisList.add("ACT/360");
        addChoiceList(noChoices, parameter, "String", "Act/360",
                "Assumes accrual days between dates and 360 days in a year");
        this.accrueBasisList.add("ACT/ACT");
        addChoiceList(noChoices, parameter, "String", "Act/Act",
                "Assumes actual days between dates and actual days in a year");
        this.accrueBasisList.add("ACT/ISDA");
        addChoiceList(noChoices, parameter, "String", "Act/ISDA",
                "Assumes actual days between dates and splits days between non-leap and leap year fractions with 366 days in a leap year");
        this.accrueBasisList.add("ACT/365L");
        addChoiceList(noChoices, parameter, "String", "Act/365L",
                "Assumes actual days between dates and assumes 365 or 366 days in a year depending on the whether Feb 29 is in the date range");
        this.accrueBasisList.add("ACT/365");
        addChoiceList(noChoices, parameter, "String", "Act/365",
                "Assumes actual days between dates and always 365 days in a year");
        this.accrueBasisList.add("ACT/364");
        addChoiceList(noChoices, parameter, "String", "Act/364",
                "Assumes actual days between dates and 364 days in year with 364/FQ days in a coupon period where FQ is payments per year");
        this.accrueBasisList.add("BUS/252");
        addChoiceList(noChoices, parameter, "String", "BUS/252",
                "Assumes counting only business days between dates and business days in year set to 252 days");
    }

    public boolean inAccrueBasisList(String accruedBasis) {
        return this.accrueBasisList.contains(accruedBasis);
    }

    public void setCompoundFreqList() {
        int i = 0;
        String parameter = "Compound Frequency";
        this.compoundFreqList.add(i, "ANNUAL"); // annual
        addChoiceList(noChoices, parameter, "String", "ANNUAL", "Compounded on a yearly basis");
        this.compoundFreqList.add(++i, "SEMI"); // semi annual
        addChoiceList(noChoices, parameter, "String", "SEMI-ANNUAL", "Compounded on a semi-annual basis");
        this.compoundFreqList.add(++i, "TRI"); // tri annaul
        addChoiceList(noChoices, parameter, "String", "TRI", "Compounded on a tri-annual basis");
        this.compoundFreqList.add(++i, "QUARTERLY"); // quarterly
        addChoiceList(noChoices, parameter, "String", "QUARTERLY", "Compounded on a quarterly basis");
        this.compoundFreqList.add(++i, "BIMONTHLY"); // bi monthly
        addChoiceList(noChoices, parameter, "String", "BIMONTHLY", "Compounded on a bi-monthly basis");
        this.compoundFreqList.add(++i, "MONTHLY"); // monthly
        addChoiceList(noChoices, parameter, "String", "MONTHLY", "Compounded on a monthly basis");
    }

    public void setFutureCalcList() {
        int i = 0; // need dtFutureValue, need disc curve at dtFuture value
        this.futureCalcList.add(i++, CalcList.FUTUREVALUERISK);
        this.futureCalcList.add(i++, CalcList.FUTUREVALUE);
        this.futureCalcList.add(i++, CalcList.FUTRETURNRISK);
        this.futureCalcList.add(i++, CalcList.HORIZONANALYSIS);
        this.futureCalcList.add(i++, CalcList.ATTRIBUTION);
        this.futureCalcList.add(i++, CalcList.SHARPRATIO);
    }

    boolean inFutureCalcList(int calcNo) {
        return futureCalcList.contains(calcNo);
    }

    public void setCurrencyList() {
        int i = 0;
        this.currencyList.add(i++, "AED");
        this.currencyList.add(i++, "AFN");
        this.currencyList.add(i++, "ALL");
        this.currencyList.add(i++, "AMD");
        this.currencyList.add(i++, "ANG");
        this.currencyList.add(i++, "AOA");
        this.currencyList.add(i++, "ARS");
        this.currencyList.add(i++, "AUD");
        this.currencyList.add(i++, "AWG");
        this.currencyList.add(i++, "AZN");
        this.currencyList.add(i++, "BAM");
        this.currencyList.add(i++, "BBD");
        this.currencyList.add(i++, "BDT");
        this.currencyList.add(i++, "BGN");
        this.currencyList.add(i++, "BHD");
        this.currencyList.add(i++, "BIF");
        this.currencyList.add(i++, "BMD");
        this.currencyList.add(i++, "BND");
        this.currencyList.add(i++, "BOB");
        this.currencyList.add(i++, "BOV");
        this.currencyList.add(i++, "BRL");
        this.currencyList.add(i++, "BSD");
        this.currencyList.add(i++, "BTN");
        this.currencyList.add(i++, "BWP");
        this.currencyList.add(i++, "BYN");
        this.currencyList.add(i++, "BZD");
        this.currencyList.add(i++, "CAD");
        this.currencyList.add(i++, "CDF");
        this.currencyList.add(i++, "CHE");
        this.currencyList.add(i++, "CHF");
        this.currencyList.add(i++, "CHW");
        this.currencyList.add(i++, "CLF");
        this.currencyList.add(i++, "CLP");
        this.currencyList.add(i++, "CNY");
        this.currencyList.add(i++, "COP");
        this.currencyList.add(i++, "COU");
        this.currencyList.add(i++, "CRC");
        this.currencyList.add(i++, "CUP");
        this.currencyList.add(i++, "CVE");
        this.currencyList.add(i++, "CZK");
        this.currencyList.add(i++, "DJF");
        this.currencyList.add(i++, "DKK");
        this.currencyList.add(i++, "DOP");
        this.currencyList.add(i++, "DZD");
        this.currencyList.add(i++, "EGP");
        this.currencyList.add(i++, "ERN");
        this.currencyList.add(i++, "ETB");
        this.currencyList.add(i++, "EUR");
        this.currencyList.add(i++, "FJD");
        this.currencyList.add(i++, "FKP");
        this.currencyList.add(i++, "GBP");
        this.currencyList.add(i++, "GEL");
        this.currencyList.add(i++, "GHS");
        this.currencyList.add(i++, "GIP");
        this.currencyList.add(i++, "GMD");
        this.currencyList.add(i++, "GNF");
        this.currencyList.add(i++, "GTQ");
        this.currencyList.add(i++, "GYD");
        this.currencyList.add(i++, "HKD");
        this.currencyList.add(i++, "HNL");
        this.currencyList.add(i++, "HTG");
        this.currencyList.add(i++, "HUF");
        this.currencyList.add(i++, "IDR");
        this.currencyList.add(i++, "ILS");
        this.currencyList.add(i++, "INR");
        this.currencyList.add(i++, "IQD");
        this.currencyList.add(i++, "IRR");
        this.currencyList.add(i++, "ISK");
        this.currencyList.add(i++, "JMD");
        this.currencyList.add(i++, "JOD");
        this.currencyList.add(i++, "JPY");
        this.currencyList.add(i++, "KES");
        this.currencyList.add(i++, "KGS");
        this.currencyList.add(i++, "KHR");
        this.currencyList.add(i++, "KMF");
        this.currencyList.add(i++, "KPW");
        this.currencyList.add(i++, "KRW");
        this.currencyList.add(i++, "KWD");
        this.currencyList.add(i++, "KYD");
        this.currencyList.add(i++, "KZT");
        this.currencyList.add(i++, "LAK");
        this.currencyList.add(i++, "LBP");
        this.currencyList.add(i++, "LKR");
        this.currencyList.add(i++, "LRD");
        this.currencyList.add(i++, "LSL");
        this.currencyList.add(i++, "LYD");
        this.currencyList.add(i++, "MAD");
        this.currencyList.add(i++, "MDL");
        this.currencyList.add(i++, "MGA");
        this.currencyList.add(i++, "MKD");
        this.currencyList.add(i++, "MMK");
        this.currencyList.add(i++, "MNT");
        this.currencyList.add(i++, "MOP");
        this.currencyList.add(i++, "MRU");
        this.currencyList.add(i++, "MUR");
        this.currencyList.add(i++, "MVR");
        this.currencyList.add(i++, "MWK");
        this.currencyList.add(i++, "MXN");
        this.currencyList.add(i++, "MXV");
        this.currencyList.add(i++, "MYR");
        this.currencyList.add(i++, "MZN");
        this.currencyList.add(i++, "NAD");
        this.currencyList.add(i++, "NGN");
        this.currencyList.add(i++, "NIO");
        this.currencyList.add(i++, "NOK");
        this.currencyList.add(i++, "NPR");
        this.currencyList.add(i++, "NZD");
        this.currencyList.add(i++, "OMR");
        this.currencyList.add(i++, "PAB");
        this.currencyList.add(i++, "PEN");
        this.currencyList.add(i++, "PGK");
        this.currencyList.add(i++, "PHP");
        this.currencyList.add(i++, "PKR");
        this.currencyList.add(i++, "PLN");
        this.currencyList.add(i++, "PYG");
        this.currencyList.add(i++, "QAR");
        this.currencyList.add(i++, "RON");
        this.currencyList.add(i++, "RSD");
        this.currencyList.add(i++, "RUB");
        this.currencyList.add(i++, "RWF");
        this.currencyList.add(i++, "SAR");
        this.currencyList.add(i++, "SBD");
        this.currencyList.add(i++, "SCR");
        this.currencyList.add(i++, "SDG");
        this.currencyList.add(i++, "SEK");
        this.currencyList.add(i++, "SGD");
        this.currencyList.add(i++, "SHP");
        this.currencyList.add(i++, "SLE");
        this.currencyList.add(i++, "SLL");
        this.currencyList.add(i++, "SOS");
        this.currencyList.add(i++, "SRD");
        this.currencyList.add(i++, "SSP");
        this.currencyList.add(i++, "STN");
        this.currencyList.add(i++, "SVC");
        this.currencyList.add(i++, "SYP");
        this.currencyList.add(i++, "SZL");
        this.currencyList.add(i++, "THB");
        this.currencyList.add(i++, "TJS");
        this.currencyList.add(i++, "TMT");
        this.currencyList.add(i++, "TND");
        this.currencyList.add(i++, "TOP");
        this.currencyList.add(i++, "TRY");
        this.currencyList.add(i++, "TTD");
        this.currencyList.add(i++, "TWD");
        this.currencyList.add(i++, "TZS");
        this.currencyList.add(i++, "UAH");
        this.currencyList.add(i++, "UGX");
        this.currencyList.add(i++, "USD");
        this.currencyList.add(i++, "USN");
        this.currencyList.add(i++, "UYI");
        this.currencyList.add(i++, "UYU");
        this.currencyList.add(i++, "UYW");
        this.currencyList.add(i++, "UZS");
        this.currencyList.add(i++, "VED");
        this.currencyList.add(i++, "VES");
        this.currencyList.add(i++, "VND");
        this.currencyList.add(i++, "VUV");
        this.currencyList.add(i++, "WST");
        this.currencyList.add(i++, "XAF");
        this.currencyList.add(i++, "XAG");
        this.currencyList.add(i++, "XAU");
        this.currencyList.add(i++, "XBA");
        this.currencyList.add(i++, "XBB");
        this.currencyList.add(i++, "XBC");
        this.currencyList.add(i++, "XBD");
        this.currencyList.add(i++, "XCD");
        this.currencyList.add(i++, "XDR");
        this.currencyList.add(i++, "XOF");
        this.currencyList.add(i++, "XPD");
        this.currencyList.add(i++, "XPF");
        this.currencyList.add(i++, "XPT");
        this.currencyList.add(i++, "XSU");
        this.currencyList.add(i++, "XTS");
        this.currencyList.add(i++, "XUA");
        this.currencyList.add(i++, "XXX");
        this.currencyList.add(i++, "YER");
        this.currencyList.add(i++, "YER ADEN");
        this.currencyList.add(i++, "ZAR");
        this.currencyList.add(i++, "ZMW");
        this.currencyList.add(i++, "ZWL");
    }

    public boolean inCurrencyList(String currency) {
        return currencyList.contains(currency);
    }

    public boolean inCompoundFreqList(String compoundFreqValue) {
        return compoundFreqList.contains(compoundFreqValue);
    }

    public void setBondEqvList() {
        int i = 0;
        String parameter = "BondEqvYld";
        this.bondEqvList.add(i, "ANNUAL");
        addChoiceList(noChoices, parameter, "String", "Annual", "Convert to a compound annual yield");
        this.bondEqvList.add(++i, "SEMI");
        addChoiceList(noChoices, parameter, "String", "Semi", "Convert to semi-annual compound yield ");
        this.bondEqvList.add(++i, "QUARTERLY");
        addChoiceList(noChoices, parameter, "String", "Quarterly", "Convert to a quarterly compounded yield");
        this.bondEqvList.add(++i, "MONTHLY");
        addChoiceList(noChoices, parameter, "String", "Monthly", "Convert to monthly compound yield");
    }

    boolean inBondEqvList(String bondEqv) {
        return bondEqvList.contains(bondEqv);
    }

    public void setBookCalcList() {
        String parameter = "BookCalcMeth";
        this.bookCalcList.add(0, "COST");
        addChoiceList(noChoices, parameter, "String", "Cost", "Maintain book value at cost");
        this.bookCalcList.add(1, "STLINE");
        addChoiceList(noChoices, parameter, "String", "SLine", "Perform straight line accretion/amrtization");
        this.bookCalcList.add(2, "CONSTYLD");
        addChoiceList(noChoices, parameter, "String", "ConstYld",
                "Perform pure constant yield or scientific accretion/amortization");
        this.bookCalcList.add(3, "CONSTYLD+");
        addChoiceList(noChoices, parameter, "String", "ConstYld+",
                "Perform constant yield or scientific accretion/amortization at interest payment date and prorate between those dates");
        this.bookCalcList.add(4, "EFFINCOME");
        addChoiceList(noChoices, parameter, "String", "EffIncome",
                "Perform effective income accretion/amortization using the total income method");
        this.bookCalcList.add(5, "EFFINCOME+");
        addChoiceList(noChoices, parameter, "String", "EffIncome+",
                "Perform effective income accretion/amortization using the total income method at interest payment dates and prorate between thos dates");
        this.bookCalcList.add(6, "PROSPECTYLD");
        addChoiceList(noChoices, parameter, "String", "ProspectYld",
                "Perform prospective accretion/amortization using updated maarket yield");
    }

    boolean inBookCalcList(String bookCalcOp) {
        return bookCalcList.contains(bookCalcOp);
    }

    public void setYieldTypeList() {
        String parameter = "YieldType";
        this.yieldTypeList.add(0, "RF");
        addChoiceList(noChoices, parameter, "String", "RF", "Risk free discount curve");
        this.yieldTypeList.add(1, "SPRD");
        addChoiceList(noChoices, parameter, "String", "SPRD", "Credit spread discount curve");
        this.yieldTypeList.add(2, "NET"); // curve already combinedingle
        addChoiceList(noChoices, parameter, "String", "NET", "Already combined risk free and credit spread curve");
        this.yieldTypeList.add(2, "COM"); // curve already combinedingle
        addChoiceList(noChoices, parameter, "String", "COM",
                "Combine both stored risk plus credit spread curve to make one net curve");
        this.yieldTypeList.add(3, "FIXING");
        addChoiceList(noChoices, parameter, "String", "FIXING", "Fixing curve for floating rates");

    }

    public boolean inYieldTypeList(String yieldType) {
        return yieldTypeList.contains(yieldType);
    }

    public void setAccrualAccountingList() {
        this.accrualAccountingList.add(0, CalcList.BOOKVALUE);
        this.accrualAccountingList.add(1, CalcList.ACCREAMORT);
        this.accrualAccountingList.add(2, CalcList.ACCREAMORTSCHED);
    }

    public boolean inAccrualAccountingList(Integer calcNo) {
        boolean isIn = accrualAccountingList.contains(calcNo);
        return isIn;
    }

    public void setIntPayList() {
        String parameter = "Interest Payment Method";
        this.intPayList.add("CONSTCASH");
        addChoiceList(noChoices, parameter, "String", "CONSTCASH",
                "Ineterest payments are the same on all coupon payment dates");
        this.intPayList.add("CONSTACCRUAL");
        addChoiceList(noChoices, parameter, "String", "CONSTACCRUAL",
                "Interest payments vary depending on the number of days between interest pay dates");
    }

    public boolean inIntPayList(String intPayMeth) {
        boolean isin = intPayList.contains(intPayMeth);
        return isin;
    }

    public void setDocumentList() {
        this.documentList.add(0, 15); // version list
        this.documentList.add(1, 16); // parameter list
        this.documentList.add(2, 17); // calculation list
        this.documentList.add(3, 25); // choices list
        this.documentList.add(4, 41); // Bond parameters
    }

    public boolean inDocumentList(ArrayList<Integer> calculationList) {
        int i = -1, no = 0, calcNo = 0;
        int noList = calculationList.size();
        while (++i < noList) {
            calcNo = calculationList.get(i);
            if (documentList.contains(calcNo))
                ++no;
        }
        if (no == noList)
            return true;
        return false;
    }

    public void setNoSecurityCalcList() {
        int i = 0;
        this.noSecurityCalcList.add(i++, CalcList.VERSION);
        this.noSecurityCalcList.add(i++, CalcList.PARAMETERS);
        this.noSecurityCalcList.add(i++, CalcList.CALCULATIONS);
        this.noSecurityCalcList.add(i++, CalcList.CHOICES);
        this.noSecurityCalcList.add(i++, CalcList.DAYCOUNT);
        this.noSecurityCalcList.add(i++, CalcList.FORWARDCURVE);
        this.noSecurityCalcList.add(i++, CalcList.MATLENYEARS);
        this.noSecurityCalcList.add(i++, CalcList.MATlENDAYS);
        this.noSecurityCalcList.add(i++, CalcList.SPOTCURVE);
        this.noSecurityCalcList.add(i++, CalcList.BONDPARAMS);

    }

    public boolean inNoSecurityList(int calcNo) {
        return noSecurityCalcList.contains(calcNo);
    }

    public void setConversionOptionList() {
        int i = 0;
        String parameter = "ConvertOpt";
        this.conversionOptionList.add(i, "ALWAYS");
        addChoiceList(noChoices, parameter, "String", "Always",
                "Convert to equity always when there is a conversion option");
        this.conversionOptionList.add(++i, "GAIN");
        addChoiceList(noChoices, parameter, "String", "Gain",
                "Convert to equity only if market value of the equity exceeds that of the bond on conversion dates");
        this.conversionOptionList.add(++i, "NEVER");
        addChoiceList(noChoices, parameter, "String", "Never", "Never convert to equity");
    }

    boolean inConversionOptionList(String convertOption) {
        return conversionOptionList.contains(convertOption);
    }

    public void setDayOfWeekList() {
        String parameter = "Days of the week";
        this.dayOfWeekList.add(0, "SUN");
        addChoiceList(noChoices, parameter, "String", "SUN", "Sunday - the start of the week");
        this.dayOfWeekList.add(1, "MON");
        addChoiceList(noChoices, parameter, "String", "MON", "Monday - 2st day of the week");
        this.dayOfWeekList.add(2, "TUE");
        addChoiceList(noChoices, parameter, "String", "TUE", "Tuesday - 3nd day of the week");
        this.dayOfWeekList.add(3, "WED");
        addChoiceList(noChoices, parameter, "String", "WED", "Wednesdat - 4th day of the week");
        this.dayOfWeekList.add(4, "THU");
        addChoiceList(noChoices, parameter, "String", "THU", "Thursday - 5th of the week");
        this.dayOfWeekList.add(5, "FRI");
        addChoiceList(noChoices, parameter, "String", "FRI", "Friday - 6th dayof the week");
        this.dayOfWeekList.add(6, "SAT");
        addChoiceList(noChoices, parameter, "String", "SAT", "Saturday - 7th day of the week");
    }

    public boolean inDayOfWeekList(String dayOfWeek) {
        return dayOfWeekList.contains(dayOfWeek);
    }

    public void setPassModelList() {
        String parameter = "PassModel";
        this.passModelList.add(0, "ABS");
        addChoiceList(noChoices, parameter, "String", "ABS",
                "Absolute prepay model with fixed percent of original amount payoff per month");
        this.passModelList.add(1, "CPR");
        addChoiceList(noChoices, parameter, "String", "CPR",
                "Constant prepayment model with fixed percent of remaining balance prepaid per month");
        this.passModelList.add(2, "PSA");
        addChoiceList(noChoices, parameter, "String", "PSA",
                "Public Securities Associated prepayment model with accelerating payments for first 30 months then fixed percent prepaid of remainingg balance");
        this.passModelList.add(3, "SMM");
        addChoiceList(noChoices, parameter, "String", "SMM",
                "A user specified model where the user supplies a sequence of prepayment rates at arbitrary times");

    }

    public boolean inPassModelList(String passModel) {
        return passModelList.contains(passModel);
    }

    public void setCompatableCalcList(ArrayList compatableCalcList) {
        compatableCalcList.add(0, 2); // yieldPrice
        compatableCalcList.add(1, 3); // price yield
        compatableCalcList.add(2, 4); // Mac duration
        compatableCalcList.add(3, 5); // mod duration SI
        compatableCalcList.add(4, 6);
        compatableCalcList.add(5, 8);// mod duration STD
    }

    public boolean inCompatableCalcList(int calNo) {
        return compatableCalcList.contains(calNo);
    }

    public void setStripTypeList() {
        String parameter = "Strip Type";
        this.stripTypeList.add(0, "NS");
        addChoiceList(noChoices, parameter, "String", "NS", "Not a strip and has coupon and principial payment(s)");
        this.stripTypeList.add(1, "IO");
        addChoiceList(noChoices, parameter, "String", "IO", "Only pays interest no principial payment");
        this.stripTypeList.add(2, "PO");
        addChoiceList(noChoices, parameter, "String", "PO", "Only pays principal no ineterest payments");

    }

    public boolean inStripTypeList(String stripType) {
        return stripTypeList.contains(stripType);
    }

    public void setCountryList() {
        String parameter = "Country";
        this.countryList.add("AFG");
        this.countryList.add("ALB");
        this.countryList.add("DZA");
        this.countryList.add("ASM");
        this.countryList.add("AND");
        this.countryList.add("AGO");
        this.countryList.add("AIA");
        this.countryList.add("ATA");
        this.countryList.add("ATG");
        this.countryList.add("ARG");
        this.countryList.add("ARM");
        this.countryList.add("ABW");
        this.countryList.add("AUS");
        this.countryList.add("AUT");
        this.countryList.add("AZE");
        this.countryList.add("BHS");
        this.countryList.add("BHR");
        this.countryList.add("BGD");
        this.countryList.add("BRB");
        this.countryList.add("BLR");
        this.countryList.add("BEL");
        this.countryList.add("BLZ");
        this.countryList.add("BEN");
        this.countryList.add("BMU");
        this.countryList.add("BTN");
        this.countryList.add("BOL");
        this.countryList.add("BES");
        this.countryList.add("BIH");
        this.countryList.add("BWA");
        this.countryList.add("BVT");
        this.countryList.add("BRA");
        this.countryList.add("IOT");
        this.countryList.add("BRN");
        this.countryList.add("BGR");
        this.countryList.add("BFA");
        this.countryList.add("BDI");
        this.countryList.add("CPV");
        this.countryList.add("KHM");
        this.countryList.add("CMR");
        this.countryList.add("CAN");
        this.countryList.add("CYM");
        this.countryList.add("CAF");
        this.countryList.add("TCD");
        this.countryList.add("CHL");
        this.countryList.add("CHN");
        this.countryList.add("CXR");
        this.countryList.add("CCK");
        this.countryList.add("COL");
        this.countryList.add("COM");
        this.countryList.add("COD");
        this.countryList.add("COG");
        this.countryList.add("COK");
        this.countryList.add("CRI");
        this.countryList.add("HRV");
        this.countryList.add("CUB");
        this.countryList.add("CUW");
        this.countryList.add("CYP");
        this.countryList.add("CZE");
        this.countryList.add("CIV");
        this.countryList.add("DNK");
        this.countryList.add("DJI");
        this.countryList.add("DMA");
        this.countryList.add("DOM");
        this.countryList.add("ECU");
        this.countryList.add("EGY");
        this.countryList.add("SLV");
        this.countryList.add("GNQ");
        this.countryList.add("ERI");
        this.countryList.add("EST");
        this.countryList.add("SWZ");
        this.countryList.add("ETH");
        this.countryList.add("FLK");
        this.countryList.add("FRO");
        this.countryList.add("FJI");
        this.countryList.add("FIN");
        this.countryList.add("FRA");
        this.countryList.add("GUF");
        this.countryList.add("PYF");
        this.countryList.add("ATF");
        this.countryList.add("GAB");
        this.countryList.add("GMB");
        this.countryList.add("GEO");
        this.countryList.add("DEU");
        this.countryList.add("GHA");
        this.countryList.add("GIB");
        this.countryList.add("GRC");
        this.countryList.add("GRL");
        this.countryList.add("GRD");
        this.countryList.add("GLP");
        this.countryList.add("GUM");
        this.countryList.add("GTM");
        this.countryList.add("GGY");
        this.countryList.add("GIN");
        this.countryList.add("GNB");
        this.countryList.add("GUY");
        this.countryList.add("HTI");
        this.countryList.add("HMD");
        this.countryList.add("VAT");
        this.countryList.add("HND");
        this.countryList.add("HKG");
        this.countryList.add("HUN");
        this.countryList.add("ISL");
        this.countryList.add("IND");
        this.countryList.add("IDN");
        this.countryList.add("IRN");
        this.countryList.add("IRQ");
        this.countryList.add("IRL");
        this.countryList.add("IMN");
        this.countryList.add("ISR");
        this.countryList.add("ITA");
        this.countryList.add("JAM");
        this.countryList.add("JPN");
        this.countryList.add("JEY");
        this.countryList.add("JOR");
        this.countryList.add("KAZ");
        this.countryList.add("KEN");
        this.countryList.add("KIR");
        this.countryList.add("PRK");
        this.countryList.add("KOR");
        this.countryList.add("KWT");
        this.countryList.add("KGZ");
        this.countryList.add("LAO");
        this.countryList.add("LVA");
        this.countryList.add("LBN");
        this.countryList.add("LSO");
        this.countryList.add("LBR");
        this.countryList.add("LBY");
        this.countryList.add("LIE");
        this.countryList.add("LTU");
        this.countryList.add("LUX");
        this.countryList.add("MAC");
        this.countryList.add("MDG");
        this.countryList.add("MWI");
        this.countryList.add("MYS");
        this.countryList.add("MDV");
        this.countryList.add("MLI");
        this.countryList.add("MLT");
        this.countryList.add("MHL");
        this.countryList.add("MTQ");
        this.countryList.add("MRT");
        this.countryList.add("MUS");
        this.countryList.add("MYT");
        this.countryList.add("MEX");
        this.countryList.add("FSM");
        this.countryList.add("MDA");
        this.countryList.add("MCO");
        this.countryList.add("MNG");
        this.countryList.add("MNE");
        this.countryList.add("MSR");
        this.countryList.add("MAR");
        this.countryList.add("MOZ");
        this.countryList.add("MMR");
        this.countryList.add("NAM");
        this.countryList.add("NRU");
        this.countryList.add("NPL");
        this.countryList.add("NLD");
        this.countryList.add("NCL");
        this.countryList.add("NZL");
        this.countryList.add("NIC");
        this.countryList.add("NER");
        this.countryList.add("NGA");
        this.countryList.add("NIU");
        this.countryList.add("NFK");
        this.countryList.add("MNP");
        this.countryList.add("NOR");
        this.countryList.add("OMN");
        this.countryList.add("PAK");
        this.countryList.add("PLW");
        this.countryList.add("PSE");
        this.countryList.add("PAN");
        this.countryList.add("PNG");
        this.countryList.add("PRY");
        this.countryList.add("PER");
        this.countryList.add("PHL");
        this.countryList.add("PCN");
        this.countryList.add("POL");
        this.countryList.add("PRT");
        this.countryList.add("PRI");
        this.countryList.add("QAT");
        this.countryList.add("MKD");
        this.countryList.add("ROU");
        this.countryList.add("RUS");
        this.countryList.add("RWA");
        this.countryList.add("REU");
        this.countryList.add("BLM");
        this.countryList.add("SHN");
        this.countryList.add("KNA");
        this.countryList.add("LCA");
        this.countryList.add("MAF");
        this.countryList.add("SPM");
        this.countryList.add("VCT");
        this.countryList.add("WSM");
        this.countryList.add("SMR");
        this.countryList.add("STP");
        this.countryList.add("SAU");
        this.countryList.add("SEN");
        this.countryList.add("SRB");
        this.countryList.add("SYC");
        this.countryList.add("SLE");
        this.countryList.add("SGP");
        this.countryList.add("SXM");
        this.countryList.add("SVK");
        this.countryList.add("SVN");
        this.countryList.add("SLB");
        this.countryList.add("SOM");
        this.countryList.add("ZAF");
        this.countryList.add("SGS");
        this.countryList.add("SSD");
        this.countryList.add("ESP");
        this.countryList.add("LKA");
        this.countryList.add("SDN");
        this.countryList.add("SUR");
        this.countryList.add("SJM");
        this.countryList.add("SWE");
        this.countryList.add("CHE");
        this.countryList.add("SYR");
        this.countryList.add("TWN");
        this.countryList.add("TJK");
        this.countryList.add("TZA");
        this.countryList.add("THA");
        this.countryList.add("TLS");
        this.countryList.add("TGO");
        this.countryList.add("TKL");
        this.countryList.add("TON");
        this.countryList.add("TTO");
        this.countryList.add("TUN");
        this.countryList.add("TUR");
        this.countryList.add("TKM");
        this.countryList.add("TCA");
        this.countryList.add("TUV");
        this.countryList.add("UGA");
        this.countryList.add("UKR");
        this.countryList.add("ARE");
        this.countryList.add("GBR");
        this.countryList.add("UMI");
        this.countryList.add("USA");
        this.countryList.add("URY");
        this.countryList.add("UZB");
        this.countryList.add("VUT");
        this.countryList.add("VEN");
        this.countryList.add("VNM");
        this.countryList.add("VGB");
        this.countryList.add("VIR");
        this.countryList.add("WLF");
        this.countryList.add("ESH");
        this.countryList.add("YEM");
        this.countryList.add("ZMB");
        this.countryList.add("ZWE");
        this.countryList.add("ALA");
        addChoiceList(noChoices, parameter, "String", "AFG", "Country of Afghanistan");
        addChoiceList(noChoices, parameter, "String", "ALB", "Country of Albania");
        addChoiceList(noChoices, parameter, "String", "DZA", "Country of Algeria");
        addChoiceList(noChoices, parameter, "String", "ASM", "Country of American Samoa");
        addChoiceList(noChoices, parameter, "String", "AND", "Country of Andorra");
        addChoiceList(noChoices, parameter, "String", "AGO", "Country of Angola");
        addChoiceList(noChoices, parameter, "String", "AIA", "Country of Anguilla");
        addChoiceList(noChoices, parameter, "String", "ATA", "Country of Antarctica");
        addChoiceList(noChoices, parameter, "String", "ATG", "Country of Antigua and Barbuda");
        addChoiceList(noChoices, parameter, "String", "ARG", "Country of Argentina");
        addChoiceList(noChoices, parameter, "String", "ARM", "Country of Armenia");
        addChoiceList(noChoices, parameter, "String", "ABW", "Country of Aruba");
        addChoiceList(noChoices, parameter, "String", "AUS", "Country of Australia");
        addChoiceList(noChoices, parameter, "String", "AUT", "Country of Austria");
        addChoiceList(noChoices, parameter, "String", "AZE", "Country of Azerbaijan");
        addChoiceList(noChoices, parameter, "String", "BHS", "Country of Bahamas (the)");
        addChoiceList(noChoices, parameter, "String", "BHR", "Country of Bahrain");
        addChoiceList(noChoices, parameter, "String", "BGD", "Country of Bangladesh");
        addChoiceList(noChoices, parameter, "String", "BRB", "Country of Barbados");
        addChoiceList(noChoices, parameter, "String", "BLR", "Country of Belarus");
        addChoiceList(noChoices, parameter, "String", "BEL", "Country of Belgium");
        addChoiceList(noChoices, parameter, "String", "BLZ", "Country of Belize");
        addChoiceList(noChoices, parameter, "String", "BEN", "Country of Benin");
        addChoiceList(noChoices, parameter, "String", "BMU", "Country of Bermuda");
        addChoiceList(noChoices, parameter, "String", "BTN", "Country of Bhutan");
        addChoiceList(noChoices, parameter, "String", "BOL", "Country of Bolivia (Plurinational State of)");
        addChoiceList(noChoices, parameter, "String", "BES", "Country of Bonaire, Sint Eustatius and Saba");
        addChoiceList(noChoices, parameter, "String", "BIH", "Country of Bosnia and Herzegovina");
        addChoiceList(noChoices, parameter, "String", "BWA", "Country of Botswana");
        addChoiceList(noChoices, parameter, "String", "BVT", "Country of Bouvet Island");
        addChoiceList(noChoices, parameter, "String", "BRA", "Country of Brazil");
        addChoiceList(noChoices, parameter, "String", "IOT", "Country of British Indian Ocean Territory (the)");
        addChoiceList(noChoices, parameter, "String", "BRN", "Country of Brunei Darussalam");
        addChoiceList(noChoices, parameter, "String", "BGR", "Country of Bulgaria");
        addChoiceList(noChoices, parameter, "String", "BFA", "Country of Burkina Faso");
        addChoiceList(noChoices, parameter, "String", "BDI", "Country of Burundi");
        addChoiceList(noChoices, parameter, "String", "CPV", "Country of Cabo Verde");
        addChoiceList(noChoices, parameter, "String", "KHM", "Country of Cambodia");
        addChoiceList(noChoices, parameter, "String", "CMR", "Country of Cameroon");
        addChoiceList(noChoices, parameter, "String", "CAN", "Country of Canada");
        addChoiceList(noChoices, parameter, "String", "CYM", "Country of Cayman Islands (the)");
        addChoiceList(noChoices, parameter, "String", "CAF", "Country of Central African Republic (the)");
        addChoiceList(noChoices, parameter, "String", "TCD", "Country of Chad");
        addChoiceList(noChoices, parameter, "String", "CHL", "Country of Chile");
        addChoiceList(noChoices, parameter, "String", "CHN", "Country of China");
        addChoiceList(noChoices, parameter, "String", "CXR", "Country of Christmas Island");
        addChoiceList(noChoices, parameter, "String", "CCK", "Country of Cocos (Keeling) Islands (the)");
        addChoiceList(noChoices, parameter, "String", "COL", "Country of Colombia");
        addChoiceList(noChoices, parameter, "String", "COM", "Country of Comoros (the)");
        addChoiceList(noChoices, parameter, "String", "COD", "Country of Congo (the Democratic Republic of the)");
        addChoiceList(noChoices, parameter, "String", "COG", "Country of Congo (the)");
        addChoiceList(noChoices, parameter, "String", "COK", "Country of Cook Islands (the)");
        addChoiceList(noChoices, parameter, "String", "CRI", "Country of Costa Rica");
        addChoiceList(noChoices, parameter, "String", "HRV", "Country of Croatia");
        addChoiceList(noChoices, parameter, "String", "CUB", "Country of Cuba");
        addChoiceList(noChoices, parameter, "String", "CUW", "Country of Curaçao");
        addChoiceList(noChoices, parameter, "String", "CYP", "Country of Cyprus");
        addChoiceList(noChoices, parameter, "String", "CZE", "Country of Czechia");
        addChoiceList(noChoices, parameter, "String", "CIV", "Country of Côte d'Ivoire");
        addChoiceList(noChoices, parameter, "String", "DNK", "Country of Denmark");
        addChoiceList(noChoices, parameter, "String", "DJI", "Country of Djibouti");
        addChoiceList(noChoices, parameter, "String", "DMA", "Country of Dominica");
        addChoiceList(noChoices, parameter, "String", "DOM", "Country of Dominican Republic (the)");
        addChoiceList(noChoices, parameter, "String", "ECU", "Country of Ecuador");
        addChoiceList(noChoices, parameter, "String", "EGY", "Country of Egypt");
        addChoiceList(noChoices, parameter, "String", "SLV", "Country of El Salvador");
        addChoiceList(noChoices, parameter, "String", "GNQ", "Country of Equatorial Guinea");
        addChoiceList(noChoices, parameter, "String", "ERI", "Country of Eritrea");
        addChoiceList(noChoices, parameter, "String", "EST", "Country of Estonia");
        addChoiceList(noChoices, parameter, "String", "SWZ", "Country of Eswatini");
        addChoiceList(noChoices, parameter, "String", "ETH", "Country of Ethiopia");
        addChoiceList(noChoices, parameter, "String", "FLK", "Country of Falkland Islands (the) [Malvinas]");
        addChoiceList(noChoices, parameter, "String", "FRO", "Country of Faroe Islands (the)");
        addChoiceList(noChoices, parameter, "String", "FJI", "Country of Fiji");
        addChoiceList(noChoices, parameter, "String", "FIN", "Country of Finland");
        addChoiceList(noChoices, parameter, "String", "FRA", "Country of France");
        addChoiceList(noChoices, parameter, "String", "GUF", "Country of French Guiana");
        addChoiceList(noChoices, parameter, "String", "PYF", "Country of French Polynesia");
        addChoiceList(noChoices, parameter, "String", "ATF", "Country of French Southern Territories (the)");
        addChoiceList(noChoices, parameter, "String", "GAB", "Country of Gabon");
        addChoiceList(noChoices, parameter, "String", "GMB", "Country of Gambia (the)");
        addChoiceList(noChoices, parameter, "String", "GEO", "Country of Georgia");
        addChoiceList(noChoices, parameter, "String", "DEU", "Country of Germany");
        addChoiceList(noChoices, parameter, "String", "GHA", "Country of Ghana");
        addChoiceList(noChoices, parameter, "String", "GIB", "Country of Gibraltar");
        addChoiceList(noChoices, parameter, "String", "GRC", "Country of Greece");
        addChoiceList(noChoices, parameter, "String", "GRL", "Country of Greenland");
        addChoiceList(noChoices, parameter, "String", "GRD", "Country of Grenada");
        addChoiceList(noChoices, parameter, "String", "GLP", "Country of Guadeloupe");
        addChoiceList(noChoices, parameter, "String", "GUM", "Country of Guam");
        addChoiceList(noChoices, parameter, "String", "GTM", "Country of Guatemala");
        addChoiceList(noChoices, parameter, "String", "GGY", "Country of Guernsey");
        addChoiceList(noChoices, parameter, "String", "GIN", "Country of Guinea");
        addChoiceList(noChoices, parameter, "String", "GNB", "Country of Guinea-Bissau");
        addChoiceList(noChoices, parameter, "String", "GUY", "Country of Guyana");
        addChoiceList(noChoices, parameter, "String", "HTI", "Country of Haiti");
        addChoiceList(noChoices, parameter, "String", "HMD", "Country of Heard Island and McDonald Islands");
        addChoiceList(noChoices, parameter, "String", "VAT", "Country of Holy See (the)");
        addChoiceList(noChoices, parameter, "String", "HND", "Country of Honduras");
        addChoiceList(noChoices, parameter, "String", "HKG", "Country of Hong Kong");
        addChoiceList(noChoices, parameter, "String", "HUN", "Country of Hungary");
        addChoiceList(noChoices, parameter, "String", "ISL", "Country of Iceland");
        addChoiceList(noChoices, parameter, "String", "IND", "Country of India");
        addChoiceList(noChoices, parameter, "String", "IDN", "Country of Indonesia");
        addChoiceList(noChoices, parameter, "String", "IRN", "Country of Iran (Islamic Republic of)");
        addChoiceList(noChoices, parameter, "String", "IRQ", "Country of Iraq");
        addChoiceList(noChoices, parameter, "String", "IRL", "Country of Ireland");
        addChoiceList(noChoices, parameter, "String", "IMN", "Country of Isle of Man");
        addChoiceList(noChoices, parameter, "String", "ISR", "Country of Israel");
        addChoiceList(noChoices, parameter, "String", "ITA", "Country of Italy");
        addChoiceList(noChoices, parameter, "String", "JAM", "Country of Jamaica");
        addChoiceList(noChoices, parameter, "String", "JPN", "Country of Japan");
        addChoiceList(noChoices, parameter, "String", "JEY", "Country of Jersey");
        addChoiceList(noChoices, parameter, "String", "JOR", "Country of Jordan");
        addChoiceList(noChoices, parameter, "String", "KAZ", "Country of Kazakhstan");
        addChoiceList(noChoices, parameter, "String", "KEN", "Country of Kenya");
        addChoiceList(noChoices, parameter, "String", "KIR", "Country of Kiribati");
        addChoiceList(noChoices, parameter, "String", "PRK", "Country of Korea (the Democratic People's Republic of)");
        addChoiceList(noChoices, parameter, "String", "KOR", "Country of Korea (the Republic of)");
        addChoiceList(noChoices, parameter, "String", "KWT", "Country of Kuwait");
        addChoiceList(noChoices, parameter, "String", "KGZ", "Country of Kyrgyzstan");
        addChoiceList(noChoices, parameter, "String", "LAO", "Country of Lao People's Democratic Republic (the)");
        addChoiceList(noChoices, parameter, "String", "LVA", "Country of Latvia");
        addChoiceList(noChoices, parameter, "String", "LBN", "Country of Lebanon");
        addChoiceList(noChoices, parameter, "String", "LSO", "Country of Lesotho");
        addChoiceList(noChoices, parameter, "String", "LBR", "Country of Liberia");
        addChoiceList(noChoices, parameter, "String", "LBY", "Country of Libya");
        addChoiceList(noChoices, parameter, "String", "LIE", "Country of Liechtenstein");
        addChoiceList(noChoices, parameter, "String", "LTU", "Country of Lithuania");
        addChoiceList(noChoices, parameter, "String", "LUX", "Country of Luxembourg");
        addChoiceList(noChoices, parameter, "String", "MAC", "Country of Macao");
        addChoiceList(noChoices, parameter, "String", "MDG", "Country of Madagascar");
        addChoiceList(noChoices, parameter, "String", "MWI", "Country of Malawi");
        addChoiceList(noChoices, parameter, "String", "MYS", "Country of Malaysia");
        addChoiceList(noChoices, parameter, "String", "MDV", "Country of Maldives");
        addChoiceList(noChoices, parameter, "String", "MLI", "Country of Mali");
        addChoiceList(noChoices, parameter, "String", "MLT", "Country of Malta");
        addChoiceList(noChoices, parameter, "String", "MHL", "Country of Marshall Islands (the)");
        addChoiceList(noChoices, parameter, "String", "MTQ", "Country of Martinique");
        addChoiceList(noChoices, parameter, "String", "MRT", "Country of Mauritania");
        addChoiceList(noChoices, parameter, "String", "MUS", "Country of Mauritius");
        addChoiceList(noChoices, parameter, "String", "MYT", "Country of Mayotte");
        addChoiceList(noChoices, parameter, "String", "MEX", "Country of Mexico");
        addChoiceList(noChoices, parameter, "String", "FSM", "Country of Micronesia (Federated States of)");
        addChoiceList(noChoices, parameter, "String", "MDA", "Country of Moldova (the Republic of)");
        addChoiceList(noChoices, parameter, "String", "MCO", "Country of Monaco");
        addChoiceList(noChoices, parameter, "String", "MNG", "Country of Mongolia");
        addChoiceList(noChoices, parameter, "String", "MNE", "Country of Montenegro");
        addChoiceList(noChoices, parameter, "String", "MSR", "Country of Montserrat");
        addChoiceList(noChoices, parameter, "String", "MAR", "Country of Morocco");
        addChoiceList(noChoices, parameter, "String", "MOZ", "Country of Mozambique");
        addChoiceList(noChoices, parameter, "String", "MMR", "Country of Myanmar");
        addChoiceList(noChoices, parameter, "String", "NAM", "Country of Namibia");
        addChoiceList(noChoices, parameter, "String", "NRU", "Country of Nauru");
        addChoiceList(noChoices, parameter, "String", "NPL", "Country of Nepal");
        addChoiceList(noChoices, parameter, "String", "NLD", "Country of Netherlands (the)");
        addChoiceList(noChoices, parameter, "String", "NCL", "Country of New Caledonia");
        addChoiceList(noChoices, parameter, "String", "NZL", "Country of New Zealand");
        addChoiceList(noChoices, parameter, "String", "NIC", "Country of Nicaragua");
        addChoiceList(noChoices, parameter, "String", "NER", "Country of Niger (the)");
        addChoiceList(noChoices, parameter, "String", "NGA", "Country of Nigeria");
        addChoiceList(noChoices, parameter, "String", "NIU", "Country of Niue");
        addChoiceList(noChoices, parameter, "String", "NFK", "Country of Norfolk Island");
        addChoiceList(noChoices, parameter, "String", "MNP", "Country of Northern Mariana Islands (the)");
        addChoiceList(noChoices, parameter, "String", "NOR", "Country of Norway");
        addChoiceList(noChoices, parameter, "String", "OMN", "Country of Oman");
        addChoiceList(noChoices, parameter, "String", "PAK", "Country of Pakistan");
        addChoiceList(noChoices, parameter, "String", "PLW", "Country of Palau");
        addChoiceList(noChoices, parameter, "String", "PSE", "Country of Palestine, State of");
        addChoiceList(noChoices, parameter, "String", "PAN", "Country of Panama");
        addChoiceList(noChoices, parameter, "String", "PNG", "Country of Papua New Guinea");
        addChoiceList(noChoices, parameter, "String", "PRY", "Country of Paraguay");
        addChoiceList(noChoices, parameter, "String", "PER", "Country of Peru");
        addChoiceList(noChoices, parameter, "String", "PHL", "Country of Philippines (the)");
        addChoiceList(noChoices, parameter, "String", "PCN", "Country of Pitcairn");
        addChoiceList(noChoices, parameter, "String", "POL", "Country of Poland");
        addChoiceList(noChoices, parameter, "String", "PRT", "Country of Portugal");
        addChoiceList(noChoices, parameter, "String", "PRI", "Country of Puerto Rico");
        addChoiceList(noChoices, parameter, "String", "QAT", "Country of Qatar");
        addChoiceList(noChoices, parameter, "String", "MKD", "Country of Republic of North Macedonia");
        addChoiceList(noChoices, parameter, "String", "ROU", "Country of Romania");
        addChoiceList(noChoices, parameter, "String", "RUS", "Country of Russian Federation (the)");
        addChoiceList(noChoices, parameter, "String", "RWA", "Country of Rwanda");
        addChoiceList(noChoices, parameter, "String", "REU", "Country of Réunion");
        addChoiceList(noChoices, parameter, "String", "BLM", "Country of Saint Barthélemy");
        addChoiceList(noChoices, parameter, "String", "SHN", "Country of Saint Helena, Ascension and Tristan da Cunha");
        addChoiceList(noChoices, parameter, "String", "KNA", "Country of Saint Kitts and Nevis");
        addChoiceList(noChoices, parameter, "String", "LCA", "Country of Saint Lucia");
        addChoiceList(noChoices, parameter, "String", "MAF", "Country of Saint Martin (French part)");
        addChoiceList(noChoices, parameter, "String", "SPM", "Country of Saint Pierre and Miquelon");
        addChoiceList(noChoices, parameter, "String", "VCT", "Country of Saint Vincent and the Grenadines");
        addChoiceList(noChoices, parameter, "String", "WSM", "Country of Samoa");
        addChoiceList(noChoices, parameter, "String", "SMR", "Country of San Marino");
        addChoiceList(noChoices, parameter, "String", "STP", "Country of Sao Tome and Principe");
        addChoiceList(noChoices, parameter, "String", "SAU", "Country of Saudi Arabia");
        addChoiceList(noChoices, parameter, "String", "SEN", "Country of Senegal");
        addChoiceList(noChoices, parameter, "String", "SRB", "Country of Serbia");
        addChoiceList(noChoices, parameter, "String", "SYC", "Country of Seychelles");
        addChoiceList(noChoices, parameter, "String", "SLE", "Country of Sierra Leone");
        addChoiceList(noChoices, parameter, "String", "SGP", "Country of Singapore");
        addChoiceList(noChoices, parameter, "String", "SXM", "Country of Sint Maarten (Dutch part)");
        addChoiceList(noChoices, parameter, "String", "SVK", "Country of Slovakia");
        addChoiceList(noChoices, parameter, "String", "SVN", "Country of Slovenia");
        addChoiceList(noChoices, parameter, "String", "SLB", "Country of Solomon Islands");
        addChoiceList(noChoices, parameter, "String", "SOM", "Country of Somalia");
        addChoiceList(noChoices, parameter, "String", "ZAF", "Country of South Africa");
        addChoiceList(noChoices, parameter, "String", "SGS", "Country of South Georgia and the South Sandwich Islands");
        addChoiceList(noChoices, parameter, "String", "SSD", "Country of South Sudan");
        addChoiceList(noChoices, parameter, "String", "ESP", "Country of Spain");
        addChoiceList(noChoices, parameter, "String", "LKA", "Country of Sri Lanka");
        addChoiceList(noChoices, parameter, "String", "SDN", "Country of Sudan (the)");
        addChoiceList(noChoices, parameter, "String", "SUR", "Country of Suriname");
        addChoiceList(noChoices, parameter, "String", "SJM", "Country of Svalbard and Jan Mayen");
        addChoiceList(noChoices, parameter, "String", "SWE", "Country of Sweden");
        addChoiceList(noChoices, parameter, "String", "CHE", "Country of Switzerland");
        addChoiceList(noChoices, parameter, "String", "SYR", "Country of Syrian Arab Republic");
        addChoiceList(noChoices, parameter, "String", "TWN", "Country of Taiwan (Province of China)");
        addChoiceList(noChoices, parameter, "String", "TJK", "Country of Tajikistan");
        addChoiceList(noChoices, parameter, "String", "TZA", "Country of Tanzania, United Republic of");
        addChoiceList(noChoices, parameter, "String", "THA", "Country of Thailand");
        addChoiceList(noChoices, parameter, "String", "TLS", "Country of Timor-Leste");
        addChoiceList(noChoices, parameter, "String", "TGO", "Country of Togo");
        addChoiceList(noChoices, parameter, "String", "TKL", "Country of Tokelau");
        addChoiceList(noChoices, parameter, "String", "TON", "Country of Tonga");
        addChoiceList(noChoices, parameter, "String", "TTO", "Country of Trinidad and Tobago");
        addChoiceList(noChoices, parameter, "String", "TUN", "Country of Tunisia");
        addChoiceList(noChoices, parameter, "String", "TUR", "Country of Turkey");
        addChoiceList(noChoices, parameter, "String", "TKM", "Country of Turkmenistan");
        addChoiceList(noChoices, parameter, "String", "TCA", "Country of Turks and Caicos Islands (the)");
        addChoiceList(noChoices, parameter, "String", "TUV", "Country of Tuvalu");
        addChoiceList(noChoices, parameter, "String", "UGA", "Country of Uganda");
        addChoiceList(noChoices, parameter, "String", "UKR", "Country of Ukraine");
        addChoiceList(noChoices, parameter, "String", "ARE", "Country of United Arab Emirates (the)");
        addChoiceList(noChoices, parameter, "String", "GBR",
                "Country of United Kingdom of Great Britain and Northern Ireland (the)");
        addChoiceList(noChoices, parameter, "String", "UMI", "Country of United States Minor Outlying Islands (the)");
        addChoiceList(noChoices, parameter, "String", "USA", "Country of United States of America (the)");
        addChoiceList(noChoices, parameter, "String", "URY", "Country of Uruguay");
        addChoiceList(noChoices, parameter, "String", "UZB", "Country of Uzbekistan");
        addChoiceList(noChoices, parameter, "String", "VUT", "Country of Vanuatu");
        addChoiceList(noChoices, parameter, "String", "VEN", "Country of Venezuela (Bolivarian Republic of)");
        addChoiceList(noChoices, parameter, "String", "VNM", "Country of Viet Nam");
        addChoiceList(noChoices, parameter, "String", "VGB", "Country of Virgin Islands (British)");
        addChoiceList(noChoices, parameter, "String", "VIR", "Country of Virgin Islands (U.S.)");
        addChoiceList(noChoices, parameter, "String", "WLF", "Country of Wallis and Futuna");
        addChoiceList(noChoices, parameter, "String", "ESH", "Country of Western Sahara");
        addChoiceList(noChoices, parameter, "String", "YEM", "Country of Yemen");
        addChoiceList(noChoices, parameter, "String", "ZMB", "Country of Zambia");
        addChoiceList(noChoices, parameter, "String", "ZWE", "Country of Zimbabwe");
        addChoiceList(noChoices, parameter, "String", "ALA", "Country of Åland Islands");

    }

    public String getCountry(String isoCountry) {
        int idx = countryList.indexOf(isoCountry);
        String countryISO = countryList.get(idx);
        return countryISO;
    }

    public void setDiscountList() {
        this.discountList.add(Instype.ATMATURITY);
        this.discountList.add(Instype.DISCOUNT);
        this.discountList.add(Instype.CURRENCY);

    }

    public boolean inDiscountList(String instype) {
        return this.discountList.contains(instype);
    }

    public void setCallPutTypeList() {
        String parameter = "Calltype";
        this.callTypeList.add(0, "AM");
        addChoiceList(noChoices, parameter, "String", "AM",
                "Indicates that this bond has American calls - continuosly callable");
        this.callTypeList.add(1, "EUR");
        addChoiceList(noChoices, parameter, "String", "EUR",
                "Indicates that this bond has European calls - calls allowed on on specific dates");
        this.callTypeList.add(1, "EUR-MW");
        addChoiceList(noChoices, parameter, "String", "EUR-MW",
                "Indicates that this bond has a European make whole call - calls allowed on on specific dates");
        this.callTypeList.add(0, "AM-MW");
        addChoiceList(noChoices, parameter, "String", "AM-MW",
                "Indicates that this bond has American make whole call - continuosly callable");
    }

    public void setCallPriceTypeList() {
        String parameter = "CallPriceType";
        this.callPutPriceTypeList.add("Price");
        addChoiceList(noChoices, parameter, "String", "Price",
                "Indicates that a schedule of call prices are specified for determining the call price for the continuously callable bond");
        this.callPutPriceTypeList.add("Amort");
        addChoiceList(noChoices, parameter, "String", "Amort",
                "Indicates that the call price is determined by an amortization calculation");
    }

    public boolean inCallTypeList(String callType) {
        return callTypeList.contains(callType);
    }

    public void setSchedRuleList() {
        this.schedRuleList.add("ACCRUE");
        this.schedRuleList.add("INTEREST");
        this.schedRuleList.add("RESET");
        this.schedRuleList.add("PAYLEG");
        this.schedRuleList.add("RECLEG");
        this.schedRuleList.add("INDEXASSUME");
        this.schedRuleList.add("INDEX");
        this.schedRuleList.add("paylegReset");
        this.schedRuleList.add("RESETRECLEG");
    }

    boolean inSchedRuleList(String schedType) {
        return schedRuleList.contains(schedType);
    }

    public void setIndexInterpList() {
        String parameter = "IdxInterpMeth";
        this.indexInterpList.add(0, BondConsts.INDEXINTERPNONE);
        addChoiceList(noChoices, parameter, "String", "IndexInterpNone",
                "No interpolation of index ratios or CPI indexes");
        this.indexInterpList.add(1, BondConsts.INDEXINTERPLINEAR);
        addChoiceList(noChoices, parameter, "String", "IndexIntepLinear",
                "Linear(sttaight line) interpolation of index ratios or CPI indexes");
        this.indexInterpList.add(2, BondConsts.INDEXINTERPBBG);
        addChoiceList(noChoices, parameter, "String", "IndexInterpBBG", "Uses Bloomberg method");
        this.indexInterpList.add(3, BondConsts.INDEXINTERPJAPAN);
        addChoiceList(noChoices, parameter, "String", "IndexInterpJapan",
                "Japanese convention for interpolation of index ratios or CPI indexes on the 10th of the month");
        this.indexInterpList.add(4, BondConsts.INDEXINTERPAUS);
        addChoiceList(noChoices, parameter, "String", "IndexInterpAUS",
                "Australian method of index ratios or CPI indexes");
        this.indexInterpList.add(5, BondConsts.INDEXINTERPISRAEL);
        addChoiceList(noChoices, parameter, "String", "IndexInterpIsrael",
                "Israel convention for interpolation of index ratios or CPI indexes");
        this.indexInterpList.add(6, BondConsts.INDEXINTERPBRAZIL);
        addChoiceList(noChoices, parameter, "String", "IndexInterpBrazil",
                "Brazil convention for interpolation of index ratios or CPI indexes");

    }

    boolean inIndexInterpList(String sVal) {
        return indexInterpList.contains(sVal);
    }

    public void setReinvestList() {
        int i = 0;
        String parameter = "ReinVestMeth";
        this.reinVestList.add(i++, "NA");
        addChoiceList(noChoices, parameter, "String", "NA", "User a zero reinvestment rate");
        this.reinVestList.add(i++, "YTM");
        addChoiceList(noChoices, parameter, "String", "YTM", "Use the yield to maturity as the reinvestment rate");
        this.reinVestList.add(i++, "SC");
        addChoiceList(noChoices, parameter, "String", "FC",
                "Use the current spot yield curve  as the reinvestment rates");
        this.reinVestList.add(i++, "REINRT");
        addChoiceList(noChoices, parameter, "String", "REINRT",
                "Use a specified set of reinvest rate and meturity lengths(yrs)");
    }

    boolean inReinvestList(String reinVestMeth) {
        return reinVestList.contains(reinVestMeth);
    }

    public void setDateFormatList() {
        int i = 0;
        String parameter = "dtFormat";
        this.dateFormatList.add(i, "MM/DD/YYYY");
        addChoiceList(noChoices, parameter, "String", "MM/DD/YYYY", "US date convention month/day/4 digit year");
        this.dateFormatList.add(++i, "MM.DD.YYYY");
        addChoiceList(noChoices, parameter, "String", "MM.DD.YYYY",
                "Alternative US date convention month.day.4 digit year");
        this.dateFormatList.add(++i, "DD/MM/YYYY");
        addChoiceList(noChoices, parameter, "String", "DD/MM/YYYY", "European date convention day/months/4 digit year");
        this.dateFormatList.add(++i, "DD.MM.YYYY");
        addChoiceList(noChoices, parameter, "String", "DD.MM.YYYY",
                "Austria & Germany & Swiss date convention day.month.4 digit year");
        this.dateFormatList.add(++i, "YYYY/MM/DD");
        addChoiceList(noChoices, parameter, "String", "YYYY/MM/DD",
                "Alternative formal date convention 4 digit year/month/day");
        this.dateFormatList.add(++i, "YYYY.MM.DD");
        addChoiceList(noChoices, parameter, "String", "YYYY.MM.DD",
                "Formal date convention 4 digit year then month then day");
        this.dateFormatList.add(++i, "MON-DAY-YYYY");
        addChoiceList(noChoices, parameter, "String", "MON-DAY-YYYY",
                "Decorative US date convention month-day-4 digit year");
        this.dateFormatList.add(++i, "DAY-MON-YYYY");
        addChoiceList(noChoices, parameter, "String", "DAY-MON-YYYY",
                "Decorative European date convention day-month-4 digit year");
        this.dateFormatList.add(++i, "MON DAY YYYY");
        addChoiceList(noChoices, parameter, "String", "MON DAY YYYY",
                "Decorative US date convention month day 4 digit year");
        this.dateFormatList.add(++i, "DAY MON YYYY");
        addChoiceList(noChoices, parameter, "String", "DAY MON YYYY",
                "Decorative European date convention day month 4 digit year");
    }

    boolean inDateFormatList(String dtFormat) {
        return dateFormatList.contains(dtFormat);
    }

    public void setCouponQuoteList() {
        String parameter = "cpnQuoteType";
        this.couponQuoteList.add(0, "ANNUAL");
        addChoiceList(noChoices, parameter, "String", "Annual",
                "The coupon is quote on the bond is an annual coupon rate - the standard convention");
        this.couponQuoteList.add(1, "SEMIEQV");
        addChoiceList(noChoices, parameter, "String", "SemiEqv",
                "The coupon rate is on the bond quote as a semi-annual coupon rate and the annual coupon is reduced - sometimes used for Brazilian bonds");
        this.couponQuoteList.add(2, "SEMIEQV+");
        addChoiceList(noChoices, parameter, "String", "SemiEqv+",
                "The coupon rate is on the bond quote as a semi-annual coupon rate and the annual coupon is reduced by modified semi-annual compounding - sometimes used for Brazilian bonds");
    }

    public void setAccIntMethod() {
        String parameter = "accINtMetho";
        int i = 0;
        this.accIntMethod.add(i++, "STD");
        addChoiceList(noChoices, parameter, "String", "STD", " no rounding of bond bond size involved");
        this.accIntMethod.add(i++, "DAY+PRIN_SIZE");
        addChoiceList(noChoices, parameter, "String", "SIZE+DAY_SIZE",
                "round on combo of size and daycount, then principal");
        this.accIntMethod.add(i++, "SIZE+PRIN_DAY");
        addChoiceList(noChoices, parameter, "String", "SIZE+PRIN_DAY",
                "round on combo of size and then principal then day");
        this.accIntMethod.add(i++, "DAY+SIZE_PRIN");
        addChoiceList(noChoices, parameter, "String", "DAY+SIZE_PRIN",
                "round on combo of day and then size then principal");
        this.accIntMethod.add(i++, "CANADACCRUED");
        addChoiceList(noChoices, parameter, "String", "CANADACCRUED",
                "change daycount from 365 when daycount is greater than 182.5");
        this.accIntMethod.add(i++, "PRIN_SIZE");
        addChoiceList(noChoices, parameter, "String", "PRIN_SIZE", "round on based on principal and size");

    }

    public void setSpotIntervalList() {
        int i = 0;
        String parameter = "Spot Curve Interval";
        this.spotIntervalList.add(i++, "1m");
        addChoiceList(noChoices, parameter, "String", "1m", "spot rate interval 1 month apart");
        this.spotIntervalList.add(i++, "2m");
        addChoiceList(noChoices, parameter, "String", "2m", "spot rate interval 2 months apart");
        this.spotIntervalList.add(i++, "3m");
        addChoiceList(noChoices, parameter, "String", "3m", "spot rate interval 3 months apart");
        this.spotIntervalList.add(i++, "6m");
        addChoiceList(noChoices, parameter, "String", "6m", "spot rate interval 6 months apart");
    }

    public void setDiscountCurveList() {
        int i = -1;
        discountCurveList.add(++i, CalcList.ZSPREAD);
        discountCurveList.add(++i, CalcList.FAIRPRICE);
        discountCurveList.add(++i, CalcList.HORIZONANALYSIS);
        discountCurveList.add(++i, CalcList.KEYRATEDURATION);
        discountCurveList.add(++i, CalcList.FUTUREVALUE);
        discountCurveList.add(++i, CalcList.FORWARDCURVE);
        discountCurveList.add(++i, CalcList.SPOTCURVE);
        discountCurveList.add(++i, CalcList.FUTUREVALUERISK);
        discountCurveList.add(++i, CalcList.FUTRETURNRISK);
        discountCurveList.add(++i, CalcList.ATTRIBUTION);
        discountCurveList.add(++i, CalcList.SWAPANALYSIS);

    }

    public boolean inDiscountCurveList(int calcNo) {
        return discountCurveList.contains(calcNo);
    }

    public boolean inDiscountCurveList(ArrayList<Integer> calculationList) {
        int count = calculationList.size();
        int i = -1;
        while (++i < count) {
            int calcNo = calculationList.get(i);
            if (discountCurveList.contains(calcNo))
                return true;
        }
        return false;
    }

    boolean inCouponQuoteList(String couponQuote) {
        return couponQuoteList.contains(couponQuote);
    }

    public boolean inHolidayOffList(String holidayOffset) {
        return this.holidayOffsetList.contains(holidayOffset);
    }

    public void setPassthruList() {
        passthruList.add(0, "ARM");
        passthruList.add(1, "MORTGAGE");
    }

    public boolean inPassthruList(String instype) {
        return passthruList.contains(instype);
    }

    public void setPassthruProjectList() {
        String parameter = "PassProjectMeth";
        this.passthruProjectList.add(0, "SPEED");
        addChoiceList(noChoices, parameter, "String", "Speed", "Specify your own prepayment speed");
        this.passthruProjectList.add(1, "FACTORS");
        addChoiceList(noChoices, parameter, "String", "Factors",
                "Specify two dates and two factor and system will calculate the speed and uses it for future payments");
        this.passthruProjectList.add(2, "ISSUED");
        addChoiceList(noChoices, parameter, "String", "Issued",
                "System calculates the average speed from dated date and uses it for future payments");
    }

    public boolean inPassthruProjectList(String projectMeth) {
        return passthruProjectList.contains(projectMeth.toUpperCase());
    }

    public void setYieldQuoteList() {
        String parameter = "yieldQuoteType";
        this.yieldQuoteList.add(0, "SIMPLE");
        addChoiceList(noChoices, parameter, "String", "Simple",
                "Use the simple interest method - no compounding - and use price as a base used for Treasury Bills less than one year maturity");
        this.yieldQuoteList.add(1, "TREAS");
        addChoiceList(noChoices, parameter, "String", "Treas",
                "Use the US Treasury interest method - no compounding - for the yield calculation typically used for short term zero coupon bonds");
        this.yieldQuoteList.add(2, "COMPOUND");
        addChoiceList(noChoices, parameter, "String", "Compound",
                "Use the standard compound yield calculation for zero coupons whose maturity length is greater than one year");
    }

    boolean inYieldQuoteList(String yieldQuote) {
        return yieldQuoteList.contains(yieldQuote);
    }

    public void setAccruedAdjustList() {
        String parameter = "Accrued Interest Adjustments";
        this.accruedAdjustList.add(0, "STD");
        addChoiceList(noChoices, parameter, "String", "STD", "No accrued interest adjustments");
        this.accruedAdjustList.add(1, "DAY+PRIN_SIZE");
        addChoiceList(noChoices, parameter, "String", "DAY+PRIN_SIZE",
                "Adjust standard accrued interest rounding principal factor together with daycounting factor then coupon rate");
        this.accruedAdjustList.add(2, "SIZE+PRIN_DAY");
        addChoiceList(noChoices, parameter, "String", "SIZE+PRIN_DAY",
                "Adjust standard accrued interest rounding principal factor together with size then coupon period factor");
        this.accruedAdjustList.add(3, "DAY+SIZE_PRIN");
        addChoiceList(noChoices, parameter, "String", "DAY+SIZE_PRIN",
                "Adjust standard accrued interest rounding daycount factor together with size then principal factor");
        this.accruedAdjustList.add(4, "SIZE");
        addChoiceList(noChoices, parameter, "String", "SIZE",
                "Adjust standard accrued interest rounding based on unit face amount size then scale to face actual face amount");
        this.accruedAdjustList.add(5, "CANADACCRUED"); // canada accrued method
        addChoiceList(noChoices, parameter, "STRING", "CANADA", "Use the Canadian accrued interest method");
    }

    public boolean inAccruedAdjustList(String accruedAdjust) {
        return accruedAdjustList.contains(accruedAdjust.toUpperCase());
    }

    public void setTimeUnitList() {
        String parameter = "Basis payment frquenct time unit";
        this.timeUnitList.add(0, "D");
        addChoiceList(noChoices, parameter, "String", BondConsts.DAYTIMEUNIT,
                "The payment frequency is a mutliple of days");
        this.timeUnitList.add(1, BondConsts.WEEKTIMEUNIT);
        addChoiceList(noChoices, parameter, "String", BondConsts.WEEKTIMEUNIT,
                "The payment frequency is a mutliple of weeks");
        this.timeUnitList.add(2, BondConsts.MONTHTIMEUNIT);
        addChoiceList(noChoices, parameter, "String", BondConsts.MONTHTIMEUNIT,
                "The payment frequency is a mutliple of months");
        this.timeUnitList.add(3, BondConsts.ENUMERATEDTIMEUNIT);
        addChoiceList(noChoices, parameter, "String", BondConsts.ENUMERATEDTIMEUNIT,
                "The payment frequency is specified in a list by you");
        this.timeUnitList.add(4, BondConsts.NOPERIODICITY);
        addChoiceList(noChoices, parameter, "String", BondConsts.NOPERIODICITY,
                "There is no payment frequency the bond make only one payment at maturity");

    }

    public boolean inTimeUnitList(String timeUnit) {
        int len = timeUnit.length();
        String strTimeUnit = timeUnit.substring(len - 1);
        return timeUnitList.contains(strTimeUnit);
    }

    public void setCashflowCalcList() {
        cashflowCalcList.add(0, CalcList.CASHFLOW);
        cashflowCalcList.add(1, CalcList.ACCREAMORT);
        cashflowCalcList.add(2, CalcList.ACCREAMORTSCHED);
        cashflowCalcList.add(3, CalcList.INTERESTINCOME);
    }

    public boolean inCashflowList(int calcNo) {
        return cashflowCalcList.contains(calcNo);
    }

    public void setYieldCurveInterpList() {
        String parameter = "YieldCurveInterpMeth";
        this.yieldCurveInterpList.add(0, "LINEAR");
        addChoiceList(noChoices, parameter, "String", "Linear", "Use straight line interpolation");
        this.yieldCurveInterpList.add(1, "CUBIC");
        addChoiceList(noChoices, parameter, "String", "Cubic", "Use Cubic Spline interpolation");

    }

    public void setAccreAmortControlList() {
        String parameter = "Amortization Control";
        accreAmortControlList.add(0, "NONEGINCOME");
        addChoiceList(noChoices, parameter, "String", "NONEGINCOME",
                "Do not allow total income on a lot to go negative");
        accreAmortControlList.add(1, "NOTBELOWPAR");
        addChoiceList(noChoices, parameter, "String", "NOTBELOWPAR", "Do not allow book value to go below par");
        accreAmortControlList.add(2, "NOTABOVEPAR");
        addChoiceList(noChoices, parameter, "String", "NOTABOVEPAR", "Do not allow book value to go above par");
    }

    boolean inAccreAmortControlList(String accreAmortOp) {
        return accreAmortControlList.contains(accreAmortOp);
    }

    boolean inCallPriceTypeList(String callPriceType) {
        return callTypeList.contains(callPriceType);
    }

    public void setCreditDefaultModelList() {
        this.creditDefaultModelList.add(0, "CDR");
        this.creditDefaultModelList.add(1, "USER");
    }

    boolean inCreditDefaultModelList(String creditDefaultModel) {
        return creditDefaultModelList.contains(creditDefaultModel);
    }

    public void setMonthOfYearList() {
        int i = 0;
        String parameter = "Months of the year";
        this.monthOfYearList.add(i, "JAN");
        addChoiceList(noChoices, parameter, "String", "JAN", "January");
        this.monthOfYearList.add(++i, "FEB");
        addChoiceList(noChoices, parameter, "String", "FEB", "February");
        this.monthOfYearList.add(++i, "MAR");
        addChoiceList(noChoices, parameter, "String", "MAR", "March");
        this.monthOfYearList.add(++i, "APR");
        addChoiceList(noChoices, parameter, "String", "APR", "April");
        this.monthOfYearList.add(++i, "MAY");
        addChoiceList(noChoices, parameter, "String", "MAY", "May");
        this.monthOfYearList.add(++i, "JUN");
        addChoiceList(noChoices, parameter, "String", "JUN", "June");
        this.monthOfYearList.add(++i, "JUL");
        addChoiceList(noChoices, parameter, "String", "JUL", "July");
        this.monthOfYearList.add(++i, "AUG");
        addChoiceList(noChoices, parameter, "String", "AUG", "August");
        this.monthOfYearList.add(++i, "SEP");
        addChoiceList(noChoices, parameter, "String", "SEP", "September");
        this.monthOfYearList.add(++i, "OCT");
        addChoiceList(noChoices, parameter, "String", "OCT", "October");
        this.monthOfYearList.add(++i, "NOV");
        addChoiceList(noChoices, parameter, "String", "NOV", "November");
        this.monthOfYearList.add(++i, "DEC");
        addChoiceList(noChoices, parameter, "String", "DEC", "December");
    }

    boolean inMonthOfYearList(String monthOfYear) {
        int key = monthOfYearList.indexOf(monthOfYear);
        return monthOfYearList.contains(monthOfYear);
    }

    public void setMatureLengthList() {
        String parameter = "Maturity length options";
        this.matureLengthList.add(0, "LEGALYEARS");
        addChoiceList(noChoices, parameter, "String", "LEGALYEARS",
                "Maturity length calculated as years to legal maturity date stipulated in the propectus");
        this.matureLengthList.add(1, "LEGALDAYS");
        addChoiceList(noChoices, parameter, "String", "LEGALDAYS",
                "Maturity length calculated as days to legal maturity date stipulated in the propectus");
        this.matureLengthList.add(2, "PRINWEIGHTEDYEARS");
        addChoiceList(noChoices, parameter, "String", "PRINWEIGHTEDYEARS",
                "Maturity length calculated as the principal weighted years to legal maturity date stipulated in the propectus");
        this.matureLengthList.add(3, "PRINWEIGHTEDDAYS");
        addChoiceList(noChoices, parameter, "String", "PRINWEIGHTEDDAYS",
                "Maturity length calculated as the principal weighted days to legal maturity date stipulated in the propectus");
        this.matureLengthList.add(4, "CASHWEIGHTEDYEARS");
        addChoiceList(noChoices, parameter, "String", "CASHWEIGHTEDYEARS",
                "Maturity length calculated as the cashflow weighted years to legal maturity date stipulated in the propectus");
        this.matureLengthList.add(5, "CASHWEIGHTEDDAYS");
        addChoiceList(noChoices, parameter, "String", "CASHWEIGHTEDDAYS",
                "Maturity length calculated as the cashflow weighted days to legal maturity date stipulated in the propectus");

    }

    boolean inMaturLengthList(String matureLengthOp) {
        return matureLengthList.contains(matureLengthOp);
    }

    public void setYieldCurveList() {
        int i = -1;
        this.yieldCurveList.add(++i, CalcList.KEYRATEDURATION);
        this.yieldCurveList.add(++i, CalcList.ZSPREAD);
        this.yieldCurveList.add(++i, CalcList.KEYRATEDURATION);
        this.yieldCurveList.add(++i, CalcList.SPOTCURVE);
        this.yieldCurveList.add(++i, CalcList.FORWARDCURVE);
        this.yieldCurveList.add(++i, CalcList.FUTUREVALUE);
        this.yieldCurveList.add(++i, CalcList.FUTUREVALUERISK);
        this.yieldCurveList.add(++i, CalcList.HORIZONANALYSIS);
    }

    public boolean inYieldCurveList(int calcNo) {
        return yieldCurveList.contains(calcNo);
    }

    public void setYieldCalcList() { // list of calc requesting yields
        int i = 0;
        this.yieldCalcList.add(i++, CalcList.PRICETOMATURIY);

    }

    public boolean inYieldCalcList(int calcNo) {
        return yieldCalcList.contains(calcNo);
    }

    public void setCalcPriceList() {
        int i = 0;
        calcPriceList.add(CalcList.REALYIELD);
        calcPriceList.add(CalcList.HORIZONANALYSIS);
        calcPriceList.add(CalcList.YIELDTOMATURITY);
        calcPriceList.add(CalcList.GAINLOSS);
        calcPriceList.add(CalcList.NETPAYMENT);
        calcPriceList.add(CalcList.ZSPREAD);
        calcPriceList.add(CalcList.SWAPANALYSIS);
    }

    public boolean inCalcPriceList(int calcNo) {
        return calcPriceList.contains(calcNo);
    }

    public void setReinvestCalcList() {
        this.reinvestCalcList.add(CalcList.FUTUREVALUE);
        this.reinvestCalcList.add(CalcList.HORIZONANALYSIS);
        this.reinvestCalcList.add(CalcList.FUTUREVALUERISK);
        this.reinvestCalcList.add(CalcList.FAIRPRICE);
        this.reinvestCalcList.add(CalcList.REALYIELD);
    }

    public boolean inReinvestCalcList(int calcNo) {
        return reinvestCalcList.contains(calcNo);
    }

    public void setSwapLegTypeList() {
        String parameter = "Type of interest rate swap legs";
        this.swapLegTypeList.add(0, "PAYFIXED");
        addChoiceList(noChoices, parameter, "String", "PAYFIXED", "This leg pays a fixed interest rate");
        this.swapLegTypeList.add(1, "PAYFLOAT");
        addChoiceList(noChoices, parameter, "String", "PAYFLOAT", "This leg pays a floating rate interest rate");
        this.swapLegTypeList.add(2, "RECEIVEFIXED");
        addChoiceList(noChoices, parameter, "String", "RECEIVEFIXED", "This leg receives a fixed interest rate");
        this.swapLegTypeList.add(3, "RECEIVEFLOAT");
        addChoiceList(noChoices, parameter, "String", "RECEIVEFLOAT",
                "This leg receives a floating rate interest rate");
    }

    boolean inSwapLegTypeList(String swapLegType) {
        return swapLegTypeList.contains(swapLegType);
    }

    public void setSwapPayTypeList() {
        String parameter = "Interest rate swap leg";
        this.swapPayTypeList.add(0, "RECLEG");
        addChoiceList(noChoices, parameter, "String", "RECLEG", "This is the receive leg of the interest rate swap");
        this.swapLegTypeList.add(1, "PAYLEG");
        addChoiceList(noChoices, parameter, "String", "PAYLEG", "This is the pay leg of the interest rate swap");
        this.swapLegTypeList.add(2, "NETLEG");
        addChoiceList(noChoices, parameter, "String", "NET", "This is a net pay leg of the interest rate swap");
    }

    boolean inSwapPayTypeList(String swapPayType) {
        return swapLegTypeList.contains(swapPayType);
    }

    public void setBookYieldOptList() {
        String parameter = "BookCalcMeth";
        this.bookYieldOptList.add("YTM");
        addChoiceList(noChoices, parameter, "String", "YTM",
                "Use yield to maturity for constant yield or effective income bookvalue");
        this.bookYieldOptList.add("YTW");
        addChoiceList(noChoices, parameter, "String", "YTW",
                "Use yield to worst for constant yield or effective income bookvalue");
        this.bookYieldOptList.add("YFC"); // yield to first call
        addChoiceList(noChoices, parameter, "String", "YFC",
                "Use yield to first call for constant yield or effective income bookvalue");

    }

    public boolean inBookYieldOptionList(String bookYieldOpt) {
        return this.bookYieldOptList.contains(bookYieldOpt);
    }

    public void setBypassMoreDefaultList() {
        bypassMoreDefaultList.add(0, CalcList.SPOTCURVE);
        bypassMoreDefaultList.add(1, CalcList.FORWARDCURVE);

    }

    public boolean inBypassMoreDefaultList(ArrayList<Integer> calculationList) {
        int i = -1, noList = calculationList.size();
        int no = 0;
        while (++i < noList) {
            int calcNo = calculationList.get(i);
            if (bypassMoreDefaultList.contains(calcNo))
                ++no;
        }
        if (no == noList)
            return true;
        return false;
    }

    public void setChoices() {
        setAccrualAccountingList();
        setAccrueBasisList();
        setBondEqvList();
        setBookCalcList();
        setBookYieldOptList();
        setBypassDefaultList();
        setBypassMoreDefaultList();
        setCalcPriceList();
        setCallPriceTypeList();
        setCallPutTypeList();
        setCashflowCalcList();
        setCompoundFreqList();
        setConversionOptionList();
        setCountryList();
        setCouponQuoteList();
        setCreditDefaultModelList();
        setCurrencyList();
        setDateFormatList();
        setDayOfWeekList();
        setDelimiterList();
        setDiscountCurveList();
        setDiscountList();
        setDocumentList();
        setFixedRateList();
        setFloatList();
        setFloatSinkList();
        setFutureCalcList();
        setFutureDiscountCurveList();
        setHolidayOffsetList();
        setIndexInterpList();
        setInstypeList();
        setMatureLengthList();
        setMonthOfYearList();
        setNoList();
        setNoSecurityCalcList();
        setOutputStringList();
        setPassModelList();
        setPassModelList();
        setPassthruList();
        setPassthruProjectList();
        setPayList();
        setPayResetList();
        setPaySinkList();
        setPerpetualList();
        setReinvestList();
        setResetRateList();
        // setSchedTypeList();
        setSinkList();
        setStripTypeList();
        setSwapList();
        setSwapLegTypeList();
        setSwapPayTypeList();
        setTimeUnitList();
        setTradeTypeList();
        setYieldCalcList();
        setYieldCurveInterpList();
        setYieldCurveList();
        setYieldForecastList();
        setYieldQuoteList();
        setPortfolioParamsList();
        setRiskReturnList();
        setBenchmarkList();
        setSwapSideList();
    }

    public boolean isInStringList(StringToBond.ParamMap paramMap, String sVal) {
        if (paramMap.isChoiceYn == 'N')
            return true;
        int idx = UnAvails.NOTAVAILINT;
        boolean isOk = validateChoice(paramMap.choiceList, sVal);
        if (!isOk) {
            String errmsg = "Parameter " + paramMap.paramName + " has an invalid value (" + sVal + ") specfied";
            error.setError(getClass().getName(), Error.PARAMERROR, errmsg);
            return false;
        }
        return isOk;
    }

    public void setPortfolioParamsList() {
        int no = 0;
        portfolioParamsList.add(no++, "DTSETTLE");
        portfolioParamsList.add(no++, "FILEOUTNAME");
        portfolioParamsList.add(no++, "FILEOUTFORMAT");
        portfolioParamsList.add(no++, "FILEOUTYN");
        portfolioParamsList.add(no++, "SHOWINDIVCALCYN");
        portfolioParamsList.add(no++, "DTFUTURE");
        portfolioParamsList.add(no++, "DURATIONWT");
        portfolioParamsList.add(no++, "RETURNWT");
        portfolioParamsList.add(no++, "CALCREQUEST");
        portfolioParamsList.add(no++, "PORTNAME");
        portfolioParamsList.add(no++, "YIELDFORECASTMETH");
        portfolioParamsList.add(no++, "YIELDCURVEINTERPMETH");
        portfolioParamsList.add(no++, "HORIZONPERIODS");
        portfolioParamsList.add(no++, "HORIZONFREQ");
        portfolioParamsList.add(no++, "RISKRETURNGOAL");
        portfolioParamsList.add(no++, "SWAPPCTGAIN");
    }

    public boolean inPortfolioParamsList(String strParam) {
        return portfolioParamsList.contains(strParam.toUpperCase());
    }

    public boolean validateChoice(String listName, String sVal) {
        boolean isOk = false;
        switch (listName.toUpperCase()) {
            case "ACCREAMORTCONTROLLIST":
                isOk = inAccreAmortControlList(sVal);
                return isOk;
            case "ACCRUEBASISLIST":
                isOk = inAccrueBasisList(sVal);
                return isOk;
            case "BONDEQVLIST":
                isOk = inBondEqvList(sVal);
                return isOk;
            case "BOOKCALCLIST":
                isOk = inBookCalcList(sVal);
                return isOk;
            case "BOOKYIELDOPTION":
                isOk = inBookYieldOptionList(sVal);
                return isOk;
            case "BOOKYIELDOPTLIST":
                isOk = inBookYieldOptionList(sVal);
                return isOk;
            case "CALLPRICETYPELIST":
                isOk = inCallPriceTypeList(sVal);
                return isOk;
            case "CALLTYPELIST":
                isOk = inCallTypeList(sVal);
                return isOk;
            case "COMPOUNDFREQLIST":
                isOk = inCompoundFreqList(sVal);
                return isOk;
            case "CONVERSIONOPTIONLIST":
                isOk = inConversionOptionList(sVal);
                return isOk;
            case "COUNTRYLIST":
                isOk = countryList.contains(sVal);
                return isOk;
            case "COUPONQUOTELIST":
                isOk = inCouponQuoteList(sVal);
                return isOk;
            case "CREDITDEFAULTMODELLIST":
                isOk = inCreditDefaultModelList(sVal);
                return isOk;
            case "DATEFORMATLIST":
                isOk = inDateFormatList(sVal);
                return isOk;
            case "DAYOFWEEKLIST":
                isOk = inDayOfWeekList(sVal);
                return isOk;
            case "DEFAULTMODELLIST":
                isOk = inCreditDefaultModelList(sVal);
                return isOk;
            case "DELIMITLIST":
                isOk = inDelimiterList(sVal);
                return isOk;
            case "DISCOUNTLIST":
                isOk = inDiscountList(sVal);
                return isOk;
            case "HOLIDAYOFFSETLIST":
                isOk = inHolidayOffList(sVal);
                return isOk;
            case "INDEXINTERPLIST":
                isOk = inIndexInterpList(sVal);
                return isOk;
            case "INSTYPELIST":
                isOk = instypeList(sVal);
                return isOk;
            case "INTPERIODICITY":
            case "IDXPERIODICITY":
            case "ACCPERIODICITY":
            case "PAYLEGPERIODICITY":
            case "RECLEGPERIODICITY":
            case "PAYLEGRESETPERIODICITY":
            case "RECLEGRESETPERIODICITY":
            case "RESETPERIODICITY":
                isOk = checkPeriodicity(sVal);
                return isOk;
            case "RISKRETURNLIST":
                isOk = inRiskRetunList(sVal);
                return isOk;
            case "SWAPLEGTYPELIST":
                isOk = inSwapLegTypeList(sVal);
                return isOk;
            case "MATURELENGTHLIST":
                isOk = inMaturLengthList(sVal);
                return isOk;
            case "MONTHOFYEARLIST":
                isOk = inMonthOfYearList(sVal);
                return isOk;
            case "NOLIST":
                return isOk;
            case "OUTPUTSTRINGLIST":
                isOk = outputStringList.contains(sVal);
                return isOk;
            case "PASSMODELLIST":
                isOk = inPassModelList(sVal);
                return isOk;
            case "PASSTHRUPROJECTLIST":
                isOk = inPassthruProjectList(sVal);
                return isOk;
            case "BENCHMARKLIST":
                isOk = inBenchmarkList(sVal);
                return isOk;
            case "STRIPTYPELIST":
                isOk = inStripTypeList(sVal);
                return isOk;
            case "SWAPPAYTYPELIST":
                isOk = inStripTypeList(sVal);
                return isOk;
            case "SWAPSIDELIST":
                isOk = inSwapSideList(sVal);
                return isOk;
            case "TRADETYPELIST":
                isOk = inTradeTypeList(sVal);
                return isOk;
            case "TIMEUNITLIST":
                isOk = inTimeUnitList(sVal);
                return isOk;
            case "YIELDCURVEINTERPLIST":
                isOk = yieldCurveInterpList.contains(sVal);
                break;
            case "YIELDQUOTELIST":
                isOk = inYieldQuoteList(sVal);
                return isOk;
            case "YIELDFORECASTLIST":
                isOk = inYieldForecastList(sVal);
                return isOk;
            default:
                return false;
        }
        return false;
    }

    class Choice {
        int idx;
        String paramName;
        String choice;
        String domain;
        String define;

        Choice() {
            idx = UnAvails.NOTAVAILINT;
            paramName = UnAvails.NOTAVAILSTRING;
            choice = UnAvails.NOTAVAILSTRING;
            domain = UnAvails.NOTAVAILSTRING;
            define = UnAvails.NOTAVAILSTRING;
        }

        Choice(int idx, String paramName, String domain, String choice, String define) {
            this.idx = idx;
            this.paramName = paramName;
            this.domain = domain;
            this.choice = choice;
            this.define = define;
        }
    }

}


import java.util.ArrayList;

public class CalcDef {
    int calcNo;
    String calcName;
    String format;
    int scale;
    char isMoney;
    String domain;
    String totalType;
    String taxableAdjustYn;
    String category;
    String arrayYn;
    int noColumns;
    String definition;
    String totalName;
    Domains domains;

    ArrayList<CalcDef> calcDefs = new ArrayList<>();

    ArrayList<CalcDef> horizonList = new ArrayList<>();

    ArrayList<CalcDefElement> calcElements = new ArrayList<>();

    CalcDef() {
        setCalcResultsDefinition();
    }

    CalcDef(int calcNo, String calcName, String format, int scale,
            char isMoney, String domain, String totalType, String taxableAdjustYn,
            String category, String arrayYn, int noColumns,
            String definition, String totalName) {
        this.calcNo = calcNo;
        this.calcName = calcName;
        this.format = format;
        this.scale = scale;
        this.isMoney = isMoney;
        this.domain = domain;
        this.totalType = totalType;
        this.taxableAdjustYn = taxableAdjustYn;
        this.category = category;
        this.arrayYn = arrayYn;
        this.noColumns = noColumns;
        this.definition = definition;
        this.totalName = totalName;
    }

    public void setCalcResultsDefinition() {
        calcDefs.add(0, new CalcDef(0, "AccruedInterest", "%.2f", 1, 'Y', "double", "S", "N", "Interest", "N", 1, "Accrued interest on settle date.", "PortAccruedInterest"));
        calcDefs.add(1, new CalcDef(1, "InterestIncome", "%.2f", 1, 'Y', "double", "S", "N", "Interest", "N", 1, "Interest income between any two dates.", "PortInterestIncome"));
        calcDefs.add(2, new CalcDef(2, "YieldToMaturity", "%.4f", 100, 'N', "double", "W", "Y", "Yield", "N", 1, "Yield to maturity given a price and settlement date.", "PortYieldToMaturity"));
        calcDefs.add(3, new CalcDef(3, "PriceToMaturity", "%.6f", 100, 'N', "double", "W", "N", "Price", "N", 1, "Price given a yield and settlement date.", "PortPriceToMaturity"));
        calcDefs.add(4, new CalcDef(4, "MacDuration", "%.4f", 1, 'N', "double", "W", "N", "Statistic", "N", 1, "Macaulay duration given a yield and settlement date.", "PortMacCaulayDuration"));
        calcDefs.add(5, new CalcDef(5, "ModDurationSIA", "%.4f", 1, 'N', "double", "W", "N", "Statistic", "N", 1, "Modified duration based on the Macaulay duration as defined by the SIA 1993 publication.", "PortDurationSIA"));
        calcDefs.add(6, new CalcDef(6, "ModDurationSTD", "%.4f", 1, 'N', "double", "W", "N", "Statistic", "N", 1, "Modified duration defined as the percentage price change per 1% change in yield.", "PortDurationStd"));
        calcDefs.add(7, new CalcDef(7, "Convexity", "%.4f", 1, 'N', "double", "W", "N", "Statistic", "N", 1, "Convexity defined as the rate of change of modified duration", "PortConvexity"));
        calcDefs.add(8, new CalcDef(8, "Cashflows", "%,.2f", 1, 'Y', "double", "S", "Y", "Cashflows", "Y", 8, "Cashflow from settle date to maturity showing full cashflow details.", "PortCashflows"));
        calcDefs.add(9, new CalcDef(9, "CouponDays", "%d", 1, 'N', "int", "W", "N", "DayCounting", "N", 1, "Number of days between last interest payment date and settle date.", "PortCouponDays"));
        calcDefs.add(10, new CalcDef(10, "YieldToWorst", "%.5f", 100, 'N', "double", "W", "N", "DayCounting", "N", 1, "Yield to the call date that gives the worst yield.", "PortYieldToWorst"));
        calcDefs.add(11, new CalcDef(11, "MaturityYears", "%.4f", 1, 'N', "double", "W", "N", "DayCounting", "N", 1, "Years from settlement to legal maturity date.", "PortLengthToMaurity(Yrs)"));
        calcDefs.add(12, new CalcDef(12, "MaturityDays", "%d", 1, 'N', "int", "W", "N", "DayCounting", "N", 1, "Days from settlement to legal maturity date.", "PortLengthToMaturity(Days)"));
        calcDefs.add(13, new CalcDef(13, "WtdPrincipalMaturityYears", "%.4f", 1, 'N', "double", "W", "N", "DayCounting", "N", 1, "Principal weighted average length in days.", "PortPrincipalMaturity(Yrs)"));
        calcDefs.add(14, new CalcDef(14, "WtdPrincipalMaturityDays", "%d", 1, 'N', "int", "W", "N", "DayCounting", "N", 1, "Principal weighted average length in years.", "PortPrincipalMaturity(Days)"));
        calcDefs.add(15, new CalcDef(15, "Version", "%s", 1, 'N', "String", "W", "N", "Version", "N", 1, "The version of the product you are running.", "Version"));
        calcDefs.add(16, new CalcDef(16, "Parameters", "%d", 1, 'N', "String", "N", "N", "Documentation", "N", 1, "Writes out the list and definitions of paramater inputs.", "Parameters"));
        calcDefs.add(17, new CalcDef(17, "Calculations", "%d", 1, 'N', "String", "N", "N", "Documentation", "N", 1, "Writes out the list of calculation results available.", "Calculations"));
        calcDefs.add(18, new CalcDef(18, "Daycount", "%d", 1, 'N', "int", "N", "N", "DayCounting", "N", 1, "Calculates the number of days between two dates using any daycount method.", "PortDaycount"));
        calcDefs.add(19, new CalcDef(19, "PaySchedule", "%d", 1, 'N', "int", "N", "N", "Payments", "N", 1, "Calculates the accrual and payment schedule along with day count, day of week and holiday indicator.", "PortPayment"));
        calcDefs.add(20, new CalcDef(20, "KeyRateDuration", "%.3f", 1, 'N', "int", "N", "N", "KeyRateDuration", "N", 1, "Calculates the key rate durations using a yield curve.", "PortKeyRateDuration"));
        calcDefs.add(21, new CalcDef(21, "BookValue", "%,.2f", 1, 'Y', "double", "W", "N", "Accounting", "N", 1, "Calculates the bookvalue at any date and the cumulative accretion amortization since purchase.", "PortBookValue"));
        calcDefs.add(22, new CalcDef(22, "AccreAmort", "%,.2f", 1, 'Y', "double", "S", "N", "Accounting", "N", 1, "Calculates the accrection/amortization between any two dates and the bookvalue at any start and end date.", "PortAccreAmort"));
        calcDefs.add(23, new CalcDef(23, "AccreAmortSchedule", "%,.2f", 1, 'Y', "double", "N", "N", "Accounting", "Y", 7, "Calculates the accretion/amortization schedule between any two dates.", "PortAccreAmortSchedule"));
        calcDefs.add(24, new CalcDef(24, "Z-Spread", "%.4f", 100, 'N', "double", "W", "N", "Analytic", "N", 1, "Calculates the yield spread over the risk free yield curve given the bond price.", "PortZSpread"));
        calcDefs.add(25, new CalcDef(25, "Choices", "%d", 1, 'N', "String", "W", "N", "Documentation", "N", 1, "Writes out the list of choices for each input parameter.", "Choices"));
        calcDefs.add(26, new CalcDef(26, "PrincipalPayment", "%.2f", 1, 'Y', "double", "S", "N", "Trading", "N", 1, "Calculates the principal payment for a trade", "TPortPrincipalPayment"));
        calcDefs.add(27, new CalcDef(27, "NetPayment", "%.2f", 1, 'Y', "double", "S", "N", "Trading", "N", 1, "Calculates the net or total payment for a trade.", "PortNetPayment"));
        calcDefs.add(28, new CalcDef(28, "FutureValue", "%.2f", 1, 'Y', "double", "S", "N", "Forecasting", "N", 1, "Calculates the future value of the cashflows from future date to maturity date", "PortFutureValue"));
        calcDefs.add(29, new CalcDef(29, "Gain/Loss", "%.2f", 1, 'Y', "double", "S", "Y", "Accounting", "N", 1, "Calculates the gain/loss on any given  and date using GAAP standards.", "PortGainLoss"));
        calcDefs.add(30, new CalcDef(30, "MaturityCash", "%.2f", 1, 'Y', "double", "S", "Y", "CASHFLOW", "N", 1, "Calculates the cash paid on the maturity date of the bond.", "TPortMaturityCash"));
        calcDefs.add(31, new CalcDef(31, "HorizonAnalysis", "%.2f", 1, 'Y', "double", "W", "Y", "Modelling", "Y", 7, "Calculates the risk, yield and cash generation power at specfied future horizon dates.", "HorizonResults"));
        calcDefs.add(32, new CalcDef(32, "RealYield", "%.4f", 1, 'N', "double", "W", "Y", "Modelling", "Y", 1, "Calculates the real yield to maturity using any set of reinvestment rates.", "PortRealYield"));
        calcDefs.add(33, new CalcDef(33, "FairValue", "%.8f", 100, 'N', "double", "W", "Y", "Modelling", "Y", 1, "Calculates the fair price of a bond based on the current yield curve.", "PortFairPrice"));
        calcDefs.add(34, new CalcDef(34, "ForwardCurve", "%.4f", 100, 'N', "double", "W", "Y", "Modelling", "Y", 1, "Calculates the forward yield curve from a current spot curve.", "ForwardCurve"));
        calcDefs.add(35, new CalcDef(35, "MarketValue", "%.2f", 1, 'Y', "double", "S", "N", "Modelling", "N", 1, "Calculates the estimated market value and market price at any future date.", "PortMarketValue"));
        calcDefs.add(36, new CalcDef(36, "SpotCurve", "%.4f", 100, 'N', "double", "W", "Y", "Modelling", "Y", 1, "Calculates the spot yield curve from a par curve.", "ForwardCurve"));
        calcDefs.add(37, new CalcDef(37, "FutureValueRisk", "%.4f", 1, 'Y', "double", "W", "N", "Modelling", "Y", 3, "Calculates the expected future value at any date plus the future value upside and downside variance", "PortFutureValueRisk"));
        calcDefs.add(38, new CalcDef(38, "RateOfReturnRisk", "%.4f", 1, 'Y', "double", "W", "N", "Modelling", "Y", 3, "Calculates the expected future rate of return on any date plus the upside and downside variance", "PortRateOfReturnRisk"));
        calcDefs.add(39, new CalcDef(39, "Attribution", "%.3f", 100, 'N', "double", "W", "Y", "Analytic", "Y", 5, "Calculates the makeup of the rate of return over any time period due to aging of the bond, yield curve changes and security selection.", "PortAttribution"));
        calcDefs.add(40, new CalcDef(40, "SwapGeneration", "%.3f", 100, 'N', "double", "S", "Y", "Analytic", "Y", 22, "Generates a set of swap recommendations with yield pick-up and risk at anu future dates.", "Swap Opportunities"));
        calcDefs.add(41, new CalcDef(41, "BondParams", "%s", 1, 'N', "Sring", "S", "N", "System", "N", 1, "Returns the comma seperated list of bond parameter names for use in JSON parsing", "BondParams"));
        calcDefs.add(42, new CalcDef(42, "LastInterestDate", "%s", 1, 'N', "Sring", "S", "N", "Info", "N", 1, "Returns the last interest payment date from settle date", "PortLastPayDate"));
        calcDefs.add(43, new CalcDef(43, "NextInterestDate", "%s", 1, 'N', "Sring", "S", "N", "Info", "N", 1, "Returns the next interest payment date after settle date", "PortNextDate"));
        calcDefs.add(44, new CalcDef(44, "CashflowInterval", "%.2f", 1, 'Y', "double", "S", "Y", "Cashflow", "Y", 1, "Returns the cashflow between two dates excluding the start date but including the end date", "PortIntervalCashflows"));
        calcDefs.add(45, new CalcDef(45, "SharpRatio", "%.3f", 1, 'N', "double", "W", "N", "Analytic", "N", 1, "Returns the Sharp ratio", "PortSharpRatio"));
        calcDefs.add(46, new CalcDef(46, "DateToWorst", "%s", 1, 'N', "date", "N", "N", "Analytic", "N", 1, "Returns the date resulting in the yield to worst", "PortDateWorstRatio"));
        calcDefs.add(47, new CalcDef(47, "DurationToWorst", "%.4", 1, 'N', "double", "N", "N", "Analytic", "N", 1, "Returns the duration to worst using the yield to worst and date to worst", "PortDurationWorst"));
        calcDefs.add(48, new CalcDef(48, "Cashflows", "%,.2f", 1, 'Y', "double", "S", "Y", "Cashflows", "Y", 8, "Cashflow from settle date to maturity showing cashflow moderate details.", "PortCashflows"));
        calcDefs.add(49, new CalcDef(49, "SpotCurve", "%.4f", 100, 'N', "double", "W", "Y", "YieldCurves", "Y", 1, "Retieves the spot yield curve for a given credit rating and date.", "PortSpotCurve"));
    }

    String securityDesc;
    Dates dtHorizon;
    double currentValue;
    double futureValue;
    double gainLoss;
    double horizonYield;
    double horizonDur; // horizon duration
    double cashGenRatio; //cash raised per unit cash spent
    double horizonMatLen;

    public void setCalcArrayDefinition() {
        horizonList.add(0, new CalcDef(0, "Horizon Date", "%s", 1, 'N', "String", "S", "N", "Horizon Model", "N", 1, "Date of horizon analysis", "Horizon Date"));
        horizonList.add(1, new CalcDef(1, "Security", "%s", 1, 'Y', "String", "S", "N", "Horizon Model", "N", 1, "Name of Security.", "TotalInterestIncome"));
        horizonList.add(2, new CalcDef(2, "Current Value", "%.6f", 100, 'N', "double", "W", "Y", "Yield", "N", 1, "The yield to maturity given a price and settlement date.", "WtdAvgYieldToMaturity"));
        horizonList.add(3, new CalcDef(3, "Horizon Value", "%.8f", 100, 'N', "double", "W", "N", "Price", "N", 1, "The percentage price given a yield and settlement date.", "WtdAvgPriceToMaturity"));
        horizonList.add(4, new CalcDef(4, "Gain/Loss", "%.4f", 1, 'N', "double", "W", "N", "Statistic", "N", 1, "The MaCaulay duration given a yield and settlement date.", "WtdAvgMacCaulayDuration"));
        horizonList.add(5, new CalcDef(5, "Yield to Horizon", "%.4f", 1, 'N', "double", "W", "N", "Statistic", "N", 1, "The modified duration based on the Macaulay duration as defined by the SIA 1993 publication.", "WtdAvgDurationSIA"));
        horizonList.add(6, new CalcDef(6, "Horizon Duration", "%.4f", 1, 'N', "double", "W", "N", "Statistic", "N", 1, "The modified duration defined as the percentage price change per 1% change in yield.", "WtdAvgDurationStd"));
        horizonList.add(7, new CalcDef(7, "Cash Gen Ratio", "%.4f", 1, 'N', "double", "W", "N", "Statistic", "N", 1, "The convexity defined as the reate of change to modified duration", "WtdAvgConvexity"));
        horizonList.add(8, new CalcDef(8, "Yrs to Horizon Date", "%,15.2f", 1, 'Y', "double", "S", "Y", "Cashflows", "Y", 8, "The cashflow from settle date to maturity showing date; period; total cash; total principal cash; schedule principal cash; prepayment cash and interest payment.", "TotalCashflows"));
    }

    class CalcDefElement     {
        String calcName;
        String elementName;
        int domain;
        CalcDefElement(String calcName, String elementName, int domain) {
            this.calcName = calcName;
            this.elementName = elementName;
            this.domain = domain;
        }
        private void setCalcElement(int no, String calcName, String elementName, int domain) {
            calcElements.add(no, new CalcDefElement(calcName, elementName, domain));
        }

    }
    
}



public class CalcList {

    static final int ACCUREDINT = 0;
    static final int INTERESTINCOME = 1;
    static final int YIELDTOMATURITY = 2;
    static final int PRICETOMATURIY = 3;
    static final int MACAULAYDUR = 4;
    static final int MODIFIEDDURSIA = 5;
    static final int MODIFIEDDURSTD = 6;
    static final int CONVEXITY = 7;
    static final int CASHFLOW = 8;
    static final int COUPONDAYS = 9;
    static final int YIELDWORST = 10;
    static final int MATLENYEARS = 11;
    static final int MATlENDAYS = 12;
    static final int MATLENYEARSWTD = 13;
    static final int MATLENDAYSWTD = 14;
    static final int VERSION = 15;
    static final int PARAMETERS = 16;
    static final int CALCULATIONS = 17;
    static final int DAYCOUNT = 18;
    static final int PAYSCHEDULE = 19;
    static final int KEYRATEDURATION = 20;
    static final int BOOKVALUE = 21;
    static final int ACCREAMORT = 22;
    static final int ACCREAMORTSCHED = 23;
    static final int ZSPREAD = 24;
    static final int CHOICES = 25;
    static final int PRINCIPALPAYMENT = 26;
    static final int NETPAYMENT = 27;
    static final int FUTUREVALUE = 28;
    static final int GAINLOSS = 29;
    static final int MATURITYCASH = 30;
    static final int HORIZONANALYSIS = 31;
    static final int REALYIELD = 32;
    static final int FAIRPRICE = 33;
    static final int FORWARDCURVE = 34;
    static final int FUTUREPRICE = 35;
    static final int SPOTCURVE = 36;
    static final int FUTUREVALUERISK = 37;
    static final int FUTRETURNRISK = 38;
    static final int ATTRIBUTION = 39;
    static final int SWAPANALYSIS = 40;
    static final int BONDPARAMS = 41;
    static final int LASTPAYDATE = 42;
    static final int NEXTPAYDATE = 43;
    static final int CASHFLOWINTERVAL = 44;
    static final int SHARPRATIO = 45;
    static final int DATEWORST = 46;
    static final int DURATIONWORST = 47;
    static final int CASHFLOWSHORT = 48;
    static final int SPOTCURVERETRIEVED = 49;
    // static final int BUYSELECTION = 49;
    // static final int SELLSELECTION = 50;

    static final int MAXCALCS = 50;
    static final String FORECASTFORWARD = "FORWARD";
    static final String FORECASTSPOT = "SPOT";
    static final String QUOTE = "\"";
    static final String CONSOLEJSONNEWPORTFOLIO = "{" + QUOTE + "New" + QUOTE + ":" + QUOTE + "PortfolioStatistics" + QUOTE + "},";
    static final String CONSOLESTDNEWORTFOLIO = "New=PortfolioStatistics;";

    static final String BONDDESCRIPTIONTAG = "Descr";
    static final String BONDISSUERTAG = "Issuer";
    static final String SECURITYIDTAG = "SecurityId";
    static final String PORTFOLIODESCRPTIONTAG = "Portfolio";
    static final String FILENEWSTDPORTFOLIO = "\nNew=PortfolioStatistics\n";
    static final String FILENEWCSVPORTFOLIO = "\nNew,PortfolioStatistics\n;";
    static final String CSV = "CSV";

    static final String STD = "STD";
    static final int noAvailCalculation = 36;
    static final int ALL = 9999;
    static final String BOOKHOLDATCOST = "ATCOST";

    static final int SPOTON = 0;
    static final int SPOTDN = -1;
    static final int SPOTUP = 1;

    // Calc names for portfolio calc that are arrays
    // Atrribution
    static final String attrRtnName = "AttrRtn";
    static final String attrAgingRtnName = "AttrAgingRtn";
    static final String attrRiskFreeRtnName = "AttrRiskFreeRtn";
    static final String attrCreditSprdRtnName = "AttrCreditSprdRtn";
    static final String attrSelectionRtnName = "AttrSelectionRtn";

    static final String portAttrRtnName = "PortAttrRtn";
    static final String portAttrAgingRtnName = "PortAttrAgingRtn";
    static final String portAttrRiskFreeRtnName = "PortAttrRiskFreeRtn";
    static  String portAttrCreditSprdRtnName = "PortAttrCreditSprdRtn";
    static final String portAttrSelectionRtnName = "PortAttrSelectionRtn";

    // future calcs
    static final String futValName = "FutVal";
    static final String futValDnName = "FutValDnSide";
    static final String futValUpName = "FutValUpSide";

    static final String portFutValName = "PortFutVal";
    static final String portFutValDnName = "PortFutValDnSide";
    static final String portFutValUpName = "PortFutValUpSide";


    static final String portFutRtnName = "PortFutRtn";
    static final String portFutRtnDnName = "PortFutRtnDnSide";
    static final String portFutRtnUpName = "PortFutRtnUpSide";

    static final String futRtnName = "FutRtn";
    static final String futRtnDnName = "FutRtnDnSide";
    static final String futRtnUpName = "FutRtnUpSide";



}

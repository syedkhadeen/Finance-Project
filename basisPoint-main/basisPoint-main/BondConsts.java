
public class BondConsts {

    final static String  VERSION = "1.3.25"; // 20241228";
    final static int PSAMONTHCONST = 30;         /* month at which psa rate is constant */
    final static double PSASTDSMM = 0.002;         /* PSA annualized monthly prepay rate acceleration */    final static double PSADEFAULTCPR = 0.06;          /* PSA default cpr rate */
    final static int NOSCALE = 1;
    final static double REDEEMPAYOFF = .0000001;
    // Schedule constanst
    final static String AFTERHOLIDAY = "+";
    final static String BEFOREHOLIDAY = "-";
    final static String AFTERHOLIDAYSAMEMONTH = "+-";
    final static String IGNOREHOLIDAYS = "0";

    final static int BEFOREDATE = -2;
    final static int BEFOREONDATE = -1;
    final static int ONDATE = 0;
    final static int AFTERONDATE = 1;
    final static int AFTERDATE = 2;
    // time units
    final static String MONTHTIMEUNIT = "M";
    final static String WEEKTIMEUNIT = "W";
    final static String DAYTIMEUNIT = "D";

    final static String ENUMERATEDTIMEUNIT = "E";

    final static String NOPERIODICITY = "N"; // for single payment at maturity
    // defaults choices
    final static String AMERICANCALL = "AM";
    final static String EUROPEANCALL = "EUR";
    final static String USECALLPRICES = "PRICE";

    // default values
    final static int CALLNOTIFICATIONDAYS = 30;
    final static String DTFORMAT = "MM/DD/YYYY";
    final static String CUBICSPLINE = "CUBIC";
    final static int DUMPCOUNT = 100;

    // types of schedule rules
    final static String ACCRUERULE = "ACCRUE";
    final static String INTERESTRULE = "INTEREST";
    final static String RESETRULE = "RESET";
    final static String PAYLEGRULE = "PAYLEG";
    final static String RECLEGRULE = "RECLEG";
    final static String INDEXRULE = "INDEX";
    final static String INDEXRATIORULE = "INDEXRATIO";
    final static String INDEXASSUMERULE = "INDEXASSUME";
    final static String paylegResetRULE = "paylegReset";
    final static String RESETRECLEGRULE = "RESETRECLEG";

    final static String TAXSCHEDRULE = "TAXPAYMENT";

    final static String DISCCURVERULE = "DISCCURVE"; // spot curve
    final static int ALLCALCS = 9999;


    // schedule rule choices
    final static String COMPOUNDFREQANNUAL = "ANNUAL"; // annual
    final static String COMPOUNDFREQSEMIANNUAL = "SEMI"; // semi annual
    final static String COMPOUNDFREQTRIANNUAL = "TRI"; // tri annaul
    final static String COMPOUNDFREQQUARTERLY = "QUARTERLY"; // quarterly
    final static String COMPOUNDFREQBIMONTHLY = "BIMONTHLY"; // bi monthly
    final static String COMPOUNDFREQMONTHLY = "MONTHLY"; // monthly

    // for strip choices
    final static String STRIPNONE = "NS";
    final static String STRIPIO = "IO";
    final static String STRIPPO = "PO";

    final static int SPOTINTERVAl = 6; // every 6 months
    final static String RISKFREECURVE = "RF";
    final static String SPRDCURVE = "SPRD";
    final static String FUTCURVE = "FUT";
    final static String TOTALCURVE = "TOTAL";
    final static int INDEXISRAELONAFTERDAY = 17;

    final static String JSON = "J"; // JSON output
    final static String NONE = "N"; // No string output
    final static String KEYVALUE = "S"; // String or STD output using Key-value separator as


    static final String CONSTCASH = "ConstCash";
    static final String CONSTACCRUAL = "ConstAccrual";

    // yield curve
    static final String SPOTCURVE = "S";
    static final String FORWARDCURVE = "F";
    static final String PARCURVE = "P";
    static final String FIXINGCURVE = "FIXINGCURVE";
    static final String LINEARINTERP = "LINEAR";
    static final String CUBICSLPINEINTERP = "CUBICSPLINE";



    static int MAXTENORCUTOFF = 61; // forward variances will only use spots going back MAXTENORMONTHS



    // indexed bond interpolation methods
    static final String INDEXINTERPBBG = "INDEXINTERPBBG";
    static final String INDEXINTERPNONE = "INDEXINTERPNONE";
    static final String INDEXINTERPLINEAR = "INDEXINTERPLINEAR";
    static final String INDEXINTERPJAPAN = "INDEXINTERPJAPAN";
    static final String INDEXINTERPISRAEL = "INDEXINTERPISRAEL";
    static final String INDEXINTERPBRAZIL = "INDEXINTERPBRAZIL";
    static final String INDEXINTERPAUS = "INDEXINTERPAUS";
    static final String INDEXPROJECTGROWTH = "INDEXGROWTH";


    static final String BUY = "B"; // a buy swap in swapped portfolip
    static final String SELL = "S"; // a postion sold as part of a swap
    static final String HOLD = "H"; // a position held as part of a swap;
    static final int DAYSLONGSHORT = 365;
    static final int NOMATURITYTERMS = 17;

    static final String COMMENT = "COMMENT";
    static final String SPOTCURVEFILENAME = "spotCurve.csv";
    static final String SPOTCURVEVOLFILENAME = "spotCurveVols.csv";
    static final String FORWARDCURVEFILENAME = "forwardCurveVols.csv";
    static final String SWAPTRADEFILENAME = "swapList.csv";
    static final String SWAPPORTFOLIOFILENAME = "swapPortfolio.csv";

    static final String SEMIEQV = "SEMIEQV";
    static final int BUYMINPAR = 1000;
    static final String REMOVESECURITY = "R";






}

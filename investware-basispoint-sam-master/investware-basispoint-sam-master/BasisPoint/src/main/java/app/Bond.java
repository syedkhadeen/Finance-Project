package app;

import java.io.File;
import java.util.*;

public class Bond {
    Holidays holidays;
    SecurityTypes securityTypes;
    Choices choices;
    Status status;
    SchedTracks schedTracks;
    // DayCount dayCount;
    Error error;
    CommandLine commandLine;
    DayCount dayCount;
    Time time;
    YieldCurves discCurve;
    YieldCurves discRfCurve;
    YieldCurves discSprdCurve;
    YieldCurves discFutCurve;
    YieldCurves discFutRfCurve;
    YieldCurves discFutSprdCurve;
    YieldCurves fixingCurve;
    YieldCurves paylegFixingCurve;
    YieldCurves reclegFixingCurve;
    SchedRule accrueSchedRule;
    SchedRule intSchedRule;
    SchedRule resetSchedRule;
    SchedRule idxRatioSchedRule;
    SchedRule idxSchedRule;
    SchedRule idxAssumeSchedRule;
    SchedRule paylegSchedRule;
    SchedRule reclegSchedRule;
    SchedRule paylegResetSchedRule;
    SchedRule reclegResetSchedRule;
    SchedRule taxSchedRule;
    Taxes taxes;
    char calcTotalsYn = UnAvails.NOTAVAILCHAR;
    String aaControl;
    double aaYield;
    char adjustMaturityYn;
    String basAccreAmort;
    DayCount.DCMethod dcAccreAmort;
    String basAccInt;
    DayCount.DCMethod dcAccruedInt;
    String basBondEqv;
    DayCount.DCMethod dcBondEqv;
    String basDelayDays;
    DayCount.DCMethod dcDelayDays;
    String basIdxInterp;
    DayCount.DCMethod dcIdxInterp;
    String basOdd1st;
    DayCount.DCMethod dcOdd1st;
    String basOddLast;
    DayCount.DCMethod dcOddLast;
    String basPayleg;
    String basResetPayleg;
    DayCount.DCMethod dcResetPayleg;
    DayCount.DCMethod dcPayleg;
    DayCount.DCMethod dcActAct;
    String basRecleg;
    DayCount.DCMethod dcRecleg;
    String basResetRecleg;
    DayCount.DCMethod dcResetRecleg;
    String basYldPrc;
    DayCount.DCMethod dcYldPrc;
    char accruedIntRndYn;
    char accruedIntPrinRndYn;
    char accruedIntSizeRndYn;
    String account;
    String accruedIntMeth;
    int accruedIntSize;
    int accSchedDOM;
    int accSchedDOW;
    char accSchedEOMRuleYn;
    int accSchedCompoundFreq;
    String accHolidayRule;
    int accSchedMOY;
    Dates accSchedNextCpnDt;
    Dates accSchedOccurDt;
    int accSchedOccurYear;
    Dates accSchedPrevCpnDt;
    int accSchedRepeat;
    String accTimeUnit;
    char accSchedUseNextLastYn;
    int accSchedWOM;
    char allDocCalculations;
    char allowBadParamYn;
    int allowedPassAge;
    char allowNegCpnYn;
    char allowNegPrcYldYn;
    char allowNegRtYn;
    char ausAccruedYn;
    String bondEqvYld;
    String bookCalcMeth;
    double bookPrc;
    double bookPrinFactor;
    double bookValue;
    String bookYldOpt;
    int bondEqvYldOpt;
    String canAccrued;
    char calcAfterMatDtYn;
    char calcCpnDtsYn;

    String calcDelimiter;
    char calcFactorsYn;
    char calcFloatRtsYn;
    char calcOnSettleYn;

    char calcOnlyPortStatsYn;
    int[] calcRequests;
    char calcResetDtsYn;

    char callableYn;
    int callNotifyDays;
    double[] callPrc;
    String callPrcType;
    String callType;
    char canadaAccruedYn;
    double claimPct;
    char cleanPrcYn;
    char cleanYieldYn;
    double contractSize;
    double conversionPrc;
    double conversionRatio;
    char convertibleYn;
    String convertOpt;
    char convertToLocalYn;
    String country;
    double cpnFloor;
    double cpnRtPayleg;
    String cpnQuoteType;
    double cpnRt;
    double cpnRtQuote;
    double cpnRtRecleg;
    String creditDefModel;
    String creditAgency;
    String creditRating;
    String creditAgencyRf;
    String creditRatingRf;
    String creditAgencySprd;
    String creditRatingSprd;
    String creditSpdInterpMeth;
    int[] creditSprdMatLen;
    double[] creditSprdRt;
    double cumAccreAmort;
    double cumIntIncome;
    double cumLocalCostAdjust;

    String currency;
    int currentCalcNo;
    String cusip;
    int daysCapGainShort;
    String defaultModel;
    int delaydaysAct;
    int delaydaysStated;
    char discFixedLegYn;

    String discCurveName;

    String[] discTenorName; // 1m, 2m, 1yr, ...

    double[] discRt; // spot rate at given years from start
    double[] discRtDn;
    double[] discRtUp;
    double[] discStdDev;
    int[] discTenor; // years to each yield point

    double[] discSprd;
    double[] discSprdDn;
    double[] discSprdUp;
    double[] discSprdStdDev;
    int[] discSprdTenor; // years to each yield point

    double[] discFutRt;
    double[] discFutRtDn;
    double[] discFutRtUp;
    double[] discFutStdDev;
    int[] discFutTenor;

    double[] discFutSprd;
    int[] discFutSprdTenor;
    double[] discFutSprdStdDev;
    double[] discFutSprdDn;
    double[] discFutSprdUp;

    char discRtParYn;
    char discSprdParYn;
    char discFutRtParYn;
    char discFutSprdParYn;

    Dates dt1stPay;
    Dates dtLastPay;
    Dates[] dtCall;
    Dates dtCallNotified;
    Dates dtCashEnd;
    Dates dtCashStart;
    Dates dtConvert;
    Dates[] dtAccrue;
    Dates dtFuture;
    Dates[] dtUserPays;
    Dates[] dtResets; // user enteres reset rate
    Dates dtDiscCurve; // date to access stored yield curve on dtSettle
    Dates dtDiscFutCurve; // date to access stored yield curve on dtFure
    Dates dtDated;
    Dates dtEnd;
    Dates[] dtEstPrepay;
    Dates dtExDividend;
    Dates dtFixingCurve;
    Dates[] dtFactors;
    Dates dtFinalPayment; // for mortgages is the date on which the last mortgage payment occurs and
                          // remaining balance is zero
    String dtFormat;
    Dates[] dtFxRts;
    Dates[] dtHorizon;
    Dates[] dtIdxAssume;
    Dates[] dtIdxRatio;
    Dates[] dtIdxCPI;
    Dates dtLastAccounting;
    Dates dtLastOnlyCoupon;
    Dates dtLastPayment;
    Dates dtLastTaxPayment;
    Dates dtLastUserCpn;
    Dates dtNextUserCpn;
    Dates dtMaturity;
    // Dates dtMarketValue;
    Dates[] dtMktPrcs;
    Dates dtOccur;
    Dates dtPassFactor;
    Dates dtPassFactor1;
    Dates dtPassFactor2;
    Dates[] dtPassFactors;
    Dates[] dtPaylegReset;
    Dates dtPaylegSchedPrevCpn;
    Dates dtPaylegSchedNextCpn;
    Dates dtPaylegSchedOccur;
    Dates dtPayleg1stPay;
    Dates dtPreFunded;
    Dates dtPrevCpn;
    Dates dtPur;
    Dates[] dtPut;
    Dates dtOddLast;
    Dates[] dtReclegReset;
    Dates dtReclegSchedPrevCpn;
    Dates dtReclegSchedNextCpn;
    Dates dtReclegSchedOccur;
    Dates dtRecleg1stRec;
    Dates dtRedeem;
    Dates[] dtReinvest;
    Dates[] dtResetMultipliers;
    Dates[] dtPaylegMultipliers;
    Dates[] dtReclegMultipliers;
    Dates[] dtResetSprds;
    Dates[] dtResetCaps;
    Dates[] dtResetFloors;
    Dates dtSettle;
    Dates[] dtSinks;
    Dates dtStart;
    Dates dtTaxPay;
    Dates dtWala;
    Dates dtWarm;
    int dumpCntMax;
    String dumpFileName;
    char dumpOnCmdYn;
    char dumpOnErrorYn;
    double durBpShift;
    int durationWt;
    double endPrc;
    double endYld;
    double[] estPrePay;
    int exDivOffsetDays;
    double faceAmt;
    char fedTaxExemptYn;
    String fileInputName;
    String fileOutputName;
    double fiscalYTDAALocal;
    double fiscalYTDCostAdjLocal;
    double fiscalYTDIncomeLocal;
    String fixingCurveName;
    int[] fixingTenor;
    String[] fixingMatName;
    char fixingParYn;
    char discCurveYn;
    char fileOutYn;
    String fileOutName;
    String fileOutFormat;
    char fixingCurveYn;
    double[] fixingRt;
    double[] fixingAvgRt;
    double[] fixingMin;
    double[] fixingMax;
    String fixingBenchmark;
    int floaterTerm;
    char forceYldYn;
    String fundName;
    double[] fxRts;
    double[] fixingRtDn;
    double[] fixingRtUp;
    double[] fixingStdDev;
    double futurePrc;
    String horizonFreq;
    String horizonTimeUnit;
    int horizonRepeatFactor;
    int horizonPeriods;
    String hostSecId;
    char idxAdjMatYn;
    double[] idxAssume;
    double[] idxCPI;
    char idxCPIRndYn;
    int idxDelayDays;
    int idxDelayMonths;
    int idxDelayWeeks;
    String idxInterpMeth;
    double idxCPIRatio;
    double idxCPIAtIssue;
    double idxCPIAtSettle;
    char idxRatioRndYn;
    int idxSchedDOM;
    int idxSchedDOW;
    char idxSchedEOMRuleYn;
    int idxSchedCompoundFreq;
    String idxHolidayRule;
    int idxSchedMOY;
    Dates idxSchedNextCpnDt;
    Dates idxSchedOccurDt;
    int idxSchedOccurYear;
    Dates idxSchedPrevCpnDt;
    int idxSchedRepeat;
    String idxTimeUnit;
    char idxSchedUseNextLastYn;
    int idxSchedWOM;
    int idxRatioSchedDOM;
    int idxRatioSchedDOW;
    String idxRatioInterpMeth;
    char idxRatioSchedEOMRuleYn;
    int idxRatioSchedCompoundFreq;
    String idxRatioHolidayRule;
    int idxRatioSchedMOY;
    Dates idxRatioSchedNextCpnDt;
    Dates idxRatioSchedOccurDt;
    int idxRatioSchedOccurYear;
    Dates idxRatioSchedPrevCpnDt;
    int idxRatioSchedRepeat;
    String idxRatioTimeUnit;
    char idxRatioSchedUseNextLastYn;
    int idxRatioSchedWOM;
    char idxValueRoundYn;

    String idxAssumeHolidayRule;
    char incldEndDtBookValYn;
    char incldEndDtIncomeYn;
    char incldSettDtCashYn;
    char incldSettDtIntRecYn;
    double incomeControlMargin;
    double inflationRt;
    String intPayMeth;
    String instype; // string form of instrument type
    int insTypeNo; // integer form of instrument type
    int intSchedDOM;
    int intSchedDOW;
    char intSchedEOMYn;
    int intSchedCompoundFreq;
    String intHolidayRule;

    int intSchedMOY;
    Dates intSchedNextCpnDt;
    Dates[] intdtUserPays;
    Dates intSchedOccurDt;
    int intSchedOccurYear;
    Dates intSchedPrevCpnDt;
    int intSchedRepeat;
    String intSchedTimeUnit;
    char intSchedUseNextLastYn;
    int intSchedWOM;
    char isCashYn;
    char isAtmaturityYn;
    char isExdividendYn;
    char isFixedrateYn;
    char isFloaterYn;
    char isForceYieldYn;
    char isInflationProtectedYn;
    char isMortgageYn;
    char isPutableYn;
    char isSinkableYn;
    char isSteppedYn;
    char isZeroCouponYn;
    String isin;
    double issuedFace;
    double issuePrc;
    String issuer;
    double issueYield;
    char levelPayFromPassDtYn;
    char longOdd1StYn;
    char longOddLastYn;
    double matFloor;
    String matLenOpt;
    int matTerm;
    double[] mktPrcs;
    double mktPrcFuture;
    double mktPrcStart;
    double mktYld;
    String nameDelimiter;
    char oddnessPrcYn;
    Dates[] parBondDtMaturity;
    String[] parBondPeriodicity;
    double passFactor;
    double passFactor1;
    double passFactor2;
    double[] passFactors; // passfact sequest to distinuish betwee passfact singule
    String passModel;
    String passProjectMeth;

    double passPayoff;
    int passWala;
    int passWarm;

    String paylegPeriodicity;
    double paylegCpnrt;
    char paylegFixedYn;
    double[] paylegResetRt;
    double[] paylegMultiplier;
    double[] paylegSprd;
    double[] paylegCap;
    double[] paylegFloor;
    double paylegLifeCap;
    double paylegLifeFloor;
    int paylegSchedDOM;
    int paylegSchedDOW;
    char paylegSchedEOMRuleYn;
    int paylegSchedCompoundFreq;
    String paylegHolidayRule;
    int paylegSchedMOY;
    int paylegSchedOccurYear;
    int paylegSchedRepeat;
    String paylegTimeUnit;
    char paylegSchedUseNextLastYn;
    int paylegSchedWOM;

    double[] pctToggleCash;
    char pikableYn;
    double[] pikRt;
    String portName;
    char prcAdjustedYn;
    double preFundedPrc;
    double prepaySpeed;
    double[] estPrePaySpeed;
    char afterTaxCalcsYn;
    double purPrc;
    int purWala;
    int purWarm;
    double purYld;
    char putableYn;
    int putNotifyDays;
    double[] putPrc;
    String putType;
    String putPrcType;

    String reclegPeriodicity;
    double[] reclegResetRt;
    double[] reclegMultiplier;
    double[] reclegSprd;
    double[] reclegCap;
    double[] reclegFloor;
    double reclegLifeCap;
    double reclegLifeFloor;
    char reclegFixedYn;
    int reclegSchedDOM;
    int reclegSchedDOW;
    char reclegSchedEOMRuleYn;
    int reclegSchedCompoundFreq;
    String reclegHolidayRule;
    int reclegSchedMOY;
    int reclegSchedOccurYear;
    int reclegSchedRepeat;
    int reclegSchedRulePeriodicity;
    String reclegTimeUnit;
    char reclegSchedUseNextLastYn;
    int reclegSchedWOM;
    double recoveryRt;
    double[] redeemAmt;
    double[] redeemFactor;
    double redeemPayoff;
    double[] redeemPct;
    double redeemPrin;
    double redeemInt;

    double[] reinMatlen;
    // String requestStr;
    char resetFixedYn;
    double[] resetMatlens;
    double[] reinRt;
    String resetBenchmark;
    String paylegBenchmark;
    String paylegResetPeriodicity;
    int paylegResetSchedDOW;
    int paylegResetSchedDOM;
    char paylegResetSchedEOMRuleYn;
    int paylegResetSchedCompoundFreq;
    String paylegResetHolidayRule;
    int paylegResetSchedMOY;
    Dates paylegResetSchedNextCpnDt;
    Dates paylegResetSchedOccurDt;
    int paylegResetSchedOccurYear;
    Dates paylegResetSchedPrevCpnDt;
    int paylegResetSchedRepeat;
    String paylegResetTimeUnit;
    char paylegResetSchedUseNextLastYn;
    int paylegResetSchedWOM;
    String resetPeriodicity;
    double[] resetRts;
    double resetLifeCap;
    double resetLifeFloor;
    double[] resetMultiplier;
    double[] resetSprd;
    double resetAvgRt;
    double[] resetCaps;
    double[] resetFloors;
    String reclegResetPeriodicity;
    String reclegBenchmark;
    int reclegResetSchedDOM;
    int reclegResetSchedDOW;
    char reclegResetSchedEOMRuleYn;
    int reclegResetSchedCompoundFreq;
    String reclegResetHolidayRule;
    int reclegResetSchedMOY;
    Dates reclegResetSchedNextCpnDt;
    Dates reclegResetSchedOccurDt;
    int reclegResetSchedOccurYear;
    Dates reclegResetSchedPrevCpnDt;
    int reclegResetSchedRepeat;
    String reclegResetTimeUnit;
    char reclegResetSchedUseNextLastYn;
    int reclegResetSchedWOM;

    int resetSchedDOM;
    int resetSchedDOW;
    char resetSchedEOMRuleYn;
    int resetSchedCompoundFreq;
    String resetHolidayRule;
    int resetSchedMOY;
    Dates dtResetSchedNextCpn;
    Dates dtResetSchedOccur;
    int resetSchedOccurYear;
    Dates dtResetSchedPrevCpn;
    int resetSchedRepeat;
    String resetTimeUnit;
    char resetSchedUseNextLastYn;
    int resetSchedWOM;
    String riskReturnGoal;
    String rootDirectory;
    int returnWt;
    String secDescr;
    String securityId;
    String securityType;
    double serviceRt;
    double settleCash;
    double settlePrc;
    double settleYld;
    int sfigAccrual; // accounting calc
    int sfigAccrued; // accrued interest calc
    int sfigAccruedSize; // accrued adjustment
    int sfigAccruedDaycount; // accrued adjustment
    int sfigAccruedPrin; // accrued adjustment
    int sfigCashflow;
    int sfigCurrency;
    int sfigCPI;
    int sfigIdxValue;
    int sfigIdxRatio;
    int sfigPrc;
    int sfigStatistic;
    int sfigYield;
    char showIndivCalcYn;
    char showRecapYn;
    char sinkableYn;
    double[] sinkAmts;
    double sinkFactor;
    double[] sinkPcts;
    double[] sinkPrcs;
    int spotDiscInterval; // in months
    String spotDiscIntervalName;
    int spotFixingInterval; // in months
    String spotFixingIntervalName;
    String stateOfIssue;
    String stateOfResidence;
    char stateTaxExemptYn;

    double startYield;
    String stripType;
    String swapPayType;
    double swapPctGain;
    String swapSide;
    char taxAdjAllowedYn;
    String taxLotId;
    double taxRtFedCapGainLong;
    double taxRtFedCapGainShort;
    double taxRtFedIncome;
    double taxRtStateCapGainLong;
    double taxRtStateCapGainShort;
    double taxRtStateIncome;
    String tradeType;
    char useCreditSprdYn;
    char useCurrentRedemptionYn;
    char useDefaultSfigYn;
    char useFixingCurveYn;
    char useForwardCurveYn;
    char useLocalCurrencyYn;
    String reinVestMethod;
    Dates[] userCashDt;
    double[] userCashflow;
    char validateYn;
    char warningsOffYn;
    int weekend1;
    int weekend2;
    char whenIssueYn;
    String yieldCurveInterpMeth;

    String yieldForeCastMeth;

    String yieldQuoteType;

    //
    int noRequests;
    int noCallDts;
    int noCallPrcs;
    int noCaps;
    int noUserPayDts;
    int noCalcRequests;
    int noBondParams;
    int noAccrueDts;
    int noIntdtUserPays;
    int noParBondCpnRts;
    int noParBondMaturityDates;
    int noParBondPeriodicity;
    int noResetDts;
    int noPaylegDts;
    int noReclegDts;
    int noPaylegResetDts;
    int noPaylegResetRts;
    int noIdxAssumeDts;
    int noResetFloors;
    int noCreditSprdMatlens;
    int noCreditSprdRates;
    int noDtAccrue;
    int noDiscMats; // names: 2m, 1y ...

    int noDiscRate;
    int noDiscRtUp;
    int noDiscRtDn;
    int noDiscStdDev;
    int noDiscTenor; // 1, 2 , 6

    int noDiscSprd;
    int noDiscSprdTenor;
    int noDiscSprdStdDev;
    int noDiscSprdDn;
    int noDiscSprdUp;

    int noDiscFutRate;
    int noDiscFutRtUp;
    int noDiscFutRtDn;
    int noDiscFutStdDev;
    int noDiscFutTenor; // 1, 2 , 6

    int noDiscFutSprd;
    int noDiscFutSprdTenor;
    int noDiscFutSprdStdDev;
    int noDiscFutSprdUp;
    int noDiscFutSprdDn;

    int noFixingRate;
    int noFixingTenor;
    int noFixingRtDn;
    int noFixingRtUp;
    int noFixingStdDev;

    // int nodtUserPays;
    int noDtRates;
    int noDtEstPrePay;
    int noEstPrePay;
    int noFactorDts;
    int noFactors;
    int noFixingAvgRt;
    int noFixingMin;
    int noFixingMax;
    int noFxDts;
    int noFxRts;
    int noMktPrcDts;
    int noMktPrcs;
    int noIdxAssumes;
    int nodtIdxAssume;
    int noIdxRatios;
    int noIdxRatioDts;
    int noIdxCPIs;
    int noIdxCPIDts;
    int noIntPays; // # of interest payments from settle
    int noPassFactors;
    int noPassFactorDts;
    int noPaylegResets;
    int noResetSprds;
    int noPikDts;
    int noPikRts;
    int noPutDts;
    int noPutPrcs;
    int noReclegResets;
    int noReclegResetDts;
    int noReclegResetRts;
    int noRedeemAmts;

    int noPaylegCaps;
    int noPaylegMultipliers;
    int noPaylegMultiplierDts;
    int noPaylegSprds;
    int noPaylegFloors;

    int noReclegCaps = 0;
    int noReclegMultipliers = 0;
    int noReclegSprds = 0;
    int noReclegFloors = 0;

    int noReinMatlens;
    int noReinRts;
    int noRedeemPcts;
    int noRedeemRts;
    int noRedeemDts;
    int noRedeemFactors;
    int noResetMultipliers;
    int noReclegMultiplierDts;

    int noResetMultiplierDts;
    int noResetCaps;
    int noResetRts;

    int noResetMatlens;

    int cumSecurityStrnoSecurities;
    int noSinkDts;
    int noSinkAmts;
    int noSinkPrcs;
    int noSinkPcts;
    // int noSpotDiscMats;

    int noTaxDates;
    int noTogglePcts;
    int noToggleDts;
    int noUserCashDts;
    int noUserCash;

    Bond(CommandLine commandLine, Error error, Holidays holidays, SecurityTypes securityTypes, Choices choices,
            Utility utility, DayCount dayCount, Time time) {
        this.error = error;
        this.holidays = holidays;
        this.securityTypes = securityTypes;
        this.dayCount = dayCount;
        this.time = time;
        this.commandLine = commandLine;
        this.choices = choices;
        this.status = new Status(this);

    }

    public void createScheduleRules(Holidays holidays, Error error, DayCount dayCount, Time time) {
        accrueSchedRule = new SchedRule(holidays, error, dayCount, time);
        intSchedRule = new SchedRule(holidays, error, dayCount, time);
        idxSchedRule = new SchedRule(holidays, error, dayCount, time);
        idxRatioSchedRule = new SchedRule(holidays, error, dayCount, time);
        resetSchedRule = new SchedRule(holidays, error, dayCount, time);
        reclegSchedRule = new SchedRule(holidays, error, dayCount, time);
        paylegSchedRule = new SchedRule(holidays, error, dayCount, time);
        reclegResetSchedRule = new SchedRule(holidays, error, dayCount, time);
        paylegResetSchedRule = new SchedRule(holidays, error, dayCount, time);
        taxSchedRule = new SchedRule(holidays, error, dayCount, time);
        return;
    }

    public void clearBond() {
        clearParams();
        clearSchedRules();
        clearYieldCurves();
        clearSchedTracks();
        taxes.setUnAvail();

    }

    public void clearSchedRules() {
        accrueSchedRule.clear();
        intSchedRule.clear();
        idxSchedRule.clear();
        idxRatioSchedRule.clear();
        resetSchedRule.clear();
        reclegSchedRule.clear();
        paylegSchedRule.clear();
        reclegResetSchedRule.clear();
        paylegResetSchedRule.clear();
        taxSchedRule.clear();

    }

    public void clearSchedTracks() {
        schedTracks.noSchedTracks = 0;
        schedTracks.schedTrackList.clear();
        schedTracks.currentTrackNo = 0;
    }

    public void createYieldCurves(CommandLine cmdLine, Error error, DayCount dayCount, Utility utility) {
        this.discRfCurve = new YieldCurves(cmdLine, error, dayCount, utility);
        this.discSprdCurve = new YieldCurves(cmdLine, error, dayCount, utility);
        this.discFutRfCurve = new YieldCurves(cmdLine, error, dayCount, utility);
        this.discFutSprdCurve = new YieldCurves(cmdLine, error, dayCount, utility);
        this.discCurve = new YieldCurves(cmdLine, error, dayCount, utility);
        this.discFutCurve = new YieldCurves(cmdLine, error, dayCount, utility);
        this.fixingCurve = new YieldCurves(cmdLine, error, dayCount, utility);
    }

    public void clearYieldCurves() {
        discRfCurve.clearYieldCurve();
        discSprdCurve.clearYieldCurve();
        discFutRfCurve.clearYieldCurve();
        discFutSprdCurve.clearYieldCurve();
        discCurve.clearYieldCurve();
        discFutCurve.clearYieldCurve();
        fixingCurve.clearYieldCurve();
    }

    public boolean clearParams() {
        error.clearError();
        this.aaControl = UnAvails.NOTAVAILSTRING;
        this.aaYield = UnAvails.NOTAVAILCOUPON;
        this.adjustMaturityYn = UnAvails.NOTAVAILCHAR;
        this.allDocCalculations = 'N';
        this.basAccreAmort = UnAvails.NOTAVAILSTRING;
        this.basAccInt = UnAvails.NOTAVAILSTRING;
        this.basBondEqv = UnAvails.NOTAVAILSTRING;
        this.basDelayDays = UnAvails.NOTAVAILSTRING;
        this.basIdxInterp = UnAvails.NOTAVAILSTRING;
        this.basOdd1st = UnAvails.NOTAVAILSTRING;
        this.basOddLast = UnAvails.NOTAVAILSTRING;
        this.basPayleg = UnAvails.NOTAVAILSTRING;
        this.basRecleg = UnAvails.NOTAVAILSTRING;
        this.basResetPayleg = UnAvails.NOTAVAILSTRING;
        this.basResetPayleg = UnAvails.NOTAVAILSTRING;
        this.basYldPrc = UnAvails.NOTAVAILSTRING;
        this.accruedIntRndYn = UnAvails.NOTAVAILCHAR;
        this.account = UnAvails.NOTAVAILSTRING;
        this.accruedIntMeth = UnAvails.NOTAVAILSTRING;
        this.accruedIntSize = UnAvails.NOTAVAILINT;
        this.accSchedDOM = UnAvails.NOTAVAILINT;
        this.accSchedDOW = UnAvails.NOTAVAILINT;
        this.accSchedEOMRuleYn = UnAvails.NOTAVAILCHAR;
        this.accSchedCompoundFreq = UnAvails.NOTAVAILINT;
        this.accHolidayRule = UnAvails.NOTAVAILSTRING;
        this.accSchedMOY = UnAvails.NOTAVAILINT;
        this.accSchedNextCpnDt = new Dates(this.error);
        this.accSchedOccurDt = new Dates(this.error);
        this.accSchedOccurYear = UnAvails.NOTAVAILINT;
        this.accSchedPrevCpnDt = new Dates(this.error);
        this.accSchedRepeat = UnAvails.NOTAVAILINT;
        this.accTimeUnit = UnAvails.NOTAVAILSTRING;
        this.accSchedUseNextLastYn = UnAvails.NOTAVAILCHAR;
        this.accSchedWOM = UnAvails.NOTAVAILINT;
        this.allowBadParamYn = 'N';
        this.allowedPassAge = UnAvails.NOTAVAILINT;
        this.allowNegCpnYn = 'Y';
        this.allowNegPrcYldYn = 'Y';
        this.allowNegRtYn = 'Y';
        this.ausAccruedYn = 'Y';
        this.bondEqvYld = UnAvails.NOTAVAILSTRING;
        this.bookCalcMeth = UnAvails.NOTAVAILSTRING;
        this.bookPrc = UnAvails.NOTAVAILDOUBLE;
        this.bookPrinFactor = UnAvails.NOTAVAILDOUBLE;
        this.bookValue = UnAvails.NOTAVAILDOUBLE;
        this.bookYldOpt = UnAvails.NOTAVAILSTRING;
        this.bondEqvYldOpt = UnAvails.NOTAVAILINT;
        this.canadaAccruedYn = UnAvails.NOTAVAILCHAR;
        this.calcAfterMatDtYn = 'N';
        this.calcCpnDtsYn = 'Y';
        this.calcFactorsYn = UnAvails.NOTAVAILCHAR;
        this.calcFloatRtsYn = UnAvails.NOTAVAILCHAR;
        this.calcOnSettleYn = 'Y';
        this.calcRequests = new int[Limits.MAXCALCS];
        this.calcResetDtsYn = UnAvails.NOTAVAILCHAR;
        // this.calcMethod.calcResults = new calcMethod.calcResults(commandLine.rootDir,
        // this);
        this.callableYn = UnAvails.NOTAVAILCHAR;
        this.callNotifyDays = BondConsts.CALLNOTIFICATIONDAYS;
        this.callPrc = new double[Limits.MAXCALLS];
        this.callPrcType = UnAvails.NOTAVAILSTRING;
        this.calcDelimiter = UnAvails.NOTAVAILSTRING;
        this.calcOnlyPortStatsYn = UnAvails.NOTAVAILCHAR;
        this.callType = UnAvails.NOTAVAILSTRING;
        this.canAccrued = UnAvails.NOTAVAILSTRING;
        this.claimPct = UnAvails.NOTAVAILDOUBLE;
        this.cleanPrcYn = 'Y';
        this.cleanYieldYn = 'Y';
        this.contractSize = UnAvails.NOTAVAILINT;
        this.conversionPrc = UnAvails.NOTAVAILDOUBLE;
        this.conversionRatio = UnAvails.NOTAVAILDOUBLE;
        this.convertibleYn = 'N';
        this.convertOpt = UnAvails.NOTAVAILSTRING;
        this.convertToLocalYn = 'N';
        this.country = UnAvails.NOTAVAILSTRING;
        this.cpnFloor = UnAvails.NOTAVAILDOUBLE;
        this.cpnRtPayleg = UnAvails.NOTAVAILDOUBLE;
        this.cpnQuoteType = UnAvails.NOTAVAILSTRING;
        this.cpnRt = UnAvails.NOTAVAILDOUBLE;
        this.cpnRtQuote = UnAvails.NOTAVAILCOUPON;
        this.cpnRtRecleg = UnAvails.NOTAVAILCOUPON;
        this.creditDefModel = UnAvails.NOTAVAILSTRING;
        this.creditAgency = UnAvails.NOTAVAILSTRING;
        this.creditRating = UnAvails.NOTAVAILSTRING;
        this.creditAgencyRf = UnAvails.NOTAVAILSTRING;
        this.creditRatingRf = UnAvails.NOTAVAILSTRING;
        this.creditAgencySprd = UnAvails.NOTAVAILSTRING;
        this.creditRatingSprd = UnAvails.NOTAVAILSTRING;
        this.creditSpdInterpMeth = BondConsts.CUBICSPLINE;
        this.creditSprdMatLen = new int[Limits.MAXYIELDPOINTS];
        this.creditSprdRt = new double[Limits.MAXYIELDPOINTS];
        this.cumAccreAmort = UnAvails.NOTAVAILDOUBLE;
        this.cumIntIncome = UnAvails.NOTAVAILDOUBLE;
        this.cumLocalCostAdjust = UnAvails.NOTAVAILDOUBLE;
        this.currency = UnAvails.NOTAVAILSTRING;
        this.currentCalcNo = UnAvails.NOTAVAILINT;
        this.cusip = UnAvails.NOTAVAILSTRING;
        this.daysCapGainShort = UnAvails.NOTAVAILINT;
        this.dcAccreAmort = DayCount.DCMethod.DC_NA;
        this.dcAccruedInt = DayCount.DCMethod.DC_NA;
        this.dcBondEqv = DayCount.DCMethod.DC_NA;
        this.dcDelayDays = DayCount.DCMethod.DC_NA;
        this.dcIdxInterp = DayCount.DCMethod.DC_NA;
        this.dcIdxInterp = DayCount.DCMethod.DC_NA;
        this.dcOdd1st = DayCount.DCMethod.DC_NA;
        this.dcOddLast = DayCount.DCMethod.DC_NA;
        this.dcPayleg = DayCount.DCMethod.DC_NA;
        this.dcRecleg = DayCount.DCMethod.DC_NA;
        this.dcYldPrc = DayCount.DCMethod.DC_NA;
        this.dcActAct = DayCount.DCMethod.DC_ACT;
        this.defaultModel = UnAvails.NOTAVAILSTRING;
        this.delaydaysAct = UnAvails.NOTAVAILINT;
        this.delaydaysStated = UnAvails.NOTAVAILINT;
        this.discCurveName = UnAvails.NOTAVAILSTRING;
        this.discFixedLegYn = UnAvails.NOTAVAILCHAR;

        this.discRt = new double[Limits.MAXYIELDPOINTS];
        this.discRtDn = new double[Limits.MAXYIELDPOINTS];
        this.discRtUp = new double[Limits.MAXYIELDPOINTS];
        this.discStdDev = new double[Limits.MAXYIELDPOINTS];
        this.discTenor = new int[Limits.MAXYIELDPOINTS];
        this.discTenorName = new String[Limits.MAXYIELDPOINTS];
        this.discSprd = new double[Limits.MAXYIELDPOINTS];
        this.discSprdDn = new double[Limits.MAXYIELDPOINTS];
        this.discSprdUp = new double[Limits.MAXYIELDPOINTS];
        this.discSprdStdDev = new double[Limits.MAXYIELDPOINTS];
        this.discSprdTenor = new int[Limits.MAXYIELDPOINTS];

        char discRtParYn;
        char discSprdParYn;
        char discFutRtParYn;
        char discFurSprdParYn;

        this.discFutRt = new double[Limits.MAXYIELDPOINTS];
        this.discFutRtDn = new double[Limits.MAXYIELDPOINTS];
        this.discFutRtUp = new double[Limits.MAXYIELDPOINTS];
        this.discFutStdDev = new double[Limits.MAXYIELDPOINTS];
        this.discFutTenor = new int[Limits.MAXYIELDPOINTS];

        this.discFutSprd = new double[Limits.MAXYIELDPOINTS];
        this.discFutSprdTenor = new int[Limits.MAXYIELDPOINTS];

        this.dt1stPay = new Dates(error);
        this.dtLastPay = new Dates(error);
        this.dtAccrue = new Dates[Limits.MAXCASHFLOWS];
        this.dtCall = new Dates[Limits.MAXCALLS];
        this.dtCallNotified = new Dates(error);
        this.dtCashEnd = new Dates(error);
        this.dtCashStart = new Dates(error);
        this.dtConvert = new Dates(error);
        this.dtDiscFutCurve = new Dates(error);
        this.dtUserPays = new Dates[Limits.MAXCASHFLOWS];
        this.dtResets = new Dates[Limits.MAXCASHFLOWS];
        this.dtResetMultipliers = new Dates[Limits.MAXRESETS];
        this.dtDated = new Dates(error);
        this.dtFinalPayment = new Dates(error);
        this.dtFixingCurve = new Dates(error);
        this.dtFuture = new Dates(error);
        this.dtWala = new Dates(error);
        this.dtWarm = new Dates(error);
        this.dtEnd = new Dates(error);
        this.dtEstPrepay = new Dates[Limits.MAXCASHFLOWS];
        this.dtExDividend = new Dates(error);
        this.dtFactors = new Dates[Limits.MAXFACTORS];
        this.dtFormat = CalUnits.DEFAULTDATEFORMAT;
        this.dtFxRts = new Dates[Limits.MAXFXRATES];
        this.dtHorizon = new Dates[Limits.MAXHORIZONPERIODS];
        this.dtIdxAssume = new Dates[Limits.MAXINDEXES];
        this.dtIdxRatio = new Dates[Limits.MAXINDEXES];
        this.dtIdxCPI = new Dates[Limits.MAXINDEXES];
        this.dtLastAccounting = new Dates(error);
        this.dtLastPayment = new Dates(error); // for mortaages will happend before legal maturoty
        this.dtLastOnlyCoupon = new Dates(error);
        this.dtLastTaxPayment = new Dates();
        this.dtMaturity = new Dates(error);
        // this.dtMarketValue = new Dates(error);
        this.dtOccur = new Dates(error);
        this.dtRedeem = new Dates(error);
        this.dtMktPrcs = new Dates[Limits.MAXMKTPRICES];
        this.dtPassFactor = new Dates(error);
        this.dtPassFactor1 = new Dates(error);
        this.dtPassFactor2 = new Dates(error);
        this.dtPassFactors = new Dates[Limits.MAXFACTORS];
        this.dtPaylegReset = new Dates[Limits.MAXRESETS];
        this.dtPreFunded = new Dates(error);
        this.dtPur = new Dates(error);
        this.dtPut = new Dates[Limits.MAXCALLS];
        this.dtResets = new Dates[Limits.MAXRESETS];
        this.dtResetCaps = new Dates[Limits.MAXRESETS];
        this.dtResetMultipliers = new Dates[Limits.MAXRESETS];
        this.dtResetFloors = new Dates[Limits.MAXRESETS];
        this.dtSettle = new Dates(error);
        this.dtReclegReset = new Dates[Limits.MAXRESETS];
        this.dtSinks = new Dates[Limits.MAXSINKS];
        this.dtStart = new Dates(error);
        this.dtDiscCurve = new Dates(error);
        this.dtFixingCurve = new Dates();
        this.dtTaxPay = new Dates(error);
        this.dumpCntMax = BondConsts.DUMPCOUNT;
        this.dumpFileName = UnAvails.NOTAVAILSTRING;
        this.dumpOnCmdYn = 'N';
        this.dumpOnErrorYn = 'N';
        this.durBpShift = UnAvails.NOTAVAILDOUBLE;
        this.durationWt = UnAvails.NOTAVAILINT;
        this.endPrc = UnAvails.NOTAVAILDOUBLE;
        this.endYld = UnAvails.NOTAVAILDOUBLE;
        this.estPrePay = new double[Limits.MAXCASHFLOWS];
        this.exDivOffsetDays = UnAvails.NOTAVAILINT;
        this.faceAmt = UnAvails.NOTAVAILDOUBLE;
        this.fedTaxExemptYn = UnAvails.NOTAVAILCHAR;
        this.fileOutYn = UnAvails.NOTAVAILCHAR;
        this.fileOutName = UnAvails.NOTAVAILSTRING;
        this.fileOutFormat = UnAvails.NOTAVAILSTRING;
        this.fiscalYTDAALocal = UnAvails.NOTAVAILDOUBLE;
        this.fiscalYTDCostAdjLocal = UnAvails.NOTAVAILDOUBLE;
        this.fiscalYTDIncomeLocal = UnAvails.NOTAVAILDOUBLE;
        this.fixingParYn = UnAvails.NOTAVAILCHAR;
        this.floaterTerm = UnAvails.NOTAVAILINT;
        this.fixingCurveName = UnAvails.NOTAVAILSTRING;
        this.fixingTenor = new int[Limits.MAXRESETS];
        this.fixingMatName = new String[Limits.MAXYIELDPOINTS];
        this.fixingRt = new double[Limits.MAXRESETS];
        this.fixingRtDn = new double[Limits.MAXYIELDPOINTS];
        this.fixingRtUp = new double[Limits.MAXYIELDPOINTS];
        this.fixingStdDev = new double[Limits.MAXYIELDPOINTS];
        this.fixingBenchmark = UnAvails.NOTAVAILSTRING;
        this.fixingAvgRt = new double[Limits.MAXYIELDPOINTS];
        this.fixingMin = new double[Limits.MAXYIELDPOINTS];
        this.fixingMax = new double[Limits.MAXYIELDPOINTS];
        this.forceYldYn = 'N';
        this.fundName = UnAvails.NOTAVAILSTRING;
        this.futurePrc = UnAvails.NOTAVAILDOUBLE;
        this.fxRts = new double[Limits.MAXFXRATES];
        this.horizonFreq = UnAvails.NOTAVAILSTRING;
        horizonRepeatFactor = UnAvails.NOTAVAILINT;
        horizonTimeUnit = UnAvails.NOTAVAILSTRING;
        int horizonPeriods = UnAvails.NOTAVAILINT;
        this.hostSecId = UnAvails.NOTAVAILSTRING;
        this.idxAdjMatYn = UnAvails.NOTAVAILCHAR;
        this.idxAssume = new double[Limits.MAXINDEXES];
        this.idxDelayMonths = UnAvails.NOTAVAILINT;
        this.idxDelayWeeks = UnAvails.NOTAVAILINT;
        this.idxDelayDays = UnAvails.NOTAVAILINT;
        this.idxInterpMeth = UnAvails.NOTAVAILSTRING;
        this.idxCPIRatio = UnAvails.NOTAVAILDOUBLE;
        this.idxCPIAtIssue = UnAvails.NOTAVAILDOUBLE;
        this.idxCPIAtSettle = UnAvails.NOTAVAILDOUBLE;
        this.idxRatioRndYn = UnAvails.NOTAVAILCHAR;
        this.idxSchedDOM = UnAvails.NOTAVAILINT;
        this.idxSchedDOW = UnAvails.NOTAVAILINT;
        this.idxSchedEOMRuleYn = UnAvails.NOTAVAILCHAR;
        this.idxSchedCompoundFreq = UnAvails.NOTAVAILINT;
        this.idxHolidayRule = UnAvails.NOTAVAILSTRING;
        this.idxSchedMOY = UnAvails.NOTAVAILINT;
        this.idxSchedNextCpnDt = new Dates(error);
        this.idxSchedOccurDt = new Dates(error);
        this.idxSchedPrevCpnDt = new Dates(error);
        this.idxSchedRepeat = UnAvails.NOTAVAILINT;
        this.idxTimeUnit = UnAvails.NOTAVAILSTRING;
        this.idxSchedUseNextLastYn = 'N';
        this.idxSchedWOM = UnAvails.NOTAVAILINT;
        this.idxRatioSchedDOM = UnAvails.NOTAVAILINT;
        this.idxRatioSchedDOW = UnAvails.NOTAVAILINT;
        this.idxRatioSchedEOMRuleYn = UnAvails.NOTAVAILCHAR;
        this.idxRatioSchedCompoundFreq = UnAvails.NOTAVAILINT;
        this.idxRatioHolidayRule = UnAvails.NOTAVAILSTRING;
        this.idxRatioSchedMOY = UnAvails.NOTAVAILINT;
        this.idxRatioSchedNextCpnDt = new Dates(error);
        this.idxRatioSchedOccurDt = new Dates(error);
        this.idxRatioSchedPrevCpnDt = new Dates(error);
        this.idxRatioSchedRepeat = UnAvails.NOTAVAILINT;
        this.idxRatioTimeUnit = UnAvails.NOTAVAILSTRING;
        this.idxRatioSchedUseNextLastYn = 'N';
        this.idxRatioSchedWOM = UnAvails.NOTAVAILINT;
        this.intPayMeth = UnAvails.NOTAVAILSTRING;
        this.intSchedTimeUnit = UnAvails.NOTAVAILSTRING;
        this.intSchedRule.repeatFactor = UnAvails.NOTAVAILINT;
        this.intSchedCompoundFreq = UnAvails.NOTAVAILINT;
        this.intSchedRepeat = UnAvails.NOTAVAILINT;
        this.instype = UnAvails.NOTAVAILSTRING;
        this.insTypeNo = UnAvails.NOTAVAILINT;
        this.isCashYn = UnAvails.NOTAVAILCHAR;
        // this.isFileOutputYn = "N";
        this.isin = UnAvails.NOTAVAILSTRING;
        this.isAtmaturityYn = UnAvails.NOTAVAILCHAR;
        this.isExdividendYn = UnAvails.NOTAVAILCHAR;
        this.isFixedrateYn = UnAvails.NOTAVAILCHAR;
        this.isFloaterYn = UnAvails.NOTAVAILCHAR;
        this.isForceYieldYn = UnAvails.NOTAVAILCHAR;
        this.isInflationProtectedYn = UnAvails.NOTAVAILCHAR;
        this.isMortgageYn = UnAvails.NOTAVAILCHAR;
        this.isPutableYn = UnAvails.NOTAVAILCHAR;
        this.isSinkableYn = UnAvails.NOTAVAILCHAR;
        this.isSteppedYn = UnAvails.NOTAVAILCHAR;
        this.issuer = UnAvails.NOTAVAILSTRING;
        this.isZeroCouponYn = UnAvails.NOTAVAILCHAR;
        //
        this.mktYld = UnAvails.NOTAVAILDOUBLE;
        this.mktPrcStart = UnAvails.NOTAVAILDOUBLE;
        this.mktPrcFuture = UnAvails.NOTAVAILDOUBLE;
        this.matTerm = UnAvails.NOTAVAILINT;
        this.nameDelimiter = UnAvails.NOTAVAILSTRING;
        this.oddnessPrcYn = 'Y';
        this.parBondDtMaturity = new Dates[Limits.MAXPARBONDS];
        this.parBondPeriodicity = new String[Limits.MAXPARBONDS];
        this.passFactor = UnAvails.NOTAVAILDOUBLE;
        this.passFactor1 = UnAvails.NOTAVAILDOUBLE;
        this.passFactor2 = UnAvails.NOTAVAILDOUBLE;
        this.passFactors = new double[Limits.MAXFACTORS];
        this.passModel = UnAvails.NOTAVAILSTRING;
        this.passProjectMeth = UnAvails.NOTAVAILSTRING;
        this.passPayoff = UnAvails.NOTAVAILDOUBLE;
        this.passWala = UnAvails.NOTAVAILINT;
        this.passWarm = UnAvails.NOTAVAILINT;
        this.paylegPeriodicity = UnAvails.NOTAVAILSTRING;
        this.paylegCpnrt = UnAvails.NOTAVAILDOUBLE;
        this.paylegFixedYn = UnAvails.NOTAVAILCHAR;
        this.paylegResetRt = new double[Limits.MAXRESETS];
        this.paylegSchedDOM = UnAvails.NOTAVAILINT;
        this.paylegSchedDOW = UnAvails.NOTAVAILINT;
        this.paylegSchedEOMRuleYn = 'Y';
        this.paylegSchedCompoundFreq = UnAvails.NOTAVAILINT;
        this.paylegHolidayRule = UnAvails.NOTAVAILSTRING;
        this.paylegSchedMOY = UnAvails.NOTAVAILINT;
        this.dtPaylegSchedNextCpn = new Dates(error);
        this.dtPaylegSchedOccur = new Dates(error);
        this.paylegSchedOccurYear = UnAvails.NOTAVAILINT;
        this.dtPaylegSchedPrevCpn = new Dates(error);
        this.paylegSchedRepeat = UnAvails.NOTAVAILINT;
        this.paylegTimeUnit = UnAvails.NOTAVAILSTRING;
        this.paylegSchedUseNextLastYn = UnAvails.NOTAVAILCHAR;
        this.paylegSchedWOM = UnAvails.NOTAVAILINT;
        this.pctToggleCash = new double[Limits.MAXCASHFLOWS];
        this.pikableYn = UnAvails.NOTAVAILCHAR;
        this.pikRt = new double[Limits.MAXPIKS];
        this.portName = UnAvails.NOTAVAILSTRING;
        this.prcAdjustedYn = 'N';
        this.preFundedPrc = UnAvails.NOTAVAILDOUBLE;
        this.prepaySpeed = UnAvails.NOTAVAILDOUBLE;
        this.afterTaxCalcsYn = 'N';
        this.purPrc = UnAvails.NOTAVAILDOUBLE;
        this.purWala = UnAvails.NOTAVAILINT;
        this.purWarm = UnAvails.NOTAVAILINT;
        this.purYld = UnAvails.NOTAVAILDOUBLE;
        this.putableYn = UnAvails.NOTAVAILCHAR;
        this.putNotifyDays = BondConsts.CALLNOTIFICATIONDAYS;
        this.putPrc = new double[Limits.MAXCALLS];
        this.putType = UnAvails.NOTAVAILSTRING;
        this.putPrcType = UnAvails.NOTAVAILSTRING;
        // receive leg
        this.reclegPeriodicity = UnAvails.NOTAVAILSTRING;
        this.reclegFixedYn = UnAvails.NOTAVAILCHAR;
        this.reclegResetRt = new double[Limits.MAXRESETS];
        this.reclegCap = new double[Limits.MAXRESETS];
        this.reclegFloor = new double[Limits.MAXRESETS];
        this.reclegMultiplier = new double[Limits.MAXRESETS];
        this.reclegSprd = new double[Limits.MAXRESETS];
        this.reclegLifeCap = UnAvails.NOTAVAILDOUBLE;
        this.reclegLifeFloor = UnAvails.NOTAVAILDOUBLE;
        this.reclegSchedDOM = UnAvails.NOTAVAILINT;
        this.reclegSchedDOW = UnAvails.NOTAVAILINT;
        this.reclegSchedEOMRuleYn = UnAvails.NOTAVAILCHAR;
        this.reclegSchedCompoundFreq = UnAvails.NOTAVAILINT;
        this.reclegHolidayRule = UnAvails.NOTAVAILSTRING;
        this.reclegSchedMOY = UnAvails.NOTAVAILINT;
        this.dtReclegSchedNextCpn = new Dates(error);
        this.dtReclegSchedOccur = new Dates(error);
        this.reclegSchedOccurYear = UnAvails.NOTAVAILINT;
        this.reclegSchedRepeat = UnAvails.NOTAVAILINT;
        this.reclegSchedCompoundFreq = UnAvails.NOTAVAILINT;
        this.reclegTimeUnit = UnAvails.NOTAVAILSTRING;
        this.reclegSchedUseNextLastYn = UnAvails.NOTAVAILCHAR;
        this.reclegSchedWOM = UnAvails.NOTAVAILINT;
        this.recoveryRt = UnAvails.NOTAVAILDOUBLE;
        this.redeemAmt = new double[Limits.MAXREDEEMS];
        this.redeemFactor = new double[Limits.MAXREDEEMS];
        this.redeemPayoff = BondConsts.REDEEMPAYOFF;
        this.redeemPct = new double[Limits.MAXREDEEMS];
        this.redeemPrin = UnAvails.NOTAVAILDOUBLE;
        this.redeemInt = UnAvails.NOTAVAILDOUBLE;
        this.reinVestMethod = UnAvails.NOTAVAILSTRING;
        this.reinRt = new double[Limits.MAXYIELDPOINTS];
        this.reinMatlen = new double[Limits.MAXYIELDPOINTS];
        this.resetMatlens = new double[Limits.MAXRESETS];
        this.paylegBenchmark = UnAvails.NOTAVAILSTRING;
        this.paylegResetPeriodicity = UnAvails.NOTAVAILSTRING;
        this.paylegResetSchedDOM = UnAvails.NOTAVAILINT;
        this.paylegResetSchedDOW = UnAvails.NOTAVAILINT;
        this.paylegResetSchedCompoundFreq = UnAvails.NOTAVAILINT;
        this.paylegResetHolidayRule = UnAvails.NOTAVAILSTRING;
        this.paylegResetSchedMOY = UnAvails.NOTAVAILINT;
        this.paylegResetSchedNextCpnDt = new Dates(error);
        this.paylegResetSchedOccurDt = new Dates(error);
        this.paylegResetSchedOccurYear = UnAvails.NOTAVAILINT;
        this.paylegResetSchedPrevCpnDt = new Dates(error);
        this.paylegResetSchedRepeat = UnAvails.NOTAVAILINT;
        this.paylegResetTimeUnit = UnAvails.NOTAVAILSTRING;
        this.paylegResetSchedUseNextLastYn = 'N';
        this.paylegResetSchedWOM = UnAvails.NOTAVAILINT;
        this.resetBenchmark = UnAvails.NOTAVAILSTRING;
        this.resetMultiplier = new double[Limits.MAXRESETS];
        this.resetSprd = new double[Limits.MAXRESETS];
        this.resetCaps = new double[Limits.MAXRESETS];
        this.resetFloors = new double[Limits.MAXRESETS];
        this.resetFixedYn = UnAvails.NOTAVAILCHAR;
        this.resetRts = new double[Limits.MAXRESETS];
        this.resetLifeCap = UnAvails.NOTAVAILDOUBLE;
        this.resetLifeFloor = UnAvails.NOTAVAILINT;
        this.reclegBenchmark = UnAvails.NOTAVAILSTRING;
        this.reclegResetPeriodicity = UnAvails.NOTAVAILSTRING;
        this.reclegResetSchedDOM = UnAvails.NOTAVAILINT;
        this.reclegResetSchedDOW = UnAvails.NOTAVAILINT;
        this.reclegResetSchedEOMRuleYn = UnAvails.NOTAVAILCHAR;
        this.reclegResetSchedCompoundFreq = UnAvails.NOTAVAILINT;
        this.reclegResetHolidayRule = UnAvails.NOTAVAILSTRING;
        this.reclegResetSchedMOY = UnAvails.NOTAVAILINT;
        this.reclegResetSchedNextCpnDt = new Dates(error);
        this.reclegResetSchedOccurDt = new Dates(error);
        this.reclegResetSchedOccurYear = UnAvails.NOTAVAILINT;
        this.reclegResetSchedPrevCpnDt = new Dates(error);
        this.reclegResetSchedRepeat = UnAvails.NOTAVAILINT;
        this.reclegResetTimeUnit = UnAvails.NOTAVAILSTRING;
        this.reclegResetSchedUseNextLastYn = UnAvails.NOTAVAILCHAR;
        this.reclegResetSchedWOM = UnAvails.NOTAVAILINT;
        this.resetPeriodicity = UnAvails.NOTAVAILSTRING;
        this.resetSchedDOM = UnAvails.NOTAVAILINT;
        this.resetSchedDOW = UnAvails.NOTAVAILINT;
        this.resetSchedEOMRuleYn = UnAvails.NOTAVAILCHAR;
        this.resetSchedCompoundFreq = UnAvails.NOTAVAILINT;
        this.resetHolidayRule = UnAvails.NOTAVAILSTRING;
        this.resetSchedMOY = UnAvails.NOTAVAILINT;
        this.dtResetSchedNextCpn = new Dates(error);
        this.dtResetSchedOccur = new Dates(error);
        this.resetSchedOccurYear = UnAvails.NOTAVAILINT;
        this.dtResetSchedPrevCpn = new Dates(error);
        this.resetSchedRepeat = UnAvails.NOTAVAILINT;
        this.resetTimeUnit = UnAvails.NOTAVAILSTRING;
        this.resetSchedUseNextLastYn = UnAvails.NOTAVAILCHAR;
        this.resetSchedWOM = UnAvails.NOTAVAILINT;
        this.returnWt = UnAvails.NOTAVAILINT;
        this.riskReturnGoal = UnAvails.NOTAVAILSTRING;
        this.secDescr = UnAvails.NOTAVAILSTRING;
        this.securityId = UnAvails.NOTAVAILSTRING;
        this.securityType = UnAvails.NOTAVAILSTRING;
        this.serviceRt = UnAvails.NOTAVAILDOUBLE;
        this.settlePrc = UnAvails.NOTAVAILDOUBLE;
        this.settleYld = UnAvails.NOTAVAILDOUBLE;
        this.sfigAccrual = UnAvails.NOTAVAILINT;
        this.sfigAccrued = UnAvails.NOTAVAILINT;
        this.sfigAccruedSize = UnAvails.NOTAVAILINT;
        this.sfigCashflow = UnAvails.NOTAVAILINT;
        this.sfigCurrency = UnAvails.NOTAVAILINT;
        this.sfigCPI = UnAvails.NOTAVAILINT;
        this.sfigIdxValue = UnAvails.NOTAVAILINT;
        this.sfigIdxRatio = UnAvails.NOTAVAILINT;
        this.sfigPrc = UnAvails.NOTAVAILINT;
        this.sfigStatistic = UnAvails.NOTAVAILINT;
        this.sfigYield = UnAvails.NOTAVAILINT;
        this.showIndivCalcYn = UnAvails.NOTAVAILCHAR;
        this.stateOfIssue = UnAvails.NOTAVAILSTRING;
        this.stateOfResidence = UnAvails.NOTAVAILSTRING;
        this.showRecapYn = UnAvails.NOTAVAILCHAR;
        this.sinkAmts = new double[Limits.MAXSINKS];
        this.sinkPcts = new double[Limits.MAXSINKS];
        this.sinkPrcs = new double[Limits.MAXSINKS];
        this.spotDiscInterval = UnAvails.NOTAVAILINT;
        this.spotFixingInterval = UnAvails.NOTAVAILINT;
        this.startYield = UnAvails.NOTAVAILDOUBLE;
        this.stateTaxExemptYn = UnAvails.NOTAVAILCHAR;
        // this.strInput = "settprc initialized to " + settlePrc;
        this.stripType = UnAvails.NOTAVAILSTRING;
        this.swapPayType = UnAvails.NOTAVAILSTRING;
        this.swapPctGain = UnAvails.NOTAVAILDOUBLE;
        this.swapSide = UnAvails.NOTAVAILSTRING;
        this.taxAdjAllowedYn = UnAvails.NOTAVAILCHAR;
        this.taxLotId = UnAvails.NOTAVAILSTRING;
        this.taxRtFedCapGainLong = UnAvails.NOTAVAILDOUBLE;
        this.taxRtFedCapGainShort = UnAvails.NOTAVAILDOUBLE;
        this.taxRtFedIncome = UnAvails.NOTAVAILDOUBLE;
        this.taxRtStateCapGainLong = UnAvails.NOTAVAILDOUBLE;
        this.taxRtStateCapGainShort = UnAvails.NOTAVAILDOUBLE;
        this.taxRtStateIncome = UnAvails.NOTAVAILDOUBLE;
        this.tradeType = UnAvails.NOTAVAILSTRING;
        this.useCreditSprdYn = UnAvails.NOTAVAILCHAR;
        this.useCurrentRedemptionYn = UnAvails.NOTAVAILCHAR;
        this.useDefaultSfigYn = 'Y';
        this.useLocalCurrencyYn = UnAvails.NOTAVAILCHAR;
        this.userCashDt = new Dates[Limits.MAXCASHFLOWS];
        this.useForwardCurveYn = UnAvails.NOTAVAILCHAR;
        this.userCashflow = new double[Limits.MAXCASHFLOWS];
        this.warningsOffYn = UnAvails.NOTAVAILCHAR;
        this.weekend1 = UnAvails.NOTAVAILINT;
        this.weekend2 = UnAvails.NOTAVAILINT;
        this.whenIssueYn = UnAvails.NOTAVAILCHAR;
        this.yieldCurveInterpMeth = UnAvails.NOTAVAILSTRING;
        this.yieldForeCastMeth = UnAvails.NOTAVAILSTRING;
        this.yieldQuoteType = UnAvails.NOTAVAILSTRING;
        this.noCallDts = 0;
        this.noCallPrcs = 0;
        this.noUserPayDts = 0;
        noAccrueDts = 0;

        noDiscMats = 0;
        noDtEstPrePay = 0;
        noEstPrePay = 0;
        noIntdtUserPays = 0;
        noResetDts = 0;
        noPaylegDts = 0;
        noReclegDts = 0;
        noPaylegResetDts = 0;
        noReclegResetDts = 0;
        this.noCreditSprdMatlens = 0;
        this.noCreditSprdRates = 0;

        this.noDiscRate = 0;
        this.noDiscTenor = 0;
        this.noDiscRtDn = 0;
        this.noDiscRtUp = 0;
        this.noDiscStdDev = 0;

        this.noDiscSprd = 0;
        this.noDiscSprdTenor = 0;
        this.noDiscSprdStdDev = 0;
        this.noDiscSprdDn = 0;
        this.noDiscSprdUp = 0;

        this.noDiscFutRate = 0;
        this.noDiscFutTenor = 0;
        this.noDiscFutRtDn = 0;
        this.noDiscFutRtUp = 0;
        this.noDiscFutStdDev = 0;

        this.noDiscFutSprd = 0;
        this.noDiscFutSprdTenor = 0;
        this.noDiscFutSprdStdDev = 0;
        this.noDiscFutSprdDn = 0;
        this.noDiscFutSprdUp = 0;

        // this.nodtUserPays = 0;
        this.noDtRates = 0;
        this.noFactorDts = 0;
        this.noFactors = 0;
        this.noFixingRate = 0;
        this.noFixingAvgRt = 0;
        this.noFixingMin = 0;
        this.noFixingMax = 0;
        this.noFixingTenor = 0;
        this.noFixingRtDn = 0;
        this.noFixingRtUp = 0;
        this.noFixingStdDev = 0;
        this.noFxDts = 0;
        this.noFxRts = 0;
        this.noMktPrcDts = 0;
        this.noMktPrcs = 0;
        this.noIdxAssumes = 0;
        this.noIdxAssumeDts = 0;
        this.noIdxRatios = 0;
        this.noIdxRatioDts = 0;
        this.noParBondPeriodicity = 0;
        this.noParBondCpnRts = 0;
        this.noParBondMaturityDates = 0;
        this.noPassFactors = 0;
        this.noPassFactorDts = 0;
        this.noPaylegResets = 0;
        this.noPaylegResetDts = 0;
        this.noPikDts = 0;
        this.noPikRts = 0;
        this.noPutDts = 0;
        this.noPutPrcs = 0;
        this.noReclegResets = 0;
        this.noReclegResetDts = 0;
        this.noReclegMultipliers = 0;
        this.noReclegSprds = 0;
        this.noReclegCaps = 0;
        this.noReclegFloors = 0;
        this.noRedeemAmts = 0;
        this.noRedeemPcts = 0;
        this.noRedeemRts = 0;
        this.noRedeemDts = 0;
        this.noRedeemFactors = 0;
        this.noReinMatlens = 0;
        this.noReinRts = 0;
        this.noRequests = 0;
        this.noResetMultipliers = 0;
        this.noResetSprds = 0;
        this.noResetCaps = 0;
        this.noResetFloors = 0;
        this.noResetDts = 0;
        this.noResetRts = 0;

        this.noPaylegCaps = 0;
        this.noPaylegMultipliers = 0;
        this.noPaylegMultiplierDts = 0;
        this.noPaylegSprds = 0;
        this.noPaylegFloors = 0;
        this.noPaylegResetRts = 0;
        this.noReclegResetRts = 0;
        this.noReclegCaps = 0;
        this.noReclegMultipliers = 0;
        this.noReclegMultiplierDts = 0;
        this.noReclegSprds = 0;
        this.noReclegFloors = 0;

        this.noResetMatlens = 0;
        this.noSinkDts = 0;
        this.noSinkAmts = 0;
        this.noSinkPrcs = 0;
        this.noSinkPcts = 0;

        this.noFixingRtDn = 0;
        this.noFixingRtUp = 0;
        this.noFixingStdDev = 0;
        this.noTaxDates = 0;
        this.noUserCashDts = 0;
        this.noUserCash = 0;
        this.noTogglePcts = 0;
        this.noToggleDts = 0;
        return true;
    }

    public boolean checkBeforeDate(Dates dtRequest) {
        boolean okToContinue;
        if (UnAvails.isAvail(dtDated)) {
            okToContinue = !dtRequest.leDate(dtRequest, dtDated);
        } else {
            okToContinue = true;
        }
        return okToContinue;
    }

    public boolean checkAfterDate(Dates dtRequest) {
        boolean okToContinue;
        if (UnAvails.isAvail(dtMaturity)) {
            okToContinue = !dtRequest.geDate(dtRequest, dtMaturity);
        } else {
            okToContinue = true;
        }
        return okToContinue;
    }

    public boolean isOddFirstPeriod() {
        Dates dtNextCpn = new Dates();
        if (UnAvails.isUnAvail(dtDated))
            return false;
        else {
            dtNextCpn = intSchedRule.getNextSchedDate(BondConsts.AFTERDATE, dtDated);
            return !dtNextCpn.eqDate(dtNextCpn, dt1stPay);
        }
    }

    public boolean isOddLastPeriod() {
        Dates dtNextCpn = new Dates();
        if (UnAvails.isUnAvail(dtLastPay))
            return false;
        else {
            dtNextCpn = intSchedRule.getNextSchedDate(BondConsts.AFTERDATE, dtDated);
            return !dtNextCpn.eqDate(dtNextCpn, dtMaturity);
        }
    }

    /*
     * public void setCalculation(int calcNo) {
     * this.error.calcName = calcMethod.calcResults.calcDefs.get(calcNo).calcName;
     * o }
     */
    static class CachedCalc {
        final String calcName;
        final String sValue;
        final double dValue;
        final int iValue;

        CachedCalc(String calcName, String sValue, double dValue, int iValue) {
            this.calcName = calcName;
            this.sValue = sValue;
            this.dValue = dValue;
            this.iValue = iValue;
        }
    }

    class ParamCategory {
        String paramName;
        String paramCategory;
        String instype;
        final ArrayList<ParamCategory> paramCategories = new ArrayList<ParamCategory>(339);

        ParamCategory(int index, String paramName, String category, String instype) {

        }

        public void setParamCategories() {
            this.paramCategories.add(new ParamCategory(0, "AACONTROL", "Accounting", "All"));
            this.paramCategories.add(new ParamCategory(1, "AAYIELD", "Accounting", "All"));
            this.paramCategories.add(new ParamCategory(2, "BASACCREAMORT", "Accounting", "All"));
            this.paramCategories.add(new ParamCategory(3, "BASACCRUEDINT", "Cashflow", "All"));
            this.paramCategories.add(new ParamCategory(4, "BASBONDEQV", "Cashflow", "Optional"));
            this.paramCategories.add(new ParamCategory(5, "BASDELAYDAYS", "DayCounting",
                    Instype.MORTGAGE));
            this.paramCategories.add(new ParamCategory(6, "BASIDXINTERP", "DayCounting", Instype.INDEXED));

        }
    }

    class AccrualBasis {
        final DayCount.DCMethod dcNum = DayCount.DCMethod.DC_NA;
        final DayCount.DCMethod dcDen = DayCount.DCMethod.DC_NA;
    }

    class Range {
        int startInd;
        int endInd;

        Range() {

        }

        Range(int startInd, int endInd) {
            this.startInd = startInd;
            this.endInd = endInd;
        }
    }

    public double calcWeightedAvg(ArrayList<SecurityResults> securityResultsList, int idxStart, int idxEnd) {
        int ix = idxStart - 1;
        double wtdAvg = 0.;
        double cumWt = 0.;
        double totWt = 0.;
        while (++ix <= idxEnd) {
            double mktVal = securityResultsList.get(ix).mktPrc * securityResultsList.get(ix).faceAmt;
            cumWt += securityResultsList.get(ix).dValue * mktVal;
            totWt += mktVal;
        }
        if (totWt != 0)
            wtdAvg = cumWt / totWt;
        else
            wtdAvg = 0.;
        return wtdAvg;
    }

    public double calcSum(ArrayList<SecurityResults> securityResultsList, int idxStart, int idxEnd) {
        int ix = idxStart - 1;
        double total = 0.;
        while (++ix <= idxEnd) {
            total += securityResultsList.get(ix).dValue;
        }
        return total;
    }

    public ArrayList<Range> getCalcRange(ArrayList<SecurityResults> securityResultsList) {
        ArrayList<Range> calcRangeList = new ArrayList<>();
        int noCalcs = securityResultsList.size();
        int calcNo = 0;
        int idxStart = 0;
        int idxEnd = 0;
        int lastCalcNo = securityResultsList.get(0).calcNo;
        Range range;
        idxStart = 0;
        while (++calcNo < noCalcs) {
            if (securityResultsList.get(calcNo).calcNo == lastCalcNo) {
                ++idxEnd;
            } else {
                idxEnd = calcNo - 1;
                range = new Range(idxStart, idxEnd);
                calcRangeList.add(range);
                idxStart = calcNo;
            }
            lastCalcNo = securityResultsList.get(calcNo).calcNo;
        }
        range = new Range(idxStart, noCalcs - 1);
        calcRangeList.add(range);
        return calcRangeList;
    }

}

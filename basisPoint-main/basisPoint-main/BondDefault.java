//import org.jetbrains.annotations.NotNull;


import java.awt.font.LineMetrics;
import java.time.LocalDate;
import java.util.ArrayList;

public class BondDefault {
    CommandLine cmdLine;
    Bond bond;
    Error error;
    Choices choices;
    DayCount dayCount;

    Utility utility;
    ArrayList<Integer> calculationList;
    YieldCurves allYieldCurves;

    BondDefault(CommandLine cmdLine, Error error, Choices choices, DayCount dayCount, Utility utility) {
        this.cmdLine = cmdLine;
        this.error = error;
        this.choices = choices;
        this.dayCount = dayCount;
        this.utility = utility;

    }

    public void setBond(Bond bond) {
        this.bond = bond;
    }

    public void setCalculationList(ArrayList<Integer> calculationList) {
        this.calculationList = calculationList;
    }


    public void setAllYieldCurves(YieldCurves allYieldCurves) {
        this.allYieldCurves = allYieldCurves;
    }


    class ResetMultiplier {
        Dates dtMultiplier;
        double multiplier;
        double sprd;

        ResetMultiplier(Dates dtMultiplier, double multiplier, double sprd) {
            this.dtMultiplier = dtMultiplier;
            this.multiplier = multiplier;
            this.sprd = sprd;
        }
    }

    public boolean setDefaults(String isInputJasonYn, int securityNo, ArrayList<Integer> calculationList) {
        boolean isOk = false;
        defaultDescription();
        String instype = bond.instype;
        if (instype == UnAvails.NOTAVAILSTRING) {
            isOk = defaultCalcRequest(instype);
            if (!isOk)
                return false;
        }
        isOk = defaultSignificantFigures(instype);
        if (!isOk)
            return false;
        isOk = defaultDelimiters(isInputJasonYn);
        if (!isOk)
            return false;
        defaultSettle();
        isOk = defaultYieldCurveOptions(securityNo, instype);
        if (!isOk)
            return false;

        if (defaultDiscCurveTrueFalse(choices, calculationList)) {
            bond.discRfCurve = defaultDiscRfCurve(allYieldCurves);
            if (error.hasErrorYn == 'Y')
                return false;
            bond.discSprdCurve = defaultDiscSprdCurve(allYieldCurves);
            if (error.hasErrorYn == 'Y') {
                error.clearError();
            }
            bond.discCurve = combineYieldCurves(bond.discRfCurve, bond.discSprdCurve);
            if (error.hasErrorYn == 'Y')
                return false;
            if (bond.discCurve.spotCurve.size() > 1) {
                bond.discCurve.sortSpotCurve(bond.discCurve.spotCurve);
            }
            if (bond.discCurve.forwardCurve.size() > 1) {
                bond.discCurve.sortSpotCurve(bond.discCurve.forwardCurve);
            }
        }
        if (defaultDiscFutCurveTrueFalse(calculationList)) {
            if (UnAvails.isUnAvail(bond.dtDiscFutCurve)) {
                String errmsg = "No future date was specified for the the future discount yield curve";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            bond.discFutRfCurve = defaultDiscFutRfCurve(allYieldCurves);
            if (error.hasErrorYn == 'Y')
                return false;
            bond.discFutSprdCurve = defaultDiscFutSprdCurve(allYieldCurves);
            if (error.hasErrorYn == 'Y')
                error.clearError();
            bond.discFutCurve = combineYieldCurves(bond.discFutRfCurve, bond.discFutSprdCurve);
            if (error.hasErrorYn == 'Y')
                return false;
            if (bond.discFutCurve.spotCurve.size() > 1) {
                bond.discFutCurve.sortSpotCurve(bond.discCurve.spotCurve);
            }
            if (bond.discFutCurve.forwardCurve.size() > 1) {
                bond.discFutCurve.sortSpotCurve(bond.discCurve.forwardCurve);
            }
        }

        isOk = bypassDefaults(calculationList);
        if (isOk)
            return true;
        isOk = defaultSecurityType();
        if (!isOk)
            return false;
        instype = bond.instype;
        if (!isOk)
            return false;
        isOk = defaultYieldQuote(instype);
        if (!isOk)
            return false;
        isOk = defaultCurrency();
        if (!isOk)
            return false;
        isOk = defaultDayCount();
        if (!isOk)
            return false;
        isOk = defaultCreditRatings();
        if (!isOk)
            return false;
        isOk = defaultSchedules(instype);
        if (!isOk)
            return false;
        isOk = defaultIRSSwap(instype);
        if (!isOk)
            return false;

        isOk = choices.inBypassMoreDefaultList(calculationList);
        if (isOk == true)
            return true;
        isOk = defaultReinvestMeth();
        if (isOk == false)
            return false;
        isOk = defaultMortgage(instype);
        if (!isOk)
            return false;
        isOk = defaultRedemption(instype);
        if (!isOk)
            return false;
        isOk = defaultPayTerms(instype);
        if (!isOk)
            return false;
        isOk = defaultBondTerms(instype);
        if (!isOk)
            return false;
        isOk = defaultSinks(instype);
        if (!isOk)
            return false;
        isOk = defaultFloater(instype);
        if (!isOk)
            return false;
        isOk = defaultIndexed(instype);
        if (!isOk)
            return false;
        isOk = defaultMisc(instype);
        if (!isOk)
            return false;
        isOk = defaultReinvestRates(instype);
        if (!isOk)
            return false;
        isOk = defaultCalcRequest(instype);
        if (!isOk)
            return false;
        isOk = defaultHorizonAnalysis(instype);
        if (!isOk)
            return false;
        isOk = defaultFutureValues(instype);
        if (!isOk)
            return false;
        isOk = defaultTaxes(instype);
        if (!isOk)
            return false;
        isOk = defaultPurchase(instype);
        if (!isOk)
            return false;
        defaultDescription();
        return isOk;

    }

    private boolean defaultSignificantFigures(String instype) {
        if (UnAvails.isUnAvail(bond.sfigAccruedSize))
            bond.sfigAccruedSize = 5; // accrued adjustment
        if (UnAvails.isUnAvail(bond.sfigAccruedDaycount))
            bond.sfigAccruedDaycount = 2;// accrued adjustment
        if (UnAvails.isUnAvail(bond.sfigAccruedPrin))
            bond.sfigAccruedPrin = 6;// accrued adjustment
        if (UnAvails.isUnAvail(bond.sfigCashflow))
            bond.sfigCashflow = 2;
        if (UnAvails.isUnAvail(bond.sfigCurrency))
            bond.sfigCurrency = 2;
        if (UnAvails.isUnAvail(bond.sfigCPI))
            bond.sfigCPI = 5;
        if (UnAvails.isUnAvail(bond.sfigPrc))
            bond.sfigPrc = 7;
        if (UnAvails.isUnAvail(bond.sfigStatistic))
            bond.sfigStatistic = 4;
        if (UnAvails.isUnAvail(bond.sfigYield))
            bond.sfigYield = 5;
        return true;
    }

    private boolean defaultCurrency() {

        if (bond.instype.equals(Instype.CURRENCY)) {
            if (UnAvails.isUnAvail(bond.dtMaturity))
                bond.dtMaturity = dayCount.time.addDaysToDate(bond.dtSettle, 1, 'Y');
            if (bond.dtMaturity.leDate(bond.dtSettle)) {
                bond.dtMaturity = dayCount.time.addDaysToDate(bond.dtSettle, 1, 'Y');
            }
            if (UnAvails.isUnAvail(bond.dtDated))
                bond.dtDated = bond.dtSettle;
            if (UnAvails.isUnAvail(bond.cpnRt))
                bond.cpnRt = 0.;
            bond.dtLastPayment = bond.dtMaturity;
            bond.redeemPrin = 1.;
            bond.redeemInt = 1.;
            if (UnAvails.isUnAvail(bond.accrueSchedRule.timeUnit)) {
                bond.accrueSchedRule.timeUnit = "D";
                bond.accrueSchedRule.repeatFactor = 1;
                bond.accrueSchedRule.compoundFreq = CalUnits.DAYSPERYEAR;
                bond.dtOccur = bond.dtMaturity;
                bond.dtRedeem = bond.dtMaturity;
                bond.redeemPrin = 1.;
                bond.redeemInt = 1.;
                bond.dtLastPayment = bond.dtMaturity;
            }
            if (UnAvails.isUnAvail(bond.basAccInt)) {
                bond.dcAccruedInt = dayCount.mapAccrualBasis("ACT/ACT");
            }
            if (UnAvails.isUnAvail(bond.mktPrcStart))
                bond.mktPrcStart = 100.;
        }
        return true;
    }

    public void defaultDescription() {
        String secDescr = UnAvails.NOTAVAILSTRING;
        if (UnAvails.isUnAvail(bond.portName)) {
            bond.portName = "Not specified";
        }
        if (UnAvails.isAvail(bond.secDescr)) {
            error.secDescr = bond.secDescr;
            return;
        }
        if (UnAvails.isAvail(bond.issuer))
            secDescr = bond.issuer;
        else if (UnAvails.isAvail(bond.securityType))
            secDescr = bond.securityType;
        else if (UnAvails.isAvail(bond.instype))
            secDescr = bond.instype;
        else
            secDescr = "?";
        if (UnAvails.isAvail(bond.cpnRt))
            secDescr = secDescr + " " + String.format("%.3f", bond.cpnRt * 100);
        if (UnAvails.isAvail(bond.dtMaturity))
            secDescr = secDescr + " " + bond.dtMaturity.getDateString();
        //   secDescr = Strings.toSentenceCase(secDescr);
        setDescription(secDescr);
        error.secDescr = secDescr;

    }

    public void setDescription(String secDescr) {
        error.secDescr = secDescr.trim();
        bond.secDescr = secDescr;
    }

    private boolean defaultYieldQuote(String instype) {
        if (instype.equals(Instype.DISCOUNT)) {
            if (UnAvails.isUnAvail(bond.yieldQuoteType)) {
                bond.yieldQuoteType = CalcEnums.TREASURYYIELD;
            }
        }
        return true;
    }


    private boolean defaultMisc(String instype) {
        if (UnAvails.isUnAvail(bond.yieldQuoteType)) {
            bond.yieldQuoteType = "COMPOUND";
        }
        if (UnAvails.isUnAvail(bond.showIndivCalcYn))
            bond.showIndivCalcYn = 'Y';
        if (UnAvails.isUnAvail(bond.mktPrcStart))
            bond.mktPrcStart = bond.settlePrc;

        if (UnAvails.isUnAvail(bond.calcOnlyPortStatsYn)) {
            if (!bond.commandLine.showPortSummary)
                bond.calcOnlyPortStatsYn = 'N';
            else
                bond.calcOnlyPortStatsYn = 'Y';
        }

        if (UnAvails.isUnAvail(bond.swapPctGain)) {
            bond.swapPctGain = -Limits.INFINITY;
        }

        return true;
    }

    private boolean defaultPurchase(String instype) {
        if (UnAvails.isUnAvail(bond.dtPur))
            bond.dtPur = bond.dtSettle;
        if (UnAvails.isUnAvail(bond.purPrc)) {
            bond.purPrc = bond.settlePrc;
        }
        return true;
    }

    private boolean defaultCalcRequest(String instype) {
        boolean isOk = true;


        if (UnAvails.isUnAvail(bond.afterTaxCalcsYn)) {
            bond.afterTaxCalcsYn = 'N';
        }

        if (bond.afterTaxCalcsYn == 'Y') {
            if (UnAvails.isUnAvail(bond.dtPur)) {
                String warningMsg = "Purchase date was not specified so analytics will not include capilat gaoin/loss";
                error.setWarning(getClass().getName(), warningMsg);
                bond.taxRtFedCapGainLong = 0.;
                bond.taxRtFedCapGainShort = 0.;
                bond.taxRtStateCapGainLong = 0.;
                bond.taxRtStateCapGainShort = 0.;
            }
            if (UnAvails.isUnAvail(bond.purPrc)) {
                String warningMsg = "Purchase price was not specified so analytics will not include capilat gaoin/loss";
                error.setWarning(getClass().getName(), warningMsg);
                bond.taxRtFedCapGainLong = 0.;
                bond.taxRtFedCapGainShort = 0.;
                bond.taxRtStateCapGainLong = 0.;
                bond.taxRtStateCapGainShort = 0.;
            }
            if (UnAvails.isUnAvail(bond.taxSchedRule.timeUnit)) {
                String warningMsg = "No tax payment schedule for time unit was specified so atax will be paid annually";
                error.setWarning(getClass().getName(), warningMsg);
                bond.taxSchedRule.timeUnit = "M";
            }
            if (UnAvails.isUnAvail(bond.taxSchedRule.repeatFactor)) {
                String warningMsg = "No tax payment schedule for repeat factor was specified so analytics will be on pre-tax basis";
                error.setWarning(getClass().getName(), warningMsg);
                bond.taxSchedRule.repeatFactor = 12;
            }
            if (UnAvails.isUnAvail(bond.taxSchedRule.dtOccur)) {
                String warningMsg = "No tax payment schedule for an actual tax payment date so will default to 04/15/2020";
                error.setWarning(getClass().getName(), warningMsg);
                bond.taxSchedRule.dtOccur.setDate("04/15/2020", "MM/DD/YYYY");
            }
            if (UnAvails.isUnAvail(bond.taxSchedRule.holidayRule))
                bond.taxSchedRule.holidayRule = BondConsts.IGNOREHOLIDAYS;
        }
        return true;
    }


    private boolean defaultDayCount() {

        if (UnAvails.isUnAvail(bond.basOdd1st)) {
            bond.basOdd1st = bond.basAccInt;

        }
        if (UnAvails.isUnAvail(bond.basOddLast)) {
            bond.basOddLast = bond.basAccInt;
        }
        if (UnAvails.isUnAvail(bond.basBondEqv)) {
            bond.basBondEqv = bond.basAccInt;

        }
        if (UnAvails.isUnAvail(bond.basYldPrc)) {
            bond.basYldPrc = bond.basAccInt;
        }

        if (UnAvails.isUnAvail(bond.basAccreAmort)) {
            bond.basAccreAmort = bond.basAccInt;
        }
        if (UnAvails.isUnAvail(bond.dtStart)) {
            bond.dtStart = bond.dtSettle;
        }
        if (UnAvails.isUnAvail(bond.dtEnd)) {
            bond.dtEnd = bond.dtMaturity;
        }
        if (UnAvails.isUnAvail(bond.basResetPayleg)) {
            bond.basResetPayleg = bond.basPayleg;
        }
        if (UnAvails.isUnAvail(bond.basResetRecleg)) {
            bond.basResetRecleg = bond.basRecleg;
        }
        return true;
    }

    boolean defaultBondTerms(String instype) {
        if (!choices.passthruList.contains(instype)) {
            if (UnAvails.isAvail(bond.dtMaturity))
                bond.dtLastPayment = bond.dtMaturity;
            else {
                String errmsg = "No maturity date was specified";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (UnAvails.isUnAvail(bond.accruedIntMeth))
            bond.accruedIntMeth = "STD";
        if (instype.equals(Instype.ATMATURITY)) {
            if (UnAvails.isUnAvail(bond.dtDated)) {
                String errmsg = "No dated date was specified";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }

        return true;
    }

    void defaultSettle() {
        if (UnAvails.isUnAvail(bond.dtSettle)) {
            if (UnAvails.isAvail(bond.dtFuture)) {
                bond.dtSettle = bond.dtFuture;
                return;
            }
            bond.dtSettle = bond.dtSettle.getToday();

        }
        return;
    }

    boolean defaultRedemption(String instype) {
        if (UnAvails.isUnAvail(bond.redeemPrin))
            bond.redeemPrin = 1.;
        bond.redeemInt = bond.redeemPrin;
        if (UnAvails.isUnAvail(bond.dtLastPayment))
            bond.dtLastPayment = bond.dtMaturity;
        if (UnAvails.isUnAvail(bond.dtRedeem))
            bond.dtRedeem = bond.dtLastPayment;
        if (Instype.PERPETUALFLOAT.equals(instype) || Instype.PERPETUAL.equals(instype) || Instype.IRSSWAP.equals(instype)) {
            bond.redeemPrin = 0.;
        }
        if (bond.stripType.equals(BondConsts.STRIPIO))
            bond.redeemPrin = 0.;
        if (bond.stripType.equals(BondConsts.STRIPPO))
            bond.redeemInt = 0.;
        return true;
    }


    private boolean defaultSchedules(String instype) {
        boolean isOk = false;
        isOk = defaultAccrueSchedule(instype);
        if (!isOk)
            return false;
        isOk = defaultIntSchedule(instype);
        if (!isOk)
            return false;

        isOk = defaultResetSchedule(instype);
        if (!isOk)
            return false;

        isOk = defaultIndexSchedule(instype);
        if (!isOk)
            return false;
        isOk = defaultIndexRatioSchedule(instype);
        if (!isOk)
            return false;

        isOk = defaultPaylegSchedule();
        if (!isOk)
            return false;

        isOk = defaultReclegSchedule();
        if (!isOk)
            return false;

        isOk = defaultPaylegResetSchedule();
        if (!isOk)
            return false;
        isOk = defaultReclegResetSchedule();
        if (!isOk)
            return false;
        isOk = defaultTaxSchedule();
        if (!isOk)
            return false;

        return isOk;
    }


    private boolean defaultAccrueSchedule(String instype) {
        // do we need the schedule
        // interest rule and accrue rule schedules will be made together in separate schedule
        if (choices.swapList.contains(instype))
            return true;
        if (UnAvails.isUnAvail(bond.accrueSchedRule.holidayRule)) {
            bond.accrueSchedRule.holidayRule = BondConsts.IGNOREHOLIDAYS;
        }
        bond.accrueSchedRule.weekend1 = bond.weekend1;
        bond.accrueSchedRule.weekend2 = bond.weekend2;
        if (instype.equals(Instype.IRSSWAP)) {
            if (UnAvails.isUnAvail(bond.basPayleg)) {
                String errmsg = "No accrual basis was specified for the pay leg of an interest rate swap";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (UnAvails.isUnAvail(bond.basRecleg)) {
                String errmsg = "No accrual basis was specified for the receive leg of an interest rate swap";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        } else {
            if (UnAvails.isUnAvail(bond.basAccInt)) {
                String errmsg = "No accrual basis was specified for the interest schedule";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        bond.accrueSchedRule.dayCountMeth = bond.dcAccruedInt;
        if (choices.inDiscountList(instype) || instype.equals(Instype.ATMATURITY)) {
            bond.accrueSchedRule.dtOccur = bond.dtMaturity;
            bond.accrueSchedRule.repeatFactor = CalUnits.SEMIANNUALREPEAT;
            bond.accrueSchedRule.timeUnit = BondConsts.ENUMERATEDTIMEUNIT;
            bond.reclegSchedRule.dtUserPays[0] = bond.dtMaturity;
            bond.accrueSchedRule.compoundFreq = CalUnits.SEMIANNUALFQ;
            return true;
        }
        if (UnAvails.isUnAvail(bond.accrueSchedRule.timeUnit)) {
            if (UnAvails.isAvail(bond.intSchedRule.timeUnit)) {
                bond.accrueSchedRule.timeUnit = bond.intSchedRule.timeUnit;
            } else {
                String errmsg = "No periodicity was specified for the interest schedule";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }

        if (UnAvails.isUnAvail(bond.accrueSchedRule.repeatFactor)) {
            if (UnAvails.isAvail(bond.intSchedRule.repeatFactor))
                bond.accrueSchedRule.repeatFactor = bond.intSchedRule.repeatFactor;
            else {
                String errmsg = "No pay frequency (REPEATFACTOR) was specified for the interest payment schedule";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (UnAvails.isUnAvail(bond.accrueSchedRule.compoundFreq)) {
            if (UnAvails.isAvail(bond.intSchedRule.compoundFreq))
                bond.accrueSchedRule.compoundFreq = bond.intSchedRule.compoundFreq;
            else {
                String errmsg = "No compound frequency was specified for interest schedule rule";
                error.setError(getClass().getName(), errmsg);
                return false;
            }

        }

        if (UnAvails.isUnAvail(bond.accrueSchedRule.holidayRule)) {
            bond.accrueSchedRule.holidayRule = BondConsts.IGNOREHOLIDAYS;
        }
        //bond.accrueSchedRule.compoundFreq = stringToBond.mapCompoundFreq(bond.accrueSchedRule.timeUnit, bond.accrueSchedRule.repeatFactor);

        if (choices.inPerpetualList(instype)) {
            if (UnAvails.isUnAvail(bond.dtDated)) {
                String errmsg = "no dated date was specified for a perpetual bond so interest payment schedule is known";
                error.setError(getClass().getName(), errmsg);
                return false;
            } else {
                bond.dtMaturity.year = bond.dtSettle.year + Defaults.PERPETUALMATURITY;
                bond.dtMaturity.month = bond.dtDated.month;
                bond.dtMaturity.day = bond.dtDated.month;
                bond.dtLastPayment = bond.dtMaturity;
            }
        } else {
            if (UnAvails.isUnAvail(bond.dtMaturity)) {
                String errmsg = "no maturity date was specified";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (UnAvails.isUnAvail(bond.accrueSchedRule.dtOccur)) {
            if (UnAvails.isAvail(bond.dt1stPay))
                bond.accrueSchedRule.dtOccur = bond.dt1stPay;
            else if (UnAvails.isAvail(bond.dtLastPay))
                bond.accrueSchedRule.dtOccur = bond.dtLastPay;
            else if (UnAvails.isAvail(bond.dtMaturity))
                bond.accrueSchedRule.dtOccur = bond.dtMaturity;
            else {
                String errmsg = "No enumerated interest payment dates were specified when you indicated an enumerated interest payment schedule";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }

        if (bond.accrueSchedRule.timeUnit.equals(BondConsts.ENUMERATEDTIMEUNIT)) {
            if (bond.noAccrueDts == 0) {
                String errmsg = "No enumerated accurual payment dates were specified when you indicated an enumerated interest payment schedule";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            int ic = -1;
            while (ic < bond.noUserPayDts) {
                bond.accrueSchedRule.dtUserPays[ic] = bond.dtAccrue[ic];
            }

        }
        if (UnAvails.isUnAvail(bond.accrueSchedRule.holidayRule))
            bond.accrueSchedRule.holidayRule = BondConsts.IGNOREHOLIDAYS;
        int ns = -1;
        while (++ns < bond.noUserPayDts) {
            bond.accrueSchedRule.dtUserPays[ns] = bond.dtUserPays[ns];
        }
        bond.accrueSchedRule.dtDated = bond.dtDated;

        if (UnAvails.isUnAvail(bond.accrueSchedRule.useEndOfMonthRuleYn)) {
            bond.accrueSchedRule.useEndOfMonthRuleYn = 'Y';
        }
        bond.accrueSchedRule.weekend1 = bond.weekend1;
        bond.accrueSchedRule.weekend2 = bond.weekend2;

        return true;
    }

    private boolean defaultIntSchedule(String instype) {
        char use360Yn = 'Y';
        bond.intSchedRule.weekend1 = bond.weekend1;
        bond.intSchedRule.weekend2 = bond.weekend2;
        bond.intSchedRule.timeUnit = bond.accrueSchedRule.timeUnit;
        bond.intSchedRule.repeatFactor = bond.accrueSchedRule.repeatFactor;
        bond.intSchedRule.compoundFreq = bond.accrueSchedRule.compoundFreq;
        if (Instype.MORTGAGE.equals(instype)) {
            if (UnAvails.isUnAvail(bond.dtLastPayment)) {
                bond.dtLastPayment = dayCount.time.addDaysToDate(bond.dtMaturity, bond.delaydaysStated, use360Yn);
                bond.intSchedRule.dtOccur = bond.dtLastPayment;
            } else {
                bond.intSchedRule.dtOccur = bond.dtLastPayment;
            }
        } else {
            bond.intSchedRule.dtOccur = bond.accrueSchedRule.dtOccur;
        }
        if (bond.dcYldPrc == DayCount.DCMethod.DC_NA) {
            bond.dcYldPrc = bond.dcAccruedInt;
        }
        bond.intSchedRule.holidayRule = bond.accrueSchedRule.holidayRule;
        bond.intSchedRule.country = bond.accrueSchedRule.country;
        bond.intSchedRule.dtMaturity = bond.accrueSchedRule.dtMaturity;
        bond.intSchedRule.useEndOfMonthRuleYn = bond.accrueSchedRule.useEndOfMonthRuleYn;
        bond.intSchedRule.dtDated = bond.accrueSchedRule.dtDated;
        bond.intSchedRule.dt1stPay = bond.accrueSchedRule.dt1stPay;
        bond.intSchedRule.dtLastOnlyCoupon = bond.accrueSchedRule.dtLastOnlyCoupon;
        bond.intSchedRule.dtLastUserCpn = bond.accrueSchedRule.dtLastUserCpn;
        bond.intSchedRule.dtNextUserCpn = bond.accrueSchedRule.dtNextUserCpn;
        if (UnAvails.isUnAvail(bond.intSchedRule.dtOccur))
            bond.intSchedRule.dtOccur = bond.accrueSchedRule.dtOccur;
        bond.intSchedRule.dayOfMonth = bond.accrueSchedRule.dayOfMonth;
        bond.intSchedRule.monthOfYear = bond.accrueSchedRule.monthOfYear;
        bond.intSchedRule.weekOfMonth = bond.accrueSchedRule.weekOfMonth;
        bond.intSchedRule.dayCountMeth = bond.accrueSchedRule.dayCountMeth;
        bond.intSchedRule.weekend1 = bond.accrueSchedRule.weekend1;
        bond.intSchedRule.weekend2 = bond.accrueSchedRule.weekend2;
        int ic = -1;
        while (++ic < bond.noUserPayDts) {
            bond.intSchedRule.dtUserPays[ic] = bond.accrueSchedRule.dtUserPays[ic];
        }
        return true;
    }

    public boolean defaultMortgage(String instype) {
        int Fterm = 0;

        char use360Yn = 'N';
        if (!choices.inPassthruList(instype)) {
            bond.dtLastPayment = bond.dtMaturity;
            return true;
        }

        if (UnAvails.isUnAvail(bond.dtPassFactor)) {
            String errmsg = "No date for the pass-thru factor specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        if (UnAvails.isUnAvail(bond.passFactor)) {
            String errmsg = "No pass-thru factor was specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.delaydaysStated)) {
            if (UnAvails.isAvail(bond.delaydaysAct))
                bond.delaydaysStated = bond.delaydaysAct - 30;
            else {
                String errmsg = "No delay days stated or actual was specified";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }

        if (UnAvails.isUnAvail(bond.dtMaturity)) {
            String errmsg = "No bond legal maturity date was specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        if (UnAvails.isUnAvail(bond.dtLastPayment)) {
            bond.dtLastPayment = dayCount.time.addDaysToDate(bond.dtMaturity, bond.delaydaysAct, use360Yn);
        }

        if (UnAvails.isUnAvail(bond.passWarm)) {
            String errmsg = "No weighted average maturity (WARM) was specfied";
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        bond.dtMaturity = dayCount.time.addMonthsToDate(bond.dtPassFactor, bond.passWarm, 'Y');

        if (UnAvails.isUnAvail(bond.passPayoff))
            bond.passPayoff = 0.;

        if (UnAvails.isUnAvail(bond.dtDated)) {
            if (UnAvails.isAvail(bond.passWala))
                bond.dtDated = dayCount.time.addDaysToDate(bond.dtPassFactor, -bond.passWala, 'Y');
            else {
                String errmsg = "No dated or passthru Wala was specified";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        bond.matTerm = (int) dayCount.time.getMonthsBetween(bond.dtDated, bond.dtMaturity, true);
        if (UnAvails.isUnAvail(bond.serviceRt))
            bond.serviceRt = 0.;
        return true;
    }

    private boolean defaultReinvestMeth() {
        if (UnAvails.isUnAvail(bond.reinVestMethod))
            bond.reinVestMethod = Defaults.REINVESTMETH;
        return true;
    }

    private boolean defaultFloater(String instype) {
        double minMatLen = 0.;
        SchedTrack schedTrack = new SchedTrack();
        // we are always using a fixing curve for rates or just user entered coupon rates
        // eighter from user entry via reset rates of from a stored market fixing curve
        // thefixing curve is associated with a benchmark
        if (!instype.equals(Instype.FLOATER))
            return true;
        // do we have a maturity date specified by a floating term
        if (UnAvails.isUnAvail(bond.dtMaturity)) {
            if (UnAvails.isUnAvail(bond.dtDated) || UnAvails.isUnAvail(bond.floaterTerm)) {
                String errmsg = "No maturity date and no dated date specified - so can't determine maturity length for floater term";
                error.setError(getClass().getName(), errmsg);
                return false;
            } else
                bond.dtMaturity = dayCount.time.addMonthsToDate(bond.dtDated, bond.floaterTerm, 'Y');
        }
        if (UnAvails.isAvail(bond.cpnRt)) {
            bond.resetSchedRule = null;
            bond.isFixedrateYn = 'Y';
            bond.resetAvgRt = bond.cpnRt;
            return true;
        }
        bond.isFixedrateYn = 'N';
        // how are reset rates specified by reset rts or fising curve
        // the fixing curve specified by fixing rates and tenors
        // and creates fixing spot curve used to get reset rates in schedtrack

        // deals with reset dates when interest payment rates change
        if (bond.noResetDts < Limits.MINFIXINGRATES && UnAvails.isUnAvail(bond.resetPeriodicity)) {
            String errmsg = "No minimum number of reset dates(" + Limits.MINFIXINGRATES + ") and no reset schedule rule were specified so interest payments cannot be determeined";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        // user specifies a schedule rule
        if (UnAvails.isAvail(bond.resetPeriodicity)) {
            if (UnAvails.isUnAvail(bond.resetSchedRule.dtOccur)) {
                bond.resetSchedRule.dtOccur = bond.dtMaturity;
            }
            Dates dtLastReset = bond.intSchedRule.getNextSchedDate(SchedTracks.ONBEFOREDATE, bond.dtSettle);
            Dates dtNextReset = bond.resetSchedRule.getNextSchedDate(SchedTracks.AFTERDATE, dtLastReset);
            bond.noResetDts = 0;
            bond.dtResets[bond.noResetDts++] = dtLastReset;
            while (dtNextReset.leDate(bond.dtMaturity)) {
                bond.dtResets[bond.noResetDts++] = dtNextReset;
                dtLastReset = dtNextReset;
                dtNextReset = bond.resetSchedRule.getNextSchedDate(SchedTracks.AFTERDATE, dtLastReset);

            }
        } else {
            if (bond.noResetDts < Limits.MINFIXINGRATES) {
                String errmsg = "A minimum of " + Limits.MINFIXINGRATES + " floating rate resets was specified";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        // we now have a set of reset dates
        // if reset date  specified by reset schedule rule a fixing rates and matlen must have been entered
        // a user entered fixing curve must be entered or a benchmark must be entered
        // if a benchmark is entered use it as first priority
        // then a fixing curve must be entered or a benchmark and benchmark date must be entered
        YieldCurves fixingCurve = new YieldCurves();
        if (UnAvails.isAvail(bond.fixingBenchmark)) {
            if (UnAvails.isUnAvail(bond.dtFixingCurve)) {
                String errmsg = "No fixing curve benchmark date was supplied with the benchmark " + bond.fixingBenchmark;
                error.setError(getClass().getName(), errmsg);
                return false;
            }

            bond.fixingCurve = defaultFixingCurve(allYieldCurves, instype);
            if (error.hasErrorYn == 'Y')
                return false;
            int no = -1;
            while (++no < bond.noResetDts) {
                int matlen = dayCount.time.getMonthsBetween(bond.dtSettle, bond.dtResets[no], false);
                bond.resetRts[no] = fixingCurve.getCurveRate(matlen, bond.yieldForeCastMeth, 0);
            }
        } else { // user entered fixing yield curve manually
            // with fixing matlen and fixing rates
            if (bond.noFixingRate == 0) {
                String errmsg = "No fixing curve, no coupon rate and no fixing benchmark were entered";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (bond.noFixingRate != bond.noFixingTenor) {
                String errmsg = "No fixing rates(" + bond.noFixingRate + ") not equal to number of fixing tenors(" + bond.noFixingTenor + ")";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            bond.fixingCurve.spotCurve = utility.interpYieldCurve(bond.dtSettle, bond.dtResets, bond.noResetDts, bond.fixingRt, bond.fixingTenor, bond.noFixingRate);
            bond.resetAvgRt = 0.;
            int no = -1;
            while (++no < bond.fixingCurve.spotCurve.size()) {
                bond.resetRts[no] = bond.fixingCurve.spotCurve.get(no).spotRate;
                bond.resetAvgRt += bond.resetRts[no];
            }
            bond.resetAvgRt /= no;
            bond.noResetRts = no;
        }

        if (bond.noResetSprds == 0 && bond.noResetMultipliers == 0)
            return true;
        int no = -1;
        if (bond.noResetSprds == 0) {
            if (bond.noResetMultipliers == bond.noResetRts) {
                no = -1;
                while (++no < bond.noResetRts) {
                    bond.resetRts[no] = bond.resetRts[no] * bond.resetMultiplier[no];
                }
            } else {
                String errmsg = "An unequal  number reset multipliers(" + bond.noResetMultipliers + ") and reset rates(" + bond.noResetRts + ") have been specified";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (bond.noResetMultipliers == 0) {
            if (bond.noResetRts == bond.noResetSprds) {
                no = -1;
                while (++no < bond.noResetRts) {
                    bond.resetRts[no] += bond.resetSprd[no];
                }
            } else {
                String errmsg = "An unequal  number reset spreads(" + bond.noResetSprds + ") and reset rates(" + bond.noResetRts + ") have been specified";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (bond.noResetMultipliers != bond.noResetRts) {
            String errmsg = "An unequal  number reset multipliers(" + bond.noResetMultipliers + ") and reset rates(" + bond.noResetRts + ") have been specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        if (bond.noResetMultipliers != bond.noResetRts && bond.noResetMultipliers > 0) {
            String errmsg = "An unequal  number reset multipliers(" + bond.noResetMultipliers + ") and reset rates(" + bond.noResetRts + ") have been specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        if (bond.noResetMultipliers != bond.noResetSprds) {
            String errmsg = "An unequal  number reset multipliers(" + bond.noResetMultipliers + ") and reset spreads(" + bond.noResetSprds + ") have been specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        } else {
            no = -1;
            while (++no < bond.noResetRts) {
                bond.resetRts[no] = bond.resetRts[no] * bond.resetMultiplier[no] + bond.resetSprd[no];
            }
        }
        return true;
    }

    private boolean defaultIRSSwap(String instype) {
        double minMatLen = 0.;
        boolean isOk = true;
        if (!Instype.IRSSWAP.equals(instype))
            return true;
        // reset rates and reset dates are calculated here becuase they go together
        if (UnAvails.isUnAvail(bond.dtMaturity)) {
            String errmsg = "No maturity date and no dated date - so can't determine maturity length from floater term";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.swapPayType)) {
            bond.swapPayType = Codes.NETLEG;
        }

        if (UnAvails.isUnAvail(bond.swapSide))
            bond.swapSide = Codes.NETLEG;
        if (UnAvails.isUnAvail(bond.yieldForeCastMeth))
            bond.yieldForeCastMeth = CalcList.FORECASTSPOT;
        if (bond.noPaylegMultiplierDts != bond.noPaylegMultipliers) {
            String errmsg = "Unequal number of reset date multipliers(" + bond.noPaylegMultiplierDts + ") and reset multipliers(" + bond.noPaylegMultipliers;
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        if (UnAvails.isUnAvail(bond.paylegPeriodicity)) {
            String errmsg = "No pay leg payment schedule was specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        if (UnAvails.isUnAvail(bond.reclegPeriodicity)) {
            String errmsg = "No receive leg payment schedule was specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        if (bond.noPaylegMultipliers != bond.noPaylegSprds) {
            String errmsg = "Detected an unequal number of pay leg reset rate multipliers(" + bond.noPaylegMultipliers + ") and pay leg reset spreads(" + bond.noPaylegSprds + ")";
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        if (bond.noReclegMultiplierDts != bond.noReclegMultipliers) {
            String errmsg = "Unequal number of receive reset date multipliers(" + bond.noReclegMultiplierDts + ") and receive leg reset multipliers(" + bond.noReclegMultipliers;
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.noReclegMultipliers != bond.noReclegSprds) {
            String errmsg = "Detected an unequal number of receive leg reset rate multipliers(" + bond.noPaylegMultipliers + ") and receive leg reset spreads(" + bond.noPaylegSprds + ")";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        isOk = defaultPaylegResetSchedule();
        if (isOk == false)
            return false;
        if (UnAvails.isAvail(bond.cpnRtPayleg)) {
            bond.paylegFixedYn = 'Y';
        } else {
            isOk = defaultPaylegResetRates();
            if (isOk == false)
                return false;
        }
        isOk = defaultReclegResetSchedule();
        if (isOk == false)
            return false;
        bond.reclegFixedYn = 'N';
        if (UnAvails.isAvail(bond.cpnRtRecleg)) {
            bond.reclegFixedYn = 'Y';
        } else {
            isOk = defaultReclegResetRates();
            if (isOk == false)
                return false;
        }
        return true;
    }

    private boolean defaultPaylegResetRates() {
        boolean isOk = true;
        if (UnAvails.isAvail(bond.paylegBenchmark)) {
            bond.paylegFixingCurve = defaultFixingCurve(allYieldCurves, bond.instype);
            if (error.hasErrorYn == 'Y')
                return false;
            int nr = -1;
            double baseRt;
            // interpolate multipliers
            double[] multipliers = null;
            double[] sprds = null;
            int noPays = bond.noPaylegResetDts;
            int noMults = bond.noPaylegMultipliers;
            if (noMults > 0) {
                int pn = -1;
                while (++pn < noPays) {
                    Dates dt = bond.dtPaylegReset[pn];
                    multipliers[pn] = utility.interpDates(dt, bond.dtPaylegMultipliers, bond.paylegMultiplier, noMults);
                    sprds[pn] = utility.interpDates(dt, bond.dtPaylegMultipliers, bond.paylegSprd, noMults);
                }
            }

            while (++nr < bond.noPaylegResetDts) {
                int matLen = dayCount.getMonthCount(bond.dtSettle, bond.dtPaylegReset[nr], 'Y');
                matLen = (matLen < 0) ? -matLen : matLen;
                if (bond.yieldForeCastMeth.equals(CalcList.FORECASTSPOT))
                    baseRt = bond.paylegFixingCurve.getSpotRate(matLen, bond.yieldCurveInterpMeth);
                else
                    baseRt = bond.paylegFixingCurve.getForwardResetRate(bond.dtSettle, bond.dtPaylegReset[nr], bond.paylegResetSchedRule);
                if (bond.noPaylegMultipliers > 0)
                    bond.paylegResetRt[nr] = baseRt * multipliers[nr] + sprds[nr];
                else
                    bond.paylegResetRt[nr] = baseRt;
            }
            bond.noPaylegResetRts = nr;
            return true;
        } else {
            if (bond.noPaylegResetRts == 0) {
                String errmsg = "No pay leg reset rates or fixing curve were specified";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        return true;
    }

    private boolean defaultReclegResetRates() {
        if (UnAvails.isAvail(bond.reclegBenchmark)) {
            bond.reclegFixingCurve = defaultFixingCurve(allYieldCurves, bond.instype);
            double baseRt;
            double[] multipliers = null;
            double[] sprds = null;
            int noRecs = bond.noReclegResetDts;
            int noMults = bond.noReclegMultipliers;
            if (noMults > 0) {
                int pn = -1;
                while (++pn < noRecs) {
                    Dates dt = bond.dtReclegReset[pn];
                    multipliers[pn] = utility.interpDates(dt, bond.dtReclegMultipliers, bond.reclegMultiplier, noMults);
                    sprds[pn] = utility.interpDates(dt, bond.dtReclegMultipliers, bond.reclegSprd, noMults);
                }
            }
            int nr = -1;
            while (++nr < bond.noReclegResetDts) {
                int matLen = dayCount.getMonthCount(bond.dtSettle, bond.dtReclegReset[nr], 'Y');
                if (bond.yieldForeCastMeth.equals(CalcList.FORECASTSPOT))
                    baseRt = bond.reclegFixingCurve.getSpotRate(matLen, bond.yieldCurveInterpMeth);
                else
                    baseRt = bond.reclegFixingCurve.getForwardResetRate(bond.dtSettle, bond.dtReclegReset[nr], bond.reclegResetSchedRule);
                if (bond.noReclegMultipliers > 0)
                    bond.reclegResetRt[nr] = baseRt * multipliers[nr] + sprds[nr];
                else
                    bond.reclegResetRt[nr] = baseRt;
            }
            bond.noReclegResetDts = nr;
        } else {
            if (bond.noReclegResetRts == 0) {
                String errmsg = "No receive leg reset rates or fixing curve were specified";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        return true;
    }


    private boolean defaultFutureValues(String instype) {
        if (UnAvails.isUnAvail(bond.dtFuture)) {
            bond.dtFuture = dayCount.time.addMonthsToDate(bond.dtSettle, Defaults.FORWARDMONTHS, 'Y');
        }
        if (UnAvails.isUnAvail(bond.dtStart)) {
            bond.dtStart = bond.dtSettle;
        }
        if (UnAvails.isUnAvail(bond.mktPrcStart))
            if (UnAvails.isAvail(bond.settlePrc))
                bond.mktPrcStart = bond.settlePrc;
        return true;
    }

    private boolean defaultSinks(String instype) {
        if (!choices.inFloatSinkList(instype))
            return true;
        if (bond.noSinkDts == 0)
            return true;
        if (bond.noSinkAmts > 0 && bond.noSinkPcts > 0) {
            String errmsg = "Cannot specify both sinking fund amounts (" + bond.noSinkAmts + ") and percents (" + bond.noSinkPcts + ") in the same request";
            error.setError(getClass().getName(), errmsg);
        }
        int sNo = -1;
        int noSinks = bond.useDefaultSfigYn;
        if (bond.noSinkAmts > 0) {
            if (UnAvails.isUnAvail(bond.issuedFace)) {
                String errmsg = "No issued face amount was specified when supplying sinking fund amounts";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (bond.noSinkDts != bond.noSinkAmts) {
                String errmsg = "An unequal number of sink dates (" + bond.noSinkDts + ") and sink amounts (" + bond.noSinkAmts + ") have been supplied";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            while (++sNo < bond.noSinkAmts) {
                bond.sinkPcts[sNo] = bond.sinkAmts[sNo] / bond.issuedFace;
            }


        } else {
            if (bond.noSinkDts != bond.noSinkPcts) {
                String errmsg = "An unequal number of sink dates (" + bond.noSinkDts + ") and sink percents (" + bond.noSinkPcts + ") have been supplied";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }


        return true;
    }


    private boolean defaultIndexSchedule(String instype) {
        // do we need the schedule
        bond.idxSchedRule.setDtMaturity(bond.dtMaturity);
        if (!choices.indexList.contains(instype))
            return true;
        if (!choices.indexList.contains(instype)) {
            if (UnAvails.isUnAvail(bond.idxSchedRule.timeUnit) && UnAvails.isUnAvail(bond.idxSchedRule.repeatFactor))
                return true;
        }
        bond.idxSchedRule.weekend1 = bond.weekend1;
        bond.idxSchedRule.weekend2 = bond.weekend2;
        if (UnAvails.isUnAvail(bond.idxSchedRule.timeUnit)) {
            String errmsg = "No periodicity was specified for the index schedule";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.idxSchedRule.repeatFactor)) {
            String errmsg = "No pay frequency - REPEATFACTOR - was specified for the index schedule";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        bond.idxSchedRule.compoundFreq = dayCount.time.mapCompoundFreq(bond.idxSchedRule.timeUnit, bond.idxSchedRule.repeatFactor);
        if (UnAvails.isUnAvail(bond.idxSchedRule.dtOccur)) {
            String errmsg = "No occurrence date - a payment seed date - was specified for the index schedule";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.idxSchedRule.timeUnit == BondConsts.ENUMERATEDTIMEUNIT) {
            if (bond.noIdxCPIDts == 0) {
                String errmsg = "No enumerated index dates were specified when you indicated an enumerated index schedule";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (UnAvails.isUnAvail(bond.idxSchedRule.holidayRule))
            bond.idxSchedRule.holidayRule = BondConsts.IGNOREHOLIDAYS;
        if (UnAvails.isUnAvail(bond.idxSchedRule.useEndOfMonthRuleYn))
            bond.idxSchedRule.useEndOfMonthRuleYn = 'Y';
        return true;
    }

    private boolean defaultIndexRatioSchedule(String instype) {
        // do we need the schedule
        bond.idxRatioSchedRule.setDtMaturity(bond.dtMaturity);
        if (!choices.indexList.contains(instype))
            return true;
        if (UnAvails.isUnAvail(bond.idxRatioSchedRule.timeUnit) && UnAvails.isUnAvail(bond.idxRatioSchedRule.repeatFactor))
            return true;
        bond.idxRatioSchedRule.time = dayCount.time;
        bond.idxRatioSchedRule.holidays = bond.holidays;
        if (UnAvails.isUnAvail(bond.idxRatioSchedRule.timeUnit)) {
            String errmsg = "No time unit was specified for the index ratio schedule";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.idxRatioSchedRule.repeatFactor)) {
            String errmsg = "No pay frequency - REPEATFACTOR - was specified for the index ratio schedule";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        bond.idxRatioSchedRule.timeUnit = bond.idxRatioTimeUnit;
        bond.idxRatioSchedRule.compoundFreq = dayCount.time.mapCompoundFreq(bond.idxRatioSchedRule.timeUnit, bond.idxRatioSchedRule.repeatFactor);
        bond.idxRatioSchedRule.holidays = bond.holidays;
        bond.idxRatioSchedRule.weekend1 = bond.weekend1;
        bond.idxRatioSchedRule.weekend2 = bond.weekend2;
        if (UnAvails.isUnAvail(bond.idxRatioSchedRule.dtOccur)) {
            String errmsg = "No occurrence date - a payment seed date - was specified for the index ratio schedule";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.idxRatioSchedRule.timeUnit == BondConsts.ENUMERATEDTIMEUNIT) {
            if (bond.noIdxCPIDts == 0) {
                String errmsg = "No enumerated index ratio dates were specified when you indicated an enumerated index ratio schedule";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (UnAvails.isUnAvail(bond.idxRatioSchedRule.useEndOfMonthRuleYn))
            bond.idxRatioSchedRule.useEndOfMonthRuleYn = 'Y';
        return true;
    }

    private boolean defaultPayleg() {
        // do we need the schedule
        if (bond.paylegFixedYn == 'Y') {
            if (UnAvails.isUnAvail(bond.cpnRtPayleg)) {
                String errmsg = "No coupon rate was specified for the fixed rate of the payment leg of an IR Swap";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        //two possibilities - user enumerated both dates a nd rates
        // or specfied reset schedule rule and fixing curve
        // set the pay reset dates the reset rates
        if (bond.noPaylegResets > 0 && bond.noPaylegResets == bond.noPaylegDts) {
            return true;
        }
        bond.paylegSchedRule.compoundFreq = dayCount.time.mapCompoundFreq(bond.paylegSchedRule.timeUnit, bond.paylegSchedRule.repeatFactor);
        bond.paylegSchedRule.holidays = bond.holidays;
        bond.paylegSchedRule.timeUnit = bond.paylegTimeUnit;
        bond.paylegSchedRule.weekend1 = bond.weekend1;
        bond.paylegSchedRule.weekend2 = bond.weekend2;

        if (UnAvails.isUnAvail(bond.paylegSchedRule.timeUnit) || UnAvails.isUnAvail(bond.reclegSchedRule.repeatFactor)) {
            String errmsg = "No payment schedule was specified for the receive leg of an IR Swap";
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        if (UnAvails.isUnAvail(bond.paylegSchedRule.dtOccur)) {
            String errmsg = "No occurrence date - a payment seed date - was specified for the swap pay leg schedule";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.paylegSchedRule.timeUnit == BondConsts.ENUMERATEDTIMEUNIT) {
            if (bond.noPaylegDts == 0) {
                String errmsg = "No enumerated swap pay leg dates were specified when you indicated an enumerated swap pay leg schedule";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }

        if (UnAvails.isUnAvail(bond.paylegSchedRule.holidayRule))
            bond.paylegSchedRule.holidayRule = BondConsts.IGNOREHOLIDAYS;
        if (UnAvails.isUnAvail(bond.paylegSchedRule.useEndOfMonthRuleYn))
            bond.paylegSchedRule.useEndOfMonthRuleYn = 'Y';

        if (bond.noPaylegResets > 0 && bond.noPaylegResets == bond.noPaylegDts) {
            return true;
        }
        Dates dtLastReset = bond.dtSettle;
        Dates dtNextReset = bond.paylegResetSchedRule.getNextSchedDate(SchedTracks.AFTERDATE, dtLastReset);
        int no = 0;
        while (dtNextReset.leDate(bond.dtMaturity)) {
            bond.dtPaylegReset[no++] = dtNextReset;
            dtLastReset = dtNextReset;
            dtNextReset = bond.paylegResetSchedRule.getNextSchedDate(SchedTracks.AFTERDATE, dtLastReset);
        }
        if (UnAvails.isUnAvail(bond.creditAgency)) {
            String errmsg = "No fixing rate benchmark was specfied";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        bond.paylegFixingCurve = defaultFixingCurve(bond.fixingCurve, Instype.IRSSWAP);
        if (error.hasErrorYn == 'Y')
            return false;
        no = -1;
        while (++no < bond.noPaylegResets) {
            double matlen = dayCount.getMonthCount(bond.dtSettle, bond.dtPaylegReset[no], 'N');
            bond.paylegResetRt[no] = bond.paylegFixingCurve.getSpotRate(matlen, bond.yieldCurveInterpMeth);
        }
        return true;
    }

    private boolean defaultRecleg() {
        // do we neeUnAvails.isUnAvail(bond.paylegSchedRule.timeUnit)d the schedule
        if (bond.reclegFixedYn == 'Y') {
            if (UnAvails.isUnAvail(bond.cpnRtRecleg)) {
                String errmsg = "No coupon rate was specified for the fixed rate of the receive leg of an IR Swap";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (UnAvails.isUnAvail(bond.reclegSchedRule.timeUnit) || UnAvails.isUnAvail(bond.reclegSchedRule.repeatFactor)) {
            String errmsg = "No payment schedule was specified for the receive leg of an IR Swap";
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        if (UnAvails.isUnAvail(bond.reclegSchedRule.dtOccur)) {
            String errmsg = "No occurrence date - a payment seed date - was specified for the swap pay leg schedule";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.reclegSchedRule.timeUnit == BondConsts.ENUMERATEDTIMEUNIT) {
            if (bond.noReclegDts == 0) {
                String errmsg = "No enumerated swap pay leg dates were specified when you indicated an enumerated swap pay leg schedule";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        //two possibilities - user enumerated both dates a nd rates
        // or specfied reset schedule rule and fixing curve
        // set the pay reset dates the reset rates
        if (bond.noReclegResets > 0 && bond.noReclegResets == bond.noReclegDts) {
            return true;
        }
        bond.reclegSchedRule.compoundFreq = dayCount.time.mapCompoundFreq(bond.reclegSchedRule.timeUnit, bond.reclegSchedRule.repeatFactor);
        bond.reclegSchedRule.holidays = bond.holidays;
        bond.reclegSchedRule.timeUnit = bond.reclegTimeUnit;
        bond.reclegSchedRule.weekend1 = bond.weekend1;
        bond.reclegSchedRule.weekend2 = bond.weekend2;

        if (UnAvails.isUnAvail(bond.reclegSchedRule.holidayRule))
            bond.reclegSchedRule.holidayRule = BondConsts.IGNOREHOLIDAYS;
        if (UnAvails.isUnAvail(bond.reclegSchedRule.useEndOfMonthRuleYn))
            bond.reclegSchedRule.useEndOfMonthRuleYn = 'Y';
        Dates dtLastReset = bond.dtSettle;
        Dates dtNextReset = bond.reclegResetSchedRule.getNextSchedDate(SchedTracks.AFTERDATE, dtLastReset);
        int no = 0;
        while (dtNextReset.leDate(bond.dtMaturity)) {
            bond.dtReclegReset[no++] = dtNextReset;
            dtLastReset = dtNextReset;
            dtNextReset = bond.reclegResetSchedRule.getNextSchedDate(SchedTracks.AFTERDATE, dtLastReset);
        }
        if (UnAvails.isUnAvail(bond.creditAgency)) {
            String errmsg = "No fixing rate benchmark was specfied";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        bond.reclegFixingCurve = defaultFixingCurve(bond.fixingCurve, Instype.IRSSWAP);
        if (error.hasErrorYn == 'Y')
            return false;
        no = -1;
        while (++no < bond.noReclegResets) {
            double matlen = dayCount.getMonthCount(bond.dtSettle, bond.dtReclegReset[no], 'N');
            bond.reclegResetRt[no] = bond.reclegFixingCurve.getSpotRate(matlen, bond.yieldCurveInterpMeth);
        }
        return true;
    }

    private boolean defaultIndexed(String instype) {
        if (!instype.equals(Instype.INDEXED))
            return true;
        if (UnAvails.isUnAvail(bond.adjustMaturityYn))
            bond.adjustMaturityYn = 'Y';
        if (UnAvails.isUnAvail(bond.inflationRt))
            bond.inflationRt = 0.;
        if (UnAvails.isUnAvail(bond.sfigIdxValue))
            bond.sfigIdxValue = Defaults.SIGIDXVALUE;
        if (UnAvails.isUnAvail(bond.sfigIdxRatio))
            bond.sfigIdxRatio = Defaults.SFIGIDXRATIO;
        if (UnAvails.isUnAvail(bond.idxValueRoundYn))
            bond.idxValueRoundYn = Defaults.IDXVALUERNDYN;
        if (UnAvails.isUnAvail(bond.idxRatioRndYn))
            bond.idxRatioRndYn = Defaults.IDXRATIORNDYN;
        if (UnAvails.isUnAvail(bond.idxInterpMeth)) {
            bond.idxInterpMeth = BondConsts.INDEXINTERPNONE;
            String errmsg = "Defaulting CPI inflation interpolation method to " + bond.idxInterpMeth;
            error.setWarning(getClass().getName(), errmsg);
        }
        if (UnAvails.isUnAvail(bond.idxSchedRule.compoundFreq)) {
            bond.idxSchedRule.compoundFreq = Defaults.INDEXFREQ;
            String errmsg = "Defaulting inflation index frequency to " + Defaults.INDEXFREQ;
            error.setWarning(getClass().getName(), errmsg);
        }
        // default all index ratios after settle from last coupon date to maturity
        // default index ratios
        if (bond.dcIdxInterp == DayCount.DCMethod.DC_NA) {
            bond.dcIdxInterp = bond.dcAccruedInt;
        }
        if (UnAvails.isUnAvail(bond.adjustMaturityYn))
            bond.adjustMaturityYn = 'Y';
        if (UnAvails.isUnAvail(bond.idxCPIRatioAtSettle)) {
            if (UnAvails.isUnAvail(bond.idxCPIAtIssue) || UnAvails.isUnAvail(bond.idxCPIAtSettle)) {
                String errmsg = "Missing a CPI index ratio at settle";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            bond.idxCPIRatioAtSettle = bond.idxCPIAtSettle / bond.idxCPIAtIssue;
            if (UnAvails.isUnAvail(bond.idxRatioRndYn))
                bond.idxRatioRndYn = 'Y';
            bond.idxCPIRatioAtSettle = Utility.truncRound(bond.idxCPIRatioAtSettle, bond.sfigIdxRatio, bond.idxRatioRndYn);
        }

        return true;
    }

    private boolean defaultPaylegSchedule() {
        // do we need the schedule
        if (!bond.instype.equals(Instype.IRSSWAP))
            return true;
        bond.paylegSchedRule.setDtMaturity(bond.dtMaturity);
        if (UnAvails.isUnAvail(bond.paylegPeriodicity)) {
            String errmsg = "No periodicity was specified for the swap pay leg schedule";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        bond.paylegSchedRule.weekend1 = bond.weekend1;
        bond.paylegSchedRule.weekend2 = bond.weekend2;
        bond.paylegSchedRule.compoundFreq = dayCount.time.mapCompoundFreq(bond.paylegSchedRule.timeUnit, bond.paylegSchedRule.repeatFactor);
        if (UnAvails.isUnAvail(bond.paylegSchedRule.dtOccur)) {
            if (UnAvails.isAvail(bond.paylegSchedRule.dt1stPay))
                bond.paylegSchedRule.dtOccur = bond.paylegSchedRule.dt1stPay;
            else if (UnAvails.isAvail(bond.paylegSchedRule.dtLastOnlyCoupon))
                bond.paylegSchedRule.dtOccur = bond.paylegSchedRule.dtLastOnlyCoupon;
            else
                bond.paylegSchedRule.dtOccur = bond.dtMaturity;
        }
        if (bond.paylegSchedRule.timeUnit == BondConsts.ENUMERATEDTIMEUNIT) {
            if (bond.noPaylegDts == 0) {
                String errmsg = "No enumerated swap  pay leg dates were specified when you indicated an enumerated swap pay leg  schedule";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (UnAvails.isUnAvail(bond.basPayleg)) {
            String errmsg = "No daycount method was specified for the pay leg of an IR Swap";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.paylegSchedRule.holidayRule))
            bond.paylegSchedRule.holidayRule = BondConsts.IGNOREHOLIDAYS;
        if (UnAvails.isUnAvail(bond.paylegSchedRule.useEndOfMonthRuleYn))
            bond.paylegSchedRule.useEndOfMonthRuleYn = 'Y';
        // create payleg s
        return true;
    }


    private boolean defaultPaylegResetSchedule() {
        // do we need the schedule
        if (!bond.instype.equals(Instype.IRSSWAP))
            return true;
        if (UnAvails.isAvail(bond.cpnRtPayleg)) {
            bond.paylegFixedYn = 'Y';
            bond.intSchedRule = bond.paylegSchedRule;
            return true;
        }
        bond.paylegFixedYn = 'N';
        bond.paylegResetSchedRule.setDtMaturity(bond.dtMaturity);
        if (UnAvails.isUnAvail(bond.paylegResetPeriodicity)) {
            String errmsg = "No periodicity was specified for the swap pay leg reset schedule";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.dtDiscCurve))
            bond.dtDiscCurve = bond.dtSettle;
        if (UnAvails.isUnAvail(bond.paylegResetSchedRule.dtOccur)) {
            if (UnAvails.isAvail(bond.paylegResetSchedRule.dt1stPay))
                bond.paylegResetSchedRule.dtOccur = bond.paylegResetSchedRule.dt1stPay;
            else if (UnAvails.isAvail(bond.paylegResetSchedRule.dtLastOnlyCoupon))
                bond.paylegResetSchedRule.dtOccur = bond.paylegResetSchedRule.dtLastOnlyCoupon;
            else
                bond.paylegResetSchedRule.dtOccur = bond.dtMaturity;
        }
        bond.paylegResetSchedRule.weekend1 = bond.weekend1;
        bond.paylegResetSchedRule.weekend2 = bond.weekend2;
        bond.paylegResetSchedRule.compoundFreq = dayCount.time.mapCompoundFreq(bond.paylegResetSchedRule.timeUnit, bond.paylegResetSchedRule.repeatFactor);
        if (UnAvails.isUnAvail(bond.paylegResetSchedRule.dtOccur)) {
            bond.paylegResetSchedRule.dtOccur = bond.dtMaturity;
        }
        if (bond.paylegResetSchedRule.timeUnit == BondConsts.ENUMERATEDTIMEUNIT) {
            if (bond.noPaylegResetDts == 0) {
                String errmsg = "No enumerated swap reset pay leg dates were specified when you indicated an enumerated swap pay leg reset schedule";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (UnAvails.isUnAvail(bond.paylegResetSchedRule.holidayRule))
            bond.paylegResetSchedRule.holidayRule = BondConsts.IGNOREHOLIDAYS;
        if (UnAvails.isUnAvail(bond.paylegResetSchedRule.useEndOfMonthRuleYn))
            bond.paylegResetSchedRule.useEndOfMonthRuleYn = 'Y';
        // create payleg resets
        Dates dtLastReset = bond.paylegSchedRule.getNextSchedDate(SchedTracks.ONBEFOREDATE, bond.dtSettle);
        int no = 0;
        Dates dtNextReset = bond.paylegResetSchedRule.getNextSchedDate(SchedTracks.AFTERDATE, dtLastReset);
        while (dtNextReset.leDate(bond.dtMaturity)) {
            bond.dtPaylegReset[no++] = dtNextReset;
            dtLastReset = dtNextReset;
            dtNextReset = bond.paylegResetSchedRule.getNextSchedDate(SchedTracks.AFTERDATE, dtLastReset);
        }
        bond.noPaylegResetDts = no;
        return true;
    }


    private boolean defaultReclegSchedule() {
        // do we need the schedule
        if (!bond.instype.equals(Instype.IRSSWAP))
            return true;
        bond.reclegSchedRule.setDtMaturity(bond.dtMaturity);
        if (UnAvails.isUnAvail(bond.reclegPeriodicity)) {
            String errmsg = "No periodicity was specified for the swap receive leg schedule";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        bond.reclegSchedCompoundFreq = dayCount.time.mapCompoundFreq(bond.reclegSchedRule.timeUnit, bond.reclegSchedRule.repeatFactor);
        bond.reclegSchedRule.compoundFreq = bond.reclegSchedCompoundFreq;
        bond.reclegSchedRule.weekend1 = bond.weekend1;
        bond.reclegSchedRule.weekend2 = bond.weekend2;
        if (UnAvails.isUnAvail(bond.reclegSchedRule.dtOccur)) {
            if (UnAvails.isAvail(bond.reclegSchedRule.dt1stPay))
                bond.reclegSchedRule.dtOccur = bond.reclegSchedRule.dt1stPay;
            else if (UnAvails.isAvail(bond.reclegSchedRule.dtLastOnlyCoupon))
                bond.reclegSchedRule.dtOccur = bond.reclegSchedRule.dtLastOnlyCoupon;
            else
                bond.reclegSchedRule.dtOccur = bond.dtMaturity;
        }

        if (bond.reclegSchedRule.timeUnit == BondConsts.ENUMERATEDTIMEUNIT) {
            if (bond.noReclegDts == 0) {
                String errmsg = "No enumerated swap pay leg dates were specified when you indicated an enumerated swap receive leg schedule";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (UnAvails.isUnAvail(bond.basRecleg)) {
            String errmsg = "No daycount method was specified for the receive leg of an IR Swap";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.reclegSchedRule.holidayRule))
            bond.reclegSchedRule.holidayRule = BondConsts.IGNOREHOLIDAYS;
        if (UnAvails.isUnAvail(bond.reclegSchedRule.useEndOfMonthRuleYn))
            bond.reclegSchedRule.useEndOfMonthRuleYn = 'Y';
        return true;
    }

    private boolean defaultReclegResetSchedule() {
        // do we need the schedule
        if (!bond.instype.equals(Instype.IRSSWAP))
            return true;
        bond.reclegResetSchedRule.setDtMaturity(bond.dtMaturity);
        if (UnAvails.isAvail(bond.cpnRtRecleg)) {
            bond.reclegFixedYn = 'Y';
            bond.intSchedRule = bond.reclegSchedRule;
            return true;
        }
        if (UnAvails.isUnAvail(bond.reclegResetSchedRule.timeUnit) && UnAvails.isUnAvail(bond.reclegResetSchedRule.repeatFactor))
            return true;

        if (UnAvails.isUnAvail(bond.reclegResetPeriodicity)) {
            String errmsg = "No time unit was specified for the swap receive leg reset schedule";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        bond.reclegResetSchedRule.compoundFreq = dayCount.time.mapCompoundFreq(bond.reclegResetSchedRule.timeUnit, bond.reclegResetSchedRule.repeatFactor);
        bond.reclegResetSchedRule.weekend1 = bond.weekend1;
        bond.reclegResetSchedRule.weekend2 = bond.weekend2;
        if (UnAvails.isUnAvail(bond.reclegResetSchedRule.dtOccur)) {
            if (UnAvails.isAvail(bond.reclegResetSchedRule.dt1stPay))
                bond.reclegResetSchedRule.dtOccur = bond.reclegResetSchedRule.dt1stPay;
            else if (UnAvails.isAvail(bond.reclegResetSchedRule.dtLastOnlyCoupon))
                bond.reclegResetSchedRule.dtOccur = bond.reclegResetSchedRule.dtLastOnlyCoupon;
            else
                bond.reclegResetSchedRule.dtOccur = bond.dtMaturity;
        }

        if (bond.reclegResetSchedRule.timeUnit == BondConsts.ENUMERATEDTIMEUNIT) {
            if (bond.noReclegResetDts == 0) {
                String errmsg = "No enumerated swap receive leg dates were specified when you indicated an enumerated swap receive leg reset schedule";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (UnAvails.isUnAvail(bond.reclegResetSchedRule.holidayRule))
            bond.reclegResetSchedRule.holidayRule = BondConsts.IGNOREHOLIDAYS;
        if (UnAvails.isUnAvail(bond.reclegResetSchedRule.useEndOfMonthRuleYn))
            bond.reclegResetSchedRule.useEndOfMonthRuleYn = 'Y';
        Dates dtLastReset = bond.paylegSchedRule.getNextSchedDate(SchedTracks.ONBEFOREDATE, bond.dtSettle);
        bond.dtReclegReset[0] = dtLastReset;
        Dates dtNextReset = bond.paylegResetSchedRule.getNextSchedDate(SchedTracks.AFTERDATE, dtLastReset);
        int nr = 1;
        while (dtNextReset.leDate(bond.dtMaturity)) {
            bond.dtReclegReset[nr++] = dtNextReset;
            dtLastReset = dtNextReset;
            dtNextReset = bond.reclegResetSchedRule.getNextSchedDate(SchedTracks.AFTERDATE, dtLastReset);
        }
        bond.noReclegResetDts = nr;
        return true;
    }

    private boolean defaultResetSchedule(String instype) {
        if (!instype.equals(Instype.FLOATER))
            return true;
        char use360Yn = 'Y';
        if (bond.noResetDts > 0) {
            return true;
        }
        bond.resetAvgRt = bond.cpnRt;
        if (UnAvails.isAvail(bond.cpnRt))
            return true;
        bond.resetSchedRule.weekend1 = bond.weekend1;
        bond.resetSchedRule.weekend2 = bond.weekend2;
        if (UnAvails.isUnAvail(bond.resetSchedRule.timeUnit)) {
            String errmsg = "No set of reset dates or reset schedule rule was specified - so no way to calculate interest payments.";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.resetSchedRule.dtOccur)) {
            if (UnAvails.isAvail(bond.dtMaturity)) {
                bond.resetSchedRule.dtOccur = bond.dtMaturity;
            } else {
                String errmsg = "no date of occurance for the reset rate schedule rule was specified - so no way to calculate interest payments.";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (UnAvails.isUnAvail(bond.resetSchedRule.repeatFactor)) {
            String errmsg = "no reset rate periodicity was specified for floating rate rest dates - so no way to calculate interest payments.";
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        if (UnAvails.isUnAvail(bond.resetSchedRule.holidayRule))
            bond.resetSchedRule.holidayRule = BondConsts.IGNOREHOLIDAYS;
        bond.resetSchedRule.country = bond.country;
        bond.resetSchedRule.dtMaturity = bond.dtMaturity;
        bond.resetSchedRule.useEndOfMonthRuleYn = bond.resetSchedEOMRuleYn;
        bond.resetSchedRule.dtDated = bond.dtDated;
        bond.resetSchedRule.dt1stPay = bond.dt1stPay;
        bond.resetSchedRule.dtLastOnlyCoupon = bond.dtLastPay;
        bond.resetSchedRule.dtLastUserCpn = bond.resetSchedRule.dtLastUserCpn;
        bond.resetSchedRule.dtNextUserCpn = bond.resetSchedRule.dtNextUserCpn;
        bond.resetSchedRule.dayOfMonth = bond.accrueSchedRule.dayOfMonth;
        bond.resetSchedRule.monthOfYear = bond.accrueSchedRule.monthOfYear;
        bond.resetSchedRule.weekOfMonth = bond.accrueSchedRule.weekOfMonth;
        bond.resetSchedRule.dayCountMeth = bond.dcAccruedInt;
        bond.resetSchedRule.weekend1 = bond.weekend1;
        bond.resetSchedRule.weekend2 = bond.weekend2;
        bond.status.useResetDateSchedRule = true;
        return true;
    }

    public boolean defaultMortgage(int calcNo, String instype) {
        Dates dtMature = new Dates();
        char endOfMonthRuleYN = 'N';
        char use360Yn = 'Y';
        if (!choices.inPassthruList(instype))
            return true;

        if (UnAvails.isUnAvail(bond.delaydaysStated)) {
            if (UnAvails.isUnAvail(bond.delaydaysAct)) {
                String errmsg = "No delay delays stated or actual for mortgage were specified";
                error.setError(getClass().getName(), errmsg);
                return false;
            } else
                bond.delaydaysStated = bond.delaydaysAct - 30;
        }
        if (UnAvails.isUnAvail(bond.dtLastPayment)) {
            if (UnAvails.isAvail(bond.dtMaturity))
                bond.dtLastPayment = dayCount.time.addDaysToDate(bond.dtMaturity, bond.delaydaysStated, use360Yn);
            else {
                String errmsg = "No last payment date or legal maturity date was specified";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }

        bond.matTerm = (int) dayCount.time.getMonthsBetween(bond.dtDated, bond.dtMaturity, true);

        if (UnAvails.isUnAvail(bond.prepaySpeed)) {
            String errmsg = "No prepayment speed wae specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.serviceRt))
            bond.serviceRt = 0.;
        if (UnAvails.isUnAvail(bond.passPayoff))
            bond.passPayoff = 0.;
        return true;
    }

    public Dates setOccurDate(Dates dtMature, Dates dt1stPay, Dates dtOddLast) {
        Dates dtOccur;
        if (dtOddLast.year != UnAvails.NOTAVAILDATE)
            dtOccur = dtOddLast;
        else if (dt1stPay.year != UnAvails.NOTAVAILDATE)
            dtOccur = dt1stPay;
        else if (dtMature.year != UnAvails.NOTAVAILDATE)
            dtOccur = dtMature;
        else {
            error.setError(getClass().getName(), " No occurence date specified so could default the occruance date");
            return dtOccur = new Dates();
        }
        return dtOccur;
    }

    private boolean defaultSecurityType() {
        if (UnAvails.isUnAvail(bond.securityType)) {
            if (bond.isZeroCouponYn == 'Y') {
                bond.instype = Instype.DISCOUNT;
                return true;
            }
            if (bond.isAtmaturityYn == 'Y') {
                bond.instype = Instype.ATMATURITY;
                return true;
            }
            if (bond.isFixedrateYn == 'Y') {
                bond.instype = Instype.FIXEDRATE;
                return true;
            }
            if (bond.isFloaterYn == 'Y') {
                bond.instype = Instype.FLOATER;
                return true;
            }
            if (bond.isMortgageYn == 'Y') {
                bond.instype = Instype.MORTGAGE;
                return true;
            }
            if (bond.isCashYn == 'Y') {
                bond.instype = Instype.CURRENCY;
                return true;
            }
            if (bond.isInflationProtectedYn == 'Y') {
                bond.instype = Instype.INDEXED;
                return true;
            }
            if (bond.isSteppedYn == 'Y') {
                bond.instype = Instype.FLOATER;
                return true;
            }
            if (UnAvails.isUnAvail(bond.instype)) {
                error.setError(getClass().getName(), "No security type or instrument type was specified");
                return false;
            }
            return true;
        }
        boolean found = false;
        int ir = -1;
        while (++ir < bond.securityTypes.securityTypeList.size()) {
            String secType = bond.securityTypes.securityTypeList.get(ir).sectype.toUpperCase();
            if (secType.equals(bond.securityType)) {
                found = true;
                if (UnAvails.isUnAvail(bond.basAccInt)) {
                    bond.basAccInt = bond.securityTypes.securityTypeList.get(ir).basIntAccrued;
                    bond.dcAccruedInt = dayCount.mapAccrualBasis(bond.basAccInt);
                }
                if (UnAvails.isUnAvail(bond.intSchedRepeat))
                    bond.intSchedRule.repeatFactor = bond.securityTypes.securityTypeList.get(ir).repeatFactor;
                if (UnAvails.isUnAvail(bond.intSchedTimeUnit))
                    bond.intSchedRule.timeUnit = bond.securityTypes.securityTypeList.get(ir).timeUnit;
                if (UnAvails.isUnAvail(bond.intSchedRule.compoundFreq = bond.securityTypes.securityTypeList.get(ir).compoundFreq))
                    bond.intSchedRule.compoundFreq = bond.securityTypes.securityTypeList.get(ir).repeatFactor;
                if (UnAvails.isUnAvail(bond.secDescr))
                    bond.secDescr = bond.securityTypes.securityTypeList.get(ir).secDescr;
                if (UnAvails.isUnAvail(bond.cleanPrcYn))
                    bond.cleanPrcYn = bond.securityTypes.securityTypeList.get(ir).cleanPrice;
                if (UnAvails.isUnAvail(bond.instype))
                    bond.instype = bond.securityTypes.securityTypeList.get(ir).instype.toUpperCase();
                if (UnAvails.isUnAvail(bond.country))
                    bond.country = bond.securityTypes.securityTypeList.get(ir).isoCountry;
                if (UnAvails.isUnAvail(bond.accruedIntSize))
                    bond.accruedIntSize = bond.securityTypes.securityTypeList.get(ir).accruedSize;
                if (UnAvails.isUnAvail(bond.accruedIntRndYn))
                    bond.accruedIntRndYn = bond.securityTypes.securityTypeList.get(ir).accruedRoundYn;
                if (UnAvails.isUnAvail(bond.sfigCurrency))
                    bond.sfigCurrency = bond.securityTypes.securityTypeList.get(ir).sfigCurrency;
                if (UnAvails.isUnAvail(bond.accruedIntMeth))
                    bond.accruedIntMeth = bond.securityTypes.securityTypeList.get(ir).accruedMeth;
                if (UnAvails.isUnAvail(bond.sfigAccruedSize))
                    bond.sfigAccruedSize = bond.securityTypes.securityTypeList.get(ir).sfigAccruedSize;
                if (UnAvails.isUnAvail(bond.sfigCurrency))
                    bond.sfigCurrency = bond.securityTypes.securityTypeList.get(ir).sfigAccruedSize;
                if (UnAvails.isUnAvail(bond.passModel))
                    bond.passModel = bond.securityTypes.securityTypeList.get(ir).passModel;
                if (UnAvails.isUnAvail(bond.passProjectMeth))
                    bond.passProjectMeth = bond.securityTypes.securityTypeList.get(ir).passProjectMeth;
                if (UnAvails.isUnAvail(bond.stripType))
                    bond.stripType = bond.securityTypes.securityTypeList.get(ir).stripType;
                if (UnAvails.isUnAvail(bond.stateTaxExemptYn))
                    bond.stateTaxExemptYn = bond.securityTypes.securityTypeList.get(ir).stateTaxExempt;
                if (UnAvails.isUnAvail(bond.fedTaxExemptYn))
                    bond.fedTaxExemptYn = bond.securityTypes.securityTypeList.get(ir).fedTaxExempt;
                if (UnAvails.isUnAvail(bond.resetPeriodicity)) {
                    String strResetTimeUnit = bond.securityTypes.securityTypeList.get(ir).resetTimeUnit;
                    int resetRepeatfactor = bond.securityTypes.securityTypeList.get(ir).resetRepeatFactor;
                    bond.resetPeriodicity = strResetTimeUnit + resetRepeatfactor;
                }
                break;
            }
        }
        if (found == false) {
            String errmsg = "You have specified an invalid security type " + bond.securityType;
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        return true;
    }


    boolean defaultPayTerms(String instype) {
        if (UnAvails.isUnAvail(bond.accruedIntMeth)) {
            bond.accruedIntMeth = Defaults.ACCRUEMETH;
            String errmsg = "defaulting accrued interest method to " + Defaults.ACCRUEMETH;
            error.setWarning(getClass().getName(), errmsg);
        }
        if (UnAvails.isUnAvail(bond.dtLastPayment))
            bond.dtLastPayment = bond.dtMaturity;
        if (UnAvails.isUnAvail(bond.useCurrentRedemptionYn))
            bond.useCurrentRedemptionYn = 'Y';

        if (bond.cpnQuoteType.equals(BondConsts.SEMIEQV)) {
            if (UnAvails.isAvail(bond.cpnRtQuote))
                bond.cpnRt = -1 + Math.sqrt(1. + bond.cpnRtQuote);
            else
                bond.cpnRt = -1 + Math.sqrt(1. + bond.cpnRt);
        }

        if (UnAvails.isUnAvail(bond.accruedIntMeth)) {
            bond.accruedIntMeth = "STD";
            return true;
        }

        if (bond.accruedIntMeth.equals("SIZE")) {
            if (UnAvails.isUnAvail(bond.accruedIntSize))
                bond.accruedIntSize = Defaults.ACCINTSIZE;
            if (UnAvails.isUnAvail(bond.accruedIntSizeRndYn))
                bond.accruedIntSizeRndYn = 'Y';
            return true;
        }

        if (UnAvails.isUnAvail(bond.sfigAccruedPrin))
            bond.sfigAccruedPrin = Defaults.SFIGACCRUEDPRIN;

        if (UnAvails.isUnAvail(bond.accruedIntPrinRndYn))
            bond.accruedIntPrinRndYn = 'Y';

        if (UnAvails.isUnAvail(bond.sfigAccruedDaycount))
            bond.sfigAccruedDaycount = Defaults.SFIGACCRUEDDAYCOUNT;
        if (UnAvails.isUnAvail(bond.accruedIntCouponRndYn))
            bond.accruedIntDaycountRndYn = 'Y';

        if (UnAvails.isUnAvail(bond.sfigAccruedCoupon))
            bond.sfigAccruedCoupon = Defaults.SFIGACCRUEDCOUPON;
        if (UnAvails.isUnAvail(bond.accruedIntPrinRndYn))
            bond.accruedIntPrinRndYn = 'Y';

        if (UnAvails.isUnAvail(bond.sfigAccruedDaycount)) {
            bond.sfigAccruedDaycount = Defaults.SFIGACCRUEDDAYCOUNT;
            if (UnAvails.isUnAvail(bond.accruedIntDaycountRndYn))
                bond.accruedIntDaycountRndYn = 'Y';
        }
        return true;
    }


    boolean assignSchedTiming(String schedType, String timeUnit, int repeatFactor, int compoundFreq) {
        boolean isOk;
        String errMsg;

        if (!choices.inSchedRuleList(schedType)) {
            errMsg = "Invalid schedule rule " + schedType + " detected";
            error.setError(getClass().getName(), errMsg);
            return false;
        }

        if (UnAvails.isUnAvail(timeUnit) || UnAvails.isUnAvail(repeatFactor)) {
            if (UnAvails.isUnAvail(compoundFreq)) {
                error.setError(getClass().getName(), "No schedule frequency parameters were specified for " + schedType + " schedule");
                return false;
            } else
                repeatFactor = compoundFreq;

        }
        return true;
    }

    private boolean defaultDelimiters(String isJsonInputYn) {
        if (UnAvails.isUnAvail(bond.nameDelimiter)) {
            if (isJsonInputYn.equals("Y"))
                bond.nameDelimiter = ":";
            else
                bond.nameDelimiter = "=";
        }
        if (UnAvails.isUnAvail(bond.calcDelimiter))
            bond.calcDelimiter = ";";

        return true;
    }

    private boolean defaultReinvestRates(String instype) {
        // are reinvestment rates relevant?

        if (bond.noReinRts > 0) {
            if (bond.noReinRts != bond.noReinMatlens) {
                String errmsg = "Unequal number of reinvestment rates " + bond.noReinRts + " and reinvestment maturity lengths " + bond.noReinMatlens;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            bond.reinVestMethod = "REINRTS";
            return true;
        }
        if (UnAvails.isUnAvail(bond.reinVestMethod)) {
            bond.reinVestMethod = "YTM";
        }
        return true;
    }

    private boolean bypassDefaults(ArrayList<Integer> calculationList) {
        int i = -1, no = 0, noList = calculationList.size();

        while (++i < noList) {
            int calcNo = calculationList.get(i);
            if (choices.bypassDefaultList.contains(calcNo))
                ++no;
        }
        if (no == noList)
            return true;
        return false;
    }

    private YieldCurves defaultYieldCurve(YieldCurves allYieldCurves, YieldCurveParams yldParams) {
        // can we read from file
        // int lookAhead = (int) dayCount.time.getMonthsBetween(bond.dtSettle, yldParams.dtCurve, true);
        YieldCurves bondCurves = new YieldCurves(cmdLine, error, dayCount, utility);
        bondCurves.setYieldParams(yldParams);
        int lookAhead = dayCount.getMonthCount(bond.dtSettle, yldParams.dtCurve, 'Y');
        if (yldParams.noPoints == 0) {  // use in memory yield curve
            if (yldParams.isRiskFreeYn == 'Y') {
                if (UnAvails.isUnAvail(yldParams.creditAgency) || UnAvails.isUnAvail(yldParams.spotCurveInterval)) {
                    String errmsg = "No credit agency or credit rating was specified for a discount yield curve";
                    error.setError(getClass().getName(), Error.MISSINGDATA, errmsg);
                    return bondCurves;
                }
                if (allYieldCurves == null) {
                    String errmsg = "No yield curves were loaded";
                    error.setError(getClass().getName(), errmsg);
                    return bondCurves;
                }
            }
            if (allYieldCurves.spotCurve.isEmpty()) {
                String errmsg = "No spot curves were loaded";
                error.setError(getClass().getName(), errmsg);
                return bondCurves;
            }
            // need to set spot curve and par curve to null
            if (yldParams.yieldType.equals(Codes.FIXINGCURVE))
                bondCurves.spotCurve = allYieldCurves.getFixingSpotCurve(yldParams);
            else
                bondCurves.spotCurve = allYieldCurves.getDiscSpotCurve(yldParams);
            if (error.hasErrorYn == 'Y')
                return bondCurves;
            bondCurves.setYieldParams(yldParams);
            if (bondCurves.spotCurve.size() == 0 && yldParams.isRiskFreeYn == 'Y') {
                String errmsg = "No spot curves found on " + yldParams.dtCurve.getDateString();
                error.setError(getClass().getName(), errmsg);
                return bondCurves;
            } else
                error.clearError();
            if (bondCurves.spotCurve.size() > Limits.MINYIELDPOINTS) {
                bondCurves.d2YSpline = bondCurves.setCubicSpline(1.0e30, 1.0e30);
                bondCurves.forwardCurve = bondCurves.calcforwardCurve(allYieldCurves.forwardCurve, bondCurves.spotCurve, yldParams, lookAhead);

            }
            return bondCurves;
        }
        // create the yield curve from user entered data
        bondCurves.parCurve = bondCurves.setBaseCurve(yldParams);
        bondCurves.spotCurve = bondCurves.setDiscSpotCurve(bondCurves.parCurve, yldParams);
        bondCurves.d2YSpline = bondCurves.setCubicSpline(1.0e30, 1.0e30);
        if (bond.yieldForeCastMeth.equals(CalcList.FORECASTFORWARD)) {
            bondCurves.calcforwardCurve(allYieldCurves.forwardCurve, bondCurves.spotCurve, yldParams, lookAhead);
        }
        return bondCurves;
    }

    private YieldCurves defaultDiscRfCurve(YieldCurves allYieldCurves) {
        YieldCurves bondCurves = new YieldCurves(cmdLine, error, dayCount, utility);
        YieldCurveParams yldParams = new YieldCurveParams();
        yldParams.spotCurveInterval = BondConsts.SPOTINTERVAl;
        yldParams.yieldCurveInterpMeth = bond.yieldCurveInterpMeth;
        String spotFileName = bond.commandLine.rootDirectory + "yieldCurves\\" + BondConsts.SPOTCURVEVOLFILENAME;
        yldParams.setCredit(BondConsts.RISKFREECURVE, spotFileName, bond.creditAgencyRf, bond.creditRatingRf, "NA");
        yldParams.setTypes(cmdLine, 'Y', bond.discRtParYn, 'Y', 'N');
        yldParams.setDates(bond.dtSettle, bond.dtSettle, bond.dtSettle);
        yldParams.setRates(bond.noDiscRate, bond.discRt, bond.discTenor, bond.discRt, bond.discStdDev, bond.discRtDn, bond.discRtUp, bond.discRtDn, bond.discRtUp);
        boolean isOk = checkYieldParameters(yldParams);
        if (!isOk)
            return bondCurves;

        bondCurves = defaultYieldCurve(allYieldCurves, yldParams);
        return bondCurves;
    }


    private YieldCurves defaultDiscSprdCurve(YieldCurves allYieldCurves) {
        YieldCurves bondCurves = new YieldCurves(cmdLine, error, dayCount, utility);
        YieldCurveParams yldParams = new YieldCurveParams();
        yldParams.setOptions(bond.yieldCurveInterpMeth, BondConsts.SPOTINTERVAl, bond.dtDiscCurve);
        yldParams.setTypes(cmdLine, bond.discSprdParYn, 'N', 'N', 'N');
        String spotFileName = bond.commandLine.rootDirectory + "yieldCurves\\" + BondConsts.SPOTCURVEVOLFILENAME;
        yldParams.setCredit(BondConsts.SPRDCURVE, spotFileName, bond.creditAgencySprd, bond.creditRatingSprd, "NA");
        yldParams.setDates(bond.dtSettle, bond.dtDiscCurve, bond.dtSettle);
        yldParams.setRates(bond.noDiscSprd, bond.discSprd, bond.discSprdTenor, bond.discSprd, bond.discSprdStdDev, bond.discSprdDn, bond.discSprdUp, bond.discSprdDn, bond.discSprdUp);
        boolean isOk = checkYieldParameters(yldParams);
        if (!isOk)
            return bondCurves;
        bondCurves = defaultYieldCurve(allYieldCurves, yldParams);
        return bondCurves;
    }


    private YieldCurves defaultDiscFutSprdCurve(YieldCurves allYieldCurves) {
        YieldCurves bondCurves = new YieldCurves(cmdLine, error, dayCount, utility);
        YieldCurveParams yldParams = new YieldCurveParams();
        String spotFileName = bond.commandLine.rootDirectory + "yieldCurves\\" + BondConsts.SPOTCURVEVOLFILENAME;
        yldParams.setCredit(BondConsts.SPRDCURVE, spotFileName, bond.creditAgencySprd, bond.creditRatingSprd, "NA");
        yldParams.setTypes(cmdLine, 'Y', bond.discFutSprdParYn, 'N', 'Y');
        yldParams.setDates(bond.dtFuture, bond.dtDiscFutCurve, bond.dtFuture);
        yldParams.setOptions(bond.yieldCurveInterpMeth, BondConsts.SPOTINTERVAl, bond.dtFuture);
        yldParams.setRates(bond.noDiscFutSprd, bond.discFutSprd, bond.discFutSprdTenor, bond.discFutSprd, bond.discFutSprdStdDev, bond.discFutSprdDn, bond.discFutSprdUp, bond.discFutSprdDn, bond.discFutSprdUp);
        bondCurves.setYieldParams(yldParams);
        boolean isOk = checkYieldParameters(yldParams);
        bondCurves = defaultYieldCurve(allYieldCurves, yldParams);
        return bondCurves;
    }

    private YieldCurves defaultDiscFutRfCurve(YieldCurves allYieldCurves) {
        YieldCurves bondCurves = new YieldCurves(cmdLine, error, dayCount, utility);
        YieldCurveParams yldParams = new YieldCurveParams();
        String spotFileName = bond.commandLine.rootDirectory + "yieldCurves\\" + BondConsts.SPOTCURVEVOLFILENAME;
        yldParams.setOptions(bond.yieldCurveInterpMeth, BondConsts.SPOTINTERVAl, bond.dtFuture);
        yldParams.setCredit(BondConsts.RISKFREECURVE, spotFileName, bond.creditAgencyRf, bond.creditRatingRf, "NA");
        yldParams.setTypes(cmdLine, 'Y', bond.discFutRtParYn, 'Y', 'Y');
        yldParams.setDates(bond.dtFuture, bond.dtFuture, bond.dtFuture);
        yldParams.setRates(bond.noDiscFutRate, bond.discFutRt, bond.discFutTenor, bond.discFutRt, bond.discFutStdDev, bond.discFutRtDn, bond.discFutRtUp, bond.discFutRtDn, bond.discFutRtUp);
        bondCurves.setYieldParams(yldParams);
        boolean isOk = checkYieldParameters(yldParams);
        if (!isOk)
            return bondCurves;
        bondCurves = defaultYieldCurve(allYieldCurves, yldParams);
        return bondCurves;
    }


    public boolean checkYieldParameters(YieldCurveParams yldParams) {
        int noRts = 0, noTenors = 0, noStdDev = 0, noRtDn = 0, noRtUp = 0;
        String dataName = "";
        if (yldParams.isDiscYn == 'Y') {
            if (yldParams.isRiskFreeYn == 'Y' && yldParams.isFutureYn == 'N') {
                noRts = bond.noDiscRate;
                noTenors = bond.noDiscTenor;
                noStdDev = bond.noDiscStdDev;
                noRtUp = bond.noDiscRtUp;
                noRtDn = bond.noDiscRtDn;
                dataName = "discount risk free";
                yldParams.noPoints = noRts;

            }
            if (yldParams.isRiskFreeYn == 'Y' && yldParams.isFutureYn == 'Y') {
                noRts = bond.noDiscFutRate;
                noTenors = bond.noDiscFutTenor;
                noStdDev = bond.noDiscFutStdDev;
                noRtUp = bond.noDiscFutRtUp;
                noRtDn = bond.noDiscFutRtDn;
                dataName = "discount future credit spread";
                yldParams.noPoints = noRts;
            }

            if (yldParams.isRiskFreeYn == 'N' && yldParams.isFutureYn == 'N') {
                noRts = bond.noDiscSprd;
                noTenors = bond.noDiscSprdTenor;
                noStdDev = noRts;
                noRtUp = noRts;
                noRtDn = noRts;
                dataName = "discount credit spread";
                yldParams.noPoints = noRts;
            }
            if (yldParams.isRiskFreeYn == 'N' && yldParams.isFutureYn == 'Y') {
                noRts = bond.noDiscFutSprd;
                noTenors = bond.noDiscFutSprdTenor;
                noStdDev = noRts;
                noRtUp = noRts;
                noRtDn = noRts;
                dataName = "discount future risk free";
                yldParams.noPoints = noRts;
            }

        } else if (yldParams.isDiscYn == 'N') {
            noRts = bond.noFixingRate;
            noTenors = bond.noFixingTenor;
            noStdDev = bond.noFixingStdDev;
            noRtUp = bond.noFixingRtUp;
            noRtDn = bond.noFixingRtDn;
            dataName = "fixing rates";
            yldParams.noPoints = noRts;
        }
        if (noRts == 0 && noTenors == 0) {
            return true; // not an error jusy return empty yield curve
        }
        if (noStdDev == 0 && noRtDn == 0 && noRtUp == 0) {
            return true;
        }
        if (noStdDev > 0 || noRtDn > 0 || noRtUp > 0) {
            if (noStdDev != noRtDn) {
                String errmsg = "Number of standard deviations for " + dataName + " is " + noStdDev + " and is not equal to number of downside rates " + noRtDn;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (noStdDev != noRtUp) {
                String errmsg = "Number of standard deviations for " + dataName + " is " + noStdDev + " and is not equal to number of upside rates " + noRtDn;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (noRts < Limits.MINYIELDPOINTS) {
            String errmsg = "You must specify at least " + Limits.MINYIELDPOINTS + " yield curve points for a yield curve - you specified " + noRts;
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (noRts > Limits.MAXYIELDPOINTS) {
            String errmsg = "You must specify no more than " + Limits.MAXYIELDPOINTS + " yield curve points for a yield curve - you specified " + noRts;
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (noRts != noRtDn) {
            String errmsg = "Number " + dataName + " rates(" + noRts + ") not equal to number " + dataName + " down side rates(" + noRtDn + ")";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (noRts != noRtUp) {
            String errmsg = "Number " + dataName + " rates(" + noRts + ") not equal to number " + dataName + " upside rates(" + noRtUp + ")";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        return true;
    }

    private YieldCurves defaultFixingCurve(YieldCurves allYieldCurves, String instype) {
        YieldCurves bondCurves = new YieldCurves(cmdLine, error, dayCount, utility);
        if (!choices.inFloatList(instype))
            return bondCurves;
        if (allYieldCurves == null) {
            String errmsg = "No spot curves were loaded for the fixing curve";
            error.setError(getClass().getName(), errmsg);
            return bondCurves;
        }
        if (allYieldCurves.spotCurve.isEmpty()) {
            String errmsg = "No spot curves were loaded for the fixing curve";
            error.setError(getClass().getName(), errmsg);
            return bondCurves;
        }
        String fixingBenchmark = UnAvails.NOTAVAILSTRING;
        if (instype.toUpperCase().equals(Instype.IRSSWAP)) {
            if (bond.paylegFixedYn == 'N') {
                fixingBenchmark = bond.paylegBenchmark;
            } else {
                if (bond.reclegFixedYn == 'N')
                    fixingBenchmark = bond.reclegBenchmark;
            }
            if (UnAvails.isUnAvail(fixingBenchmark)) {
                if (UnAvails.isUnAvail(bond.cpnRtPayleg) || UnAvails.isUnAvail(bond.cpnRtRecleg)) {
                    String errmsg = "No reset fixing curve or reset rates were specified";
                    error.setError(getClass().getName(), errmsg);
                    return bondCurves;
                }
            }
        } else {
            fixingBenchmark = bond.fixingBenchmark;
        }
        // get the x month forward rate on reset date
        // on each reset date we need a forward curve and it it's value on reset date for matlen reset periodicity
        YieldCurveParams yldParams = new YieldCurveParams();
        String spotFileName = bond.commandLine.rootDirectory + "yieldCurves\\" + BondConsts.SPOTCURVEVOLFILENAME;
        yldParams.setOptions(bond.yieldCurveInterpMeth, BondConsts.SPOTINTERVAl, bond.dtFuture);
        yldParams.setCredit(Codes.FIXINGCURVE, spotFileName, "NA", "NA", fixingBenchmark);
        yldParams.setTypes(cmdLine, 'N', 'N', 'N', 'N');
        yldParams.setDates(bond.dtFixingCurve, bond.dtFixingCurve, bond.dtSettle);
        yldParams.setRates(bond.noFixingRate, bond.fixingRt, bond.fixingTenor, bond.fixingAvgRt, bond.fixingStdDev, bond.fixingRtDn, bond.fixingRtUp, bond.fixingMin, bond.fixingMax);
        bondCurves.setYieldParams(yldParams);
        boolean isOk = checkYieldParameters(yldParams);
        if (!isOk)
            return bondCurves;
        bondCurves = defaultYieldCurve(allYieldCurves, yldParams);
        return bondCurves;
    }


    private boolean defaultHorizonAnalysis(String instype) {
        int nho = -1, hoff = 0;
        if (bond.horizonPeriods == 0)
            return true;
        char useEndMonthRule = 'N';
        int hInterval = bond.horizonRepeatFactor;
        if (bond.horizonTimeUnit.equals("M")) {
            while (++nho < bond.horizonPeriods) {
                hoff = nho + 1;
                bond.dtHorizon[nho] = dayCount.time.addMonthsToDate(bond.dtSettle, hInterval * hoff, useEndMonthRule);
                if (bond.dtHorizon[nho].geDate(bond.dtHorizon[nho], bond.dtMaturity)) {
                    String errmsg = "some horizon dates are greater than maturity date - onlu " + nho + " dates were used for horizon analysis";
                    error.setWarning(getClass().getName(), errmsg);
                    bond.horizonPeriods = ++nho;
                    return true;
                }
            }
            return true;
        }
        while (++nho < bond.horizonPeriods)
            bond.dtHorizon[nho] = dayCount.time.addWeeksToDate(bond.dtSettle, hInterval * nho);
        return true;
    }

    private boolean defaultTaxes(String instype) {
        Taxes taxes = bond.taxes;
        if (UnAvails.isUnAvail(bond.afterTaxCalcsYn) || bond.afterTaxCalcsYn == 'N') {
            taxes.fedTaxExemptYn = 'N';
            taxes.fedIncTaxRt = 0;
            taxes.fedCapGLShortTaxRt = 0.;
            taxes.fedCapGLLongTaxRt = 0.;
            taxes.fedDaysCapGlShort = 30;
            taxes.stateTaxExemptYn = 'N';
            taxes.stateIncTaxRt = 0.;
            taxes.stateCapGLShortTaxRt = 0.;
            taxes.stateCapGLLongTaxRt = 0.;
            taxes.stateDaysCapGlShort = 30;
            bond.afterTaxCalcsYn = 'N';
            return true;
        }
        if (UnAvails.isUnAvail(bond.stateOfResidence)) {
            String errmsg = "No state of residence was specified - so cannot determine after tax analytics";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.stateOfIssue)) {
            String errmsg = "No state of issuance was specified - so cannot determine after tax analytics";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.stateOfResidence.equals(bond.stateOfIssue))
            taxes.stateTaxExemptYn = 'Y';
        else
            taxes.stateTaxExemptYn = 'N';

        if (UnAvails.isUnAvail(bond.fedTaxExemptYn))
            taxes.fedTaxExemptYn = 'N';
        else
            taxes.fedTaxExemptYn = bond.fedTaxExemptYn;

        if (bond.taxRtFedCapGainShort == UnAvails.NOTAVAILDOUBLE)
            taxes.fedCapGLShortTaxRt = 0.;
        else
            taxes.fedCapGLShortTaxRt = bond.taxRtFedCapGainShort;

        if (bond.taxRtFedCapGainLong == UnAvails.NOTAVAILDOUBLE)
            taxes.fedCapGLLongTaxRt = 0.;
        else
            taxes.fedCapGLLongTaxRt = bond.taxRtFedCapGainShort;

        if (bond.taxRtFedIncome == UnAvails.NOTAVAILDOUBLE)
            taxes.fedIncTaxRt = 0.;
        else
            taxes.fedIncTaxRt = bond.taxRtFedIncome;

        if (bond.taxRtStateCapGainShort == UnAvails.NOTAVAILDOUBLE)
            taxes.stateCapGLShortTaxRt = 0.;
        else
            taxes.stateCapGLShortTaxRt = bond.taxRtStateCapGainShort;

        if (bond.taxRtStateCapGainLong == UnAvails.NOTAVAILDOUBLE)
            taxes.stateCapGLLongTaxRt = 0.;
        else
            taxes.stateCapGLLongTaxRt = bond.taxRtFedCapGainLong;

        if (bond.taxRtStateIncome == UnAvails.NOTAVAILDOUBLE)
            taxes.stateIncTaxRt = 0.;
        else
            taxes.stateIncTaxRt = bond.taxRtStateIncome;

        taxes.fedDaysCapGlShort = Defaults.FEDDAYSCAPGLSFORT;
        taxes.stateDaysCapGlShort = Defaults.STATEDAYSCAPGLSFORT;
        bond.taxes.dayCount = dayCount;

        //  bond.taxes.setStateTaxRates(bond.stateTaxExemptYn, bond.taxRtStateIncome, bond.taxRtStateCapGainShort, bond.taxRtStateCapGainLong, BondConsts.DAYSLONGSHORT);
        if (UnAvails.isUnAvail(bond.dtPur))
            bond.dtPur = bond.dtSettle;
        if (UnAvails.isUnAvail(bond.purPrc))
            bond.purPrc = bond.redeemPrin; // forces zero capital GL taes
        return true;
    }

    private ArrayList<YieldPoint> setParCurve(Bond bond, char discYn, char riskFreeYn, char futCurveYn) {
        ArrayList<YieldPoint> parCurve = new ArrayList<>();
        YieldPoint yieldPoint = new YieldPoint();
        int ct = -1;
        String curveType = BondConsts.PARCURVE;
        double d2Yield = 0.;
        if (discYn == 'Y') {
            curveType = BondConsts.PARCURVE;
            if (riskFreeYn == 'Y' && futCurveYn == 'N') {
                while (++ct < bond.noDiscRate) {
                    String matName = bond.discRfCurve.getMatLenName(bond.discTenor[ct]);
                    yieldPoint.curveType = curveType;
                    yieldPoint.dtCurve = bond.dtSettle;
                    yieldPoint.spotRate = bond.discRt[ct];
                    yieldPoint.matLen = bond.discTenor[ct];
                    yieldPoint.spotAvg = bond.discRt[ct];
                    yieldPoint.spotStdDev = bond.discStdDev[ct];
                    yieldPoint.spotDnSide = bond.discRtDn[ct];
                    yieldPoint.spotUpSide = bond.discRtUp[ct];
                    yieldPoint.matName = bond.discCurve.getMatLenName(yieldPoint.matLen);
                    yieldPoint.lookAhead = 0;
                    parCurve.add(ct, new YieldPoint(yieldPoint));
                }
            }
            if (riskFreeYn == 'N' && futCurveYn == 'N') {
                while (++ct < bond.noDiscSprd) {
                    String matName = bond.discRfCurve.getMatLenName(bond.discSprdTenor[ct]);
                    yieldPoint.curveType = curveType;
                    yieldPoint.dtCurve = bond.dtSettle;
                    yieldPoint.spotRate = bond.discSprd[ct];
                    yieldPoint.matLen = bond.discSprdTenor[ct];
                    yieldPoint.spotAvg = bond.discSprd[ct];
                    yieldPoint.spotStdDev = bond.discSprdStdDev[ct];
                    yieldPoint.spotDnSide = bond.discSprdDn[ct];
                    yieldPoint.spotUpSide = bond.discSprdUp[ct];
                    yieldPoint.matName = bond.discCurve.getMatLenName(yieldPoint.matLen);
                    yieldPoint.lookAhead = 0;
                    parCurve.add(ct, new YieldPoint(yieldPoint));
                }
            }
            if (riskFreeYn == 'N' && futCurveYn == 'Y') {
                while (++ct < bond.noDiscFutRate) {
                    String matName = bond.discFutRfCurve.getMatLenName(bond.discSprdTenor[ct]);
                    yieldPoint.curveType = curveType;
                    yieldPoint.dtCurve = bond.dtSettle;
                    yieldPoint.spotRate = bond.discFutSprd[ct];
                    yieldPoint.matLen = bond.discFutSprdTenor[ct];
                    yieldPoint.spotAvg = bond.discFutSprd[ct];
                    yieldPoint.spotStdDev = bond.discFutSprdStdDev[ct];
                    yieldPoint.spotDnSide = bond.discFutSprdDn[ct];
                    yieldPoint.spotUpSide = bond.discFutSprdUp[ct];
                    yieldPoint.matName = bond.discFutSprdCurve.getMatLenName(yieldPoint.matLen);
                    yieldPoint.lookAhead = 0;
                    parCurve.add(ct, new YieldPoint(yieldPoint));

                }
            }
            if (riskFreeYn == 'Y' && futCurveYn == 'Y') {
                while (++ct < bond.noDiscFutSprd) {
                    String matName = bond.discFutRfCurve.getMatLenName(bond.discSprdTenor[ct]);
                    yieldPoint.curveType = curveType;
                    yieldPoint.dtCurve = bond.dtSettle;
                    yieldPoint.spotRate = bond.discFutRt[ct];
                    int matLen = bond.discFutTenor[ct];
                    yieldPoint.matLen = bond.discFutTenor[ct];
                    yieldPoint.spotAvg = bond.discFutRt[ct];
                    yieldPoint.spotStdDev = bond.discFutStdDev[ct];
                    yieldPoint.spotDnSide = bond.discFutRtDn[ct];
                    yieldPoint.spotUpSide = bond.discFutRtUp[ct];
                    yieldPoint.matName = bond.discFutRfCurve.getMatLenName(matLen);
                    yieldPoint.lookAhead = 0;
                    parCurve.add(ct, new YieldPoint(yieldPoint));
                }
            }
        }
        return parCurve;
    }

    private boolean defaultYieldCurveOptions(int securityNo, String instype) {
        if (UnAvails.isUnAvail(bond.yieldCurveInterpMeth))
            bond.yieldCurveInterpMeth = BondConsts.LINEARINTERP;
        if (UnAvails.isUnAvail(bond.spotDiscInterval))
            bond.spotDiscInterval = BondConsts.SPOTINTERVAl;
        if (UnAvails.isUnAvail(bond.dtFuture)) {
            if (UnAvails.isAvail(bond.dtSettle)) {
                bond.dtFuture = dayCount.time.addMonthsToDate(bond.dtSettle, Defaults.FUTUREMONTHS, 'N');
            }
        }
        if (UnAvails.isUnAvail(bond.dtDiscCurve)) {
            if (UnAvails.isAvail(bond.dtFuture))
                bond.dtDiscCurve = bond.dtFuture;
        }

        if (UnAvails.isUnAvail(bond.dtDiscFutCurve)) {
            bond.dtDiscFutCurve = bond.dtFuture;
        }

        if (UnAvails.isUnAvail(bond.yieldForeCastMeth))
            bond.yieldForeCastMeth = CalcList.FORECASTSPOT;

        if (choices.inDiscountCurveList(calculationList)) {
            if (UnAvails.isUnAvail(bond.creditAgencyRf)) {
                bond.creditAgencyRf = Defaults.CREDITAGENCY;
            }
            if (UnAvails.isUnAvail(bond.creditRatingRf)) {
                bond.creditRatingRf = Defaults.CREDITRATING;
            }
            if (UnAvails.isUnAvail(bond.discRtParYn))
                bond.discRtParYn = 'N';

            if (UnAvails.isUnAvail(bond.creditAgencySprd)) {
                bond.creditAgencySprd = Defaults.CREDITAGENCY;
            }
            if (UnAvails.isUnAvail(bond.creditRatingSprd)) {
                bond.creditRatingSprd = Defaults.CREDITRATING;
            }
            if (UnAvails.isUnAvail(bond.discSprdParYn))
                bond.discSprdParYn = 'N';
        }


        if (choices.inFutureDiscountCurveList(calculationList)) {
            if (UnAvails.isUnAvail(bond.creditAgencyRf)) {
                bond.creditAgencyRf = Defaults.CREDITAGENCY;
            }
            if (UnAvails.isUnAvail(bond.creditRatingRf)) {
                bond.creditRatingRf = Defaults.CREDITRATING;
            }
            if (UnAvails.isUnAvail(bond.discFutRtParYn))
                bond.discFutRtParYn = 'N';
            if (bond.noDiscFutSprd == 0) {
                if (UnAvails.isUnAvail(bond.creditAgencySprd)) {
                    bond.creditAgencySprd = Defaults.CREDITAGENCY;
                }
                if (UnAvails.isUnAvail(bond.creditRatingSprd)) {
                    bond.creditRatingSprd = Defaults.CREDITRATING;
                }
                if (UnAvails.isUnAvail(bond.discFutSprdParYn))
                    bond.discSprdParYn = 'N';
            }
        }
        return true;
    }

    private ArrayList<YieldPoint> getInterpYieldCurve
            (ArrayList<YieldPoint> riskFreeCurve, ArrayList<YieldPoint> sprdCurve) {
        ArrayList<YieldPoint> extSprdCurve = new ArrayList<>();
        YieldPoint yieldPoint = new YieldPoint();
        YieldCurves tempCurves = new YieldCurves();
        if (sprdCurve.isEmpty()) {
            extSprdCurve = tempCurves.zeroCurve(riskFreeCurve.size());
            return extSprdCurve;
        }

        int i = -1;
        int noPoints = riskFreeCurve.size();
        while (++i < noPoints) {
            int matLen = riskFreeCurve.get(i).matLen;
            yieldPoint.matLen = matLen;
            Utility.DoubleRange doubleRange = bond.discCurve.getCurveRange(sprdCurve, matLen);
            yieldPoint.spotRate = utility.interpRange(matLen, doubleRange);
            yieldPoint.curveType = BondConsts.SPOTCURVE;
            yieldPoint.dtCurve = riskFreeCurve.get(i).dtCurve;
            yieldPoint.matName = bond.discRfCurve.getMatLenName(matLen);
            yieldPoint.lookAhead = 0;
            extSprdCurve.add(i, new YieldPoint(yieldPoint));
        }
        return extSprdCurve;
    }

    public YieldCurves combineYieldCurves(YieldCurves rfCurve, YieldCurves sprdCurve) {
        YieldCurves combinedCurves = new YieldCurves(cmdLine, error, dayCount, utility);
        YieldCurveParams yieldCurveParams = new YieldCurveParams();
        yieldCurveParams = rfCurve.yieldCurveParams;
        if (rfCurve.spotCurve.isEmpty() && sprdCurve.spotCurve.isEmpty()) {
            String errmsg = "No risk free or spread curves were specified for creating a combined yield curve";
            error.setError(getClass().getName(), errmsg);
            return combinedCurves;
        }
        ArrayList<YieldPoint> extSpotCurve = new ArrayList<>();
        ArrayList<YieldPoint> extForwardCurve = new ArrayList<>();
        // the tenors must be the same
        YieldPoint spotPoint = new YieldPoint();
        YieldPoint forwardPoint = new YieldPoint();
        extSpotCurve = getInterpYieldCurve(rfCurve.spotCurve, sprdCurve.spotCurve);
        int noRf = rfCurve.spotCurve.size();
        int noSprd = extSpotCurve.size();
        if (noRf != noSprd) {
            String errmsg = "Application error - cannot merge yield curves - there are " + noRf + " spot rate points in the risk free curve and " + noSprd + " points in the credit spread curve";
            error.setError(getClass().getName(), errmsg);
            return combinedCurves;
        }
        extForwardCurve = getInterpYieldCurve(rfCurve.forwardCurve, sprdCurve.forwardCurve);
        int noRffor = rfCurve.forwardCurve.size();
        int noSprdfor = extForwardCurve.size();
        if (noRffor != noSprdfor) {
            String errmsg = "Application error - cannot merge yield curves - there are " + noRffor + " forward points in the risk free curve and " + noSprdfor + " points in the credit spread curve";
            error.setError(getClass().getName(), errmsg);
            return combinedCurves;
        }

        int np = -1;
        while (++np < noRf) {
            spotPoint.spotRate = rfCurve.spotCurve.get(np).spotRate + extSpotCurve.get(np).spotRate;
            spotPoint.matLen = rfCurve.spotCurve.get(np).matLen;
            spotPoint.spotDnSide = rfCurve.spotCurve.get(np).spotDnSide + extSpotCurve.get(np).spotDnSide;
            spotPoint.spotUpSide = rfCurve.spotCurve.get(np).spotUpSide + extSpotCurve.get(np).spotUpSide;
            spotPoint.spotAvg = rfCurve.spotCurve.get(np).spotAvg + extSpotCurve.get(np).spotAvg;
            double var = rfCurve.spotCurve.get(np).spotStdDev + extSpotCurve.get(np).spotStdDev;
            spotPoint.spotStdDev = var;
            if (sprdCurve.spotCurve.size() == 0) {
                spotPoint.creditAgency = rfCurve.spotCurve.get(np).creditAgency;
                spotPoint.creditRating = rfCurve.spotCurve.get(np).creditRating;
            } else {
                spotPoint.creditAgency = extSpotCurve.get(np).creditAgency;
                spotPoint.creditRating = extSpotCurve.get(np).creditRating;
            }
            spotPoint.dtCurve = rfCurve.spotCurve.get(np).dtCurve;
            spotPoint.matLen = rfCurve.spotCurve.get(np).matLen;
            spotPoint.dtAnalysisStart = rfCurve.spotCurve.get(np).dtAnalysisStart;
            spotPoint.analysisDays = rfCurve.spotCurve.get(np).analysisDays;
            spotPoint.yieldType = BondConsts.TOTALCURVE;
            combinedCurves.spotCurve.add(np, new YieldPoint(spotPoint));
        }
        np = -1;
        while (++np < noRffor) {
            forwardPoint.spotRate = rfCurve.forwardCurve.get(np).spotRate + extForwardCurve.get(np).spotRate;
            forwardPoint.matLen = rfCurve.forwardCurve.get(np).matLen + extForwardCurve.get(np).matLen;
            forwardPoint.spotDnSide = rfCurve.forwardCurve.get(np).spotDnSide + extForwardCurve.get(np).spotDnSide;
            forwardPoint.spotUpSide = rfCurve.forwardCurve.get(np).spotUpSide + extForwardCurve.get(np).spotUpSide;
            forwardPoint.spotAvg = rfCurve.forwardCurve.get(np).spotAvg + extForwardCurve.get(np).spotAvg;
            double var = rfCurve.forwardCurve.get(np).spotStdDev + extForwardCurve.get(np).spotStdDev;
            forwardPoint.spotStdDev = var;
            if (sprdCurve.forwardCurve.size() == 0) {
                forwardPoint.creditAgency = rfCurve.forwardCurve.get(np).creditAgency;
                forwardPoint.creditRating = rfCurve.forwardCurve.get(np).creditRating;
            } else {
                forwardPoint.creditAgency = extForwardCurve.get(np).creditAgency;
                forwardPoint.creditRating = extForwardCurve.get(np).creditRating;
            }
            forwardPoint.dtCurve = rfCurve.forwardCurve.get(np).dtCurve;
            forwardPoint.matLen = rfCurve.forwardCurve.get(np).matLen;
            forwardPoint.dtAnalysisStart = rfCurve.forwardCurve.get(np).dtAnalysisStart;
            forwardPoint.analysisDays = rfCurve.forwardCurve.get(np).analysisDays;
            forwardPoint.yieldType = BondConsts.TOTALCURVE;
            combinedCurves.forwardCurve.add(np, new YieldPoint(forwardPoint));
        }
        return combinedCurves;

    }

    private boolean defaultDiscCurveTrueFalse(Choices choices, ArrayList<Integer> calculationList) {
        int i = -1;
        while (++i < calculationList.size()) {
            int calcNo = calculationList.get(i);
            if (choices.inDiscountCurveList(calcNo))
                return true;
        }
        return false;
    }

    private boolean defaultDiscFutCurveTrueFalse(ArrayList<Integer> calculationList) {
        int i = -1, calcNo = 0;
        while (++i < calculationList.size()) {
            calcNo = calculationList.get(i);
            if (choices.inFutureDiscountCurveList(calcNo))
                return true;
        }
        return false;
    }

    public boolean defaultCreditRatings() {
        if (UnAvails.isUnAvail(bond.creditAgency)) {
            if (UnAvails.isAvail(bond.creditAgencyRf))
                bond.creditAgency = bond.creditAgencyRf;
        }

        if (UnAvails.isUnAvail(bond.creditRating)) {
            if (UnAvails.isAvail(bond.creditRatingRf))
                bond.creditRatingRf = bond.creditRating;
        }
        if (UnAvails.isUnAvail(bond.creditAgency))
            bond.creditRating = Defaults.CREDITRATING;
        if (UnAvails.isUnAvail(bond.creditRating))
            bond.creditRating = Defaults.CREDITRATING;

        if (UnAvails.isUnAvail(bond.creditRatingRf)) {
            bond.creditRatingRf = Defaults.CREDITRATING;
        }

        if (UnAvails.isUnAvail(bond.creditRatingSprd)) {
            bond.creditRatingSprd = Defaults.CREDITRATING;
        }
        return true;

    }

    public boolean defaultTaxSchedule() {
        if (bond.afterTaxCalcsYn == 'Y') {
            if (UnAvails.isUnAvail(bond.taxSchedRule.timeUnit) && UnAvails.isUnAvail(bond.dtTaxPay)) {
                bond.taxSchedRule.timeUnit = "M";
                bond.taxSchedRule.repeatFactor = 12;
                bond.taxSchedRule.dtOccur.setDate(Defaults.TAXPAYDATE);
            }

        }
        return true;
    }

}






import javax.swing.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.*;

public class CalcMethod {
    //import org.jetbrains.annotations.NotNull;
    Formatter fileFormatter;
    Error error;
    CommandLine commandLine;
    Bond bond;
    Discount discount;
    AtMaturity atMaturity;
    Floater floater;
    Mortgage mortgage;
    Indexed indexed;
    DayCount dayCount;
    CalcDef calcDef;
    Utility utility;
    CalcResults calcResults = new CalcResults();
    CashflowResults cashflowResults = new CashflowResults();
    ArrayList<CashflowResults> cashflowList = new ArrayList<>();
    ArrayList<HorizonResults> horizonResults = new ArrayList<>();
    CalcMethod.KeyRateDuration keyRateDuration = new CalcMethod.KeyRateDuration();
    YieldCurves yieldCurveDisc;
    Choices choices = new Choices(error);
    String fileCalcStr;
    ArrayList<Integer> calculationList;
    int paySchedCount = 0;

    //YieldCurves yieldCurves;
    CalcMethod() {
    }

    CalcMethod(Formatter fileFormatter, CommandLine commandLine, Error error, CalcDef calcDef, Utility utility, Choices choices, DayCount dayCount) {
        this.fileFormatter = fileFormatter;
        this.error = error;
        this.commandLine = commandLine;
        this.calcDef = calcDef;
        this.utility = utility;
        this.choices = choices;
        this.dayCount = dayCount;

    }

    public void setCalcMethod(Bond bond, CashflowResults cashflowResults) {
        this.bond = bond;
        discount = new Discount(bond, cashflowResults);
        atMaturity = new AtMaturity(bond, cashflowResults);
        floater = new Floater(bond);
        mortgage = new Mortgage(bond, cashflowResults);
        indexed = new Indexed(bond);
        accInt = 0.;
        dayFactor = 0.;
        prinFactor = 0.;
        couponFactor = 0.;

    }


    double accInt;
    double dayFactor;
    double prinFactor;
    double couponFactor;

    public void setCalculationList(ArrayList<Integer> calculationList) {
        this.calculationList = calculationList;
    }

    public double interestIncome(int calcNo, String instype, double redeemInt, Dates dtStart, Dates dtEnd) {
        double intInc = 0.0;
        intInc = bond.faceAmt * getInterestIncome(calcNo, instype, redeemInt, dtStart, dtEnd);
        return intInc;
    }

    public double getInterestIncome(int calcNo, String instype, double redeemPrin, Dates dtStart, Dates dtEnd) {
        double intInc = 0.;
        int tNo = -1, tNoStart = 0;
        Dates dtLastEvent = new Dates();
        Dates dtLastCpn = new Dates();
        Dates dtNextCpn = new Dates();
        Dates dtLast = new Dates();
        Dates dtShed = new Dates();
        Dates dtNext = new Dates();
        Dates dtSched = new Dates();
        double intIncome = 0.;
        SchedTrack schedTrack;

        if (bond.stripType.equals(BondConsts.STRIPPO))
            return 0.;
        switch (instype) {
            case Instype.DISCOUNT:
                return 0.;
            case Instype.CURRENCY:
            case Instype.ATMATURITY:
                intInc = atMaturity.getInterestIncome(calcNo, dtStart, dtEnd);
                return intInc;
            case Instype.FLOATER:
                intInc = floater.getInterestAccrual(calcNo, dtStart, dtEnd);
                return intInc;
            case Instype.FIXEDRATE:
            case Instype.INDEXED:
            case Instype.SINKER:
            case Instype.MORTGAGE:
            case Instype.PERPETUAL:
            case Instype.PERPETUALFLOAT:
                break;
            default:
                String errmsg = "Invalid instrument " + instype + " was specified";
                bond.error.setError(getClass().getName(), errmsg);
                return 0.;
        }
        double rrv = 0.;
        double cpnRt = 0.;
        String errmsg = null;
        double couponPeriod = 0.;
        int trackNo = 0;
        dtLast = dtStart;
        SchedTrack nextTrack = new SchedTrack();
        nextTrack = bond.schedTracks.getSchedTrackAfter(dtSched, SchedTrack.ANYTYPETRACK);
        dtNext = nextTrack.schedDate;
        int nextSched = nextTrack.schedType;
        double incIncome = 0.;
        while (++tNo < bond.schedTracks.noSchedTracks && dtNext.leDate(dtNext, dtEnd)) {
            rrv = getRedeemValue(instype, dtLast, bond.redeemPrin);   // get next event on or after dtlast
            cpnRt = getCouponRate(calcNo, instype, dtLast);
            couponPeriod = bond.intSchedRule.getCouponFactor(dtLast, dtNext);
            incIncome = rrv * cpnRt * couponPeriod / bond.intSchedRule.compoundFreq;
            incIncome += incIncome;
            dtLast = dtNext;
            nextTrack = bond.schedTracks.getSchedTrackAfter(dtLast, SchedTrack.ANYTYPETRACK);
            if (!nextTrack.found)
                break;
            dtNext = nextTrack.schedDate;
            nextSched = nextTrack.schedType;
        }
        // next calc date is either next reset date or enddt
        if (dtNext.gtDate(dtNext, dtEnd)) {
            rrv = getRedeemValue(instype, dtLast, redeemPrin);
            cpnRt = getCouponRate(calcNo, instype, dtLast);
            couponPeriod = bond.intSchedRule.getCouponFactor(dtLast, dtEnd);
            incIncome = rrv * cpnRt * couponPeriod / bond.intSchedRule.compoundFreq;
            intIncome += incIncome;
        }
        return intIncome;
    }

    public double getIncomeFactor(int calcNo, String instype, Dates dtLast, Dates dtNext, Dates dtRedeem, double redeemPrin) {
        double incFactor = 0.0;
        double rrv = 0.0, cpnRt = 0.0;
        double scale = 1.;
        if ('Y' == calcResults.isCalcMoney(calcNo))
            scale = bond.faceAmt;
        rrv = getRedeemValue(instype, dtLast, redeemPrin);
        return incFactor;
    }


    public double accruedInterest(int calcNo, String instype, Dates dtSettle) {
        double accInt = 0.;
        int trackNo = 0;
        bond.intSchedRule.dayCountMeth = bond.dcAccruedInt;
        if (calcNo == CalcList.YIELDTOMATURITY || calcNo == CalcList.PRICETOMATURIY) {
            bond.intSchedRule.dayCountMeth = bond.dcYldPrc;
        }
        switch (instype) {
            case "DISCOUNT":
                accInt = 0.;
                break;
            case "ATMATURITY":
            case "CURRENCY":
                accInt = atMaturity.accruedInterest(calcNo, bond.dtSettle);
                break;
            case "MORTGAGE":
                accInt = bond.faceAmt * getAccruedInterest(calcNo, instype, bond.dtSettle) * mortgage.getPassthruFactor(bond.dtSettle);
                break;
            case "IRSSWAP":
                accInt = 0.;
                break;
            default:
                if (bond.stripType.equals(BondConsts.STRIPPO))
                    return 0.;
                if (bond.cleanPrcYn == 'N')
                    return 0.;
                // handles all non=floatrr binds
                accInt = getAccruedInterest(calcNo, instype, dtSettle);
                if (bond.error.hasErrorYn == 'Y')
                    return 0.;
                boolean isExdiv = isExDividend(dtSettle, bond.dtExDividend);
                accInt *= bond.faceAmt;
                accInt = adjustAccrued(bond.accruedIntMeth, accInt, bond.redeemPrin, couponFactor, dayFactor, prinFactor);
                if (isExdiv) {
                    SchedTrack schedTrack = new SchedTrack();
                    schedTrack = bond.schedTracks.getSchedTrackBefore(dtSettle, SchedTrack.ACCRUETRACK);
                    Dates dtLastCpn = schedTrack.schedDate;
                    dtLastCpn = bond.intSchedRule.getNextSchedDate(BondConsts.BEFOREONDATE, dtSettle);
                    Dates dtNextCpn = bond.intSchedRule.getNextSchedDate(BondConsts.AFTERDATE, dtLastCpn);
                    dayFactor = bond.intSchedRule.getCouponFactor(dtLastCpn, dtSettle);
                    accInt = bond.faceAmt * dayFactor * couponFactor * prinFactor;
                }
                // does not use cashflow
                break;
        }
        return accInt;
    }

    public double getAccruedInterest(int calcNo, String instype, Dates dtSettle) {
        double accInt = 0.;
        Dates dtLastCpn = bond.intSchedRule.getNextSchedDate(BondConsts.BEFOREONDATE, dtSettle);
        switch (instype) {
            case Instype.FLOATER:
                accInt = floater.getInterestAccrual(calcNo, dtLastCpn, dtSettle);
                dayFactor = 1.;
                prinFactor = 1.;
                couponFactor = 1.;
                break;
            case Instype.IRSSWAP:
                dayFactor = 1.;
                prinFactor = 1.;
                couponFactor = 1.;
                accInt = 0.;
                break;
            default:
                // handle canadian accrued
                dayFactor = bond.intSchedRule.getCouponFactor(dtLastCpn, dtSettle);// deals with daycount 364 -the day count term for fraction of a coupon period
                prinFactor = getRedeemValue(instype, dtSettle, bond.redeemInt); // the redmption value per unit of face or par
                couponFactor = getCouponRate(calcNo, instype, dtSettle) / bond.intSchedRule.compoundFreq;  // averaagr coupon rate (for floaters) and the constant rate for non-floaters
                accInt = dayFactor * couponFactor * prinFactor;
        }
        return accInt;
    }

    public double getInterstCashFixedRate(Dates dtLast, Dates dtNext, double cpnRt, SchedRule schedRule) {
        double intCash = 0.;
        intCash = cpnRt * schedRule.getCouponFactor(dtLast, dtNext) / schedRule.compoundFreq;
        return intCash;
    }

    public int couponDays(int calcNo, Dates dtSettle) {
        int cpnDays = 0;
        Dates dtLastCpn = new Dates();
        String errmsg = null;
        dtLastCpn = bond.intSchedRule.getNextSchedDate(BondConsts.BEFOREONDATE, dtSettle);
        cpnDays = dayCount.getDaycount(dtLastCpn, dtSettle, bond.dtMaturity, bond.dcAccruedInt);
        return cpnDays;
    }

    public double principalPayment(int calcNo, String instype, Dates dtSettle, double settlePrc, double redeemPrin) {
        double principalPay = bond.faceAmt * settlePrc * getRedeemValue(instype, dtSettle, redeemPrin);
        return principalPay;
    }

    public double netPayment(int calcNo, String instype, Dates dtSettle, double settlePrc) {
        if (instype.equals(Instype.CURRENCY))
            return bond.faceAmt;
        double netPay = accruedInterest(calcNo, instype, dtSettle);
        netPay += principalPayment(calcNo, instype, dtSettle, settlePrc, bond.redeemPrin);
        return netPay;
    }

    public double getCouponRate(int calcNo, String instype, Dates dtCalc) {
        double cpnRt = bond.cpnRt;
        switch (instype) {
            case Instype.CURRENCY:
                cpnRt = bond.cpnRt;
            case Instype.DISCOUNT:
            case Instype.DISCOUNTSINK:
                cpnRt = 0.;
            case Instype.ATMATURITY:
                cpnRt = bond.cpnRt;
                break;
            case Instype.FIXEDRATE:
            case Instype.INDEXED:
            case Instype.SINKER:
            case Instype.MORTGAGE:
                cpnRt = bond.cpnRt;
                break;
            case Instype.FLOATER:
                cpnRt = getResetRate(dtCalc);
                break;
            default:
                break;
        }
        return cpnRt;
    }

    public boolean isOnCouponDate(SchedTrack schedTrack) {
        boolean isOk = false;
        Dates dtCpn = new Dates();
        Dates dtPay = new Dates();
        SchedTrack cpnTrack = new SchedTrack();

        dtPay = schedTrack.schedDate;
        cpnTrack = bond.schedTracks.getSchedTrackOn(dtPay, SchedTrack.INTERESTPAYTRACK);
        if (!cpnTrack.found)
            return false;
        dtCpn = cpnTrack.schedDate;
        isOk = dtCpn.eqDate(dtCpn, dtPay);
        return isOk;
    }
    // C version
    /*
    double accIntUnitizedFactored(ISSUESPEC *issue, double accIntUnAdjusted, double prinFactor, double daycountFactor)
{
	double accIntB, accIntBR, accIntR, accIntRB, accInt;
	//double nomninal_par = (issue->bondsize == 1.0) ? issue->par : issue->bondsize;
	double bondsize = issue->bondsize;
	BpDate lcpndt, ncpndt, scheddt;
	int includeOddness = BPTRUE, daysPeriod1, daysPeriod2, daysAccrued;

	switch (issue->accruedRoundingMeth) {
	case NOTAVAILINT:
	case BPAPPLYNOROUNDING:
		accInt = accIntUnAdjusted * prinFactor * daycountFactor;
		break;
	case BPAPPLYBONDSIZEFIRST:
		accIntB = TruncRnd(accIntUnAdjusted * daycountFactor * bondsize, issue->sfigunitsize, issue->accruedrndop);
		accIntBR = accIntB * prinFactor;
		accInt = TruncRnd(accIntBR, issue->sfigunitsize, issue->accruedrndop) / bondsize;
		break;
	case BPAPPLYFACTORFIRST:
		accIntR = TruncRnd(accIntUnAdjusted * prinFactor, issue->sfigunitsize,  issue->accruedrndop);
		accIntRB = accIntR * bondsize * daycountFactor;
		accInt = TruncRnd(accIntRB, issue->sfigunitsize, issue->accruedrndop) / bondsize;
		break;
	case BPAPPLYNOFACTORROUND:
		accIntB = TruncRnd(accIntUnAdjusted * bondsize, issue->sfigunitsize, issue->accruedrndop);
		accIntB = TruncRnd(accIntB * daycountFactor, issue->sfigunitsize,  issue->accruedrndop);
		accInt = accIntB * prinFactor / bondsize;
		break;
	case BPAPPLYSIMULTANEOUS:
		accIntB = TruncRnd(accIntUnAdjusted * prinFactor * daycountFactor * bondsize, issue->sfigunitsize, issue->accruedrndop);
		accInt = accIntB / bondsize;
		break;
	case BPAPPLYBONDSIZEONLY: //accIntUnAdjusted is full period payment
		accInt = accIntUnAdjusted * prinFactor * daycountFactor;
		accIntB = TruncRnd(accInt * bondsize, issue->sfigunitsize, issue->accruedrndop);
		//accIntB = TruncRnd (accIntUnAdjusted * bondsize, issue->sfigunitsize, issue->accruedrndop);
		accInt = accIntB / bondsize;
		break;
	case BPAPPLYBONDSIZEPRINFACTORONLY:
		accIntB = TruncRnd(accIntUnAdjusted * prinFactor * daycountFactor * bondsize, issue->sfigunitsize, issue->accruedrndop);
		accInt = accIntB / bondsize;
		break;
	case BPAPPLYCOUPONFACTOR:
		nextBondSchedDate(issue, lcpndt, scheddt, BPSTDLEG, issue->settdt, BPONBEFOREDATE, includeOddness);
		nextBondSchedDate(issue, ncpndt, scheddt, BPSTDLEG, lcpndt, BPAFTERDATE, includeOddness);
		daysAccrued = daycount(issue, FROMCOUPON, issue->endmonth_rule_yn, issue->fq, issue->bas1, lcpndt, issue->settdt);
		daysPeriod1 = daycount(issue, FROMCOUPON, issue->endmonth_rule_yn, issue->fq, issue->bas1, lcpndt, ncpndt);
		daysPeriod2 = daycount(issue, FROMCOUPON, issue->endmonth_rule_yn, issue->fq, issue->bas2, lcpndt, ncpndt);
		//coufac = issue->cpnrt / issue->fq;
		if (daysPeriod2 == 0 || daysAccrued == 0) {
			return 0.0;
		}
		if (issue->bondsize == 0) {
			accInt = (issue->cpnrt / issue->fq) * daysAccrued / daysPeriod2;
			return accInt;
		}
		accInt = bondsize * issue->cpnrt * daysPeriod1 / (daysPeriod2 * issue->fq);
		accIntR = TruncRnd(accInt, issue->sfigunitsize, issue->accruedrndop);
		accInt = accIntR * daysAccrued / daysPeriod1;
		accIntR = TruncRnd(accInt, issue->sfigunitsize, issue->accruedrndop);
		accInt = accIntR / bondsize;
		break;
	default:
		ISSUE_ERROR("an invalid method %d for rounding or truncating the accrued interest is detected", issue->accruedRoundingMeth);
		return 0.0;
	}

	return accInt;
}
    */


    public double adjustAccrued(String accruedIntMeth, double accIntUnAdjusted, double faceAmt, double couponFactor, double dayFactor, double prinFactor) {
        double adjAccrued = 0., accruedFactor = 0.;
        double prinPayment = faceAmt * prinFactor;
        switch (accruedIntMeth) {
            case "STD": // no rounding no bond bond size invovled
                adjAccrued = accIntUnAdjusted; // accInt here is average rate (floates) or coupon rate at last payment
                break;
            case "SIZE":
                accruedFactor = bond.accruedIntSize * couponFactor * dayFactor;
                adjAccrued = Utility.truncRound(accruedFactor, bond.sfigAccruedSize, bond.accruedIntSizeRndYn);
                adjAccrued *= adjAccrued * prinPayment / bond.accruedIntSize;
                break;
            case "PRIN":
                adjAccrued = Utility.truncRound(prinPayment, bond.sfigAccruedPrin, bond.accruedIntPrinRndYn);
                adjAccrued *= dayFactor * couponFactor;
                break;
            case "COUPON":
                adjAccrued = Utility.truncRound(couponFactor, bond.sfigAccruedCoupon, bond.accruedIntCouponRndYn);
                adjAccrued *= dayFactor * prinPayment;
                break;
            case "DAYCOUNT":
                adjAccrued = Utility.truncRound(couponFactor, bond.sfigAccruedDaycount, bond.accruedIntDaycountRndYn);
                adjAccrued *= couponFactor * prinPayment;
                break;
            case "PRIN_DAYCOUNT": // round on combo of size and daycount, then principal
                adjAccrued = Utility.truncRound(dayFactor * prinPayment, bond.sfigAccruedPrin, bond.accruedIntPrinRndYn);
                adjAccrued *= couponFactor;
                break;
            case "PRIN_COUPON": // round on combo of size and daycount, then principal
                adjAccrued = Utility.truncRound(couponFactor * prinPayment, bond.sfigAccruedPrin, bond.accruedIntPrinRndYn);
                adjAccrued *= dayFactor;
                break;
           case "PRIN_SIZE":
                adjAccrued = Utility.truncRound(prinPayment, bond.sfigAccruedSize, bond.accruedIntPrinRndYn);
                adjAccrued = adjAccrued * couponFactor * dayFactor;
                break;
            case "PRIN+COUPON":
                adjAccrued = Utility.truncRound(prinPayment * couponFactor, bond.sfigAccruedPrin, bond.accruedIntPrinRndYn);
                adjAccrued *= dayFactor;
                break;
            case "PRIN+DAYCOUNT":
                adjAccrued = Utility.truncRound(prinPayment * dayFactor, bond.sfigAccruedPrin, bond.accruedIntPrinRndYn);
                adjAccrued *= couponFactor;
                break;
            case "COUPON+DAYCOUNT":
                adjAccrued = Utility.truncRound(couponFactor * dayFactor, bond.sfigAccruedCoupon, bond.accruedIntCouponRndYn);
                adjAccrued *= prinPayment;
                break;
            case "CANADACCRUED":
                Dates dtLastCpn = bond.intSchedRule.getNextSchedDate(BondConsts.BEFOREONDATE, bond.dtSettle);
                Dates dtNextCpn = bond.intSchedRule.getNextSchedDate(BondConsts.AFTERDATE, bond.dtSettle);
                double daysLS = dayCount.getDaycount(dtLastCpn, bond.dtSettle, bond.dtMaturity, bond.dcAccruedInt);
                double daysLN = dayCount.getDaycount(dtLastCpn, dtNextCpn, bond.dtMaturity, bond.dcAccruedInt);
                double freq = bond.intSchedRule.compoundFreq;
                double daysLimit = 365. / freq;
                if (daysLS < daysLimit)
                    adjAccrued = prinFactor * couponFactor * daysLS / (freq * daysLimit);
                else
                    adjAccrued = prinFactor * couponFactor * (1. / freq - (daysLN - daysLS) / 365.);
                break;
            default:
                String errmsg = "invalid accrued interest method";
                bond.error.setError(getClass().getName(), errmsg);
                adjAccrued = 0.;
                break;
        }
        return adjAccrued;
    }

    // dtSettle is the date on which the forward rate on dtRest is desired
//the time berween dtSettle and dtRest is the years ahead from dtsettle on which the rate is desired
    public double getResetRate(Dates dtReset) {
        double cpnRt = 0.;
        String errmsg = null;
        SchedTrack schedTrack = new SchedTrack();
        int trackNo = 0;
        if (bond.noResetDts > 0) {
            schedTrack = bond.schedTracks.getSchedTrackOnBefore(dtReset, SchedTrack.RESETRATETRACK);
            cpnRt = schedTrack.schedValue;
        } else {
            if (bond.noFixingRate > 0) {
                cpnRt = bond.fixingCurve.getForwardResetRate(bond.dtSettle, dtReset, bond.resetSchedRule);
                return cpnRt;
            }
            if (bond.cpnRt != UnAvails.NOTAVAILDOUBLE)
                return cpnRt;
            else {
                errmsg = "No fixing curve or reset rates were specified";
                bond.error.setError(getClass().getName(), errmsg);
                cpnRt = 0.;
            }

        }
        return 0.0;
    }

    private double getReinvestRate(Dates dtCasflow, Dates dtMaturity, String reinVestMethod) {
        double reinRt = 0.;
        double matlen = 0.;
        switch (reinVestMethod) {
            case "NA":
                reinRt = 0.;
                break;
            case "YTM":
                reinRt = bond.settleYld;
                break;
            case "SC":
                matlen = CalUnits.MONTHSPERYEAR * bond.time.getYearsBetweenDates(dtCasflow, dtMaturity) * CalUnits.MONTHSPERYEAR;
                reinRt = this.yieldCurveDisc.getSpotRate(matlen, bond.yieldCurveInterpMeth);
                break;
            case "REINRT":
                matlen = CalUnits.MONTHSPERYEAR * bond.time.getYearsBetweenDates(dtCasflow, dtMaturity);
                reinRt = getReinvestRate(matlen);
                break;
            default:
                String errmsg = "Application error - invalid reinvestment rate option(" + reinVestMethod + ") was detetected";
                bond.error.setError(getClass().getName(), errmsg);
                reinRt = 0.;
        }
        return reinRt;
    }

    private double getReinvestRate(double matlen) {
        double reinRt = 0.;
        if (bond.noReinRts > 0) {
            int no = -1;

            while (++no < bond.noReinRts) {
                if (bond.reinMatlen[no] == matlen)
                    return bond.reinRt[no];
                else if (bond.reinMatlen[no] > matlen) {
                    double rightMat = bond.reinMatlen[no];
                    double rightVal = bond.reinRt[no];
                    double leftMat = bond.reinMatlen[no - 1];
                    double leftVal = bond.reinRt[no - 1];
                    reinRt = utility.interpDouble(matlen, leftMat, leftVal, rightMat, rightVal);
                    return reinRt;
                }
            }
        }
        int no = -1;
        if (matlen > bond.reinMatlen[0]) {
            return bond.reinRt[0];
        }
        while (++no < bond.noReinRts) {
            if (bond.reinMatlen[no] == matlen)
                return bond.reinRt[no];
            else if (bond.reinMatlen[no] > matlen) {
                double rightMat = bond.reinMatlen[no];
                double rightVal = bond.reinRt[no];
                double leftMat = bond.reinMatlen[no - 1];
                double leftVal = bond.reinRt[no - 1];
                reinRt = utility.interpDouble(matlen, leftMat, leftVal, rightMat, rightVal);
                return reinRt;
            }
        }
        return bond.reinRt[bond.noReinRts - 1];
    }

    /* returns principal value per unit of 1 par */
    public double getRedeemValue(String instype, Dates dtSettle, double redeemInt) {
        double rrv = 0.;
        if (bond.stripType.equals(BondConsts.STRIPIO))
            return 0.;
        switch (instype) {
            case Instype.CURRENCY:
            case Instype.DISCOUNT:
            case Instype.ATMATURITY:
            case Instype.FIXEDRATE:
            case Instype.FLOATER:
                rrv = redeemInt;
                break;
            case Instype.SINKER:
            case Instype.SINKFLOAT:
            case Instype.DISCOUNTSINK:
                rrv = getRedeemValueSink(dtSettle, redeemInt);
                break;
            case Instype.INDEXED:
                rrv = getRedeemValueIndexed(dtSettle);
                break;
            case Instype.MORTGAGE:
                //  bond.mortgage.getPassthruFactor(dtSettle, bond.passModel, bond.prepaySpeed);
                rrv = mortgage.getPassthruFactor(dtSettle);
                break;
            case Instype.PERPETUAL:
            case Instype.PERPETUALFLOAT:
            case Instype.IRSSWAP:
                if (dtSettle.eqDate(bond.dtRedeem))
                    rrv = redeemInt;
                break;
            default:
                String errmsg = "Invalid instrument type " + instype + " was specified";
                bond.error.setError(getClass().getName(), errmsg);
                return 0.;
        }
        return rrv;
    }

    public double calcReinvestCash(int calcNo, Dates dtCashflow, Dates dtMaturity, double cashToRein) {
        double cfRein = 0.;
        double matlen = bond.time.getYearsBetweenDates(dtCashflow, dtMaturity);
        double reinRt = getReinvestRate(dtCashflow, dtMaturity, bond.reinVestMethod);
        double noPer = matlen / Defaults.REINVESTFREQ;
        cfRein = cashToRein * Math.pow((1. + reinRt / Defaults.REINVESTFREQ), noPer);
        return cfRein;
    }

    public boolean isExDividend(Dates dtSettle, Dates dtExdiv) {
        Dates dtCpn = new Dates();
        if (dtExdiv.geDate(dtExdiv, dtSettle)) {
            dtCpn = bond.intSchedRule.getNextSchedDate(BondConsts.AFTERONDATE, dtSettle);
            return dtExdiv.leDate(dtExdiv, dtSettle);
        }
        return false;
    }

    public double getPrinPaymentSinkFactor(Dates dtSettle, double redeemPrin) {
        double prinPay = 0., sinkPay = 0., rrv = bond.redeemInt;
        int sNo = -1;

        SchedTrack schedTrack = new SchedTrack();
        Dates dtNext = bond.dtSinks[0];
        if (bond.stripType.equals(BondConsts.STRIPIO))
            return 0.;
        if (dtSettle.eqDate(dtSettle, bond.dtRedeem)) {
            prinPay = getRedeemValue(bond.instype, dtSettle, redeemPrin);
            return prinPay;
        }
        while (++sNo < bond.schedTracks.noSchedTracks) {
            int schedType = bond.schedTracks.schedTrackList.get(sNo).schedType;
            if (schedType == SchedTrack.SINKTRACK) {
                if (dtSettle.eqDate(dtSettle, bond.schedTracks.schedTrackList.get(sNo).schedDate)) {
                    sinkPay = bond.schedTracks.schedTrackList.get(sNo).schedValue;
                    rrv -= sinkPay;
                    return sinkPay;
                }
            }
        }
        prinPay = 0.;
        return prinPay;
    }

    public double getRedeemValueSink(Dates dtSettle, double redeemPrin) {
        double rrv = redeemPrin;
        // reduce all sinks up to an including settle date
        if (bond.noSinkDts == 0)
            return redeemPrin;
        int it = -1;
        while (++it < bond.schedTracks.noSchedTracks) {
            int schedType = bond.schedTracks.schedTrackList.get(it).schedType;
            Dates dtSched = new Dates();
            dtSched.setDate(bond.schedTracks.schedTrackList.get(it).schedDate);
            if (schedType == SchedTrack.SINKTRACK && dtSched.leDate(dtSched, dtSettle))
                rrv -= bond.schedTracks.schedTrackList.get(it).schedValue;
        }
        return rrv;
    }

    public double getPrinPaymentIndexed(Dates dtSettle) {
        double prinPay = 0.;
        // get index ratios from schedTracks
        if (dtSettle.eqDate(dtSettle, bond.dtRedeem)) {
            double idxRatio = indexed.getIndexRatio(dtSettle, bond.idxInterpMeth, bond.inflationRt);
            if (dtSettle.eqDate(dtSettle, bond.dtMaturity))
                prinPay = Math.max(bond.matFloor, idxRatio * bond.redeemPrin);
            else if (bond.adjustMaturityYn == 'Y')
                prinPay = idxRatio * bond.redeemInt;
            else
                prinPay = bond.redeemInt;
        } else
            prinPay = 0.;
        return prinPay;
    }


    public double getRedeemValueIndexed(Dates dtCalc) {
        double rrv = 0;
        double idxRatio = indexed.getIndexRatio(dtCalc, bond.idxInterpMeth, bond.inflationRt);
        rrv = bond.redeemPrin * idxRatio;
        // if (dtCalc.eqDate(dtCalc, bond.dtMaturity)) {
        if (bond.adjustMaturityYn == 'N')
            rrv = bond.redeemPrin;
        // }
        return rrv;
    }

    class KeyRateDuration {
        final int noKeyRts = 15;

        String[] keyRateNames = {"1m", "3m", "6m", "1yr", "2Yr", "3Yr",
                "5Yr", "7Yr", "10Yr", "15Yr", "20Yr", "25Yr", "30Yr",
                "40Yr", "50Yr"};
        double[] keyRateDurations = new double[noKeyRts];
        double[] keyRateCashflows = new double[Limits.MAXCASHFLOWS];
        double[] keyRateTerm = {1 / 12., 3 / 12., 6 / 12., 1., 2., 3., 5., 7., 10., 15., 20., 25., 30., 40., 50.};
        double totalKeyRates = 0.;

        KeyRateDuration() {
            int kr = -1;
            while (++kr < noKeyRts)
                this.keyRateCashflows[kr] = 0.;
        }

        // allocate the bond cashflow to nearest key rate maturity lengths.
        public double setKeyRateCashflow(int cfNo) {
            // first right most key rate
            int kr = -1, idxLeft = 0, idxRight = 0;
            double cfRight, cfLeft, diffLen, leftLen, rightLen;
            double cfYears = cashflowList.get(cfNo).years;
            double cfFlow = cashflowList.get(cfNo).totCashflow;
            // get the key rate whose term is on or before the cashflow length
            while (++kr < keyRateDuration.noKeyRts) {
                double term = keyRateDuration.keyRateTerm[kr];
                if (cfYears == term) {
                    keyRateDuration.keyRateCashflows[kr] = cfFlow;
                    return cfFlow;
                }
                if (term > cfYears) {
                    if (kr == 0) {
                        keyRateDuration.keyRateCashflows[kr] += cfFlow;
                        return cfFlow;
                    }
                    idxRight = kr;
                    idxLeft = kr - 1;
                    rightLen = keyRateDuration.keyRateTerm[kr];
                    leftLen = keyRateDuration.keyRateTerm[kr - 1];
                    diffLen = rightLen - leftLen;
                    // double leftFrac = 1. - (cfYears - leftLen) / diffLen;
                    double leftFrac = (cfYears - leftLen) / diffLen;
                    // double rightFrac = 1. - leftFrac;
                    double rightFrac = (rightLen - cfYears) / diffLen;
                    double cashLeft = cfFlow * (1. - leftFrac);
                    double cashRight = cfFlow - cashLeft;
                    keyRateDuration.keyRateCashflows[idxLeft] += cashLeft;
                    keyRateDuration.keyRateCashflows[idxRight] += cashRight;
                    double keyCash = cashRight + cashLeft;
                    return keyCash;
                }
            }
            keyRateDuration.keyRateCashflows[kr] += cfFlow;
            return cfFlow;
        }

        public int keyRateCashflow(int calcNo, int securityNo, String instype, Dates dtSettle, Dates dtRedeem,
                                   double redeemPrin, double redeemInt) {
            double scale = calcDef.calcDefs.get(calcNo).scale;
            switch (instype) {
                case Instype.DISCOUNT:
                    discount.cashflow(calcNo, securityNo, dtSettle, bond.dtRedeem, bond.redeemPrin);
                    cashflowList = discount.cashflowList;
                    break;
                case Instype.ATMATURITY:
                    atMaturity.cashflow(calcNo, securityNo, dtSettle, bond.dtRedeem, bond.redeemPrin);
                    cashflowList = atMaturity.cashflowList;
                    break;
                case Instype.FIXEDRATE:
                    cashflow(CalcList.KEYRATEDURATION, scale, securityNo, instype, dtSettle, dtRedeem);
                    break;
                case Instype.FLOATER:
                    cashflow(CalcList.KEYRATEDURATION, scale, securityNo, instype, dtSettle, dtRedeem);
                    break;
                case Instype.SINKER:
                    cashflow(CalcList.KEYRATEDURATION, scale, securityNo, instype, dtSettle, dtRedeem);
                    break;
                case Instype.MORTGAGE:
                    //       mortgage.cashflow(KEYRATEDURATION, dtSettle, bond.passModel, bond.prepaySpeed, bond.dtPassFactor, bond.passFactor);
                    mortgage.cashflow(calcNo, scale, securityNo, dtSettle, dtRedeem, redeemPrin, redeemInt);
                    break;
                case Instype.INDEXED:
                    cashflow(CalcList.KEYRATEDURATION, scale, securityNo, instype, dtSettle, dtRedeem);
                    break;
                default:
                    String errmsg = "Key rate calculation request for instrument " + instype + " is not supported";
                    bond.error.setError(getClass().getName(), errmsg);
                    return 0;
            }
            int kr = -1, cf = -1;
            int lastKr = 0;
            cashflowResults.totCashflow = 0.;
            int noCashflows = cashflowList.size();
            while (++cf < noCashflows)
                keyRateDuration.keyRateCashflows[cf] = 0.;
            cf = -1;
            double cfFlow = 0.;
            while (++cf < noCashflows) {
                cfFlow += setKeyRateCashflow(cf);
            }
            return cf;
        }

        int calcKeyRateDuration(int calcNo, int securityNo, String instype, double redeemPrin, double redeemInt) {
            keyRateDuration.totalKeyRates = 0.;
            double yldShift = CalUnits.durationYieldShift;
            double priceShiftedUp = 0.;
            double priceShiftedDown = 0.;
            double priceUnShifted = 0.;
            int kr = -1;
            if (instype.equals(Instype.CURRENCY)) {
                while (++kr < keyRateDuration.noKeyRts) {
                    keyRateDuration.keyRateDurations[kr] = 0.;
                }
                return keyRateDuration.noKeyRts;

            }
            keyRateDuration.keyRateCashflow(calcNo, securityNo, instype, bond.dtSettle, bond.dtRedeem, redeemPrin, redeemInt);
            // double priceUnshifted = presentValueKeyRates(UnAvails.NOTAVAILINT, shift);
            kr = -1;
            if (instype.equals(Instype.CURRENCY)) {
                while (++kr < keyRateDuration.noKeyRts) {
                    keyRateDuration.keyRateDurations[kr] = 0.;
                }
                return keyRateDuration.noKeyRts;
            }
            kr = -1;
            while (++kr < keyRateDuration.noKeyRts) {
                priceShiftedDown = presentValueKeyRates(kr, -yldShift);
                priceShiftedUp = presentValueKeyRates(kr, yldShift);
                priceUnShifted = presentValueKeyRates(kr, 0.);
                if (yldShift == 0. || priceUnShifted == 0.)
                    keyRateDuration.keyRateDurations[kr] = 0.;
                else {
                    double prcPct = (priceShiftedDown - priceShiftedUp) / priceUnShifted;
                    double yldPct = 2 * yldShift;
                    keyRateDuration.keyRateDurations[kr] = calcDef.calcDefs.get(CalcList.KEYRATEDURATION).scale + prcPct / yldPct;
                }

            }
            return keyRateDuration.noKeyRts;
        }

        public double presentValueKeyRates(int keyNo, double shift) {
            double pv = 0., yldCrv = 0., matLen = 0., rate = 0., cash = 0.;
            int compoundFreq = CalUnits.SEMIANNUALFQ;
            matLen = CalUnits.MONTHSPERYEAR * keyRateDuration.keyRateTerm[keyNo];
            yldCrv = bond.discCurve.getSpotRate(matLen, bond.yieldCurveInterpMeth);
            rate = shift + yldCrv;
            double discFac = 1. / (1. + rate / compoundFreq);
            double perLen = compoundFreq * keyRateDuration.keyRateTerm[keyNo];
            double pvFac = Math.pow(discFac, perLen);
            cash = keyRateDuration.keyRateCashflows[keyNo];
            pv = pvFac * cash;
            return pv;

        }

        public String outputKeyRateDuration(int noCalc, int calcNo, String newLine, int iResult) {
            String strKr = "", strVal = null;
            String strKeyRate, strKeyRateJSON;
            int kr = -1;
            if (error.hasErrorYn == 'Y')
                strKr = bond.error.getErrorMessage();
            strKeyRate = outKeyRateString(noCalc, calcNo, newLine, iResult);
            if (bond.fileOutYn == 'Y') {
                if ('Y' == bond.error.hasErrorYn) {
                    String errmsg = bond.error.getErrorMessage();
                    fileFormatter.format("%s\n", errmsg);
                    return strKr;
                }
                fileFormatter.format(newLine);
                fileFormatter.format("KeyRateDurations=%d\n", keyRateDuration.noKeyRts);
                fileFormatter.format("TotalKeyRateDuration=%.4f\n", keyRateDuration.totalKeyRates);
                kr = -1;
                while (++kr < keyRateDuration.noKeyRts - 1) {
                    strVal = keyRateDuration.keyRateNames[kr] + "=" + String.format("%.4f", keyRateDuration.keyRateDurations[kr]);
                    fileFormatter.format("%s\n", strVal);
                }
                strVal = keyRateDuration.keyRateNames[kr] + "=" + String.format("%.4f", keyRateDuration.keyRateDurations[kr]);
                fileFormatter.format("%s\n", strVal);
            }
            if (commandLine.isInputJSONYn.equals("Y"))
                strKeyRate = outKeyRateJSON(noCalc, calcNo, iResult);
            return strKeyRate;
        }

        public String outKeyRateString(int noCalc, int calcNo, String newLine, int iResult) {
            String strKr = "", strVal = null;
            int kr = -1;
            strKr = "KeyRateDurations=" + keyRateDuration.noKeyRts + ";";
            String totKeyRt = String.format("TotalKeyRateDuration=%.4f;", keyRateDuration.totalKeyRates);
            strKr = strKr + totKeyRt;
            while (++kr < keyRateDuration.noKeyRts - 1) {
                strVal = keyRateDuration.keyRateNames[kr] + "=" + String.format("%.4f", keyRateDuration.keyRateDurations[kr]);
                strKr = strKr.concat(strVal + ";");
            }
            strVal = keyRateDuration.keyRateNames[kr] + "=" + String.format("%.4f", keyRateDuration.keyRateDurations[kr]);
            strKr = strKr.concat(strVal);
            return strKr;
        }


        public String outKeyRateJSON(int noCalc, int calcNo, int iResult) {
            String strKr = "", format = null;
            if (noCalc == 0) {
                strKr = "{";
            }

            int kr = -1;
            if (UnAvails.isUnAvail(bond.sfigStatistic))
                format = "%.4f";
            else
                format = "%." + bond.sfigStatistic + "f";
            while (++kr < keyRateDuration.noKeyRts) {
                if (kr == keyRateDuration.noKeyRts - 1)
                    strKr = strKr + "\"" + keyRateDuration.keyRateNames[kr] + "\"" + ":" + String.format(format, keyRateDuration.keyRateDurations[kr]);
                else
                    strKr = strKr + "\"" + keyRateDuration.keyRateNames[kr] + "\"" + ":" + String.format(format, keyRateDuration.keyRateDurations[kr]) + ",";
            }
            if (noCalc == bond.noCalcRequests - 1)
                strKr = strKr + "}";
            return strKr;
        }

    }


    class RootResult {
        double func; // value of
        double dFunc; //
        boolean isOk;

        RootResult() {
            func = UnAvails.NOTAVAILDOUBLE;
            dFunc = UnAvails.NOTAVAILDOUBLE;
            isOk = true;
        }
    }


    public int couponDays(int calcNo, String instype, Dates dtSettle) {
        int cpnDays = 0;
        String errmsg = null;
        if (bond.stripType.equals(BondConsts.STRIPPO))
            return 0;
        switch (instype) {
            case Instype.DISCOUNT:
                return 0;
            case Instype.CURRENCY:
                return 0;
            case Instype.ATMATURITY:
                cpnDays = dayCount.getDaycount(bond.dtDated, dtSettle, bond.dtMaturity, bond.dcAccruedInt);
                return cpnDays;
            case Instype.FIXEDRATE:
            case Instype.FACTORED:
            case Instype.FLOATER:
            case Instype.INDEXED:
            case Instype.MORTGAGE:
            case Instype.SINKER:
                Dates dtLastCpn = bond.intSchedRule.getNextSchedDate(BondConsts.BEFOREONDATE, bond.dtSettle);
                if (errmsg != null) {
                    bond.error.setError(getClass().getName(), errmsg);
                    return 0;
                }
                cpnDays = dayCount.getDaycount(dtLastCpn, dtSettle, bond.dtMaturity, bond.dcAccruedInt);
                break;
            default:
                errmsg = "Invalid instruement " + instype + "specified";
                bond.error.setError(getClass().getName(), errmsg);
                cpnDays = 0;
                break;

        }
        return cpnDays;
    }

    private double getInterstCash(int calcNo, String instype, Dates dtRedeem, double redeemInt, SchedTrack cashTrack) {
        double couponCash = 0., payCash = 0.;
        double couFacNum = 0., couFacDen = 0.;
        double couPayFactor = 0.;
        Dates dtPay = cashTrack.schedDate;
        char use360Yn = 'N';
        int stop;
        Dates dtLastAccrue;
        // is this an odd last coupon
        SchedTrack lastPayTrack = bond.schedTracks.getSchedTrackBefore(dtPay, SchedTrack.INTERESTPAYTRACK);
        dtLastAccrue = lastPayTrack.schedDate;
        if (!isOnCouponDate(cashTrack)) {
            if (dtLastAccrue.ltDate(dtRedeem)) {
                dtPay = dtRedeem;
            } else
                return 0.;
        }
        // in the case of inteerst
        double rrv = getRedeemValue(bond.instype, dtLastAccrue, redeemInt);
        double daysNum = 0.;
        if (bond.stripType.equals(BondConsts.STRIPPO))
            return 0.;
        switch (instype) {
            case Instype.DISCOUNT:
                couponCash = 0.;
                break;
            case Instype.CURRENCY:
            case Instype.ATMATURITY:
                if (dtRedeem.eqDate(dtPay))
                    couponCash = bond.cpnRt * dayCount.getDaycount(bond.dtDated, dtRedeem, bond.dtMaturity, bond.dcAccruedInt);
                couponCash /= dayCount.getDaysInYear(bond.dcAccruedInt, bond.dtDated, dtRedeem, 1);
                break;
            case Instype.FIXEDRATE:
            case Instype.INDEXED:
            case Instype.SINKER:
            case Instype.PERPETUAL:
                if (bond.dcAccruedInt == DayCount.DCMethod.DC_30ISDA || bond.dcAccruedInt == DayCount.DCMethod.DC_ACTISDA) { // this is constant accrual
                    couFacNum = dayCount.getDaycount(dtLastAccrue, dtPay, bond.dtMaturity, bond.dcAccruedInt);
                    couFacDen = dayCount.getDaysInYear(bond.dcAccruedInt, dtPay, dtRedeem, bond.intSchedRule.compoundFreq);
                    if (couFacDen == 0) {
                        bond.error.setError(getClass().getName(), "Attempt to divide by zero");
                        return 0.;
                    }
                    couponCash = rrv * bond.cpnRt * couFacNum / couFacDen; // constant accrual
                } else {
                    couponCash = rrv * bond.cpnRt / bond.intSchedRule.compoundFreq; // constant cash
                }
                break;
                /*
            case Instype.MORTGAGE:
                couponCash = mortgage.getInterestCash(cashTrack);
                break;
                 */
            case Instype.FLOATER:
                couponCash = floater.getInterestCash(calcNo, cashTrack);
                break;
            default:
                String errmsg = "invalid instrument type detected";
                bond.error.setError(getClass().getName(), errmsg);
        }
        return couponCash;
    }

    private double getInterstCash(String instype, Dates dtPay) {
        double intCash = 0.;
        Dates dtLastPay = bond.intSchedRule.getNextSchedDate(SchedTracks.BEFOREDATE, dtPay);
        double rrv = getRedeemValue(bond.instype, dtLastPay, bond.redeemInt);
        double daysNum = 0.;
        if (bond.stripType.equals(BondConsts.STRIPPO))
            return 0.;
        switch (instype) {
            case Instype.DISCOUNT:
                intCash = 0.;
                break;
            case Instype.CURRENCY:
            case Instype.ATMATURITY:
                intCash = bond.cpnRt * dayCount.getDaycount(bond.dtDated, bond.dtMaturity, bond.dtMaturity, bond.dcAccruedInt);
                intCash /= dayCount.getDaysInYear(bond.dcAccruedInt, bond.dtDated, bond.dtMaturity, 1);
                break;
            case Instype.FIXEDRATE:
            case Instype.SINKER:
            case Instype.PERPETUAL:
            case Instype.INDEXED:
            case Instype.FLOATER:
                if (bond.dcAccruedInt == DayCount.DCMethod.DC_30ISDA || bond.dcAccruedInt == DayCount.DCMethod.DC_ACTISDA) { // this is constant accrual
                    int couFacNum = dayCount.getDaycount(dtLastPay, dtPay, bond.dtMaturity, bond.dcAccruedInt);
                    int couFacDen = dayCount.getDaysInYear(bond.dcAccruedInt, dtLastPay, dtPay, bond.intSchedRule.compoundFreq);
                    if (couFacDen == 0) {
                        bond.error.setError(getClass().getName(), "Attempt to divide by zero");
                        return 0.;
                    }
                    intCash = rrv * bond.cpnRt * couFacNum / couFacDen; // constant accrual
                } else {
                    intCash = rrv * bond.cpnRt / bond.intSchedRule.compoundFreq; // constant cash
                }
                break;
            default:
                String errmsg = "invalid instrument type detected";
                bond.error.setError(getClass().getName(), errmsg);
        }
        return intCash;
    }

    public double getSchedulePrin(int calcNo, String instype, Dates dtRedeem, double redeemPrin, SchedTrack schedTrack) {
        double cfSched = 0.;
        SchedTrack schedTrack1 = new SchedTrack();
        if (bond.stripType.equals(BondConsts.STRIPIO)) {
            return 0.;
        }
        if (schedTrack.schedDate.gtDate(schedTrack.schedDate, dtRedeem))
            return 0.;
        Dates dtSink = new Dates();
        Dates dtPrin = new Dates();
        dtPrin = schedTrack.schedDate;
        Dates dtSched = new Dates();
        int trackNo = 0;
        switch (bond.instype) {
            case Instype.DISCOUNT:
            case Instype.CURRENCY:
            case Instype.ATMATURITY:
            case Instype.FIXEDRATE:
            case Instype.INDEXED:
            case Instype.FLOATER:
            case Instype.PERPETUAL:
            case Instype.PERPETUALFLOAT:
                if (dtPrin.eqDate(dtPrin, dtRedeem))
                    cfSched = getRedeemValue(instype, dtRedeem, redeemPrin);
                else
                    cfSched = 0.;
                break;
            case Instype.SINKER:
            case Instype.DISCOUNTSINK:
                if (dtPrin.eqDate(dtPrin, dtRedeem)) {
                    cfSched = getRedeemValue(instype, dtRedeem, redeemPrin);
                } else {
                    trackNo = schedTrack.trackNo;
                    schedTrack1 = bond.schedTracks.getSchedTrackOnBefore(dtPrin, SchedTrack.SINKTRACK);
                    dtSink = schedTrack1.schedDate;
                    if (dtPrin.eqDate(dtPrin, dtSink))
                        cfSched = schedTrack1.schedValue;
                    else
                        cfSched = 0.;
                }
                break;
                /*
            case Instype.MORTGAGE:
                cfSched = mortgage.getPassthruSchedPay(dtPrin);
                break;
                 */
            default:
                String errmsg = "Invalid instrument type " + bond.instype + " was specified";
                bond.error.setError(getClass().getName(), errmsg);
                cfSched = 0.;
                break;
        }
        return cfSched;
    }

    public double getPrepayPrin(int calcNo, String instype, Dates dtRedeem, double redeemPrin, SchedTrack schedTrack) {
        double cfPrePay = 0.;
        Dates dtPrin = schedTrack.schedDate;
        if (!instype.equals(Instype.MORTGAGE))
            return 0.;
        //cfPrePay = mortgage.getPrepayCash(dtPrin);
        return cfPrePay = 0.;
    }

    public ArrayList<CashflowResults> cashflow(int calcNo, double scale, int securityNo, String instype, Dates dtStart, Dates dtEnd) {
        // ArrayList<CashflowResults> cashflowList;
        String side = bond.swapSide;
        char includeReinvestYn = 'N';
        int noFlows = cashflowList.size();
        if (instype.equals(Instype.IRSSWAP)) {
            boolean isOk = bond.status.isCashflowValid(calcNo, noFlows, dtStart, dtEnd);
            if (!isOk)
                cashflowList = getCashflowIRS(calcNo, side, dtStart, dtEnd, includeReinvestYn);
        } else {
            boolean isOk = bond.status.isCashflowValid(calcNo, noFlows, dtStart, dtEnd);
            if (!isOk)
                cashflowList = getCashflow(calcNo, instype, dtStart, dtEnd, includeReinvestYn);
        }

        noFlows = cashflowList.size();
        bond.status.setCashflowStatus(calcNo, noFlows, dtStart, dtEnd);
        return cashflowList;
    }

    public ArrayList<CashflowResults> getCashflow(int calcNo, String instype, Dates dtStart, Dates dtEnd, char includeReinvestYn) {
        ArrayList<CashflowResults> cashFlowList = new ArrayList<>();
        CashflowResults cashFlow = new CashflowResults();
        // one fixed and one variable
        SchedRule intPayRule = null;
        SchedRule resetRule = null;
        int resetTrack = 0;
        // align start and end interval
        int noTracks = bond.schedTracks.schedTrackList.size();
        Dates dtFirstSched = bond.schedTracks.schedTrackList.get(0).schedDate;
        Dates dtLastSched = bond.schedTracks.schedTrackList.get(noTracks - 1).schedDate;
        if (dtStart.leDate(dtFirstSched))
            dtStart = dtFirstSched;
        if (dtEnd.gtDate(dtLastSched))
            dtEnd = dtLastSched;
        int sign = 1;
        switch (instype) {
            case Instype.DISCOUNT:
                cashflowList = discount.cashflow(CalcList.CASHFLOW, 0, bond.dtSettle, bond.dtMaturity, bond.redeemPrin);
                return cashflowList;
            case Instype.ATMATURITY:
                cashflowList = atMaturity.cashflow(CalcList.CASHFLOW, 0, bond.dtSettle, bond.dtMaturity, bond.redeemPrin);
                return cashflowList;
            case Instype.FIXEDRATE:
            case Instype.INDEXED:
                intPayRule = bond.intSchedRule;
                break;
            case Instype.FLOATER:
                intPayRule = bond.intSchedRule;
                if (bond.resetFixedYn == 'Y' || bond.noResetRts > 0)
                    resetRule = intPayRule;
                else
                    resetRule = bond.resetSchedRule;
                break;
            case Instype.MORTGAGE:
                cashFlowList = mortgage.cashflow(CalcList.CASHFLOW, 1., 0, bond.dtSettle, bond.dtMaturity, bond.redeemPrin, bond.redeemInt);
                return cashFlowList;
            case Instype.PERPETUAL:
                intPayRule = bond.intSchedRule;
                break;
        }
        SchedTrack schedTrack = new SchedTrack();
        double totAccInt = 0., accInt = 0.;
        double totCashflow = 0.;
        double fedTaxDue = 0., stateTaxDue = 0.;
        double fedTax = 0., stateTax = 0.;
        double lastReinRt = 0.;
        // get starting reinvestment rate
        schedTrack = bond.schedTracks.getSchedTrackOnBefore(dtStart, SchedTrack.REINVESTRATETRACK);
        if (schedTrack.found && includeReinvestYn == 'Y')
            lastReinRt = schedTrack.schedValue;
        else {
            lastReinRt = 0.;
            error.clearError();
        }
        // get previous payment date for interest calc
        double lastResetRt;
        Dates dtLastAccrue = new Dates();
        schedTrack = bond.schedTracks.getSchedTrackOnBefore(bond.dtSettle, SchedTrack.RESETRATETRACK);
        if (schedTrack.found) {
            lastResetRt = schedTrack.schedValue;
            dtLastAccrue.setDate(schedTrack.schedDate);
        } else {
            lastResetRt = 0;
            // dtLastAccrue = dtStart;
            dtLastAccrue = intPayRule.getNextSchedDate(SchedTracks.BEFOREDATE, dtStart);
            error.clearError();
        }
        double cumReinVestCash = 0.;
        // inclusive
        int no = 0;
        int nextTrackNo = -1;
        int endTrackNo;
        if (dtEnd.geDate(bond.dtMaturity))
            endTrackNo = bond.schedTracks.schedTrackList.size() - 1;
        else
            endTrackNo = getEndTrackNo(bond.schedTracks.schedTrackList, dtEnd);
        Dates dtCashflow = new Dates();
        dtCashflow.setDate(schedTrack.schedDate);
        // include end date
        while (++nextTrackNo <= endTrackNo) {
            schedTrack = bond.schedTracks.schedTrackList.get(nextTrackNo);
            dtCashflow.setDate(schedTrack.schedDate);
            if (dtCashflow.leDate(dtStart))
                continue;
            cashFlow.zero();
            boolean include = true;
            switch (schedTrack.schedType) {
                case SchedTrack.INTERESTPAYTRACK:
                    if (UnAvails.isAvail(bond.cpnRt))
                        cashFlow.intCash = getInterstCash(instype, dtCashflow);
                    else {
                        cashFlow.intCash = accInt;
                        dtLastAccrue.setDate(dtCashflow);
                        accInt = 0.;
                    }
                    if (bond.afterTaxCalcsYn == 'Y') {
                        stateTax = bond.taxes.calcStateIncTaxes(cashFlow.intCash);
                        fedTax = bond.taxes.calcFedIncTaxes(cashFlow.intCash);
                        stateTaxDue += stateTax;
                        fedTaxDue += fedTax;
                    }
                    break;
                case SchedTrack.SINKTRACK:
                    cashFlow.schedPrinCash = schedTrack.schedValue;
                    if (bond.afterTaxCalcsYn == 'Y') {
                        stateTax = bond.taxes.calcStateCapGLTaxes(cashFlow.schedPrinCash, bond.purPrc, bond.dtRedeem, bond.dtPur);
                        fedTax = bond.taxes.calcFedCapGLTaxes(cashFlow.schedPrinCash, bond.purPrc, bond.dtRedeem, bond.dtPur, stateTax);
                        stateTaxDue += stateTax;
                        fedTaxDue += fedTax;
                    }
                    break;
                case SchedTrack.RESETRATETRACK:
                    double coufac = intPayRule.getCouponFactor(dtLastAccrue, dtCashflow) / intPayRule.compoundFreq;
                    accInt += lastResetRt * coufac;
                    ;
                    lastResetRt = schedTrack.schedValue;
                    dtLastAccrue.setDate(dtCashflow);
                    break;
                case SchedTrack.PIKTRACK:
                    cashFlow.schedPrinCash = sign * schedTrack.schedValue;
                    if (bond.afterTaxCalcsYn == 'Y') {
                        stateTax = bond.taxes.calcStateCapGLTaxes(cashFlow.schedPrinCash, bond.purPrc, bond.dtRedeem, bond.dtPur);
                        fedTax = bond.taxes.calcFedCapGLTaxes(cashFlow.schedPrinCash, bond.purPrc, bond.dtRedeem, bond.dtPur, stateTax);
                        stateTaxDue += stateTax;
                        fedTaxDue += fedTax;
                    }
                    break;
                case SchedTrack.REINVESTRATETRACK:
                    lastReinRt = 0.;
                    if (includeReinvestYn == 'Y') {
                        lastReinRt = schedTrack.schedValue;
                    }
                    break;
                case SchedTrack.CALLTRACK:
                    if (dtCashflow.eqDate(dtEnd)) {
                        cashFlow.schedPrinCash = schedTrack.schedValue;
                        if (bond.afterTaxCalcsYn == 'Y') {
                            stateTax = bond.taxes.calcStateCapGLTaxes(cashFlow.schedPrinCash, bond.purPrc, bond.dtRedeem, bond.dtPur);
                            fedTax = bond.taxes.calcFedCapGLTaxes(cashFlow.schedPrinCash, bond.purPrc, bond.dtRedeem, bond.dtPur, stateTax);
                            stateTaxDue += stateTax;
                            fedTaxDue += fedTax;
                        }
                    }
                    break;
                case SchedTrack.REDEEMTRACK:
                    cashFlow.schedPrinCash = getRedeemValue(bond.instype, bond.dtMaturity, bond.redeemPrin);
                    if (bond.afterTaxCalcsYn == 'Y') {
                        stateTax = bond.taxes.calcStateCapGLTaxes(cashFlow.schedPrinCash, bond.purPrc, bond.dtRedeem, bond.dtPur);
                        fedTax = bond.taxes.calcFedCapGLTaxes(cashFlow.schedPrinCash, bond.purPrc, bond.dtRedeem, bond.dtPur, stateTax);
                        stateTaxDue += stateTax;
                        fedTaxDue += fedTax;
                    }
                    cashFlow.intCash += cumReinVestCash;
                    break;
                case SchedTrack.FEDTAXPAYMENTTRACK:
                    cashFlow.stateTaxPaid = sign * stateTaxDue;
                    cashFlow.fedTaxPaid = sign * fedTaxDue;
                    fedTaxDue = stateTaxDue = 0.;
                    break;
                default:
                    include = false;
                    break;
            }
            totCashflow = cashFlow.intCash + cashFlow.schedPrinCash - cashFlow.fedTaxPaid - cashFlow.stateTaxPaid;
            double reinvestCash = cashFlow.intCash + cashFlow.schedPrinCash;
            if (includeReinvestYn == 'Y' && reinvestCash != 0.) {
                double perCount = dayCount.simplePeriodCount(dtCashflow, dtEnd, CalUnits.SEMIANNUALREPEAT);
                if (bond.afterTaxCalcsYn == 'Y') {
                    stateTax = bond.taxes.calcStateIncTaxes(reinvestCash);
                    fedTax = bond.taxes.calcFedIncTaxes(reinvestCash);
                    stateTaxDue += stateTax;
                    fedTaxDue += fedTax;
                }
                cumReinVestCash += reinvestCash * Math.pow(1. + lastReinRt / CalUnits.SEMIANNUALFQ, perCount);
            }
            if (totCashflow != 0. && include) {
                cashFlow.dtCashflow.setDate(dtCashflow);
                cashFlow.years = dayCount.getYearsBetweenDates(dtStart, dtCashflow);
                cashFlow.perLen = intPayRule.getCouponFactor(dtStart, dtCashflow);
                cashFlow.strDayOfWeek = dayCount.time.getDayOfWeek(dtCashflow);
                cashFlow.totPrinCash += cashFlow.schedPrinCash;
                cashFlow.totCashflow += cashFlow.schedPrinCash + cashFlow.intCash - cashFlow.fedTaxPaid - cashFlow.stateTaxPaid;
                if (nextTrackNo == endTrackNo)
                    cashFlow.intCash += cumReinVestCash;
                cashFlowList.add(no++, new CashflowResults(cashFlow));
            }
        }

        return combineCashflows(cashFlowList);
    }

    public ArrayList<CashflowResults> getCashflowIRSSide(String instype, String side, Dates dtStart, Dates dtEnd, char includeReinvestYn) {
        ArrayList<CashflowResults> cashFlowList = new ArrayList<>();
        CashflowResults cashFlow = new CashflowResults();
        SchedTrack schedTrack = new SchedTrack();
        // one fixed and one variable
        SchedRule intPaylegRule = null;
        SchedRule intReclegRule = null;
        SchedRule paylegResetRule = null;
        SchedRule reclegResetRule = null;
        Dates dtLastAccrue = new Dates();
        double lastPayResetRt = 0., lastRecResetRt = 0.;
        int noTracks = bond.schedTracks.schedTrackList.size();
        double accInt = 0.;
        if (side.equals(Codes.PAYLEG)) {
            intPaylegRule = bond.paylegSchedRule;
            paylegResetRule = bond.paylegResetSchedRule;
            if (bond.paylegFixedYn == 'N') {
                dtLastAccrue = bond.paylegSchedRule.getNextSchedDate(SchedTracks.ONBEFOREDATE, dtStart);
                schedTrack = bond.schedTracks.getSchedTrackOnBefore(dtLastAccrue, SchedTrack.PAYLEGRESETRATETRACK);
                if (schedTrack.found) {
                    lastPayResetRt = schedTrack.schedValue;
                } else {
                    if (schedTrack.found == false) {
                        String errmsg = "No schedule found for " + SchedTrack.PAYLEGRESETRATETRACKNAME;
                        error.setError(getClass().getName(), errmsg);
                        return cashFlowList;
                    }
                }
            }
        }
        if (side.equals(Codes.RECLEG)) {
            intReclegRule = bond.reclegSchedRule;
            reclegResetRule = bond.reclegResetSchedRule;
            dtLastAccrue = intReclegRule.getNextSchedDate(SchedTracks.ONBEFOREDATE, dtStart);
            if (bond.reclegFixedYn == 'N') {
                schedTrack = bond.schedTracks.getSchedTrackOnBefore(dtStart, SchedTrack.RECLEGRESETRATETRACK);
                if (schedTrack.found) {
                    lastRecResetRt = schedTrack.schedValue;

                } else {
                    if (schedTrack.found == false) {
                        String errmsg = "No schedule found for " + SchedTrack.RECLEGRESETRATETRACKNAME;
                        error.setError(getClass().getName(), errmsg);
                        return cashFlowList;
                    }
                }
            }
        }
        double totCashflow = 0.;
        double fedTaxDue = 0., stateTaxDue = 0.;
        double lastReinRt = 0.;
        // get starting reinvestment rate
        schedTrack = bond.schedTracks.getSchedTrackOnBefore(dtStart, SchedTrack.REINVESTRATETRACK);
        if (schedTrack.found && includeReinvestYn == 'Y')
            lastReinRt = schedTrack.schedValue;
        else {
            lastReinRt = 0.;
            error.clearError();
        }
        // get previous payment date for interest calc
        double cumReinVestCash = 0.;
        double stateTax = 0., fedTax = 0.;
        // inclusive
        int no = 0;
        int nextTrackNo = 0;
        int endTrackNo;
        if (dtEnd.geDate(bond.dtMaturity))
            endTrackNo = bond.schedTracks.schedTrackList.size() - 1;
        else
            endTrackNo = getEndTrackNo(bond.schedTracks.schedTrackList, dtEnd);
        Dates dtCashflow = schedTrack.schedDate;
        // include end date
        while (++nextTrackNo <= endTrackNo) {
            schedTrack = bond.schedTracks.schedTrackList.get(nextTrackNo);
            dtCashflow = schedTrack.schedDate;
            if (dtCashflow.leDate(dtStart))
                continue;
            int schedType = schedTrack.schedType;
            if (side.equals(Codes.PAYLEG)) {
                if (schedType == SchedTrack.RECLEGTRACK || schedType == SchedTrack.RECLEGRESETRATETRACK)
                    continue;
            }
            if (side.equals(Codes.RECLEG)) {
                if (schedType == SchedTrack.PAYLEGTRACK || schedType == SchedTrack.PAYLEGRESETRATETRACK)
                    continue;
            }
            dtCashflow = schedTrack.schedDate;
            cashFlow.zero();
            cashFlow.dtCashflow = dtCashflow;
            boolean include = true;
            switch (schedTrack.schedType) {
                case SchedTrack.PAYLEGTRACK:
                    if (bond.paylegFixedYn == 'Y') {
                        cashFlow.intCash = getInterstCashFixedRate(dtLastAccrue, dtCashflow, bond.cpnRtPayleg, bond.paylegSchedRule);
                    } else {
                        accInt += lastPayResetRt * bond.paylegResetSchedRule.getCouponFactor(dtLastAccrue, dtCashflow) / paylegResetRule.compoundFreq;
                        cashFlow.intCash = accInt;
                        accInt = 0;
                        dtLastAccrue = dtCashflow;
                    }
                    cashFlow.perLen = paylegResetRule.getCouponFactor(dtStart, dtCashflow);
                    if (bond.afterTaxCalcsYn == 'Y') {
                        stateTax = bond.taxes.calcStateIncTaxes(cashFlow.intCash);
                        stateTaxDue += stateTax;
                        fedTax = bond.taxes.calcFedIncTaxes(cashFlow.intCash);
                        fedTaxDue += fedTax;
                    }
                    break;
                case SchedTrack.RECLEGTRACK:
                    if (bond.reclegFixedYn == 'Y') {
                        cashFlow.intCash = getInterstCashFixedRate(dtLastAccrue, dtCashflow, bond.cpnRtRecleg, intReclegRule);
                        dtLastAccrue = dtCashflow;
                    } else {
                        accInt += lastRecResetRt * bond.reclegResetSchedRule.getCouponFactor(dtLastAccrue, dtCashflow) / bond.reclegResetSchedRule.compoundFreq;
                        cashFlow.intCash = accInt;
                        accInt = 0;
                        dtLastAccrue = dtCashflow;
                    }
                    cashFlow.perLen = intReclegRule.getCouponFactor(dtStart, dtCashflow);
                    if (bond.afterTaxCalcsYn == 'Y') {
                        stateTax = bond.taxes.calcStateIncTaxes(cashFlow.intCash);
                        stateTaxDue += stateTax;
                        fedTax = bond.taxes.calcFedIncTaxes(cashFlow.intCash);
                        fedTaxDue += fedTax;
                    }
                    break;
                case SchedTrack.PAYLEGRESETRATETRACK:
                    accInt += lastPayResetRt * paylegResetRule.getCouponFactor(dtLastAccrue, dtCashflow) / paylegResetRule.compoundFreq;
                    dtLastAccrue = dtCashflow;
                    lastPayResetRt = schedTrack.schedValue;
                    break;
                case SchedTrack.RECLEGRESETRATETRACK:
                    accInt += lastRecResetRt * reclegResetRule.getCouponFactor(dtLastAccrue, dtCashflow) / reclegResetRule.compoundFreq;
                    dtLastAccrue = dtCashflow;
                    lastRecResetRt = schedTrack.schedValue;
                    break;
                case SchedTrack.REINVESTRATETRACK:
                    lastReinRt = 0.;
                    if (includeReinvestYn == 'Y') {
                        lastReinRt = schedTrack.schedValue;
                    }
                    break;
                case SchedTrack.REDEEMTRACK:
                    cashFlow.schedPrinCash = getRedeemValue(bond.instype, bond.dtMaturity, bond.redeemPrin);
                    if (bond.afterTaxCalcsYn == 'Y') {
                        stateTax = bond.taxes.calcStateCapGLTaxes(cashFlow.schedPrinCash, bond.purPrc, bond.dtRedeem, bond.dtPur);
                        stateTaxDue += stateTax;
                        stateTax = bond.taxes.calcStateCapGLTaxes(cashFlow.schedPrinCash, bond.purPrc, bond.dtRedeem, bond.dtPur);
                        fedTax = bond.taxes.calcFedCapGLTaxes(cashFlow.schedPrinCash, bond.purPrc, bond.dtRedeem, bond.dtPur, stateTax);
                        fedTaxDue += fedTax;
                    }
                    break;
                case SchedTrack.FEDTAXPAYMENTTRACK:
                    cashFlow.fedTaxPaid = fedTaxDue;
                    cashFlow.stateTaxPaid = stateTaxDue;
                    fedTaxDue = stateTaxDue = 0.;
                    break;
                default:
                    include = false;
                    break;
            }
            cashFlow.totPrinCash = cashFlow.schedPrinCash;
            double reinvestCash = cashFlow.intCash + cashFlow.schedPrinCash;
            cashFlow.totCashflow = cashFlow.intCash + cashFlow.schedPrinCash - cashFlow.fedTaxPaid - cashFlow.stateTaxPaid;
            if (includeReinvestYn == 'Y' && reinvestCash != 0.) {
                double perCount = dayCount.simplePeriodCount(dtCashflow, dtEnd, CalUnits.SEMIANNUALREPEAT);
                stateTax = bond.taxes.calcStateCapGLTaxes(cashFlow.schedPrinCash, bond.purPrc, bond.dtRedeem, bond.dtPur);
                stateTaxDue += stateTax;
                stateTax = bond.taxes.calcStateCapGLTaxes(cashFlow.schedPrinCash, bond.purPrc, bond.dtRedeem, bond.dtPur);
                fedTax = bond.taxes.calcFedCapGLTaxes(cashFlow.schedPrinCash, bond.purPrc, bond.dtRedeem, bond.dtPur, stateTax);
                fedTaxDue += fedTax;
                cumReinVestCash += reinvestCash * Math.pow(1. + lastReinRt / CalUnits.SEMIANNUALFQ, perCount);
            }
            if (cashFlow.totCashflow != 0.) {
                cashFlow.dtCashflow = dtCashflow;
                cashFlow.years = dayCount.getYearsBetweenDates(dtStart, dtCashflow);
                cashFlow.strDayOfWeek = dayCount.time.getDayOfWeek(dtCashflow);
                cashFlow.totPrinCash = cashFlow.schedPrinCash;
                if (nextTrackNo == endTrackNo) {
                    cashFlow.intCash += cumReinVestCash;
                    cashFlow.totCashflow += cumReinVestCash;
                }
                cashFlowList.add(no++, new CashflowResults(cashFlow));
            }
        }
        return combineCashflows(cashFlowList);
    }


    public ArrayList<CashflowResults> getCashflowIRS(int calcNo, String side, Dates dtStart, Dates dtEnd, char includeReinvestYn) {
        ArrayList<CashflowResults> netCashflowList = new ArrayList<>();
        ArrayList<CashflowResults> payCashflowList = new ArrayList<>();
        ArrayList<CashflowResults> recCashflowList = new ArrayList<>();
        CashflowResults cashflow = new CashflowResults();

        if (side.toUpperCase().equals(Codes.PAYLEG)) {
            payCashflowList = getCashflowIRSSide(Instype.IRSSWAP, Codes.PAYLEG, dtStart, dtEnd, includeReinvestYn);
            return combineCashflows(payCashflowList);
        }
        if (side.toUpperCase().equals(Codes.RECLEG)) {
            recCashflowList = getCashflowIRSSide(Instype.IRSSWAP, Codes.RECLEG, dtStart, dtEnd, includeReinvestYn);
            return combineCashflows(recCashflowList);
        }
        if (side.equals(Codes.NETLEG)) {
            int count = 0;
            recCashflowList = getCashflowIRSSide(Instype.IRSSWAP, Codes.RECLEG, dtStart, dtEnd, includeReinvestYn);
            int no = -1;
            while (++no < recCashflowList.size()) {
                netCashflowList.add(count++, new CashflowResults(recCashflowList.get(no)));
            }
            payCashflowList = getCashflowIRSSide(Instype.IRSSWAP, Codes.PAYLEG, dtStart, dtEnd, includeReinvestYn);
            no = -1;
            while (++no < payCashflowList.size()) {
                cashflow = payCashflowList.get(no);
                cashflow.intCash = -cashflow.intCash;
                cashflow.schedPrinCash = -cashflow.schedPrinCash;
                cashflow.fedTaxPaid = -cashflow.fedTaxPaid;
                cashflow.stateTaxPaid = -cashflow.stateTaxPaid;
                cashflow.totCashflow = cashflow.intCash + cashflow.schedPrinCash;
                cashflow.totPrinCash = cashflow.schedPrinCash;
                netCashflowList.add(count++, new CashflowResults(cashflow));
            }
        }
        return combineCashflows(netCashflowList);

    }


    public String outputPayScheduleFile(int noCalc, int calcNo, int result) {
        String strPay;
        String strPayOut;
        if (bond.error.hasErrorYn == 'Y') {
            strPay = bond.error.getErrorMessage();
            bond.error.clearError();
            return strPay;
        }
        CalcResults.PaySchedule paySchedule;
        int flowNo;
        flowNo = -1;
        strPay = "";
        strPay = strPay + "PayAccrueSchedule=" + calcResults.paySchedCount + commandLine.fileCalcDelimit;
        String fmtAccrueDt = "";
        String fmtPayDt = "";
        String fmtDow = "";
        String fmtDays = "";
        String fmtHoliday = "";
        if (bond.fileOutFormat.equals("STD")) {
            strPay = strPay + "  Accrue Dt    Pay Dt      DayofWk    Days    Holiday\n";
            strPay = strPay + " ----------   ----------   --------   -----   -------\n";
            fmtAccrueDt = "%10s";
            fmtPayDt = "   %10s  ";
            fmtDow = "     %s ";
            fmtDays = " %6d  ";
            fmtHoliday = "     %c  ";
        } else {
            strPay = strPay + "Accrue Dt,Pay Dt,DayofWk,Days,Holiday\n";
            strPay = strPay + " ----------,----------,--------,----,-------\n";
            fmtAccrueDt = "%s,";
            fmtPayDt = "%s,";
            fmtDow = "%s,";
            fmtDays = "%d,";
            fmtHoliday = "%c";
        }
        int noFlows = calcResults.paySchedCount;
        while (++flowNo < noFlows) {
            paySchedule = calcResults.payScheduleList.get(flowNo);
            String sDate = paySchedule.dtAccrue.getDateString();
            strPayOut = String.format(fmtAccrueDt, sDate);
            sDate = paySchedule.dtPay.getDateString();
            strPayOut = strPayOut.concat(String.format(fmtPayDt, sDate));
            strPayOut = strPayOut.concat(String.format(fmtDow, paySchedule.dow));
            strPayOut = strPayOut.concat(String.format(fmtDays, paySchedule.days));
            strPayOut = strPayOut.concat(String.format(fmtHoliday, paySchedule.isHolidayYn));
            if (flowNo == noFlows - 1)
                strPay = strPay + strPayOut;
            else
                strPay = strPay + strPayOut + commandLine.fileCalcDelimit;
        }
        return strPay;
    }

    public String outputPayScheduleConsole(int noCalc, int calcNo, int result) {
        int flowNo = -1;
        int noFlows = calcResults.paySchedCount;
        String strItem = "";
        String strPay = "PaySchedule";
        if (bond.error.hasErrorYn == 'Y') {
            strPay = bond.error.getErrorMessage();
            bond.error.clearError();
            return strPay;
        }

        strPay = commandLine.consoleQuote + strPay + commandLine.consoleQuote;
        strPay = strPay + commandLine.consoleParamDelimit;
        //String strPayOut = "[";
        CalcResults.PaySchedule paySchedule;
        if (noCalc == 0)
            strPay = "{" + strPay;
        while (++flowNo < noFlows) {
            paySchedule = calcResults.payScheduleList.get(flowNo);
            strItem = commandLine.consoleLeftBracket + commandLine.consoleQuote + paySchedule.dtAccrue.getDateString() + commandLine.consoleQuote + ",";
            strPay = strPay + strItem;
            //strPay = strPay + "{" + "\"" + paySchedNames[0] + "\"" + ":" + "\"" + paySchedule.dtAccrue.getDateString() +" \"" + ",";
            strItem = commandLine.consoleQuote + paySchedule.dtPay.getDateString() + commandLine.consoleQuote + ",";
            strPay = strPay + strItem;
            strItem = commandLine.consoleQuote + paySchedule.dow + commandLine.consoleQuote + ",";
            strPay = strPay + strItem;
            strItem = paySchedule.days + ",";
            strPay += strItem;
            if (flowNo == noFlows - 1)
                strItem = commandLine.consoleQuote + paySchedule.isHolidayYn + commandLine.consoleQuote + commandLine.consoleRightBracket;
            else
                strItem = commandLine.consoleQuote + paySchedule.isHolidayYn + commandLine.consoleQuote + commandLine.consoleRightBracket + ",";
            strPay += strItem;
        }
        return strPay;
    }

    public int addCashflowList(int calcNo, int noFlow, int securityNo, Dates dtSettle, Dates dtCashflow, double cfInt, double cfSched, double cfPrePay, double fedTaxPay, double stateTaxPay) {

        int dowNum = 0;
        double cfTotPrin = cfSched + cfPrePay;
        double cfTotalCash = cfTotPrin + cfInt + fedTaxPay + stateTaxPay;
        double cfYears = dayCount.matLenYears(calcNo, dtSettle, dtCashflow, bond.dtMaturity);
        // double perLen = bond.schedTracks.getCouponFactor(calcNo, dtSettle, dtCashflow);
        double perLen = bond.intSchedRule.getCouponFactor(dtSettle, dtCashflow);
        String dow = dtCashflow.getDayOfWeek(dtCashflow);
        cashflowResults.matLen = dayCount.getMonthCount(dtSettle, dtCashflow, 'Y');
        cashflowResults.fedTaxPaid = fedTaxPay;
        cashflowResults.strDayOfWeek = dow;
        cashflowResults.sumTotCashflow += cfTotalCash;
        cashflowResults.years = cfYears;
        cashflowResults.dtCashflow = dtCashflow;
        cashflowResults.totCashflow = cfTotalCash;
        cashflowResults.totPrinCash = cfTotPrin;
        cashflowResults.schedPrinCash = cfSched;
        cashflowResults.prePaycash = cfPrePay;
        cashflowResults.perLen = perLen;
        cashflowResults.intCash = cfInt;
        cashflowResults.sumTotSchedCashflow += cfSched;
        cashflowResults.sumTotPrePayCashflow += cfPrePay;
        cashflowResults.sumTotPrinCashflow += cfTotPrin;
        cashflowResults.sumTotIntCashflow += cfInt;
        cashflowResults.sumTotCashflow += cfTotalCash;
        cashflowList.add(noFlow, new CashflowResults(cashflowResults));
        //  cashflowList.add(noFlow, new CashflowResults(cashflowResults));
        //empty the cumulative cashflowc
        return noFlow;
    }


    //calcNo, scale, bond.instype, dtCalc, yieldEst
    public double presentValue(double yield) {
        int flowNo = -1;
        double presValue = 0.;
        double perLen;
        double discFac;
        double cashScale = 1.;
        double cfTotal = 0.;
        double perYield = yield / bond.intSchedRule.compoundFreq;
        if (perYield < -1.) {
            return UnAvails.NOTAVAILDOUBLE;
        }
        while (++flowNo < cashflowList.size()) {
            cfTotal = cashflowList.get(flowNo).totCashflow;
            perLen = cashflowList.get(flowNo).perLen;
            perYield = yield / bond.intSchedRule.compoundFreq;
            discFac = 1. / Math.pow((1. + perYield), perLen);
            presValue += cfTotal * discFac;
        }
        return presValue;
    }

    public double presentValue(double yield, ArrayList<CashflowResults> cashflowList) {
        int flowNo = -1;
        double presValue = 0.;
        double perLen;
        double discFac;
        double cashScale = 1.;
        double cfTotal = 0.;
        double perYield = yield / bond.intSchedRule.compoundFreq;
        if (perYield < -1.) {
            return UnAvails.NOTAVAILDOUBLE;
        }
        while (++flowNo < cashflowList.size()) {
            cfTotal = cashflowList.get(flowNo).totCashflow;
            perLen = cashflowList.get(flowNo).perLen;
            perYield = yield / bond.intSchedRule.compoundFreq;
            discFac = 1. / Math.pow((1. + perYield), perLen);
            presValue += cfTotal * discFac;
        }
        return presValue;
    }


    public String outputKeyRateConsole(int noCalc, int calcNo) {
        String strKr = "";
        double krVal = 0.;

        if (bond.error.hasErrorYn == 'Y') {
            strKr = bond.error.getErrorMessage();
            return strKr;
        }
        CommandLine cl = commandLine;
        String strLine = "";
        strLine = cl.consoleQuote + calcDef.calcDefs.get(CalcList.KEYRATEDURATION).calcName + cl.consoleQuote + cl.consoleParamDelimit + cl.consoleLeftBracket;
        if ('Y' == bond.error.hasErrorYn) {
            String errmsg = bond.error.getErrorMessage();
            fileFormatter.format("%s\n", errmsg);
            return strKr;
        }
        String strKeyRate = "Rate";
        String strTenor = "Tenor";
        String strItem = "";
        char roundYn = 'Y';
        int kr = -1;
        while (++kr < keyRateDuration.noKeyRts - 1) {
            strItem = cl.consoleObjectStartDelimit + cl.consoleQuote + strTenor + cl.consoleQuote + cl.consoleParamDelimit + cl.consoleQuote + keyRateDuration.keyRateNames[kr] + cl.consoleQuote + cl.consoleElementDelimit;
            strLine += strItem;
            krVal = keyRateDuration.keyRateDurations[kr];
            krVal = Utility.truncRound(krVal, bond.sfigStatistic, roundYn);
            strItem = cl.consoleQuote + strKeyRate + cl.consoleQuote + cl.consoleParamDelimit + krVal + cl.consoleObjectEndDelimit + cl.consoleElementDelimit;
            strLine += strItem;
        }
        strItem = cl.consoleObjectStartDelimit + cl.consoleQuote + strTenor + cl.consoleQuote + cl.consoleParamDelimit + cl.consoleQuote + keyRateDuration.keyRateNames[kr] + cl.consoleQuote + cl.consoleCalcDelimit;
        strLine += strItem;
        krVal = Utility.truncRound(krVal, bond.sfigStatistic, roundYn);
        strItem = cl.consoleQuote + strKeyRate + cl.consoleQuote + cl.consoleParamDelimit + krVal + cl.consoleObjectEndDelimit + cl.consoleRightBracket;
        strLine += strItem;
        return strLine;
    }


    public String outputKeyRateFile(int noCalc, int calcNo) {
        String strKr = calcDef.calcDefs.get(CalcList.KEYRATEDURATION).calcName + "=" + keyRateDuration.noKeyRts + "\n";
        int noKeyRts = keyRateDuration.noKeyRts;
        double srVal = 0.;
        int kr = -1;
        if (bond.error.hasErrorYn == 'Y') {
            strKr = bond.error.errorMessage;
            return strKr;
        }
        if (bond.fileOutFormat.equals("STD")) {
            strKr = strKr + "Key Rate" + "\n";
            strKr = strKr + "Duration    Term" + "\n";
            strKr = strKr + "---------   ------" + "\n";
        } else {
            strKr = strKr + "Key Rate" + "\n";
            strKr = strKr + "Duration,Term" + "\n";
            strKr = strKr + "---------,------" + "\n";
        }
        char roundYn = 'Y';

        String strVal = "";
        while (++kr < noKeyRts - 1) {
            srVal = keyRateDuration.keyRateDurations[kr];
            String krName = keyRateDuration.keyRateNames[kr];
            strVal = String.format("%9.4f    ", srVal) + krName;
            strKr += strVal + "\n";
        }
        srVal = keyRateDuration.keyRateDurations[kr];
        String krName = keyRateDuration.keyRateNames[kr];
        strVal = String.format("%9.4f    ", srVal) + krName;
        strKr += strVal;
        return strKr;
    }


    public CalcMethod.RootResult presentValueFxDx(int calcNo, double scale, Dates dtSettle, double yield) {
        int flowNo = -1;
        double presValue = 0.;
        double perLen;
        double discFac;
        double cashScale = 1.;
        double cfTotal = 0.;
        double dxPresValue = 0.;
        CalcMethod.RootResult rootResult = new CalcMethod.RootResult();

        while (++flowNo < cashflowResults.count) {
            cfTotal = cashflowList.get(flowNo).totCashflow;
            perLen = cashflowList.get(flowNo).perLen;
            double perYield = yield / bond.intSchedRule.compoundFreq;
            discFac = 1. / Math.pow((1. + perYield), perLen);
            presValue += cfTotal * discFac;
            dxPresValue += -cfTotal * perLen * Math.pow(1. / (1. + perYield), perLen + 1.);
        }
        rootResult.func = presValue;
        rootResult.dFunc = dxPresValue;
        return rootResult;
    }

    // dtRedeem cab be call dates or put dates
// redeemPrin can be call price or put price
// redeemInt is the orginal redemptio values on which interest depends
    public double yieldPrice(int calcNo, int securityNo, String instype, Dates dtSettle, double price, Dates dtRedeem, double redeemPrin, double redeemInt) {
        double yield = 0.;

        switch (instype) {
            case Instype.DISCOUNT:
                yield = discount.yieldPrice(calcNo, dtSettle, price, dtRedeem, redeemPrin, redeemInt);
                break;
            case Instype.ATMATURITY:
            case Instype.CURRENCY:
                yield = atMaturity.yieldPrice(calcNo, dtSettle, price);
                break;
            case Instype.IRSSWAP:
                break;
            default:
                yield = yieldPricePeriodic(calcNo, securityNo, instype, dtSettle, price, dtRedeem, redeemPrin, redeemInt);
                break;
        }// both dependent princupal and original principla  for interest calc
        if (UnAvails.isUnAvail(bond.settleYld))
            bond.settleYld = yield;
        return yield;
    }

    public double realYield(int calcNo, int securityNo, String instype, Dates dtSettle, double price, Dates dtRedeem, double redeemPrin, double redeemInt) {
        double yield = 0.;

        if (instype.equals(Instype.DISCOUNT))
            yield = discount.yieldPrice(calcNo, dtSettle, price, dtRedeem, redeemPrin, redeemInt); // no interest here
        else if (instype.equals(Instype.ATMATURITY) || instype.equals(Instype.CURRENCY))  // no call for at maturuty
            yield = atMaturity.yieldPrice(calcNo, dtSettle, price);
        else {
            double futValue = futureValue(calcNo, securityNo, instype, dtSettle, dtRedeem, redeemPrin, redeemInt);
            futValue /= bond.faceAmt;
            double nPer = bond.intSchedRule.getCouponFactor(dtSettle, dtRedeem);
            yield = bond.intSchedRule.compoundFreq * (-1. + Math.pow(futValue / bond.settlePrc, 1. / nPer));
            // both dependent princupal and original principla  for interest calc
        }
        return yield * calcDef.calcDefs.get(calcNo).scale;
    }


    public double maturityCash(int calcNo, String instype, Dates dtRedeem, double redeemPrin, double redeemInt) {
        double matCash = 0.;
        SchedTrack schedTrack = new SchedTrack();
        schedTrack.schedDate = dtRedeem;
        schedTrack.schedType = SchedTrack.INTERESTPAYTRACK;
        double rrv = getRedeemValue(instype, dtRedeem, redeemPrin);
        double intCash = getInterstCash(calcNo, instype, dtRedeem, redeemInt, schedTrack);
        matCash = bond.faceAmt * (intCash + rrv);
        return matCash;

    }


    public double yieldPricePeriodic(int calcNo, int securityNo, String instype, Dates dtSettle, double price, Dates dtRedeem, double redeemPrin, double redeemInt) {
        double yieldEst, dYield, netPay;
        if (instype.equals(Instype.IRSSWAP)) {
            yieldEst = 0.;
            netPay = price;
        } else {
            double noYears;
            int days;
            noYears = dayCount.getYearsBetweenDates(dtSettle, dtRedeem);
            double totIncome = bond.cpnRt * noYears + (redeemInt - price);
            double accIntUnit = 0.;
            if (bond.cleanPrcYn == 'Y')
                accIntUnit = accruedInterest(calcNo, instype, dtSettle) / bond.faceAmt;
            else
                accIntUnit = 0.;
            if ('Y' == bond.error.hasErrorYn)
                return 0.;
            if (dtRedeem.leDate(bond.dtSettle)) {
                String errmsg = "Invalid settle date or redemption date because settle date " + bond.dtSettle.getDateString() + " is after maturity date " + bond.dtMaturity.getDateString();
                bond.error.setError(getClass().getName(), errmsg);
                return UnAvails.NOTAVAILDOUBLE;
            }
            double rrvAi = getRedeemValue(instype, dtSettle, redeemInt);
            //double noPer = bond.intSchedRule.getCouponFactor( bond.dtSettle, dtRedeem);
            double noPer = bond.intSchedRule.getCouponFactor(dtSettle, dtRedeem);
            netPay = accIntUnit + price * rrvAi;
            double rrvPrin = getRedeemValue(instype, dtSettle, redeemPrin);
            if (noPer <= 1.) {
                if (bond.yieldQuoteType.equals(CalcEnums.TREASURYYIELD)) {
                    double daysYear = dayCount.getDaysInYear(bond.dtSettle);
                    double daysMature = dayCount.getDaycount(dtSettle, dtRedeem, dtRedeem, bond.dcYldPrc);
                    double matCash = cashflowList.get(0).totCashflow;
                    dYield = (daysYear / daysMature) * (-1. + matCash / netPay);
                    if (dYield <= 0.) {
                        String errmsg = "A negative yield was calculated from a specified price of " + price;
                        bond.error.setWarning(getClass().getName(), errmsg);
                    }
                    return dYield * calcDef.calcDefs.get(calcNo).scale;
                }
            }
            double cpnRt = bond.cpnRt;
            if (instype.equals("FLOATER") && UnAvails.isUnAvail(cpnRt))
                cpnRt = bond.resetAvgRt;
            double futValue = rrvPrin + cpnRt * noPer / bond.intSchedRule.compoundFreq;
            double freq = bond.intSchedRule.compoundFreq;
            // yieldEstStart = bond.cpnRt;
            yieldEst = freq * (-1. + Math.pow(futValue / netPay, 1. / noPer));
        }
        //
        dYield = getYieldRootNewton(calcNo, netPay, securityNo, instype, dtSettle, dtRedeem, redeemPrin, redeemInt, yieldEst);
        if (bond.error.hasErrorYn == 'Y')
            return 0.;
        if (dYield <= 0) {
            String errmsg = "A negative yield was calculated from a specified price of " + price;
            bond.error.setWarning(getClass().getName(), errmsg);
        }
        if (UnAvails.isAvail(bond.bondEqvYld))
            dYield = convertBondEquivalent(dYield);
        if (UnAvails.isUnAvail(bond.settleYld))
            bond.settleYld = dYield;
        return dYield;
    }

    public double convertBondEquivalent(double yield) {
        double cYield = yield;
        double annualYld = 1.;
        int cFreq = bond.intSchedRule.compoundFreq;
        int beFreq = bond.bondEqvYldOpt;
        if (cFreq != bond.bondEqvYldOpt) {
            annualYld = Math.pow(1. + yield / cFreq, cFreq);
            cYield = bond.bondEqvYldOpt * (-1 + Math.pow(annualYld, 1. / beFreq));
        }
        return cYield;
    }

    public double getYieldRootNewton(int calcNo, double netPay, int securityNo, String instype, Dates dtCalc, Dates dtRedeem, double redeemPrin, double redeemInt, double startYieldEst) {
        CalcMethod.RootResult rootResult = new CalcMethod.RootResult();
        int it = 0;
        String strYieldEst = "";
        double func = 0, dFunc = 0, deltaFunc = 0.;

        if (instype.equals(Instype.MORTGAGE))
            cashflowList = mortgage.cashflow(calcNo, 1., securityNo, dtCalc, dtRedeem, redeemPrin, redeemInt);
        else
            cashflowList = cashflow(calcNo, 1., securityNo, instype, dtCalc, dtRedeem);
        if (bond.error.hasErrorYn == 'Y')
            return 0.;
        double lastYieldEst = startYieldEst;
        for (it = 0; it < CalcEnums.MAXROOTITERATIONS; ++it) {
            rootResult = trialYieldRootNewton(calcNo, netPay, instype, dtCalc, dtRedeem, redeemPrin, redeemInt, lastYieldEst);
            func = rootResult.func;
            dFunc = rootResult.dFunc;
            if (dFunc == 0. || UnAvails.isUnAvail(func)) {
                String errmsg = "Cannot determine yield because the derivative of the price/yield is zero";
                bond.error.setWarning(getClass().getName(), errmsg);
                return UnAvails.NOTAVAILDOUBLE / 100.;
            }
            if (lastYieldEst > Limits.MAXYIELD || lastYieldEst < Limits.MINYIELD) {
                strYieldEst = String.valueOf(lastYieldEst);
                bond.error.setError(getClass().getName(), "Yield estimate found (" + strYieldEst + ") is out of reasonable bounds");
                return lastYieldEst;
            }
            if (Math.abs(func) <= CalcEnums.ROOTPRECISION)
                break;
            deltaFunc = func / dFunc;
            lastYieldEst -= deltaFunc;
            boolean stop = false;
        }
        if (it > CalcEnums.MAXROOTITERATIONS) {
            strYieldEst = String.valueOf(lastYieldEst);
            bond.error.setWarning(getClass().getName(), "Yield found (" + strYieldEst + ") is best yield within maximun alloed trials");
            return UnAvails.NOTAVAILDOUBLE / 100.;
        }
        return lastYieldEst;
    }

    public CalcMethod.RootResult trialYieldRootNewton(int calcNo, double netPay, String instype, Dates dtCalc, Dates dtRedeem, double redeemPrin, double redeemInt, double lastYieldEst) {
        CalcMethod.RootResult rootResult = new CalcMethod.RootResult();
        double scale = 1.;
        // cashflow previously called
        double presValue1 = presentValue(lastYieldEst);
        if (UnAvails.isUnAvail(presValue1)) {
            rootResult.func = UnAvails.NOTAVAILDOUBLE;
            rootResult.dFunc = UnAvails.NOTAVAILDOUBLE;
            return rootResult;
        }
        double presValue2 = presentValue(lastYieldEst + CalcEnums.ROOTYIELDDERIVATIVEDELTA);
        rootResult.func = netPay - presValue1;
        rootResult.dFunc = (presValue1 - presValue2) / CalcEnums.ROOTYIELDDERIVATIVEDELTA;
        return rootResult;
    }

    /*
        public double getYieldRootBracket(int calcNo, double netPay, Dates dtCalc, double yieldEst) {
            /*RootResult rootResult = new RootResult();
            int it = 0;
            String strYieldEst ="";
            double func = 0, dFunc = 0, deltaFunc = 0.;
            int noFlows = cashflow();
            for (it = 0; it < CalcEnums.MAXROOTITERATIONS; ++it) {
                rootResult = trialYieldRootBrackett(calcNo, dtCalc, netPay, yieldEst);
                func = rootResult.func;
                if (func > 0.) {// net payment is too high  inrease yield
                    // new yield must be > current yield
                    // new yield must be lower than current min
                    // new yield must be between current max and current est
                    yieldEst  = .5 * (currentMax - yieldEst);
                if (func < 0.) { // yield est is to low
                    //
                    yieldEst = .5 (currentMax - yieldEst)

                }

                // if current est


                dFunc = rootResult.dFunc;
                if (dFunc == 0.) {
                    String errmsg = "Cannot determine yield because the derivative of the price/yield is zero";
                    bond.error.setError(getClass().getName(), errmsg);
                    return yieldEst;
                }
                if (yieldEst  > Limits.MAXYIELD || yieldEst < Limits.MINYIELD) {
                    strYieldEst = String.valueOf(yieldEst);
                    bond.error.setError(getClass().getName(), "Yield found (" + strYieldEst + ") is out of reasonable bounds");
                    return yieldEst;
                }
                if (Math.abs(func) <= CalcEnums.ROOTPRECISION)
                    break;
                deltaFunc = func / dFunc;
                yieldEst -= deltaFunc;
            }
            if (it > CalcEnums.MAXROOTITERATIONS) {
                strYieldEst = String.valueOf(yieldEst);
                bond.error.setError(getClass().getName(), "Yield found (" + strYieldEst + ") is best yield within maximun alloed trials");
                return yieldEst;
            }
            return yieldEst;

          public futureValue(
     */


    public String outputAccreAmortSheduleJSON(int noCalc, int calcNo, int result) {
        int flowNo = -1;
        String strFlow = "", strVal, format, sFormat = "%,15.2f";
        String strItem;
        Dates dtStart = new Dates();
        Dates dtEnd = new Dates();
        if (UnAvails.isUnAvail(bond.sfigAccrual)) {
            format = "%.2f";
            sFormat = "%17.2f   ";
        } else {
            format = "%." + bond.sfigAccrual + "f";
            sFormat = "%17." + bond.sfigAccrual + "f   ";
        }
        if (noCalc == 0)
            strFlow = "{";
        strFlow += "\"" + calcDef.calcDefs.get(CalcList.ACCREAMORTSCHED).calcName + "\"" + ":[";
        while (++flowNo < calcResults.accreAmortResults.count) {
            strItem = setItemName(calcResults.accreAmortSchedNames[0], calcResults.accreAmortScheduleList.get(flowNo).dtStart, ",");
            strFlow = strFlow + "{" + strItem;

            //strFlow = strFlow + "{" + "\"" + accreAmortSchedNames[0] + "\"" + ":" + accreAmortScheduleList.get(flowNo).dtStart.getDateString() + "'";
            strItem = setItemName(calcResults.accreAmortSchedNames[1], calcResults.accreAmortScheduleList.get(flowNo).dtEnd, ",");
            strFlow = strFlow + strItem;

            strItem = setItemName(calcResults.accreAmortSchedNames[2], calcResults.accreAmortScheduleList.get(flowNo).bkStart, ",", format);
            strFlow = strFlow + strItem;

            strItem = setItemName(calcResults.accreAmortSchedNames[3], calcResults.accreAmortScheduleList.get(flowNo).bkEnd, ",", format);
            strFlow = strFlow + strItem;

            strItem = setItemName(calcResults.accreAmortSchedNames[4], calcResults.accreAmortScheduleList.get(flowNo).accreAmort, ",", format);
            strFlow = strFlow + strItem;

            strItem = setItemName(calcResults.accreAmortSchedNames[5], calcResults.accreAmortScheduleList.get(flowNo).dailyAccreAmort, ",", format);
            strFlow = strFlow + strItem;

            if (flowNo == calcResults.accreAmortResults.count - 1)
                strItem = setItemName(calcResults.accreAmortSchedNames[6], calcResults.accreAmortScheduleList.get(flowNo).days, "}]", "%d");

            else
                strItem = setItemName(calcResults.accreAmortSchedNames[6], calcResults.accreAmortScheduleList.get(flowNo).days, "},", "%d");
            strFlow = strFlow + strItem;
        }
        if (bond.fileOutYn == 'Y')
            printAccreAmortSchedule(sFormat);
        return strFlow.trim();
    }


    public double futureValue(int calcNo, int securityNo, String instype, Dates dtFuture, Dates dtRedeem, double redeemPrin, double redeemInt) {
        Dates dtCashflow = new Dates();
        double matLen, useYield = 0.;
        double totCash = 0., perCash = 0., remPer = 0.;
        int freq = CalUnits.SEMIANNUALFQ;
        YieldCurves yieldCurves = bond.discFutCurve;
        double noPer = 0;
        switch (instype) {
            case "DISCOUNT":
                totCash = discount.futureValue(calcNo, securityNo, instype, bond.faceAmt, dtFuture, dtRedeem, redeemPrin);
                break;
            case "ATMATURITY":
                totCash = atMaturity.futureValue(calcNo, securityNo, instype, bond.faceAmt, dtFuture, dtRedeem, redeemPrin);
                break;
            default:
                double pvFactor = 0.;
                cashflowList = cashflow(calcNo, bond.faceAmt, securityNo, bond.instype, dtFuture, dtRedeem);
                int noFlows = cashflowList.size();
                int ic = -1;
                while (++ic < noFlows) {
                    dtCashflow = cashflowList.get(ic).dtCashflow;
                    perCash = cashflowList.get(ic).totCashflow;
                    remPer = bond.schedTracks.getCouponFactor(calcNo, dtFuture, dtCashflow);
                    matLen = CalUnits.MONTHSPERYEAR * bond.time.getYearsBetweenDates(dtFuture, dtCashflow);
                    if (bond.yieldForeCastMeth == CalcList.FORECASTFORWARD)
                        useYield = yieldCurves.getForwardRate(matLen, bond.yieldCurveInterpMeth);
                    else
                        useYield = yieldCurves.getSpotRate(matLen, bond.yieldCurveInterpMeth);
                    pvFactor = 0.;
                    if (useYield != -1)
                        pvFactor = 1. / Math.pow(1. + useYield / bond.intSchedRule.compoundFreq, remPer);
                    totCash += perCash * pvFactor;
                }
                if (dtFuture.gtDate(dtRedeem)) {
                    matLen = dayCount.getMonthCount(dtRedeem, dtFuture, 'Y');
                    freq = CalUnits.SEMIANNUALFQ;
                    noPer = matLen / freq; // no semi-annual period
                    if (bond.yieldForeCastMeth == CalcList.FORECASTFORWARD)
                        if (bond.yieldForeCastMeth == CalcList.FORECASTFORWARD)
                            useYield = yieldCurves.getForwardRate(matLen, bond.yieldCurveInterpMeth);
                        else
                            useYield = yieldCurves.getSpotRate(matLen, bond.yieldCurveInterpMeth);
                    pvFactor = 0.;
                    if (useYield != -1)
                        pvFactor = 1. / Math.pow(1. + useYield / freq, noPer);
                    double reinCash = totCash * Math.pow(1. + useYield / freq, noPer);
                    totCash += reinCash;
                }
                break;
        }
        accInt = accruedInterest(CalcList.ACCUREDINT, instype, dtFuture);
        return totCash + accInt;

    }


    public FutureValueResults futureValueRisk(int calcNo, int securityNo, String instype, Dates dtFuture, Dates dtRedeem, double redeemPrin, double redeemInt) {
        FutureValueResults futureValueResults = new FutureValueResults();
        Dates dtCashflow = new Dates();
        double useYieldExp = 0., useYieldDn = 0., useYieldUp = 0.;
        double totCashExp = 0.;
        double totCashDn = 0.;
        double totCashUp = 0.;
        double perCash = 0., remPer = 0.;
        double discFac = 1.;
        // future value risk calculates 3 future values, one using the specified curve and another
        // using the upSide and doenSide curve on a date

        switch (instype) {
            case "DISCOUNT":
                futureValueResults = discount.futureValueRisk(calcNo, securityNo, instype, dtFuture, dtRedeem, redeemPrin);
                break;
            case "ATMATURITY":
                futureValueResults = atMaturity.futureValueRisk(calcNo, securityNo, instype, dtFuture, dtRedeem, redeemPrin);
                break;
            case "FIXEDRATE":
            case "FLOATER":
            case "INDEXED":
            case "MORTGAGE":
            default:
                // ask the future values to adjust the spot rate
                cashflowList = cashflow(calcNo, bond.faceAmt, securityNo, bond.instype, dtFuture, dtRedeem);
                int ic = -1;
                int matLen = 0;
                double noPer = 0.;
                char isDiscYn = 'Y';
                YieldCurves yieldCurves = bond.discFutCurve;
                if (bond.discFutCurve.spotCurve.isEmpty()) {
                    yieldCurves = bond.discCurve;
                }
                int freq = bond.intSchedRule.compoundFreq;
                double mktValue = 0.;
                if (dtFuture.gtDate(dtRedeem)) {
                    double matCash = maturityCash(calcNo, instype, dtRedeem, redeemPrin, redeemInt);
                    double accInt = accruedInterest(CalcList.ACCUREDINT, instype, dtFuture);
                    futureValueResults.mktValue = matCash;
                    matLen = dayCount.getMonthCount(dtRedeem, dtFuture, 'Y');
                    futureValueResults.mktValue = matCash;
                    useYieldExp = yieldCurves.getSpotRate(matLen, bond.yieldCurveInterpMeth);
                    noPer = dayCount.getYearsBetweenDates(dtRedeem, dtFuture) * freq;
                    discFac = Math.pow(1. + useYieldExp / freq, noPer);
                    futureValueResults.futVal = matCash * discFac + accInt;
                    useYieldExp = yieldCurves.getSpotRate(matLen, CalcList.SPOTDN);
                    discFac = Math.pow(1. + useYieldExp / freq, noPer);
                    futureValueResults.futValUp = discFac * matCash + accInt;
                    useYieldExp = yieldCurves.getSpotRate(matLen, CalcList.SPOTUP);
                    discFac = Math.pow(1. + useYieldExp / freq, noPer);
                    futureValueResults.futValDn = discFac * matCash + accInt;
                    return futureValueResults;
                }
                int noFlows = cashflowList.size();
                while (++ic < noFlows) {
                    dtCashflow = cashflowList.get(ic).dtCashflow;
                    perCash = cashflowList.get(ic).totCashflow;
                    remPer = bond.schedTracks.getCouponFactor(calcNo, dtFuture, dtCashflow);
                    matLen = bond.dayCount.getMonthCount(dtFuture, dtCashflow, 'Y');
                    if (bond.yieldForeCastMeth.equals(CalcList.FORECASTFORWARD)) {
                        useYieldExp = bond.discCurve.getForwardRate(matLen, CalcList.SPOTON);
                        useYieldDn = bond.discCurve.getForwardRate(matLen, CalcList.SPOTDN);
                        useYieldUp = bond.discCurve.getForwardRate(matLen, CalcList.SPOTUP);
                    } else {
                        useYieldExp = bond.discCurve.getSpotRate(matLen, CalcList.SPOTON);
                        useYieldDn = bond.discCurve.getSpotRate(matLen, CalcList.SPOTDN);
                        useYieldUp = bond.discCurve.getSpotRate(matLen, CalcList.SPOTUP);
                    }
                    if (dtFuture.ltDate(dtFuture, bond.dtSettle)) {
                        totCashExp = 0.;
                        totCashUp = totCashExp;
                        totCashDn = totCashExp;
                    }
                    try {
                        totCashExp += perCash / Math.pow(1. + useYieldExp / bond.intSchedRule.compoundFreq, remPer);
                        totCashUp += perCash / Math.pow(1. + useYieldDn / bond.intSchedRule.compoundFreq, remPer);
                        totCashDn += perCash / Math.pow(1. + useYieldUp / bond.intSchedRule.compoundFreq, remPer);
                    } catch (Exception ex) {
                        error.setError(getClass().getName(), ex.getMessage());
                        return futureValueResults;

                    }

                }
                if (calcNo == CalcList.FUTRETURNRISK) {
                    double perLen = bond.intSchedRule.getCouponFactor(bond.dtSettle, dtFuture);
                    double mktValueSettle = bond.settlePrc + accruedInterest(calcNo, instype, bond.dtSettle) / bond.faceAmt;
                    futureValueResults.futVal = freq * (-1 + Math.pow(totCashExp / mktValueSettle, 1. / perLen));
                    futureValueResults.futValDn = freq * (-1 + Math.pow(totCashDn / mktValueSettle, 1. / perLen));
                    futureValueResults.futValUp = freq * (-1 + Math.pow(totCashUp / mktValueSettle, 1. / perLen));
                    return futureValueResults;
                }
                futureValueResults.mktValue = totCashExp;
                futureValueResults.futVal = totCashExp;
                futureValueResults.futValUp = totCashUp;
                futureValueResults.futValDn = totCashDn;
        }

        return futureValueResults;
    }


    // horizonAnalysis(calcNo, securityNo, instype, bond.dtRedeem, bond.redeemPrin, bond.redeemInt);
    public ArrayList<HorizonResults> horizonAnalysis(int calcNo, int securityNo, String instype, Dates dtRedeem, double redeemPrin, double redeemInt) {
        int ih = -1;
        ArrayList<HorizonResults> secHorzList = new ArrayList<>();
        HorizonResults horzResults = new HorizonResults();
        SchedRule schedRule = new SchedRule(bond.holidays, bond.error, bond.dayCount, bond.time);
        schedRule.dtOccur = bond.dtSettle;
        schedRule.repeatFactor = bond.horizonRepeatFactor;
        schedRule.timeUnit = bond.horizonTimeUnit;
        Dates dtLastHorz = new Dates();
        dtLastHorz.setDate(bond.dtSettle);
        while (++ih < bond.horizonPeriods) {
            Dates dtHorz = schedRule.getNextSchedDate(SchedTracks.AFTERDATE, dtLastHorz);
            horzResults.dtHorizon = dtHorz;
            if (instype.equals(Instype.CURRENCY)) {
                horzResults = new HorizonResults(dtHorz, bond.faceAmt);
                horizonResults.add(ih, new HorizonResults(horzResults));
                return horizonResults;
            }
            // horizonfuture value goes from dtCalc to maturity
            double currPrc = bond.settlePrc;
            accInt = accruedInterest(CalcList.ACCUREDINT, instype, bond.dtSettle);
            double currPrinValue = bond.settlePrc * bond.faceAmt;
            double currValue = currPrinValue + accInt;
            double horzFutMktValue = futureValue(calcNo, securityNo, instype, dtHorz, dtRedeem, redeemPrin, redeemInt);
            accInt = accruedInterest(CalcList.ACCUREDINT, instype, dtHorz);
            double futMktPrin = horzFutMktValue - accInt;
            double noPer = bond.intSchedRule.getCouponFactor(bond.dtSettle, dtHorz);
            horzResults.horzReturn = 100. * bond.intSchedRule.compoundFreq * (-1. + Math.pow(horzFutMktValue / currValue, 1. / noPer));
            double horzGainLoss = 0.;
            double horzPrc = futMktPrin / bond.faceAmt;
            if (UnAvails.isAvail(bond.purPrc)) {
                horzGainLoss = bond.faceAmt * (horzPrc - bond.purPrc);
            }
            horzPrc *= 100.;
            horzResults.horzPrc = horzPrc;
            horzResults.horzMatLen = dayCount.getYearsBetweenDates(dtHorz, bond.dtLastPayment);
            double horzYield = yieldPrice(CalcList.YIELDTOMATURITY, securityNo, instype, dtHorz, horzPrc, bond.dtRedeem, bond.redeemPrin, bond.redeemInt);
            horzResults.horzYield = horzYield;
            horzResults.horzDur = modDurationSTD(calcNo, securityNo, instype, dtHorz, horzYield, dtRedeem, redeemInt, redeemPrin);
            horzResults.horzGainLoss = horzGainLoss;
            secHorzList.add(ih, new HorizonResults(horzResults));
            // secHorzList.add(noHorz, new HorizonResults(horzResults));
            dtLastHorz.setDate(dtHorz);
        }
        return secHorzList;
    }

    public double yieldWorst(int calcNo, int securityNo, String instype, Dates dtSettle, double settlePrc, Dates dtRedeem, double redeemPrin, double redeemInt) {
        double ytw, ytc;
        int cno = -1;
        int noCalls = bond.noCallDts;
        ytw = 9999999999.99;
        /*
        if (instype.equals(Instype.CURRENCY))
            return 0.;

         */
        ytw = yieldPrice(calcNo, securityNo, instype, dtSettle, settlePrc, dtRedeem, bond.redeemPrin, redeemInt);
        calcResults.dtWorst = bond.dtMaturity;
        calcResults.yieldToWorst = ytw;
        calcResults.callPrcWorst = redeemPrin;
        calcResults.dtWorst = dtRedeem;
        while (++cno < noCalls) {
            ytc = yieldPrice(calcNo, securityNo, instype, dtSettle, settlePrc, bond.dtCall[cno], bond.callPrc[cno], redeemInt);
            if (ytc < ytw) {
                ytw = ytc;
                calcResults.dtWorst = bond.dtCall[cno];
            }
        }
        return calcResults.yieldToWorst = ytw;
    }

    public boolean updateYieldWorst(int calcNo, int securityNo, String instype, Dates dtSettle, double settlePrc, Dates dtRedeem, double redeemPrin, double redeemInt) {
        double ytc = yieldPrice(calcNo, securityNo, instype, dtSettle, settlePrc, dtRedeem, redeemPrin, redeemInt);
        if (bond.error.hasErrorYn == 'N') {
            if (ytc < calcResults.yieldToWorst) {
                calcResults.dtWorst = dtRedeem;
                calcResults.yieldToWorst = ytc;
                calcResults.callPrcWorst = redeemPrin;
            }
        } else {
            return false;
        }
        return true;
    }

    public double calcMarketValue(int calcNo, int securityNo, String instype, Dates dtStart, Dates dtRedeem, double redeemInt, double redeemPrin) {
        double estPrice = 0., estMarketValue = 0.;
        double spotRt = 0.;
        if (dtStart.geDate(bond.dtMaturity)) {
            calcResults.estimatedPrice = 0.;
            calcResults.estimatedMarkerValue = 0.;
            return 0.;
        }
        double futValue = futureValue(calcNo, securityNo, instype, dtStart, dtRedeem, redeemInt, redeemPrin);
        double matLen = CalUnits.MONTHSPERYEAR * bond.time.getYearsBetweenDates(bond.dtStart, dtRedeem);
        spotRt = bond.discCurve.getSpotRate(matLen, bond.yieldCurveInterpMeth);
        double discFactor = (1. + spotRt / bond.intSchedRule.compoundFreq);
        double noPer = bond.intSchedRule.getCouponFactor(dtStart, bond.dtLastPayment);
        discFactor = Math.pow(discFactor, noPer);
        if (discFactor == 0.)
            estPrice = 0.;
        else
            estPrice = futValue / discFactor;
        calcResults.estimatedPrice = estPrice * calcDef.calcDefs.get(CalcList.PRICETOMATURIY).scale;
        estMarketValue = estPrice + accruedInterest(calcNo, bond.instype, bond.dtStart);
        return estMarketValue;
    }

    public double macDuration(int calcNo, int securityNo, String instype, Dates dtSettle, double yield, Dates dtRedeem, double redeemInt, double redeemPrin) {
        int fno = -1;
        double pvNum = 0., pvDen = 0., macDur = 0.;
        double perLen;
        DayCount.DCMethod dcMethod;
        if (choices.inDiscountList(instype))
            return 0.;
        perLen = bond.intSchedRule.periodPerYear();
        if (instype.equals(Instype.DISCOUNT))
            return 0.;
        double discFac = 1. / (1. + yield / perLen);
        double cashScale = 1.;
        cashflowList = cashflow(calcNo, cashScale, securityNo, instype, dtSettle, dtRedeem);
        int noFlows = cashflowList.size();
        while (++fno < noFlows) {
            double cashflow = cashflowList.get(fno).totCashflow;
            Dates dtCash = new Dates();
            dtCash.setDate(cashflowList.get(fno).dtCashflow);
            // double perCount = bond.schedTracks.getCouponFactor(calcNo, dtSettle, dtCash);
            double perCount = bond.intSchedRule.getCouponFactor(dtSettle, dtCash);
            double wt = Math.pow(discFac, perCount);
            double pv = wt * cashflow;
            pvNum += perCount * pv;
            pvDen += pv;
        }
        if (pvDen == 0.) {
            bond.error.setError(getClass().getName(), "Attempt to divide by zero");
            return macDur;
        }
        macDur = pvNum / (pvDen * perLen);
        return macDur;
    }

    public double modDurationSIA(int calcNo, int securityNo, String instype, Dates dtSettle, double yield, Dates dtRedeem, double redeemPrin, double redeemInt) {
        double modDur = 0.;

        if (instype.equals(Instype.DISCOUNT)) {
            modDur = discount.modDurationSIA(calcNo, dtSettle, dtRedeem);
            return modDur;
        }
        if (instype.equals(Instype.ATMATURITY) || instype.equals(Instype.CURRENCY)) {
            modDur = atMaturity.macDuration(calcNo, dtSettle, dtRedeem);
            return modDur;
        }
        double macDur = macDuration(calcNo, securityNo, instype, dtSettle, yield, dtRedeem, redeemInt, redeemPrin);
        double perPerYear = bond.intSchedRule.periodPerYear();

        if (perPerYear == 0.) {
            bond.error.setError(getClass().getName(), "attempt to divide by zero");
            return modDur;
        }

        if (UnAvails.isUnAvail(yield)) {
            if (UnAvails.isUnAvail(bond.settlePrc)) {
                String errmsg = "no price or yield was specified so cannot calculate modified duration SIA";
                bond.error.setError(getClass().getName(), errmsg);
                return 0.;
            }
            yield = yieldPrice(calcNo, securityNo, instype, dtSettle, bond.settlePrc, dtRedeem, redeemPrin, redeemInt);
            if (bond.error.hasErrorYn == 'Y') {
                return 0.;
            }
        }
        modDur = macDur / (1. + yield / perPerYear);
        double scale = calcDef.calcDefs.get(calcNo).scale;
        return modDur * scale;
    }

    public double modDurationSTD(int calcNo, int securityNo, String instype, Dates dtSettle, double yield, Dates dtRedeem, double redeemInt, double redeemPrin) {
        double prcUp, prcDn, prc, yldShift = .5 * CalUnits.durationYieldShift;
        if (instype.equals(Instype.DISCOUNT)) {
            double modStd = discount.modDurationSTD(calcNo, dtSettle, yield, dtRedeem, redeemPrin, redeemInt);
            return modStd;
        }
        char cleanPrcYn = bond.cleanPrcYn;
        prc = priceYield(calcNo, securityNo, instype, dtSettle, yield, dtRedeem, redeemPrin, redeemInt);
        prcDn = priceYield(calcNo, securityNo, instype, dtSettle, yield + yldShift, dtRedeem, redeemPrin, redeemInt);
        prcUp = priceYield(calcNo, securityNo, instype, dtSettle, yield - yldShift, dtRedeem, redeemPrin, redeemInt);
        double modDur = (prcUp - prcDn) / (CalUnits.durationYieldShift * prc);
        /*
        double pctPrc = (prcUp - prcDn) /prc;
        double pctYld = (2 * yldShift) / yield;
        double modDur = pctPrc / pctYld;

         */
        double scale = calcDef.calcDefs.get(calcNo).scale;
        return modDur * scale;
    }

    public double conVexity(int calcNo, int securityNo, String instype, Dates dtSettle, double yield, Dates dtRedeem, double redeemInt, double redeemPrin) {
        double cVex = 0.;
        double v, vUp, vDn, dV = CalUnits.durationYieldShift;

        if (UnAvails.isUnAvail(yield)) {
            if (UnAvails.isUnAvail(bond.settlePrc)) {
                String errmsg = "no price or yield was specified so cannot calculate convexity";
                bond.error.setError(getClass().getName(), errmsg);
                return 0.;
            }
            yield = yieldPrice(calcNo, securityNo, instype, dtSettle, bond.settlePrc, dtRedeem, redeemPrin, redeemInt);
            if (bond.error.hasErrorYn == 'Y') {
                return 0.;
            }

        }
        if (instype.equals(Instype.DISCOUNT)) {
            cVex = discount.convexity(calcNo, dtSettle, yield, dtRedeem, redeemPrin, redeemInt, bond.yieldQuoteType);
            return cVex;
        }
        v = priceYield(calcNo, securityNo, instype, dtSettle, yield, dtRedeem, redeemPrin, redeemInt);
        vDn = priceYield(calcNo, securityNo, instype, dtSettle, yield - dV, dtRedeem, redeemPrin, redeemInt);
        vUp = priceYield(calcNo, securityNo, instype, dtSettle, yield + dV, dtRedeem, redeemPrin, redeemInt);
        cVex = (vUp + vDn - 2 * v) / (v * dV * dV);
        double scale = calcDef.calcDefs.get(calcNo).scale;
        return cVex * scale;
    }


    class CashflowsPayments {
        double levelPay;
        double schedPay;
        double prePay;

        double intPay;
        Dates dtPay;

        CashflowsPayments() {
            levelPay = 0.;
            schedPay = 0.;
            prePay = 0.;
            dtPay = new Dates();
            intPay = 0;
        }
    }

// interet pay on dtSettle
// if dtSettle is not an interest pay date, then payment = 0


    public int paySchedule(int calcNo, Dates dtSettle) {
        int count = 0, wkNum = 0;
        String strPaySched = null;
        Dates dtAccrueLast = new Dates();
        Dates dtAccrueNext = new Dates();
        Dates dtPay = new Dates();
        int days = 0;
        int idx = 0;
        char isHolidayYn;
        String dow;
        dtAccrueLast = bond.accrueSchedRule.getNextSchedDate(BondConsts.BEFOREONDATE, dtSettle);
        dtAccrueNext = bond.accrueSchedRule.getNextSchedDate(BondConsts.AFTERDATE, dtAccrueLast);
        while (dtAccrueNext.ltDate(dtAccrueNext, bond.dtMaturity)) {
            dtAccrueNext.setUpdateDate(bond.dtFormat);
            dtPay = bond.intSchedRule.getNextSchedDate(BondConsts.AFTERDATE, dtAccrueNext);
            dtPay.setUpdateDate(bond.dtFormat);
            dow = dtPay.getDayOfWeek(dtPay);
            days = dayCount.getDaycount(dtAccrueLast, dtAccrueNext, bond.dtMaturity, bond.dcAccruedInt);
            idx = bond.intSchedRule.holidays.getHolidayIndex(dtAccrueNext, bond.country);
            isHolidayYn = bond.error.BSNO;
            if (idx >= 0)
                isHolidayYn = bond.error.BSYES;
            calcResults.addPaySchedule(count, dtAccrueNext, dtPay, dow, days, isHolidayYn);
            dtAccrueLast.setDate(dtAccrueNext);
            dtAccrueNext = bond.accrueSchedRule.getNextSchedDate(BondConsts.AFTERDATE, dtAccrueLast);
            ++calcResults.paySchedCount;
        }
        return calcResults.paySchedCount;
    }

    public int calcCashflowInterval(int calcNo, int securityNo, int tNo, String instype, Dates dtRedeem, double redeemPrin, double redeemInt, Dates dtStart, Dates dtEnd) {
        double intPay = 0., schedPay = 0., prePay = 0.;
        Dates dtCashflow = new Dates();
        double cfRein = 0.;
        double intInc = 0.;
        double prinInc = 0.;
        int trackNo = bond.schedTracks.currentTrackNo;
        SchedTrack schedTrack = new SchedTrack();
        schedTrack = bond.schedTracks.getSchedTrackOnAfter(dtStart, SchedTrack.ANYTYPETRACK);
        dtCashflow = schedTrack.schedDate;
        int noFlow = 0;
        while (dtCashflow.leDate(dtCashflow, dtEnd)) {
            intPay = getInterstCash(calcNo, instype, dtRedeem, redeemInt, schedTrack);
            if (bond.error.hasErrorYn == 'Y')
                return 0;
            schedPay = getSchedulePrin(calcNo, instype, dtRedeem, redeemPrin, schedTrack);
            if (bond.error.hasErrorYn == 'Y')
                return 0;
            prePay = getPrepayPrin(calcNo, instype, dtRedeem, redeemPrin, schedTrack);
            cfRein = calcReinvestCash(calcNo, dtCashflow, bond.dtMaturity, intPay);
            intInc += intPay + cfRein;
            prinInc += schedPay + prePay;
            int cashflowYear = dtCashflow.year;
            SchedTrack taxTrack = new SchedTrack();
            taxTrack = bond.schedTracks.getSchedTrackOn(dtCashflow, SchedTrack.FEDTAXPAYMENTTRACK);
            double cfFedTax = 0.;
            double cfStateTax = 0.;
            if (taxTrack.found) {
                int daysHeld = dayCount.getDaycountDCACT(bond.dtPur, dtCashflow);
                int taxYear = cashflowYear - 1;
                // cfFedTax = -bond.taxes.calcFedTax(taxYear);
                // cfStateTax = -bond.taxes.calcStateTax(taxYear);
            }
            addCashflowList(calcNo, noFlow, securityNo, dtStart, dtCashflow, intPay, schedPay, prePay, cfFedTax, cfStateTax);
            schedTrack = bond.schedTracks.getSchedTrackOnAfter(dtCashflow, SchedTrack.ANYTYPETRACK);
            dtCashflow = schedTrack.schedDate;
            ++noFlow;
        }

        return noFlow;
    }

    // the bookvalue on any date
    public double bookValue(int calcNo, int securityNo, String instype, Dates dtSettle, double purYld, Dates dtRedeem, double redeemPrin, double redeemInt, String bookMeth) {
        double bookVal = 0.;
        if (dtSettle.geDate(dtSettle, bond.dtRedeem)) {
            return bond.faceAmt * bond.redeemPrin;
        }
        if (instype.equals(Instype.CURRENCY))
            return bond.faceAmt;
        switch (bookMeth) {
            case Choices.HOLDATCOST:
                bookVal = bond.purPrc;
                break;
            case Choices.STRIGHTLINE:
                bookVal = bookValueStLine(calcNo, instype, dtSettle, dtRedeem, redeemPrin, redeemInt);
                break;
            case Choices.CONSTYIELD:
            case Choices.CONSTYIELDP:
                bookVal = bookValueConstYield(calcNo, securityNo, instype, dtSettle, dtRedeem, redeemPrin, redeemInt, purYld);
                break;
            case Choices.EFFINCOME:
            case Choices.EFFINCOMEP:
                bookVal = bookValueEffIncome(calcNo, securityNo, instype, dtSettle, dtRedeem, redeemPrin, redeemInt, purYld);
                break;
            case Choices.PROSPECTYIELD:
                // bookVal = bookValueProspectYield(calcNo, dtSettle);
                break;
            default:
                break;
        }
        return bond.faceAmt * bookVal;
    }


    public double priceYield(int calcNo, int securityNo, String instype, Dates dtSettle, double yield, Dates dtRedeem, double redeemPrin, double redeemInt) {
        double price = 0.;
        if (calcNo == CalcList.PRICETOMATURIY) {
            if (UnAvails.isAvail(bond.settlePrc))
                return bond.settlePrc;
        }
        switch (instype) {
            case Instype.DISCOUNT:
                price = discount.priceYield(calcNo, dtSettle, yield, dtRedeem, redeemPrin, redeemInt);
                break;
            case Instype.ATMATURITY:
            case Instype.CURRENCY:
                price = atMaturity.priceYield(calcNo, dtSettle, yield, dtRedeem, redeemPrin, redeemInt);
                break;
            default:
                price = priceYieldPeriodic(calcNo, securityNo, instype, dtSettle, yield, dtRedeem, redeemPrin, redeemInt);
                break;
        }
        if (UnAvails.isUnAvail(bond.settlePrc))
            bond.settlePrc = price;
        return price;

    }


    public double priceYieldPeriodic(int calcNo, int securityNo, String instype, Dates dtSettle, double yield, Dates dtRedeem, double redeemPrin, double redeemInt) {
        double netPay;
        double accInt = 0.;
        if (bond.cleanPrcYn == 'Y')
            accInt = accruedInterest(calcNo, instype, dtSettle) / bond.faceAmt;
        double scale = 1.;
        double rrv = getRedeemValue(instype, dtSettle, redeemPrin);
        cashflowList = cashflow(calcNo, scale, securityNo, instype, dtSettle, dtRedeem);
        double presVal = presentValue(yield);
        if (rrv == 0.) {
            if (instype.equals(Instype.IRSSWAP) || instype.equals(Instype.PERPETUAL))
                return presVal * calcDef.calcDefs.get(calcNo).scale;
            else {
                String errmsg = "no price can be calculated because the redemption value on settle is zero - the bond has matured";
                bond.error.setError(getClass().getName(), errmsg);
                return 0.;
            }
        }
        double price = 0.;
        if (bond.useCurrentRedemptionYn == 'Y')
            price = (presVal - accInt) / rrv;
        else
            price = (presVal - accInt) * bond.redeemPrin;
        if (price <= 0.) {
            String errmsg = "a negative price of " + price + " was calculated from a specified yield of " + yield;
            bond.error.setWarning(getClass().getName(), errmsg);
        }
        return price;
    }

    public double priceYieldIRSSwap(int calcNo, int securityNo, String instype, Dates dtSettle, double yield) {
        double netPay;
        double accInt = 0.;
        if (bond.cleanPrcYn == 'Y')
            accInt = accruedInterest(calcNo, instype, dtSettle) / bond.faceAmt;
        double scale = 1.;
        double rrv = 0;
        cashflowList = cashflow(calcNo, scale, securityNo, instype, dtSettle, bond.dtMaturity);
        double presVal = presentValue(yield);
        double price = presVal;
        return price * calcDef.calcDefs.get(calcNo).scale;
    }


    public double bookValueStLine(int calcNo, String instype, Dates dtSettle, Dates dtRedeem, double redeemPrin, double redeemInt) {
        double bookValue = 0.;
        switch (instype) {
            case Instype.DISCOUNT:
                bookValue = bond.purPrc;
                break;
            case Instype.ATMATURITY:
                bookValue = bond.purPrc;
                break;
            case Instype.FIXEDRATE:
                bookValue = bookValueStLineBullets(calcNo, instype, dtSettle);
                break;
            case Instype.FLOATER:
                bookValue = bookValueStLineBullets(calcNo, instype, dtSettle);
                break;
            case Instype.MORTGAGE:
                bookValue = bookValueStLineMortgage(dtSettle);
                break;
            case Instype.SINKER:
                bookValue = bookValueStLineSinker(dtSettle);
                break;
            case Instype.PERPETUAL:
            case Instype.PERPETUALFLOAT:
                bookValue = bond.purPrc;
                break;
            case Instype.DISCOUNTSINK:
                break;
            case Instype.INDEXED:
                break;
            case Instype.STEPPED:
                break;
            case Instype.ARM:
                break;
            case Instype.PIK:
                break;
            case Instype.INDEXSINK:
                break;
            case Instype.PIKSINK:
                break;
            case Instype.FACTOREDFLOAT:
                break;
            case Instype.SINKFLOAT:
                break;
            case Instype.TOGGLE:
                break;
            case Instype.IRSSWAP:
                break;
            default:
                String errmsg = " invalid instrument type " + instype + " detected";
                bond.error.setError(getClass().getName(), errmsg);
                return 0;

        }
        return bookValue;

    }

    public double bookValueStLineBullets(int calcNo, String instype, Dates dtSettle) {
        double premDisc, bookVal, accreAmort;
        // only for non-sinkers, non-factored and non-indexed
        double purRedeem = getRedeemValue(instype, bond.dtPur, bond.redeemPrin);
        premDisc = purRedeem - bond.purPrc;
        double daysMaturity = dayCount.getDaycount(bond.dtPur, bond.dtLastPayment, bond.dtMaturity, bond.dcAccruedInt);
        if (daysMaturity == 0) {
            String errmsg = "Attempt to divide by zero as zero daycount between purchase date " + bond.dtPur.getDateString() + " and maturity date " + bond.dtRedeem.getDateString();
            bond.error.setError(getClass().getName(), errmsg);
            return 0.;
        }
        double dayRate = premDisc / daysMaturity;
        int daysHeld = dayCount.getDaycount(bond.dtPur, dtSettle, bond.dtMaturity, bond.dcAccruedInt);
        accreAmort = premDisc * daysHeld / daysMaturity;
        bookVal = bond.purPrc + accreAmort;
        return bookVal;
    }

    private double bookValueConstYield(int calcNo, int securityNo, String instype, Dates dtSettle, Dates dtRedeem, double redeemPrin, double redeemInt, double purYld) {
        double bookVal = 0.;
        double rrv = 0.;

        if (choices.inPerpetualList(instype))
            return bond.purPrc;
        if (dtSettle.geDate(dtSettle, dtRedeem)) {
            bookVal = getRedeemValue(instype, dtSettle, redeemPrin);
            return bookVal;
        }

        bookVal = priceYield(calcNo, securityNo, instype, dtSettle, purYld, dtRedeem, redeemPrin, redeemInt);
        return bookVal;
    }

    private double bookValueStLineMortgage(Dates dtSettle) {
        double rrv = mortgage.getPassthruFactor(dtSettle);
        return rrv;
    }

    private double bookValueStLineSinker(Dates dtSettle) {
        double rrv = getRedeemValueSink(dtSettle, bond.redeemPrin);
        return rrv;
    }


    public double gainLoss(int calcNo, int securityNo, String instype, Dates dtSettle, Dates dtRedeem, double redeemPrin, double redeemInt, double mktPrc) {
        double gainloss = 0.;
        double useMktPrc = mktPrc;
        char use360 = 'N';
        if (UnAvails.isUnAvail(mktPrc)) {
            if (UnAvails.isAvail(bond.mktYld))
                useMktPrc = priceYield(calcNo, securityNo, instype, dtSettle, bond.mktYld, dtRedeem, redeemPrin, redeemInt);
            else {
                String errmsg = "no market price or market yield was specified";
                bond.error.setError(getClass().getName(), errmsg);
                return 0.;
            }
        }
        gainloss = bond.faceAmt * (useMktPrc - bond.purPrc);
        double months = dayCount.getMonthCount(bond.dtPur, dtSettle, use360);
        if (months < CalUnits.MONTHSSHORTTERMTAXGL) {
            if (UnAvails.isAvail(bond.taxRtFedCapGainShort))
                gainloss = gainloss * (1. - bond.taxRtFedCapGainShort) * (1. - bond.taxRtStateIncome);
        }
        if (months > CalUnits.MONTHSSHORTTERMTAXGL) {
            if (UnAvails.isAvail(bond.taxRtFedCapGainLong))
                gainloss = gainloss * (1. - bond.taxRtFedCapGainLong) * (1. - bond.taxRtStateIncome);
        }
        return gainloss;
    }


    private double bookValueEffIncome(int calcNo, int securityNo, String instype, Dates dtSettle, Dates dtRedeem, double redeemPrin, double redeemInt, double purYld) {
        Dates dtLast = new Dates();
        Dates dtNext = new Dates();
        double cashScale = 1.;
        double accreAmort = 0.;
        SchedTrack schedTrack = new SchedTrack();
        dtNext = bond.intSchedRule.getNextSchedDate(SchedTracks.AFTERDATE, bond.dtPur);
        double perYld = purYld / bond.intSchedRule.compoundFreq;
        double currBookValue = cashScale * bond.purPrc;
        dtLast = bond.dtPur;
        if (dtSettle.eqDate(dtSettle, bond.dtLastPayment))
            return getRedeemValue(instype, bond.dtLastPayment, bond.redeemPrin);
        cashflowList = cashflow(calcNo, cashScale, securityNo, instype, bond.dtPur, bond.dtRedeem);
        schedTrack = bond.schedTracks.getSchedTrackAfter(bond.dtPur, SchedTrack.ACCRUETRACK);
        if (!schedTrack.found)
            return 0.;
        dtNext = schedTrack.schedDate;
        int cFNo = 0;
        while (dtNext.leDate(dtNext, dtSettle)) {
            double intCash = cashflowList.get(cFNo).intCash;
            double prinCash = cashflowList.get(cFNo).totPrinCash;
            accreAmort = currBookValue * perYld - intCash - prinCash;
            dtLast = dtNext;
            currBookValue = currBookValue + accreAmort;
            schedTrack = bond.schedTracks.getSchedTrackAfter(dtLast, SchedTrack.ACCRUETRACK);
            if (!schedTrack.found)
                return 0.;
            dtNext = schedTrack.schedDate;
            ++cFNo;
        }
        return currBookValue;
    }

    // accretion or amortization from any date to maturity
    public int accreAmortSchedule(int calcNo, int securityNo, String instype, Dates dtStart, double purYield, Dates dtRedeem, double redeemPrin, double redeemInt, String bookCalcMeth) {
        int count = 0;
        double totAccreAmort = 0.;
        Dates dtLast = new Dates();
        Dates dtNext = new Dates();
        double useYield = 0.;
        calcResults.accreAmortResults.zeroAccreAmortResultList();
        int noFlow = 0;
        if (bond.error.hasErrorYn == 'Y') {
            return 0;
        }
        if (instype.equals(Instype.CURRENCY)) {
            calcResults.accreAmortResults.sumAccreAmort = 0.;
            calcResults.accreAmortResults.sumDays = 0;
            calcResults.accreAmortResults.count = 1;
            return 1;
        }
        if (choices.inDiscountList(instype)) {
            // discount and atmaturity are the same calculations
            if (instype.equals(Instype.DISCOUNT))
                count = discount.accreAmortSchedule(calcNo, instype, dtStart, dtRedeem, redeemPrin, redeemInt, bookCalcMeth);
            else
                count = discount.accreAmortSchedule(calcNo, instype, dtStart, dtRedeem, redeemPrin, redeemInt, bookCalcMeth);
            return count;
        }
        boolean isAfter = true;
        calcResults.accreAmortResults.sumAccreAmort = 0.;
        calcResults.accreAmortResults.sumDays = 0;
        calcResults.accreAmortResults.count = count;
        useYield = purYield;
        if (UnAvails.isUnAvail(purYield)) {
            if (UnAvails.isUnAvail(bond.purPrc)) {
                String errmsg = "no purchase yield or purchase price was specified";
                bond.error.setError(getClass().getName(), errmsg);
                return 0;
            } else {
                useYield = yieldPrice(calcNo, securityNo, instype, bond.dtPur, bond.purPrc, bond.dtRedeem, bond.redeemPrin, bond.redeemInt);
                if (bond.error.hasErrorYn == 'Y')
                    return 0;
            }
        }
        SchedTrack schedTrack = new SchedTrack();
        dtLast = dtStart;
        schedTrack = bond.schedTracks.getSchedTrackAfter(dtStart, SchedTrack.ACCRUETRACK);
        if (!schedTrack.found)
            return 0;
        double bookLast = 0.;
        if (dtStart.leDate(dtStart, bond.dtPur))
            bookLast = bond.purPrc * bond.faceAmt;
        else
            bookLast = bookValue(calcNo, securityNo, instype, dtStart, useYield, dtRedeem, redeemPrin, redeemInt, bookCalcMeth);
        double bookNext = 0.;
        dtLast = schedTrack.schedDate;
        schedTrack = bond.schedTracks.getSchedTrackAfter(dtLast, SchedTrack.ACCRUETRACK);
        if (!schedTrack.found)
            return 0;
        count = 0;
        boolean isOk = false;
        dtNext = schedTrack.schedDate;
        while (dtNext.ltDate(dtNext, bond.dtLastPayment)) {
            bookNext = bookValue(calcNo, securityNo, instype, dtNext, useYield, dtRedeem, redeemPrin, redeemInt, bookCalcMeth);
            isOk = accreAmortInterval(calcNo, count, dtLast, dtNext, bookLast, bookNext);
            if (!isOk)
                return 0;
            dtLast = dtNext;
            bookLast = bookNext;
            schedTrack = bond.schedTracks.getSchedTrackAfter(dtLast, SchedTrack.ACCRUETRACK);
            if (!schedTrack.found)
                return 0;
            dtNext = schedTrack.schedDate;
            totAccreAmort += bookNext - bookLast;
            ++count;
        }

        bookNext = bond.faceAmt * getRedeemValue(instype, dtRedeem, redeemPrin);
        isOk = accreAmortInterval(calcNo, count, dtLast, dtRedeem, bookLast, bookNext);
        if (!isOk)
            return 0;
        calcResults.accreAmortResults.count = ++count;
        return count;
    }

    public double accreAmort(int calcNo, String instype, Dates dtStart, Dates dtEnd, double prcYield, String bookCalcMeth) {
        return 0;
    }

    // accreation or amortization etween two dates
    public boolean accreAmortInterval(int calcNo, int count, Dates dtLast, Dates dtNext, double bookLast, double bookNext) {
        double accreAmort = bookNext - bookLast;
        int days = dayCount.getDaycount(dtLast, dtNext, bond.dtMaturity, bond.dcAccruedInt);
        if (days == 0) {
            String errmsg = "attempt to divide by zero in day counting between " + dtLast.getDateString() + " and " + dtNext.getDateString();
            bond.error.setError(getClass().getName(), errmsg);
            return false;
        }
        double dailyAccreAmort = accreAmort / days;
        calcResults.addAccreAmortSchedule(count, dtLast, dtNext, bookLast, bookNext, accreAmort, dailyAccreAmort,
                days);
        calcResults.accreAmortResults.sumAccreAmort += accreAmort;
        calcResults.accreAmortResults.sumDays += days;
        return true;
    }

    public String outputDoubleConsole(int calcNo, double result, int sfig) {
        String sResult = "";
        //String format = "%0." + sfig + "f";
        String format = "";
        if (bond.error.hasErrorYn == 'Y') {
            sResult = bond.error.getErrorMessage();
            return sResult;

        } else {
            if (UnAvails.isAvail(sfig))
                format = "%." + sfig + "f";
            else
                format = calcDef.calcDefs.get(calcNo).format;
            sResult = commandLine.consoleQuote + calcDef.calcDefs.get(calcNo).calcName + commandLine.consoleQuote + commandLine.consoleParamDelimit + String.format(format, result);
        }
        return sResult;
    }

    public String outputDoubleFile(int calcNo, double result, int sfig) {
        String sResult = "";
        //String format = "%0." + sfig + "f";
        String format = "";
        calcResults.calcDef = calcDef.calcDefs.get(calcNo);
        if (bond.error.hasErrorYn == 'Y') {
            sResult = bond.error.getErrorMessage();
            fileCalcStr = "Error" + commandLine.fileParamDelimit + bond.error.getErrorMessage();
            return sResult;
        } else {
            if (UnAvails.isAvail(sfig))
                format = "%." + sfig + "f";
            else
                format = calcDef.calcDefs.get(calcNo).format;
            sResult = calcDef.calcDefs.get(calcNo).calcName + commandLine.fileParamDelimit + String.format(format, result);
        }
        return sResult;
    }

    public String outputIntFile(int calcNo, int iResult) {
        String sResult;
        String sfileResult;
        String sCalcResult = "";
        //String format = "%0." + sfig + "f";
        String format = calcDef.calcDefs.get(calcNo).format;
        if (bond.error.hasErrorYn == 'Y')
            sResult = bond.error.errorMessage;
        else
            sResult = calcDef.calcDefs.get(calcNo).calcName + commandLine.fileParamDelimit + String.format(format, iResult);
        return sResult;
    }

    public String outputIntConsole(int calcNo, int iResult) {
        String sResult;
        String sfileResult;
        String sCalcResult = "";
        //String format = "%0." + sfig + "f";
        String format = "%d";
        if (bond.error.hasErrorYn == 'Y') {
            sResult = bond.error.getErrorMessage();
            bond.error.clearError();
            sResult = bond.error.getErrorMessage();
        } else {
            format = calcDef.calcDefs.get(calcNo).format;
            sResult = commandLine.consoleQuote + calcDef.calcDefs.get(calcNo).calcName + commandLine.consoleQuote + commandLine.consoleParamDelimit + String.format(format, iResult);
        }
        return sResult;
    }

    public String outputError() {
        String sResult = "";
        //String format = "%0." + sfig + "f";
        sResult = bond.error.errorMessage;
        return sResult;
    }


    public String outResultString(CalcDef calcList, String result) {
        return result;
    }

    public String outputConsoleDate(int calcNo, Dates dtResult) {
        CommandLine cl = commandLine;
        String strOut = cl.consoleQuote + calcDef.calcDefs.get(calcNo).calcName + cl.consoleQuote + cl.consoleParamDelimit;
        strOut += cl.consoleQuote + dtResult.getDateString() + cl.consoleQuote;
        return strOut;
    }

    public String outputFileDate(int calcNo, Dates dtResult) {
        CommandLine cl = commandLine;
        String strOut = calcDef.calcDefs.get(calcNo).calcName + cl.fileParamDelimit + dtResult.getDateString();
        return strOut;
    }


    public String outputCashflowConsoleOrig(String calcName) {
        int flowNo = -1;
        String strFlow = "";
        if (bond.error.hasErrorYn == 'Y') {
            strFlow = bond.error.getErrorMessage();
            return strFlow;
        }
        String format = null;
        if (UnAvails.isUnAvail(bond.sfigCashflow))
            format = "%,.2f";
        else
            format = "%,." + bond.sfigCashflow + "f";
        String strItem = "";

        int noFlows = cashflowList.size();
        while (++flowNo < noFlows) {
            int iflowNo = flowNo + 1;
            String keyWord = commandLine.consoleQuote + calcDef.calcDefs.get(CalcList.CASHFLOW).calcName + iflowNo + commandLine.consoleQuote + commandLine.consoleParamDelimit;
            strFlow = strFlow + keyWord + commandLine.consoleLeftBracket;
            Dates dtFlow = cashflowList.get(flowNo).dtCashflow;
            // cashflow datr
            strItem = commandLine.consoleQuote + dtFlow.getDateString(dtFlow, bond.dtFormat) + commandLine.consoleQuote + ",";
            strFlow = strFlow.concat(strItem);
            // day of the week
            strItem = commandLine.consoleQuote + cashflowList.get(flowNo).strDayOfWeek + commandLine.consoleQuote + ",";
            strFlow = strFlow.concat(strItem);

            double perLen = cashflowList.get(flowNo).perLen;
            perLen = Utility.truncRound(perLen, 3, 'Y');
            // coupon periods to cashflow
            strItem = perLen + ",";
            strFlow = strFlow.concat(strItem);

            // years to cashflow
            double years = cashflowList.get(flowNo).years;
            years = Utility.truncRound(years, 3, 'Y');
            strItem = years + ",";
            strFlow = strFlow.concat(strItem);

            // total cashflow
            double dVal = cashflowList.get(flowNo).totCashflow;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = dVal + ",";
            strFlow = strFlow.concat(strItem);

            // total principal cashflow
            dVal = cashflowList.get(flowNo).totPrinCash;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = dVal + ",";
            strFlow = strFlow.concat(strItem);

            // scheduled principal flow
            dVal = cashflowList.get(flowNo).schedPrinCash;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = dVal + ",";
            strFlow = strFlow.concat(strItem);

            // prepay cashfllow
            dVal = cashflowList.get(flowNo).prePaycash;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = dVal + ",";
            strFlow = strFlow.concat(strItem);

            // interest cashflow
            dVal = cashflowList.get(flowNo).intCash;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = dVal + ",";
            strFlow = strFlow + strItem;

            dVal = cashflowList.get(flowNo).fedTaxPaid;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = dVal + ",";
            strFlow = strFlow + strItem;

            dVal = cashflowList.get(flowNo).stateTaxPaid;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = "" + dVal;
            strFlow = strFlow + strItem;
            if (flowNo < cashflowResults.count - 1)
                strFlow = strFlow + "]" + commandLine.consoleCalcDelimit;
            else
                strFlow = strFlow + "]";

        }
        strFlow = strFlow.trim();
        return strFlow;
    }

    public String outputCashflowConsole(int calcNo, String calcName) {
        int flowNo = -1;
        String strFlow = "";
        if (bond.error.hasErrorYn == 'Y') {
            strFlow = bond.error.getErrorMessage();
            return strFlow;
        }
        String format = null;
        if (UnAvails.isUnAvail(bond.sfigCashflow))
            format = "%,.2f";
        else
            format = "%,." + bond.sfigCashflow + "f";
        String strItem = "";
        strFlow = commandLine.consoleQuote + calcName + commandLine.consoleQuote + commandLine.consoleParamDelimit + commandLine.consoleLeftBracket;
        int noFlows = cashflowList.size();
        while (++flowNo < noFlows) {
            strFlow = strFlow + commandLine.consoleObjectStartDelimit;
            Dates dtFlow = cashflowList.get(flowNo).dtCashflow;
            // cashflow datr"
            strItem = commandLine.consoleQuote + "Date" + commandLine.consoleQuote + commandLine.consoleParamDelimit + commandLine.consoleQuote + dtFlow.getDateString(dtFlow, bond.dtFormat) + commandLine.consoleQuote + ",";
            strFlow = strFlow.concat(strItem);
            // day of the week
            if (calcNo == CalcList.CASHFLOW) {
                strItem = commandLine.consoleQuote + "DayOfWk" + commandLine.consoleQuote + commandLine.consoleParamDelimit + commandLine.consoleQuote + cashflowList.get(flowNo).strDayOfWeek + commandLine.consoleQuote + ",";
                strFlow = strFlow.concat(strItem);
                double perLen = cashflowList.get(flowNo).perLen;
                perLen = Utility.truncRound(perLen, 3, 'Y');
                // coupon periods to cashflow
                strItem = commandLine.consoleQuote + "Periods" + commandLine.consoleQuote + commandLine.consoleParamDelimit + perLen + ",";
                strFlow = strFlow.concat(strItem);
            }
            // years to cashflow
            double years = cashflowList.get(flowNo).years;
            years = Utility.truncRound(years, 3, 'Y');
            strItem = commandLine.consoleQuote + "Years" + commandLine.consoleQuote + commandLine.consoleParamDelimit + years + ",";
            strFlow = strFlow.concat(strItem);
            // total cashflow
            double dVal = cashflowList.get(flowNo).totCashflow;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = commandLine.consoleQuote + "TotalCash" + commandLine.consoleQuote + commandLine.consoleParamDelimit + dVal + ",";
            strFlow = strFlow.concat(strItem);
            // total principal cashflow
            dVal = cashflowList.get(flowNo).totPrinCash;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = commandLine.consoleQuote + "TotalPrin" + commandLine.consoleQuote + commandLine.consoleParamDelimit + dVal + ",";
            strFlow = strFlow.concat(strItem);
            // scheduled principal flow
            if (calcNo == CalcList.CASHFLOW) {
                dVal = cashflowList.get(flowNo).schedPrinCash;
                dVal = Utility.truncRound(dVal, 2, 'Y');
                strItem = commandLine.consoleQuote + "ScheduledPrin" + commandLine.consoleQuote + commandLine.consoleParamDelimit + dVal + ",";
                strFlow = strFlow.concat(strItem);
                // prepay cashfllow
                dVal = cashflowList.get(flowNo).prePaycash;
                dVal = Utility.truncRound(dVal, 2, 'Y');
                strItem = commandLine.consoleQuote + "PrePayPrin" + commandLine.consoleQuote + commandLine.consoleParamDelimit + dVal + ",";
                strFlow = strFlow.concat(strItem);
            }
            // interest cashflow
            dVal = cashflowList.get(flowNo).intCash;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = commandLine.consoleQuote + "Interest" + commandLine.consoleQuote + commandLine.consoleParamDelimit + dVal + ",";
            strFlow = strFlow + strItem;
            dVal = cashflowList.get(flowNo).fedTaxPaid;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = strItem = commandLine.consoleQuote + "FedTax" + commandLine.consoleQuote + commandLine.consoleParamDelimit + dVal + ",";
            strFlow = strFlow + strItem;
            dVal = cashflowList.get(flowNo).stateTaxPaid;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = commandLine.consoleQuote + "StateTax" + commandLine.consoleQuote + commandLine.consoleParamDelimit + dVal;
            strFlow = strFlow + strItem;
            if (flowNo < noFlows - 1)
                strFlow = strFlow + commandLine.consoleObjectEndDelimit + commandLine.consoleCalcDelimit;
            else
                // strFlow = strFlow + "}]";
                strFlow = strFlow + commandLine.consoleObjectEndDelimit + commandLine.consoleRightBracket;


        }
        strFlow = strFlow.trim();
        return strFlow;
    }


    public String outputCashflowConsoleOrFile(int calcNo, String cashflowName, boolean outputToFile) {
        int flowNo = -1;
        double years = 0.;
        String format, strFlow = "", strItem = "";
        String fileItemStr = "";
        if (bond.error.hasErrorYn == 'Y')
            return bond.error.errorMessage;
        if (UnAvails.isUnAvail(bond.sfigCashflow))
            format = "%.2f";
            // format = calcDefs.get(CASHFLOW).format;
        else {
            format = "%," + bond.sfigCashflow + "f";
        }
        String cflowDelimit = ",";
        String heading = "";
        String openBrackett = ":[";
        String closeBrackett = "]";
        String keyWordSeparator = "";
        if (outputToFile) {
            cflowDelimit = "\n";
            heading = cashflowName + '\n' + "Cashflow,Date,DayOfWk,Periods,Years,Total Cash,Total Prin,Sched Prin,Prepay Prin,Interest" + "\n";
            strFlow = heading;
            openBrackett = "";
            closeBrackett = "";
            keyWordSeparator = ",";

        }
        int noFlows = cashflowList.size();
        while (++flowNo < noFlows) {
            String keyWord = cashflowName + (flowNo + 1);
            keyWord = "\"" + keyWord + "\"" + openBrackett;
            Dates dtStr = cashflowList.get(flowNo).dtCashflow;
            String strPre = keyWord + keyWordSeparator + "\"" + dtStr.getDateString(dtStr, bond.dtFormat) + "\"" + ",";
            strFlow = strFlow.concat(strPre);
            strItem = "\"" + cashflowList.get(flowNo).strDayOfWeek + "\"" + ",";
            strFlow = strFlow.concat(strItem);
            double perLen = cashflowList.get(flowNo).perLen;
            perLen = Utility.truncRound(perLen, 4, 'Y');
            strItem = String.format("%.4f", perLen) + ",";
            strFlow = strFlow.concat(strItem);
            // round years and period
            years = cashflowList.get(flowNo).years;
            years = Utility.truncRound(years, 4, 'Y');
            strItem = String.format("%.4f", years);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            // total cashflow
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            strItem = decimalFormat.format(cashflowList.get(flowNo).totCashflow);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            // total principal
            strItem = decimalFormat.format(cashflowList.get(flowNo).totPrinCash);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            strItem = decimalFormat.format(cashflowList.get(flowNo).schedPrinCash);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            strItem = decimalFormat.format(cashflowList.get(flowNo).prePaycash);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            strItem = decimalFormat.format(cashflowList.get(flowNo).intCash);
            // interest c.cashflow
            strFlow = strFlow + strItem + commandLine.fileCalcDelimit;
            fileCalcStr = fileItemStr.concat((strFlow + commandLine.fileCalcDelimit));
            if (flowNo < cashflowResults.count - 1)
                strFlow = strFlow.concat(cflowDelimit);
        }
        return strFlow;
    }


// For net cahflows that are a combined cashflow for multiple securities


    public String outputCashflowFile(int calcNo, String calcName) {
        String format = "";
        String footerLine = "";
        String totalsFormat = "";
        String sGap = "   "; // space separater
        int noFlows = cashflowList.size();
        String headerTitle = calcName;
        String columnHeading;
        String columnUnderLine;
        String fmtDate = "%10s"; // 10 spaces
        String fmtDow = "%7s";
        String fmtPerLen = "%8.3f";
        String fmtYears = "%8.3f";
        format = "%17.2f";
        String strFlow = "";
        if (bond.error.hasErrorYn == 'Y') {
            strFlow = bond.error.getErrorMessage();
            bond.error.clearError();
            return strFlow;
        } else {
            if (bond.fileOutFormat.equals("CSV")) {
                sGap = ",";
                format = "%.4f";
                fmtDate = "%s";
                fmtDow = "%s";
                fmtPerLen = "%.3f";
                fmtYears = "%.3f";
                headerTitle += "," + noFlows;
                if (calcNo == CalcList.CASHFLOW) {
                    columnHeading = "Date,DayOfWk,Periods,Years,Total Cash,Total Prin,Sched Prin,Prepay Prin,Interest,Fed Taxes,State Taxes";
                    columnUnderLine = "----------,-------,--------,--------,-----------------,-----------------,-----------------,-----------------,-----------------,------------,------------";
                } else {
                    columnHeading = "Date,Years,Total Cash,Total Prin,Interest,Fed Taxes,State Taxes";
                    columnUnderLine = "----------,--------,-----------------,-----------------,-----------------,------------,------------";
                }
                footerLine = "Totals,";
            } else {
                sGap = "   ";
                headerTitle += "=" + noFlows;
                if (calcNo == CalcList.CASHFLOW) {
                    columnHeading = "   Date      DayOfWk     Period      Years          Total Cash          Total Prin          Sched Prin         Prepay Prin            Interest       Federal Taxes        State Taxes   ";
                    columnUnderLine = "----------   -------   --------   --------   -----------------   -----------------   -----------------   -----------------   -----------------   -----------------   -----------------";
                } else {
                    columnHeading = "   Date        Years          Total Cash          Total Prin           Interest         Federal Taxes        State Taxes   ";
                    columnUnderLine = "----------   --------   -----------------   ----------------    -----------------   -----------------   -----------------";
                }
                footerLine = "Totals                                       ";
            }
            int flowNo = -1;

            double totCashflow = 0.;
            double totPrinflow = 0.;
            double totSchedflow = 0.;
            double totPrepayflow = 0.;
            double totIntflow = 0.;
            double totFedTax = 0.;
            double totStateTax = 0.;
            String strItem = "";

            while (++flowNo < noFlows) {
                totCashflow += cashflowList.get(flowNo).totCashflow;
                totPrinflow += cashflowList.get(flowNo).totPrinCash;
                totSchedflow += cashflowList.get(flowNo).schedPrinCash;
                totPrepayflow += cashflowList.get(flowNo).prePaycash;
                totIntflow += cashflowList.get(flowNo).intCash;
                totFedTax += cashflowList.get(flowNo).fedTaxPaid;
                totStateTax += cashflowList.get(flowNo).stateTaxPaid;
            }
            double total1 = totCashflow;
            double total2 = totPrinflow;
            double total3 = totSchedflow;
            double total4 = totPrepayflow;
            double total5 = totIntflow;
            double total6 = totFedTax;
            double total7 = totStateTax;
            strFlow = headerTitle + "\n";
            strFlow += columnHeading + "\n";
            strFlow += columnUnderLine + "\n";

            flowNo = -1;
            while (++flowNo < noFlows) {
                Dates dtCashflow = cashflowList.get(flowNo).dtCashflow; //10 chars
                String sDate = dtCashflow.getDateString(dtCashflow, bond.dtFormat);
                strItem = sDate + sGap;
                strFlow = strFlow + strItem;
                if (calcNo == CalcList.CASHFLOW) {
                    strItem = String.format(fmtDow, cashflowList.get(flowNo).strDayOfWeek) + sGap;
                    strFlow = strFlow + strItem;
                    strItem = String.format(fmtPerLen, cashflowList.get(flowNo).perLen) + sGap;
                    strFlow = strFlow + strItem;
                }
                strItem = String.format(fmtYears, cashflowList.get(flowNo).years) + sGap;
                strFlow = strFlow + strItem;
                strItem = String.format(format, cashflowList.get(flowNo).totCashflow) + sGap;
                strFlow = strFlow + strItem;
                strItem = String.format(format, cashflowList.get(flowNo).totPrinCash) + sGap;
                strFlow = strFlow + strItem;
                if (calcNo == CalcList.CASHFLOW) {
                    strItem = String.format(format, cashflowList.get(flowNo).schedPrinCash) + sGap;
                    strFlow = strFlow + strItem;
                    strItem = String.format(format, cashflowList.get(flowNo).prePaycash) + sGap;
                    strFlow = strFlow + strItem;
                }
                strItem = String.format(format, cashflowList.get(flowNo).intCash) + sGap;
                strFlow = strFlow + strItem;
                strItem = String.format(format, cashflowList.get(flowNo).fedTaxPaid) + sGap;
                strFlow = strFlow + strItem;
                strItem = String.format(format, cashflowList.get(flowNo).stateTaxPaid) + sGap;
                strFlow = strFlow + strItem + "\n";
            }
            strFlow = strFlow.concat(columnUnderLine);
            strFlow = strFlow.concat("\n" + footerLine);
            strItem = String.format(format, total1) + sGap;
            strFlow = strFlow + strItem;
            strItem = String.format(format, total2) + sGap;
            strFlow = strFlow + strItem;
            strItem = String.format(format, total3) + sGap;
            strFlow = strFlow + strItem;
            strItem = String.format(format, total4) + sGap;
            strFlow = strFlow + strItem;
            strItem = String.format(format, total5) + sGap;
            strFlow = strFlow + strItem;
            strItem = String.format(format, total6) + sGap;
            strFlow = strFlow + strItem;
            strItem = String.format(format, total7) + sGap;
            strFlow = strFlow + strItem;
            // strFlow = strFlow.concat("\n");
        }
        return strFlow;
    }

    public String outputAccreAmortSchedule(int calcNo, int result) {
        int flowNo = -1;
        String strFlow = "";
        String strFlowOut = "";
        if (bond.error.hasErrorYn == 'Y') {
            strFlow = bond.error.getWarningMessage();
            bond.error.clearError();
            return strFlow;
        }
        Dates dtStart = new Dates();
        Dates dtEnd = new Dates();
        if (bond.error.hasErrorYn == 'Y') {
            strFlow = bond.error.getErrorMessage();
            return strFlow;
        }
        if (commandLine.isInputJSONYn.equals("Y"))
            strFlow = outputAccreAmortScheduleJSON(calcNo, calcNo, result);
        else
            strFlow = outputAccreAmortScheduleString(calcNo, calcNo, result);

        return strFlow;
    }

    public String outputAccreAmortScheduleJSON(int noCalc, int calcNo, int result) {
        int flowNo = -1;
        String strFlow = "", strVal, format, sFormat = "%,15.2f";
        String strItem;
        Dates dtStart = new Dates();
        Dates dtEnd = new Dates();
        if (UnAvails.isUnAvail(bond.sfigAccrual)) {
            format = "%.2f";
            sFormat = "%17.2f   ";
        } else {
            format = "%." + bond.sfigAccrual + "f";
            sFormat = "%17." + bond.sfigAccrual + "f   ";
        }
        if (noCalc == 0)
            strFlow = "{";
        strFlow += "\"" + calcDef.calcDefs.get(CalcList.ACCREAMORTSCHED).calcName + "\"" + ":[";
        while (++flowNo < calcResults.accreAmortResults.count) {
            strItem = setItemName(calcResults.accreAmortSchedNames[0], calcResults.accreAmortScheduleList.get(flowNo).dtStart, ",");
            strFlow = strFlow + "{" + strItem;

            //strFlow = strFlow + "{" + "\"" + accreAmortSchedNames[0] + "\"" + ":" + accreAmortScheduleList.get(flowNo).dtStart.getDateString() + "'";
            strItem = setItemName(calcResults.accreAmortSchedNames[1], calcResults.accreAmortScheduleList.get(flowNo).dtEnd, ",");
            strFlow = strFlow + strItem;

            strItem = setItemName(calcResults.accreAmortSchedNames[2], calcResults.accreAmortScheduleList.get(flowNo).bkStart, ",", format);
            strFlow = strFlow + strItem;

            strItem = setItemName(calcResults.accreAmortSchedNames[3], calcResults.accreAmortScheduleList.get(flowNo).bkEnd, ",", format);
            strFlow = strFlow + strItem;

            strItem = setItemName(calcResults.accreAmortSchedNames[4], calcResults.accreAmortScheduleList.get(flowNo).accreAmort, ",", format);
            strFlow = strFlow + strItem;

            strItem = setItemName(calcResults.accreAmortSchedNames[5], calcResults.accreAmortScheduleList.get(flowNo).dailyAccreAmort, ",", format);
            strFlow = strFlow + strItem;

            if (flowNo == calcResults.accreAmortResults.count - 1)
                strItem = setItemName(calcResults.accreAmortSchedNames[6], calcResults.accreAmortScheduleList.get(flowNo).days, "}]", "%d");

            else
                strItem = setItemName(calcResults.accreAmortSchedNames[6], calcResults.accreAmortScheduleList.get(flowNo).days, "},", "%d");
            strFlow = strFlow + strItem;
        }
        if (bond.fileOutYn == 'Y')
            printAccreAmortSchedule(sFormat);
        return strFlow.trim();
    }


    public String outputAccreAmortScheduleString(int noCalc, int calcNo, int result) {
        int flowNo = -1;
        String strFlowOut, format, sFormat;
        Dates dtStart = new Dates();
        Dates dtEnd = new Dates();
        if (UnAvails.isUnAvail(bond.sfigAccrual)) {
            format = calcDef.calcDefs.get(CalcList.ACCREAMORTSCHED).format;
            sFormat = "%,17.2f" + "   ";
        } else {
            format = "%,17." + bond.sfigAccrual + "f   ";
            sFormat = format + "  ";
        }
        String strFlow = calcDef.calcDefs.get(calcNo).calcName + "=" + calcResults.accreAmortResults.count + ":";
        while (++flowNo < calcResults.accreAmortResults.count) {
            strFlow = strFlow + calcResults.accreAmortSchedNames[0] + "=" + calcResults.accreAmortScheduleList.get(flowNo).dtStart.getDateString() + ";";
            strFlow = strFlow + calcResults.accreAmortSchedNames[1] + "=" + calcResults.accreAmortScheduleList.get(flowNo).dtEnd.getDateString() + ";";
            strFlow = strFlow + calcResults.accreAmortSchedNames[2] + "=" + String.format(format, calcResults.accreAmortScheduleList.get(flowNo).bkStart) + ";";
            strFlow = strFlow + calcResults.accreAmortSchedNames[3] + "=" + String.format(format, calcResults.accreAmortScheduleList.get(flowNo).bkEnd) + ";";
            strFlow = strFlow + calcResults.accreAmortSchedNames[4] + "=" + String.format(format, calcResults.accreAmortScheduleList.get(flowNo).accreAmort) + ";";
            strFlow = strFlow + calcResults.accreAmortSchedNames[5] + "=" + String.format(format, calcResults.accreAmortScheduleList.get(flowNo).dailyAccreAmort) + ";";
            strFlow = strFlow + calcResults.accreAmortSchedNames[6] + "=" + calcResults.accreAmortScheduleList.get(flowNo).days + ";";
        }

        if (bond.fileOutYn == 'Y')
            printAccreAmortSchedule(sFormat);

        return strFlow.trim();
    }

    public boolean printAccreAmortSchedule(String sFormat) {
        String strFlowOut;
        int flowNo = -1;
        fileFormatter.format("\n");
        fileFormatter.format("AccreAmorts=%d\n", calcResults.accreAmortResults.count);
        fileFormatter.format("%s\n", "     Start Dt        End Dt          Book Start            Book End         Accre/Amort   Daily Accre/Amort      Days");
        fileFormatter.format("%s\n", "____________   ____________   _________________   _________________   _________________   _________________   _______");

        while (++flowNo < calcResults.accreAmortResults.count) {
            //cashflow = cashflowResults.sumTotCashflow;
            String sDate = calcResults.accreAmortScheduleList.get(flowNo).dtStart.getDateString();
            strFlowOut = String.format("%12s   ", sDate);
            strFlowOut = strFlowOut.concat(String.format("%12s   ", calcResults.accreAmortScheduleList.get(flowNo).dtEnd.getDateString()));
            strFlowOut = strFlowOut.concat(String.format(sFormat, calcResults.accreAmortScheduleList.get(flowNo).bkStart));
            strFlowOut = strFlowOut.concat(String.format(sFormat, calcResults.accreAmortScheduleList.get(flowNo).bkEnd));
            strFlowOut = strFlowOut.concat(String.format(sFormat, calcResults.accreAmortScheduleList.get(flowNo).accreAmort));
            strFlowOut = strFlowOut.concat(String.format(sFormat, calcResults.accreAmortScheduleList.get(flowNo).dailyAccreAmort));
            strFlowOut = strFlowOut.concat(String.format("%7d", calcResults.accreAmortScheduleList.get(flowNo).days));
            fileFormatter.format("%s\n", strFlowOut);
        }
        fileFormatter.format("%s\n", "____________   ____________   _________________   _________________   _________________   _________________   _______");
        fileFormatter.format("Totals                                                                %,17.2f                          %d\n",
                calcResults.accreAmortResults.sumAccreAmort, calcResults.accreAmortResults.sumDays);
        return true;

    }

    public String outputFutRiskConsole(int calcNo, FutureValueResults futureValueResults, int sigCurrency) {
        String calcStr = "";
        String sVal = "";
        String name = "", dnName = "", upName = "";
        if (calcNo == CalcList.FUTRETURNRISK) {
            name = CalcList.futRtnName;
            dnName = CalcList.futRtnDnName;
            upName = CalcList.futRtnUpName;
        } else {
            name = CalcList.futValName;
            dnName = CalcList.futValDnName;
            upName = CalcList.futValUpName;
        }
        String fmtStr = "%." + sigCurrency + "f";
        sVal = String.format(fmtStr, futureValueResults.futVal);
        String strItem = commandLine.consoleQuote + name + commandLine.consoleQuote + commandLine.consoleParamDelimit + sVal + commandLine.consoleCalcDelimit;
        calcStr = strItem;
        if (bond.error.hasErrorYn == 'Y') {
            strItem = bond.error.getErrorMessage();
            bond.error.clearError();
            return strItem;
        }
        sVal = String.format(fmtStr, futureValueResults.futValDn);
        strItem = commandLine.consoleQuote + dnName + commandLine.consoleQuote + commandLine.consoleParamDelimit + sVal + commandLine.consoleCalcDelimit;
        calcStr = calcStr + strItem;
        sVal = String.format(fmtStr, futureValueResults.futValUp);
        strItem = commandLine.consoleQuote + upName + commandLine.consoleQuote + commandLine.consoleParamDelimit + sVal;
        calcStr = calcStr + strItem;
        return calcStr;
    }

    public String outputFutRiskFile(int calcNo, FutureValueResults futureValueResults, int sfigCurrency) {
        String calcStr = "";
        String fmtStr = "%." + sfigCurrency + "f";
        String sVal = String.format(fmtStr, futureValueResults.futVal);
        String name = "", dnName = "", upName = "";
        if (calcNo == CalcList.FUTUREVALUERISK) {
            name = CalcList.futValName;
            dnName = CalcList.futValDnName;
            upName = CalcList.futValUpName;
        } else {
            name = CalcList.futRtnName;
            dnName = CalcList.futRtnDnName;
            upName = CalcList.futRtnUpName;
        }

        String strItem = name + commandLine.fileParamDelimit + sVal + commandLine.fileCalcDelimit;
        calcStr = strItem;
        if (bond.error.hasErrorYn == 'Y') {
            strItem = bond.error.getErrorMessage();
            bond.error.clearError();
            return strItem;
        }

        sVal = String.format(fmtStr, futureValueResults.futValDn);
        strItem = dnName + commandLine.fileParamDelimit + sVal + commandLine.fileCalcDelimit;
        calcStr = calcStr + strItem;
        sVal = String.format(fmtStr, futureValueResults.futValUp);
        strItem = upName + commandLine.fileParamDelimit + sVal;
        calcStr = calcStr + strItem;
        return calcStr;
    }

    public String convertToFileString(int calcNo, String nameDelimiter, String sCalcResult) {
        String fileStr = "";
        if (calcDef.calcDefs.get(calcNo).arrayYn.equals("Y"))
            return sCalcResult;
        if (commandLine.isInputJSONYn.equals("Y"))
            fileStr = sCalcResult.replace(":", nameDelimiter);
        else
            fileStr = sCalcResult.replace(";", "\n");
        return fileStr;
    }


    String setItemName(String itemName, double value, String comma, String format) {
        String itemString;
        itemString = "\"" + itemName + "\"" + ":" + String.format(format, value) + comma;
        return itemString;
    }

    String setItemName(String itemName, Dates dtValue, String comma) {
        String itemString;
        itemString = "\"" + itemName + "\"" + ":" + dtValue.getDateString() + comma;
        return itemString;
    }

    String setItemName(String itemName, int iVal, String comma, String format) {
        String itemString;
        itemString = "\"" + itemName + "\"" + ":" + String.format(format, iVal) + comma;
        return itemString;
    }

    String setItemName(String itemName, char cVal, String comma) {
        String itemString;
        itemString = "\"" + itemName + "\"" + ":" + cVal + comma;
        return itemString;
    }

    String setItemName(String itemName, String sVal, String comma) {
        String itemString;
        itemString = "\"" + itemName + "\"" + ":" + sVal + comma;
        return itemString;
    }

    public double zSpread(int calcNo, int securityNo, String instype, Dates dtSettle, double settlePrc) {
        // get cashflow
        int count;

        double estSpread = 0., minSpread = -10., maxSpread = 10;
        double cashScale = 1.;
        double zSprd = 0.;
        cashflowList = cashflow(calcNo, cashScale, securityNo, instype, dtSettle, bond.dtRedeem);
        bond.error.setCalcName(calcDef.calcDefs.get(calcNo).calcName);
        zSprd = getZeroSpreadRoot(calcNo, dtSettle, settlePrc, estSpread, minSpread, maxSpread);
        //zeroPrc = zeroPrice(dtSettle, bond.discCurve);
        return zSprd * calcDef.calcDefs.get(CalcList.ZSPREAD).scale;
    }

    private double getTaxIntPayment(Dates dtCashflow, double taxRt, double intInc) {
        SchedTrack taxTrack = new SchedTrack();
        taxTrack = bond.schedTracks.getSchedTrackOn(dtCashflow, SchedTrack.FEDTAXPAYMENTTRACK);
        if (taxTrack.found && taxTrack.schedDate.leDate(taxTrack.schedDate, bond.dtMaturity))
            return taxRt * intInc;
        return 0.;
    }

    private double getTaxCapGainLossPayment(Dates dtCashflow, double prinPay, Dates dtPur, double shortCapGlRt, double longCapGLRt) {
        double taxPay = 0.;
        Dates dtPay = new Dates();
        SchedTrack taxTrack = bond.schedTracks.getSchedTrackOn(dtCashflow, SchedTrack.FEDTAXPAYMENTTRACK);
        if (taxTrack.found && taxTrack.schedDate.leDate(taxTrack.schedDate, bond.dtMaturity)) {
            int daysHeld = dayCount.getDaycountDCACT(dtPur, dtPur);
            double prinFac = (bond.redeemPrin - bond.purPrc);
            if (daysHeld > 365)
                taxPay = prinFac * prinPay * longCapGLRt;
            else
                taxPay = prinFac * prinPay * shortCapGlRt;

        }
        return taxPay;
    }

    public double getZeroSpreadRoot(int calcNo, Dates dtSettle, double settlePrc, double estSpread, double minSpread, double maxSpread) {
        int it = 0;
        String strSpread;
        Double zSpread = 0.;
        CalcMethod.RootResult rootResult = new CalcMethod.RootResult();

        double func = 0., dFunc = 0, deltaFunc = 0., zSprd = 0.;
        for (it = 0; it < CalcEnums.MAXROOTITERATIONS; ++it) {
            rootResult = getSpreadRootTrial(calcNo, dtSettle, settlePrc, estSpread);
            func = rootResult.func;
            dFunc = rootResult.dFunc;
            deltaFunc = func / dFunc;
            if (dFunc == 0.) {
                String errmsg = "Cannot calculate Z-Spread because the derivative of the spread price function is zero";
                bond.error.setError(getClass().getName(), errmsg);
                return 0.;
            }
            estSpread -= deltaFunc;
            if (estSpread < minSpread || estSpread > maxSpread) {
                zSprd = estSpread * 100.;
                zSpread = zSprd;
                strSpread = zSpread.toString();
                bond.error.setError(getClass().getName(), "Z-Spread found (" + strSpread + ") is out of reasonable bounds");
                return 0.;
            }
            if (Math.abs(func) <= CalcEnums.ROOTPRECISION)
                break;
        }
        if (it > CalcEnums.MAXROOTITERATIONS) {
            zSprd = estSpread;
            strSpread = zSpread.toString();
            bond.error.setError(getClass().getName(), "Z-Spread found (" + strSpread + ") is best Z-Spread found within maximun allowed trials");
            return 0.;
        }
        return estSpread;
    }

    public CalcMethod.RootResult getSpreadRootTrial(int calcNo, Dates dtSettle, double settlePrc, double estSpread) {
        double func = -settlePrc, func1 = -settlePrc, dFunc = 0., pv = 0., presVal = 0., deltaYield = .000001;
        CalcMethod.RootResult rootResult = new CalcMethod.RootResult();
        int cNo = -1;
        while (++cNo < cashflowList.size()) {
            double cashflow = cashflowList.get(cNo).totCashflow;
            Dates dtCashflow = cashflowList.get(cNo).dtCashflow;
            double noPer = bond.schedTracks.getCouponFactor(calcNo, dtSettle, dtCashflow);
            double matLen = CalUnits.MONTHSPERYEAR * dayCount.time.getYearsBetweenDates(dtSettle, dtCashflow);
            double discRate = bond.discCurve.getSpotRate(matLen, bond.yieldCurveInterpMeth);
            double discFac = 1. / (1. + (discRate + estSpread) / bond.intSchedRule.compoundFreq);
            discFac = Math.pow(discFac, noPer);
            pv = cashflow * discFac;
            double discRate1 = discRate + deltaYield;
            double discFac1 = 1. / (1. + (discRate1 + estSpread) / bond.intSchedRule.compoundFreq);
            discFac1 = Math.pow(discFac1, noPer);
            double pv1 = cashflow * discFac1;
            func += pv;
            func1 += pv1;
        }
        rootResult.func = func;
        rootResult.dFunc = (func1 - func) / deltaYield;
        return rootResult;
    }


    class CashflowInterval {
        double cumIntash;
        double cumPrincash;

        CashflowInterval() {
            cumIntash = 0.;
            cumPrincash = 0.;
        }

        public void setCashflowInterval(double cumIntAccrual, double cumPrinPay) {
            this.cumIntash = cumIntAccrual;
            this.cumPrincash = cumPrinPay;
        }


        public void addCashflowInterval(double cumIntcash, double cumPrinPay) {
            this.cumIntash += cumIntcash;
            this.cumPrincash += cumPrinPay;
        }
    }

    public Attribution calcAttribution(int calcNo, int securityNo, String instype, Dates dtFuture, Dates dtRedeem, double redeemPrin, double redeemInt) {
        double rtnRfAge = 0., rtnSprdAge = 0., rtnRfFut = 0., rtnSprdFut = 0.;
        double pvRfAge = 0., pvSprdAge = 0., pvRfFut = 0., pvSprdFut = 0.;
        Attribution attribution = new Attribution();
        cashflowList = cashflow(calcNo, 1., securityNo, instype, dtFuture, bond.dtRedeem);
        // cumulate cashflowa
        int noFlows = cashflowList.size();
        int freq = bond.intSchedRule.compoundFreq;
        int ic = -1;
        double cflow = 0.;
        double scale = calcDef.calcDefs.get(CalcList.ATTRIBUTION).scale;
        while (++ic < noFlows) {
            cflow = cashflowList.get(ic).totCashflow;
            Dates dtFlow = cashflowList.get(ic).dtCashflow;
            int matLenMos = bond.dayCount.time.getMonthsBetween(bond.dtFuture, dtFlow, false);
            pvRfAge += calcCurveAttribution(bond.discRfCurve, bond.yieldCurveInterpMeth, cflow, matLenMos, freq);
            pvSprdAge += calcCurveAttribution(bond.discSprdCurve, bond.yieldCurveInterpMeth, cflow, matLenMos, freq);
            pvRfFut += calcCurveAttribution(bond.discFutRfCurve, bond.yieldCurveInterpMeth, cflow, matLenMos, freq);
            pvSprdFut += calcCurveAttribution(bond.discFutSprdCurve, bond.yieldCurveInterpMeth, cflow, matLenMos, freq);
        }
        double noPer = bond.intSchedRule.getCouponFactor(bond.dtSettle, bond.dtFuture);
        //
        double pvBond = bond.mktPrcFuture - pvRfFut - pvSprdFut;
        double mktPrc2 = bond.mktPrcStart * bond.mktPrcStart;
        rtnRfAge = 0.;
        if (pvRfAge > 0.)
            rtnRfAge = scale * freq * (-1 + Math.pow(pvRfAge / bond.mktPrcStart, 1. / noPer));
        else if (pvRfAge < 0.) {
            double pvRfAge2 = pvRfAge * pvRfAge;
            rtnRfAge = scale * freq * (-1 + Math.pow(pvRfAge2 / mktPrc2, 1. / 2 * noPer));
        }
        rtnSprdAge = 0.;
        if (pvSprdAge > 0.)
            rtnSprdAge = scale * freq * (-1 + Math.pow(pvSprdAge / bond.mktPrcStart, 1. / noPer));
        else if (pvSprdAge < 0.) {
            double pvSprdAge2 = pvSprdAge * pvSprdAge;
            rtnSprdAge = scale * freq * (-1 + Math.pow(pvSprdAge2 / mktPrc2, 1. / 2 * noPer));
        }

        rtnRfFut = 0.;
        if (pvRfFut > 0.)
            rtnRfFut = scale * freq * (-1 + Math.pow(pvRfFut / bond.mktPrcStart, 1. / noPer));
        else if (pvRfFut < 0.) {
            double pvFutRf2 = pvRfFut * pvRfFut;
            rtnRfFut = scale * freq * (-1 + Math.pow(pvFutRf2 / mktPrc2, 1. / 2 * noPer));
        }

        rtnSprdFut = 0.;
        if (pvSprdFut > 0.)
            rtnSprdFut = scale * freq * (-1 + Math.pow(pvSprdFut / bond.mktPrcStart, 1. / noPer));
        else if (pvSprdFut < 0.) {
            double pvSprdFut2 = pvSprdFut * pvSprdFut;
            rtnSprdFut = scale * freq * (-1 + Math.pow(pvSprdFut2 / mktPrc2, 1. / 2 * noPer));
        }

        double ratio = bond.mktPrcFuture / bond.mktPrcStart;
        double totRtn = scale * freq * (-1 + Math.pow(ratio, 1. / noPer));
        double totRtnAttr = rtnRfFut + rtnSprdFut;
        double rtnBond = totRtn - totRtnAttr;

        attribution.rtnTime = rtnRfAge + rtnSprdAge;
        attribution.rtnRates = rtnRfFut - rtnRfAge;
        attribution.rtnCrdSprd = rtnSprdFut - rtnSprdAge;
        attribution.rtnSecurity = rtnBond;
        attribution.totRtn = totRtn;
        attribution.mktValue = bond.mktPrcFuture * bond.faceAmt;
        return attribution;
    }

    public String outputAtrributionConsole(int calcNo, Attribution attribution, int sfigStatistic) {
        CommandLine cl = commandLine;
        Formatter formatter = new Formatter();
        if (bond.error.hasErrorYn == 'Y')
            return bond.error.errorMessage;
        String strCalc = "";
        String fmtStr = "%.2f";
        String strItem = String.format(fmtStr, attribution.totRtn);
        strCalc += cl.consoleQuote + CalcList.attrRtnName + cl.consoleQuote + cl.consoleParamDelimit + strItem + cl.consoleCalcDelimit;

        strItem = String.format(fmtStr, attribution.rtnTime);
        strCalc += cl.consoleQuote + CalcList.attrAgingRtnName + cl.consoleQuote + cl.consoleParamDelimit + strItem + cl.consoleCalcDelimit;

        strItem = String.format(fmtStr, attribution.rtnRates);
        strCalc += cl.consoleQuote + CalcList.attrRiskFreeRtnName + cl.consoleQuote + cl.consoleParamDelimit + strItem + cl.consoleCalcDelimit;

        strItem = String.format(fmtStr, attribution.rtnCrdSprd);
        strCalc += cl.consoleQuote + CalcList.attrCreditSprdRtnName + cl.consoleQuote + cl.consoleParamDelimit + strItem + cl.consoleCalcDelimit;

        strItem = String.format(fmtStr, attribution.rtnSecurity);
        strCalc += cl.consoleQuote + CalcList.attrSelectionRtnName + cl.consoleQuote + cl.consoleParamDelimit + strItem;
        return strCalc;
    }

    public String outputAtrributionFile(int calcNo, Attribution attribution, int sfigStatistic) {
        CommandLine cl = commandLine;
        Formatter formatter = new Formatter();
        if (bond.error.hasErrorYn == 'Y')
            return bond.error.errorMessage;
        String strCalc = "";
        String fmtStr = "%." + sfigStatistic + "f";
        String strItem = String.format(fmtStr, attribution.totRtn);
        strCalc += CalcList.attrRtnName + cl.fileParamDelimit + strItem + cl.fileCalcDelimit;

        strItem = String.format(fmtStr, attribution.rtnTime);
        strCalc += CalcList.attrAgingRtnName + cl.fileParamDelimit + strItem + cl.fileCalcDelimit;

        strItem = String.format(fmtStr, attribution.rtnRates);
        strCalc += CalcList.attrRiskFreeRtnName + cl.fileParamDelimit + strItem + cl.fileCalcDelimit;

        strItem = String.format(fmtStr, attribution.rtnCrdSprd);
        strCalc += CalcList.attrCreditSprdRtnName + cl.fileParamDelimit + strItem + cl.fileCalcDelimit;

        strItem = String.format(fmtStr, attribution.rtnSecurity);
        strCalc += CalcList.attrSelectionRtnName + cl.fileParamDelimit + strItem + cl.fileCalcDelimit;
        return strCalc;
    }

    // Returns the present value
    private double calcCurveAttribution(YieldCurves yieldCurves, String curveInterpMeth, double cashFlow, int matLenMos, int freq) {
        if (yieldCurves.spotCurve.isEmpty())
            return 0.;
        double rt = yieldCurves.getSpotRate(matLenMos, curveInterpMeth);
        double noPer = (double) matLenMos / (CalUnits.MONTHSPERYEAR / freq);
        double discFac = Math.pow(1. + rt / freq, noPer);
        double pv = 0.;
        if (discFac != 0.) {
            pv = cashFlow / discFac;
        }
        return pv;
    }

    public double calcFairPrice(int calcNo, int securityNo, String instype) {
        double prc = 0.;
        int noFlows;
        // security specific cashfloq lives in CalacResult cashglow result list
        cashflowList = cashflow(calcNo, 1., securityNo, instype, bond.dtSettle, bond.dtRedeem);
        // cumulate cashflowa
        noFlows = cashflowList.size();
        int ic = -1;
        double cflow = 0.;
        while (++ic < noFlows) {
            cflow = cashflowList.get(ic).totCashflow;
            Dates dtFlow = cashflowList.get(ic).dtCashflow;
            double matLenYrs = dayCount.getYearsBetweenDates(bond.dtSettle, dtFlow);
            double noPer = matLenYrs * bond.intSchedRule.compoundFreq;
            double matLenMo = matLenYrs * CalUnits.MONTHSPERYEAR;
            double rate = bond.discCurve.getSpotRate(matLenMo, bond.yieldCurveInterpMeth);
            double discFac = Math.pow(1. + rate / bond.intSchedRule.compoundFreq, noPer);
            double presVal = cflow / discFac;
            prc += presVal;
        }
        prc *= calcDef.calcDefs.get(calcNo).scale;
        return prc;
    }

    public boolean isInCalculation(int calcNo, ArrayList<Integer> calculationList) {
        return calculationList.contains(calcNo);
    }

    ArrayList<CashflowResults> getNextcashflow(ArrayList<CashflowResults> cashflowList, Dates dtAdvance) {
        ArrayList<CashflowResults> nextCashflowList = new ArrayList<>();
        CashflowResults cashflowResults;
        int no = -1, nx = 0;
        int noFlows = cashflowList.size();
        while (++no < noFlows) {
            if (cashflowList.get(no).dtCashflow.gtDate(dtAdvance)) {
                cashflowResults = cashflowList.get(no);
                double years = dayCount.getYearsBetweenDates(dtAdvance, cashflowResults.dtCashflow);
                Dates dtCashflow = cashflowResults.dtCashflow;
                double perLen = bond.intSchedRule.getCouponFactor(dtAdvance, dtCashflow);
                int matLen = dayCount.getMonthCount(dtAdvance, dtAdvance, 'Y');
                cashflowResults.years = years;
                cashflowResults.perLen = perLen;
                cashflowResults.matLen = matLen;
                nextCashflowList.add(nx++, new CashflowResults(cashflowList.get(no)));
            }
        }
        return nextCashflowList;
    }

    public double sharpRatioStdDev(int calcNo, int securityNo, String instype, Dates dtStart, String yieldType, int analysisPeriod, YieldCurves allYieldCurves) {
        double sharpRatio = 0.;
        Vector<Double> rateRtns = new Vector<Double>();
        String spotFileName = bond.commandLine.rootDirectory + "yieldCurves\\" + BondConsts.SPOTCURVEVOLFILENAME;
        Dates dtLast = dtStart;
        Dates dtNext;
        ArrayList<CashflowResults> cashflowLast;
        ArrayList<CashflowResults> cashflowNext;
        YieldCurves yieldCurvesLast = new YieldCurves();
        YieldCurveParams yldParams = new YieldCurveParams();
        yldParams.setRates(bond.noDiscRate, bond.discRt, bond.discTenor, bond.discRt, bond.discStdDev, bond.discRtDn, bond.discRtUp, bond.discRtDn, bond.discRtUp);
        yldParams.setCredit(yieldType, spotFileName, bond.creditAgency, bond.creditRating, "NA");
        yldParams.setTypes(commandLine, 'Y', 'N', 'Y', 'N');
        yldParams.setDates(dtLast, dtLast, dtLast);
        yldParams.setOptions(bond.yieldCurveInterpMeth, BondConsts.SPOTINTERVAl, dtLast);
        Dates dtCalc = new Dates();
        dtCalc.setDate(2024, 9, 11);
        if (yldParams.dtCurve.eqDate(dtCalc))
            sharpRatio = 0.;
        yieldCurvesLast.spotCurve = allYieldCurves.getDiscSpotCurve(yldParams);
        if (error.hasErrorYn == 'Y') {
            return sharpRatio;
        }
        cashflowList = cashflow(calcNo, 1., securityNo, instype, dtLast, bond.dtRedeem);
        cashflowLast = cashflowList;
        double futValueLast = futureValue(calcNo, securityNo, instype, dtStart, cashflowLast, yieldCurvesLast);
        Dates dtEnd = dayCount.time.addDaysToDate(dtStart, analysisPeriod, 'N');
        double perLen = Defaults.RETURNINTERVAL / CalUnits.SEMIANNUALREPEAT;
        double freq = CalUnits.SEMIANNUALFQ;
        double totRtn = 0.;
        while (dtLast.leDate(dtEnd)) {
            if (dtLast.year == 2024 && dtLast.month == 1 && dtLast.day > 25)
                totRtn = totRtn;
            dtNext = dayCount.time.addDaysToDate(dtLast, Defaults.RETURNINTERVAL, 'N');
            cashflowNext = getNextcashflow(cashflowList, dtNext);
            yldParams.dtCurve = dtNext;
            YieldCurves yieldCurvesNext = new YieldCurves();
            yieldCurvesNext.spotCurve = allYieldCurves.getDiscSpotCurve(yldParams);
            if (error.hasErrorYn == 'Y') {
                return sharpRatio;
            }
            double futValueNext = futureValue(calcNo, securityNo, instype, dtNext, cashflowNext, yieldCurvesNext);
            if (error.hasErrorYn == 'Y') {
                return sharpRatio;
            }
            double rateRtnNext = freq * (-1. + Math.pow(futValueNext / futValueLast, 1. / perLen));
            rateRtns.add(rateRtnNext);
            totRtn += 100. * rateRtnNext;
            cashflowLast = cashflowNext;
            yieldCurvesLast = yieldCurvesNext;
            dtLast = dtNext;
        }
        double avgRt = totRtn / rateRtns.size();
        double stdDev = utility.calcStandardDeviation(avgRt, rateRtns);
        return stdDev;
    }

    public double futureValue(int calcNo, int securityNo, String instype, Dates dtFuture, ArrayList<CashflowResults> cashflowList, YieldCurves yieldCurves) {
        Dates dtCashflow = new Dates();
        double matLen, useYield = 0.;
        double totCash = 0., perCash = 0., remPer = 0.;
        int freq = CalUnits.SEMIANNUALFQ;
        double noPer = 0;
        switch (instype) {
            case "DISCOUNT":
                totCash = discount.futureValue(calcNo, securityNo, instype, bond.faceAmt, dtFuture, bond.dtRedeem, bond.redeemPrin);
                break;
            case "ATMATURITY":
                totCash = atMaturity.futureValue(calcNo, securityNo, instype, bond.faceAmt, dtFuture, bond.dtRedeem, bond.redeemPrin);
                break;
            default:
                double pvFactor = 0.;
                int noFlows = cashflowList.size();
                int ic = -1;
                while (++ic < noFlows) {
                    dtCashflow = cashflowList.get(ic).dtCashflow;
                    perCash = cashflowList.get(ic).totCashflow;
                    remPer = bond.schedTracks.getCouponFactor(calcNo, dtFuture, dtCashflow);
                    matLen = CalUnits.MONTHSPERYEAR * bond.time.getYearsBetweenDates(dtFuture, dtCashflow);
                    if (bond.yieldForeCastMeth == CalcList.FORECASTFORWARD)
                        useYield = yieldCurves.getForwardRate(matLen, bond.yieldCurveInterpMeth);
                    else
                        useYield = yieldCurves.getSpotRate(matLen, bond.yieldCurveInterpMeth);
                    pvFactor = 0.;
                    if (useYield != -1)
                        pvFactor = 1. / Math.pow(1. + useYield / bond.intSchedRule.compoundFreq, remPer);
                    totCash += perCash * pvFactor;
                }
                if (dtFuture.gtDate(bond.dtRedeem)) {
                    matLen = dayCount.getMonthCount(bond.dtRedeem, dtFuture, 'Y');
                    freq = CalUnits.SEMIANNUALFQ;
                    noPer = matLen / freq; // no semi-annual period
                    if (bond.yieldForeCastMeth == CalcList.FORECASTFORWARD)
                        if (bond.yieldForeCastMeth == CalcList.FORECASTFORWARD)
                            useYield = yieldCurves.getForwardRate(matLen, bond.yieldCurveInterpMeth);
                        else
                            useYield = yieldCurves.getSpotRate(matLen, bond.yieldCurveInterpMeth);
                    pvFactor = 0.;
                    if (useYield != -1)
                        pvFactor = 1. / Math.pow(1. + useYield / freq, noPer);
                    double reinCash = totCash * Math.pow(1. + useYield / freq, noPer);
                    totCash += reinCash;
                }
                break;
        }
        //  accInt = accruedInterest(CalcList.ACCUREDINT, instype, dtFuture);
        accInt = getAccruedInterest(calcNo, instype, dtFuture);
        return totCash + accInt;

    }

    public String lastPayDate(int calcNo, int securityNo, Dates dtSettle) {
        String strDate = "NA";
        Dates dtLastPay;
        if (choices.discountList.contains(bond.instype)) {
            strDate = Codes.NOTAVAILSTRING;
            return strDate;
        }
        dtLastPay = bond.intSchedRule.getNextSchedDate(BondConsts.BEFOREONDATE, bond.dtSettle);
        strDate = dtLastPay.getDateString();
        return strDate;
    }

    public String nextPayDate(int calcNo, int securityNo, Dates dtSettle) {
        String strDate = "NA";
        Dates dtNextPay;
        if (choices.discountList.contains(bond.instype)) {
            strDate = bond.dtMaturity.getDateString();
            return strDate;
        }
        dtNextPay = bond.intSchedRule.getNextSchedDate(BondConsts.AFTERDATE, bond.dtSettle);
        strDate = dtNextPay.getDateString();
        return strDate;
    }

    public ArrayList<CashflowResults> combineCashflows(ArrayList<CashflowResults> cumCashflowList) {
        ArrayList<CashflowResults> netCashflowList = new ArrayList<>();
        CashflowResults incremCashflow = new CashflowResults();
        Collections.sort(cumCashflowList, new Comparator<CashflowResults>() {
            @Override
            public int compare(CashflowResults o1, CashflowResults o2) {
                if (o1.dtCashflow.gtDate(o2.dtCashflow))
                    return 1;
                if (o1.dtCashflow.leDate(o2.dtCashflow))
                    return -1;
                return 0;
            }
        });
        int noFlows = cumCashflowList.size();
        Dates dtLast = new Dates();
        dtLast.setDate(1900, 1, 1);
        Dates dtCurr = new Dates();
        int count = 0; // no row in appended cashflow
        dtLast = cumCashflowList.get(0).dtCashflow;
        incremCashflow.zero();
        int ic = -1;
        while (++ic < noFlows) {
            dtCurr = cumCashflowList.get(ic).dtCashflow;
            // is this cashflow date not a duplicate of last cashflow date
            // keep accumulating until we have a new grou[
            if (dtCurr.eqDate(dtCurr, dtLast)) { // skip adding a duplicate
                // cumulate casflow electemts
                incremCashflow.dtCashflow = cumCashflowList.get(ic).dtCashflow;
                incremCashflow.strDayOfWeek = cumCashflowList.get(ic).strDayOfWeek;
                incremCashflow.securityNo = cumCashflowList.get(ic).securityNo;
                incremCashflow.perLen = cumCashflowList.get(ic).perLen;
                incremCashflow.years = cumCashflowList.get(ic).years;
                incremCashflow.totCashflow += cumCashflowList.get(ic).totCashflow;
                incremCashflow.totPrinCash += cumCashflowList.get(ic).totPrinCash;
                incremCashflow.schedPrinCash += cumCashflowList.get(ic).schedPrinCash;
                incremCashflow.prePaycash += cumCashflowList.get(ic).prePaycash;
                incremCashflow.intCash += cumCashflowList.get(ic).intCash;
                incremCashflow.fedTaxPaid += cumCashflowList.get(ic).fedTaxPaid;
                incremCashflow.stateTaxPaid += cumCashflowList.get(ic).stateTaxPaid;
                dtLast = dtCurr;
            } else { // alway add what's left of{
                netCashflowList.add(count++, new CashflowResults(incremCashflow));
                // start new accumulator
                incremCashflow.zero();
                incremCashflow.dtCashflow = dtCurr;
                incremCashflow.strDayOfWeek = cumCashflowList.get(ic).strDayOfWeek;
                incremCashflow.securityNo = cumCashflowList.get(ic).securityNo;
                incremCashflow.perLen = cumCashflowList.get(ic).perLen;
                incremCashflow.years = cumCashflowList.get(ic).years;
                incremCashflow.totCashflow += cumCashflowList.get(ic).totCashflow;
                incremCashflow.totPrinCash += cumCashflowList.get(ic).totPrinCash;
                incremCashflow.schedPrinCash += cumCashflowList.get(ic).schedPrinCash;
                incremCashflow.prePaycash += cumCashflowList.get(ic).prePaycash;
                incremCashflow.intCash += cumCashflowList.get(ic).intCash;
                incremCashflow.fedTaxPaid += cumCashflowList.get(ic).fedTaxPaid;
                incremCashflow.stateTaxPaid += cumCashflowList.get(ic).stateTaxPaid;
                dtLast = dtCurr;
            }
        }
        netCashflowList.add(count++, new CashflowResults(incremCashflow));
        return netCashflowList;
    }

    private int getEndTrackNo(List<SchedTrack> trackList, Dates dtEnd) {
        int count = trackList.size();
        int no = count;
        while (--no >= 0) {
            if (trackList.get(no).schedDate.leDate(dtEnd))
                return no;
        }
        return 0;
    }

    private int getStartTrackNo(List<SchedTrack> trackList, Dates dtStart) {
        int no = -1;
        int count = trackList.size();
        while (++no < count) {
            if (trackList.get(no).schedDate.gtDate(dtStart))
                return no;
        }
        return no;
    }


}
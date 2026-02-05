package app;

import java.util.ArrayList;

public class Mortgage {
    Bond bond;
    Error error;
    DayCount dayCount;
    Choices choices;
    CashflowResults cashflowResults;
    ArrayList<CashflowResults> cashflowList = new ArrayList<>();
    // CalcResults calcResults;
    Taxes taxes;
    int psaMonth = 30;
    double psaSmmRtStart = .002;
    CashflowPayments cashflowPayments;

    CalcMethod calcMethod;

    /*
     * Initialize for mortgage issues
     **/
    Mortgage(Bond bond, CashflowResults cashflowResults) {
        this.bond = bond;
        this.error = new Error();
        this.choices = new Choices(error);
        this.dayCount = new DayCount(error);
        this.cashflowPayments = new CashflowPayments();
        this.cashflowResults = new CashflowResults();
        double totGl = bond.redeemPrin - bond.purPrc;

        // bondDefault = new BondDefault(bond);
    }

    // get the passthru conditions on date of user passthru factor
    // cashflow(calcNo, scale, instype, dtSettle, dtRedeem, redeemPrin, redeemInt);

    public ArrayList<CashflowResults> cashflow(int calcNo, double scale, int securityNo, Dates dtSettle, Dates dtRedeem,
            double redeemPrin, double redeemInt) {
        // get prepayspeed that gets factor starting at issue 1. to passthru factor on
        // factor date
        SchedTrack schedTrack = new SchedTrack();
        PassthruLevelPay passthruLevelPay = new PassthruLevelPay();
        char use360Yn = 'Y';
        Dates dtAccrue = new Dates();
        Dates dtEndAccrue = new Dates();
        Dates dtNextCpn = new Dates();
        Dates dtLastCpn = new Dates();
        Dates dtCashflow = new Dates();
        double intPay = 0., userIntPay = 0.;
        double schedPay = 0., userSchedPay = 0.;
        double prePay = 0., userPrepay = 0.;
        boolean usePrin = true;
        boolean useInt = true;
        double userPrin = 0.;
        double prinPay = 0.;
        double reinPay = 0.;
        double cfReinPay = 0.;
        double totPrin = 0.;
        double cfFedTax = 0.;
        double cfStateTax = 0.;
        cashflowList.removeAll(cashflowList);
        int noFlow = 0;
        if (bond.stripType.equals(BondConsts.STRIPPO))
            useInt = false;
        if (bond.stripType.equals(BondConsts.STRIPIO))
            usePrin = false;
        // start accrue date is the date to get to staring speed
        schedTrack = bond.schedTracks.getSchedTrackAfter(dtSettle, SchedTrack.INTERESTPAYTRACK);
        if (!schedTrack.found) {
            String errmsg = "cannot find next payment date for mortgage";
            bond.error.setError(getClass().getName(), errmsg);
            return null;
        }
        Dates dtMaturity = new Dates();
        dtMaturity = calcMaturityDate(calcNo, securityNo, dtSettle);
        dtNextCpn = schedTrack.schedDate;
        double remFac = bond.passFactor; // factor on pass dtPassfact
        Dates dtCalc = new Dates();
        passthruLevelPay = getLevelPayment(bond.dtPassFactor, bond.passFactor);
        double levelPay = passthruLevelPay.levelPay;
        double remLast = 0.;
        double totPerPay = 0.;
        while (remFac > 0.) { // start at user pass thru factor date
            intPay = remFac * bond.cpnRt / bond.accrueSchedRule.compoundFreq;
            if (useInt)
                userIntPay = remFac * (bond.cpnRt - bond.serviceRt) / bond.accrueSchedRule.compoundFreq;
            else
                userIntPay = 0.;
            schedPay = levelPay - intPay;
            if (usePrin)
                userSchedPay = schedPay;
            else
                userSchedPay = 0.;
            Dates dtCurrentAccrue = bond.accrueSchedRule.getNextSchedDate(BondConsts.BEFOREDATE, dtNextCpn);
            int perNo = (int) dayCount.time.getMonthsBetween(bond.dtDated, dtCurrentAccrue, true);
            double smmRt = getSmmRtPrepaySpeed(perNo, bond.passModel, bond.prepaySpeed);
            levelPay *= (1. - smmRt);
            if (usePrin)
                userPrepay = (remFac - userSchedPay) * smmRt;
            else
                userPrepay = 0.;
            userPrin = userSchedPay + userPrepay;
            prinPay = userPrin;
            remLast = remFac - userPrin;
            if (remLast < 0.) {
                userSchedPay = remFac;
                userPrepay = 0.;
                totPerPay = userSchedPay;
                remFac = 0.;
            } else
                remFac -= userPrin;
            totPerPay = userIntPay + userSchedPay + userPrepay;
            reinPay = 0.;
            if (choices.inReinvestCalcList(calcNo)) {
                reinPay += calcMethod.calcReinvestCash(calcNo, dtCurrentAccrue, dtRedeem, totPerPay);
            }

            SchedTrack taxTrack = new SchedTrack();
            taxTrack = bond.schedTracks.getSchedTrackOn(dtCurrentAccrue, SchedTrack.FEDTAXPAYMENTTRACK);
            double currFedTax = 0.;
            double currStateTax = 0.;
            if (taxTrack.found) {
                currFedTax = cfFedTax;
                currStateTax = cfStateTax;
                cfFedTax = 0.;
                cfStateTax = 0.;
            }
            if (remFac <= 0) {
                userIntPay += reinPay;
            }
            cashflowResults.securityNo = securityNo;
            cashflowResults.perLen = bond.intSchedRule.getCouponFactor(dtSettle, dtNextCpn);
            cashflowResults.strDayOfWeek = dtNextCpn.getDayOfWeek(dtNextCpn);
            cashflowResults.dtCashflow = dtNextCpn;
            cashflowResults.intCash = userIntPay;
            cashflowResults.schedPrinCash = userSchedPay;
            cashflowResults.prePaycash = userPrepay;
            cashflowResults.fedTaxPaid = currFedTax;
            cashflowResults.stateTaxPaid = currStateTax;
            cashflowResults.matLen = dayCount.getMonthCount(dtSettle, dtNextCpn, 'Y');
            cashflowResults.years = dayCount.getYearsBetweenDates(dtSettle, dtNextCpn);
            cashflowResults.totCashflow = userPrepay + userSchedPay + userIntPay + currFedTax + currStateTax;
            cashflowResults.totPrinCash = userPrepay + userSchedPay;
            cashflowResults.sumTotSchedCashflow += userSchedPay;
            cashflowResults.sumTotPrePayCashflow += userPrepay;
            cashflowResults.sumTotPrinCashflow += userPrepay + userSchedPay;
            cashflowResults.sumTotIntCashflow += userIntPay;
            cashflowResults.sumTotCashflow += userPrepay + userSchedPay + userIntPay + currFedTax + currStateTax;
            cashflowList.add(noFlow, new CashflowResults(cashflowResults));
            ++cashflowResults.count;
            ++cashflowResults.cumCount;
            totPrin += userPrin;
            dtLastCpn.setDate(dtNextCpn);
            dtNextCpn = bond.intSchedRule.getNextSchedDate(BondConsts.AFTERDATE, dtLastCpn);
            ++noFlow;
        }
        bond.dtMaturity = dtLastCpn;
        return cashflowList;
    }

    // get the passthru conditions on date of user passthru factor
    public CashflowPayments getPassthruPayments(Dates dtCalc) {
        // get prepayspeed that gets factor starting at issue 1. to passthru factor on
        // factor date
        SchedTrack schedTrack = new SchedTrack();
        PassthruLevelPay passthruLevelPay = new PassthruLevelPay();
        passthruLevelPay = getLevelPayment(bond.dtPassFactor, bond.passFactor);
        double levelPay = passthruLevelPay.levelPay;
        char use360Yn = 'Y';
        Dates dtAccrue = new Dates();
        Dates dtEndAccrue = new Dates();
        Dates dtNextCpn = new Dates();
        Dates dtLastCpn = new Dates();
        double intPay = 0., userIntPay = 0.;
        double schedPay = 0., userSchedPay = 0.;
        double prePay = 0., userPrepay = 0.;
        boolean usePrin = true;
        boolean useInt = !bond.stripType.equals(BondConsts.STRIPPO);
        if (bond.stripType.equals(BondConsts.STRIPIO))
            usePrin = false;
        // start accrue date is the date to get to staring speed
        dtNextCpn.setDate(bond.dtPassFactor);
        // get the level mortgage payment on passthru factor date
        double remFac = bond.passFactor; // factor on pass dtPassfact
        while (dtNextCpn.leDate(dtNextCpn, dtCalc) && remFac > 0. && dtNextCpn.leDate(dtNextCpn, bond.dtLastPayment)) { // start
                                                                                                                        // at
                                                                                                                        // user
                                                                                                                        // pass
                                                                                                                        // thru
                                                                                                                        // factor
                                                                                                                        // date
            intPay = remFac * bond.cpnRt / bond.accrueSchedRule.compoundFreq;
            if (useInt)
                userIntPay = remFac * (bond.cpnRt - bond.serviceRt) / bond.accrueSchedRule.compoundFreq;
            else
                userIntPay = 0.;
            schedPay = levelPay - intPay;
            if (usePrin)
                userSchedPay = schedPay;
            else
                userSchedPay = 0.;
            Dates dtCurrentAccrue = bond.accrueSchedRule.getNextSchedDate(BondConsts.BEFOREDATE, dtNextCpn);
            int perNo = (int) dayCount.time.getMonthsBetween(bond.dtDated, dtCurrentAccrue, true);
            double smmRt = getSmmRtPrepaySpeed(perNo, bond.passModel, bond.prepaySpeed);
            userPrepay = (remFac - userSchedPay) * smmRt;
            remFac -= (userSchedPay + userPrepay);
            levelPay -= userPrepay;
            double remFac1 = remFac - bond.passPayoff;
            if (remFac1 < 0.) {
                prePay = 0.;
                userPrepay = Math.max(0, (remFac - schedPay) * smmRt);
                userPrepay = 0.;
                userSchedPay = Math.max(0, remFac);
                remFac = 0.;
                bond.dtRedeem = dtCurrentAccrue;
                break;
            }
            dtLastCpn.setDate(dtNextCpn);
            dtNextCpn = bond.intSchedRule.getNextSchedDate(BondConsts.AFTERDATE, dtLastCpn);
        }
        cashflowPayments.levelPay = levelPay;
        cashflowPayments.intPay = userIntPay;
        cashflowPayments.schedPay = userSchedPay;
        cashflowPayments.prePay = userPrepay;
        cashflowPayments.dtPay = dtCalc;
        cashflowPayments.dtLastPayment = dtLastCpn;
        cashflowPayments.remFactor = remFac;
        return cashflowPayments;
    }

    public double getPassthruSchedPay(Dates dtCalc) {
        this.cashflowPayments = getPassthruPayments(dtCalc);
        return cashflowPayments.schedPay;
    }

    public double getInterestCash(SchedTrack schedTrack) {
        double intCash = 0.;
        Dates dtCalc = schedTrack.schedDate;
        cashflowPayments = getPassthruPayments(dtCalc);
        intCash = cashflowPayments.intPay;
        return intCash;

    }

    public double getPrepayCash(Dates dtCalc) {
        return this.cashflowPayments.prePay;
    }

    class CashflowPayments {
        double levelPay;
        double schedPay;
        double prePay;

        double intPay;

        Dates dtLastPayment;
        Dates dtPay;
        double remFactor;

        CashflowPayments() {
            levelPay = UnAvails.NOTAVAILDOUBLE;
            schedPay = UnAvails.NOTAVAILDOUBLE;
            prePay = UnAvails.NOTAVAILDOUBLE;
            dtPay = new Dates();
            intPay = UnAvails.NOTAVAILDOUBLE;
            dtLastPayment = bond.dtLastPayment;
            remFactor = UnAvails.NOTAVAILDOUBLE;
        }
    }
    /*
     * Gets the passthru level payment on a given passthru date
     * Gets the prespeed from mortgage dated date to passthru date
     * Using successive convergence
     * 
     */

    private double getPrePaySpeed(String passModel, Dates dtPassFactor, double passFactor) {
        double prepaySpeed = 0.;
        double levelPay = getInitalMortgagePayment(bond.cpnRt, bond.matTerm, bond.accrueSchedRule.compoundFreq);
        double estSpeed = getEstPrepaySpeed(bond.dtDated, dtPassFactor, passFactor);
        int trialNo = 0;
        String errmsg = "";
        double delta = 0.;
        int noMonths = (int) dayCount.time.getMonthsBetween(bond.dtDated, dtPassFactor, true);
        while (trialNo < CalcEnums.MAXROOTITERATIONS) {
            double trialFactor = trialPrePaySpeed(estSpeed, passModel, levelPay, bond.dtDated, 1., dtPassFactor,
                    passFactor);
            double diff = Math.abs(trialFactor - passFactor);
            if (diff <= CalcEnums.ROOTPRECISION)
                return estSpeed;
            if (trialFactor > passFactor) {
                delta = 1. - Math.pow(passFactor / trialFactor, 1. / noMonths);
                estSpeed += delta;
            }
            if (trialFactor < passFactor) {
                delta = 1. - Math.pow(trialFactor / passFactor, 1. / noMonths);
                estSpeed -= delta;
            }
            ++trialNo;
        }
        errmsg = "Could not find prepayspeed within " + CalcEnums.MAXROOTITERATIONS;
        bond.error.setError(getClass().getName(), errmsg);
        return 0.;
    }

    Dates calcMaturityDate(int calcNo, int securityNo, Dates dtSettle) {
        PassthruLevelPay passthruLevelPay;
        Dates dtAccrue = new Dates();
        Dates dtEndAccrue = new Dates();
        Dates dtNextCpn = new Dates();
        Dates dtLastCpn = new Dates();
        double intPay = 0., userIntPay = 0.;
        double schedPay = 0., userSchedPay = 0.;
        double prePay = 0., userPrepay = 0.;
        boolean usePrin = true;
        boolean useInt = !bond.stripType.equals(BondConsts.STRIPPO);
        if (bond.stripType.equals(BondConsts.STRIPIO))
            usePrin = false;
        passthruLevelPay = getLevelPayment(bond.dtPassFactor, bond.passFactor);
        double levelPay = passthruLevelPay.levelPay;
        // start accrue date is the date to get to staring speed
        dtNextCpn.setDate(bond.dtPassFactor);
        // get the level mortgage payment on passthru factor date
        double remFac = bond.passFactor; // factor on pass dtPassfact
        while (remFac > 0. && dtNextCpn.leDate(dtNextCpn, bond.dtLastPayment)) { // start at user pass thru factor date
            intPay = remFac * bond.cpnRt / bond.accrueSchedRule.compoundFreq;
            if (useInt)
                userIntPay = remFac * (bond.cpnRt - bond.serviceRt) / bond.accrueSchedRule.compoundFreq;
            else
                userIntPay = 0.;
            schedPay = levelPay - intPay;
            if (usePrin)
                userSchedPay = schedPay;
            else
                userSchedPay = 0.;
            Dates dtCurrentAccrue = bond.accrueSchedRule.getNextSchedDate(BondConsts.BEFOREDATE, dtNextCpn);
            int perNo = (int) dayCount.time.getMonthsBetween(bond.dtDated, dtCurrentAccrue, true);
            double smmRt = getSmmRtPrepaySpeed(perNo, bond.passModel, bond.prepaySpeed);
            userPrepay = (remFac - userSchedPay) * smmRt;
            remFac -= (userSchedPay + userPrepay);
            levelPay -= userPrepay;
            double remFac1 = remFac - bond.passPayoff;
            if (remFac1 < 0.) {
                prePay = 0.;
                userPrepay = Math.max(0, (remFac - schedPay) * smmRt);
                userPrepay = 0.;
                userSchedPay = Math.max(0, remFac);
                remFac = 0.;
                bond.dtRedeem = dtCurrentAccrue;
                break;
            }
            dtLastCpn.setDate(dtNextCpn);
            dtNextCpn = bond.intSchedRule.getNextSchedDate(BondConsts.AFTERDATE, dtLastCpn);
        }
        return bond.dtRedeem;
    }

    class SpeedResults {
        double prePaySpeed;
        double currHigh;
        double currLow;

        SpeedResults() {
            prePaySpeed = 0.;
            currHigh = 0.;
            currHigh = 0;
        }
    }

    // get estimated level payment to reach a pass factor from dated date
    public double getEstPrepaySpeed(Dates dtDated, Dates dtPassFactor, double passFactor) {
        double remFacZero = 0., estPrePaySpeed = 0.;
        int freq = bond.intSchedRule.compoundFreq;
        // int term = bond.passWala + bond.passWarm;
        remFacZero = getFactorZeroSpeed(dtPassFactor);
        int noMonths = (int) dayCount.time.getMonthsBetween(dtDated, dtPassFactor, true);
        estPrePaySpeed = 1. - Math.pow(passFactor / remFacZero, 1. / noMonths);
        return estPrePaySpeed;
    }

    class PassthruLevelPay {
        double levelPay;
        double remFac;

        PassthruLevelPay() {
            levelPay = 0.;
            remFac = 0.;
        }
    }

    // Calculates the passthru factor on desire dtCalc
    // Starting from the dated date
    public double getPassthruFactor(Dates dtCalc) {
        PassthruLevelPay passthruLevelPay = new PassthruLevelPay();
        SchedTrack schedTrack = new SchedTrack();
        double factor = 0., smmRt;
        char useEndMonthRuleYn = 'N';
        Dates dtLastAccrue = new Dates();
        Dates dtNextAccrue = new Dates();
        Dates dtEndAccrue = new Dates();
        Dates dtBefore = dayCount.time.addMonthsToDate(bond.dtPassFactor, -Defaults.MAXPASSTHRUAGEMONTHS, 'N');
        Dates dtAfter = dayCount.time.addMonthsToDate(bond.dtPassFactor, Defaults.MAXPASSTHRUAGEMONTHS, 'N');
        if (dtCalc.between(dtBefore, dtAfter))
            return bond.passFactor;
        int lastTrackNo = 0;
        schedTrack = bond.schedTracks.getSchedTrackOnBefore(dtCalc, SchedTrack.ANYTYPETRACK);
        dtEndAccrue = schedTrack.schedDate;
        int freq = bond.accrueSchedRule.compoundFreq;
        dtNextAccrue.setDate(bond.dtPassFactor);
        double remFac = bond.passFactor;
        passthruLevelPay = getLevelPayment(bond.dtPassFactor, bond.passFactor);
        double levelPay = passthruLevelPay.levelPay;
        while (dtNextAccrue.leDate(dtNextAccrue, dtEndAccrue) && remFac > bond.passPayoff
                && dtNextAccrue.leDate(dtNextAccrue, bond.dtMaturity)) {
            double intInc = remFac * bond.cpnRt / freq;
            double schedPay = levelPay - intInc;
            int perNo = (int) dayCount.time.getMonthsBetween(bond.dtDated, dtNextAccrue, true);
            smmRt = getSmmRtPrepaySpeed(perNo, bond.passModel, bond.prepaySpeed);
            double prePay = (remFac - schedPay) * smmRt;
            levelPay *= (1. - smmRt);
            remFac -= (schedPay + prePay);
            dtNextAccrue = bond.accrueSchedRule.getNextSchedDate(BondConsts.AFTERDATE, dtNextAccrue);
        }
        return remFac;
    }

    public double getFactorZeroSpeed(Dates dtCalc) {
        double levelPay = 0., remFac = 1.;
        int freq = bond.accrueSchedRule.compoundFreq;
        // start from beginning
        int term = bond.matTerm;
        levelPay = getInitalMortgagePayment(bond.cpnRt, term, freq);
        Dates dtNextAccrue = new Dates();
        dtNextAccrue.setDate(bond.dtDated);
        while (dtNextAccrue.leDate(dtNextAccrue, dtCalc)) {
            double intInc = remFac * bond.cpnRt / freq;
            double schedPay = levelPay - intInc;
            double prePay = 0.;
            remFac -= schedPay;
            dtNextAccrue = bond.accrueSchedRule.getNextSchedDate(BondConsts.AFTERDATE, dtNextAccrue);
        }
        return remFac;
    }

    // get the level payment on the user specified passthru date and the factor on
    // that date
    public PassthruLevelPay getLevelPayment(Dates dtPassFactor, double passFactor) {
        PassthruLevelPay passthruLevelPay = new PassthruLevelPay();
        double zeroSpeedFactor = 0.;
        double prePaySpeed = 0.;
        int freq = bond.accrueSchedRule.compoundFreq;
        double levelPay = getInitalMortgagePayment(bond.cpnRt, bond.matTerm, freq);
        // get zero factr on dtPriorAccrue
        double couFactor = bond.cpnRt / freq;
        // double currFactor = getFactorZeroSpeed(dtPassFactor);
        double smmrt = getPrePaySpeed(bond.passModel, bond.dtPassFactor, bond.passFactor);
        double remFac = 1.;
        int ip = -1;
        double intInc = 0.;
        double schedPrin = 0.;
        double prepayPrin = 0.;
        while (remFac < passFactor) {
            intInc = remFac * couFactor;
            schedPrin = levelPay - intInc;
            prepayPrin = (remFac - schedPrin) * smmrt;
            levelPay *= (1. - smmrt);
            remFac -= schedPrin;
        }
        passthruLevelPay.levelPay = levelPay;
        passthruLevelPay.remFac = remFac;
        return passthruLevelPay;
        //
    }

    public double getSmmRtPrepaySpeed(int perNo, String passModel, double prePaySpeed) {
        char useEndOfMonthRuleYn = 'N', use360Yn = 'Y';
        double smmRt = 0.;
        switch (passModel) {
            case "CPR":
                smmRt = getCprSmmSpeed(perNo, prePaySpeed);
                break;
            case "PSA":
                smmRt = getPsaSmmSpeed(perNo, prePaySpeed);
                break;
            case "ABS":
                smmRt = getAbsSmmSpeed(perNo, prePaySpeed);
                break;
            case "SMM":
                smmRt = getUserSmmSpeed(perNo);
                break;
            default:
        }
        return smmRt;
    }

    // get the monthly or period single period mortality speed
    public double getCprSmmSpeed(int perNo, double prePaySpeed) {
        double smmRt = 0.;
        smmRt = 1. - Math.pow(1. - prePaySpeed, 1. / bond.intSchedRule.compoundFreq);
        return smmRt;
    }

    public double getPsaSmmSpeed(int perNo, double prePaySpeed) {
        double smmRt = 0., smmRtAnnual = 0.;
        int freq = bond.intSchedRule.compoundFreq;
        if (perNo <= this.psaMonth)
            smmRtAnnual = perNo * this.psaSmmRtStart * prePaySpeed;
        else
            smmRtAnnual = perNo * prePaySpeed;
        smmRt = 1. - Math.pow(1. - smmRtAnnual, 1. / freq);
        return smmRt;
    }

    public double getAbsSmmSpeed(int perNo, double prePaySpeed) {
        int freq = bond.intSchedRule.compoundFreq;
        double smmRt = 1. - Math.pow(1. - prePaySpeed, 1. / freq);
        return smmRt;
    }

    public double getUserSmmSpeed(int perNo) {
        double smmRt = 0.;
        double prePay = 0.;
        char use360Yn = 'Y';
        int sNo = -1;
        Dates dtSettle = new Dates();
        dtSettle = dayCount.time.addMonthsToDate(bond.dtDated, perNo, use360Yn);
        while (++sNo < bond.noDtEstPrePay) {
            if (dtSettle.gtDate(bond.dtEstPrepay[sNo], dtSettle)) {
                if (sNo >= 1)
                    prePay = bond.estPrePay[sNo - 1];
                else
                    prePay = bond.estPrePay[0];
            }
            if (dtSettle.eqDate(dtSettle, bond.dtEstPrepay[sNo]))
                prePay = bond.estPrePay[sNo];
        }
        smmRt = 1. - Math.pow(1. - prePay, 1. / bond.intSchedRule.compoundFreq);
        return smmRt;
    }
    /*
     * public double getPrepaySpeedRoot(double prePayEst, double levelPayFromDated)
     * {
     * int it = 0;
     * String strPrePaySpeed;
     * RootResult rootResult = new RootResult();
     * double func = 0, dFunc = 0, deltaFunc = 0.;
     * Double speedEst = prePayEst;
     * for (it = 0; it < CalcEnums.MAXROOTITERATIONS; ++it) {
     * rootResult = trialPrepaySpeed(speedEst, levelPayFromDated);
     * func = rootResult.func;
     * dFunc = rootResult.dFunc;
     * if (dFunc == 0.) {
     * String errmsg =
     * "Cannot determine prepay speed because the derivative of the ratio of passthru factor to speed is zero"
     * ;
     * bond.error.setError(getClass().getName(), errmsg);
     * return speedEst;
     * }
     * 
     * if (speedEst < Limits.MINPREPAYSPEED || speedEst > Limits.MAXPREPAYSPEED) {
     * strPrePaySpeed = speedEst.toString();
     * bond.error.setError(getClass().getName(), "Prepay speed found (" +
     * strPrePaySpeed + ") is out of reasonable bounds");
     * return speedEst;
     * }
     * if (Math.abs(func) <= CalcEnums.ROOTPRECISION)
     * break;
     * deltaFunc = func / dFunc;
     * speedEst -= deltaFunc;
     * }
     * if (it > CalcEnums.MAXROOTITERATIONS) {
     * strPrePaySpeed = speedEst.toString();
     * bond.error.setError(getClass().getName(), "Yield found (" + strPrePaySpeed +
     * ") is best yield within maximun alloed trials");
     * return speedEst;
     * }
     * return speedEst;
     * }
     * 
     * // alwasy from dated date
     * 
     * public RootResult trialPrepaySpeed(double prePayEst, double levelPay) {
     * double remFac = 0.;
     * double deltaPrePaySpeed = CalcEnums.ROOTSPEEDDERIVATIVEDELTA;
     * RootResult rootResult = new RootResult();
     * 
     * double remFac1 = getPassthruFactor(bond.dtDated, levelPay, 1., prePayEst,
     * bond.dtPassFactor);
     * double remFac2 = getPassthruFactor(bond.dtDated, levelPay, 1., prePayEst +
     * deltaPrePaySpeed, bond.dtPassFactor);
     * double diff = remFac1 - remFac2;
     * rootResult.func = diff;
     * rootResult.dFunc = diff / deltaPrePaySpeed;
     * return rootResult;
     * }
     */

    public double trialPrePaySpeed(double estSpeed, String passModel, double startLevelPay, Dates dtStart,
            double startFactor, Dates dtEnd, double endFactor) {
        double trialFactor;
        int ip = 0;
        char use360Yn = 'Y';
        double remFactor = startFactor;
        Dates dtNextAccrue = new Dates();
        Dates dtLastAccrue = new Dates();
        double levelPay = startLevelPay;
        SchedTrack schedTrack = new SchedTrack();
        double freq = bond.intSchedRule.compoundFreq;
        int trackNo = 0;
        dtNextAccrue = bond.accrueSchedRule.getNextSchedDate(BondConsts.BEFOREONDATE, dtStart);
        while (dtNextAccrue.leDate(dtNextAccrue, dtEnd)) {
            double intInc = remFactor * bond.cpnRt / freq;
            double schedPay = levelPay - intInc;
            double prePay = (remFactor - schedPay) * estSpeed;
            remFactor -= (schedPay + prePay);
            levelPay *= (1. - estSpeed);
            dtLastAccrue.setDate(dtNextAccrue);
            dtNextAccrue = bond.accrueSchedRule.getNextSchedDate(BondConsts.AFTERDATE, dtLastAccrue);
            // schedTrack = bond.schedTracks.getSchedTrackAfter(dtLastAccrue,
            // SchedTrack.ACCRUETRACK);
        }
        return remFactor;
    }

    double getInitalMortgagePayment(double cpnRt, int term, int freq) {
        if (freq <= 0) {
            String errmsg = "invalid payment frequency of zero specified - would cause divide by zero or is negative";
            bond.error.setError(getClass().getName(), errmsg);
            return 0.;
        }
        double couFac = cpnRt / freq;
        double xFac = 1. / (1. + couFac);

        double payment = (1. - xFac) / (xFac * (1. - Math.pow(xFac, term)));
        return payment;
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

}
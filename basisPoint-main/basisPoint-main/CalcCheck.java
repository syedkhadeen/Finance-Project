import java.util.ArrayList;

public class CalcCheck {
    Bond bond;
    Error error = new Error();
    Choices choices;
    CalcMethod calcMethod;
    CalcDef calcDef = new CalcDef();

    CalcCheck(Bond bond, Error error, Choices choices, CalcMethod calcMethod) {
        this.bond = bond;
        this.error = error;
        this.choices = choices;
        this.calcMethod = calcMethod;
    }

    public boolean checkCalc(int securityNo, int calcNo) {
        boolean isOk = false, isDocs;
        String instype = bond.instype;
        if (choices.noSecurityCalcList.contains(calcNo))
            return true;

        isOk = docCheck(instype, calcNo); //is this a utility calculation
        if (error.hasErrorYn == 'Y')
            return false;
        if (isOk)
            return true;
        isOk = checkForwardCurve(instype, calcNo);
        if (!isOk)
            return false;
        isOk = checkSpotCurve(instype, calcNo);
        if (!isOk)
            return false;
        if (choices.inNoSecurityList(calcNo)) // this is a utility calculation
            return true;
        isOk = dayCountCheck(instype, calcNo);
        if (!isOk)
            return false;
        isOk = checkDates(instype, calcNo);
        if (!isOk)
            return false;
        isOk = checkInstype(instype, calcNo);
        if (!isOk)
            return false;
        isOk = checkCashflow(instype, calcNo);
        if (!isOk)
            return false;
        isOk = checkInterestIncome(instype, calcNo);
        if (!isOk)
            return false;
        isOk = checkYield(instype, calcNo, securityNo);
        if (!isOk)
            return false;
        isOk = checkPrice(instype, calcNo, securityNo);
        if (!isOk)
            return false;
        isOk = checkKeyRateDuration(instype, calcNo);
        if (!isOk)
            return false;
        isOk = checkZSpread(instype, calcNo);
        if (!isOk)
            return false;
        isOk = checkFairValue(instype, calcNo);
        if (!isOk)
            return false;

        isOk = checkBookValue(securityNo, instype, calcNo);
        if (!isOk)
            return false;
        isOk = checkDuration(instype, calcNo);
        if (!isOk)
            return false;
        isOk = checkConvexity(instype, calcNo);
        if (!isOk)
            return false;
        isOk = checkHorizonAnalysis(instype, calcNo);
        if (!isOk)
            return false;
        isOk = checkFutureValue(instype, calcNo);
        if (!isOk)
            return false;
        isOk = checkFutureReturnRisk(instype, calcNo);
        if (!isOk)
            return false;
        isOk = checkTradeEvaluation(instype, calcNo);
        if (!isOk)
            return false;
        isOk = checkSettlePrice(instype, calcNo);
        if (!isOk)
            return false;
        return isOk;
    }

    public void setBond(Bond bond) {
        this.bond = bond;
    }

    public boolean docCheck(String instype, int calcNo) {
        boolean isOk = choices.documentList.contains(calcNo);
        if (isOk) {
            if (UnAvails.isUnAvail(bond.rootDirectory)) {
                String errmsg = "No root directory was specified so cannot write out documentation for " + calcDef.calcDefs.get(calcNo).calcName;
                error.errorMessage = errmsg;
                error.hasErrorYn = 'Y';
                return true;
            } else
                return true;
        }
        return false;
    }


    public boolean dayCountCheck(String instype, int calcNo) {
        boolean isOk = false;
        if (calcNo != CalcList.DAYCOUNT)
            return true;
        if (bond.dtStart.geDate(bond.dtStart, bond.dtEnd)) {
            String errmsg = "Start date " + bond.dtStart.getDateString() + " is after end date " + bond.dtEnd.getDateString();
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.basAccInt)) {
            String errmsg = "Accrual basis has not been entered for the daycount calculation ";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        return true;
    }

    private boolean checkYieldCurve(String instype, int calcNo) {
        // populate spot curve
        char curveType;
        char useEndMonthRuleYn = 'Y';
        // convert to spot curve based on calcrequest

        if (choices.inFloatList(instype) && bond.useFixingCurveYn == 'Y') {
            if (bond.noFixingRate != bond.noFixingTenor) {
                String errmsg = "Number of fixing rates " + bond.noFixingRate + " is not equal to number of fixing maturity lengths " + bond.noFixingTenor;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (bond.noFixingRate < Limits.MINDISCGRATES) {
                String errmsg = "Number of fixing rates specified " + bond.noFixingRate + " must be equal to or greater than " + Limits.MINDISCGRATES;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (bond.noFixingRate > Limits.MINDISCGRATES) {
                String errmsg = "Number of fixing rates specified " + bond.noFixingRate + " must be less than or equal to " + Limits.MAXDISCRATES;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            return true;
        }
        if (!choices.inYieldCalcList(calcNo))
            return true;
        if (bond.noDiscRate != bond.noDiscTenor) {
            String errmsg = "Number of discount rates " + bond.noDiscRate + " is not equal to number of discount maturity lengths " + bond.noDiscTenor;
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.noDiscRate < Limits.MINDISCGRATES) {
            String errmsg = "Number of discount rates specified " + bond.noDiscRate + " must be equal to or greater than " + Limits.MINDISCGRATES;
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.noDiscRate > Limits.MAXDISCRATES) {
            String errmsg = "Number of discount rates specified " + bond.noDiscRate + " must be less than or equal to " + Limits.MAXDISCRATES;
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.noDiscRtDn != bond.noDiscRtUp) {
            String errmsg = "Number of discount volatilities down side " + bond.noDiscRtDn + " not equal to number of discount volatilities upside " + bond.noDiscRtUp;
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        return true;

    }

    public boolean checkInstype(String instype, int calcNo) {
        boolean isOk = true;
        if (instype.equals("Indexed")) {
            if (bond.noIdxCPIDts > 0 && bond.noIdxRatioDts == 0) {
                if (UnAvails.isUnAvail(bond.idxCPIAtIssue)) {
                    String errmsg = "No index at issue was specified for this index linked bond ";
                    error.setError(getClass().getName(), errmsg);
                    return false;
                }
            }
            isOk = true;
        }
        return isOk;
    }

    public boolean checkDates(String instype, int calcNo) {
        // aettle dare cannot be on or after maturity dat
        if (choices.inDiscountCurveList(calcNo))
            return true;
        if (bond.dtSettle.gtDate(bond.dtSettle, bond.dtMaturity)) {
            String errmsg = "Settle date " + bond.dtSettle.getDateString() + " is after maturity date " + bond.dtMaturity.getDateString();
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.dt1stPay.year != UnAvails.NOTAVAILDATE && bond.dtDated.year != UnAvails.NOTAVAILDATE) {
            if (bond.dtDated.geDate(bond.dtDated, bond.dt1stPay)) {
                String errmsg = "Dated date " + bond.dtDated.getDateString() + " is after 1st payment date " + bond.dt1stPay.getDateString();
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (bond.dtLastPayment.gtDate(bond.dtLastPayment, bond.dtMaturity)) {
            if (!choices.inPassthruList(bond.instype)) {
                String errmsg = "last payment date " + bond.dtLastPayment.getDateString() + " is after maturity date " + bond.dtMaturity.getDateString();
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (bond.dtDated.geDate(bond.dtDated, bond.dtMaturity)) {
            String errmsg = "Dated date " + bond.dtDated.getDateString() + " is after maturity date " + bond.dtMaturity.getDateString();
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        int i;
        if (bond.noCallDts > 0) {
            for (i = 0; i < bond.noCallDts; ++i) {
                if (bond.dtCall[i].geDate(bond.dtCall[i], bond.dtMaturity)) {
                    error.setError(getClass().getName(), "Call date " + bond.dtCall[i].getDateString() + " is greater or equal to maturity date " + bond.dtMaturity.getDateString());
                    return false;
                }
            }
        }
        if (bond.noPutDts > 0) {
            for (i = 0; i < bond.noPutDts; ++i) {
                if (bond.dtPut[i].geDate(bond.dtPut[i], bond.dtMaturity)) {
                    error.setError(getClass().getName(), "Put date " + bond.dtPut[i].getDateString() + " is greater or equal to maturity date " + bond.dtMaturity.getDateString());
                    return false;
                }
            }
        }
        if (bond.noSinkDts > 0) {
            for (i = 0; i < bond.noSinkDts; ++i) {
                if (bond.dtSinks[i].geDate(bond.dtSinks[i], bond.dtMaturity)) {
                    error.setError(getClass().getName(), "Sink date " + bond.dtSinks[i].getDateString() + " is greater or equal to maturity date " + bond.dtMaturity.getDateString());
                    return false;
                }
            }
        }
        if (bond.dtSettle.gtDate(bond.dtFuture) && UnAvails.isAvail(bond.dtFuture)) {
            String errmsg = "Future value date(" + bond.dtFuture.getDateString() + ") is before settle date(" + bond.dtSettle.getDateString() + ")";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        return true;
    }

    public boolean checkCashFlowInput(String instype, int calcNo) {
        boolean found;
        found = choices.inCashflowList(calcNo);
        if (found) {
            if (UnAvails.isUnAvail(bond.faceAmt)) {
                error.setError(getClass().getName(), "face amount must be entered for " + calcDef.calcDefs.get(calcNo));
                return false;
            }
        }
        return true;
    }


    public boolean checkBookValue(int securityNo, String instype, int calcNo) {
        error.setCalcName(calcDef.calcDefs.get(calcNo).calcName);
        if (!choices.inAccrualAccountingList(calcNo))
            return true;
        if (UnAvails.isUnAvail(bond.dtPur)) {
            String errmsg = "No purchase date was specified for " + calcDef.calcDefs.get(calcNo).calcName;
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        if (bond.dtPur.geDate(bond.dtPur, bond.dtMaturity)) {
            String errmsg = "Purchase date " + bond.dtPur.getDateString() + " cannot be on or after maturity date " + bond.dtMaturity.getDateString();
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        if (bond.dtPur.gtDate(bond.dtPur, bond.dtSettle)) {
            String errmsg = "purchase date " + bond.dtPur.getDateString() + " cannot be after calculation settle date " + bond.dtSettle.getDateString();
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        if (UnAvails.isUnAvail(bond.bookCalcMeth)) {
            String errmsg = "No book value calculation method was specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        if (bond.bookCalcMeth.equals(Choices.STRIGHTLINE)) {
            if (UnAvails.isUnAvail(bond.purPrc)) {
                String errmsg = "No purchase price was specified";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (bond.bookCalcMeth.equals(Choices.CONSTYIELD) || bond.bookCalcMeth.equals(Choices.CONSTYIELDP)) {
            if (UnAvails.isUnAvail(bond.purYld)) {
                if (UnAvails.isUnAvail(bond.purPrc)) {
                    String errmsg = "No purchase yield or purchase price was specified for constant yield bookvalue";
                    error.setError(getClass().getName(), errmsg);
                    return false;
                } else {
                    bond.purYld = calcMethod.yieldPrice(calcNo, securityNo, instype, bond.dtPur, bond.purPrc, bond.dtRedeem, bond.redeemPrin, bond.redeemInt);
                    return true;
                }
            }
        }


        if (bond.bookCalcMeth.equals(Choices.EFFINCOME) || bond.bookCalcMeth.equals(Choices.EFFINCOMEP)) {
            if (UnAvails.isUnAvail(bond.purYld)) {
                if (UnAvails.isUnAvail(bond.purPrc)) {
                    String errmsg = "No purchase yield or purchase price was specified for effective income bookvalue";
                    error.setError(getClass().getName(), errmsg);
                    return false;
                } else {
                    bond.purYld = calcMethod.yieldPrice(calcNo, securityNo, instype, bond.dtPur, bond.purPrc, bond.dtMaturity, bond.redeemPrin, bond.redeemInt);
                    return false;

                }
            }
        }
        return true;
    }

    public boolean checkInterestPayment(String instype, int calcNo) {
        boolean failed = false;
        if (instype == Instype.DISCOUNT || instype == Instype.MORTGAGE)
            return true;
        if (bond.intPayMeth == UnAvails.NOTAVAILSTRING) {
            String errmsg = "no interest payment method <intPayMeth> has been specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        return failed;
    }

    public boolean checkInterestIncome(String instype, int calcNo) {
        if (calcNo != CalcList.INTERESTINCOME)
            return true;
        if (UnAvails.isUnAvail(bond.dtStart)) {
            String errmsg = "No start date specified for calculation " + calcDef.calcDefs.get(calcNo).calcName;
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.dtEnd)) {
            String errmsg = "No end date specified for calculation " + calcDef.calcDefs.get(calcNo).calcName;
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.dtStart.geDate(bond.dtStart, bond.dtEnd)) {
            String errmsg = "The start date specfied " + bond.dtStart.getDateString() + " is not less than the end date " + bond.dtEnd.getDateString();
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.dtEnd.gtDate(bond.dtEnd, bond.dtMaturity)) {
            String errmsg = "The end date specified " + bond.dtEnd.getDateString() + " cannot be greater than maturity date " + bond.dtMaturity.getDateString();
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.dtStart.leDate(bond.dtStart, bond.dtDated)) {
            String errmsg = "The start date specified " + bond.dtStart.getDateString() + " cannot be before than dated date " + bond.dtDated.getDateString();
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        return true;
    }

    public boolean checkYield(String instype, int calcNo, int securityNo) {
        if (choices.inYieldCalcList(calcNo)) {
            if (UnAvails.isUnAvail(bond.settleYld)) {
                if (UnAvails.isAvail(bond.settlePrc)) {
                    bond.settleYld = .01 * calcMethod.yieldPrice(calcNo, securityNo, instype, bond.dtSettle, bond.settlePrc, bond.dtRedeem, bond.redeemPrin, bond.redeemInt);
                    if (error.hasErrorYn == 'Y') {
                        String errmsg = "No yield was supplied and cannot calculate price";
                        error.setError(getClass().getName(), errmsg);
                        return false;
                    }
                    return true;
                } else {
                    String errmsg = "No yield was supplied";
                    error.setError(getClass().getName(), errmsg);
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkPrice(String instype, int calcNo, int securityNo) {
        if (choices.inCalcPriceList(calcNo)) {
            if (bond.settlePrc == UnAvails.NOTAVAILDOUBLE) {
                if (UnAvails.isAvail(bond.settleYld)) {
                    bond.settlePrc =  calcMethod.priceYield(calcNo, securityNo, instype, bond.dtSettle, bond.settleYld, bond.dtRedeem, bond.redeemPrin, bond.redeemInt);
                    if (error.hasErrorYn == 'Y') {
                        String errmsg = "No yield was supplied and cannot calculate price";
                        error.setError(getClass().getName(), errmsg);
                        return false;
                    }
                    return true;
                } else {
                    String errmsg = "No price was specified for calculation " + calcDef.calcDefs.get(calcNo).calcName;
                    error.setError(getClass().getName(), errmsg);
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkDuration(String instype, int calcNo) {
        if (calcNo == CalcList.MACAULAYDUR)
            if (UnAvails.isUnAvail(bond.settleYld)) {
                // bond.settleYld = bond.yieldPrice(calcNo, instype, bond.dtSettle, bond.settlePrc, bond.dtRedeem, bond.redeemInt, bond.redeemPrin, bond.yieldQuoteType);
                if ('Y' == error.hasErrorYn) {
                    String errmsg = "No yield was specified";
                    error.setError(getClass().getName(), errmsg);
                    return false;
                }
            }
        if (calcNo == CalcList.MODIFIEDDURSIA)
            if (UnAvails.isUnAvail(bond.settleYld)) {
                //  bond.settleYld = bond.yieldPrice(calcNo, instype, bond.dtSettle, bond.settlePrc, bond.dtRedeem, bond.redeemInt, bond.redeemPrin, bond.yieldQuoteType);
                if ('Y' == error.hasErrorYn) {
                    String errmsg = "No yield was specified";
                    error.setError(getClass().getName(), errmsg);
                    return false;
                }
            }
        if (calcNo == CalcList.MODIFIEDDURSTD)
            if (UnAvails.isUnAvail(bond.settleYld)) {
                //    bond.settleYld = bond.yieldPrice(calcNo, instype, bond.dtSettle, bond.settlePrc, bond.dtRedeem, bond.redeemInt, bond.redeemPrin, bond.yieldQuoteType);
                if ('Y' == error.hasErrorYn) {
                    String errmsg = "No yield was specified";
                    error.setError(getClass().getName(), errmsg);
                    return false;
                }
            }
        return true;
    }

    public boolean checkConvexity(String instype, int calcNo) {
        if (calcNo == CalcList.CONVEXITY)
            if (UnAvails.isUnAvail(bond.settleYld)) {
                //  bond.settleYld = bond.yieldPrice(calcNo, instype, bond.dtSettle, bond.settlePrc, bond.dtRedeem, bond.redeemInt, bond.redeemPrin, bond.yieldQuoteType);
                if ('Y' == error.hasErrorYn) {
                    String errmsg = "No yield was specified";
                    error.setError(getClass().getName(), errmsg);
                    return false;
                }
            }
        return true;
    }

    boolean checkKeyRateDuration(String instype, int calcNo) {
        if (calcNo == CalcList.KEYRATEDURATION) {
            if (bond.noDiscRate != bond.noDiscTenor) {
                String errmsg = "no of discount rates " + bond.noDiscRate + " unequal to number of discount maturity lengths " + bond.noDiscTenor;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (bond.discCurve.spotCurve.isEmpty()) {
                String errmsg = "No spot curve was specified";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (UnAvails.isUnAvail(bond.yieldCurveInterpMeth)) {
                bond.yieldCurveInterpMeth = CalcEnums.LINEAR;
                String msg = "Yield curve interpolation was defaulted to Linear";
                error.setWarning(getClass().getName(), msg);
            }
        }

        return true;
    }

    boolean checkZSpread(String instype, int calcNo) {
        if (calcNo == CalcList.ZSPREAD) {
            if (bond.discCurve.spotCurve.size() > 3)
                return true;
            if (bond.noDiscRate != bond.noDiscTenor) {
                String errmsg = "No of discount rates " + bond.noDiscRate + " unequal to number of discount maturity lengths " + bond.noDiscTenor;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (bond.noDiscTenor < Defaults.MINIMUMYIELDPOINTS) {
                String errmsg = "a minimum of " + Defaults.MINIMUMYIELDPOINTS + " discount rates and maturity lengths must be specified - you entered " + bond.noDiscTenor;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (UnAvails.isUnAvail(bond.yieldCurveInterpMeth)) {
                bond.yieldCurveInterpMeth = CalcEnums.LINEAR;
                String msg = "Yield curve interpolation was defaulted to Linear";
                error.setWarning(getClass().getName(), msg);
            }
        }
        return true;
    }

    boolean checkFairValue(String instype, int calcNo) {
        if (calcNo == CalcList.FAIRPRICE) {
            if (bond.noDiscRate != bond.noDiscTenor) {
                String errmsg = "No of discount rate " + bond.noDiscRate + " unequal to number of discount maturity lengths " + bond.noDiscTenor;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (bond.noDiscTenor < Defaults.MINIMUMYIELDPOINTS) {
                String errmsg = "a minimum of " + Defaults.MINIMUMYIELDPOINTS + " discount rates and maturity lengths must be specified - you entered " + bond.noDiscTenor;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (UnAvails.isUnAvail(bond.yieldCurveInterpMeth)) {
                bond.yieldCurveInterpMeth = CalcEnums.LINEAR;
                String msg = "Yield curve interpolation was defaulted to Linear";
                error.setWarning(getClass().getName(), msg);
            }
        }
        return true;
    }


    boolean checkDiscCurve(String instype, int calcNo) {
        if (choices.inDiscountCurveList(calcNo)) {
            if (bond.noDiscRate == 0) {
                String errmsg = "No discount yield curve was supplied for calculation " + calcDef.calcDefs.get(calcNo).calcName;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (bond.noDiscTenor < Defaults.MINIMUMYIELDPOINTS) {
                String errmsg = "a minimum of " + Defaults.MINIMUMYIELDPOINTS + " discount rates and maturity lengths must be specified - you entered " + bond.noDiscTenor;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (bond.noDiscRate != bond.noDiscTenor) {
                String errmsg = "Number of discount rate(s) " + bond.noDiscRate + " unequal to number of discount maturity lengths " + bond.noDiscTenor;
                error.setError(getClass().getName(), errmsg);
                return false;
            }

        }
        return true;
    }

    public boolean checkForwardCurve(String instype, int calcNo) {
        if (!choices.inDiscountCurveList(calcNo))
            return true;
        if (choices.inFutureCalcList(calcNo) && bond.yieldForeCastMeth.equals(BondConsts.FORWARDCURVE)) {
            if (UnAvails.isUnAvail(bond.dtFuture)) {
                String errmsg = "No future date was set for the " + calcDef.calcDefs.get(calcNo).calcName + " calculation";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (bond.discRfCurve.forwardCurve.size() == 0) {
                String errmsg = "No forward yield curve was specified for " + calcDef.calcDefs.get(calcNo).calcName + " calculation";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        return true;
    }

    public boolean checkSpotCurve(String instype, int calcNo) {
        if (!choices.inDiscountCurveList(calcNo))
            return true;
        if (choices.inFutureCalcList(calcNo)) {
            if (UnAvails.isUnAvail(bond.dtFuture)) {
                String errmsg = "No future date was set for the " + calcDef.calcDefs.get(calcNo).calcName + " calculation";
                error.setError(getClass().getName(), errmsg);
                return false;
            }

            if (UnAvails.isUnAvail(bond.dtDiscCurve)) {
                String errmsg = "No date for current spot yield curve was specified for " + calcDef.calcDefs.get(calcNo).calcName + " calculation";
                error.setError(getClass().getName(), errmsg);
                return false;
            }



            if (bond.discFutRfCurve.spotCurve.size() == 0) {
                if (calcNo == CalcList.ATTRIBUTION) {// atribution need 2 yiekd curves
                    String errmsg = "No future spot yield curve was specified for " + calcDef.calcDefs.get(calcNo).calcName + " calculation";
                    error.setError(getClass().getName(), errmsg);
                    return false;
                }
                if (bond.discRfCurve.spotCurve.size() > 0) {
                    bond.status.useDiscCurveForFutValue = true;
                    return false;
                }
            }
        }
        return true;
    }


    private boolean checkFutureValue(String instype, int calcNo) {
        if (!choices.inFutureCalcList(calcNo))
            return true;
        if (UnAvails.isUnAvail(bond.dtFuture)) {
            String errmsg = "No future calculation date was specified";
            error.setError(getClass().getName(), calcNo, errmsg);
            return false;
        }

        if (UnAvails.isUnAvail(bond.dtSettle)) {
            String errmsg = "No settlement date was specified";
            error.setError(getClass().getName(), calcNo, errmsg);
            return false;
        }

        if (bond.discRfCurve == null) {
            String errmsg = "No discount yield curve at future date was supplied";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (calcNo == CalcList.ATTRIBUTION) {
            if (UnAvails.isUnAvail(bond.mktPrcStart)) {
                if (UnAvails.isAvail(bond.settlePrc))
                    bond.mktPrcStart = bond.settlePrc;
                else {
                    String errsmg = "No market value at settle was specified";
                    error.calcName = calcDef.calcDefs.get(CalcList.ATTRIBUTION).calcName;
                    error.setError(getClass().getName(), errsmg);
                    return false;
                }
            }
            if (UnAvails.isUnAvail(bond.mktPrcFuture)) {
                String errsmg = "No market value at future was specified";
                error.calcName = calcDef.calcDefs.get(CalcList.ATTRIBUTION).calcName;
                error.setError(getClass().getName(), errsmg);
                return false;
            }
        }
        return true;
    }

    private boolean checkFutureReturnRisk(String instype, int calcNo) {
        boolean isOk = true;
        if (calcNo == CalcList.FUTRETURNRISK) {
            isOk = checkFutureValue(instype, calcNo);
            if (!isOk) {
                return false;
            }
            if (UnAvails.isUnAvail(bond.settlePrc)) {
                String errmsg = "No settle price was entered for calculation " + calcDef.calcDefs.get(calcNo).calcName;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (UnAvails.isUnAvail(bond.faceAmt)) {
                String errmsg = "No face amount was entered for calculation " + calcDef.calcDefs.get(calcNo).calcName;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        return true;
    }


    private boolean checkHorizonAnalysis(String instype, int calcNo) {

        if (calcNo == CalcList.HORIZONANALYSIS) {
            if (UnAvails.isUnAvail(bond.horizonFreq)) {
                String errmsg = "No horizon frequency was specified for horizon analysis";
                error.setError(getClass().getName(), calcNo, errmsg);
                return false;
            }
            if (UnAvails.isUnAvail(bond.horizonPeriods) || bond.horizonPeriods == 0) {
                String errmsg = "No horizon periods were specified for horizon analysis";
                error.setError(getClass().getName(), calcNo, errmsg);
                return false;
            }

            if (bond.discFutCurve.spotCurve.size() == 0) {
                String errmsg = "No discount rate curve was specified for horizon analysis";
                error.setError(getClass().getName(), calcNo, errmsg);
                return false;
            }

        }
        return true;
    }

    public boolean checkAttribution(String instype, int calcNo) {
        if (calcNo != CalcList.ATTRIBUTION)
            return true;
        error.calcName = calcDef.calcDefs.get(calcNo).calcName;
        if (UnAvails.isUnAvail(bond.mktPrcStart)) {
            String errmsg = "No market price at settle date was specified";
            error.setError(getClass().getName(), calcNo, errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.mktPrcFuture)) {
            String errmsg = "No market price for future date was specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.dtFuture)) {
            String errmsg = "No future calculation date was specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.discRfCurve.spotCurve.size() == 0) {
            String errmsg = "No risk free discount yield curve was specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.discSprdCurve.spotCurve.size() == 0) {
            String errmsg = "No credit spread yield curve was specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.discFutRfCurve.spotCurve.size() == 0) {
            String errmsg = "No future risk free discount yield curve was specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.discFutSprdCurve.spotCurve.size() == 0) {
            String errmsg = "No future credit spread yield curve was specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        return true;
    }

    public boolean checkTradeEvaluation(String instype, int calcNo) {
        if (calcNo != CalcList.SWAPANALYSIS)
            return true;
        if (bond.discCurve.spotCurve.isEmpty()) {
            String errmsg = "No discount spot curve was specified on " + bond.dtSettle.getDateString();
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (bond.discFutCurve.spotCurve.isEmpty()) {
            String errmsg = "No discount spot curve was specified on " + bond.dtFuture.getDateString();
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.settlePrc)) {
            String errmsg = "No settle price was specified on " + bond.dtSettle.getDateString();
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        return true;
    }

    public boolean checkSettlePrice(String instype, int calcNo) {
        if (UnAvails.isAvail(bond.settlePrc))
            return true;
        boolean isOk = true;
        if (choices.inCalcPriceList(calcNo)) {
            isOk = checkFairValue(instype, CalcList.FAIRPRICE);
            if (isOk == false)
                return false;
            bond.settlePrc = calcMethod.calcFairPrice(CalcList.FAIRPRICE, 0, instype);
            if (error.hasErrorYn == 'Y')
                return false;
        }
        return true;
    }

    private boolean checkCashflow(String instype, int calcNo) {
        if (calcNo == CalcList.CASHFLOW) {
            if (UnAvails.isUnAvail(bond.dtSettle)) {
                String errmsg = "No settle date was specified";
                error.setError(getClass().getName(), calcNo, errmsg);
                return false;
            }
        }
        if (calcNo == CalcList.CASHFLOWINTERVAL) {
            if (UnAvails.isUnAvail(bond.dtStart)) {
                String errmsg = "No start date was specified";
                error.setError(getClass().getName(), calcNo, errmsg);
                return false;
            }
            if (UnAvails.isUnAvail(bond.dtEnd)) {
                String errmsg = "No end date was specified";
                error.setError(getClass().getName(), calcNo, errmsg);
                return false;
            }
        }
        return true;
    }
}




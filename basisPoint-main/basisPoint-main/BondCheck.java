
import java.util.ArrayList;

public class BondCheck {
    Bond bond;
    Error error;
    Choices choices;
    ArrayList<Integer> calculationList;

    BondCheck(Bond bond, Error error, Choices choices) {
        this.bond = bond;
        this.error = error;
        this.choices = choices;
    }

    public void setBond(Bond bond) {
        this.bond = bond;
    }

    public void setCalculationList(ArrayList<Integer> calculationList) {
        this.calculationList = calculationList;
    }
    public boolean checkBond(String instype) {
        boolean isOk = false;

        isOk = checkNeedInstype();
        if (isOk == false)
            return true;

        isOk = checkDaycount(instype);
        if (!isOk)
            return false;
        isOk = checkInstype(instype);
        if (!isOk)
            return false;
        isOk = checkPayTerms(instype);
        if (!isOk)
            return false;
        isOk = checkSinks(instype);
        if (!isOk)
            return false;
        isOk = checkResetRates(instype);
        if (!isOk)
            return false;
        isOk = checkMortgage(instype);
        if (!isOk)
            return false;
        isOk = checkSettle(instype);
        if (!isOk)
            return false;
        isOk = checkIndexed(instype);
        if (!isOk)
            return false;
        isOk = checkCalls(instype);
        if (!isOk)
            return false;
        isOk = checkReinRts(instype);
        if (!isOk)
            return false;
        isOk = checkCalcRequests(instype);
        return isOk;

    }


    public boolean checkYieldCalcs(int calcNo, String instype) {
        if (choices.inYieldCalcList(calcNo)) {
            if (bond.noDiscRate < Limits.MINDISCGRATES) {
                String errmsg = "at least three points on the discount yield curve must be specified - you entered " + bond.noDiscRate;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        return true;
    }

    public boolean checkNeedInstype() {
        int noCalcs, cNo = -1, calcCount = 0;
        noCalcs = calculationList.size();
        while (++cNo < noCalcs) {
            if (choices.inDocumentList(calculationList)) {
                ++calcCount;
                continue;
            }
            if (choices.inBypassMoreDefaultList(calculationList)) {
                ++calcCount;
                continue;
            }
            if (choices.inBypassMoreDefaultList(calculationList))
                ++calcCount;
        }
        if (calcCount == noCalcs)
            return false;
        return true;
    }


    private boolean checkByPassCalcs(int calcNo) {
       switch (calcNo) {
           case CalcList.SPOTCURVE:
           return true;
           case CalcList.FORWARDCURVE:
       }
       return false;
    }

    public boolean checkCalls(String instype) {

        if (bond.noCallDts != bond.noCallPrcs) {
            String errmsg = "Number of call dates (" + bond.noCallDts + ") is not equal to number of call prices (" + bond.noCallPrcs + ")";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        int ic = -1;
        while (++ic < bond.noCallDts) {
            if (bond.dtMaturity.leDate(bond.dtMaturity, bond.dtCall[ic])) {
                String errmsg = "Invalid specified call date " + bond.dtCall[ic].getDateString() + " is after maturity date " + bond.dtMaturity.getDateString();
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        return true;
    }


    public boolean checkMortgage(String instype) {
        if (!instype.equals(Instype.MORTGAGE))
            return true;
        char use360Yn = 'Y';
        if (UnAvails.isUnAvail(bond.passFactor)) {
            String errmsg = "No passthru factor was specified so cannot calculate analytics";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.dtPassFactor)) {
            String errmsg = "No passthru factor date was specified so cannot calculate analytics";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.passModel)) {
            bond.passModel = Defaults.PASSMODEL;
            String errmsg = "Defaulting passthru model to " + Defaults.PASSMODEL;
            error.setWarning(getClass().getName(), errmsg);
        }

        if (UnAvails.isUnAvail(bond.delaydaysStated)) {
            if (UnAvails.isUnAvail(bond.delaydaysAct)) {
                String errmsg = "No delay delays stated or delay days actual were specified";
                error.setError(getClass().getName(), errmsg);
                return false;
            } else
                bond.delaydaysStated = bond.delaydaysAct + 30;
        } else {
            if (UnAvails.isUnAvail(bond.delaydaysAct))
                bond.delaydaysAct = bond.delaydaysStated - 30;
        }
        if (UnAvails.isUnAvail(bond.dtMaturity)) {
            String errmsg = "No legal maturity date was specfied";
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        return true;
    }


    public boolean checkSettle(String instype) {
        if (UnAvails.isUnAvail(bond.dtSettle)) {
            error.setError(getClass().getName(), "No settle date was specified");
            return false;
        }
        if (bond.dtSettle.geDate(bond.dtSettle, bond.dtMaturity)) {
            error.setError(getClass().getName(), "Invalid settle date of " + bond.dtSettle.getDateString() + " as it must be before maturity date of " + bond.dtMaturity.getDateString());
            return false;
        }
        if (UnAvails.isAvail(bond.dtDated)) {
            if (bond.dtSettle.ltDate(bond.dtSettle, bond.dtDated)) {
                String errmsg = "Settle date " + bond.dtSettle.getDateString() + " is before dated date " + bond.dtDated.getDateString();
                error.setError(getClass().getName(), errmsg);
            }
        }
        return true;
    }

    public boolean checkInstype(String instype) {
        if (UnAvails.isUnAvail(instype)) {
            error.setError(getClass().getName(), "No instrument type was specified");
            return false;
        }
        return true;
    }

    public boolean checkDaycount(String instype) {
        if (calculationList.size() == 1 && calculationList.get(0) == CalcList.DAYCOUNT) {
            if (UnAvails.isUnAvail(bond.dtStart)) {
                String errmsg = "No start date was specified for the daycount calculation";
                error.setError(getClass().getName(), errmsg);
                return false; // not a daycount calc
            }
            if (UnAvails.isUnAvail(bond.dtEnd)) {
                String errmsg = "No end date was specified for the daycount calculation";
                error.setError(getClass().getName(), errmsg);
                return false; // not a daycount calc
            }
            if (UnAvails.isUnAvail(bond.basAccInt)) {
                String errmsg = "No accrual basis was specified for the daycount calculation";
                error.setError(getClass().getName(), errmsg);
                return false; // not a daycount calc
            }
            return false;
        }
        return true;
    }


    public boolean checkPayTerms(String instype) {
        int compFreq = 0;
        if (choices.fixedRateList.contains(bond.instype)) {
            if (UnAvails.isUnAvail(bond.cpnRt)) {
                error.setError(getClass().getName(), "No coupon rate was specified for instrument " + bond.instype);
                return false;
            }
        }
        if (bond.instype.equals("ATMATURITY")) {
            if (UnAvails.isUnAvail(bond.dtDated)) {
                String errmsg = "No dated date was specified for an AtMaturity bond";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (UnAvails.isUnAvail(bond.cpnRt)) {
                String errmsg = "No coupon rate was specified for an AtMaturity bond";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        if (UnAvails.isUnAvail(bond.faceAmt)) {
            String errmsg = "No bond face amount was specified";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (instype.equals(Instype.IRSSWAP)) {
            if (UnAvails.isUnAvail(bond.basPayleg)) {
                error.setError(getClass().getName(), "No interest accrual basis was specified for the pay leg of " + bond.instype);
                return false;
            }
            if (UnAvails.isUnAvail(bond.basRecleg)) {
                error.setError(getClass().getName(), "No interest accrual basis was specified for the receive leg of " + bond.instype);
                return false;
            }
            if (!bond.paylegSchedRule.holidayRule.equals(BondConsts.IGNOREHOLIDAYS)) {
                if (UnAvails.isUnAvail(bond.paylegSchedRule.country)) {
                    String errmsg = "No country was specified for the pay leg payemtn holiday sensitive security";
                    error.setError(getClass().getName(), errmsg);
                    return false;
                }
            }
            if (!bond.reclegSchedRule.holidayRule.equals(BondConsts.IGNOREHOLIDAYS)) {
                if (UnAvails.isUnAvail(bond.reclegSchedRule.country)) {
                    String errmsg = "No country was specified for the receive leg payment holiday sensitive security";
                    error.setError(getClass().getName(), errmsg);
                    return false;
                }
            }
        }

        else {
            if (UnAvails.isUnAvail(bond.basAccInt)) {
                error.setError(getClass().getName(), "No interest accrual basis was specified for " + bond.instype);
                return false;
            }
            String holRule = bond.intSchedRule.holidayRule;
            if (!holRule.equals(BondConsts.IGNOREHOLIDAYS)) {
                if (UnAvails.isUnAvail(bond.country)) {
                    String errmsg = "No country was specified for the interest payment for holiday sensitive security";
                    error.setError(getClass().getName(), errmsg);
                    return false;
                }
            }

        }

        if (UnAvails.isUnAvail(bond.yieldQuoteType))
            bond.yieldQuoteType = CalcEnums.CONPOUNDYIELD;
        return true;
    }


    public boolean checkIndexed(String instype) {
            return true;
    }

    public boolean checkSinks(String instype) {
        int sNo = -1;
        double totPct = 0.;
        while (++sNo < bond.noSinkDts) {
            totPct += bond.sinkPcts[sNo];
            if (bond.dtSinks[sNo].geDate(bond.dtSinks[sNo], bond.dtMaturity)) {
                String errmsg = "Invalid sink date " + bond.dtSinks[sNo].getDateString() + " is on or after maturity date " + bond.dtMaturity.getDateString();
                error.setWarning(getClass().getName(), errmsg);
                return true;
            }

        }
        if (totPct > 1.) {
            String errmsg = "Total sinking funds for " + instype + " is " + totPct * 100. + "% and cannot exceed 100%";
            error.setError(getClass().getName(), errmsg);
            return false;
        }

        return true;
    }

    public boolean checkResetRates(String instype) {
        if (!choices.inFloatList(instype))
            return true;
        boolean isTrue = false;
        if (bond.noResetDts > 0 || bond.noResetRts > 0) {
            if (bond.noResetDts != bond.noResetRts) {
                String errmsg = "No of reset dates " + bond.noResetDts + " is not equal to no of reset rates " + bond.noResetRts;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            bond.useFixingCurveYn = 'N';
            return true;
        }
        if (bond.noFixingRate > 0 || bond.noFixingTenor > 0) {
            if (bond.noFixingTenor != bond.noFixingRate) {
                String errmsg = "No of fixing maturity lengths " + bond.noFixingTenor + " is not equal to no of fixing rates " + bond.noFixingRate;
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            if (bond.noFixingTenor < Limits.MINFIXINGRATES) {
                String errmsg = "No of fixing rates specified was " + bond.noFixingTenor + " you must specify at least " + Limits.MINFIXINGRATES;
                error.setError(getClass().getName(), errmsg);
                return false;
            }

            bond.useFixingCurveYn = 'Y';
            return true;
        }


        return true;

    }

    public boolean checkBeforeDate(Dates dtRequest) {
        boolean okToContinue;
        if (UnAvails.isAvail(bond.dtDated)) {
            okToContinue = !dtRequest.leDate(dtRequest, bond.dtDated);
        } else {
            okToContinue = true;
        }
        return okToContinue;
    }

    public boolean checkAfterDate(Dates dtRequest) {
        boolean okToContinue;
        if (UnAvails.isAvail(bond.dtMaturity)) {
            okToContinue = !dtRequest.geDate(dtRequest, bond.dtMaturity);
        } else {
            okToContinue = true;
        }
        return okToContinue;
    }

    private boolean checkReinRts(String instype) {
        return bond.noReinRts == bond.noReinMatlens;
    }

    private boolean checkCalcRequests(String instype) {
        boolean isOk = true;


        return isOk;
    }
}
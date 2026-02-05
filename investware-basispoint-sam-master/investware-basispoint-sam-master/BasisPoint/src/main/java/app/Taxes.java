package app;

import java.util.ArrayList;

public class Taxes {
    DayCount dayCount;
    double fedIncTax;
    double stateIncTax;
    double fedCapGLShortTax;
    double fedCapGLLongTax;
    int fedDaysCapGlShort;
    int stateDaysCapGlShort;
    char fedTaxExemptYn;
    char stateTaxExemptYn;
    double purPrc;
    Dates dtPur;
    double totalGl;
    double stateCapGLShortTax;
    double stateCapGLLongTax;
    String stateOfIssue;
    String stateOfResidence;

    Taxes(DayCount dayCount) {
        this.dayCount = dayCount;
        this.dtPur = new Dates();
    }

    public void setUnAvail() {
        fedIncTax = UnAvails.NOTAVAILDOUBLE;
        stateIncTax = UnAvails.NOTAVAILDOUBLE;
        fedCapGLShortTax = UnAvails.NOTAVAILDOUBLE;
        fedCapGLLongTax = UnAvails.NOTAVAILDOUBLE;
        fedDaysCapGlShort = UnAvails.NOTAVAILINT;
        stateDaysCapGlShort = UnAvails.NOTAVAILINT;
        fedTaxExemptYn = UnAvails.NOTAVAILCHAR;
        stateTaxExemptYn = UnAvails.NOTAVAILCHAR;
        purPrc = UnAvails.NOTAVAILDOUBLE;
        dtPur.year = UnAvails.NOTAVAILDATE;
        totalGl = UnAvails.NOTAVAILDOUBLE;
        stateCapGLShortTax = UnAvails.NOTAVAILDOUBLE;
        stateCapGLLongTax = UnAvails.NOTAVAILDOUBLE;
        stateOfIssue = UnAvails.NOTAVAILSTRING;
        stateOfResidence = UnAvails.NOTAVAILSTRING;
    }

    public void setDayCount(DayCount dayCount) {
        this.dayCount = dayCount;
    }

    public void setFedTaxRates(double fedIncTax, double fedCapGLShortTax, double fedCapGLLongTax,
            int fedDaysCapGLShort) {
        this.fedIncTax = fedIncTax;
        this.fedCapGLShortTax = fedCapGLShortTax;
        this.fedCapGLLongTax = fedCapGLLongTax;
        this.fedDaysCapGlShort = fedDaysCapGLShort;
        return;

    }

    public void setStateTaxRates(double stateIncTax, double stateCapGLShortTax, double stateCapGLLongTax,
            int stateDaysCapGLShort) {
        this.stateIncTax = stateIncTax;
        this.stateCapGLShortTax = stateCapGLShortTax;
        this.stateCapGLLongTax = stateCapGLLongTax;
        this.stateDaysCapGlShort = stateDaysCapGLShort;
        return;
    }

    public void setPurInfo(double purPrc, Dates dtPur) {
        this.purPrc = purPrc;
        this.dtPur = dtPur;
    }

    public double calcFedIncTax(double intInc) {
        if (fedTaxExemptYn == 'Y')
            return 0.;
        return intInc * fedIncTax;

    }

    public double calcStateIncTax(double intInc) {
        if (stateTaxExemptYn == 'Y')
            return 0.;
        return intInc * stateIncTax;
    }

    public double calcFedCapGainTax(double prinCash, Dates dtCashflow) {
        double tax = 0.;
        double gainLoss = (prinCash - purPrc);
        int daysHeld = dayCount.getDaycountDCACT(dtPur, dtCashflow);
        if (daysHeld > fedDaysCapGlShort)
            tax = gainLoss * fedCapGLLongTax;
        else
            tax = gainLoss * fedCapGLShortTax;
        return tax;
    }

    public double calcStateCapGainTax(double prinCash, Dates dtCashflow) {
        double tax = 0.;
        double gainLoss = (prinCash - purPrc);
        int daysHeld = dayCount.getDaycountDCACT(dtPur, dtCashflow);
        if (daysHeld > stateDaysCapGlShort)
            tax = gainLoss * stateCapGLLongTax;
        else
            tax = gainLoss * stateCapGLShortTax;
        return tax;
    }

}

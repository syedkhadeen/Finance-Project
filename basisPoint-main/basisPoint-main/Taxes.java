
import java.util.ArrayList;

public class Taxes {
    DayCount dayCount;
    double fedIncTaxRt;
    double fedCapGLShortTaxRt;
    double fedCapGLLongTaxRt;
    int fedDaysCapGlShort;
    double stateIncTaxRt;
    int stateDaysCapGlShort;
    double stateCapGLShortTaxRt;
    double stateCapGLLongTaxRt;
    char fedTaxExemptYn;
    char stateTaxExemptYn;


    void setTaxes(DayCount dayCount, Taxes taxes) {
        this.dayCount = dayCount;
        this.fedTaxExemptYn = taxes.fedTaxExemptYn;
        this.fedIncTaxRt = taxes.fedIncTaxRt;
        this.fedCapGLLongTaxRt = taxes.fedCapGLLongTaxRt;
        this.fedDaysCapGlShort = taxes.fedDaysCapGlShort;
        this.stateTaxExemptYn = taxes.stateTaxExemptYn;
        this.stateIncTaxRt = taxes.stateIncTaxRt;
        this.stateCapGLShortTaxRt = taxes.stateCapGLShortTaxRt;
        this.stateCapGLLongTaxRt = taxes.stateCapGLShortTaxRt;
        this.stateDaysCapGlShort = taxes.stateDaysCapGlShort;
    }

    public void clear () {
        fedTaxExemptYn = 'N';
        fedIncTaxRt = 0.;
        fedCapGLShortTaxRt = 0.;
        fedCapGLLongTaxRt = 0.;
        fedDaysCapGlShort = 0;
        stateTaxExemptYn = 'N';
        stateIncTaxRt = 0.;
        stateCapGLShortTaxRt = 0.;
        stateCapGLLongTaxRt = 0.;
        stateDaysCapGlShort = 0;
    }


    public double calcFedIncTaxes(double intInc) {
        double fedTaxPay = 0.;
        if (fedTaxExemptYn == 'N')
            fedTaxPay = (fedIncTaxRt * intInc);
        return fedTaxPay;
    }

    public double calcFedCapGLTaxes(double redeemValue, double purPrc, Dates dtRedeem, Dates dtPur, double stateCapGLTaxes) {
        double fedTaxPay = 0.;
        int heldDays = dayCount.getDaycountDCACT(dtPur, dtRedeem);
        if (heldDays > fedDaysCapGlShort)
            fedTaxPay = fedCapGLLongTaxRt * (redeemValue - purPrc);
        else
            fedTaxPay = fedCapGLShortTaxRt * (redeemValue - purPrc);

        return fedTaxPay;
    }

    public double calcStateIncTaxes(double intInc) {
        double stateTaxPay = 0.;
        if (stateTaxExemptYn == 'N') {
            stateTaxPay = stateIncTaxRt * intInc;
        }
        return stateTaxPay;
    }

    public double calcStateCapGLTaxes(double redeemValue, double purPrc, Dates dtRedeem, Dates dtPur) {
        double stateTaxPay = 0.;
        int heldDays = dayCount.getDaycountDCACT(dtPur, dtRedeem);
        if (heldDays > fedDaysCapGlShort)
            stateTaxPay = fedCapGLLongTaxRt * (redeemValue - purPrc);
        else
            stateTaxPay = fedCapGLShortTaxRt * (redeemValue - purPrc);
        return stateTaxPay;
    }


}





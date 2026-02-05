package app;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CashflowResults {
    // ArrayList<CashflowResults> cashflowResultList = new ArrayList<>();
    int noSecurities; // how many securities contributed to this combined cshflow
    int securityNo; // what is the security number that create this cshflow
    Dates dtCashflow;
    String strDayOfWeek;
    double perLen;
    int matLen;
    double years;
    double totCashflow;
    double totPrinCash;
    double schedPrinCash;
    double prePaycash;
    double intCash;
    double fedTaxPaid;
    double stateTaxPaid;
    double sumTotPrinCashflow;
    double sumTotSchedCashflow;
    double sumTotPrePayCashflow;
    double sumTotIntCashflow;
    double sumTotCashflow;
    int count;
    int cumCount;

    CashflowResults() {
        count = 0;
        cumCount = 0;
        noSecurities = 0;
        int securityNo = 0;
        dtCashflow = new Dates();
        String strDayOfWeek = UnAvails.NOTAVAILSTRING;
        double perLen = 0.;
        int matLen = 0;
        double years = 0.;
        double totCashflow = 0.;
        double totPrinCash = 0.;
        double schedPrinCash = 0.;
        double prePaycash = 0.;
        double intCash = 0;
        double fedTaxPaid = 0.;
        double stateTaxPaid = 0.;
        double sumTotCashflow = 0.;
        double sumTotPrinCashflow = 0.;
        double sumTotSchedCashflow = 0.;
        double sumTotPrePayCashflow = 0.;
        double sumTotIntCashflow = 0.;

    }

    CashflowResults(CashflowResults cfResult) {
        this.count = cfResult.count;
        this.intCash = cfResult.intCash;
        this.dtCashflow = cfResult.dtCashflow;
        this.fedTaxPaid = cfResult.fedTaxPaid;
        this.cumCount = cfResult.cumCount;
        this.noSecurities = cfResult.noSecurities;
        this.perLen = cfResult.perLen;
        this.prePaycash = cfResult.prePaycash;
        this.schedPrinCash = cfResult.schedPrinCash;
        this.stateTaxPaid = cfResult.stateTaxPaid;
        this.strDayOfWeek = cfResult.strDayOfWeek;
        this.totCashflow = cfResult.totCashflow;
        this.totPrinCash = cfResult.totPrinCash;
        this.matLen = cfResult.matLen;
        this.years = cfResult.years;
        this.sumTotCashflow = cfResult.sumTotCashflow;
        this.sumTotPrinCashflow = cfResult.sumTotPrinCashflow;
        this.sumTotSchedCashflow = cfResult.sumTotSchedCashflow;
        this.sumTotPrePayCashflow = cfResult.sumTotPrePayCashflow;
        this.sumTotIntCashflow = cfResult.sumTotIntCashflow;
    }

    CashflowResults(int index, int securityNo, Dates dtCashflow, String strDayOfWeek, double perLen, double years,
            double totCashflow, double totPrincashflow, double schedCashflow, double prePayCashflow, double intCashflow,
            double fedTaxPaid, double stateTaxPaid) {
        this.count = index;
        this.securityNo = securityNo;
        this.dtCashflow = dtCashflow;
        this.strDayOfWeek = strDayOfWeek;
        this.perLen = perLen;
        this.years = years;
        this.totCashflow = totCashflow;
        this.totPrinCash = totPrincashflow;
        this.schedPrinCash = schedCashflow;
        this.prePaycash = prePayCashflow;
        this.intCash = intCashflow;
        this.fedTaxPaid = fedTaxPaid;
        this.stateTaxPaid = stateTaxPaid;
        ++this.count;
    }

    public void zero() {
        this.dtCashflow = new Dates();
        this.years = 0.;
        this.perLen = 0.;
        this.schedPrinCash = 0.;
        this.prePaycash = 0.;
        this.intCash = 0;
        this.fedTaxPaid = 0.;
        this.stateTaxPaid = 0.;
        this.totPrinCash = 0.;
        this.totCashflow = 0.;
        // this.dtCashflow.setUnavail();
    }

}

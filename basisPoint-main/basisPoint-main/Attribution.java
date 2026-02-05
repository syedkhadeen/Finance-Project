public class Attribution {
    double mktValue;
    double totRtn; // mktPrcStart * (1. totRtn / freq) ** noper = mktPriceEnd
    double rtnTime; // mktPrcStart * (1. totRtnTime / freq) ** noper = presValTime
    double rtnRates; // mktPrcStart * (1. totRtnChgRate / freq) ** noper = presValChgRates
    double rtnCrdSprd; // mktPrcStart * (1. totRtnChgRate / freq) ** noper = presValChgCrdSprd
    double rtnSecurity; // totRtnChgSecurity = totRtn - totRtTime - totRtnChgRates - totRtnChgCrdSprd


Attribution() {

}

Attribution (Attribution attribution) {
    this.mktValue = attribution.mktValue;
    this.rtnSecurity = attribution.rtnSecurity;
    this.rtnCrdSprd = attribution.rtnCrdSprd;
    this.rtnTime = attribution.rtnTime;
    this.rtnRates = attribution.rtnRates;
    this.totRtn = attribution.totRtn;
}

}


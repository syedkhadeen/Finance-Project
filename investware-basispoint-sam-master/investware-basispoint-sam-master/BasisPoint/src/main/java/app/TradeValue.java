package app;

class TradeValue {
    boolean inSwap = false;
    int tradeId;
    String creditAgency;
    String credtRating;
    String instype;
    double faceAmt;
    double currYield;
    double futYield;
    double futDuration;
    double currDuration;
    double futConvexity;
    double currConvexity;
    double futMatLen;
    double currMatLen;
    double futValue;
    double currValue;
    double futPrc;
    double currPrc;
    double futValueDnSide;
    double futValueStdDev;
    double futValueUpSide;
    double rateReturnExp;
    double rateReturnDnSide;
    double rateReturnUpSide;
    double riskReturnValue;
    double sharpRatio;
    double sharpStdDev;
    double currNetPay;
    double futNetPay;
    double currAi;
    double futAi;
    String secDescr;
    String tradeType;

    TradeValue() {
    }

    TradeValue(TradeValue tradeValue) {
        this.inSwap = tradeValue.inSwap;
        this.instype = tradeValue.instype;
        this.secDescr = tradeValue.secDescr;
        this.creditAgency = tradeValue.creditAgency;
        this.credtRating = tradeValue.credtRating;
        this.currAi = tradeValue.currAi;
        this.futAi = tradeValue.futAi;
        this.futMatLen = tradeValue.futMatLen;
        this.currMatLen = tradeValue.currMatLen;
        this.tradeType = tradeValue.tradeType;
        this.futYield = tradeValue.futYield;
        this.currYield = tradeValue.currYield;
        this.rateReturnExp = tradeValue.rateReturnExp;
        this.rateReturnDnSide = tradeValue.rateReturnDnSide;
        this.rateReturnUpSide = tradeValue.rateReturnUpSide;
        this.riskReturnValue = tradeValue.riskReturnValue;
        this.faceAmt = tradeValue.faceAmt;
        this.instype = tradeValue.instype;
        this.sharpRatio = tradeValue.sharpRatio;
        this.currConvexity = tradeValue.currConvexity;
        this.futConvexity = tradeValue.futConvexity;
        this.futNetPay = tradeValue.futNetPay;
        this.currNetPay = tradeValue.currNetPay;
        this.futDuration = tradeValue.futDuration;
        this.currDuration = tradeValue.currDuration;
        this.futPrc = tradeValue.futPrc;
        this.currPrc = tradeValue.currPrc;
        this.instype = tradeValue.instype;
        this.sharpRatio = tradeValue.sharpRatio;
        this.futValue = tradeValue.futValue;
        this.currValue = tradeValue.currValue;
        this.futValueDnSide = tradeValue.futValueDnSide;
        this.futValueStdDev = tradeValue.futValueStdDev;
        this.futValueUpSide = tradeValue.futValueUpSide;
        this.faceAmt = tradeValue.faceAmt;
        this.sharpStdDev = tradeValue.sharpStdDev;
        this.tradeType = tradeValue.tradeType;
        this.tradeId = tradeValue.tradeId;
    }

}
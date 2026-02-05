import java.util.Comparator;

public class TradeEval {
    Dates dtStart;
    Dates dtFuture;
    TradeValue buyTrade;
    TradeValue sellTrade;
    int buyId;
    int sellId;
    double futValueNeutralBuyFaceAmt;
    double deltaFutValue;
    double deltaFutValueDn;
    double deltaFutValueUp;
    double deltaRiskReturnValue;
    double cashNeutralBuyFaceAmt;
    double brkEvenSwapCash; // if just match future value of sell
    double brkEvenPrc;
    boolean neutralSwap;
    TradeEval() {
    }

    TradeEval(TradeEval tradeEval) {
        this.buyTrade = tradeEval.buyTrade;
        this.sellTrade = tradeEval.sellTrade;
        this.neutralSwap = tradeEval.neutralSwap;
        this.cashNeutralBuyFaceAmt = tradeEval.cashNeutralBuyFaceAmt;
        this.futValueNeutralBuyFaceAmt = tradeEval.futValueNeutralBuyFaceAmt;
        this.brkEvenSwapCash = tradeEval.brkEvenSwapCash;
        this.deltaRiskReturnValue= tradeEval.deltaRiskReturnValue;
        this.deltaFutValue= tradeEval.deltaFutValue;
        this.deltaFutValueDn = tradeEval.deltaFutValueDn;
        this.deltaFutValueUp = tradeEval.deltaFutValueUp;
        this.brkEvenPrc = tradeEval.brkEvenPrc;

               /*
        this.buyCreditRating = tradeEval.buyCreditRating;
        this.sellCreditRating = tradeEval.sellCreditRating;
        this.brkEvenPrc = tradeEval.brkEvenPrc;
        this.buyDuration = tradeEval.buyDuration;
        this.buyFutValueDnSide = tradeEval.buyFutValueDnSide;
        this.buyFutValueUpSide = tradeEval.buyFutValueUpSide;
        this.buyTradeId = tradeEval.buyTradeId;
        this.buyPrc = tradeEval.buyPrc;
        this.buyPar = tradeEval.buyPar;
        this.buyConvexity = tradeEval.buyConvexity;
        this.buyMatlen = tradeEval.buyMatlen;
        this.buyAi = tradeEval.buyAi;
        this.buyNetPay = tradeEval.buyNetPay;
        this.buyFutYield = tradeEval.buyFutYield;
        this.buyReturn = tradeEval.buyReturn;
        this.buyReturnDnSide = tradeEval.buyReturnDnSide;
        this.buyReturnUpSide = tradeEval.buyReturnUpSide;
        this.buySecDescr = tradeEval.buySecDescr;
        this.neutralSwap = tradeEval.neutralSwap;
        this.sellDuration = tradeEval.sellDuration;
        this.sellFutValueDnSide = tradeEval.sellFutValueDnSide;
        this.sellFutValueUpSide = tradeEval.sellFutValueUpSide;
        this.sellPrc = tradeEval.sellPrc;
        this.sellConvexity = tradeEval.sellConvexity;
        this.sellMatlen = tradeEval.sellMatlen;
        this.sellPar = tradeEval.sellPar;
        this.sellAi = tradeEval.sellAi;
        this.sellNetPay = tradeEval.sellNetPay;
        this.sellFutYield = tradeEval.sellFutYield;
        this.sellReturn = tradeEval.sellReturn;
        this.sellReturnDnSide = tradeEval.sellReturnDnSide;
        this.sellReturnUpSide = tradeEval.sellReturnUpSide;
        this.sellSecDescr = tradeEval.sellSecDescr;
        this.sellTradeId = tradeEval.sellTradeId;
        this.buyRiskReturnRatio = tradeEval.buyRiskReturnRatio;
        this.sellRiskReturnRatio = tradeEval.sellRiskReturnRatio;
        this.swapReturnRiskRatio = tradeEval.swapReturnRiskRatio;
        this.rankNeutral = tradeEval.rankNeutral;
        this.deltaReturn = tradeEval.deltaReturn;
        this.deltaDuration = tradeEval.deltaDuration;
        this.buySharpRatio = tradeEval.buySharpRatio;
        this.sellSharpRatio = tradeEval.sellSharpRatio;
        this.deltaSharpRatio = tradeEval.deltaSharpRatio;
        this.tradeType = tradeEval.tradeType;
        */

    }

    public void clear() {
        this.dtStart.clear();
        this.dtFuture.clear();
        buyTrade.clear();
        sellTrade.clear();
        int buyId = UnAvails.NOTAVAILINT;
        int sellId = UnAvails.NOTAVAILINT;
        double futValueNeutralBuyFaceAmt;
        double deltaFutValue = 0.;
        double deltaFutValueDn = 0.;
        double deltaFutValueUp = 0.;
        double deltaRiskReturnValue = 0.;
        double cashNeutralBuyFaceAmt = 0.;
        double swapCash = 0.; // if just match future value of sell
        double brkEvenPrc = 0.;
        boolean neutralSwap = false;
    }


}
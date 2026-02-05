package app;

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
  double deltaRiskReturnValue;
  double cashNeutralBuyFaceAmt;
  double swapCash; // if just match future value of sell
  double brkEvenPrc;
  boolean neutralSwap;

  /*
   * String buyInstype;
   * String buySecRequest;
   * int buyTradeId;
   * String portfolioName;
   * String buyCreditRating;
   * String sellCreditRating;
   * double buyRiskReturnRatio;
   * double sellRiskReturnRatio;
   * double buySharpRatio;
   * double sellSharpRatio;
   * double deltaSharpRatio;
   * double brkEvenPrc;
   * double buyDuration;
   * double buyFutValue;
   * double buyFutValueDnSide;
   * double buyFutValueStdDev;
   * double buyFutValueUpSide;
   * double buyPar;
   * double buyPrc;
   * double buyConvexity;
   * double buyMatlen;
   * double buyFutYield;
   * double buyAi;
   * double buyNetPay;
   * double buyReturn;
   * double buyReturnDnSide;
   * double buyReturnUpSide;
   * String buySecDescr;
   * boolean neutralSwap;
   * String sellInstype;
   * double sellDuration;
   * double sellFutValue;
   * double sellFutValueDnSide;
   * double sellFutValueUpSide;
   * double sellFutValueStdDev;
   * double sellPrc;
   * double sellPar;
   * double sellConvexity;
   * double sellMatlen;
   * double sellAi;
   * double sellFutYield;
   * double sellNetPay;
   * double sellReturn;
   * double sellReturnDnSide;
   * double sellReturnUpSide;
   * String sellSecDescr;
   * int sellTradeId;
   * double swapReturnRiskRatio;
   * String tradeType;
   * double deltaReturn;
   * double deltaDuration;
   * int rankNeutral;
   * TradeValue tradeValue = new TradeValue();
   */
  TradeEval() {
  }

  TradeEval(TradeEval tradeEval) {
    this.buyTrade = tradeEval.buyTrade;
    this.sellTrade = tradeEval.sellTrade;
    this.neutralSwap = tradeEval.neutralSwap;
    this.cashNeutralBuyFaceAmt = tradeEval.cashNeutralBuyFaceAmt;
    this.futValueNeutralBuyFaceAmt = tradeEval.futValueNeutralBuyFaceAmt;
    this.swapCash = tradeEval.swapCash;
    /*
     * this.buyCreditRating = tradeEval.buyCreditRating;
     * this.sellCreditRating = tradeEval.sellCreditRating;
     * this.brkEvenPrc = tradeEval.brkEvenPrc;
     * this.buyDuration = tradeEval.buyDuration;
     * this.buyFutValueDnSide = tradeEval.buyFutValueDnSide;
     * this.buyFutValueUpSide = tradeEval.buyFutValueUpSide;
     * this.buyTradeId = tradeEval.buyTradeId;
     * this.buyPrc = tradeEval.buyPrc;
     * this.buyPar = tradeEval.buyPar;
     * this.buyConvexity = tradeEval.buyConvexity;
     * this.buyMatlen = tradeEval.buyMatlen;
     * this.buyAi = tradeEval.buyAi;
     * this.buyNetPay = tradeEval.buyNetPay;
     * this.buyFutYield = tradeEval.buyFutYield;
     * this.buyReturn = tradeEval.buyReturn;
     * this.buyReturnDnSide = tradeEval.buyReturnDnSide;
     * this.buyReturnUpSide = tradeEval.buyReturnUpSide;
     * this.buySecDescr = tradeEval.buySecDescr;
     * this.neutralSwap = tradeEval.neutralSwap;
     * this.sellDuration = tradeEval.sellDuration;
     * this.sellFutValueDnSide = tradeEval.sellFutValueDnSide;
     * this.sellFutValueUpSide = tradeEval.sellFutValueUpSide;
     * this.sellPrc = tradeEval.sellPrc;
     * this.sellConvexity = tradeEval.sellConvexity;
     * this.sellMatlen = tradeEval.sellMatlen;
     * this.sellPar = tradeEval.sellPar;
     * this.sellAi = tradeEval.sellAi;
     * this.sellNetPay = tradeEval.sellNetPay;
     * this.sellFutYield = tradeEval.sellFutYield;
     * this.sellReturn = tradeEval.sellReturn;
     * this.sellReturnDnSide = tradeEval.sellReturnDnSide;
     * this.sellReturnUpSide = tradeEval.sellReturnUpSide;
     * this.sellSecDescr = tradeEval.sellSecDescr;
     * this.sellTradeId = tradeEval.sellTradeId;
     * this.buyRiskReturnRatio = tradeEval.buyRiskReturnRatio;
     * this.sellRiskReturnRatio = tradeEval.sellRiskReturnRatio;
     * this.swapReturnRiskRatio = tradeEval.swapReturnRiskRatio;
     * this.rankNeutral = tradeEval.rankNeutral;
     * this.deltaReturn = tradeEval.deltaReturn;
     * this.deltaDuration = tradeEval.deltaDuration;
     * this.buySharpRatio = tradeEval.buySharpRatio;
     * this.sellSharpRatio = tradeEval.sellSharpRatio;
     * this.deltaSharpRatio = tradeEval.deltaSharpRatio;
     * this.tradeType = tradeEval.tradeType;
     */

  }

}
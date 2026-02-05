import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.security.Key;
import java.text.DecimalFormat;
import java.util.*;

public class TradeEvals {
    Error error;
    CommandLine cmdLine;
    DayCount dayCount;
    Utility utility;
    ArrayList<TradeValue> buyTrades = new ArrayList<>();
    ArrayList<TradeValue> sellTrades = new ArrayList<>();  // swaps meeting a goal
    ArrayList<TradeValue> swapTrades = new ArrayList<>(); // all possible swap trade
    ArrayList<TradeValue> swapPortfolio = new ArrayList<>(); // portfolio holdings after swap
    double swapBuyLimitPct = Defaults.MAXSWAPBUYPCT * .01;
    Dates dtStart;
    Dates dtFuture;
    String strYrMoDay;
    String portfolioName;
    String instype;
    String riskReturnGoal;
    double swapPctGain;
    int noSwaps = 0;
    int noBuys = 0;
    int noSells = 0;
    int noNeutral = 0;
    int noPickups = 0;
    int noEvaluated = 0;

    TradeEvals(Error error, CommandLine cmdLine, DayCount dayCount, Utility utility) {
        this.error = error;
        this.cmdLine = cmdLine;
        this.dayCount = dayCount;
        this.utility = utility;
    }

    public void setRiskReturnGoal(String riskReturnGoal, double swapPctGain) {
        this.riskReturnGoal = riskReturnGoal;
        this.swapPctGain = swapPctGain;
    }

    public ArrayList<TradeValue> calcSwapTrades(ArrayList<TradeValue> tradeValues, Dates dtSettle, Dates dtFuture) {
        ArrayList<TradeValue> tradeList = new ArrayList<>();
        TradeEval tradeEval = new TradeEval();
        tradeEval.buyTrade = new TradeValue();
        tradeEval.sellTrade = new TradeValue();
        int freq = CalUnits.SEMIANNUALFQ;
        int nt = -1, nb = 0, ns = 0;
        int nTot = tradeValues.size();
        if (nTot == 0) {
            String errmsg = "No buy or sell trades were supplied for trade opportunity evaluation";
            error.setError(getClass().getName(), errmsg);
            return tradeList;
        }
        while (++nt < nTot) {
            if (tradeValues.get(nt).tradeType.equals(BondConsts.BUY)) {
                buyTrades.add(nb++, new TradeValue(tradeValues.get(nt)));
            } else {
                sellTrades.add(ns++, new TradeValue(tradeValues.get(nt)));
            }
        }
        noBuys = nb;
        noSells = ns;
        switch (riskReturnGoal) {
            case Codes.MAXSHARPRATIO:
                sortTradesBySharpRatio(buyTrades, false);
                sortTradesBySharpRatio(sellTrades, true);
                tradeList = setTradeListMaxSharpRatio(buyTrades, sellTrades);
                break;
            case Codes.MAXEXPRETURN:
                sortTradesByReturnExp(buyTrades, false);
                sortTradesByReturnExp(sellTrades, true);
                setMaxReturnTrades(buyTrades, sellTrades);
                sortByTradeId(swapTrades, true);
                swapTrades = combineSwaps(swapTrades);
                swapPortfolio = combineSwaps(swapPortfolio);
                break;
            case Codes.MAXDNSIDERETURN:
                sortTradesByReturnDn(buyTrades, false);
                sortTradesByReturnDn(sellTrades, true);
                tradeList = setTradeListMaxDnSideReturn(buyTrades, sellTrades);
                break;
            case Codes.MAXSUPSIDERETURN:
                sortTradesByReturnUp(buyTrades, false);
                sortTradesByReturnUp(sellTrades, true);
                // tradeList = setTradeListMaxExpReturn(buyTrades, sellTrades);
                break;
            case Codes.MAXFUTUREVALUE:
                sortTradesByReturnExp(buyTrades, false);
                //    tradeList = setTradeListMaxExpReturn(buyTrades, sellTrades);
                break;
            case Codes.MINFUTUREVALUELOSS:
                sortTradesByReturnExp(sellTrades, true);
                //  tradeList = setTradeListMaxExpReturn(buyTrades, sellTrades);
                break;
            default:
                sortTradesByReturnExp(buyTrades, false);
                sortTradesByReturnExp(sellTrades, true);
                //     tradeList = setTradeListMaxExpReturn(buyTrades, sellTrades);
                break;
        }

        // create the trade opps
        // create buy list trade values and sell list trade values
        // we several buy may want to sell same sell.
        noSwaps = swapTrades.size();
        // sorted
        return swapTrades;
    }


    public void setAssumptions(Dates dtStart, Dates dtFuture, String portfolioName) {
        this.dtStart = dtStart;
        this.dtFuture = dtFuture;
        this.portfolioName = portfolioName;
        int yrs = dtFuture.year - dtStart.year;
        int mons = dtFuture.month - dtStart.month;
        if (mons < 0) {
            --yrs;
            mons = 12 + mons;
        }
        int days = dtFuture.day - dtStart.day;
        if (days < 0) {
            days = 30 + days;
        }
        String strYrs = "";
        if (yrs > 0)
            strYrs = yrs + "yrs";
        String strMons = "";
        if (mons > 0)
            strMons = mons + "mos";
        String strDays = "";
        if (days > 0)
            strDays = days + "dys";
        if (!strYrs.isEmpty()) {
            if (!strMons.isEmpty()) {
                if (!strDays.isEmpty()) {
                    strYrMoDay = strYrs + "-" + strMons + "-" + strDays;
                } else {
                    strYrMoDay = strYrs + "-" + strMons;
                    ;
                }
            } else {
                if (!strDays.isEmpty()) {
                    strYrMoDay = strYrs + "-" + strDays;
                } else {
                    strYrMoDay = strYrs;
                }
            }
        } else {
            if (!strMons.isEmpty()) {
                if (!strDays.isEmpty()) {
                    strYrMoDay = strMons + "-" + strDays;
                } else {
                    strYrMoDay = strMons;
                }
            } else {
                strYrMoDay = strDays;
            }
        }
        return;
    }

    ArrayList<TradeValue> setTradeListMaxSharpRatio(ArrayList<TradeValue> buyTrades, ArrayList<TradeValue> sellTrades) {
        TradeEval tradeEval = new TradeEval();
        int noBuys = buyTrades.size();
        int noSells = sellTrades.size();

        noEvaluated = noBuys * noSells;
        // perLen cannot be zero - need to check in calcCheck
        int nb = 0;
        int ns = 0;
        int nt = 0;
        while (nb < noBuys) {
            if (buyTrades.get(nb).instype.equals("CURRENCY")) {
                ++nb;
                break;
            }
            ns = 0;
            while (ns < noSells) {
                tradeEval.buyTrade = buyTrades.get(nb);
                tradeEval.sellTrade = sellTrades.get(ns);
                tradeEval.buyTrade.inSwap = false;
                tradeEval.sellTrade.inSwap = false;
                tradeEval.buyTrade = buyTrades.get(nb);
                tradeEval.sellTrade = sellTrades.get(ns);
                calcSwapStats(tradeEval, riskReturnGoal);
                if (sellTrades.get(ns).instype.equals("CURRENCY")) {
                    ++ns;
                    break;
                }
                //swapTrades.add(nt++, new TradeEval(tradeEval));
                ++ns;
            }
            ++nb;
        }
        return sellTrades;
    }

    ArrayList<TradeValue> setMaxReturnTrades(ArrayList<TradeValue> buyTrades, ArrayList<TradeValue> sellTrades) {
        // sell worst and buy best
        TradeEval tradeEval = new TradeEval();
        TradeValue buyTrade = new TradeValue();
        int noSells = sellTrades.size();
        int noBuys = buyTrades.size();
        int ns = -1, nb = 0, nt = 0, np = 0;
        double sellCash = 0.;
        noPickups = 0;
        while (++ns < noSells) {
            sellCash += sellTrades.get(ns).currNetPay;
        }
        double buyCashLimit = swapBuyLimitPct * sellCash;
        double buyCashAmt = 0.;
        TradeValue nextSell = sellTrades.get(0);
        TradeValue nextBuy = buyTrades.get(0);
        TradeValue buySwap = new TradeValue();
        ns = -1;
        while (++ns < noSells) {
            nextSell = sellTrades.get(ns);
            double deltaReturn = nextBuy.rateReturnExp - nextSell.rateReturnExp;
            if (deltaReturn > 0.) {
                ++noPickups;
                double capacity = buyCashLimit - buyCashAmt;
                if (nextSell.currNetPay > capacity) { // need to use next buy
                    if (nb == noBuys - 1) {
                        return sellTrades;//
                    } // is there a next buy to trade?
                    nextBuy = buyTrades.get(++nb);
                    buyCashAmt = 0.;// get next buy
                } // swap current sell for current buy
                buyCashAmt += nextSell.currNetPay;
                tradeEval.buyTrade = nextBuy;
                tradeEval.sellTrade = nextSell;
                tradeEval.deltaRiskReturnValue = deltaReturn;
                // System.out.println("Next Buy Pre: " + nextBuy.secDescr);
                calcSwapStats(tradeEval, riskReturnGoal);
                // System.out.println("Next Buy Post: " + nextBuy.secDescr);
                nextBuy.faceAmt = tradeEval.buyTrade.faceAmt;
                nextBuy.currNetPay = nextSell.currNetPay;
                nextBuy.brkEvenCash = tradeEval.brkEvenSwapCash;
                nextBuy.amtTraded = nextBuy.faceAmt;
                nextBuy.inSwap = true;
                buySwap.setTradeValue(nextBuy);
                buySwap.brkEvenPrc = 100. * tradeEval.brkEvenPrc;
                buySwap.currYield *= 100.;
                buySwap.futYield *= 100.;
                buySwap.currPrc *= 100.;
                buySwap.futPrc *= 100.;
                buySwap.currAi *= nextBuy.faceAmt;
                buySwap.currValue *= nextBuy.faceAmt;
                buySwap.futValue *= nextBuy.faceAmt;
                buySwap.futValueDnSide *= nextBuy.faceAmt;
                buySwap.futValueUpSide *= nextBuy.faceAmt;
                buySwap.futNetPay *= nextBuy.faceAmt;
                buySwap.secDescr = nextBuy.secDescr;
                //
                nextSell.brkEvenPrc = 100. * tradeEval.brkEvenPrc;
                nextSell.brkEvenCash = tradeEval.brkEvenSwapCash;
                nextSell.currPrc *= 100.;
                nextSell.futPrc *= 100.;
                nextSell.currYield *= 100.;
                nextSell.futYield *= 100.;
                swapTrades.add(nt++, new TradeValue(buySwap));
                // System.out.println("Next Buy Post: " + swapTrades.get(nt - 1).secDescr);
                swapTrades.add(nt++, new TradeValue(nextSell));
                swapPortfolio.add(np++, new TradeValue(buySwap));
            } else {
                nextSell.inSwap = false;
                nextSell.currPrc *= 100.;
                nextSell.futPrc *= 100.;
                nextSell.currYield *= 100.;
                nextSell.futYield *= 100.;
                swapPortfolio.add(np++, new TradeValue(nextSell));
            }
        }
        return swapTrades;
    }

    ArrayList<TradeValue> setTradeListMaxDnSideReturn(ArrayList<TradeValue> buyTrades, ArrayList<TradeValue> sellTrades) {
        TradeEval tradeEval = new TradeEval();
        ArrayList<TradeEval> tradeList = new ArrayList<>();
        int noBuys = buyTrades.size();
        int noSells = sellTrades.size();

        noEvaluated = noBuys * noSells;
        // perLen cannot be zero - need to check in calcCheck
        int nb = 0;
        int ns = 0;
        int nt = 0;
        while (nb < noBuys) {
            if (buyTrades.get(nb).instype.equals("CURRENCY")) {
                ++nb;
                break;
            }
            ns = 0;
            while (ns < noSells) {
                tradeEval.buyTrade = buyTrades.get(nb);
                tradeEval.sellTrade = sellTrades.get(ns);
                tradeEval.buyTrade.inSwap = false;
                tradeEval.sellTrade.inSwap = false;
                tradeEval.buyTrade = buyTrades.get(nb);
                tradeEval.sellTrade = sellTrades.get(ns);
                calcSwapStats(tradeEval, riskReturnGoal);
                if (sellTrades.get(ns).instype.equals("CURRENCY")) {
                    ++ns;
                    break;
                }
                //swapTrades.add(nt++, new TradeEval(tradeEval));
                ++ns;
            }
            ++nb;
        }
        return sellTrades;
    }

    ArrayList<TradeEval> setTradeListMaxUpSideReturn(ArrayList<TradeValue> buyTrades, ArrayList<TradeValue> sellTrades) {
        TradeEval tradeEval = new TradeEval();
        ArrayList<TradeEval> tradeList = new ArrayList<>();
        int noBuys = buyTrades.size();
        int noSells = sellTrades.size();

        noEvaluated = noBuys * noSells;
        // perLen cannot be zero - need to check in calcCheck
        int nb = 0;
        int ns = 0;
        int nt = 0;
        while (nb < noBuys) {
            if (buyTrades.get(nb).instype.equals("CURRENCY")) {
                ++nb;
                break;
            }
            ns = 0;
            while (ns < noSells) {
                tradeEval.buyTrade = buyTrades.get(nb);
                tradeEval.sellTrade = sellTrades.get(ns);
                tradeEval.buyTrade.inSwap = false;
                tradeEval.sellTrade.inSwap = false;
                tradeEval.buyTrade = buyTrades.get(nb);
                tradeEval.sellTrade = sellTrades.get(ns);
                calcSwapStats(tradeEval, riskReturnGoal);
                if (sellTrades.get(ns).instype.equals("CURRENCY")) {
                    ++ns;
                    break;
                }
                tradeList.add(nt++, new TradeEval(tradeEval));
                ++ns;
            }
            ++nb;
        }
        return tradeList;
    }

    private boolean isNeutralDurationSwap(TradeEval tradeEval) {
        int month = (int) tradeEval.sellTrade.currDuration * CalUnits.MONTHSPERYEAR;
        int[] rng = new int[8];
        rng[0] = 2;
        rng[1] = 4;
        rng[2] = 6;
        rng[3] = 12;
        rng[4] = 18;
        rng[5] = 24;
        rng[6] = 36;
        double maxDurDiff = 2.;
        if (month <= 12)
            maxDurDiff = rng[0];
        if (month > 12 && month <= 36)
            maxDurDiff = rng[1];
        if (month > 36 && month <= 60)
            maxDurDiff = rng[2];
        if (month > 60 && month <= 84)
            maxDurDiff = rng[3];
        if (month > 84 && month <= 120)
            maxDurDiff = rng[4];
        if (month > 120 && month < 240)
            maxDurDiff = rng[5];
        if (month > 240)
            maxDurDiff = rng[6];
        double durDiff = tradeEval.sellTrade.currDuration - tradeEval.buyTrade.currDuration;
        durDiff = durDiff < 0. ? -durDiff : durDiff;
        if (durDiff <= maxDurDiff)
            return true;
        return false;
    }

    private ArrayList<TradeValue> combineSwaps(ArrayList<TradeValue> tradeValueList) {
        ArrayList<TradeValue> trades = new ArrayList<>();
        TradeValue totTrade = new TradeValue();
        TradeValue lastTrade = new TradeValue();
        TradeValue currTrade = new TradeValue();
        int count = tradeValueList.size();
        int no = 0;
        String lastType = tradeValueList.get(0).tradeType;
        double totAmt = tradeValueList.get(0).amtTraded;
        double totFutVal = tradeValueList.get(0).futValue;
        double totFutValDn = tradeValueList.get(0).futValueDnSide;
        double totFutValUp = tradeValueList.get(0).futValueUpSide;
        double totCurrVal = tradeValueList.get(0).faceAmt * .01 * tradeValueList.get(0).currPrc;
        double totBrkEvenCash = tradeValueList.get(0).brkEvenCash;
        double totBrkEvenPrc = tradeValueList.get(0).brkEvenPrc * tradeValueList.get(0).faceAmt;
        lastTrade = tradeValueList.get(0);
        int nt = 0;
        while (++no < count) {
            currTrade =tradeValueList.get(no);
            if (currTrade.tradeId == 1)
                currTrade.tradeId = 1;
            if (currTrade.tradeId == lastTrade.tradeId) {
                totAmt += currTrade.faceAmt;
                totFutVal += currTrade.futValue;
                totFutValDn += currTrade.futValueDnSide;
                totFutValUp += currTrade.futValueUpSide;
                totCurrVal += currTrade.faceAmt * .01 * tradeValueList.get(no).currPrc;
                totBrkEvenCash += currTrade.brkEvenCash;
                totBrkEvenPrc += currTrade.brkEvenPrc * tradeValueList.get(no).faceAmt;
                lastTrade = currTrade;
            } else {
                totTrade.tradeType = lastTrade.tradeType;
                totTrade.tradeId = lastTrade.tradeId;
                totTrade.currPrc = lastTrade.currPrc;
                totTrade.secDescr = lastTrade.secDescr;
                totTrade.credtRating = lastTrade.credtRating;
                totTrade.faceAmt = lastTrade.faceAmt + totAmt ;
                totTrade.amtTraded = lastTrade.faceAmt;
                totTrade.currValue = lastTrade.currValue + totCurrVal;
                totTrade.futPrc = lastTrade.futPrc;
                totTrade.futValue = lastTrade.futValue + totFutVal;
                totTrade.futValueDnSide = lastTrade.futValueDnSide + totFutValDn;
                totTrade.futValueUpSide = lastTrade.futValueUpSide + totFutValUp;
                totTrade.rateReturnExp = lastTrade.rateReturnExp;
                totTrade.rateReturnDnSide = lastTrade.rateReturnDnSide;
                totTrade.rateReturnUpSide = lastTrade.rateReturnUpSide;
                totTrade.brkEvenCash = lastTrade.brkEvenCash + totBrkEvenCash;
                totTrade.brkEvenPrc = (lastTrade.brkEvenPrc* lastTrade.faceAmt +  totBrkEvenPrc) / totTrade.faceAmt;
                totTrade.currYield = lastTrade.currYield;
                totTrade.futYield = lastTrade.futYield;
                totTrade.currDuration = lastTrade.currDuration;
                totTrade.futDuration = lastTrade.futDuration;
                totTrade.currMatLen = lastTrade.currMatLen;
                totTrade.futMatLen = lastTrade.futMatLen;
                totTrade.currConvexity =  lastTrade.currConvexity;
                totTrade.futConvexity = lastTrade.futConvexity;
                trades.add(nt++, new TradeValue(totTrade));
                totAmt = tradeValueList.get(0).amtTraded;
                 totFutVal = currTrade.futValue;
                 totFutValDn = currTrade.futValueDnSide;
                 totFutValUp = currTrade.futValueUpSide;
                 totCurrVal = currTrade.faceAmt * .01 * currTrade.currPrc;
                 totBrkEvenCash = currTrade.brkEvenCash;
                 totBrkEvenPrc = currTrade.brkEvenPrc * currTrade.faceAmt;
                lastTrade = currTrade;
            }
        }
        if (lastTrade.tradeId == currTrade.tradeId) {
            totTrade.tradeType = currTrade.tradeType;
            totTrade.tradeId = currTrade.tradeId;
            totTrade.currPrc = currTrade.currPrc;
            totTrade.secDescr = currTrade.secDescr;
            totTrade.credtRating = currTrade.credtRating;
            totTrade.faceAmt = currTrade.faceAmt + totAmt;
            totTrade.amtTraded = totTrade.faceAmt;
            totTrade.currValue = currTrade.currValue + totCurrVal;
            totTrade.futPrc = currTrade.futPrc;
            totTrade.futValue = currTrade.futValue + totFutVal;
            totTrade.futValueDnSide = currTrade.futValueDnSide + totFutValDn;
            totTrade.futValueUpSide = currTrade.futValueUpSide + totFutValUp;
            totTrade.rateReturnExp = currTrade.rateReturnExp;
            totTrade.rateReturnDnSide = currTrade.rateReturnDnSide;
            totTrade.rateReturnUpSide = currTrade.rateReturnUpSide;
            totTrade.brkEvenCash = currTrade.brkEvenCash + totBrkEvenCash;
            totTrade.brkEvenPrc = (currTrade.brkEvenPrc * currTrade.faceAmt + totBrkEvenPrc) / totTrade.faceAmt;
            totTrade.currYield = currTrade.currYield;
            totTrade.futYield = currTrade.futYield;
            totTrade.currDuration = currTrade.currDuration;
            totTrade.futDuration = currTrade.futDuration;
            totTrade.currMatLen = currTrade.currMatLen;
            totTrade.futMatLen = currTrade.futMatLen;
            totTrade.currConvexity = currTrade.currConvexity;
            totTrade.futConvexity = currTrade.futConvexity;
            trades.add(nt++, new TradeValue(totTrade));
        }
        swapTrades = trades;
        return trades;
    }

    public ConsoleFileResults outputSwapResultsConsole() {
        String consoleStr;
        CommandLine cl = cmdLine;
        ConsoleFileResults cfrCalc = new ConsoleFileResults();
        if (error.hasErrorYn == 'Y') {
            error.setError(getClass().getName(), CalcList.SWAPANALYSIS, error.errorMessage);
            cfrCalc.consoleStr = error.errorMessage;
            return cfrCalc;
        }
        // [{"SwapResults": "SwapSummary",
        consoleStr = cl.consoleLeftBracket + cl.consoleObjectStartDelimit + cl.consoleQuote + "SwapResults" + cl.consoleQuote + cl.consoleParamDelimit + cmdLine.consoleQuote + "SwapSummary" + cmdLine.consoleQuote + cmdLine.consoleCalcDelimit;

        String strItem = cmdLine.consoleQuote + "TradesEvaluated" + cmdLine.consoleQuote + cmdLine.consoleParamDelimit + noBuys * noSells;
        consoleStr += strItem + cmdLine.consoleCalcDelimit;

        strItem = cmdLine.consoleQuote + "PositionsForSale" + cmdLine.consoleQuote + cmdLine.consoleParamDelimit + noSells;
        consoleStr += strItem + cmdLine.consoleCalcDelimit;
        strItem = cmdLine.consoleQuote + "SwapCandidates" + cmdLine.consoleQuote + cmdLine.consoleParamDelimit + noBuys;
        consoleStr += strItem + cmdLine.consoleCalcDelimit;
        strItem = cmdLine.consoleQuote + "SwapTrades" + cmdLine.consoleQuote + cmdLine.consoleParamDelimit + swapTrades.size();
        consoleStr += strItem + cmdLine.consoleCalcDelimit;
        strItem = cmdLine.consoleQuote + "RiskReturnPickups" + cmdLine.consoleQuote + cmdLine.consoleParamDelimit + noPickups;
        consoleStr += strItem + cl.consoleObjectEndDelimit;         // do trades
        String swapStr = outputSwapTradesConsole();
        consoleStr += cmdLine.consoleCalcDelimit + swapStr;
        swapStr = outputSwapPortfolioConsole();
        consoleStr += cmdLine.consoleCalcDelimit + swapStr;
        consoleStr += cmdLine.consoleRightBracket;
        cfrCalc.consoleStr = consoleStr;
        cfrCalc.fileStr = "";
        return cfrCalc;
    }

    public int outputTradeEvalSummaryFile(String fileName, String fileFormat, int maxTopSwaps) {
        int no = -1;
        String sp = "   ";
        String strItem = "", strRec = "";
        FileWriter fileWriter = null;
        BufferedWriter buffWriter = null;
        Vector<String> dupBuys = new Vector<>();
        try {
            fileWriter = new FileWriter(fileName, false);
            buffWriter = new BufferedWriter(fileWriter);
        } catch (Exception ex) {
            String errmsg = ex.getMessage();
            error.setError(getClass().getName(), errmsg);
            return 0;
        }
        if (error.hasErrorYn == 'Y') {
            try {
                buffWriter.write(error.errorMessage);
                return 0;
            } catch (Exception ex) {
                return 0;
            }
        }

        if (fileFormat.toUpperCase().equals("STD"))
            sp = ",";
        else
            sp = ",";
        String assumeHeader = "Date of Swap," + this.dtStart.getDateString() + "\n";
        assumeHeader += "Forecast Date," + this.dtFuture.getDateString() + "\n";
        assumeHeader += "Forecast Period," + this.strYrMoDay + "\n";
        assumeHeader += "Portfolio," + this.portfolioName + "\n";
        assumeHeader += "Risk/Return Target," + this.riskReturnGoal + "\n";
        assumeHeader += "Type of Swap, Cash Neutral" + "\n";
        assumeHeader += "Trades Evaluted," + noEvaluated + "\n";
        assumeHeader += "Positions To Sell," + noSells + "\n";
        assumeHeader += "Swap Candidates," + noBuys + "\n";
        assumeHeader += "Swap Made," + swapTrades.size() + "\n";
        assumeHeader += "No Duration Neutral Swaps," + noNeutral + "\n";
        assumeHeader += "No Return Pickups," + noPickups + "\n\n";

        String strhead1 = "Buy Par        " + sp;
        String strhead2 = "===============" + sp;
        strhead1 += "Buy Security                   " + sp;
        strhead2 += "===============================" + sp;
        strhead1 += "Buy Price     " + sp;
        strhead2 += "==============" + sp;
        strhead1 += "Buy Credit Rating" + sp;
        strhead2 += "=================" + sp;
        strhead1 += "Sell Par        " + sp;
        strhead2 += "===============" + sp;
        strhead1 += "Sell Security                   " + sp;
        strhead2 += "===============================" + sp;
        strhead1 += "Sell Price     " + sp;
        strhead2 += "===============" + sp;
        strhead1 += "Sell Credit Rating" + sp;
        strhead2 += "=================" + sp;
        strhead1 += "Duration Neutral" + sp;
        strhead2 += "=================" + sp;
        strhead1 += "Extra Cash Matching Sell Future Value" + sp;
        strhead2 += "=====================================" + sp;
        strhead1 += "Buy Goal     " + sp;
        strhead2 += "==============" + sp;
        strhead1 += "Sell Goal     " + sp;
        strhead2 += "==============" + sp;
        strhead1 += "Buy Forecast Mkt Value" + sp;
        strhead2 += "==============" + sp;
        strhead1 += "Sell Forecast Mkt Value" + sp;
        strhead2 += "==============" + sp;
        strhead1 += "\n";
        strhead2 += "\n";
        String strHead = assumeHeader + strhead1 + strhead2;
        String strTot = strHead;
        double dVal;
        int noSwaps = swapTrades.size();
        if (noSwaps > maxTopSwaps)
            noSwaps = maxTopSwaps;
        int strLen = 0;
        while (++no < noSwaps) {
            double sellFace = swapTrades.get(no).faceAmt;
            double buyFaceAmt = swapTrades.get(no).faceAmt;
            dVal = buyFaceAmt;
            strItem = String.format("%.0f", dVal);
            strRec = strItem + sp;
            String buySecDesc = swapTrades.get(no).secDescr;
            int len = buySecDesc.length();
            if (len > 30)
                buySecDesc = buySecDesc.substring(0, 30);
            strRec += buySecDesc + sp;
            dVal = 100 * swapTrades.get(no).currPrc;
            strItem = String.format("%.3f", dVal);
            strRec += strItem + sp;
            strRec += swapTrades.get(no).credtRating + sp;
            double sellFaceAmt = swapTrades.get(no).faceAmt;
            dVal = sellFaceAmt;
            dVal = swapTrades.get(no).faceAmt;
            strItem = String.format("%.0f", dVal);
            strRec += strItem + sp;
            String sellSecDesc = swapTrades.get(no).secDescr;
            len = sellSecDesc.length();
            if (len > 30)
                sellSecDesc = sellSecDesc.substring(0, 30);
            strRec += sellSecDesc + sp;
            dVal = 100 * swapTrades.get(no).currPrc;
            strItem = String.format("%.3f", dVal);
            strRec += strItem + sp;
            strRec += swapTrades.get(no).credtRating + sp;

            if (true == sellTrades.get(no).neutralSwap)
                strRec += "Y" + sp;
            else
                strRec += "N" + sp;

            dVal = swapTrades.get(no).rateReturnDnSide;
            strItem = String.format("%.0f", dVal);
            strRec += strItem + sp;
            dVal = sellTrades.get(no).riskReturnValue;
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;
            dVal = swapTrades.get(no).riskReturnValue;
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;
            dVal = buyFaceAmt * sellTrades.get(no).futValue;
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;
            dVal = sellFaceAmt + swapTrades.get(no).futValue;
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;
            strTot += strRec + "\n";

        }
        try {
            buffWriter.write(strTot);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            error.setError(getClass().getName(), ex.getMessage());
            System.exit(-1);
        }
        try {
            buffWriter.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            error.setError(getClass().getName(), ex.getMessage());
            System.exit(-1);
        }
        return noSwaps;
    }

    private void calcSwapStats(TradeEval tradeEval, String riskReturnGoal) {
        double buyFaceAmt = tradeEval.sellTrade.currNetPay / (tradeEval.buyTrade.currPrc + tradeEval.buyTrade.currAi);
        tradeEval.buyTrade.faceAmt = buyFaceAmt;
        tradeEval.deltaFutValue = buyFaceAmt * tradeEval.buyTrade.futValue - tradeEval.sellTrade.futValue;
        double buyBrkEvenFaceAmt = tradeEval.sellTrade.futValue / tradeEval.buyTrade.futValue;
        tradeEval.brkEvenPrc = (tradeEval.sellTrade.currNetPay - tradeEval.buyTrade.currAi * buyBrkEvenFaceAmt) / buyBrkEvenFaceAmt;
        double brkEvenNetPay = buyBrkEvenFaceAmt * (tradeEval.buyTrade.currPrc + tradeEval.buyTrade.currAi);
        tradeEval.brkEvenSwapCash = tradeEval.sellTrade.currNetPay - brkEvenNetPay;
        tradeEval.neutralSwap = isNeutralDurationSwap(tradeEval);
        switch (riskReturnGoal) {
            case Codes.MAXSHARPRATIO:
                tradeEval.deltaRiskReturnValue = tradeEval.buyTrade.sharpRatio - tradeEval.sellTrade.sharpRatio;
                break;
            case Codes.MAXDNSIDERETURN:
                // tradeEval.deltaRiskReturnValue = tradeEval.buyTrade.rateReturnDnSide - tradeEval.sellTrade.rateReturnDnSide;
                tradeEval.deltaRiskReturnValue = tradeEval.buyTrade.rateReturnExp - tradeEval.sellTrade.rateReturnExp;
                break;
            case Codes.MAXEXPRETURN:
                tradeEval.deltaRiskReturnValue = tradeEval.buyTrade.rateReturnExp - tradeEval.sellTrade.rateReturnExp;
                break;
            case Codes.MAXSUPSIDERETURN:
                // tradeEval.deltaRiskReturnValue = tradeEval.buyTrade.rateReturnDnSide - tradeEval.sellTrade.rateReturnDnSide;
                tradeEval.deltaRiskReturnValue = tradeEval.buyTrade.rateReturnExp - tradeEval.sellTrade.rateReturnExp;
                break;
            case Codes.MAXFUTUREVALUE:
                tradeEval.deltaRiskReturnValue = tradeEval.buyTrade.futValue;
                break;
            case Codes.MINFUTUREVALUELOSS:
                tradeEval.deltaRiskReturnValue = tradeEval.buyTrade.futValue;
                break;
            default:
                // tradeEval.deltaRiskReturnValue = tradeEval.buyTrade.rateReturnExp - tradeEval.sellTrade.rateReturnExp;
                tradeEval.deltaRiskReturnValue = tradeEval.buyTrade.rateReturnExp - tradeEval.sellTrade.rateReturnExp;
                break;
        }
        return;
    }


    private TradeValue assignBuySideSwapStats(TradeEval tradeEval) {
        TradeValue tradeValue = new TradeValue();
        tradeValue.inSwap = tradeEval.buyTrade.inSwap;
        tradeValue.currNetPay = tradeEval.buyTrade.currNetPay;
        tradeValue.tradeType = BondConsts.BUY;
        tradeValue.currPrc = tradeEval.buyTrade.currPrc;
        tradeValue.futPrc = tradeEval.buyTrade.futPrc;
        tradeValue.currDuration = tradeEval.buyTrade.currDuration;
        tradeValue.futDuration = tradeEval.buyTrade.futDuration;
        tradeValue.currConvexity = tradeEval.buyTrade.currConvexity;
        tradeValue.futConvexity = tradeEval.buyTrade.futConvexity;
        tradeValue.currYield = tradeEval.buyTrade.currYield;
        tradeValue.futYield = tradeEval.buyTrade.futYield;
        tradeValue.currMatLen = tradeEval.buyTrade.currMatLen;
        tradeValue.futMatLen = tradeEval.buyTrade.futMatLen;
        tradeValue.futValue = tradeEval.buyTrade.futValue;
        tradeValue.futValueDnSide = tradeEval.buyTrade.futValueDnSide;
        tradeValue.futValueUpSide = tradeEval.buyTrade.futValueUpSide;
        tradeValue.sharpRatio = tradeEval.buyTrade.sharpRatio;
        return tradeValue;
    }


    private TradeValue assignSellSideSwapStats(TradeEval tradeEval) {
        TradeValue tradeValue = new TradeValue();
        tradeValue.inSwap = tradeEval.sellTrade.inSwap;
        tradeValue.currNetPay = tradeEval.sellTrade.currNetPay;
        tradeValue.tradeType = BondConsts.BUY;
        tradeValue.currPrc = tradeEval.sellTrade.currPrc;
        tradeValue.futPrc = tradeEval.sellTrade.futPrc;
        tradeValue.currDuration = tradeEval.sellTrade.currDuration;
        tradeValue.futDuration = tradeEval.sellTrade.futDuration;
        tradeValue.currConvexity = tradeEval.sellTrade.currConvexity;
        tradeValue.futConvexity = tradeEval.sellTrade.futConvexity;
        tradeValue.currYield = tradeEval.sellTrade.currYield;
        tradeValue.futYield = tradeEval.sellTrade.futYield;
        tradeValue.currMatLen = tradeEval.sellTrade.currMatLen;
        tradeValue.futMatLen = tradeEval.sellTrade.futMatLen;
        tradeValue.futValue = tradeEval.sellTrade.futValue;
        tradeValue.futValueDnSide = tradeEval.sellTrade.futValueDnSide;
        tradeValue.futValueUpSide = tradeEval.sellTrade.futValueUpSide;
        tradeValue.sharpRatio = tradeEval.sellTrade.sharpRatio;
        return tradeValue;
    }

    public String outputSwapTradesConsole() {
        ArrayList<KeyValue> portKeyValues = new ArrayList<>();
        String jsonStr = "";
        int count = swapTrades.size();
        if (count == 0) {
            return jsonStr;
        }
        String com = cmdLine.consoleCalcDelimit;
        KeyValue kv = new KeyValue();
        String strHead = cmdLine.consoleObjectStartDelimit + cmdLine.consoleQuote + "SwapResults" + cmdLine.consoleQuote + cmdLine.consoleParamDelimit + cmdLine.consoleQuote + "SwapTrades" + cmdLine.consoleQuote + com;

        int no = -1;
        String str = "";
        while (++no < count) {
            jsonStr += strHead;
            kv.name = "Action";
            kv.dataType = "String";
            kv.value = "Buy";
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "Par";
            kv.dataType = "Number";
            kv.value = String.format("%.0f", swapTrades.get(no).faceAmt);
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "Security";
            kv.dataType = "String";
            kv.value = swapTrades.get(no).secDescr;
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "Credit Rating";
            kv.dataType = "String";
            kv.value = sellTrades.get(no).credtRating;
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "Price";
            kv.dataType = "Number";
            kv.value = String.format("%.4f", swapTrades.get(no).currPrc);

            if (no == count - 1)
                jsonStr += kv.getJsonTerm(kv) + cmdLine.consoleObjectEndDelimit ;
            else
                jsonStr += kv.getJsonTerm(kv) + cmdLine.consoleObjectEndDelimit + cmdLine.consoleCalcDelimit;
        }
        no = -1;
        jsonStr += cmdLine.consoleCalcDelimit;
        while (++no < count) {
            jsonStr += strHead;
            kv.name = "Action";
            kv.dataType = "String";
            kv.value = "Sell";
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "Par";
            kv.dataType = "Number";
            kv.value = String.format("%.0f", sellTrades.get(no).faceAmt);
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "Security";
            kv.dataType = "String";
            kv.value = swapTrades.get(no).secDescr;
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "Credit Rating";
            kv.dataType = "String";
            kv.value = sellTrades.get(no).credtRating;
            jsonStr += kv.getJsonTerm(kv) + com;


            kv.name = "Price";
            kv.dataType = "Number";
            kv.value = String.format("%.4f", sellTrades.get(no).currPrc);

            if (no == count - 1)
                jsonStr += kv.getJsonTerm(kv) + cmdLine.consoleObjectEndDelimit;
            else
                jsonStr += kv.getJsonTerm(kv) + cmdLine.consoleObjectEndDelimit + cmdLine.consoleCalcDelimit;
        }
        return jsonStr;
    }

    public String outputSwapPortfolioConsole() {
        String com = cmdLine.consoleCalcDelimit;
        int count = swapPortfolio.size();
        KeyValue kv = new KeyValue();
        String jsonStr = "";
        int iVal = 0;
        String strHead = cmdLine.consoleObjectStartDelimit + cmdLine.consoleQuote + "SwapResults" + cmdLine.consoleQuote + cmdLine.consoleParamDelimit + cmdLine.consoleQuote + "SwapPortfolio" + cmdLine.consoleQuote + com;
        int no = -1;
        while (++no < count) {
            jsonStr += strHead;
            double faceAmt = swapPortfolio.get(no).faceAmt;
            kv.name = "Par";
            iVal = (int) Math.round(faceAmt);
            kv.dataType = "Number";
            kv.value = String.valueOf(iVal);

            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "Security";
            kv.dataType = "String";
            kv.value = swapPortfolio.get(no).secDescr;
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "Credit Rating";
            kv.dataType = "String";
            kv.value = swapPortfolio.get(no).credtRating;
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "Current MktPrice";
            kv.dataType = "Number";
            kv.value = String.format("%.2f", swapPortfolio.get(no).currPrc);
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "CurrMktValue";
            double mktValue = swapPortfolio.get(no).currPrc * faceAmt;

            kv.dataType = "Number";
            kv.value = String.format("%.2f", mktValue);
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "FutMktValue";
            double futureValue = swapPortfolio.get(no).futValue;
            kv.dataType = "Number";
            kv.value = "" + (int) Math.round(futureValue);
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "CurrMaturityLen";
            kv.dataType = "Number";
            kv.value = String.format("%.2f", swapPortfolio.get(no).currMatLen);
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "FutMaturityLen";
            kv.dataType = "Number";
            kv.value = String.format("%.2f", swapPortfolio.get(no).futMatLen);
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "CurrYieldToMaturity";
            kv.dataType = "Number";
            kv.value = String.format("%.3f", swapPortfolio.get(no).currYield);
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "FutYieldToMaturity";
            kv.dataType = "Number";
            kv.value = String.format("%.3f", swapPortfolio.get(no).futYield);
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "CurrDuration";
            kv.dataType = "Number";
            kv.value = String.format("%.3f", swapPortfolio.get(no).currDuration);
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "FutDuration";
            kv.dataType = "Number";
            kv.value = String.format("%.3f", swapPortfolio.get(no).futDuration);
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "CurrConvexity";
            kv.dataType = "Number";
            kv.value = String.format("%.3f", swapPortfolio.get(no).currConvexity);
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "FutConvexity";
            kv.dataType = "Number";
            kv.value = String.format("%.3f", swapPortfolio.get(no).futConvexity);
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "RateOfReturn";
            kv.dataType = "Number";
            kv.value = String.format("%.4f", swapPortfolio.get(no).rateReturnExp);
            if (no == count - 1)
                jsonStr += kv.getJsonTerm(kv) + cmdLine.consoleObjectEndDelimit;
            else
                jsonStr += kv.getJsonTerm(kv) + cmdLine.consoleObjectEndDelimit + com;
        }

        return jsonStr;
    }

    public ConsoleFileResults outputSwapPortfolioFile(String outFileName, String fileFormat, int maxSwaps) {
        ConsoleFileResults cfrCalc = new ConsoleFileResults();
        BufferedWriter buffWriter = null;
        FileWriter fileWriter = null;
        /*  if (cmdLine.isFileOutputYn.equals("N"))
            return cfrCalc;
      */
        try {
            fileWriter = new FileWriter(outFileName);
            buffWriter = new BufferedWriter(fileWriter);
        } catch (Exception ex) {
            String errmsg = ex.getMessage();
            cfrCalc.consoleStr = errmsg;
            return cfrCalc;
        }
        String sp = ",";
        if (fileFormat.equals("STD"))
            sp = ",";
        String hd1 = "Par          " + sp;
        String ul1 = "-------------" + sp;
        String hd2 = "Security                      " + sp;
        String ul2 = "------------------------------" + sp;
        String hd3 = "Credit Rating" + sp;
        String ul3 = "-------------" + sp;
        String hd4 = "Current Price" + sp;
        String ul4 = "------------ " + sp;
        String hd5 = "Current Mkt Value" + sp;
        String ul5 = "----------------- " + sp;
        String hd6 = "Future Value" + sp;
        String ul6 = "----------------- " + sp;
        String hd7 = "Current Maturity" + sp;
        String ul7 = "----------------- " + sp;
        String hd8 = "Future Maturity" + sp;
        String ul8 = "------------" + sp;
        String hd9 = "Current Yield" + sp;
        String ul9 = "-------------" + sp;
        String hd10 = "Future Yield" + sp;
        String ul10 = "----------- " + sp;
        String hd11 = "Current Duration" + sp;
        String ul11 = "----------------" + sp;
        String hd12 = "Future Duration" + sp;
        String ul12 = "---------------" + sp;
        String hd13 = "Current Convexity" + sp;
        String ul13 = "----------------- " + sp;
        String hd14 = "Future Convexity" + sp;
        String ul14 = "----------------- " + sp;
        String hd15 = "Rate Return" + sp;
        String ul15 = "-----------" + sp;
        String hd16 = "Rate Return DnSide" + sp;
        String ul16 = "------------------" + sp;
        String hd17 = "Rate Return UpSide" + sp;
        String ul17 = "------------------" + sp;
        String head1 = hd1 + hd2 + hd3 + hd4 + hd5 + hd6 + hd7 + hd8 + hd9 + hd10 + hd11 + hd12 + hd13 + hd14 + hd15 + hd16 + hd17 + "\n";
        String head2 = ul1 + ul2 + ul3 + ul4 + ul5 + ul5 + ul7 + ul8 + ul9 + ul10 + ul11 + ul12 + ul13 + ul14 + ul15 + ul16 + ul17 + "\n";
        String head = head1 + head2;
        try {
            buffWriter.write(head);
        } catch (Exception ex) {
            String errmsg = ex.getMessage();
            cfrCalc.fileStr = errmsg;
            return cfrCalc;
        }
        int iVal = 0;
        int no = -1;
        String strRec = "", strItem = "";
        int count = swapPortfolio.size();
        double totPar = 0.;
        double totCurrPrc = 0.;
        double totCurrMktVal = 0.;
        double totFutMktVal = 0.;
        double totCurrMatLen = 0.;
        double totFutMatLen = 0.;
        double totCurrYld = 0.;
        double totFutYld = 0.;
        double totCurrDur = 0.;
        double totFutDur = 0.;
        double totCurrCon = 0.;
        double totFutCon = 0.;
        double totRtn = 0.;
        double totRtnDn = 0.;
        double totRtnUp = 0.;
        TradeValue portVal = new TradeValue();
        while (++no < count) {
            portVal = swapPortfolio.get(no);
            double par = portVal.faceAmt;
            double currMktVal = .01 * portVal.faceAmt * portVal.currPrc;
            //hd1 = "Par          " + sp;
            totPar += par;
            iVal = (int) Math.round(par);
            strItem = "" + iVal;
            strRec += strItem + sp;
            // "Security                      " + sp;
            strItem = portVal.secDescr;
            strRec += strItem + sp;

            //String hd3 = "Credit Rating" + sp;
            strRec += portVal.credtRating + sp;

            double currPrc =  portVal.currPrc;
            strItem = String.format("%.4f", currPrc);
            strRec += strItem + sp;
            totCurrPrc += currPrc * currMktVal;

            strItem = "" + Math.round(currMktVal);
            strRec += strItem + sp;
            totCurrMktVal += currMktVal;

            double futPrc = portVal.futPrc;
            double futMktVal = par * futPrc * .01;
            strItem = "" + (int) Math.round(futMktVal);
            strRec += strItem + sp;
            totFutMktVal += futMktVal;

            double currMatLen = portVal.currMatLen;
            strItem = String.format("%.2f", currMatLen);
            strRec += strItem + sp;
            totCurrMatLen += currMatLen * currMktVal;

            double futMatLen = portVal.futMatLen;
            strItem = String.format("%.2f", futMatLen);
            strRec += strItem + sp;
            totFutMatLen += futMatLen * currMktVal;

            double currYield = portVal.currYield;
            strItem = String.format("%.3f", currYield);
            strRec += strItem + sp;
            totCurrYld += currYield * currMktVal;

            double futYield = portVal.futYield;
            strItem = String.format("%.3f", futYield);
            strRec += strItem + sp;
            totFutYld += futYield * futMktVal;

            double currDuration = portVal.currDuration;
            strItem = String.format("%.3f", currDuration);
            strRec += strItem + sp;
            totCurrDur += currDuration * currMktVal;

            double futDuration = portVal.futDuration;
            strItem = String.format("%.3f", futDuration);
            strRec += strItem + sp;
            totFutDur += futDuration * currMktVal;

            double currConvexity = portVal.currConvexity;
            strItem = String.format("%.3f", currConvexity);
            strRec += strItem + sp;
            totCurrCon += currConvexity * currMktVal;

            double futConvexity = portVal.futConvexity;
            strItem = String.format("%.3f", futConvexity);
            strRec += strItem + sp;
            totFutCon += futConvexity * currMktVal;

            double rateReturn = portVal.rateReturnExp;
            strItem = String.format("%.2f", rateReturn);
            strRec += strItem + sp;
            totRtn += rateReturn * currMktVal;

            rateReturn = portVal.rateReturnDnSide;
            strItem = String.format("%.2f", rateReturn);
            strRec += strItem + sp;
            totRtnDn += rateReturn * currMktVal;

            rateReturn = portVal.rateReturnUpSide;
            strItem = String.format("%.2f", rateReturn);
            strRec += strItem + sp;
            strRec += "\n";
            totRtnUp += rateReturn * currMktVal;

        }
        // calc totals and averages
        totCurrPrc /= totCurrMktVal;
        totCurrMatLen /= totPar;
        totFutMatLen /= totPar;
        totCurrYld /= totCurrMktVal;
        totFutYld /= totFutMktVal;
        totCurrDur /= totCurrMktVal;
        totFutDur /= totFutMktVal;
        totCurrCon /= totCurrMktVal;
        totFutCon /= totFutMktVal;
        totRtn /= totCurrMktVal;
        totRtnDn /= totCurrMktVal;
        totRtnUp /= totCurrMktVal;

        String totLine = Math.round(totPar) + sp;
        totLine += "Totals" + sp;
        totLine += portVal.credtRating + sp;
        totLine += String.format("%.4f", totCurrPrc) + sp;
        totLine += Math.round(totCurrMktVal) + sp;
        totLine += Math.round(totFutMktVal) + sp;
        totLine += String.format("%.3f", totCurrMatLen) + sp;
        totLine += String.format("%.3f", totFutMatLen) + sp;
        totLine += String.format("%.4f", totCurrYld) + sp;
        totLine += String.format("%.4f", totFutYld) + sp;
        totLine += String.format("%.3f", totCurrDur) + sp;
        totLine += String.format("%.3f", totFutDur) + sp;
        totLine += String.format("%.3f", totCurrCon) + sp;
        totLine += String.format("%.3f", totFutCon) + sp;
        totLine += String.format("%.2f", totRtn) + sp;
        totLine += String.format("%.2f", totRtnDn) + sp;
        totLine += String.format("%.2f", totRtnUp) + "\n";
        try {
            buffWriter.write(strRec);
            buffWriter.write(head2);
            buffWriter.write(totLine);


        } catch (Exception ex) {
            String errmsg = ex.getMessage();
            cfrCalc.fileStr = errmsg;
        }
        try {
            buffWriter.close();
            fileWriter.close();
        } catch (Exception ex) {
            cfrCalc.fileStr = ex.getMessage();
        }

        cfrCalc.fileStr = strRec;
        return cfrCalc;
    }

    public ConsoleFileResults outputSwapTradesFileOld(String outFileName, String fileFormat, ArrayList<TradeValue> swaps, int maxSwaps) {
        ConsoleFileResults cfrCalc = new ConsoleFileResults();
        BufferedWriter buffWriter = null;
        FileWriter fileWriter = null;
        /*  if (cmdLine.isFileOutputYn.equals("N"))
            return cfrCalc;
      */
        try {
            fileWriter = new FileWriter(outFileName);
            buffWriter = new BufferedWriter(fileWriter);
        } catch (Exception ex) {
            String errmsg = ex.getMessage();
            cfrCalc.consoleStr = errmsg;
            return cfrCalc;
        }
        String sp = ",";
        if (fileFormat.equals("STD"))
            sp = ",";
        String hd1 = "Buy Par          " + sp;
        String ul1 = "-------------" + sp;
        String hd2 = "Buy Security                      " + sp;
        String ul2 = "------------------------------" + sp;
        String hd3 = "Sell Par          " + sp;
        String ul3 = "-------------" + sp;
        String hd4 = "Sell Security                      " + sp;
        String ul4 = "------------------------------" + sp;
        String hd5 = "Buy Credit" + sp;
        String ul5 = "----------" + sp;
        String hd6 = "Sell Credit" + sp;
        String ul6 = "----------" + sp;
        String hd7 = "Buy Price" + sp;
        String ul7 = "---------" + sp;
        String hd8 = "Sell Price" + sp;
        String ul8 = "----------" + sp;
        String hd9 = "BrkEven Buy Price" + sp;
        String ul9 = "-----------------" + sp;
        String hd10 = "Buy Return" + sp;
        String ul10 = "---------- " + sp;
        String hd11 = "Buy DnSide Return" + sp;
        String ul11 = "---------------- " + sp;
        String hd12 = "Buy UpSide Return" + sp;
        String ul12 = "---------------- " + sp;
        String hd13 = "Sell Return" + sp;
        String ul13 = "---------- " + sp;
        String hd14 = "Sell DnSide Return" + sp;
        String ul14 = "---------------- " + sp;
        String hd15 = "Sell UpSide Return" + sp;
        String ul15 = "---------------- " + sp;
        String hd16 = "Buy Future Value" + sp;
        String ul16 = "--------------- " + sp;
        String hd17 = "Buy DnSide Future Value" + sp;
        String ul17 = "---------------------- " + sp;
        String hd18 = "Buy UpSide Future Value" + sp;
        String ul18 = "---------------------- " + sp;
        String hd19 = "Sell Future Value" + sp;
        String ul19 = "-----------------" + sp;
        String hd20 = "Sell DnSide Future Value" + sp;
        String ul20 = "------------------ " + sp;
        String hd21 = "Sell UpSide Future Value" + sp;
        String ul21 = "------------------- " + sp;
        String hd22 = "Duration Neutral Swap" + sp;
        String ul22 = "-------------------- " + sp;
        String hd23 = "Buy Curr Duration" + sp;
        String ul23 = "-----------------" + sp;
        String hd24 = "Sell Curr Duration" + sp;
        String ul24 = "------------------" + sp;
        String hd25 = "Buy Fut Duration" + sp;
        String ul25 = "----------------" + sp;
        String hd26 = "Sell Fut Duration" + sp;
        String ul26 = "-----------------" + sp;
        String hd27 = "Buy Curr Maturity" + sp;
        String ul27 = "-----------------" + sp;
        String hd28 = "Sell Curr Maturity" + sp;
        String ul28 = "------------------" + sp;
        String hd29 = "Buy Fut Maturity" + sp;
        String ul29 = "----------------" + sp;
        String hd30 = "Sell Fut Maturity" + sp;
        String ul30 = "-----------------" + sp;
        String hd31 = "Buy Curr Convexity" + sp;
        String ul31 = "------------------" + sp;
        String hd32 = "Sell Curr Convexity" + sp;
        String ul32 = "-------------------" + sp;
        String hd33 = "Buy Fut Convexity" + sp;
        String ul33 = "----------------" + sp;
        String hd34 = "Sell Fut Convexity" + sp;
        String ul34 = "------------------" + sp;

        String head1 = hd1 + hd2 + hd3 + hd4 + hd5 + hd6 + hd7 + hd8 + hd9 + hd10 + hd11 + hd12 + hd13 + hd14 + hd15 + hd16 + hd17 + hd18 + hd19 + hd20 + hd21 + hd22 + hd23 + hd24 + hd25 + hd26 + hd27 + hd28 + hd29 + hd30 + hd31 + hd32 + hd33 + hd34 + "\n";
        String head2 = ul1 + ul2 + ul3 + ul4 + ul5 + ul6 + ul7 + ul8 + ul9 + ul10 + ul11 + ul12 + ul13 + ul14 + ul15 + ul16 + ul17 + ul18 + ul19 + ul20 + ul21 + ul22 + ul23 + ul24 + ul25 + ul26 + ul27 + ul28 + ul29 + ul30 + ul31 + ul32 + ul33 + ul34 + "\n";
        String head = head1 + head2 + "\n";
        try {
            buffWriter.write(head);
        } catch (Exception ex) {
            String errmsg = ex.getMessage();
            cfrCalc.fileStr = errmsg;
            return cfrCalc;
        }
        int iVal = 0;
        int no = -1;
        String strRec = "", strItem = "";
        int count = swaps.size();
        while (++no < count) {
            double dVal;
            iVal = (int) swaps.get(no).faceAmt;

            strItem = "" + iVal;
            strRec += strItem + sp;

            strItem = swaps.get(no).secDescr; //2
            strRec += strItem + sp;

            iVal = (int) swaps.get(no).faceAmt; //3
            strItem = "" + iVal;
            strRec += strItem + sp;

            strItem = swaps.get(no).secDescr; //4
            strRec += strItem + sp;

            strRec += swaps.get(no).credtRating + sp; //5

            strRec += swaps.get(no).credtRating + sp; //6

            dVal = 100. * swaps.get(no).currPrc; //7
            strItem = String.format("%.4f", dVal);
            strRec += strItem + sp;

            dVal = 100. * swaps.get(no).currPrc; //8
            strItem = String.format("%.4f", dVal);
            strRec += strItem + sp;

            dVal = 100. * swaps.get(no).brkEvenPrc; //9
            strItem = String.format("%.4f", dVal);
            strRec += strItem + sp;

            dVal = swaps.get(no).rateReturnExp; //10
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;

            dVal = swaps.get(no).rateReturnDnSide; //11
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;

            dVal = swaps.get(no).rateReturnUpSide; //12
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;

            dVal = swaps.get(no).rateReturnExp; //13
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;

            dVal = swaps.get(no).rateReturnDnSide; //14
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;

            dVal = swaps.get(no).rateReturnUpSide; //15
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;


            dVal = swaps.get(no).faceAmt * swapTrades.get(no).futValue; //16
            iVal = (int) dVal;
            strItem = "" + iVal;
            strRec += strItem + sp;

            dVal = swaps.get(no).faceAmt * swapTrades.get(no).futValueDnSide; // 17
            iVal = (int) dVal;
            strItem = "" + iVal;
            strRec += strItem + sp;

            dVal = swaps.get(no).faceAmt * swapTrades.get(no).futValue; //19
            iVal = (int) dVal;
            strItem = "" + iVal;
            strRec += strItem + sp;

            dVal = swaps.get(no).faceAmt * swapTrades.get(no).futValueDnSide; //20
            iVal = (int) dVal;
            strItem = "" + iVal;
            strRec += strItem + sp;

            dVal = swaps.get(no).faceAmt * swapTrades.get(no).futValueUpSide; //21
            iVal = (int) dVal;
            strItem = "" + iVal;
            strRec += strItem + sp;

            if (swaps.get(no).neutralSwap == true) //22
                strRec += "Y" + sp;
            else
                strRec += "N" + sp;

            dVal = swaps.get(no).currDuration; //23
            strItem = String.format("%.3f", dVal);
            strRec += strItem + sp;

            dVal = swaps.get(no).currDuration; //24
            strItem = String.format("%.3f", dVal);
            strRec += strItem + sp;

            dVal = swaps.get(no).futDuration; //25
            strItem = String.format("%.3f", dVal);
            strRec += strItem + sp;

            dVal = swaps.get(no).futDuration; //26
            strItem = String.format("%.3f", dVal);
            strRec += strItem + sp;

            dVal = swaps.get(no).currMatLen; //27
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;

            dVal = swaps.get(no).currMatLen; //28
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;


            dVal = swaps.get(no).futMatLen; //29
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;

            dVal = swaps.get(no).futMatLen; //30
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;


            dVal = swaps.get(no).currConvexity; //31
            strItem = String.format("%.3f", dVal);
            strRec += strItem + sp;

            dVal = swaps.get(no).currConvexity; //32
            strItem = String.format("%.3f", dVal);
            strRec += strItem + sp;


            dVal = swaps.get(no).futConvexity; //33
            strItem = String.format("%.3f", dVal);
            strRec += strItem + sp;

            dVal = swaps.get(no).futConvexity; //34
            strItem = String.format("%.3f", dVal);
            strRec += strItem + sp;
            strRec += "\n";
        }


        try {
            buffWriter.write(strRec);

        } catch (Exception ex) {
            String errmsg = ex.getMessage();
            cfrCalc.fileStr = errmsg;
        }
        try {
            buffWriter.close();
            fileWriter.close();
        } catch (Exception ex) {
            cfrCalc.fileStr = ex.getMessage();
        }

        cfrCalc.fileStr = strRec;
        return cfrCalc;
    }

    public ConsoleFileResults outputSwapTradesFile(String outFileName, String fileFormat, int maxSwaps) {
        ConsoleFileResults cfrCalc = new ConsoleFileResults();
        BufferedWriter buffWriter = null;
        FileWriter fileWriter = null;
        /*  if (cmdLine.isFileOutputYn.equals("N"))
            return cfrCalc;
      */
        try {
            fileWriter = new FileWriter(outFileName);
            buffWriter = new BufferedWriter(fileWriter);
        } catch (Exception ex) {
            String errmsg = ex.getMessage();
            cfrCalc.consoleStr = errmsg;
            return cfrCalc;
        }
        String sp = ",";
        if (fileFormat.equals("STD"))
            sp = ",";
        String hd1 = "Trade" + sp;
        String ul1 = "-----" + sp;
        String hd2 = "Security                      " + sp;
        String ul2 = "------------------------------" + sp;
        String hd3 = "Face Amount     " + sp;
        String ul3 = "----------------" + sp;
        String hd4 = "Credit Rating" + sp;
        String ul4 = "-------------" + sp;
        String hd5 = "Trade Price" + sp;
        String ul5 = "-----------" + sp;
        String hd6 = "Market Value    " + sp;
        String ul6 = "----------------";
        String hd7 = "Fut Return" + sp;
        String ul7 = "----------" + sp;
        String hd8 = "Fut DnSide Return" + sp;
        String ul8 = "---------------- " + sp;
        String hd9 = "Fut UpSide Return" + sp;
        String ul9 = "---------------- " + sp;
        String hd10 = "Future Value    " + sp;
        String ul10 = "----------------" + sp;
        String hd11 = "Future Value DnSide " + sp;
        String ul11 = "------------------- " + sp;
        String hd12 = "Future Value UpSide" + sp;
        String ul12 = "------------------- " + sp;
        String hd13 = "BreakEven Price" + sp;
        String ul13 = "---------------" + sp;
        String hd131 = "BreakEven Cash" + sp;
        String ul131 = "---------------" + sp;
        String hd14 = "Curr Duration" + sp;
        String ul14 = "-----------------" + sp;
        String hd141 = "Fut Duration" + sp;
        String ul141 = "-----------------" + sp;
        String hd15 = "Curr Maturity" + sp;
        String ul15 = "-------------" + sp;
        String hd16 = "Fut Maturity" + sp;
        String ul16 = "------------" + sp;
        String hd17 = "Curr Convexity" + sp;
        String ul17 = "--------------" + sp;
        String hd171 = "Fut Convexity" + sp;
        String ul171 = "--------------" + sp;
        String head1 = hd1 + hd2 + hd3 + hd4 + hd5 + hd6 + hd7 + hd8 + hd9 + hd10 + hd11 + hd12 + hd13 + hd131 + hd14 + hd141 + hd15 + hd16 + hd17 + hd171 + "\n";
        String head2 = ul1 + ul2 + ul3 + ul4 + ul5 + ul6 + ul7 + ul8 + ul9 + ul10 + ul11 + ul12 + ul13 + ul131 + ul14 + ul141 + ul15 + ul16 + ul17 + ul171 + "\n";
        String head = head1 + head2;
        try {
            buffWriter.write(head);
        } catch (Exception ex) {
            String errmsg = ex.getMessage();
            cfrCalc.fileStr = errmsg;
            return cfrCalc;
        }
        int iVal = 0;
        double dVal = 0.;
        int no = -1;
        String strRec = "", strItem = "";
        int count = swapTrades.size();
        TradeValue tradeValue = null;
        while (++no < count) {

            tradeValue = swapTrades.get(no);
            if (tradeValue.tradeType.equals("B"))
                strItem = "Buy";
                else
                strItem = "Sell";
            strRec += strItem + sp;

            strItem = tradeValue.secDescr; //2
            strRec += strItem + sp;

            dVal = tradeValue.faceAmt;
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;

            strRec += tradeValue.credtRating + sp;

            dVal = tradeValue.currPrc; //7
            strItem = String.format("%.4f", dVal);
            strRec += strItem + sp;

            dVal = tradeValue.faceAmt * .01 * tradeValue.currPrc;
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;

            dVal = tradeValue.rateReturnExp;
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;

            dVal = tradeValue.rateReturnDnSide;
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;

            dVal = tradeValue.rateReturnUpSide;
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;

            dVal = tradeValue.futValue; //
            iVal = (int) dVal;
            strItem = "" + iVal;
            strRec += strItem + sp;

            dVal = tradeValue.futValueDnSide; //
            iVal = (int) dVal;
            strItem = "" + iVal;
            strRec += strItem + sp;

            dVal = tradeValue.futValueUpSide; //19
            iVal = (int) dVal;
            strItem = "" + iVal;
            strRec += strItem + sp;

            if (tradeValue.tradeType.equals("S"))
                strItem = "NA";
            else {
                dVal = tradeValue.brkEvenPrc; //19
                strItem = "" + String.format("%.3f", dVal);
            }
            strRec += strItem + sp;


            if (tradeValue.tradeType.equals("S"))
                strItem = "NA";
            else {
                dVal = tradeValue.brkEvenCash; //19
                strItem = "" + String.format("%.3f", dVal);
            }
            strRec += strItem + sp;


            dVal = tradeValue.currDuration; //23
            strItem = String.format("%.3f", dVal);
            strRec += strItem + sp;

            dVal = tradeValue.futDuration; //25
            strItem = String.format("%.3f", dVal);
            strRec += strItem + sp;

            dVal = tradeValue.currMatLen;
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;

            dVal = tradeValue.futMatLen;
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;


            dVal = tradeValue.currConvexity;
            strItem = String.format("%.3f", dVal);
            strRec += strItem + sp;

            dVal = tradeValue.futConvexity; //33
            strItem = String.format("%.3f", dVal);
            strRec += strItem + sp;
            strRec += "\n";
        }


        try {
            buffWriter.write(strRec);

        } catch (Exception ex) {
            String errmsg = ex.getMessage();
            cfrCalc.fileStr = errmsg;
        }
        try {
            buffWriter.close();
            fileWriter.close();
        } catch (Exception ex) {
            cfrCalc.fileStr = ex.getMessage();
        }

        cfrCalc.fileStr = strRec;
        return cfrCalc;
    }

    private void sortTradesBySharpRatio(ArrayList<TradeValue> trades, boolean ascend) {
        int asc;
        if (ascend == true)
            asc = 1;
        else
            asc = -1;
        Collections.sort(trades, new Comparator<TradeValue>() {
            @Override
            public int compare(TradeValue o1, TradeValue o2) {
                if (o1.sharpRatio > o2.sharpRatio)
                    return asc;
                if (o1.sharpRatio < o2.sharpRatio)
                    return -asc;
                return 0;
            }
        });
    }


    private void sortTradesByReturnExp(ArrayList<TradeValue> trades, boolean ascend) {
        int asc;
        if (ascend == true)
            asc = 1;
        else
            asc = -1;
        Collections.sort(trades, new Comparator<TradeValue>() {
            @Override
            public int compare(TradeValue o1, TradeValue o2) {
                if (o1.rateReturnExp > o2.rateReturnExp)
                    return asc;
                if (o1.rateReturnExp < o2.rateReturnExp)
                    return -asc;
                return 0;
            }
        });
    }


    private void sortTradesByReturnDn(ArrayList<TradeValue> trades, boolean ascend) {
        int asc;
        if (ascend == true)
            asc = 1;
        else
            asc = -1;
        Collections.sort(trades, new Comparator<TradeValue>() {
            @Override
            public int compare(TradeValue o1, TradeValue o2) {
                if (o1.rateReturnDnSide > o2.rateReturnDnSide)
                    return asc;
                if (o1.rateReturnDnSide < o2.rateReturnDnSide)
                    return -asc;
                return 0;
            }
        });
    }

    private void sortTradesByReturnUp(ArrayList<TradeValue> trades, boolean ascend) {
        int asc;
        if (ascend == true)
            asc = 1;
        else
            asc = -1;
        Collections.sort(trades, new Comparator<TradeValue>() {
            @Override
            public int compare(TradeValue o1, TradeValue o2) {
                if (o1.rateReturnUpSide > o2.rateReturnUpSide)
                    return asc;
                if (o1.rateReturnUpSide < o2.rateReturnUpSide)
                    return -asc;
                return 0;
            }
        });
    }

    private void sortByTradeId(ArrayList<TradeValue> trades, boolean ascend) {
        int asc;
        if (ascend == true)
            asc = 1;
        else
            asc = -1;
        Collections.sort(trades, new Comparator<TradeValue>() {
            @Override
            public int compare(TradeValue o1, TradeValue o2) {
                if (o1.tradeId > o2.tradeId)
                    return -1;
                if (o1.tradeId < o2.tradeId)
                    return 1;
                return 0;

            }
        });
    }


    private Trade assignSwapMade(TradeEval tradeEval, String tradeType) {
        Trade trade = new Trade();
        if (tradeType.equals("B")) {
            trade.tradeType = "Buy";
            trade.price = tradeEval.buyTrade.currPrc;
            trade.faceAmt = tradeEval.buyTrade.faceAmt;
            trade.secDescr = tradeEval.buyTrade.secDescr;
            trade.riskReturnValue = tradeEval.buyTrade.riskReturnValue;
            trade.dtSettle = this.dtStart;
            return trade;
        } else {
            trade.tradeType = "Sell";
            trade.price = tradeEval.sellTrade.currPrc;
            trade.faceAmt = tradeEval.sellTrade.faceAmt;
            trade.secDescr = tradeEval.sellTrade.secDescr;
            trade.riskReturnValue = tradeEval.sellTrade.riskReturnValue;
            trade.dtSettle = this.dtStart;
            return trade;
        }


    }

    class Trade {
        String tradeType; // buy or sell
        double faceAmt;
        double price;
        String secDescr;
        Dates dtSettle;
        double riskReturnValue;

        Trade(Trade trade) {
            this.tradeType = trade.tradeType;
            this.faceAmt = trade.faceAmt;
            this.price = trade.price;
            this.secDescr = trade.secDescr;
            this.dtSettle = trade.dtSettle;
            this.riskReturnValue = trade.riskReturnValue;
        }

        Trade() {

        }

    }


}





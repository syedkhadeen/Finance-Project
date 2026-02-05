package app;

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
    ArrayList<TradeValue> sellTrades = new ArrayList<>();
    ArrayList<TradeEval> swapTrades = new ArrayList<>(); // all possible swap trade
    ArrayList<TradeValue> swapPortfolio;
    ArrayList<TradeEval> swapList = new ArrayList<>(); // swap list of trade to make
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

    public ArrayList<TradeEval> calcTradeEval(ArrayList<TradeValue> tradeValues, Dates dtSettle, Dates dtFuture) {
        ArrayList<TradeEval> tradeList = new ArrayList<>();
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
                tradeList = setTradeListMaxExpReturn(buyTrades, sellTrades);
                break;
            case Codes.MAXDNSIDERETURN:
                sortTradesByReturnDn(buyTrades, false);
                sortTradesByReturnDn(sellTrades, true);
                tradeList = setTradeListMaxDnSideReturn(buyTrades, sellTrades);
                break;
            case Codes.MAXSUPSIDERETURN:
                sortTradesByReturnUp(buyTrades, false);
                sortTradesByReturnUp(sellTrades, true);
                tradeList = setTradeListMaxExpReturn(buyTrades, sellTrades);
                break;
            case Codes.MAXFUTUREVALUE:
                sortTradesByReturnExp(buyTrades, false);
                tradeList = setTradeListMaxExpReturn(buyTrades, buyTrades);
                break;
            case Codes.MINFUTUREVALUELOSS:
                sortTradesByReturnExp(sellTrades, true);
                tradeList = setTradeListMaxExpReturn(buyTrades, sellTrades);
                break;
            default:
                sortTradesByReturnExp(buyTrades, false);
                sortTradesByReturnExp(sellTrades, true);
                tradeList = setTradeListMaxExpReturn(buyTrades, sellTrades);
                break;
        }

        // create the trade opps
        // create buy list trade values and sell list trade values
        // we several buy may want to sell same sell.
        noSwaps = tradeList.size();
        // sorted
        return tradeList;
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

    ArrayList<TradeEval> setTradeListMaxSharpRatio(ArrayList<TradeValue> buyTrades, ArrayList<TradeValue> sellTrades) {
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

    ArrayList<TradeEval> setTradeListMaxExpReturn(ArrayList<TradeValue> buyTrades, ArrayList<TradeValue> sellTrades) {
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
                tradeEval.buyId = buyTrades.get(nb).tradeId;
                tradeEval.sellId = sellTrades.get(ns).tradeId;
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

    ArrayList<TradeEval> setTradeListMaxDnSideReturn(ArrayList<TradeValue> buyTrades,
            ArrayList<TradeValue> sellTrades) {
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

    ArrayList<TradeEval> setTradeListMaxUpSideReturn(ArrayList<TradeValue> buyTrades,
            ArrayList<TradeValue> sellTrades) {
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
        consoleStr = cl.consoleLeftBracket + cl.consoleObjectStartDelimit + cl.consoleQuote + "SwapResults"
                + cl.consoleQuote + cl.consoleParamDelimit + cmdLine.consoleQuote + "SwapSummary" + cmdLine.consoleQuote
                + cmdLine.consoleCalcDelimit;

        String strItem = cmdLine.consoleQuote + "TradesEvaluated" + cmdLine.consoleQuote + cmdLine.consoleParamDelimit
                + noBuys * noSells;
        consoleStr += strItem + cmdLine.consoleCalcDelimit;

        strItem = cmdLine.consoleQuote + "PositionsForSale" + cmdLine.consoleQuote + cmdLine.consoleParamDelimit
                + noSells;
        consoleStr += strItem + cmdLine.consoleCalcDelimit;
        strItem = cmdLine.consoleQuote + "SwapCandidates" + cmdLine.consoleQuote + cmdLine.consoleParamDelimit + noBuys;
        consoleStr += strItem + cmdLine.consoleCalcDelimit;
        strItem = cmdLine.consoleQuote + "SwapTrades" + cmdLine.consoleQuote + cmdLine.consoleParamDelimit
                + swapTrades.size();
        consoleStr += strItem + cmdLine.consoleCalcDelimit;
        strItem = cmdLine.consoleQuote + "RiskReturnPickups" + cmdLine.consoleQuote + cmdLine.consoleParamDelimit
                + noPickups;
        consoleStr += strItem + cl.consoleObjectEndDelimit; // do trades
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
        assumeHeader += "Swap Made," + swapList.size() + "\n";
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
        int noSwaps = swapList.size();
        if (noSwaps > maxTopSwaps)
            noSwaps = maxTopSwaps;
        int strLen = 0;
        while (++no < noSwaps) {
            double sellFace = swapList.get(no).sellTrade.faceAmt;
            double buyFaceAmt = swapList.get(no).cashNeutralBuyFaceAmt;
            dVal = buyFaceAmt;
            strItem = String.format("%.0f", dVal);
            strRec = strItem + sp;
            String buySecDesc = swapList.get(no).buyTrade.secDescr;
            int len = buySecDesc.length();
            if (len > 30)
                buySecDesc = buySecDesc.substring(0, 30);
            strRec += buySecDesc + sp;
            dVal = 100 * swapList.get(no).buyTrade.currPrc;
            strItem = String.format("%.3f", dVal);
            strRec += strItem + sp;
            strRec += swapList.get(no).buyTrade.credtRating + sp;
            double sellFaceAmt = swapList.get(no).sellTrade.faceAmt;
            dVal = sellFaceAmt;
            dVal = swapList.get(no).sellTrade.faceAmt;
            strItem = String.format("%.0f", dVal);
            strRec += strItem + sp;
            String sellSecDesc = swapList.get(no).sellTrade.secDescr;
            len = sellSecDesc.length();
            if (len > 30)
                sellSecDesc = sellSecDesc.substring(0, 30);
            strRec += sellSecDesc + sp;
            dVal = 100 * swapList.get(no).sellTrade.currPrc;
            strItem = String.format("%.3f", dVal);
            strRec += strItem + sp;
            strRec += swapList.get(no).sellTrade.credtRating + sp;

            if (true == swapList.get(no).neutralSwap)
                strRec += "Y" + sp;
            else
                strRec += "N" + sp;

            dVal = swapList.get(no).swapCash;
            strItem = String.format("%.0f", dVal);
            strRec += strItem + sp;
            dVal = swapList.get(no).buyTrade.riskReturnValue;
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;
            dVal = swapList.get(no).sellTrade.riskReturnValue;
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;
            dVal = buyFaceAmt * swapList.get(no).buyTrade.futValue;
            strItem = String.format("%.2f", dVal);
            strRec += strItem + sp;
            dVal = sellFaceAmt + swapList.get(no).sellTrade.futValue;
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
        double cashNeutralBuyFaceAmt = tradeEval.sellTrade.faceAmt * tradeEval.sellTrade.currNetPay
                / tradeEval.buyTrade.currNetPay;
        tradeEval.cashNeutralBuyFaceAmt = cashNeutralBuyFaceAmt;
        tradeEval.deltaFutValue = tradeEval.cashNeutralBuyFaceAmt * tradeEval.buyTrade.futValue
                - tradeEval.sellTrade.futValue * tradeEval.sellTrade.faceAmt;
        double buyBrkEvenPar = tradeEval.sellTrade.futValue * tradeEval.sellTrade.faceAmt / tradeEval.buyTrade.futValue;
        double buyPrin = buyBrkEvenPar * tradeEval.buyTrade.currNetPay - buyBrkEvenPar * tradeEval.buyTrade.currAi;
        tradeEval.brkEvenPrc = buyPrin / tradeEval.cashNeutralBuyFaceAmt;
        tradeEval.swapCash = (tradeEval.cashNeutralBuyFaceAmt - buyBrkEvenPar) * tradeEval.buyTrade.currNetPay;
        tradeEval.neutralSwap = isNeutralDurationSwap(tradeEval);

        switch (riskReturnGoal) {
            case Codes.MAXSHARPRATIO:
                tradeEval.deltaRiskReturnValue = tradeEval.buyTrade.sharpRatio - tradeEval.sellTrade.sharpRatio;
                break;
            case Codes.MAXDNSIDERETURN:
                tradeEval.deltaRiskReturnValue = tradeEval.buyTrade.rateReturnDnSide
                        - tradeEval.sellTrade.rateReturnDnSide;
                break;
            case Codes.MAXEXPRETURN:
                tradeEval.deltaRiskReturnValue = tradeEval.buyTrade.rateReturnExp - tradeEval.sellTrade.rateReturnExp;
                break;
            case Codes.MAXSUPSIDERETURN:
                tradeEval.deltaRiskReturnValue = tradeEval.buyTrade.rateReturnDnSide
                        - tradeEval.sellTrade.rateReturnDnSide;
                break;
            case Codes.MAXFUTUREVALUE:
                tradeEval.deltaRiskReturnValue = tradeEval.buyTrade.futValue;
                break;
            case Codes.MINFUTUREVALUELOSS:
                tradeEval.deltaRiskReturnValue = tradeEval.buyTrade.futValue;
                break;
            default:
                tradeEval.deltaRiskReturnValue = tradeEval.buyTrade.rateReturnExp - tradeEval.sellTrade.rateReturnExp;
                break;
        }
        return;
    }

    public ArrayList<TradeValue> getSwapPortfolio(ArrayList<TradeEval> swapTrades) {
        ArrayList<TradeValue> portTradeValueList = new ArrayList<>();
        TradeValue tradeValue = new TradeValue();
        double cashPool = 0., totMktValue = 0., totalGoalValue = 0.;
        int no = -1, noSwaps = 0, noPorts = 0;
        // get face amoount of buy based of allocating cash
        noSwaps = swapTrades.size();
        double faceAmt = 0.;
        TradeEval tradeEval = new TradeEval();
        Vector<Integer> sellSwaps = new Vector();
        int noTrades = 0;
        no = -1;
        int noSellIds = 0, sellId = 0;
        double swapPctRiskDrop;
        while (++no < noSwaps) {
            faceAmt = 0.;
            // make all swaps with best upside resturn and downside > x
            // make all swaps with best expectedd return and downside > x
            // make all swaps with best worst case and return at least > x
            swapPctRiskDrop = 100.
                    * (swapTrades.get(no).buyTrade.futValueDnSide - swapTrades.get(no).sellTrade.futValueUpSide)
                    / swapTrades.get(no).buyTrade.futValueDnSide;
            if (swapPctRiskDrop > this.swapPctGain) {
                if (swapTrades.get(no).buyTrade.inSwap == false && swapTrades.get(no).sellTrade.inSwap == false) {
                    portTradeValueList.add(noPorts++, new TradeValue(swapTrades.get(no).buyTrade));
                    swapTrades.get(no).sellId = swapTrades.get(no).sellTrade.tradeId;
                    swapTrades.get(no).buyTrade.inSwap = true;
                    swapTrades.get(no).sellTrade.inSwap = true;
                    sellId = swapTrades.get(no).sellTrade.tradeId;
                    noSellIds = sellSwaps.size();
                    tradeEval = swapTrades.get(no);
                    if (tradeEval.neutralSwap == true)
                        ++noNeutral;
                    sellSwaps.add(noSellIds, sellId);
                    swapList.add(noTrades++, tradeEval);
                }
            }
        }
        // add sells not swapped out
        no = -1;
        while (++no < noSells) {
            if (!sellSwaps.contains(sellTrades.get(no).tradeId)) {
                portTradeValueList.add(noPorts++, new TradeValue(sellTrades.get(no)));
            }
        }
        return portTradeValueList;
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
        int count = swapList.size();
        if (count == 0) {
            return jsonStr;
        }
        String com = cmdLine.consoleCalcDelimit;
        KeyValue kv = new KeyValue();
        String strHead = cmdLine.consoleObjectStartDelimit + cmdLine.consoleQuote + "SwapResults" + cmdLine.consoleQuote
                + cmdLine.consoleParamDelimit + cmdLine.consoleQuote + "SwapTrades" + cmdLine.consoleQuote + com;

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
            kv.value = String.format("%.0f", swapList.get(no).buyTrade.faceAmt);
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "Security";
            kv.dataType = "String";
            kv.value = swapList.get(no).buyTrade.secDescr;
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "Credit Rating";
            kv.dataType = "String";
            kv.value = swapList.get(no).buyTrade.credtRating;
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "Price";
            kv.dataType = "Number";
            kv.value = String.format("%.4f", 100. * swapList.get(no).buyTrade.currPrc);

            if (no == count - 1)
                jsonStr += kv.getJsonTerm(kv) + cmdLine.consoleObjectEndDelimit;
            else
                jsonStr += kv.getJsonTerm(kv) + cmdLine.consoleObjectEndDelimit + cmdLine.consoleCalcDelimit;
        }
        no = -1;
        while (++no < count) {
            kv.name = "Action";
            kv.dataType = "String";
            kv.value = "Sell";
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "Par";
            kv.dataType = "Number";
            kv.value = String.format("%.0f", swapList.get(no).sellTrade.faceAmt);
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "Security";
            kv.dataType = "String";
            kv.value = swapList.get(no).sellTrade.secDescr;
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "Credit Rating";
            kv.dataType = "String";
            kv.value = swapList.get(no).sellTrade.credtRating;
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "Price";
            kv.dataType = "Number";
            kv.value = String.format("%.4f", 100. * swapList.get(no).sellTrade.currPrc);

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
        String strHead = cmdLine.consoleObjectStartDelimit + cmdLine.consoleQuote + "SwapResults" + cmdLine.consoleQuote
                + cmdLine.consoleParamDelimit + cmdLine.consoleQuote + "SwapPortfolio" + cmdLine.consoleQuote + com;
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
            kv.value = String.format("%.2f", 100 * swapPortfolio.get(no).currPrc);
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "CurrMktValue";
            double mktValue = swapPortfolio.get(no).currNetPay * faceAmt;
            iVal = (int) Math.round(mktValue);
            kv.dataType = "Number";
            kv.value = "" + iVal;
            jsonStr += kv.getJsonTerm(kv) + com;

            kv.name = "FutMktValue";
            double futureValue = faceAmt * swapPortfolio.get(no).futValue;
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
            int freq = CalUnits.SEMIANNUALFQ;
            double perLen = dayCount.simplePeriodCount(dtStart, dtFuture, CalUnits.SEMIANNUALREPEAT);
            double ratio = futureValue / mktValue;
            double rateReturn = 0.;
            if (perLen > 0.)
                rateReturn = 100. * freq * (-1 + Math.pow(ratio, 1. / perLen));
            kv.value = String.format("%.4f", rateReturn);
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
        /*
         * if (cmdLine.isFileOutputYn.equals("N"))
         * return cfrCalc;
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
        String hd21 = "Credit Rating" + sp;
        String ul21 = "-------------" + sp;
        String hd3 = "Current Price" + sp;
        String ul3 = "------------ " + sp;
        String hd4 = "Current Mkt Value" + sp;
        String ul4 = "----------------- " + sp;
        String hd5 = "Mkt Value at Forecast" + sp;
        String ul5 = "----------------- " + sp;
        String hd51 = "Current Maturity Len" + sp;
        String ul51 = "----------------- " + sp;
        String hd52 = "Maturity Len at Forecast" + sp;
        String ul52 = "----------------- " + sp;
        String hd6 = "Current Yield     " + sp;
        String ul6 = "----------------- " + sp;
        String hd7 = "Yield at Forecast   " + sp;
        String ul7 = "----------------- " + sp;
        String hd8 = "Current Duration" + sp;
        String ul8 = "----------------- " + sp;
        String hd9 = "Duration at Forecast" + sp;
        String ul9 = "----------------- " + sp;
        String hd10 = "Current Convexity" + sp;
        String ul10 = "----------------- " + sp;
        String hd11 = "Forecast Convexity" + sp;
        String ul11 = "----------------- " + sp;
        String hd12 = "Forecast Rate Return" + sp;
        String ul12 = "------------------- " + sp;
        String head1 = hd1 + hd2 + hd21 + hd3 + hd4 + hd5 + hd51 + hd52 + hd6 + hd7 + hd8 + hd9 + hd10 + hd11 + hd12
                + "\n";
        String head2 = ul1 + ul2 + ul21 + ul3 + ul4 + ul5 + ul51 + ul52 + ul6 + ul7 + ul8 + ul9 + ul10 + ul11 + ul12
                + "\n";
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
        while (++no < count) {
            double faceAmt = swapPortfolio.get(no).faceAmt;
            double currMktVal = faceAmt * swapPortfolio.get(no).currNetPay;
            double futMktVal = faceAmt * swapPortfolio.get(no).futNetPay;

            iVal = (int) Math.round(faceAmt);
            strItem = "" + iVal;
            strRec += strItem + sp;
            totPar += faceAmt;

            strItem = swapPortfolio.get(no).secDescr;
            strRec += strItem + sp;

            strRec += swapPortfolio.get(no).credtRating + sp;

            double currPrc = 100. * swapPortfolio.get(no).currPrc;
            strItem = String.format("%.4f", currPrc);
            strRec += strItem + sp;
            totCurrPrc += currPrc * currMktVal;

            strItem = "" + Math.round(currMktVal);
            strRec += strItem + sp;
            totCurrMktVal += currMktVal;

            strItem = "" + (int) Math.round(futMktVal);
            strRec += strItem + sp;
            totFutMktVal += futMktVal;

            double currMatLen = swapPortfolio.get(no).currMatLen;
            strItem = String.format("%.2f", currMatLen);
            strRec += strItem + sp;
            totCurrMatLen += currMatLen * faceAmt;

            double futMatLen = swapPortfolio.get(no).futMatLen;
            strItem = String.format("%.2f", futMatLen);
            strRec += strItem + sp;
            totFutMatLen += futMatLen * faceAmt;

            double currYield = swapPortfolio.get(no).currYield;
            strItem = String.format("%.3f", currYield);
            strRec += strItem + sp;
            totCurrYld += currYield * currMktVal;

            double futYield = swapPortfolio.get(no).futYield;
            strItem = String.format("%.3f", futYield);
            strRec += strItem + sp;
            totFutYld += futYield * futMktVal;

            double currDuration = swapPortfolio.get(no).currDuration;
            strItem = String.format("%.3f", currDuration);
            strRec += strItem + sp;
            totCurrDur += currDuration * currMktVal;

            double futDuration = swapPortfolio.get(no).futDuration;
            strItem = String.format("%.3f", futDuration);
            strRec += strItem + sp;
            totFutDur += futDuration * futMktVal;

            double currConvexity = swapPortfolio.get(no).currConvexity;
            strItem = String.format("%.3f", swapPortfolio.get(no).currConvexity);
            strRec += strItem + sp;
            totCurrCon += currConvexity * currMktVal;

            double futConvexity = swapPortfolio.get(no).futConvexity;
            strItem = String.format("%.3f", futConvexity);
            strRec += strItem + sp;
            totFutCon += futConvexity * futMktVal;

            int freq = CalUnits.SEMIANNUALFQ;
            double perLen = dayCount.simplePeriodCount(dtStart, dtFuture, CalUnits.SEMIANNUALREPEAT);
            double futureValue = faceAmt * swapPortfolio.get(no).futValue;
            double ratio = futureValue / currMktVal;
            double rateReturn = 0.;
            rateReturn = 100. * freq * (-1 + Math.pow(ratio, 1. / perLen));
            strItem = String.format("%.4f", rateReturn);
            strRec += strItem + "\n";
            totRtn += rateReturn * currMktVal;
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

        String totLine = Math.round(totPar) + sp;
        String totDesc = "Totals                        " + sp;
        totLine += totDesc;
        totLine += sp;
        totLine += String.format("%.4f", totCurrPrc) + sp;
        totLine += Math.round(totCurrMktVal) + sp + Math.round(totFutMktVal) + sp;
        totLine += String.format("%.3f", totCurrMatLen) + sp + String.format("%.3f", totFutMatLen) + sp;
        totLine += String.format("%.4f", totCurrYld) + sp + String.format("%.4f", totFutYld) + sp;
        totLine += String.format("%.3f", totCurrDur) + sp + String.format("%.3f", totFutDur) + sp;
        totLine += String.format("%.3f", totCurrCon) + sp + String.format("%.3f", totFutCon) + sp;
        totLine += String.format("%.3f", totRtn) + "\n";

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

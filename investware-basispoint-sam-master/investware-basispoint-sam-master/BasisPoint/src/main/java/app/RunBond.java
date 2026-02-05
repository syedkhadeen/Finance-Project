package app;

// This comment  is to test java

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.DelayQueue;

public class RunBond {
    // Formatter logFormatter;
    // Formatter dumpFormatter;
    String PHASE = "PHASE1";
    FileWriter logWriter;
    FileWriter dumpWriter;
    Formatter fileFormatter;
    BondDefault bondDefault;
    BondCheck bondCheck;
    CalcCheck calcCheck;
    CashflowResults cashflowResults = new CashflowResults();
    CalcMethod calcMethod;
    StringToBond stringToBond;
    CalcDef calcDef = new CalcDef();
    ConsoleFileResults cfrRun = new ConsoleFileResults();
    ConsoleFileResults cfrSec = new ConsoleFileResults();
    Bond bond;
    Error error;
    CommandLine commandLine;
    Choices choices;
    Holidays holidays;
    SecurityTypes securityTypes;
    DayCount dayCount;
    Time time;
    Utility utility;
    Status status;
    TradeEvals tradeEvals;
    int noSecurities;
    int calcResultNo = -1;
    int noCalculations;
    int cumCashflowCount = 0;
    YieldCurves allYieldCurves;
    ArrayList<String> secRequestList = new ArrayList<>();
    ArrayList<Integer> calculationList = new ArrayList<Integer>();
    ArrayList<SecurityResults> securityResults = new ArrayList();
    ArrayList<Attribution> attributionList = new ArrayList<>();
    ArrayList<FutureValueResults> futureValueList = new ArrayList<>();
    ArrayList<FutureValueResults> futureReturnList = new ArrayList<>();
    ArrayList<CashflowResults> cumCashflowList = new ArrayList<>();
    ArrayList<HorizonResults> portHorzList = new ArrayList<>();
    ArrayList<TradeValue> tradeValueList = new ArrayList<>();
    // ArrayList<TradeEval> tradeEvalList = new ArrayList<>();
    CreditAgencyRating creditAgencyRating;
    String portName = UnAvails.NOTAVAILSTRING;
    double cumFaceAmt = 0.;
    double[] cumKeyRateDuration = new double[16];
    String requestAll;
    String consoleAllStr;
    String consoleCalcStr;
    String fileCalcStr;
    String fileAllStr;
    String consoleHorizonResults;
    String fileHorizonResults;

    public String runBond(String[] args) {
        // get the processing setup
        if (args == null) {
            System.out.println("\"" + "Error" + "\"" + ":" + "\"" + "No arguments were entered" + "\"" + "}");
            System.exit(-1);
        }
        initBasisPoint(args);
        if (error.hasErrorYn == 'Y') {
            return error.errorMessage;
        }
        secRequestList = getUserRequestList(requestAll);
        if (error.hasErrorYn == 'Y') {
            return error.errorMessage;
        }
        // extract the calculation list applied to all securities
        calculationList = getCalcRequestList(secRequestList);
        noCalculations = calculationList.size();
        allYieldCurves = loadYieldCurves(calculationList);
        if (error.hasErrorYn == 'Y') {
            System.out.println(error.errorMessage);
            System.exit(-1);
        }

        noSecurities = secRequestList.size();
        commandLine.setDelimiters(noSecurities);
        // strips portfolio params
        if (error.hasErrorYn == 'Y') {
            System.out.println(error.errorMessage);
            System.exit(-1);
        }
        fileAllStr = "";
        consoleAllStr = "";
        cfrSec = calcSecuritiesFromString(secRequestList, calculationList);
        if (error.hasErrorYn == 'Y') {
            return cfrSec.consoleStr = error.errorMessage;
        }
        consoleAllStr = "";
        fileAllStr = "";
        if (noSecurities == 1) {
            if (bond.fileOutYn == 'Y' || commandLine.isFileOutputYn.equals("Y")) {
                fileAllStr = cfrSec.cumFileStr;
                fileAllStr = fileAllStr.replace("\"", "");
                try {
                    fileFormatter.format("%s", fileAllStr);
                    fileFormatter.flush();
                    fileFormatter.close();
                } catch (Exception ex) {

                }
            }
            return cfrSec.cumConsoleStr; // sent to called
        }
        // at this point consoleAll has no separarator at end
        ConsoleFileResults cfrPort = new ConsoleFileResults();
        cfrPort = calcPortfolioTotals();
        if (calcMethod.isInCalculation(CalcList.SWAPANALYSIS, calculationList)) {
            ConsoleFileResults cfrSwap = new ConsoleFileResults();
            tradeEvals.setRiskReturnGoal(bond.riskReturnGoal, bond.swapPctGain);
            tradeEvals.swapTrades = tradeEvals.calcTradeEval(tradeValueList, bond.dtSettle, bond.dtFuture);
            tradeEvals.swapPortfolio = tradeEvals.getSwapPortfolio(tradeEvals.swapTrades);
            cfrSwap = tradeEvals.outputSwapResultsConsole();
            tradeEvals.outputTradeEvalSummaryFile(Defaults.SWAPTRADESFILENAME, bond.fileOutFormat, Limits.MAXTOPSWAPS);
            tradeEvals.outputSwapPortfolioFile(Defaults.SWAPPORTFOLIOFILENAME, bond.fileOutFormat, Limits.MAXTOPSWAPS);

            return cfrSwap.consoleStr;
        }
        if (!cfrPort.cumConsoleStr.isEmpty()) {
            if (bond.showIndivCalcYn == 'Y') {
                if (cfrRun.cumConsoleStr.isEmpty() == false) {
                    cfrRun.cumConsoleStr += commandLine.consoleCalcDelimit + commandLine.consoleObjectStartDelimit
                            + cfrPort.cumConsoleStr + commandLine.consoleMultiPostFix;
                    cfrRun.cumFileStr += commandLine.fileCalcDelimit + cfrPort.cumFileStr;
                } else
                    cfrRun.cumConsoleStr += cfrPort.cumConsoleStr + commandLine.consoleObjectEndDelimit;
            } else {
                cfrRun.cumConsoleStr = commandLine.consoleObjectStartDelimit + cfrPort.cumConsoleStr
                        + commandLine.consoleObjectEndDelimit;
                cfrRun.cumFileStr = cfrPort.cumFileStr + commandLine.fileCalcDelimit;
            }
        }

        if (bond.fileOutYn == 'Y' || commandLine.isFileOutputYn.equals("Y")) {
            cfrRun.cumFileStr = cfrRun.cumFileStr.replace("\"", "");
            fileFormatter.format("%s", cfrRun.cumFileStr);
            fileFormatter.flush();
            fileFormatter.close();
        }
        return cfrRun.cumConsoleStr;
    }

    public boolean setConfiguration(CommandLine commandLine) {
        String errmsg = "";
        String outPutFile;

        if (commandLine.useConfigYn.equals("N")) {
            return true;
        }
        outPutFile = commandLine.rootDirectory + "\\log\\log.bsdata";
        boolean same = outPutFile.equals(outPutFile);
        try {
            logWriter = new FileWriter(outPutFile, true);
        } catch (Exception ex) {
            errmsg = "Error: could not open file " + outPutFile;
            System.out.println(errmsg);
            return false;
        }
        outPutFile = commandLine.rootDirectory + "\\dump\\dump.bsdata";
        try {
            dumpWriter = new FileWriter(outPutFile, true);
        } catch (Exception ex) {
            errmsg = "Error: could not open file " + outPutFile;
            System.out.println(errmsg);
            return false;
        }
        error.setErrorLogging(logWriter, dumpWriter);
        // error = new Error(logFormatter, dumpFormatter, isOutputJSONYn);
        String title = "#====================================================================================";
        Date dt = new Date();
        String version = "# BasisPoint " + BondConsts.VERSION + " on " + dt;
        try {
            logWriter.write(title + "\n");
            logWriter.write(version + "\n");
            logWriter.flush();
            dumpWriter.write(title + "\n");
            dumpWriter.write(version + "\n");
            dumpWriter.flush();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        // to Do get holidays from database table
        return true;
    }

    // creates the security request list in standard format without any portfolio
    // params
    private ArrayList<String> getUserRequestList(String requestAll) {
        ArrayList<String> secRequestList = new ArrayList<>();
        boolean isOk = getInvalidParameter(requestAll);
        if (!isOk) {
            return secRequestList;
        }
        if (commandLine.quotesIncluded.equals("Y")) {
            try {
                JSONParser jsonParser = new JSONParser(error);
                String nextReqStr = jsonParser.parseJson(requestAll.toUpperCase());
                nextReqStr = nextReqStr.toUpperCase();
                secRequestList.add(nextReqStr);
                commandLine.userRequestList.add(nextReqStr);
            } catch (Exception ex) {
                String errmsg = "Invalid JSON string " + requestAll;
                error.setError(getClass().getName(), errmsg);
                return secRequestList;
            }
        } else {
            String strName;
            if (commandLine.isInputJSONYn.equals("Y"))
                strName = "JSON string request";
            else
                strName = "standard string request";
            try {
                secRequestList = setRequestList(requestAll, commandLine.isInputJSONYn);
            } catch (Exception ex) {
                String errmsg = "Invalid " + strName + requestAll;
                error.setError(getClass().getName(), errmsg);
                return secRequestList;
            }
        }
        if (error.hasErrorYn == 'Y') {
            return secRequestList;
        }
        return secRequestList;
    }

    private ArrayList<String> getSecurityRequestList(ArrayList<String> allRequestList, String portParams) {
        ArrayList<String> secRequestList = new ArrayList<>();
        secRequestList = resetSecRequestList(allRequestList, portParams);
        return secRequestList;
    }

    public Bond createBond(CommandLine commandLine, Error error, Holidays holidays, SecurityTypes securityTypes,
            Choices choices, Utility utility, DayCount dayCount, Time time) {
        Bond bond = new Bond(commandLine, error, holidays, securityTypes, choices, utility, dayCount, time);
        bond.createScheduleRules(holidays, error, dayCount, time);
        bond.schedTracks = new SchedTracks(bond, error, choices, dayCount);
        bond.taxes = new Taxes(dayCount);
        bond.createYieldCurves(commandLine, error, dayCount, utility);
        return bond;
    }

    private ArrayList<Integer> defaultCalcList() {
        ArrayList<Integer> calculationList = new ArrayList<>();
        calculationList.add(CalcList.ACCUREDINT);
        calculationList.add(CalcList.PRINCIPALPAYMENT);
        calculationList.add(CalcList.NETPAYMENT);
        calculationList.add(CalcList.PRICETOMATURIY);
        calculationList.add(CalcList.YIELDTOMATURITY);
        calculationList.add(CalcList.MODIFIEDDURSTD);
        calculationList.add(CalcList.CONVEXITY);
        calculationList.add(CalcList.MATLENYEARS);
        calculationList.add(CalcList.YIELDWORST);
        calculationList.add(CalcList.CASHFLOW);
        calculationList.add(CalcList.ZSPREAD);
        calculationList.add(CalcList.KEYRATEDURATION);
        return calculationList;
    }

    public boolean getInvalidParameter(String reqString) {
        boolean isOk = true;
        if (commandLine.isInputJSONYn.equals("Y")) {
            isOk = getJsonInvalidParameter(reqString);
            return isOk;
        }
        isOk = getStandardInvalidParameter(reqString);
        return isOk;
    }

    public boolean getJsonInvalidParameter(String jString) {
        int noChars = 0;
        noChars = jString.length();
        String cJString = "";
        String cString = "";
        cJString = jString.replaceAll("\"", "");
        if (cJString.charAt(0) == '[') {
            cJString = cJString.substring(1);
        }
        if (cJString.charAt(0) == '{') {
            cJString = cJString.substring(1);
        }

        cString = replaceInternalCommas(cJString);
        StringTokenizer strTok = new StringTokenizer(cString, ",");
        StringToBond stringToBond = new StringToBond(bond, "Y", error, choices, dayCount);
        stringToBond.setParamMaps();
        while (strTok.hasMoreTokens()) {
            String strTerm = strTok.nextToken();
            StringTokenizer strTokTerm = new StringTokenizer(strTerm, ":");
            String paramName = strTokTerm.nextToken();
            paramName = paramName.replaceAll("\\{", "");
            paramName = paramName.toUpperCase();
            stringToBond.paramMap = stringToBond.paramMapHashMap.get(paramName);
            if (stringToBond.paramMap == null) {
                String errmsg = "Parameter " + paramName + " is not a valid parameter";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        return true;
    }

    public boolean getStandardInvalidParameter(String stdString) {
        String errmsg = "";
        int noChars = 0;
        noChars = stdString.length();
        StringTokenizer strTok = new StringTokenizer(stdString, ";");
        stringToBond.setParamMaps();
        while (strTok.hasMoreTokens()) {
            String strTerm = strTok.nextToken();
            StringTokenizer strTokTerm = new StringTokenizer(strTerm, "=");
            String paramName = strTokTerm.nextToken();
            paramName = paramName.toUpperCase();
            stringToBond.paramMap = stringToBond.paramMapHashMap.get(paramName);
            if (stringToBond.paramMap == null) {
                errmsg = "Parameter " + paramName + " is not a valid parameter";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
        return true;
    }

    public String replaceInternalCommas(String jString) {
        int i = -1;
        String cString = "";
        boolean isInArray = false;
        char chReplace = '#';
        while (++i < jString.length()) {
            char ch = jString.charAt(i);
            if (ch != '[') { // not at end of an array
                if (isInArray) {
                    if (ch == ',')
                        cString += '#';
                    else if (ch == ']') {
                        cString += ch;
                        isInArray = false;
                    } else
                        cString += ch;
                } else
                    cString += ch;
            } else { // at end of array
                cString += ch;
                isInArray = true;
            }
        }
        return cString;
    }

    public ArrayList<String> setRequestList(String requestAll, String isJsonString) {
        ArrayList<String> requestList = new ArrayList<>();
        ArrayList<String> jsonRequestList = new ArrayList<>();
        String upperRequestAll = requestAll.toUpperCase();
        if (isJsonString.equals("Y")) {
            requestList = setJsonRequestList(upperRequestAll);
            if (error.hasErrorYn == 'Y') {
                return requestList;
            }
            return requestList;
        }
        requestList = setStdRequestList(upperRequestAll);
        if (error.hasErrorYn == 'Y')
            return requestList;
        int ir = -1;
        int noReqs = requestList.size();
        while (++ir < noReqs) {
            commandLine.userRequestList.add(requestList.get(ir).replace(';', '\n'));
        }
        return requestList;
    }

    public ArrayList<Integer> setCalculations(ArrayList<String> requestList) {
        ArrayList<Integer> calcArrayList = new ArrayList<>();
        String strTerm;
        String strRequest;
        int noTerms = requestList.size();
        String[] strParts = new String[2];
        int noCalc = 0;
        int calcNo = 0;
        int ir = -1;
        int count = 0;
        StringTokenizer stok;
        while (++ir < 1) {
            strRequest = requestList.get(ir).toUpperCase();
            stok = new StringTokenizer(strRequest, ";");
            while (stok.hasMoreTokens()) {
                strTerm = stok.nextToken();
                strParts = strTerm.split("=");
                if (strParts[0].equals("CALCREQUEST")) {
                    try {
                        String calcNoStr = strParts[1].trim();
                        calcNoStr = calcNoStr.replace('[', ' ');
                        calcNoStr = calcNoStr.replace(']', ' ');
                        calcNoStr = calcNoStr.trim();
                        try {
                            // separate the calculs
                            StringTokenizer calcTok = new StringTokenizer(calcNoStr, ",");
                            while (calcTok.hasMoreTokens()) {
                                String strCalc = calcTok.nextToken();
                                try {
                                    calcArrayList.add(count++, Integer.valueOf(strCalc));
                                } catch (Exception ex) {
                                    error.setError(getClass().getName(), ex.getMessage());
                                    return calcArrayList;
                                }
                            }
                        } catch (Exception ex) {
                            error.setError(getClass().getName(), ex.getMessage());
                            return calcArrayList;
                        }
                        // bond.= utility.convertToIntList(calcNoStr, "calculationrequests");
                    } catch (Exception ex) {
                        String errmsg = "an invalid value for a calculation has been specified: " + strTerm;
                        error.setError(getClass().getName(), errmsg);
                        return calcArrayList;
                    }

                }
            }
        }
        return calcArrayList;
    }

    public ArrayList<String> splitJsonRequest(String bondRequest) {
        ArrayList<String> splitList = new ArrayList<>();
        int noChars = bondRequest.length();
        int cNo = -1;
        String jRequest = "";
        char ch = '?';
        String strNew = "\"" + "NEW" + ";";
        String strTest = "";
        boolean lastMatch = false;
        while (++cNo < noChars) {
            ch = bondRequest.charAt(cNo);
            if (ch == '}') {
                jRequest += ch;
                splitList.add(jRequest);
                jRequest = "";
                // skip comma
                ++cNo;
                continue;
            }
            jRequest += ch;
        }
        if (jRequest.length() > 1)
            splitList.add(jRequest);
        return splitList;
    }

    public ArrayList<String> setJsonRequestList(String jString) {
        String strReq = "";
        ArrayList<String> jsonRequestList = new ArrayList<>();
        ArrayList<String> jsonSecList = new ArrayList<>();
        // how many securities
        JSONParser jsonParser = new JSONParser(error);
        // break command line request into separate json requests
        // to handle multiple securiy requests
        jsonSecList = getSecRequests(jString);
        int noReqs = jsonSecList.size();
        if (error.hasErrorYn == 'Y')
            return jsonRequestList;
        // parse individual security reque
        if (noReqs < 0) {
            error.setError(getClass().getName(), "no security calculation request were specified");
            return jsonRequestList;
        }
        int reqNo = -1;
        String jsonUserReq = "";
        String strParts = "";
        String nextReqStr;
        while (++reqNo < noReqs) {
            nextReqStr = jsonSecList.get(reqNo);
            nextReqStr = jsonParser.parseJson(nextReqStr);
            commandLine.userRequestList.add(nextReqStr);
            if (error.hasErrorYn == 'Y') {
                return jsonRequestList;
            }
            jsonRequestList.add(nextReqStr);
        }
        /*
         * 
         * strReq = strReq + commandLine.consoleStdSecStartDelimit+ nextReqStr;
         * commandLine.userRequestList.add(nextReqStr);
         * if (reqNo < noReqs - 1)
         * strReq = strReq.concat(commandLine.consoleStdCalcDelimit +
         * commandLine.consoleStdSecStartDelimit);
         * }
         * 
         * if (error.hasErrorYn == 'Y') {
         * return jsonRequestList;
         * }
         * strReq = strReq.replace('{', ' ');
         * strReq = strReq.replace('}', ' ');
         * jsonRequestList = setStdRequestList(strReq);
         */

        return jsonRequestList;
    }

    public ArrayList<String> getSecRequestsOld(String jString) {
        ArrayList<String> secRequestList = new ArrayList<>();
        int len = jString.length();
        boolean inArray = false; // in an array of parameters within a given security request
        int iCh = -1; // ith character in JSON
        char lCh = '?', nCh = '?';
        int iSecCh = -1; // ith last position is security request
        boolean inSecArray = false;
        int noSecs = 0;
        String str = "";
        if (jString.length() < Limits.MINREQUESTCHARS) {
            String errmsg = "JSON string request have at least " + Limits.MINREQUESTCHARS + " characters";
            error.setError(getClass().getName(), errmsg);
            return secRequestList;
        }
        char ch = jString.charAt(0);
        Strings strings = new Strings(error);
        // find first keyword and value - is it an array of securutities with multiple {
        // } or just one
        // do the 1st character for parameter name
        if (ch == '[' && jString.charAt(1) == '{') {
            // multiple securities requested
            String[] secParts = jString.split("},");
            int noParts = secParts.length;
            int ip = 0;
            // remove the dyartinb array bracket [
            String jStrReq = secParts[0].substring(1);
            // add the ending onjecty bracket }
            jStrReq = jStrReq + "}"; // {param:value}
            jStrReq = strings.quotizeJsonString(jStrReq);
            if (error.hasErrorYn == 'Y') {
                return secRequestList;
            }
            secRequestList.add(ip, jStrReq);
            while (++ip < noParts - 1) {
                jStrReq = secParts[ip] + "}";
                jStrReq = strings.quotizeJsonString(jStrReq);
                if (error.hasErrorYn == 'Y') {
                    return secRequestList;
                }
                secRequestList.add(ip, jStrReq);

            }
            // do the last security request
            jStrReq = secParts[noParts - 1];
            len = jStrReq.length();
            if (!jStrReq.contains("}]") && jStrReq.charAt(len - 1) != ']') {
                String errmsg = "Invalid multi-security JSON request string - the string does not end in }]}";
                error.setError(getClass().getName(), errmsg);
                return secRequestList;
            }
            // remove closing array bracket ] and closing json delimiter brackett
            jStrReq = jStrReq.substring(0, len - 2);
            jStrReq = strings.quotizeJsonString(jStrReq);
            secRequestList.add(ip, jStrReq);
        } else {
            if (ch == '{') {
                jString = strings.quotizeJsonString(jString);
                secRequestList.add(0, jString);
            } else {
                String errmsg = "Invalid JSON string - missing initial { for single request or [{ for multiple security requests";
                error.setError(getClass().getName(), errmsg);
                return secRequestList;
            }
            return secRequestList;
        }
        return secRequestList;
    }

    public ArrayList<String> getSecRequests(String jString) {
        ArrayList<String> secRequestList = new ArrayList<>();
        int len = jString.length();
        boolean inArray = false; // in an array of parameters within a given security request
        int iCh = -1; // ith character in JSON
        char lCh = '?', nCh = '?';
        int iSecCh = -1; // ith last position is security request
        boolean inSecArray = false;
        int noSecs = 0;
        String str = "";
        if (jString.length() < Limits.MINREQUESTCHARS) {
            String errmsg = "JSON string request have at least " + Limits.MINREQUESTCHARS + " characters";
            error.setError(getClass().getName(), errmsg);
            return secRequestList;
        }
        char ch = jString.charAt(0);
        String jStrReq = "";
        Strings strings = new Strings(error);
        // find first keyword and value - is it an array of securutities with multiple {
        // } or just one
        // do the 1st character for parameter name
        if (ch == '[' && jString.charAt(1) == '{') {
            // multiple securities requested
            jString = jString.substring(1);
            int sLen = jString.length();
            jString = jString.substring(0, sLen - 2);
            String[] strParts = jString.split("},");
            int noParts = strParts.length;
            String jQStrReq = "";
            int ip = -1;
            while (++ip < noParts) {
                jStrReq = strParts[ip].substring(0);
                // add the ending ojecty bracket }
                jStrReq = jStrReq + "}";
                jQStrReq = strings.quotizeJsonString(jStrReq);
                secRequestList.add(ip, jQStrReq);
            }

        } else {
            if (ch == '{') {
                jString = strings.quotizeJsonString(jString);
                secRequestList.add(0, jString);
            } else {
                String errmsg = "Invalid JSON string - missing initial { for single request or [{ for multiple security requests";
                error.setError(getClass().getName(), errmsg);
                return secRequestList;
            }
            return secRequestList;
        }
        return secRequestList;
    }

    public ArrayList<String> setStdRequestList(String strRequest) {
        ArrayList<String> requestList = new ArrayList<>();
        // StringTokenizer stok = new StringTokenizer(strString, ";NEW;");
        String[] strParts = strRequest.split(",New,");
        String reqStr = "";
        if (strRequest.length() < Limits.MINREQUESTCHARS) {
            String errmsg = "Request string must contain at least " + Limits.MINREQUESTCHARS + "characters";
            error.setError(getClass().getName(), errmsg);
            return requestList;
        }
        int noReqs = strParts.length;
        int pn = -1;
        while (++pn < noReqs) {
            reqStr = strParts[pn].toUpperCase();
            requestList.add(reqStr);
        }
        return requestList;
    }

    public ConsoleFileResults calcSecuritiesFromString(ArrayList<String> requestList, ArrayList<Integer> calcList) {
        String requestStr = "";
        int securityNo = -1;
        boolean useNewNextCalc = false;
        String calcSecStr = ""; // specific security result for all calc request no separator chaes
        String cumSecStr = ""; // cumulative security results with calc prefix, postfix and calc seopartors
        String recapStr = "", securityNameParam = "Security=";
        bondDefault.setCalculationList(calcList);
        bondCheck.setCalculationList(calcList);
        if (noSecurities > 1) {
            // get the portfolio params to be used for all securities
            String portfolioParams = getPortfolioParams(secRequestList);
            if (error.hasErrorYn == 'Y') {
                cfrSec.cumFileStr = error.errorMessage;
                cfrSec.cumFileStr = error.errorMessage;
                cfrSec.consoleStr = error.errorMessage;
                cfrSec.fileStr = error.errorMessage;
                return cfrSec;
            }
            // remove any security level portfolio params and substitute commnon ones
            secRequestList = getSecurityRequestList(secRequestList, portfolioParams);
            if (error.hasErrorYn == 'Y') {
                cfrSec.cumFileStr = error.errorMessage;
                cfrSec.cumFileStr = error.errorMessage;
                cfrSec.consoleStr = error.errorMessage;
                cfrSec.fileStr = error.errorMessage;
                return cfrSec;
            }
        }
        int ic = -1;
        while (++securityNo < noSecurities) {
            requestStr = secRequestList.get(securityNo);
            // self-sufficient single security calculartion string
            cfrSec.consoleStr = "";
            cfrSec.cumConsoleStr = "";
            cfrSec.fileStr = "";
            cfrSec.cumFileStr = "";
            cfrSec = calcSecurityFromString(securityNo, noCalculations, requestStr);
            if (cfrSec.consoleStr.equals(""))
                continue;
            if (error.hasErrorYn == 'Y') {
                cfrSec.consoleStr = commandLine.consoleObjectStartDelimit + cfrSec.consoleStr
                        + commandLine.consoleObjectEndDelimit;
                cfrSec.cumConsoleStr = cfrSec.consoleStr;
                cfrSec.cumFileStr += commandLine.fileCalcDelimit;
                return cfrSec;
            }
            int i = -1;
            while (++i < cumCashflowList.size()) {
                if (cumCashflowList.get(i).dtCashflow.year == -1)
                    i = i;

            }
            // at this point the job of the code is to put delimiters around security
            // results
            if (noSecurities == 1) {
                cfrSec.cumConsoleStr = commandLine.consoleObjectStartDelimit + cfrSec.cumConsoleStr
                        + commandLine.consoleObjectEndDelimit;
                recapStr = setRecapString(commandLine.showRecapYn, securityNo, commandLine.fileParamDelimit);
                cfrSec.cumFileStr = cfrSec.cumFileStr + commandLine.fileCalcDelimit + recapStr;
                return cfrSec;
            }
            if (securityNo == 0) { // first security in multi-security batch
                cfrRun.cumConsoleStr = commandLine.consoleMultiPreface + cfrSec.cumConsoleStr
                        + commandLine.consoleObjectEndDelimit;
                cfrRun.cumFileStr = cfrSec.cumFileStr + commandLine.fileCalcDelimit;
                if (commandLine.showRecapYn.equals("Y")) {
                    recapStr = setRecapString(commandLine.showRecapYn, securityNo, commandLine.fileParamDelimit);
                    cfrRun.cumFileStr += recapStr + commandLine.fileCalcDelimit;
                }
            }
            if (securityNo == noSecurities - 1) { // last security term
                cfrRun.cumConsoleStr += commandLine.consoleCalcDelimit + commandLine.consoleObjectStartDelimit
                        + cfrSec.cumConsoleStr + commandLine.consoleObjectEndDelimit;
                cfrRun.cumFileStr += commandLine.fileCalcDelimit + cfrSec.cumFileStr + commandLine.fileCalcDelimit;
                if (commandLine.showRecapYn.equals("Y")) {
                    recapStr = setRecapString(commandLine.showRecapYn, securityNo, commandLine.fileParamDelimit);
                    cfrRun.cumFileStr += commandLine.fileCalcDelimit + recapStr + commandLine.fileCalcDelimit;
                }
            } else {
                // for JSON end secDelimiter = }] for std console ""
                cfrRun.cumConsoleStr += commandLine.consoleCalcDelimit + commandLine.consoleObjectStartDelimit
                        + cfrSec.cumConsoleStr + commandLine.consoleObjectEndDelimit;
                cfrRun.cumFileStr += commandLine.fileCalcDelimit + cfrSec.cumFileStr + commandLine.fileCalcDelimit;
                if (commandLine.showRecapYn.equals("Y")) {
                    recapStr = setRecapString(commandLine.showRecapYn, securityNo, commandLine.fileParamDelimit);
                    cfrRun.cumFileStr += recapStr + commandLine.fileCalcDelimit;
                }
            }
        }
        return cfrRun;
    }

    public ConsoleFileResults calcSecurityFromString(int securityNo, int noCalculations, String requestStr) {
        int noCalc = -1, calcNo = UnAvails.NOTAVAILINT;
        String calcSecStr = "";
        ConsoleFileResults cfrSec = new ConsoleFileResults();
        // bond = new Bond(commandLine, holidays, securityTypes, utility,
        // calculationList);
        error.clearError();
        String userOrigParamString = requestStr;
        error.userOrigRequest = requestStr;
        // bond.clearYieldCurves();
        // set bond value to not available
        // set all members to unavilable
        bond.clearBond();
        stringToBond.setBond(bond);
        stringToBond.assignBondParameters(requestStr); // populate bond with user entered string for this security
        if (error.hasErrorYn == 'Y') {
            cfrSec.consoleStr = error.errorMessage;
            cfrSec.fileStr = error.errorMessage;
            return cfrSec;
        }

        dayCount.setMaturityDate(bond.dtMaturity);
        bond.noRequests = calculationList.size();
        bondDefault.setBond(bond);
        bondDefault.setDefaults(commandLine.isInputJSONYn, securityNo, calculationList);
        if (error.hasErrorYn == 'Y') {
            cfrSec.consoleStr = error.errorMessage;
            cfrSec.fileStr = error.errorMessage;
            return cfrSec;
        }
        tradeEvals.setAssumptions(bond.dtSettle, bond.dtFuture, bond.portName);
        bond.schedTracks.schedTrackList.clear();
        bond.schedTracks.setBond(bond);
        bond.schedTracks.setSchedTracks(bond.instype);
        portName = bond.portName;
        if (error.hasErrorYn == 'Y') {
            // go to next bond
            cfrSec.consoleStr = error.errorMessage;
            cfrSec.cumConsoleStr = error.errorMessage;
            cfrSec.fileStr = error.errorMessage;
            cfrSec.cumFileStr = error.errorMessage;
            error.clearError();
            error.stopPortTotal = true;
            return cfrSec;
        }
        bond.status.clear();
        bondCheck.setBond(bond);
        calcCheck.setBond(bond);
        if (error.hasErrorYn == 'Y') { // go to next bond
            cfrSec.consoleStr = error.errorMessage;
            cfrSec.cumConsoleStr = error.errorMessage;
            cfrSec.fileStr = error.errorMessage;
            cfrSec.cumFileStr = error.errorMessage;
            error.clearError();
            return cfrSec;
        }
        if (noCalculations == 0) {
            String errmsg = "no calculation requests were specified";
            error.setError(getClass().getName(), errmsg);
            cfrSec.consoleStr = error.errorMessage;
            cfrSec.cumConsoleStr = error.errorMessage;
            cfrSec.fileStr = error.errorMessage;
            cfrSec.cumFileStr = error.errorMessage;
            error.clearError();
            return cfrSec;
        }
        // deal with spaces in comment where we set space to ^
        dayCount.setMaturity(bond.dtMaturity);
        calcMethod.setCalcMethod(bond, cashflowResults);
        error.setDumpString(userOrigParamString);
        if (error.hasErrorYn == 'Y') {
            cfrSec.consoleStr = error.errorMessage;
            cfrSec.cumConsoleStr = error.errorMessage;
            cfrSec.fileStr = error.errorMessage;
            if (noCalculations == 0)
                cfrSec.cumConsoleStr = error.errorMessage;
            else
                // requestResult = "\nNew\n" + error.errorMessage;
                cfrSec.cumFileStr = "\n" + error.errorMessage;
            if (bond.fileOutYn == 'Y') {
                fileFormatter.format("%s\n", calcSecStr);
                fileFormatter.format("============ Entered Parameters ============\n");
                fileFormatter.format("%s", requestStr + "\n" + "============ End of Input Parameters ============\n");
            }
            return cfrSec;
        }
        boolean isOk;
        isOk = checkSecurityHolding(bond.faceAmt, bond.mktPrcStart, bond.account, bond.secDescr);
        if (error.hasErrorYn == 'Y') {
            cfrRun.cumConsoleStr = error.errorMessage;
            cfrRun.cumFileStr = error.errorMessage;
            error.clearError();
            return cfrRun;
        }
        if (error.hasErrorYn == 'Y') {
            cfrRun.cumConsoleStr = error.errorMessage;
            cfrRun.cumFileStr = error.errorMessage;
            error.clearError();
            return cfrRun;
        }
        //
        cfrSec = calcSecurity(securityNo, noCalculations, bond.instype);
        if (!cfrSec.cumConsoleStr.isEmpty()) {
            String consoleDescr = commandLine.consoleQuote + CalcList.BONDDESCRIPTIONTAG + commandLine.consoleQuote
                    + commandLine.consoleParamDelimit + commandLine.consoleQuote + bond.secDescr
                    + commandLine.consoleQuote + commandLine.consoleCalcDelimit;
            String fileDescr = CalcList.BONDDESCRIPTIONTAG + commandLine.fileParamDelimit + bond.secDescr
                    + commandLine.fileCalcDelimit;
            cfrSec.cumConsoleStr = consoleDescr + cfrSec.cumConsoleStr;
            cfrSec.cumFileStr = fileDescr + cfrSec.cumFileStr;
            if (UnAvails.isAvail(bond.issuer)) {
                consoleDescr = commandLine.consoleQuote + CalcList.BONDISSUERTAG + commandLine.consoleQuote
                        + commandLine.consoleParamDelimit + commandLine.consoleQuote + bond.issuer
                        + commandLine.consoleQuote + commandLine.consoleCalcDelimit;
                fileDescr = CalcList.BONDISSUERTAG + commandLine.fileParamDelimit + bond.issuer
                        + commandLine.fileCalcDelimit;
                cfrSec.cumConsoleStr = consoleDescr + cfrSec.cumConsoleStr;
                cfrSec.cumFileStr = fileDescr + cfrSec.cumFileStr;
            }
            if (UnAvails.isAvail(bond.securityId)) {
                consoleDescr = commandLine.consoleQuote + CalcList.SECURITYIDTAG + commandLine.consoleQuote
                        + commandLine.consoleParamDelimit + commandLine.consoleQuote + bond.securityId
                        + commandLine.consoleQuote + commandLine.consoleCalcDelimit;
                fileDescr = CalcList.SECURITYIDTAG + commandLine.fileParamDelimit + bond.securityId
                        + commandLine.fileCalcDelimit;
                cfrSec.cumConsoleStr = consoleDescr + cfrSec.cumConsoleStr;
                cfrSec.cumFileStr = fileDescr + cfrSec.cumFileStr;
            }
        }
        return cfrSec;
    }

    public boolean checkSecurityHolding(double faceAmt, double mktPrc, String account, String secDescr) {
        if (noSecurities <= 1)
            return true;

        if (UnAvails.isUnAvail(faceAmt) && noSecurities > 1) {
            String errmsg = "no face amount was specified for security: " + secDescr;
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (UnAvails.isUnAvail(bond.mktPrcStart) && noSecurities > 1) {
            if (UnAvails.isAvail(bond.settlePrc))
                bond.mktPrcStart = bond.settlePrc;
            else {
                bond.mktPrcStart = 1.;
                String errmsg = "No market price was specified for weighting portfolio statistic for security: "
                        + secDescr;
                error.setWarning(getClass().getName(), errmsg);
            }
            return true;
        }

        if (UnAvails.isUnAvail(account)) {
            String warning = "No account was specified for portfolio statistics: " + secDescr;
            error.setWarning(getClass().getName(), warning);
            return true;
        }
        return true;
    }

    private ConsoleFileResults calcSecurity(int securityNo, int noCalculations, String instype) {
        int noCalc = -1;
        int calcNo = UnAvails.NOTAVAILINT;
        String cumCalcStr = "";
        error.clearError();
        calcCheck.setBond(bond);
        boolean isOk = bondCheck.checkBond(bond.instype);
        if (error.hasErrorYn == 'Y') {
            cfrSec.consoleStr = error.errorMessage;
            cfrSec.fileStr = error.errorMessage;
            return cfrSec;
        }
        cfrSec.consoleStr = "";
        cfrSec.fileStr = "";
        cfrSec.cumConsoleStr = "";
        cfrSec.cumFileStr = "";
        while (++noCalc < noCalculations) {
            calcNo = calculationList.get(noCalc);
            int parentCalcNo = calcNo;
            // calcStr = bsCalculate(securityNo, noCalc, noCalculations, calcNo, instype);
            cfrSec = bsCalculate(securityNo, calcNo, noCalculations, instype);
            if (cfrSec.consoleStr.equals(""))
                continue;
            // is this the last calculucation for the last security
            if (noCalc == noCalculations - 1) {
                cfrSec.cumConsoleStr += cfrSec.consoleStr;
                cfrSec.cumFileStr += cfrSec.fileStr;
            } else {
                cfrSec.cumConsoleStr += cfrSec.consoleStr + commandLine.consoleCalcDelimit;
                cfrSec.cumFileStr += cfrSec.fileStr + commandLine.fileCalcDelimit;
            }
        }
        return cfrSec;
    }

    public ConsoleFileResults bsCalculate(int securityNo, int calcNo, int requestCount, String instype) {
        double dResult = 0.;
        int iResult = 0;
        Dates dtResult;
        String calcName = calcDef.calcDefs.get(calcNo).calcName;
        error.clearError();
        calcMethod.calcResults.currentCalc = calcNo;
        calcMethod.calcResults.parentCalc = calcNo;
        double cashScale = 1.;
        error.setCalcName(calcName);
        if (calcNo == CalcList.CASHFLOW && securityNo == 1)
            calcNo = calcNo;
        switch (calcNo) {
            case CalcList.ACCUREDINT:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = calcMethod.accruedInterest(calcNo, bond.instype, bond.dtSettle);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigAccrued);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigAccrued);
                break;
            case CalcList.INTERESTINCOME:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = calcMethod.interestIncome(calcNo, instype, bond.redeemInt, bond.dtStart, bond.dtEnd);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigCurrency);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigCurrency);
                break;
            case CalcList.YIELDTOMATURITY:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else
                    dResult = calcMethod.yieldPrice(calcNo, securityNo, instype, bond.dtSettle, bond.settlePrc,
                            bond.dtRedeem, bond.redeemPrin, bond.redeemInt);
                // System.out.println("Yield = " + dResult + " Security = " + bond.secDescr +
                // "\n");
                dResult *= calcDef.calcDefs.get(CalcList.YIELDTOMATURITY).scale;
                securityResults.add(++calcResultNo,
                        new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigYield);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigYield);
                break;
            case CalcList.PRICETOMATURIY:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else
                    dResult = calcMethod.priceYield(calcNo, securityNo, instype, bond.dtSettle, bond.settleYld,
                            bond.dtRedeem, bond.redeemPrin, bond.redeemInt);
                dResult *= calcDef.calcDefs.get(CalcList.PRICETOMATURIY).scale;
                securityResults.add(++calcResultNo,
                        new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigPrc);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigPrc);
                break;
            case CalcList.MACAULAYDUR:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = calcMethod.macDuration(calcNo, securityNo, instype, bond.dtSettle, bond.settleYld,
                            bond.dtMaturity, bond.redeemPrin, bond.redeemPrin);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.settlePrc, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigStatistic);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigStatistic);
                break;
            case CalcList.MODIFIEDDURSTD:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = calcMethod.modDurationSTD(calcNo, securityNo, instype, bond.dtSettle, bond.settleYld,
                            bond.dtRedeem, bond.redeemInt, bond.redeemPrin);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));

                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigStatistic);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigStatistic);
                break;
            case CalcList.MODIFIEDDURSIA:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = calcMethod.modDurationSIA(calcNo, securityNo, instype, bond.dtSettle, bond.settleYld,
                            bond.dtRedeem, bond.redeemInt, bond.redeemPrin);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigStatistic);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigStatistic);
                break;
            case CalcList.CONVEXITY:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = calcMethod.conVexity(calcNo, securityNo, instype, bond.dtSettle, bond.settleYld,
                            bond.dtRedeem, bond.redeemInt, bond.redeemPrin);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigStatistic);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigStatistic);
                break;
            case CalcList.MATLENYEARS:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = dayCount.matLenYears(calcNo, bond.dtSettle, bond.dtRedeem, bond.dtMaturity);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigStatistic);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigStatistic);
                break;
            case CalcList.MATlENDAYS:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    iResult = 0;
                else {
                    iResult = dayCount.matLenDays(calcNo, bond.dtSettle, bond.dtMaturity);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, iResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputIntConsole(calcNo, iResult);
                fileCalcStr = calcMethod.outputIntFile(calcNo, iResult);
                break;
            case CalcList.MATLENYEARSWTD:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = dayCount.matLenYearsWtd(calcNo, bond.dtSettle, bond.dtMaturity);
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigStatistic);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigStatistic);
                break;
            case CalcList.MATLENDAYSWTD:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    iResult = 0;
                else {
                    iResult = dayCount.matLenDaysWtd(calcNo, bond.dtSettle, bond.dtMaturity);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, iResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputIntConsole(calcNo, iResult);
                fileCalcStr = calcMethod.outputIntFile(calcNo, iResult);
                break;
            case CalcList.PARAMETERS:
                iResult = writeParamters(commandLine.rootDirectory);
                consoleCalcStr = calcMethod.outputIntConsole(calcNo, iResult);
                fileCalcStr = consoleCalcStr;
                break;
            case CalcList.CALCULATIONS:
                iResult = writeCalculations(commandLine.rootDirectory);
                consoleCalcStr = calcMethod.outputIntConsole(calcNo, iResult);
                fileCalcStr = consoleCalcStr;
                break;
            case CalcList.CHOICES:
                iResult = writeChoices(commandLine.rootDirectory);
                consoleCalcStr = calcMethod.outputIntConsole(calcNo, iResult);
                fileCalcStr = consoleCalcStr;
                break;
            case CalcList.DAYCOUNT:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    iResult = 0;
                else {
                    iResult = dayCount.dayCounting(calcNo, bond.dtStart, bond.dtEnd, bond.dtMaturity,
                            bond.dcAccruedInt);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, iResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputIntConsole(calcNo, iResult);
                consoleCalcStr = calcMethod.outputIntConsole(calcNo, iResult);
                fileCalcStr = calcMethod.outputIntFile(calcNo, iResult);
                break;
            case CalcList.CASHFLOW: // reininvestment cash may use forward yield curve
            case CalcList.CASHFLOWSHORT:
            case CalcList.CASHFLOWINTERVAL:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    iResult = 0;
                else {
                    String sCalcFileResult = "";
                    // security specific cashfloq lives in CalacResult cashglow result list
                    if (calcNo == CalcList.CASHFLOWINTERVAL) {
                        calcMethod.cashflowList = calcMethod.cashflow(calcNo, 1., securityNo, instype, bond.dtStart,
                                bond.dtEnd);
                    } else {
                        calcName = calcDef.calcDefs.get(CalcList.CASHFLOW).calcName;
                        calcMethod.cashflowList = calcMethod.cashflow(calcNo, 1., securityNo, instype, bond.dtSettle,
                                bond.dtRedeem);
                    }
                    // cumulate cashflowa
                    // int noFlows = calcMethod.cashflowList.size();
                    int noFlows = calcMethod.cashflowList.size();
                    int ic = -1;
                    while (++ic < noFlows) {
                        calcMethod.cashflowList.get(ic).totCashflow *= bond.faceAmt;
                        calcMethod.cashflowList.get(ic).totPrinCash *= bond.faceAmt;
                        calcMethod.cashflowList.get(ic).schedPrinCash *= bond.faceAmt;
                        calcMethod.cashflowList.get(ic).prePaycash *= bond.faceAmt;
                        calcMethod.cashflowList.get(ic).intCash *= bond.faceAmt;
                        calcMethod.cashflowList.get(ic).fedTaxPaid *= bond.faceAmt;
                        calcMethod.cashflowList.get(ic).stateTaxPaid *= bond.faceAmt;
                        cumCashflowList.add(cumCashflowCount++, calcMethod.cashflowList.get(ic));
                    }
                }
                consoleCalcStr = calcMethod.outputCashflowConsole(calcNo, calcName);
                fileCalcStr = calcMethod.outputCashflowFile(calcNo, calcName);
                // add cashflow
                break;
            case CalcList.PAYSCHEDULE:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    iResult = 0;
                else
                    iResult = calcMethod.paySchedule(calcNo, bond.dtSettle);
                consoleCalcStr = calcMethod.outputPayScheduleConsole(calcNo, calcNo, iResult);
                fileCalcStr = calcMethod.outputPayScheduleFile(calcNo, calcNo, iResult);
                break;
            case CalcList.KEYRATEDURATION: // uses discount spot curve
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    iResult = 0;
                else {
                    iResult = calcMethod.keyRateDuration.calcKeyRateDuration(calcNo, securityNo, instype,
                            bond.redeemPrin, bond.redeemInt);
                    int ik = -1;
                    while (++ik < iResult) {
                        cumKeyRateDuration[ik] += calcMethod.keyRateDuration.keyRateDurations[ik] * bond.faceAmt;
                        cumFaceAmt += bond.faceAmt;
                    }
                }
                consoleCalcStr = calcMethod.outputKeyRateConsole(calcNo, calcNo);
                fileCalcStr = calcMethod.outputKeyRateFile(calcNo, calcNo);
                break;
            case CalcList.COUPONDAYS:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    iResult = 0;
                else {
                    iResult = calcMethod.couponDays(calcNo, instype, bond.dtSettle);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, iResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputIntConsole(calcNo, iResult);
                fileCalcStr = calcMethod.outputIntFile(calcNo, iResult);
                break;
            case CalcList.YIELDWORST:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = calcMethod.yieldWorst(calcNo, securityNo, instype, bond.dtSettle, bond.settlePrc,
                            bond.dtRedeem, bond.redeemInt, bond.redeemPrin);
                    dResult *= calcDef.calcDefs.get(CalcList.YIELDWORST).scale;
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.settlePrc, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigYield);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigYield);
                break;
            case CalcList.DURATIONWORST:
                double ytw = 0.;
                Dates dtWorst = bond.dtMaturity;
                if (UnAvails.isUnAvail(calcMethod.calcResults.modDurationWorst)) {
                    ytw = calcMethod.yieldWorst(calcNo, securityNo, instype, bond.dtSettle, bond.settlePrc,
                            bond.dtRedeem, bond.redeemInt, bond.redeemPrin);
                    dtWorst = calcMethod.calcResults.dtWorst;
                } else {
                    ytw = calcMethod.calcResults.yieldToWorst;
                    dtWorst = calcMethod.calcResults.dtWorst;
                }
                dResult = calcMethod.modDurationSTD(calcNo, securityNo, instype, bond.dtSettle, ytw, dtWorst,
                        bond.redeemInt, calcMethod.calcResults.callPrcWorst);
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigYield);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigYield);
                break;
            case CalcList.DATEWORST:
                ytw = 0.;
                dtWorst = bond.dtMaturity;
                if (UnAvails.isUnAvail(calcMethod.calcResults.yieldToWorst))
                    ytw = calcMethod.yieldWorst(calcNo, securityNo, instype, bond.dtSettle, bond.settlePrc,
                            bond.dtRedeem, bond.redeemInt, bond.redeemPrin);
                else {
                    ytw = calcMethod.calcResults.yieldToWorst;
                    dtWorst = calcMethod.calcResults.dtWorst;
                }
                consoleCalcStr = calcMethod.outputConsoleDate(calcNo, dtWorst);
                fileCalcStr = calcMethod.outputFileDate(calcNo, dtWorst);
                break;
            case CalcList.BOOKVALUE:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = calcMethod.bookValue(calcNo, securityNo, instype, bond.dtSettle, bond.purYld,
                            bond.dtRedeem, bond.redeemPrin, bond.redeemInt, bond.bookCalcMeth);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigCurrency);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigCurrency);
                break;
            case CalcList.ACCREAMORT:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = calcMethod.accreAmort(calcNo, instype, bond.dtStart, bond.dtEnd, bond.purYld,
                            bond.bookCalcMeth);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigAccrual);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigAccrual);
                break;
            case CalcList.ACCREAMORTSCHED:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    iResult = 0;
                else
                    iResult = calcMethod.accreAmortSchedule(calcNo, securityNo, instype, bond.dtSettle, bond.purYld,
                            bond.dtRedeem, bond.redeemPrin, bond.redeemInt, bond.bookCalcMeth);
                consoleCalcStr = calcMethod.outputAccreAmortSchedule(calcNo, iResult);
                break;
            case CalcList.ZSPREAD:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = calcMethod.zSpread(calcNo, securityNo, instype, bond.dtSettle, bond.settlePrc);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigStatistic);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigStatistic);
                break;
            case CalcList.VERSION:
                consoleCalcStr = getVersionConsole();
                fileCalcStr = getVersionFile();
                break;
            case CalcList.PRINCIPALPAYMENT:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = calcMethod.principalPayment(calcNo, instype, bond.dtSettle, bond.settlePrc,
                            bond.redeemPrin);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigCurrency);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigCurrency);
                break;
            case CalcList.NETPAYMENT:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = calcMethod.netPayment(calcNo, instype, bond.dtSettle, bond.settlePrc);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigCurrency);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigCurrency);
                break;
            case CalcList.FUTUREVALUE: // at maturity date
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = calcMethod.futureValue(calcNo, securityNo, instype, bond.dtFuture, bond.dtRedeem,
                            bond.redeemPrin, bond.redeemInt);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigCurrency);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigCurrency);
                break;
            case CalcList.MATURITYCASH:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = calcMethod.maturityCash(calcNo, instype, bond.dtRedeem, bond.redeemPrin, bond.redeemInt);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigCurrency);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigCurrency);
                break;
            case CalcList.GAINLOSS:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = calcMethod.gainLoss(calcNo, securityNo, instype, bond.dtSettle, bond.dtRedeem,
                            bond.redeemPrin, bond.redeemInt, bond.mktPrcStart);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigCurrency);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigCurrency);
                break;
            case CalcList.HORIZONANALYSIS:
                ArrayList<HorizonResults> secHorzList = new ArrayList<>();
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'N') {
                    consoleCalcStr = commandLine.consoleQuote + calcDef.calcDefs.get(CalcList.HORIZONANALYSIS).totalName
                            + commandLine.consoleQuote + commandLine.consoleParamDelimit + bond.horizonPeriods;
                    secHorzList = calcMethod.horizonAnalysis(calcNo, securityNo, instype, bond.dtRedeem,
                            bond.redeemPrin, bond.redeemInt);
                }
                consoleCalcStr = outputConsoleHorizonResults(secHorzList);
                fileCalcStr = outputFileHorizonResults(secHorzList);
                if (error.hasErrorYn == 'N') {
                    if (noSecurities == 1) {
                        break;
                    } else {
                        int noHorz = secHorzList.size();
                        int noPort = portHorzList.size();
                        int no = -1;
                        while (++no < noHorz)
                            portHorzList.add(noPort++, new HorizonResults(secHorzList.get(no)));
                    }
                } else
                    error.clearError();
                break;
            case CalcList.REALYIELD:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = calcMethod.realYield(calcNo, securityNo, instype, bond.dtSettle, bond.settlePrc,
                            bond.dtRedeem, bond.redeemPrin, bond.redeemInt);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigYield);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigYield);
                break;
            case CalcList.FAIRPRICE:
                dResult = calcMethod.calcFairPrice(calcNo, securityNo, instype);
                dResult *= calcDef.calcDefs.get(calcNo).scale;
                securityResults.add(++calcResultNo,
                        new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigPrc);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigPrc);
                break;
            case CalcList.FORWARDCURVE:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    iResult = 0;
                else
                    iResult = bond.discCurve.forwardCurve.size();
                calcName = calcDef.calcDefs.get(CalcList.FORWARDCURVE).calcName;
                consoleCalcStr = outputConsoleYieldCurve(bond.discCurve, BondConsts.FORWARDCURVE,
                        calcDef.calcDefs.get(CalcList.FORWARDCURVE).calcName, commandLine.isInputJSONYn, calcNo);
                fileCalcStr = outputFileYieldCurve(bond.discCurve, BondConsts.FORWARDCURVE, calcNo);
                break;
            case CalcList.FUTUREPRICE:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    dResult = 0.;
                else {
                    dResult = calcMethod.futureValue(calcNo, securityNo, instype, bond.dtStart, bond.dtRedeem,
                            bond.redeemInt, bond.redeemPrin);
                    securityResults.add(++calcResultNo,
                            new SecurityResults(securityNo, calcNo, calcName, dResult, bond.mktPrcStart, bond.faceAmt));
                }
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigCurrency);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigCurrency);
                break;
            case CalcList.SPOTCURVE:
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn == 'Y')
                    iResult = 0;
                else
                    iResult = bond.discCurve.spotCurve.size();
                // iResult = calcMethod.calcYieldCurveCubicSpline(calcNo, securityNo,
                // BondConsts.SPOTCURVE, bond.discYieldCurve);
                consoleCalcStr = outputConsoleYieldCurve(bond.discCurve, BondConsts.SPOTCURVE, calcName,
                        commandLine.isInputJSONYn, calcNo);
                fileCalcStr = outputFileYieldCurve(bond.discCurve, BondConsts.SPOTCURVE, calcNo);
                break;
            case CalcList.FUTUREVALUERISK:
                calcCheck.checkCalc(securityNo, calcNo);
                FutureValueResults futureValueResults = new FutureValueResults();
                futureValueResults = calcMethod.futureValueRisk(calcNo, securityNo, instype, bond.dtFuture,
                        bond.dtRedeem, bond.redeemPrin, bond.redeemInt);
                if (error.hasErrorYn != 'Y') {
                    futureValueList.add(securityNo, new FutureValueResults(futureValueResults));
                }
                consoleCalcStr = calcMethod.outputFutRiskConsole(calcNo, futureValueResults, bond.sfigCurrency);
                fileCalcStr = calcMethod.outputFutRiskFile(calcNo, futureValueResults, bond.sfigCurrency);
                break;
            case CalcList.FUTRETURNRISK:
                FutureValueResults futureReturnRisk = new FutureValueResults();
                calcCheck.checkCalc(securityNo, calcNo);
                if (error.hasErrorYn != 'Y') {
                    futureReturnRisk = calcMethod.futureValueRisk(calcNo, securityNo, instype, bond.dtFuture,
                            bond.dtRedeem, bond.redeemPrin, bond.redeemInt);
                    // futureReturnRisk.calcReturnRisk(bond, futureValueResults);
                    futureReturnRisk.futVal *= 100.;
                    futureReturnRisk.futValDn *= 100.;
                    futureReturnRisk.futValUp *= 100.;
                    futureReturnList.add(securityNo, futureReturnRisk);
                }
                consoleCalcStr = calcMethod.outputFutRiskConsole(calcNo, futureReturnRisk, bond.sfigCurrency);
                fileCalcStr = calcMethod.outputFutRiskFile(calcNo, futureReturnRisk, bond.sfigCurrency);
                break;
            case CalcList.ATTRIBUTION:
                calcCheck.checkCalc(securityNo, calcNo);
                Attribution attribution = new Attribution();
                if (error.hasErrorYn != 'Y') {
                    attribution = calcMethod.calcAttribution(calcNo, securityNo, instype, bond.dtFuture, bond.dtRedeem,
                            bond.redeemPrin, bond.redeemInt);
                    attributionList.add(securityNo, attribution);
                }
                consoleCalcStr = calcMethod.outputAtrributionConsole(calcNo, attribution, bond.sfigStatistic);
                fileCalcStr = calcMethod.outputAtrributionFile(calcNo, attribution, bond.sfigStatistic);
                break;
            case CalcList.SWAPANALYSIS:
                double tFaceAmt = bond.faceAmt;
                bond.faceAmt = 1.;
                calcCheck.checkCalc(securityNo, calcNo);
                TradeValue tradeValue = new TradeValue();
                futureValueResults = calcMethod.futureValueRisk(CalcList.FUTUREVALUERISK, securityNo, instype,
                        bond.dtFuture, bond.dtRedeem, bond.redeemPrin, bond.redeemInt);
                double futValue = futureValueResults.futVal;
                tradeValue.tradeId = securityNo;
                tradeValue.futValue = futValue;
                tradeValue.futValueDnSide = futureValueResults.futValDn;
                tradeValue.futValueUpSide = futureValueResults.futValUp;
                double futAi = calcMethod.accruedInterest(calcNo, instype, bond.dtFuture);
                tradeValue.futAi = futAi;
                double currPrc = bond.settlePrc;
                tradeValue.currPrc = currPrc;
                double futPrc = futValue - futAi;
                tradeValue.futPrc = futPrc;
                double futYield = calcMethod.yieldPrice(CalcList.YIELDTOMATURITY, securityNo, instype, bond.dtFuture,
                        futPrc, bond.dtRedeem, bond.redeemPrin, bond.redeemInt);
                tradeValue.futYield = futYield;

                double currYield = calcMethod.yieldPrice(CalcList.YIELDTOMATURITY, securityNo, instype, bond.dtSettle,
                        currPrc, bond.dtRedeem, bond.redeemPrin, bond.redeemInt);
                tradeValue.currYield = currYield;

                tradeValue.inSwap = false;
                tradeValue.futDuration = calcMethod.modDurationSTD(CalcList.MODIFIEDDURSTD, securityNo, instype,
                        bond.dtFuture, futYield, bond.dtRedeem, bond.redeemInt, bond.redeemPrin);
                tradeValue.currDuration = calcMethod.modDurationSTD(CalcList.MODIFIEDDURSTD, securityNo, instype,
                        bond.dtSettle, currYield, bond.dtRedeem, bond.redeemInt, bond.redeemPrin);
                tradeValue.futConvexity = calcMethod.conVexity(CalcList.CONVEXITY, securityNo, instype, bond.dtFuture,
                        futYield, bond.dtRedeem, bond.redeemInt, bond.redeemPrin);
                tradeValue.currConvexity = calcMethod.conVexity(CalcList.CONVEXITY, securityNo, instype, bond.dtSettle,
                        currYield, bond.dtRedeem, bond.redeemInt, bond.redeemPrin);
                tradeValue.currMatLen = dayCount.matLenYears(calcNo, bond.dtSettle, bond.dtRedeem, bond.dtMaturity);
                tradeValue.futMatLen = dayCount.matLenYears(calcNo, bond.dtFuture, bond.dtRedeem, bond.dtMaturity);
                tradeValue.instype = bond.instype;
                tradeValue.credtRating = bond.creditRatingRf;
                tradeValue.tradeType = bond.tradeType;
                tradeValue.faceAmt = bond.faceAmt;
                tradeValue.secDescr = bond.secDescr;
                tradeValue.currAi = calcMethod.accruedInterest(calcNo, instype, bond.dtSettle);
                double currNetPay = calcMethod.netPayment(CalcList.NETPAYMENT, instype, bond.dtSettle, bond.settlePrc);
                tradeValue.currValue = currNetPay;
                tradeValue.currNetPay = currNetPay;
                tradeValue.futNetPay = calcMethod.netPayment(CalcList.NETPAYMENT, instype, bond.dtFuture, futPrc);
                double sharpStdDev = calcMethod.sharpRatioStdDev(CalcList.SHARPRATIO, securityNo, instype,
                        bond.dtSettle, "RF", Defaults.ANALYSISPERIOD, allYieldCurves);
                tradeValue.sharpStdDev = sharpStdDev;
                int freq = CalUnits.SEMIANNUALFQ;
                double perLen = dayCount.simplePeriodCount(bond.dtSettle, bond.dtFuture, CalUnits.SEMIANNUALREPEAT);
                if (perLen > 0.) {
                    double ratio = tradeValue.futValue / tradeValue.currNetPay;
                    tradeValue.rateReturnExp = 100. * (freq * (-1. + Math.pow(ratio, 1. / perLen)));
                    ratio = tradeValue.futValueDnSide / tradeValue.currNetPay;
                    tradeValue.rateReturnDnSide = 100. * (freq * (-1. + Math.pow(ratio, 1. / perLen)));
                    ratio = tradeValue.futValueUpSide / tradeValue.currNetPay;
                    tradeValue.rateReturnUpSide = 100. * (freq * (-1. + Math.pow(ratio, 1. / perLen)));
                }
                tradeValue.sharpRatio = tradeValue.rateReturnExp / sharpStdDev;
                bond.faceAmt = tFaceAmt;
                tradeValue.faceAmt = tFaceAmt;
                tradeValue.riskReturnValue = setRiskReturnValue(bond.riskReturnGoal, tradeValue);
                int noTrade = tradeValueList.size();
                tradeValueList.add(noTrade, new TradeValue(tradeValue));
                consoleCalcStr = "";
                fileCalcStr = "";
                break;
            case CalcList.BONDPARAMS:
                String strBondParams = getBondParamters();
                consoleCalcStr = commandLine.consoleQuote + calcDef.calcDefs.get(calcNo).calcName
                        + commandLine.consoleQuote + commandLine.consoleParamDelimit + strBondParams;
                fileCalcStr = consoleCalcStr;
                break;
            case CalcList.LASTPAYDATE:
                calcCheck.checkCalc(securityNo, calcNo);
                String strLastPay = calcMethod.lastPayDate(calcNo, securityNo, bond.dtSettle);
                consoleCalcStr = commandLine.consoleQuote + calcDef.calcDefs.get(CalcList.LASTPAYDATE).calcName
                        + commandLine.consoleQuote + commandLine.consoleParamDelimit + commandLine.consoleQuote
                        + strLastPay + commandLine.consoleQuote;
                fileCalcStr = calcDef.calcDefs.get(CalcList.LASTPAYDATE).calcName + commandLine.fileParamDelimit
                        + strLastPay;
                break;
            case CalcList.NEXTPAYDATE:
                calcCheck.checkCalc(securityNo, calcNo);
                String strNextPay = calcMethod.nextPayDate(calcNo, securityNo, bond.dtSettle);
                consoleCalcStr = commandLine.consoleQuote + calcDef.calcDefs.get(CalcList.NEXTPAYDATE).calcName
                        + commandLine.consoleQuote + commandLine.consoleParamDelimit + commandLine.consoleQuote
                        + strNextPay + commandLine.consoleQuote;
                fileCalcStr = calcDef.calcDefs.get(CalcList.NEXTPAYDATE).calcName + commandLine.fileParamDelimit
                        + strNextPay;
                break;
            case CalcList.SHARPRATIO:
                calcCheck.checkCalc(securityNo, calcNo);
                futureReturnRisk = calcMethod.futureValueRisk(CalcList.FUTRETURNRISK, securityNo, instype,
                        bond.dtFuture, bond.dtRedeem, bond.redeemPrin, bond.redeemInt);
                sharpStdDev = calcMethod.sharpRatioStdDev(CalcList.SHARPRATIO, securityNo, instype, bond.dtSettle, "RF",
                        Defaults.ANALYSISPERIOD, allYieldCurves);
                dResult = futureReturnRisk.futVal / sharpStdDev;
                consoleCalcStr = calcMethod.outputDoubleConsole(calcNo, dResult, bond.sfigStatistic);
                fileCalcStr = calcMethod.outputDoubleFile(calcNo, dResult, bond.sfigStatistic);
                break;
            default:
                String errmsg = "Application error - invalid calculation " + calcNo + " was detected";
                error.setError(getClass().getName(), errmsg);
                break;
        }

        cfrSec.consoleStr = consoleCalcStr;
        cfrSec.fileStr = fileCalcStr;
        return cfrSec;
    }

    public int writeChoices(String rootDir) {
        int count;
        count = choices.noChoices;
        String fileName = rootDir + "doc\\choices.csv";
        try {
            Formatter formatter = new Formatter(fileName);
            writeChoiceLines(formatter);
            formatter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return count;
    }

    public void writeChoiceLines(Formatter formatter) {
        int ip = -1;
        String header = "Parameter,Domain,Choice,Definition";
        formatter.format("%s\n", header);
        while (++ip < choices.choiceList.size()) {
            formatter.format("%s,%s,%s,%s\n", choices.choiceList.get(ip).paramName, choices.choiceList.get(ip).domain,
                    choices.choiceList.get(ip).choice, choices.choiceList.get(ip).define);
        }
        formatter.close();
    }

    public int writeParamters(String rootDir) {
        int count = stringToBond.paramMapHashMap.size();
        String fileName = rootDir + "doc\\paramDefinitions.csv";
        String paramList = "";
        try {
            Formatter formatter = new Formatter(fileName);
            count = writeParameterLines(formatter, stringToBond);
            formatter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return count;
    }

    public String getBondParamters() {
        int count = stringToBond.paramMapHashMap.size();
        String strBondParams;
        int no = 0;
        StringToBond.ParamMap paramMap;
        List<String> keys = new ArrayList<String>(stringToBond.paramMapHashMap.keySet());
        keys.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.compareTo(o2) > 0)
                    return 1;
                else
                    return -1;
            }
        });
        paramMap = stringToBond.paramMapHashMap.get(keys.get(0));
        strBondParams = "\"" + paramMap.paramName + "\"";
        while (++no < count) {
            paramMap = stringToBond.paramMapHashMap.get(keys.get(no));
            if (0 == count % 5)
                strBondParams += "," + "\"" + paramMap.paramName + "\"" + "\n";
            else
                strBondParams += "," + "\"" + paramMap.paramName + "\"";
        }
        consoleCalcStr = strBondParams;
        fileCalcStr = strBondParams;
        return consoleCalcStr;
    }

    public int writeParameterLines(Formatter formatter, StringToBond stringToBond) {
        int ip = -1;
        String header = "Name,Domain,Choices,Definition,Array,Min Value, Max Value";
        formatter.format("%s\n", header);
        int noParams = stringToBond.paramMapHashMap.size();
        if (noParams == 0) {
            stringToBond.setParamMaps();
            noParams = stringToBond.paramMapHashMap.size();
        }
        StringToBond.ParamMap paramMap;
        List<String> keys = new ArrayList<String>(stringToBond.paramMapHashMap.keySet());
        keys.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.compareTo(o2) > 0)
                    return 1;
                else
                    return -1;
            }
        });
        while (++ip < noParams) {
            paramMap = stringToBond.paramMapHashMap.get(keys.get(ip));
            // paramMap = stringToBond.paramMapHashMap.get(keys.get(ip));
            formatter.format("%s,%s,%s,%s,%c,%f,%f\n", paramMap.paramName, Domains.TOPDOMAINS[paramMap.paramDomain],
                    paramMap.choiceList, paramMap.definition, paramMap.isArrayYn, paramMap.dMin, paramMap.dMax);

        }
        return noParams;
    }

    public int writeCalculations(String rootDir) {
        int count = 0;

        String fileName = rootDir + "doc\\calcDefinitions.csv";
        try {
            Formatter formatter = new Formatter(fileName);
            writeCalculationLines(formatter);
            formatter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        count = calcDef.calcDefs.size();
        return count;
    }

    public void writeCalculationLines(Formatter formatter) {
        int ip = -1;
        String header = "Calc No,Name,Definition";
        formatter.format("%s\n", header);
        int size = calcDef.calcDefs.size();
        while (++ip < calcDef.calcDefs.size()) {
            formatter.format("%d,%s,%s\n", ip, calcDef.calcDefs.get(ip).calcName, calcDef.calcDefs.get(ip).definition);
        }
        formatter.close();
    }

    private String appendFileResult(String fileResultStr, CommandLine commandLine, String calcReultStr, int calcNo) {
        String totStr = "";
        if (calcDef.calcDefs.get(calcNo).arrayYn.equals("Y")) {
            switch (calcNo) {
                case CalcList.CASHFLOW:
                    break;
                case CalcList.PAYSCHEDULE:
                    break;
                case CalcList.ACCREAMORTSCHED:
                    break;
                default:
                    String errmsg = "invalid array schedule detected when attempting to write schedule to file "
                            + bond.fileOutputName;
                    error.setError(getClass().getName(), errmsg);
                    return totStr;
            }
        } else if (commandLine.isInputJSONYn.equals("Y")) {
            int len = calcReultStr.length();
            calcReultStr = calcReultStr.substring(0, len - 2);
            calcReultStr = calcReultStr + "\n";
            fileResultStr = fileResultStr.concat(calcReultStr);
        } else
            fileResultStr = fileResultStr.concat(calcReultStr + "\n");
        return fileResultStr;
    }

    public String outputCashflowConsoleStd(String calcName, ArrayList<CashflowResults> cashflowResultList) {
        int flowNo = -1;
        String format = null;
        if (UnAvails.isUnAvail(bond.sfigCashflow))
            format = "%,.2f";
        else
            format = "%,." + bond.sfigCashflow + "f";
        String strDtCashflow = "";
        String strFlow = "";
        String strItem = "";
        String strNewFlow = "[";
        String calcTitle = calcName + "=";
        strFlow = calcTitle;
        int noFlows = cashflowResultList.size();
        while (++flowNo < noFlows) {
            Dates dtFlow = cashflowResultList.get(flowNo).dtCashflow;
            // cashflow datr
            strItem = "[" + dtFlow.getDateString(dtFlow, bond.dtFormat) + ",";
            strFlow = strFlow.concat(strItem);
            // day of the week
            strItem = cashflowResultList.get(flowNo).strDayOfWeek + ",";
            strFlow = strFlow.concat(strItem);
            double years = cashflowResultList.get(flowNo).years;
            years = Utility.truncRound(years, 4, 'Y');
            double perLen = cashflowResultList.get(flowNo).perLen;
            perLen = Utility.truncRound(perLen, 3, 'Y');
            // coupon periods to cashflow
            strItem = perLen + ",";
            strFlow = strFlow.concat(strItem);
            // years to cashflow
            strItem = years + ",";
            strFlow = strFlow.concat(strItem);
            // total cashflow
            double dVal = cashflowResultList.get(flowNo).totCashflow;
            strItem = dVal + ",";
            strFlow = strFlow.concat(strItem);
            // total principal casgflow
            dVal = cashflowResultList.get(flowNo).totPrinCash;
            strItem = dVal + ",";
            strFlow = strFlow.concat(strItem);
            // scheduled principal flow
            dVal = cashflowResultList.get(flowNo).schedPrinCash;
            strItem = dVal + ",";
            strFlow = strFlow.concat(strItem);
            // prepay cashfllow
            dVal = cashflowResultList.get(flowNo).prePaycash;
            strItem = dVal + ",";
            strFlow = strFlow.concat(strItem);
            // interest cashflow
            dVal = cashflowResultList.get(flowNo).intCash;
            strItem = dVal + ",";
            strFlow = strFlow.concat(strItem);
            dVal = cashflowResultList.get(flowNo).fedTaxPaid;
            strItem = dVal + ",";
            strFlow = strFlow.concat(strItem);
            dVal = cashflowResultList.get(flowNo).stateTaxPaid;
            strItem = "" + dVal;
            if (flowNo < cashflowResultList.size() - 1)
                strFlow = strFlow.concat(strItem + "],");
            else
                strFlow = strFlow.concat(strItem + "]");

        }
        strFlow = strFlow.trim();
        return strFlow;
    }

    class Cfr {
        String consoleStr;
        String fileStr;
        String cumConsoleStr;
        String cumFileStr;

        Cfr() {
            consoleStr = "";
            fileStr = "";
            cumConsoleStr = "";
            cumFileStr = "";
        }
    }

    private ConsoleFileResults calcCumulativeResults() {
        ConsoleFileResults cfrPort = new ConsoleFileResults();
        if (securityResults.size() == 0) {
            cfrPort.consoleStr = "";
            cfrPort.fileStr = "";
            cfrPort.cumConsoleStr = "";
            cfrPort.cumFileStr = "";
            return cfrPort;
        }
        sortSecurityResults();
        String cumConsoleStatStr = "";
        String cumFileStatStr = "";
        int cCount = -1;
        int noPortCalc = 0;
        int calcNo = -1;
        double cumVal = 0.;
        double val = 0.;
        double totVal = 0.;
        int nextCalc = securityResults.get(0).calcNo;
        int lastCalc = nextCalc;
        String lastTotType = calcDef.calcDefs.get(nextCalc).totalType;
        String nextTotType = lastTotType;
        if (lastTotType.equals("S")) {
            cumVal = securityResults.get(0).dValue;
            totVal = 1.;
        } else {
            cumVal = securityResults.get(0).dValue * securityResults.get(0).mktPrc;
            totVal = securityResults.get(0).mktPrc;
        }
        int calcRequestNo = 0;
        String calcName = "";
        int noCalcEntries = securityResults.size();
        while (++cCount < noCalcEntries) {
            int cCalcNo = securityResults.get(cCount).calcNo;
            if (error.calcsInError.contains(calcNo))
                continue;
            calcName = calcDef.calcDefs.get(cCalcNo).calcName;
            nextCalc = securityResults.get(cCount).calcNo; // what is next calc in line
            nextTotType = calcDef.calcDefs.get(cCalcNo).totalType;
            val = securityResults.get(cCount).dValue;
            if (nextCalc == lastCalc) {
                if (nextTotType == "S") {
                    cumVal += val;
                    totVal = 1.;
                } else {
                    cumVal += val * securityResults.get(cCount).faceAmt;
                    totVal += securityResults.get(cCount).faceAmt;
                }
            } else {
                cumVal = cumVal / totVal;
                String fmt = calcDef.calcDefs.get(lastCalc).format;
                String strCumVal = String.format(fmt, cumVal);
                calcName = calcDef.calcDefs.get(lastCalc).totalName;
                if (calcRequestNo == 0) {
                    cfrPort.consoleStr = commandLine.consoleQuote + calcName + commandLine.consoleQuote
                            + commandLine.consoleParamDelimit + strCumVal;
                    cfrPort.cumConsoleStr += cfrPort.consoleStr;
                    cfrPort.fileStr = calcName + commandLine.fileParamDelimit + strCumVal;
                    cfrPort.cumFileStr += cfrPort.fileStr;
                } else {
                    cfrPort.consoleStr = commandLine.consoleCalcDelimit + commandLine.consoleQuote + calcName
                            + commandLine.consoleQuote + commandLine.consoleParamDelimit + strCumVal;
                    cfrPort.cumConsoleStr += cfrPort.consoleStr;
                    cfrPort.fileStr = commandLine.fileCalcDelimit + calcName + commandLine.fileParamDelimit + strCumVal;
                    cfrPort.cumFileStr += cfrPort.fileStr;
                }
                ++calcRequestNo;
                // now set new cums +
                if (nextTotType == "S") {
                    cumVal = val;
                    totVal = 1.;
                } else {
                    cumVal = val * securityResults.get(cCount).faceAmt;
                    totVal = securityResults.get(cCount).faceAmt;
                }
            }
            lastCalc = nextCalc;
        }
        if (nextTotType == "W") {
            cumVal = cumVal / totVal;
        }
        String fmt = calcDef.calcDefs.get(nextCalc).format;
        String strCumVal = String.format(fmt, cumVal);
        calcName = calcDef.calcDefs.get(nextCalc).totalName;
        cfrPort.consoleStr = commandLine.consoleCalcDelimit + commandLine.consoleQuote + calcName
                + commandLine.consoleQuote + commandLine.consoleParamDelimit + strCumVal;
        cfrPort.cumConsoleStr += cfrPort.consoleStr;
        cfrPort.fileStr = commandLine.fileCalcDelimit + calcName + commandLine.fileParamDelimit + strCumVal;
        // cfrPort.fileStr = calcName + commandLine.fileParamDelimit + strCumVal;
        cfrPort.cumFileStr += cfrPort.fileStr;
        return cfrPort;
    }

    public boolean sortSecurityResults() {
        Collections.sort(securityResults, new Comparator<SecurityResults>() {
            @Override
            public int compare(SecurityResults o1, SecurityResults o2) {
                if (o1.calcNo > o2.calcNo)
                    return 1;
                if (o1.calcNo == o2.calcNo)
                    return 0;
                if (o1.calcNo < o2.calcNo)
                    return -1;
                return 0;
            }
        });
        return true;
    }

    private String setRecapString(String showRecapYn, int securityNo, String nameDelimiter) {
        if (showRecapYn.equals("N"))
            return "";
        commandLine.recapTitle = "================ Entered Parameters ================\n";
        commandLine.recapFooter = "================ End of Input Parameters ================\n";
        commandLine.recapBody = "";
        char lf = '\n';
        String userRequest = commandLine.userRequestList.get(securityNo);
        int sLen = userRequest.length();
        if (userRequest.charAt(sLen - 1) == lf)
            userRequest = userRequest.substring(0, sLen - 1);
        commandLine.recapBody = commandLine.recapBody + userRequest;
        commandLine.recapBody = commandLine.recapBody.replace('=', nameDelimiter.charAt(0));
        commandLine.recapBody = commandLine.recapBody.replace(';', '\n');
        return commandLine.recapTitle + commandLine.recapBody + "\n" + commandLine.recapFooter
                + commandLine.fileCalcDelimit;
    }

    public boolean sortCashflowResults(ArrayList<CashflowResults> cashflowResults) {
        Collections.sort(cashflowResults, new Comparator<CashflowResults>() {
            @Override
            public int compare(CashflowResults s1, CashflowResults s2) {
                if (s1.dtCashflow.geDate(s2.dtCashflow))
                    return 1;
                if (s1.dtCashflow.eqDate(s2.dtCashflow))
                    return 0;
                if (s1.dtCashflow.ltDate(s2.dtCashflow))
                    return -1;
                return 0;
            }
        });
        return true;
    }

    /*
     * public ArrayList<CashflowResults> combineCashflows(ArrayList<CashflowResults>
     * cumCashflowList) {
     * ArrayList<CashflowResults> netCashflowList = new ArrayList<>();
     * CashflowResults incremCashflow = new CashflowResults();
     * Collections.sort(cumCashflowList, new CashflowComparator());
     * int noFlows = cumCashflowList.size();
     * Dates dtLast = new Dates();
     * dtLast.setDate(1900, 1, 1);
     * Dates dtCurr = new Dates();
     * int count = -1; // no row in appended cashflow
     * int cumCount = 0;
     * int netIdx = -1;
     * int icLast = 0;
     * int securityNo = 0;
     * String strDayOfWeek;
     * double perLen = 0.;
     * double years = 0.;
     * double totCash = 0.;
     * double totPrin = 0.;
     * double schedCash = 0.;
     * double prepayCash = 0.;
     * double intCash = 0.;
     * double fedTax = 0.;
     * double stateTax = 0.;
     * Dates dtCashflow = new Dates();
     * strDayOfWeek = cumCashflowList.get(0).strDayOfWeek;
     * securityNo = cumCashflowList.get(0).securityNo;
     * dtLast = cumCashflowList.get(0).dtCashflow;
     * int ic = -1;
     * while (++ic < noFlows) {
     * dtCurr = cumCashflowList.get(ic).dtCashflow;
     * // is this cashflow date not a duplicate of last cashflow date
     * // keep accumulating until we have a new grou[
     * if (dtCurr.eqDate(dtCurr, dtLast)) { // skip adding a duplicate
     * // cumulate casflow electemts
     * icLast = ic;
     * dtCashflow = cumCashflowList.get(ic).dtCashflow;
     * strDayOfWeek = cumCashflowList.get(ic).strDayOfWeek;
     * securityNo = cumCashflowList.get(ic).securityNo;
     * perLen = cumCashflowList.get(ic).perLen;
     * years = cumCashflowList.get(ic).years;
     * totCash += cumCashflowList.get(ic).totCashflow;
     * totPrin += cumCashflowList.get(ic).totPrinCash;
     * schedCash += cumCashflowList.get(ic).schedPrinCash;
     * prepayCash += cumCashflowList.get(ic).prePaycash;
     * intCash += cumCashflowList.get(ic).intCash;
     * fedTax += cumCashflowList.get(ic).fedTaxPaid;
     * stateTax += cumCashflowList.get(ic).stateTaxPaid;
     * dtLast = dtCurr;
     * } else { // alway add what's left of{
     * ++cumCount;
     * perLen = Utility.truncRound(perLen, 3, 'Y');
     * years = Utility.truncRound(years, 3, 'Y');
     * totCash = Utility.truncRound(totCash, 2, 'Y');
     * totPrin = Utility.truncRound(totPrin, 2, 'Y');
     * schedCash = Utility.truncRound(schedCash, 2, 'Y');
     * prepayCash = Utility.truncRound(prepayCash, 2, 'Y');
     * intCash = Utility.truncRound(intCash, 2, 'Y');
     * fedTax = Utility.truncRound(fedTax, 2, 'Y');
     * stateTax = Utility.truncRound(stateTax, 2, 'Y');
     * netCashflowList.add(++netIdx, new CashflowResults(noFlows, cumCount,
     * dtCashflow, strDayOfWeek, perLen, years, totCash, totPrin, schedCash,
     * prepayCash, intCash, fedTax, stateTax));
     * // rest cashflow accumulator
     * dtCashflow = cumCashflowList.get(ic).dtCashflow;
     * strDayOfWeek = cumCashflowList.get(ic).strDayOfWeek;
     * securityNo = cumCashflowList.get(ic).securityNo;
     * perLen = cumCashflowList.get(ic).perLen;
     * years = cumCashflowList.get(ic).years;
     * totCash = cumCashflowList.get(ic).totCashflow;
     * totPrin = cumCashflowList.get(ic).totPrinCash;
     * schedCash = cumCashflowList.get(ic).schedPrinCash;
     * prepayCash = cumCashflowList.get(ic).prePaycash;
     * intCash = cumCashflowList.get(ic).intCash;
     * fedTax = cumCashflowList.get(ic).fedTaxPaid;
     * stateTax = cumCashflowList.get(ic).stateTaxPaid;
     * dtLast = dtCurr;
     * icLast = ic;
     * }
     * 
     * }
     * ++cumCount;
     * securityNo = cumCashflowList.get(icLast).securityNo;
     * perLen = Utility.truncRound(perLen, 3, 'Y');
     * years = Utility.truncRound(years, 3, 'Y');
     * totCash = Utility.truncRound(totCash, 2, 'Y');
     * totPrin = Utility.truncRound(totPrin, 2, 'Y');
     * schedCash = Utility.truncRound(schedCash, 2, 'Y');
     * prepayCash = Utility.truncRound(prepayCash, 2, 'Y');
     * intCash = Utility.truncRound(intCash, 2, 'Y');
     * fedTax = Utility.truncRound(fedTax, 2, 'Y');
     * stateTax = Utility.truncRound(stateTax, 2, 'Y');
     * netCashflowList.add(++netIdx, new CashflowResults(noFlows, cumCount,
     * dtCashflow, strDayOfWeek, perLen, years, totCash, totPrin, schedCash,
     * prepayCash, intCash, fedTax, stateTax));
     * // this initializes new accumulation
     * return netCashflowList;
     * }
     */

    // Collections.sort(securityResultsList, new Comparator<Bond.SecurityResults>()
    // {

    public String outputCashflowJSON(String cashflowName, boolean outputToFile) {
        int flowNo = -1;
        double years = 0.;
        String format, strFlow = "", strItem = "";
        String fileItemStr = "";
        if (error.hasErrorYn == 'Y')
            return error.errorMessage;
        if (UnAvails.isUnAvail(bond.sfigCashflow))
            format = "%.2f";
        // format = calcDefs.get(CASHFLOW).format;
        else {
            format = "%," + bond.sfigCashflow + "f";
        }
        String cflowDelimit = ",";
        String heading = "";
        String openBrackett = ":[";
        String closeBrackett = "]";
        String keyWordSeparator = "";
        if (outputToFile) {
            cflowDelimit = "\n";
            heading = "Cashflow,Date,DayOfWk,Periods,Years,Total Cash,Total Prin,Sched Prin,Prepay Prin,Interest"
                    + "\n";
            strFlow = heading;
            openBrackett = "";
            closeBrackett = "";
            keyWordSeparator = ",";

        }
        while (++flowNo < calcMethod.cashflowResults.count) {
            String keyWord = cashflowName + (flowNo + 1);
            keyWord = "\"" + keyWord + "\"" + openBrackett;
            Dates dtStr = calcMethod.cashflowList.get(flowNo).dtCashflow;
            String strPre = keyWord + keyWordSeparator + "\"" + dtStr.getDateString(dtStr, bond.dtFormat) + "\"" + ",";
            strFlow = strFlow.concat(strPre);
            strItem = "\"" + calcMethod.cashflowList.get(flowNo).strDayOfWeek + "\"" + ",";
            strFlow = strFlow.concat(strItem);
            double perLen = calcMethod.cashflowList.get(flowNo).perLen;
            perLen = Utility.truncRound(perLen, 4, 'Y');
            strItem = String.format("%.4f", perLen) + ",";
            strFlow = strFlow.concat(strItem);
            // round years and period
            years = calcMethod.cashflowList.get(flowNo).years;
            years = Utility.truncRound(years, 4, 'Y');
            strItem = String.format("%.4f", years);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            // total cashflow
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            strItem = decimalFormat.format(bond.faceAmt * calcMethod.cashflowList.get(flowNo).totCashflow);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            // total principal
            strItem = decimalFormat.format(bond.faceAmt * calcMethod.cashflowList.get(flowNo).totPrinCash);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            strItem = decimalFormat.format(bond.faceAmt * calcMethod.cashflowList.get(flowNo).schedPrinCash);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            strItem = decimalFormat.format(bond.faceAmt * calcMethod.cashflowList.get(flowNo).prePaycash);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            strItem = decimalFormat.format(bond.faceAmt * calcMethod.cashflowList.get(flowNo).intCash);
            // interest c.cashflow
            strFlow = strFlow + strItem + closeBrackett;
            fileCalcStr = fileItemStr.concat((strFlow + commandLine.fileCalcDelimit));
            if (flowNo < calcMethod.cashflowResults.count - 1)
                strFlow = strFlow.concat(cflowDelimit);
        }
        return strFlow;
    }

    public String outputCashflowJSON(String calcName, boolean isFileOut, ArrayList<CashflowResults> cashflowList,
            int lastPortCalc) {
        int flowNo = -1;
        double years = 0.;
        String format, strFlow = "", strItem = "";
        String fileItemStr = "";
        if (error.hasErrorYn == 'Y')
            return error.errorMessage;
        if (UnAvails.isUnAvail(bond.sfigCashflow))
            format = "%.2f";
        // format = calcDefs.get(CASHFLOW).format;
        else {
            format = "%," + bond.sfigCashflow + "f";
        }
        String cflowDelimit = ",";
        String heading = "";
        String openBrackett = ":[";
        String closeBrackett = "]";
        String keyWordSeparator = "";
        int noFlows = cashflowList.size();
        if (isFileOut) {
            cflowDelimit = "\n";
            // heading = calcName + '\n' + "Cashflow,Date,DayOfWk,Periods,Years,Total
            // Cash,Total Prin,Sched Prin,Prepay Prin,Interest" + "\n";
            strFlow = heading;
            openBrackett = "";
            closeBrackett = "";
            keyWordSeparator = ",";

        }
        while (++flowNo < noFlows) {
            String keyWord = calcName + (flowNo + 1);
            keyWord = "\"" + keyWord + "\"" + openBrackett;
            Dates dtStr = cashflowList.get(flowNo).dtCashflow;
            String strPre = keyWord + keyWordSeparator + "\"" + dtStr.getDateString(dtStr, bond.dtFormat) + "\"" + ",";
            strFlow = strFlow.concat(strPre);
            strItem = "\"" + cashflowList.get(flowNo).strDayOfWeek + "\"" + ",";
            strFlow = strFlow.concat(strItem);
            double perLen = cashflowList.get(flowNo).perLen;
            perLen = Utility.truncRound(perLen, 4, 'Y');
            strItem = String.format("%.4f", perLen) + ",";
            strFlow = strFlow.concat(strItem);
            // round years and period
            years = cashflowList.get(flowNo).years;
            years = Utility.truncRound(years, 4, 'Y');
            strItem = String.format("%.4f", years);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            // total cashflow
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            strItem = decimalFormat.format(cashflowList.get(flowNo).totCashflow);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            // total principal
            strItem = decimalFormat.format(cashflowList.get(flowNo).totPrinCash);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            strItem = decimalFormat.format(cashflowList.get(flowNo).schedPrinCash);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            strItem = decimalFormat.format(cashflowList.get(flowNo).prePaycash);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            strItem = decimalFormat.format(cashflowList.get(flowNo).intCash);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            strItem = decimalFormat.format(cashflowList.get(flowNo).fedTaxPaid);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            strItem = decimalFormat.format(cashflowList.get(flowNo).stateTaxPaid);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            strFlow = strFlow + strItem + commandLine.consoleRightBracket;
            fileCalcStr = fileItemStr.concat((strFlow + commandLine.fileCalcDelimit));
            if (flowNo < noFlows - 1) {
                if (lastPortCalc == CalcList.CASHFLOW)
                    strFlow = strFlow + commandLine.consoleMultiPostFix;
                else
                    strFlow = strFlow.concat(cflowDelimit);
            }
        }
        return strFlow;
    }

    public String outputCashflowConsoleJSON(String calcName, ArrayList<CashflowResults> cashflowList,
            int lastPortCalc) {
        String outputStr;
        boolean isFileOut = false;
        outputStr = outputCashflowJSON(calcName, isFileOut, cashflowList, lastPortCalc);
        return outputStr;
    }

    public String outputCashflowFileCSV(String cashflowName, ArrayList<CashflowResults> cashflowResultList) {
        int flowNo = -1;
        double years = 0.;
        String format, strFlow = "", strItem = "";
        String fileItemStr = "";
        if (error.hasErrorYn == 'Y')
            return error.errorMessage;
        if (UnAvails.isUnAvail(bond.sfigCashflow))
            format = "%.2f";
        // format = calcDefs.get(CASHFLOW).format;
        else {
            format = "%," + bond.sfigCashflow + "f";
        }
        String heading = "";
        heading = cashflowName
                + "Cashflow,Date,DayOfWk,Periods,Years,Total Cash,Total Prin,Sched Prin,Prepay Prin,Interest" + "\n";
        strFlow = heading;
        while (++flowNo < cashflowResultList.size()) {
            strItem = cashflowResultList.get(flowNo).dtCashflow.getDateString() + ",";
            strFlow = strFlow + strItem;
            strItem = cashflowResultList.get(flowNo).strDayOfWeek + "\"" + ",";
            strFlow = strFlow.concat(strItem);
            double perLen = cashflowResultList.get(flowNo).perLen;
            perLen = Utility.truncRound(perLen, 4, 'Y');
            strItem = String.format("%.4f", perLen) + ",";
            strFlow = strFlow.concat(strItem);
            // round years and period
            years = cashflowResultList.get(flowNo).years;
            years = Utility.truncRound(years, 4, 'Y');
            strItem = String.format("%.4f", years);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            // total cashflow
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            strItem = decimalFormat.format(cashflowResultList.get(flowNo).totCashflow);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            // total principal
            strItem = decimalFormat.format(cashflowResultList.get(flowNo).totPrinCash);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            strItem = decimalFormat.format(cashflowResultList.get(flowNo).schedPrinCash);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            strItem = decimalFormat.format(cashflowResultList.get(flowNo).prePaycash);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            strItem = decimalFormat.format(cashflowResultList.get(flowNo).intCash);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            strItem = decimalFormat.format(cashflowResultList.get(flowNo).fedTaxPaid);
            strItem = strItem + ",";
            strFlow = strFlow.concat(strItem);
            // interest c.cashflow
            strItem = decimalFormat.format(cashflowResultList.get(flowNo).stateTaxPaid);
            strFlow = strFlow + strItem + commandLine.fileCalcDelimit;

        }
        return strFlow;
    }

    public String outputCashflowFileStd(String calcName, ArrayList<CashflowResults> cashflowList) {
        String format = "";
        String footerLine = "";
        String totalsFormat = "";
        String ss = ""; // space separater
        int noCashflows = cashflowList.size();
        String[] strLines = new String[noCashflows + 1];
        if (bond.fileOutYn == 'N') {
            return "";
        }
        format = "%17.2f   ";
        // String headerTitle = calcDef.calcDefs.get(CalcList.CASHFLOW).calcName;
        String headerTitle = "";
        String columnHeading;
        String columnUnderLine;
        String calcSep = "";
        String columnfooting;
        calcSep = "   ";
        headerTitle += "=" + noCashflows;
        columnHeading = "   Date      DayOfWk     Period      Years          Total Cash          Total Prin          Sched Prin         Prepay Prin            Interest       Federal Taxes       State Taxes    ";
        columnUnderLine = "----------   -------   --------   --------   -----------------   -----------------   -----------------   -----------------   -----------------   -----------------   -----------------";
        footerLine = "Totals                                       ";

        int flowNo = -1;
        String strFlow;
        double totCashflow = 0.;
        double totPrinflow = 0.;
        double totSchedflow = 0.;
        double totPrepayflow = 0.;
        double totIntflow = 0.;
        double totFedTaxPaid = 0.;
        double totStateTaxPaid = 0.;
        while (++flowNo < noCashflows) {
            totCashflow += cashflowList.get(flowNo).totCashflow;
            totPrinflow += cashflowList.get(flowNo).totPrinCash;
            totSchedflow += cashflowList.get(flowNo).schedPrinCash;
            totPrepayflow += cashflowList.get(flowNo).prePaycash;
            totIntflow += cashflowList.get(flowNo).intCash;
            totFedTaxPaid += cashflowList.get(flowNo).fedTaxPaid;
            totStateTaxPaid += cashflowList.get(flowNo).stateTaxPaid;
        }
        double total1 = totCashflow;
        double total2 = totPrinflow;
        double total3 = totSchedflow;
        double total4 = totPrepayflow;
        double total5 = totIntflow;
        double total6 = totFedTaxPaid;
        double total7 = totStateTaxPaid;
        strFlow = calcDef.calcDefs.get(CalcList.CASHFLOW).totalName;
        strFlow = strFlow + columnHeading + "\n";
        strFlow = strFlow + columnUnderLine + "\n";
        flowNo = -1;
        while (++flowNo < cashflowList.size()) {
            Dates dtCashflow = cashflowList.get(flowNo).dtCashflow; // 10 chars
            String sDate = dtCashflow.getDateString(dtCashflow, bond.dtFormat);
            strFlow = strFlow.concat(String.format("%10s   ", sDate)); // 10 spaces
            strFlow = strFlow.concat(String.format("%7s   ", cashflowList.get(flowNo).strDayOfWeek));
            strFlow = strFlow.concat(String.format("%8.4f   ", cashflowList.get(flowNo).perLen));
            strFlow = strFlow.concat(String.format("%8.4f   ", cashflowList.get(flowNo).years));
            strFlow = strFlow.concat(String.format(format, cashflowList.get(flowNo).totCashflow));
            strFlow = strFlow.concat(String.format(format, cashflowList.get(flowNo).totPrinCash));
            strFlow = strFlow.concat(String.format(format, cashflowList.get(flowNo).schedPrinCash));
            strFlow = strFlow.concat(String.format(format, cashflowList.get(flowNo).prePaycash));
            strFlow = strFlow.concat(String.format(format, cashflowList.get(flowNo).intCash));
            strFlow = strFlow.concat(String.format(format, cashflowList.get(flowNo).fedTaxPaid));
            strFlow = strFlow.concat(String.format(format, cashflowList.get(flowNo).stateTaxPaid));
            strFlow = strFlow.concat("\n");
        }
        strFlow = strFlow.concat(columnUnderLine);
        strFlow = strFlow.concat("\n" + footerLine);
        strFlow = strFlow.concat(String.format(format, total1));
        strFlow = strFlow.concat(String.format(format, total2));
        strFlow = strFlow.concat(String.format(format, total3));
        strFlow = strFlow.concat(String.format(format, total4));
        strFlow = strFlow.concat(String.format(format, total5));
        strFlow = strFlow.concat(String.format(format, total6));
        strFlow = strFlow.concat(String.format(format, total7));
        return strFlow;
    }

    private String outputConsoleHorizonResults(ArrayList<HorizonResults> secHorzList) {
        // sort horizon results by date
        CommandLine cl = commandLine;
        String hConsoleStr = "", itemStr = "";
        if (error.hasErrorYn == 'Y') {
            hConsoleStr = error.getErrorMessage();
            return hConsoleStr;
        }
        int count = secHorzList.size();
        String calcName = calcDef.calcDefs.get(CalcList.HORIZONANALYSIS).calcName;
        hConsoleStr = cl.consoleQuote + calcName + cl.consoleQuote + cl.consoleParamDelimit + cl.consoleLeftBracket;
        int ih = -1;
        while (++ih < count) {
            itemStr = cl.consoleObjectStartDelimit + cl.consoleQuote + "Date" + commandLine.consoleQuote
                    + commandLine.consoleParamDelimit + commandLine.consoleQuote
                    + secHorzList.get(ih).dtHorizon.getDateString() + cl.consoleQuote + cl.consoleCalcDelimit;
            hConsoleStr += itemStr;

            double dVal = secHorzList.get(ih).horzValue;
            String sVal = String.format("%.2f", dVal);
            itemStr = commandLine.consoleQuote + "HorzValue" + cl.consoleQuote + cl.consoleParamDelimit + sVal
                    + cl.consoleCalcDelimit;
            hConsoleStr += itemStr;

            dVal = secHorzList.get(ih).horzReturn;
            sVal = String.format("%.3f", dVal);
            itemStr = commandLine.consoleQuote + "HorzRtn" + cl.consoleQuote + cl.consoleParamDelimit + sVal
                    + cl.consoleCalcDelimit;
            hConsoleStr += itemStr;

            dVal = secHorzList.get(ih).horzPrc;
            sVal = String.format("%.4f", dVal);
            itemStr = commandLine.consoleQuote + "HorzPrc" + cl.consoleQuote + cl.consoleParamDelimit + sVal
                    + cl.consoleCalcDelimit;
            hConsoleStr += itemStr;

            dVal = secHorzList.get(ih).horzYield;
            sVal = String.format("%.3f", dVal);
            itemStr = commandLine.consoleQuote + "HorzYield" + cl.consoleQuote + cl.consoleParamDelimit + sVal
                    + cl.consoleCalcDelimit;
            hConsoleStr += itemStr;

            dVal = secHorzList.get(ih).horzDur;
            sVal = String.format("%.3f", dVal);
            itemStr = commandLine.consoleQuote + "HorzDuration" + cl.consoleQuote + cl.consoleParamDelimit + sVal
                    + cl.consoleCalcDelimit;
            hConsoleStr += itemStr;

            dVal = secHorzList.get(ih).horzMatLen;
            sVal = String.format("%.2f", dVal);
            itemStr = commandLine.consoleQuote + "HorzMatlen" + cl.consoleQuote + cl.consoleParamDelimit + sVal
                    + cl.consoleCalcDelimit;
            hConsoleStr += itemStr;

            dVal = secHorzList.get(ih).horzGainLoss;
            sVal = String.format("%.2f", dVal);
            itemStr = commandLine.consoleQuote + "HorzGainLoss" + cl.consoleQuote + cl.consoleParamDelimit + sVal
                    + cl.consoleCalcDelimit;
            hConsoleStr += itemStr;

            dVal = secHorzList.get(ih).cashGenRatio;
            sVal = String.format("%.3f", dVal);
            if (ih < count - 1)
                itemStr = cl.consoleQuote + "HorzCashRatio" + cl.consoleQuote + cl.consoleParamDelimit + sVal
                        + cl.consoleObjectEndDelimit + commandLine.consoleCalcDelimit;
            else
                itemStr = cl.consoleQuote + "CashRatio" + cl.consoleQuote + cl.consoleParamDelimit + sVal
                        + cl.consoleObjectEndDelimit + commandLine.consoleRightBracket;
            hConsoleStr += itemStr;
        }
        return hConsoleStr;
    }

    private String outputConsoleHorizonResult(HorizonResults secHorzList) {
        // sort horizon results by date
        CommandLine cl = commandLine;
        String hConsoleStr = "", itemStr = "";
        if (error.hasErrorYn == 'Y') {
            hConsoleStr = error.getErrorMessage();
            error.clearError();
            return hConsoleStr;
        }

        itemStr = cl.consoleObjectStartDelimit + cl.consoleQuote + "Date" + commandLine.consoleQuote
                + commandLine.consoleParamDelimit + commandLine.consoleQuote + secHorzList.dtHorizon.getDateString()
                + cl.consoleQuote + cl.consoleCalcDelimit;
        hConsoleStr += itemStr;

        double dVal = secHorzList.horzValue;
        String sVal = String.format("%.2f", dVal);
        itemStr = commandLine.consoleQuote + "HorzValue" + cl.consoleQuote + cl.consoleParamDelimit + sVal
                + cl.consoleCalcDelimit;
        hConsoleStr += itemStr;

        dVal = secHorzList.horzReturn;
        sVal = String.format("%.3f", dVal);
        itemStr = commandLine.consoleQuote + "HorzRtn" + cl.consoleQuote + cl.consoleParamDelimit + sVal
                + cl.consoleCalcDelimit;
        hConsoleStr += itemStr;

        dVal = secHorzList.horzPrc;
        sVal = String.format("%.4f", dVal);
        itemStr = commandLine.consoleQuote + "HorzPrc" + cl.consoleQuote + cl.consoleParamDelimit + sVal
                + cl.consoleCalcDelimit;
        hConsoleStr += itemStr;

        dVal = secHorzList.horzYield;
        sVal = String.format("%.3f", dVal);
        itemStr = commandLine.consoleQuote + "HorzYield" + cl.consoleQuote + cl.consoleParamDelimit + sVal
                + cl.consoleCalcDelimit;
        hConsoleStr += itemStr;

        dVal = secHorzList.horzDur;
        sVal = String.format("%.3f", dVal);
        itemStr = commandLine.consoleQuote + "HorzDuration" + cl.consoleQuote + cl.consoleParamDelimit + sVal
                + cl.consoleCalcDelimit;
        hConsoleStr += itemStr;

        dVal = secHorzList.horzMatLen;
        sVal = String.format("%.2f", dVal);
        itemStr = commandLine.consoleQuote + "HorzMatlen" + cl.consoleQuote + cl.consoleParamDelimit + sVal
                + cl.consoleCalcDelimit;
        hConsoleStr += itemStr;

        dVal = secHorzList.horzGainLoss;
        sVal = String.format("%.2f", dVal);
        itemStr = commandLine.consoleQuote + "HorzGainLoss" + cl.consoleQuote + cl.consoleParamDelimit + sVal
                + cl.consoleCalcDelimit;
        hConsoleStr += itemStr;

        dVal = secHorzList.cashGenRatio;
        sVal = String.format("%.3f", dVal);
        itemStr = cl.consoleQuote + "HorzCashRatio" + cl.consoleQuote + cl.consoleParamDelimit + sVal
                + cl.consoleObjectEndDelimit;
        hConsoleStr += itemStr;
        return hConsoleStr;
    }

    private String outputFileHorizonResults(ArrayList<HorizonResults> secHorzList) {
        String strRec = "";
        String strHead = "";
        String delim = ",";
        char roundYn = 'Y';
        if (error.hasErrorYn == 'Y') {
            strRec = error.getErrorMessage();
            return strRec;
        }
        int count = secHorzList.size();
        if (count == 0)
            return "";
        String sp = ",";
        if (bond.fileOutFormat.equals("STD"))
            sp = "   ";
        String hd1 = "Horizon Date"; // 12
        String ul1 = "------------";
        String hd3 = "Horizon Value" + sp;
        String ul3 = "-------------" + sp;
        String hd4 = "Horizon Return" + sp;
        String ul4 = "--------------" + sp;
        String hd5 = "Horizon Price" + sp;
        String ul5 = "-------------" + sp;
        String hd6 = "Horizon Yield" + sp;
        String ul6 = "--------------" + sp;
        String hd7 = "Horizon Duration" + sp;
        String ul7 = "-----------------" + sp;
        String hd8 = "Horizon MatLen" + sp;
        String ul8 = "---------------" + sp;
        String hd9 = "Horizon Gain/Loss" + sp;
        String ul9 = "------------------" + sp;
        String hd10 = "Horizon CashRatio" + sp;
        String ul10 = "------------------" + sp;

        // String strNew = "\n" + "New" + commandLine.fileParamDelimit +
        // calcDef.calcDefs.get(CalcList.HORIZONANALYSIS).totalName + "\n";
        strHead = "HorizonResults=" + count + "\n";
        strHead += hd1 + hd3 + hd4 + hd5 + hd6 + hd7 + hd8 + hd9 + hd10 + "\n";
        strHead += ul1 + ul3 + ul4 + ul5 + ul6 + ul7 + ul8 + ul9 + ul10 + "\n";
        strRec = strHead;
        int ih = -1;
        while (++ih < count) {
            strRec += String.format("%12s", secHorzList.get(ih).dtHorizon.getDateString()) + sp;
            String strItem = String.format("%13.2f", secHorzList.get(ih).horzValue) + sp;
            strRec += strItem;
            strItem = String.format("%13.2f", secHorzList.get(ih).horzReturn) + sp;
            strRec += strItem;
            strItem = String.format("%13.4f", secHorzList.get(ih).horzPrc) + sp;
            strRec += strItem;
            strItem = String.format("%13.3f", secHorzList.get(ih).horzYield) + sp;
            strRec += strItem;
            strItem = String.format("%16.3f", secHorzList.get(ih).horzDur) + sp;
            strRec += strItem;
            strItem = String.format("%15.3f", secHorzList.get(ih).horzMatLen) + sp;
            strRec += strItem;
            strItem = String.format("%18.2f", secHorzList.get(ih).horzGainLoss) + sp;
            strRec += strItem;
            strItem = String.format("%18.2f", secHorzList.get(ih).cashGenRatio);
            strRec += strItem + commandLine.fileCalcDelimit;
        }
        return strRec;
    }

    private String outputFileHorizonResult(HorizonResults secHorzList) {
        String strRec = "";
        String strHead = "";
        String delim = ",";
        char roundYn = 'Y';
        if (error.hasErrorYn == 'Y') {
            strRec = error.getErrorMessage();
            return strRec;
        }
        String sp = ",";
        if (bond.fileOutFormat.equals("STD"))
            sp = "   ";

        strRec += String.format("%12s", secHorzList.dtHorizon.getDateString()) + sp;
        String strItem = String.format("%13.2f", secHorzList.horzValue) + sp;
        strRec += strItem;
        strItem = String.format("%13.2f", secHorzList.horzReturn) + sp;
        strRec += strItem;
        strItem = String.format("%13.4f", secHorzList.horzPrc) + sp;
        strRec += strItem;
        strItem = String.format("%13.3f", secHorzList.horzYield) + sp;
        strRec += strItem;
        strItem = String.format("%16.3f", secHorzList.horzDur) + sp;
        strRec += strItem;
        strItem = String.format("%15.3f", secHorzList.horzMatLen) + sp;
        strRec += strItem;
        strItem = String.format("%18.2f", secHorzList.horzGainLoss) + sp;
        strRec += strItem;
        strItem = String.format("%18.2f", secHorzList.cashGenRatio);
        strRec += strItem + commandLine.fileCalcDelimit;

        return strRec;
    }

    public String outputConsoleYieldCurve(YieldCurves yieldCurves, String curveType, String calcName,
            String isInputJSONYn, int calcNo) {
        String calcStr = "";
        if (error.hasErrorYn == 'Y') {
            calcStr = error.getErrorMessage();
            return calcStr;
        }
        CommandLine cmdLine = commandLine;
        calcStr = cmdLine.consoleQuote + calcName + commandLine.consoleQuote + cmdLine.consoleParamDelimit
                + cmdLine.consoleLeftBracket;
        // {"spotCurve":[
        char roundYn = 'Y';
        int matlen = 0;
        int ic = -1;
        int scale = calcDef.calcDefs.get(calcNo).scale;
        double rate = 0.;
        double stdDev = 0.;
        double dnSide = 0.;
        double upSide = 0.;
        String tenorKey = "Tenor";
        String yieldKey = "Yield";
        String stdDevKey = "StdDev";

        ArrayList<YieldPoint> spotCurve;
        spotCurve = yieldCurves.spotCurve;
        if (curveType.equals(BondConsts.FORWARDCURVE)) {
            spotCurve = yieldCurves.forwardCurve;
        }
        String strItem = "";
        String sVal = "";
        int noPoints = spotCurve.size();
        while (++ic < noPoints) {
            int matLen = spotCurve.get(ic).matLen;
            sVal = cmdLine.consoleQuote + yieldCurves.getMatLenName(matLen) + cmdLine.consoleQuote;
            strItem = cmdLine.consoleObjectStartDelimit + cmdLine.consoleQuote + "Tenor" + cmdLine.consoleQuote
                    + cmdLine.consoleParamDelimit + sVal + cmdLine.consoleCalcDelimit;
            calcStr += strItem;

            rate = scale * spotCurve.get(ic).spotRate;
            sVal = String.format("%.3f", rate);
            strItem = cmdLine.consoleQuote + "Rate" + cmdLine.consoleQuote + cmdLine.consoleParamDelimit + sVal
                    + cmdLine.consoleCalcDelimit;
            calcStr += strItem;

            rate = scale * spotCurve.get(ic).spotStdDev;
            sVal = String.format("%.3f", rate);
            strItem = cmdLine.consoleQuote + "StdDev" + cmdLine.consoleQuote + cmdLine.consoleParamDelimit + sVal
                    + cmdLine.consoleCalcDelimit;
            calcStr += strItem;

            rate = scale * spotCurve.get(ic).spotDnSide;
            sVal = String.format("%.3f", rate);
            strItem = cmdLine.consoleQuote + "DnSideRate" + cmdLine.consoleQuote + cmdLine.consoleParamDelimit + sVal
                    + cmdLine.consoleCalcDelimit;
            calcStr += strItem;

            rate = scale * spotCurve.get(ic).spotUpSide;
            sVal = String.format("%.3f", rate);
            if (ic == noPoints - 1)
                strItem = cmdLine.consoleQuote + "UpSideRate" + cmdLine.consoleQuote + cmdLine.consoleParamDelimit
                        + sVal + cmdLine.consoleObjectEndDelimit + cmdLine.consoleRightBracket;
            else
                strItem = cmdLine.consoleQuote + "UpSideRate" + cmdLine.consoleQuote + cmdLine.consoleParamDelimit
                        + sVal + cmdLine.consoleObjectEndDelimit + cmdLine.consoleCalcDelimit;
            calcStr += strItem;
        }
        return calcStr;
    }

    public String outputFileYieldCurve(YieldCurves yieldCurves, String curveType, int calcNo) {
        int ic = -1;
        double yield = 0.;
        int matlen = 0;
        String matName = "";
        String calcStr = "";
        String sGap = "      ";
        String tGap = "   ";
        if (error.hasErrorYn == 'Y') {
            calcStr = error.getErrorMessage();
            error.clearError();
            return calcStr;
        }
        int scale = calcDef.calcDefs.get(calcNo).scale;
        String calcName = calcDef.calcDefs.get(calcNo).calcName;
        char roundYn = 'Y';
        if (bond.fileOutFormat.equals("CSV")) {
            sGap = ",";
            tGap = ",";
        }
        ArrayList<YieldPoint> spotCurve;
        spotCurve = yieldCurves.spotCurve;
        if (curveType.equals(BondConsts.FORWARDCURVE)) {
            spotCurve = yieldCurves.forwardCurve;
        }
        calcStr = calcName + commandLine.fileParamDelimit + yieldCurves.spotCurve.size() + commandLine.fileCalcDelimit;
        calcStr = calcStr + " Rate   " + tGap + " Tenor  " + tGap + " StdDev " + commandLine.fileCalcDelimit;
        calcStr = calcStr + "--------" + tGap + "--------" + tGap + "--------" + commandLine.fileCalcDelimit;
        while (++ic < spotCurve.size() - 1) {
            yield = scale * spotCurve.get(ic).spotRate;
            matlen = spotCurve.get(ic).matLen;
            matName = yieldCurves.getMatLenName(matlen);
            matName = String.format("%8s", matName);
            String strStdDev = String.format("%8.3f", scale * spotCurve.get(ic).spotStdDev);
            String strItem = String.format("%8.3f", yield) + tGap + matName + tGap + strStdDev;
            calcStr = calcStr + strItem + commandLine.fileCalcDelimit;
        }
        yield = scale * yieldCurves.spotCurve.get(ic).spotRate;
        matlen = yieldCurves.spotCurve.get(ic).matLen;
        matName = yieldCurves.getMatLenName(matlen);
        matName = String.format("%8s", matName);
        yield = scale * spotCurve.get(ic).spotRate;
        String strStdDev = String.format("%8.3f", spotCurve.get(ic).spotStdDev);
        String strItem = String.format("%8.3f", yield) + tGap + matName + tGap + strStdDev;
        calcStr = calcStr + strItem;
        return calcStr;
    }

    public String outputCumConsoleKeyRateDuration(int noCalc, int calcNo, String calcName, double[] cumKeyRateDuration,
            int lastPortCalc) {
        String strKr = "";
        double krVal = 0.;
        if (error.hasErrorYn == 'Y')
            strKr = error.getErrorMessage();
        strKr = commandLine.consoleQuote + calcName + commandLine.consoleQuote;
        strKr = strKr + commandLine.consoleParamDelimit + "[";
        if ('Y' == error.hasErrorYn) {
            String errmsg = error.getErrorMessage();
            fileFormatter.format("%s\n", errmsg);
            return strKr;
        }
        char roundYn = 'Y';
        int kr = -1;
        while (++kr < calcMethod.keyRateDuration.noKeyRts - 2) {
            krVal = cumKeyRateDuration[kr] / cumFaceAmt;
            krVal = Utility.truncRound(krVal, bond.sfigStatistic, roundYn);
            strKr = strKr + krVal + ",";
        }
        krVal = cumKeyRateDuration[kr] / cumFaceAmt;
        krVal = Utility.truncRound(krVal, bond.sfigStatistic, roundYn);
        strKr = strKr + krVal + "]";
        if (lastPortCalc == CalcList.KEYRATEDURATION)
            strKr = strKr + commandLine.consoleMultiPostFix;
        return strKr;
    }

    public String outputCumFileKeyRateDuration(int noCalc, int calcNo, String calcName, double[] cumKeyRateDuration) {
        String strKr = "";
        double srVal = 0.;
        int kr = -1;
        strKr = "\n" + "New" + commandLine.fileParamDelimit + calcName + "\n";
        if (bond.fileOutFormat.equals("STD")) {
            strKr = strKr + "Key Rate" + "\n";
            strKr = strKr + "Duration    Term" + "\n";
            strKr = strKr + "---------   ------" + "\n";
        } else {
            strKr = strKr + "Key Rate" + "\n";
            strKr = strKr + "Duration,Term" + "\n";
            strKr = strKr + "---------,------" + "\n";
        }
        char roundYn = 'Y';

        String strVal = "";
        while (++kr < calcMethod.keyRateDuration.noKeyRts - 1) {
            srVal = cumKeyRateDuration[kr] / cumFaceAmt;
            String krName = calcMethod.keyRateDuration.keyRateNames[kr];
            strVal = String.format("%9.4f    ", srVal) + krName;
            strKr = strKr.concat(strVal + "\n");
        }
        return strKr;
    }

    private ConsoleFileResults calcPortfolioTotals() {
        CommandLine cl = new CommandLine();
        ConsoleFileResults cfrCalc = new ConsoleFileResults();
        cl = commandLine;
        ConsoleFileResults cfrPort = new ConsoleFileResults();
        if (error.calcsInError.size() > 0) {
            cfrPort.cumConsoleStr = error.calcsInError.get(0).message;
            cfrPort.cumFileStr = error.calcsInError.get(0).message;
            return cfrPort;
        }
        if (attributionList.size() > 0)
            cfrPort = calcPortAttribution(attributionList);
        if (futureValueList.size() > 0) {
            cfrCalc = calcPortFutureVal(futureValueList);
            if (cfrPort.cumConsoleStr.isEmpty()) {
                cfrPort.cumConsoleStr = cfrCalc.cumConsoleStr;
                cfrPort.cumFileStr = cfrCalc.cumFileStr;
            } else {
                cfrPort.cumConsoleStr += cl.consoleCalcDelimit + cfrCalc.cumConsoleStr;
                cfrPort.cumFileStr += cfrCalc.cumFileStr + cl.fileCalcDelimit;
            }

        }
        // String portStatsConsoleDelimitStr = commandLine.consoleCalcDelimit + "New" +
        // commandLine.consoleParamDelimit + "PortfolioStatistics" +
        // commandLine.consoleCalcDelimit;
        if (securityResults.size() == 0 || error.calcsInError.size() > 0)
            if (cumCashflowList.size() == 0)
                return cfrPort;
        String calcName = "";

        cfrPort = calcCumulativeResults();
        int lastPortCalc = CalcList.CASHFLOW;
        lastPortCalc = UnAvails.NOTAVAILINT;
        if (!error.calcsInError.contains(CalcList.CASHFLOW)) {
            if (calculationList.contains(CalcList.CASHFLOW)) {
                ArrayList<CashflowResults> combinedCashflowList = new ArrayList<CashflowResults>();
                // consoleAllStr = consoleAllStr + commandLine.consoleCalcDelimit;
                combinedCashflowList = calcMethod.combineCashflows(cumCashflowList);
                calcName = calcDef.calcDefs.get(CalcList.CASHFLOW).totalName;
                cfrPort.consoleStr = outputCashflowConsole(calcName, combinedCashflowList);
                cfrPort.fileStr = outputCashflowFile(calcName, combinedCashflowList);
                cfrPort.cumConsoleStr += cl.consoleCalcDelimit + cfrPort.consoleStr;
                cfrPort.cumFileStr += cl.fileCalcDelimit + cfrPort.fileStr;
            }
        }
        if (!error.calcsInError.contains(CalcList.KEYRATEDURATION)) {
            if (calculationList.contains(CalcList.KEYRATEDURATION)) {
                calcName = calcDef.calcDefs.get(CalcList.KEYRATEDURATION).totalName;
                cfrPort.consoleStr = outputCumConsoleKeyRateDuration(noSecurities, CalcList.KEYRATEDURATION, calcName,
                        cumKeyRateDuration, lastPortCalc);
                cfrPort.cumConsoleStr += cl.consoleCalcDelimit + cfrPort.consoleStr;
                cfrPort.fileStr = outputCumFileKeyRateDuration(noSecurities, CalcList.KEYRATEDURATION, calcName,
                        cumKeyRateDuration);
                cfrPort.cumFileStr += cl.fileCalcDelimit + cfrPort.fileStr;
            }
        }
        if (calculationList.contains(CalcList.HORIZONANALYSIS)) {
            if (error.calcsInError.contains(CalcList.HORIZONANALYSIS)) {
                cfrPort.cumConsoleStr = error.calcsInError.get(0).message;
                cfrPort.cumFileStr = error.calcsInError.get(0).message;
                return cfrPort;
            }
            cfrCalc = combineHorizonResults(portHorzList);
            if (!cfrPort.cumConsoleStr.isEmpty()) {
                cfrPort.cumConsoleStr += cl.consoleCalcDelimit + cfrCalc.cumConsoleStr;
                cfrPort.cumFileStr += cl.fileCalcDelimit + cfrCalc.cumFileStr;
            } else {
                cfrPort.cumConsoleStr = cfrCalc.cumConsoleStr;
                cfrPort.cumFileStr = cfrCalc.cumFileStr;
            }

        }

        return cfrPort;
    }

    private ConsoleFileResults combineHorizonResults(ArrayList<HorizonResults> portHorzList) {
        ConsoleFileResults cfrCalc = new ConsoleFileResults();
        CommandLine cl = commandLine;
        if (error.calcsInError.contains(CalcList.HORIZONANALYSIS)) {
            cfrCalc.cumConsoleStr = error.errorMessage;
            cfrCalc.cumFileStr = error.errorMessage;
        }
        SortHorizonResults portHorzListSort = new SortHorizonResults();
        Collections.sort(portHorzList, portHorzListSort);
        int count = portHorzList.size();
        Dates dtNext = new Dates();
        Dates dtLast = portHorzList.get(0).dtHorizon;
        double totMktValue = 0.;
        int no = -1;
        int nd = 0;
        HorizonResults horzResult = new HorizonResults();
        consoleCalcStr = cl.consoleQuote + "PortfolioHorizon" + cl.consoleQuote + cl.consoleParamDelimit
                + commandLine.consoleLeftBracket;
        while (++no < count) {
            dtNext = portHorzList.get(no).dtHorizon;
            if (dtNext.eqDate(dtLast)) {
                horzResult.dtHorizon = dtNext;
                double mktValue = portHorzList.get(no).horzValue;
                totMktValue += mktValue;
                horzResult.horzValue += mktValue;
                horzResult.horzPrc += mktValue * portHorzList.get(no).horzPrc;
                horzResult.horzReturn += mktValue * portHorzList.get(no).horzReturn;
                horzResult.horzDur += mktValue * portHorzList.get(no).horzDur;
                horzResult.horzYield += mktValue * portHorzList.get(no).horzYield;
                horzResult.horzMatLen += mktValue * portHorzList.get(no).horzMatLen;
                horzResult.horzGainLoss += portHorzList.get(no).horzGainLoss;
                horzResult.cashGenRatio += mktValue * portHorzList.get(no).cashGenRatio;
            } else {
                HorizonResults portSummary = new HorizonResults();
                portSummary = scalePortSummary(horzResult, totMktValue);
                consoleCalcStr += outputConsoleHorizonResult(portSummary) + commandLine.consoleCalcDelimit;
                fileCalcStr = outputFileHorizonResult(portSummary);
                cfrCalc.cumConsoleStr += consoleCalcStr;
                cfrCalc.cumFileStr += fileCalcStr;
                totMktValue = 0.;
                horzResult.cashGenRatio = 0.;
                horzResult.horzGainLoss = 0.;
                horzResult.horzMatLen = 0.;
                horzResult.horzYield = 0.;
                horzResult.horzReturn = 0.;
                horzResult.horzPrc = 0.;
                consoleCalcStr = "";
                dtLast = dtNext;
            }
        }
        HorizonResults portSummary = new HorizonResults();
        portSummary = scalePortSummary(portSummary, totMktValue);
        consoleCalcStr = outputConsoleHorizonResult(portSummary);
        fileCalcStr = outputFileHorizonResult(portSummary);
        cfrCalc.cumConsoleStr += consoleCalcStr + commandLine.consoleRightBracket;
        cfrCalc.cumFileStr += fileCalcStr;
        return cfrCalc;
    }

    private HorizonResults scalePortSummary(HorizonResults portHorzList, double mktValue) {
        portHorzList.horzPrc /= mktValue / 100.;
        portHorzList.horzReturn /= mktValue;
        portHorzList.horzDur /= mktValue;
        portHorzList.horzYield /= mktValue / 100.;
        portHorzList.horzMatLen /= mktValue;
        portHorzList.horzGainLoss /= mktValue;
        portHorzList.cashGenRatio /= mktValue;
        return portHorzList;
    }

    public String outputCashflowConsole(String calcName, ArrayList<CashflowResults> combinedCashflowList) {
        int flowNo = -1;
        String strFlow = "";
        if (error.hasErrorYn == 'Y') {
            strFlow = error.getErrorMessage();
            return strFlow;
        }
        String format = null;
        if (UnAvails.isUnAvail(bond.sfigCashflow))
            format = "%,.2f";
        else
            format = "%,." + bond.sfigCashflow + "f";
        String strItem = "";
        strFlow = commandLine.consoleQuote + calcName + commandLine.consoleQuote + commandLine.consoleParamDelimit
                + commandLine.consoleLeftBracket;
        int noFlows = combinedCashflowList.size();
        while (++flowNo < noFlows) {
            strFlow = strFlow + commandLine.consoleObjectStartDelimit;
            Dates dtFlow = combinedCashflowList.get(flowNo).dtCashflow;
            // cashflow datr"
            strItem = commandLine.consoleQuote + "Date" + commandLine.consoleQuote + commandLine.consoleParamDelimit
                    + commandLine.consoleQuote + dtFlow.getDateString(dtFlow, bond.dtFormat) + commandLine.consoleQuote
                    + ",";
            strFlow = strFlow.concat(strItem);
            // day of the week
            strItem = commandLine.consoleQuote + "DayOfWk" + commandLine.consoleQuote + commandLine.consoleParamDelimit
                    + commandLine.consoleQuote + combinedCashflowList.get(flowNo).strDayOfWeek
                    + commandLine.consoleQuote + ",";
            strFlow = strFlow.concat(strItem);
            double perLen = combinedCashflowList.get(flowNo).perLen;
            perLen = Utility.truncRound(perLen, 3, 'Y');
            // coupon periods to cashflow
            strItem = commandLine.consoleQuote + "Periods" + commandLine.consoleQuote + commandLine.consoleParamDelimit
                    + perLen + ",";
            strFlow = strFlow.concat(strItem);
            // years to cashflow
            double years = combinedCashflowList.get(flowNo).years;
            years = Utility.truncRound(years, 3, 'Y');
            strItem = commandLine.consoleQuote + "Years" + commandLine.consoleQuote + commandLine.consoleParamDelimit
                    + years + ",";
            strFlow = strFlow.concat(strItem);
            // total cashflow
            double dVal = combinedCashflowList.get(flowNo).totCashflow;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = commandLine.consoleQuote + "TotalCash" + commandLine.consoleQuote
                    + commandLine.consoleParamDelimit + dVal + ",";
            strFlow = strFlow.concat(strItem);
            // total principal cashflow
            dVal = combinedCashflowList.get(flowNo).totPrinCash;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = commandLine.consoleQuote + "TotalPrin" + commandLine.consoleQuote
                    + commandLine.consoleParamDelimit + dVal + ",";
            strFlow = strFlow.concat(strItem);
            // scheduled principal flow
            dVal = combinedCashflowList.get(flowNo).schedPrinCash;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = commandLine.consoleQuote + "ScheduledPrin" + commandLine.consoleQuote
                    + commandLine.consoleParamDelimit + dVal + ",";
            strFlow = strFlow.concat(strItem);
            // prepay cashfllow
            dVal = combinedCashflowList.get(flowNo).prePaycash;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = commandLine.consoleQuote + "PrePayPrin" + commandLine.consoleQuote
                    + commandLine.consoleParamDelimit + dVal + ",";
            strFlow = strFlow.concat(strItem);
            // interest cashflow
            dVal = combinedCashflowList.get(flowNo).intCash;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = commandLine.consoleQuote + "Interest" + commandLine.consoleQuote + commandLine.consoleParamDelimit
                    + dVal + ",";
            strFlow = strFlow + strItem;
            dVal = combinedCashflowList.get(flowNo).fedTaxPaid;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = strItem = commandLine.consoleQuote + "FedTax" + commandLine.consoleQuote
                    + commandLine.consoleParamDelimit + dVal + ",";
            strFlow = strFlow + strItem;
            dVal = combinedCashflowList.get(flowNo).stateTaxPaid;
            dVal = Utility.truncRound(dVal, 2, 'Y');
            strItem = commandLine.consoleQuote + "StateTax" + commandLine.consoleQuote + commandLine.consoleParamDelimit
                    + dVal;
            strFlow = strFlow + strItem;
            if (flowNo < noFlows - 1)
                strFlow = strFlow + commandLine.consoleObjectEndDelimit + commandLine.consoleCalcDelimit;
            else
                strFlow = strFlow + commandLine.consoleObjectEndDelimit + commandLine.consoleRightBracket;

        }
        strFlow = strFlow.trim();
        return strFlow;

    }

    public String outputCashflowFile(String calcName, ArrayList<CashflowResults> combinedCashflowList) {
        String format = "";
        String footerLine = "";
        String totalsFormat = "";
        String sGap = "   "; // space separater
        int noFlows = combinedCashflowList.size();
        String headerTitle = calcDef.calcDefs.get(CalcList.CASHFLOW).calcName;
        String columnHeading;
        String columnUnderLine;
        String fmtDate = "%10s"; // 10 spaces
        String fmtDow = "%7s";
        String fmtPerLen = "%8.3f";
        String fmtYears = "%8.3f";
        format = "%17.2f";
        if (bond.fileOutFormat.equals("CSV")) {
            sGap = ",";
            format = "%.4f";
            fmtDate = "%s";
            fmtDow = "%s";
            fmtPerLen = "%.3f";
            fmtYears = "%.3f";
            headerTitle += "," + noFlows;
            columnHeading = "Date,DayOfWk,Periods,Years,Total Cash,Total Prin,Sched Prin,Prepay Prin,Interest,Fed Taxes,State Taxes";
            columnUnderLine = "----------,-------,--------,--------,-----------------,-----------------,-----------------,-----------------,-----------------,------------,------------";
            footerLine = "Totals,";
        } else {
            sGap = "   ";
            headerTitle += "=" + noFlows;
            columnHeading = "   Date      DayOfWk     Period      Years          Total Cash          Total Prin          Sched Prin         Prepay Prin            Interest       Federal Taxes        State Taxes   ";
            columnUnderLine = "----------   -------   --------   --------   -----------------   -----------------   -----------------   -----------------   -----------------   -----------------   -----------------";
            footerLine = "Totals                                       ";
        }
        int flowNo = -1;
        String strFlow;
        double totCashflow = 0.;
        double totPrinflow = 0.;
        double totSchedflow = 0.;
        double totPrepayflow = 0.;
        double totIntflow = 0.;
        double totFedTax = 0.;
        double totStateTax = 0.;
        String strItem = "";

        while (++flowNo < noFlows) {
            totCashflow += combinedCashflowList.get(flowNo).totCashflow;
            totPrinflow += combinedCashflowList.get(flowNo).totPrinCash;
            totSchedflow += combinedCashflowList.get(flowNo).schedPrinCash;
            totPrepayflow += combinedCashflowList.get(flowNo).prePaycash;
            totIntflow += combinedCashflowList.get(flowNo).intCash;
            totFedTax += combinedCashflowList.get(flowNo).fedTaxPaid;
            totStateTax += combinedCashflowList.get(flowNo).stateTaxPaid;
        }
        double total1 = totCashflow;
        double total2 = totPrinflow;
        double total3 = totSchedflow;
        double total4 = totPrepayflow;
        double total5 = totIntflow;
        double total6 = totFedTax;
        double total7 = totStateTax;
        strFlow = headerTitle + "\n";
        strFlow += columnHeading + "\n";
        strFlow += columnUnderLine + "\n";

        flowNo = -1;
        while (++flowNo < noFlows) {
            Dates dtCashflow = combinedCashflowList.get(flowNo).dtCashflow; // 10 chars
            String sDate = dtCashflow.getDateString(dtCashflow, bond.dtFormat);
            strItem = sDate + sGap;
            strFlow = strFlow + strItem;
            strItem = String.format(fmtDow, combinedCashflowList.get(flowNo).strDayOfWeek) + sGap;
            strFlow = strFlow + strItem;
            strItem = String.format(fmtPerLen, combinedCashflowList.get(flowNo).perLen) + sGap;
            strFlow = strFlow + strItem;
            strItem = String.format(fmtYears, combinedCashflowList.get(flowNo).years) + sGap;
            strFlow = strFlow + strItem;
            strItem = String.format(format, combinedCashflowList.get(flowNo).totCashflow) + sGap;
            strFlow = strFlow + strItem;
            strItem = String.format(format, combinedCashflowList.get(flowNo).totPrinCash) + sGap;
            strFlow = strFlow + strItem;
            strItem = String.format(format, combinedCashflowList.get(flowNo).schedPrinCash) + sGap;
            strFlow = strFlow + strItem;
            strItem = String.format(format, combinedCashflowList.get(flowNo).prePaycash) + sGap;
            strFlow = strFlow + strItem;
            strItem = String.format(format, combinedCashflowList.get(flowNo).intCash) + sGap;
            strFlow = strFlow + strItem;
            strItem = String.format(format, combinedCashflowList.get(flowNo).fedTaxPaid) + sGap;
            strFlow = strFlow + strItem;
            strItem = String.format(format, combinedCashflowList.get(flowNo).stateTaxPaid) + sGap;
            strFlow = strFlow + strItem + "\n";
        }
        strFlow = strFlow.concat(columnUnderLine);
        strFlow = strFlow.concat("\n" + footerLine);
        strItem = String.format(format, total1) + sGap;
        strFlow = strFlow + strItem;
        strItem = String.format(format, total2) + sGap;
        strFlow = strFlow + strItem;
        strItem = String.format(format, total3) + sGap;
        strFlow = strFlow + strItem;
        strItem = String.format(format, total4) + sGap;
        strFlow = strFlow + strItem;
        strItem = String.format(format, total5) + sGap;
        strFlow = strFlow + strItem;
        strItem = String.format(format, total6) + sGap;
        strFlow = strFlow + strItem;
        strItem = String.format(format, total7) + sGap;
        strFlow = strFlow + strItem;
        // strFlow = strFlow.concat("\n");
        return strFlow;
    }

    public String getVersionConsole() {
        String strVersion = commandLine.consoleQuote + "Version" + commandLine.consoleQuote
                + commandLine.consoleParamDelimit;
        strVersion = strVersion + commandLine.consoleQuote + BondConsts.VERSION + commandLine.consoleQuote;
        return strVersion;
    }

    public String getVersionFile() {
        String strVersion = "Version" + commandLine.fileParamDelimit;
        strVersion = strVersion + BondConsts.VERSION;
        return strVersion;
    }

    class CashflowComparator implements Comparator<CashflowResults> {
        // override the compare() method
        public int compare(CashflowResults c1, CashflowResults c2) {
            if (c1.dtCashflow.gtDate(c1.dtCashflow, c2.dtCashflow))
                return 1;
            else if (c1.dtCashflow.ltDate(c1.dtCashflow, c2.dtCashflow))
                return -1;
            else
                return 0;
        }
    }

    class SecurityResults {
        final int securityNo; // security sequence number
        final int calcNo; // calcullatioon number as in CalcDefs
        final String calcName; //
        // ame of the calculations as in CalcDefs
        final double dValue;// calulatedvalue
        final double mktPrc;
        final double faceAmt;

        SecurityResults() {
            this.securityNo = UnAvails.NOTAVAILINT;
            this.calcNo = UnAvails.NOTAVAILINT;
            this.calcName = UnAvails.NOTAVAILSTRING;
            this.dValue = UnAvails.NOTAVAILDOUBLE;
            this.mktPrc = UnAvails.NOTAVAILDOUBLE;
            this.faceAmt = UnAvails.NOTAVAILDOUBLE;

        }

        SecurityResults(int securityNo, int calcNo, String calcName, double dValue, double mktPrc, double faceAmt) {
            this.securityNo = securityNo;
            this.calcNo = calcNo;
            this.calcName = calcName;
            this.dValue = dValue;
            this.mktPrc = mktPrc;
            this.faceAmt = faceAmt;
        }
    }

    public ArrayList<YieldPoint> loadSpotVolCurve(String fileName, int pastDays) {
        FileReader spotReader;
        ArrayList<YieldPoint> spotCurve = new ArrayList<>();
        YieldPoint yieldPoint = new YieldPoint();
        try {
            spotReader = new FileReader(fileName);
        } catch (Exception ex) {
            String errmsg = "Cannot open discount spot rate file " + fileName;
            error.setPlainError(errmsg);
            return spotCurve;
        }
        BufferedReader bufferedReader = new BufferedReader(spotReader);
        String strLine;
        int noParts = 15;
        int recNo = 0;
        String[] strParts = new String[noParts];
        Dates dtToday = new Dates();
        Dates dtPast = dayCount.time.addDaysToDate(dtToday.getToday(), -pastDays, 'N');
        try {
            strLine = bufferedReader.readLine(); // get the header
        } catch (Exception ex) {
            String errmsg = "Cannot read a line in spot rate file " + fileName;
            error.setError(getClass().getName(), errmsg);
            return spotCurve;
        }
        try {
            while ((strLine = bufferedReader.readLine()) != null) {
                // yieldType
                // CreditAgency,CreditRating,Date,SpotTenor,SpotRate,SpotRateAvg,SpotStdDev,SpotRateDnSide,SpotRateUpSide,SpotRateMin,SpotRateMax
                strParts = strLine.split(",");
                String creditAgency = strParts[1].toUpperCase(); // 2
                String creditRating = strParts[2].toUpperCase();
                String yieldType = strParts[0].toUpperCase(); // 1
                yieldPoint.analysisDays = Defaults.ANALYSISPERIOD;
                strParts = strLine.split(",");
                String strDtAnalysis = strParts[3];
                Dates dtAnalysis = new Dates(strDtAnalysis, "MM/DD/YYYY");
                if (dtAnalysis.ltDate(dtPast))
                    continue;
                String strAnalysisPeriod = strParts[4];
                yieldPoint.yieldType = yieldType;
                yieldPoint.dtAnalysisStart = dtAnalysis;
                yieldPoint.creditAgency = creditAgency;
                yieldPoint.creditRating = creditRating;
                yieldPoint.yieldType = yieldType;
                yieldPoint.dtCurve = dtAnalysis;
                yieldPoint.matLen = Integer.valueOf(strParts[5]); // 5
                yieldPoint.spotRate = .01 * Double.valueOf(strParts[6]);
                yieldPoint.spotAvg = .01 * Double.valueOf(strParts[7]);
                yieldPoint.spotStdDev = .01 * Double.valueOf(strParts[8]);
                yieldPoint.spotDnSide = .01 * Double.valueOf(strParts[9]);
                yieldPoint.spotUpSide = .01 * Double.valueOf(strParts[10]);
                yieldPoint.spotMin = .01 * Double.valueOf(strParts[11]);
                yieldPoint.spotMax = .01 * Double.valueOf(strParts[12]);
                yieldPoint.lookAhead = UnAvails.NOTAVAILINT;
                spotCurve.add(recNo++, new YieldPoint(yieldPoint));
            }

        } catch (Exception ex) {
            String errmsg = "Cannot read a line in spot rate file " + fileName;
            error.setError(getClass().getName(), errmsg);
            return spotCurve;
        }
        SortSpotCurveDesc spotCurveDateSort = new SortSpotCurveDesc();
        Collections.sort(spotCurve, spotCurveDateSort);
        return spotCurve;
    }

    public ArrayList<YieldPoint> loadForwardVolCurve(String fileName, int pastDays) {
        ArrayList<YieldPoint> recentVol = new ArrayList<>();
        YieldCurves yldCurves = new YieldCurves();
        YieldPoint yieldPoint = new YieldPoint();
        FileReader fileReader = null;
        BufferedReader buffReader = null;
        Dates dtToday = new Dates("Today");
        Dates dtPast = dayCount.time.addDaysToDate(dtToday, -pastDays, 'N');
        int vn = 0;
        int pn = 0;
        try {
            fileReader = new FileReader(fileName);
            buffReader = new BufferedReader(fileReader);
            String strLine = null;
            String header = buffReader.readLine();
            while (null != (strLine = buffReader.readLine())) {
                yieldPoint = yldCurves.convertForwardVolRec(strLine);
                // System.out.println("YP(" + pn++ + "):" + strLine );
                if (yieldPoint.dtForward.ltDate(dtPast))
                    continue;
                recentVol.add(vn++, yieldPoint);
            }
            SortSpotCurveDesc spotCurveDateSort = new SortSpotCurveDesc();
            Collections.sort(recentVol, spotCurveDateSort);
            return recentVol;
        } catch (Exception ex) {
            String errmsg = ex.getMessage();
            error.setError(getClass().getName(), errmsg);
            return recentVol;
        }

    }

    public boolean isInCalcList(int calcNo, ArrayList<Integer> calcList) {
        boolean isOk = true;
        if (calcList.contains(calcNo))
            return true;
        return false;
    }

    class SortSpotCurveDesc implements Comparator<YieldPoint> {
        public int compare(YieldPoint yp1, YieldPoint yp2) {
            if (yp1.dtCurve.gtDate(yp2.dtCurve))
                return -1; // move down
            if (yp1.dtCurve.ltDate(yp2.dtCurve))
                return 1;
            if (yp1.dtCurve.eqDate(yp1.dtCurve)) {
                if (yp1.matLen > yp2.matLen)
                    return 1;
                if (yp1.matLen < yp2.matLen)
                    return -1;
                return 0;
            }
            return 0;
        }
    }

    class SortHorizonResults implements Comparator<HorizonResults> {
        public int compare(HorizonResults h1, HorizonResults h2) {
            if (h1.dtHorizon.gtDate(h2.dtHorizon))
                return -1; // move down
            if (h1.dtHorizon.ltDate(h2.dtHorizon))
                return 1;
            return 0;
        }
    }

    private String resetSecRequest(String secRequest) {
        String secRequestReset = "";
        String[] strParts = secRequest.split(";");
        int no = -1;
        String[] keyValue;
        String strKey, strValue;
        while (++no < strParts.length - 1) {
            keyValue = strParts[no].split("=");
            strKey = keyValue[0];
            if (!choices.inPortfolioParamsList(strKey))
                secRequestReset += strParts[no] + ";";
        }
        keyValue = strParts[no].split("=");
        strKey = keyValue[0];
        if (!choices.inPortfolioParamsList(strKey))
            secRequestReset += strParts[no];
        return secRequestReset;
    }

    private String getPortfolioParams(ArrayList<String> allRequestList) {
        String strPortParams = "";
        String[] strParts;
        String[] keyValue;
        String strReq = allRequestList.get(0);
        strParts = strReq.split(";");
        int pn = -1;
        while (++pn < strParts.length) {
            keyValue = strParts[pn].split("=");
            String key = keyValue[0].toUpperCase();
            if (choices.inPortfolioParamsList(key)) {
                if (pn == strParts.length - 1)
                    strPortParams += strParts[pn];
                else
                    strPortParams += strParts[pn] + ";";
            }
        }
        int len = strPortParams.length();
        if (strPortParams.charAt(len - 1) == ';')
            strPortParams = strPortParams.substring(0, len - 1);
        return strPortParams;
    }

    // resets the security request by removing the portfolio params
    private ArrayList<String> resetSecRequestList(ArrayList<String> secRequestList, String portParams) {
        ArrayList<String> resetSecRequestList = new ArrayList<>();
        int nr = -1;
        while (++nr < secRequestList.size()) {
            String strSecReq = secRequestList.get(nr);
            String strResetSecReq = resetSecRequest(strSecReq) + ";" + portParams;
            resetSecRequestList.add(nr, strResetSecReq);
        }
        return resetSecRequestList;
    }

    private ArrayList<Integer> getCalcRequestList(ArrayList<String> allRequestList) {
        ArrayList<Integer> calcRequestList = new ArrayList<>();
        String strReq = allRequestList.get(0);
        String[] strParts = strReq.split(";");
        String[] keyValue;
        int it = -1;
        while (++it < strParts.length) {
            keyValue = strParts[it].split("=");
            if (keyValue[0].toUpperCase().equals("CALCREQUEST")) {
                String strCalc = keyValue[1].replace("[", "");
                strCalc = strCalc.replace("]", "").trim();
                strParts = strCalc.split(",");
                int no = -1;
                while (++no < strParts.length) {
                    try {
                        calcRequestList.add(no, Integer.valueOf(strParts[no]));
                    } catch (Exception ex) {
                        error.setError(getClass().getName(), ex.getMessage());
                        return calcRequestList;
                    }
                }
                return calcRequestList;
            }

        }
        error.setError(getClass().getName(), "No calculations were specified");
        return calcRequestList;
    }

    private ConsoleFileResults calcPortAttribution(ArrayList<Attribution> attributionList) {
        ConsoleFileResults cfr = new ConsoleFileResults();
        Attribution attribution = new Attribution();
        CommandLine cl = commandLine;
        int no = -1;
        double totMkt = 0.;
        while (++no < attributionList.size()) {
            double mktVal = attributionList.get(no).mktValue;
            totMkt += mktVal;
            attribution.rtnSecurity += mktVal * attributionList.get(no).rtnSecurity;
            attribution.rtnCrdSprd += mktVal * attributionList.get(no).rtnCrdSprd;
            attribution.rtnRates += mktVal * attributionList.get(no).rtnRates;
            attribution.rtnTime += mktVal * attributionList.get(no).rtnTime;
            attribution.totRtn += mktVal * attributionList.get(no).totRtn;
        }
        attribution.rtnSecurity /= totMkt;
        attribution.rtnCrdSprd /= totMkt;
        attribution.rtnRates /= totMkt;
        attribution.rtnTime /= totMkt;
        attribution.totRtn /= totMkt;

        String strItem = String.format("%.2f", attribution.totRtn);
        String strConsole = cl.consoleQuote + CalcList.portAttrRtnName + cl.consoleQuote + cl.consoleParamDelimit
                + strItem + cl.consoleCalcDelimit;
        String strFile = CalcList.portAttrRtnName + cl.fileParamDelimit + strItem + cl.fileCalcDelimit;

        strItem = String.format("%.2f", attribution.rtnTime);
        strConsole += cl.consoleQuote + CalcList.portAttrAgingRtnName + cl.consoleQuote + cl.consoleParamDelimit
                + strItem + cl.consoleCalcDelimit;
        strFile += CalcList.portAttrAgingRtnName + cl.fileParamDelimit + strItem + cl.fileCalcDelimit;

        strItem = String.format("%.2f", attribution.rtnRates);
        strConsole += cl.consoleQuote + CalcList.portAttrRiskFreeRtnName + cl.consoleQuote + cl.consoleParamDelimit
                + strItem + cl.consoleCalcDelimit;
        strFile += CalcList.portAttrRiskFreeRtnName + cl.fileParamDelimit + strItem + cl.fileCalcDelimit;

        strItem = String.format("%.2f", attribution.rtnCrdSprd);
        strConsole += cl.consoleQuote + CalcList.portAttrCreditSprdRtnName + cl.consoleQuote + cl.consoleParamDelimit
                + strItem + cl.consoleCalcDelimit;
        strFile += CalcList.portAttrCreditSprdRtnName + cl.fileParamDelimit + strItem + cl.fileCalcDelimit;

        strItem = String.format("%.2f", attribution.rtnSecurity);
        strConsole += cl.consoleQuote + CalcList.portAttrSelectionRtnName + cl.consoleQuote + cl.consoleParamDelimit
                + strItem;
        strFile += CalcList.portAttrSelectionRtnName + cl.consoleParamDelimit + strItem;

        cfr.consoleStr = strConsole;
        cfr.cumConsoleStr = strConsole;
        cfr.fileStr = strFile;
        cfr.cumFileStr = strFile;
        return cfr;

    }

    private ConsoleFileResults calcPortFutureVal(ArrayList<FutureValueResults> futureValueResults) {
        CommandLine cl = commandLine;
        ConsoleFileResults cfr = new ConsoleFileResults();
        int no = -1;
        double totVal = 0., totValDn = 0., totValUp = 0.;
        while (++no < futureValueResults.size()) {
            totVal += futureValueResults.get(no).futVal;
            totValDn += futureValueResults.get(no).futValDn;
            totValUp += futureValueResults.get(no).futValUp;
        }
        String strItem = String.format("%.2f", totVal);
        String strConsole = cl.consoleQuote + CalcList.portFutValName + cl.consoleQuote + cl.consoleParamDelimit
                + strItem + cl.consoleCalcDelimit;
        strItem = String.format("%.2f", totValDn);
        strConsole += cl.consoleQuote + CalcList.portFutValDnName + cl.consoleQuote + cl.consoleParamDelimit + strItem
                + cl.consoleCalcDelimit;
        strItem = String.format("%.2f", totValUp);
        strConsole += cl.consoleQuote + CalcList.portFutValUpName + cl.consoleQuote + cl.consoleParamDelimit + strItem;
        cfr.cumConsoleStr = strConsole;
        cfr.consoleStr = strConsole;

        strItem = String.format("%.2f", totVal);
        String strFile = CalcList.portFutValName + cl.fileParamDelimit + strItem + cl.fileCalcDelimit;
        strItem = String.format("%.2f", totValDn);
        strFile += CalcList.portFutValDnName + cl.fileParamDelimit + strItem + cl.fileCalcDelimit;
        strItem = String.format("%.2f", totValUp);
        strConsole += CalcList.portFutValUpName + cl.consoleParamDelimit + strItem;
        cfr.cumFileStr = strFile;
        cfr.fileStr = strFile;
        return cfr;
    }

    private ConsoleFileResults calcPortFutureRtn(ArrayList<FutureValueResults> futureValueResults) {
        CommandLine cl = commandLine;
        ConsoleFileResults cfr = new ConsoleFileResults();
        int no = -1;
        double totMkt = 0., totVal = 0., totValDn = 0., totValUp = 0.;
        while (++no < futureValueResults.size()) {
            double mktVal = futureValueResults.get(no).mktValue;
            totMkt += mktVal;
            totVal += futureValueResults.get(no).futVal * mktVal;
            totValDn += futureValueResults.get(no).futValDn * mktVal;
            totValUp += futureValueResults.get(no).futValUp * mktVal;
        }
        String strItem = String.format("%.2f", totVal);
        String strConsole = cl.consoleQuote + CalcList.portFutRtnDnName + cl.consoleQuote + cl.consoleParamDelimit
                + strItem + cl.consoleCalcDelimit;
        strItem = String.format("%.2f", totValDn);
        strConsole += cl.consoleQuote + CalcList.portFutRtnDnName + cl.consoleQuote + cl.consoleParamDelimit + strItem
                + cl.consoleCalcDelimit;
        strItem = String.format("%.2f", totValUp);
        strConsole += cl.consoleQuote + CalcList.portFutRtnUpName + cl.consoleQuote + cl.consoleParamDelimit + strItem;
        cfr.cumConsoleStr = strConsole;
        cfr.consoleStr = strConsole;

        strItem = String.format("%.2f", totVal);
        String strFile = CalcList.portFutRtnName + cl.fileParamDelimit + strItem + cl.fileCalcDelimit;
        strItem = String.format("%.2f", totValDn);
        strFile += CalcList.portFutRtnDnName + cl.fileParamDelimit + strItem + cl.fileCalcDelimit;
        strItem = String.format("%.2f", totValUp);
        strFile += CalcList.portFutRtnUpName + cl.fileParamDelimit + strItem + cl.fileCalcDelimit;
        cfr.cumFileStr = strFile;
        cfr.fileStr = strFile;
        return cfr;
    }

    private ArrayList<String> addSecurityNo(ArrayList<String> secRequestList, int securityNo) {
        ArrayList<String> newSecRequestList = new ArrayList<>();
        CommandLine cl = commandLine;
        int no = -1;
        while (++no < secRequestList.size()) {
            String securityId = cl.consoleQuote + "SecurityId" + cl.consoleQuote + cl.consoleParamDelimit + no;
            String strRequest = secRequestList.get(no) + securityId + cl.consoleCalcDelimit;
            newSecRequestList.add(no, strRequest);
        }
        return newSecRequestList;
    }

    // ==========================================
    private void initBasisPoint(String[] args) {
        // getTheRequest
        error = new Error();
        error.clearError();
        commandLine = new CommandLine(error);

        requestAll = commandLine.setCommandLine(args);
        error.isInputJSONYn = commandLine.isInputJSONYn;
        if (error.hasErrorYn == 'Y') {
            return;
        }
        setConfiguration(commandLine);
        if (error.hasErrorYn == 'Y') {
            System.out.println(error.errorMessage);
            System.exit(-1);
        }

        if (commandLine.isFileOutputYn.equals("Y")) {
            try {
                if (UnAvails.isAvail(commandLine.fileOutputName))
                    fileFormatter = new Formatter(commandLine.fileOutputName);
                else
                    fileFormatter = new Formatter(Defaults.STANDARDOUTFILENAME);
            } catch (Exception ex) {
                String errmsg = "Invalid output file name specified " + Defaults.STANDARDOUTFILENAME;
                error.setError(getClass().getName(), errmsg);
                return;
            }
        }
        // initialize the process objects
        error.userOrigRequest = requestAll;
        error.logWriter = logWriter;
        error.dumpWriter = dumpWriter;
        error.isInputJSONYn = commandLine.isInputJSONYn;

        //
        // Instaniate context object
        choices = new Choices(error);
        choices.setChoices();
        cumCashflowCount = 0;
        time = new Time(error);
        dayCount = new DayCount(error);
        utility = new Utility(error, dayCount);
        holidays = new Holidays(error);
        holidays.setHolidays(commandLine.useConfigYn, commandLine.rootDirectory, error);
        securityTypes = new SecurityTypes(commandLine.useConfigYn, commandLine.rootDirectory, error);
        securityTypes.setSecurityTypes(commandLine.useConfigYn, commandLine.rootDirectory, error);
        if (error.hasErrorYn == 'Y') {
            return;
        }
        allYieldCurves = new YieldCurves(commandLine, error, dayCount, utility);
        creditAgencyRating = new CreditAgencyRating(dayCount);
        creditAgencyRating.setAgency();
        creditAgencyRating.setRating();
        tradeEvals = new TradeEvals(error, commandLine, dayCount, utility);
        //
        // initialize the bond and all it's embedded objects
        bond = createBond(commandLine, error, holidays, securityTypes, choices, utility, dayCount, time);
        stringToBond = new StringToBond(bond, commandLine.isInputJSONYn, error, choices, dayCount);
        //
        if (error.hasErrorYn == 'Y') {
            System.out.println(error.errorMessage);
        }
        calcMethod = new CalcMethod(fileFormatter, commandLine, error, calcDef, utility, choices, dayCount);
        calcMethod.setCalculationList(calculationList);
        bondDefault = new BondDefault(commandLine, error, choices, dayCount, utility);
        bondDefault.setAllYieldCurves(allYieldCurves);
        bondCheck = new BondCheck(bond, error, choices);
        calcCheck = new CalcCheck(bond, error, choices, calcMethod);
        fileAllStr = "";
        consoleAllStr = "";

        // get the request list for all securities in standard format
        //
    }

    private String calcPortfolioResults() {
        ConsoleFileResults cfrPort = new ConsoleFileResults();
        cfrPort = calcPortfolioTotals();
        if (calcMethod.isInCalculation(CalcList.SWAPANALYSIS, calculationList)) {
            ConsoleFileResults cfrCalc = new ConsoleFileResults();
            tradeEvals.swapTrades = tradeEvals.calcTradeEval(tradeValueList, bond.dtSettle, bond.dtFuture);
            tradeEvals.getSwapPortfolio(tradeEvals.swapTrades);
            cfrCalc = tradeEvals.outputSwapResultsConsole();
            if (cfrPort.cumConsoleStr.isEmpty() == true)
                cfrPort.cumConsoleStr = commandLine.consoleObjectStartDelimit + cfrCalc.consoleStr;
            else
                cfrPort.cumConsoleStr += commandLine.consoleCalcDelimit + cfrCalc.consoleStr;
            String fileName = commandLine.rootDirectory + "\\data\\" + BondConsts.SWAPTRADEFILENAME;
            tradeEvals.outputTradeEvalSummaryFile(fileName, bond.fileOutFormat, Limits.MAXTOPSWAPS);
            tradeEvals.outputSwapPortfolioFile(fileName, bond.fileOutFormat, Limits.MAXTOPSWAPS);
        }
        if (!cfrPort.cumConsoleStr.isEmpty()) {
            if (bond.showIndivCalcYn == 'Y') {
                if (cfrRun.cumConsoleStr.isEmpty() == false) {
                    cfrRun.cumConsoleStr += commandLine.consoleCalcDelimit + commandLine.consoleObjectStartDelimit
                            + cfrPort.cumConsoleStr + commandLine.consoleMultiPostFix;
                    cfrRun.cumFileStr += commandLine.fileCalcDelimit + cfrPort.cumFileStr;
                } else
                    cfrRun.cumConsoleStr += cfrPort.cumConsoleStr + commandLine.consoleObjectEndDelimit;
            } else {
                cfrRun.cumConsoleStr = cfrPort.cumConsoleStr + commandLine.consoleObjectEndDelimit;
                cfrRun.cumFileStr = cfrPort.cumFileStr + commandLine.fileCalcDelimit;
            }
        }

        if (bond.fileOutYn == 'Y') {
            cfrRun.cumFileStr = cfrRun.cumFileStr.replace("\"", "");
            fileFormatter.format("%s", cfrRun.cumFileStr);
            fileFormatter.flush();
            fileFormatter.close();
        }
        return cfrRun.cumConsoleStr;
    }

    private double setRiskReturnValue(String riskReturnGoal, TradeValue tradeValue) {
        double rislReturnValue = 0.;
        switch (riskReturnGoal) {
            case "MAXSHARPRATIO":
                rislReturnValue = tradeValue.sharpRatio;
                break;
            case "MAXEXPRETURN":
                rislReturnValue = tradeValue.rateReturnExp;
                break;
            case "MAXDNSIDERETURN":
                rislReturnValue = tradeValue.rateReturnDnSide;
                break;
            case "MAXUPSIDERETURN":
                rislReturnValue = tradeValue.rateReturnUpSide;
                break;
            default:
                rislReturnValue = tradeValue.rateReturnExp;
                break;
        }
        return rislReturnValue;
    }

    public YieldCurves loadYieldCurves(ArrayList<Integer> calculationList) {
        String spotFileName = bond.commandLine.rootDirectory + "yieldCurves\\" + BondConsts.SPOTCURVEVOLFILENAME;
        YieldCurves allYieldCurve = new YieldCurves();
        int i = -1;
        while (++i < calculationList.size()) {
            if (choices.inDiscountCurveList(calculationList.get(i))) {
                allYieldCurves.spotCurve = loadSpotVolCurve(spotFileName, Limits.PASTDAYS);
                return allYieldCurves;
            }
        }
        return allYieldCurves;
    }

    public ArrayList<String> getJSONRequests(String request) {
        ArrayList<String> jRequests = new ArrayList<>();
        int len = request.length();
        if (request.charAt(0) == '[') {
            if (request.charAt(len - 1) == ']') {
                // jRequests = getJSONSingleRequests(request);
                return jRequests;
            } else {
                String errmsg = "Invalid JSON request";
                error.setError(getClass().getName(), errmsg);
                return jRequests;
            }
        }
        jRequests.add(request);
        return jRequests;

    }

}

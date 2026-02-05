package app;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class JsonConvert {
    Error error = new Error();
    String fileNameIn;
    String fileNameOut;
    String jString = "{";
    StringToBond strToBond;
    Choices choices;
    DayCount dayCount;

    ArrayList<StringToBond.ParamMap> paramMapList = new ArrayList<StringToBond.ParamMap>();

    JsonConvert() {

    }

    JsonConvert(String fileNameIn, String fileNameOut) {
        this.fileNameIn = fileNameIn;
        this.fileNameOut = fileNameOut;
        choices = new Choices(error);
        dayCount = new DayCount(error);
        strToBond = new StringToBond(error, choices, dayCount);

    }

    // Takes a JSON command line string or JSON string in a file
    // and converts it to BasisPoint standard string to run in calculatiobs

    public void parseStringToJsonFile() {
        File fileIn = new File(fileNameIn);
        File fileOut = new File(fileNameOut);
        Choices choices = new Choices();
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileNameOut);
        } catch (Exception ex) {
            System.out.println("cannot open " + fileOut + " for writing");
            System.exit(-1);
        }
        choices.setChoices();
        Scanner scanner = null;
        try {
            scanner = new Scanner(fileIn);
        } catch (Exception ex) {
            String errmsg = "could not find file " + fileNameIn;
            System.out.println();
            System.exit(-1);
        }
        strToBond = new StringToBond(error, choices, dayCount);
        strToBond.setParamMaps();
        String nextJString = "", paramLastName = "", strLine = "";
        String paramName = "", paramValue = "";
        String[] strParts = new String[2];
        while (scanner.hasNextLine()) {
            strLine = scanner.nextLine();
            strParts = strLine.split("=");
            if (strParts.length != 2) {
                continue;
            }
            paramName = strParts[0].trim().toUpperCase();
            paramValue = strParts[1].toUpperCase();
            try {
                nextJString = parseLIneToJson(paramName, paramValue);
            } catch (Exception ex) {
                String errmsg = "Invalid value of " + paramValue + " for parameter " + paramName;
                System.out.println(errmsg);
                System.exit(-1);
            }
            if (nextJString.equals(""))
                continue;
            jString = jString + nextJString + ",";
        }
        int sLen = jString.length();
        jString = jString.substring(0, sLen - 1);
        jString = jString + "}";
        // String jArrays = appendBondArrays(this.arrayList);
        try {
            fileWriter.write(jString);
        } catch (Exception ex) {
            System.out.println("cannot write out " + jString + " to file " + fileNameOut);
        }
        try {
            fileWriter.close();
        } catch (Exception ex) {
            System.out.println("Cannot close file " + fileNameOut);
        }
        try {
            scanner.close();
        } catch (Exception ex) {
            System.out.println("cannot close file " + fileNameIn);
        }
    }

    public String parseLIneToJson(String paramName, String paramValue) throws Exception {
        // split the line into parameter and value
        String jsLine = "";
        // strToBond.paramMap = paramMapList.
        StringToBond.ParamMap paramMap = strToBond.paramMapHashMap.get(paramName);
        String jParamName = "\"" + paramName + "\"";
        String jParamValue = "";
        String str = "";
        int iVal = UnAvails.NOTAVAILINT;
        double dVal = UnAvails.NOTAVAILDOUBLE;
        String sVal = UnAvails.NOTAVAILSTRING;
        char cVal = UnAvails.NOTAVAILCHAR;
        Dates dtVal = new Dates();
        boolean isOk = false;
        StringTokenizer stok;
        paramName = paramName.toUpperCase();
        if (paramName.equals("NEW")) {
            jsLine = "\"" + paramName + "\"" + ":" + "\"" + paramValue + "\"";
            return jsLine;
        }
        if (paramMap.isArrayYn == 'Y') {
            jParamName = "\"" + paramName + "\"";
            jParamValue = "\"" + paramValue; // mopening quotet
            jsLine = jParamName + ":" + jParamValue;
            return jsLine;
        }
        switch (paramMap.paramDomain) {
            case Domains.DOMAINDOUBLE:
                dVal = Double.valueOf(paramValue);
                isOk = strToBond.validate(paramName, paramMap, iVal, dVal, sVal, cVal, dtVal);
                if (!isOk) {
                    String errmsg = "Invalid value " + paramValue + " for parameter " + paramName;
                    throw new Exception(errmsg);
                }
                break;
            case Domains.DOMAININT:
                iVal = Integer.valueOf(paramValue);
                isOk = strToBond.validate(paramName, paramMap, iVal, dVal, sVal, cVal, dtVal);
                if (!isOk) {
                    String errmsg = "Invalid value " + paramValue + " for parameter " + paramName;
                    throw new Exception(errmsg);
                }
                break;
            case Domains.DOMAINDATE:
                dtVal.setDate(paramValue);
                isOk = strToBond.validate(paramName, paramMap, iVal, dVal, sVal, cVal, dtVal);
                if (!isOk) {
                    String errmsg = "Invalid value " + paramValue + " for parameter " + paramName;
                    throw new Exception(errmsg);
                }
                break;
            default:
                break;
        }

        if (paramName.equals(BondConsts.COMMENT)) {
            jsLine = "\"" + paramName + "\"" + ":" + "\"" + paramValue.trim() + "\"";
            return jsLine;
        }
        jsLine = jParamName + ":" + "\"" + paramValue + "\"";
        return jsLine;
    }

}

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Formatter;


public class Error {

    static final int PARAMERROR = -1;
    static final int FILEREADERROR = -2;
    static final int MISSINGDATA = -3;

    FileWriter logWriter;
    FileWriter dumpWriter;
    String warningMessage;
    ArrayList<CalcInError> calcsInError = new ArrayList<>();
    String errorMessage;
    String userOrigRequest;
    String secDescr;
    char hasErrorYn;
    char hasWarningYn;
    char warningsOff = 'N';
    char dumpOnErrorYn = 'N';
    char logOnError = 'N';
    int dumpCountMax = 1;
    int dumpCount = 0;
    int errorRunCount = 0;
    boolean stopSecCalc = false;
    boolean stopPortTotal = false;
    boolean stopRunCalc = false;
    int warningCount = 0;
    String isInputJSONYn;
    String calcName = UnAvails.NOTAVAILSTRING;
    int calcNo;
    char BSNO = 'N';
    char BSYES = 'Y';
    int BSOK = 0;
    //String appName = "BasisPoint";
    String appName = "";

    Error() {
        warningMessage = UnAvails.NOTAVAILSTRING;
        errorMessage = UnAvails.NOTAVAILSTRING;
        hasErrorYn = BSNO;
        hasWarningYn = BSNO;
        dumpOnErrorYn = 'N';
        logOnError = 'N';
        calcName = UnAvails.NOTAVAILSTRING;
        secDescr = UnAvails.NOTAVAILSTRING;
        logWriter = null;
        dumpWriter = null;
        errorRunCount = 0;
        warningCount = 0;
        stopSecCalc = false;
        stopPortTotal = false;
        stopRunCalc = false;
        calcNo = UnAvails.NOTAVAILINT;
    }

    Error(FileWriter logWriter, FileWriter dumpWriter, String isInputJSONYn) {
        this.logWriter = logWriter;
        this.dumpWriter = dumpWriter;
        warningMessage = UnAvails.NOTAVAILSTRING;
        errorMessage = UnAvails.NOTAVAILSTRING;
        hasErrorYn = BSNO;
        hasWarningYn = BSNO;
        errorRunCount = 0;
        warningCount = 0;
        stopSecCalc = false;
        stopPortTotal = false;
        stopRunCalc = false;
        this.isInputJSONYn = isInputJSONYn;
        String calcName = UnAvails.NOTAVAILSTRING;
        String secDescr = UnAvails.NOTAVAILSTRING;

    }

    public void setCalcError(int calcNo) {
        this.calcNo = calcNo;
    }

    class CalcInError {
        int calcNo;
        String message;

        CalcInError(int calcNo, String message) {
            this.calcNo = calcNo;
            this.message = message;
        }
    }

    public String setOldError(String className, String errorMessage) {
        ++errorRunCount;
        if (errorRunCount >= Limits.MAXERRORCOUNT) {
            hasErrorYn = 'Y';
            stopSecCalc = true;
        }
        String preface = "Error-" + errorRunCount;
        String strBody = "";
        if (secDescr == null || UnAvails.isUnAvail(secDescr))
            secDescr = "?";
        if (isInputJSONYn == null)
            isInputJSONYn = "N";
        if (isInputJSONYn.equals("N")) {
            if (UnAvails.isUnAvail(calcName))
                calcName = className;
            strBody = errorMessage + " in " + calcName + " for " + secDescr + " in input";
            this.errorMessage = preface + "=" + strBody;
        }

        if (isInputJSONYn.equals("Y")) {
            if (UnAvails.isUnAvail(calcName)) {
                calcName = className;
            }
            strBody = preface + " " + errorMessage + " for " + secDescr;
            this.errorMessage = "\"" + calcName + "\"" + ":" + "\"" + strBody + "\"";
        }
        hasErrorYn = 'Y';
        calcsInError.add(calcNo, new CalcInError(calcNo, errorMessage));
        try {
            if (logWriter != null) {
                logWriter.write(errorMessage + "\n");
                logWriter.flush();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
        if (userOrigRequest == null)
            userOrigRequest = "No calculation request specfied";
        userOrigRequest = userOrigRequest.replace(';', '\n');
        if (this.dumpOnErrorYn == 'Y' && dumpWriter != null) {
            try {
                if (this.dumpCount < this.dumpCountMax) {
                    dumpWriter.write(userOrigRequest + "\n");
                    dumpWriter.flush();
                }
                if (dumpCount == 0)
                    dumpWriter.write("NEW" + "\n");
                ++dumpCount;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.exit(-1);
            }
        }
        return errorMessage;
    }

    // Error format: Error:[Calculation:calcName,Message:Error message, Count: no, Security: USTbill, Class:Run]
    public String setError(String className, String progErrorMessage) {
        String quote = "\"";
        String errorOut = "";
        String preface = "Error";
        String strBody = "";
        if (secDescr == null || UnAvails.isUnAvail(secDescr))
            secDescr = "?";
        if (isInputJSONYn == null)
            isInputJSONYn = "N";
        if (UnAvails.isUnAvail(calcName))
            calcName = className;
        hasErrorYn = 'Y';
        try {
            if (logOnError == 'Y' && logWriter != null) {
                logWriter.write(this.errorMessage + "\n");
                logWriter.flush();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
        if (userOrigRequest == null)
            userOrigRequest = "No calculation request specfied";
        userOrigRequest = userOrigRequest.replace(';', '\n');
        if (this.dumpOnErrorYn == 'Y' && dumpWriter != null) {
            try {
                if (this.dumpCount < this.dumpCountMax) {
                    dumpWriter.write(userOrigRequest + "\n");
                    dumpWriter.flush();
                }
                if (dumpCount == 0)
                    dumpWriter.write("NEW" + "\n");
                ++dumpCount;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.exit(-1);
            }
        }
        int noErrors = calcsInError.size();
        calcsInError.add(noErrors, new CalcInError(calcNo, errorMessage));
        if (isInputJSONYn.equals("Y")) {
            errorMessage = quote + "Error" + quote + ":" + quote + progErrorMessage + " for security " + secDescr + " in calculation " + calcName + quote;
        } else {
            errorMessage = "Error=" + progErrorMessage + " for security " + secDescr + " in calculation " + calcName + quote;
        }
        return errorMessage;
    }

    public String setError(String className, int calcNo, String progErrorMessage) {
        String quote = "\"";
        String errorOut = "";
         this.calcNo = calcNo;
        String preface = "Error";
        String strBody = "";
        if (secDescr == null || UnAvails.isUnAvail(secDescr))
            secDescr = "?";
        if (isInputJSONYn == null)
            isInputJSONYn = "N";
        if (UnAvails.isUnAvail(calcName))
            calcName = className;
        hasErrorYn = 'Y';
        try {
            if (logOnError == 'Y' && logWriter != null) {
                logWriter.write(this.errorMessage + "\n");
                logWriter.flush();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
        if (userOrigRequest == null)
            userOrigRequest = "No calculation request specfied";
        userOrigRequest = userOrigRequest.replace(';', '\n');
        if (this.dumpOnErrorYn == 'Y' && dumpWriter != null) {
            try {
                if (this.dumpCount < this.dumpCountMax) {
                    dumpWriter.write(userOrigRequest + "\n");
                    dumpWriter.flush();
                }
                if (dumpCount == 0)
                    dumpWriter.write("NEW" + "\n");
                ++dumpCount;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.exit(-1);
            }
        }
        errorRunCount = calcsInError.size();
        calcsInError.add(errorRunCount++, new CalcInError(calcNo, progErrorMessage));
        if (isInputJSONYn.equals("Y")) {
            errorMessage = quote + "Error" + quote + ":" + quote + progErrorMessage + " for security " + secDescr + " in calculation " + calcName + quote;
        } else {
            errorMessage = "Error=" + progErrorMessage + " for security " + secDescr + " in calculation " + calcName + quote;
        }
        return errorMessage;
    }

    public String setWarning(String className, String progErrorMessage) {
        String quote = "\"";
        String errorOut = "";
        String preface = "Error";
        String strBody = "";
        if (secDescr == null || UnAvails.isUnAvail(secDescr))
            secDescr = "?";
        if (isInputJSONYn == null)
            isInputJSONYn = "N";
        if (UnAvails.isUnAvail(calcName))
            calcName = className;
        hasErrorYn = 'N';
        hasWarningYn = 'Y';
        try {
            if (logWriter != null && logOnError == 'Y') {
                logWriter.write(progErrorMessage + "\n");
                logWriter.flush();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
        if (userOrigRequest == null)
            userOrigRequest = "No calculation request specfied";
        userOrigRequest = userOrigRequest.replace(';', '\n');
        try {
            if (this.dumpOnErrorYn == 'Y' && dumpWriter != null) {
                if (this.dumpCount < this.dumpCountMax) {
                    dumpWriter.write(userOrigRequest + "\n");
                    dumpWriter.flush();
                }
                if (dumpCount == 0)
                    dumpWriter.write("NEW" + "\n");
                ++dumpCount;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
        if (isInputJSONYn.equals("Y")) {
            errorMessage = quote + "Warning" + quote + ":" + quote + progErrorMessage + " for security " + secDescr + " in calculation " + calcName + quote;
        } else {
            errorMessage = "Warning=" + progErrorMessage + " for security " + secDescr + " in calculation " + calcName + quote;
        }
        return errorMessage;
    }

    public void setPlainError(String errorMessage) {
        this.errorMessage = errorMessage;
        this.hasErrorYn = 'Y';
    }

    public String setOldWarning(String className, String errorMessage) {
        ++warningCount;
        if (UnAvails.isUnAvail(secDescr))
            secDescr = "?";
        String trimCalassName = className.trim();
        if (UnAvails.isUnAvail(calcName))
            this.errorMessage = "Warning" + appName + ":" + errorMessage + " in " + className + " for " + secDescr + " in input";
        else
            this.errorMessage = "Warning" + appName + ":" + errorMessage + " in " + className + " for " + secDescr + " in " + calcName;
        hasErrorYn = 'N';
        hasWarningYn = 'Y';
        try {
            if (logWriter != null) {
                logWriter.write(this.errorMessage + "\n");
                logWriter.flush();
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
        return errorMessage;
    }


    public void clearError() {
        this.hasErrorYn = BSNO;
        this.hasWarningYn = BSNO;
        this.errorMessage = "";
        this.warningMessage = "";
        this.calcName = UnAvails.NOTAVAILSTRING;
        int noErr = calcsInError.size();
        if (noErr == 0)
            return;
        this.calcsInError.remove(noErr -1);

    }

    public Error getError() {
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorLogging(FileWriter logWriter, FileWriter dumpWriter) {
        this.logWriter = logWriter;
        this.dumpWriter = dumpWriter;
        this.logOnError = 'Y';
        this.dumpOnErrorYn = 'Y';
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setCalcName(String calcName) {
        this.calcName = calcName;
    }

    public void setDumpString(String userOrigRequest) {
        this.userOrigRequest = userOrigRequest;
    }

    public void setMaxDumpCount(int dumpCountMax) {
        this.dumpCountMax = dumpCountMax;
    }

    public void setDumpOnErrorYn(char dumpOnErrorYn) {
        this.dumpOnErrorYn = dumpOnErrorYn;
    }
}


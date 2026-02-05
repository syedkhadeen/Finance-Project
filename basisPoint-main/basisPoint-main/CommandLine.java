


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class CommandLine {

    // Input config

    String rootDirectory;
    Error error;
    String useConfigYn = UnAvails.NOTAVAILSTRING;
    String isInputJSONYn;
    String isFileInputYn;
    String isFileOutputYn;
    String quotesIncluded = "N";
    int noArgs;
    String cmdStr;
    String fileInputName;
    String fileOutputName;
    // Separaters
    String consoleMultiPreface;
    String consoleMultiPostFix;
    String consoleMultiSecEnd;
    String consoleParamDelimit;
    String consoleLeftBracket;
    String consoleRightBracket;
    String consoleObjectStartDelimit;
    String consoleResultHeader;
    String consoleObjectEndDelimit;
    String consoleQuote;
    String consoleCalcDelimit;
    String consoleSecStartDelimit = "New=Security";
    String consoleSecEndDelimit = "";
    // String consoleStdCalcDelimit = ";";
    String consolePortfolioDelimit;
    String consoleObjectDelimit = ",";
    String consoleElementDelimit = ",";
    String fileCalcDelimit;
    String fileParamDelimit;
    String fileSecDelimit;
    String filePortfolioDelimit;
    String fileMultiPostFix;
    String portfolioName;
    String showRecapYn;
    String fileFormat;
    boolean showPortSummary;
    String recapTitle;
    String recapFooter;
    String recapBody;
    String requestAll;
    String defaultRootDir = "";
    ArrayList<String> userRequestList = new ArrayList<>();


    /**
     * documentation
     * CommendLine checks that the command line has the no of takens, namely 5
     * it also checks that a valid directory has been entered.
     * then is passed along the file input name and file output name
     * then is passed along whether the input string is json or Bp
     * and whether the output is json of Bp
     */
    // JSON console output surrounded by {...}
    // STD console output surrounded ""
    // JSON console each portfolio stat is srruounded by {...}
    // STD console each portfolio stat is surrounded by ""
    // JSON entire console result surrounded by [...]
    // STD entire console result surroounded by ""
    // JSON portfolio delimiter is "New=PortfolioStats"

    CommandLine() {

    }

    CommandLine(Error error) {
        this.error = error;
        this.useConfigYn = UnAvails.NOTAVAILSTRING;
        this.rootDirectory = UnAvails.NOTAVAILSTRING;
        this.isFileInputYn = UnAvails.NOTAVAILSTRING;
        this.isInputJSONYn = UnAvails.NOTAVAILSTRING;
        this.isFileOutputYn = UnAvails.NOTAVAILSTRING;
        this.fileInputName = UnAvails.NOTAVAILSTRING;
        this.fileOutputName = UnAvails.NOTAVAILSTRING;
        this.fileFormat = UnAvails.NOTAVAILSTRING;
        this.portfolioName = "PortfolioName";
        error.isInputJSONYn = UnAvails.NOTAVAILSTRING;

    }


    public String commandLineHelp() {
        String strHelp;
        strHelp = "\nArgument choices:\n";
        strHelp += "0 Arguments: Returns the application version and command line help message to caller";
        strHelp += "1 Argument: <JSON request>\n";
        strHelp += "2 Arguments: <Is JSON String = Y or N> | <Request String>\nOutput to default ouput file\n";
        strHelp += "3 Arguments: <CONVERT> | <BasisPoint Format Request File> | <JSON Format Request File\n";
        strHelp += "3 Arguments: <Is JSON String = Y or N> | <Input file> | <Output file>\n";
        strHelp += "4 Arguments:  <Is JSON String = Y or N> | <Root Directory> | <Quotes Included = Y or N> | <Request Srtring>\n";
        strHelp += "5 Arguments: <Root Directory> | <Is JSON input = Y or N> | <Quotes included = Y or N> | <Input File> | <Output File>\n";
        strHelp += "7 Arguments: <Root Directory>| " +
                "<Is JSON Format = Y or N> | " +
                "<Quoted included = Y or N> | " +
                "<Input File Name> | " +
                "<Ouptut File Name> | " +
                "<Output File Format = STD or CSV>| " +
                "<Print Recap = Y pr N>\n";
        ;
        return strHelp;
    }


    public String setCommandLine(String[] args) {
        String requestAll = "No request was specified";
        int noArgs = args.length;
        String qt = "\"";
        String strHelp = "";
        switch (noArgs) {
            case 0: // version and help message
                noArgs = 0;
                strHelp = qt + "Version" + qt + ":" + qt + BondConsts.VERSION + qt;// +commandLineHelp();
                System.out.println(strHelp);
                System.exit(0);
            case 1: // // JSON and output to file standard.out, SF = non JSON input output to file standrd.out
                if (args[0].equalsIgnoreCase("HELP") || args[0].equals("")) {
                    strHelp = "Version: " + BondConsts.VERSION + commandLineHelp();
                    System.out.println(strHelp);
                    System.exit(0);
                }
                this.noArgs = 1;
                this.useConfigYn = "N";
                this.rootDirectory = defaultRootDir;
                this.isInputJSONYn = "Y";
                this.isFileOutputYn = "N";
                this.isFileInputYn = "N";
                this.quotesIncluded = "N";
                this.fileInputName = UnAvails.NOTAVAILSTRING;
                this.showRecapYn = "Y";
                this.showPortSummary = true;
                requestAll = args[0];
                break;
            case 2: // option to specify JSON or BP standard - no files
                this.noArgs = 2;
                this.useConfigYn = "N";
                this.isInputJSONYn = args[0];
                this.rootDirectory = defaultRootDir;
                this.isFileInputYn = "N";
                this.isFileOutputYn = "Y";
                this.quotesIncluded = "N";
                this.fileInputName = UnAvails.NOTAVAILSTRING;
                this.fileOutputName = Defaults.STANDARDOUTFILENAME;
                this.showRecapYn = "Y";
                this.showPortSummary = true;
                requestAll = args[1];
                break;
            case 3: // convert standard format to JSON from files
                // Or is quotes included and JSON or Standard - no files and request string
                this.noArgs = 3;
                this.useConfigYn = "Y";
                cmdStr = args[0];
                cmdStr = cmdStr.toUpperCase();
                if (cmdStr.equals("CONVERT")) {  // convert input file to JSON and store in ourput file
                    fileInputName = args[1];
                    String fileOutName = args[2];
                    JsonConvert jsonConvert = new JsonConvert(fileInputName, fileOutName);
                    jsonConvert.parseStringToJsonFile();
                    System.out.println("Successfully converted standard string from file " + fileInputName + " to file " + Defaults.STANDARDOUTFILENAME);
                    System.exit(0);
                }
                this.useConfigYn = "N";
                this.rootDirectory = defaultRootDir;
                this.isInputJSONYn = args[0];
                this.quotesIncluded = "Y";
                this.isFileInputYn = "Y";
                this.isFileOutputYn = "Y";
                this.fileInputName = args[1];
                this.fileOutputName = args[2];
                this.showRecapYn = "Y";
                requestAll = readRequest(fileInputName, "Y");
               break;
            case 4: // no config JSON or Stardard input, output file, output format - rquest string
                this.noArgs = 4;
                this.useConfigYn = "Y";
                this.isInputJSONYn = args[0];
                this.rootDirectory = args[1];
                if (args[0].length() > 1) {
                    String errmsg = "An invalid command line was specified for a two argument command line for is this a JSON string \n" + args[0];
                    error.setPlainError(errmsg);
                    return errmsg;
                }
                this.quotesIncluded = args[2];
                this.isFileInputYn = "N";
                this.fileInputName = UnAvails.NOTAVAILSTRING;
                this.showRecapYn = "Y";
                requestAll = args[3];
                break;
            case 5: // use root Dir, JSON or Stanard format input file, output file and output format
                this.noArgs = 5;
                this.useConfigYn = "Y";
                this.rootDirectory = args[1];
                this.isInputJSONYn = args[0];
                if (args[1].length() > 1) {
                    String errmsg = "An invalid command line was specified for a two argument command line for is this a JSON string \n" + args[0];
                    error.setPlainError(errmsg);
                    return errmsg;
                }
                this.quotesIncluded = args[2];
                this.isFileInputYn = "Y";
                this.fileInputName = args[3];
                this.fileOutputName = args[4];
                this.showRecapYn = "Y";
                requestAll = readRequest(this.fileInputName, isInputJSONYn);
                requestAll = requestAll.replaceAll("\"", "");
                break;
            case 7: // options
                this.noArgs = 11;
                this.useConfigYn = "Y";
                this.rootDirectory = args[0];
                this.isInputJSONYn = args[1];
                if (args[2].length() > 1) {
                    String errmsg = "An invalid command line was specified for a two argument command line for is this a JSON string \n" + args[0];
                    error.setPlainError(errmsg);
                    return errmsg;
                }

                this.quotesIncluded = args[2];
                this.isFileInputYn = "Y";
                this.fileInputName = args[3];
                this.isFileOutputYn = "Y";
                this.fileOutputName = args[4];
                this.fileFormat = args[5];
                this.showRecapYn = args[6];
                if (isFileInputYn.equals("Y"))
                    requestAll = readRequest(fileInputName, isInputJSONYn);
                else
                    requestAll = args[9];
                String strShowIndividualCalc = args[10];


                requestAll = requestAll.replaceAll("\"", "");
                break;
            default:
                strHelp = "Invalid number of arguments(" + noArgs + ") were entered";
                strHelp = strHelp + commandLineHelp();
                System.out.println(strHelp);
                System.exit(0);
                break;

        }
        this.requestAll = requestAll;
        return requestAll;
    }

    public String setCommandLineVersions(String[] args) {
        String requestAll = "No request was specified";
        if (args.length == 1) {
            if (args[0].equals("[{")) ;
            this.isInputJSONYn = "Y";
            if (args[0].charAt(0) == '{')
                this.isInputJSONYn = "Y";
            this.noArgs = 1;
            this.useConfigYn = "";
            this.rootDirectory = "c:\\users\\alroi\\Dropbox\\InvestWare\\BasisPoint";
            this.isFileInputYn = "N";
            ;
            this.fileInputName = UnAvails.NOTAVAILSTRING;

            this.showPortSummary = true;
            this.showRecapYn = "N";
            requestAll = args[0];
            return requestAll;
        }
        requestAll = setCommandLine(args);
        return requestAll;
    }


    public void setDelimiters(int noSecurities) {
        if (isInputJSONYn.equals("Y")) {
            if (noSecurities > 1) {
                consoleMultiPreface = "[{";
                consoleMultiPostFix = "}]";
            } else {
                consoleMultiPreface = "";
                consoleMultiPostFix = ";";
            }
            consoleParamDelimit = ":";
            consoleLeftBracket = "[";
            consoleRightBracket = "]";
            consoleObjectStartDelimit = "{";
            consoleObjectEndDelimit = "}";
            consoleQuote = "\"";
            consoleCalcDelimit = ",";
            consoleObjectDelimit = ",";
            consoleElementDelimit = ",";
            consoleResultHeader = "";
            consoleSecStartDelimit = "";
            consoleSecEndDelimit = "";
            consoleSecStartDelimit = "";
            consoleSecEndDelimit = "";
            consolePortfolioDelimit = ",{";
        } else {
            consoleMultiPreface = "";
            consoleMultiPostFix = "";
            consoleParamDelimit = "=";
            consoleLeftBracket = "[";
            consoleRightBracket = "]";
            consoleObjectStartDelimit = "";
            consoleObjectEndDelimit = "";
            consoleSecStartDelimit = "New=Security";
            consoleSecEndDelimit = "";
            consoleResultHeader = "";
            consoleSecEndDelimit = "";
            consoleQuote = "";
            consoleCalcDelimit = ";";
            consolePortfolioDelimit = "New=PortfolioStats";
            consoleObjectDelimit = ",";
            consoleElementDelimit = ",";
        }

        fileParamDelimit = "=";
        fileCalcDelimit = "\n";

    }

    String readRequest(String fileName, String isJsonRequest) {
        int len;
        String strRequest = "";
        String sLine = "";
        char delimit = ',';
        if (isJsonRequest.equals("N"))
            delimit = ';';
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (null != (sLine = bufferedReader.readLine())) {
                len = sLine.length();
                if (len > 0) {
                    if (delimit == sLine.charAt(len - 1))
                        strRequest = strRequest + sLine;
                    else
                        strRequest = strRequest + sLine + delimit;
                } else {
                    len = strRequest.length();
                    if (delimit == strRequest.charAt(len - 1)) {
                        strRequest = strRequest.substring(0, len - 1);
                    }
                    fileReader.close();
                    bufferedReader.close();
                    return strRequest;
                }
            }
            len = strRequest.length();
            if (delimit == strRequest.charAt(len - 1)) {
                strRequest = strRequest.substring(0, len - 1);
            }
            fileReader.close();
            bufferedReader.close();
            return strRequest;
        } catch (Exception ex) {
            System.out.println("Can't open file " + fileName);
            System.exit(-1);
        }
        return strRequest;
    }


}

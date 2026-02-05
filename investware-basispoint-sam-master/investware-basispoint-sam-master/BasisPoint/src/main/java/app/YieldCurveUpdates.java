package app;

import java.lang.reflect.Array;
import java.util.*;
import java.io.*;

public class YieldCurveUpdates {
    String rootDir; // from command line or caller
    Error error = new Error();
    DayCount dayCount = new DayCount(error);
    Utility utility = new Utility(error, dayCount);
    String creditAgency;
    String creditRating;
    CommandLine cmdLine;
    String fileNameUSTreas;
    String fileNamePar; // input file where US Treas raw par curves are stored and source for reating
                        // spot curve
    String fileNameSpot; // source file for accessing par curves used to create spot curves
    String fileNameSpotVol; // output file storing spot curves
    String fileNameForward; // source file to reading spot curves
    String fileNameForwardVol; // output file for storing forward yield curves

    int maxRecords = 100000;
    Dates dtAnalysis;
    int analysisPeriod;
    String dateFormat;
    String yieldType;
    int[] lookAheads = { 1, 3, 6, 12, 24, 36, 60, 84, 120 };// months to project fureard curves
    YieldCurveParams yldParams = new YieldCurveParams();
    YieldCurves yieldCurves = new YieldCurves();
    CreditAgencyRating creditAgencyRating;
    // String fileHeader =
    // "yieldType,creditAgency,creditRating,curveDate,tenor,spotRate,matName,lookAhead"
    // + "\n";
    // yieldType
    // CreditAgency,CreditRating,Date,SpotTenor,SpotRate,SpotRateAvg,SpotStdDev,SpotRateDnSide,SpotRateUpSide,SpotRateMin,SpotRateMax
    ArrayList<MatCurveStructure> fileMatStructure = new ArrayList<>(); // extract current date

    YieldCurveUpdates() {

    }

    public void setCredit(String yieldType, String creditAgency, String creditRating, String dateFormat) {
        this.yieldType = yieldType;
        this.creditAgency = creditAgency;
        this.creditRating = creditRating;
        this.dateFormat = dateFormat;
    }

    public void setAnalysis(Dates dtAnalysis, int analysisPeriod, String dateFormat) {
        this.dtAnalysis = dtAnalysis;
        this.analysisPeriod = analysisPeriod;
        this.dateFormat = dateFormat;
    }

    public void setFileNames(String rootDir) {
        this.rootDir = rootDir;
        this.fileNamePar = this.rootDir + "\\USTreas\\parCurves.csv";
        this.fileNameSpot = this.rootDir + "\\yieldCurves\\spotCurves.csv";
        this.fileNameSpotVol = this.rootDir + "\\yieldCurves\\spotCurveVols.csv";
        this.fileNameForward = this.rootDir + "\\yieldCurves\\forwardCurves.csv";
        this.fileNameForwardVol = this.rootDir + "\\yieldCurves\\forwardCurveVols.csv";
    }

    public void loadTreasCurves(String pathInPar, String yieldType, String creditAgency, String creditRating) {
        loadTreasCurve(pathInPar + "dailyTreas1990.csv", yieldType, creditAgency, creditRating, false);
        loadTreasCurve(pathInPar + "dailyTreas1991.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas1992.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas1993.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas1994.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas1995.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas1996.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas1997.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas1998.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas1999.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2000.csv", yieldType, creditAgency, creditRating, false);
        loadTreasCurve(pathInPar + "dailyTreas2001.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2002.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2003.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2004.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2005.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2006.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2007.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2008.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2009.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2010.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2011.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2012.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2013.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2014.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2015.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2016.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2017.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2018.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2019.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2020.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2021.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2022.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2023.csv", yieldType, creditAgency, creditRating, true);
        loadTreasCurve(pathInPar + "dailyTreas2024.csv", yieldType, creditAgency, creditRating, true);
    }

    public void loadTreasCurve(String treasFileName, String yieldType, String creditAgency, String creditRating,
            boolean append) {
        FileReader fileReader = null;
        String curveHeader = "";
        if (!append)
            curveHeader = "YieldType,CreditAgency,CreditRating,CurveType,CurveDate,ForwardDate,Tenor,Rate,TenorName,LookAhead";
        try {
            fileReader = new FileReader(treasFileName);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(fileReader);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        FileWriter fileWriterPar;
        BufferedWriter buffWriterPar;
        try {
            fileWriterPar = new FileWriter(fileNamePar, append);
            buffWriterPar = new BufferedWriter(fileWriterPar);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }
        FileWriter fileWriterSpot;
        BufferedWriter buffWriterSpot;
        try {
            fileWriterSpot = new FileWriter(fileNameSpot, append);
            buffWriterSpot = new BufferedWriter(fileWriterSpot);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }
        FileWriter fileWriterForward;
        BufferedWriter buffWriterForward;
        try {
            fileWriterForward = new FileWriter(this.fileNameForward, append);
            buffWriterForward = new BufferedWriter(fileWriterForward);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }
        // first line of file is header record
        String strHeader = "";
        try {
            strHeader = bufferedReader.readLine();
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        String benchmark = "NA";
        yldParams.setOptions(BondConsts.LINEARINTERP, BondConsts.SPOTINTERVAl, null);
        yldParams.setTypes(cmdLine, 'Y', 'N', 'Y', 'N');
        yldParams.setCredit(BondConsts.RISKFREECURVE, fileNameSpot, creditAgency, creditRating, benchmark);
        Dates dtAnalysis = new Dates();
        yldParams.setDates(dtAnalysis, dtAnalysis, dtAnalysis);
        String[] strHeaderParts = strHeader.split(",");
        int noHeaderPoints = strHeaderParts.length;
        int noMaturites = noHeaderPoints - 1; // date label ignored
        if (noMaturites < Limits.MINYIELDPOINTS) {
            String errmsg = "Too few maturity points(" + noMaturites + ") found in yield curve file " + treasFileName;
            error.setError(getClass().getName(), errmsg);
            System.out.println(error.getErrorMessage());
        }
        // what maturity lengths are in the file
        // new we can create the baseCurve
        // yieldCurve.setYieldParameters(yieldCurveParameters);
        MatCurveStructure matCurveStructure = new MatCurveStructure();
        matCurveStructure.initMaturityStructure();
        fileMatStructure = getFileMatStructure(strHeader, matCurveStructure.matCurveStructureList);
        if (error.hasErrorYn == 'Y') {
            System.out.println(error.errorMessage);
            return;
        }
        File file = null;
        try {
            file = new File(treasFileName);
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        if (file.length() == 0) {
            try {
                buffWriterPar.write(curveHeader);
                buffWriterSpot.write(curveHeader);
                buffWriterForward.write(curveHeader);
            } catch (Exception ex) {
                System.out.println(ex.getStackTrace());
            }
        }

        int lineNo = 0, recCount = 0;
        String[] strLineParts;
        try {
            String strLine = bufferedReader.readLine();
            while (strLine != null) {
                strLineParts = strLine.split(",");
                if (noHeaderPoints != strLineParts.length) {
                    String errmsg = "Invalid yield curve record - the number of headers(" + noHeaderPoints
                            + ") not equal to number of yield curve points(" + strLineParts.length;
                    error.setError(getClass().getName(), errmsg);
                    System.out.println(errmsg);
                    System.exit(-1);
                }
                Dates dtCurve = new Dates(strLineParts[0], "MM/DD/YYYY");
                Dates dtFuture;
                dtFuture = dtCurve;
                yieldCurves = new YieldCurves(cmdLine, error, dayCount, utility);
                yieldCurves.setYieldParams(yldParams);
                yieldCurves.parCurve = setBaseCurve(dtCurve, strLineParts, fileMatStructure);
                dateFormat = "MM/DD/YYYY";
                recCount = writeYieldPointRecords(buffWriterPar, curveHeader, dateFormat, yieldCurves.parCurve,
                        yieldType, creditAgency, creditRating);
                System.out.println("\nWrote " + recCount + " base curve records written to " + fileNamePar);
                YieldCurveParams yieldCurveParams = new YieldCurveParams();
                yieldCurveParams.setTypes(cmdLine, 'N', 'Y', 'Y', 'N');
                yieldCurves.spotCurve = yieldCurves.convertToSpotCurve(yieldCurves.parCurve, yieldCurveParams);
                int noSpots = yieldCurves.spotCurve.size();
                recCount = 0;
                recCount = writeYieldPointRecords(buffWriterSpot, curveHeader, dateFormat, yieldCurves.spotCurve,
                        yieldType, creditAgency, creditRating);
                System.out.println("\nWrote " + recCount + " spot curve records written to " + fileNameSpot);
                // calc Forward curve
                yieldCurves.d2YSpline = yieldCurves.setCubicSpline(1.0e30, 1.0e30);
                // ca.calculate forward rates curves on future dates as specified by lookAhead
                yieldCurves.forwardCurve = calcLookAheadForwardCurve(yieldCurves, lookAheads);
                recCount = 0;
                recCount = writeYieldPointRecords(buffWriterForward, curveHeader, dateFormat, yieldCurves.forwardCurve,
                        yieldType, creditAgency, creditRating);
                System.out.println("\nWrote " + recCount + " forward curve records written to " + fileNameForward);
                // calc Forward curve
                curveHeader = "";
                strLine = bufferedReader.readLine();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
        try {
            bufferedReader.close();
            buffWriterPar.close();
            buffWriterSpot.close();
            buffWriterForward.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void removeCurve(String strStart, String strEnd, String curveFileName, String yieldType, String creditAgency,
            String creditRating) {
        yieldType = yieldType.toUpperCase();
        creditAgency = creditAgency.toUpperCase();
        creditRating = creditRating.toUpperCase();
        Dates dtStart = new Dates(strStart);
        Dates dtEnd = new Dates(strEnd);
        try {
            // Step 1: Read the file
            BufferedReader reader = new BufferedReader(new FileReader(curveFileName));
            List<String> lines = new ArrayList<>();
            String line;
            String cAgency, cRating, yldType, strDate;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                // Step 2: Filter records
                String[] strParts = line.split(",");
                yldType = strParts[0];
                cAgency = strParts[1];
                cRating = strParts[2];
                strDate = strParts[4];
                int tPeriod;
                Dates dtCurve = new Dates(strDate, "MM/DD/YYYY");
                if (dtCurve.between(dtStart, dtEnd)) {
                    if (cAgency.equals(creditAgency) && cRating.equals(creditRating) && yldType.equals(yieldType))
                        continue;
                    lines.add(line);
                    continue;
                }
            }
            reader.close();
            // Step 3: Write back the filtered records
            BufferedWriter writer = new BufferedWriter(new FileWriter(curveFileName));
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
            writer.close();

            System.out.println("Records removed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    public void updateSpotVolCurve(String strStart, String strEnd, int analysisPeriod, String yieldType,
            String creditAgency, String creditRating, boolean append) {
        Dates dtAnalysis = new Dates();
        String curveHeader = "YieldType,CreditAgency,CreditRating,DateOfAnalysis,AnalysisPeriod,Tenor, SpotRate,AvgSpot,StdDev,RateDnSide,RateUpSide,RateMin,RateMax\n";
        if (append)
            curveHeader = "";
        BufferedWriter curveVolWriter = null;
        try {
            curveVolWriter = new BufferedWriter(new FileWriter(fileNameSpotVol, append));
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        Dates dtStart = new Dates(strStart);
        Dates dtEnd = new Dates(strEnd);
        Dates dtNext = dtStart;
        while (dtNext.leDate(dtEnd)) {
            ArrayList<YieldPoint> spotCurve = calcSpotVariance(dtNext, analysisPeriod, creditAgency, creditRating);
            int noRec = writerSpotCurveVols(curveVolWriter, curveHeader, dtAnalysis, analysisPeriod, spotCurve,
                    yieldType, creditAgency, creditRating);
            System.out.println("Wrote out " + noRec + " spot volatility records to " + this.fileNameSpotVol);

        }
        try {
            curveVolWriter.close();
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        return;
    }

    /*
     * public void updateSpotVolCurves() {
     * updateSpotVolCurve("01/01/2016", 180, "RF", "S&P", "AAA", false);
     * updateSpotVolCurve("01/02/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/03/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/04/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/05/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/06/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/07/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/08/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/09/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/10/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/11/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/12/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/13/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/14/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/15/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/16/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/17/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/18/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/19/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/20/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/21/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/22/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/23/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/24/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/25/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/26/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/27/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/28/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/29/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/30/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/31/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/01/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/02/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/03/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/04/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/05/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/06/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/07/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/08/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/09/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/10/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/11/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/12/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/13/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/14/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/15/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/16/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/17/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/18/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/19/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/20/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/21/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/22/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/23/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/24/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/25/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/26/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/27/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/28/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/29/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/01/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/02/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/03/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/04/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/05/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/06/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/07/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/08/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/09/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/10/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/11/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/12/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/13/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/14/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/15/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/16/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/17/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/18/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/19/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/20/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/21/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/22/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/23/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/24/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/25/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/26/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/27/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/28/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/29/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/30/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/31/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/01/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/02/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/03/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/04/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/05/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/06/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/07/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/08/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/09/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/10/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/11/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/12/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/13/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/14/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/15/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/16/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/17/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/18/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/19/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/20/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/21/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/22/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/23/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/24/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/25/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/26/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/27/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/28/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/29/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/30/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/01/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/02/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/03/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/04/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/05/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/06/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/07/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/08/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/09/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/10/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/11/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/12/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/13/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/14/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/15/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/16/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/17/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/18/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/19/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/20/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/21/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/22/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/23/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/24/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/25/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/26/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/27/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/28/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/29/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/30/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/31/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/01/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/02/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/03/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/04/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/05/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/06/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/07/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/08/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/09/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/10/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/11/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/12/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/13/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/14/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/15/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/16/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/17/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/18/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/19/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/20/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/21/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/22/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/23/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/24/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/25/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/26/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/27/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/28/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/29/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/30/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/01/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/02/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/03/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/04/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/05/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/06/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/07/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/08/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/09/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/10/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/11/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/12/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/13/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/14/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/15/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/16/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/17/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/18/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/19/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/20/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/21/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/22/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/23/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/24/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/25/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/26/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/27/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/28/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/29/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/30/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/31/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/01/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/02/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/03/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/04/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/05/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/06/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/07/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/08/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/09/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/10/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/11/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/12/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/13/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/14/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/15/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/16/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/17/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/18/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/19/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/20/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/21/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/22/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/23/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/24/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/25/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/26/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/27/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/28/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/29/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/30/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/31/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/01/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/02/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/03/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/04/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/05/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/06/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/07/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/08/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/09/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/10/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/11/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/12/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/13/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/14/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/15/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/16/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/17/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/18/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/19/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/20/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/21/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/22/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/23/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/24/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/25/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/26/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/27/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/28/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/29/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/30/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/01/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/02/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/03/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/04/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/05/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/06/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/07/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/08/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/09/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/10/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/11/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/12/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/13/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/14/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/15/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/16/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/17/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/18/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/19/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/20/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/21/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/22/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/23/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/24/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/25/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/26/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/27/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/28/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/29/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/30/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/31/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/01/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/02/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/03/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/04/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/05/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/06/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/07/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/08/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/09/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/10/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/11/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/12/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/13/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/14/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/15/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/16/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/17/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/18/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/19/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/20/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/21/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/22/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/23/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/24/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/25/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/26/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/27/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/28/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/29/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/30/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/01/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/02/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/03/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/04/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/05/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/06/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/07/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/08/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/09/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/10/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/11/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/12/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/13/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/14/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/15/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/16/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/17/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/18/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/19/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/20/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/21/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/22/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/23/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/24/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/25/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/26/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/27/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/28/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/29/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/30/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/31/2016", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/01/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/02/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/03/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/04/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/05/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/06/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/07/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/08/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/09/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/10/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/11/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/12/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/13/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/14/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/15/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/16/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/17/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/18/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/19/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/20/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/21/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/22/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/23/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/24/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/25/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/26/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/27/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/28/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/29/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/30/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/31/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/01/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/02/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/03/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/04/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/05/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/06/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/07/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/08/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/09/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/10/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/11/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/12/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/13/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/14/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/15/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/16/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/17/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/18/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/19/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/20/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/21/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/22/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/23/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/24/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/25/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/26/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/27/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/28/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/01/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/02/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/03/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/04/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/05/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/06/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/07/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/08/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/09/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/10/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/11/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/12/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/13/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/14/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/15/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/16/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/17/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/18/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/19/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/20/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/21/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/22/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/23/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/24/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/25/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/26/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/27/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/28/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/29/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/30/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/31/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/01/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/02/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/03/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/04/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/05/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/06/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/07/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/08/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/09/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/10/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/11/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/12/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/13/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/14/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/15/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/16/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/17/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/18/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/19/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/20/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/21/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/22/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/23/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/24/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/25/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/26/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/27/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/28/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/29/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/30/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/01/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/02/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/03/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/04/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/05/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/06/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/07/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/08/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/09/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/10/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/11/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/12/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/13/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/14/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/15/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/16/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/17/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/18/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/19/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/20/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/21/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/22/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/23/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/24/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/25/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/26/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/27/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/28/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/29/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/30/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/31/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/01/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/02/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/03/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/04/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/05/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/06/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/07/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/08/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/09/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/10/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/11/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/12/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/13/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/14/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/15/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/16/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/17/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/18/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/19/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/20/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/21/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/22/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/23/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/24/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/25/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/26/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/27/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/28/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/29/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/30/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/01/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/02/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/03/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/04/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/05/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/06/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/07/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/08/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/09/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/10/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/11/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/12/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/13/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/14/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/15/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/16/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/17/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/18/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/19/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/20/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/21/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/22/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/23/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/24/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/25/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/26/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/27/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/28/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/29/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/30/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/31/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/01/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/02/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/03/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/04/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/05/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/06/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/07/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/08/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/09/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/10/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/11/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/12/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/13/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/14/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/15/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/16/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/17/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/18/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/19/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/20/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/21/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/22/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/23/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/24/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/25/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/26/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/27/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/28/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/29/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/30/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/31/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/01/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/02/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/03/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/04/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/05/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/06/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/07/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/08/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/09/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/10/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/11/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/12/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/13/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/14/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/15/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/16/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/17/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/18/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/19/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/20/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/21/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/22/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/23/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/24/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/25/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/26/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/27/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/28/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/29/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/30/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/01/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/02/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/03/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/04/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/05/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/06/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/07/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/08/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/09/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/10/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/11/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/12/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/13/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/14/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/15/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/16/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/17/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/18/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/19/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/20/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/21/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/22/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/23/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/24/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/25/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/26/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/27/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/28/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/29/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/30/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/31/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/01/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/02/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/03/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/04/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/05/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/06/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/07/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/08/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/09/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/10/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/11/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/12/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/13/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/14/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/15/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/16/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/17/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/18/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/19/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/20/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/21/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/22/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/23/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/24/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/25/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/26/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/27/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/28/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/29/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/30/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/01/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/02/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/03/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/04/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/05/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/06/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/07/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/08/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/09/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/10/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/11/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/12/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/13/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/14/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/15/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/16/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/17/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/18/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/19/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/20/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/21/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/22/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/23/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/24/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/25/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/26/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/27/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/28/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/29/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/30/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/31/2017", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/01/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/02/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/03/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/04/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/05/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/06/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/07/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/08/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/09/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/10/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/11/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/12/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/13/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/14/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/15/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/16/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/17/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/18/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/19/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/20/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/21/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/22/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/23/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/24/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/25/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/26/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/27/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/28/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/29/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/30/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/31/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/01/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/02/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/03/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/04/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/05/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/06/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/07/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/08/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/09/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/10/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/11/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/12/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/13/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/14/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/15/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/16/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/17/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/18/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/19/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/20/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/21/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/22/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/23/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/24/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/25/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/26/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/27/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/28/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/01/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/02/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/03/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/04/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/05/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/06/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/07/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/08/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/09/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/10/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/11/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/12/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/13/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/14/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/15/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/16/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/17/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/18/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/19/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/20/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/21/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/22/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/23/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/24/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/25/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/26/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/27/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/28/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/29/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/30/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/31/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/01/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/02/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/03/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/04/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/05/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/06/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/07/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/08/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/09/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/10/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/11/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/12/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/13/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/14/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/15/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/16/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/17/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/18/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/19/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/20/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/21/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/22/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/23/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/24/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/25/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/26/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/27/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/28/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/29/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/30/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/01/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/02/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/03/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/04/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/05/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/06/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/07/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/08/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/09/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/10/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/11/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/12/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/13/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/14/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/15/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/16/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/17/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/18/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/19/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/20/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/21/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/22/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/23/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/24/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/25/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/26/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/27/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/28/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/29/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/30/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/31/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/01/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/02/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/03/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/04/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/05/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/06/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/07/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/08/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/09/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/10/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/11/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/12/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/13/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/14/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/15/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/16/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/17/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/18/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/19/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/20/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/21/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/22/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/23/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/24/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/25/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/26/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/27/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/28/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/29/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/30/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/01/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/02/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/03/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/04/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/05/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/06/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/07/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/08/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/09/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/10/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/11/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/12/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/13/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/14/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/15/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/16/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/17/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/18/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/19/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/20/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/21/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/22/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/23/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/24/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/25/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/26/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/27/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/28/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/29/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/30/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/31/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/01/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/02/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/03/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/04/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/05/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/06/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/07/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/08/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/09/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/10/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/11/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/12/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/13/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/14/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/15/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/16/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/17/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/18/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/19/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/20/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/21/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/22/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/23/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/24/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/25/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/26/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/27/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/28/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/29/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/30/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/31/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/01/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/02/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/03/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/04/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/05/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/06/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/07/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/08/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/09/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/10/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/11/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/12/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/13/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/14/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/15/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/16/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/17/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/18/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/19/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/20/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/21/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/22/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/23/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/24/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/25/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/26/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/27/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/28/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/29/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/30/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/01/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/02/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/03/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/04/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/05/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/06/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/07/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/08/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/09/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/10/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/11/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/12/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/13/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/14/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/15/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/16/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/17/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/18/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/19/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/20/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/21/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/22/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/23/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/24/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/25/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/26/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/27/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/28/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/29/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/30/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/31/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/01/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/02/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/03/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/04/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/05/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/06/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/07/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/08/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/09/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/10/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/11/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/12/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/13/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/14/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/15/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/16/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/17/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/18/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/19/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/20/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/21/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/22/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/23/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/24/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/25/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/26/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/27/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/28/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/29/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/30/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/01/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/02/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/03/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/04/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/05/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/06/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/07/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/08/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/09/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/10/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/11/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/12/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/13/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/14/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/15/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/16/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/17/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/18/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/19/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/20/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/21/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/22/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/23/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/24/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/25/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/26/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/27/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/28/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/29/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/30/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/31/2018", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/01/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/02/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/03/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/04/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/05/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/06/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/07/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/08/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/09/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/10/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/11/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/12/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/13/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/14/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/15/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/16/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/17/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/18/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/19/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/20/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/21/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/22/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/23/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/24/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/25/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/26/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/27/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/28/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/29/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/30/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/31/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/01/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/02/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/03/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/04/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/05/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/06/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/07/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/08/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/09/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/10/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/11/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/12/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/13/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/14/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/15/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/16/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/17/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/18/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/19/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/20/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/21/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/22/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/23/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/24/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/25/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/26/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/27/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/28/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/01/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/02/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/03/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/04/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/05/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/06/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/07/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/08/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/09/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/10/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/11/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/12/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/13/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/14/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/15/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/16/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/17/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/18/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/19/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/20/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/21/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/22/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/23/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/24/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/25/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/26/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/27/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/28/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/29/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/30/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/31/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/01/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/02/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/03/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/04/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/05/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/06/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/07/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/08/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/09/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/10/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/11/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/12/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/13/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/14/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/15/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/16/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/17/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/18/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/19/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/20/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/21/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/22/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/23/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/24/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/25/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/26/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/27/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/28/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/29/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/30/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/01/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/02/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/03/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/04/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/05/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/06/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/07/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/08/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/09/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/10/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/11/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/12/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/13/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/14/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/15/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/16/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/17/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/18/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/19/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/20/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/21/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/22/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/23/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/24/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/25/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/26/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/27/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/28/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/29/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/30/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/31/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/01/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/02/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/03/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/04/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/05/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/06/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/07/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/08/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/09/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/10/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/11/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/12/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/13/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/14/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/15/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/16/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/17/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/18/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/19/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/20/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/21/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/22/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/23/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/24/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/25/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/26/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/27/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/28/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/29/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/30/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/01/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/02/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/03/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/04/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/05/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/06/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/07/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/08/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/09/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/10/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/11/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/12/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/13/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/14/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/15/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/16/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/17/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/18/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/19/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/20/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/21/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/22/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/23/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/24/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/25/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/26/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/27/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/28/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/29/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/30/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/31/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/01/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/02/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/03/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/04/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/05/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/06/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/07/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/08/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/09/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/10/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/11/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/12/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/13/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/14/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/15/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/16/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/17/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/18/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/19/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/20/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/21/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/22/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/23/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/24/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/25/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/26/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/27/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/28/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/29/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/30/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/31/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/01/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/02/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/03/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/04/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/05/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/06/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/07/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/08/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/09/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/10/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/11/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/12/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/13/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/14/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/15/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/16/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/17/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/18/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/19/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/20/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/21/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/22/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/23/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/24/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/25/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/26/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/27/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/28/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/29/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/30/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/01/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/02/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/03/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/04/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/05/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/06/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/07/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/08/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/09/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/10/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/11/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/12/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/13/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/14/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/15/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/16/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/17/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/18/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/19/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/20/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/21/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/22/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/23/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/24/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/25/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/26/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/27/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/28/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/29/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/30/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/31/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/01/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/02/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/03/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/04/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/05/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/06/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/07/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/08/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/09/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/10/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/11/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/12/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/13/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/14/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/15/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/16/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/17/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/18/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/19/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/20/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/21/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/22/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/23/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/24/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/25/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/26/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/27/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/28/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/29/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/30/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/01/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/02/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/03/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/04/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/05/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/06/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/07/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/08/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/09/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/10/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/11/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/12/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/13/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/14/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/15/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/16/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/17/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/18/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/19/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/20/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/21/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/22/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/23/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/24/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/25/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/26/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/27/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/28/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/29/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/30/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/31/2019", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/01/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/02/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/03/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/04/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/05/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/06/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/07/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/08/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/09/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/10/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/11/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/12/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/13/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/14/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/15/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/16/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/17/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/18/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/19/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/20/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/21/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/22/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/23/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/24/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/25/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/26/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/27/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/28/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/29/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/30/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/31/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/01/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/02/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/03/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/04/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/05/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/06/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/07/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/08/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/09/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/10/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/11/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/12/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/13/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/14/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/15/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/16/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/17/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/18/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/19/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/20/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/21/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/22/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/23/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/24/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/25/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/26/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/27/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/28/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/29/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/01/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/02/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/03/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/04/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/05/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/06/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/07/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/08/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/09/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/10/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/11/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/12/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/13/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/14/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/15/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/16/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/17/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/18/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/19/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/20/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/21/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/22/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/23/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/24/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/25/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/26/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/27/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/28/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/29/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/30/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/31/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/01/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/02/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/03/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/04/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/05/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/06/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/07/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/08/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/09/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/10/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/11/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/12/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/13/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/14/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/15/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/16/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/17/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/18/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/19/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/20/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/21/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/22/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/23/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/24/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/25/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/26/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/27/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/28/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/29/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/30/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/01/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/02/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/03/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/04/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/05/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/06/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/07/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/08/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/09/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/10/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/11/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/12/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/13/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/14/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/15/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/16/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/17/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/18/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/19/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/20/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/21/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/22/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/23/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/24/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/25/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/26/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/27/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/28/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/29/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/30/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/31/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/01/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/02/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/03/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/04/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/05/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/06/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/07/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/08/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/09/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/10/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/11/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/12/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/13/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/14/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/15/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/16/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/17/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/18/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/19/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/20/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/21/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/22/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/23/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/24/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/25/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/26/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/27/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/28/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/29/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/30/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/01/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/02/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/03/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/04/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/05/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/06/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/07/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/08/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/09/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/10/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/11/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/12/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/13/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/14/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/15/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/16/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/17/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/18/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/19/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/20/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/21/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/22/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/23/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/24/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/25/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/26/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/27/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/28/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/29/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/30/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/31/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/01/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/02/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/03/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/04/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/05/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/06/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/07/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/08/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/09/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/10/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/11/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/12/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/13/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/14/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/15/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/16/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/17/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/18/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/19/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/20/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/21/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/22/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/23/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/24/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/25/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/26/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/27/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/28/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/29/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/30/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/31/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/01/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/02/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/03/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/04/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/05/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/06/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/07/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/08/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/09/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/10/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/11/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/12/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/13/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/14/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/15/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/16/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/17/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/18/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/19/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/20/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/21/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/22/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/23/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/24/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/25/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/26/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/27/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/28/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/29/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/30/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/01/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/02/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/03/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/04/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/05/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/06/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/07/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/08/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/09/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/10/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/11/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/12/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/13/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/14/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/15/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/16/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/17/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/18/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/19/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/20/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/21/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/22/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/23/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/24/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/25/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/26/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/27/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/28/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/29/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/30/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/31/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/01/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/02/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/03/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/04/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/05/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/06/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/07/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/08/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/09/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/10/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/11/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/12/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/13/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/14/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/15/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/16/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/17/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/18/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/19/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/20/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/21/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/22/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/23/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/24/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/25/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/26/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/27/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/28/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/29/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/30/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/01/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/02/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/03/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/04/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/05/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/06/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/07/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/08/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/09/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/10/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/11/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/12/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/13/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/14/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/15/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/16/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/17/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/18/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/19/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/20/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/21/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/22/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/23/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/24/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/25/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/26/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/27/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/28/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/29/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/30/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/31/2020", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/01/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/02/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/03/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/04/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/05/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/06/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/07/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/08/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/09/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/10/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/11/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/12/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/13/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/14/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/15/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/16/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/17/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/18/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/19/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/20/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/21/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/22/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/23/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/24/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/25/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/26/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/27/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/28/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/29/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/30/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/31/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/01/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/02/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/03/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/04/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/05/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/06/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/07/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/08/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/09/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/10/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/11/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/12/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/13/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/14/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/15/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/16/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/17/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/18/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/19/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/20/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/21/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/22/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/23/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/24/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/25/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/26/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/27/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/28/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/01/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/02/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/03/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/04/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/05/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/06/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/07/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/08/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/09/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/10/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/11/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/12/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/13/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/14/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/15/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/16/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/17/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/18/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/19/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/20/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/21/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/22/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/23/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/24/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/25/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/26/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/27/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/28/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/29/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/30/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/31/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/01/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/02/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/03/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/04/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/05/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/06/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/07/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/08/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/09/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/10/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/11/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/12/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/13/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/14/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/15/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/16/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/17/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/18/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/19/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/20/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/21/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/22/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/23/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/24/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/25/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/26/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/27/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/28/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/29/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/30/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/01/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/02/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/03/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/04/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/05/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/06/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/07/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/08/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/09/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/10/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/11/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/12/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/13/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/14/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/15/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/16/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/17/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/18/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/19/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/20/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/21/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/22/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/23/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/24/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/25/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/26/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/27/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/28/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/29/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/30/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/31/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/01/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/02/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/03/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/04/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/05/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/06/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/07/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/08/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/09/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/10/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/11/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/12/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/13/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/14/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/15/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/16/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/17/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/18/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/19/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/20/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/21/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/22/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/23/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/24/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/25/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/26/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/27/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/28/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/29/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/30/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/01/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/02/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/03/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/04/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/05/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/06/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/07/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/08/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/09/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/10/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/11/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/12/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/13/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/14/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/15/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/16/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/17/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/18/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/19/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/20/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/21/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/22/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/23/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/24/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/25/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/26/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/27/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/28/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/29/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/30/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/31/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/01/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/02/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/03/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/04/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/05/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/06/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/07/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/08/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/09/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/10/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/11/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/12/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/13/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/14/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/15/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/16/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/17/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/18/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/19/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/20/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/21/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/22/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/23/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/24/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/25/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/26/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/27/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/28/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/29/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/30/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/31/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/01/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/02/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/03/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/04/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/05/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/06/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/07/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/08/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/09/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/10/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/11/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/12/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/13/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/14/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/15/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/16/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/17/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/18/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/19/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/20/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/21/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/22/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/23/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/24/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/25/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/26/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/27/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/28/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/29/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/30/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/01/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/02/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/03/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/04/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/05/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/06/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/07/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/08/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/09/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/10/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/11/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/12/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/13/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/14/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/15/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/16/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/17/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/18/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/19/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/20/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/21/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/22/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/23/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/24/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/25/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/26/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/27/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/28/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/29/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/30/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/31/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/01/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/02/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/03/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/04/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/05/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/06/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/07/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/08/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/09/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/10/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/11/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/12/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/13/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/14/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/15/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/16/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/17/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/18/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/19/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/20/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/21/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/22/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/23/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/24/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/25/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/26/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/27/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/28/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/29/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/30/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/01/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/02/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/03/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/04/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/05/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/06/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/07/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/08/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/09/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/10/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/11/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/12/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/13/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/14/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/15/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/16/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/17/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/18/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/19/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/20/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/21/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/22/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/23/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/24/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/25/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/26/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/27/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/28/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/29/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/30/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/31/2021", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/01/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/02/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/03/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/04/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/05/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/06/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/07/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/08/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/09/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/10/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/11/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/12/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/13/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/14/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/15/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/16/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/17/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/18/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/19/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/20/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/21/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/22/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/23/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/24/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/25/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/26/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/27/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/28/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/29/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/30/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/31/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/01/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/02/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/03/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/04/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/05/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/06/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/07/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/08/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/09/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/10/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/11/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/12/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/13/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/14/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/15/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/16/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/17/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/18/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/19/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/20/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/21/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/22/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/23/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/24/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/25/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/26/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/27/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/28/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/01/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/02/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/03/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/04/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/05/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/06/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/07/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/08/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/09/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/10/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/11/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/12/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/13/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/14/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/15/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/16/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/17/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/18/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/19/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/20/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/21/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/22/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/23/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/24/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/25/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/26/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/27/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/28/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/29/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/30/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/31/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/01/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/02/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/03/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/04/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/05/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/06/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/07/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/08/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/09/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/10/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/11/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/12/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/13/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/14/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/15/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/16/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/17/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/18/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/19/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/20/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/21/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/22/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/23/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/24/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/25/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/26/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/27/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/28/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/29/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/30/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/01/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/02/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/03/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/04/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/05/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/06/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/07/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/08/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/09/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/10/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/11/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/12/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/13/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/14/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/15/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/16/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/17/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/18/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/19/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/20/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/21/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/22/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/23/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/24/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/25/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/26/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/27/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/28/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/29/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/30/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/31/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/01/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/02/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/03/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/04/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/05/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/06/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/07/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/08/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/09/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/10/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/11/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/12/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/13/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/14/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/15/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/16/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/17/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/18/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/19/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/20/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/21/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/22/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/23/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/24/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/25/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/26/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/27/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/28/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/29/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/30/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/01/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/02/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/03/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/04/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/05/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/06/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/07/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/08/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/09/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/10/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/11/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/12/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/13/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/14/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/15/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/16/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/17/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/18/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/19/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/20/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/21/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/22/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/23/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/24/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/25/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/26/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/27/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/28/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/29/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/30/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/31/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/01/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/02/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/03/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/04/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/05/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/06/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/07/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/08/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/09/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/10/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/11/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/12/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/13/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/14/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/15/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/16/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/17/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/18/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/19/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/20/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/21/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/22/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/23/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/24/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/25/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/26/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/27/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/28/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/29/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/30/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/31/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/01/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/02/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/03/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/04/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/05/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/06/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/07/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/08/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/09/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/10/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/11/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/12/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/13/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/14/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/15/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/16/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/17/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/18/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/19/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/20/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/21/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/22/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/23/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/24/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/25/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/26/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/27/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/28/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/29/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/30/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/01/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/02/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/03/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/04/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/05/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/06/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/07/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/08/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/09/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/10/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/11/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/12/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/13/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/14/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/15/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/16/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/17/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/18/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/19/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/20/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/21/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/22/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/23/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/24/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/25/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/26/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/27/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/28/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/29/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/30/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/31/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/01/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/02/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/03/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/04/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/05/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/06/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/07/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/08/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/09/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/10/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/11/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/12/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/13/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/14/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/15/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/16/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/17/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/18/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/19/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/20/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/21/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/22/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/23/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/24/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/25/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/26/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/27/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/28/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/29/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/30/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/01/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/02/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/03/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/04/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/05/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/06/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/07/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/08/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/09/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/10/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/11/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/12/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/13/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/14/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/15/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/16/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/17/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/18/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/19/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/20/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/21/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/22/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/23/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/24/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/25/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/26/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/27/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/28/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/29/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/30/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/31/2022", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/01/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/02/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/03/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/04/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/05/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/06/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/07/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/08/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/09/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/10/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/11/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/12/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/13/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/14/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/15/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/16/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/17/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/18/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/19/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/20/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/21/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/22/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/23/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/24/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/25/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/26/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/27/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/28/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/29/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/30/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/31/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/01/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/02/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/03/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/04/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/05/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/06/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/07/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/08/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/09/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/10/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/11/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/12/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/13/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/14/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/15/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/16/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/17/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/18/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/19/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/20/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/21/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/22/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/23/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/24/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/25/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/26/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/27/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/28/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/01/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/02/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/03/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/04/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/05/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/06/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/07/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/08/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/09/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/10/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/11/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/12/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/13/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/14/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/15/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/16/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/17/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/18/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/19/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/20/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/21/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/22/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/23/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/24/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/25/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/26/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/27/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/28/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/29/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/30/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/31/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/01/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/02/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/03/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/04/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/05/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/06/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/07/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/08/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/09/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/10/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/11/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/12/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/13/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/14/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/15/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/16/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/17/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/18/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/19/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/20/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/21/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/22/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/23/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/24/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/25/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/26/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/27/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/28/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/29/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/30/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/01/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/02/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/03/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/04/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/05/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/06/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/07/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/08/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/09/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/10/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/11/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/12/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/13/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/14/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/15/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/16/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/17/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/18/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/19/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/20/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/21/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/22/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/23/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/24/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/25/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/26/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/27/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/28/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/29/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/30/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/31/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/01/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/02/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/03/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/04/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/05/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/06/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/07/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/08/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/09/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/10/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/11/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/12/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/13/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/14/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/15/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/16/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/17/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/18/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/19/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/20/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/21/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/22/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/23/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/24/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/25/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/26/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/27/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/28/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/29/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("06/30/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/01/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/02/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/03/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/04/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/05/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/06/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/07/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/08/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/09/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/10/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/11/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/12/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/13/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/14/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/15/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/16/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/17/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/18/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/19/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/20/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/21/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/22/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/23/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/24/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/25/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/26/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/27/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/28/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/29/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/30/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("07/31/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/01/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/02/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/03/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/04/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/05/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/06/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/07/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/08/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/09/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/10/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/11/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/12/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/13/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/14/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/15/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/16/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/17/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/18/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/19/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/20/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/21/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/22/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/23/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/24/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/25/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/26/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/27/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/28/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/29/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/30/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("08/31/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/01/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/02/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/03/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/04/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/05/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/06/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/07/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/08/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/09/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/10/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/11/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/12/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/13/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/14/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/15/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/16/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/17/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/18/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/19/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/20/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/21/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/22/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/23/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/24/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/25/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/26/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/27/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/28/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/29/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("09/30/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/01/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/02/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/03/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/04/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/05/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/06/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/07/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/08/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/09/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/10/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/11/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/12/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/13/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/14/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/15/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/16/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/17/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/18/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/19/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/20/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/21/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/22/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/23/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/24/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/25/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/26/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/27/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/28/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/29/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/30/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("10/31/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/01/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/02/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/03/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/04/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/05/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/06/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/07/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/08/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/09/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/10/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/11/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/12/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/13/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/14/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/15/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/16/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/17/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/18/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/19/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/20/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/21/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/22/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/23/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/24/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/25/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/26/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/27/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/28/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/29/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("11/30/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/01/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/02/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/03/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/04/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/05/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/06/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/07/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/08/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/09/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/10/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/11/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/12/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/13/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/14/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/15/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/16/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/17/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/18/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/19/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/20/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/21/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/22/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/23/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/24/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/25/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/26/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/27/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/28/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/29/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/30/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("12/31/2023", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/01/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/02/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/03/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/04/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/05/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/06/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/07/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/08/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/09/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/10/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/11/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/12/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/13/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/14/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/15/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/16/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/17/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/18/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/19/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/20/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/21/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/22/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/23/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/24/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/25/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/26/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/27/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/28/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/29/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/30/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("01/31/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/01/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/02/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/03/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/04/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/05/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/06/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/07/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/08/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/09/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/10/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/11/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/12/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/13/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/14/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/15/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/16/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/17/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/18/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/19/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/20/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/21/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/22/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/23/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/24/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/25/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/26/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/27/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/28/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("02/29/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/01/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/02/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/03/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/04/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/05/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/06/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/07/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/08/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/09/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/10/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/11/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/12/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/13/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/14/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/15/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/16/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/17/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/18/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/19/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/20/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/21/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/22/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/23/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/24/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/25/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/26/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/27/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/28/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/29/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/30/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("03/31/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/01/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/02/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/03/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/04/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/05/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/06/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/07/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/08/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/09/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/10/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/11/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/12/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/13/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/14/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/15/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/16/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/17/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/18/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/19/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/20/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/21/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/22/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/23/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/24/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/25/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/26/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/27/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/28/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/29/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("04/30/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/01/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/02/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/03/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/04/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/05/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/06/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/07/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/08/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/09/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/10/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/11/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/12/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/13/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/14/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/15/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/16/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/17/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/18/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/19/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/20/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/21/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/22/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/23/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/24/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/25/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/26/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/27/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/28/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/29/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/30/2024", 180, "RF", "S&P", "AAA", true);
     * updateSpotVolCurve("05/31/2024", 180, "RF", "S&P", "AAA", true);
     * }
     * 
     * public void updateForwardVolCurves() {
     * 
     * updateForwardVolCurve("12/01/2023", 180, "RF", "S&P", "AAA", false);
     * updateForwardVolCurve("12/02/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/03/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/04/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/05/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/06/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/07/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/08/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/09/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/10/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/11/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/12/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/13/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/14/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/15/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/16/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/17/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/18/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/19/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/20/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/21/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/22/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/23/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/24/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/25/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/26/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/27/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/28/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/29/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/30/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("12/31/2023", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/01/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/02/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/03/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/04/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/05/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/06/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/07/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/08/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/09/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/10/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/11/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/12/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/13/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/14/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/15/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/16/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/17/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/18/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/19/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/20/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/21/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/22/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/23/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/24/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/25/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/26/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/27/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/28/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/29/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/30/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("01/31/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/01/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/02/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/03/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/04/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/05/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/06/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/07/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/08/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/09/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/10/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/11/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/12/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/13/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/14/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/15/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/16/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/17/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/18/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/19/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/20/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/21/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/22/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/23/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/24/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/25/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/26/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/27/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/28/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("02/29/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/01/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/02/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/03/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/04/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/05/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/06/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/07/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/08/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/09/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/10/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/11/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/12/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/13/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/14/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/15/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/16/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/17/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/18/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/19/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/20/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/21/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/22/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/23/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/24/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/25/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/26/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/27/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/28/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/29/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/30/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("03/31/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/01/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/02/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/03/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/04/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/05/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/06/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/07/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/08/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/09/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/10/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/11/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/12/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/13/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/14/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/15/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/16/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/17/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/18/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/19/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/20/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/21/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/22/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/23/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/24/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/25/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/26/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/27/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/28/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/29/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("04/30/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/01/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/02/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/03/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/04/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/05/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/06/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/07/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/08/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/09/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/10/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/11/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/12/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/13/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/14/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/15/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/16/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/17/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/18/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/19/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/20/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/21/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/22/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/23/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/24/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/25/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/26/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/27/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/28/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/29/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/30/2024", 180, "RF", "S&P", "AAA", true);
     * updateForwardVolCurve("05/31/2024", 180, "RF", "S&P", "AAA", true);
     * 
     * 
     * }
     * 
     */

    public void updateForwardVolCurve(String strStart, String strEnd, int analysisPeriod, String yieldType,
            String creditAgency, String creditRating, boolean append) {
        ArrayList<YieldPoint> forwardVols = new ArrayList<>();
        Dates dtNext = new Dates(strStart);
        Dates dtEnd = new Dates(strEnd);
        String fileName = this.fileNameForwardVol;
        while (dtNext.leDate(dtEnd)) {
            forwardVols = calcForwardVariance(yieldType, dtNext, analysisPeriod, creditAgency, creditRating);
            writeForwardVols(fileName, append, forwardVols);
        }
    }

    private void writeForwardVols(String fileName, boolean append, ArrayList<YieldPoint> forwardVols) {
        BufferedWriter curveVolWriter = null;
        try {
            curveVolWriter = new BufferedWriter(new FileWriter(fileName, append));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        // String curveHeader YieldType CreditAgency CreditRating DateOfAnalysis
        // AnalysisPeriod lookAhead Tenor forwardRate AvgRate StdDev RateDnSide
        // RateUpSide RateMin RateMax
        String curveHeader = "YieldType,CreditAgency,CreditRating,AnalysisStart,AnalysisPeriod,LookAhead,Tenor,SpotRate,ForwardRate,AvgError,StdDevOfError,RateDnSide,RateUpSide,MinError,MaxError\n";
        if (append)
            curveHeader = "";
        int noRecs = writerForwardCurveVols(curveVolWriter, curveHeader, dateFormat, forwardVols);
        try {
            curveVolWriter.close();
            curveVolWriter.close();
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        System.out.println("Wrote " + noRecs + " forward curve volatility records to " + fileName);
    }

    private ArrayList<MatCurveStructure> getFileMatStructure(String strHeader,
            ArrayList<MatCurveStructure> matCurveStructures) {
        StringTokenizer strTok = new StringTokenizer(strHeader, ",");
        ArrayList<MatCurveStructure> fileCurveStructure = new ArrayList<>();
        int tok = 0;
        int matIdx = 0;
        int noHits = 0;
        int noMats = 0;
        String dummy = strTok.nextToken(); // ignore date label
        while (strTok.hasMoreTokens()) {
            String strMat = strTok.nextToken();// ignore date
            ++noMats;
            int matNo = -1;
            while (++matNo < matCurveStructures.size()) {
                String matName = matCurveStructures.get(matNo).matName;
                if (strMat.equals(matName)) {
                    int matlen = matCurveStructures.get(matNo).matLen;
                    fileCurveStructure.add(matIdx++, new MatCurveStructure(matName, matlen));
                    ++noHits;
                    break;
                }
            }
        }
        if (noMats != noHits) {
            String errmsg = "The number of maturities in the file(" + noMats
                    + ") not equal to number of maturities found(" + noHits + ") in allowable maturity structure";
            error.setError(getClass().getName(), errmsg);

        }
        return fileCurveStructure;
    }

    private ArrayList<YieldPoint> setBaseCurve(Dates dtCurve, String[] strLineParts,
            ArrayList<MatCurveStructure> fileMatStructure) {
        ArrayList<YieldPoint> baseCurve = new ArrayList<>();
        YieldPoint yieldPoint = new YieldPoint();
        // parse the string containing curve rate
        int fileMats = fileMatStructure.size();
        int noMats = strLineParts.length - 1;
        if (noMats != fileMats) {
            String errmsg = "Invalid number of yield points detect in file - expected(" + fileMats + ") found "
                    + noMats;
            this.yieldCurves.error.setError(getClass().getName(), errmsg);
            return baseCurve;
        }
        double d2Yield = 0.;
        String curveType = BondConsts.PARCURVE;
        int lookAhead = 0;
        Dates dtForward = dtCurve;
        Dates dtSpotCompare = dtCurve;
        int no = -1;
        while (++no < noMats) {
            String strYield = strLineParts[no + 1];
            yieldPoint.spotRate = Double.valueOf(strYield);
            int matLen = fileMatStructure.get(no).matLen;
            yieldPoint.matLen = matLen;
            yieldPoint.matName = fileMatStructure.get(no).matName;
            yieldPoint.lookAhead = lookAhead;
            yieldPoint.dtCurve = dtCurve;
            yieldPoint.curveType = curveType;
            yieldPoint.dtForward = yieldCurves.dayCount.time.addMonthsToDate(dtCurve, lookAhead, 'N');
            baseCurve.add(no, new YieldPoint(yieldPoint));
        }
        yieldCurves.parCurve = baseCurve;
        return baseCurve;
    }

    private int writeYieldPointRecords(BufferedWriter fileWriter, String curveHeader, String dateFormat,
            ArrayList<YieldPoint> yieldCurve, String yieldType, String creditAgency, String creditRating) {
        // write out base curve
        int noRecs = yieldCurve.size();
        try {
            if (curveHeader != "")
                fileWriter.write(curveHeader + "\n");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        int no = -1;
        while (++no < noRecs) {
            try {
                String strCurve = setCurveStringRecord(yieldCurve, dateFormat, yieldType, creditAgency, creditRating,
                        no);
                fileWriter.write(strCurve + "\n");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return noRecs;
    }

    public int writerYieldCurveVolatility(BufferedWriter fileWriter, String dateFormat,
            ArrayList<YieldPoint> yieldPoint, String creditAgency, String creditRating) {
        // write out base curve
        int noRecs = yieldPoint.size();

        int no = -1;
        while (++no < noRecs) {
            try {
                String strRec = getCurveStringVolatilityRecord(yieldPoint, dateFormat, creditAgency, creditRating, no);
                fileWriter.write(strRec + "\n");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return noRecs;
    }

    public int writerSpotCurveVols(BufferedWriter currentWriter, String curveHeader, Dates dtAnalysis,
            int analysisPeriod, ArrayList<YieldPoint> spotCurve, String yieldType, String creditAgency,
            String creditRating) {
        String strRec = "";
        int nr = -1;
        double itemVal = 0.;
        try {
            currentWriter.write(curveHeader);

        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        int matLen = 0;
        while (++nr < spotCurve.size()) {
            strRec = yieldType + "," + creditAgency + "," + creditRating + "," + dtAnalysis.getDateString() + ","
                    + analysisPeriod + ",";
            strRec = strRec + spotCurve.get(nr).matLen + ",";
            itemVal = spotCurve.get(nr).spotRate;
            itemVal = Utility.truncRound(itemVal, 3, 'Y');
            strRec = strRec + itemVal + ",";

            itemVal = spotCurve.get(nr).spotAvg;
            itemVal = Utility.truncRound(itemVal, 3, 'Y');
            strRec = strRec + itemVal + ",";

            itemVal = spotCurve.get(nr).spotStdDev;
            itemVal = Utility.truncRound(itemVal, 3, 'Y');
            strRec = strRec + itemVal + ",";

            itemVal = spotCurve.get(nr).spotDnSide;
            itemVal = Utility.truncRound(itemVal, 3, 'Y');
            strRec = strRec + itemVal + ",";

            itemVal = spotCurve.get(nr).spotUpSide;
            itemVal = Utility.truncRound(itemVal, 3, 'Y');
            strRec = strRec + itemVal + ",";

            itemVal = spotCurve.get(nr).spotMin;
            itemVal = Utility.truncRound(itemVal, 3, 'Y');
            strRec = strRec + itemVal + ",";

            itemVal = spotCurve.get(nr).spotMax;
            itemVal = Utility.truncRound(itemVal, 3, 'Y');
            strRec = strRec + itemVal + "\n";
            try {
                currentWriter.write(strRec);

            } catch (Exception ex) {
                System.out.println(ex.getStackTrace());
            }
        }
        try {
            currentWriter.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return nr;
    }

    private void writeHeader(BufferedWriter fileWriter, String header) {
        try {
            fileWriter.write(header);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private int writerForwardCurveVols(BufferedWriter buffWriter, String curveHeader, String dateFormat,
            ArrayList<YieldPoint> forwardVols) {
        int noRecs = 0;

        String strRec = "";
        int no = -1;
        int noPoints = forwardVols.size();
        try {
            buffWriter.write(curveHeader);
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        // String curveHeader =
        // "YieldType,CreditAgency,CreditRating,AnalysisStart,AnalysisPeriod,LookAhead,Tenor,SpotRate,ForwardRate,AvgForwardRate,StdDev,RateDnSide,RateUpSide,MinRate,MaxRate\n";
        while (++no < noPoints) {
            strRec = forwardVols.get(no).yieldType + ",";

            strRec = strRec + forwardVols.get(no).creditAgency + ",";

            strRec = strRec + forwardVols.get(no).creditRating + ",";

            strRec = strRec + forwardVols.get(no).dtAnalysisStart.getDateString() + ",";

            strRec = strRec + forwardVols.get(no).analysisDays + ",";

            strRec += forwardVols.get(no).lookAhead + ",";

            strRec = strRec + forwardVols.get(no).matLen + ",";

            double spotRate = forwardVols.get(no).spotRate;
            spotRate = Utility.truncRound(spotRate, 3, 'Y');
            strRec = strRec + spotRate + ",";

            double forwardRate = forwardVols.get(no).forwardRate;
            forwardRate = Utility.truncRound(forwardRate, 3, 'Y');
            strRec = strRec + forwardRate + ",";

            double dVal = forwardVols.get(no).spotAvg;
            dVal = Utility.truncRound(dVal, 3, 'Y');
            strRec = strRec + dVal + ",";

            dVal = forwardVols.get(no).spotStdDev;
            dVal = Utility.truncRound(dVal, 3, 'Y');
            strRec = strRec + dVal + ",";

            dVal = forwardVols.get(no).spotDnSide;
            dVal = Utility.truncRound(dVal, 3, 'Y');
            strRec = strRec + dVal + ",";

            dVal = forwardVols.get(no).spotUpSide;
            dVal = Utility.truncRound(dVal, 3, 'Y');
            strRec = strRec + dVal + ",";

            dVal = forwardVols.get(no).spotMin;
            dVal = Utility.truncRound(dVal, 3, 'Y');
            strRec = strRec + dVal + ",";

            dVal = forwardVols.get(no).spotMax;
            dVal = Utility.truncRound(dVal, 3, 'Y');
            strRec = strRec + dVal + "\n";

            try {
                buffWriter.write(strRec);
            } catch (Exception ex) {
                System.out.println(ex.getStackTrace());
            }
        }
        try {
            buffWriter.close();
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        return no;
    }

    // curveType, avgOrPoint, dateFormat, no
    private String setCurveStringRecord(ArrayList<YieldPoint> yieldCurve, String dateFormat, String yieldType,
            String creditAgency, String creditRating, int no) {
        double yield;
        int matLen = 0;
        matLen = yieldCurve.get(no).matLen;
        int lookAhead = yieldCurve.get(no).lookAhead;
        String strRec;
        strRec = yieldType + "," + creditAgency + "," + creditRating + "," + yieldCurve.get(no).curveType + ",";
        strRec = strRec + yieldCurve.get(no).dtCurve.getDateString(dateFormat) + ",";
        strRec = strRec + yieldCurve.get(no).dtForward.getDateString(dateFormat) + ",";
        strRec = strRec + matLen + ",";
        yield = yieldCurve.get(no).spotRate;
        yield = Utility.truncRound(yield, 3, 'Y');
        strRec = strRec + yield + ",";
        String matName = yieldCurves.getMatLenName(matLen);
        strRec = strRec + matName + ",";
        strRec = strRec + lookAhead;
        return strRec;
    }

    private String getCurveStringVolatilityRecord(ArrayList<YieldPoint> yieldPoint, String dateFormat,
            String creditAgency, String creditRating, int no) {
        String strRec = creditAgency + "," + creditRating + "," + yieldPoint.get(no).matLen + ",";
        strRec = strRec + yieldPoint.get(no).dtAnalysisStart.getDateString(dateFormat) + ",";
        strRec = strRec + yieldPoint.get(no).analysisDays + ",";

        double spotVal = yieldPoint.get(no).spotRate;
        spotVal = Utility.truncRound(spotVal, 3, 'Y');
        strRec = strRec + spotVal + ",";

        spotVal = yieldPoint.get(no).spotAvg;
        spotVal = Utility.truncRound(spotVal, 3, 'Y');
        strRec = strRec + spotVal + ",";

        spotVal = yieldPoint.get(no).spotStdDev;
        spotVal = Utility.truncRound(spotVal, 3, 'Y');
        strRec = strRec + spotVal + ",";

        spotVal = yieldPoint.get(no).spotDnSide;
        spotVal = Utility.truncRound(spotVal, 3, 'Y');
        strRec = strRec + spotVal + ",";

        spotVal = yieldPoint.get(no).spotUpSide;
        spotVal = Utility.truncRound(spotVal, 3, 'Y');
        strRec = strRec + spotVal + ",";

        spotVal = yieldPoint.get(no).spotMin;
        spotVal = Utility.truncRound(spotVal, 3, 'Y');
        strRec = strRec + spotVal + ",";

        spotVal = yieldPoint.get(no).spotMax;
        spotVal = Utility.truncRound(spotVal, 3, 'Y');
        strRec = strRec + spotVal + ",";

        return strRec;
    }

    private ArrayList<YieldPoint> calcLookAheadForwardCurve(YieldCurves yieldCurve, int[] lookAheads) {
        ArrayList<YieldPoint> forwardCurves = new ArrayList<>();
        ArrayList<YieldPoint> forwardCurve = new ArrayList<>();
        Dates dtForward = new Dates();
        Dates dtCurve = new Dates();
        double d2Yield = 0.;
        dtCurve = yieldCurve.spotCurve.get(0).dtCurve;
        int cumNo = 0;
        int no = -1;
        YieldPoint yieldPoint = new YieldPoint();
        while (++no < lookAheads.length) {
            int lookAhead = lookAheads[no];
            dtForward = yieldCurves.dayCount.time.addMonthsToDate(dtCurve, lookAhead, 'N');
            yieldCurve.yieldCurveParams.dtCurve = dtCurve;
            forwardCurve = yieldCurves.calcforwardCurve(yieldCurve.spotCurve, yieldCurve.yieldCurveParams, lookAhead);
            // Dates predictDate = dtFuture +
            int cNo = -1;
            int noPoints = forwardCurve.size();
            while (++cNo < noPoints) {
                yieldPoint = forwardCurve.get(cNo);
                forwardCurves.add(cumNo++, new YieldPoint(yieldPoint));
            }
        }
        return forwardCurves;
    }

    public ArrayList<YieldPoint> calcSpotVariance(Dates dtAnalysis, int analysisDays, String creditAgency,
            String creditRating) {
        ArrayList<YieldPoint> spotCurve = new ArrayList<>();
        spotCurve = getSpotCurveInAnalysisPeriod(dtAnalysis, analysisDays, creditAgency, creditRating);
        // Collections.sort(list, comparator)
        if (spotCurve.isEmpty()) {
            Dates dtStart = dayCount.time.addDaysToDate(dtAnalysis, -analysisPeriod, 'N');
            System.out.println("No spot curves satisfy analysis criteria for rates in analsyis period from "
                    + dtStart.getDateString() + " to " + dtAnalysis.getDateString() + " and creditAgency "
                    + creditAgency + " and  credit rating " + creditRating);
            System.exit(0);
        }
        SortSpotCurveAsc sortSpotCurveAsc = new SortSpotCurveAsc();
        Collections.sort(spotCurve, sortSpotCurveAsc);
        boolean isForward = false;
        spotCurve = calcTenorSpotVariance(spotCurve, dtAnalysis, analysisDays);
        return spotCurve;
        // write spot volatilities
    }

    private ArrayList<YieldPoint> getSpotCurveInAnalysisPeriod(Dates dtAnalysis, int analysisDays, String creditAgency,
            String creditRating) {
        ArrayList<YieldPoint> spotCurve = new ArrayList<>();
        YieldPoint yieldPoint = new YieldPoint();
        Scanner scanner;
        FileReader fileReader;
        try {
            fileReader = new FileReader(fileNameSpotVol);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(fileReader);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
        creditAgency = creditAgency.toUpperCase();
        creditRating = creditRating.toUpperCase();
        scanner = new Scanner(bufferedReader);
        // read in all spot curve history and store the last n months
        String strHeading = "Credit Agency,Credit Rating,Tenor,Analysis Start,Analysis Period(days),Avg Spot,StdDev Spot,StdDev Dn,StdDevUp";
        int tNo = 0;
        String strHeader = scanner.nextLine();
        Dates dtStart = new Dates();
        dtStart = dayCount.time.addDaysToDate(dtAnalysis, -analysisDays, 'N');
        // read spot header
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String spotLine = scanner.nextLine();
            String[] strParts = spotLine.split(",");
            String yieldType = strParts[0];
            String spotCreditAgency = strParts[1].toUpperCase();
            String spotCreditRating = strParts[2].toUpperCase();
            String curveType = strParts[3];
            String strCurveDate = strParts[4];
            Dates dtCurve = new Dates(error);
            dtCurve.setDate(strCurveDate, dateFormat);
            if (dtCurve.geDate(dtStart) && dtCurve.leDate(dtAnalysis)) {
                if (spotCreditRating.equals(creditRating) && spotCreditAgency.equals(creditAgency)) {
                    yieldPoint.yieldType = strParts[0];
                    yieldPoint.curveType = BondConsts.SPOTCURVE;
                    yieldPoint.dtAnalysisStart = dtAnalysis;
                    yieldPoint.analysisDays = analysisPeriod;
                    yieldPoint.creditAgency = creditAgency;
                    yieldPoint.creditRating = creditRating;
                    yieldPoint.curveType = curveType;
                    yieldPoint.dtCurve = dtCurve;
                    String strForwardDate = strParts[5];
                    yieldPoint.dtForward = new Dates(error);
                    yieldPoint.dtForward.setDate(strForwardDate, dateFormat);
                    int matLen = Integer.valueOf(strParts[6]);
                    double rate = Double.valueOf(strParts[7]);
                    String matName = strParts[8];
                    int lookAhead = Integer.valueOf(strParts[9]);
                    yieldPoint.lookAhead = lookAhead;
                    yieldPoint.spotRate = rate;
                    yieldPoint.matLen = matLen;
                    yieldPoint.matName = matName;
                    spotCurve.add(tNo++, new YieldPoint(yieldPoint));

                }
            }
        }
        try {
            fileReader.close();
            bufferedReader.close();
            scanner.close();
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        return spotCurve;
    }

    private ArrayList<YieldPoint> getForwardCurveInAnalysisPeriod(Dates dtAnalysis, int analysisDays,
            String creditAgency, String creditRating) {
        ArrayList<YieldPoint> forwardCurve = new ArrayList<>();
        YieldPoint yieldPoint = new YieldPoint();
        Scanner scanner;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(fileNameForwardVol);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            System.exit(-1);
        }
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(fileReader);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
        // scanner = new Scanner(bufferedReader);
        // read in all spot curve history and store the last n months
        int tNo = 0;
        creditAgency = creditAgency.toUpperCase();
        creditRating = creditRating.toUpperCase();
        // String strHeader = scanner.nextLine();
        try {
            String strHeader = bufferedReader.readLine();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
        Dates dtStart = new Dates();
        dtStart = dayCount.time.addDaysToDate(dtAnalysis, -analysisDays, 'N');
        // read spot header
        // scanner.nextLine();
        // YieldCurve{0],CreditAgency[1],CreditRating[2],CurveType[3],
        // ,CurveDate[4],ForwardDate[5],Tenor[6],Rate[7],TenorName[8],LookAhead(mos)[9]
        // while (scanner.hasNextLine()) {
        String spotLine;
        try {
            while (null != (spotLine = bufferedReader.readLine())) {
                // String spotLine = scanner.nextLine();
                String[] strParts = spotLine.split(",");
                String yieldType = strParts[0];
                String spotCreditAgency = strParts[1].toUpperCase();
                String spotCreditRating = strParts[2].toUpperCase();
                String curveType = strParts[3].toUpperCase();
                String strCurveDate = strParts[4];
                Dates dtCurve = new Dates(error);
                dtCurve.setDate(strCurveDate, "MM/DD/YYYY");
                String strForwardDate = strParts[5];
                Dates dtForwardCurve = new Dates(error);
                dtForwardCurve.setDate(strForwardDate, "MM/DD/YYYY");
                int matLen = Integer.valueOf(strParts[6]);
                if (dtForwardCurve.geDate(dtStart) && dtForwardCurve.leDate(dtAnalysis)) {
                    if (spotCreditRating.equals(creditRating) && spotCreditAgency.equals(creditAgency)) {
                        if (matLen <= BondConsts.MAXTENORCUTOFF) {
                            yieldPoint.dtAnalysisStart = dtAnalysis;
                            yieldPoint.yieldType = yieldType;
                            yieldPoint.curveType = BondConsts.FORWARDCURVE;
                            yieldPoint.analysisDays = analysisDays;
                            yieldPoint.creditAgency = creditAgency;
                            yieldPoint.creditRating = creditRating;
                            yieldPoint.dtCurve = dtCurve;
                            yieldPoint.dtForward = dtForwardCurve;
                            yieldPoint.matLen = matLen;
                            String strRate = strParts[7];
                            yieldPoint.forwardRate = Double.valueOf(strRate);
                            String matName = strParts[8];
                            int lookAhead = Integer.valueOf(strParts[9]);
                            yieldPoint.lookAhead = lookAhead;
                            forwardCurve.add(tNo++, new YieldPoint(yieldPoint));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());

        }
        return forwardCurve;
    }
    // Collections.sort(list, comparator)

    private ArrayList<YieldPoint> calcForwardVariance(String yieldType, Dates dtAnalysis, int analysisPeriod,
            String creditAgency, String creditRating) {
        ArrayList<YieldPoint> forwardVols = new ArrayList<>();
        YieldPoint forwardVol = new YieldPoint();
        ArrayList<YieldPoint> spotCurve = new ArrayList<>();
        ArrayList<YieldPoint> forwardCurve = new ArrayList<>();
        forwardCurve = getForwardCurveInAnalysisPeriod(dtAnalysis, analysisPeriod, creditAgency, creditRating);
        int noForwards = forwardCurve.size();
        if (noForwards == 0) {
            Dates dtStart = dayCount.time.addDaysToDate(dtAnalysis, -analysisPeriod, 'N');
            System.out.println("No forward curves satisfy analysis criteria in analysis period from "
                    + dtStart.getDateString() + " to " + dtAnalysis.getDateString() + " and creditAgency "
                    + creditAgency + " and  credit rating " + creditRating);
            System.exit(0);
        }
        spotCurve = getSpotCurveInAnalysisPeriod(dtAnalysis, analysisPeriod, creditAgency, creditRating);
        if (spotCurve.isEmpty()) {
            Dates dtStart = dayCount.time.addDaysToDate(dtAnalysis, -analysisPeriod, 'N');
            System.out.println("No spot curves satisfy analysis criteria for rates in analsyis period from "
                    + dtStart.getDateString() + " to " + dtAnalysis.getDateString() + " andcreditAgency " + creditAgency
                    + "and  credit rating " + creditRating);
            System.exit(0);
        }
        SortSpotCurveAsc sortSpotCurveAsc = new SortSpotCurveAsc();
        Collections.sort(spotCurve, sortSpotCurveAsc);
        // set up hash tableForwardCurveCompare
        SortForwardCurveAsc sortForwardCurveAsc = new SortForwardCurveAsc();
        Collections.sort(forwardCurve, sortForwardCurveAsc);
        HashMap<Long, YieldPoint> spotCurveHashMap = new HashMap<>();
        long hashKey;
        int no = -1;
        Dates dtTest = new Dates();
        while (++no < spotCurve.size()) {
            hashKey = spotCurve.get(no).matLen + 1000 * spotCurve.get(no).dtCurve.getDateCode();
            spotCurveHashMap.put(hashKey, spotCurve.get(no));
        }
        no = -1;
        int listCount = 0;
        while (++no < noForwards) {
            forwardVol.yieldType = yieldType;
            forwardVol.analysisDays = forwardCurve.get(no).analysisDays;
            forwardVol.dtAnalysisStart = dtAnalysis;
            forwardVol.dtCurve = forwardCurve.get(no).dtCurve;
            forwardVol.creditAgency = forwardCurve.get(no).creditAgency;
            forwardVol.creditRating = forwardCurve.get(no).creditRating;
            forwardVol.matLen = forwardCurve.get(no).matLen;
            forwardVol.lookAhead = forwardCurve.get(no).lookAhead;
            double spotRate = 0.;
            hashKey = forwardCurve.get(no).matLen + 1000 * forwardCurve.get(no).dtForward.getDateCode();
            try {
                spotRate = spotCurveHashMap.get(hashKey).spotRate;

            } catch (Exception ex) {
                // System.out.println("Cannot find spot rate with hashKey " + hashKey);
                continue;
            }
            forwardVol.spotRate = spotRate;
            forwardVol.forwardRate = forwardCurve.get(no).forwardRate;
            forwardVol.dtForward = forwardCurve.get(no).dtForward;
            forwardVols.add(listCount++, new YieldPoint(forwardVol));
        }
        boolean isForward = true;
        forwardVols = calcTenorForwardVariance(forwardVols, dtAnalysis, analysisPeriod);
        if (forwardVols.isEmpty()) {
            Dates dtStart = dayCount.time.addDaysToDate(dtAnalysis, -analysisPeriod, 'N');
            System.out.println("No forward and spot curves satisfy analysis criteria for rates in analsyis period from "
                    + dtStart.getDateString() + " to " + dtAnalysis.getDateString() + " andcreditAgency " + creditAgency
                    + "and  credit rating " + creditRating);
            System.exit(0);
        }
        return forwardVols;
    }

    // Output: Calculated all spot aggregated spot stats across a given analysis
    // time period
    // Input is all indiviudal spot records in the analsyis period
    private ArrayList<YieldPoint> calcTenorSpotVariance(ArrayList<YieldPoint> yldInCurve, Dates dtAnalysis,
            int analysisDays) {
        ArrayList<YieldPoint> yldCurveOut = new ArrayList<>();
        ArrayList<YieldPoint> matLenList = new ArrayList<>(); // list to be summarized
        YieldPoint yldPointOut = new YieldPoint();
        int nextMatLen = 0;
        int noRates = 0;
        int no = -1;
        int noTenors = 0;
        double yield = 0.;
        int lastMatlen = yldInCurve.get(0).matLen;
        int noPoints = yldInCurve.size();
        while (++no < noPoints) {
            nextMatLen = yldInCurve.get(no).matLen;
            if (nextMatLen == lastMatlen) { // get list of all spot records for a given tenor or maturity length
                yield = yldInCurve.get(no).spotRate;
                lastMatlen = nextMatLen;
                matLenList.add(noRates++, new YieldPoint(yldInCurve.get(no)));
            } else {
                // summararize are statistic for a given tenor on a given start analysis dates
                yldPointOut = calcTenorSpotStats(matLenList, dtAnalysis, analysisDays, lastMatlen);
                yldCurveOut.add(noTenors++, new YieldPoint(yldPointOut));
                matLenList.clear();
                lastMatlen = nextMatLen;
                noRates = 0;
                yield = yldInCurve.get(no).spotRate;
                matLenList.add(noRates++, new YieldPoint(yldInCurve.get(no)));
            }
        }
        yldPointOut = calcTenorSpotStats(matLenList, dtAnalysis, analysisPeriod, lastMatlen);
        yldCurveOut.add(noTenors++, new YieldPoint(yldPointOut));
        return yldCurveOut;
    }

    // Input: All forward curve records with all lookahead and tenor records in an
    // analysis period
    // Output: Aggregated fkrward curve volatility record for each combination of
    // lookahead and tenor
    private ArrayList<YieldPoint> calcTenorForwardVariance(ArrayList<YieldPoint> yldInCurve, Dates dtAnalysis,
            int analysisDays) {
        ArrayList<YieldPoint> yldCurveOut = new ArrayList<>();
        YieldPoint yldPointOut = new YieldPoint();
        YieldPoint yldPointIn = new YieldPoint();
        ArrayList<YieldPoint> lookAheadList = new ArrayList<>();
        int nextMatLen = 0;
        int noRates = 0;
        int no = -1;
        int noTenors = 0;
        double yield = 0.;
        int lastMatlen = yldInCurve.get(0).matLen;
        int noPoints = yldInCurve.size();
        while (++no < noPoints) {
            nextMatLen = yldInCurve.get(no).matLen;
            if (nextMatLen == lastMatlen) {
                yield = yldInCurve.get(no).spotRate;
                lastMatlen = nextMatLen;
                yldPointIn = yldInCurve.get(no);
                lookAheadList.add(noRates++, new YieldPoint(yldPointIn));
            } else {
                // yldPointOut is first yield of next maturity
                yldPointOut = calcTenorForwardStats(lookAheadList);
                yldCurveOut.add(noTenors++, new YieldPoint(yldPointOut));
                yldPointOut.matLen = lastMatlen;
                lookAheadList.clear();
                lastMatlen = nextMatLen;
                noRates = 0;
                lookAheadList.add(noRates++, new YieldPoint(yldInCurve.get(no)));
            }
        }
        yldPointOut = calcTenorForwardStats(lookAheadList);
        yldPointOut.matLen = lastMatlen;
        yldCurveOut.add(noRates, yldPointOut);
        return yldCurveOut;
    }

    private YieldPoint calcTenorSpotStats(ArrayList<YieldPoint> matLenList, Dates dtAnalysis, int analysisPeriod,
            int lastMatLen) {
        YieldPoint yldPoint = new YieldPoint();
        double avgYield = 0.;
        double stdDev = 0.;
        int no = -1;
        // calc tenor sample
        int noRates = matLenList.size();
        Dates dtCurve = matLenList.get(noRates - 1).dtCurve;
        double yieldMin = Limits.MAXRATE * 100.;
        double yieldMax = Limits.MINRATE * 100.;
        double yield = 0., totYield = 0.;
        double yieldBase = matLenList.get(0).spotRate;
        double analysisRate = matLenList.get(noRates - 1).spotRate;
        double totChgUp = 0.;
        double totChgDn = 0.;
        yldPoint = matLenList.get(noRates - 1);
        int noChgUp = 0, noChgDn = 0;
        while (++no < noRates)
            totYield += matLenList.get(no).spotRate;
        if (noRates > 0)
            avgYield = totYield / noRates;
        no = -1;
        while (++no < noRates) {
            yield = matLenList.get(no).spotRate;
            stdDev += (yield - avgYield) * (yield - avgYield);
            double yieldChg = yield - yieldBase;
            if (yieldChg > 0.) {
                totChgUp += yieldChg;
                ++noChgUp;
            } else {
                if (yieldChg < 0.) {
                    totChgDn += -yieldChg;
                    ++noChgDn;
                }
            }
            if (yield > yieldMax)
                yieldMax = yield;
            if (yield < yieldMin)
                yieldMin = yield;
        }
        if (noRates > 1)
            stdDev = Math.sqrt(stdDev / (noRates - 1));
        yldPoint.spotDnSide = analysisRate;
        if (noChgDn > 0)
            yldPoint.spotDnSide = analysisRate - (totChgDn / noChgDn);
        yldPoint.spotUpSide = analysisRate;
        if (noChgUp > 0)
            yldPoint.spotUpSide = analysisRate + (totChgUp / noChgUp);
        yldPoint.spotRate = analysisRate;
        yldPoint.dtCurve = dtCurve;
        yldPoint.dtAnalysisStart = dtAnalysis;
        yldPoint.matLen = lastMatLen;
        yldPoint.matName = yieldCurves.getMatLenName(yldPoint.matLen);
        yldPoint.spotAvg = avgYield;
        yldPoint.spotStdDev = stdDev;
        yldPoint.spotMin = yieldMin;
        yldPoint.spotMax = yieldMax;
        yldPoint.analysisDays = analysisPeriod;
        return yldPoint;
    }

    // laList is lookAhead list
    private YieldPoint calcTenorForwardStats(ArrayList<YieldPoint> lookAheadList) {
        YieldPoint yldPoint = new YieldPoint();
        double avgDev = 0.;
        double stdDev = 0.;
        int no = -1;
        // calc tenor sample
        int noRates = lookAheadList.size();
        double devMin = Limits.MAXRATE * 100.;
        double devMax = Limits.MINRATE * 100.;
        double totDevUp = 0.;
        double totDevDn = 0.;
        double totDev = 0.;
        double totYield = 0.;
        int noDevUp = 0, noDevDn = 0;
        double dev = 0.;
        yldPoint = lookAheadList.get(0); // this will be populated with stat values
        while (++no < noRates) {
            dev = lookAheadList.get(no).forwardRate - lookAheadList.get(no).spotRate;
            totDev += dev;
            stdDev += dev * dev;
            if (dev > 0.) {
                totDevUp += dev;
                ++noDevUp;
            }
            if (dev < 0.) {
                totDevDn += -dev;
                ++noDevDn;
            }
            if (dev < devMin)
                devMin = dev;
            if (dev > devMax)
                devMax = dev;
        }
        if (noRates > 0)
            avgDev = Math.abs(totDev) / noRates;
        yldPoint.spotAvg = avgDev; // average deviation between forward and spot
        if (noRates > 1)
            stdDev = Math.sqrt(stdDev) / (noRates - 1);
        else
            stdDev = 0.;
        yldPoint.spotMin = devMin;
        yldPoint.spotMax = devMax;
        yldPoint.spotStdDev = stdDev;
        double avgDevDn = 0.;
        if (noDevDn > 0)
            avgDevDn = totDevDn / noDevDn;
        double analysisRate = lookAheadList.get(noRates - 1).forwardRate;
        yldPoint.spotDnSide = analysisRate - avgDevDn;
        double avgDevUp = 0.;
        if (noDevUp > 0)
            avgDevUp = totDevUp / noDevUp;
        yldPoint.forwardRate = analysisRate;
        yldPoint.dtForward = lookAheadList.get(noRates - 1).dtForward;
        yldPoint.spotUpSide = analysisRate + avgDevUp;
        return yldPoint;
    }

    class SortSpotCurveAsc implements Comparator<YieldPoint> {
        public int compare(YieldPoint yp1, YieldPoint yp2) {
            if (yp1.matLen > yp2.matLen)
                return 1;
            if (yp1.matLen < yp2.matLen)
                return -1;
            return 0;
        }

    }

    class SortForwardCurveAsc implements Comparator<YieldPoint> {
        public int compare(YieldPoint yp1, YieldPoint yp2) {
            if (yp1.lookAhead > yp2.lookAhead)
                return 1; // move down
            if (yp1.lookAhead < yp2.lookAhead)
                return -1;
            if (yp1.lookAhead == yp2.lookAhead) {
                if (yp1.matLen > yp2.matLen)
                    return 1;
                if (yp1.matLen < yp2.matLen)
                    return -1;
                if (yp1.matLen == yp2.matLen) {
                    if (yp1.dtForward.gtDate(yp2.dtForward))
                        return 1;
                    else if (yp1.dtForward.leDate(yp2.dtForward))
                        return -1;
                    return 0;
                }
                return 0;
            }
            return 0;
        }

    }

    // updates all yield curve files based on one years' worth of daily data
    public void updateUSTreasCurve(String treasCurveName, String strStart, String strEnd) {
        setFileNames(treasCurveName);
        removeCurve(strStart, strEnd, fileNamePar, "RF", "S&P", "AAA");
        removeCurve(strStart, strEnd, fileNameSpot, "RF", "S&P", "AAA");
        removeCurve(strStart, strEnd, fileNameSpotVol, "RF", "S&P", "AAA");
        removeCurve(strStart, strEnd, fileNameForward, "RF", "S&P", "AAA");
        removeCurve(strStart, strEnd, fileNameForwardVol, "RF", "S&P", "AAA");
        // updates the parcurve, spotcurve and forard curve at the same time
        loadTreasCurve(treasCurveName, "RF", "S&P", "AAA", true);
        updateSpotVolCurve(strStart, strEnd, analysisPeriod, yieldType, creditAgency, creditRating, true);
        updateForwardVolCurve(strStart, strEnd, analysisPeriod, yieldType, creditAgency, creditRating, true);

    }
}

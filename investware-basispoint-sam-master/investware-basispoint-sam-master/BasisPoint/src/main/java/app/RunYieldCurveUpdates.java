package app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class RunYieldCurveUpdates {

    public static void main(String[] args) {
        YieldCurveUpdates yieldCurveUpdates = new YieldCurveUpdates();
        if (args[0].equalsIgnoreCase("LOADTREASCURVES")) {
            // Example: LOADTREASCURVES C:\Users\alroi\Dropbox\InvestWare\BasisPoint RF S&P
            // AAA
            String rootDir = args[1];
            yieldCurveUpdates.setFileNames(rootDir);
            // yieldCurveUpdates.cmdLine.rootDirectory = rootDir;
            String pathParIn = rootDir + "\\USTreas\\";
            yieldCurveUpdates.dateFormat = "MM/DD/YYYY";
            String yieldType = args[2];
            String creditAgency = args[3];
            String creditRating = args[4];
            yieldCurveUpdates.loadTreasCurves(pathParIn, yieldType, creditAgency, creditRating);
        }
        if (args[0].equalsIgnoreCase("LOADTREASCURVE")) {
            // Example: LOADTREASCURVES C:\Users\alroi\Dropbox\InvestWare\BasisPoint RF S&P
            // AAA
            String rootDir = args[1];
            String treasFileName = args[2];
            yieldCurveUpdates.setFileNames(rootDir);
            String pathParIn = rootDir + "\\USTreas\\";
            yieldCurveUpdates.dateFormat = "MM/DD/YYYY";
            String yieldType = Codes.RISKFREECURVE;
            String creditAgency = "S&P";
            String creditRating = "AAA";
            String appendYn = args[3];
            if (appendYn.equals("Y"))
                yieldCurveUpdates.loadTreasCurve(treasFileName, yieldType, creditAgency, creditRating, true);
            else
                yieldCurveUpdates.loadTreasCurve(treasFileName, yieldType, creditAgency, creditRating, false);
        }

        if (args[0].equalsIgnoreCase("UPDATESPOTVOL")) {
            // Example: UPDATESPOTVOLS C:\Users\alroi\Dropbox\InvestWare\BasisPoint
            // 03/03/2023 RF S&P AAA Y
            String rootDir = args[1];
            yieldCurveUpdates.rootDir = rootDir;
            yieldCurveUpdates.fileNameSpot = rootDir + "\\yieldCurves\\spotCurves.csv";
            yieldCurveUpdates.fileNameSpotVol = rootDir + "\\yieldCurves\\spotCurveVols.csv";
            yieldCurveUpdates.dateFormat = "MM/DD/YYYY";
            String strStart = args[2];
            String strEnd = args[3];
            int analysisPeriod = 180;
            String yieldType = args[4];
            String creditAgency = args[5];
            String creditRating = args[6];
            String appendYn = args[7];
            boolean append = true;
            if (appendYn.equals("N"))
                append = false;
            yieldCurveUpdates.updateSpotVolCurve(strStart, strEnd, analysisPeriod, yieldType, creditAgency,
                    creditRating, append);
        }
        if (args[0].equalsIgnoreCase("UPDATEFORWARDVOLS")) {
            // Example: UPDATEFORWARDVOLS C:\Users\alroi\Dropbox\InvestWare\BasisPoint
            String rootDir = args[1];
            String strStart = args[2];
            String strEnd = args[3];
            int analysisPeriod = Integer.valueOf(args[4]);
            String yieldType = args[5];
            String creditAgency = args[6];
            String creditRating = args[7];
            boolean append = true;
            yieldCurveUpdates.rootDir = rootDir;
            yieldCurveUpdates.fileNameSpot = rootDir + "\\yieldCurves\\spotCurves.csv";
            yieldCurveUpdates.fileNameForward = rootDir + "\\yieldCurves\\forwardCurves.csv";
            yieldCurveUpdates.fileNameForwardVol = rootDir + "\\yieldCurves\\forwardCurveVols.csv";
            yieldCurveUpdates.dateFormat = "MM/DD/YYYY";
            yieldCurveUpdates.updateForwardVolCurve(strStart, strEnd, analysisPeriod, yieldType, creditAgency,
                    creditRating, append);
        }
        if (args[0].equalsIgnoreCase("UPDATEFORWARDVOL")) {
            String rootDir = args[1];
            yieldCurveUpdates.cmdLine.rootDirectory = rootDir;
            String strStart = args[2];
            String strEnd = args[3];
            int analysisPeriod = Integer.valueOf(args[4]);
            String yieldType = args[5];
            String creditAgency = args[6];
            String creditRating = args[7];
            String appendYn = args[8];
            boolean append = true;
            if (appendYn.equals('N'))
                append = false;
            yieldCurveUpdates.fileNameForward = rootDir + "\\yieldCurves\\forwardCurves.csv";
            yieldCurveUpdates.fileNameForwardVol = rootDir + "\\yieldCurves\\forwardCurveVols.csv";
            yieldCurveUpdates.dateFormat = "MM/DD/YYYY";
            yieldCurveUpdates.updateForwardVolCurve(strStart, strEnd, analysisPeriod, yieldType, creditAgency,
                    creditRating, append);
        }
        if (args[0].equalsIgnoreCase("UPDATETREASCURVE")) {
            yieldCurveUpdates.rootDir = args[1];
            String treasCurveName = args[2];
            String strStart = args[3];
            String strEnd = args[4];
            yieldCurveUpdates.updateUSTreasCurve(treasCurveName, strStart, strEnd);
        }
    }
}
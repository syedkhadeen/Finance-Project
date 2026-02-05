import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class YieldCurves {
    CommandLine cmdLine;
    Error error = new Error();
    DayCount dayCount;
    Utility utility = new Utility();
    YieldCurveParams yieldCurveParams;
    public ArrayList<YieldPoint> parCurve = new ArrayList<>();
    public ArrayList<YieldPoint> spotCurve = new ArrayList<>(); // this is the spot curve to use for discount cashfkow with settle date as a reference for maturity length
    public ArrayList<YieldPoint> forwardCurve = new ArrayList<>(); // this is the forweard rate curve at a future date that uses the future date as a reference for maturity length
    public HashMap<Long, YieldPoint> yieldPointHashMap = new HashMap<>(); // yield curve points
    public double[] d2YSpline;

    YieldCurves() {

    }

    YieldCurves(CommandLine cmdLine, Error error, DayCount dayCount, Utility utility) {
        this.cmdLine = cmdLine;
        this.error = error;
        this.dayCount = dayCount;
        this.utility = utility;
    }

    public YieldCurves(CommandLine cmdLine, Error error, DayCount dayCount, Utility utility, CreditAgencyRating creditAgencyRating) {
        this.cmdLine = cmdLine;
        this.error = error;
        this.dayCount = dayCount;
        this.utility = utility;
    }

    public YieldCurves(YieldCurveParams yldParams, ArrayList<YieldPoint> spotCurve) {
        this.yieldCurveParams = yldParams;
        this.spotCurve = spotCurve;
    }


    public void setYieldParams(YieldCurveParams yieldCurveParams) {
        this.yieldCurveParams = yieldCurveParams;
    }

    public void clearYieldCurve() {
        this.parCurve.clear();
        this.spotCurve.clear();
        this.forwardCurve.clear();
        this.yieldPointHashMap.clear();
        this.yieldCurveParams = new YieldCurveParams();
        this.yieldCurveParams.noPoints = 0;
        this.d2YSpline = new double[Limits.MAXYIELDPOINTS];
    }

    public ArrayList<YieldPoint> zeroCurve(int count) {
        ArrayList<YieldPoint> zeroCurve = new ArrayList<>();
        YieldPoint zeroPoint = new YieldPoint();
        int i = -1;
        while (++i < count) {
            zeroPoint.spotAvg = 0.;
            zeroPoint.creditRating = UnAvails.NOTAVAILSTRING;
            zeroPoint.creditAgency = UnAvails.NOTAVAILSTRING;
            zeroPoint.yieldType = UnAvails.NOTAVAILSTRING;
            zeroPoint.dtCurve = new Dates();
            zeroPoint.spotMax = 0.;
            zeroPoint.spotMin = 0;
            zeroPoint.spotStdDev = 0.;
            zeroPoint.spotUpSide = 0.;
            zeroPoint.spotDnSide = 0.;
            zeroPoint.dtAnalysisStart = new Dates();
            zeroPoint.spotRate = 0.;
            zeroPoint.forwardRate = 0.;
            zeroPoint.lookAhead = 0;
            zeroPoint.matLen = 0;
            zeroPoint.analysisDays = 0;
            zeroPoint.curveType = UnAvails.NOTAVAILSTRING;
            zeroPoint.matName = UnAvails.NOTAVAILSTRING;
            zeroCurve.add(i, new YieldPoint(zeroPoint));
        }
        return zeroCurve;
    }

    public String getMatLenName(int matlen) {
        String strMatLen = "";
        int wholeYrs = matlen / CalUnits.MONTHSPERYEAR;
        int wholeMon = matlen - CalUnits.MONTHSPERYEAR * wholeYrs;
        if (wholeYrs == 0) {
            //strMatLen = mon + "mo";
            strMatLen = wholeMon + "m";
            return strMatLen;
        }
        if (wholeMon == 0)
            strMatLen = wholeYrs + "y";
        else {
            strMatLen = wholeYrs + "y" + wholeMon + "m";
        }
        return strMatLen;
    }

    public double getCurveRate(int matLen, String ycForecastMethod, String yieldInterpMeth, int upDn) {
        if (ycForecastMethod.equals(CalcList.FORECASTSPOT)) {
            double rate = getSpotRate(matLen, upDn);
            return rate;
        }
        double rate = getForwardRate(matLen, upDn);
        return rate;
    }

    public double getSpotRate(int matLen, int uPdown) {
        double spotVol = 0.;
        int noVols = this.spotCurve.size();
        if (noVols == 0)
            return 0.;
        if (matLen <= this.spotCurve.get(0).matLen) {
            if (uPdown == CalcList.SPOTON)
                return this.spotCurve.get(0).spotRate;
            if (uPdown == CalcList.SPOTDN)
                return this.spotCurve.get(0).spotDnSide;
            else
                return this.spotCurve.get(0).spotUpSide;
        }
        if (matLen >= this.spotCurve.get(noVols - 1).matLen) {
            if (uPdown == CalcList.SPOTON)
                return this.spotCurve.get(noVols - 1).spotRate;
            if (uPdown == CalcList.SPOTDN)
                return this.spotCurve.get(noVols - 1).spotDnSide;
            else
                return this.spotCurve.get(noVols - 1).spotUpSide;
        }
        double leftSpotVol = 0.;
        double rightSpotVol = 0.;
        double rightSpotMat = 0.;
        double leftSpotMat = 0.;
        int volNo = -1;
        while (++volNo < noVols - 1) {
            if (spotCurve.get(volNo).matLen > matLen) {
                if (uPdown == CalcList.SPOTDN) {
                    leftSpotVol = spotCurve.get(volNo - 1).spotDnSide;
                    leftSpotMat = spotCurve.get(volNo - 1).matLen;
                    rightSpotVol = spotCurve.get(volNo).spotDnSide;
                    rightSpotMat = spotCurve.get(volNo).matLen;

                }
                if (uPdown == CalcList.SPOTUP) {
                    leftSpotVol = spotCurve.get(volNo - 1).spotUpSide;
                    leftSpotMat = spotCurve.get(volNo - 1).matLen;
                    rightSpotVol = spotCurve.get(volNo).spotUpSide;
                    rightSpotMat = spotCurve.get(volNo).matLen;

                }
                if (uPdown == CalcList.SPOTON) {
                    leftSpotVol = spotCurve.get(volNo - 1).spotRate;
                    leftSpotMat = spotCurve.get(volNo - 1).matLen;
                    rightSpotVol = spotCurve.get(volNo).spotRate;
                    rightSpotMat = spotCurve.get(volNo).matLen;
                }

                double totVolMat = rightSpotMat - leftSpotMat;
                double totVolVol = rightSpotVol - leftSpotVol;
                double rateOfChange = 0.;
                if (totVolMat != 0.) {
                    rateOfChange = totVolVol / totVolMat;
                    spotVol = leftSpotVol + rateOfChange * totVolMat;
                }
                return spotVol;
            }
        }
        return 0.;
    }

    public double getForwardRate(int matLen, int uPdown) {
        double spotRt = 0.;
        int noPoints = this.forwardCurve.size();
        if (noPoints == 0)
            return 0.;
        if (matLen <= this.forwardCurve.get(0).matLen) {
            if (uPdown == CalcList.SPOTON)
                return this.forwardCurve.get(0).spotRate;
            else if (uPdown == CalcList.SPOTDN)
                return this.forwardCurve.get(0).spotDnSide;
            else
                return this.forwardCurve.get(0).spotUpSide;
        }
        if (matLen >= this.forwardCurve.get(noPoints - 1).matLen) {
            if (uPdown == CalcList.SPOTDN)
                return this.forwardCurve.get(noPoints - 1).spotDnSide;
            else
                return this.forwardCurve.get(noPoints - 1).spotUpSide;
        }
        double leftSpotVol = 0.;
        double rightSpotVol = 0.;
        double rightSpotMat = 0.;
        double leftSpotMat = 0.;
        int volNo = -1;
        while (++volNo < noPoints - 1) {
            if (forwardCurve.get(volNo).matLen > matLen) {
                if (uPdown == CalcList.SPOTON) {
                    leftSpotVol = forwardCurve.get(volNo - 1).spotRate;
                    leftSpotMat = forwardCurve.get(volNo - 1).matLen;
                    rightSpotVol = forwardCurve.get(volNo).spotRate;
                    rightSpotMat = forwardCurve.get(volNo).matLen;
                }
                if (uPdown == CalcList.SPOTDN) {
                    leftSpotVol = forwardCurve.get(volNo - 1).spotDnSide;
                    leftSpotMat = forwardCurve.get(volNo - 1).matLen;
                    rightSpotVol = forwardCurve.get(volNo).spotDnSide;
                    rightSpotMat = forwardCurve.get(volNo).matLen;

                }
                if (uPdown == CalcList.SPOTUP) {
                    leftSpotVol = forwardCurve.get(volNo - 1).spotUpSide;
                    leftSpotMat = forwardCurve.get(volNo - 1).matLen;
                    rightSpotVol = forwardCurve.get(volNo).spotUpSide;
                    rightSpotMat = forwardCurve.get(volNo).matLen;

                }
                double totVolMat = rightSpotMat - leftSpotMat;
                double totVolVol = rightSpotVol - leftSpotVol;
                double rateOfChange = 0.;
                if (totVolMat != 0.)
                    rateOfChange = totVolVol / totVolMat;
                spotRt = leftSpotVol + (matLen - rightSpotMat) * rateOfChange;
                return spotRt;
            }
        }
        return 0.;
    }

    public double getCurveRate(int matLen, String foreCastMeth, int upDn) {
        double rate = 0.;
        if (foreCastMeth.equals(BondConsts.FORWARDCURVE))
            rate = getForwardRate(matLen, upDn);
        else
            rate = getSpotRate(matLen, upDn);
        return rate;
    }

    public ArrayList<YieldPoint> convertToSpotCurve(ArrayList<YieldPoint> parCurve, YieldCurveParams yldParams) {
        // get first
        ArrayList<YieldPoint> spotCurve = new ArrayList<>();
        ArrayList<YieldPoint> parCurveExt = new ArrayList<>();
        int spotInterval = yldParams.spotCurveInterval; // std length of interest payment for par bond
        parCurveExt = createParCurveExtended(parCurve, spotInterval);
        spotCurve = createSpotCurve(parCurveExt);
        return spotCurve;
    }

    private ArrayList<YieldPoint> createParCurveExtended(ArrayList<YieldPoint> parCurve, int spotInterval) {
        ArrayList<YieldPoint> parCurveExt = new ArrayList<>();
        int pNo = -1;
        int noExt = 0;
        YieldPoint spotPoint;
        Utility.DoubleRange matRange = new Utility.DoubleRange();
        int spotMat = spotInterval;
        int noPoints = parCurve.size();
        double parMat = 0;
        double maxSpotInterval = parCurve.get(noPoints - 1).matLen;
        pNo = 0;
        Dates dtStart = parCurve.get(0).dtCurve;
        while (spotMat <= maxSpotInterval && pNo < noPoints) {
            parMat = parCurve.get(pNo).matLen;
            spotPoint = new YieldPoint();
            if (parMat < spotInterval) {
                spotPoint.yieldType = this.yieldCurveParams.yieldType;
                spotPoint.creditAgency = this.yieldCurveParams.creditAgency;
                spotPoint.creditRating = this.yieldCurveParams.creditRating;
                spotPoint.matLen = parCurve.get(pNo).matLen;
                spotPoint.spotRate = parCurve.get(pNo).spotRate;
                spotPoint.dtCurve = parCurve.get(pNo).dtCurve;
                spotPoint.dtForward = parCurve.get(pNo).dtCurve;
                spotPoint.lookAhead = 0;
                spotPoint.matName = getMatLenName(spotPoint.matLen);
                spotPoint.curveType = BondConsts.PARCURVE;
                parCurveExt.add(noExt++, spotPoint);
            }
            if (parMat > spotMat) {
                matRange.rightPoint = parMat;
                matRange.rightValue = parCurve.get(pNo).spotRate;
                if (pNo == 0) {
                    matRange.leftPoint = UnAvails.NOTAVAILINT;
                    matRange.leftValue = matRange.rightValue;
                } else {
                    matRange.leftPoint = parCurve.get(pNo - 1).matLen;
                    matRange.leftValue = parCurve.get(pNo - 1).spotRate;
                }
                // Dates dtCurve = dayCount.time.addMonthsToDate(parCurve.get(0).dtCurve, spotMat, 'Y');
                Dates dtCurve = parCurve.get(pNo).dtCurve;
                spotPoint.lookAhead = 0;
                spotPoint.yieldType = this.yieldCurveParams.yieldType;
                spotPoint.creditAgency = this.yieldCurveParams.creditAgency;
                spotPoint.creditRating = this.yieldCurveParams.creditRating;
                spotPoint = calcYieldPoint(dtCurve, spotMat, matRange);
                spotPoint.curveType = BondConsts.PARCURVE;
                spotPoint.matName = getMatLenName(spotMat);
                parCurveExt.add(noExt++, spotPoint);
                spotMat += spotInterval;
            }
            if (parMat == spotMat) {
                spotPoint = parCurve.get(pNo);
                spotPoint.yieldType = this.yieldCurveParams.yieldType;
                spotPoint.creditAgency = this.yieldCurveParams.creditAgency;
                spotPoint.creditRating = this.yieldCurveParams.creditRating;
                spotPoint.curveType = BondConsts.PARCURVE;
                spotPoint.dtCurve = parCurve.get(pNo).dtCurve;
                spotPoint.dtForward = parCurve.get(pNo).dtCurve;
                spotPoint.lookAhead = 0;
                spotPoint.matName = getMatLenName((int) parMat);
                parCurveExt.add(noExt++, spotPoint);
                spotMat += spotInterval;
            }
            if (parMat < spotMat)
                ++pNo;
        }
        return parCurveExt;
    }

    private YieldPoint calcYieldPoint(Dates dtCurve, int spotMat, Utility.DoubleRange matRange) {
        YieldPoint yieldPoint = new YieldPoint();
        int matDiff = (int) (matRange.rightPoint - matRange.leftPoint);
        double yield = 0.;
        if (matDiff == 0) {
            yieldPoint.matLen = spotMat;
            yieldPoint.spotRate = matRange.rightValue;
            yieldPoint.dtCurve = dtCurve;
            yieldPoint.lookAhead = 0;
            yieldPoint.dtForward = dtCurve;
        } else {
            double ratePer = (matRange.rightValue - matRange.leftValue) / matDiff;
            yield = matRange.leftValue + ratePer * (spotMat - matRange.leftPoint);
            yieldPoint.matLen = spotMat;
            yieldPoint.spotRate = yield;
            yieldPoint.dtCurve = dtCurve;
            yieldPoint.lookAhead = 0;
            yieldPoint.dtForward = dtCurve;
        }
        return yieldPoint;
    }

    private ArrayList<YieldPoint> createSpotCurve(ArrayList<YieldPoint> parCurveExt) {
        ArrayList<YieldPoint> localCurve = new ArrayList<>();
        YieldPoint yieldPoint = new YieldPoint();
        int no = -1, spotNo = 0, noPoints = parCurveExt.size();
        int spotInterval = yieldCurveParams.spotCurveInterval;
        int spotMat = 0, spotIntervalNo = 0;
        double presValue = 0.;
        int freq = CalUnits.MONTHSPERYEAR / spotInterval;
        int matLen = 0;
        while (++no < noPoints) {
            matLen = parCurveExt.get(no).matLen;
            double parYield = parCurveExt.get(no).spotRate;
            Dates dtCurve = parCurveExt.get(no).dtCurve;
            if (matLen <= (double) spotInterval) {
                yieldPoint.yieldType = this.yieldCurveParams.yieldType;
                yieldPoint.creditAgency = this.yieldCurveParams.creditAgency;
                yieldPoint.creditRating = this.yieldCurveParams.creditRating;
                yieldPoint.matLen = matLen;
                yieldPoint.curveType = BondConsts.SPOTCURVE;
                yieldPoint.matName = getMatLenName(matLen);
                yieldPoint.spotRate = parYield;
                yieldPoint.yieldType = BondConsts.RISKFREECURVE;
                yieldPoint.dtCurve = dtCurve;
                yieldPoint.dtForward = dtCurve;
                yieldPoint.lookAhead = 0;
                spotIntervalNo = no;
                localCurve.add(spotNo++, new YieldPoint(yieldPoint));
            } else {
                presValue = calcCurvePresentValue(localCurve, spotIntervalNo, parYield);
                double ratio = (1. + .01 * parYield / freq) / (1. - presValue);
                matLen = parCurveExt.get(no).matLen;
                double noPeriods = matLen / (double) spotInterval;
                double spotYield = freq * (-1. + Math.pow(ratio, 1. / noPeriods));
                yieldPoint.yieldType = this.yieldCurveParams.yieldType;
                yieldPoint.creditAgency = this.yieldCurveParams.creditAgency;
                yieldPoint.creditRating = this.yieldCurveParams.creditRating;
                yieldPoint.yieldType = BondConsts.RISKFREECURVE;
                yieldPoint.spotRate = 100 * spotYield;
                yieldPoint.matLen = matLen;
                yieldPoint.matName = getMatLenName(matLen);
                yieldPoint.lookAhead = 0;
                yieldPoint.dtCurve = parCurveExt.get(no).dtCurve;
                yieldPoint.dtForward = parCurveExt.get(no).dtCurve;
                localCurve.add(spotNo++, new YieldPoint(yieldPoint));
            }
        }
        return localCurve;
    }

    private double calcCurvePresentValue(ArrayList<YieldPoint> spotCurve, int spotStartNo, double parYield) {
        int spotNo = spotStartNo - 1;
        double presValue = 0.;
        int noPoints = spotCurve.size();
        int spotInterval = yieldCurveParams.spotCurveInterval;
        int freq = CalUnits.MONTHSPERYEAR / spotInterval;
        double noPeriods;
        double spotYield;
        double discFac;
        while (++spotNo < noPoints) {
            noPeriods = spotCurve.get(spotNo).matLen / spotInterval;
            spotYield = .01 * spotCurve.get(spotNo).spotRate;
            discFac = 1. / Math.pow(1. + spotYield / freq, noPeriods);
            presValue += .01 * parYield * .5 * discFac;
        }
        return presValue;
    }


    private double getNextSpotRate(double presVal, double parRate, int parMatLen, int spotInterval) {
        // matFreq is in years and typically 6 months
        int payPerYear = CalUnits.MONTHSPERYEAR / spotInterval;
        int matPer = parMatLen / spotInterval;
        double frac = (1. + parRate) / (1. - presVal);
        double spotRt = payPerYear * (-1 + Math.pow(frac, 1. / matPer));
        return spotRt;
    }

    public Utility.DoubleRange getCurveRange(ArrayList<YieldPoint> yieldPoint, double tenor) {
        Utility.DoubleRange maturityRange = new Utility.DoubleRange();
        int noPoints = yieldPoint.size();
        int no = -1;
        double mat = 0;
        while (++no < noPoints) {
            // find next right boundary
            mat = yieldPoint.get(no).matLen;
            if (tenor > mat) {
                if (no < noPoints - 1) {
                    maturityRange.leftPoint = yieldPoint.get(no).matLen;
                    maturityRange.leftValue = yieldPoint.get(no).spotRate;
                    maturityRange.rightValue = yieldPoint.get(no + 1).spotRate;
                    maturityRange.rightPoint = yieldPoint.get(no + 1).matLen;
                    maturityRange.nextIdx = no;
                    return maturityRange;
                } else { // there is not right boundary
                    maturityRange.leftPoint = yieldPoint.get(no).matLen;
                    maturityRange.leftValue = yieldPoint.get(no).spotRate;
                    maturityRange.rightValue = yieldPoint.get(no).spotRate;
                    maturityRange.rightPoint = yieldPoint.get(no).matLen;
                    maturityRange.nextIdx = no;
                    return maturityRange;
                }
            }
            if (tenor == mat) {
                // par maturity same as next spot maturitu
                maturityRange.rightPoint = yieldPoint.get(no).matLen;
                maturityRange.rightValue = yieldPoint.get(no).spotRate;
                maturityRange.leftPoint = yieldPoint.get(no).matLen;
                maturityRange.leftValue = yieldPoint.get(no).spotRate;
                maturityRange.nextIdx = no;
                return maturityRange;
            }
        }  // no left boundary
        maturityRange.rightPoint = yieldPoint.get(no).matLen;
        maturityRange.rightValue = yieldPoint.get(no).spotRate;
        maturityRange.leftPoint = maturityRange.rightPoint;
        maturityRange.leftValue = maturityRange.rightValue;
        maturityRange.nextIdx = no;
        return maturityRange;
    }

    public double getCurveRate(double matLen, String forecastMeth, String interpMethod) {
        if (forecastMeth.toUpperCase().equals(CalcList.FORECASTSPOT)) {
            double rate = getSpotRate(matLen, interpMethod);
            return rate;
        }
        double rate = getForwardRate(matLen, interpMethod);
        return rate;
    }

    public double getSpotRate(double matLen, String yieldCurveInterpMeth) {
        double spotRt = 0.;
        if (this.spotCurve.isEmpty())
            return spotRt;
        if (yieldCurveInterpMeth.equals(BondConsts.LINEARINTERP))
            spotRt = getSpotRateLinear(matLen);
        else
            spotRt = getSpotRateCubic(matLen);
        return spotRt;
    }

    public double getSpotRate(double matLen, int upDn, String yieldCurveInterpMeth) {
        double spotRt = 0.;
        if (this.spotCurve.isEmpty())
            return spotRt;
        if (yieldCurveInterpMeth.equals(BondConsts.LINEARINTERP))
            spotRt = getSpotRateLinear(matLen);
        else
            spotRt = getSpotRateCubic(matLen);
        return spotRt;
    }

    public double getForwardRate(double matLen, String yieldCurveInterpMeth) {
        double spotRt = 0.;
        if (this.spotCurve.isEmpty())
            return spotRt;
        if (yieldCurveInterpMeth.equals(CalcEnums.LINEAR))
            spotRt = getForwardRateLinear(matLen);
        else
            // getSpotRateCubicSpline
            spotRt = getForwardRateCubic(matLen);
        return spotRt;
    }


    // public boolean generateYieldCurveSpotList
// boolean isOk = convertParCurveToSpotCurve(Dates dtCurveStart, ArrayList<YieldCurve> parYieldCurve, int freq)
    public double getSpotRateLinear(double matLen) {
        // add cubic spline method
        double fixingRate = 0.;
        double leftMatLen = 0., leftRate = 0.;
        double rightMatLen = 0., rightRate = 0.;
        int rNo = -1;
        int noPoints = spotCurve.size();
        while (++rNo < noPoints) {
            if (spotCurve.get(rNo).matLen > matLen) {
                if (rNo == 0)
                    return spotCurve.get(rNo).spotRate;
                rightMatLen = spotCurve.get(rNo).matLen;
                rightRate = spotCurve.get(rNo).spotRate;
                leftMatLen = spotCurve.get(rNo - 1).matLen;
                leftRate = spotCurve.get(rNo - 1).spotRate;
                double spotRt = utility.interpDouble(matLen, leftMatLen, leftRate, rightMatLen, rightRate);
                return spotRt;
            }
        }
        // assume flat yield curve after last data point
        return spotCurve.get(noPoints - 1).spotRate;
    }

    public double getSpotRateCubic(double matLen) {
        // double spotRt = utility.getSplineValue(dSpotMatLen, spotRate, spotD2Yield, noRates, matLen);
        double spotRt = getSplineValue(matLen);
        return spotRt;
    }

    public double getForwardRateCubic(double matLen) {
        //double forwardRt = utility.getSplineValue(forwardMatlen, forwardRate, forwardD2Yield, noRates, matLen);
        double forwardRt = getSplineValue(matLen);
        return forwardRt;
    }

    public double getForwardRateLinear(double matLen) {
        // add cubic spline method
        double fixingRate = 0.;
        double leftMatLen = 0., leftRate = 0.;
        double rightMatLen = 0., rightRate = 0.;
        int rNo = -1;
        int noPoints = forwardCurve.size();
        while (++rNo < noPoints) {
            if (forwardCurve.get(rNo).matLen > matLen) {
                if (rNo == 0)
                    return spotCurve.get(rNo).spotRate;
                rightMatLen = forwardCurve.get(rNo).matLen;
                rightRate = forwardCurve.get(rNo).spotRate;
                leftMatLen = forwardCurve.get(rNo - 1).matLen;
                leftRate = forwardCurve.get(rNo - 1).spotRate;
                double spotRt = utility.interpDouble(matLen, leftMatLen, leftRate, rightMatLen, rightRate);
                return spotRt;
            }
        }
        // assume flat yield curve after last data point
        return forwardCurve.get(noPoints - 1).spotRate;
    }

    public double getForwardResetRate(Dates dtCurrent, Dates dtReset, SchedRule resetScheduleRule) {
        String yieldCurveInterpMeth = yieldCurveParams.yieldCurveInterpMeth;
        double matLen1 = dayCount.time.getMonthsBetween(dtCurrent, dtReset, false);
        double spot1Rt = getSpotRate(matLen1, yieldCurveInterpMeth);
        Dates dtSpot1 = dtReset;
        int resetPeriod = CalUnits.MONTHSPERYEAR / resetScheduleRule.compoundFreq;
        double matLen2 = matLen1 + resetPeriod;
        Dates dtSpot2 = dayCount.time.addYearsToDate(dtCurrent, matLen2);
        double spot2Rt = getSpotRate(matLen2, yieldCurveInterpMeth);
        double resetRt = getForwardRate(spot1Rt, spot2Rt, matLen1, matLen2);
        return resetRt;

    }

    // Settle date is usually date of the current curve
// dtForwardCurve is the future start date of the forward curve as seen from dtCurve - current point in time
// matlen1 is current origin of the foreward curve as seen from dtCurrent
// matlen is maturity length for the yield on as seen from dtForewardCurve
    public double getForwardRate(String yieldCurveInterpMeth, Dates dtBase, Dates dtForwardCurve, double matLen) {
        double matLen1 = dayCount.time.getMonthsBetween(dtBase, dtForwardCurve, false);
        double spot1Rt = getSpotRate(matLen1, yieldCurveInterpMeth);
        double matLen2 = matLen1 + matLen;
        double spot2Rt = getSpotRate(matLen2, yieldCurveInterpMeth);
        double forwardRt = getForwardRate(spot1Rt, spot2Rt, matLen1, matLen2);
        return forwardRt;

    }


    public double getForwardRate(double spotRt1, double spotRt2, double matlen1, double matlen2) {
        double forwardRt = 0.;
        double matlen12 = matlen2 - matlen1;
        int spotCurveInterval = yieldCurveParams.spotCurveInterval;
        double noPer1 = matlen1 / spotCurveInterval;
        double noper2 = matlen2 / spotCurveInterval;
        double noPer12 = matlen12 / spotCurveInterval;
        double freq = CalUnits.MONTHSPERYEAR / spotCurveInterval;
        double fValue1 = Math.pow(1. + spotRt1 / freq, noPer1);
        double fValue2 = Math.pow(1. + spotRt2 / freq, noper2);
        if (fValue1 == 0.0) {
            error.setError(getClass().getName(), "invalid spot rate found for fixing curve in  forward rate estimation- would cause divide by zero");
            return 0.;
        }
        forwardRt = freq * (-1. + Math.pow(fValue2 / fValue1, 1. / noPer12));
        if (forwardRt < 0.) {
            error.setWarning(getClass().getName(), "negative forward rate found for fixing curve");
        }
        return forwardRt;
    }

    public int compareYieldpoint(YieldPoint o1, YieldPoint o2) {
        if (o1.matLen > o2.matLen)
            return 1;
        if (o1.matLen == o2.matLen)
            return 0;
        if (o1.matLen < o2.matLen)
            return -1;
        return 0;
    }


    /*
        Calculates the set of forward rates term structure on a given future date
        the values are the rate at different maturity terms on the the future date

     */

    public ArrayList<YieldPoint> calcforwardCurve(ArrayList<YieldPoint> spotCurve, YieldCurveParams yldParams, int lookAhead) {
        ArrayList<YieldPoint> forwardCurve = new ArrayList<>();
        YieldPoint yieldPoint = new YieldPoint();
        int noSpots = spotCurve.size();
        if (noSpots == 0) {
            return forwardCurve;
        }
        String yieldCurveMeth = yldParams.yieldCurveInterpMeth;
        double maxSpotMat = spotCurve.get(noSpots - 1).matLen;
        int ip = -1;
        int matLen = 0;
        Dates dtForward = new Dates();
        while (++ip < noSpots) {
            //int matLen = baseCurve.get(ip).matlen;
            matLen += yldParams.spotCurveInterval;
            if (matLen > maxSpotMat)
                break;
            yieldPoint.creditAgency = yldParams.creditAgency;
            yieldPoint.creditRating = yldParams.creditRating;
            yieldPoint.yieldType = yldParams.yieldType;
            yieldPoint.dtAnalysisStart = yldParams.dtAnalysis;
            yieldPoint.analysisDays = yldParams.analysisPeriod;
            yieldPoint.dtCurve = yldParams.dtCurve;
            yieldPoint.curveType = BondConsts.FORWARDCURVE;
            yieldPoint.matLen = matLen;
            d2YSpline = setCubicSpline(1.0e30, 1.0e30);
            dtForward = dayCount.time.addMonthsToDate(yldParams.dtCurve, lookAhead, 'N');
            yieldPoint.forwardRate = getForwardRate(yieldCurveMeth, yldParams.dtCurve, dtForward, matLen);
            yieldPoint.spotRate = yieldPoint.forwardRate;
            yieldPoint.lookAhead = lookAhead;
            yieldPoint.dtForward = dtForward;
            yieldPoint.matName = this.getMatLenName(matLen);
            yieldPoint.spotDnSide = UnAvails.NOTAVAILDOUBLE;
            yieldPoint.spotUpSide = UnAvails.NOTAVAILDOUBLE;
            yieldPoint.spotStdDev = UnAvails.NOTAVAILDOUBLE;
            forwardCurve.add(ip, new YieldPoint(yieldPoint));
        }

        return forwardCurve;
    }

    // user never enteres the forward rate
    public ArrayList<YieldPoint> calcforwardCurve(ArrayList<YieldPoint> allForwardCurve, ArrayList<YieldPoint> spotCurve, YieldCurveParams yldParams, int lookAhead) {
        ArrayList<YieldPoint> forwardCurve = new ArrayList<>();
        YieldPoint yieldPoint = new YieldPoint();
        int noSpots = spotCurve.size();
        if (noSpots == 0) {
            return forwardCurve;
        }
        String yieldCurveMeth = yldParams.yieldCurveInterpMeth;
        double maxSpotMat = spotCurve.get(noSpots - 1).matLen;
        int ip = -1;
        int matLen = 0;
        Dates dtForward = new Dates();
        while (++ip < noSpots) {
            //int matLen = baseCurve.get(ip).matlen;
            matLen += yldParams.spotCurveInterval;
            if (matLen > maxSpotMat)
                break;
            yieldPoint.creditAgency = yldParams.creditAgency;
            yieldPoint.creditRating = yldParams.creditRating;
            yieldPoint.yieldType = yldParams.yieldType;
            yieldPoint.dtAnalysisStart = yldParams.dtAnalysis;
            yieldPoint.analysisDays = yldParams.analysisPeriod;
            yieldPoint.dtCurve = yldParams.dtCurve;
            yieldPoint.curveType = BondConsts.FORWARDCURVE;
            yieldPoint.matLen = matLen;
            d2YSpline = setCubicSpline(1.0e30, 1.0e30);
            dtForward = dayCount.time.addMonthsToDate(yldParams.dtCurve, lookAhead, 'N');
            yieldPoint.forwardRate = getForwardRate(yieldCurveMeth, yldParams.dtCurve, dtForward, matLen);
            yieldPoint.spotRate = yieldPoint.forwardRate;
            yieldPoint.lookAhead = lookAhead;
            yieldPoint.dtForward = dtForward;
            yieldPoint.matName = this.getMatName(matLen);
            yieldPoint.spotDnSide = getForwardDnSide(allForwardCurve, yldParams, matLen); // get the forward vols from user or from recent loaded
            yieldPoint.spotUpSide = getForwardUpSide(allForwardCurve, yldParams, matLen);
            yieldPoint.spotStdDev = getForwardStdDev(allForwardCurve, yldParams, matLen);
            forwardCurve.add(ip, new YieldPoint(yieldPoint));
        }

        return forwardCurve;
    }

    private double getForwardDnSide(ArrayList<YieldPoint> allForwardCurve, YieldCurveParams yldParams, int matLen) {
        int no = -1;
        while (++no < allForwardCurve.size()) {
            if (matLen == allForwardCurve.get(no).matLen) {
                if (yldParams.creditAgency.equals(allForwardCurve.get(no).creditAgency))
                    if (yldParams.creditRating.equals(allForwardCurve.get(no).creditRating))
                        return allForwardCurve.get(no).spotDnSide;
            }
        }
        return UnAvails.NOTAVAILDOUBLE;
    }

    private double getForwardUpSide(ArrayList<YieldPoint> allForwardCurve, YieldCurveParams yldParams, int matLen) {
        int no = -1;
        while (++no < allForwardCurve.size()) {
            if (matLen == allForwardCurve.get(no).matLen) {
                if (yldParams.creditAgency.equals(allForwardCurve.get(no).creditAgency))
                    if (yldParams.creditRating.equals(allForwardCurve.get(no).creditRating))
                        return allForwardCurve.get(no).spotUpSide;
            }
        }
        return UnAvails.NOTAVAILDOUBLE;
    }

    private double getForwardStdDev(ArrayList<YieldPoint> allForwardCurve, YieldCurveParams yldParams, int matLen) {
        int no = -1;
        while (++no < allForwardCurve.size()) {
            if (matLen == allForwardCurve.get(no).matLen) {
                if (yldParams.creditAgency.equals(allForwardCurve.get(no).creditAgency))
                    if (yldParams.creditRating.equals(allForwardCurve.get(no).creditRating))
                        return allForwardCurve.get(no).spotStdDev;
            }
        }
        return UnAvails.NOTAVAILDOUBLE;
    }


    public boolean sortSpotCurve(ArrayList<YieldPoint> spotCurve) {
        Collections.sort(spotCurve, new Comparator<YieldPoint>() {
            @Override
            public int compare(YieldPoint s1, YieldPoint s2) {
                if (s1.matLen > s2.matLen)
                    return 1;
                if (s1.matLen == s2.matLen)
                    return 0;
                if (s1.matLen < s2.matLen)
                    return -1;
                return 0;
            }
        });
        return true;
    }

    private double getPeriodSpotRate(ArrayList<YieldPoint> todaySpots, double matlength) {
        int noSpots = todaySpots.size();
        int np = -1;
        while (++np < noSpots) {
            if (Math.abs(matlength - todaySpots.get(np).matLen) < Limits.DOUBLEEQUALS)
                return todaySpots.get(np).spotRate;
        }
        String errmsg = "Application error - cannot find spot rate for par bond with maturity of " + matlength + " months";
        return 0.;
    }

    public String getMatName(ArrayList<MatCurveStructure> matCurveStructure, double matLen) {
        int noMats = matCurveStructure.size();
        int matNo = -1;
        int iMatLen = (int) matLen;
        while (++matNo < noMats) {
            if (matLen == matCurveStructure.get(matNo).matLen)
                return matCurveStructure.get(matNo).matName;
        }
        return "?";
    }

    // get all yiled curve points for given credit agency and rating for all dates in maturity range of a bond
    public ArrayList<YieldPoint> getFixingSpotCurve(YieldCurveParams yldParams) {
        ArrayList<YieldPoint> bondSpotCurve = new ArrayList<>();
        ArrayList<YieldPoint> nearByCurve = new ArrayList<>();
        YieldPoint yieldPoint = new YieldPoint();
        Dates dtBefore = dayCount.time.addDaysToDate(yldParams.dtCurve, -Defaults.SPOTNEARBYDAYS, 'N');
        Dates dtAfter = dayCount.time.addDaysToDate(yldParams.dtCurve, Defaults.SPOTNEARBYDAYS, 'N');
        Dates dtSpot;
        Dates dtLastNearby = dtBefore;
        int no = -1, noSpots = 0, noNearby = 0;
        while (++no < spotCurve.size()) {
            dtSpot = spotCurve.get(no).dtCurve;
            if (spotCurve.get(no).creditAgency.equals(yldParams.benchmark)) {
                if (spotCurve.get(no).yieldType.equals(yldParams.yieldType)) {
                    if (dtSpot.eqDate(yldParams.dtCurve)) {
                        bondSpotCurve.add(noSpots++, spotCurve.get(no));
                        yldParams.lastSpotRecNo = no;
                    } else { // get curves nearby
                        if (dtSpot.geDate(dtBefore) && dtSpot.leDate(dtAfter)) {
                            nearByCurve.add(noNearby++, spotCurve.get(no));
                            if (dtSpot.gtDate(dtSpot))
                                dtLastNearby = dtSpot;
                            yldParams.lastSpotRecNo = no;
                        }
                    }
                }

            }
        }
        if (bondSpotCurve.isEmpty()) {
            if (noNearby > 0) {
                no = -1;
                int nl = 0;
                while (++no < noNearby) {
                    if (dtLastNearby.eqDate(nearByCurve.get(no).dtCurve)) {
                        bondSpotCurve.add(nl++, nearByCurve.get(no));
                    }
                }
                return bondSpotCurve;
            } else {
                String errmsg = "No spot yield curve found on " + yldParams.dtCurve.getDateString() + " with benchmark " + yldParams.benchmark;
                error.setError(getClass().getName(), errmsg);
                return bondSpotCurve;
            }
        }
        return bondSpotCurve;
    }

    public ArrayList<YieldPoint> getDiscSpotCurve(YieldCurveParams yldParams) {
        ArrayList<YieldPoint> bondSpotCurve = new ArrayList<>();
        ArrayList<YieldPoint> nearByCurve = new ArrayList<>();
        YieldPoint yieldPoint = new YieldPoint();
        Dates dtBefore = dayCount.time.addDaysToDate(yldParams.dtCurve, -Defaults.SPOTNEARBYDAYS, 'N');
        Dates dtAfter = dayCount.time.addDaysToDate(yldParams.dtCurve, Defaults.SPOTNEARBYDAYS, 'N');
        Dates dtSpot;
        Dates dtLastNearby = dtBefore;
        String creditAgency, creditRating, yieldType;
        int no = -1, noSpots = 0, noNearby = 0;

        while (++no < spotCurve.size()) {
            dtSpot = spotCurve.get(no).dtCurve;
            creditAgency = spotCurve.get(no).creditAgency;
            creditRating = spotCurve.get(no).creditRating;
            yieldType = spotCurve.get(no).yieldType;
            if(creditAgency.equalsIgnoreCase(yldParams.creditAgency) && creditRating.equalsIgnoreCase(yldParams.creditRating)) {
                if (yieldType.equalsIgnoreCase(yldParams.yieldType)) {
                    if (dtSpot.eqDate(yldParams.dtCurve)) {
                        bondSpotCurve.add(noSpots++, spotCurve.get(no));
                        yldParams.lastSpotRecNo = no;
                    } else { // get curves nearby
                        if (dtSpot.geDate(dtBefore) && dtSpot.leDate(dtAfter)) {
                            nearByCurve.add(noNearby++, spotCurve.get(no));
                            if (dtSpot.gtDate(dtSpot))
                                dtLastNearby = dtSpot;
                            yldParams.lastSpotRecNo = no;
                        }
                    }
                }

            }
        }
        if (bondSpotCurve.isEmpty()) {
            if (noNearby > 0) {
                no = -1;
                int nl = 0;
                while (++no < noNearby) {
                    if (dtLastNearby.eqDate(nearByCurve.get(no).dtCurve)) {
                        bondSpotCurve.add(nl++, nearByCurve.get(no));
                    }
                }
                return bondSpotCurve;
            }
            if (yldParams.yieldType.equalsIgnoreCase(BondConsts.RISKFREECURVE)) {
                String errmsg = "No spot yield curve found on " + yldParams.dtCurve.getDateString() + " with credit agency " + yldParams.creditAgency + " and credit rating " + yldParams.creditRating;
                error.setError(getClass().getName(), errmsg);
                return bondSpotCurve;
            }
        }
        return bondSpotCurve;
    }

    public ArrayList<YieldPoint> setDiscSpotCurve(ArrayList<YieldPoint> baseCurve, YieldCurveParams yldParams) {
        ArrayList<YieldPoint> spotCurve;
        if (baseCurve.size() == 0) {
            spotCurve = new ArrayList<YieldPoint>();
            return spotCurve;
        }
        if (yldParams.isParYn == 'Y') {
            parCurve = baseCurve;
            spotCurve = convertToSpotCurve(parCurve, yldParams);
        } else {
            spotCurve = createParCurveExtended(parCurve, yldParams.spotCurveInterval);
        }
        return spotCurve;
    }


    public ArrayList<YieldPoint> setBaseCurve(YieldCurveParams yldParams) {
        ArrayList<YieldPoint> baseCurve = new ArrayList<>();
        YieldPoint yieldPoint = new YieldPoint();
        int no = -1;
        while (++no < yldParams.noPoints) {
            yieldPoint.matLen = yldParams.tenor[no];
            yieldPoint.spotRate = yldParams.rt[no];
            yieldPoint.yieldType = yldParams.yieldType;
            if (yldParams.isParYn == 'Y')
                yieldPoint.curveType = BondConsts.PARCURVE;
            else
                yieldPoint.curveType = BondConsts.SPOTCURVE;
            yieldPoint.creditAgency = yldParams.creditAgency;
            yieldPoint.creditRating = yldParams.creditRating;
            yieldPoint.dtCurve = yldParams.dtCurve;
            yieldPoint.dtForward = yldParams.dtForward;
            yieldPoint.spotAvg = yldParams.rtAvg[no];
            yieldPoint.matName = getMatLenName(yieldPoint.matLen);
            yieldPoint.spotDnSide = yldParams.rtDn[no];
            yieldPoint.spotUpSide = yldParams.rtUp[no];
            yieldPoint.spotStdDev = yldParams.stdDev[no];
            yieldPoint.spotMin = yldParams.rtMin[no];
            yieldPoint.spotMax = yldParams.rtMax[no];
            baseCurve.add(no, new YieldPoint(yieldPoint));
        }
        return baseCurve;
    }

    private ArrayList<YieldPoint> assignSpotCurve(ArrayList<YieldPoint> parCurve, YieldCurveParams yldParams) {
        ArrayList<YieldPoint> spotCurve = new ArrayList<>();
        YieldPoint yieldPoint = new YieldPoint();
        int no = -1;
        while (++no < parCurve.size()) {
            yieldPoint.spotRate = parCurve.get(no).spotRate;
            yieldPoint.matLen = parCurve.get(no).matLen;
            yieldPoint.dtCurve = parCurve.get(no).dtCurve;
            yieldPoint.dtForward = parCurve.get(no).dtForward;
            yieldPoint.lookAhead = 0;
            yieldPoint.creditAgency = UnAvails.NOTAVAILSTRING;
            yieldPoint.creditRating = UnAvails.NOTAVAILSTRING;
            yieldPoint.forwardRate = parCurve.get(no).spotRate;
            spotCurve.add(no, new YieldPoint(yieldPoint));
        }
        return spotCurve;
    }

    public ArrayList<YieldPoint> readFixingCurve(String fileNameFixingCurve, String benchmark) {
        FileReader curveReader;
        ArrayList<YieldPoint> fixingCurve = new ArrayList<>();
        YieldPoint YieldPoint = new YieldPoint();
        try {
            curveReader = new FileReader(fileNameFixingCurve);
        } catch (Exception ex) {
            String errmsg = "Cannot open spot rate fixing file " + fileNameFixingCurve;
            error.setError(getClass().getName(), errmsg);
            return fixingCurve;
        }
        BufferedReader bufferedReader = new BufferedReader(curveReader);
        String strLine;
        int noParts = 12;
        int recNo = 0;
        String[] strParts = new String[noParts];
        try {
            strLine = bufferedReader.readLine(); // get the header
        } catch (Exception ex) {
            String errmsg = "Cannot read a line in spot rate fixing file " + fileNameFixingCurve;
            error.setError(getClass().getName(), errmsg);
            return fixingCurve;
        }
        try {
            while (true) {
                strLine = bufferedReader.readLine();
                int i = 0;
                if (strLine == null)
                    break;
                double scale = .01;
                strParts = strLine.split(",");
                String strBenchmark = strParts[i++].toUpperCase();
                String strSpotDate = strParts[i++];
                if (strBenchmark.equals(benchmark.toUpperCase())) {
                    String strTenor = strParts[i++];
                    String strSpotRt = strParts[i++];
                    int tenor = Integer.valueOf(strTenor);
                    double spotRt = scale * Double.valueOf(strSpotRt);
                    int lookAhead = 0;
                    YieldPoint.matLen = tenor;
                    YieldPoint.spotRate = spotRt;
                    YieldPoint.lookAhead = 0;
                    MatCurveStructure matCurveStructure = new MatCurveStructure();
                    YieldPoint.matName = getMatName(matCurveStructure.matCurveStructureList, tenor);
                    fixingCurve.add(recNo++, new YieldPoint(YieldPoint));

                }
            }
        } catch (Exception ex) {
            String errmsg = "Cannot read a line in spot rate fixing file " + fileNameFixingCurve;
            error.setError(getClass().getName(), errmsg);
            return fixingCurve;
        }
        try {
            curveReader.close();
            bufferedReader.close();
        } catch (Exception ex) {
            String errmsg = "Cannot close fileReader or bufferedReader for file " + fileNameFixingCurve;
            error.setError(getClass().getName(), errmsg);
            return fixingCurve;
        }
        if (recNo == 0) {
            String errmsg = "No spot yield curve found with credit agency + bon" + fileNameFixingCurve;
            error.setError(getClass().getName(), errmsg);
            return fixingCurve;
        }
        return fixingCurve;
    }

    public String outputSpotCurveConsole(String isJSON) {
        String strCurve = "", pDel = "", cDel = "";
        String strRate = "", strTenor = "";
        String strDn = "", strUp = "";
        String qt = "\"";
        pDel = ":";
        cDel = ",";
        if (isJSON.equals("N")) {
            qt = "";
            pDel = "=";
            cDel = ";";
        }
        strRate = qt + "discRt" + qt + pDel + qt + "[";
        strTenor = qt + "discTenor" + qt + pDel + qt + "[";
        strDn = qt + "discRtDn" + qt + pDel + qt + "[";
        strUp = qt + "discRtUp" + qt + pDel+ qt + "[";
        int no = -1, count = spotCurve.size();
        double ds = 100.;
        double dVal = 0.;
        while (++no < count - 1) {
            dVal = ds * this.spotCurve.get(no).spotRate;
            strRate += String.format("%.3f", dVal) + ",";
            strTenor += qt + getMatName(this.spotCurve.get(no).matLen) + qt + cDel;
            dVal = ds * this.spotCurve.get(no).spotDnSide;
            strDn += String.format("%.3f", dVal) + cDel;
            dVal = ds * this.spotCurve.get(no).spotUpSide;
            strUp += String.format("%.3f", dVal) + cDel;

        }
        dVal = ds * this.spotCurve.get(no).spotRate;
        strRate += String.format("%.3f", dVal) + "]" + cDel;
        strTenor += qt + getMatName(this.spotCurve.get(no).matLen) + qt + cDel;
        dVal = ds * this.spotCurve.get(no).spotDnSide;
        strDn += String.format("%.3f", dVal) + "]" + cDel;
        dVal = ds * this.spotCurve.get(no).spotUpSide;
        strUp += String.format("%.3f", dVal) + "]";
        strCurve = strRate + strTenor + strDn + strUp;
        return strCurve;
    }

    public String outputSpotCurveFile(String format) {
        String strFile = "";
        String sp = "   ";
        if (format.equals("CSV"))
            sp = ",";
        String h1 = "Tenor(mo)" + sp;
        String l1 = "---------" + sp;
        String h2 = "Rate     " + sp;
        String l2 = "---------" + sp;
        String h3 = "DnSide Rate" + sp;
        String l3 = "-----------" + sp;
        String h4 = "UpSide Rate";
        String l4 = "-----------";

        int no = -1;
        strFile = h1 + h2 + h3 + h4 + "\n";
        strFile += l1 + l2 + l3 + l4 + "\n";
        double dVal = 0., ds = 100.;
        while (++no < this.spotCurve.size()) {
            String sItem = getMatName(this.spotCurve.get(no).matLen);
            strFile += String.format("%9s",sItem) + sp;

            dVal = ds * this.spotCurve.get(no).spotRate;
            sItem = String.format("%9.3f", dVal) + sp;
            strFile += sItem;

            dVal = ds * this.spotCurve.get(no).spotDnSide;
            sItem = String.format("%11.3f", dVal) + sp;
            strFile += sItem;

            dVal = ds * this.spotCurve.get(no).spotUpSide;
            sItem = String.format("%11.3f", dVal) + sp;
            strFile += sItem + "\n";
        }
        // cfrFile
        return strFile;
    }


    public double[] setCubicSpline(double yp1, double ypn) {
        // x1 and y1 have data starting at index = 1
        int nc = -1;
        int noPoints = this.spotCurve.size();

        if (noPoints == 0) {
            return null;
        }

        double[] x1 = new double[noPoints + 1];
        double[] y1 = new double[noPoints + 1];
        while (++nc < noPoints) {
            x1[nc] = (double) spotCurve.get(nc).matLen;
            y1[nc] = spotCurve.get(nc).spotRate;
        }
        int n = noPoints;

        double[] y2 = new double[n + 1]; // second derivative
        double[] u = new double[n];

        double[] x = new double[n + 1];
        double[] y = new double[n + 1];
        int i = 0;
        while (++i <= n) {
            x[i] = x1[i - 1];
            y[i] = y1[i - 1];
        }
        if (yp1 > .99e30)
            y2[1] = u[1] = 0.;
        else {
            y2[1] = -.5;
            u[1] = (3.0 / (x[2] - x[1])) * ((y[2] - y[1]) / (x[2] - x[1]) - yp1);
        }

        for (i = 2; i <= n - 1; ++i) {
            double sig = (x[i] - x[i - 1]) / (x[i + 1] - x[i - 1]);
            double p = sig * y2[i - 1] + 2.0;
            y2[i] = (sig - 1.0) / p;
            u[i] = (y[i + 1] - y[i]) / (x[i + 1] - x[i]) - (y[i] - y[i - 1]) / (x[i] - x[i - 1]);
            u[i] = (6.0 * u[i] / (x[i + 1] - x[i - 1]) - sig * u[i - 1]) / p;
        }
        double un = 0., qn = 0.;
        if (ypn > 0.99e30)
            qn = un = 0.0;
        else {
            qn = .5;
            un = (3.0 / (x[n] - x[n - 1])) * (ypn - (y[n] - y[n - 1]) / (x[n] - x[n - 1]));
        }
        y2[n] = (un - qn * u[n - 1]) / (qn * y2[n - 1] + 1.0);
        int k;
        for (k = n - 1; k >= 1; k--)
            y2[k] = y2[k] * y2[k + 1] + u[k];
        double[] y20 = new double[n + 1];
        i = -1;
        while (++i < n)
            y20[i] = y2[i + 1];
        return y20;

    }

    public double getSplineValue(double x) {
        // xa,ya, y2a have data start at inde1

        int noPoints = this.spotCurve.size();
        double[] xa = new double[noPoints + 1];
        double[] ya = new double[noPoints + 1];
        double[] y2a = new double[noPoints + 1];

        int nc = -1;

        double[] xa1 = new double[noPoints + 1];
        double[] ya1 = new double[noPoints + 1];
        double[] y2a1 = new double[noPoints + 1];
        while (++nc < noPoints) {
            xa1[nc] = (double) spotCurve.get(nc).matLen;
            ya1[nc] = spotCurve.get(nc).spotRate;
            y2a[nc] = this.d2YSpline[nc];
        }
        int n = noPoints;

        int i = 0;
        while (++i <= n) {
            xa[i] = xa1[i - 1];
            ya[i] = ya1[i - 1];
            y2a[i] = y2a1[i - 1];
        }
        double y = 0.; // cubic spline interpolated value
        int klo, khi, k;
        double h, b, a;
        klo = 1;
        khi = n;
        while (khi - klo > 1) {
            // k = (khi+klo) >> 1;
            k = (khi + klo) / 2;
            if (xa[k] > x)
                khi = k;
            else
                klo = k;
        }
        h = xa[khi] - xa[klo];
        if (h == 0.) {
            String errmsg = "Invalid yield curve detected";
            error.setError(getClass().getName(), errmsg);
            return 0.;
        }
        a = (xa[khi] - x) / h;
        b = (x - xa[klo]) / h;
        y = a * ya[klo] + b * ya[khi] + ((a * a * a - a) * y2a[klo] + (b * b * b - b) * y2a[khi]) * (h * h) / 6.0;
        return y;
    }

    public HashMap<Long, YieldPoint> readYieldCurves(String fileName, int recentDays) {
        HashMap<Long, YieldPoint> hashMapYieldSet = new HashMap();
        YieldPoint yieldPoint = new YieldPoint();
        FileReader fReader = null;
        BufferedReader buffReader = null;
        long hashKey = 0L;
        Dates dtCurve = new Dates();
        LocalDate dtNow = LocalDate.now();
        Dates dtCurrent = new Dates();
        dtCurrent.year = dtNow.getYear();
        dtCurrent.month = dtNow.getMonthValue();
        dtCurrent.day = dtNow.getDayOfMonth();
        Dates dtRecent = dayCount.time.addDaysToDate(dtCurrent, -recentDays, 'N');
        try {
            fReader = new FileReader(fileName);
            buffReader = new BufferedReader(fReader);
            String sLine = "";
            while ((sLine = buffReader.readLine()) != null) {
                String[] strParts = sLine.split(",");
                String strDtCurve = strParts[3];
                dtCurve = new Dates(strDtCurve, "MM/DD/YYYY");

                if (dtCurve.ltDate(dtRecent)) {
                    continue;
                }
                // YieldType[0]	CreditAgency[1]	CreditRating[2]	DateOfAnalysis[3]	AnalysisPeriod[4]	Tenor[5]	 SpotRate[6]	AvgRate[7]	StdDev[8]	RateDnSide[9]	RateUpSide[10]	RateMin[11]	RateMax[12]
                String cAgency = strParts[1];
                String cRating = strParts[2];
                String strCurveDate = strParts[3];
                String strAnalysisPeriod = strParts[4];
                String strMatLen = strParts[5];
                String strSpotRate = strParts[6];
                String strAvgRate = strParts[7];
                String strStdDev = strParts[8];
                String strRateDnSide = strParts[9];
                String strRateUpSide = strParts[10];
                String strRateMin = strParts[11];
                String strRateMax = strParts[12];
                int iDate = dayCount.time.daysFrom1900(dtCurve);
                int iMatlen = Integer.valueOf(strMatLen);// days since 1960
                // hashKey = 1000000000L * iAgency + 10000000L * iRating + 100L * iDate + iMatlen;
                yieldPoint.yieldType = strParts[0];
                yieldPoint.creditAgency = cAgency;
                yieldPoint.creditRating = cRating;
                yieldPoint.analysisDays = Integer.valueOf(strAnalysisPeriod);
                yieldPoint.dtCurve = dtCurve;
                yieldPoint.matLen = Integer.valueOf(strMatLen);
                yieldPoint.spotRate = Double.valueOf(strSpotRate);
                yieldPoint.spotAvg = Double.valueOf(strAvgRate);
                yieldPoint.spotStdDev = Double.valueOf(strStdDev);
                yieldPoint.spotDnSide = Double.valueOf(strRateDnSide);
                yieldPoint.spotUpSide = Double.valueOf(strRateUpSide);
                yieldPoint.spotMin = Double.valueOf(strRateMin);
                yieldPoint.spotMax = Double.valueOf(strRateMax);
                hashMapYieldSet.put(hashKey, yieldPoint);
            }
        } catch (Exception ex) {
            String errmsg = ex.getMessage();
            error.setError(getClass().getName(), errmsg);
            return hashMapYieldSet;
        }
        return hashMapYieldSet;
    }

    public YieldPoint getYieldCurvePoint(String cAgency, String cRating, Dates dtCurve, int matLen) {
        YieldPoint yieldPoint = null;
        int iDate = dayCount.time.getDaysFromDate(dtCurve);
        // long hashKey = 1000000000L * iAgency + 10000000L * iRating + 100L * iDate + matLen;
        // yieldPoint = yieldPointHashMap.get(hashKey);
        return yieldPoint;
    }


    public YieldPoint convertForwardVolRec(String strLine) {
        YieldPoint yldPoint = new YieldPoint();
        String[] strParts = strLine.split(",");
        // YieldType	CreditAgency	CreditRating	AnalysisStart	AnalysisPeriod	LookAhead	Tenor	SpotRate	ForwardRate	AvgForwardRate	StdDev	RateDnSide	RateUpSide	MinRate	MaxRate
        yldPoint.yieldType = strParts[0];
        yldPoint.creditAgency = strParts[1];
        yldPoint.creditRating = strParts[2];
        Dates dtAnalysis = new Dates();
        dtAnalysis.setDate(strParts[3], "MM/DD/YYYY");
        yldPoint.dtAnalysisStart = dtAnalysis;
        yldPoint.dtForward = dtAnalysis;
        yldPoint.dtCurve = yldPoint.dtForward;
        yldPoint.analysisDays = Integer.valueOf(strParts[4]);
        yldPoint.lookAhead = Integer.valueOf(strParts[5]);
        yldPoint.matLen = Integer.valueOf(strParts[6]);
        yldPoint.spotRate = Double.valueOf(strParts[7]);
        yldPoint.forwardRate = Double.valueOf(strParts[8]);
        yldPoint.spotAvg = Double.valueOf(strParts[9]);
        yldPoint.spotStdDev = Double.valueOf(strParts[10]);
        yldPoint.spotDnSide = Double.valueOf(strParts[11]);
        yldPoint.spotUpSide = Double.valueOf(strParts[12]);
        yldPoint.spotMin = Double.valueOf(strParts[13]);
        yldPoint.spotMax = Double.valueOf(strParts[14]);
        return yldPoint;
    }

    private String getMatName(int matLen) {
        int mo = matLen % 12; // 0 - 11
        int yr = matLen / 12; // 0 - whatever
        String strYr = yr + "yr";
        if (yr == 0) {
            strYr = "";
        }
        String strMo = mo + "mo";
        if (mo == 0)
            strMo = "";
        return strYr + strMo;

    }



}










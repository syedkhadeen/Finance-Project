package app;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

public class Utility {
    final int BEFOREDATE = -2;
    final int ONBEFOREDATE = -1;
    final int ONDATE = 0;
    final int ONAFTERDATE = 1;
    final int AFTERDATE = 2;
    char BSYES = 'Y';
    char BSNO = 'N';
    Error error;
    DayCount dayCount;

    Utility() {
        char BSYES = 'Y';
        char BSNO = 'N';
        error = new Error();
        dayCount = new DayCount(error);
    }

    Utility(Error error, DayCount dayCount) {
        this.error = error;
        this.dayCount = dayCount;
    }

    public static double accruedPerBond(double accInt, int accruedSize, int sfigAccruedSize, char accRoundYn) {
        double accIntAdj = accInt;
        if (accruedSize > 1) {
            accIntAdj = truncRound(accInt * accruedSize, sfigAccruedSize, accRoundYn);
            accIntAdj /= accruedSize;
        }
        return accIntAdj;
    }

    public static double truncRound(double dVal, int decimalPlace, char roundYn) {
        double trRnVal = dVal, decimalShift;
        if (decimalPlace > Limits.MAXDECIMAL) // no rounding
            return dVal;
        decimalShift = Math.pow(10, decimalPlace);
        trRnVal = dVal * decimalShift;
        if (roundYn == 'Y') {
            trRnVal += .5;
            trRnVal = Math.floor(trRnVal);
            trRnVal /= decimalShift;
        } else {
            dVal = Math.floor(trRnVal);
            dVal /= decimalShift;
            trRnVal = dVal;

        }
        return trRnVal;
    }

    // dates must be sorted
    public int getNextRowIndex(Dates date, Dates[] anyDts, int nextLast) {
        int i = anyDts.length;
        int rowIndex = -1;
        switch (nextLast) {
            case BEFOREDATE:
                rowIndex = getRowIndexBefore(date, anyDts);
                break;
            case ONBEFOREDATE:
                rowIndex = getRowIndexOnBefore(date, anyDts);
                break;
            case ONDATE:
                rowIndex = getRowIndexOn(date, anyDts);
                break;
            case ONAFTERDATE:
                rowIndex = getRowIndexOnAfter(date, anyDts);
                break;
            case AFTERDATE:
                rowIndex = getRowIndexAfter(date, anyDts);
                break;
        }
        return rowIndex;
    }

    public int getRowIndexBefore(Dates date, Dates[] anyDts) {
        int i = 1;
        while (++i < anyDts.length) {
            if (date.gtDate(date, anyDts[i])) {
                if (i == 0) {
                    return 0;
                }
                return i - 1;
            }
        }
        return anyDts.length - 1;
    }

    public int getRowIndexOnBefore(Dates date, Dates[] anyDts) {
        int i = 1;
        while (++i < anyDts.length) {
            if (date.geDate(date, anyDts[i])) {
                if (i == 0) {
                    return 0;
                }
                return i - 1;
            }
        }
        return anyDts.length - 1;
    }

    public int getRowIndexOn(Dates date, Dates[] anyDts) {
        int i = 1;
        while (++i < anyDts.length) {
            if (date.eqDate(date, anyDts[i]))
                return i;
        }

        return -1;
    }

    public int getRowIndexAfter(Dates date, Dates[] anyDts) {
        int i = 1;
        while (++i < anyDts.length) {
            if (anyDts[i].gtDate(anyDts[i], date)) {
                return i;
            }
        }
        return anyDts.length - 1;
    }

    public int getRowIndexOnAfter(Dates date, Dates[] anyDts) {
        int i = 1;
        while (++i < anyDts.length) {
            if (anyDts[i].geDate(anyDts[i], date)) {
                return i;
            }
        }
        return anyDts.length - 1;
    }

    public double interpDate(Dates dtFrom, Dates dtLeft, double valLeft, Dates dtRight, double valRight) {
        if (dtFrom.leDate(dtLeft))
            return valLeft;
        if (dtFrom.geDate(dtRight))
            return valRight;
        double daySpan = dayCount.getDaycountDCACT(dtLeft, dtRight);
        if (daySpan == 0)
            return valLeft;
        double dayFrom = dayCount.getDaycountDCACT(dtLeft, dtFrom);
        double valFrom = valLeft + (valRight - valLeft) * dayFrom / daySpan;
        return valFrom;
    }

    public double interpDates(Dates dtFrom, Dates[] dts, double[] vals, int count) {

        int no = -1;
        while (++no < count) {
            if (dts[no].geDate(dtFrom)) {// looking for left bound
                break;
            }
        }
        if (no == count)
            return vals[count - 1];
        if (no == 0) {
            // no right bound
            return vals[0];
        }
        Dates dtLeft = dts[no - 1];
        Dates dtRight = dts[no];
        double valLeft = vals[no - 1];
        double valRight = vals[no];
        double val = interpDate(dtFrom, dtLeft, valLeft, dtRight, valRight);
        return val;
    }

    public double interpDouble(double point, double leftPoint, double leftValue, double rightPoint, double rightValue) {
        double value = 0.;
        if (rightPoint == leftPoint) {
            return 0.;
        }
        double runRate = (rightValue - leftValue) / (rightPoint - leftPoint);
        value = leftValue + (point - leftPoint) * runRate;
        return value;
    }

    public double interpRange(double point, DoubleRange range) {
        double value = 0.;
        if (range.leftPoint == point) {
            return range.leftValue;
        }
        double runRate = (range.rightValue - range.leftValue) / (range.rightPoint - range.leftPoint);
        value = range.leftValue + (point - range.leftPoint) * runRate;
        return value;
    }

    public int[] doubleSort(double[] arr, int n) {
        int[] indx = new int[n];
        int i = -1, xi, j;
        double xa;

        while (++i < n)
            indx[i] = i;

        if (n <= 1)
            return indx;

        for (j = 1; j < n; j++) {
            xa = arr[j];
            xi = j;
            i = j - 1;
            while (i > -1 && arr[i] > xa) {
                arr[i + 1] = arr[i];
                indx[i + 1] = indx[i];
                i--;
            }
            arr[i + 1] = xa;
            indx[i + 1] = xi;
        }
        return indx;
    }

    public boolean isNumeric(String str) {

        try {
            Double num = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public double growthRate(double annuakGrowthRt, int compundFreq, Dates dtStart, Dates dtEnd) {
        double gFactor = 0., period, noPeriods;
        // compoundFreq ispayments per year
        double noYears = dayCount.getYearsBetweenDates(dtStart, dtEnd);
        noPeriods = noYears * compundFreq;
        gFactor = Math.pow((1. + annuakGrowthRt / compundFreq), noPeriods);
        return gFactor;
    }

    public String removeDuplcates(String strSource, char chRemove) {
        int cNo = -1;
        String strClean = "";
        char lastCh = UnAvails.NOTAVAILCHAR, nextCh;
        while (++cNo < strSource.length()) {
            nextCh = strSource.charAt(cNo);
            if (nextCh == lastCh)
                continue;
            else
                strClean = strClean + nextCh;
        }
        return strClean;
    }

    public String convertToTitleCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }
        return converted.toString();
    }

    public ArrayList<Integer> convertToIntList(String calcNoStr, String listName) {
        ArrayList<Integer> intList = new ArrayList<>();
        String[] intParts = calcNoStr.split(",");
        int noParts = intParts.length;
        Integer iVal = null;
        int ip = -1;
        while (++ip < noParts) {
            try {
                iVal = Integer.valueOf(intParts[ip]);
                intList.add(iVal);
            } catch (Exception ex) {
                String errmsg = "could not convert " + listName + ": " + calcNoStr + " to integers";
                return intList;
            }
        }
        return intList;
    }

    public ArrayList<String> getJsonRequestString(String jString) {
        ArrayList<String> jsonStrList = new ArrayList<>();
        int noChars = 0;
        StringTokenizer stok = new StringTokenizer(jString, "{");
        int noTokens = stok.countTokens();
        String str = "";
        str = stok.nextToken();
        int noToks = 0;
        while (stok.hasMoreTokens() && noToks < noTokens - 2) {
            str = "{" + stok.nextToken();
            str = str.replaceAll(",", "");
            jsonStrList.add(str);
        }
        // last string has "}]"
        String[] lastStr = str.split("]");
        jsonStrList.add(lastStr[0]);
        return jsonStrList;
    }

    public double convertToYears(String timeUnit, int repeatFactor) {
        double yrs = 0.;
        switch (timeUnit) {
            case "M":
                yrs = repeatFactor * (1. / CalUnits.MONTHSPERYEAR);
                break;
            case "W":
                yrs = repeatFactor * (1. / CalUnits.WEEKSPERYEAR);
                break;
            case "D":
                yrs = repeatFactor * (1. / CalUnits.DAYSPERYEAR);
                break;
        }
        return yrs;
    }

    public double[] setCubicSpline0(double[] x, double[] y, int n, double yp1, double ypn) {
        // x1 and y1 have data starting at index = 1
        double[] y2 = new double[Limits.MAXYIELDPOINTS]; // second derivative
        double[] u = new double[Limits.MAXYIELDPOINTS];

        if (yp1 > .99e30)
            y2[0] = u[0] = 0.;
        else {
            y2[0] = -.5;
            u[0] = (3.0 / (x[1] - x[0])) * ((y[1] - y[0]) / (x[1] - x[0]) - yp1);
        }
        int i;
        for (i = 1; i <= n - 2; ++i) {
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
            un = (3.0 / (x[n - 1] - x[n - 2])) * (ypn - (y[n - 1] - y[n - 2]) / (x[n - 1] - x[n - 2]));
        }
        y2[n - 1] = (un - qn * u[n - 2]) / (qn * y2[n - 2] + 1.0);
        int k;
        for (k = n - 2; k >= 0; k--)
            y2[k] = y2[k] * y2[k + 1] + u[k];
        return y2;

    }

    public double[] setCubicSpline(double[] x1, double[] y1, int n, double yp1, double ypn) {
        // x1 and y1 have data starting at index = 1
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

    public double getSplineValue0(double[] xa, double[] ya, double[] y2a, int n, double x) {
        // xa,ya, y2a have data start at inde1
        double y = 0.; // cubic spline interpolated value
        int klo, khi, k;
        double h, b, a;
        klo = 0;
        khi = n;
        while (khi - klo > 0) {
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

    public double getSplineValue(double[] xa1, double[] ya1, double[] y2a1, int n, double x) {
        // xa,ya, y2a have data start at inde1
        double[] xa = new double[n + 1];
        double[] ya = new double[n + 1];
        double[] y2a = new double[n + 1];
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

    public double calcStandardDeviation(double avgVal, Vector<Double> vector) {
        double stdDev = 0.;
        double var2 = 0.;
        int no = -1, noValues = vector.size();
        while (++no < noValues) {
            var2 = (avgVal - vector.get(no)) * (avgVal - vector.get(no));
            stdDev += var2;
        }
        if (noValues > 0)
            stdDev = Math.sqrt(stdDev / noValues);
        return stdDev;
    }

    static class DoubleRange {
        double leftPoint;
        double rightPoint;
        int nextIdx;
        double leftValue;
        double rightValue;
    }

    public ArrayList<YieldPoint> interpYieldCurve(Dates dtOrigin, Dates[] resetDates, int noResetDates,
            double[] ycRates, int[] ycMatlen, int noCurvePoints) {
        ArrayList<YieldPoint> yieldurve = new ArrayList<>();
        YieldPoint ycPoint = new YieldPoint();
        int no = -1;
        Dates[] ycDates = new Dates[noCurvePoints];
        // convert yield curve maturity leengths to dates from origin
        while (++no < noCurvePoints)
            ycDates[no] = dayCount.time.addMonthsToDate(dtOrigin, ycMatlen[no], 'N');
        Dates ycDate = new Dates();
        no = -1;
        while (++no < noResetDates) {
            ycPoint.dtCurve = dtOrigin;
            ycPoint.matLen = ycMatlen[no];
            ycPoint.yieldType = BondConsts.FIXINGCURVE;
            ycPoint.spotRate = interpDates(resetDates[no], ycDates, ycRates, noCurvePoints);
            yieldurve.add(no, new YieldPoint(ycPoint));
        }
        return yieldurve;
    }

} // end utility class


import java.util.StringTokenizer;


public class Holidays {
    String fullFileName;
    FileTableReader ftReader;
    String afterHol = BondConsts.AFTERHOLIDAY;
    String beforeHol = BondConsts.BEFOREHOLIDAY;
    String sameMonth = BondConsts.AFTERHOLIDAYSAMEMONTH;
    String ignoreHolidays = BondConsts.IGNOREHOLIDAYS;
    String dtFormat;
    char afterYn;
    int noHolidays;
    int weekend1;
    int weekend2;
    int iVal;
    double nVal;
    String sVal;
    Dates dtVal = new Dates();
    char cVal;
    Time time;
    Error error;
    DayCount dayCount;
    Holiday[] holidayCal;

    Holidays (Error error) {
        this.error = error;
    }

    public void setHolidays() {
        int ih = -1, limit = 20;
        Dates date = new Dates();
        this.weekend1 = 5; // Saturday
        this.weekend2 = 6; // Sunday
        this.noHolidays = limit;
        holidayCal = new Holiday[Limits.MAXHOLIDAYS];
        while (++ih < limit) {
            holidayCal[ih] = new Holiday();
            holidayCal[ih].setCountry("USA");
            date.setDate(2020 + ih, 1, 1);
            holidayCal[ih].setHolidayDt(date);
            holidayCal[ih].setType('S');
        }

    }

    public void setHolidays(String rootDir, Error error) {
        this.error = error;
        this.afterYn = error.BSYES;
        this.fullFileName = rootDir + "\\config\\holiday.csv";
        try {
            ftReader = new FileTableReader(fullFileName);
            readHolidays(fullFileName);
        } catch (Exception ex) {
            error.setError(getClass().getName(), "Could no open holiday file " + fullFileName);
        }
    }

   public void setHolidays(String useConfigYn, String rootDir, Error error) {
        if (useConfigYn.equals("Y"))
            setHolidays(rootDir, error);
        else
            setHolidays();
    }

    public boolean readHolidays(String fullFileName) throws Exception {
        ftReader = new FileTableReader(fullFileName);
        ftReader.tableReader(fullFileName);
        boolean isOk = readHolidayData(ftReader);
        return isOk;
    }

    public boolean readHolidayData(FileTableReader fileRead) {
        holidayCal = new Holiday[fileRead.noRows];
        int ic = -1, row = -1;
        String nextTok;
        boolean isOk = false;
        while (++row < fileRead.noRows) {
            isOk = readRow(row);
        }
        this.noHolidays = fileRead.noRows;
        return isOk;
    }


    public boolean readRow(int row) {
        int ic = -1;
        String nextTok, sDomain;
        String sRowString = ftReader.scanner.nextLine();
        boolean isOk = true;
        ftReader.stok = new StringTokenizer(sRowString, ",;|");
        holidayCal[row] = new Holiday();
        while (++ic < ftReader.noCols) {
            nextTok = ftReader.stok.nextToken();
            sDomain = ftReader.colDomains[ic].toUpperCase();
            switch (sDomain) {
                case "I":
                    this.iVal = Integer.valueOf(nextTok);
                    break;
                case "N":
                    this.nVal = Double.valueOf(nextTok);
                    break;
                case "D":
                    dtVal.setDate(nextTok, ftReader.dtFormat);
                    break;
                case "S":
                    sVal = nextTok;
                    break;
                case "C":
                    cVal = nextTok.charAt(0);
                    break;
                default:
                    error.setError(getClass().getName(), "Invalid data domain " + sDomain + " detected in holiday file");
                    return false;
            }
            isOk = assignToHoliday(row, ftReader.colNames[ic], sVal, dtVal, cVal);
            if (!isOk) {
                return false;
            }
        }
        return isOk;

    }


    public boolean assignToHoliday(int row, String colName, String sVal, Dates dtVal, char cVal) {
        switch (colName.toUpperCase()) {
            case "DATE":
                holidayCal[row].dtHoliday = dtVal;
                break;
            case "COUNTRYEXCHANGE":
                holidayCal[row].countryExchange = sVal;
                break;
            case "TYPE":
                holidayCal[row].type = cVal;
                break;
            default:
                error.setError(getClass().getName(), "Invalid holiday field name " + colName + " detected in holiday file");
                return false;
        }
        return true;
    }

    public void setWeekends(int weekend1, int weekend2) {
        this.weekend1 = weekend1;
        this.weekend2 = weekend2;
    }

    public void loadHolidays() {
        int ih = 0, count = 0;
        String dtFormat = "MM/DD/YYYY";
        Dates[] dates = new Dates[3];
        String[] countries = new String[3];

        countries[ih] = "USA";
        dates[ih] = new Dates();
        dates[ih].setDate("01/01/2020", dtFormat);
        holidayCal[ih].type = 'S';

        countries[++ih] = "USA";
        dates[ih] = new Dates();
        dates[ih].setDate("02/15/2020", dtFormat);
        holidayCal[ih].type = 'S';

        countries[++ih] = "USA";
        dates[ih] = new Dates();
        dates[ih].setDate("07/04/2020", dtFormat);
        holidayCal[ih].type = 'S';

    }

    public Holiday[] getHolidayCal() {
        return holidayCal;
    }

    public int getHolidayIndex(Dates dtDate, String countryExchange) {
        int ih = -1;
        while (++ih < noHolidays) {
            if (holidayCal[ih].countryExchange.equals(countryExchange))
                if (holidayCal[ih].dtHoliday.eqDate(dtDate, holidayCal[ih].dtHoliday))
                    return ih;
        }
        return UnAvails.NOTAVAILINT;
    }

    // holiday file must be sorted by country then by date
    public Dates getNextHoliday(int index, String holOffset, String countryExchange) {
        Dates dtUnavil = new Dates();
        if (holOffset == afterHol || holOffset == sameMonth) {
            if (index < holidayCal.length - 1) {
                if (holidayCal[index + 1].countryExchange.equals(countryExchange))
                    return holidayCal[index + 1].dtHoliday;
                return dtUnavil;
            }
        } else {
            if (index > 1) {
                if (holidayCal[index - 1].countryExchange.equals(countryExchange))
                    return holidayCal[index - 1].dtHoliday;
            } else
                return dtUnavil;
        }
        return dtUnavil;
    }


    public Holiday[] getHolidays() {
        return this.holidayCal;
    }

    public Dates getNonHolidayBeforeAfter(String holidayOffset, Dates dtDate, String countryExchange) {
        if (holidayOffset.equals(this.ignoreHolidays))
            return dtDate;
        Dates dtNonHol;
        char use360Yn = 'N';
        int dayOfWeekNum = UnAvails.NOTAVAILDATE;
        String dayOfWeekString;
        int hIndex = getHolidayIndex(dtDate, countryExchange);
        if (hIndex == UnAvails.NOTAVAILINT)
            return dtDate;
        // start loop to get next/last holidays and non weekends where there is a business dat gap
        dtNonHol = getNextHoliday(hIndex, holidayOffset, countryExchange);
        if (dtNonHol.year == UnAvails.NOTAVAILDATE)
            return time.addDaysToDate(dtNonHol, 1, use360Yn); // go the the next date as at is a non holiday be default
        // get holiday gap
        if (holidayOffset.equals(afterHol))
            dtNonHol = getNonHolidayAfter(hIndex, dtDate, countryExchange);
        else
            dtNonHol = getNonHolidayBefore(hIndex, dtDate, countryExchange);
        return dtNonHol;
    }

    public Dates getNonHolidayAfter(int hIndex, Dates dtDate, String countryExchange) {
        Dates dtHol, dtLast, dtNonHol;
        int dayOfWeekNum = UnAvails.NOTAVAILDATE, dayGap = 0;
        String dayOfWeekString = null;
        boolean isWeekend = true;
        char use360Yn = 'N';
        dtNonHol = new Dates(dtDate);

        // start loop to get next/last holidays and non weekends where there is a business dat gap
        int ih = hIndex;
        dtLast = dtDate;
        while (++ih < holidayCal.length && dayGap < 2 || isWeekend) {
            dtHol = getNextHoliday(ih, afterHol, countryExchange);
            if (dtHol.year == UnAvails.NOTAVAILDATE) {
                dtNonHol = time.addDaysToDate(dtLast, 1, use360Yn);
                dtNonHol = getNextNonWeekEnd(dtNonHol, afterYn);
                return dtNonHol;
            }
            dayGap = dayCount.getDaycountDCACT(dtDate, dtHol);
            dtNonHol = time.addDaysToDate(dtLast, 1, use360Yn);
            isWeekend = dtNonHol.isWeekend(dtNonHol);
            dtLast = dtHol;
        }
        return dtNonHol;
    }

    public Dates getNonHolidayBefore(int hIndex, Dates dtDate, String countryExchange) {
        Dates dtHol, dtNonHol, dtLast;
        int dayOfWeekNum = UnAvails.NOTAVAILDATE, dayGap = 0;
        String dayOfWeekString = null;
        char use360Yn = 'N';
        boolean isWeekend = true;
        dtNonHol = new Dates(dtDate);
        // start loop to get next/last holidays and non weekends where there is a business dat gap
        int ih = hIndex;
        dtLast = dtDate;
        while (--ih < 0 && dayGap < 2 || isWeekend) {
            dtHol = getNextHoliday(ih, afterHol, countryExchange);
            if (dtHol.year == UnAvails.NOTAVAILDATE) {
                dtNonHol = time.addDaysToDate(dtLast, -1, use360Yn);
                dtNonHol = getNextNonWeekEnd(dtNonHol, afterYn);
                return dtNonHol;
            }
            dayGap = dayCount.getDaycountDCACT(dtDate, dtHol);
            dtNonHol = time.addDaysToDate(dtLast, -1, use360Yn);
            isWeekend = dtNonHol.isWeekend(dtNonHol);
            dtLast = dtHol;
        }
        return dtNonHol;
    }

    public Dates getNextNonWeekEnd(Dates dtDate, char afterYn) {
        boolean isWE = dtDate.isWeekend(dtDate);
        Dates dtNonWe;
        char use360Yn = 'N';
        if (!isWE)
            return dtDate;
        int nextDayNum1;
        if (afterYn == error.BSYES) {
            nextDayNum1 = (this.weekend1 + 1) % 7;
            if (nextDayNum1 == this.weekend2)
                dtNonWe = time.addDaysToDate(dtDate, 2, use360Yn);
            else
                dtNonWe = time.addDaysToDate(dtDate, 1, use360Yn);
            return dtNonWe;
        }
        nextDayNum1 = (this.weekend1 + -1) % 7;
        if (nextDayNum1 == this.weekend2)
            dtNonWe = time.addDaysToDate(dtDate, -2, use360Yn);
        else
            dtNonWe = time.addDaysToDate(dtDate, -1, use360Yn);
        return dtNonWe;
    }

    public Dates getNonHolidayDate(String holidayOffset, Dates dtDate, String countryExchange) {
        Dates dtNonHol = getNonHolidayBeforeAfter(holidayOffset, dtDate, countryExchange);
        if (holidayOffset.equals(sameMonth)) {
            if (dtDate.month == dtNonHol.month) {
                dtNonHol = getNonHolidayBeforeAfter(beforeHol, dtDate, countryExchange);
            }
        }
        return dtNonHol;
    }

    public boolean isHolidayDate(Dates dtDate) {

        for (int ih = 0; ih < noHolidays; ++ih) {
            if (dtDate.eqDate(dtDate, holidayCal[ih].dtHoliday)) {
                return true;
            }
        }
        return false;
    }
}











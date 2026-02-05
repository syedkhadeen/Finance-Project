package app;

public class CalUnits {
    public static final Dates dt1900 = new Dates("01/01/1900", "MM/DD/YYYY");
    public static final int MAXYEAR = 2220;
    // calendar constants
    public static final int daysPer30DayMonth = 30;
    public static final int[] daysInMonthNonLeap = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    public static final int[] daysInMonthLeap = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    public static final int[] cumDaysInYearNonLeap = { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365 };
    public static final int[] cumDaysInYearLeap = { 0, 31, 60, 99, 121, 152, 182, 213, 244, 274, 305, 335, 366 };
    public static final int[] daysInLeapCycle = { 0, 366, 731, 1096, 1461 }; // day preceding the cycle year,index is in
                                                                             // cycle year from beginning
    public static final int daysInNonLeapYear = 365;
    public static final int daysInLeapYear = 366;
    public static final int leapCycleDays = 1461;
    public static final int monthsInYear = 12;
    public static final int calcYearFractional = 1; // 365.25 days per year
    public static final int calcYear365 = 2;
    public static final int calcYearPrecise = 3; // full year plus fraction of remaining year
    public static final double averageDaysInYear = 365.25;
    public static final int noYearsInLeapCycle = 4;
    //
    public static final double durationYieldShift = .01;
    // days of week\
    static final int SUNDAY = 0;
    static final int MONDAY = 1;
    static final int TUESDAY = 2;
    static final int WEDSDAY = 3;
    static final int THURSDAY = 4;
    static final int FRIDAY = 5;
    static final int SATURDAY = 6;
    // payments per year for fq
    static final int ANNUALFQ = 1;
    static final int SEMIANNUALFQ = 2;
    static final int TRIANNUALFQ = 3;
    static final int QUARTERLYFQ = 4;
    static final int BIMONTHLYFQ = 6;
    static final int MONTHLYFQ = 12;
    // repeat factor for payments per year
    static final int ANNUALREPEAT = 12;
    static final int SEMIANNUALREPEAT = 6;
    static final int TRIANNUALREPEAT = 4;
    static final int QUARTERLYREPEAT = 3;
    static final int BIMONTHLYREPEAT = 2;
    static final int MONTHLYREPEAT = 1;
    // Calendar time constants
    static final int NOREPEAT = 0;
    static final int DAYSIN1900 = 693975; // 1900 * 1461
    static final int WEEKSPERYEAR = 52;
    static final int MAXWEEKSPERMONTH = 5;
    static final int MONTHSPERYEAR = 12;
    static final int MONTHSPERSEMIANNUAL = 6;
    static final int MONTHSPERTRIANNUAL = 4;
    static final int MONTHSPERQUARTER = 3;
    static final int MONTHSPERBIMONTHLY = 2;
    static final int MONTHSPERMONTH = 1;
    static final int DAYSPERWEEK = 7;
    static final int DAYSPERYEAR = 364;
    static final int DAYSPERYEARNONLEAP = 365;
    static final int DAYSPERYEARLEAP = 366;

    static final int SIXMONTHS = 6;

    static final int THREEMONTHS = 3;

    static final double ONETHIRDYEAR = 1. / 4.;
    static final double ONETWELVEYEAR = 1. / 12.;

    static final int VololityUP = -1;
    static final int VololityDN = 1;

    static Dates naDate = new Dates();

    static final int MONTHSSHORTTERMTAXGL = 12;
    static final String DEFAULTDATEFORMAT = "MM/DD/YYYY";
    static final int Jan = 1;
    static final int Feb = 2;
    static final int Mar = 3;
    static final int Apr = 4;
    static final int May = 5;
    static final int Jun = 6;
    static final int Jul = 7;
    static final int Aug = 8;
    static final int Sep = 9;
    static final int Oct = 10;
    static final int Nov = 11;
    static final int Dec = 12;

}
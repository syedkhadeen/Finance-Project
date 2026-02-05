package app;

public class UnAvails {
    static final int NOTAVAILINT = -99;
    static final double NOTAVAILDOUBLE = -99.;
    static final long NOTAVAILLONG = -1;
    static final int NOTAVAILDATE = -1;
    static final double NOTAVAILCOUPON = -9999999.;
    static final String NOTAVAILSTRING = "?";
    static final char NOTAVAILCHAR = '?';
    Dates dtUnAvail = new Dates();

    UnAvails() {
        setDateUnAvail();
    }

    public void setDateUnAvail() {
        dtUnAvail.year = UnAvails.NOTAVAILINT;
        dtUnAvail.month = UnAvails.NOTAVAILINT;
        dtUnAvail.day = UnAvails.NOTAVAILINT;
        dtUnAvail.dtCode = UnAvails.NOTAVAILLONG;
    }

    public static boolean isUnAvail(int iVal) {
        return iVal == UnAvails.NOTAVAILINT;
    }

    public static boolean isAvail(int iVal) {
        return iVal != UnAvails.NOTAVAILINT;
    }

    public static boolean isUnAvail(double dVal) {
        return dVal == UnAvails.NOTAVAILDOUBLE;
    }

    public static boolean isUnAvail(char cVal) {
        return cVal == UnAvails.NOTAVAILCHAR;
    }

    public static boolean isAvail(double dVal) {
        return dVal != UnAvails.NOTAVAILDOUBLE;
    }

    public static boolean isUnAvail(String sVal) {
        return sVal == UnAvails.NOTAVAILSTRING;
    }

    public static boolean isAvail(String sVal) {
        return sVal != UnAvails.NOTAVAILSTRING;
    }

    public static boolean isUnAvail(Dates dtDate) {
        return dtDate.year == UnAvails.NOTAVAILDATE;
    }

    public static boolean isAvail(Dates dtDate) {
        return dtDate.year != UnAvails.NOTAVAILDATE;
    }

}
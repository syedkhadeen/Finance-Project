package app;

import java.util.ArrayList;

public class CreditAgencyRating {
    public ArrayList<CreditMapping> cAgencyMap = new ArrayList<>();
    public ArrayList<CreditMapping> cRatingMap = new ArrayList<>();
    DayCount dayCount;

    CreditAgencyRating(DayCount dayCount) {
        this.dayCount = dayCount;
    }

    public int setAgency() {
        ArrayList<CreditMapping> agencyMap = new ArrayList<>();
        cAgencyMap.add(0, new CreditMapping("S&P", 1));
        cAgencyMap.add(1, new CreditMapping("Moody", 2));
        cAgencyMap.add(2, new CreditMapping("Fitch", 3));
        cAgencyMap.add(3, new CreditMapping("Internal", 4));
        return 4;
    }

    class CreditMapping {
        String name;
        int value;

        CreditMapping(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    public int setRating() {
        int i = 0;
        cRatingMap.add(i++, new CreditMapping("AAA", i));
        cRatingMap.add(i++, new CreditMapping("AA+", i));
        cRatingMap.add(i++, new CreditMapping("AA", i));
        cRatingMap.add(i++, new CreditMapping("AA-", i));
        cRatingMap.add(i++, new CreditMapping("A+", i));
        cRatingMap.add(i++, new CreditMapping("A", i));
        cRatingMap.add(i++, new CreditMapping("BBB+", i));
        cRatingMap.add(i++, new CreditMapping("BBB", i));
        cRatingMap.add(i++, new CreditMapping("BBB-", i));
        cRatingMap.add(i++, new CreditMapping("BB+", i));
        cRatingMap.add(i++, new CreditMapping("BB", i));
        cRatingMap.add(i++, new CreditMapping("BB-", i));
        cRatingMap.add(i++, new CreditMapping("B+", i));
        cRatingMap.add(i++, new CreditMapping("B", i));
        cRatingMap.add(i++, new CreditMapping("B-+", i));
        cRatingMap.add(i++, new CreditMapping("CCC+", i));
        cRatingMap.add(i++, new CreditMapping("CCC", i));
        cRatingMap.add(i++, new CreditMapping("CCC-", i));
        cRatingMap.add(i++, new CreditMapping("CCC", i));
        cRatingMap.add(i++, new CreditMapping("CCC-", i));
        cRatingMap.add(i++, new CreditMapping("D", i));
        cRatingMap.add(i++, new CreditMapping("SD", i));
        // cRatingMap.add(i++, new CreditMapping("R", i));
        return i;
    }

    public long getHashKey(String cAgency, String cRating, Dates dtCurve, int matLen) {
        long hashKey = 0;
        int cAgencyId = getCAgencyId(cAgency);
        int cRatingId = getCRatingId(cAgency);
        int cDateId = dayCount.time.getDaysFromDate(dtCurve);
        hashKey = 1000000000L * cAgencyId + 10000000L * cRatingId + 100L * cDateId + matLen;
        return hashKey;

    }

    public int getCAgencyId(String cAgency) {
        int i = -1;
        while (++i < this.cAgencyMap.size()) {
            if (cAgency.equalsIgnoreCase(cAgencyMap.get(i).name)) {
                return cAgencyMap.get(i).value;
            }
        }
        return UnAvails.NOTAVAILINT;
    }

    public int getCRatingId(String cRating) {
        int i = -1;
        while (++i < this.cRatingMap.size()) {
            if (cRating.equalsIgnoreCase(cRatingMap.get(i).name)) {
                return cRatingMap.get(i).value;
            }
        }
        return UnAvails.NOTAVAILINT;
    }
}

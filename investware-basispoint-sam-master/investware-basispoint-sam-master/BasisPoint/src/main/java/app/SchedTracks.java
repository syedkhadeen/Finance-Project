package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SchedTracks {
    // cashflow type
    static final int BEFOREDATE = -2;
    static final int ONBEFOREDATE = -1;
    static final int ONDATE = 0;
    static final int ONAFTERDATE = 1;
    static final int AFTERDATE = 2;
    Error error;
    DayCount dayCount;
    Choices choices;
    int noSchedTracks = 0;
    int currentTrackNo = 0;
    // SchedRule schedRule;
    String[] trackNames = new String[Limits.MAXSCHEDTRACKS];
    Bond bond;
    Dates dt1900;
    Dates dtInfinity;
    Dates dtLastTrack;
    long lastSequence;
    List<SchedTrack> schedTrackList = new ArrayList<SchedTrack>();

    SchedTracks(Bond bond, Error error, Choices choices, DayCount dayCount) {
        this.dayCount = dayCount;
        this.choices = choices;
        this.error = error;
        this.dt1900 = new Dates();
        this.dt1900 = dt1900.assignDate(1900, 1, 1);
        this.currentTrackNo = 0;
        this.noSchedTracks = 0;
        this.dtInfinity = new Dates("12/31/3000", "MM/DD/YYYY");
        this.bond = bond;
    }

    public void setBond(Bond bond) {
        this.bond = bond;
    }

    public Dates getLastTrackDate() {
        int noTracks = schedTrackList.size();
        Dates dtLastTRack = dtLastTrack = schedTrackList.get(noTracks - 1).schedDate;
        return dtLastTRack;
    }

    public long getLastTrackSequence() {
        int noTracks = schedTrackList.size();
        long lastSequence = schedTrackList.get(noTracks - 1).schedSequence;
        return lastSequence;
    }

    private SchedTrack assignSchedTrack(int trackNo, Dates schedDate, double schedValue, Dates dtSchedule,
            int schedType) {
        SchedTrack schedTrack = new SchedTrack();
        schedTrack.found = true;
        schedTrack.schedValue = schedValue;
        schedTrack.schedDate = dtSchedule;
        schedTrack.schedType = schedType;
        schedTrack.trackNo = trackNo;
        schedTrack.schedDate = schedDate;
        schedTrack.schedSequence = schedTrack.createSequence(schedDate, schedType);
        return schedTrack;
    }

    public void addSchedTrack(int idx, int schedType, String schedTrackName, Dates schedDate, char schedOptionalYn,
            double schedValue, double schedPrice, long schedSequence) {
        schedTrackList.add(noSchedTracks++, new SchedTrack(idx, schedType, schedTrackName, schedDate, schedOptionalYn,
                schedValue, schedPrice, schedSequence));

    }

    public SchedTrack getNextSchedTrack(int beforeAfter, Dates dtDate, int schedType) {
        int idxFound = UnAvails.NOTAVAILINT;
        SchedTrack schedTrack = new SchedTrack();
        switch (beforeAfter) {
            case BEFOREDATE:
                schedTrack = getSchedTrackBefore(dtDate, schedType);
                break;
            case ONBEFOREDATE:
                schedTrack = getSchedTrackOnBefore(dtDate, schedType);
                break;
            case ONDATE:
                schedTrack = getSchedTrackOn(dtDate, schedType);
                break;
            case ONAFTERDATE:
                schedTrack = getSchedTrackOnAfter(dtDate, schedType);
                break;
            case AFTERDATE:
                schedTrack = getSchedTrackAfter(dtDate, schedType);
                break;
            default:
                schedTrack.errmsg = "Invalid date option detected " + beforeAfter;
                schedTrack.found = false;
                return schedTrack;
        }
        return schedTrack;
    }

    public SchedTrack getNextSchedTrack() {
        if (currentTrackNo == noSchedTracks - 1) {
            schedTrackList.get(currentTrackNo).found = false;
            return schedTrackList.get(currentTrackNo);
        }
        return schedTrackList.get(currentTrackNo + 1);
    }

    public boolean setSchedTracks(String instype) {
        this.noSchedTracks = 0;

        if (bond.allDocCalculations == 'Y')
            return true;
        if (UnAvails.isUnAvail(instype)) {
            return true;
        }
        addAccrueTracks(instype);
        if (bond.error.hasErrorYn == 'Y')
            return false;
        addIntPayTracks(instype);
        if (bond.error.hasErrorYn == 'Y')
            return false;
        addRedeemTrack(instype);
        if (bond.error.hasErrorYn == 'Y')
            return false;
        addSinkTracks(instype);
        if (bond.error.hasErrorYn == 'Y')
            return false;
        addResetTracks(instype);
        if (bond.error.hasErrorYn == 'Y')
            return false;
        addIRSSwapTracks(instype);
        if (bond.error.hasErrorYn == 'Y')
            return false;
        addIndexTracks(instype);
        if (bond.error.hasErrorYn == 'Y')
            return false;
        addIndexRatioTracks(instype);
        if (bond.error.hasErrorYn == 'Y')
            return false;
        addCallTracks(instype);
        if (bond.error.hasErrorYn == 'Y')
            return false;
        addPutTracks(instype);
        if (bond.error.hasErrorYn == 'Y')
            return false;
        addFxRateTracks(instype);
        if (bond.error.hasErrorYn == 'Y')
            return false;
        addReinvestTracks(instype);
        if (bond.error.hasErrorYn == 'Y')
            return false;
        addTaxPaymentTracks(instype);
        if (bond.error.hasErrorYn == 'Y')
            return false;
        if (bond.error.hasErrorYn == 'Y')
            return false;
        sortSchedTracks();
        if (bond.error.hasErrorYn == 'Y')
            return false;
        currentTrackNo = 0;
        setTrackNames();
        return true;
    }

    public SchedTrack getLastSchedTrack(int schedType) {
        SchedTrack schedTrack = new SchedTrack();
        schedTrack = schedTrackList.get(noSchedTracks - 1);
        currentTrackNo = noSchedTracks - 1;
        schedTrack.found = true;
        return schedTrack;
    }

    public SchedTrack getFirstSchedTrack(int schedType) {
        SchedTrack schedTrack = new SchedTrack();
        int no = -1;
        while (++no < noSchedTracks) {
            if (schedType == bond.schedTracks.schedTrackList.get(no).schedType) {
                schedTrack = bond.schedTracks.schedTrackList.get(no);
                schedTrack.found = true;
                return schedTrack;
            }
        }
        currentTrackNo = 0;
        schedTrack.found = false;
        return schedTrack;
    }

    public boolean addIntPayTracks(String instype) {
        if (instype.equals(Instype.IRSSWAP))
            return true;
        if (choices.inDiscountList(instype))
            return true;
        int schedType = SchedTrack.INTERESTPAYTRACK;
        String schedTrackName = SchedTrack.INTERESTPAYTRACKNAME;
        SchedTrack schedTrack = new SchedTrack();
        char optionalYn = 'N';
        long sequence;
        int np = -1;
        Dates dtLast = new Dates();
        Dates dtNext = new Dates();
        if (bond.intSchedRule.timeUnit == BondConsts.NOPERIODICITY) { // zero coupom and at amturity
            sequence = schedTrack.createSequence(bond.dtMaturity, schedType);
            addSchedTrack(noSchedTracks, schedType, schedTrackName, bond.dtMaturity, 'N', 0., 0., sequence);
            return true;
        }
        dtNext = bond.intSchedRule.getNextSchedDate(BEFOREDATE, bond.dtSettle);
        while (dtNext.leDate(dtNext, bond.dtMaturity)) {
            sequence = schedTrack.createSequence(dtNext, schedType);
            addSchedTrack(noSchedTracks, schedType, schedTrackName, dtNext, 'N', 0., 0., sequence);
            dtLast.setDate(dtNext);
            dtNext = bond.intSchedRule.getNextSchedDate(AFTERDATE, dtLast);
        }
        if (dtLast.ltDate(bond.dtMaturity)) {
            sequence = schedTrack.createSequence(bond.dtMaturity, schedType);
            addSchedTrack(noSchedTracks, schedType, schedTrackName, bond.dtMaturity, 'N', 0., 0., sequence);
        }
        return true;
    }

    public boolean addAccrueTracks(String instype) {
        if (instype.toUpperCase().equals(Instype.IRSSWAP))
            return true;
        if (choices.inDiscountList(instype))
            return true;
        int schedType = SchedTrack.ACCRUETRACK;
        String schedTrackName = SchedTrack.ACCRUETRACKNAME;
        SchedTrack schedTrack = new SchedTrack();
        char optionalYn = 'N';
        long sequence;
        int np = -1;
        Dates dtLast = new Dates();
        Dates dtNext = new Dates();
        if (bond.accrueSchedRule.timeUnit == BondConsts.NOPERIODICITY) { // zero coupom and at amturity
            sequence = schedTrack.createSequence(bond.dtMaturity, schedType);
            addSchedTrack(noSchedTracks, schedType, schedTrackName, bond.dtSettle, 'N', 0., 0., sequence);
            return true;
        }
        dtNext = bond.accrueSchedRule.getNextSchedDate(BEFOREDATE, bond.dtSettle);
        // we need to start befote the coupon date prior to settle date
        while (dtNext.leDate(dtNext, bond.dtMaturity)) {
            sequence = schedTrack.createSequence(dtNext, schedType);
            addSchedTrack(noSchedTracks, schedType, schedTrackName, dtNext, 'N', 0., 0., sequence);
            dtLast.setDate(dtNext);
            dtNext = bond.accrueSchedRule.getNextSchedDate(AFTERDATE, dtLast);
            if (bond.error.hasErrorYn == 'Y')
                return false;
        }
        if (dtLast.ltDate(bond.dtMaturity)) {
            sequence = schedTrack.createSequence(bond.dtMaturity, schedType);
            addSchedTrack(noSchedTracks, schedType, schedTrackName, bond.dtMaturity, 'N', 0., 0., sequence);
        }

        return true;
    }

    public boolean addRedeemTrack(String instype) {
        long sequence;
        SchedTrack schedTrack = new SchedTrack();
        int schedType = SchedTrack.REDEEMTRACK;
        String schedTrackName = SchedTrack.REDEEMTRACKNAME;
        sequence = schedTrack.createSequence(bond.dtMaturity, schedType);
        addSchedTrack(noSchedTracks, schedType, schedTrackName, bond.dtMaturity, 'N', bond.redeemPrin, bond.redeemPrin,
                sequence);
        return true;
    }

    public boolean addSinkTracks(String instype) {
        if (bond.noSinkDts == 0)
            return true;
        SchedTrack schedTrack = new SchedTrack();
        int schedType = SchedTrack.SINKTRACK;
        String schedTrackName = SchedTrack.SINKTRACKNAME;

        char optionalYn = 'N';
        int sNo = -1;
        if (bond.noSinkPcts > 0) {
            while (++sNo < bond.noSinkDts) {
                long sequence = schedTrack.createSequence(bond.dtSinks[sNo], schedType);
                addSchedTrack(noSchedTracks, schedType, schedTrackName, bond.dtSinks[sNo], optionalYn,
                        bond.sinkPcts[sNo], bond.sinkPrcs[sNo], sequence);
            }
        }
        sNo = -1;
        if (bond.noSinkAmts > 0) {
            while (++sNo < bond.noSinkDts) {
                long sequence = schedTrack.createSequence(bond.dtSinks[sNo], schedType);
                addSchedTrack(noSchedTracks, schedType, schedTrackName, bond.dtSinks[sNo], optionalYn,
                        bond.sinkAmts[sNo], bond.sinkAmts[sNo], sequence);
            }
        }
        return true;
    }

    public boolean addTaxPaymentTracks(String instype) {
        int schedType = SchedTrack.FEDTAXPAYMENTTRACK;
        String schedTrackName = SchedTrack.FEDTAXPAYMENTTRACKNAME;
        SchedTrack schedTrack = new SchedTrack();
        char optionalYn = 'N';
        long sequence;
        int np = -1;
        Dates dtLast = new Dates();
        Dates dtNext = new Dates();

        if (bond.afterTaxCalcsYn == 'N')
            return true;

        if (bond.taxSchedRule.timeUnit == BondConsts.NOPERIODICITY) { // zero coupom and at amturity
            sequence = schedTrack.createSequence(bond.dtMaturity, schedType);
            addSchedTrack(noSchedTracks, schedType, schedTrackName, bond.dtMaturity, 'N', 0., 0., sequence);
            return true;
        }
        dtNext = bond.taxSchedRule.getNextSchedDate(BEFOREDATE, bond.dtSettle);
        Dates dtEnd = new Dates();
        dtEnd = bond.time.addYearsToDate(bond.dtMaturity, Limits.MAXPOSTMATURITYYEARS);
        while (dtNext.leDate(dtNext, dtEnd)) {
            sequence = schedTrack.createSequence(dtNext, schedType);
            addSchedTrack(noSchedTracks, schedType, schedTrackName, dtNext, 'N', 0., 0., sequence);
            dtLast = dtNext;
            dtNext = bond.taxSchedRule.getNextSchedDate(AFTERDATE, dtLast);
        }
        bond.dtLastTaxPayment = dtNext;
        return true;
    }

    public boolean addResetTracks(String instype) {
        if (!instype.equalsIgnoreCase(Instype.FLOATER))
            return true;
        if (UnAvails.isAvail(bond.cpnRt))
            return true;
        SchedTrack schedTrack = new SchedTrack();
        int schedType = SchedTrack.RESETRATETRACK;
        String schedTrackName = SchedTrack.RESETRATETRACKNAME;
        schedType = SchedTrack.RESETRATETRACK;
        Dates dtNextReset = new Dates();
        Dates dtLastReset = new Dates();
        int count = 0;
        char optionalYn = 'N';
        // reset dates are default from reset schedule (or enumerated)
        int ir = -1;
        while (++ir < bond.noResetDts) {
            long sequence = schedTrack.createSequence(bond.dtResets[ir], schedType);
            addSchedTrack(noSchedTracks, schedType, schedTrackName, bond.dtResets[ir], optionalYn, bond.resetRts[ir],
                    0., sequence);
        }
        return true;
    }

    public boolean addIRSSwapTracks(String instype) {
        if (!instype.equalsIgnoreCase(Instype.IRSSWAP))
            return true;
        boolean isOk = addPaylegTracks();
        if (isOk == false)
            return false;
        isOk = addPaylegResetTracks();
        if (isOk == false)
            return false;
        isOk = addReclegTracks();
        if (isOk == false)
            return false;
        isOk = addReclegResetTracks();
        if (isOk == false)
            return false;
        return true;
    }

    private boolean addPaylegTracks() {
        SchedTrack schedTrack = new SchedTrack();
        char optionalYn = 'N';
        Dates dtLast = bond.dtSettle;
        Dates dtNext = bond.paylegSchedRule.getNextSchedDate(SchedTracks.AFTERDATE, dtLast);
        int schedType = SchedTrack.PAYLEGTRACK;
        String schedTrackName = SchedTrack.PAYLEGTRACKNAME;
        while (dtNext.leDate(bond.dtMaturity)) {
            long sequence = schedTrack.createSequence(dtNext, schedType);
            addSchedTrack(noSchedTracks, schedType, schedTrackName, dtNext, optionalYn, 0., 0., sequence);
            dtLast = dtNext;
            dtNext = bond.paylegResetSchedRule.getNextSchedDate(SchedTracks.AFTERDATE, dtLast);
        }
        if (error.hasErrorYn == 'Y')
            return false;
        return true;
    }

    private boolean addReclegTracks() {
        SchedTrack schedTrack = new SchedTrack();
        char optionalYn = 'N';
        Dates dtLast = bond.dtSettle;
        Dates dtNext = bond.reclegSchedRule.getNextSchedDate(SchedTracks.AFTERDATE, dtLast);
        int schedType = SchedTrack.RECLEGTRACK;
        String schedTrackName = SchedTrack.RECLEGTRACKNAME;
        while (dtNext.leDate(bond.dtMaturity)) {
            long sequence = schedTrack.createSequence(dtNext, schedType);
            addSchedTrack(noSchedTracks, schedType, schedTrackName, dtNext, optionalYn, 0., 0., sequence);
            dtLast = dtNext;
            dtNext = bond.reclegSchedRule.getNextSchedDate(SchedTracks.AFTERDATE, dtLast);
        }
        if (error.hasErrorYn == 'Y')
            return false;
        return true;
    }

    private boolean addPaylegResetTracks() {
        if (bond.paylegFixedYn == 'N') {
            SchedTrack schedTrack = new SchedTrack();
            char optionalYn = 'N';
            int no = -1;
            int schedType = SchedTrack.PAYLEGRESETRATETRACK;
            String schedName = SchedTrack.PAYLEGRESETRATETRACKNAME;
            while (++no < bond.noPaylegResetDts) {
                long sequence = schedTrack.createSequence(bond.dtPaylegReset[no], schedType);
                addSchedTrack(noSchedTracks, schedType, schedName, bond.dtPaylegReset[no], optionalYn,
                        bond.paylegResetRt[no], 0., sequence);
            }
            return true;
        }
        return true;
    }

    private boolean addReclegResetTracks() {
        if (bond.reclegFixedYn == 'N') {
            SchedTrack schedTrack = new SchedTrack();
            char optionalYn = 'N';
            int no = -1;
            int schedType = SchedTrack.RECLEGRESETRATETRACK;
            String schedName = SchedTrack.RECLEGRESETRATETRACKNAME;
            while (++no < bond.noReclegResetDts) {
                long sequence = schedTrack.createSequence(bond.dtReclegReset[no], schedType);
                addSchedTrack(noSchedTracks, schedType, schedName, bond.dtReclegReset[no], optionalYn,
                        bond.reclegResetRt[no], 0., sequence);
            }
            return true;
        }
        return true;
    }

    public boolean addResetCaps(String instype) {
        if (!choices.inFloatList(instype)) {
            return true;
        }
        char optionalYn = 'N';
        SchedTrack schedTrack = new SchedTrack();
        int schedType = SchedTrack.RESETCAPTRACK;
        String schedTrackName = SchedTrack.RESETCAPTRACKNAME;
        int ir = -1;
        while (++ir < bond.noResetCaps) {
            long sequence = schedTrack.createSequence(bond.dtResetCaps[ir], schedType);
            addSchedTrack(noSchedTracks, schedType, schedTrackName, bond.dtResetCaps[ir], optionalYn,
                    bond.resetCaps[ir], bond.resetCaps[ir], sequence);
        }
        return true;
    }

    public boolean addResetFloors(String instype) {
        if (!choices.inFloatList(instype)) {
            return true;
        }
        char optionalYn = 'N';
        SchedTrack schedTrack = new SchedTrack();
        int schedType = SchedTrack.RESETFLOORTRACK;
        String schedTrackName = SchedTrack.RESETFLOORTRACKNAME;
        int ir = -1;
        while (++ir < bond.noResetFloors) {
            long sequence = schedTrack.createSequence(bond.dtResets[ir], schedType);
            addSchedTrack(noSchedTracks, schedType, schedTrackName, bond.dtResetFloors[ir], optionalYn,
                    bond.resetFloors[ir], bond.resetFloors[ir], sequence);
        }
        return true;
    }

    public boolean addCallTracks(String instype) {
        if (bond.noCallDts == 0)
            return true;
        int schedType = SchedTrack.CALLTRACK;
        String schedTrackName = SchedTrack.CALLTRACKNAME;
        SchedTrack schedTrack = new SchedTrack();
        char optionalYn = 'N';

        int ir = -1;
        while (++ir < bond.noCallDts) {
            long sequence = schedTrack.createSequence(bond.dtCall[ir], schedType);
            addSchedTrack(noSchedTracks, schedType, schedTrackName, bond.dtCall[ir], optionalYn, bond.callPrc[ir],
                    bond.callPrc[ir], sequence);
        }
        return true;
    }

    public boolean addPutTracks(String instype) {
        if (bond.noPutDts == 0)
            return true;
        int schedType = SchedTrack.PUTTRACK;
        String schedTrackName = SchedTrack.PUTTRACKNAME;
        SchedTrack schedTrack = new SchedTrack();
        char optionalYn = 'N';

        int ir = -1;
        while (++ir < bond.noPutDts) {
            long sequence = schedTrack.createSequence(bond.dtPut[ir], schedType);
            addSchedTrack(noSchedTracks, schedType, schedTrackName, bond.dtPut[ir], optionalYn, bond.putPrc[ir],
                    bond.putPrc[ir], sequence);
        }
        return true;
    }

    public boolean addIndexRatioTracks(String instype) {
        if (!instype.equalsIgnoreCase(Instype.INDEXED))
            return true;
        return true;
    }

    public boolean addIndexTracks(String instype) {
        if (!instype.equalsIgnoreCase(Instype.INDEXED))
            return true;
        SchedTrack schedTrack = new SchedTrack();
        return true;
    }

    public boolean addFxRateTracks(String instype) {
        if (bond.noFxDts == 0)
            return true;
        int schedType = SchedTrack.FXRATETRACK;
        String schedTrackName = SchedTrack.FXRATETRACKNAME;
        SchedTrack schedTrack = new SchedTrack();
        char optionalYn = 'N';

        int ir = -1;
        while (++ir < bond.noFxDts) {
            long sequence = schedTrack.createSequence(bond.dtFxRts[ir], schedType);
            addSchedTrack(noSchedTracks, schedType, schedTrackName, bond.dtFxRts[ir], optionalYn, bond.fxRts[ir],
                    bond.fxRts[ir], sequence);
        }
        return true;
    }

    public boolean addReinvestTracks(String instype) {
        if (bond.noReinRts == 0)
            return true;
        int schedType = SchedTrack.REINVESTRATETRACK;
        String schedTrackName = SchedTrack.REINVESTRATETRACKNAME;
        SchedTrack schedTrack = new SchedTrack();
        char optionalYn = 'N';

        int ir = -1;
        while (++ir < bond.noReinRts) {
            long sequence = schedTrack.createSequence(bond.dtReinvest[ir], schedType);
            addSchedTrack(noSchedTracks, schedType, schedTrackName, bond.dtReinvest[ir], optionalYn, bond.reinRt[ir],
                    bond.reinRt[ir], sequence);
        }
        return true;
    }

    public int getWholePeriods(Dates dtFromCpn, Dates dtToCpn) {
        int noPeriods = 0;
        boolean isOk = true;

        switch (bond.intSchedRule.timeUnit) {
            case BondConsts.MONTHTIMEUNIT:
                int noMonths = (int) dayCount.time.getMonthsBetween(dtFromCpn, dtToCpn, true);
                noPeriods = noMonths / bond.intSchedRule.repeatFactor;
                break;
            case BondConsts.WEEKTIMEUNIT:
                int noWeeks = dayCount.time.getWeeksBetween(dtFromCpn, dtToCpn);
                noPeriods = noWeeks / bond.intSchedRule.repeatFactor;
                break;
            case BondConsts.DAYTIMEUNIT:
                int noDays = dayCount.getDaycount(dtFromCpn, dtToCpn, bond.dtMaturity, bond.dcAccruedInt);
                noPeriods = noDays / bond.intSchedRule.repeatFactor;
                break;
            default:
                String errmsg = "Invalid schedule time unit detected";
                bond.error.setError(getClass().getName(), errmsg);
                return 0;
        }
        return noPeriods;
    }

    public void setTrackNames() {
        int i = 0;
        trackNames[i] = SchedTrack.ACCRUETRACKNAME;
        trackNames[++i] = SchedTrack.SINKTRACKNAME;
        trackNames[++i] = SchedTrack.PRINCIPALPAYTRACKNAME;
        trackNames[++i] = SchedTrack.PREPRINPAYTRACKNAME;
        trackNames[++i] = SchedTrack.PRINFACTORTRACKNAME;
        trackNames[++i] = SchedTrack.PIKTRACKNAME;
        trackNames[++i] = SchedTrack.ACCRUETRACKNAME;
        trackNames[++i] = SchedTrack.INTERESTPAYTRACKNAME;
        trackNames[++i] = SchedTrack.RESETRATETRACKNAME;
        trackNames[++i] = SchedTrack.CALLTRACKNAME;
        trackNames[++i] = SchedTrack.PUTTRACKNAME;
        trackNames[++i] = SchedTrack.INDEXRATIOTRACKNAME;
        trackNames[++i] = SchedTrack.INDEXCPITRACKNAME;
        trackNames[++i] = SchedTrack.FXRATETRACKNAME;
        trackNames[++i] = SchedTrack.USERCASHFLOWTRACKNAME;
        trackNames[++i] = SchedTrack.INDEXASSUMPTIONTRACKNAME;
        trackNames[++i] = SchedTrack.CASHPCTTRACKNAME;
        trackNames[++i] = SchedTrack.RESETCAPTRACKNAME;
        trackNames[++i] = SchedTrack.RESETFLOORTRACKNAME;
        trackNames[++i] = SchedTrack.PAYLEGRESETRATETRACKNAME;
        trackNames[++i] = SchedTrack.RECLEGRESETRATETRACKNAME;

    }

    public boolean sortSchedTracks() {
        // bond.noSchedTracks = bond.noCallDts + bond.noPikDts + bond.noSinkDts +
        // bond.noPutDts + bond.noResetDts + noIntPays;

        Collections.sort(schedTrackList, new Comparator<SchedTrack>() {
            @Override
            public int compare(SchedTrack o1, SchedTrack o2) {
                if (o1.schedSequence > o2.schedSequence)
                    return 1;
                if (o1.schedSequence == o2.schedSequence)
                    return 0;
                if (o1.schedSequence < o2.schedSequence)
                    return -1;
                return 0;
            }
        });

        return true;
    }

    public double getCouponFactor(int calcNo, Dates dt1, Dates dt2) {
        double perCount = 0;
        DayCount.DCMethod dcNum;
        DayCount.DCMethod dcDen;
        Dates dt1Last = new Dates();
        Dates dt1Next = new Dates();
        Dates dt2Last = new Dates();
        Dates dt2Next = new Dates();
        boolean ok = false;
        double per1Tail = 0.;
        int lastTrackNo = 0;
        if (dt1.eqDate(dt1, dt2))
            return 0.;
        dcNum = getDaycountNumerator(calcNo);
        dcDen = getDaycountDenominor(calcNo);
        SchedTrack schedTrack = new SchedTrack();
        schedTrack = getSchedTrackOnBefore(dt1, SchedTrack.ACCRUETRACK);
        dt1Last = schedTrack.schedDate;
        schedTrack = getSchedTrackAfter(dt1Last, SchedTrack.ACCRUETRACK);
        if (!schedTrack.found)
            return 0.;
        dt1Next = schedTrack.schedDate;

        // is dt2 inb dt1's period?
        if (dt2.leDate(dt2, dt1Next)) {
            int daysPeriod = dayCount.getDaycount(dt1Last, dt1Next, bond.dtMaturity, bond.dcAccruedInt);
            int days1Last = dayCount.getDaycount(dt1Last, dt1, bond.dtMaturity, dcNum);
            int days2Last = dayCount.getDaycount(dt1Last, dt2, bond.dtMaturity, dcNum);
            if (daysPeriod > 0)
                perCount = (double) (days2Last - days1Last) / daysPeriod;
            else
                perCount = 0.;
            return perCount;
        }
        // dt2 is in it's own periods > dt1's period
        // go from dt1 to dt1Next - the remainder of the peruod
        int days1Last = dayCount.getDaycount(dt1Last, dt1, bond.dtMaturity, dcNum);
        int daysPeriod = dayCount.getDaycount(dt1Last, dt1Next, bond.dtMaturity, dcDen);
        int days1Tail = daysPeriod - days1Last;
        if (daysPeriod == 0 && days1Tail == 0)
            per1Tail = 0;
        else if (daysPeriod == 0) {
            String errmsg = "zero days in a coupon period has been detected";
            bond.error.setError(getClass().getName(), errmsg);
            return 0.;
        } else
            per1Tail = (double) (daysPeriod - days1Last) / daysPeriod;
        // we go from end of dt1 period to start of dt2 perios
        ok = bond.checkBeforeDate(dt2);
        if (!ok)
            dt2Last = bond.dtDated;
        else {
            schedTrack = getSchedTrackOnBefore(dt2, SchedTrack.INTERESTPAYTRACK);
            dt2Last = schedTrack.schedDate;
        }
        ok = bond.checkAfterDate(dt2Last);
        if (!ok)
            dt2Next = bond.dtMaturity;
        else {
            schedTrack = getSchedTrackAfter(dt2Last, SchedTrack.INTERESTPAYTRACK);
            dt2Next = schedTrack.schedDate;
        }
        double noPeriod12 = getWholePeriods(dt1Next, dt2Last);
        // now we go from dt2Last to dt2
        int days2Nose = dayCount.getDaycount(dt2Last, dt2, bond.dtMaturity, dcNum);
        double per2Nose;
        int days2Period = dayCount.getDaycount(dt2Last, dt2Next, bond.dtMaturity, dcDen);
        if (days2Period == 0 && days2Period == 0)
            per2Nose = 0.;
        else
            per2Nose = (double) days2Nose / days2Period;
        perCount = per1Tail + noPeriod12 + per2Nose;
        return perCount;
    }

    public DayCount.DCMethod getDaycountNumerator(int calcNo) {
        switch (calcNo) {
            case 0: // . accrued interest
            case 1: // interest incomr
                return bond.dcAccruedInt;
            case 2: // yield
            case 3:
                return bond.dcYldPrc;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return bond.dcActAct;
            case 9:
                return bond.dcAccruedInt;
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                return bond.dcActAct;
            case 15:
            case 16:
            case 17:
                return bond.dcAccruedInt;
            case 18:
            case 19:
                return bond.dcAccruedInt;
            case 20:
                return bond.dcActAct;
            case 21:
            case 22:
            case 23:
                return bond.dcAccreAmort;
            case 24:
            case 25:
                return bond.dcActAct;
            case 26:
            case 27:
            case 28:

            case 29:
                return bond.dcActAct;
            case 30:
                return bond.dcAccruedInt;
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            default:
                return bond.dcAccruedInt;
        }

    }

    public DayCount.DCMethod getDaycountDenominor(int calcNo) {
        switch (calcNo) {
            case 0: // . accrued interest
            case 1: // interest incomr
                return bond.dcAccruedInt;
            case 2: // yield
            case 3:
                return bond.dcYldPrc;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return bond.dcActAct;
            case 9:
                return bond.dcAccruedInt;
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                return bond.dcActAct;
            case 15:
            case 16:
            case 17:
                return bond.dcAccruedInt;
            case 18:
            case 19:
                return bond.dcAccruedInt;
            case 20:
                return bond.dcActAct;
            case 21:
            case 22:
            case 23:
                return bond.dcAccreAmort;
            case 24:
            case 25:
                return bond.dcActAct;
            case 26:
            case 27:
            case 28:

            case 29:
                return bond.dcActAct;
            case 30:
                return bond.dcAccruedInt;
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            default:
                return bond.dcAccruedInt;
        }

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public SchedTrack getSchedTrackBefore(Dates dtSettle, int schedType) {
        int tNo = schedTrackList.size();
        Dates dtCurrent = new Dates();
        int tNoLast = UnAvails.NOTAVAILINT;
        SchedTrack schedTrack = new SchedTrack();
        while (--tNo >= 0) {
            if (schedType == schedTrackList.get(tNo).schedType || schedType == SchedTrack.ANYTYPETRACK) {
                dtCurrent = schedTrackList.get(tNo).schedDate;
                if (dtCurrent.ltDate(dtCurrent, dtSettle)) {
                    schedTrack = schedTrackList.get(tNo);
                    schedTrack.found = true;
                    currentTrackNo = tNo;
                    return schedTrack;
                } else { // extend the eariest scehdule found
                    tNoLast = tNo;
                }
            }
        }
        // extend back from last schedule having schedType
        if (UnAvails.isAvail(tNoLast)) {
            schedTrack = schedTrackList.get(tNoLast);
            schedTrack.schedDate = dayCount.time.dt1900;
            schedTrack.found = true;
            currentTrackNo = tNoLast;
            return schedTrack;
        }
        String errmsg = "could not find " + trackNames[schedType] + " before " + dtSettle.getDateString();
        bond.error.setError(getClass().getName(), errmsg);
        schedTrack.errmsg = errmsg;
        schedTrack.found = false;
        return schedTrack;
    }

    public SchedTrack getSchedTrackOnBefore(Dates dtSettle, int schedType) {
        int tNo = noSchedTracks;
        Dates dtCurrent = new Dates();
        int tNoLast = UnAvails.NOTAVAILINT;
        SchedTrack schedTrack = new SchedTrack();
        while (--tNo >= 0) {
            if (schedType == schedTrackList.get(tNo).schedType || schedType == SchedTrack.ANYTYPETRACK) {
                dtCurrent = schedTrackList.get(tNo).schedDate;
                if (dtCurrent.leDate(dtCurrent, dtSettle)) {
                    schedTrack = schedTrackList.get(tNo);
                    schedTrack.found = true;
                    currentTrackNo = tNo;
                    return schedTrack;
                } else
                    tNoLast = tNo;
            }
        }
        if (UnAvails.isAvail(tNo) && UnAvails.isAvail(tNoLast)) {
            schedTrack = schedTrackList.get(tNoLast);
            schedTrack.found = true;
            currentTrackNo = tNoLast;
            return schedTrack;
        }
        String errmsg = "could not find " + trackNames[schedType] + " before " + dtSettle.getDateString();
        bond.error.setError(getClass().getName(), errmsg);
        schedTrack.errmsg = errmsg;
        schedTrack.found = false;
        return schedTrack;
    }

    public SchedTrack getSchedTrackOn(Dates dtSettle, int schedType) {
        int tNo = -1;
        Dates dtCurrent = new Dates();
        SchedTrack schedTrack = new SchedTrack();
        while (++tNo < noSchedTracks) {
            if (schedType == schedTrackList.get(tNo).schedType || schedType == SchedTrack.ANYTYPETRACK) {
                dtCurrent = schedTrackList.get(tNo).schedDate;
                if (dtCurrent.eqDate(dtCurrent, dtSettle)) {
                    schedTrack = schedTrackList.get(tNo);
                    schedTrack.found = true;
                    currentTrackNo = tNo;
                    return schedTrack;
                }
            }
        }
        schedTrack.found = false;
        return schedTrack;
    }

    public SchedTrack getSchedTrackOnAfter(Dates dtSettle, int schedType) {
        Dates dtCurrent = new Dates();
        SchedTrack schedTrack = new SchedTrack();
        int tNoLast = UnAvails.NOTAVAILINT;
        int tNo = -1;
        while (++tNo < noSchedTracks) {
            if (schedType == schedTrackList.get(tNo).schedType) {
                dtCurrent = schedTrackList.get(tNo).schedDate;
                if (dtCurrent.geDate(dtCurrent, dtSettle)) {
                    schedTrack = schedTrackList.get(tNo);
                    schedTrack.found = true;
                    currentTrackNo = tNo;
                    return schedTrack;
                }
                tNoLast = tNo;
            }
        }
        if (UnAvails.isAvail(tNoLast)) {
            schedTrack = schedTrackList.get(tNoLast);
            schedTrack.schedDate = dtInfinity;
            schedTrack.found = true;
            currentTrackNo = tNoLast;
            return schedTrack;
        }
        String errmsg = "could not find schedule " + trackNames[schedType] + " on or after " + dtSettle.getDateString();
        bond.error.setError(getClass().getName(), errmsg);
        schedTrack.found = false;
        schedTrack.errmsg = errmsg;
        return schedTrack;
    }

    public SchedTrack getSchedTrackAfter(Dates dtSettle, int schedType) {
        Dates dtCurrent = new Dates();
        SchedTrack schedTrack = new SchedTrack();
        int tNoLast = UnAvails.NOTAVAILINT;
        int tNo = -1;
        while (++tNo < noSchedTracks) {
            if (schedType == schedTrackList.get(tNo).schedType || schedType == SchedTrack.ANYTYPETRACK) {
                dtCurrent = schedTrackList.get(tNo).schedDate;
                if (dtCurrent.gtDate(dtCurrent, dtSettle)) {
                    schedTrack = schedTrackList.get(tNo);
                    schedTrack.found = true;
                    currentTrackNo = tNo;
                    return schedTrack;
                }
                tNoLast = tNo;
            }
        }
        schedTrack.found = false;
        String errmsg = "could not find schedule " + trackNames[schedType] + " on or after " + dtSettle.getDateString();
        schedTrack.found = false;
        schedTrack.errmsg = errmsg;
        return schedTrack;

    }

    public SchedTrack getNextSchedSequenceAfter(long sequence) {
        SchedTrack schedTrack = new SchedTrack();
        int no = -1;
        int count = schedTrackList.size();
        while (++no < count) {
            if (schedTrackList.get(no).schedSequence > sequence) {
                return schedTrackList.get(no);
            }
        }

        schedTrack.errmsg = "Could not find schedule after sequence " + sequence;
        schedTrack.found = false;
        schedTrack = schedTrackList.get(count - 1);
        return schedTrack;
    }

    public SchedTrack getNextSchedSequenceBefore(long sequence) {
        SchedTrack schedTrack = new SchedTrack();
        int count = schedTrackList.size();
        int no = count;
        while (--no > 0) {
            if (schedTrackList.get(no).schedSequence < sequence) {
                return schedTrackList.get(no);
            }
        }
        schedTrack.errmsg = "Could not find schedule before sequence " + sequence;
        schedTrack.found = false;
        schedTrack = schedTrackList.get(0);
        return schedTrack;
    }

    public SchedTrack getFirstTrack(int schedType) {
        SchedTrack schedTrack = new SchedTrack();
        int no = -1;
        while (++no < schedTrackList.size()) {
            if (schedType == schedTrackList.get(no).schedType) {
                schedTrack = schedTrackList.get(no);
                schedTrack.found = true;
                return schedTrack;
            }
        }
        schedTrack.found = false;
        return schedTrack;
    }

    public SchedTrack getLastTrack(int schedType) {
        SchedTrack schedTrack = null;
        int no = schedTrackList.size();
        while (--no >= 0) {
            if (schedType == schedTrackList.get(no).schedType) {
                schedTrack = schedTrackList.get(no);
                schedTrack.found = true;
                return schedTrack;
            }
        }
        schedTrack.found = false;
        return schedTrack;
    }
}

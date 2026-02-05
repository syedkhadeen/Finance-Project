package app;

public class Limits {
    // maximun sizes for Bond arrays data
    static final int MAXCASHFLOWS = 2400;
    static final int MAXCALLS = 2400;
    static final int MAXSINKS = 1200;
    static final int MAXKEYRATEDURATIONS = 100;
    static final int MAXACCREAMORTSCHEDS = 1000;
    static final int MAXRESETS = 2400;
    static final int MAXFXRATES = 1200;
    static final int MAXSCHEDTYPES = 100;
    static final int MAXUSERDATES = 1200;
    static final int MAXPIKS = 1200;
    static final int MINDUMPCOUNT = 1;
    static final double MININFLATION = -20.;
    static final int MAXNOTIFYDAYS = 366;

    static final double DOUBLEEQUALS = .00000001;
    static final int MAXYIELDCURVEDAYDIFF = 5; // any spot curve within 5 days of request will be used if none found

    // yield curves
    static final int MAXYIELDCURVEMONTHS = 600;
    static final int MINFIXINGRATES = 3;
    static final int MINDISCGRATES = 3;
    static final int MAXFIXINGRATES = 100;
    static final int MAXDISCRATES = 100;

    static final int MINMATLEN = 1;
    static final int MAXMATLEN = 720;

    static final int MINYIELDPOINTS = 5;
    static final int MAXYIELDPOINTS = 100;
    static final int MAXYIELDRECPARTS = 25;

    static final int MAXSPOTRATES = 200;

    static final int MAXPARMULTIPLE = 5; // maximium resonal spot rate calculation

    static final int MINHORIZONPERIODS = 1;
    static final int MAXHORIZONPERIODS = 156;

    static final int MAXPOSTMATURITYYEARS = 1;

    static final double INFINITY = 10e10;

    // maximium minium for analytics
    static final int MAXCALCS = 500;
    static final int MAXWEEKENDDAYS = 3;
    static final int MAXHOLDINGS = 2500;

    static final int MINREQUESTCHARS = 6;
    static final int MAXSTRPARTS = 100; // maximum parts to a parmeter string with
    // maximum array sizes
    static final int MAXDESCRCHARS = 192;
    static final int MAXERRORCHARS = 256;
    static final int MAXPARAMETERS = 2000;
    static final int MAXWARINGMESSAGES = 200;
    static final int MAXMAPCOLUMNS = 500;
    // range boundaries
    static final int MAXPORFOLIOHOLDINGS = 2000;
    static final int MAXPORTFOLIOCALCS = 50;
    static final int MAXHOLIDAYS = 20000;

    static final int MAXTAXPAYDATES = 100;

    static final int MAXSECTYPEPARAMS = 120;
    static final int MAXSECURITYTYPES = 1000;
    static final int MAXDATEPARTS = 3;
    static final int MAXMKTPRICES = 1200;
    static final int MAXINDEXES = 600;
    static final int MAXACCREAMORTS = 2400;
    static final double MAXACRUESIZE = 1000.;
    static final double MINPREPAYSPEED = 0.;
    static final double MAXPREPAYSPEED = 100.;
    static final double MINCOUPON = -99.;
    static final double MAXCOUPON = 50.;
    static final double MINYIELD = -200.;
    static final double MAXYIELD = 200.;
    static final double MINDOUBLE = -1.;
    static final double MAXDOUBLE = 10000.;

    static final int MAXPARBONDS = 200;
    static final double MINNONE = 0;
    static final double MAXNONE = 0.;
    static final int MAXDAYOFMONTH = 31;
    static final int MINDAYOFMONTH = 1;
    static final int MAXDAYOFWEEK = 6;
    static final int MINDAYOFWEEK = 1;
    static final int MAXMONTHOFYEAR = 12;
    static final int MINYEAR = 1900;
    static final int MAXYEAR = 3000;
    static final int MAXREPEATFACTOR = 60;
    static final int MAXWEEKOFMONTH = 5;
    static final int MAXYEAROFOCCUR = 3000;
    static final int MINYEAROFOCCUR = 1900;
    static final double MAXACCRURESIZE = 1000.;
    static final int MAXPASSTHRUAGE = 24;
    static final int MINPASSTHRUAGE = 0;
    static final double MAXBOOKPRICE = 100.;
    static final double MAXFACTOR = 100.;
    static final double MINFACTOR = .001;
    static final double MAXBOOKVALUE = 200.;
    static final double MAXPRICE = 200.;
    static final double MINPRICE = -1.;
    static final int MAXCONTRACTSIZE = 100000000;
    static final double MAXCONVERSIONRATIO = 100000.;
    static final double MINCONVERSIONRATIO = .1;

    static final int MAXFACTORS = 1200;
    static final double MAXMONEY = 999000000000.;
    static final double MINMONEY = .01;
    static final int MAXEXDIVOFFSET = 30;
    static final int MINEXDIVOFFSET = 3;
    static final int MAXDELAYDAYS = 134;

    static final int MAXRESETMULYIPLIERS = 200;
    static final int MINDELAYDAYS = 14;
    static final double MAXDURATIONSHIFT = 100.;
    static final double MINDURATIONSHIFT = 1.;
    static final double MAXDEFAULTRATE = 99.;
    static final double MAXTERM = 12. * 10.;
    static final double MAXFXRATE = 900000000.;
    static final double MINFXRATE = .00000001;
    static final int MAXINDEXDELAYMONTHS = 6;
    static final int MININDEXDELAYMONTHS = 1;
    static final int MAXINDEXDELAYWEEKS = 52;
    static final int MININDEXDELAYWEEKS = 1;
    static final int MAXINDEXDELAYDAYS = 366;
    static final int MININDEXDELAYDAYS = 7;
    static final double MAXINDEXRATIO = 1000.;

    static final double MININDEXVALUE = 1.;
    static final double MAXINDEXVALUE = 100000.;

    static final double MININDEXRATIO = .01;
    static final double MAXINFLATION = 500.;
    static final double MINNFLATION = -20.;
    static final int MINCONTRACTSIZE = 1000;
    static final int MAXODDPERIOD = 2;
    static final double MAXPASSTHRUFACTOR = 1.;
    static final double MINPASSTHRUFACTOR = .00001;
    static final int MAXWARM = 480;
    static final int MINWARM = 1;
    static final double MAXPCT = 100.;
    static final double MINPCT = .001;
    static final double MAXWALA = 480;
    static final double MINWALA = 0;
    static final double MAXRATE = 100.;

    static final double MAXRATEVOL = 100.;

    static final double MINRATEVOL = 0.;

    static final double MINSPOTINTERVAL = CalUnits.ONETWELVEYEAR;
    static final double MAXSPOTINTERVAL = 3.; // 3 years

    static final double MINPASSPAYOFF = 0.;
    static final double MAXPASSPAYOFF = .0001;
    static final double MINRATE = -99.;
    static final int MAXSDECIMAL = 14;
    static final int MINSDECIMAL = 1;
    static final double MINTAXRATE = 0.;
    static final double MAXTAXRATE = 99.;
    static final int MINMONTHOFYEAR = 1;
    static final int MINREPEATFACTOR = 1;
    static final int MINWEEKOFMONTH = 1;
    static final int MAXZERO = 0;
    static final int MAXDUMPCOUNT = 1000;
    static final double MASBASISPTSHIFT = 1.0;
    static final int MINTERM = 2;
    static final int MINCALLNOTIFYDAYS = 30;
    static final int MAXCALLNOTIFYDAYS = 180;
    static final int MAXDATEFORMAT = 12;
    static final double MINCSRATE = 0.;
    static final double MAXCSRATE = 100.;
    static final int MINCSMATLEN = 0;
    static final int MAXCSMATLEN = 720;
    static final int MINDAYSCAPITALGAINSHORT = 10;
    static final int MAXDAYSCAPITALGAINSHORT = 367;
    static final double MINCPIVALUE = 1.;
    static final double MAXCPIVALUE = 100000.;
    static final double MAXBASISPTSHIFT = 200.0;
    static final double MINBASISPTSHIFT = 0.0;
    static final int MINZERO = 0;
    static final int MAXDECIMAL = 14;
    static final int MINDECIMAL = 0;
    static final double MINSERVICERATE = 0.;
    static final double MAXSERVICERATE = 200.;

    static final int MAXALLOWEDERRORS = 100;
    static final int MINSFIG = 0;
    static final int MINNOTIFYDAYS = 30;
    static final double MAXPCTREDEEMVALUE = 100.;
    static final int MAXREDEEMS = 1200;
    static final double MAXFLOOR = 10.;
    static final double MINFLOOR = -5.;
    static final double MAXCAP = 10.;
    static final double MINCAP = -5.;

    static final int PASTDAYS = 3650;

    static final int MINCURVEMATLEN = 30;

    static final int MAXREQUESTCHARS = 25000;

    // maximum size of list entries
    static final int MAXINSTYPES = 50;
    static final int MAXACCREAMORTCONTROL = 4;
    static final int MAXCOMPOUNDFREQ = 6;
    static final int MAXACCREAMORTCALC = 6;
    static final int MAXACCRUALBASES = 32;
    static final int MAXPASSMODEL = 5;
    static final int MAXSTRIPTYPE = 3;
    static final int MAXBOOKCALCS = 4;
    static final int MAXYIELDINTERPOLATION = 2;
    static final int MINACCRUESIZE = 1;
    static final int MAXACCRUESIZE = 10000;
    static final int MAXTIMEUNITS = 3;
    static final int MAXBONDEQVYIELD = 4; /* Annual, Semi-Annual, Quarterly, Monthly */
    static final int MAXBOOKCALMETH = 5; /* None, Straight Line, Constant yield, Bond Eqvyield, Prospective yield */
    static final int MAXBOOKYIELDOPTION = 3; /* yield purchase, yield worst, yield next call */
    static final int MAXCALLPRICETYPE = 2; /* P = price array, A = amortized */
    static final int MAXCALLTYPE = 2; /* A = american, E - european */
    static final int MAXCOUPONQUOTETYPE = 5; /* N = normal, C = compound, */
    static final int MAXINDEXINTERPOLATION = 6;
    static final int MAXMATURITYLENGTHOP = 3;
    static final int MAXPASSTHRUPOJECTMETH = 4;
    static final int MAXPRICEQUOTETYPE = 3;
    static final int MAXYIELDCURVEINTERPOLATE = 2;
    static final int MAXYIELDQUOTETYPE = 3;
    static final int MAXLEGTYPE = 5;
    static final int MAXSWAPPAYTYPE = 3;
    static final int MAXHOLIDAYOFFSETS = 4;
    static final int MAXCOUNTRIES = 248;
    static final int MAXCREDITDEFAULTMODEL = 4;
    static final int MAXDERIVETYPE = 3;
    static final int MAXCREDIRDEFAULTMODEL = 5;
    static final int MAXSFIG = 14;
    static final int MXNOTIFYDAYS = 366;
    static final int MAXSCHEDTRACKS = 6000;

    static final int MAXERRORCOUNT = 2;
    static final int MAXTOPSWAPS = 1000;

    static final int MAXWARNINGCOUNT = 2;
    static final int MINWEIGHT = 0;
    static final int MAXWEIGHT = 100;
    static final int MAXSECDESCR = 40;

    static final double MAXRETURNTOREPORT = 50.;

}
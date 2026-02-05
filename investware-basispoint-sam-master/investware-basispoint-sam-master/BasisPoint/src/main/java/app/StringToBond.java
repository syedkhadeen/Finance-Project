package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class StringToBond {
    Bond bond;
    Error error;
    ParamMap paramMap;
    String dtFormat = "MM/DD/YYYY";
    String userCleanParamString;
    DayCount dayCount;
    Choices choices;
    HashMap<String, ParamMap> paramMapHashMap = new HashMap<>(500);
    String isInputJSONYn = "Y";
    String rootDirectory;

    StringToBond(Error error, Choices choices, DayCount dayCount) {
        this.error = error;
        this.choices = choices;
        this.dayCount = dayCount;
        setParamMaps();
    }

    StringToBond(Bond bond, String isInputJSONYn, Error error, Choices choices, DayCount dayCount) {
        this.isInputJSONYn = isInputJSONYn;
        this.error = error;
        this.choices = choices;
        this.dayCount = dayCount;
        this.bond = bond;
        setParamMaps();
    }

    public void setBond(Bond bond) {
        this.bond = bond;
    }

    public void setParamMaps() {
        paramMapHashMap.put("AACONTROL", new ParamMap("AACONTROL", Domains.DOMAINSTRING, 'N', 'Y',
                "accreAmortControlList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the rule to use in controlling accretion/amortization based on whether the security was purchased at a discount or premium."));
        paramMapHashMap.put("AAYIELD",
                new ParamMap("AAYIELD", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINYIELD, Limits.MAXYIELD,
                        "Indicates the yield to use in determining accretion/amortization for book value."));
        paramMapHashMap.put("ACCOUNT", new ParamMap("ACCOUNT", Domains.DOMAINSTRING, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Identifies the account that owns this security."));
        paramMapHashMap.put("ACCRUEDINTDAYCOUNTRNDYN", new ParamMap("ACCRUEDINTDAYCOUNTRNDYN", Domains.DOMAINYESNO, 'N',
                'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicate whether to round or truncate the accured interest adjustment for the daycount part of the calculation"));
        paramMapHashMap.put("ACCRUEDDTOCCUR",
                new ParamMap("ACCRUEDDTOCCUR", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates a seed date used to determine when an accrual or other event occurs in the future"));
        paramMapHashMap.put("ACCRUEDINTMETH",
                new ParamMap("ACCRUEDINTMETH", Domains.DOMAINSTRING, 'N', 'Y', "accruedAdjustList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the method to use in adjusting accrued interest for bond size and rounding."));
        paramMapHashMap.put("ACCRUEDINTPRINRNDYN", new ParamMap("ACCRUEDINTPRINRNDYN", Domains.DOMAINYESNO, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicate whether to round or truncate the accured interest adjustment for the principal part of the calculation"));
        paramMapHashMap.put("ACCRUEDINTRNDYN",
                new ParamMap("ACCRUEDINTRNDYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates whether  to round or truncate the accrued interest calculation for net payment."));
        paramMapHashMap.put("ACCRUEDINTSIZE", new ParamMap("ACCRUEDINTSIZE", Domains.DOMAININT, 'N', 'N', "noList", 1,
                1, Limits.MAXACCRURESIZE,
                "Indicates the unit calculation size to use for accrued interest when accrued interest is based on a unit  size and the scaled by the actual face amount of the bond."));
        paramMapHashMap.put("ACCRUEDINTSIZERNDYN", new ParamMap("ACCRUEDINTSIZERNDYN", Domains.DOMAINYESNO, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicate whether to round or truncate the accured interest adjustment for the bond size part of the calculation"));
        paramMapHashMap.put("ADJUSTMATURITYYN",
                new ParamMap("ADJUSTMATURITYYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates whether to adjust the maturity payment of an index linked for inflatio"));
        paramMapHashMap.put("AFTERTAXCALCSYN",
                new ParamMap("AFTERTAXCALCSYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Indicates whether to report analytics on a pre-tax or after tax basis."));
        paramMapHashMap.put("ALLOWBADPARAMYN",
                new ParamMap("ALLOWBADPARAMYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates whether to report an error message when an invalid parameter is entered."));
        paramMapHashMap.put("ALLOWEDPASSAGE", new ParamMap("ALLOWEDPASSAGE", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MINPASSTHRUAGE, Limits.MAXPASSTHRUAGE,
                "Indicates the maximum staleness or age allowed between the last factor date and the settle date for a pass-thru."));
        paramMapHashMap.put("ALLOWNEGCPNYN", new ParamMap("ALLOWNEGCPNYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether to allow negative forward rates or not."));
        paramMapHashMap.put("ALLOWNEGPRCYLDYN",
                new ParamMap("ALLOWNEGPRCYLDYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Indicates whether to allow a negative prices or yields."));
        paramMapHashMap.put("ALLOWNEGRTYN", new ParamMap("ALLOWNEGRTYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether to allow negative coupon rates or not."));
        paramMapHashMap.put("AUSACCRUEDYN", new ParamMap("AUSACCRUEDYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether the bond is subject to the Australia accrual convention for odd coupon bonds."));
        paramMapHashMap.put("BASACCINT",
                new ParamMap("BASACCINT", Domains.DOMAINSTRING, 'N', 'Y', "accrueBasisList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Identifies the accrual basis to use for the accrued interest calculation."));
        paramMapHashMap.put("BASACCREAMORT",
                new ParamMap("BASACCREAMORT", Domains.DOMAINSTRING, 'N', 'Y', "accrueBasisList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Identifies the accrual basis to use for the accre/amortization calculation."));
        paramMapHashMap.put("BASBONDEQV",
                new ParamMap("BASBONDEQV", Domains.DOMAINSTRING, 'N', 'Y', "accrueBasisList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Identifies the accrual basis to use for the bond equivalent calculation."));
        paramMapHashMap.put("BASDELAYDAYS",
                new ParamMap("BASDELAYDAYS", Domains.DOMAINSTRING, 'N', 'Y', "accrueBasisList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Identifies the accrual basis to use for dalay days calculation for mortgages."));
        paramMapHashMap.put("BASIDXINTERP",
                new ParamMap("BASIDXINTERP", Domains.DOMAINSTRING, 'N', 'Y', "accrueBasisList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Identifies the accrual basis to use for index interpolation of CPI values."));
        paramMapHashMap.put("BASODD1ST", new ParamMap("BASODD1ST", Domains.DOMAINSTRING, 'N', 'Y', "accrueBasisList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Identifies the accrual basis to use for the accrued interest calculation in the odd first period."));
        paramMapHashMap.put("BASODDLAST", new ParamMap("BASODDLAST", Domains.DOMAINSTRING, 'N', 'Y', "accrueBasisList",
                1, Limits.MINNONE, Limits.MAXNONE,
                "Identifies the accrual basis to use for the accrued interest calculation in the odd last period."));
        paramMapHashMap.put("BASPAYLEG", new ParamMap("BASPAYLEG", Domains.DOMAINSTRING, 'N', 'Y', "accrueBasisList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Identifies the accrual basis to use for accrued interest calculation for the pay leg of an IRS Swap."));
        paramMapHashMap.put("BASRECLEG", new ParamMap("BASRECLEG", Domains.DOMAINSTRING, 'N', 'Y', "accrueBasisList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Identifies the accrual basis to use for accrued interest calculation for the receive leg of an IRS Swap."));
        paramMapHashMap.put("BASRESETPAYLEG", new ParamMap("BASRESETPAYLEG", Domains.DOMAINSTRING, 'N', 'Y',
                "accrueBasisList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Identifies the accrual basis to use for accrued interest calculation for the reset pay leg of an IRS Swap."));
        paramMapHashMap.put("BASRESETRECLEG", new ParamMap("BASRESETRECLEG", Domains.DOMAINSTRING, 'N', 'Y',
                "accrueBasisList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Identifies the accrual basis to use for accrued interest calculation for the reset receive leg of an IRS Swap."));
        paramMapHashMap.put("BASYLDPRC", new ParamMap("BASYLDPRC", Domains.DOMAINSTRING, 'N', 'Y', "accrueBasisList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Identifies the accrual basis to use for accrued interest calculation to use for the price or yield calculation."));
        paramMapHashMap.put("BONDEQVYLD", new ParamMap("BONDEQVYLD", Domains.DOMAINSTRING, 'N', 'Y', "bondEqvList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates the compound frequency used to report yields."));
        paramMapHashMap.put("BOOKCALCMETH",
                new ParamMap("BOOKCALCMETH", Domains.DOMAINSTRING, 'N', 'Y', "bookCalcList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the method to use in calculating book value and accretion/amortization."));
        paramMapHashMap.put("BOOKPRC", new ParamMap("BOOKPRC", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                Limits.MINPRICE, Limits.MAXPRICE, "Indicates the current book price for use in accrual calculations."));
        paramMapHashMap.put("BOOKPRINFACTOR", new ParamMap("BOOKPRINFACTOR", Domains.DOMAINDOUBLE, 'N', 'N', "noList",
                1, Limits.MINFACTOR, Limits.MAXFACTOR, "Indicates the current book value as a fraction of par value."));
        paramMapHashMap.put("BOOKVALUE", new ParamMap("BOOKVALUE", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1,
                Limits.MINMONEY, Limits.MAXMONEY, "Indicates the current book value of this bond."));
        paramMapHashMap.put("BOOKYLDOPT",
                new ParamMap("BOOKYLDOPT", Domains.DOMAINSTRING, 'N', 'Y', "bookYieldOption", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the yield type to use in calculating book value and accretion/amortization."));
        paramMapHashMap.put("CALCAFTERMATDTYN", new ParamMap("CALCAFTERMATDTYN", Domains.DOMAINYESNO, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to extend cashflows beyond maturity to assess bond returns over a horizon period greater than maturity date."));
        paramMapHashMap.put("CALCCPNDTSYN", new ParamMap("CALCCPNDTSYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to compute payment dates or whether they are supplied for irregular schedule payment  securities."));
        paramMapHashMap.put("CALCDELIMITER",
                new ParamMap("CALCDELIMITER", Domains.DOMAINSTRING, 'N', 'Y', "delimitList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Indicates the delimiter separating one calculation from another."));
        paramMapHashMap.put("CALCFACTORSYN", new ParamMap("CALCFACTORSYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to compute pass-thru factors based on a prepayment model or whether factors are supplied."));
        paramMapHashMap.put("CALCFLOATRATESYN",
                new ParamMap("CALCFLOATRATESYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates whether to compute floating base rates or whether they are supplied."));
        paramMapHashMap.put("CALCONLYPORTSTATSYN",
                new ParamMap("CALCONLYPORTSTATSYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates whether to compute only portfolio totals when multiple securities are entered."));
        paramMapHashMap.put("CALCONSETTLEYN", new ParamMap("CALCONSETTLEYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to report a cashflow on settle date if the bond pays interest on that date."));
        paramMapHashMap.put("CALCREQUEST",
                new ParamMap("CALCREQUEST", Domains.DOMAININT, 'Y', 'N', "noList", 1, 0, Limits.MAXCALCS,
                        "Indicates the set of calculations to perform for one analytical or accounting request."));
        paramMapHashMap.put("CALCRESETDTSYN", new ParamMap("CALCRESETDTSYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to compute floating rate reset dates based on a schedule or whether they are supplied."));
        paramMapHashMap.put("CALCTOTALSYN", new ParamMap("CALCTOTALSYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether security total should be calculated."));
        paramMapHashMap.put("CALLABLEYN", new ParamMap("CALLABLEYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates if the bond has call dates."));
        paramMapHashMap.put("CALLNOTIFYDAYS",
                new ParamMap("CALLNOTIFYDAYS", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINCALLNOTIFYDAYS,
                        Limits.MAXCALLNOTIFYDAYS,
                        "Indicates the number of days in advance that a notice must be sent prior to calling a bond."));
        paramMapHashMap.put("CALLPRC", new ParamMap("CALLPRC", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINPRICE, Limits.MAXPRICE, "Indicates the call prices specified for a callable bond."));
        paramMapHashMap.put("CALLPRCTYPE", new ParamMap("CALLPRCTYPE", Domains.DOMAINSTRING, 'N', 'Y',
                "callPutPriceTypeList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the method to use in obtaining the call price. Based on a list of call prices or based on an amortization calculation."));
        paramMapHashMap.put("CALLTYPE",
                new ParamMap("CALLTYPE", Domains.DOMAINSTRING, 'N', 'Y', "callTypeList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the type of call allowed on this bond. American call or European call."));
        paramMapHashMap.put("CANADADACCRUEDYN",
                new ParamMap("CANADADACCRUEDYN", Domains.DOMAINYESNO, 'N', 'N', "accruedAdjustList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Indicates whether to follow the Canadian accrued interest convention."));
        paramMapHashMap.put("CLAIMPCT", new ParamMap("CLAIMPCT", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                Limits.MINPCT, Limits.MAXPCT,
                "Indicates the percent of orginal par  claimed or received once a default has been legally settled. Used for Credit Default Swaps."));
        paramMapHashMap.put("CLEANPRCYN", new ParamMap("CLEANPRCYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether the price specified is a clean price and excludes accrued interest or a dirty price including accrued interest."));
        paramMapHashMap.put("CLEANYIELDYN",
                new ParamMap("CLEANYIELDYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates whether the supplied yield is a yield based on a clean or dirty price."));
        paramMapHashMap.put("COMMENT",
                new ParamMap("COMMENT", Domains.DOMAINIGNORE, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "A user defined comment to note or describe something about this security or request."));
        paramMapHashMap.put("CONTRACTSIZE", new ParamMap("CONTRACTSIZE", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1,
                Limits.MINCONTRACTSIZE, Limits.MAXCONTRACTSIZE, "Indicates the contract size for an interest future."));
        paramMapHashMap.put("CONVERSIONPRC",
                new ParamMap("CONVERSIONPRC", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINPRICE,
                        Limits.MAXPRICE,
                        "Indicates the equity price at which the bond can be converted to one share of stock."));
        paramMapHashMap.put("CONVERSIONRATIO", new ParamMap("CONVERSIONRATIO", Domains.DOMAINDOUBLE, 'N', 'N', "noList",
                1, Limits.MINCONVERSIONRATIO, Limits.MAXCONVERSIONRATIO,
                "Indicates the number of shares that can be obtained by converting one bond (par of 1000). Also the ratio of par value divided by share price. ."));
        paramMapHashMap.put("CONVERTIBLEYN", new ParamMap("CONVERTIBLEYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether this bond has a option to convert to stock."));
        paramMapHashMap.put("CONVERTOPT", new ParamMap("CONVERTOPT", Domains.DOMAINSTRING, 'N', 'Y',
                "conversionOptionList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether you wish to always convert the bond or convert only if doing so is advantageous."));
        paramMapHashMap.put("CONVERTTOLOCALYN", new ParamMap("CONVERTTOLOCALYN", Domains.DOMAINYESNO, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to convert cashflow and prices to local currencies when calculating prices or yields and other analytics."));
        paramMapHashMap.put("COUNTRY",
                new ParamMap("COUNTRY", Domains.DOMAINSTRING, 'N', 'Y', "countryList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Indicates ISO abbreviation for the country in which the bond was issued."));
        paramMapHashMap.put("CPNFLOOR",
                new ParamMap("CPNFLOOR", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINCOUPON,
                        Limits.MAXCOUPON, "Indicates the smallest coupon rate allowed for a floating rate bond."));
        paramMapHashMap.put("CPNRTPAYLEG",
                new ParamMap("CPNRTPAYLEG", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINCOUPON,
                        Limits.MAXCOUPON, "Indicates the fixed rate coupon for the pay leg of an interest rate swap."));
        paramMapHashMap.put("CPNQUOTETYPE", new ParamMap("CPNQUOTETYPE", Domains.DOMAINSTRING, 'N', 'Y',
                "couponQuoteList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the how to convert a quoted coupon rate into an annual coupon rate which is used to calculate interest payments."));
        paramMapHashMap.put("CPNRATEQUOTE", new ParamMap("CPNRATEQUOTE", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                Limits.MINCOUPON, Limits.MAXCOUPON, "Indicates the coupon rate quoted on the bond."));
        paramMapHashMap.put("CPNRTRECLEG",
                new ParamMap("CPNRTRECLEG", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINCOUPON,
                        Limits.MAXCOUPON,
                        "Indicates the coupon rate to use for the receive leg of an interest rate swap."));
        paramMapHashMap.put("CPNRT", new ParamMap("CPNRT", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                Limits.MINCOUPON, Limits.MAXCOUPON, "Indicates the coupon rate to use for this bond."));
        paramMapHashMap.put("CREDITDEFMODEL", new ParamMap("CREDITDEFMODEL", Domains.DOMAINSTRING, 'N', 'Y',
                "creditDefaultModelList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the model to use in setting default percentages when calculating interest and principal payments."));
        paramMapHashMap.put("CREDITAGENCY", new ParamMap("CREDITAGENCY", Domains.DOMAINSTRING, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates the risk free or credit spread agency that sets the credit rating supplied for the bond."));
        paramMapHashMap.put("CREDITRATING", new ParamMap("CREDITRATING", Domains.DOMAINSTRING, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates the risk free credit or spread rating for the bond."));
        paramMapHashMap.put("CREDITAGENCYRF",
                new ParamMap("CREDITAGENCYRF", Domains.DOMAINSTRING, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the risk free credit agency that sets the credit rating supplied for the bond."));
        paramMapHashMap.put("CREDITRATINGRF", new ParamMap("CREDITRATINGRF", Domains.DOMAINSTRING, 'N', 'N', "noList",
                1, Limits.MINNONE, Limits.MAXNONE, "Indicates the risk free credit rating for the bond."));
        paramMapHashMap.put("CREDITAGENCYSPRD", new ParamMap("CREDITAGENCYSPRD", Domains.DOMAINSTRING, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the credit agency that sets the credit rating for the credit spreads supplied for the bond."));
        paramMapHashMap.put("CREDITRATINGSPRD",
                new ParamMap("CREDITAGENCYSPRD", Domains.DOMAINSTRING, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Indicates the credit spread credit rating for the bond."));
        paramMapHashMap.put("CREDITSPDINTERPMETH",
                new ParamMap("CREDITSPDINTERPMETH", Domains.DOMAINSTRING, 'N', 'Y', "yieldCurveInterpList", 1,
                        Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the method to use in interpolating points within a yield curve."));
        paramMapHashMap.put("CREDITSPRDMATLEN",
                new ParamMap("CREDITSPRDMATLEN", Domains.DOMAININT, 'Y', 'N', "noList", 1, Limits.MINCSMATLEN,
                        Limits.MAXCSMATLEN,
                        "Indicates the maturity length months for sample points on a credit spread yield curve."));
        paramMapHashMap.put("CREDITSPRDRT", new ParamMap("CREDITSPRDRT", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINCSRATE, Limits.MAXCSRATE,
                "Indicates the credit spreads to add to a risk free yield curve to obtain an adjusted yield curve for calculations."));
        paramMapHashMap.put("CUMACCREAMORT", new ParamMap("CUMACCREAMORT", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1,
                Limits.MINMONEY, Limits.MAXMONEY,
                "Indicates the current cumulative accretion/amortization for a bond. Used for controlling subsequent accrual accounting."));
        paramMapHashMap.put("CUMINTINCOME", new ParamMap("CUMINTINCOME", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1,
                Limits.MINMONEY, Limits.MAXMONEY,
                "Indicates the current cumulative interest income for a bond. Used for controlling subsequent accrual accounting."));
        paramMapHashMap.put("CUMLOCALCOSTADJUST", new ParamMap("CUMLOCALCOSTADJUST", Domains.DOMAINDOUBLE, 'N', 'N',
                "noList", 1, Limits.MINMONEY, Limits.MAXMONEY,
                "Indicates the current cumulative cost adjustments  for a bond. Used for controlling subsequent accrual accounting."));
        paramMapHashMap.put("CURRENCY", new ParamMap("CURRENCY", Domains.DOMAINSTRING, 'N', 'Y', "currencyList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates the currency is which interest and principal is paid"));
        paramMapHashMap.put("CUSIP", new ParamMap("CUSIP", Domains.DOMAINSTRING, 'N', 'N', "noList", 1, Limits.MINNONE,
                Limits.MAXNONE, "Indicates CUSIP of the bond."));
        paramMapHashMap.put("DAYSCAPGAINSHORT",
                new ParamMap("DAYSCAPGAINSHORT", Domains.DOMAININT, 'N', 'N', "noList", 1,
                        Limits.MINDAYSCAPITALGAINSHORT, Limits.MAXDAYSCAPITALGAINSHORT,
                        "Indicates the number of days that a bond is held to qualify as a short investment."));
        paramMapHashMap.put("DEFAULTMODEL",
                new ParamMap("DEFAULTMODEL", Domains.DOMAINSTRING, 'N', 'Y', "creditDefaultModelList", 1,
                        Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the default model to use in calculating interest income."));
        paramMapHashMap.put("DELAYDAYSACT", new ParamMap("DELAYDAYSACT", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MINDELAYDAYS, Limits.MAXDELAYDAYS,
                "Indicates the daycount method to use in calculating the delay days from accrual date for actual date."));
        paramMapHashMap.put("DELAYDAYSSTATED", new ParamMap("DELAYDAYSSTATED", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MINDELAYDAYS, Limits.MAXDELAYDAYS,
                "Indicates the daycount method to use in calculating the delay days from accrual date for stated daleys."));
        paramMapHashMap.put("DESCR", new ParamMap("DESCR", Domains.DOMAINSTRING, 'N', 'N', "noList", 1, Limits.MINNONE,
                Limits.MAXNONE, "Indicates a description for the bond."));
        paramMapHashMap.put("DISCCURVENAME", new ParamMap("DISCCURVENAME", Domains.DOMAINSTRING, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates the name for the discount rate curve."));
        paramMapHashMap.put("DISCFIXEDLEGYN", new ParamMap("DISCFIXEDLEGYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether the fixed rate leg of an IRS swap should be evaluates using a discount yield curve."));
        paramMapHashMap.put("DISCRTPARYN",
                new ParamMap("DISCRTPARYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates whether the discount rate curve is a par curve or spot curve."));
        paramMapHashMap.put("DISCSPRDPARYN",
                new ParamMap("DISCSPRDPARYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates whether the discount credit spread curve is a par curve or spot curve."));
        paramMapHashMap.put("DISCFUTRTPARYN", new ParamMap("DISCFUTRTPARYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether the future discount rate curve used for interest rate attribution is a par curve or spot curve."));
        paramMapHashMap.put("DISCFUTSPRDPARYN", new ParamMap("DISCFUTSPRDPARYN", Domains.DOMAINYESNO, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether the future discount credit spread curve for interest rate attribution is a par curve or spot curve."));
        paramMapHashMap.put("DISCCURVEINTERVALNAME", new ParamMap("SPOTDISCCURVEINTERVALNAME", Domains.DOMAINSTRING,
                'N', 'Y', "spotIntervalList", 1, Limits.MINSPOTINTERVAL, Limits.MAXSPOTINTERVAL,
                "Indicates the time interval for spot rate conversion from the discount par curve to the spot curve in years"));
        paramMapHashMap.put("DISCRT", new ParamMap("DISCRT", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINRATE, Limits.MAXRATE,
                "Indicates the interest rate in percent associated with each yield curve point for the discount xurve."));
        paramMapHashMap.put("DISCRTDN", new ParamMap("DISCRTDN", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINRATE, Limits.MAXRATE, "Indicates the discount rate down side for the spot discount curve."));
        paramMapHashMap.put("DISCRTUP", new ParamMap("DISCRTUP", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINRATE, Limits.MAXRATE, "Indicates the discount rate up side for the spot discount curve."));
        paramMapHashMap.put("DISCSTDDEV", new ParamMap("DISCSTDDEV", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINRATEVOL, Limits.MAXRATEVOL, "Indicates the standard deviation of the spot discount curve."));
        paramMapHashMap.put("DISCTENOR", new ParamMap("DISCTENOR", Domains.DOMAININT, 'Y', 'N', "matLenList", 1,
                Limits.MINMATLEN, Limits.MAXMATLEN,
                "Indicates the tenor in MONTHS associated with each yield curve point for the risk free discount curve."));
        paramMapHashMap.put("DISCSPRD", new ParamMap("DISCSPRDRT", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINRATE, Limits.MAXRATE,
                "Indicates the interest rate in percent associated with each yield curve point for the discount xurve."));
        paramMapHashMap.put("DISCSPRDTENOR", new ParamMap("DISCSPRDTENOR", Domains.DOMAINDOUBLE, 'Y', 'N', "matLenList",
                1, Limits.MINMATLEN, Limits.MAXMATLEN,
                "Indicates the tenor in MONTHS associated with each yield curve point for the discount credit spread curve."));
        paramMapHashMap.put("DISCFUTRT", new ParamMap("DISCFUTRT", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINRATE, Limits.MAXRATE,
                "Indicates the interest rate in percent associated with each yield curve point for the discount xurve."));
        paramMapHashMap.put("DISCFUTRTDN", new ParamMap("DISCFUTRTDN", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINRATE, Limits.MAXRATE, "Indicates the discount rate down side for the spot discount curve."));
        paramMapHashMap.put("DISCFUTRTUP", new ParamMap("DISCFUTRTUP", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINRATE, Limits.MAXRATE, "Indicates the discount rate up side for the spot discount curve."));
        paramMapHashMap.put("DISCFUTSTDDEV",
                new ParamMap("DISCFUTSTDDEV", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINRATEVOL,
                        Limits.MAXRATEVOL, "Indicates the standard deviation of the spot discount curve."));
        paramMapHashMap.put("DISCFUTTENOR", new ParamMap("DISCFUTTENOR", Domains.DOMAINDOUBLE, 'Y', 'N', "matLenList",
                1, Limits.MINMATLEN, Limits.MAXMATLEN,
                "Indicates the tenor in MONTHS associated with each yield curve point for the risk free discount curve."));
        paramMapHashMap.put("DISCFUTSPRD", new ParamMap("DISCFUTSPRD", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINRATE, Limits.MAXRATE,
                "Indicates the interest rate in percent associated with each yield curve point for the discount xurve."));
        paramMapHashMap.put("DISCFUTSPRDTENOR", new ParamMap("DISCFUTSPRDTENOR", Domains.DOMAINDOUBLE, 'Y', 'N',
                "matLenList", 1, Limits.MINMATLEN, Limits.MAXMATLEN,
                "Indicates the tenor in MONTHS associated with each yield curve point for the discount credit spread curve."));
        paramMapHashMap.put("DISCFUTSPRDDN",
                new ParamMap("DISCFUTSPRDDN", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINRATE,
                        Limits.MAXRATE, "Indicates the discount rate down side for the future spot discount curve."));
        paramMapHashMap.put("DISCFUTSPRDUP",
                new ParamMap("DISCFUTSPRDUP", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINRATE,
                        Limits.MAXRATE, "Indicates the discount rate up side for the future spot discount curve."));
        paramMapHashMap.put("DISCFUTSPRDSTDDEV",
                new ParamMap("DISCFUTSPRDSTDDEV", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINRATEVOL,
                        Limits.MAXRATEVOL, "Indicates the standard deviation of future the spot discount curve."));
        paramMapHashMap.put("DISCCURVEINTERVALNAME", new ParamMap("SPOTDISCCURVEINTERVALNAME", Domains.DOMAINSTRING,
                'N', 'Y', "spotIntervalList", 1, Limits.MINSPOTINTERVAL, Limits.MAXSPOTINTERVAL,
                "Indicates the time interval for spot rate conversion from the discount par curve to the spot curve in years"));
        paramMapHashMap.put("DTACCRUE", new ParamMap("DTACCRUE", Domains.DOMAINDATE, 'Y', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates a set of enumerated accrual start dates"));
        paramMapHashMap.put("DTCALL", new ParamMap("DTCALL", Domains.DOMAINDATE, 'Y', 'N', "noList", 1, Limits.MINNONE,
                Limits.MAXNONE, "Indicates the set of call dates for a callable bond."));
        paramMapHashMap.put("DTCALLELIGIBLE",
                new ParamMap("DTCALLELIGIBLE", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the date on which call can be made for a continuous callable bond."));
        paramMapHashMap.put("DTCALLNOTIFIED",
                new ParamMap("DTCALLNOTIFIED", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the number of days that a bond holder must be notified prior to making a call."));
        paramMapHashMap.put("DTCASHEND",
                new ParamMap("DTCASHEND", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the end date (exclusive) on which cashflow projections are made."));
        paramMapHashMap.put("DTCASHSTART",
                new ParamMap("DTCASHSTART", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the start date (inclusive) on which cashflow projections are made."));
        paramMapHashMap.put("DTCONVERT",
                new ParamMap("DTCONVERT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the last date for which a conversion from bonds to stock must be made."));
        paramMapHashMap.put("DTCOUPON",
                new ParamMap("DTCOUPON", Domains.DOMAINDATE, 'Y', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates a user supplied set of coupon dates that cannot to specified by schedule rule"));
        paramMapHashMap.put("DTDISCCURVE", new ParamMap("DTDISCCURVE", Domains.DOMAINDATE, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates the date of the discount curve to use o settle date calculations like kyerate duration and fair value"));
        paramMapHashMap.put("DTDISCFUTCURVE", new ParamMap("DTDISCFUTCURVE", Domains.DOMAINDATE, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates the date of the discount curve to use in future pricing calcu;ations like fiture value and attribution"));
        paramMapHashMap.put("DTDATED", new ParamMap("DTDATED", Domains.DOMAINDATE, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates the date on which interest accruals begin."));
        paramMapHashMap.put("DTEND", new ParamMap("DTEND", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                Limits.MAXNONE, "Indicates the end date for horizon analysis."));
        paramMapHashMap.put("DTESTPREPAY",
                new ParamMap("DTESTPREPAY", Domains.DOMAINDATE, 'Y', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the set of dates on which you specify estimated passthur pre-pay speed."));
        paramMapHashMap.put("DTEXDIVIDEND",
                new ParamMap("DTEXDIVIDEND", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the date on which the most recent ex-dividend period begins."));
        paramMapHashMap.put("DTFACTORS", new ParamMap("DTFACTORS", Domains.DOMAINDATE, 'Y', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates a set of dates on which principal factors should be applied. This along with the set of principal factors determines principal payment cashflows."));
        // paramMapHashMap.put("DTFIXINGCURVE", new ParamMap("DTFIXCURVE",
        // Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
        // "Indicates the date of the fixing curve used for floating rates."));
        paramMapHashMap.put("DTFORMAT", new ParamMap("DTFORMAT", Domains.DOMAINSTRING, 'N', 'Y', "dateFormatList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates the date format used when specifying maturity dates or calculation dates and all other dates."));
        paramMapHashMap.put("DTFUTURE", new ParamMap("DTFUTURE", Domains.DOMAINDATE, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates the date on which to calculate a future value."));
        paramMapHashMap.put("DTFXRTS", new ParamMap("DTFXRTS", Domains.DOMAINDATE, 'Y', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates a set of dates on which FX rates are to be applied in converting local currency payments to another currency."));
        paramMapHashMap.put("DTHORIZONEND", new ParamMap("DTHORIZONEND", Domains.DOMAINDATE, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates a the end of a horizon analysis period."));
        paramMapHashMap.put("DTIDXASSUME", new ParamMap("DTIDXASSUME", Domains.DOMAINDATE, 'Y', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates a set of dates on which index assumptions are made for use in calculating future cashflows. Used for Brazil TIPs bonds."));
        paramMapHashMap.put("DTIDXCPI",
                new ParamMap("DTIDXCPI", Domains.DOMAINDATE, 'Y', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates a set of dates on which index CPI values occur. Used for TIPs bonds."));
        paramMapHashMap.put("DTIDXRATIO",
                new ParamMap("DTIDXRATIO", Domains.DOMAINDATE, 'Y', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates a set of dates on which index ratios occur. Used for TIPs bonds."));
        paramMapHashMap.put("DTLASTACCOUNTING",
                new ParamMap("DTLASTACCOUNTING", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Indicates the date that accounting was performed on this bond or lot."));
        paramMapHashMap.put("DTLASTPAYMENT",
                new ParamMap("DTLASTPAYMENT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the last payment date for a pass-thru bond sometimes used as maturity date."));
        paramMapHashMap.put("DTLASTONLYCOUPON", new ParamMap("DTLASTONLYCOUPON", Domains.DOMAINDATE, 'N', 'N', "noList",
                1, Limits.MINNONE, Limits.MAXNONE, "Indicates the date of the coupon just prior to maturity date."));
        paramMapHashMap.put("DTLASTTAXPAYMENT",
                new ParamMap("DTLASTTAXPAYMENT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Indicates the date of the last tax payment. Used for tax cashflow payments."));
        paramMapHashMap.put("DTLASTUSERCPN",
                new ParamMap("DTLASTUSERCPN", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates a user entered last coupon datee to use for accrued interest calculations."));
        paramMapHashMap.put("DTMARKETVALUE", new ParamMap("DTMARKETVALUE", Domains.DOMAINDATE, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates the date on which the market value is estimated."));
        paramMapHashMap.put("DTMATURITY", new ParamMap("DTMATURITY", Domains.DOMAINDATE, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates the legal maturity date for this bond."));
        paramMapHashMap.put("DTMKTPRCS", new ParamMap("DTMKTPRCS", Domains.DOMAINDATE, 'Y', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates a set of dates associated with market prices."));
        paramMapHashMap.put("DTNEXTUSERCPN",
                new ParamMap("DTNEXTUSERCPN", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates a user entered next coupon datee to use for accrued interest calculations."));
        paramMapHashMap.put("DTOCCUR", new ParamMap("DTOCCUR", Domains.DOMAINDATE, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates a seed date used to determine when a payment, accrual or other event occurs in the future"));
        paramMapHashMap.put("DT1STPAY",
                new ParamMap("DT1STPAY", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the first coupon payment after dated date for oddness calculation."));
        paramMapHashMap.put("DTLASTPAY",
                new ParamMap("DTLASTPAY", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the last coupon payment before maturity for oddness calculation."));
        paramMapHashMap.put("DTPASSFACTOR", new ParamMap("DTPASSFACTOR", Domains.DOMAINDATE, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates the date on which the passthru factor occurs."));
        paramMapHashMap.put("DTPASSFACTOR1", new ParamMap("DTPASSFACTOR1", Domains.DOMAINDATE, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates a pass-thru factor date associated with a pass-thru factor for use in estimating the end of a period over which the prepayment speed will be calculated."));
        paramMapHashMap.put("DTPASSFACTOR2", new ParamMap("DTPASSFACTOR2", Domains.DOMAINDATE, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates a pass-thru factor date associated with a pass-thru factor for use in estimating the start of a period over which the prepayment speed will be calculated."));
        paramMapHashMap.put("DTPASSFACTORS", new ParamMap("DTPASSFACTORS", Domains.DOMAINDATE, 'Y', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates a set of dates associated with pass-thru factors."));
        paramMapHashMap.put("DTPAYLEGRESET", new ParamMap("DTPAYLEGRESET", Domains.DOMAINDATE, 'Y', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates a set of dates on which base reset rates will be changed on the pag leg of an IRS swap."));
        paramMapHashMap.put("DTPREFUNDED",
                new ParamMap("DTPREFUNDED", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the date on which a prefunded bond will mature. Used for municipal bonds."));
        paramMapHashMap.put("DTPUR", new ParamMap("DTPUR", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                Limits.MAXNONE, "Indicates the settlement date on which this bond was purchased."));
        paramMapHashMap.put("DTPUT", new ParamMap("DTPUT", Domains.DOMAINDATE, 'Y', 'N', "noList", 1, Limits.MINNONE,
                Limits.MAXNONE,
                "Indicates the set of put dates on which the bond holder can put the bond back to the issuer."));
        paramMapHashMap.put("DTRECLEGRESET", new ParamMap("DTRECLEGRESET", Domains.DOMAINDATE, 'Y', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates a set of enumerated swap receive leg reset dates"));
        paramMapHashMap.put("DTRESET", new ParamMap("DTRESET", Domains.DOMAINDATE, 'Y', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates a set of enumerated reset dates"));
        paramMapHashMap.put("DTRESETMULTIPLIERS",
                new ParamMap("DTRESETMULTIPLIERS", Domains.DOMAINDATE, 'Y', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the start date for applying a multiplier to a base floating rate value."));
        paramMapHashMap.put("DTSETTLE",
                new ParamMap("DTSETTLE", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the settle date or calculation date to be used in calculating bond analytics."));
        paramMapHashMap.put("DTSINK", new ParamMap("DTSINKS", Domains.DOMAINDATE, 'Y', 'N', "noList", 1, Limits.MINNONE,
                Limits.MAXNONE, "Indicates the set of dates on which sinking fund payments will be made."));
        paramMapHashMap.put("DTSTART", new ParamMap("DTSTART", Domains.DOMAINDATE, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates the start date for the start period for horizon analysis."));
        paramMapHashMap.put("DTTAXPAY",
                new ParamMap("DTTAXPAY", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates a date that taxes are paid for security income and gain/loss"));
        paramMapHashMap.put("DTUSERPAYDTS", new ParamMap("DTUSERPAYDTS", Domains.DOMAINDATE, 'Y', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates a set of dates on which the coupon payments will be made."));
        paramMapHashMap.put("DUMPCNTMAX",
                new ParamMap("DUMPCNTMAX", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINDUMPCOUNT,
                        Limits.MAXDUMPCOUNT,
                        "Indicates the maximum number of times a dump file can be created for a bond session."));
        paramMapHashMap.put("DUMPFILENAME", new ParamMap("DUMPFILENAME", Domains.DOMAINSTRING, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates the name of the dump file you wish to use for containing a list of values submitted for calculations."));
        paramMapHashMap.put("DUMPONCMDYN",
                new ParamMap("DUMPONCMDYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates whether to dump the data submitted for calculations to a file for review."));
        paramMapHashMap.put("DUMPONERRORYN", new ParamMap("DUMPONERRORYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to dump the data submitted for calculations to a file for review when a data error is detected."));
        paramMapHashMap.put("DURATIONWT", new ParamMap("DURATIONWT", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1,
                Limits.MINWEIGHT, Limits.MAXWEIGHT,
                "Indicates a weighting of the importance of duration shift minimization vs reurn mazimization."));
        paramMapHashMap.put("DURBPSHIFT", new ParamMap("DURBPSHIFT", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1,
                Limits.MINRATE, Limits.MAXRATE,
                "Indicates the number of basis points to shift the yield curve when calculating modified duration or key rate duration."));
        paramMapHashMap.put("ENDPRC", new ParamMap("ENDPRC", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                Limits.MINPRICE, Limits.MAXPRICE,
                "Indicates the market price of this bond at the end of a horizon period used when performing horizon analysis."));
        paramMapHashMap.put("ENDYLD", new ParamMap("ENDYLD", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                Limits.MINYIELD, Limits.MAXYIELD,
                "Indicates the market yield of this bond at the end of a horizon period used when performing horizon analysis."));
        paramMapHashMap.put("ESTPREPAY",
                new ParamMap("ESTPREPAY", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 1, Limits.MINPREPAYSPEED,
                        Limits.MAXPREPAYSPEED, "Indicates the estimated passthru pre-pay speed for a mortgage"));
        paramMapHashMap.put("EXDIVOFFSETDAYS",
                new ParamMap("EXDIVOFFSETDAYS", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINEXDIVOFFSET,
                        Limits.MAXEXDIVOFFSET,
                        "Indicates the number of days prior to coupon pay date that starts the ex-dividend period."));
        paramMapHashMap.put("FACEAMT", new ParamMap("FACEAMT", Domains.DOMAINMONEY, 'N', 'N', "noList", 1,
                Limits.MINMONEY, Limits.MAXMONEY, "Indicates face amount of this bond received at maturity."));
        paramMapHashMap.put("FEDTAXEXEMPTYN", new ParamMap("FEDTAXEXEMPTYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether this bond is exempt from federal income taxes."));
        paramMapHashMap.put("FILEOUTYN", new ParamMap("FILEOUTYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether to write the calculation results to file"));
        paramMapHashMap.put("FILEOUTNAME", new ParamMap("FILEOUTPUTNAME", Domains.DOMAINSTRING, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates the name of the file to output calculation results."));
        paramMapHashMap.put("FILEOUTFORMAT", new ParamMap("FILEOUTFORMAT", Domains.DOMAINSTRING, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates the format of the output file: STD or CSV."));
        paramMapHashMap.put("FISCALYTDAALOCAL",
                new ParamMap("FISCALYTDAALOCAL", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1, Limits.MINMONEY,
                        Limits.MAXMONEY,
                        "Indicates the current fiscal year accretion/amortization taken for this bond."));
        paramMapHashMap.put("FISCALYTDCOSTADJLOCAL",
                new ParamMap("FISCALYTDCOSTADJLOCAL", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1, Limits.MINMONEY,
                        Limits.MAXMONEY, "Indicates the current fiscal year cost adjustment made for this bond."));
        paramMapHashMap.put("FISCALYTDINCOMELOCAL",
                new ParamMap("FISCALYTDINCOMELOCAL", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1, Limits.MINMONEY,
                        Limits.MAXMONEY, "Indicates the current fiscal year interest income taken for this bond."));
        paramMapHashMap.put("FIXINGCURVENAME", new ParamMap("FIXINGCURVENAME", Domains.DOMAINSTRING, 'N', 'N', "noList",
                1, Limits.MINNONE, Limits.MAXNONE, "Indicates the name for the fixing rate curve."));
        paramMapHashMap.put("FIXINGMATNAME", new ParamMap("FIXINGMATNAME", Domains.DOMAINSTRING, 'Y', 'N', "matLenList",
                1, Limits.MINNONE, Limits.MINNONE,
                "Indicates the maturity length in months or years associated with each yield curve point for the fixing curve."));
        paramMapHashMap.put("FIXINGBENCHMARK",
                new ParamMap("FIXINGBENCHMARK", Domains.DOMAINSTRING, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the benchmark to use for obtaining a fixing curve for floating rate bonds/"));
        paramMapHashMap.put("FIXINGTENOR", new ParamMap("fixingTenor", Domains.DOMAININT, 'Y', 'N', "noList", 1,
                Limits.MINMATLEN, Limits.MAXMATLEN,
                "Indicates the maturity length in MONTHS associated with each yield curve point for the fixing curve for floaters."));
        paramMapHashMap.put("FIXINGPARYN",
                new ParamMap("FIXINGPARYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates whether the yield curve for discounting is a spot curve or a par curve."));
        paramMapHashMap.put("FIXINGRT",
                new ParamMap("FIXINGRT", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINRATE, Limits.MAXRATE,
                        "Indicates a set of fixing rate for the floating rate reset yield curve."));
        paramMapHashMap.put("FIXINGAVGRT",
                new ParamMap("FIXINGAVGRT", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINRATE,
                        Limits.MAXRATE,
                        "Indicates a set of fixing average rates for the floating rate reset yield curve."));
        paramMapHashMap.put("FIXINGINTERVALNAME", new ParamMap("SPOTFIXINGINTERVALNAME", Domains.DOMAINSTRING, 'N', 'Y',
                "spotIntervalList", 1, Limits.MINSPOTINTERVAL, Limits.MAXSPOTINTERVAL,
                "Indicates the time interval for spot rate conversion from the fixing par curve to the spot curve in months"));
        paramMapHashMap.put("FIXINGMIN",
                new ParamMap("FIXINGMIN", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINRATE, Limits.MAXRATE,
                        "Indicates a set of fixing rate for the floating rate reset yield curve."));
        paramMapHashMap.put("FIXINGMAX",
                new ParamMap("FIXINGMAX", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINRATE, Limits.MAXRATE,
                        "Indicates a set of fixing rate for the floating rate reset yield curve."));
        paramMapHashMap.put("FIXINGRTDN",
                new ParamMap("FIXINGRTDN", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINRATEVOL,
                        Limits.MAXRATEVOL, "Indicates the spot fixing rate down side for the spot fixing curve."));
        paramMapHashMap.put("FIXINGRTUP",
                new ParamMap("FIXINGRTUP", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINRATEVOL,
                        Limits.MAXRATEVOL, "Indicates the spot rate up side volatility for the spot fixing curve."));
        paramMapHashMap.put("FIXINGSTDDEV", new ParamMap("FIXINGSTDDEV", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINRATEVOL, Limits.MAXRATEVOL, "Indicates the standard deviation for the spot fixing rate."));
        paramMapHashMap.put("FLOATERTERM", new ParamMap("FLOATERTERM", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MINTERM, Limits.MAXTERM,
                "Indicates the maturity length in months for a floating rate bond or financial future or option."));
        paramMapHashMap.put("FORCEYLDYN", new ParamMap("FORCEYLDYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to set the price to 100 if the yield equals the coupon rate or to set the yield equal to the coupon rate when the price = 100. Used for municipal bonds."));
        paramMapHashMap.put("FUTUREPRC", new ParamMap("FUTUREPRC", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                Limits.MINPRICE, Limits.MAXPRICE,
                "Indicates the market price to use in calculating analytics on a future date - used for return attribution."));
        paramMapHashMap.put("FUNDNAME", new ParamMap("FUNDNAME", Domains.DOMAINSTRING, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates the name of the fund in which this bond exists."));
        paramMapHashMap.put("FXRTS", new ParamMap("FXRATE", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 1, Limits.MINRATE,
                Limits.MAXRATE,
                "Indicates a set of foreign exchange rates associated used to convert foreign currency into another currency for calculating cashflow and analytics."));
        paramMapHashMap.put("HORIZONFREQ", new ParamMap("HORIZONFREQ", Domains.DOMAINPERIODICITY, 'N', 'Y', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates the periodicity of future horizon calculation dates."));
        paramMapHashMap.put("HORIZONPERIODS", new ParamMap("HORIZONPERIODS", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MINHORIZONPERIODS, Limits.MAXHORIZONPERIODS, "Indicates the number of horizon periods."));
        paramMapHashMap.put("HOSTSECID", new ParamMap("HOSTSECID", Domains.DOMAINSTRING, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates the identifier of this holding or security used by the host system that provided this bond."));
        paramMapHashMap.put("IDXADJMATYN", new ParamMap("IDXADJMATYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to adjust the maturity payment for calculations for TIPs bonds using the  index value at settle or index ratio or inflation."));
        paramMapHashMap.put("IDXASSUME", new ParamMap("IDXASSUME", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 1,
                Limits.MININDEXRATIO, Limits.MAXINDEXRATIO,
                "Indicates the index values to use in projecting the cashflows for an inflation protected bond. Used for Brazilian TIPs bonds."));
        paramMapHashMap.put("IDXASSUMEHOLIDAYRULE", new ParamMap("IDXASSUMEHOLIDAYRULE", Domains.DOMAINSTRING, 'N', 'Y',
                "holidayOffsetList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the rule to use to adjust an assumed index CPI date if it is schedule to occur on a non-business date."));
        paramMapHashMap.put("IDXCPIATISSUE", new ParamMap("IDXATISSUE", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1,
                Limits.MININDEXRATIO, Limits.MAXINDEXRATIO, "Indicates the index CPI value on dated date."));
        paramMapHashMap.put("IDXCPIRATIO", new ParamMap("IDXCPIRATIO", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1,
                Limits.MININDEXRATIO, Limits.MAXINDEXRATIO, "Indicates the set of index values for calculations."));
        paramMapHashMap.put("IDXCPIATSETTLE",
                new ParamMap("IDXRATIOATSETTLE", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1, Limits.MININDEXVALUE,
                        Limits.MAXINDEXVALUE, "Indicates the index value for TIPs bond on settle date."));
        paramMapHashMap.put("IDXDELAYDAYS", new ParamMap("IDXDELAYDAYS", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MININDEXDELAYDAYS, Limits.MAXINDEXDELAYDAYS,
                "Indicates the number of days to look back when determining the index values to use in calculating the accrued interest or interest payments."));
        paramMapHashMap.put("IDXDELAYMONTHS", new ParamMap("IDXDELAYMONTHS", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MININDEXDELAYMONTHS, Limits.MAXINDEXDELAYMONTHS,
                "Indicates the number of months to look back when determining the index values to use in calculating the accrued interest or interest payments."));
        paramMapHashMap.put("IDXDELAYWEEKS", new ParamMap("IDXDELAYWEEKS", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MININDEXDELAYWEEKS, Limits.MAXINDEXDELAYWEEKS,
                "Indicates the number of weeks to look back when determining the index values to use in calculating the accrued interest or interest payments."));
        paramMapHashMap.put("IDXHOLIDAYRULE", new ParamMap("IDXHOLIDAYRULE", Domains.DOMAINSTRING, 'N', 'Y',
                "holidayOffsetList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the rule to use to adjust an index CPI date if it is schedule to occur on a non-business date."));
        paramMapHashMap.put("IDXINTERPMETH", new ParamMap("IDXINTERPMETH", Domains.DOMAINSTRING, 'N', 'Y',
                "indexInterpList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the method to use in interpolating the index at on any date by using index values around the date."));
        paramMapHashMap.put("IDXPERIODICITY", new ParamMap("IDXPERIODICITY", Domains.DOMAINPERIODICITY, 'N', 'Y',
                "timeUnitList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the base time unit to use in setting the length of an index period. It can be monthly or weekly or daily time units."));
        paramMapHashMap.put("IDXRARIOSCHEDDOW", new ParamMap("IDXRARIOSCHEDDOW", Domains.DOMAINSTRING, 'N', 'Y',
                "dayOfWeekList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the day-of-the-week on which an index ratio period starts for a weekly based payment bond."));
        paramMapHashMap.put("IDXRATIOPERIODICITY", new ParamMap("IDXRATIOPERIODICITY", Domains.DOMAINPERIODICITY, 'N',
                'Y', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the base time unit to use in setting the length of an index ratio period. It can be monthly or weekly or daily time units."));
        paramMapHashMap.put("IDXRATIORNDYN", new ParamMap("IDXRATIORNDYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to truncate or round the index ratio when calculating accrued interest or interest payments."));
        paramMapHashMap.put("IDXRATIOSCEHDCOMPOUNDFREQ", new ParamMap("IDXRATIOSCEHDCOMPOUNDFREQ", Domains.DOMAINSTRING,
                'N', 'Y', "compoundFreqList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the default the number of index ratio periods per year for month based index frequencies."));
        paramMapHashMap.put("IDXRATIOSCHEDDOM",
                new ParamMap("IDXRATIOSCHEDDOM", Domains.DOMAININT, 'N', 'Y', "noList", 1, Limits.MINDAYOFMONTH,
                        Limits.MAXDAYOFMONTH, "Indicates an actual month on which an index period begins."));
        paramMapHashMap.put("IDXRATIOSCHEDEOMRULEYN", new ParamMap("IDXRATIOSCHEDEOMRULEYN", Domains.DOMAINYESNO, 'N',
                'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether or not index ratio dates should always fall on month end if one index date falls on month end."));
        paramMapHashMap.put("IDXRATIOSCHEDMOY", new ParamMap("IDXRATIOSCHEDMOY", Domains.DOMAINSTRING, 'N', 'Y',
                "monthOfYearList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the month of year on which a index ratio is published and available for subsequent adjustments of par."));
        paramMapHashMap.put("IDXRATIOSCHEDNEXTCPNDT", new ParamMap("IDXRATIOSCHEDNEXTCPNDT", Domains.DOMAINDATE, 'N',
                'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the next index ratio begin date specified by the user to use defining the next index date."));
        paramMapHashMap.put("IDXRATIOSCHEDOCCURDT", new ParamMap("IDXRATIOSCHEDOCCURDT", Domains.DOMAINDATE, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates an actual date on which an index ratio period begins. Used to determine the set of index periods for the entire bond schedule."));
        paramMapHashMap.put("IDXRATIOSCHEDOCCURYEAR", new ParamMap("IDXRATIOSCHEDOCCURYEAR", Domains.DOMAININT, 'N',
                'N', "noList", 1, Limits.MINYEAROFOCCUR, Limits.MAXYEAROFOCCUR,
                "Indicates the a year on which an indexratio  publishing date occurs. Used to calculation the index publishing schedule."));
        paramMapHashMap.put("IDXRATIOSCHEDPREVCPNDT;",
                new ParamMap("IDXRATIOSCHEDPREVCPNDT;", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Indicates an the index ratio date that is prior to the current settle date."));
        paramMapHashMap.put("IDXRATIOSCHEDREPEAT",
                new ParamMap("IDXRATIOSCHEDREPEAT", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINREPEATFACTOR,
                        Limits.MAXREPEATFACTOR,
                        "Indicates the number of time units between index ratio publishing dates."));
        paramMapHashMap.put("IDXRATIOSCHEDUSENEXTLASTYN", new ParamMap("IDXRATIOSCHEDUSENEXTLASTYN",
                Domains.DOMAINYESNO, 'N', 'Y', "timeUnitList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to use the supplied next or prev index ratio schedule date in calculations."));
        paramMapHashMap.put("IDXRATIOSCHEDWOM", new ParamMap("IDXRATIOSCHEDWOM", Domains.DOMAININT, 'N', 'N', "noList",
                1, Limits.MINWEEKOFMONTH, Limits.MAXWEEKOFMONTH,
                "Indicates the week of the month on which a an index ratio begin date falls for weekly based payments."));
        paramMapHashMap.put("IDXSCEHDCOMPOUNDFREQ", new ParamMap("IDXSCEHDCOMPOUNDFREQ", Domains.DOMAINSTRING, 'N', 'Y',
                "compoundFreqList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the default the number of index periods per year for month based index frequencies."));
        paramMapHashMap.put("IDXSCHEDDOM",
                new ParamMap("IDXSCHEDDOM", Domains.DOMAININT, 'N', 'Y', "noList", 1, Limits.MINDAYOFMONTH,
                        Limits.MAXDAYOFMONTH, "Indicates an actual month on which an index period begins."));
        paramMapHashMap.put("IDXSCHEDDOW", new ParamMap("IDXSCHEDDOW", Domains.DOMAINSTRING, 'N', 'Y', "dayOfWeekList",
                1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the day-of-the-week on which an index period starts for a weekly based payment bond."));
        paramMapHashMap.put("IDXSCHEDEOMRULEYN", new ParamMap("IDXSCHEDEOMRULEYN", Domains.DOMAINYESNO, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether or not index dates should always fall on month end if one index date falls on month end."));
        paramMapHashMap.put("IDXSCHEDMOY", new ParamMap("IDXSCHEDMOY", Domains.DOMAINSTRING, 'N', 'Y',
                "monthOfYearList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the month of year on which a index is published and available for subsequent adjustments of par."));
        paramMapHashMap.put("IDXSCHEDNEXTCPNDT", new ParamMap("IDXSCHEDNEXTCPNDT", Domains.DOMAINDATE, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the next index begin date specified by the user to use defining the next index date."));
        paramMapHashMap.put("IDXSCHEDOCCURDT", new ParamMap("IDXSCHEDOCCURDT", Domains.DOMAINDATE, 'N', 'N', "noList",
                1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates an actual date on which an index period begins. Used to determine the set of index periods for the entire bond schedule."));
        paramMapHashMap.put("IDXSCHEDOCCURYEAR", new ParamMap("IDXSCHEDOCCURYEAR", Domains.DOMAININT, 'N', 'N',
                "noList", 1, Limits.MINYEAROFOCCUR, Limits.MAXYEAROFOCCUR,
                "Indicates the a year on which an index publishing date occurs. Used to calculation the index publishing schedule."));
        paramMapHashMap.put("IDXSCHEDPREVCPNDT;",
                new ParamMap("IDXSCHEDPREVCPNDT;", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Indicates an the index date that is prior to the current settle date."));
        paramMapHashMap.put("IDXSCHEDREPEAT",
                new ParamMap("IDXSCHEDREPEAT", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINREPEATFACTOR,
                        Limits.MAXREPEATFACTOR, "Indicates the number of time units between index publishing dates."));
        paramMapHashMap.put("IDXSCHEDUSENEXTLASTYN",
                new ParamMap("IDXSCHEDUSENEXTLASTYN", Domains.DOMAINYESNO, 'N', 'Y', "timeUnitList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates whether to use the supplied next or prev schedule date in calculations."));
        paramMapHashMap.put("IDXSCHEDWOM", new ParamMap("IDXSCHEDWOM", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MINWEEKOFMONTH, Limits.MAXWEEKOFMONTH,
                "Indicates the week of the month on which a an index begin date falls for weekly based payments."));
        paramMapHashMap.put("IDXVALUEROUNDYN",
                new ParamMap("IDXVALUEROUNDYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates whether to truncate or round an index value when computing an index ratio."));
        paramMapHashMap.put("INCLDENDDTBOOKVALYN", new ParamMap("INCLDENDDTBOOKVALYN", Domains.DOMAINYESNO, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether or not to include the end date in an accounting interval when calculating accretion/amortization for book value."));
        paramMapHashMap.put("INCLDENDDTINCOMEYN", new ParamMap("INCLDENDDTINCOMEYN", Domains.DOMAINYESNO, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether or not to include the end date in an accounting interval when calculating interest income for book value."));
        paramMapHashMap.put("INCLDSETTDTCASHYN", new ParamMap("INCLDSETTDTCASHYN", Domains.DOMAINYESNO, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to include the settle date in cashflow when the settle date falls on a payment date."));
        paramMapHashMap.put("INCLDSETTDTINTRECYN", new ParamMap("INCLDSETTDTINTRECYN", Domains.DOMAINYESNO, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to include the settle date in accrued interest when calculating accrued interest. Certain countries include the settle date in accrued interest."));
        paramMapHashMap.put("INCOMECONTROLMARGIN", new ParamMap("INCOMECONTROLMARGIN", Domains.DOMAINDOUBLE, 'N', 'N',
                "noList", 1, Limits.MINMONEY, Limits.MAXMONEY,
                "Indicates the income control margin to use in as a basis for calculating accretion/amortization under accretion/amortization control criteria."));
        paramMapHashMap.put("INFLATIONRT", new ParamMap("INFLATIONRT", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                Limits.MININFLATION, Limits.MAXINFLATION,
                "Indicates the inflation rate in percent assumed when projecting future index ratios. Used in cashflow calculations and analytics."));
        paramMapHashMap.put("INSTYPE", new ParamMap("INSTYPE", Domains.DOMAINSTRING, 'N', 'Y', "instypeList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates the instrument type for this bond. Used to determine the algorithms to use for bond convention and all algorithms used in analytic calculations."));
        paramMapHashMap.put("INTHOLIDAYRULE", new ParamMap("INTHOLIDAYRULE", Domains.DOMAINSTRING, 'N', 'Y',
                "holidayOffsetList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the rule to use to adjust a payment date if it is schedule to occur on a non-business date."));
        paramMapHashMap.put("INTPAYMETH", new ParamMap("INPAYMETH", Domains.DOMAINSTRING, 'N', 'Y', "intPayList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether interst pay pay tge same interst every period or based on the days between coupon datee"));
        paramMapHashMap.put("INTPERIODICITY", new ParamMap("INTPERIODICITY", Domains.DOMAINPERIODICITY, 'N', 'Y',
                "INTERPERIODICITY", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the interest payment frequency using a repeat factor and time unit indicating the length of an accrual period. It can be monthly or weekly or daily time units."));
        paramMapHashMap.put("INTSCHEDDOM",
                new ParamMap("INTSCHEDDOM", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINDAYOFMONTH,
                        Limits.MAXDAYOFMONTH, "Indicates the day of the month on which a monthly schedule falls."));
        paramMapHashMap.put("INTSCHEDDOW", new ParamMap("INTSCHEDDOW", Domains.DOMAINSTRING, 'N', 'Y', "dayOfWeekList",
                1, Limits.MINNONE, Limits.MAXNONE, "Indicates the day of the week on which a weekly schedule falls."));
        paramMapHashMap.put("INTSCHEDEOMYN", new ParamMap("INTSCHEDEOMYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether or not payment dates should always fall on month end if one payment date falls on month end."));
        paramMapHashMap.put("INTSCHEDMOY",
                new ParamMap("INTSCHEDMOY", Domains.DOMAINSTRING, 'N', 'Y', "monthOfYearList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Indicates the month of the year on which a schedule date falls."));
        paramMapHashMap.put("INTSCHEDNEXTCPNDT",
                new ParamMap("INTSCHEDNEXTCPNDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the next payment date specified by the user relative to the settlement date."));
        paramMapHashMap.put("INTSCHEDOCCURDT", new ParamMap("INTSCHEDOCCURDT", Domains.DOMAINDATE, 'N', 'N', "noList",
                1, Limits.MINNONE, Limits.MAXNONE, "Indicates a date on which an interest payment falls."));
        paramMapHashMap.put("INTSCHEDOCCURYEAR",
                new ParamMap("INTSCHEDOCCURYEAR", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINYEAROFOCCUR,
                        Limits.MAXYEAROFOCCUR, "Indicates a year on which a schedule date falls."));
        paramMapHashMap.put("INTSCHEDPREVCPNDT",
                new ParamMap("INTSCHEDPREVCPNDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the last payment date specified by the user relative to the settlement date."));
        paramMapHashMap.put("INTSCHEDREPEAT",
                new ParamMap("INTSCHEDREPEAT", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINREPEATFACTOR,
                        Limits.MAXREPEATFACTOR, "Indicates the number of base time units between payment dates."));
        paramMapHashMap.put("INTSCHEDTIMEUNIT", new ParamMap("INTSCHEDTIMEUNIT", Domains.DOMAINSTRING, 'N', 'Y',
                "timeUnitList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the base time unit to use in setting the length of an accrual period. It can be monthly or weekly or daily time units."));
        paramMapHashMap.put("INTSCHEDUSENEXTLASTYN",
                new ParamMap("INTSCHEDUSENEXTLASTYN", Domains.DOMAINYESNO, 'N', 'Y', "timeUnitList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates whether to use the supplied next or prev schedule date in calculations."));
        paramMapHashMap.put("INTSCHEDWOM",
                new ParamMap("INTSCHEDWOM", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINWEEKOFMONTH,
                        Limits.MAXWEEKOFMONTH,
                        "Indicates the week of the month on which a payment date falls for weekly based payments."));
        paramMapHashMap.put("ISCASHYN",
                new ParamMap("ISCASHYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates whether the bond is cash or cash equivalent with a one day rolling maturity date"));
        paramMapHashMap.put("ISATMATURITYYN", new ParamMap("ISATMATURITYYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether the bond is an ATMATURITY bond type"));
        paramMapHashMap.put("ISEXDIVIDEND", new ParamMap("ISEXDIVIDEND", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether the bond is has an ex-dividend date"));
        paramMapHashMap.put("ISFIXEDRATEYN", new ParamMap("ISFIXEDRATEYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether the bond is a fixed rate bond"));
        paramMapHashMap.put("ISFLOATERYN", new ParamMap("ISFLOATERYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether the bond is a floating rate bond type"));
        paramMapHashMap.put("ISFORCEYIELDYN", new ParamMap("ISFORCEYIELDYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether the bond is is subject to force price or yield to coupon when price = 100 or yield = coupon rate"));
        paramMapHashMap.put("ISINFLATIONPROTECTEDYN",
                new ParamMap("ISINFLATIONPROTECTEDYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Indicates whether the bond is an inflation protected bond"));
        paramMapHashMap.put("ISMORTGAGEYN", new ParamMap("ISMORTGAGEYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether the bond is mortgage pass-thru bond"));
        paramMapHashMap.put("ISPUTABLEYN", new ParamMap("ISPUTABLEYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether the has put dates and prices"));
        paramMapHashMap.put("ISSINKABLE", new ParamMap("ISSINKABLE", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether the bond is has sinking funds"));
        paramMapHashMap.put("ISSTEPPEDYN", new ParamMap("ISSTEPPEDYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether the bond is a a stepped rate bond"));
        paramMapHashMap.put("ISZEROCOUPONYN", new ParamMap("ISZEROCOUPONYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether the bond is zero coupon bond"));
        paramMapHashMap.put("ISIN", new ParamMap("ISIN", Domains.DOMAINSTRING, 'N', 'N', "noList", 1, Limits.MINNONE,
                Limits.MAXNONE, "Indicates ISIN of the bond."));
        paramMapHashMap.put("ISSUEDFACE", new ParamMap("ISSUEDFACE", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1,
                Limits.MINMONEY, Limits.MAXMONEY,
                "Indicates the original issued amount associated with the face amount of the bond holding or trade. Used in generating cashflows for sinking fund bonds."));
        paramMapHashMap.put("ISSUEPRC", new ParamMap("ISSUEPRC", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                Limits.MINPRICE, Limits.MAXPRICE, "Indicates the price at which the bond was issued."));
        paramMapHashMap.put("ISSUER", new ParamMap("ISSUER", Domains.DOMAINSTRING, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates the name of the issuer."));
        paramMapHashMap.put("ISSUEYIELD", new ParamMap("ISSUEYIELD", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                Limits.MINYIELD, Limits.MAXRATE, "Indicates the yield of the bond when issued."));
        paramMapHashMap.put("LEVELPAYFROMPASSDTYN",
                new ParamMap("LEVELPAYFROMPASSDTYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates how to determine the level mortgage payment associated with a paydown factor."));
        paramMapHashMap.put("LONGODD1STYN", new ParamMap("LONGODD1STYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether the first coupon payment is short or long."));
        paramMapHashMap.put("LONGODDLASTYN", new ParamMap("LONGODDLASTYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether the last  coupon payment is short or long."));
        paramMapHashMap.put("MATFLOOR",
                new ParamMap("MATFLOOR", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINPCT, Limits.MAXPCT,
                        "Indicates the minimum face amount at redemption. Used for inflation protected bonds."));
        paramMapHashMap.put("MATLENOPT", new ParamMap("MATLENOPT", Domains.DOMAINSTRING, 'N', 'Y', "matureLengthList",
                1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether the maturity length should be reported as days or years and whether it should be weighted by principal or cashflow payments."));
        paramMapHashMap.put("MKTPRCSTART",
                new ParamMap("MKTPRCSTART", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINPRICE,
                        Limits.MAXPRICE,
                        "Indicates the market price used performance attribution and gain/loss calculations."));
        paramMapHashMap.put("MKTPRCFUTURE", new ParamMap("MKTPRCFUTURE", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                Limits.MINPRICE, Limits.MAXPRICE,
                "Indicates the market price on a future date with respect to settle date for performance attribution and swap analysis."));
        paramMapHashMap.put("MKTPRCS", new ParamMap("MKTPRCS", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINPRICE, Limits.MAXPRICE, "Indicates a set of market prices used for return calculations."));
        paramMapHashMap.put("MKTYLDS", new ParamMap("MKTYLD", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1,
                Limits.MINYIELD, Limits.MAXYIELD, "Indicates the market yield used for gain/loss calculations."));
        // paramMapHashMap.put("MUNIYN", new ParamMap("MUNIYN", Domains.DOMAINYESNO,
        // 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE, "Indicates whether the
        // bond is a Municipal bond whose interest income is state and federal tax
        // free."));
        paramMapHashMap.put("NAMEDELIMITER",
                new ParamMap("NAMEDELIMITER", Domains.DOMAINSTRING, 'N', 'Y', "delimitList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the delimiter separating the name of the calculation from its value."));
        paramMapHashMap.put("ODDNESSPRCYN", new ParamMap("ODDNESSPRCYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to take account of odd long or short periods in  the price or yield calculations."));
        paramMapHashMap.put("OUTSTRINGFORMAT", new ParamMap("OUTSTRINGFORMAT", Domains.DOMAINSTRING, 'N', 'Y',
                "outputStringList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether you wish the calculation result string should be in JSON format of key-value pair format or nt output string"));
        paramMapHashMap.put("PASSFACTOR", new ParamMap("PASSFACTOR", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1,
                Limits.MINFACTOR, Limits.MAXFACTOR, "Indicates the most recent pass-thru factor."));
        paramMapHashMap.put("PASSFACTOR1", new ParamMap("PASSFACTOR1", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1,
                Limits.MINFACTOR, Limits.MAXFACTOR,
                "Indicates an initial pass thru factor used with an ending pass thru factor to estimate the prepayment speed of a pass-thru and thus the price or yield."));
        paramMapHashMap.put("PASSFACTOR2", new ParamMap("PASSFACTOR2", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1,
                Limits.MINFACTOR, Limits.MAXFACTOR,
                "Indicates an ending pass thru factor used with an initial pass thru factor to estimate the prepayment speed of a pass-thru and thus the price or yield."));
        paramMapHashMap.put("PASSFACTORS",
                new ParamMap("PASSFACTORS", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 1, Limits.MINFACTOR,
                        Limits.MAXFACTOR,
                        "Indicates a set of pass thru factors. Used to compute cashflows and analytics."));
        paramMapHashMap.put("PASSMODEL", new ParamMap("PASSMODEL", Domains.DOMAINSTRING, 'N', 'Y', "passModelList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates the prepayment model used to generate prepayment and principal cashflows and analytics."));
        paramMapHashMap.put("PASSPAYOFF", new ParamMap("PASSPAYOFF", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MINPASSPAYOFF, Limits.MAXPASSPAYOFF,
                "Indicates the remaining passthru factor that trigger a payoff of the residual mortgage balance by the transfer agent"));
        paramMapHashMap.put("PASSPROJECTMETH",
                new ParamMap("PASSPROJECTMETH", Domains.DOMAINSTRING, 'N', 'Y', "passthruProjectList", 1,
                        Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the method to use in  estimating prepayment speed for future cashflows. ."));
        paramMapHashMap.put("PASSWALA",
                new ParamMap("PASSWALA", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINWALA, Limits.MAXWALA,
                        "Indicates the vendor supplied weighted average pass thru age in months. ."));
        paramMapHashMap.put("PASSWARM",
                new ParamMap("PASSWARM", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINWARM, Limits.MAXWARM,
                        "Indicates the vendor supplied remaining weighted average pass thru maturity in months. ."));
        paramMapHashMap.put("PAYLEGBENCHMARK", new ParamMap("PAYLEGBENCHMARK", Domains.DOMAINSTRING, 'N', 'Y',
                "benchmarkList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the floating rate benchmark which determines the fixing rates for the interest of the pay leg of an IR Swaps."));
        // pay leg enumerated rates set
        paramMapHashMap.put("PAYLEGRT",
                new ParamMap("PAYLEGRESETRT", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINYIELD,
                        Limits.MAXYIELD,
                        "Indicates the set of base reset rates associated with the pay leg of an IR Swap."));
        paramMapHashMap.put("PAYLEGCAP", new ParamMap("PAYLEGRESETCAP", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINYIELD, Limits.MAXYIELD, "Indicates the set of maximum floating rate over a time interval."));
        paramMapHashMap.put("PAYLEGFLOOR",
                new ParamMap("PAYLEGRESETFLOOR", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINYIELD,
                        Limits.MAXYIELD, "Indicates the set of minimum floating rate over a time interval."));
        paramMapHashMap.put("PAYLEGSPRD", new ParamMap("PAYLEGRESETSPRD", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINRATE, Limits.MAXRATE,
                "Indicates the set of reset rate spreads to be used to determine the absolute interest rates for this bond."));
        paramMapHashMap.put("PAYLEGLIFECAP",
                new ParamMap("PAYLEGRESETLIFECAP", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINRATE,
                        Limits.MAXRATE,
                        "Indicates the set of maximum reset rates that can be set for this floating rate bond."));
        paramMapHashMap.put("PAYLEGLIFEFLOOR",
                new ParamMap("PAYLEGRESETLIFEFLOOR", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINRATE,
                        Limits.MAXRATE,
                        "Indicates the the minimum reset rate that can be set for this floating rate bond."));
        paramMapHashMap.put("PAYLEGMULTIPLIER", new ParamMap("PAYLEGRESETMULTIPLIER", Domains.DOMAINDOUBLE, 'Y', 'N',
                "noList", 1, Limits.MINFACTOR, Limits.MAXFACTOR,
                "Indicates the set reset rate multipliers used to determine the absolute interest rate for this bond."));
        paramMapHashMap.put("DTPAYLEGMULTIPLIER",
                new ParamMap("DTPAYLEGMULTIPLIER", Domains.DOMAINDATE, 'Y', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the set of dates for the pay leg of a swap on which a reset multiptiplier applies"));
        // pay leg schedule
        paramMapHashMap.put("PAYLEGHOLIDAYRULE",
                new ParamMap("PAYLEGHOLIDAYRULE", Domains.DOMAINSTRING, 'N', 'Y', "holidayOffsetList", 1,
                        Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the holiday rule to use to adjust the  dates on the pay leg of a swap."));
        paramMapHashMap.put("PAYLEGPERIODICITY", new ParamMap("PAYLEGPERIODICITY", Domains.DOMAINPERIODICITY, 'N', 'Y',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the base time unit to use in setting the length of a payment period. It can be monthly time units or weekly time units or daily time units."));
        paramMapHashMap.put("PAYLEGSCHEDCOMPOUNDFREQ", new ParamMap("PAYLEGSCHEDCOMPOUNDFREQ", Domains.DOMAINSTRING,
                'N', 'Y', "compoundFreqList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the default the number of payment periods per year for month based payment frequencies."));
        paramMapHashMap.put("PAYLEGSCHEDDOM",
                new ParamMap("PAYLEGSCHEDDOM", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINDAYOFMONTH,
                        Limits.MAXDAYOFMONTH,
                        "Indicates the day of the month on which payment occurs on the receive leg of an IRS swap."));
        paramMapHashMap.put("PAYLEGSCHEDDOW",
                new ParamMap("PAYLEGSCHEDDOW", Domains.DOMAINSTRING, 'N', 'Y', "dayOfWeekList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the day of the week on which a payment occurs on the receive leg of n IRS swap."));
        paramMapHashMap.put("PAYLEGSCHEDEOMRULEYN", new ParamMap("PAYLEGSCHEDEOMRULEYN", Domains.DOMAINYESNO, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether or not payment dates should always fall on month end if one payment date falls on month end."));
        paramMapHashMap.put("PAYLEGSCHEDMOY", new ParamMap("PAYLEGSCHEDMOY", Domains.DOMAINSTRING, 'N', 'Y',
                "monthOfYearList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the month of year on which a payment date occurs on the receive leg of an IRS swap."));
        paramMapHashMap.put("DTPAYLEGSCHEDNEXTCPN",
                new ParamMap("PAYLEGSCHEDNEXTCPNDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the next payment date specified by the user relative to the settlement date."));
        paramMapHashMap.put("DTPAYLEGSCHEDOCCUR",
                new ParamMap("PAYLEGSCHEDOCCURDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the next payment date specified by the user relative to the settlement date."));
        paramMapHashMap.put("PAYLEGSCHEDOCCURYEAR",
                new ParamMap("PAYLEGSCHEDOCCURYEAR", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINYEAROFOCCUR,
                        Limits.MAXYEAROFOCCUR, "Indicates the year of a payment for the receive leg of an IRS Swap."));
        paramMapHashMap.put("DTPAYLEGSCHEDPREVCPN",
                new ParamMap("PAYLEGSCHEDPREVCPNDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the last payment date specified by the user relative to the settlement date."));
        paramMapHashMap.put("PAYLEGSCHEDREPEAT",
                new ParamMap("PAYLEGSCHEDREPEAT", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINREPEATFACTOR,
                        Limits.MAXREPEATFACTOR, "Indicates the number of base time units between payment dates."));
        paramMapHashMap.put("PAYLEGSCHEDUSENEXTLASTYN",
                new ParamMap("PAYLEGSCHEDUSENEXTLASTYN", Domains.DOMAINYESNO, 'N', 'Y', "timeUnitList", 1,
                        Limits.MINNONE, Limits.MAXNONE,
                        "Indicates whether to use the supplied next or prev schedule date in calculations."));
        paramMapHashMap.put("PAYLEGSCHEDWOM", new ParamMap("PAYLEGSCHEDWOM", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MINWEEKOFMONTH, Limits.MAXWEEKOFMONTH,
                "Indicates the week of the month on which a payment occurs for the receive leg of an IRS swap."));

        // pay leg reset schedule
        paramMapHashMap.put("PAYLEGRESETHOLIDAYRULE",
                new ParamMap("PAYLEGRESETHOLIDAYRULE", Domains.DOMAINSTRING, 'N', 'Y', "holidayOffsetList", 1,
                        Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the holiday rule to use to adjust the reset dates on the pay leg of a swap."));
        paramMapHashMap.put("PAYLEGRESETPERIODICITY", new ParamMap("PAYLEGRESETPERIODICITY", Domains.DOMAINPERIODICITY,
                'N', 'Y', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the base time unit to use in setting the length of a payment period. It can be monthly time units or weekly time units or daily time units."));
        paramMapHashMap.put("PAYLEGRESETSCHEDCOMPOUNDFREQ", new ParamMap("PAYLEGRESETSCHEDCOMPOUNDFREQ",
                Domains.DOMAINSTRING, 'N', 'Y', "compoundFreqList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the default the number of payment periods per year for month based payment frequencies."));
        paramMapHashMap.put("PAYLEGRESETSCHEDDOM",
                new ParamMap("PAYLEGRESETSCHEDDOM", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINDAYOFMONTH,
                        Limits.MAXDAYOFMONTH,
                        "Indicates the day of the month on which payment occurs on the receive leg of an IRS swap."));
        paramMapHashMap.put("PAYLEGRESETSCHEDDOW",
                new ParamMap("PAYLEGRESETSCHEDDOW", Domains.DOMAINSTRING, 'N', 'Y', "dayOfWeekList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the day of the week on which a payment occurs on the receive leg of n IRS swap."));
        paramMapHashMap.put("PAYLEGRESETSCHEDEOMRULEYN", new ParamMap("PAYLEGRESETSCHEDEOMRULEYN", Domains.DOMAINYESNO,
                'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether or not payment dates should always fall on month end if one payment date falls on month end."));
        paramMapHashMap.put("PAYLEGRESETSCHEDMOY", new ParamMap("PAYLEGRESETSCHEDMOY", Domains.DOMAINSTRING, 'N', 'Y',
                "monthOfYearList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the month of year on which a payment date occurs on the receive leg of an IRS swap."));
        paramMapHashMap.put("DTPAYLEGRESETSCHEDNEXTCPN",
                new ParamMap("PAYLEGRESETSCHEDNEXTCPNDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the next payment date specified by the user relative to the settlement date."));
        paramMapHashMap.put("DTPAYLEGRESETSCHEDOCCUR",
                new ParamMap("PAYLEGRESETSCHEDOCCURDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the next payment date specified by the user relative to the settlement date."));
        paramMapHashMap.put("PAYRESETLEGSCHEDOCCURYEAR",
                new ParamMap("PAYLEGRESETSCHEDOCCURYEAR", Domains.DOMAININT, 'N', 'N', "noList", 1,
                        Limits.MINYEAROFOCCUR, Limits.MAXYEAROFOCCUR,
                        "Indicates the year of a payment for the receive leg of an IRS Swap."));
        paramMapHashMap.put("DTPAYLEGRESETSCHEDPREVCPN",
                new ParamMap("PAYLEGRESETSCHEDPREVCPNDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the last payment date specified by the user relative to the settlement date."));
        paramMapHashMap.put("PAYLEGRESETSCHEDREPEAT",
                new ParamMap("PAYLEGRESETSCHEDREPEAT", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINREPEATFACTOR,
                        Limits.MAXREPEATFACTOR, "Indicates the number of base time units between payment dates."));
        paramMapHashMap.put("PAYLEGRESETSCHEDUSENEXTLASTYN",
                new ParamMap("PAYLEGRESETSCHEDUSENEXTLASTYN", Domains.DOMAINYESNO, 'N', 'Y', "timeUnitList", 1,
                        Limits.MINNONE, Limits.MAXNONE,
                        "Indicates whether to use the supplied next or prev schedule date in calculations."));
        paramMapHashMap.put("PAYLEGRESETSCHEDWOM", new ParamMap("PAYLEGRESETSCHEDWOM", Domains.DOMAININT, 'N', 'N',
                "noList", 1, Limits.MINWEEKOFMONTH, Limits.MAXWEEKOFMONTH,
                "Indicates the week of the month on which a payment occurs for the receive leg of an IRS swap."));

        paramMapHashMap.put("PCTTOGGLECASH",
                new ParamMap("PCTTOGGLECASH", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINPCT,
                        Limits.MAXPCT,
                        "Indicates the percent of the schedule interest rate that is paid in cash for a toggle bond."));
        paramMapHashMap.put("PIKABLEYN", new ParamMap("PIKABLEYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether this bond a payment in kind bond."));
        paramMapHashMap.put("PIKRATE",
                new ParamMap("PIKRATE", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINRATE, Limits.MAXRATE,
                        "Indicates the set of payment-in-kind rates associated with the bond."));
        paramMapHashMap.put("PORTNAME", new ParamMap("PORTNAME", Domains.DOMAINSTRING, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates the name of the portfolio managing this bond position."));
        paramMapHashMap.put("PRCADJUSTEDYN", new ParamMap("PRCADJUSTEDYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether the price of sinking funds or index ratios or other principal reduction factors has already been included in the bond price."));
        paramMapHashMap.put("PREFUNDEDPRC",
                new ParamMap("PREFUNDEDPRC", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINPRICE,
                        Limits.MAXPRICE,
                        "Indicates the prefunded price paid to the holder when this bond is matured."));
        paramMapHashMap.put("PREPAYSPEED", new ParamMap("PREPAYSPEED", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                Limits.MINPREPAYSPEED, Limits.MAXPREPAYSPEED,
                "Indicates the prepayment speed to use in conjunction with the pass thru model to obtain cashflows and analytics."));
        paramMapHashMap.put("PURPRC", new ParamMap("PURPRC", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                Limits.MINMONEY, Limits.MAXMONEY,
                "Indicates the purchase cost excluding accrued interest. Used to calculate accretion/amortization."));
        paramMapHashMap.put("PURWALA", new ParamMap("PURWALA", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINWALA,
                Limits.MAXWALA, "Indicates the weighted average pass thru age on purchase date in months "));
        paramMapHashMap.put("PURWARM", new ParamMap("PURWARM", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINWARM,
                Limits.MAXWARM, "Indicates the weighted average pass thru maturity on purchase date in months"));
        paramMapHashMap.put("PURYLD", new ParamMap("PURYLD", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                Limits.MINYIELD, Limits.MAXYIELD, "Indicates the yield on purchase date for this bond."));
        paramMapHashMap.put("PUTABLEYN", new ParamMap("PUTABLEYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether this is a puttable bond or not."));
        paramMapHashMap.put("PUTNOTIFYDAYS", new ParamMap("PUTNOTIFYDAYS", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MINNOTIFYDAYS, Limits.MAXNOTIFYDAYS,
                "Indicates the number of days in advance of  to putting the bond that notice must be given to the issuer. ."));
        paramMapHashMap.put("PUTPRC", new ParamMap("PUTPRC", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINPRICE, Limits.MAXPRICE, "Indicates the set of put prices for this bond."));
        paramMapHashMap.put("PUTPRCTYPE", new ParamMap("PUTPRCTYPE", Domains.DOMAINSTRING, 'N', 'Y',
                "callPutPriceTypeList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the method to use in obtaining the put price. Based on a list of put prices or based on an amortization calculation."));
        paramMapHashMap.put("PUTTYPE", new ParamMap("PUTTYPE", Domains.DOMAINSTRING, 'N', 'Y', "callTypeList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether the put in an American or European put."));

        paramMapHashMap.put("RECLEGFIXEDYN",
                new ParamMap("RECLEGFIXEDYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Indicates whether the receive leg of an IRS Swap is fixed rate or floating."));
        paramMapHashMap.put("RECLEGRESETRT", new ParamMap("RECLEGRESETRT", Domains.DOMAINDOUBLE, 'Y', 'N', "noList",
                100, Limits.MINYIELD, Limits.MAXYIELD,
                "Indicates the set of base index rates associated with the receive leg of an interest rate swap."));
        // receive leg schedule
        paramMapHashMap.put("RECLEGBENCHMARK", new ParamMap("RECLEGBENCHMARK", Domains.DOMAINSTRING, 'N', 'Y',
                "benchmarkList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the fixing rate benchmark to use for determining the reset rates on the receive leg of an IR Swap."));
        paramMapHashMap.put("RECLEG1STPAYDT",
                new ParamMap("RECLEG1STPAYDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the 1st payment date after dated date for the receive leg of an IR Swap."));
        paramMapHashMap.put("RECLEGHOLIDAYRULE", new ParamMap("RECLEGHOLIDAYRULE", Domains.DOMAINSTRING, 'N', 'Y',
                "holidayOffsetList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the rule to use to adjust a payment date for the receive leg of a swap if it is schedule to occur on a non-business date."));
        paramMapHashMap.put("RECLEGPERIODICITY", new ParamMap("RECLEGPERIODICITY", Domains.DOMAINPERIODICITY, 'N', 'Y',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the base time unit to use in setting the length of a payment period. It can be monthly time units or weekly time units or daily time units."));
        paramMapHashMap.put("RECLEGSCHEDCOMPOUNDFREQ", new ParamMap("RECLEGSCHEDCOMPOUNDFREQ", Domains.DOMAINSTRING,
                'N', 'Y', "compoundFreqList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the default the number of payment periods per year for month based payment frequencies."));
        paramMapHashMap.put("RECLEGSCHEDDOM",
                new ParamMap("RECLEGSCHEDDOM", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINDAYOFMONTH,
                        Limits.MAXDAYOFMONTH,
                        "Indicates the day of the month on which payment occurs on the receive leg of an IRS swap."));
        paramMapHashMap.put("RECLEGSCHEDDOW",
                new ParamMap("RECLEGSCHEDDOW", Domains.DOMAINSTRING, 'N', 'Y', "dayOfWeekList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the day of the week on which a payment occurs on the receive leg of n IRS swap."));
        paramMapHashMap.put("RECLEGSCHEDEOMRULEYN", new ParamMap("RECLEGSCHEDEOMRULEYN", Domains.DOMAINYESNO, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether or not payment dates should always fall on month end if one payment date falls on month end."));
        paramMapHashMap.put("RECLEGSCHEDMOY", new ParamMap("RECLEGSCHEDMOY", Domains.DOMAINSTRING, 'N', 'Y',
                "monthOfYearList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the month of year on which a payment date occurs on the receive leg of an IRS swap."));
        paramMapHashMap.put("DTRECLEGSCHEDNEXTCPN",
                new ParamMap("RECLEGSCHEDNEXTCPNDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the next payment date specified by the user relative to the settlement date."));
        paramMapHashMap.put("DTRECLEGSCHEDOCCUR",
                new ParamMap("RECLEGSCHEDOCCURDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the next payment date specified by the user relative to the settlement date."));
        paramMapHashMap.put("RECLEGSCHEDOCCURYEAR",
                new ParamMap("RECLEGSCHEDOCCURYEAR", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINYEAROFOCCUR,
                        Limits.MAXYEAROFOCCUR, "Indicates the year of a payment for the receive leg of an IRS Swap."));
        paramMapHashMap.put("DTRECLEGSCHEDPREVCPN",
                new ParamMap("RECLEGSCHEDPREVCPNDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the last payment date specified by the user relative to the settlement date."));
        paramMapHashMap.put("RECLEGSCHEDREPEAT",
                new ParamMap("RECLEGSCHEDREPEAT", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINREPEATFACTOR,
                        Limits.MAXREPEATFACTOR, "Indicates the number of base time units between payment dates."));
        paramMapHashMap.put("RECLEGSCHEDUSENEXTLASTYN",
                new ParamMap("RECLEGSCHEDUSENEXTLASTYN", Domains.DOMAINYESNO, 'N', 'Y', "timeUnitList", 1,
                        Limits.MINNONE, Limits.MAXNONE,
                        "Indicates whether to use the supplied next or prev schedule date in calculations."));
        paramMapHashMap.put("RECLEGSCHEDWOM", new ParamMap("RECLEGSCHEDWOM", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MINWEEKOFMONTH, Limits.MAXWEEKOFMONTH,
                "Indicates the week of the month on which a payment occurs for the receive leg of an IRS swap."));

        paramMapHashMap.put("RECLEGRT",
                new ParamMap("RECLEGRESETRT", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINYIELD,
                        Limits.MAXYIELD,
                        "Indicates the set of base reset rates associated with the receive leg of an IR Swap."));
        paramMapHashMap.put("RECLEGCAP", new ParamMap("RECLEGRESETCAP", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINYIELD, Limits.MAXYIELD, "Indicates the set of maximum floating rate over a time interval."));
        paramMapHashMap.put("RECLEGFLOOR",
                new ParamMap("RECLEGRESETFLOOR", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINYIELD,
                        Limits.MAXYIELD, "Indicates the set of minimum floating rate over a time interval."));
        paramMapHashMap.put("RECLEGSPRD", new ParamMap("RECLEGRESETSPRD", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINRATE, Limits.MAXRATE,
                "Indicates the set of reset rate spreads to be used to determine the absolute interest rates for this bond."));
        paramMapHashMap.put("RECLEGLIFECAP",
                new ParamMap("RECLEGRESETLIFECAP", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINRATE,
                        Limits.MAXRATE,
                        "Indicates the set of maximum reset rates that can be set for this floating rate bond."));
        paramMapHashMap.put("RECLEGLIFEFLOOR",
                new ParamMap("RECLEGRESETLIFEFLOOR", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINRATE,
                        Limits.MAXRATE,
                        "Indicates the the minimum reset rate that can be set for this floating rate bond."));
        paramMapHashMap.put("RECLEGMULTIPLIER", new ParamMap("RECLEGRESETMULTIPLIER", Domains.DOMAINDOUBLE, 'Y', 'N',
                "noList", 1, Limits.MINFACTOR, Limits.MAXFACTOR,
                "Indicates the set reset rate multipliers used to determine the absolute interest rate for this bond."));
        paramMapHashMap.put("DTRECLEGMULTIPLIER", new ParamMap("DTRECLEGMULTIPLIER", Domains.DOMAINDATE, 'Y', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the set of dates for the receive leg of a swap on which a reset multiptiplier applies"));

        paramMapHashMap.put("RECOVERYRATE", new ParamMap("RECOVERYRATE", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                Limits.MINPCT, Limits.MAXPCT, "Indicates the recovery rate for a credit default swap."));
        paramMapHashMap.put("REDEEMAMT", new ParamMap("REDEEMAMT", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 1,
                Limits.MINMONEY, Limits.MAXMONEY,
                "Indicates a set of redemption amounts to be used for calculating the principal payments for this bond."));
        paramMapHashMap.put("REDEEMFACTOR", new ParamMap("REDEEMFACTOR", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 1,
                Limits.MINEXDIVOFFSET, Limits.MAXFACTOR,
                "Indicates a set of redemption factors to be used for calculating the principal payments for this bond."));
        paramMapHashMap.put("REDEEMPAYOFF",
                new ParamMap("REDEEMPAYOFF", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINPCT,
                        Limits.MAXPCT,
                        "Indicates that  when the remaining principal hits this value it will entirely be repaid."));
        paramMapHashMap.put("REDEEMPCT", new ParamMap("REDEEMPCT", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINPCT, Limits.MAXPCT,
                "Indicates a set of redemption percents to be used for calculating the principal payments for this bond."));
        paramMapHashMap.put("REDEEMPRIN",
                new ParamMap("REDEEMPRIN", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINPCT, Limits.MAXPCT,
                        "Indicates the percent of face amount that will be paid on maturity date."));
        paramMapHashMap.put("REINMATLEN", new ParamMap("REINMATLEN", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 1,
                Limits.MINMATLEN, Limits.MAXMATLEN,
                "Indicates a future date on which a reinvestment rate applies - length in years associated with each reinvestment rate."));
        paramMapHashMap.put("REINRT", new ParamMap("REINRT", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINRATE, Limits.MAXRATE,
                "Indicates the reinvestment rate for cashflows generated by interest or maturity oayments in percent."));
        paramMapHashMap.put("REINVESTMETH",
                new ParamMap("REINVESTMETH", Domains.DOMAINSTRING, 'N', 'Y', "reinInvestList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Identifies the method for reinveting cashflows."));
        paramMapHashMap.put("RESERVED",
                new ParamMap("RESERVED", Domains.DOMAINSTRING, 'N', 'Y', "compoundFreqList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the number of payment periods per year for month based payment frequencies."));

        // reset rates
        paramMapHashMap.put("RESETBENCHMARK", new ParamMap("RESETBENCHMARK", Domains.DOMAINSTRING, 'N', 'Y',
                "benchmarkList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the floating rate benchmark which determines the fixing rates for the interest of a floating rate bond."));
        paramMapHashMap.put("RESETRT", new ParamMap("RESETRT", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINYIELD, Limits.MAXYIELD,
                "Indicates the set of base index rates associated with this bond and used to determine the absolute interest rates for this bond."));
        paramMapHashMap.put("RESETCAP", new ParamMap("RESETCAP", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINYIELD, Limits.MAXYIELD, "Indicates the set of maximum floating rate over a time interval."));
        paramMapHashMap.put("RESETFLOOR", new ParamMap("RESETFLOOR", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINYIELD, Limits.MAXYIELD, "Indicates the set of minimum floating rate over a time interval."));
        paramMapHashMap.put("RESETSPRD", new ParamMap("RESETSPRD", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINRATE, Limits.MAXRATE,
                "Indicates the set of reset rate spreads to be used to determine the absolute interest rates for this bond."));
        paramMapHashMap.put("RESETLIFECAP",
                new ParamMap("RESETLIFECAP", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINRATE,
                        Limits.MAXRATE,
                        "Indicates the set of maximum reset rates that can be set for this floating rate bond."));
        paramMapHashMap.put("RESETLIFEFLOOR",
                new ParamMap("RESETLIFFLOOR", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINRATE,
                        Limits.MAXRATE,
                        "Indicates the the minimum reset rate that can be set for this floating rate bond."));
        paramMapHashMap.put("RESETMULTIPLIER", new ParamMap("RESETMULTIPLIER", Domains.DOMAINDOUBLE, 'Y', 'N', "noList",
                1, Limits.MINFACTOR, Limits.MAXFACTOR,
                "Indicates the set reset rate multipliers used to determine the absolute interest rate for this bond."));
        // floating rate reset schedule
        paramMapHashMap.put("RESETPERIODICITY", new ParamMap("RESETPERIODICITY", Domains.DOMAINPERIODICITY, 'N', 'Y',
                "RESETPERIODICITY", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the base time unit to use in setting the length of a payment period. It can be monthly time units or weekly time units or daily time units."));
        paramMapHashMap.put("RESETHOLIDAYRULE", new ParamMap("RESETHOLIDAYRULE", Domains.DOMAINSTRING, 'N', 'Y',
                "holidayOffsetList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the rule to use to adjust a reset date if it is schedule to occur on a non-business date."));
        paramMapHashMap.put("RESETSCHEDDOM",
                new ParamMap("RESETSCHEDDOM", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINDAYOFMONTH,
                        Limits.MAXDAYOFMONTH,
                        "Indicates the day of the month on which payment occurs on the receive leg of an IRS swap."));
        paramMapHashMap.put("RESETSCHEDDOW",
                new ParamMap("RESETSCHEDDOW", Domains.DOMAINSTRING, 'N', 'Y', "dayOfWeekList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the day of the week on which a payment occurs on the receive leg of an IRS swap."));
        paramMapHashMap.put("RESETSCHEDEOMRULEYN", new ParamMap("RESETSCHEDEOMRULEYN", Domains.DOMAINYESNO, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether or not payment dates should always fall on month end if one payment date falls on month end."));
        paramMapHashMap.put("RESETSCHEDMOY", new ParamMap("RESETSCHEDMOY", Domains.DOMAINSTRING, 'N', 'Y',
                "monthOfYearList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the month of year on which a payment date occurs on the receive leg of an IRS swap."));
        paramMapHashMap.put("RESETSCHEDNEXTCPNDT",
                new ParamMap("RESETSCHEDNEXTCPNDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the next payment date specified by the user relative to the settlement date."));
        paramMapHashMap.put("RESETSCHEDOCCURDT",
                new ParamMap("RESETSCHEDOCCURDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the next payment date specified by the user relative to the settlement date."));
        paramMapHashMap.put("RESETSCHEDOCCURYEAR",
                new ParamMap("RESETSCHEDOCCURYEAR", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINYEAROFOCCUR,
                        Limits.MAXYEAROFOCCUR, "Indicates the year of a payment for the receive leg of an IRS Swap."));
        paramMapHashMap.put("RESETSCHEDPREVCPNDT",
                new ParamMap("RESETSCHEDPREVCPNDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the last payment date specified by the user relative to the settlement date."));

        paramMapHashMap.put("RESETSCHEDREPEAT",
                new ParamMap("RESETSCHEDREPEAT", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINREPEATFACTOR,
                        Limits.MAXREPEATFACTOR, "Indicates the number of base time units between payment dates."));
        paramMapHashMap.put("RESETSCHEDUSENEXTLASTYN",
                new ParamMap("RESETSCHEDUSENEXTLASTYN", Domains.DOMAINYESNO, 'N', 'Y', "timeUnitList", 1,
                        Limits.MINNONE, Limits.MAXNONE,
                        "Indicates whether to use the supplied next or prev schedule date in calculations."));
        paramMapHashMap.put("RESETSCHEDWOM", new ParamMap("RESETSCHEDWOM", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MINWEEKOFMONTH, Limits.MAXWEEKOFMONTH,
                "Indicates the week of the month on which a payment occurs for the receive leg of an IRS swap."));
        paramMapHashMap.put("RESETSHEDCOMPOUNDFREQ", new ParamMap("RESETSHEDCOMPOUNDFREQ", Domains.DOMAINSTRING, 'N',
                'Y', "compoundFreqList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the default the number of payment periods per year for month based payment frequencies."));

        // receive leg enumerated rates set
        paramMapHashMap.put("RECLEGRESETRT",
                new ParamMap("RECLEGRESETRT", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINYIELD,
                        Limits.MAXYIELD, "Indicates the set of base reset for the receive leg of an IR Swap."));
        paramMapHashMap.put("RECLEGRESETCAP",
                new ParamMap("RECLEGRESETCAP", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINYIELD,
                        Limits.MAXYIELD, "Indicates the set of maximum floating rate over a time interval."));
        paramMapHashMap.put("RECLEGRESETFLOOR",
                new ParamMap("RECLEGRESETFLOOR", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINYIELD,
                        Limits.MAXYIELD, "Indicates the set of minimum floating rate over a time interval."));
        paramMapHashMap.put("RECLEGRESETSPRD", new ParamMap("RECLEGRESETSPRD", Domains.DOMAINDOUBLE, 'Y', 'N', "noList",
                100, Limits.MINRATE, Limits.MAXRATE,
                "Indicates the set of reset rate spreads to be used to determine the absolute interest rates for this bond."));
        paramMapHashMap.put("RECLEGRESETLIFECAP",
                new ParamMap("RECLEGRESETLIFECAP", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINRATE,
                        Limits.MAXRATE,
                        "Indicates the set of maximum reset rates that can be set for this floating rate bond."));
        paramMapHashMap.put("RECLEGRESETLIFEFLOOR",
                new ParamMap("RECLEGRESETLIFEFLOOR", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINRATE,
                        Limits.MAXRATE,
                        "Indicates the the minimum reset rate that can be set for this floating rate bond."));
        paramMapHashMap.put("RECLEGRESETMULTIPLIER", new ParamMap("RECLEGRESETMULTIPLIER", Domains.DOMAINDOUBLE, 'Y',
                'N', "noList", 1, Limits.MINFACTOR, Limits.MAXFACTOR,
                "Indicates the set reset rate multipliers used to determine the absolute interest rate for this bond."));
        // receive leg schedule
        paramMapHashMap.put("RECLEGRESETBENCHMARK", new ParamMap("RESETRECBENCHMARK", Domains.DOMAINSTRING, 'N', 'Y',
                "benchmarkList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the floating rate benchmark which determines the fixing rates for the interest of the receive leg of an IR Swaps."));
        paramMapHashMap.put("RECLEGRESETHOLIDAYRULE",
                new ParamMap("RECLEGRESETHOLIDAYRULE", Domains.DOMAINSTRING, 'N', 'Y', "holidayOffsetList", 1,
                        Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the holiday rule to use to adjust the reset dates on the receive leg of a swap."));
        paramMapHashMap.put("RECLEGRESETPERIODICITY", new ParamMap("RECLEGRESETPERIODICITY", Domains.DOMAINPERIODICITY,
                'N', 'Y', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the base time unit to use in setting the length of a payment period. It can be monthly time units or weekly time units or daily time units."));
        paramMapHashMap.put("RECLEGRESETSCHEDCOMPOUNDFREQ", new ParamMap("RECLEGRESETSCHEDCOMPOUNDFREQ",
                Domains.DOMAINSTRING, 'N', 'Y', "compoundFreqList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the default the number of payment periods per year for month based payment frequencies."));
        paramMapHashMap.put("RECLEGRESETSCHEDDOM",
                new ParamMap("RECLEGRESETSCHEDDOM", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINDAYOFMONTH,
                        Limits.MAXDAYOFMONTH,
                        "Indicates the day of the month on which payment occurs on the receive leg of an IRS swap."));
        paramMapHashMap.put("RECLEGRESETSCHEDDOW",
                new ParamMap("RECLEGRESETSCHEDDOW", Domains.DOMAINSTRING, 'N', 'Y', "dayOfWeekList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the day of the week on which a payment occurs on the receive leg of n IRS swap."));
        paramMapHashMap.put("RECLEGRESETSCHEDEOMRULEYN",
                new ParamMap("RECLEGRESETSCHEDEOMRULEYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates whether to use the supplied next or prev schedule date in calculations."));
        paramMapHashMap.put("RECLEGRESETSCHEDMOY", new ParamMap("RECLEGRESETSCHEDMOY", Domains.DOMAINSTRING, 'N', 'Y',
                "monthOfYearList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the month of year on which a payment date occurs on the receive leg of an IRS swap."));
        paramMapHashMap.put("RECLEGRESETSCHEDNEXTCPNDT",
                new ParamMap("RECLEGRESETSCHEDNEXTCPNDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the next payment date specified by the user relative to the settlement date."));
        paramMapHashMap.put("RECLEGRESETSCHEDOCCURDT",
                new ParamMap("RECLEGRESETSCHEDOCCURDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the next payment date specified by the user relative to the settlement date."));
        paramMapHashMap.put("RECLEGRESETSCHEDOCCURYEAR",
                new ParamMap("RECLEGRESETSCHEDOCCURYEAR", Domains.DOMAININT, 'N', 'N', "noList", 1,
                        Limits.MINYEAROFOCCUR, Limits.MAXYEAROFOCCUR,
                        "Indicates the year of a payment for the receive leg of an IRS Swap."));
        paramMapHashMap.put("RECLEGRESETSCHEDPREVCPNDT",
                new ParamMap("RECLEGRESETSCHEDPREVCPNDT", Domains.DOMAINDATE, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the last payment date specified by the user relative to the settlement date."));
        paramMapHashMap.put("RECLEGRESETSCHEDREPEAT",
                new ParamMap("RECLEGRESETSCHEDREPEAT", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINREPEATFACTOR,
                        Limits.MAXREPEATFACTOR, "Indicates the number of base time units between payment dates."));
        paramMapHashMap.put("RECLEGRESETSCHEDUSENEXTLASTYN",
                new ParamMap("RECLEGRESETSCHEDUSENEXTLASTYN", Domains.DOMAINYESNO, 'N', 'Y', "timeUnitList", 1,
                        Limits.MINNONE, Limits.MAXNONE,
                        "Indicates whether to use the supplied next or prev schedule date in calculations."));
        paramMapHashMap.put("RECLEGRESETSCHEDWOM", new ParamMap("RECLEGRESETSCHEDWOM", Domains.DOMAININT, 'N', 'N',
                "noList", 1, Limits.MINWEEKOFMONTH, Limits.MAXWEEKOFMONTH,
                "Indicates the week of the month on which a payment occurs for the receive leg of an IRS swap."));

        paramMapHashMap.put("RETURNWT", new ParamMap("RETURNWT", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1,
                Limits.MINWEIGHT, Limits.MAXWEIGHT,
                "Indicates a weighting of the importance of duration shift minimization vs reurn mazimization."));
        paramMapHashMap.put("RISKRETURNGOAL", new ParamMap("RISKRETURNGOAL", Domains.DOMAINSTRING, 'N', 'Y',
                "riskReturnList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the goal to determine the best trades to swap when evaluating trade opportunities."));
        paramMapHashMap.put("SECURITYID", new ParamMap("SECURITYID", Domains.DOMAINSTRING, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates an internal security identifier for this bond."));
        paramMapHashMap.put("SECURITYTYPE",
                new ParamMap("SECURITYTYPE", Domains.DOMAINSTRING, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates the security type to use in defaulting various characteristics for this bond."));
        paramMapHashMap.put("SERVICERATE",
                new ParamMap("SERVICERATE", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINSERVICERATE,
                        Limits.MAXSERVICERATE,
                        "Indicates the service rate changed by an agent for administrating a pass thru security."));
        paramMapHashMap.put("SETTLECASH", new ParamMap("SETTLECASH", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1,
                Limits.MINMONEY, Limits.MAXMONEY,
                "Indicates the amount of cash invested to obtain a specified set of future cashflows. Used to calculate analytics on an arbitrary stream of cashflows."));
        paramMapHashMap.put("SETTLEPRC",
                new ParamMap("SETTLEPRC", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINPRICE,
                        Limits.MAXPRICE,
                        "Indicates the market yield to use in calculating analytics on settlement date."));
        paramMapHashMap.put("SETTLEYLD",
                new ParamMap("SETTLEYLD", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINYIELD,
                        Limits.MAXYIELD,
                        "Indicates the market price to use in calculating analytics on settlement date."));
        paramMapHashMap.put("SFIGACCRUAL",
                new ParamMap("SFIGACCRUAL", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINSFIG, Limits.MAXSFIG,
                        "Indicates the decimal place precision to use in reporting accounting accruals."));
        paramMapHashMap.put("SFIGACCRUED",
                new ParamMap("SFIGACCRUED", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINSFIG, Limits.MAXSFIG,
                        "Indicates the decimal place precision to use in reporting accrued interest."));
        paramMapHashMap.put("SFIGACCRUEDDAYCOUNT", new ParamMap("SFIGACCRUEDDAYCOUNT", Domains.DOMAININT, 'N', 'N',
                "noList", 1, Limits.MINSFIG, Limits.MAXSFIG,
                "Indicates the number of decimal places to truncate or round the daycount in adjusting the accrued interest calculation."));
        paramMapHashMap.put("SFIGACCRUEDPRIN", new ParamMap("SFIGACCRUEDPRIN", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MINSFIG, Limits.MAXSFIG,
                "Indicates the number of decimal places to truncate or round the principal factor in adjusting the accrued interest calculation."));
        paramMapHashMap.put("SFIGACCRUEDSIZE", new ParamMap("SFIGACCRUEDSIZE", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MINSFIG, Limits.MAXSFIG,
                "Indicates the number of decimal places to truncate or round the bond size in adjusting the accrued interest calculation."));
        paramMapHashMap.put("SFIGACCRUESIZE", new ParamMap("SFIGACCRUESIZE", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MINSFIG, Limits.MAXSFIG,
                "Indicates the decimal place precision to use in calculating the accrued interest on the accrued unit size."));
        paramMapHashMap.put("SFIGCASHFLOW",
                new ParamMap("SFIGCASHFLOW", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINSFIG, Limits.MAXSFIG,
                        "Indicates the number of decimal places to truncate or round the cashflows."));
        paramMapHashMap.put("SFIGCPI", new ParamMap("SFIGCPI", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINSFIG,
                Limits.MAXSFIG,
                "Indicates the decimal place precision to use in calculating an index ratio for an inflation protected bond."));
        paramMapHashMap.put("SFIGCURRENCY",
                new ParamMap("SFIGCURRENCY", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINSFIG, Limits.MAXSFIG,
                        "Indicates the decimal place precision to use in reporting currency amounts."));
        paramMapHashMap.put("SFIGIDXRATIO", new ParamMap("SFIGIDXRATIO", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MINSFIG, Limits.MAXSFIG,
                "Indicates the decimal place precision to use in calculating an index ratio for an inflation protected bond."));
        paramMapHashMap.put("SFIGIDXVALUE", new ParamMap("SFIGIDXVALUE", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MINSFIG, Limits.MAXSFIG,
                "Indicates the decimal place precision to use in calculating an index value for an inflation protected bond."));
        paramMapHashMap.put("SFIGPRC", new ParamMap("SFIGPRC", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINSFIG,
                Limits.MAXSFIG,
                "Indicates the decimal place precision to use in reporting analytic statistics such as duration and convexity."));
        paramMapHashMap.put("SFIGSTATISTIC",
                new ParamMap("SFIGSTATISTIC", Domains.DOMAININT, 'N', 'N', "noList", 1, Limits.MINSFIG, Limits.MAXSFIG,
                        "Indicates the decimal place precision to use in reporting price calculations."));
        paramMapHashMap.put("SFIGYIELD", new ParamMap("SFIGYIELD", Domains.DOMAININT, 'N', 'N', "noList", 1,
                Limits.MINSFIG, Limits.MAXSFIG, "Indicates the decimal place precision to use in reporting yields."));
        paramMapHashMap.put("SHOWRECAPYN", new ParamMap("SHOWRECAPYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to display a recap of user entered data when printing calcuation results to file."));
        paramMapHashMap.put("SHOWINDIVCALCYN",
                new ParamMap("SHOWINDIVCALCYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates whether to display individual security calculations for a portfolio."));
        paramMapHashMap.put("SINKABLEYN", new ParamMap("SINKABLEYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates whether this bond has a sinking fund schedule."));
        paramMapHashMap.put("SINKAMT",
                new ParamMap("SINKAMT", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 1, Limits.MINMONEY, Limits.MAXMONEY,
                        "Indicates the set of sinking funds amounts associated with this bond."));
        paramMapHashMap.put("SINKFACTOR", new ParamMap("SINKFACTOR", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1,
                Limits.MINPCT, Limits.MAXPCT,
                "Indicates the sinking percent of original face remaining on settlement date for this sinking fund bond."));
        paramMapHashMap.put("SINKPCT", new ParamMap("SINKPCT", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100,
                Limits.MINPCT, Limits.MAXPCT,
                "Indicates the set of sinking funds percent of original face amount associated with this bond."));
        paramMapHashMap.put("SINKPRC",
                new ParamMap("SINKPRCS", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 100, Limits.MINPRICE,
                        Limits.MAXPRICE, "Indicates the set of sinking fund prices associated with this bond."));
        paramMapHashMap.put("STARTPRC", new ParamMap("STARTPRC", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                Limits.MINPRICE, Limits.MAXPRICE,
                "Indicates initial market yield at the beginning of a horizon period to be used for horizon analysis."));
        paramMapHashMap.put("STARTYIELD", new ParamMap("STARTYIELD", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                Limits.MINYIELD, Limits.MAXYIELD,
                "Indicates initial market price at the beginning of a horizon period to be used for horizon analysis."));
        paramMapHashMap.put("STATEOFISSUE", new ParamMap("STATEOFISSUE", Domains.DOMAINSTRING, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates the state where the bond was issue - if the state of the holder is the same as state of issuer and bond is a muni then income for bond is non-taxable."));
        paramMapHashMap.put("STATEOFRESIDENCE", new ParamMap("STATEOFRESIDENCE", Domains.DOMAINSTRING, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the state of residence of the the holder - if the state of residence of the holder is the same as the state where the bond was issuer and the bond is a munu then income for bond is non-taxable."));
        paramMapHashMap.put("STATETAXEXEMPTYN",
                new ParamMap("STATETAXEXEMPTYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Indicates whether this bond is exempt from state income taxes."));
        paramMapHashMap.put("STRIPTYPE", new ParamMap("STRIPTYPE", Domains.DOMAINSTRING, 'N', 'Y', "stripTypeList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether the bond has interest stripped or principal stripped or neither for this bond."));
        paramMapHashMap.put("SWAPSIDE", new ParamMap("SWAPSIDE", Domains.DOMAINSTRING, 'N', 'Y', "swapSideList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to perform calculations on the pay leg only, receive leg only or the net of pay and receive legs for an IR Swap"));
        paramMapHashMap.put("SWAPPCTGAIN", new ParamMap("SWAPPCTGAIN", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 1,
                -Limits.MAXPCT, Limits.MAXPCT,
                "Indicates the minimum return over a trade swap must make over it's the worst case against no making a trade swaps"));
        paramMapHashMap.put("TAXADJALLOWEDYN",
                new ParamMap("TAXADJALLOWEDYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates whether this bond has tax exemptions. Used to generated after tax analytics."));
        paramMapHashMap.put("TAXHOLIDAYRULE", new ParamMap("TAXHOLIDAYRULE", Domains.DOMAINSTRING, 'N', 'Y',
                "holidayOffsetList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the rule to use to adjust a tax payment date if it is schedule to occur on a non-business date."));
        paramMapHashMap.put("TAXLOTID",
                new ParamMap("TAXLOTID", Domains.DOMAINSTRING, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the host system identifier for bond on its lot based accounting system."));
        paramMapHashMap.put("TAXPERIODICITY", new ParamMap("TAXPERIODICITY", Domains.DOMAINPERIODICITY, 'N', 'Y',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates the tax payment frequency using a repeat factor and time unit indicating the when tax payments occur. It can be monthly or weekly or daily time units."));
        paramMapHashMap.put("TAXRATEFEDCAPGAINLONG",
                new ParamMap("TAXRATEFEDCAPGAINLONG", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINTAXRATE,
                        Limits.MAXTAXRATE, "Indicates the federal tax rate on long term capital gains."));
        paramMapHashMap.put("TAXRATEFEDCAPGAINSHORT",
                new ParamMap("TAXRATEFEDCAPGAINSHORT", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINTAXRATE,
                        Limits.MAXTAXRATE, "Indicates the federal tax rate on short term capital gains."));
        paramMapHashMap.put("TAXRATEFEDINCOME",
                new ParamMap("TAXRATEFEDINCOME", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINTAXRATE,
                        Limits.MAXTAXRATE, "Indicates the federal tax rate on interest income."));
        paramMapHashMap.put("TAXRATESTATECAPGAINLONG",
                new ParamMap("TAXRATESTATECAPGAINLONG", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                        Limits.MINTAXRATE, Limits.MAXTAXRATE,
                        "Indicates the state tax rate on long term capital gains."));
        paramMapHashMap.put("TAXRATESTATECAPGAINSHORT",
                new ParamMap("TAXRATESTATECAPGAINSHORT", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100,
                        Limits.MINTAXRATE, Limits.MAXTAXRATE,
                        "Indicates the state tax rate on short term capital gains."));
        paramMapHashMap.put("TAXRATESTATEINCOME",
                new ParamMap("TAXRATESTATEINCOME", Domains.DOMAINDOUBLE, 'N', 'N', "noList", 100, Limits.MINTAXRATE,
                        Limits.MAXTAXRATE, "Indicates the state tax rate on interest income."));
        paramMapHashMap.put("TAXSCHEDOCCURDT", new ParamMap("TAXSCHEDOCCURDT", Domains.DOMAINDATE, 'N', 'N', "noList",
                1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates a date on which an tax payments will fall and used as the basis to determing future tax payment dates."));
        paramMapHashMap.put("TRADETYPE", new ParamMap("TRADETYPE", Domains.DOMAINSTRING, 'N', 'Y', "tradeTypeList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether this bond is a buy trade 'B' or sell trade 'S'. User for trade opportunity analytics"));
        paramMapHashMap.put("USECREDITSPRDYN", new ParamMap("USECREDITSPRDYN", Domains.DOMAINYESNO, 'N', 'N', "noList",
                1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to use the credit spread curve in addition to the risk free curve in calculations."));
        paramMapHashMap.put("USECURRENTREDEMPTIONYN", new ParamMap("USECURRENTREDEMPTIONYN", Domains.DOMAINYESNO, 'N',
                'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to use current redemption value in price and accrued interest calculations or face value"));
        paramMapHashMap.put("USEDEFAULTSFIGYN", new ParamMap("USEDEFAULTSFIGYN", Domains.DOMAINYESNO, 'N', 'N',
                "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether you want to use the system dafault decimal precision in reporting calculations."));
        paramMapHashMap.put("USEFORWARDCURVEYN",
                new ParamMap("USEFORWARDCURVEYN", Domains.DOMAINYESNO, 'N', 'Y', "noLIst", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates whether to use forward yield curve for forecasting future interest rates"));
        paramMapHashMap.put("USEFIXINGCURVEYN",
                new ParamMap("USEFIXINGCURVEYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Indicate whether to use the floating rate fixing curve for reset rates"));
        paramMapHashMap.put("USELOCALCURRENCYYN",
                new ParamMap("USELOCALCURRENCYYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE,
                        "Indicates whether you wish to convert the cashflow and analytics into another currrency."));
        paramMapHashMap.put("USERCASHDT", new ParamMap("USERCASHDT", Domains.DOMAINDATE, 'Y', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates a set of cash flows for creating your own arbitrary bond and obtain calculations on this set of arbitrary cashflows."));
        paramMapHashMap.put("USERCASHFLOW", new ParamMap("USERCASHFLOW", Domains.DOMAINDOUBLE, 'Y', 'N', "noList", 1,
                Limits.MINMONEY, Limits.MAXMONEY,
                "Indicates a set of cash flow dates for creating your own arbitrary bond and obtain calculations on this set of arbitrary cashflows."));
        paramMapHashMap.put("USEREINRATESYN", new ParamMap("USEREINRATES", Domains.DOMAINYESNO, 'N', 'Y', "noLIst", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to use or default reininvest raters when calcualting cashsflows or future values"));
        paramMapHashMap.put("VALIDATEYN",
                new ParamMap("VALIDATEYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE, Limits.MAXNONE,
                        "Indicates whether you wish to suspend validation on th data entered."));
        paramMapHashMap.put("WARNINGSOFFYN",
                new ParamMap("WARNINGSOFFYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Indicates what day of the week is a weekend for the country of this bond."));
        paramMapHashMap.put("WEEKEND1",
                new ParamMap("WEEKEND1", Domains.DOMAININT, 'N', 'Y', "dayOfWeekList", 1, Limits.MINNONE,
                        Limits.MAXNONE, "Indicates what day of the week is a weekend for the country of this bond."));
        paramMapHashMap.put("WEEKEND2", new ParamMap("WEEKEND2", Domains.DOMAININT, 'N', 'Y', "dayOfWeekList", 1,
                Limits.MINNONE, Limits.MAXNONE, "Indicates the decimal place precision to use in reporting yields."));
        paramMapHashMap.put("WHENISSUEYN", new ParamMap("WHENISSUEYN", Domains.DOMAINYESNO, 'N', 'N', "noList", 1,
                Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether this bond was a when-issued bond and allow calculation prior to dated date."));
        paramMapHashMap.put("YIELDCURVEINTERPMETH",
                new ParamMap("YIELDCURVEINTERPMETH", Domains.DOMAINSTRING, 'N', 'Y', "yieldCurveInterpList", 1,
                        Limits.MINNONE, Limits.MAXNONE,
                        "Indicates the method you wish to use in interpolating a yield cutve."));
        paramMapHashMap.put("YIELDFORECASTMETH", new ParamMap("YIELDFORECASTMETH", Domains.DOMAINSTRING, 'N', 'Y',
                "yieldForecastList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether to use an previous spot curve or generated forward curve to computes future values"));
        paramMapHashMap.put("YIELDQUOTETYPE", new ParamMap("YIELDQUOTETYPE", Domains.DOMAINSTRING, 'N', 'Y',
                "yieldQuoteList", 1, Limits.MINNONE, Limits.MAXNONE,
                "Indicates whether the yield you specified was based on a clean or dirty price. That whether you wish to calculate the price on a clean or dirty basis."));
    }

    /*
     * Handles a request as a single string
     * 
     **/

    public boolean assignBondParameters(String userString) {
        // ParamMap paramMap = new ParamMap(this.dtFormat, userString);
        boolean isComment = false;
        int noEntries = 0;
        int pIndex = UnAvails.NOTAVAILINT;
        // this.userString = userString;
        StringTokenizer stok = new StringTokenizer(userString, ";|");
        String strEntry = null;
        String paramName = null;
        String paramValue = null;
        noEntries = 0;
        // initializeChoices();
        while (stok.hasMoreTokens()) {
            isComment = false;
            strEntry = stok.nextToken();
            strEntry = strEntry.trim();
            strEntry = strEntry.toUpperCase();
            String[] tokens = strEntry.split("=");
            int noToks = tokens.length;
            String strParamPair = "";
            if (noToks == 0) // blank line
                continue;
            if (noToks == 1) {
                String errmsg = "parameter " + tokens[0] + " is missing a value";
                error.setError(getClass().getName(), Error.PARAMERROR, errmsg);
                ++noEntries;
                return false;
            }
            if (noToks == 2) {
                paramName = tokens[0].trim();
                paramValue = tokens[1];
                strParamPair = paramName + "=" + paramValue;
            } else {
                String errmsg;
                if (isInputJSONYn == "Y")
                    errmsg = "invalid JSON request string was specified";
                else
                    errmsg = "invalid request string was specified";
                error.setError(getClass().getName(), Error.PARAMERROR, errmsg);
                return false;
            }
            if (paramName.equalsIgnoreCase(BondConsts.COMMENT))
                continue;
            this.userCleanParamString = this.userCleanParamString + strParamPair + ";";
            paramName = paramName.toUpperCase();
            paramMap = paramMapHashMap.get(paramName);
            if (paramMap == null) {
                paramName = "COMMENT";
                paramMap = paramMapHashMap.get("COMMENT");
            }
            if (paramMap.isArrayYn == 'Y') {
                if (paramValue.charAt(0) == '[') {
                    paramValue = paramValue.substring(1, paramValue.length() - 1);
                    paramValue = paramValue.replaceAll("\"", "");
                    String[] strValueParts = paramValue.split(",");
                    int ip = -1;
                    int noparts = strValueParts.length;
                    while (++ip < noparts) {
                        convertValidateAssignParameter(paramName, strValueParts[ip]);
                        if (error.hasErrorYn == 'Y') {
                            return false;
                        }
                        ++noEntries;
                    }
                }
            } else {
                boolean isOk = convertValidateAssignParameter(paramName, paramValue);
                ++noEntries;
                if (!isOk)
                    return false;
            }
        }
        return true;
    }

    public boolean assignBondParameter(String paramName, String paramValue) {
        // ParamMap paramMap = new ParamMap(this.dtFormat, userString);
        boolean isComment = false;
        int pIndex = UnAvails.NOTAVAILINT;
        // this.userString = userString;
        if (paramName.equalsIgnoreCase(BondConsts.COMMENT))
            return true;
        paramName = paramName.toUpperCase();
        // break up array string into array of parameters
        boolean isOk = convertValidateAssignParameter(paramName, paramValue);
        return isOk;
    }

    ParamMap convertParamValue(String paramName, String paramValue) {
        int iVal = UnAvails.NOTAVAILINT;
        double dVal = UnAvails.NOTAVAILDOUBLE;
        boolean isConverted = false;
        String sVal = null;
        String sDtVal;
        Error error = new Error();
        String errmsg = "";
        this.paramMap = new ParamMap();
        paramMap.paramName = paramName;
        Dates dtVal = new Dates(error);
        if ((paramName).equalsIgnoreCase(BondConsts.COMMENT)) {
            paramMap.sVal = paramValue;
            return paramMap;
        }
        paramName = paramName.trim().toUpperCase();
        paramMap = paramMapHashMap.get(paramName);
        if (paramMap == null) {
            paramMap.errMsg = "Invalid parameter name " + paramName + " was specified";
            return paramMap;
        }
        // int domain = this.paramMaps.get(idx).paramDomain;
        int domain = paramMap.paramDomain;
        Character cVal = UnAvails.NOTAVAILCHAR;
        switch (domain) {
            case Domains.DOMAININT:
                try {
                    paramMap.iVal = Integer.parseInt(paramValue);
                } catch (Exception ex) {
                    paramMap.errMsg = "An invalid integer value " + paramValue + " has been specified for " + paramName;
                    paramMap.errMsg = errmsg;
                    paramMap.isOk = false;
                    return paramMap;
                }
                break;
            case Domains.DOMAINCHAR:
                sVal = paramValue.toUpperCase();
                paramMap.cVal = sVal.charAt(0);
                break;
            case Domains.DOMAINYESNO:
                sVal = paramValue.toUpperCase();
                cVal = sVal.charAt(0);
                paramMap.cVal = cVal;
                break;
            // to do validate that the date is a valid date string
            case Domains.DOMAINDATE:
                dtVal.setDate(paramValue, dtFormat);
                paramMap.dtVal = dtVal;
                if (dtVal.error.hasErrorYn == 'Y') {
                    paramMap.errMsg = dtVal.error.errorMessage;
                }
                break;
            case Domains.DOMAINDOUBLE:
                try {
                    dVal = Double.valueOf(paramValue);
                    paramMap.dVal = dVal;
                } catch (Exception ex) {
                    paramMap.errMsg = "An invalid numerical value " + paramValue + " has been specified for "
                            + paramName;
                    paramMap.isOk = false;
                }
                break;
            case Domains.DOMAINMONEY:
                paramValue = paramValue.replace(",", "");
                try {
                    dVal = Double.valueOf(paramValue);
                    paramMap.dVal = dVal;
                } catch (Exception ex) {
                    paramMap.errMsg = "An invalid money value " + paramValue + " has been specified for " + paramName;
                    paramMap.isOk = false;
                }
                break;
            case Domains.DOMAINPRICE:
                // dVal = convertPrice(paramValue);
                break;
            case Domains.DOMAINSTRING:
                paramMap.sVal = paramValue.toUpperCase();
                break;
            case Domains.DOMAINIGNORE:
                break;
            case Domains.DOMAINPERIODICITY:
                paramMap.sVal = paramValue.toUpperCase();
                break;
            default:
                paramMap.errMsg = "invalid domain " + paramMap.paramDomain + "detected for parameter " + paramName;
                paramMap.isOk = false;
                break;
        }
        return paramMap;
    }

    public boolean convertValidateAssignParameter(String paramName, String paramValue) {
        int iVal = UnAvails.NOTAVAILINT;
        double dVal = UnAvails.NOTAVAILDOUBLE;
        boolean isConverted = false, isValidated = false, isAssigned = false;
        String sVal = null;
        Dates dtVal = new Dates(error);
        String sDtVal;
        paramValue = paramValue.trim();
        // String dtFormat = "MM/DD/YYYY";
        // int idx = this.nameParam.indexOf(paramName.trim());
        paramMap = paramMapHashMap.get(paramName);
        if (paramMap == null) {
            String errmsg = "Invalid parameter name " + paramName + " was specified";
            error.setError(getClass().getName(), Error.PARAMERROR, errmsg);
            return false;
        }
        paramMap.paramName = paramName;
        // int domain = this.paramMaps.get(idx).paramDomain;
        int domain = paramMap.paramDomain;
        Character cVal = UnAvails.NOTAVAILCHAR;
        switch (domain) {
            case Domains.DOMAININT:
                try {
                    iVal = Integer.parseInt(paramValue);
                } catch (Exception ex) {
                    String errmsg = "An invalid integer value " + paramValue + " has been specified for " + paramName;
                    error.setError(getClass().getName(), Error.PARAMERROR, errmsg);
                    return false;
                } finally {
                    iVal = Integer.valueOf(paramValue);
                    error.clearError();
                }
                break;
            case Domains.DOMAINCHAR:
                sVal = paramValue.toUpperCase();
                cVal = sVal.charAt(0);
                break;
            case Domains.DOMAINYESNO:
                sVal = paramValue.toUpperCase();
                cVal = sVal.charAt(0);
                break;
            // to do validate that the date is a valid date string
            case Domains.DOMAINDATE:
                sDtVal = paramValue;
                dtVal.setDate(sDtVal, dtFormat);
                if (error.hasErrorYn == 'Y')
                    return false;
                break;
            case Domains.DOMAINDOUBLE:
                try {
                    dVal = Double.valueOf(paramValue);
                } catch (Exception ex) {
                    String errmsg = "An invalid numerical value " + paramValue + " has been specified for " + paramName;
                    error.setError(getClass().getName(), Error.PARAMERROR, errmsg);
                    return false;
                } finally {
                    try {
                        dVal = Double.valueOf(paramValue);
                        error.clearError();
                    } catch (Exception ex) {
                        String errmsg = "An invalid numerical value " + paramValue + " has been specified for "
                                + paramName;
                        error.setError(getClass().getName(), Error.PARAMERROR, errmsg);
                        return false;
                    }
                }
                break;
            case Domains.DOMAINMONEY:
                paramValue = paramValue.replace(",", "");
                try {
                    dVal = Double.valueOf(paramValue);
                } catch (Exception ex) {
                    String errmsg = "An invalid money value " + paramValue + " has been specified for " + paramName;
                    error.setError(getClass().getName(), Error.PARAMERROR, errmsg);
                    return false;
                }
                break;
            case Domains.DOMAINPRICE:
                // dVal = convertPrice(paramValue);
                break;
            case Domains.DOMAINSTRING:
                sVal = paramValue.toUpperCase();
                break;
            case Domains.DOMAINIGNORE:
                break;
            case Domains.DOMAINPERIODICITY:
                sVal = paramValue.toUpperCase();
                break;
            default:
                error.setError(this.getClass().getName(), Error.PARAMERROR,
                        "Invalid domain " + paramMap.paramDomain + "detected for parameter " + paramName);
                break;
        }
        isAssigned = assignToParameter(paramMap, iVal, dVal, dtVal, sVal, cVal);
        if (!isAssigned) {
            return false;
        }
        isValidated = validate(paramMap.paramName, iVal, dVal, sVal, cVal, dtVal);
        if (!isValidated) {
            return false;
        }

        return isValidated && isAssigned;
    }

    /*
     * Program: parseRequestStringToJson
     * Purpose: Converts a standard basispoint request to a Json string
     * Usage: Use to
     * 
     **/

    private boolean isIntInRange(String paramName, int iVal, double dMin, double dMax) {
        boolean inRange = false;
        int iMin = (int) dMin;
        int iMax = (int) dMax;
        if (iVal >= iMin && iVal <= iMax)
            inRange = true;
        else {
            inRange = false;
            error.setError(getClass().getName(), Error.PARAMERROR,
                    "Invalid parameter value for " + paramName + "(" + iVal + ")"
                            + "it is outside the allowed range between "
                            + iMin + " and " + iMax);
        }
        return inRange;
    }

    private boolean isDoubleInRange(String paramName, double dVal, double dMin, double dMax) {
        boolean inRange = false;
        if (dVal >= dMin && dVal <= dMax)
            inRange = true;
        else {
            inRange = false;
            error.setError(getClass().getName(),
                    "Parameter " + paramName + "(" + dVal + ")" + "is outside the allowed range between "
                            + dMin + " and " + dMax);
        }
        return inRange;
    }

    private boolean isValidDate(String paramName, Dates dtVal) {
        int year = dtVal.year;
        int mon = dtVal.month;
        int day = dtVal.day;
        String errmsg = "";
        if (year > Limits.MAXYEAR || year < Limits.MINYEAROFOCCUR) {
            dtVal.setUpdateDate(dtFormat);
            errmsg = "Invalid year specified for " + paramName.trim() + " for " + dtVal.getDateString();
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (mon < 1 || mon > 12) {
            dtVal.setUpdateDate(dtFormat);
            errmsg = "Invalid month specified for " + paramName + " for " + dtVal.getDateString();
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (day < 1) {
            dtVal.setUpdateDate(dtFormat);
            errmsg = "Invalid day specified for " + paramName + " for " + dtVal.getDateString();
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        if (dtVal.isLeapYear(year)) {
            if (day > CalUnits.daysInMonthLeap[mon])
                ;
            // return false;
            return true;
        } else {
            if (day <= CalUnits.daysInMonthNonLeap[mon])
                return true;
            else {
                dtVal.setUpdateDate(dtFormat);
                errmsg = "Invalid day specified for parameter " + paramName + " for " + dtVal.getDateString();
                error.setError(getClass().getName(), errmsg);
                return false;
            }
        }
    }

    boolean preSetParams(String strRequest) {
        String strPriority, nextRequest, nextParam, nextValue, strDtMaturity = UnAvails.NOTAVAILSTRING,
                strCpnRt = UnAvails.NOTAVAILSTRING;
        String strPassWala = UnAvails.NOTAVAILSTRING, strPassWarm = UnAvails.NOTAVAILSTRING,
                strDtSettle = UnAvails.NOTAVAILSTRING;
        Dates dtMature = new Dates();
        char useEndMonthRuleYn = 'N';
        int term = UnAvails.NOTAVAILINT;
        StringTokenizer stok = new StringTokenizer(strRequest, ";|");
        while (stok.hasMoreTokens()) {
            nextRequest = stok.nextToken();
            String[] tokens = nextRequest.split("=");
            if (2 != tokens.length) {
                if (nextRequest.charAt(0) == '#')
                    continue;
                String errmsg = "Invalid parameter assignment - missing an '=' ";
                error.setError(getClass().getName(), errmsg);
                return false;
            }
            nextParam = tokens[0].trim().toUpperCase();

            if (nextParam.equals("DUMPONERRORYN")) {
                bond.dumpOnErrorYn = tokens[1].charAt(0);
                error.setDumpOnErrorYn(bond.dumpOnErrorYn);
                continue;
            }
            if (nextParam.equals("DUMPCNTMAX")) {
                String strDumpCntMax = tokens[1];
                bond.dumpCntMax = Integer.valueOf(strDumpCntMax);
                error.setMaxDumpCount(bond.dumpCntMax);
                continue;
            }
            if (nextParam.equals("DTFORMAT")) {
                this.dtFormat = tokens[1];
                continue;
            }
            if (nextParam.equals("DESCR")) {
                bond.secDescr = tokens[1];
                continue;
            }
            if (nextParam.equals("SECURITYID")) {
                bond.securityId = tokens[1];
                continue;
            }
            if (nextParam.equals("INSTYPE")) {
                bond.instype = tokens[1];
                continue;
            }
            if (nextParam.equals("ISIN")) {
                bond.isin = tokens[1];
                continue;
            }
            if (nextParam.equals("CUSIP")) {
                bond.cusip = tokens[1];
                continue;
            }
            if (nextParam.equals("SECURITYTYPE")) {
                bond.securityType = tokens[1];
                continue;
            }
            if (nextParam.equals("DTMATURITY")) {
                bond.dtMaturity.setDate(tokens[1], dtFormat);
                continue;
            }
            if (nextParam.equals("CPNRT")) {
                bond.cpnRt = Double.valueOf(tokens[1]);
                continue;
            }
            if (nextParam.equals("PASSWALA")) {
                bond.passWala = Integer.valueOf(tokens[1]);
                continue;
            }
            if (nextParam.equals("PASSWARM")) {
                bond.passWarm = Integer.valueOf(tokens[1]);
                continue;
            }
            if (nextParam.equals("DTSETTLE")) {
                if (UnAvails.isUnAvail(dtFormat) || dtFormat == null)
                    dtFormat = "MM/DD/YYYY";
                bond.dtSettle.setDate(tokens[1], dtFormat);
                continue;
            }
            if (nextParam.equals("DTPASSFACTOR")) {
                if (UnAvails.isUnAvail(dtFormat))
                    dtFormat = "MM/DD/YYYY";
                bond.dtPassFactor.setDate(tokens[1], dtFormat);
                continue;
            }
        }
        return true;
    }

    public boolean assignToParameter(int iVal, double dVal, Dates dtVal, String sVal, Character cVal) {
        boolean isOk = false;
        switch (paramMap.paramDomain) {
            case Domains.DOMAININT:
                isOk = assignToIntParam(paramMap.paramName, iVal);
                break;
            case Domains.DOMAINDOUBLE:
                isOk = assignToDoubleParam(dVal);
                break;
            case Domains.DOMAINDATE:
                isOk = assignToDateParam(paramMap, dtVal);
                break;
            case Domains.DOMAINSTRING:
            case Domains.DOMAINPERIODICITY:
                isOk = assignToStringParam(paramMap, sVal);
                break;
            case Domains.DOMAINCHAR:
                isOk = assignToCharParm(paramMap, cVal);
                break;
            case Domains.DOMAINYESNO:
                isOk = assignToCharParm(paramMap, cVal);
                break;
            default:
                error.setError(getClass().getName(), "Invalid parameter domain " + paramMap.paramDomain + " detected");
                return false;
        }
        return isOk;
    }

    private double convertPrice(String paramValue) {
        paramValue = paramValue.trim();
        String[] sTerms = paramValue.split("\"+");
        double dVal = 0.;
        String errmsg = "";
        switch (sTerms.length) {
            case 0:
                errmsg = "an empty price value was entered";
                error.setError(getClass().getName(), errmsg);
                return 0.;
            case 1:
            case 2:
                try {
                    dVal = Double.valueOf(sTerms[0]);
                } catch (Exception ex) {
                    errmsg = "an invalid price value of " + paramValue + " was entered";
                    error.setError(getClass().getName(), errmsg);
                    return 0.;
                }
                String sFrac = "";
                double frac = 0.;
                if (sTerms.length == 2) {
                    char plus = sTerms[1].charAt(-1 + sTerms[1].length());
                    if (plus == '+') {
                        dVal += 1. / 64.;
                        sFrac = sTerms[1].replace("+", "");
                    }
                    try {
                        frac = Double.valueOf(sTerms[1]);
                    } catch (Exception ex) {
                        errmsg = "an invalid price value of " + paramValue + " was entered";
                        error.setError(getClass().getName(), errmsg);
                    }
                    dVal += frac;
                    break;
                }
            default:
                errmsg = "an invalid price value of " + paramValue + " was entered";
                error.setError(getClass().getName(), errmsg);
        }

        return dVal;
    }

    public void commandLineHelp() {
        System.out.println("Argument choices:");
        System.out.println("1 Argument: <JSON request string>"); // all JSON
        System.out.println(
                "2 Arguments: <Request string> ; <All JSON input and output = Y or All BasisPoint format = N>");// all
                                                                                                                // JSON
        System.out.println(
                "3 Arguments: <Root directory> <Request string> <All JSON input and output = Y or All BasisPoint format = N>"); // all
                                                                                                                                // JSON
        System.out.println(
                "4 arguments: <Root directory> <File Name or Request String> <UseFile input file = Y or Entered  Request string = N> <All JSON input and output = Y or All BasisPoint format = N>");
        System.out.println(
                "5 arguments: <Root directory> <File Name or Request String> <UseFile input file = Y or Entered  Request string = N> <Output file Name> <All JSON input and output = Y or All BasisPoint format = N>");
    }

    String readRequest(String fileName) {
        String strRequest = "";
        Scanner scanner = null;
        try {
            File file = new File(fileName); // creates a new file instance
            scanner = new Scanner(file);

        } catch (Exception ex) {
            String errmsg = "could not file input file " + fileName;
            error.setError(getClass().getName(), errmsg);
            System.exit(-1);
        }
        String sLine = "";
        while (scanner.hasNextLine()) {
            sLine = scanner.nextLine();
            strRequest += sLine + ";";
        }
        scanner.close();
        return strRequest;
    }

    private boolean readConfig() {
        boolean isOk = false;

        StringTokenizer stok;
        String paramName;
        String paramValue;
        Scanner scanner = null;
        File config = null;
        String configFile = rootDirectory + "config//config.bp";

        try {
            config = new File(configFile);
        } catch (Exception ex) {
            ex.printStackTrace();
            String errmsg = "config file, " + configFile + ", not found";
            error.setError(getClass().getName(), errmsg);
            return false;
        }
        try {
            scanner = new Scanner(config);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
            ex.printStackTrace();
            error.setError(getClass().getName(), "config file not found");
            return false;
        }
        while (scanner.hasNextLine()) {
            String sLine = scanner.nextLine();
            stok = new StringTokenizer(sLine, "=:");
            if (stok.hasMoreTokens()) {
                paramName = stok.nextToken();
                if (paramName.charAt(0) == '#')
                    continue;
                paramName = paramName.toUpperCase();
                paramValue = stok.nextToken();
                isOk = convertValidateAssignParameter(paramName, paramValue);

            }

        }
        scanner.close();
        return true;
    }

    public boolean validate(String paramName, int iVal, double dVal, String sVal, Character cVal, Dates dtVal) {
        boolean valid = false;
        int domain = paramMap.paramDomain;

        switch (domain) {
            case Domains.DOMAININT:
                valid = isIntInRange(paramName, iVal, paramMap.dMin, paramMap.dMax);
                break;
            case Domains.DOMAINDOUBLE:
                valid = isDoubleInRange(paramName, dVal, paramMap.dMin, paramMap.dMax);
                break;
            case Domains.DOMAINMONEY:
                valid = isDoubleInRange(paramName, dVal, paramMap.dMin, paramMap.dMax);
                break;
            case Domains.DOMAINDATE:
                valid = isValidDate(paramName, dtVal);
                break;
            case Domains.DOMAINSTRING:
                valid = choices.isInStringList(paramMap, sVal);
                break;
            case Domains.DOMAINYESNO:
                valid = isInYesNo(cVal);
                break;
            case Domains.DOMAINCHAR:
                valid = isInCharacter(cVal);
                break;
            case Domains.DOMAINPERIODICITY:
                valid = choices.checkPeriodicity(sVal);
                break;
            case Domains.DOMAINIGNORE:
                valid = true;
                break;
            default:
                error.setError(getClass().getName(), "Invalid parameter domain " + paramMap.paramDomain
                        + " detected for parameter " + paramMap.paramName);
                valid = false;
        }
        return valid;
    }

    public boolean validate(String paramName, ParamMap paramMap, int iVal, double dVal, String sVal, Character cVal,
            Dates dtVal) {
        boolean valid = false;
        switch (paramMap.paramDomain) {
            case Domains.DOMAININT:
                valid = isIntInRange(paramName, iVal, paramMap.dMin, paramMap.dMax);
                break;
            case Domains.DOMAINDOUBLE:
                valid = isDoubleInRange(paramName, dVal, paramMap.dMin, paramMap.dMax);
                break;
            case Domains.DOMAINMONEY:
                valid = isDoubleInRange(paramName, dVal, paramMap.dMin, paramMap.dMax);
                break;
            case Domains.DOMAINDATE:
                valid = isValidDate(paramName, dtVal);
                break;
            case Domains.DOMAINSTRING:
                valid = choices.isInStringList(paramMap, sVal);
                break;
            case Domains.DOMAINYESNO:
                valid = isInYesNo(cVal);
                break;
            case Domains.DOMAINCHAR:
                valid = isInCharacter(cVal);
                break;
            case Domains.DOMAINPERIODICITY:
                valid = choices.checkPeriodicity(sVal);
                break;
            case Domains.DOMAINIGNORE:
                valid = true;
                break;
            default:
                error.setError(getClass().getName(), "Invalid parameter domain " + paramMap.paramDomain
                        + " detected for parameter " + paramMap.paramName);
                valid = false;
        }
        return valid;
    }

    boolean assignToIntFromString(ParamMap paramMap, String sVal) {
        int freq = mapFrequency(sVal);
        switch (paramMap.paramName) {
            case "ACCSCHEDCOMPOUNDFREQ":
                bond.accrueSchedRule.compoundFreq = freq;
                bond.accrueSchedRule.schedType = BondConsts.ACCRUERULE;
            case "IDXSCHEDCOMPOUNDFREQ":
                bond.idxSchedRule.compoundFreq = freq;
                bond.idxSchedRule.schedType = BondConsts.INDEXRULE;
                break;
            case "INTSCHEDCOMPOUNDFREQ":
                bond.accrueSchedRule.compoundFreq = freq;
                bond.accrueSchedRule.schedType = BondConsts.INTERESTRULE;
                break;
            case "PAYLEGSCHEDCOMPOUNDFREQ":
                bond.paylegSchedRule.compoundFreq = freq;
                bond.paylegSchedRule.schedType = BondConsts.PAYLEGRULE;
                break;
            case "PAYLEGRESETSCHEDCOMPOUNDFREQ":
                bond.paylegResetSchedRule.compoundFreq = freq;
                bond.paylegResetSchedRule.schedType = BondConsts.paylegResetRULE;
                break;

            case "RECLEGSCHEDCOMPOUNDFREQ":
                bond.reclegSchedRule.compoundFreq = freq;
                bond.reclegSchedRule.schedType = BondConsts.RECLEGRULE;
                break;
            case "RECLEGRESETSCHEDCOMPOUNDFREQ":
                bond.reclegResetSchedRule.compoundFreq = freq;
                bond.reclegResetSchedRule.schedType = BondConsts.RESETRECLEGRULE;
                break;
            case "RESETSCHEDCOMPOUNDFREQ":
                bond.resetSchedRule.compoundFreq = freq;
                bond.resetSchedRule.schedType = BondConsts.RESETRULE;
                break;
            default:
                error.setError(getClass().getName(),
                        "Parameter " + paramMap.paramName + "not found in the list of integer parameter names");
                return false;
        }
        return true;
    }

    private boolean isInYesNo(char cVal) {
        return cVal == 'Y' || cVal == 'N';

    }

    private boolean assignToDoubleParam(double dVal) {
        if (paramMap.paramDomain != Domains.DOMAINDOUBLE) {
            error.setError(getClass().getName(),
                    "Parameter " + paramMap.paramName + " has been mis-assigned as a double value");
            return false;
        }
        dVal /= paramMap.scale;
        switch (paramMap.paramName) {
            case "AAYIELD":
                bond.aaYield = dVal;
                break;

            case "BOOKPRC":
                bond.bookPrc = dVal;
                break;
            case "BOOKPRINFACTOR":
                bond.bookPrinFactor = dVal;
                break;
            case "BOOKVALUE":
                bond.bookValue = dVal;
                break;
            case "CALLPRC":
                bond.callPrc[bond.noCallPrcs++] = dVal;
                break;
            case "CLAIMPCT":
                bond.claimPct = dVal;
                break;
            case "CONTRACTSIZE":
                bond.contractSize = dVal;
                break;
            case "CONVERSIONPRC":
                bond.conversionPrc = dVal;
                break;
            case "CONVERSIONRATIO":
                bond.conversionRatio = dVal;
                break;
            case "CPNFLOOR":
                bond.cpnFloor = dVal;
                break;
            case "CPNRTPAYLEG":
                bond.cpnRtPayleg = dVal;
                break;
            case "CPNRT":
                bond.cpnRt = dVal;
                break;
            case "CPNRATEQUOTE":
                bond.cpnRtQuote = dVal;
                break;
            case "CPNRTRECLEG":
                bond.cpnRtRecleg = dVal;
                break;
            case "CREDITSPRDRT":
                bond.creditSprdRt[bond.noCreditSprdRates++] = dVal;
                break;
            case "CUMACCREAMORT":
                bond.cumAccreAmort = dVal;
                break;
            case "CUMINTINCOME":
                bond.cumIntIncome = dVal;
                break;
            case "CUMLOCALCOSTADJUST":
                bond.cumLocalCostAdjust = dVal;
                break;
            // risk-free rates
            case "DISCRT":
                bond.discRt[bond.noDiscRate++] = dVal;
                break;

            case "DISCRTDN":
                bond.discRtDn[bond.noDiscRtDn++] = dVal;
                break;
            case "DISCRTUP":
                bond.discRtUp[bond.noDiscRtUp++] = dVal;
                break;
            case "DISCSTDDEV":
                bond.discStdDev[bond.noDiscStdDev++] = dVal;
                break;
            // credit spreads
            case "DISCSPRD":
                bond.discSprd[bond.noDiscSprd++] = dVal;
                break;

            case "DISCSPRDSTDDEV":
                bond.discFutSprd[bond.noDiscSprdStdDev++] = dVal;
                break;
            case "DISCSPRDDN":
                bond.discSprdDn[bond.noDiscSprdDn++] = dVal;
                break;
            case "DISCSPRDUP":
                bond.discSprdUp[bond.noDiscSprdUp++] = dVal;
                break;
            // future risk free rates
            case "DISCFUTRT":
                bond.discFutRt[bond.noDiscFutRate++] = dVal;
                break;

            case "DISCFUTSTDTDEV":
                bond.discFutStdDev[bond.noDiscFutStdDev++] = dVal;
                break;
            case "DISCFUTRTDN":
                bond.discFutRtDn[bond.noDiscFutRtDn++] = dVal;
                break;
            case "DISCFUTRTUP":
                bond.discFutRtUp[bond.noDiscFutRtUp++] = dVal;
                break;

            // future credit Spread
            case "DISCFUTSPRD":
                bond.discFutSprd[bond.noDiscFutSprd++] = dVal;
                break;

            case "DISCFUTSPRDSTDTDEV":
                bond.discFutSprdStdDev[bond.noDiscFutSprdStdDev++] = dVal;
                break;
            case "DISCFUTSPRDDN":
                bond.discFutRtDn[bond.noDiscFutSprdDn++] = dVal;
                break;
            case "DISCFUTSPRDUP":
                bond.discFutSprdUp[bond.noDiscFutSprdUp++] = dVal;
                break;
            case "DURBPSHIFT":
                bond.durBpShift = dVal;
                break;
            case "ENDPRC":
                bond.endPrc = dVal;
                break;
            case "ENDYLD":
                bond.endYld = dVal;
                break;
            case "ESTPREPAY":
                bond.estPrePay[bond.noEstPrePay++] = dVal;
                break;
            case "FACEAMT":
                bond.faceAmt = dVal;
                break;
            case "FISCALYTDAALOCAL":
                bond.fiscalYTDAALocal = dVal;
                break;
            case "FISCALYTDCOSTADJLOCAL":
                bond.fiscalYTDCostAdjLocal = dVal;
                break;
            case "FISCALYTDINCOMELOCAL":
                bond.fiscalYTDIncomeLocal = dVal;
                break;
            case "FIXINGRT":
                bond.fixingRt[bond.noFixingRate++] = dVal;
                break;
            case "FIXINGRTDN":
                bond.fixingRtDn[bond.noFixingRtDn++] = dVal;
                break;
            case "FIXINGRTUP":
                bond.fixingRtUp[bond.noFixingRtUp++] = dVal;
                break;
            case "FIXINGSTDDEV":
                bond.fixingStdDev[bond.noFixingStdDev++] = dVal;
                break;
            case "FIXINGAVGRT":
                bond.fixingAvgRt[bond.noFixingAvgRt++] = dVal;
                break;
            case "FIXINGMIN":
                bond.fixingMin[bond.noFixingMin++] = dVal;
                break;
            case "FIXINGMax":
                bond.fixingAvgRt[bond.noFixingMax++] = dVal;
                break;
            case "FUTUREPRC":
                bond.futurePrc = dVal;
                break;
            case "FXRTS":
                bond.fxRts[bond.noFxRts++] = dVal;
                break;
            case "IDXASSUME":
                bond.idxAssume[bond.noIdxAssumes++] = dVal;
                break;
            case "IDXCPIRATIO":
                bond.idxCPIRatio = dVal;
                break;
            case "IDXCPIATISSUE":
                bond.idxCPIAtIssue = dVal;
                break;
            case "IDXCPIATSETTLE":
                bond.idxCPIAtSettle = dVal;
                break;

            case "INCOMECONTROLMARGIN":
                bond.incomeControlMargin = dVal;
                break;
            case "INFLATIONRT":
                bond.inflationRt = dVal;
                break;
            case "ISSUEDFACE":
                bond.issuedFace = dVal;
                break;
            case "ISSUEPRC":
                bond.issuePrc = dVal;
                break;
            case "ISSUEYIELD":
                bond.issueYield = dVal;
                break;
            case "MATFLOOR":
                bond.matFloor = dVal;
                break;
            case "MKTPRCS":
                bond.mktPrcs[bond.noMktPrcs++] = dVal;
                break;
            case "MKTPRCFUTURE":
                bond.mktPrcFuture = dVal;
                break;
            case "MKTYLD":
                bond.mktYld = dVal;
                break;
            case "PASSFACTOR":
                bond.passFactor = dVal;
                break;
            case "PASSFACTOR1":
                bond.passFactor1 = dVal;
                break;
            case "PASSFACTOR2":
                bond.passFactor2 = dVal;
                break;
            case "PASSFACTORS":
                bond.passFactors[bond.noPassFactors++] = dVal;
                break;
            case "PASSPAYOFF":
                bond.passPayoff = dVal;
                break;
            case "PAYLEGRESETRT":
                bond.paylegResetRt[bond.noPaylegResets++] = dVal;
                break;
            case "PAYLEGMULTIPLIER":
                bond.paylegMultiplier[bond.noPaylegMultipliers++] = dVal;
                break;
            case "PAYLEGSPRD":
                bond.paylegSprd[bond.noPaylegSprds++] = dVal;
                break;
            case "PAYLEGCAP":
                bond.paylegCap[bond.noPaylegCaps++] = dVal;
                break;
            case "PAYLEGFLOOR":
                bond.paylegFloor[bond.noPaylegFloors++] = dVal;
                break;
            case "PAYLEGLIFECAP":
                bond.paylegLifeCap = dVal;
                break;
            case "PAYLEGLIFEFLOOR":
                bond.paylegLifeFloor = dVal;
                break;
            case "PCTTOGGLECASH":
                bond.pctToggleCash[bond.noTogglePcts++] = dVal;
                break;
            case "PIKRATE":
                bond.pikRt[bond.noPikRts++] = dVal;
                break;
            case "PREFUNDEDPRC":
                bond.preFundedPrc = dVal;
                break;
            case "PREPAYSPEED":
                bond.prepaySpeed = dVal;
                break;
            case "PURPRC":
                bond.purPrc = dVal;
                break;
            case "PURYLD":
                bond.purYld = dVal;
                break;
            case "PUTPRC":
                bond.putPrc[bond.noPutPrcs++] = dVal;
                break;
            case "RECLEGRESETRT":
                bond.reclegResetRt[bond.noReclegResets++] = dVal;
                break;
            case "RECLEGMULTIPLIER":
                bond.reclegMultiplier[bond.noReclegMultipliers++] = dVal;
                break;
            case "RECLEGSPRD":
                bond.reclegSprd[bond.noReclegSprds++] = dVal;
                break;
            case "RECLEGCAP":
                bond.paylegCap[bond.noReclegCaps++] = dVal;
                break;
            case "RECLEGFLOOR":
                bond.reclegFloor[bond.noReclegFloors++] = dVal;
                break;
            case "RECLEGLIFECAP":
                bond.reclegLifeCap = dVal;
                break;
            case "RECLEGLIFEFLOOR":
                bond.reclegLifeFloor = dVal;
                break;
            case "RECOVERYRATE":
                bond.recoveryRt = dVal;
                break;
            case "REDEEMAMT":
                bond.redeemAmt[bond.noRedeemAmts++] = dVal;
                break;
            case "REDEEMFACTOR":
                bond.redeemFactor[bond.noRedeemFactors++] = dVal;
                break;
            case "REDEEMPAYOFF":
                bond.redeemPayoff = dVal;
                break;
            case "REDEEMPCT":
                bond.redeemPct[bond.noRedeemPcts++] = dVal;
                break;
            case "REDEEMPRIN":
                bond.redeemPrin = dVal;
                break;
            case "REINMATLEN":
                bond.reinMatlen[bond.noReinMatlens++] = dVal;
                break;
            case "REINRT":
                bond.reinRt[bond.noReinRts++] = dVal;
                break;
            case "RESETCAP":
                bond.resetCaps[bond.noResetCaps++] = dVal;
                break;
            case "RESETFLOOR":
                bond.resetFloors[bond.noResetFloors++] = dVal;
                break;
            case "RESETRT":
                bond.resetRts[bond.noResetRts++] = dVal;
                break;
            case "RESETLIFECAP":
                bond.resetLifeCap = dVal;
                break;
            case "RESETLIFEFLOOR":
                bond.resetLifeFloor = dVal;
                break;
            case "RESETMULTIPLIER":
                bond.resetMultiplier[bond.noResetMultipliers++] = dVal;
                break;
            case "RESETSPRD":
                bond.resetSprd[bond.noResetSprds++] = dVal;
                break;
            case "SERVICERATE":
                bond.serviceRt = dVal;
                break;
            case "SETTLECASH":
                bond.settleCash = dVal;
                break;
            case "SETTLEPRC":
                bond.settlePrc = dVal;
                // strInput = strInput + "; settleprc was set from user string to " + dVal;
                break;
            case "SETTLEYLD":
                bond.settleYld = dVal;
                break;
            case "SINKAMTS":
                bond.sinkAmts[bond.noSinkAmts++] = dVal;
                break;
            case "SINKFACTOR":
                bond.sinkFactor = dVal;
                break;
            case "SINKPCTS":
                bond.sinkPcts[bond.noSinkPcts++] = dVal;
                break;
            case "SINKPRCS":
                bond.sinkPrcs[bond.noSinkPrcs++] = dVal;
                break;
            case "MKTPRCSTART":
                bond.mktPrcStart = dVal;
                break;
            case "STARTYIELD":
                bond.startYield = dVal;
                break;
            case "SWAPPCTGAIN":
                bond.swapPctGain = dVal;
                break;
            case "TAXRATEFEDCAPGAINLONG":
                bond.taxRtFedCapGainLong = dVal;
                break;
            case "TAXRATEFEDCAPGAINSHORT":
                bond.taxRtFedCapGainShort = dVal;
                break;
            case "TAXRATEFEDINCOME":
                bond.taxRtFedIncome = dVal;
                break;
            case "TAXRATESTATECAPGAINLONG":
                bond.taxRtStateCapGainLong = dVal;
                break;
            case "TAXRATESTATECAPGAINSHORT":
                bond.taxRtStateCapGainShort = dVal;
                break;
            case "TAXRATESTATEINCOME":
                bond.taxRtStateIncome = dVal;
                break;
            case "USERCASHFLOW":
                bond.userCashflow[bond.noUserCash++] = dVal;
                break;
            default:
                error.setError(getClass().getName(), "Parameter " + paramMap.paramName + " is invalid");
                return false;
        }
        return true;
    }

    private boolean assignToPriceParam(ParamMap paramMap, double dVal) {
        if (paramMap.paramDomain != Domains.DOMAINPRICE) {
            error.setError(getClass().getName(), "Parameter " + paramMap.paramName + " is invalid");
            return false;
        }
        if ("SETTLEPRC".equals(paramMap.paramName)) {
            bond.settlePrc = dVal / paramMap.scale;
        } else {
            error.setError(getClass().getName(), "Parameter " + paramMap.paramName + " is invalid");
            return false;
        }
        return true;
    }

    private boolean isInCharacter(Character cval) {
        return true;
    }

    public boolean assignToParameter(ParamMap paramMap, int iVal, double dVal, Dates dtVal, String sVal,
            Character cVal) {
        boolean isOk = false;
        switch (paramMap.paramDomain) {
            case Domains.DOMAININT:
                isOk = assignToIntParam(paramMap.paramName, iVal);
                break;
            case Domains.DOMAINDOUBLE:
                isOk = assignToDoubleParam(dVal);
                break;
            case Domains.DOMAINMONEY:
                isOk = assignMoneyParam(paramMap, dVal);
                break;
            case Domains.DOMAINPRICE:
                isOk = assignToPriceParam(paramMap, dVal);
                break;
            case Domains.DOMAINDATE:
                isOk = assignToDateParam(paramMap, dtVal);
                break;
            case Domains.DOMAINSTRING:
                isOk = assignToStringParam(paramMap, sVal);
                break;
            case Domains.DOMAINCHAR:
                isOk = assignToCharParm(paramMap, cVal);
                break;
            case Domains.DOMAINYESNO:
                isOk = assignToCharParm(paramMap, cVal);
                break;
            case Domains.DOMAINPERIODICITY:
                isOk = choices.checkPeriodicity(sVal);
                if (error.hasErrorYn == 'Y')
                    return false;
                isOk = assignToStringParam(paramMap, sVal);
                break;
            case Domains.DOMAINIGNORE:
                isOk = true;
                break;
            default:
                error.setError(getClass().getName(), "Invalid parameter domain " + paramMap.paramDomain + " detected");
                return false;
        }
        return isOk;
    }

    private boolean assignToIntParam(String paramName, int iVal) {
        switch (paramName) {
            case "ACCRUEDINTSIZE":
                bond.accruedIntSize = iVal;
                break;
            case "ACCSCHEDDOM":
                bond.accrueSchedRule.dayOfMonth = iVal;
                break;
            case "ACCSCHEDREPEAT":
                bond.accrueSchedRule.repeatFactor = iVal;
                break;
            case "ACCSCHEDWOM":
                bond.accrueSchedRule.weekOfMonth = iVal;
                break;
            case "ALLOWEDPASSAGE":
                bond.allowedPassAge = iVal;
                break;
            case "CALCREQUEST":
                bond.calcRequests[bond.noRequests++] = iVal;
                bond.noCalcRequests = bond.noRequests;
                break;
            case "CALLNOTIFYDAYS":
                bond.callNotifyDays = iVal;
                break;
            case "CREDITSPRDMATLEN":
                bond.creditSprdMatLen[bond.noCreditSprdMatlens++] = iVal;
                break;
            case "DAYSCAPGAINSHORT":
                bond.daysCapGainShort = iVal;
                break;
            case "DELAYDAYSACT":
                bond.delaydaysAct = iVal;
                break;
            case "DELAYDAYSSTATED":
                bond.delaydaysStated = iVal;
                break;
            case "DISCTENOR":
                bond.discTenor[bond.noDiscTenor++] = iVal;
                break;
            case "DISCSPRDTENOR":
                bond.discSprdTenor[bond.noDiscSprdTenor++] = iVal;
                break;
            case "DISCFUTTENOR":
                bond.discFutTenor[bond.noDiscFutTenor++] = iVal;
                break;
            case "DISCFUTSPRDTENOR":
                bond.discFutSprdTenor[bond.noDiscFutSprdTenor++] = iVal;
                break;
            case "DUMPCNTMAX":
                bond.dumpCntMax = iVal;
                break;
            case "DURATIONWT":
                bond.durationWt = iVal;
                break;
            case "EXDIVOFFSETDAYS":
                bond.exDivOffsetDays = iVal;
                break;
            case "FIXINGTENOR":
                bond.fixingTenor[bond.noFixingTenor++] = iVal;
                break;
            case "FLOATERTERM":
                bond.floaterTerm = iVal;
                break;
            case "HORIZONPERIODS":
                bond.horizonPeriods = iVal;
                break;
            case "IDXDELAYMONTHS":
                bond.idxDelayMonths = iVal;
                break;
            case "IDXDELAYWEEKS":
                bond.idxDelayWeeks = iVal;
                break;
            case "IDXDELAYDAYS":
                bond.idxDelayDays = iVal;
                break;
            case "IDXSCHEDDOM":
                bond.idxSchedDOM = iVal;
                bond.idxSchedRule.dayOfMonth = iVal;
                break;
            case "IDXSCHEDREPEAT":
                bond.idxSchedRepeat = iVal;
                bond.idxSchedRule.repeatFactor = iVal;
                break;
            case "IDXSCHEDWOM":
                bond.idxSchedWOM = iVal;
                bond.idxSchedRule.weekOfMonth = iVal;
                break;
            case "IDXRATIODELAYMON":
                bond.idxRatioSchedDOM = iVal;
                break;
            case "IDXRATIOSCHEDDOM":
                bond.idxRatioSchedDOM = iVal;
                bond.idxRatioSchedRule.dayOfMonth = iVal;
                break;
            case "IDXRATIOSCHEDREPEAT":
                bond.idxRatioSchedRepeat = iVal;
                bond.idxRatioSchedRule.repeatFactor = iVal;
                break;
            case "IDXRATIOSCHEDWOM":
                bond.idxRatioSchedWOM = iVal;
                bond.idxRatioSchedRule.weekOfMonth = iVal;
                break;
            case "INTRATIOSCHEDDOM":
                bond.accrueSchedRule.dayOfMonth = iVal;
                break;
            case "INTSCHEDREPEAT":
                bond.intSchedRepeat = iVal;
                bond.accrueSchedRule.repeatFactor = iVal;
                break;
            case "INTSCHEDWOM":
                bond.intSchedWOM = iVal;
                bond.accrueSchedRule.weekOfMonth = iVal;
                break;
            case "PASSWALA":
                bond.passWala = iVal;
                break;
            case "PASSWARM":
                bond.passWarm = iVal;
                break;
            case "PAYLEGSCHEDDOM":
                bond.paylegSchedDOM = iVal;
                bond.paylegSchedRule.dayOfMonth = iVal;
                break;
            case "PAYLEGSCHEDREPEAT":
                bond.paylegSchedRepeat = iVal;
                bond.paylegSchedRule.repeatFactor = iVal;
                break;
            case "PURWALA":
                bond.purWala = iVal;
                break;
            case "PURWARM":
                bond.purWarm = iVal;
                break;
            case "PAYLEGSCHEDWOM":
                bond.paylegSchedWOM = iVal;
                bond.paylegSchedRule.weekOfMonth = iVal;
                break;
            case "PUTNOTIFYDAYS":
                bond.putNotifyDays = iVal;
                break;
            case "RECLEGSCHEDDOM":
                bond.reclegSchedDOM = iVal;
                bond.reclegSchedRule.dayOfMonth = iVal;
                break;
            case "RECLEGSCHEDREPEAT":
                bond.reclegSchedCompoundFreq = CalUnits.MONTHSPERYEAR / iVal;
                bond.reclegSchedRule.repeatFactor = iVal;
                bond.reclegSchedRepeat = iVal;
                break;
            case "RECLEGSCHEDWOM":
                bond.reclegSchedWOM = iVal;
                bond.reclegSchedRule.weekOfMonth = iVal;
                break;
            case "PAYLEGRESETSCHEDDOM":
                bond.paylegResetSchedDOM = iVal;
                bond.paylegResetSchedRule.dayOfMonth = iVal;
                break;
            case "PAYLEGRESETSCHEDREPEAT":
                bond.paylegResetSchedRepeat = iVal;
                bond.paylegResetSchedRule.repeatFactor = iVal;
                break;
            case "RECLEGRESETSCHEDDOM":
                bond.reclegResetSchedDOM = iVal;
                bond.reclegResetSchedRule.dayOfMonth = iVal;
                break;
            case "RESETSCHEDDOM":
                bond.resetSchedDOM = iVal;
                bond.resetSchedRule.dayOfMonth = iVal;
                break;
            case "RESETSCHEDREPEAT":
                bond.resetSchedRepeat = iVal;
                bond.resetSchedRule.repeatFactor = iVal;
                break;
            case "RESETSCHEDWOM":
                bond.resetSchedWOM = iVal;
                bond.resetSchedRule.weekOfMonth = iVal;
                break;
            case "RETURNWT":
                bond.returnWt = iVal;
                break;
            case "SFIGACCRUAL":
                bond.sfigAccrual = iVal;
                break;
            case "SFIGACCRUED":
                bond.sfigAccrued = iVal;
                break;
            case "SFIGACCRUEDSIZE":
                bond.sfigAccruedSize = iVal;
                break;
            case "SFIGACCRUEDPRIN":
                bond.sfigAccruedPrin = iVal;
                break;
            case "SFIGACCRUEDDAYCOUNT":
                bond.sfigAccruedDaycount = iVal;
                break;
            case "SFIFCASHFLOW":
                bond.sfigCashflow = iVal;
                break;
            case "SFIGCURRENCY":
                bond.sfigCurrency = iVal;
                break;
            case "SFIGCPI":
                bond.sfigCPI = iVal;
                break;
            case "SFIGVALUE":
                bond.sfigIdxValue = iVal;
                break;
            case "SFIGIDXRATIO":
                bond.sfigIdxRatio = iVal;
                break;
            case "SFIGPRC":
                bond.sfigPrc = iVal;
                break;
            case "SFIGSTATISTIC":
                bond.sfigStatistic = iVal;
                break;
            case "SFIGYIELD":
                bond.sfigYield = iVal;
                break;
            case "WEEKEND1":
                bond.weekend1 = iVal;
                bond.accrueSchedRule.weekend1 = iVal;
                break;
            case "WEEKEND2":
                bond.weekend2 = iVal;
                bond.accrueSchedRule.weekend2 = iVal;
                break;
            default:
                error.setError(getClass().getName(),
                        "invalid integer value " + iVal + " detected for parameter " + paramName);
                return false;

        }
        return true;
    }

    private boolean assignToCharParm(ParamMap paramMap, char cVal) {
        if (paramMap.paramDomain != Domains.DOMAINCHAR && paramMap.paramDomain != Domains.DOMAINYESNO) {
            error.setError(getClass().getName(),
                    "Parameter " + paramMap.paramName + "has been mis-assigned as a character value");
            return false;
        }
        switch (paramMap.paramName) {
            case "ACCREUDINTRNDYN":
                bond.accruedIntRndYn = cVal;
                break;
            case "ACCREUDINTSIZERNDYN":
                bond.accruedIntSizeRndYn = cVal;
                break;
            case "ACCREUDINTPRIBRNDYN":
                bond.accruedIntPrinRndYn = cVal;
                break;
            case "ADJUSTMATURITYYN":
                bond.adjustMaturityYn = cVal;
                break;
            case "ALLOWBADPARAMYN":
                bond.allowBadParamYn = cVal;
                break;
            case "ALLOWNEGCPNYN":
                bond.allowNegCpnYn = cVal;
                break;
            case "ALLOWNEGPRCYLDYN":
                bond.allowNegPrcYldYn = cVal;
                break;
            case "ALLOWNEGRTYN":
                bond.allowNegRtYn = cVal;
                break;
            case "AUSACCRUEDYN":
                bond.ausAccruedYn = cVal;
                break;
            case "CALCAFTERMATDTYN":
                bond.calcAfterMatDtYn = cVal;
                break;
            case "CALCCPNDTSYN":
                bond.calcCpnDtsYn = cVal;
                break;
            case "CALCFACTORSYN":
                bond.calcFactorsYn = cVal;
                break;
            case "CALCFLOATRATESYN":
                bond.calcFloatRtsYn = cVal;
                break;
            case "CALCONLYPORTSTATSYN":
                bond.calcOnlyPortStatsYn = cVal;
                break;
            case "CALCONSETTLEYN":
                bond.calcOnSettleYn = cVal;
                break;
            case "CALCRESETDTSYN":
                bond.calcResetDtsYn = cVal;
                break;
            case "CALCTOTALSYN":
                bond.calcTotalsYn = cVal;
                break;
            case "CALLABLEYN":
                bond.callableYn = cVal;
                break;
            case "CANADADACCRUEDYN":
                bond.canadaAccruedYn = cVal;
                break;
            case "CLEANPRCYN":
                bond.cleanPrcYn = cVal;
                break;
            case "CLEANYIELDYN":
                bond.cleanYieldYn = cVal;
                break;
            case "CONVERTIBLEYN":
                bond.convertibleYn = cVal;
                break;
            case "CONVERTTOLOCALYN":
                bond.convertToLocalYn = cVal;
                break;
            case "DISCFIXEDLEGYN":
                bond.discFixedLegYn = cVal;
                break;
            case "DISCRTPARYN":
                bond.discRtParYn = cVal;
                break;
            case "DISCSPRDPARYN":
                bond.discSprdParYn = cVal;
                break;
            case "DISCFUTRTPARYN":
                bond.discFutRtParYn = cVal;
                break;
            case "DISCFUTSPRDPARYN":
                bond.discFutSprdParYn = cVal;
                break;
            case "DUMPONCMDYN":
                bond.dumpOnCmdYn = cVal;
                break;
            case "DUMPONERRORYN":
                bond.dumpOnErrorYn = cVal;
                break;
            case "FILEOUTYN":
                bond.fileOutYn = cVal;
                break;
            case "FEDTAXEXEMPTYN":
                bond.fedTaxExemptYn = cVal;
                break;
            case "FIXINGPARYN":
                bond.fixingParYn = cVal;
                break;
            case "FORCEYLDYN":
                bond.forceYldYn = cVal;
                break;
            case "IDXADJMATYN":
                bond.idxAdjMatYn = cVal;
                break;
            case "INDEXVALUERNDYM":
                bond.idxValueRoundYn = cVal;
                break;
            case "IDXRATIORNDYN":
                bond.idxRatioRndYn = cVal;
                break;
            case "IDXSCHEDEOMRULEYN":
                bond.idxSchedEOMRuleYn = cVal;
                break;
            case "IDXRATIOSCHEDUSENEXTLASTYN":
                bond.idxSchedUseNextLastYn = cVal;
                break;
            case "IDXRATIOSCHEDEOMRULEYN":
                bond.idxRatioSchedEOMRuleYn = cVal;
                break;
            case "IDXSCHEDUSENEXTLASTYN":
                bond.idxSchedUseNextLastYn = cVal;
                break;
            case "IDXVALUEROUNDYN":
                bond.idxValueRoundYn = cVal;
                break;
            case "INCLDENDDTBOOKVALYN":
                bond.incldEndDtBookValYn = cVal;
                break;
            case "INCLDENDDTINCOMEYN":
                bond.incldEndDtIncomeYn = cVal;
                break;
            case "INCLDSETTDTCASHYN":
                bond.incldSettDtCashYn = cVal;
                break;
            case "INCLDSETTDTINTRECYN":
                bond.incldSettDtIntRecYn = cVal;
                break;
            case "ISCASHYN":
                bond.isCashYn = cVal;
                break;
            case "ISATMATURITYYN":
                bond.isAtmaturityYn = cVal;
                break;
            case "ISEXDIVIDEND":
                bond.isExdividendYn = cVal;
                break;
            case "ISFIXEDRATEYN":
                bond.isFixedrateYn = cVal;
                break;
            case "ISFLOATERYN":
                bond.isFloaterYn = cVal;
                break;
            case "ISFORCEYIELDYN":
                bond.isForceYieldYn = cVal;
                break;
            case "ISINFLATIONPROTECTEDYN":
                bond.isInflationProtectedYn = cVal;
                break;
            case "ISMORTGAGEYN":
                bond.isMortgageYn = cVal;
                break;
            case "ISPUTABLEYN":
                bond.isPutableYn = cVal;
                break;
            case "ISSINKABLEYN":
                bond.isSinkableYn = cVal;
                break;
            case "ISSTEPPEDYN":
                bond.isSteppedYn = cVal;
                break;
            case "ISZEROCOUPONYN":
                bond.isSinkableYn = cVal;
                break;
            case "INTSCHEDEOMRULEYN":
                bond.intSchedEOMYn = cVal;
                bond.accrueSchedRule.useEndOfMonthRuleYn = cVal;
                break;
            case "INTSCHEDUSENEXTLASTYN":
                bond.intSchedUseNextLastYn = cVal;
                break;
            case "LEVELPAYFROMPASSDTYN":
                bond.levelPayFromPassDtYn = cVal;
                break;
            case "LONGODD1STYN":
                bond.longOdd1StYn = cVal;
                break;
            case "LONGODDLASTYN":
                bond.longOddLastYn = cVal;
                break;
            case "ODDNESSPRCYN":
                bond.oddnessPrcYn = cVal;
                break;
            case "PAYLEGFIXEDYN":
                bond.paylegFixedYn = cVal;
                break;
            case "PAYLEGSCHEDEOMRULEYN":
                bond.paylegSchedEOMRuleYn = cVal;
                break;
            case "PAYLEGSCHEDUSENEXTLASTYN":
                bond.paylegSchedUseNextLastYn = cVal;
                break;
            case "PIKABLEYN":
                bond.pikableYn = cVal;
                break;
            case "PRCADJUSTEDYN":
                bond.prcAdjustedYn = cVal;
                break;
            case "AFTERTAXCALCSYN":
                bond.afterTaxCalcsYn = cVal;
                break;
            case "PUTABLEYN":
                bond.putableYn = cVal;
                break;
            case "RECLEGFIXEDYN":
                bond.reclegFixedYn = cVal;
                break;
            case "RECLEGSCHEDEOMRULEYN":
                bond.reclegSchedEOMRuleYn = cVal;
                break;
            case "RECLEGSCHEDUSENEXTLASTYN":
                bond.reclegSchedUseNextLastYn = cVal;
                break;
            case "paylegResetSCHEDEOMRULEYN":
                bond.paylegResetSchedEOMRuleYn = cVal;
                break;
            case "paylegResetSCHEDUSENEXTLASTYN":
                bond.paylegResetSchedUseNextLastYn = cVal;
                break;
            case "RESETRECLEGSCHEDEOMRULEYN":
                bond.reclegResetSchedEOMRuleYn = cVal;
                break;
            case "RESETRECLEGSCHEDUSENEreclegResetSchedEOMRuleYnXTLASTYN":
                bond.reclegResetSchedUseNextLastYn = cVal;
                break;
            case "RESETSCHEDEOMRULEYN":
                bond.resetSchedEOMRuleYn = cVal;
                break;
            case "RESETSCHEDUSENEXTLASTYN":
                bond.resetSchedUseNextLastYn = cVal;
                break;
            case "SHOWINDIVCALCYN":
                bond.showIndivCalcYn = cVal;
                break;
            case "SHOWRECAP":
                bond.showRecapYn = cVal;
                break;
            case "SINKABLEYN":
                bond.sinkableYn = cVal;
                break;
            case "STATETAXEXEMPTYN":
                bond.stateTaxExemptYn = cVal;
                break;
            case "TAXADJALLOWEDYN":
                bond.taxAdjAllowedYn = cVal;
                break;
            case "USECREDITSPRDYN":
                bond.useCreditSprdYn = cVal;
                break;
            case "USEDEFAULTSFIGYN":
                bond.useDefaultSfigYn = cVal;
                break;
            case "USELOCALCURRENCYYN":
                bond.useLocalCurrencyYn = cVal;
                break;
            case "USEFORWARDCURVE":
                bond.useForwardCurveYn = cVal;
                break;
            case "USEFIXINGCURVEYN":
                bond.useFixingCurveYn = cVal;
                break;
            case "VALIDATEYN":
                bond.validateYn = cVal;
                break;
            case "USECURRENTREDEMPTIONYN":
                bond.useCurrentRedemptionYn = cVal;
                break;
            case "WARNINGSOFFYN":
                bond.warningsOffYn = cVal;
                break;
            case "WHENISSUEYN":
                bond.whenIssueYn = cVal;
                break;
            default:
                error.setError(getClass().getName(),
                        "Parameter " + paramMap.paramName + "could not be assigned a yes or no value");
                return false;
        }
        return true;
    }

    private boolean assignMoneyParam(ParamMap paramMap, double dVal) {
        if (paramMap.paramDomain != Domains.DOMAINMONEY) {
            error.setError(getClass().getName(), "Parameter " + paramMap.paramName + " has been mis-assigned to money");
            return false;
        }
        if ("FACEAMT".equals(paramMap.paramName)) {
            bond.faceAmt = dVal / paramMap.scale;
        } else {
            error.setError(getClass().getName(), "Parameter " + paramMap.paramName + " is invalid");
            return false;
        }
        return true;

    }

    private boolean assignToDateParam(ParamMap paramMap, Dates dtVal) {
        if (paramMap.paramDomain != Domains.DOMAINDATE) {
            error.setError(getClass().getName(),
                    "Parameter " + paramMap.paramName + " has been mis-assigned as a date");
            return false;
        }
        //

        if (!isValidDate(paramMap.paramName, dtVal)) {
            return false;
        }

        switch (paramMap.paramName) {
            case "ACCSCHEDNEXTCPNDT":
                bond.accrueSchedRule.dtNextUserCpn = dtVal;
                break;
            case "ACCSCHEDLASTCPNDT":
                bond.accrueSchedRule.dtLastUserCpn = dtVal;
                break;
            case "ACCRUEDDTOCCUR":
                bond.accrueSchedRule.dtOccur = dtVal;
                break;
            case "DT1STPAY":
                bond.dt1stPay = dtVal;
                break;
            case "DTLASTPAY":
                bond.dtLastPay = dtVal;
                break;
            case "DTCALL":
                bond.dtCall[bond.noCallDts] = new Dates(error);
                bond.dtCall[bond.noCallDts++] = dtVal;
                break;
            case "DTCALLNOTIFIED":
                bond.dtCallNotified = dtVal;
                break;
            case "DTCASHEND":
                bond.dtCashEnd = dtVal;
                break;
            case "DTCASHSTART":
                bond.dtCashStart = dtVal;
                break;
            case "DTACCRUALS":
                bond.accrueSchedRule.dtUserPays[bond.noAccrueDts] = new Dates();
                bond.accrueSchedRule.dtUserPays[bond.noAccrueDts++] = dtVal;
                break;
            case "DTCONVERT":
                bond.dtConvert = dtVal;
                break;
            case "DTUSERPAYS": // List of user enteres coupon payment dates
                bond.intSchedRule.dtUserPays[bond.noUserPayDts] = new Dates();
                bond.intSchedRule.dtUserPays[bond.noUserPayDts++] = dtVal;
                break;
            case "DTDATED":
                bond.accrueSchedRule.dtDated = dtVal;
                bond.dtDated = dtVal;
                break;
            case "DTEND":
                bond.dtEnd = dtVal;
                break;
            case "DTDISCCURVE":
                bond.dtDiscCurve = dtVal;
                break;
            case "DTDISCFUTCURVE":
                bond.dtDiscFutCurve = dtVal;
                break;
            case "DTFUTURE":
                bond.dtFuture = dtVal;
                break;
            case "DTESTPREPAY":
                bond.dtEstPrepay[bond.noDtEstPrePay++] = dtVal;
                break;
            case "DTEXDIVIDEND":
                bond.dtExDividend = dtVal;
                break;
            case "DTFACTORS":
                bond.dtFactors[bond.noFactorDts] = new Dates(error);
                bond.dtFactors[bond.noFactorDts++] = dtVal;
                break;
            case "DTFXRTS":
                bond.dtFxRts[bond.noFxDts] = new Dates(error);
                bond.dtFxRts[bond.noFxDts++] = dtVal;
                break;
            case "DTFIXINGCURVE":
                bond.dtFixingCurve = dtVal;
                break;
            case "DTIDXASSUME":
                bond.dtIdxAssume[bond.noIdxAssumeDts] = new Dates(error);
                bond.dtIdxAssume[bond.noIdxAssumeDts++] = dtVal;
                break;
            case "DTLASTONLYCOUPON":
                bond.dtLastOnlyCoupon = dtVal;
                break;
            case "DTLASTTAXPAYMENT":
                bond.dtLastTaxPayment = dtVal;
                break;
            case "DTOCCUR":
                bond.accrueSchedRule.dtOccur = dtVal;
                break;
            case "DTIDXRATIO":
                bond.dtIdxRatio[bond.noIdxRatioDts] = new Dates(error);
                bond.dtIdxRatio[bond.noIdxRatioDts++] = dtVal;
                break;
            case "DTIDXCPI":
                bond.dtIdxCPI[bond.noIdxCPIDts] = new Dates(error);
                bond.dtIdxCPI[bond.noIdxCPIDts++] = dtVal;
                break;
            case "DTRESET":
                bond.dtResets[bond.noResetDts] = new Dates(error);
                bond.dtResets[bond.noResetDts++] = dtVal;
                break;
            case "DTLASTACCOUNTING":
                bond.dtLastAccounting = dtVal;
                break;
            case "DTLASTPAYMENT": // for interst payments on mortgages
                bond.dtLastPayment = dtVal;
                bond.dtLastPayment = dtVal;
                break;
            case "DTMATURITY":
                bond.dtMaturity = dtVal;
                bond.accrueSchedRule.dtMaturity = dtVal;
                break;
            case "DTMKTPRCS":
                bond.dtMktPrcs[bond.noMktPrcDts] = new Dates(error);
                bond.dtMktPrcs[bond.noMktPrcDts++] = dtVal;
                break;
            case "DTPASSFACTOR":
                bond.dtPassFactor = dtVal;
                break;
            case "DTPASSFACTOR1":
                bond.dtPassFactor1 = dtVal;
                break;
            case "DTPASSFACTOR2":
                bond.dtPassFactor2 = dtVal;
                break;
            case "DTPASSFACTORS":
                bond.dtPassFactors[bond.noPassFactorDts] = new Dates(error);
                bond.dtPassFactors[bond.noPassFactorDts++] = dtVal;
                break;
            case "DTPAYLEGRESET":
                bond.dtPaylegReset[bond.noPaylegResetDts] = new Dates(error);
                bond.dtPaylegReset[bond.noPaylegResetDts++] = dtVal;
                break;
            case "DTRECLEGRESET":
                bond.dtReclegReset[bond.noReclegResetDts] = new Dates(error);
                bond.dtReclegReset[bond.noReclegResetDts++] = dtVal;
                break;
            case "DTPREFUNDED":
                bond.dtPreFunded = dtVal;
                break;
            case "DTLASTUSERCPN":
                bond.dtLastUserCpn = dtVal;
                bond.accrueSchedRule.dtLastUserCpn = dtVal;
                break;
            case "DTNEXTUSERCPN":
                bond.accrueSchedRule.dtNextUserCpn = dtVal;
                break;
            case "DTPUR":
                bond.dtPur = dtVal;
                break;
            case "DTPUT":
                bond.dtPut[bond.noPutDts] = new Dates(error);
                bond.dtPut[bond.noPutDts++] = dtVal;
                break;
            case "DTRESETMULTIPLIERS":
                bond.dtResetMultipliers[bond.noResetMultiplierDts++] = dtVal;
                break;
            case "DTPAYLEGMULTIPLIER":
                bond.dtPaylegMultipliers[bond.noPaylegMultiplierDts++] = dtVal;
                break;
            case "DTRECLEGMULTIPLIER":
                bond.dtReclegMultipliers[bond.noReclegMultiplierDts++] = dtVal;
                break;
            case "DTRESETSPRD":
                bond.dtResetSprds[bond.noResetSprds] = new Dates(error);
                bond.dtResetSprds[bond.noResetSprds++] = dtVal;
                break;
            case "DTRESETCAP":
                bond.dtResetCaps[bond.noResetCaps] = new Dates(error);
                bond.dtResetCaps[bond.noResetCaps++] = dtVal;
                break;
            case "DTRESETFLOOR":
                bond.dtResetFloors[bond.noResetFloors] = new Dates(error);
                bond.dtResetFloors[bond.noResetFloors++] = dtVal;
                break;
            case "DTSETTLE":
                bond.dtSettle = dtVal;
                break;
            case "DTSINKS":
                bond.dtSinks[bond.noSinkDts] = new Dates(error);
                bond.dtSinks[bond.noSinkDts++] = dtVal;
                break;
            case "DTSTART":
                bond.dtStart = dtVal;
                break;
            case "DTTAXPAY":
                bond.taxSchedRule.dtOccur = dtVal;
                break;
            case "IDXSCHEDNEXTCPNDT":
                bond.idxSchedRule.dtNextUserCpn = dtVal;
                break;
            case "IDXSCHEDOCCURDT":
                bond.idxSchedRule.dtOccur = dtVal;
                break;
            case "IDXSCHEDPREVCPNDT":
                bond.idxSchedRule.dtLastUserCpn = dtVal;
                break;
            case "INTSCHEDNEXTCPNDT":
                bond.accrueSchedRule.dtNextUserCpn = dtVal;
                break;
            case "INTSCHEDOCCURDT":
                bond.accrueSchedRule.dtOccur = dtVal;
                bond.intSchedRule.dtOccur = dtVal;
                break;
            case "DTPAYLEGSCHEDNEXTCPN":
                bond.paylegSchedRule.dtNextUserCpn = dtVal;
                break;
            case "DTPAYLEGSCHEDOCCUR":
                bond.paylegSchedRule.dtOccur = dtVal;
                break;
            case "DTPAYLEGSCHEDPREV":
                bond.paylegSchedRule.dtLastUserCpn = dtVal;
                break;
            case "DTPAYLEG1STPAY":
                bond.paylegResetSchedRule.dt1stPay = dtVal;
                break;
            case "DTRECLEG1STPAY":
                bond.reclegResetSchedRule.dt1stPay = dtVal;
                break;
            case "DTRECLEGSCHEDNEXTCPN":
                bond.reclegSchedRule.dtNextUserCpn = dtVal;
                break;
            case "DTRECLEGSCHEDOCCUR":
                bond.reclegSchedRule.dtOccur = dtVal;
                break;
            case "DTRECLEGSCHEDPREVCPN":
                bond.reclegSchedRule.dtLastUserCpn = dtVal;
                break;
            case "DTPAYLEGRESETSCHEDNEXTCPN":
                bond.paylegResetSchedRule.dtNextUserCpn = dtVal;
                break;
            case "DTPAYLEGRESETSCHEDOCCURDT":
                bond.paylegResetSchedRule.dtOccur = dtVal;
                break;
            case "DTPAYLEGRESETSCHEDPREVCPNDT":
                bond.paylegResetSchedRule.dtLastUserCpn = dtVal;
                break;
            case "DTRECLEGRESETSCHEDNEXTCPNDT":
                bond.reclegResetSchedRule.dtNextUserCpn = dtVal;
                break;
            case "DTRECLEGRESETSCHEDOCCUR":
                bond.reclegResetSchedRule.dtOccur = dtVal;
                break;
            case "DTRECLEGRESETSCHEDPREVCPNDT":
                bond.reclegResetSchedRule.dtNextUserCpn = dtVal;
                break;
            case "RESETSCHEDNEXTCPNDT":
                bond.resetSchedRule.dtNextUserCpn = dtVal;
                break;
            case "RESETSCHEDOCCURDT":
                bond.resetSchedRule.dtOccur = dtVal;
                break;
            case "RESETSCHEDPREVCPNDT":
                bond.resetSchedRule.dtLastUserCpn = dtVal;
                break;
            case "USERCASHDT":
                bond.userCashDt[bond.noUserCashDts] = new Dates(error);
                bond.userCashDt[bond.noUserCashDts++] = dtVal;
                break;
            default:
                error.setError(getClass().getName(),
                        "Invalid parameter name " + paramMap.paramName + " for a date was detected");
                return false;
        }
        return true;
    }

    public int mapFrequency(String sVal) {
        int freq = 0;
        switch (sVal) {
            case "ANNUAL":
                return 1;
            case "SEMI":
                return 2;
            case "TRI":
                return 3;
            case "QUARTERLY":
                return 4;
            case "BIMONTHLY":
                return 6;
            case "MONTHLY":
                return 12;
            case "DAILY":
                return 365;
            default:
                return 0;
        }
    }

    private int mapDayOfWeek(String sDow) {
        switch (sDow) {
            case "SUN":
                return 0;
            case "MON":
                return 1;
            case "TUE":
                return 2;
            case "WED":
                return 3;
            case "THU":
                return 4;
            case "FRI":
                return 5;
            case "SAT":
                return 6;
            default:
                error.setError(getClass().getName(), "Invalid day of the week " + sDow + " detected");
                return UnAvails.NOTAVAILINT;
        }
    }

    private int mapMonthOfYear(String sMoy) {
        switch (sMoy) {
            case "JAN":
                return 1;
            case "FEB":
                return 2;
            case "MAR":
                return 3;
            case "APR":
                return 4;
            case "MAY":
                return 5;
            case "JUN":
                return 6;
            case "JUL":
                return 7;
            case "AUG":
                return 8;
            case "SEP":
                return 9;
            case "OCT":
                return 10;
            case "NOV":
                return 11;
            case "DEC":
                return 12;
            default:
                error.setError(getClass().getName(), "Invalid month of year " + sMoy + " detected");
                return UnAvails.NOTAVAILINT;
        }
    }

    public String mapTimeUnitFromCompoundFreq(int compFreq) {
        String timeUnit = BondConsts.MONTHTIMEUNIT;
        switch (compFreq) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 6:
            case 12:
                break;
            case 52:
            case 26:
            case 13:
                timeUnit = BondConsts.WEEKTIMEUNIT;
                break;
            case 7:
            case 14:
            case 181:
            case 182:
            case 90:
            case 91:
            case 365:
                timeUnit = BondConsts.DAYTIMEUNIT;
                break;
            default:
                return UnAvails.NOTAVAILSTRING;
        }
        return timeUnit;

    }

    int mapYieldQuoteType(String strYieldQuoteType) {
        switch (strYieldQuoteType) {
            case "SIMPLE":
                return 0;
            case "TREAS":
                return 1;
            case "COMPOUND":
                return 2;
            default:
                error.setError(getClass().getName(), "Invalid yield quote type " + strYieldQuoteType + " detected");
                return UnAvails.NOTAVAILINT;
        }
    }

    private boolean assignToStringParam(ParamMap paramMap, String slVal) {
        if (paramMap.paramDomain != Domains.DOMAINSTRING && paramMap.paramDomain != Domains.DOMAINPERIODICITY) {
            error.setError(getClass().getName(),
                    "Parameter " + paramMap.paramName + " has been mis-assigned as a string");
            return false;
        }
        if (error.hasErrorYn == 'Y')
            return false;
        String sVal = slVal.toUpperCase();
        switch (paramMap.paramName) {
            case "AACONTROL":
                bond.aaControl = sVal;
                break;
            case "BASACCREAMORT":
                bond.basAccreAmort = sVal;
                bond.dcAccreAmort = dayCount.mapAccrualBasis(sVal);
                break;
            case "BASACCINT":
                bond.basAccInt = sVal;
                bond.dcAccruedInt = dayCount.mapAccrualBasis(sVal);
                bond.intSchedRule.dayCountMeth = bond.dcAccruedInt;
                break;
            case "BASACCPAYLEG":
                bond.basPayleg = sVal;
                bond.dcPayleg = dayCount.mapAccrualBasis(sVal);
                break;
            case "BASACCRECLEG":
                bond.basRecleg = sVal;
                bond.dcRecleg = dayCount.mapAccrualBasis(sVal);
                break;
            case "BASBONDEQV":
                bond.basBondEqv = sVal;
                bond.dcBondEqv = dayCount.mapAccrualBasis(sVal);
                break;
            case "BASDELAYDAYS":
                bond.basDelayDays = sVal;
                bond.dcDelayDays = dayCount.mapAccrualBasis(sVal);
                break;
            case "BASIDXINTERP":
                bond.basIdxInterp = sVal;
                bond.dcIdxInterp = dayCount.mapAccrualBasis(sVal);
                break;
            case "BASODD1ST":
                bond.basOdd1st = sVal;
                bond.dcOdd1st = dayCount.mapAccrualBasis(sVal);
                break;
            case "BASODDLAST":
                bond.basOddLast = sVal;
                bond.dcOddLast = dayCount.mapAccrualBasis(sVal);
                break;
            case "BASPAYLEG":
                bond.basPayleg = sVal;
                bond.dcPayleg = dayCount.mapAccrualBasis(sVal);
                bond.paylegSchedRule.dayCountMeth = bond.dcPayleg;
                break;
            case "BASRECLEG":
                bond.basRecleg = sVal;
                bond.dcRecleg = dayCount.mapAccrualBasis(sVal);
                bond.reclegSchedRule.dayCountMeth = bond.dcPayleg;
                break;
            case "BASRESETPAYLEG":
                bond.basResetPayleg = sVal;
                bond.dcResetPayleg = dayCount.mapAccrualBasis(sVal);
                bond.paylegResetSchedRule.dayCountMeth = bond.dcResetPayleg;
                break;
            case "BASRESETRECLEG":
                bond.basResetRecleg = sVal;
                bond.dcResetRecleg = dayCount.mapAccrualBasis(sVal);
                bond.reclegResetSchedRule.dayCountMeth = bond.dcResetRecleg;
                break;

            case "BASYLDPRC":
                bond.basYldPrc = sVal;
                bond.dcYldPrc = dayCount.mapAccrualBasis(sVal);
                break;
            case "ACCOUNT":
                bond.account = sVal;
                break;
            case "BONDEQVYLD":
                bond.bondEqvYld = sVal;
                bond.bondEqvYldOpt = mapFrequency(sVal);
                break;
            case "BOOKCALCMETH":
                bond.bookCalcMeth = sVal;
                break;
            case "BOOKYLDOPT":
                bond.bookYldOpt = sVal;
                break;
            case "CALCDELIMITER":
                bond.calcDelimiter = sVal;
                break;
            case "CALLPRCTYPE":
                bond.callPrcType = sVal;
                break;
            case "CALLTYPE":
                bond.callType = sVal;
                break;
            case "CANACCRUED":
                bond.canAccrued = sVal;
                break;
            case "CONVERTOPT":
                bond.convertOpt = sVal;
                break;
            case "COUNTRY":
                bond.country = sVal;
                bond.accrueSchedRule.country = sVal;
                break;
            case "CPNQUOTETYPE":
                bond.cpnQuoteType = sVal;
                break;
            case "CREDITDEFMODEL":
                bond.creditDefModel = sVal;
                break;
            case "CREDITAGENCY":
                bond.creditAgency = sVal;
                break;
            case "CREDITRATING":
                bond.creditRating = sVal;
                break;
            case "CREDITAGENCYRF":
                bond.creditAgencyRf = sVal;
                break;
            case "CREDITRATINGRF":
                bond.creditRatingRf = sVal;
                break;
            case "CREDITAGENCYSPRD":
                bond.creditAgencySprd = sVal;
                break;
            case "CREDITRATINGSPRD":
                bond.creditRatingSprd = sVal;
                break;
            case "CREDITSPDINTERPMETH":
                bond.creditSpdInterpMeth = sVal;
                break;
            case "CUSIP":
                bond.cusip = sVal;
                break;
            case "CURRENCY":
                bond.currency = sVal;
                break;
            case "DTFORMAT":
                dtFormat = sVal;
                break;
            case "DEFAULTMODEL":
                bond.defaultModel = sVal;
                break;
            case "DESCR":
                bond.secDescr = sVal;
                break;
            case "DISCCURVENAME":
                bond.discCurveName = sVal;
                break;
            case "DUMPFILENAME":
                bond.dumpFileName = sVal;
                break;
            case "FILEOUTFORMAT":
                bond.fileOutFormat = sVal;
                break;
            case "FILEOUTNAME":
                bond.fileOutName = sVal;
                break;
            case "FIXINGCURVENAME":
                bond.fixingCurveName = sVal;
                break;
            case "FIXINGBENCHMARK":
                bond.fixingBenchmark = sVal;
                break;
            case "FIXINGMATNAME":
                bond.fixingMatName[bond.noFixingTenor] = sVal;
                bond.fixingTenor[bond.noFixingTenor++] = mapMatlen(sVal);
                break;
            case "FUNDNAME":
                bond.fundName = sVal;
                break;
            case "HORIZONFREQ":
                bond.horizonFreq = sVal;
                bond.horizonTimeUnit = bond.time.mapTimeUnit(sVal);
                bond.horizonRepeatFactor = bond.time.mapRepeatFactor(sVal);
                break;
            case "HOSTSECID":
                bond.hostSecId = sVal;
                break;
            case "IDXINTERPMETH":
                bond.idxInterpMeth = sVal;
                break;
            case "IDXRATIOINTERPMETH":
                bond.idxRatioInterpMeth = sVal;
                break;
            case "IDXSCHEDDOW":
                bond.idxSchedRule.dayOfWeek = sVal;
                break;
            case "IDXSCHEDMOY":
                bond.idxSchedRule.monthOfYear = sVal;
                break;
            case "IDXPERIODICITY":
                bond.idxSchedRule.timeUnit = bond.time.mapTimeUnit(sVal);
                bond.idxSchedRule.repeatFactor = bond.time.mapRepeatFactor(sVal);
                bond.idxSchedRule.compoundFreq = bond.time.mapCompoundFreq(bond.idxSchedRule.timeUnit,
                        bond.idxSchedRule.repeatFactor);
                bond.idxSchedRule.schedType = BondConsts.INDEXRULE;
                break;
            case "IDXSCHEDCOMPOUNDFREQ":
                bond.idxSchedRule.compoundFreq = mapFrequency(sVal);
            case "IDXRATIOSCHEDDOW":
                bond.idxRatioSchedRule.dayOfWeek = sVal;
                break;
            case "IDXRATIOSCHEDMOY":
                bond.idxRatioSchedRule.monthOfYear = sVal;
                break;
            case "IDXRATIOPERIODICITY":
                bond.idxRatioSchedRule.timeUnit = bond.time.mapTimeUnit(sVal);
                bond.idxRatioSchedRule.repeatFactor = bond.time.mapRepeatFactor(sVal);
                bond.idxRatioSchedRule.compoundFreq = bond.time.mapCompoundFreq(bond.idxRatioSchedRule.timeUnit,
                        bond.idxRatioSchedRule.repeatFactor);
                bond.idxRatioSchedRule.schedType = BondConsts.INDEXRATIORULE;
                break;
            case "IDXHOLIDAYRULE":
                bond.idxHolidayRule = sVal;
                break;
            case "IDXASSUMEHOLIDAYRULE":
                bond.idxAssumeHolidayRule = sVal;
                break;
            case "INSTYPE":
                bond.instype = sVal;
                break;
            case "INTSCHEDDOW":
                bond.accrueSchedRule.dayOfWeek = sVal;
                bond.intSchedRule.dayOfWeek = sVal;
                break;
            case "INTSCHEDMOY":
                bond.accrueSchedRule.monthOfYear = sVal;
                bond.intSchedRule.monthOfYear = sVal;
                break;
            case "INTPAYMETH":
                bond.intPayMeth = sVal;
                break;
            case "INTPERIODICITY":
                bond.accrueSchedRule.timeUnit = bond.time.mapTimeUnit(sVal);
                bond.accrueSchedRule.repeatFactor = bond.time.mapRepeatFactor(sVal);
                bond.accrueSchedRule.schedType = BondConsts.INTERESTRULE;
                bond.accrueSchedRule.compoundFreq = bond.time.mapCompoundFreq(bond.accrueSchedRule.timeUnit,
                        bond.accrueSchedRule.repeatFactor);
                break;
            case "INTHOLIDAYRULE":
                bond.intHolidayRule = sVal;
                break;
            case "ISIN":
                bond.isin = sVal;
                break;
            case "ISSUER":
                bond.issuer = sVal;
                break;
            case "MATLENOPT":
                bond.matLenOpt = sVal;
                break;
            case "NAMEDELIMITER":
                bond.nameDelimiter = sVal;
                break;
            case "PASSMODEL":
                bond.passModel = sVal;
                break;
            case "PASSPROJECTMETH":
                bond.passProjectMeth = sVal;
                break;
            case "PAYLEGSCHEDDOW":
                bond.paylegSchedRule.dayOfWeek = sVal;
                break;
            case "PAYLEGSCHEDMOY":
                bond.paylegSchedRule.monthOfYear = sVal;
                break;
            case "PAYLEGHOLIDAYRULE":
                bond.paylegHolidayRule = sVal;
                break;
            case "PAYLEGPERIODICITY":
                bond.paylegPeriodicity = sVal;
                bond.paylegSchedRule.timeUnit = bond.time.mapTimeUnit(sVal);
                bond.paylegSchedRule.repeatFactor = bond.time.mapRepeatFactor(sVal);
                bond.paylegSchedRule.compoundFreq = bond.time.mapCompoundFreq(bond.paylegSchedRule.timeUnit,
                        bond.paylegSchedRule.repeatFactor);
                bond.paylegSchedRule.schedType = BondConsts.PAYLEGRULE;
                break;
            case "PAYLEGBENCHMARK":
                bond.paylegBenchmark = sVal;
                break;
            case "PORTNAME":
                bond.portName = sVal;
                break;
            case "PUTTYPE":
                bond.putType = sVal;
                break;
            case "RECLEGSCHEDDOW":
                bond.reclegSchedRule.dayOfWeek = sVal;
                break;
            case "RECLEGSCHEDMOY":
                bond.reclegSchedRule.monthOfYear = sVal;
                break;
            case "RECLEGHOLIDAYRULE":
                bond.reclegHolidayRule = sVal;
                break;
            case "RECLEGPERIODICITY":
                bond.reclegPeriodicity = sVal;
                bond.reclegSchedRule.timeUnit = bond.time.mapTimeUnit(sVal);
                bond.reclegSchedRule.repeatFactor = bond.time.mapRepeatFactor(sVal);
                bond.reclegSchedRule.compoundFreq = bond.time.mapCompoundFreq(bond.reclegSchedRule.timeUnit,
                        bond.reclegSchedRule.repeatFactor);
                bond.reclegSchedRule.schedType = BondConsts.RECLEGRULE;
                break;
            case "REINVESTMETH":
                bond.reinVestMethod = sVal;
                break;
            case "RESETBENCHMARK":
                bond.resetBenchmark = sVal;
                break;
            case "RECLEGBENCHMARK":
                bond.reclegBenchmark = sVal;
                break;
            case "PAYLEGRESETSCHEDDOW":
                bond.paylegResetSchedRule.dayOfWeek = sVal;
                break;
            case "PAYLEGRESETSCHEDMOY":
                bond.paylegResetSchedRule.monthOfYear = sVal;
                break;
            case "PAYLEGRESETPERIODICITY":
                bond.paylegResetPeriodicity = sVal;
                bond.paylegResetSchedRule.timeUnit = bond.time.mapTimeUnit(sVal);
                bond.paylegTimeUnit = bond.paylegResetSchedRule.timeUnit;
                bond.paylegResetSchedRule.repeatFactor = bond.time.mapRepeatFactor(sVal);
                bond.paylegResetSchedRule.compoundFreq = bond.time.mapCompoundFreq(bond.paylegResetSchedRule.timeUnit,
                        bond.paylegResetSchedRule.repeatFactor);
                bond.paylegResetSchedRule.schedType = BondConsts.paylegResetRULE;
                break;
            case "PAYLEGRESETHOLIDAYRULE":
                bond.paylegResetHolidayRule = sVal;
                break;
            case "RECLEGRESETSCHEDDOW":
                bond.reclegResetSchedRule.dayOfWeek = sVal;
                break;
            case "RECLEGRESETSCHEDMOY":
                bond.reclegResetSchedRule.monthOfYear = sVal;
                break;
            case "RECLEGRESETPERIODICITY":
                bond.reclegResetPeriodicity = sVal;
                bond.reclegResetSchedRule.timeUnit = bond.time.mapTimeUnit(sVal);
                bond.reclegResetSchedRule.repeatFactor = bond.time.mapRepeatFactor(sVal);
                bond.reclegResetSchedRule.compoundFreq = bond.time.mapCompoundFreq(bond.reclegResetSchedRule.timeUnit,
                        bond.reclegResetSchedRule.repeatFactor);
                bond.reclegResetSchedRule.schedType = BondConsts.paylegResetRULE;
                break;
            case "RECLEGRESETHOLIDAYRULE":
                bond.reclegResetHolidayRule = sVal;
                break;
            case "RESETPERIODICITY":
                bond.resetPeriodicity = sVal;
                bond.resetSchedRule.timeUnit = bond.time.mapTimeUnit(sVal);
                bond.resetSchedRule.repeatFactor = bond.time.mapRepeatFactor(sVal);
                bond.resetSchedRule.compoundFreq = bond.time.mapCompoundFreq(bond.resetSchedRule.timeUnit,
                        bond.resetSchedRule.repeatFactor);
                bond.resetSchedRule.schedType = BondConsts.RESETRULE;
                break;
            case "RESETHOLIDAYRULE":
                bond.resetHolidayRule = sVal;
                break;
            case "RESETSCHEDDOW":
                bond.resetSchedRule.dayOfWeek = sVal;
                break;
            case "RESETSCHEDMOY":
                bond.resetSchedRule.monthOfYear = sVal;
                break;
            case "RISKRETURNGOAL":
                bond.riskReturnGoal = sVal;
                break;
            case "SECURITYID":
                bond.securityId = sVal;
                break;
            case "SECURITYTYPE":
                bond.securityType = sVal;
                break;
            case "SPOTFIXINGINTERVALNAME":
                bond.discCurveYn = 'N';
                bond.fixingCurveYn = 'Y';
                bond.spotFixingIntervalName = sVal;
                bond.spotFixingInterval = mapSpotInterval(sVal);
                break;
            case "SPOTDISCINTERVALNAME":
                bond.spotDiscIntervalName = sVal;
                bond.spotDiscInterval = mapSpotInterval(sVal);
                bond.discCurveYn = 'Y';
                bond.fixingCurveYn = 'N';
                break;
            case "STATEOFISSUE":
                bond.stateOfIssue = sVal;
                break;
            case "STATEOFRESIDENCE":
                bond.stateOfResidence = sVal;
                break;
            case "STRIPTYPE":
                bond.stripType = sVal;
                break;
            case "SWAPPAYTYPE":
                bond.swapPayType = sVal;
                break;
            case "SWAPSIDE":
                bond.swapSide = sVal;
                break;
            case "TAXLOTID":
                bond.taxLotId = sVal;
                break;
            case "TAXHOLIDAYRULE":
                bond.taxSchedRule.holidayRule = sVal;
                break;
            case "TAXPERIODICITY":
                bond.taxSchedRule.timeUnit = bond.time.mapTimeUnit(sVal);
                bond.taxSchedRule.repeatFactor = bond.time.mapRepeatFactor(sVal);
                bond.taxSchedRule.compoundFreq = bond.time.mapCompoundFreq(bond.idxSchedRule.timeUnit,
                        bond.idxSchedRule.repeatFactor);
                bond.taxSchedRule.schedType = BondConsts.TAXSCHEDRULE;
                break;
            case "YIELDCURVEINTERPMETH":
                bond.yieldCurveInterpMeth = sVal;
                break;
            case "YIELDQUOTETYPE":
                bond.yieldQuoteType = sVal;
                break;
            case "IDXRATIOCOMPOUNDFREQ":
                bond.idxRatioSchedRule.compoundFreq = mapFrequency(sVal);
                bond.idxRatioSchedRule.schedType = BondConsts.INDEXRATIORULE;
            case "INTSCHEDCOMPOUNDFREQ":
                bond.accrueSchedRule.compoundFreq = mapFrequency(sVal);
                bond.accrueSchedRule.schedType = BondConsts.INTERESTRULE;
                bond.intSchedRule.compoundFreq = bond.accSchedCompoundFreq;
                bond.intSchedRule.schedType = BondConsts.INTERESTRULE;
                break;
            case "PAYLEGSCHEDCOMPOUNDFREQ":
                bond.paylegSchedRule.compoundFreq = mapFrequency(sVal);
                bond.paylegSchedRule.schedType = BondConsts.PAYLEGRULE;
                break;
            case "RECLEGSCHEDCOMPOUNDFREQ":
                bond.reclegSchedRule.compoundFreq = mapFrequency(sVal);
                bond.reclegSchedRule.schedType = BondConsts.RECLEGRULE;
                break;
            case "paylegResetSCHEDCOMPOUNDFREQ":
                bond.paylegResetSchedRule.compoundFreq = mapFrequency(sVal);
                bond.paylegResetSchedRule.schedType = BondConsts.paylegResetRULE;
                break;
            case "RESETRECLEGSCHEDCOMPOUNDFREQ":
                bond.reclegResetSchedRule.compoundFreq = mapFrequency(sVal);
                bond.reclegResetSchedRule.schedType = BondConsts.RESETRECLEGRULE;
                break;
            case "RESETSCHEDCOMPOUNDFREQ":
                bond.resetSchedRule.compoundFreq = mapFrequency(sVal);
                bond.resetSchedRule.schedType = BondConsts.RESETRULE;
                break;
            case "TRADETYPE":
                bond.tradeType = sVal;
                break;
            case "YIELDFORECASTMETH":
                bond.yieldForeCastMeth = sVal;
                break;
            default:
                error.setError(getClass().getName(), "Invalid parameter " + paramMap.paramName
                        + " was specfifed and could not be assigned a text value");
                return false;

        }
        return true;
    }

    private int mapMatlen(String sVal) {
        String uSval = sVal.toUpperCase();
        int matLen = 0;
        switch (uSval) {
            case "1M":
                matLen = 1;
                break;
            case "2M":
                matLen = 2;
                break;
            case "3M":
                matLen = 3;
                break;
            case "6M":
                matLen = 6;
                break;
            case "1Y":
                matLen = 12;
                break;
            case "2Y":
                matLen = 24;
                break;
            case "3Y":
                matLen = 36;
                break;
            case "5Y":
                matLen = 60;
                break;
            case "7Y":
                matLen = 84;
                break;
            case "10Y":
                matLen = 120;
                break;
            case "15Y":
                matLen = 180;
                break;
            case "20Y":
                matLen = 240;
                break;
            case "25Y":
                matLen = 300;
                break;
            case "30Y":
                matLen = 360;
                break;
            case "40Y":
                matLen = 480;
                break;
            case "50Y":
                matLen = 600;
                break;
            case "60Y":
                matLen = 720;
                break;
            default:
                String errmsg = "Appliation error - detected a invalid maturity length string " + sVal;
                error.setError(getClass().getName(), errmsg);
                matLen = 0;
                break;
        }
        return matLen;
    }

    private int mapMatKey(String sVal) {
        String uSval = sVal.toUpperCase();
        int matKey = 0;
        switch (uSval) {
            case "1M":
                matKey = 0;
                break;
            case "2M":
                matKey = 1;
                break;
            case "3M":
                matKey = 2;
                break;
            case "6M":
                matKey = 3;
                break;
            case "1Y":
                matKey = 4;
                break;
            case "2Y":
                matKey = 5;
                break;
            case "3Y":
                matKey = 6;
                break;
            case "5Y":
                matKey = 7;
                break;
            case "7Y":
                matKey = 8;
                break;
            case "10Y":
                matKey = 9;
                break;
            case "15Y":
                matKey = 10;
                break;
            case "20Y":
                matKey = 11;
                break;
            case "25Y":
                matKey = 12;
                break;
            case "30Y":
                matKey = 13;
                break;
            case "40Y":
                matKey = 14;
                break;
            case "50Y":
                matKey = 15;
                break;
            case "60Y":
                matKey = 16;
                break;
            default:
                String errmsg = "Appliation error - detected a invalid maturity length string " + sVal;
                error.setError(getClass().getName(), errmsg);
                matKey = UnAvails.NOTAVAILINT;
                break;
        }
        return matKey;
    }

    private int mapSpotInterval(String spotIntervalName) {
        int spotInt = 3;
        String uSpotInt = spotIntervalName.toUpperCase();
        switch (uSpotInt) {
            case "1M":
                spotInt = 1;
                break;
            case "2M":
                spotInt = 2;
                break;
            case "3M":
                spotInt = 3;
                break;
            case "6M":
                spotInt = 6;
                break;
            default:
                String errmsg = "Application error - detected an invalid spot yield curve interval";
                error.setError(getClass().getName(), errmsg);
                spotInt = UnAvails.NOTAVAILINT;

        }
        return spotInt;
    }

    class ParamMap {
        String paramName;
        int paramDomain;
        char isArrayYn;
        char isChoiceYn;
        String choiceList;
        double scale;
        double dMin;
        double dMax;

        int iVal;
        double dVal;
        Dates dtVal;
        String sVal;
        String errMsg;

        boolean isOk;

        char cVal;

        String strToConvert;
        String definition;

        ParamMap() {
            String paramName = UnAvails.NOTAVAILSTRING;
            int paramDomain = UnAvails.NOTAVAILINT;
            char isArrayYn = UnAvails.NOTAVAILCHAR;
            char isChoiceYn = UnAvails.NOTAVAILCHAR;
            String choiceList = UnAvails.NOTAVAILSTRING;
            double scale = UnAvails.NOTAVAILDOUBLE;
            double dMin = UnAvails.NOTAVAILDOUBLE;
            double dMax = UnAvails.NOTAVAILDOUBLE;
            int iVal = UnAvails.NOTAVAILINT;
            double dVal = UnAvails.NOTAVAILDOUBLE;
            Dates dtVal = new Dates();
            String sVal = UnAvails.NOTAVAILSTRING;
            String errMsg = UnAvails.NOTAVAILSTRING;
            boolean isOk = true;
            char cVal = UnAvails.NOTAVAILCHAR;
            String strToConvert = UnAvails.NOTAVAILSTRING;
            String definition = UnAvails.NOTAVAILSTRING;
        }

        ParamMap(String paramName, int paramDomain, char isArray, char isChoice, String choiceList, double scale,
                double dMin, double dMax,
                String definition) {
            this.paramName = paramName;
            this.paramDomain = paramDomain;
            this.isArrayYn = isArray;
            this.isChoiceYn = isChoice;
            this.choiceList = choiceList;
            this.scale = scale;
            this.dMin = dMin;
            this.dMax = dMax;
            this.definition = definition;
        }
    }

}
// String none = "None";

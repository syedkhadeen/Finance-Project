package app;

public class Domains {
    static final int DOMAINCHAR = 0; /* 0 */
    static final int DOMAININT = 1; /* 1 */
    static final int DOMAINDATE = 2; /* 2 */
    static final int DOMAINDOUBLE = 3; /* 3 */
    static final int DOMAINSTRING = 4; /* 4 */
    static final int DOMAINYESNO = 5; /* 5 */
    static final int DOMAINONOFF = 6; /* 6 */
    static final int DOMAINIGNORE = 7; /* 7 ignore this field entirely, used of oas only */
    static final int DOMAINLONG = 8;
    static final int DOMAINPCT = 9;
    static final int DOMAINPERIODICITY = 10; // like 6M
    static final int DOMAINMONEY = 11; // commas allowed
    static final int DOMAINPRICE = 12; // bond price as 98.124 or 98 1/16 or 98 1/32+
    static final String[] TOPDOMAINS = { "Char", "Int", "Date", "Double", "String", "String", "Int", "NA",
            "Long", "Double", "String", "Double", "Double" };
}

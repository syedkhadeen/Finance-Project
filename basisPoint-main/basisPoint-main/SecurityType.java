
/* Data file layout
Row 1 is Header:
Field 1: no of data rows
Field 2: no columns
Field 3: date format
Field 4: start row of data
Field 5: Name of file
Field 6: Last date updated
Field 7: Product Version
Row 2 is list of data columns to be mapped in Object
Row 3 is list of data domains: D=date, I=integer, C = character, N = decimal number, S = string
Row StartDate row - 1
Row ....
Row StartData


**/
class SecurityType {
    String sectype;
    String accruedDCBasis;
    int compoundFreq;
    String timeUnit;
    int repeatFactor;
    String secDescr;
    char cleanPriceYn;
    String instype;
    String isoCountry;
    int accruedSize;
    String accruedMeth;
    int sfigAccrued;
    int sfigAccruedSize;
    int sfigCurrency;
    char accruedRoundYn;
    String passthruModel;
    String passthruProjectmeth;
    String stripType;
    String currency;
}



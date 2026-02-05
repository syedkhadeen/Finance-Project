
public class CalcEnums {
    // list of yield calc options
    static final String SIMPLEYIELD = "SIMPLE";
    static final String TREASURYYIELD = "TREASURY";
    static final String CONPOUNDYIELD = "COMPOUND";

    static final double USEDISCOUNTSIMPLEYIELDPERIOD = 2.; // in years
    // maturity length options

    // list limits and precision for root solving
    static final int MAXROOTITERATIONS = 200;
    static final double ROOTPRECISION = .0000001;
    static final double ROOTYIELDDERIVATIVEDELTA = .0000001;
    static final double ROOTSPEEDDERIVATIVEDELTA = .0000001;
    static final double MINSPEED = -.01;
    static final double MAXSPEED = 20.;


    // yield curves
    static final String LINEAR = "LINEAR";
    static final String CUBICSPLINE = "CSPLINE";
    static final String DISCCURVE = "D";
    static final String RESETCURVE = "R";
    static final double KEYRATESHIFT = 25 * .0001; // 25 basis points

    static final char NO = 'N';
    static final char YES = 'Y';

}

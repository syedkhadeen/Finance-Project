package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.util.StringTokenizer;

public class FileTableReader {

    /*
     * Data file layout
     * Row 1 is Header:
     * Field 1: no of data rows
     * Field 2: no columns
     * Field 3: date format
     * Field 4: start row of data
     * Field 5: Name of file
     * Field 6: Last date updated
     * Field 7: Product Version
     * Row 2 is list of data columns to be mapped in Object
     * Row 3 is list of data domains: D=date, I=integer, C = character, N = decimal
     * number, S = string
     * Row StartDate row - 1
     * Row ....
     * Row StartData
     */

    String rootDir;
    FileReader fileReader;
    BufferedReader bufferedReader;
    Scanner scanner;
    StringTokenizer stok;
    int noRows;
    int noCols;
    int dataStartRow;
    String[] colNames;
    String[] colDomains;
    int dataOffset;
    String dtFormat;
    String fileDt;
    String fullFileName;
    String fileName;
    String version;
    Dates dateOfFile;
    String errorMessage = null;
    String warningMessage = null;
    char BSNO = 'N';
    char BSYES = 'Y';
    char hasWarning = BSNO;
    char hasErrorYn = BSNO;

    FileTableReader(String fullFileName) {
        this.fullFileName = fullFileName;
    }

    public boolean tableReader(String fullFileName) throws Exception {
        try {
            this.fileReader = new FileReader(fullFileName);
        } catch (Exception exception) {
            hasErrorYn = BSYES;
            errorMessage = "could not open file " + fullFileName;
            return false;
        }
        try {
            this.bufferedReader = new BufferedReader(this.fileReader);
        } catch (Exception exception) {
            hasErrorYn = BSYES;
            errorMessage = this.getClass().getName() + " could not open buffered reader " + fullFileName;
            return false;
        }
        this.scanner = new Scanner(this.bufferedReader);
        // read header - row 1
        String sHeader = this.scanner.nextLine(); // header row
        this.stok = new StringTokenizer(sHeader, ",;|");
        String sRowCount = stok.nextToken(); // row count
        this.noRows = Integer.valueOf(sRowCount);
        String sColCount = stok.nextToken(); // column count
        this.noCols = Integer.valueOf(sColCount);
        String sDataStart = stok.nextToken();
        this.dataStartRow = Integer.valueOf(sDataStart); // start row of data
        String sDtFormat = stok.nextToken(); // date format
        this.dtFormat = sDtFormat;
        this.fileName = stok.nextToken(); // name of file
        this.fileDt = stok.nextToken();
        this.version = stok.nextToken();

        // read domains - row 2
        String sDomains = this.scanner.nextLine(); // row of domains

        stok = new StringTokenizer(sDomains, ",;|");
        this.colDomains = new String[this.noCols];
        int ic = -1;
        while (++ic < this.noCols) {
            this.colDomains[ic] = stok.nextToken();
        }
        // read column names - row 3
        this.colNames = new String[this.noCols];
        String sColNames = this.scanner.nextLine(); // fields name for columns
        stok = new StringTokenizer(sColNames, ",;|");
        ic = -1;
        while (++ic < this.noCols) {
            this.colNames[ic] = stok.nextToken();
        }
        // skip lines that are left for misc use
        int skipLines = dataStartRow - 4;
        int is = -1;
        while (++is < skipLines) {
            this.scanner.nextLine();
        }
        return true;
    }

}

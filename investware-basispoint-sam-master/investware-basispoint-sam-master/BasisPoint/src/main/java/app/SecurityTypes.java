package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

class SecurityTypes {
        Error error;
        String useConfigYn;
        String rootDir;
        String sectype;
        String basIntAccrued;
        int compoundFreq;
        String timeUnit;
        int repeatFactor;
        int sfigCurrency;
        String secDescr;
        char cleanPrice;
        String instype;
        String isoCountry;
        int accruedSize;
        String accruedMeth;
        int sfigAccrued;
        int sfigAccruedSize;
        char accruedRoundYn;
        String passModel;
        String passProjectMeth;
        String stripType;
        String resetTimeUnit;
        int resetRepeatFactor;
        char fedTaxExempt;
        char stateTaxExempt;
        ArrayList<SecurityTypes> securityTypeList = new ArrayList();

        SecurityTypes() {

        }

        SecurityTypes(String useConfigYn, String rootDir, Error error) {
                this.useConfigYn = useConfigYn;
                this.rootDir = rootDir;
                this.error = error;
        }

        // secType accruedDCBasis compoundFreq timeUnit repeatFactor secDescr
        // cleanPriceYn instype isoCountry accruedSize accruedMeth sfigAccruedSize
        // sfigAccrued sfigCurrencya accruedRoundYn passthruModel passthruProjectMeth
        // stripType resetTimeUnit resetRepeatFactor currency fedTaxExempt
        // stateTaxExempt
        // secType accruedDCBasis compoundFreq timeUnit repeatFactor secDescr
        // cleanPriceYn instype isoCountry accruedSize accruedMeth sfigAccruedSize
        // sfigAccrued sfigCurrencya accruedRoundYn passthruModel passthruProjectMeth
        // stripType resetTimeUnit resetRepeatFactor currency fedTaxExempt
        // stateTaxExempt
        SecurityTypes(String sectype, String basIntAccrued, int compoundFreq, String timeUnit, int repeatFactor,
                        String secDescr, char cleanPrice, String instype, String isoCountry, int accruedSize,
                        String accruedMeth, int sfigAccruedSize, int sfigAccrued, int sfigCurrency, char accruedRndYn,
                        String passModel, String passProjectMeth, String stripType, String resetTimeUnit,
                        int resetRepeatFactor,
                        String currency, char fedTaxExempt, char stateTaxExempt) {
                this.sectype = sectype;
                this.basIntAccrued = basIntAccrued;
                this.compoundFreq = compoundFreq;
                this.timeUnit = timeUnit;
                this.repeatFactor = repeatFactor;
                this.sfigCurrency = sfigCurrency;
                this.secDescr = secDescr;
                this.cleanPrice = cleanPrice;
                this.instype = instype;
                this.isoCountry = isoCountry;
                this.accruedSize = accruedSize;
                this.accruedMeth = accruedMeth;
                this.sfigAccruedSize = sfigAccruedSize;
                this.sfigAccrued = sfigAccrued;
                this.sfigCurrency = sfigCurrency;
                this.accruedRoundYn = accruedRoundYn;
                this.passModel = passModel;
                this.passProjectMeth = passProjectMeth;
                this.stripType = stripType;
                this.resetTimeUnit = resetTimeUnit;
                this.resetRepeatFactor = resetRepeatFactor;
                this.fedTaxExempt = fedTaxExempt;
                this.stateTaxExempt = stateTaxExempt;
        }

        public boolean setSecurityTypes(String useConfigYn, String rootDirectory, Error error) {
                if (useConfigYn.equals("N")) {
                        assignSecurityTypes();
                        return true;
                } else {
                        String fileName = rootDirectory + "\\config\\sectype.csv";
                        boolean isOk = readSecurityTypes(fileName);
                        return isOk;
                }
        }

        public boolean readSecurityTypes(String fullFileName) {
                String strSecType = "";
                FileReader fileReader;
                BufferedReader buffReaader;
                // creates a new file instance
                try {
                        fileReader = new FileReader(fullFileName);
                        buffReaader = new BufferedReader(fileReader);
                } catch (Exception ex) {
                        String errmsg = "Could not find input file " + fullFileName;
                        error.setError(getClass().getName(), Error.FILEREADERROR, errmsg);
                        return false;
                }
                String sLine = "";
                try {
                        buffReaader.readLine();
                        buffReaader.readLine();
                        buffReaader.readLine();
                        int noSectypes = 0;
                        String[] strParts = new String[Limits.MAXSECTYPEPARAMS + 1];

                        // secType[0] accruedDCBasis compoundFreq timeUnit repeatFactor secDescr
                        // cleanPriceYn instype isoCountry accruedSize accruedMeth sfigAccruedSize
                        // sfigAccrued sfigCurrencya accruedRoundYn passthruModel passthruProjectMeth
                        // stripType resetTimeUnit resetRepeatFactor currency fedTaxExempt
                        // stateTaxExempt

                        while (null != (sLine = buffReaader.readLine())) {
                                strParts = sLine.split(",");
                                int ip = -1;
                                String sectype = strParts[++ip]; // 0
                                String accruedDCBasis = strParts[++ip]; // 1
                                int compoundFreq = Integer.valueOf(strParts[++ip]); // 2
                                String timeUnit = strParts[++ip]; // 3
                                int repeatFactor = Integer.valueOf(strParts[++ip]); // 4
                                String secDescr = strParts[++ip]; // 5
                                char cleanPriceYn = strParts[++ip].charAt(0); // 6
                                String instype = strParts[++ip]; // 7
                                String isoCountry = strParts[++ip]; // 8
                                int accruedSize = Integer.valueOf(strParts[++ip]); // 9
                                String accruedMeth = strParts[++ip]; // 10
                                int sfigAccruedSize = Integer.valueOf(strParts[++ip]); // 11
                                int sfigAccrued = Integer.valueOf(strParts[++ip]); // 12
                                int sfigCurrency = Integer.valueOf(strParts[++ip]); // 13
                                char accruedRoundYn = strParts[++ip].charAt(0);
                                String passthruModel = strParts[++ip];
                                String passthruProjectMeth = strParts[++ip];
                                String stripType = strParts[++ip];
                                String resetTimeUnit = strParts[++ip];
                                int resetRepeatFactor = Integer.valueOf(strParts[++ip]);
                                String currency = strParts[++ip];
                                char fedTaxExempt = strParts[++ip].charAt(0);
                                char stateTaxExempt = strParts[++ip].charAt(0);
                                // secType accruedDCBasis compoundFreq timeUnit repeatFactor secDescr
                                // cleanPriceYn instype isoCountry accruedSize accruedMeth sfigAccruedSize
                                // sfigAccrued sfigCurrencya accruedRoundYn passthruModel passthruProjectMeth
                                // stripType resetTimeUnit resetRepeatFactor currency fedTaxExempt
                                // stateTaxExempt
                                securityTypeList.add(noSectypes++, new SecurityTypes(sectype, accruedDCBasis,
                                                compoundFreq, timeUnit, repeatFactor, secDescr,
                                                cleanPriceYn, instype, isoCountry, accruedSize, accruedMeth,
                                                sfigAccruedSize, sfigAccrued, sfigCurrency, accruedRoundYn,
                                                passthruModel, passthruProjectMeth, stripType, resetTimeUnit,
                                                resetRepeatFactor, currency, fedTaxExempt, stateTaxExempt));
                        }

                } catch (Exception ex) {
                        String errmsg = "could not populate security types";
                        error.setError(getClass().getName(), errmsg);
                        return false;
                }
                return true;
        }

        public void assignSecurityTypes() {
                int sNo = 0, count = 1;
                securityTypeList.add(sNo++, new SecurityTypes("USTBILL", "ACT/360", 0, "N", 0, "US Treas Bill", 'Y',
                                "Discount", "USA", 1, "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USD", 'N', 'N'));
                securityTypeList.add(sNo++, new SecurityTypes("USTNOTE", "ACT/ACT", 2, "M", 6, "US Treas Note", 'Y',
                                "FixedRate", "USA", 1, "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USD", 'N', 'N'));
                securityTypeList.add(sNo++, new SecurityTypes("USTBOND", "ACT/ACT", 2, "M", 6, "US Treas Bond", 'Y',
                                "FixedRate", "USA", 1, "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USD", 'N', 'N'));
                securityTypeList.add(sNo++,
                                new SecurityTypes("USCORP-Semi-30/360", "30/360", 2, "M", 6,
                                                "US Corp Semi-annual interest 30/360 Daycount", 'Y', "FixedRate", "USA",
                                                1, "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USD", 'N', 'N'));
                securityTypeList.add(sNo++,
                                new SecurityTypes("USCORP-Qrtly-30/360", "30/360", 4, "M", 4,
                                                "US Corp Quarterly interest 30/360 basis", 'Y', "FixedRate", "USA", 1,
                                                "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USD", 'N', 'N'));
                securityTypeList.add(sNo++, new SecurityTypes("OAT-Semi-Act/Act", "ACT/ACT", 2, "M", 6,
                                "French  OAT Semi-annaul interest Act/Act Daycount", 'Y', "FixedRate", "FRA", 100,
                                "STD", 5, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "EUR", 'N', 'N'));
                securityTypeList.add(sNo++,
                                new SecurityTypes("MUNI-Semi-30/360", "30/360", 2, "M", 6,
                                                "Muni Semi Annual interest 30/360 Daycount", 'Y', "FixedRate", "USA", 1,
                                                "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USD", 'N', 'Y'));
                securityTypeList.add(sNo++,
                                new SecurityTypes("MORT-Monthly-30/360", "30/360", 12, "M", 1,
                                                "Mortgage Pass-thru Monthly 30/360", 'Y', "Mortgage", "USA", 1, "STD",
                                                2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USD", 'N', 'N'));
                securityTypeList.add(sNo++,
                                new SecurityTypes("CD-ACT/360", "Act/360", 2, "N", 1,
                                                "Certificate of Deposit-Act/360 Daycount", 'Y', "AtMaturity", "USA", 1,
                                                "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USD", 'N', 'N'));
                securityTypeList.add(sNo++,
                                new SecurityTypes("CD-30/360", "30/360", 2, "N", 1,
                                                "Certificate of Deposit-30/360 Daycount", 'Y', "AtMaturity", "USA", 1,
                                                "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USD", 'N', 'N'));
                securityTypeList.add(sNo++, new SecurityTypes("INDEX-USA-Semi-Act", "Act/Act", 2, "M", 6,
                                "US Index Adjusted Semi Annual Interest Act/Act Daycount", 'Y', "Indexed", "USA", 1,
                                "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USD", 'N', 'N'));
                securityTypeList.add(sNo++, new SecurityTypes("INDEX-ILR-Semi-Act", "Act/Act", 2, "M", 6,
                                "Israel Index Adjusted Semi Annual Interest  Act/Act Daycount", 'Y', "Indexed", "ISR",
                                1, "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "ILS", 'N', 'N'));
                securityTypeList.add(sNo++,
                                new SecurityTypes("FLOATER-Semi-Qrtly-Act/Act", "Act/Act", 2, "M", 6,
                                                "Floater pays semi resets qrtly Act/Act", 'Y', "Floater", "USA", 1,
                                                "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USA", 'N', 'N'));
                securityTypeList.add(sNo++,
                                new SecurityTypes("FLOATER-Semi-Monthly-Act/Act", "Act/Act", 2, "M", 6,
                                                "Floater pays qrtly resets monthly Act/Act", 'Y', "Floater", "USA", 1,
                                                "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 1, "USA", 'N', 'N'));
                securityTypeList.add(sNo++,
                                new SecurityTypes("FLOATER-Qrtly-Monthly-Act/Act", "Act/Act", 2, "M", 6,
                                                "Floater pays semi resets qrtly Act/Act", 'Y', "Floater", "USA", 1,
                                                "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USA", 'N', 'N'));
                securityTypeList.add(sNo++, new SecurityTypes("USD", "Act/Act", 0, "M", 0, "US Dollars", 'Y',
                                "Currency", "USA", 2, "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USD", 'N', 'N'));
                securityTypeList.add(sNo++, new SecurityTypes("CANGOVT-Semi-Act/365", "Act/365", 2, "M", 6,
                                "Canadian Government Semi Annual Interest Act/365 Daycount", 'Y', "FixedRate", "CAN", 2,
                                "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "CAD", 'N', 'N'));
                securityTypeList.add(sNo++,
                                new SecurityTypes("ZERO-Act/360", "Act/360", 0, "M", 6, "Zero Coupon Bond", 'Y',
                                                "Discount", "USA", 2, "STD", 2, 2, 2, 'Y', "CPR", "P", "NP", "M", 0,
                                                "USD", 'N', 'N'));
                securityTypeList.add(sNo++, new SecurityTypes("EUR", "Act/Act", 0, "N", 0, "Euro", 'Y', "Currency",
                                "EUR", 2, "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USD", 'N', 'N'));
                securityTypeList.add(sNo++, new SecurityTypes("CAD", "Act/Act", 0, "N", 0, "Canadian dollar", 'Y',
                                "Currency", "CAN", 2, "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USD", 'N', 'N'));
                securityTypeList.add(sNo++, new SecurityTypes("GBP", "Act/Act", 0, "N", 0, "Pounds", 'Y', "Currency",
                                "GBR", 2, "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USD", 'N', 'N'));
                securityTypeList.add(sNo++, new SecurityTypes("JPY", "Act/Act", 0, "N", 0, "Yen", 'Y', "Currency",
                                "JPN", 2, "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USD", 'N', 'N'));
                securityTypeList.add(sNo++, new SecurityTypes("AUD", "Act/Act", 0, "N", 0, "Austrailan dollar", 'Y',
                                "Currency", "AUS", 2, "STD", 2, 2, 2, 'Y', "CPR", "P", "NO", "M", 4, "USD", 'N', 'N'));
                securityTypeList.add(sNo++,
                                new SecurityTypes("EURO-Semi-30E/360", "30E/360", 2, "M", 6,
                                                "Euro semi-annual bond 30E", 'Y', "FixedRate", "EU", 2, "STD", 2, 2, 2,
                                                'Y', "CPR", "P", "NO", "M", 4, "USD", 'N', 'N'));
        }
}

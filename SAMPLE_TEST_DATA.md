# Sample Test Data for Bond Calculator

## Test Case 1: Basic Corporate Bond (No Forecast)

### Input Data:
```
Security Description: Apple Inc 3.5% 2028
Security ID: AAPL2028
Sector: Corp
Issuer: Apple Inc
Coupon: 3.5
Maturity Date: 08/03/2028
Par Amount: 100000
Price: 98.125
Settle Date: 02/17/2026
```

### Expected Backend Logs:
```
[Java BasisPoint] Attempting to call Java...
[Java BasisPoint] Path: D:/Finance Project/basisPoint-main/basisPoint-main
[Java BasisPoint] Java is installed
[Java BasisPoint] Input file created
[Java BasisPoint] Executing: cd "D:/Finance Project/basisPoint-main/basisPoint-main" && java -cp ".;gson-2.11.0.jar" RunFromFile
[Java BasisPoint] ✅ Execution successful
```

---

## Test Case 2: Corporate Bond with Forecast (SOFR Curve)

### Input Data:
```
Security Description: Microsoft Corp 4.25% 2030
Security ID: MSFT2030
Sector: Corp
Issuer: Microsoft Corporation
Coupon: 4.25
Maturity Date: 12/15/2030
Par Amount: 250000
Price: 102.50
Settle Date: 02/17/2026

FORECAST SECTION:
Forecast Date: 09/01/2026
Forecast Discount Curve: SOFR
Forecast Fixing Curve: Forward
```

### Expected Backend Logs:
```
[Java BasisPoint] Attempting to call Java...
[Java BasisPoint] Path: D:/Finance Project/basisPoint-main/basisPoint-main
[Java BasisPoint] Java is installed
[Java BasisPoint] Forecast date: 09/01/2026
[Java BasisPoint] Forecast discount curve: SOFR
[Java BasisPoint] Forecast fixing curve: Forward
[Java BasisPoint] Input file created
[Java BasisPoint] Executing: cd "D:/Finance Project/basisPoint-main/basisPoint-main" && java -cp ".;gson-2.11.0.jar" RunFromFile
[Java BasisPoint] ✅ Execution successful
```

---

## Test Case 3: Government Bond with Custom Discount Curve

### Input Data:
```
Security Description: US Treasury 2.875% 2029
Security ID: UST2029
Sector: Govt
Issuer: US Treasury
Coupon: 2.875
Maturity Date: 05/15/2029
Par Amount: 500000
Price: 99.75
Settle Date: 02/17/2026

FORECAST SECTION:
Forecast Date: 06/01/2026
Forecast Discount Curve: Other... (Custom)
```

### Custom Discount Curve:
```
Curve Name: Treasury Yield Curve
Curve Date: 02/17/2026
Spot Curve: Yes

Points:
1. Tenor: 3m,  Rate: 5.25, StdDev: 0.05
2. Tenor: 6m,  Rate: 5.15, StdDev: 0.08
3. Tenor: 1yr, Rate: 4.95, StdDev: 0.10
4. Tenor: 2yr, Rate: 4.50, StdDev: 0.12
5. Tenor: 3yr, Rate: 4.25, StdDev: 0.15
6. Tenor: 5yr, Rate: 4.00, StdDev: 0.18
7. Tenor: 7yr, Rate: 3.85, StdDev: 0.20
8. Tenor: 10yr, Rate: 3.75, StdDev: 0.22
9. Tenor: 20yr, Rate: 3.90, StdDev: 0.25
10. Tenor: 30yr, Rate: 4.00, StdDev: 0.28
```

### Expected Backend Logs:
```
[Java BasisPoint] Attempting to call Java...
[Java BasisPoint] Path: D:/Finance Project/basisPoint-main/basisPoint-main
[Java BasisPoint] Java is installed
[Java BasisPoint] Forecast date: 06/01/2026
[Java BasisPoint] Custom discount curve: Treasury Yield Curve
[Java BasisPoint] Input file created
[Java BasisPoint] Executing: cd "D:/Finance Project/basisPoint-main/basisPoint-main" && java -cp ".;gson-2.11.0.jar" RunFromFile
[Java BasisPoint] ✅ Execution successful
```

---

## Test Case 4: Municipal Bond with Both Custom Curves

### Input Data:
```
Security Description: California Muni 3.0% 2027
Security ID: CAMUNI2027
Sector: Muni
Issuer: State of California
Coupon: 3.0
Maturity Date: 06/01/2027
Par Amount: 150000
Price: 97.50
Settle Date: 02/17/2026

FORECAST SECTION:
Forecast Date: 08/15/2026
Forecast Discount Curve: Other... (Custom)
Forecast Fixing Curve: Other... (Custom)
```

### Custom Discount Curve:
```
Curve Name: Muni Discount Curve
Curve Date: 02/17/2026
Spot Curve: Yes

Points:
1. Tenor: 6m,  Rate: 3.50, StdDev: 0.05
2. Tenor: 1yr, Rate: 3.45, StdDev: 0.08
3. Tenor: 2yr, Rate: 3.40, StdDev: 0.10
4. Tenor: 3yr, Rate: 3.35, StdDev: 0.12
5. Tenor: 5yr, Rate: 3.30, StdDev: 0.15
```

### Custom Fixing Curve:
```
Curve Name: Muni Fixing Curve
Curve Date: 02/17/2026
Spot Curve: No

Points:
1. Tenor: 6m,  Rate: 3.55, StdDev: 0.06
2. Tenor: 1yr, Rate: 3.50, StdDev: 0.09
3. Tenor: 2yr, Rate: 3.45, StdDev: 0.11
4. Tenor: 3yr, Rate: 3.40, StdDev: 0.13
5. Tenor: 5yr, Rate: 3.35, StdDev: 0.16
```

### Expected Backend Logs:
```
[Java BasisPoint] Attempting to call Java...
[Java BasisPoint] Path: D:/Finance Project/basisPoint-main/basisPoint-main
[Java BasisPoint] Java is installed
[Java BasisPoint] Forecast date: 08/15/2026
[Java BasisPoint] Custom discount curve: Muni Discount Curve
[Java BasisPoint] Custom fixing curve: Muni Fixing Curve
[Java BasisPoint] Input file created
[Java BasisPoint] Executing: cd "D:/Finance Project/basisPoint-main/basisPoint-main" && java -cp ".;gson-2.11.0.jar" RunFromFile
[Java BasisPoint] ✅ Execution successful
```

---

## Quick Test Data (Copy-Paste Ready)

### Minimal Test:
```
Coupon: 3.5
Maturity: 08/03/2028
Par: 100000
Price: 98.125
```

### With Forecast:
```
Coupon: 4.25
Maturity: 12/15/2030
Par: 250000
Price: 102.50
Forecast Date: 09/01/2026
Forecast Discount Curve: SOFR
```

---

## How to Verify Java is Being Used

### Method 1: Check Backend Console
Look for these specific logs:
- ✅ `[Java BasisPoint] Attempting to call Java...`
- ✅ `[Java BasisPoint] ✅ Execution successful`
- ❌ `Java BasisPoint not available, using fallback calculations`

### Method 2: Check Calculation Accuracy
**Java Results** will have:
- Precise yield calculations
- Detailed cashflow schedules
- Accurate key rate durations
- Professional-grade analytics

**Fallback Results** will have:
- Approximate calculations
- Simplified cashflows
- Mock key rate durations (many zeros)

### Method 3: Check input.json File
After calculation, check if this file exists:
```
D:\Finance Project\basisPoint-main\basisPoint-main\input.json
```

If Java is working, this file will be created and then deleted.

### Method 4: Test Java Manually
Run this in PowerShell:
```powershell
cd D:\Finance Project\basisPoint-main\basisPoint-main

# Create test input
echo '{"DESCR":"Test","CPNRT":3.5,"DTMATURITY":"08/03/2028","DTSETTLE":"02/17/2026","FACEAMT":100000,"SETTLEPRC":98.125,"INTPERIODICITY":2,"INSTYPE":"FIXEDRATE","CALCREQUEST":[1,2,3,4,5,6,7,8,9,10]}' > input.json

# Run Java
java -cp ".;gson-2.11.0.jar" RunFromFile

# Check if it worked (should output JSON)
```

If this works, Java BasisPoint is functional.

---

## Expected JSON Sent to Java

### Basic Calculation:
```json
{
  "DESCR": "Apple Inc 3.5% 2028",
  "CUSIP": "",
  "CPNRT": 3.5,
  "DTMATURITY": "08/03/2028",
  "DTSETTLE": "02/17/2026",
  "FACEAMT": 100000,
  "SETTLEPRC": 98.125,
  "INTPERIODICITY": 2,
  "INSTYPE": "FIXEDRATE",
  "CALCREQUEST": [1,2,3,4,5,6,7,8,9,10],
  "ISSUER": "Apple Inc",
  "SECTOR": "Corp"
}
```

### With Forecast:
```json
{
  "DESCR": "Microsoft Corp 4.25% 2030",
  "CUSIP": "",
  "CPNRT": 4.25,
  "DTMATURITY": "12/15/2030",
  "DTSETTLE": "02/17/2026",
  "FACEAMT": 250000,
  "SETTLEPRC": 102.50,
  "INTPERIODICITY": 2,
  "INSTYPE": "FIXEDRATE",
  "CALCREQUEST": [1,2,3,4,5,6,7,8,9,10],
  "ISSUER": "Microsoft Corporation",
  "SECTOR": "Corp",
  "FORECASTDATE": "09/01/2026",
  "FORECASTDISCOUNTCURVE": "SOFR",
  "FORECASTFIXINGCURVE": "Forward"
}
```

### With Custom Curve:
```json
{
  "DESCR": "US Treasury 2.875% 2029",
  "CPNRT": 2.875,
  "DTMATURITY": "05/15/2029",
  "DTSETTLE": "02/17/2026",
  "FACEAMT": 500000,
  "SETTLEPRC": 99.75,
  "INTPERIODICITY": 2,
  "INSTYPE": "FIXEDRATE",
  "CALCREQUEST": [1,2,3,4,5,6,7,8,9,10],
  "ISSUER": "US Treasury",
  "SECTOR": "Govt",
  "FORECASTDATE": "06/01/2026",
  "CUSTOMDISCOUNTCURVE": {
    "NAME": "Treasury Yield Curve",
    "DATE": "02/17/2026",
    "SPOTCURVE": true,
    "POINTS": [
      {"TENOR": "3m", "RATE": 5.25, "STDDEV": 0.05},
      {"TENOR": "6m", "RATE": 5.15, "STDDEV": 0.08},
      {"TENOR": "1yr", "RATE": 4.95, "STDDEV": 0.10},
      {"TENOR": "2yr", "RATE": 4.50, "STDDEV": 0.12},
      {"TENOR": "3yr", "RATE": 4.25, "STDDEV": 0.15},
      {"TENOR": "5yr", "RATE": 4.00, "STDDEV": 0.18},
      {"TENOR": "7yr", "RATE": 3.85, "STDDEV": 0.20},
      {"TENOR": "10yr", "RATE": 3.75, "STDDEV": 0.22},
      {"TENOR": "20yr", "RATE": 3.90, "STDDEV": 0.25},
      {"TENOR": "30yr", "RATE": 4.00, "STDDEV": 0.28}
    ]
  }
}
```

---

## Troubleshooting

### If you see "using fallback calculations":
1. Check the error message in backend console
2. Verify Java path: `D:\Finance Project\basisPoint-main\basisPoint-main`
3. Test Java manually (see Method 4 above)
4. Check file permissions on basisPoint folder

### If calculations seem wrong:
- Compare with expected professional results
- Check if key rate durations are all zeros (indicates fallback)
- Look for detailed cashflow schedules (Java provides more detail)

---

## Summary

Use **Test Case 1** for basic verification, then **Test Case 2** for forecast testing. Watch the backend console carefully - you should see the `[Java BasisPoint]` logs confirming Java execution.

**The key indicator**: If you see `✅ Execution successful` in the logs, Java is working!

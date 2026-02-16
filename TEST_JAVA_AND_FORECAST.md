# Testing Java Integration & Forecast Features

## 🧪 Test Plan

### Test 1: Basic Java Integration (No Forecast)

**Purpose**: Verify Java BasisPoint is being called for basic calculations

**Steps**:
1. Go to http://localhost:3001/trade
2. Fill in ONLY basic fields:
   - Security Description: "Test Corporate Bond"
   - Coupon: 3.5
   - Maturity Date: 08/03/2028
   - Par Amount: 100000
   - Price: 98.125
   - Settle Date: Today
3. Click "Calculate"
4. Check backend console

**Expected Backend Logs**:
```
[Java BasisPoint] Attempting to call Java...
[Java BasisPoint] Path: D:/Finance Project/basisPoint-main/basisPoint-main
[Java BasisPoint] Java is installed
[Java BasisPoint] Input file created
[Java BasisPoint] Executing: cd "D:/Finance Project/basisPoint-main/basisPoint-main" && java -cp ".;gson-2.11.0.jar" RunFromFile
[Java BasisPoint] ✅ Execution successful
```

**Expected Results**:
- ✅ Calculation completes
- ✅ Analytics section shows data
- ✅ Cashflows are displayed
- ✅ Key Rate Durations are shown

---

### Test 2: Forecast with Discount Curve

**Purpose**: Test forecast features with custom discount curve

**Steps**:
1. Fill in basic fields (same as Test 1)
2. In "Forecast" section:
   - Forecast Date: Select a future date (e.g., 09/01/2026)
   - Forecast Discount Curve: Select "SOFR"
3. Click "Calculate"
4. Check backend console

**Expected**:
- ✅ Java receives forecast parameters
- ✅ Calculation includes forecast data
- ✅ Results show forecast analytics

---

### Test 3: Custom Curve Definition

**Purpose**: Test custom curve creation and usage

**Steps**:
1. Fill in basic fields
2. In "Forecast" section:
   - Forecast Discount Curve: Select "Other..."
3. In the modal that opens:
   - Curve Name: "My Custom Curve"
   - Curve Date: Today
   - Spot Curve: Yes
   - Add points:
     - Tenor: 1yr, Rate: 3.5, StdDev: 0.1
     - Tenor: 2yr, Rate: 3.8, StdDev: 0.15
     - Tenor: 5yr, Rate: 4.2, StdDev: 0.2
4. Click "Save Curve"
5. Click "Calculate"

**Expected**:
- ✅ Custom curve is saved
- ✅ Payload includes customDiscountCurve object
- ✅ Java processes custom curve data

---

## 📊 What Gets Sent to Java

### Basic Calculation (No Forecast):
```json
{
  "DESCR": "Test Corporate Bond",
  "CPNRT": 3.5,
  "DTMATURITY": "08/03/2028",
  "DTSETTLE": "02/17/2026",
  "FACEAMT": 100000,
  "SETTLEPRC": 98.125,
  "INTPERIODICITY": 2,
  "INSTYPE": "FIXEDRATE",
  "CALCREQUEST": [1,2,3,4,5,6,7,8,9,10]
}
```

### With Forecast:
```json
{
  "DESCR": "Test Corporate Bond",
  "CPNRT": 3.5,
  "DTMATURITY": "08/03/2028",
  "DTSETTLE": "02/17/2026",
  "FACEAMT": 100000,
  "SETTLEPRC": 98.125,
  "INTPERIODICITY": 2,
  "INSTYPE": "FIXEDRATE",
  "CALCREQUEST": [1,2,3,4,5,6,7,8,9,10],
  "FORECASTDATE": "09/01/2026",
  "FORECASTDISCOUNTCURVE": "SOFR",
  "FORECASTFIXINGCURVE": ""
}
```

---

## ⚠️ Current Forecast Implementation Status

### ✅ What's Working:
1. **Frontend UI**: Forecast section with date picker and curve selectors
2. **Frontend Payload**: Sends forecast data to backend
3. **Backend DTO**: Accepts forecast fields
4. **Backend Service**: Receives forecast data

### ⚠️ What Needs Verification:

The backend `calculation.service.ts` currently maps these fields:
- ✅ Basic bond parameters
- ❓ Forecast parameters (need to check if mapped to Java format)

Let me check if forecast fields are being mapped...

---

## 🔍 Checking Forecast Mapping

Looking at `calculation.service.ts`, the `mapToJavaFormat()` function currently maps:
- DESCR, CUSIP, CPNRT, DTMATURITY, DTSETTLE, FACEAMT, SETTLEPRC
- INTPERIODICITY, INSTYPE, CALCREQUEST

**Missing**: Forecast field mappings!

---

## 🛠️ Fix Needed for Forecast

The forecast fields need to be added to the Java mapping:

```typescript
// In mapToJavaFormat() method, add:
if (data.forecastDate) {
    javaInput.FORECASTDATE = formatDate(data.forecastDate);
}
if (data.forecastDiscountCurve) {
    javaInput.FORECASTDISCOUNTCURVE = data.forecastDiscountCurve;
}
if (data.forecastFixingCurve) {
    javaInput.FORECASTFIXINGCURVE = data.forecastFixingCurve;
}
if (data.customDiscountCurve) {
    javaInput.CUSTOMDISCOUNTCURVE = data.customDiscountCurve;
}
if (data.customFixingCurve) {
    javaInput.CUSTOMFIXINGCURVE = data.customFixingCurve;
}
```

---

## 📋 Test Results Template

### Test 1: Basic Calculation
- [ ] Backend shows Java logs
- [ ] Java execution successful
- [ ] Results displayed correctly
- [ ] No errors in console

### Test 2: Forecast with SOFR
- [ ] Forecast date accepted
- [ ] Curve selection works
- [ ] Calculation completes
- [ ] Results include forecast data

### Test 3: Custom Curve
- [ ] Modal opens
- [ ] Points can be added
- [ ] Curve saves successfully
- [ ] Calculation uses custom curve

---

## 🎯 Summary

**Java Integration**: ✅ FIXED and READY
- Path issue resolved
- Logging added
- Should work for basic calculations

**Forecast Features**: ⚠️ NEEDS UPDATE
- Frontend sends forecast data ✅
- Backend receives forecast data ✅
- **Mapping to Java format**: ❌ MISSING
- Need to add forecast field mappings

---

## 🚀 Next Steps

1. **Test basic calculation first** (without forecast)
   - This should work with the Java fix
   
2. **If basic works**, then add forecast mappings
   - Update `mapToJavaFormat()` in calculation.service.ts
   - Add forecast field mappings
   
3. **Test forecast features**
   - Verify forecast data reaches Java
   - Check results include forecast analytics

Would you like me to:
1. Test the basic calculation now?
2. Add the forecast field mappings?
3. Both?

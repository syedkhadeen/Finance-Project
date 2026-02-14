# Portfolio API Testing Guide

## Prerequisites
- Backend server running on port 3002
- PostgreSQL database running
- JWT token from authentication

## Step 1: Check Server Health

```bash
curl http://localhost:3002/health
```

## Step 2: Sign Up (if needed)

```bash
curl -X POST http://localhost:3002/auth/signup \
  -H "Content-Type: application/json" \
  -d "{\"auth_id\": \"test@example.com\", \"email\": \"test@example.com\", \"firstName\": \"Test\", \"lastName\": \"User\"}"
```

## Step 3: Login to Get Token

```bash
curl -X POST http://localhost:3002/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\": \"test@example.com\", \"password\": \"Test123!\"}"
```

Save the `id_token` from the response.

## Step 4: Create a Portfolio

Replace `YOUR_TOKEN_HERE` with the token from Step 3:

```bash
curl -X POST http://localhost:3002/portfolios \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d "{\"abbrevName\": \"TECH_PORT\", \"fullName\": \"Technology Portfolio\", \"strategy\": \"Growth\"}"
```

Save the portfolio `id` from the response.

## Step 5: Get All Portfolios

```bash
curl -X GET http://localhost:3002/portfolios \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## Step 6: Add a Holding

Replace `PORTFOLIO_ID` with the ID from Step 4:

```bash
curl -X POST http://localhost:3002/portfolios/PORTFOLIO_ID/holdings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d "{\"isin\": \"US0378331005\", \"amount\": 100, \"price\": 150.50, \"holdDate\": \"2026-02-10\"}"
```

## Step 7: Bulk Add Holdings

```bash
curl -X POST http://localhost:3002/portfolios/PORTFOLIO_ID/holdings/bulk \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d "{\"holdings\": [{\"isin\": \"US5949181045\", \"amount\": 50, \"price\": 200.00, \"holdDate\": \"2026-02-10\"}, {\"isin\": \"US88160R1014\", \"amount\": 75, \"price\": 180.00, \"holdDate\": \"2026-02-10\"}]}"
```

## Step 8: Get Holdings

```bash
curl -X GET http://localhost:3002/portfolios/PORTFOLIO_ID/holdings \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## Step 9: Create Market Bonds

```bash
curl -X POST http://localhost:3002/market-bonds \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d "{\"isin\": \"US912828XG93\", \"price\": 98.5, \"exchange\": \"NYSE\", \"date\": \"2026-02-10\"}"
```

## Step 10: Bulk Create Market Bonds

```bash
curl -X POST http://localhost:3002/market-bonds/bulk \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d "{\"bonds\": [{\"isin\": \"US283695BH69\", \"price\": 99.0, \"exchange\": \"NASDAQ\", \"date\": \"2026-02-10\"}, {\"isin\": \"US912828YK09\", \"price\": 97.5, \"exchange\": \"NYSE\", \"date\": \"2026-02-10\"}]}"
```

## Step 11: Get All Market Bonds

```bash
curl -X GET http://localhost:3002/market-bonds \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## PowerShell Commands (Windows)

If using PowerShell, use these commands instead:

### Login
```powershell
$response = Invoke-RestMethod -Uri "http://localhost:3002/auth/login" -Method POST -ContentType "application/json" -Body '{"email": "test@example.com", "password": "Test123!"}'
$token = $response.data.id_token
```

### Create Portfolio
```powershell
$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}
$body = '{"abbrevName": "TECH_PORT", "fullName": "Technology Portfolio", "strategy": "Growth"}'
$portfolio = Invoke-RestMethod -Uri "http://localhost:3002/portfolios" -Method POST -Headers $headers -Body $body
$portfolioId = $portfolio.id
```

### Get Portfolios
```powershell
Invoke-RestMethod -Uri "http://localhost:3002/portfolios" -Method GET -Headers $headers
```

### Add Holding
```powershell
$body = '{"isin": "US0378331005", "amount": 100, "price": 150.50, "holdDate": "2026-02-10"}'
Invoke-RestMethod -Uri "http://localhost:3002/portfolios/$portfolioId/holdings" -Method POST -Headers $headers -Body $body
```

### Get Holdings
```powershell
Invoke-RestMethod -Uri "http://localhost:3002/portfolios/$portfolioId/holdings" -Method GET -Headers $headers
```

## Expected Responses

### Successful Portfolio Creation
```json
{
  "id": "clx1234567890",
  "userId": "clx0987654321",
  "abbrevName": "TECH_PORT",
  "fullName": "Technology Portfolio",
  "strategy": "Growth",
  "createdAt": "2026-02-10T12:00:00.000Z",
  "updatedAt": "2026-02-10T12:00:00.000Z",
  "holdings": []
}
```

### Successful Holding Creation
```json
{
  "id": "clx1111111111",
  "portfolioId": "clx1234567890",
  "isin": "US0378331005",
  "amount": 100,
  "price": 150.5,
  "holdDate": "2026-02-10T00:00:00.000Z",
  "createdAt": "2026-02-10T12:00:00.000Z",
  "updatedAt": "2026-02-10T12:00:00.000Z"
}
```

## Troubleshooting

### 401 Unauthorized
- Check that your token is valid and not expired
- Ensure the Authorization header format is correct: `Bearer <token>`

### 404 Not Found
- Verify the portfolio ID is correct
- Check that the endpoint URL is correct

### 409 Conflict
- Portfolio with the same abbreviation already exists
- Try a different abbreviation name

### 500 Internal Server Error
- Check server logs for detailed error messages
- Verify database connection is working
- Ensure all migrations have been applied

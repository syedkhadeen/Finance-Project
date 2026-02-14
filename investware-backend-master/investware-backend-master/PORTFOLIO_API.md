# Portfolio Management API

This document describes the Portfolio Management API endpoints for managing user portfolios, holdings, and market bonds.

## Authentication

All endpoints require authentication via JWT token in the Authorization header:
```
Authorization: Bearer <token>
```

## Endpoints

### Portfolio Management

#### Create Portfolio
```http
POST /portfolios
Content-Type: application/json

{
  "abbrevName": "TECH_PORT",
  "fullName": "Technology Portfolio",
  "strategy": "Growth"
}
```

#### Get All Portfolios
```http
GET /portfolios
```

#### Get Portfolio by ID
```http
GET /portfolios/:portfolioId
```

#### Update Portfolio
```http
PUT /portfolios/:portfolioId
Content-Type: application/json

{
  "fullName": "Updated Technology Portfolio",
  "strategy": "Balanced Growth"
}
```

#### Delete Portfolio
```http
DELETE /portfolios/:portfolioId
```

### Holdings Management

#### Add Holding to Portfolio
```http
POST /portfolios/:portfolioId/holdings
Content-Type: application/json

{
  "isin": "US0378331005",
  "amount": 100,
  "price": 150.50,
  "holdDate": "2026-02-10"
}
```

#### Bulk Add Holdings
```http
POST /portfolios/:portfolioId/holdings/bulk
Content-Type: application/json

{
  "holdings": [
    {
      "isin": "US0378331005",
      "amount": 100,
      "price": 150.50,
      "holdDate": "2026-02-10"
    },
    {
      "isin": "US5949181045",
      "amount": 50,
      "price": 200.00,
      "holdDate": "2026-02-10"
    }
  ]
}
```

#### Get All Holdings for Portfolio
```http
GET /portfolios/:portfolioId/holdings
```

#### Update Holding
```http
PUT /portfolios/:portfolioId/holdings/:holdingId
Content-Type: application/json

{
  "amount": 150,
  "price": 155.00
}
```

#### Delete Holding
```http
DELETE /portfolios/:portfolioId/holdings/:holdingId
```

### Market Bonds Management

#### Create Market Bond
```http
POST /market-bonds
Content-Type: application/json

{
  "isin": "US912828XG93",
  "price": 98.5,
  "exchange": "NYSE",
  "date": "2026-02-10"
}
```

#### Bulk Create Market Bonds
```http
POST /market-bonds/bulk
Content-Type: application/json

{
  "bonds": [
    {
      "isin": "US912828XG93",
      "price": 98.5,
      "exchange": "NYSE",
      "date": "2026-02-10"
    },
    {
      "isin": "US283695BH69",
      "price": 99.0,
      "exchange": "NASDAQ",
      "date": "2026-02-10"
    }
  ]
}
```

#### Get All Market Bonds
```http
GET /market-bonds
```

#### Get Market Bond by ID
```http
GET /market-bonds/:bondId
```

#### Update Market Bond
```http
PUT /market-bonds/:bondId
Content-Type: application/json

{
  "price": 99.5,
  "exchange": "NASDAQ"
}
```

#### Delete Market Bond
```http
DELETE /market-bonds/:bondId
```

#### Delete All Market Bonds
```http
DELETE /market-bonds
```

## Data Models

### Portfolio
```typescript
{
  id: string;
  userId: string;
  abbrevName: string;
  fullName: string;
  strategy?: string;
  createdAt: Date;
  updatedAt: Date;
}
```

### Holding
```typescript
{
  id: string;
  portfolioId: string;
  isin: string;
  amount: number;
  price: number;
  holdDate: Date;
  createdAt: Date;
  updatedAt: Date;
}
```

### Market Bond
```typescript
{
  id: string;
  userId: string;
  isin: string;
  price: number;
  exchange?: string;
  date: Date;
  createdAt: Date;
  updatedAt: Date;
}
```

## Error Responses

All endpoints return standard HTTP status codes:

- `200 OK` - Successful request
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Missing or invalid authentication
- `404 Not Found` - Resource not found
- `409 Conflict` - Resource already exists
- `500 Internal Server Error` - Server error

Error response format:
```json
{
  "statusCode": 404,
  "message": "Portfolio not found"
}
```

## CSV Import Format

### Portfolio Holdings CSV
```csv
isin,amount,price,holdDate
US0378331005,100,150.50,2026-02-10
US5949181045,50,200.00,2026-02-10
```

### Market Bonds CSV
```csv
isin,price,exchange,date
US912828XG93,98.5,NYSE,2026-02-10
US283695BH69,99.0,NASDAQ,2026-02-10
```

## Usage Example

1. **Create a portfolio**
2. **Add holdings to the portfolio** (single or bulk)
3. **Create market bonds** for available bonds in the market
4. **Use the portfolio calculator** to perform swap analysis between portfolio holdings and market bonds
5. **Call BasisPoint API** with the formatted data to get calculation results

## Next Steps

- Implement CSV import/export functionality
- Add portfolio analytics endpoints
- Integrate with BasisPoint calculation engine
- Add swap analysis functionality

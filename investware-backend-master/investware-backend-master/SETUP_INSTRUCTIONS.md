# Portfolio Feature Setup Instructions

## Prerequisites

- Node.js installed
- PostgreSQL database running
- Environment variables configured in `.env.development.local`

## Setup Steps

### 1. Install Dependencies

```bash
cd investware-backend-master/investware-backend-master
npm install
```

### 2. Generate Prisma Client

```bash
npm run prisma:generate:dev
```

### 3. Create Database Migration

```bash
npm run prisma:create:migration
```

When prompted, name the migration: `add_portfolio_tables`

### 4. Apply Migration

```bash
npm run prisma:migrate:dev
```

### 5. Build the Application

```bash
npm run build
```

### 6. Start Development Server

```bash
npm run dev
```

The server will start on the port specified in your environment variables (default: 3000).

## Verify Installation

### Test Authentication

1. **Sign up a user** (if not already done):
```bash
curl -X POST http://localhost:3000/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com", "password": "Test123!"}'
```

2. **Login to get token**:
```bash
curl -X POST http://localhost:3000/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com", "password": "Test123!"}'
```

Save the `id_token` from the response.

### Test Portfolio Endpoints

3. **Create a portfolio**:
```bash
curl -X POST http://localhost:3000/portfolios \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "abbrevName": "TEST_PORT",
    "fullName": "Test Portfolio",
    "strategy": "Growth"
  }'
```

4. **Get all portfolios**:
```bash
curl -X GET http://localhost:3000/portfolios \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

5. **Add a holding**:
```bash
curl -X POST http://localhost:3000/portfolios/PORTFOLIO_ID/holdings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "isin": "US0378331005",
    "amount": 100,
    "price": 150.50,
    "holdDate": "2026-02-10"
  }'
```

6. **Create a market bond**:
```bash
curl -X POST http://localhost:3000/market-bonds \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "isin": "US912828XG93",
    "price": 98.5,
    "exchange": "NYSE",
    "date": "2026-02-10"
  }'
```

## API Documentation

Once the server is running, you can access the Swagger API documentation at:

```
http://localhost:3000/api-docs
```

## Database Schema

The following tables have been added:

### portfolios
- `id` (UUID, Primary Key)
- `userId` (String, Foreign Key to Users)
- `abbrevName` (String)
- `fullName` (String)
- `strategy` (String, Optional)
- `createdAt` (DateTime)
- `updatedAt` (DateTime)

### holdings
- `id` (UUID, Primary Key)
- `portfolioId` (String, Foreign Key to Portfolio)
- `isin` (String)
- `amount` (Float)
- `price` (Float)
- `holdDate` (DateTime)
- `createdAt` (DateTime)
- `updatedAt` (DateTime)

### market_bonds
- `id` (UUID, Primary Key)
- `userId` (String, Foreign Key to Users)
- `isin` (String)
- `price` (Float)
- `exchange` (String, Optional)
- `date` (DateTime)
- `createdAt` (DateTime)
- `updatedAt` (DateTime)

## Troubleshooting

### Migration Errors

If you encounter migration errors:

1. Reset the database:
```bash
npm run prisma:migrate:dev -- --name reset
```

2. Or manually reset:
```bash
npx prisma migrate reset
```

### Authentication Issues

- Ensure your JWT secret is properly configured in environment variables
- Check that the Authorization header format is correct: `Bearer <token>`
- Verify the token hasn't expired

### Database Connection Issues

- Verify DATABASE_URL in `.env.development.local`
- Ensure PostgreSQL is running
- Check database credentials

## Environment Variables

Required environment variables in `.env.development.local`:

```env
DATABASE_URL="postgresql://user:password@localhost:5432/investware_db"
JWT_SECRET="your-secret-key"
PORT=3000
NODE_ENV=development
```

## Next Steps

1. Implement CSV import/export functionality for portfolios and holdings
2. Add portfolio analytics and reporting
3. Integrate with BasisPoint calculation engine
4. Implement swap analysis between portfolio and market bonds
5. Add data validation and business rules
6. Implement audit logging for portfolio changes

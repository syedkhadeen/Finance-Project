# Investware Bond Calculator

A comprehensive bond trading and calculation platform with Java BasisPoint integration.

## Features

- **Trade Screen** - Complete bond trading interface with all required fields
- **Security Search** - Advanced search with filters (Sector, Coupon, Maturity)
- **Bond Calculations** - Yield, Duration, Convexity, Cashflows, Key Rate Duration
- **Java BasisPoint Integration** - Professional-grade calculation engine
- **Real-time Results** - Analytics, Risk metrics, Projections, Cashflows

## 📁 Project Structure

```
.
├── investware-backend-master/     # Backend API (Node.js + TypeScript)
├── investware-frontend-master/    # Frontend UI (Next.js + React)
├── basisPoint-main/               # Java BasisPoint calculation engine
└── investware-infrastructure-master/ # Infrastructure configuration
```

## 🛠️ Prerequisites

- **Node.js** 16+ and npm
- **Java** 11+ (for BasisPoint calculations)
- **Git**

## ⚡ Quick Start

### 1. Clone Repository

```bash
git clone https://github.com/syedkhadeen/Finance-Project.git
cd Finance-Project
```

### 2. Backend Setup

```bash
cd investware-backend-master/investware-backend-master
npm install
cp .env.example .env
# Edit .env with your configuration
npm run dev
```

Backend runs on: **http://localhost:3002**

### 3. Frontend Setup

```bash
cd investware-frontend-master/investware-frontend-master
npm install
cp .env.example .env.local
# Edit .env.local with your configuration
npm run dev
```

Frontend runs on: **http://localhost:3001**

### 4. Java BasisPoint (Optional)

```bash
cd basisPoint-main/basisPoint-main
javac -cp ".;gson-2.11.0.jar" *.java
```

## 📊 Usage

### Trade Screen

1. Open **http://localhost:3001**
2. Navigate to **Trade** screen
3. Fill in bond details:
   - Security Type: `Corporate`
   - Security ID: `TEST123`
   - Coupon: `3.5`
   - Maturity: `08/03/2028`
   - Par Amount: `100000`
   - Price: `98.125`
   - Settle Date: `02/05/2026`
4. Click **Calculate**
5. View results in sections below

### Search Securities

1. Click **Search** button on Trade screen
2. Enter search criteria:
   - Sector: `Corp`, `Govt`, or `Muni`
   - Coupon Range: Low/High
   - Maturity Range: Low/High
3. Click **Search**
4. Select security from results
5. Trade form auto-populates

## 🔧 API Endpoints

### Calculate Bond
```bash
POST /v1/api/trade/calculate
Content-Type: application/json

{
  "securityType": "Corporate",
  "securityId": "TEST123",
  "coupon": 3.5,
  "maturityDate": "2028-08-03",
  "settleDate": "2026-02-03",
  "par": 100000,
  "price": 98.125
}
```

### Search Securities
```bash
POST /v1/api/trade/search
Content-Type: application/json

{
  "sector": "Corp",
  "couponLow": 3.0,
  "couponHigh": 4.0,
  "maturityFrom": "2027-01-01",
  "maturityTo": "2029-12-31"
}
```

## 📈 Calculation Results

The system calculates:

### Analytics
- Principal, Accrued Interest
- Price, Yield to Maturity, Yield to Worst
- Modified Duration, Convexity
- Net Payment, Maturity Years
- OAS Spread, OAS Duration, OAS Convexity
- Fair Value, Z-Spread

### Cashflow Schedule
- Payment dates and amounts
- Principal and interest breakdown
- Tax calculations

### Key Rate Duration
- Duration by tenor (1m to 50yr)

## 🔐 Environment Variables

### Backend (.env)
```env
NODE_ENV=development
PORT=3002
DATABASE_URL=your_database_url
JWT_SECRET=your_jwt_secret
```

### Frontend (.env.local)
```env
NEXT_PUBLIC_API_URL=http://localhost:3002
```

## 🏗️ Technology Stack

### Backend
- **Node.js** + **TypeScript**
- **Express.js** - Web framework
- **TypeDI** - Dependency injection
- **class-validator** - DTO validation
- **Java BasisPoint** - Calculation engine

### Frontend
- **Next.js 14** - React framework
- **TypeScript** - Type safety
- **Tailwind CSS** - Styling
- **Shadcn/ui** - UI components

### Java BasisPoint
- **Java 11+**
- **Gson** - JSON processing
- Professional bond calculation algorithms

## 🧪 Testing

### Test Backend API
```bash
cd investware-backend-master/investware-backend-master
npm test
```

### Test Calculations
```bash
node test-calculate.js
```

### Test Search
```bash
node test-search.js
```

## 📦 Building for Production

### Backend
```bash
cd investware-backend-master/investware-backend-master
npm run build
npm start
```

### Frontend
```bash
cd investware-frontend-master/investware-frontend-master
npm run build
npm start
```

## 🔄 Java BasisPoint Integration

The system integrates with Java BasisPoint for accurate bond calculations:

1. **Automatic Detection** - Checks if Java is available
2. **Graceful Fallback** - Uses TypeScript calculations if Java unavailable
3. **File-based Communication** - JSON input/output via files
4. **Error Handling** - Comprehensive error logging

### How It Works
```
Frontend → Backend API → Java BasisPoint → Results
                ↓
         (If Java fails)
                ↓
         TypeScript Fallback
```

## 🐛 Troubleshooting

### Backend won't start
- Check Node.js version: `node -v` (should be 16+)
- Verify .env file exists
- Check port 3002 is available

### Frontend won't start
- Check Node.js version
- Verify .env.local file exists
- Check port 3001 is available

### Calculations not working
- Check backend logs for errors
- Verify Java is installed: `java -version`
- System will use fallback calculations automatically

### Java BasisPoint errors
- Ensure Java 11+ is installed
- Compile Java files: `javac -cp ".;gson-2.11.0.jar" *.java`
- Check gson-2.11.0.jar exists in basisPoint-main folder

## 📝 Field Mappings

| Frontend Field | Backend DTO | Java BasisPoint |
|---------------|-------------|-----------------|
| Security Description | securityDescription | DESCR |
| Coupon | coupon | CPNRT |
| Maturity Date | maturityDate | DTMATURITY |
| Settle Date | settleDate | DTSETTLE |
| Par Amount | par | FACEAMT |
| Price | price | SETTLEPRC |

## 🤝 Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature-name`
3. Commit changes: `git commit -m 'Add feature'`
4. Push to branch: `git push origin feature-name`
5. Submit pull request

## 📄 License

This project is proprietary software.

## 👥 Team

- **Developer**: Syed Khadeen
- **Repository**: https://github.com/syedkhadeen/Finance-Project

## 📞 Support

For issues or questions:
- Create an issue on GitHub
- Check existing documentation
- Review backend logs for errors

## 🎯 Roadmap

- [ ] Add more bond types (Convertible, Callable)
- [ ] Implement portfolio analysis
- [ ] Add historical data charts
- [ ] Export results to Excel
- [ ] Real-time market data integration
- [ ] User authentication and authorization

## ⚙️ Configuration

### Backend Configuration
Located in `investware-backend-master/investware-backend-master/src/config/`

### Frontend Configuration
Located in `investware-frontend-master/investware-frontend-master/next.config.js`

### Java BasisPoint Configuration
Located in `basisPoint-main/basisPoint-main/`

## 🔒 Security

- Environment variables for sensitive data
- .gitignore configured to exclude secrets
- Input validation on all endpoints
- CORS configured for frontend access

## 📊 Performance

- Backend response time: ~500ms-1s
- Java BasisPoint: ~1-2s (first call includes compilation)
- Frontend rendering: <100ms
- Supports concurrent calculations

## 🌐 Deployment

### Heroku
```bash
# Backend
cd investware-backend-master/investware-backend-master
heroku create your-app-name-backend
git push heroku main

# Frontend
cd investware-frontend-master/investware-frontend-master
vercel deploy
```

### Docker
```bash
# Build images
docker-compose build

# Run containers
docker-compose up
```

## 📚 Documentation

- API documentation available at `/api-docs` when backend is running
- Frontend component documentation in Storybook
- Java BasisPoint documentation in `basisPoint-main/docs/`

---

**Last Updated**: February 5, 2026  
**Version**: 1.0.0  
**Status**: Production Ready ✅

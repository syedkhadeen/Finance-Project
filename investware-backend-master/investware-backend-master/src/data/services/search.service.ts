import { Service } from 'typedi';
import { SearchCriteriaDTO } from '@data/dtos/search/searchCriteria.dto';

interface SecurityResult {
  selected: boolean;
  security: string;
  sector: string;
  par: number;
  price: number;
  accruedInterest: number;
  yieldToMaturity: number;
  yieldToWorst: number;
  marketValue: number;
  maturityLength: number;
  duration: number;
  convexity: number;
  fairValue: number;
  zSpread: number;
  forecastReturn: number;
  forecastUpSide: number;
  forecastDownSide: number;
}

@Service()
export class SearchService {
  // Mock data for securities
  private mockSecurities: SecurityResult[] = [
    {
      selected: false,
      security: 'Air Products Corp',
      sector: 'Corp',
      par: 100000,
      price: 98.25,
      accruedInterest: 33456.09,
      yieldToMaturity: 4.780,
      yieldToWorst: 4.320,
      marketValue: 98250,
      maturityLength: 5.23,
      duration: 3.46,
      convexity: 1.24,
      fairValue: 9825000,
      zSpread: 2.71,
      forecastReturn: 3.51,
      forecastUpSide: 0.48,
      forecastDownSide: -1.12
    },
    {
      selected: false,
      security: 'IBM 3.5% Corp',
      sector: 'Corp',
      par: 100000,
      price: 101.5,
      accruedInterest: 35956.09,
      yieldToMaturity: 2.220,
      yieldToWorst: 2.220,
      marketValue: 101500,
      maturityLength: 4.30,
      duration: 2.09,
      convexity: 2.36,
      fairValue: 10150000,
      zSpread: 1.45,
      forecastReturn: 3.76,
      forecastUpSide: 0.43,
      forecastDownSide: -0.92
    },
    {
      selected: false,
      security: 'Proctor & Muni',
      sector: 'Muni',
      par: 100000,
      price: 99.78,
      accruedInterest: 33457.09,
      yieldToMaturity: 4.100,
      yieldToWorst: 3.770,
      marketValue: 99780,
      maturityLength: 8.15,
      duration: 3.88,
      convexity: 4.76,
      fairValue: 9978000,
      zSpread: 2.11,
      forecastReturn: 4.02,
      forecastUpSide: 0.38,
      forecastDownSide: -0.72
    },
    {
      selected: false,
      security: 'US Treas 3 Govt',
      sector: 'Govt',
      par: 100000,
      price: 98.875,
      accruedInterest: 35957.09,
      yieldToMaturity: 4.100,
      yieldToWorst: 4.100,
      marketValue: 98875,
      maturityLength: 7.96,
      duration: 5.67,
      convexity: 2.45,
      fairValue: 9887500,
      zSpread: 0.03,
      forecastReturn: 4.28,
      forecastUpSide: 0.33,
      forecastDownSide: -0.52
    }
  ];

  async searchSecurities(criteria: SearchCriteriaDTO): Promise<SecurityResult[]> {
    let results = [...this.mockSecurities];

    // Filter by security name
    if (criteria.security) {
      results = results.filter(s => 
        s.security.toLowerCase().includes(criteria.security!.toLowerCase())
      );
    }

    // Filter by sector
    if (criteria.sector) {
      results = results.filter(s => 
        s.sector.toLowerCase() === criteria.sector!.toLowerCase()
      );
    }

    // Filter by par range
    if (criteria.parFrom !== undefined) {
      results = results.filter(s => s.par >= criteria.parFrom!);
    }
    if (criteria.parTo !== undefined) {
      results = results.filter(s => s.par <= criteria.parTo!);
    }

    // Filter by price range
    if (criteria.priceFrom !== undefined) {
      results = results.filter(s => s.price >= criteria.priceFrom!);
    }
    if (criteria.priceTo !== undefined) {
      results = results.filter(s => s.price <= criteria.priceTo!);
    }

    // Filter by accrued interest range
    if (criteria.accruedInterestFrom !== undefined) {
      results = results.filter(s => s.accruedInterest >= criteria.accruedInterestFrom!);
    }
    if (criteria.accruedInterestTo !== undefined) {
      results = results.filter(s => s.accruedInterest <= criteria.accruedInterestTo!);
    }

    // Filter by yield to maturity range
    if (criteria.yieldToMaturityFrom !== undefined) {
      results = results.filter(s => s.yieldToMaturity >= criteria.yieldToMaturityFrom!);
    }
    if (criteria.yieldToMaturityTo !== undefined) {
      results = results.filter(s => s.yieldToMaturity <= criteria.yieldToMaturityTo!);
    }

    // Filter by yield to worst range
    if (criteria.yieldToWorstFrom !== undefined) {
      results = results.filter(s => s.yieldToWorst >= criteria.yieldToWorstFrom!);
    }
    if (criteria.yieldToWorstTo !== undefined) {
      results = results.filter(s => s.yieldToWorst <= criteria.yieldToWorstTo!);
    }

    // Filter by market value range
    if (criteria.marketValueFrom !== undefined) {
      results = results.filter(s => s.marketValue >= criteria.marketValueFrom!);
    }
    if (criteria.marketValueTo !== undefined) {
      results = results.filter(s => s.marketValue <= criteria.marketValueTo!);
    }

    // Filter by maturity length range
    if (criteria.maturityLengthFrom !== undefined) {
      results = results.filter(s => s.maturityLength >= criteria.maturityLengthFrom!);
    }
    if (criteria.maturityLengthTo !== undefined) {
      results = results.filter(s => s.maturityLength <= criteria.maturityLengthTo!);
    }

    // Filter by duration range
    if (criteria.durationFrom !== undefined) {
      results = results.filter(s => s.duration >= criteria.durationFrom!);
    }
    if (criteria.durationTo !== undefined) {
      results = results.filter(s => s.duration <= criteria.durationTo!);
    }

    // Filter by convexity range
    if (criteria.convexityFrom !== undefined) {
      results = results.filter(s => s.convexity >= criteria.convexityFrom!);
    }
    if (criteria.convexityTo !== undefined) {
      results = results.filter(s => s.convexity <= criteria.convexityTo!);
    }

    // Filter by fair value range
    if (criteria.fairValueFrom !== undefined) {
      results = results.filter(s => s.fairValue >= criteria.fairValueFrom!);
    }
    if (criteria.fairValueTo !== undefined) {
      results = results.filter(s => s.fairValue <= criteria.fairValueTo!);
    }

    // Filter by z-spread range
    if (criteria.zSpreadFrom !== undefined) {
      results = results.filter(s => s.zSpread >= criteria.zSpreadFrom!);
    }
    if (criteria.zSpreadTo !== undefined) {
      results = results.filter(s => s.zSpread <= criteria.zSpreadTo!);
    }

    // Filter by forecast return range
    if (criteria.forecastReturnFrom !== undefined) {
      results = results.filter(s => s.forecastReturn >= criteria.forecastReturnFrom!);
    }
    if (criteria.forecastReturnTo !== undefined) {
      results = results.filter(s => s.forecastReturn <= criteria.forecastReturnTo!);
    }

    // Filter by forecast upside range
    if (criteria.forecastUpSideFrom !== undefined) {
      results = results.filter(s => s.forecastUpSide >= criteria.forecastUpSideFrom!);
    }
    if (criteria.forecastUpSideTo !== undefined) {
      results = results.filter(s => s.forecastUpSide <= criteria.forecastUpSideTo!);
    }

    // Filter by forecast downside range
    if (criteria.forecastDownSideFrom !== undefined) {
      results = results.filter(s => s.forecastDownSide >= criteria.forecastDownSideFrom!);
    }
    if (criteria.forecastDownSideTo !== undefined) {
      results = results.filter(s => s.forecastDownSide <= criteria.forecastDownSideTo!);
    }

    return results;
  }
}

import { IsOptional, IsString, IsNumber } from 'class-validator';

export class SearchCriteriaDTO {
  @IsOptional()
  @IsString()
  security?: string;

  @IsOptional()
  @IsString()
  sector?: string;

  @IsOptional()
  @IsNumber()
  parFrom?: number;

  @IsOptional()
  @IsNumber()
  parTo?: number;

  @IsOptional()
  @IsNumber()
  priceFrom?: number;

  @IsOptional()
  @IsNumber()
  priceTo?: number;

  @IsOptional()
  @IsNumber()
  accruedInterestFrom?: number;

  @IsOptional()
  @IsNumber()
  accruedInterestTo?: number;

  @IsOptional()
  @IsNumber()
  yieldToMaturityFrom?: number;

  @IsOptional()
  @IsNumber()
  yieldToMaturityTo?: number;

  @IsOptional()
  @IsNumber()
  yieldToWorstFrom?: number;

  @IsOptional()
  @IsNumber()
  yieldToWorstTo?: number;

  @IsOptional()
  @IsNumber()
  marketValueFrom?: number;

  @IsOptional()
  @IsNumber()
  marketValueTo?: number;

  @IsOptional()
  @IsNumber()
  maturityLengthFrom?: number;

  @IsOptional()
  @IsNumber()
  maturityLengthTo?: number;

  @IsOptional()
  @IsNumber()
  durationFrom?: number;

  @IsOptional()
  @IsNumber()
  durationTo?: number;

  @IsOptional()
  @IsNumber()
  convexityFrom?: number;

  @IsOptional()
  @IsNumber()
  convexityTo?: number;

  @IsOptional()
  @IsNumber()
  fairValueFrom?: number;

  @IsOptional()
  @IsNumber()
  fairValueTo?: number;

  @IsOptional()
  @IsNumber()
  zSpreadFrom?: number;

  @IsOptional()
  @IsNumber()
  zSpreadTo?: number;

  @IsOptional()
  @IsNumber()
  forecastReturnFrom?: number;

  @IsOptional()
  @IsNumber()
  forecastReturnTo?: number;

  @IsOptional()
  @IsNumber()
  forecastUpSideFrom?: number;

  @IsOptional()
  @IsNumber()
  forecastUpSideTo?: number;

  @IsOptional()
  @IsNumber()
  forecastDownSideFrom?: number;

  @IsOptional()
  @IsNumber()
  forecastDownSideTo?: number;
}

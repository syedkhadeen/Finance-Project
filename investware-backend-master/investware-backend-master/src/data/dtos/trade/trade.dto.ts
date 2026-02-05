import { IsString, IsOptional, IsDate, IsNumber } from 'class-validator';
import { Type } from 'class-transformer';

export class TradeDTO {
  // Security fields
  @IsOptional()
  @IsString()
  readonly desc?: string;  // Security description

  @IsOptional()
  @IsString()
  readonly securityId?: string;  // Security ID

  @IsOptional()
  @IsString()
  readonly sector?: string;  // Sector (Corp, Govt, Muni)

  @IsOptional()
  @IsString()
  readonly issuer?: string;  // Issuer

  @IsOptional()
  @IsNumber()
  readonly cpnrt?: number;  // Coupon rate

  @IsOptional()
  @IsDate()
  @Type(() => Date)
  readonly dtMaturity?: Date;  // Maturity date

  // Trade details
  @IsOptional()
  @IsNumber()
  readonly parAmt?: number;  // Par amount

  @IsOptional()
  @IsNumber()
  readonly settlePrc?: number;  // Settlement price

  @IsOptional()
  @IsDate()
  @Type(() => Date)
  readonly dtSettle?: Date;  // Settlement date

  // Forecast
  @IsOptional()
  @IsDate()
  @Type(() => Date)
  readonly forecastDate?: Date;

  @IsOptional()
  @IsString()
  readonly forecastDiscountCurve?: string;

  @IsOptional()
  @IsString()
  readonly forecastFixingCurve?: string;

  // Legacy fields for backward compatibility
  @IsOptional()
  @IsString()
  readonly securityType?: string;

  @IsOptional()
  @IsString()
  readonly securityDescription?: string;

  @IsOptional()
  @IsNumber()
  readonly coupon?: number;

  @IsOptional()
  @IsDate()
  @Type(() => Date)
  readonly maturityDate?: Date;

  @IsOptional()
  @IsNumber()
  readonly par?: number;

  @IsOptional()
  @IsNumber()
  readonly price?: number;

  @IsOptional()
  @IsDate()
  @Type(() => Date)
  readonly settleDate?: Date;

  @IsOptional()
  @IsNumber()
  readonly yield?: number;
}

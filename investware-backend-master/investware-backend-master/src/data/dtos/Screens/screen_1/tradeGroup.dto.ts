import { IsNotEmpty, IsString, IsBoolean, IsOptional, IsDate, IsNumber } from 'class-validator';
import { Type } from 'class-transformer';

export class TradeGroupDTO {
  @IsNotEmpty()
  @IsString()
  readonly securityType: string;

  @IsNotEmpty()
  @IsString()
  readonly securityId: string;

  @IsOptional()
  @IsString()
  readonly securityDescription?: string;

  @IsOptional()
  @IsString()
  readonly issuer?: string;

  @IsOptional()
  @IsNumber()
  readonly coupon?: number;

  @IsOptional()
  @IsDate()
  @Type(() => Date)
  readonly maturityDate?: Date;

  @IsNotEmpty()
  @IsDate()
  @Type(() => Date)
  readonly settleDate: Date;

  @IsOptional()
  @IsDate()
  @Type(() => Date)
  readonly datedDate?: Date;

  @IsOptional()
  @IsDate()
  @Type(() => Date)
  readonly firstCouponDate?: Date;

  @IsOptional()
  @IsNumber()
  readonly par?: number;

  @IsOptional()
  @IsNumber()
  readonly price?: number;

  @IsOptional()
  @IsNumber()
  readonly yield?: number;

  @IsOptional()
  @IsString()
  readonly excludeHolidays?: string;

  @IsOptional()
  @IsBoolean()
  readonly afterTaxCalls?: boolean;

  @IsOptional()
  @IsString()
  readonly sector?: string;

  @IsOptional()
  @IsNumber()
  readonly couponFrom?: number;

  @IsOptional()
  @IsNumber()
  readonly couponTo?: number;

  @IsOptional()
  @IsDate()
  @Type(() => Date)
  readonly maturityFrom?: Date;

  @IsOptional()
  @IsDate()
  @Type(() => Date)
  readonly maturityTo?: Date;

  @IsOptional()
  @IsBoolean()
  readonly enterYieldCurve?: boolean;

  @IsOptional()
  @IsDate()
  @Type(() => Date)
  readonly forecastDate?: Date;

  @IsOptional()
  @IsBoolean()
  readonly useCurve?: boolean;

  @IsOptional()
  @IsBoolean()
  readonly enterFixingCurve?: boolean;

  @IsOptional()
  @IsString()
  readonly state?: string;

  @IsOptional()
  @IsNumber()
  readonly stateRate?: number;

  @IsOptional()
  @IsNumber()
  readonly fedRate?: number;

  addOns: any;
}

import { IsString, IsNumber, IsNotEmpty, IsDateString, IsOptional, IsArray, ValidateNested } from 'class-validator';
import { Type } from 'class-transformer';

export class CreateMarketBondDto {
  @IsString()
  @IsNotEmpty()
  isin: string;

  @IsNumber()
  @IsNotEmpty()
  price: number;

  @IsString()
  @IsOptional()
  exchange?: string;

  @IsDateString()
  @IsNotEmpty()
  date: string;
}

export class UpdateMarketBondDto {
  @IsString()
  @IsOptional()
  isin?: string;

  @IsNumber()
  @IsOptional()
  price?: number;

  @IsString()
  @IsOptional()
  exchange?: string;

  @IsDateString()
  @IsOptional()
  date?: string;
}

export class BulkCreateMarketBondsDto {
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => CreateMarketBondDto)
  bonds: CreateMarketBondDto[];
}

export class MarketBondResponseDto {
  id: string;
  userId: string;
  isin: string;
  price: number;
  exchange?: string;
  date: Date;
  createdAt: Date;
  updatedAt: Date;
}

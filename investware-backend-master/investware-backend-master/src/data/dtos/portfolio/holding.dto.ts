import { IsString, IsNumber, IsNotEmpty, IsDateString, IsOptional, IsArray, ValidateNested } from 'class-validator';
import { Type } from 'class-transformer';

export class CreateHoldingDto {
  @IsString()
  @IsNotEmpty()
  isin: string;

  @IsNumber()
  @IsNotEmpty()
  amount: number;

  @IsNumber()
  @IsNotEmpty()
  price: number;

  @IsDateString()
  @IsNotEmpty()
  holdDate: string;
}

export class UpdateHoldingDto {
  @IsString()
  @IsOptional()
  isin?: string;

  @IsNumber()
  @IsOptional()
  amount?: number;

  @IsNumber()
  @IsOptional()
  price?: number;

  @IsDateString()
  @IsOptional()
  holdDate?: string;
}

export class BulkCreateHoldingsDto {
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => CreateHoldingDto)
  holdings: CreateHoldingDto[];
}

export class HoldingResponseDto {
  id: string;
  portfolioId: string;
  isin: string;
  amount: number;
  price: number;
  holdDate: Date;
  createdAt: Date;
  updatedAt: Date;
}

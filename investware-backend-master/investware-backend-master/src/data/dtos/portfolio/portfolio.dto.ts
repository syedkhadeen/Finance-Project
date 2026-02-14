import { IsString, IsOptional, IsNotEmpty } from 'class-validator';

export class CreatePortfolioDto {
  @IsString()
  @IsNotEmpty()
  abbrevName: string;

  @IsString()
  @IsNotEmpty()
  fullName: string;

  @IsString()
  @IsOptional()
  strategy?: string;
}

export class UpdatePortfolioDto {
  @IsString()
  @IsOptional()
  abbrevName?: string;

  @IsString()
  @IsOptional()
  fullName?: string;

  @IsString()
  @IsOptional()
  strategy?: string;
}

export class PortfolioResponseDto {
  id: string;
  userId: string;
  abbrevName: string;
  fullName: string;
  strategy?: string;
  createdAt: Date;
  updatedAt: Date;
}

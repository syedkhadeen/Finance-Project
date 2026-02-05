import { IsDate, IsNotEmpty, IsNumber, IsOptional } from 'class-validator';

export class SinkDTO {
  @IsNotEmpty()
  @IsDate()
  readonly sinkDate: Date;

  @IsOptional()
  @IsNumber()
  readonly sinkAmount?: number;

  @IsOptional()
  @IsNumber()
  readonly sinkPrice?: number;

  @IsOptional()
  @IsNumber()
  readonly totalRow?: number;
}

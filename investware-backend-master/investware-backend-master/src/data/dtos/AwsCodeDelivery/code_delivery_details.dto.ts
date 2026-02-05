import { IsNotEmpty, IsString } from 'class-validator';

export class CodeDeliveryDetailsDto {
  @IsNotEmpty()
  @IsString()
  readonly attributeName: string;

  @IsNotEmpty()
  @IsString()
  readonly deliveryMedium: string;

  @IsNotEmpty()
  @IsString()
  readonly destination: string;

  @IsNotEmpty()
  @IsString()
  readonly userId: number;
}

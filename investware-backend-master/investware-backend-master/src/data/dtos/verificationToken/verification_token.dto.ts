import { IsString, IsNotEmpty } from 'class-validator';

export class VerifyTokenDto {
  @IsString()
  @IsNotEmpty()
  auth_id: string;

  @IsString()
  @IsNotEmpty()
  type: string;
}

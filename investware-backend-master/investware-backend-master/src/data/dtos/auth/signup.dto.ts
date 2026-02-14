import { IsNotEmpty, IsString, IsEmail, IsOptional } from 'class-validator';

export class SignupDto {
  @IsNotEmpty()
  @IsString()
  auth_id: string;

  @IsEmail()
  @IsNotEmpty()
  email: string;

  @IsString()
  @IsOptional()
  firstName?: string;

  @IsString()
  @IsOptional()
  lastName?: string;
}

import { IsEmail, IsNotEmpty, IsString, IsBoolean } from 'class-validator';

export class CreateUserDto {
  @IsNotEmpty()
  @IsString()
  readonly username: string;

  @IsNotEmpty()
  @IsString()
  readonly firstName: string;

  @IsNotEmpty()
  @IsString()
  readonly lastName: string;

  @IsNotEmpty()
  @IsString()
  readonly companyName: string;

  @IsNotEmpty()
  @IsEmail()
  readonly email: string;

  @IsNotEmpty()
  @IsString()
  readonly password: string;

  @IsNotEmpty()
  @IsString()
  readonly userSub: string; // Cognito user sub

  @IsNotEmpty()
  @IsBoolean()
  readonly userConfirmed: boolean;

  @IsNotEmpty()
  @IsString()
  readonly auth_id: string;

  @IsNotEmpty()
  @IsString()
  readonly stripe_customer_id: string;
}

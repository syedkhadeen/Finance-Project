import { IsEmail, IsNotEmpty, MinLength, MaxLength } from 'class-validator';

export class SetPasswordDto {
  @IsEmail()
  readonly email: string;

  @IsNotEmpty()
  @MinLength(8)
  @MaxLength(30)
  readonly password: string;

  @IsNotEmpty()
  @MinLength(8)
  @MaxLength(30)
  readonly confirmPassword: string;

  @IsNotEmpty()
  @MinLength(8)
  @MaxLength(8)
  readonly token: string;
}

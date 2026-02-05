import { IsNotEmpty, IsString } from 'class-validator';

export class ForgotPasswordDto {
  // TODO: need to discuss this
  @IsString()
  readonly code?: string;

  @IsString()
  @IsNotEmpty()
  readonly password: string;

  @IsString()
  @IsNotEmpty()
  readonly confirm_password: string;
}

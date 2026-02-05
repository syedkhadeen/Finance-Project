import { IsNotEmpty, IsString } from 'class-validator';

export class VerifyUserDto {
  @IsNotEmpty()
  @IsString()
  readonly userSub: string;

  @IsNotEmpty()
  @IsString()
  readonly verificationCode: string;
}

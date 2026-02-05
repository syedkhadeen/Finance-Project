import { Inject, Service } from 'typedi';
import { HttpResponse } from '@data/res/http_response';
import { AuthService } from '@data/services/auth.service';
import { ForgotPasswordDto } from '@data/dtos/auth/forgot_password.dto';

@Service()
export class AuthForgetPassswordUsecase {
  @Inject()
  auth: AuthService;

  public async call(data: ForgotPasswordDto) {
    const { email } = data;
    const result = await this.auth.forgetPassword({ email });
    return new HttpResponse(result, false);
  }
}

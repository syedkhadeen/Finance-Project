import { Inject, Service } from 'typedi';
import { SignupDto } from '@data/dtos/auth/signup.dto';
import { HttpResponse } from '@data/res/http_response';
import { AuthService } from '@data/services/auth.service';

@Service()
export class AuthSignupUsecase {
  @Inject()
  auth: AuthService;

  public async call(data: SignupDto) {
    const result = await this.auth.signUpByEmail(data);
    return new HttpResponse(result, false);
  }
}

import { Inject, Service } from 'typedi';
import { HttpResponse } from '@data/res/http_response';
import { AuthService } from '@data/services/auth.service';
import { HttpException } from '@data/exceptions/http_exception';
import { SetPasswordDto } from '@data/dtos/auth/set_password.dto';

@Service()
export class AuthSetForgetPassswordUsecase {
  @Inject()
  auth: AuthService;

  public async call(data: SetPasswordDto) {
    if (data.newPassword !== data.confirmPassword) {
      throw new HttpException(400, 'Passwords do not match');
    }

    const result = await this.auth.setPassword(data);
    return new HttpResponse(result, false);
  }
}

import { sign } from 'jsonwebtoken';
import { Inject, Service } from 'typedi';
import { LoginDto } from '@data/dtos/auth/login.dto';
import { parseJwt } from '@infrastructure/common/jwt';
import { HttpResponse } from '@data/res/http_response';
import { AuthService } from '@data/services/auth.service';
import { UserService } from '@data/services/users.service';
import { AUTH0_USER_PASSWORD } from '@config/environments';
import { HttpException } from '@data/exceptions/http_exception';

@Service()
export class AuthLoginUsecase {
  @Inject()
  auth: AuthService;
  @Inject()
  users: UserService;

  public async call(body: LoginDto) {
    try {
      const tokenResponse = await this.auth.loginUser(body);

      const token_data = parseJwt(tokenResponse.data.id_token);
      const user = await this.users.findUserByAuthID(token_data.sub);

      const jsonwebtoken = sign({ ...token_data, id: user.id }, AUTH0_USER_PASSWORD);

      return new HttpResponse({ idToken: jsonwebtoken }, false);
    } catch (error) {
      const err = error as any;
      throw new HttpException(err.statusCode || 500, err.message);
    }
  }
}

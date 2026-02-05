import { sign } from 'jsonwebtoken';
import { Inject, Service } from 'typedi';
import { HttpResponse } from '@data/res/http_response';
import { AuthService } from '@data/services/auth.service';
import { AUTH0_USER_PASSWORD } from '@config/environments';
import { UserService } from '@data/services/users.service';
import { StripeService } from '@data/services/stripe.service';
import { HttpException } from '@data/exceptions/http_exception';
import { SetPasswordDto } from '@data/dtos/auth/set_password.dto';

@Service()
export class AuthSetPasswordUsecase {
  @Inject()
  auth: AuthService;
  @Inject()
  stripe: StripeService;
  @Inject()
  users: UserService;

  public async call(data: SetPasswordDto) {
    if (data.newPassword !== data.confirmPassword) {
      throw new HttpException(400, 'Passwords do not match');
    }

    const user_id = await this.auth.setPassword(data);

    const stripeCustomer = await this.stripe.createCustomer(user_id, data.email);

    const user = await this.users.createuser({ auth_id: user_id, email: data.email, stripe_customer_id: stripeCustomer.id });
    const result = await this.auth.loginUser({
      email: data.email,
      password: data.newPassword,
    });

    const jsonwebtoken = sign({ ...result, id: user.id }, AUTH0_USER_PASSWORD);

    return new HttpResponse({ idToken: jsonwebtoken }, false);
  }
}

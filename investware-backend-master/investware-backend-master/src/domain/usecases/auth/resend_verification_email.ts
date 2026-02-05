import { Inject, Service } from 'typedi';
import { AuthService } from '@data/services/auth.service';

@Service()
export class AuthResendEmailVerificationUsecase {
  @Inject()
  auth: AuthService;

  public async call(email: string): Promise<{ message: string }> {
    const user = await this.auth.getAuth0UserByEmail(email);
    const user_id = user.data[0].user_id;
    await this.auth.resendVerificationEmail(user_id);

    return { message: 'Verification email resent successfully.' };
  }
}

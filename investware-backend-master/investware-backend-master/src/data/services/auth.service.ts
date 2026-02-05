import { sign } from 'jsonwebtoken';
import { Service } from 'typedi';
import database from '@config/database';
import { HttpError } from 'routing-controllers';
import { SignupDto } from '@data/dtos/auth/signup.dto';
import { LoginDto } from '@data/dtos/auth/login.dto';
import { ERROR_MESSAGES } from '@infrastructure/common/statusCodes';
@Service()
export class AuthService {
  private users = database.instance.users;

  //create user
  public async signUpUser(data: SignupDto) {
    try {
      return await this.users.create({ data });
    } catch {
      throw new HttpError(ERROR_MESSAGES.FAILED_TO_CREATE_USER.statusCode, ERROR_MESSAGES.FAILED_TO_CREATE_USER.message);
    }
  }

  // mock login
  public async loginUser(data: LoginDto) {
    try {
      const user = await this.users.findUnique({ where: { auth_id: data.email } });
      if (!user) {
        throw new HttpError(ERROR_MESSAGES.USER_NOT_FOUND.statusCode, ERROR_MESSAGES.USER_NOT_FOUND.message);
      }

      // Create a valid JWT payload that parseJwt expects
      const payload = {
        sub: user.auth_id,
        email: user.auth_id,
        family_name: 'User',
        given_name: 'Mock',
        exp: Math.floor(Date.now() / 1000) + (60 * 60) // 1 hour
      };

      const mockIdToken = sign(payload, 'mock-secret');

      return {
        data: {
          id_token: mockIdToken,
        },
      };
    } catch (error) {
      if (error instanceof HttpError) throw error;
      throw new HttpError(ERROR_MESSAGES.INTERNAL_SERVER_ERROR.statusCode, ERROR_MESSAGES.INTERNAL_SERVER_ERROR.message);
    }
  }
}

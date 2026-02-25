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

  // -----------------------------
  // Create a new user (signup)
  // -----------------------------
  public async signUpUser(data: SignupDto) {
    try {
      return await this.users.create({
        data: {
          auth_id: data.auth_id,
          email: data.email,
          firstName: data.firstName,
          lastName: data.lastName,
          password: data.password, // Save password as plain text
        },
      });
    } catch (error) {
      console.error(error);
      throw new HttpError(
        ERROR_MESSAGES.FAILED_TO_CREATE_USER.statusCode,
        ERROR_MESSAGES.FAILED_TO_CREATE_USER.message
      );
    }
  }

  // -----------------------------
  // Login user (plain-text password)
  // -----------------------------
  public async loginUser(data: LoginDto) {
    try {
      // Find user by email (or auth_id)
      const user = await this.users.findUnique({ where: { email: data.email } });
      if (!user) {
        throw new HttpError(
          ERROR_MESSAGES.USER_NOT_FOUND.statusCode,
          ERROR_MESSAGES.USER_NOT_FOUND.message
        );
      }

      // Check password in plain text
      if (user.password !== data.password) {
        throw new HttpError(
          ERROR_MESSAGES.INVALID_CREDENTIALS.statusCode,
          ERROR_MESSAGES.INVALID_CREDENTIALS.message
        );
      }

      // Create JWT payload
      const payload = {
        sub: user.auth_id,
        email: user.email,
        family_name: user.lastName,
        given_name: user.firstName,
        exp: Math.floor(Date.now() / 1000) + 60 * 60, // 1 hour expiration
      };

      // Sign token
      const idToken = sign(payload, 'mock-secret');

      return {
        data: {
          id_token: idToken,
        },
      };
    } catch (error) {
      if (error instanceof HttpError) throw error;
      console.error(error);
      throw new HttpError(
        ERROR_MESSAGES.INTERNAL_SERVER_ERROR.statusCode,
        ERROR_MESSAGES.INTERNAL_SERVER_ERROR.message
      );
    }
  }
}
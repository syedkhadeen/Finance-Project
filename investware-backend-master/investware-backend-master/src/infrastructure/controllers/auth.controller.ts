import { Container } from 'typedi';
import { SignupDto } from '@data/dtos/auth/signup.dto';
import { LoginDto } from '@data/dtos/auth/login.dto';
import { AuthLoginUsecase } from '@domain/usecases/auth/login';
import { ValidationMiddleware } from '@infrastructure/middlewares/validation.middleware';
import { JsonController, Body, Post, UseBefore, HttpCode, HttpError } from 'routing-controllers';
import { AuthService } from '@data/services/auth.service';
import { HttpResponse } from '@data/res/http_response';
import { ERROR_MESSAGES } from '@infrastructure/common/statusCodes';
@JsonController('/auth')
export class AuthController {
  public auth = Container.get(AuthService);
  public loginUsecase = Container.get(AuthLoginUsecase);

  @Post('/signup')
  @UseBefore(ValidationMiddleware(SignupDto))
  @HttpCode(201)
  async signUpByEmail(@Body() data: SignupDto) {
    try {
      const user = await this.auth.signUpUser(data);
      if (!user) {
        throw new HttpError(ERROR_MESSAGES.FAILED_TO_CREATE_USER.statusCode, ERROR_MESSAGES.FAILED_TO_CREATE_USER.message);
      }
      return new HttpResponse(user, false);
    } catch {
      throw new HttpError(ERROR_MESSAGES.BAD_REQUEST.statusCode, ERROR_MESSAGES.BAD_REQUEST.message);
    }
  }

  @Post('/login')
  @UseBefore(ValidationMiddleware(LoginDto))
  @HttpCode(200)
  async login(@Body() data: LoginDto) {
    try {
      const result = await this.loginUsecase.call(data);
      return result;
    } catch (error: any) {
      throw new HttpError(error.statusCode || 500, error.message);
    }
  }
}

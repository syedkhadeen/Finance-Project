import { Container } from 'typedi';
import { HttpResponse } from '@data/res/http_response';
import { ERROR_MESSAGES } from '../common/statusCodes';
import { UserService } from '../../data/services/users.service';
import { Authorized, Body, Get, HttpCode, HttpError, JsonController, Put, Req, UseBefore } from 'routing-controllers';
import { RequestWithUser } from '@data/interfaces/request.interface';
import { CheckTokenExpiry } from '@infrastructure/middlewares/token_expiry.middleware';
import { UpdateProfileDto } from '@data/dtos/users/update_profile.dto';
import { ValidationMiddleware } from '@infrastructure/middlewares/validation.middleware';
@JsonController('/users')
@UseBefore(CheckTokenExpiry)
@Authorized()
export class UserController {
  public userService = Container.get(UserService);
  private empty_string = '';

  @Get('/profile')
  @HttpCode(200)
  async getProfile(@Req() req: RequestWithUser) {
    const user_id = req.user.sub;
    if (!user_id) {
      throw new HttpError(ERROR_MESSAGES.INVALID_USER_ID.statusCode, ERROR_MESSAGES.INVALID_USER_ID.message);
    }
    const getUser = await this.userService.getProfile(user_id);
    if (!getUser) {
      throw new HttpError(ERROR_MESSAGES.USER_NOT_FOUND.statusCode, ERROR_MESSAGES.USER_NOT_FOUND.message);
    }
    const userAttributes = getUser.UserAttributes?.reduce(
      (acc, attr) => {
        acc[attr.Name || this.empty_string] = attr.Value || this.empty_string;
        return acc;
      },
      {} as Record<string, string>,
    );
    const response = {
      user_id: getUser.Username,
      first_name: userAttributes['given_name'] || this.empty_string,
      last_name: userAttributes['family_name'] || this.empty_string,
      email: userAttributes['email'] || this.empty_string,
      company_name: userAttributes['custom:companyName'] || this.empty_string,
      status: getUser.UserStatus,
    };

    return new HttpResponse(response, false);
  }

  @Put('/profile')
  @UseBefore(ValidationMiddleware(UpdateProfileDto))
  @HttpCode(200)
  async updateProfile(@Req() req: RequestWithUser, @Body() data: UpdateProfileDto) {
    const user_id = req.user.sub;
    if (!user_id) {
      throw new HttpError(ERROR_MESSAGES.INVALID_USER_ID.statusCode, ERROR_MESSAGES.INVALID_USER_ID.message);
    }
    const updateUser = await this.userService.updateProfile(user_id, data);
    if (!updateUser) {
      throw new HttpError(ERROR_MESSAGES.INVALID_USER_ATTRIBUTES.statusCode, ERROR_MESSAGES.INVALID_USER_ATTRIBUTES.message);
    }
    return new HttpResponse(true, false);
  }
}

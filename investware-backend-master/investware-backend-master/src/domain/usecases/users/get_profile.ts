import { Inject, Service } from 'typedi';
import { UserService } from '@data/services/users.service';
import { HttpError } from 'routing-controllers';
import { ERROR_MESSAGES } from '@infrastructure/common/statusCodes';
import { HttpResponse } from '@data/res/http_response';

@Service()
export class GetProfileUsecase {
  @Inject()
  userService: UserService;

  public async call(user_id: string) {
    try {
      const user = await this.userService.getProfile(user_id);
      if (!user) {
        throw new HttpError(ERROR_MESSAGES.USER_NOT_FOUND.statusCode, ERROR_MESSAGES.USER_NOT_FOUND.message);
      }
      return new HttpResponse(user, false);
    } catch (error) {
      throw new HttpError(ERROR_MESSAGES.USER_NOT_FOUND.statusCode, ERROR_MESSAGES.USER_NOT_FOUND.message);
    }
  }
}

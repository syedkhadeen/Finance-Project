import { Inject, Service } from 'typedi';
import { HttpResponse } from '@data/res/http_response';
import { UserService } from '@data/services/users.service';
import { ERROR_MESSAGES } from '@infrastructure/common/statusCodes';
import { HttpError } from 'routing-controllers';
import { UpdateProfileDto } from '@data/dtos/users/update_profile.dto';
@Service()
export class UpdateProfileUsecase {
  @Inject()
  userService: UserService;

  public async call(id: string, data: UpdateProfileDto) {
    try {
      const user = await this.userService.updateProfile(id, data);
      return new HttpResponse(user, false);
    } catch (error) {
      throw new HttpError(ERROR_MESSAGES.BAD_REQUEST.statusCode, ERROR_MESSAGES.BAD_REQUEST.message);
    }
  }
}

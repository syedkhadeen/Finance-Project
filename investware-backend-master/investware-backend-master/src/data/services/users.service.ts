import { AdminGetUserCommand, AdminUpdateUserAttributesCommand } from '@aws-sdk/client-cognito-identity-provider';
import { cognitoClient } from '@config/aws/aws_cognito';
import { AWS_COGNITO_USER_POOL_ID } from '@config/environments';
import { UpdateProfileDto } from '@data/dtos/users/update_profile.dto';
import { ERROR_MESSAGES } from '@infrastructure/common/statusCodes';
import { HttpError } from 'routing-controllers';
import { Service } from 'typedi';
import database from '@config/database';
@Service()
export class UserService {
  private users = database.instance.users;

  public async findUserByAuthID(auth_id: string) {
    try {
      const user = await this.users.findUnique({ where: { auth_id } });
      if (!user) {
        throw new HttpError(ERROR_MESSAGES.USER_NOT_FOUND.statusCode, ERROR_MESSAGES.USER_NOT_FOUND.message);
      }
      return user;
    } catch (error) {
      if (error instanceof HttpError) throw error;
      throw new HttpError(ERROR_MESSAGES.INTERNAL_SERVER_ERROR.statusCode, ERROR_MESSAGES.INTERNAL_SERVER_ERROR.message);
    }
  }

  public async getProfile(user_id: string) {
    const params = new AdminGetUserCommand({
      UserPoolId: AWS_COGNITO_USER_POOL_ID,
      Username: user_id,
    });

    try {
      return await cognitoClient.send(params);
    } catch {
      throw new HttpError(ERROR_MESSAGES.USER_NOT_FOUND.statusCode, ERROR_MESSAGES.USER_NOT_FOUND.message);
    }
  }

  public async updateProfile(user_id: string, data: UpdateProfileDto) {
    const userAttributes = [
      { Name: 'given_name', Value: data.first_name },
      { Name: 'family_name', Value: data.last_name },
      { Name: 'custom:companyName', Value: data.company_name },
    ];

    console.log(userAttributes);
    const params = new AdminUpdateUserAttributesCommand({
      UserPoolId: AWS_COGNITO_USER_POOL_ID,
      Username: user_id,
      UserAttributes: userAttributes,
    });

    try {
      return await cognitoClient.send(params);
    } catch {
      throw new HttpError(ERROR_MESSAGES.INVALID_USER_ATTRIBUTES.statusCode, ERROR_MESSAGES.INVALID_USER_ATTRIBUTES.message);
    }
  }
}

import { Service } from 'typedi';
import database from '@config/database';
import { HttpError } from 'routing-controllers';
import { AddOnsDTO } from '@data/dtos/Screens/screen_1/addOn.dto';
import { ERROR_MESSAGES } from '@infrastructure/common/statusCodes';

@Service()
export class AddOnsService {
  private addOns = database.instance.addOns;

  async createAddOns(data: AddOnsDTO) {
    try {
      const createdAddOns = await this.addOns.create({
        data: {
          sinks: {
            create: data.sinks || [],
          },
        },
      });
      return createdAddOns;
    } catch (error) {
      throw new HttpError(ERROR_MESSAGES.INTERNAL_SERVER_ERROR.statusCode, ERROR_MESSAGES.INTERNAL_SERVER_ERROR.message);
    }
  }

  async getAddOnsById(id: string) {
    try {
      const addOns = await this.addOns.findUnique({
        where: { id },
        include: {
          sinks: true,
        },
      });
      if (!addOns) {
        throw new HttpError(ERROR_MESSAGES.NOT_FOUND.statusCode, ERROR_MESSAGES.NOT_FOUND.message);
      }
      return addOns;
    } catch (error) {
      throw new HttpError(ERROR_MESSAGES.INTERNAL_SERVER_ERROR.statusCode, ERROR_MESSAGES.INTERNAL_SERVER_ERROR.message);
    }
  }
}

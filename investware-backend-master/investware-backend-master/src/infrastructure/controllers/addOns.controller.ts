import { Container } from 'typedi';
import { AddOnsDTO } from '@data/dtos/Screens/screen_1/addOn.dto';
import { ValidationMiddleware } from '@infrastructure/middlewares/validation.middleware';
import { JsonController, Body, Post, Get, Param, UseBefore, HttpCode, HttpError } from 'routing-controllers';
import { AddOnsService } from '@data/services/addOn.service';
import { HttpResponse } from '@data/res/http_response';
import { ERROR_MESSAGES } from '@infrastructure/common/statusCodes';

@JsonController('/addons')
export class AddOnsController {
  private addOnsService = Container.get(AddOnsService);

  @Post('/')
  @UseBefore(ValidationMiddleware(AddOnsDTO))
  @HttpCode(201)
  async createAddOns(@Body() data: AddOnsDTO) {
    try {
      const addOns = await this.addOnsService.createAddOns(data);
      return new HttpResponse(addOns, false);
    } catch {
      throw new HttpError(ERROR_MESSAGES.BAD_REQUEST.statusCode, ERROR_MESSAGES.BAD_REQUEST.message);
    }
  }

  @Get('/:id')
  @HttpCode(200)
  async getAddOnsById(@Param('id') id: string) {
    try {
      const addOns = await this.addOnsService.getAddOnsById(id);
      return new HttpResponse(addOns, false);
    } catch {
      throw new HttpError(ERROR_MESSAGES.NOT_FOUND.statusCode, ERROR_MESSAGES.NOT_FOUND.message);
    }
  }
}

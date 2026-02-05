import { Container } from 'typedi';
import { ValidationMiddleware } from '@infrastructure/middlewares/validation.middleware';
import { JsonController, Body, Post, Get, Param, UseBefore, HttpCode, HttpError } from 'routing-controllers';
import { TradeService } from '@data/services/trade.service';
import { CalculationService } from '@data/services/calculation.service';
import { SearchService } from '@data/services/search.service';
import { HttpResponse } from '@data/res/http_response';
import { ERROR_MESSAGES } from '@infrastructure/common/statusCodes';
import { TradeGroupDTO } from '@data/dtos/Screens/screen_1/tradeGroup.dto';
import { SearchCriteriaDTO } from '@data/dtos/search/searchCriteria.dto';

@JsonController('/trade')
export class TradeController {
  private tradeService = Container.get(TradeService);
  private calculationService = Container.get(CalculationService);
  private searchService = Container.get(SearchService);

  @Post('/create_trade')
  @UseBefore(ValidationMiddleware(TradeGroupDTO))
  @HttpCode(201)
  async createTrade(@Body() data: TradeGroupDTO) {
    try {
      const trade = await this.tradeService.createTrade(data);
      return new HttpResponse(trade, false);
    } catch {
      throw new HttpError(ERROR_MESSAGES.BAD_REQUEST.statusCode, ERROR_MESSAGES.BAD_REQUEST.message);
    }
  }

  @Post('/calculate')
  @UseBefore(ValidationMiddleware(TradeGroupDTO))
  @HttpCode(200)
  async calculate(@Body() data: TradeGroupDTO) {
    try {
      const result = await this.calculationService.calculateBond(data);
      return new HttpResponse(result, false);
    } catch {
      throw new HttpError(ERROR_MESSAGES.BAD_REQUEST.statusCode, "Calculation failed");
    }
  }

  @Post('/search')
  @UseBefore(ValidationMiddleware(SearchCriteriaDTO))
  @HttpCode(200)
  async search(@Body() data: SearchCriteriaDTO) {
    try {
      const results = await this.searchService.searchSecurities(data);
      return new HttpResponse(results, false);
    } catch (error) {
      throw new HttpError(ERROR_MESSAGES.BAD_REQUEST.statusCode, "Search failed");
    }
  }

  @Get('/get_trade/:id')
  @HttpCode(200)
  async getTradeById(@Param('id') id: string) {
    try {
      const trade = await this.tradeService.getTradeById(id);
      return new HttpResponse(trade, false);
    } catch {
      throw new HttpError(ERROR_MESSAGES.NOT_FOUND.statusCode, ERROR_MESSAGES.NOT_FOUND.message);
    }
  }
}

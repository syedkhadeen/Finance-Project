import { JsonController, Get, Post, Put, Delete, Param, Body, Authorized, Req } from 'routing-controllers';
import { OpenAPI, ResponseSchema } from 'routing-controllers-openapi';
import { Container } from 'typedi';
import { MarketBondService } from '@data/services/marketBond.service';
import { CreateMarketBondDto, UpdateMarketBondDto, BulkCreateMarketBondsDto, MarketBondResponseDto } from '@data/dtos/portfolio/marketBond.dto';
import { RequestWithUser } from '@data/interfaces/request.interface';

@JsonController('/market-bonds')
@Authorized()
export class MarketBondController {
  public marketBondService = Container.get(MarketBondService);

  @Post()
  @OpenAPI({ summary: 'Create a new market bond' })
  @ResponseSchema(MarketBondResponseDto)
  async createMarketBond(@Req() req: RequestWithUser, @Body() data: CreateMarketBondDto) {
    const userId = req.user.id;
    return await this.marketBondService.createMarketBond(userId, data);
  }

  @Post('/bulk')
  @OpenAPI({ summary: 'Bulk create market bonds' })
  @ResponseSchema(MarketBondResponseDto, { isArray: true })
  async bulkCreateMarketBonds(@Req() req: RequestWithUser, @Body() data: BulkCreateMarketBondsDto) {
    const userId = req.user.id;
    return await this.marketBondService.bulkCreateMarketBonds(userId, data);
  }

  @Get()
  @OpenAPI({ summary: 'Get all market bonds for the authenticated user' })
  @ResponseSchema(MarketBondResponseDto, { isArray: true })
  async getMarketBonds(@Req() req: RequestWithUser) {
    const userId = req.user.id;
    return await this.marketBondService.getMarketBonds(userId);
  }

  @Get('/:bondId')
  @OpenAPI({ summary: 'Get a specific market bond by ID' })
  @ResponseSchema(MarketBondResponseDto)
  async getMarketBondById(@Req() req: RequestWithUser, @Param('bondId') bondId: string) {
    const userId = req.user.id;
    return await this.marketBondService.getMarketBondById(userId, bondId);
  }

  @Put('/:bondId')
  @OpenAPI({ summary: 'Update a market bond' })
  @ResponseSchema(MarketBondResponseDto)
  async updateMarketBond(@Req() req: RequestWithUser, @Param('bondId') bondId: string, @Body() data: UpdateMarketBondDto) {
    const userId = req.user.id;
    return await this.marketBondService.updateMarketBond(userId, bondId, data);
  }

  @Delete('/:bondId')
  @OpenAPI({ summary: 'Delete a market bond' })
  async deleteMarketBond(@Req() req: RequestWithUser, @Param('bondId') bondId: string) {
    const userId = req.user.id;
    return await this.marketBondService.deleteMarketBond(userId, bondId);
  }

  @Delete()
  @OpenAPI({ summary: 'Delete all market bonds for the authenticated user' })
  async deleteAllMarketBonds(@Req() req: RequestWithUser) {
    const userId = req.user.id;
    return await this.marketBondService.deleteAllMarketBonds(userId);
  }
}

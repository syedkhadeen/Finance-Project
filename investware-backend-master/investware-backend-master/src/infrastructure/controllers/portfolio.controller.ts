import { JsonController, Get, Post, Put, Delete, Param, Body, Authorized, Req } from 'routing-controllers';
import { OpenAPI, ResponseSchema } from 'routing-controllers-openapi';
import { Container } from 'typedi';
import { PortfolioService } from '@data/services/portfolio.service';
import { CreatePortfolioDto, UpdatePortfolioDto, PortfolioResponseDto } from '@data/dtos/portfolio/portfolio.dto';
import { CreateHoldingDto, BulkCreateHoldingsDto, HoldingResponseDto } from '@data/dtos/portfolio/holding.dto';
import { RequestWithUser } from '@data/interfaces/request.interface';

@JsonController('/portfolios')
@Authorized()
export class PortfolioController {
  public portfolioService = Container.get(PortfolioService);

  @Post()
  @OpenAPI({ summary: 'Create a new portfolio' })
  @ResponseSchema(PortfolioResponseDto)
  async createPortfolio(@Req() req: RequestWithUser, @Body() data: CreatePortfolioDto) {
    const userId = req.user.id;
    return await this.portfolioService.createPortfolio(userId, data);
  }

  @Get()
  @OpenAPI({ summary: 'Get all portfolios for the authenticated user' })
  @ResponseSchema(PortfolioResponseDto, { isArray: true })
  async getPortfolios(@Req() req: RequestWithUser) {
    const userId = req.user.id;
    return await this.portfolioService.getPortfolios(userId);
  }

  @Get('/:portfolioId')
  @OpenAPI({ summary: 'Get a specific portfolio by ID' })
  @ResponseSchema(PortfolioResponseDto)
  async getPortfolioById(@Req() req: RequestWithUser, @Param('portfolioId') portfolioId: string) {
    const userId = req.user.id;
    return await this.portfolioService.getPortfolioById(userId, portfolioId);
  }

  @Put('/:portfolioId')
  @OpenAPI({ summary: 'Update a portfolio' })
  @ResponseSchema(PortfolioResponseDto)
  async updatePortfolio(@Req() req: RequestWithUser, @Param('portfolioId') portfolioId: string, @Body() data: UpdatePortfolioDto) {
    const userId = req.user.id;
    return await this.portfolioService.updatePortfolio(userId, portfolioId, data);
  }

  @Delete('/:portfolioId')
  @OpenAPI({ summary: 'Delete a portfolio' })
  async deletePortfolio(@Req() req: RequestWithUser, @Param('portfolioId') portfolioId: string) {
    const userId = req.user.id;
    return await this.portfolioService.deletePortfolio(userId, portfolioId);
  }

  // Holdings endpoints
  @Post('/:portfolioId/holdings')
  @OpenAPI({ summary: 'Add a holding to a portfolio' })
  @ResponseSchema(HoldingResponseDto)
  async addHolding(@Req() req: RequestWithUser, @Param('portfolioId') portfolioId: string, @Body() data: CreateHoldingDto) {
    const userId = req.user.id;
    return await this.portfolioService.addHolding(userId, portfolioId, data);
  }

  @Post('/:portfolioId/holdings/bulk')
  @OpenAPI({ summary: 'Bulk add holdings to a portfolio' })
  @ResponseSchema(HoldingResponseDto, { isArray: true })
  async bulkAddHoldings(@Req() req: RequestWithUser, @Param('portfolioId') portfolioId: string, @Body() data: BulkCreateHoldingsDto) {
    const userId = req.user.id;
    return await this.portfolioService.bulkAddHoldings(userId, portfolioId, data);
  }

  @Get('/:portfolioId/holdings')
  @OpenAPI({ summary: 'Get all holdings for a portfolio' })
  @ResponseSchema(HoldingResponseDto, { isArray: true })
  async getHoldings(@Req() req: RequestWithUser, @Param('portfolioId') portfolioId: string) {
    const userId = req.user.id;
    return await this.portfolioService.getHoldings(userId, portfolioId);
  }

  @Put('/:portfolioId/holdings/:holdingId')
  @OpenAPI({ summary: 'Update a holding' })
  @ResponseSchema(HoldingResponseDto)
  async updateHolding(
    @Req() req: RequestWithUser,
    @Param('portfolioId') portfolioId: string,
    @Param('holdingId') holdingId: string,
    @Body() data: CreateHoldingDto,
  ) {
    const userId = req.user.id;
    return await this.portfolioService.updateHolding(userId, portfolioId, holdingId, data);
  }

  @Delete('/:portfolioId/holdings/:holdingId')
  @OpenAPI({ summary: 'Delete a holding' })
  async deleteHolding(@Req() req: RequestWithUser, @Param('portfolioId') portfolioId: string, @Param('holdingId') holdingId: string) {
    const userId = req.user.id;
    return await this.portfolioService.deleteHolding(userId, portfolioId, holdingId);
  }
}

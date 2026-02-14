import { Service } from 'typedi';
import { PrismaClient } from '@prisma/client';
import { HttpException } from '@data/exceptions/http_exception';
import { CreatePortfolioDto, UpdatePortfolioDto } from '@data/dtos/portfolio/portfolio.dto';
import { CreateHoldingDto, BulkCreateHoldingsDto } from '@data/dtos/portfolio/holding.dto';

@Service()
export class PortfolioService {
  private prisma = new PrismaClient();

  // Portfolio CRUD operations
  async createPortfolio(userId: string, data: CreatePortfolioDto) {
    const existingPortfolio = await this.prisma.portfolio.findUnique({
      where: {
        userId_abbrevName: {
          userId,
          abbrevName: data.abbrevName,
        },
      },
    });

    if (existingPortfolio) {
      throw new HttpException(409, 'Portfolio with this abbreviation already exists');
    }

    return await this.prisma.portfolio.create({
      data: {
        userId,
        ...data,
      },
      include: {
        holdings: true,
      },
    });
  }

  async getPortfolios(userId: string) {
    return await this.prisma.portfolio.findMany({
      where: { userId },
      include: {
        holdings: true,
      },
      orderBy: {
        createdAt: 'desc',
      },
    });
  }

  async getPortfolioById(userId: string, portfolioId: string) {
    const portfolio = await this.prisma.portfolio.findFirst({
      where: {
        id: portfolioId,
        userId,
      },
      include: {
        holdings: true,
      },
    });

    if (!portfolio) {
      throw new HttpException(404, 'Portfolio not found');
    }

    return portfolio;
  }

  async updatePortfolio(userId: string, portfolioId: string, data: UpdatePortfolioDto) {
    const portfolio = await this.prisma.portfolio.findFirst({
      where: {
        id: portfolioId,
        userId,
      },
    });

    if (!portfolio) {
      throw new HttpException(404, 'Portfolio not found');
    }

    return await this.prisma.portfolio.update({
      where: { id: portfolioId },
      data,
      include: {
        holdings: true,
      },
    });
  }

  async deletePortfolio(userId: string, portfolioId: string) {
    const portfolio = await this.prisma.portfolio.findFirst({
      where: {
        id: portfolioId,
        userId,
      },
    });

    if (!portfolio) {
      throw new HttpException(404, 'Portfolio not found');
    }

    await this.prisma.portfolio.delete({
      where: { id: portfolioId },
    });

    return { message: 'Portfolio deleted successfully' };
  }

  // Holdings operations
  async addHolding(userId: string, portfolioId: string, data: CreateHoldingDto) {
    const portfolio = await this.prisma.portfolio.findFirst({
      where: {
        id: portfolioId,
        userId,
      },
    });

    if (!portfolio) {
      throw new HttpException(404, 'Portfolio not found');
    }

    return await this.prisma.holding.create({
      data: {
        portfolioId,
        ...data,
        holdDate: new Date(data.holdDate),
      },
    });
  }

  async bulkAddHoldings(userId: string, portfolioId: string, data: BulkCreateHoldingsDto) {
    const portfolio = await this.prisma.portfolio.findFirst({
      where: {
        id: portfolioId,
        userId,
      },
    });

    if (!portfolio) {
      throw new HttpException(404, 'Portfolio not found');
    }

    const holdings = data.holdings.map(holding => ({
      portfolioId,
      ...holding,
      holdDate: new Date(holding.holdDate),
    }));

    await this.prisma.holding.createMany({
      data: holdings,
    });

    return await this.prisma.holding.findMany({
      where: { portfolioId },
    });
  }

  async getHoldings(userId: string, portfolioId: string) {
    const portfolio = await this.prisma.portfolio.findFirst({
      where: {
        id: portfolioId,
        userId,
      },
    });

    if (!portfolio) {
      throw new HttpException(404, 'Portfolio not found');
    }

    return await this.prisma.holding.findMany({
      where: { portfolioId },
      orderBy: {
        holdDate: 'desc',
      },
    });
  }

  async updateHolding(userId: string, portfolioId: string, holdingId: string, data: Partial<CreateHoldingDto>) {
    const portfolio = await this.prisma.portfolio.findFirst({
      where: {
        id: portfolioId,
        userId,
      },
    });

    if (!portfolio) {
      throw new HttpException(404, 'Portfolio not found');
    }

    const holding = await this.prisma.holding.findFirst({
      where: {
        id: holdingId,
        portfolioId,
      },
    });

    if (!holding) {
      throw new HttpException(404, 'Holding not found');
    }

    return await this.prisma.holding.update({
      where: { id: holdingId },
      data: {
        ...data,
        holdDate: data.holdDate ? new Date(data.holdDate) : undefined,
      },
    });
  }

  async deleteHolding(userId: string, portfolioId: string, holdingId: string) {
    const portfolio = await this.prisma.portfolio.findFirst({
      where: {
        id: portfolioId,
        userId,
      },
    });

    if (!portfolio) {
      throw new HttpException(404, 'Portfolio not found');
    }

    const holding = await this.prisma.holding.findFirst({
      where: {
        id: holdingId,
        portfolioId,
      },
    });

    if (!holding) {
      throw new HttpException(404, 'Holding not found');
    }

    await this.prisma.holding.delete({
      where: { id: holdingId },
    });

    return { message: 'Holding deleted successfully' };
  }
}

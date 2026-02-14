import { Service } from 'typedi';
import { PrismaClient } from '@prisma/client';
import { HttpException } from '@data/exceptions/http_exception';
import { CreateMarketBondDto, UpdateMarketBondDto, BulkCreateMarketBondsDto } from '@data/dtos/portfolio/marketBond.dto';

@Service()
export class MarketBondService {
  private prisma = new PrismaClient();

  async createMarketBond(userId: string, data: CreateMarketBondDto) {
    return await this.prisma.marketBond.create({
      data: {
        userId,
        ...data,
        date: new Date(data.date),
      },
    });
  }

  async bulkCreateMarketBonds(userId: string, data: BulkCreateMarketBondsDto) {
    const bonds = data.bonds.map(bond => ({
      userId,
      ...bond,
      date: new Date(bond.date),
    }));

    await this.prisma.marketBond.createMany({
      data: bonds,
    });

    return await this.prisma.marketBond.findMany({
      where: { userId },
      orderBy: {
        date: 'desc',
      },
    });
  }

  async getMarketBonds(userId: string) {
    return await this.prisma.marketBond.findMany({
      where: { userId },
      orderBy: {
        date: 'desc',
      },
    });
  }

  async getMarketBondById(userId: string, bondId: string) {
    const bond = await this.prisma.marketBond.findFirst({
      where: {
        id: bondId,
        userId,
      },
    });

    if (!bond) {
      throw new HttpException(404, 'Market bond not found');
    }

    return bond;
  }

  async updateMarketBond(userId: string, bondId: string, data: UpdateMarketBondDto) {
    const bond = await this.prisma.marketBond.findFirst({
      where: {
        id: bondId,
        userId,
      },
    });

    if (!bond) {
      throw new HttpException(404, 'Market bond not found');
    }

    return await this.prisma.marketBond.update({
      where: { id: bondId },
      data: {
        ...data,
        date: data.date ? new Date(data.date) : undefined,
      },
    });
  }

  async deleteMarketBond(userId: string, bondId: string) {
    const bond = await this.prisma.marketBond.findFirst({
      where: {
        id: bondId,
        userId,
      },
    });

    if (!bond) {
      throw new HttpException(404, 'Market bond not found');
    }

    await this.prisma.marketBond.delete({
      where: { id: bondId },
    });

    return { message: 'Market bond deleted successfully' };
  }

  async deleteAllMarketBonds(userId: string) {
    await this.prisma.marketBond.deleteMany({
      where: { userId },
    });

    return { message: 'All market bonds deleted successfully' };
  }
}

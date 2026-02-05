import { Service } from 'typedi';
import database from '@config/database';
import { HttpError } from 'routing-controllers';
import { ERROR_MESSAGES } from '@infrastructure/common/statusCodes';
import { TradeGroupDTO } from '@data/dtos/Screens/screen_1/tradeGroup.dto';
import { ExcludeHolidays, SecurityType } from '@prisma/client';
@Service()
export class TradeService {
  private trade = database.instance.trade;

  async createTrade(data: TradeGroupDTO) {
    try {
      const createdTrade = await this.trade.create({
        data: {
          securityType: data.securityType as SecurityType,
          securityId: data.securityId,
          securityDescription: data.securityDescription,
          issuer: data.issuer,
          coupon: data.coupon,
          maturityDate: data.maturityDate,
          settleDate: data.settleDate,
          datedDate: data.datedDate,
          firstCouponDate: data.firstCouponDate,
          par: data.par,
          price: data.price,
          yield: data.yield,
          excludeHolidays: data.excludeHolidays as ExcludeHolidays,
          afterTaxCalls: data.afterTaxCalls,
          sector: data.sector,
          couponFrom: data.couponFrom,
          couponTo: data.couponTo,
          maturityFrom: data.maturityFrom,
          maturityTo: data.maturityTo,
          enterYieldCurve: data.enterYieldCurve,
          forecastDate: data.forecastDate,
          useCurve: data.useCurve,
          enterFixingCurve: data.enterFixingCurve,
          state: data.state,
          stateRate: data.stateRate,
          fedRate: data.fedRate,
          addOns: {
            create: {
              sinks: {
                create: data.addOns?.sinks || [],
              },
            },
          },
        },
      });
      return createdTrade;
    } catch (error) {
      throw new HttpError(ERROR_MESSAGES.INTERNAL_SERVER_ERROR.statusCode, ERROR_MESSAGES.INTERNAL_SERVER_ERROR.message);
    }
  }

  async getTradeById(id: string) {
    try {
      const trade = await this.trade.findUnique({
        where: { id },
        include: {
          addOns: {
            include: {
              sinks: true,
            },
          },
        },
      });
      if (!trade) {
        throw new HttpError(ERROR_MESSAGES.NOT_FOUND.statusCode, ERROR_MESSAGES.NOT_FOUND.message);
      }
      return trade;
    } catch (error) {
      throw new HttpError(ERROR_MESSAGES.INTERNAL_SERVER_ERROR.statusCode, ERROR_MESSAGES.INTERNAL_SERVER_ERROR.message);
    }
  }
}

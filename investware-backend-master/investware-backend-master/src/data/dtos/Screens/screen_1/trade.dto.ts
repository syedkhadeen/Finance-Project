import { IsNotEmpty, IsOptional } from 'class-validator';
import { TradeGroupDTO } from './tradeGroup.dto';
import { AddOnsDTO } from './addOn.dto';

export class TradeDTO {
  @IsNotEmpty()
  readonly tradeGroup: TradeGroupDTO;

  @IsOptional()
  readonly addOns?: AddOnsDTO;
}

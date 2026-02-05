import { IAddOns } from './addOns.interface';
import { ITradeGroup } from './tradeGroup.interface';

export interface ITrade {
  tradeGroup: ITradeGroup; // Trade Group section
  addOns?: IAddOns; // Add-Ons section, optional
}

export interface ITradeGroup {
  securityType: string; // Drop-down selection
  securityId: string; // Text (editable)
  securityDescription?: string; // Optional
  issuer?: string; // Optional
  maturityDate: Date; // Calendar (mandatory)
  settleDate: Date; // Calendar (mandatory)
  par?: number; // Optional as it's not mentioned as mandatory
  price?: number; // Optional as it's not mentioned as mandatory
  yield?: number; // Optional as it's not mentioned as mandatory
  excludeHolidays?: string; // Drop-down selection, Optional as it's not mentioned as mandatory
  afterTaxCalls?: boolean; // Tickbox, Optional
}

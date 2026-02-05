export interface IUser {
  sub: string;
  email: string;
  family_name: string;
  given_name: string;
}

export interface ICodeDeliveryDetails {
  id: number;
  attributeName: string;
  deliveryMedium: string;
  destination: string;
  userId: number;
  user?: IUser;
  createdAt: Date;
  updatedAt: Date;
}

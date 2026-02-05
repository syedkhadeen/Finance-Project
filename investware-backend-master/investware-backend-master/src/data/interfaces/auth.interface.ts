export interface AuthUser {
  id: string;
  email: string;
  role: string;
  firm_id?: number;
}

export interface TokenData {
  token: string;
  expiresIn: number;
}

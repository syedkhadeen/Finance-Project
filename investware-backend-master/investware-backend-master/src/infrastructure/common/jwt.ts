import { jwtDecode } from 'jwt-decode';

type JwtPayload = {
  exp: number;
  sub: string;
  family_name: string;
  given_name: string;
  email: string;
};

export function parseJwt(token: string) {
  const decoded = jwtDecode<JwtPayload>(token);
  return decoded;
}

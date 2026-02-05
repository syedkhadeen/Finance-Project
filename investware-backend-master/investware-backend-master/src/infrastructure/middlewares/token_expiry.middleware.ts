import { HttpError } from 'routing-controllers';
import { parseJwt } from '@infrastructure/common/jwt';
import { getAuthorization } from './auth.middleware';
import { NextFunction, Request, Response } from 'express';
import { ERROR_MESSAGES } from '@infrastructure/common/statusCodes';

export const CheckTokenExpiry = (req: Request, _: Response, next: NextFunction) => {
  const Authorization = getAuthorization(req);
  if (!Authorization) {
    throw new HttpError(ERROR_MESSAGES.TOKEN_NOT_FOUND.statusCode, ERROR_MESSAGES.TOKEN_NOT_FOUND.message);
  }
  const { exp } = parseJwt(Authorization);
  const is_token_expired = exp * 1000 < Date.now();
  if (is_token_expired) {
    throw new HttpError(ERROR_MESSAGES.TOKEN_NOT_FOUND.statusCode, ERROR_MESSAGES.TOKEN_NOT_FOUND.message);
  }
  next();
};

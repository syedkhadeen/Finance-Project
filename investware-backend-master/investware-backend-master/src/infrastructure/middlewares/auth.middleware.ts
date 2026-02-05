import { parseJwt } from '@infrastructure/common/jwt';
import { Action } from 'routing-controllers';

// Utility function to extract token from the Authorization header
export const getAuthorization = req => {
  const header = req.header('Authorization');
  if (header) return header.split('Bearer ')[1];
  return null;
};

export const AuthMiddleware = async (action: Action): Promise<boolean> => {
  try {
    const req = action.request;
    const Authorization = getAuthorization(req);

    if (Authorization) {
      const { exp, email, family_name, given_name, sub } = parseJwt(Authorization);
      action.request.user = {
        exp,
        email,
        family_name,
        given_name,
        sub,
      };

      if (!action.request.user) return false;

      if (action.request.user) return true;

      return false;
    }
    return false;
  } catch {
    return false;
  }
};

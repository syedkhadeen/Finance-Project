import { parseJwt } from '@infrastructure/common/jwt';
import { Action } from 'routing-controllers';
import database from '@config/database';

// Utility function to extract token from the Authorization header
export const getAuthorization = req => {
  if (!req || !req.headers) return null;
  const header = req.headers.authorization || req.headers.Authorization;
  if (header) return header.split('Bearer ')[1];
  return null;
};

export const AuthMiddleware = async (action: Action): Promise<boolean> => {
  try {
    if (!action || !action.request) {
      console.error('Auth middleware error: action or action.request is undefined');
      return false;
    }

    const req = action.request;
    const Authorization = getAuthorization(req);

    if (Authorization) {
      const { exp, email, family_name, given_name, sub } = parseJwt(Authorization);
      
      // Get or create user in database
      let dbUser = await database.instance.users.findUnique({ 
        where: { auth_id: sub } 
      });

      if (!dbUser) {
        // Create user if doesn't exist
        dbUser = await database.instance.users.create({
          data: {
            auth_id: sub,
            email: email,
            firstName: given_name,
            lastName: family_name,
          },
        });
      }

      action.request.user = {
        id: dbUser.id,
        exp,
        email,
        family_name,
        given_name,
        sub,
      };

      return true;
    }
    return false;
  } catch (error) {
    console.error('Auth middleware error:', error);
    return false;
  }
};

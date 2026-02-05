export const ERROR_MESSAGES = {
  BAD_REQUEST: {
    statusCode: 400,
    message: 'Bad Request. The server could not understand the request due to invalid syntax.',
  },
  UNAUTHORIZED: {
    statusCode: 401,
    message: 'Unauthorized. The client must authenticate itself to get the requested response.',
  },
  FORBIDDEN: {
    statusCode: 403,
    message: 'Forbidden. The client does not have access rights to the content.',
  },
  NOT_FOUND: {
    statusCode: 404,
    message: 'Not Found. The server can not find the requested resource.',
  },
  METHOD_NOT_ALLOWED: {
    statusCode: 405,
    message: 'Method Not Allowed. The request method is known by the server but has been disabled and cannot be used.',
  },
  CONFLICT: {
    statusCode: 409,
    message: 'Conflict. The request could not be completed due to a conflict with the current state of the target resource.',
  },
  INTERNAL_SERVER_ERROR: {
    statusCode: 500,
    message: "Internal Server Error. The server has encountered a situation it doesn't know how to handle.",
  },
  BAD_GATEWAY: {
    statusCode: 502,
    message: 'Bad Gateway. The server, while acting as a gateway or proxy, received an invalid response from the upstream server.',
  },
  SERVICE_UNAVAILABLE: {
    statusCode: 503,
    message: 'Service Unavailable. The server is not ready to handle the request.',
  },
  GATEWAY_TIMEOUT: {
    statusCode: 504,
    message: 'Gateway Timeout. The server, while acting as a gateway or proxy, did not receive a timely response from the upstream server.',
  },
  USER_NOT_CONFIRMED: {
    statusCode: 400,
    message: 'User not confirmed. Please verify',
  },
  USER_NOT_DELETED: {
    statusCode: 400,
    message: 'User not deleted',
  },
  INVALID_USER_ATTRIBUTES: {
    statusCode: 400,
    message: 'Invalid User Attributes',
  },
  INVALID_USER_ID: {
    statusCode: 502,
    message: 'Invalid or undefined user id. Please verify.',
  },

  INVALID_CREDENTIALS: {
    statusCode: 401,
    message: 'Invalid credentials. Please check your username and password.',
  },
  PASSWORD_RESET_FAILED: {
    statusCode: 500,
    message: 'Password reset failed. Please try again later.',
  },
  USER_ALREADY_EXISTS: {
    statusCode: 409,
    message: 'User already exists. Please use a different email.',
  },
  USER_NOT_FOUND: {
    statusCode: 404,
    message: 'User not found. Please check the provided information.',
  },
  INVALID_EMAIL_FORMAT: {
    statusCode: 400,
    message: 'Invalid email format. Please provide a valid email address.',
  },
  TOKEN_NOT_VERFIED: {
    statusCode: 400,
    message: 'Invalid token format. Please provide a valid token',
  },
  TOKEN_NOT_FOUND: {
    statusCode: 400,
    message: 'Token not found',
  },
  TOKEN_ALREADY_EXIST: {
    statusCode: 400,
    message: 'token already exist',
  },
  PASSWORD_DO_NOT_MATCH: {
    statusCode: 400,
    message: 'passwords do not match',
  },
  // Success Messages
  PROFILE_UPDATED: {
    statusCode: 200,
    message: 'Profile updated successfully.',
  },
  EMAIL_VERIFIED: {
    statusCode: 200,
    message: 'Email verified successfully.',
  },
  FAILED_TO_CREATE_USER: {
    statusCode: 500,
    message: 'Failed to create the user. ',
  },
};

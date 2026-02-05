import { App } from './app';
import { ValidateEnv } from '@infrastructure/common/validateEnv';
import { AuthController } from '@infrastructure/controllers/auth.controller';
import { UserController } from '@infrastructure/controllers/users.controller';
import { HealthCheckController } from '@infrastructure/controllers/healthcheck.controller';
import { TradeController } from '@infrastructure/controllers/trade.controller';

ValidateEnv();

const app = new App([AuthController, UserController, HealthCheckController, TradeController]);

app.listen();

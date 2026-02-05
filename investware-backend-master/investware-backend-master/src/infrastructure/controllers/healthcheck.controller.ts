import { HttpCode, Get, Controller } from 'routing-controllers';

@Controller('/health-check')
export class HealthCheckController {
  @Get('/')
  @HttpCode(200)
  confirmHealthCheck() {
    return true;
  }
}

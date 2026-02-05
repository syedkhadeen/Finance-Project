import { IsNotEmpty } from 'class-validator';
import { SinkDTO } from './sink.dto';

export class AddOnsDTO {
  @IsNotEmpty({ each: true })
  readonly sinks: SinkDTO[];
}

import { WebPlugin } from '@capacitor/core';

import type { SumUpPlugin } from './definitions';

export class SumUpWeb extends WebPlugin implements SumUpPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}

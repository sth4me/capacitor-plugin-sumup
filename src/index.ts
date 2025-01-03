import { registerPlugin } from '@capacitor/core';

import type { SumUpPlugin } from './definitions';

const SumUp = registerPlugin<SumUpPlugin>('SumUpPlugin', {
  web: () => import('./web').then((m) => new m.SumUpWeb()),
});

export * from './definitions';
export { SumUp };


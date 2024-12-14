export interface SumUpPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}

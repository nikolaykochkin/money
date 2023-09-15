export interface User {
  id: bigint;
  name: string;
  login: string;
  password?: string;
  telegramId?: bigint;
}
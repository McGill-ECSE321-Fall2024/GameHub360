import { UserType } from './UserType';

export type LoginUser = {
  id: number | null;
  email: string;
  password: string;
  userType: UserType;
};

import { UserType } from './UserType';

export type LoginUser = {
  email: string;
  password: string;
  userType: UserType;
};

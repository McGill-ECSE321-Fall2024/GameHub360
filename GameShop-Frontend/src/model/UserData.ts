import { UserType } from '../model/user/UserType';

export interface StoredUserData {
  email: string;
  customerId?: number;
  staffId?: number;
  userType: UserType;
}

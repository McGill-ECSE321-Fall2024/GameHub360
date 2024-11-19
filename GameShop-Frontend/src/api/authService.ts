import { isAxiosError } from 'axios';
import { LoginUser } from '../model/user/LoginUser';
import { UserType } from '../model/user/UserType';
import apiService from '../config/axiosConfig';

export async function login(user: LoginUser) {
  const { email, password, userType } = user;

  let endpoint = '';

  switch (userType) {
    case UserType.MANAGER:
      endpoint = '/manager/login';
      break;
    case UserType.EMPLOYEE:
      endpoint = '/employees/login';
      break;
    case UserType.CUSTOMER:
      endpoint = '/customers/login';
      break;
    default:
      throw new Error('Invalid user type');
  }

  try {
    const response = await apiService.post(endpoint, { email, password });
    return response.data;
  } catch (error) {
    if (isAxiosError(error) && error.response?.status === 400) {
      throw new Error('Invalid email or password.');
    } else {
      throw new Error('An unexpected error occurred. Please try again.');
    }
  }
}

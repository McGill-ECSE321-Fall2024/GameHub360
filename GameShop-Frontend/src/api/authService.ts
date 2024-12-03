import { isAxiosError } from 'axios';
import { LoginUser } from '../model/user/LoginUser';
import { UserType } from '../model/user/UserType';
import apiService from '../config/axiosConfig';
import { SignupUser } from '../model/user/SignupUser';

export interface StoredUserData {
  email: string;
  customerId?: number;
  staffId?: number;
  userType: UserType;
}

// Function to log in a user
export async function login(user: LoginUser) {
  const { email, password, userType } = user;

  let endpoint = '';

  // Hit the appropriate endpoint based on the user type
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
    // Make a POST request to the appropriate endpoint
    const response = await apiService.post(endpoint, { email, password });
    
    let userData: StoredUserData = {
      email,
      userType
    };

    if (userType === UserType.CUSTOMER) {
      userData.customerId = response.data.customerId;
    } else if (userType === UserType.EMPLOYEE || userType === UserType.MANAGER) {
      userData.staffId = response.data.staffId;
    }
    
    return userData;
  } catch (error) {
    if (isAxiosError(error) && error.response?.status === 400) {
      throw new Error('Invalid email or password.');
    } else {
      throw new Error('An unexpected error occurred. Please try again.');
    }
  }
}

// Function to create a customer account
export async function registerCustomer(user: SignupUser) {
  try {
    // Make a POST request to the customer registration endpoint
    const response = await apiService.post('/customers/', user);
    return response.data;
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 409) {
        throw new Error('An account with this email already exists.');
      } else if (error.response?.status === 400) {
        throw new Error('Validation error: Please check the input fields.');
      }
    }
    throw new Error('An unexpected error occurred. Please try again.');
  }
}

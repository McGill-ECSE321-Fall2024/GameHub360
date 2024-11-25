import { AuthState } from '../model/AuthState';
import { LoginUser } from '../model/user/LoginUser';
import { UserType } from '../model/user/UserType';

/*
Note for TAs: we know that this authentication system is not secure, but we are not focusing on security in this project. 
*/

const LOCAL_STORAGE_KEY = 'loggedInUser';

// Function to get the current authentication state
export const getAuthState: () => AuthState = () => {
  const storedUser = localStorage.getItem(LOCAL_STORAGE_KEY);
  if (storedUser) {
    const user: LoginUser = JSON.parse(storedUser);
    switch (user.userType) {
      case UserType.CUSTOMER:
        return AuthState.CUSTOMER;
      case UserType.EMPLOYEE:
        return AuthState.EMPLOYEE;
      case UserType.MANAGER:
        return AuthState.MANAGER;
      default:
        return AuthState.UNAUTHENTICATED;
    }
  }
  return AuthState.UNAUTHENTICATED;
};

// Function to set the logged-in user in local storage
export const setAuthUser = (user: LoginUser) => {
  localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(user));
};

// Utility function to retrieve the logged-in user object from localStorage
export const getAuthUser = () => {
  const storedUser = localStorage.getItem('loggedInUser');
  return storedUser ? JSON.parse(storedUser) : null;
};

// Function to remove the logged-in user from local storage (logout)
export const clearAuthUser = () => {
  localStorage.removeItem(LOCAL_STORAGE_KEY);
};

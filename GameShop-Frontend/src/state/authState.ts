import { AuthState } from '../model/AuthState';

export const getAuthState: () => AuthState = () => {
  return AuthState.UNAUTHENTICATED;
};

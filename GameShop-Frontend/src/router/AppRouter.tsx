import { BrowserRouter } from 'react-router-dom';
import { AuthState } from '../model/AuthState';
import AuthRouter from './AuthRouter';
import CustomerRouter from './CustomerRouter';
import EmployeeRouter from './EmployeeRouter';
import ManagerRouter from './ManagerRouter';
import { getAuthState } from '../state/authState';
import { useState, useEffect } from 'react';

const AppRouter = () => {
  const [authState, setAuthState] = useState<AuthState>(getAuthState());

  // Effect to update authState when local storage changes
  useEffect(() => {
    const handleStorageChange = () => {
      setAuthState(getAuthState());
    };

    // Listen for storage changes
    window.addEventListener('storage', handleStorageChange);

    // Cleanup listener on unmount
    return () => {
      window.removeEventListener('storage', handleStorageChange);
    };
  }, []);

  let RouterComponent;

  switch (authState) {
    case AuthState.CUSTOMER:
      RouterComponent = <CustomerRouter />;
      break;
    case AuthState.EMPLOYEE:
      RouterComponent = <EmployeeRouter />;
      break;
    case AuthState.MANAGER:
      RouterComponent = <ManagerRouter />;
      break;
    case AuthState.UNAUTHENTICATED:
    default:
      RouterComponent = <AuthRouter />;
      break;
  }

  return <BrowserRouter>{RouterComponent}</BrowserRouter>;
};

export default AppRouter;

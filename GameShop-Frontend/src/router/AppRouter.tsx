import { BrowserRouter } from 'react-router-dom';
import { AuthState } from '../model/AuthState';
import AuthRouter from './AuthRouter';
import CustomerRouter from './CustomerRouter';
import EmployeeRouter from './EmployeeRouter';
import ManagerRouter from './ManagerRouter';
import { getAuthState } from '../state/authState';
import { useState, useEffect } from 'react';
import { CartProvider } from '../Context/CartContext';
import { WishlistProvider } from '../Context/WishlistContext';
import { ToastProvider } from '../Context/ToastContext';

const AppRouter = () => {
  const [authState, setAuthState] = useState<AuthState>(getAuthState());

  useEffect(() => {
    const handleStorageChange = () => {
      setAuthState(getAuthState());
    };

    window.addEventListener('storage', handleStorageChange);

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

  return (
    <BrowserRouter>
      <ToastProvider>
        <CartProvider>
          <WishlistProvider>
            {RouterComponent}
          </WishlistProvider>
        </CartProvider>
      </ToastProvider>
    </BrowserRouter>
  );
};

export default AppRouter;

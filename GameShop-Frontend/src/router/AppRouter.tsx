import { BrowserRouter } from 'react-router-dom';
import { AuthState } from '../model/AuthState';
import AuthRouter from './AuthRouter';
import CustomerRouter from './CustomerRouter';
import EmployeeRouter from './EmployeeRouter';
import ManagerRouter from './ManagerRouter';
import { getAuthState } from '../state/authState';
import { useState, useEffect } from 'react';
import { CartProvider } from '../context/CartContext';
import { WishlistProvider } from '../context/WishlistContext';
import { ToastProvider } from '../context/ToastContext';

const AppRouter = () => {
  // Initialize auth state from local storage
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

  // Determine which router to show based on auth state
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

  // Wrap router with necessary providers
  return (
    <BrowserRouter>
      <ToastProvider>
        <CartProvider>
          <WishlistProvider>{RouterComponent}</WishlistProvider>
        </CartProvider>
      </ToastProvider>
    </BrowserRouter>
  );
};

export default AppRouter;

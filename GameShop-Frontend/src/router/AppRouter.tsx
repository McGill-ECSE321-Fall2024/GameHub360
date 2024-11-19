import { BrowserRouter } from 'react-router-dom';
import { AuthState } from '../model/AuthState';
import AuthRouter from './AuthRouter';
import CustomerRouter from './CustomerRouter';
import EmployeeRouter from './EmployeeRouter';
import ManagerRouter from './ManagerRouter';
import { getAuthState } from '../state/authState';

const AppRouter = () => {
  const authState = getAuthState();

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

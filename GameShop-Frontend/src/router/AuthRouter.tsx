import { Route, Routes, Navigate } from 'react-router-dom';
import { GeneralRouteNames } from '../model/routeNames/GeneralRouteNames';
import LoginPage from '../pages/auth/LoginPage';
import SignupPage from '../pages/auth/SignupPage';

const AuthRouter = () => {
  return (
    <>
      <Routes>
        <Route path={GeneralRouteNames.LOGIN} element={<LoginPage />} />
        <Route path={GeneralRouteNames.SIGNUP} element={<SignupPage />} />

        {/* Redirect all other routes to login */}
        <Route
          path="*"
          element={<Navigate to={GeneralRouteNames.LOGIN} replace />}
        />
      </Routes>
    </>
  );
};

export default AuthRouter;

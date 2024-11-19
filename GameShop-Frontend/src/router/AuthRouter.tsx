import { Route, Routes, Navigate } from 'react-router-dom';
import { RouteNames } from '../model/RouteNames';
import LoginPage from '../pages/LoginPage';
import SignupPage from '../pages/SignupPage';

const AuthRouter = () => {
  return (
    <>
      <Routes>
        <Route path={RouteNames.LOGIN} element={<LoginPage />} />
        <Route path={RouteNames.SIGNUP} element={<SignupPage />} />

        {/* Redirect all other routes to login */}
        <Route path="*" element={<Navigate to={RouteNames.LOGIN} replace />} />
      </Routes>
    </>
  );
};

export default AuthRouter;

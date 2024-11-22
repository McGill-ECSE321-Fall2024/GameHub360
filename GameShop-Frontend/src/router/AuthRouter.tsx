import { Route, Routes, Navigate } from 'react-router-dom';
import { GeneralRouteNames } from '../model/routeNames/GeneralRouteNames';
import LoginPage from '../pages/auth/LoginPage';
import SignupPage from '../pages/auth/SignupPage';
import BrowsePage from '../pages/BrowsePage';
import Navbar from '../components/Navbar';

const AuthRouter = () => {
  return (
    <>
      <Navbar userType={null} />
      <Routes>
        <Route path={GeneralRouteNames.BROWSE} element={<BrowsePage />} />
        <Route path={GeneralRouteNames.LOGIN} element={<LoginPage />} />
        <Route path={GeneralRouteNames.SIGNUP} element={<SignupPage />} />

        {/* Redirect base route to browse */}
        <Route
          path={GeneralRouteNames.BASE}
          element={<Navigate to={GeneralRouteNames.BROWSE} replace />}
        />

        {/* TEMP (will need a 404)*/}
        <Route
          path="*"
          element={<Navigate to={GeneralRouteNames.BROWSE} replace />}
        />
      </Routes>
    </>
  );
};

export default AuthRouter;

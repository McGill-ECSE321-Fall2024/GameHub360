import { Route, Routes } from 'react-router-dom';
import { GeneralRouteNames } from '../model/routeNames/GeneralRouteNames';
import LoginPage from '../pages/auth/LoginPage';
import SignupPage from '../pages/auth/SignupPage';
import BrowsePage from '../pages/BrowsePage';
import Navbar from '../components/Navbar';
import NotFoundPage from '../pages/NotFoundPage';
import GameDetailsPage from '../pages/GameDetailsPage';
import LandingPage from '../pages/LandingPage';

const AuthRouter = () => {
  return (
    <>
      <Navbar userType={null} />
      <Routes>
        <Route path={GeneralRouteNames.BASE} element={<LandingPage />} />
        <Route path={GeneralRouteNames.BROWSE} element={<BrowsePage />} />
        <Route
          path={`${GeneralRouteNames.BROWSE}/game/:id`}
          element={<GameDetailsPage />}
        />
        <Route path={GeneralRouteNames.LOGIN} element={<LoginPage />} />
        <Route path={GeneralRouteNames.SIGNUP} element={<SignupPage />} />

        {/* 404 Page */}
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </>
  );
};

export default AuthRouter;

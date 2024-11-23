import { Navigate, Route, Routes } from 'react-router-dom';
import { GeneralRouteNames } from '../model/routeNames/GeneralRouteNames';
import Navbar from '../components/Navbar';
import { UserType } from '../model/user/UserType';
import ProfilePage from '../pages/ProfilePage';
import DashboardPage from '../pages/manager/DashboardPage';
import { ManagerRouteNames } from '../model/routeNames/ManagerRouteNames';
import BrowsePage from '../pages/BrowsePage';
import NotFoundPage from '../pages/NotFoundPage';
import GameDetailsPage from '../pages/GameDetailsPage';

const ManagerRouter = () => {
  return (
    <>
      <Navbar userType={UserType.MANAGER} />
      <Routes>
        <Route path={GeneralRouteNames.BROWSE} element={<BrowsePage />} />
        <Route path={`${GeneralRouteNames.BROWSE}/game/:id`} element={<GameDetailsPage />} />
        <Route path={GeneralRouteNames.PROFILE} element={<ProfilePage />} />

        <Route path={ManagerRouteNames.DASHBOARD} element={<DashboardPage />} />
        {/* Continue adding routes here */}

        {/* Redirect base route to browse */}
        <Route
          path={GeneralRouteNames.BASE}
          element={<Navigate to={GeneralRouteNames.BROWSE} replace />}
        />

        {/* 404 Page */}
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </>
  );
};

export default ManagerRouter;

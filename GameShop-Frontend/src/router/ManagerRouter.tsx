import { Navigate, Route, Routes } from 'react-router-dom';
import { GeneralRouteNames } from '../model/routeNames/GeneralRouteNames';
import Navbar from '../components/Navbar';
import { UserType } from '../model/user/UserType';
import ProfilePage from '../pages/ProfilePage';
import DashboardPage from '../pages/manager/DashboardPage';
import { ManagerRouteNames } from '../model/routeNames/ManagerRouteNames';
import BrowsePage from '../pages/BrowsePage';
import GameRequestsPage from '../pages/manager/GameRequestsPage';
import GamesPage from '../pages/manager/GamesPage';
import ManagerProfilePage from '../pages/manager/ManagerProfilePage';
import CategoriesPage from '../pages/manager/CategoriesPage';
import NotFoundPage from '../pages/NotFoundPage';
import GameRequestsPage from '../pages/manager/GameRequestsPage';
import GamesPage from '../pages/manager/GamesPage';
import ManagerProfilePage from '../pages/manager/ManagerProfilePage';
import CategoriesPage from '../pages/manager/CategoriesPage';
import NotFoundPage from '../pages/NotFoundPage';

const ManagerRouter = () => {
  return (
    <>
      <Navbar userType={UserType.MANAGER} />
      <Routes>
        <Route path={GeneralRouteNames.BROWSE} element={<BrowsePage />} />
        <Route path={GeneralRouteNames.PROFILE} element={<ManagerProfilePage />} />

        <Route path={ManagerRouteNames.DASHBOARD} element={<DashboardPage />} />
        <Route path={ManagerRouteNames.GAME_REQUESTS} element={<GameRequestsPage />} />
        <Route path={ManagerRouteNames.GAMES} element={<GamesPage />} />
        <Route path={ManagerRouteNames.CATEGORIES} element={<CategoriesPage />} />


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

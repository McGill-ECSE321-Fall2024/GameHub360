import { Navigate, Route, Routes } from 'react-router-dom';
import { GeneralRouteNames } from '../model/routeNames/GeneralRouteNames';
import Navbar from '../components/Navbar';
import { UserType } from '../model/user/UserType';
import DashboardPage from '../pages/manager/DashboardPage';
import { ManagerRouteNames } from '../model/routeNames/ManagerRouteNames';
import BrowsePage from '../pages/BrowsePage';
import GameRequestsPage from '../pages/manager/ManagerGameRequestsPage';
import GamesPage from '../pages/manager/ManagerGamesPage';
import ManagerProfilePage from '../pages/manager/ManagerProfilePage';
import CategoriesPage from '../pages/manager/ManagerCategoriesPage';
import NotFoundPage from '../pages/NotFoundPage';
import GameRequestDetailPage from '../pages/manager/ManagerGameRequestDetailPage';
import CreateGamePage from '../pages/manager/CreateGamePage';
import GameDetailPage from '../pages/manager/ManagerGameDetailPage';
import CreateGameCategoryPage from '../pages/manager/ManagerCreateCategoryPage';
import GameCategoryDetailPage from '../pages/manager/ManagerCategoryDetailPage';

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
        <Route path={ManagerRouteNames.GAME_REQUEST_DETAIL} element={<GameRequestDetailPage />} />
        <Route path={ManagerRouteNames.GAME_DETAIL} element={<GameDetailPage />} />
        <Route path={ManagerRouteNames.CREATE_GAME} element={<CreateGamePage />} />
        <Route path={ManagerRouteNames.CREATE_CATEGORY} element={<CreateGameCategoryPage />} />
        <Route path={ManagerRouteNames.CATEGORY_DETAIL} element={<GameCategoryDetailPage />} />


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

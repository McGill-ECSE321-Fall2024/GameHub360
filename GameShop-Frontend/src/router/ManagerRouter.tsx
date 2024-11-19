import { Navigate, Route, Routes } from 'react-router-dom';
import { GeneralRouteNames } from '../model/routeNames/GeneralRouteNames';
import Navbar from '../components/Navbar';
import { UserType } from '../model/user/UserType';
import HomePage from '../pages/HomePage';
import ProfilePage from '../pages/ProfilePage';
import DashboardPage from '../pages/dashboard/DashboardPage';
import { ManagerRouteNames } from '../model/routeNames/ManagerRouteNames';

const ManagerRouter = () => {
  return (
    <>
      <Navbar userType={UserType.MANAGER} />
      <Routes>
        <Route path={GeneralRouteNames.BASE} element={<HomePage />} />
        <Route path={GeneralRouteNames.PROFILE} element={<ProfilePage />} />

        <Route path={ManagerRouteNames.DASHBOARD} element={<DashboardPage />} />
        {/* Continue adding routes here */}

        {/* Redirect all other routes to Base --> temporary! (will need a 404) */}
        <Route
          path="*"
          element={<Navigate to={GeneralRouteNames.BASE} replace />}
        />
      </Routes>
    </>
  );
};

export default ManagerRouter;

import { Navigate, Route, Routes } from 'react-router-dom';
import { GeneralRouteNames } from '../model/routeNames/GeneralRouteNames';
import Navbar from '../components/Navbar';
import { UserType } from '../model/user/UserType';
import HomePage from '../pages/HomePage';
import ProfilePage from '../pages/ProfilePage';
import GameRequestsPage from '../pages/employee/GameRequestsPage';
import { EmployeeRouteNames } from '../model/routeNames/EmployeeRouteNames';

const EmployeeRouter = () => {
  return (
    <>
      <Navbar userType={UserType.EMPLOYEE} />
      <Routes>
        <Route path={GeneralRouteNames.BASE} element={<HomePage />} />
        <Route path={GeneralRouteNames.PROFILE} element={<ProfilePage />} />

        <Route
          path={EmployeeRouteNames.GAME_REQUESTS}
          element={<GameRequestsPage />}
        />
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

export default EmployeeRouter;

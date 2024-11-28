import { Navigate, Route, Routes } from 'react-router-dom';
import { GeneralRouteNames } from '../model/routeNames/GeneralRouteNames';
import Navbar from '../components/Navbar';
import { UserType } from '../model/user/UserType';
import EmployeeProfilePage from '../pages/employee/EmployeeProfilePage';
import EmployeeCategoriesPage from '../pages/employee/EmployeeCategoriesPage';
import EmployeeCategoryDetailPage from '../pages/employee/EmployeeCategoryDetailPage';
import EmployeeGameRequestsPage from '../pages/employee/EmployeeGameRequestsPage';
import EmployeeGameRequestDetailPage from '../pages/employee/EmployeeGameRequestDetailPage';
import EmployeeGameRequestCreatePage from '../pages/employee/EmployeeGameRequestCreatePage';
import { EmployeeRouteNames } from '../model/routeNames/EmployeeRouteNames';
import BrowsePage from '../pages/BrowsePage';
import GameDetailsPage from '../pages/GameDetailsPage';

const EmployeeRouter = () => {
  return (
    <>
      <Navbar userType={UserType.EMPLOYEE} />
      <Routes>
        <Route path={GeneralRouteNames.BROWSE} element={<BrowsePage />} />
        <Route path={GeneralRouteNames.PROFILE} element={<EmployeeProfilePage />} />

        <Route path="/games/:id" element={<GameDetailsPage />} />

        <Route
          path={EmployeeRouteNames.GAME_REQUESTS}
          element={<EmployeeGameRequestsPage />}
        />

        <Route
          path={EmployeeRouteNames.GAME_REQUEST_DETAIL}
          element={<EmployeeGameRequestDetailPage />} />

        <Route
          path={EmployeeRouteNames.GAME_REQUEST_CREATE}
          element={<EmployeeGameRequestCreatePage />} /> 

        
        <Route
          path={EmployeeRouteNames.CATEGORIES}
          element={<EmployeeCategoriesPage />} />

        <Route
          path={EmployeeRouteNames.CATEGORY_DETAIL}
          element={<EmployeeCategoryDetailPage />} />

        {/* Redirect base route to browse */}
        <Route
          path={GeneralRouteNames.BASE}
          element={<Navigate to={GeneralRouteNames.BROWSE} replace />}
        />

        {/* TEMP (will need a 404)*/}
        <Route
          path="*"
          element={<Navigate to={GeneralRouteNames.BASE} replace />}
        />
      </Routes>
    </>
  );
};

export default EmployeeRouter;

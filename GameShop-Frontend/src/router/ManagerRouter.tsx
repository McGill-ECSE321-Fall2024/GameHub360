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
import EmployeesPage from '../pages/manager/ManagerEmployeesPage';
import EmployeeDetailsPage from '../pages/manager/ManagerEmployeeDetailsPage';
import CreateEmployeePage from '../pages/manager/ManagerCreateEmployeePage';
import PromotionsPage from '../pages/manager/ManagerPromotionsPage';
import CreatePromotionPage from '../pages/manager/ManagerCreatePromotionPage';
import ReviewsPage from '../pages/manager/ManagerReviewsPage';
import ReviewDetailsPage from '../pages/manager/ManagerReviewDetailsPage';

const ManagerRouter = () => {
  return (
    <>
      <Navbar userType={UserType.MANAGER} />
      <Routes>
        <Route path={GeneralRouteNames.BROWSE} element={<BrowsePage />} />
        <Route path="/games/:id" element={<GameDetailsPage />} />
        <Route path={GeneralRouteNames.PROFILE} element={<ProfilePage />} />
        <Route path={ManagerRouteNames.EMPLOYEE_DETAILS} element={<EmployeeDetailsPage />} />
        <Route path={ManagerRouteNames.DASHBOARD} element={<DashboardPage />} />
          <Route path={ManagerRouteNames.EMPLOYEES} element={<EmployeesPage />} />
          <Route path={ManagerRouteNames.CREATE_EMPLOYEE} element={<CreateEmployeePage />} />
          <Route path={ManagerRouteNames.PROMOTIONS} element={<PromotionsPage />} />
          <Route path={ManagerRouteNames.CREATE_PROMOTION} element={<CreatePromotionPage />} />
          <Route path={ManagerRouteNames.REVIEWS} element={<ReviewsPage />} />
          <Route path={`${ManagerRouteNames.REVIEW_DETAILS}/:id`} element={<ReviewDetailsPage />} />

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

import { Navigate, Route, Routes } from 'react-router-dom';
import { GeneralRouteNames } from '../model/routeNames/GeneralRouteNames';
import Navbar from '../components/Navbar';
import { UserType } from '../model/user/UserType';
import HomePage from '../pages/HomePage';
import ProfilePage from '../pages/ProfilePage';
import { CustomerRouteNames } from '../model/routeNames/CustomerRouteNames';
import OrdersPage from '../pages/customer/OrdersPage';
import WishlistPage from '../pages/customer/WishlistPage';

const CustomerRouter = () => {
  return (
    <>
      <Navbar userType={UserType.CUSTOMER} />
      <Routes>
        <Route path={GeneralRouteNames.BASE} element={<HomePage />} />
        <Route path={GeneralRouteNames.PROFILE} element={<ProfilePage />} />

        <Route path={CustomerRouteNames.ORDERS} element={<OrdersPage />} />
        <Route path={CustomerRouteNames.WISHLIST} element={<WishlistPage />} />
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

export default CustomerRouter;

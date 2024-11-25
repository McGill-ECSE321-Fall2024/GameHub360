import { Navigate, Route, Routes } from 'react-router-dom';
import { GeneralRouteNames } from '../model/routeNames/GeneralRouteNames';
import Navbar from '../components/Navbar';
import { UserType } from '../model/user/UserType';
import ProfilePage from '../pages/ProfilePage';
import { CustomerRouteNames } from '../model/routeNames/CustomerRouteNames';
import OrdersPage from '../pages/customer/OrdersPage';
import WishlistPage from '../pages/customer/WishlistPage';
import BrowsePage from '../pages/BrowsePage';
import NotFoundPage from '../pages/NotFoundPage';
import GameDetailsPage from '../pages/GameDetailsPage';
import CartPage from '../pages/customer/CartPage';

const CustomerRouter = () => {
  return (
    <>
      <Navbar userType={UserType.CUSTOMER} />
      <Routes>
        <Route path={GeneralRouteNames.BROWSE} element={<BrowsePage />} />
        <Route path="/games/:id" element={<GameDetailsPage />} />
        <Route path={GeneralRouteNames.PROFILE} element={<ProfilePage />} />

        <Route path={CustomerRouteNames.ORDERS} element={<OrdersPage />} />
        <Route path={CustomerRouteNames.WISHLIST} element={<WishlistPage />} />
        <Route path={CustomerRouteNames.CART} element={<CartPage />} />
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

export default CustomerRouter;

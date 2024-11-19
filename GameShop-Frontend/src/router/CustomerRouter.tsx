import { Route, Routes } from 'react-router-dom';
import { RouteNames } from '../model/RouteNames';

const CustomerRouter = () => {
  return (
    <>
      <Routes>
        <Route path={RouteNames.BASE} element={<>Hello Customer</>} />
      </Routes>
    </>
  );
};

export default CustomerRouter;

import { Route, Routes } from 'react-router-dom';
import { RouteNames } from '../model/RouteNames';

const ManagerRouter = () => {
  return (
    <>
      <Routes>
        <Route path={RouteNames.BASE} element={<>Hello Manager</>} />
      </Routes>
    </>
  );
};

export default ManagerRouter;

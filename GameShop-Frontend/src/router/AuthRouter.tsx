import { Route, Routes } from 'react-router-dom';
import { RouteNames } from '../model/RouteNames';

const AuthRouter = () => {
  return (
    <>
      <Routes>
        <Route path={RouteNames.BASE} element={<>Hello Auth</>} />
      </Routes>
    </>
  );
};

export default AuthRouter;

import { Route, Routes } from 'react-router-dom';
import { RouteNames } from '../model/RouteNames';

const EmployeeRouter = () => {
  return (
    <>
      <Routes>
        <Route path={RouteNames.BASE} element={<>Hello Employee</>} />
      </Routes>
    </>
  );
};

export default EmployeeRouter;

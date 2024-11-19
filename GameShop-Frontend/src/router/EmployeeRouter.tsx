import { Navigate, Route, Routes } from 'react-router-dom';
import { RouteNames } from '../model/RouteNames';

const EmployeeRouter = () => {
  return (
    <>
      <Routes>
        <Route path={RouteNames.BASE} element={<>Hello Employee</>} />

        {/* Redirect all other routes to Base --> temporary! (will need a 404) */}
        <Route path="*" element={<Navigate to={RouteNames.BASE} replace />} />
      </Routes>
    </>
  );
};

export default EmployeeRouter;

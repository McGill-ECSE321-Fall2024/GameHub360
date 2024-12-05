import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAllEmployees } from '../../api/employeeService'; // Replace with your actual API call file
import { ManagerRouteNames } from '../../model/routeNames/ManagerRouteNames'; // Update if necessary

const ManagerEmployeesPage = () => {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [filter, setFilter] = useState<'ALL' | 'ACTIVE' | 'INACTIVE'>('ALL');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchEmployees = async () => {
      setLoading(true);
      setErrorMessage(null);

      try {
        const data = await getAllEmployees(); // Fetch all employees
        setEmployees(data);
      } catch (error) {
        console.error('Error fetching employees:', error);
        setErrorMessage('Failed to load employees.');
      } finally {
        setLoading(false);
      }
    };

    fetchEmployees();
  }, []);

  const filteredEmployees = employees.filter((employee: any) => {
    if (filter === 'ALL') return true;
    return filter === 'ACTIVE' ? employee.isActive : !employee.isActive;
  });

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-3xl font-semibold text-gray-800">Employees</h2>
        <button
          onClick={() => navigate(ManagerRouteNames.EMPLOYEE_CREATE)} // Navigate to the create employee page
          className="bg-blue-500 hover:bg-blue-600 text-white font-medium px-5 py-2 rounded-lg shadow-md transition duration-200"
        >
          Create Employee
        </button>
      </div>

      {errorMessage && (
        <div className="mb-4 p-4 text-red-700 bg-red-100 rounded-lg">
          {errorMessage}
        </div>
      )}

      {/* Filter Buttons */}
      <div className="mb-6 flex space-x-4">
        {['ALL', 'ACTIVE', 'INACTIVE'].map((status) => (
          <button
            key={status}
            className={`px-5 py-2 rounded-lg shadow-sm font-medium transition duration-200 ${
              filter === status
                ? 'bg-blue-500 text-white'
                : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
            }`}
            onClick={() => setFilter(status as 'ALL' | 'ACTIVE' | 'INACTIVE')}
          >
            {status}
          </button>
        ))}
      </div>

      {loading ? (
        <p className="text-gray-700 text-lg">Loading employees...</p>
      ) : filteredEmployees.length === 0 ? (
        <p className="text-gray-700 text-lg">No employees found.</p>
      ) : (
        <div className="overflow-x-auto bg-white rounded-lg shadow-md">
          <table className="min-w-full border-collapse">
            <thead className="bg-gray-100 border-b">
              <tr>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                  ID
                </th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                  Name
                </th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                  Email
                </th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                  Phone Number
                </th>
                <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                  Status
                </th>
              </tr>
            </thead>
            <tbody>
              {filteredEmployees.map((employee: any) => (
                <tr
                  key={employee.staffId}
                  className="border-b hover:bg-gray-50 cursor-pointer transition"
                  onClick={() =>
                    navigate(`/manager/employees/${employee.staffId}`)
                  }
                >
                  <td className="px-6 py-4 text-sm text-gray-800">
                    {employee.staffId}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-800">
                    {employee.name || 'N/A'}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-800">
                    {employee.email}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-800">
                    {employee.phoneNumber || 'N/A'}
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-800">
                    {employee.isActive ? 'Active' : 'Inactive'}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default ManagerEmployeesPage;

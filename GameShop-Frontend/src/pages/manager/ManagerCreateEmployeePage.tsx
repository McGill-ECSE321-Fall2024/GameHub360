import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

interface Employee {
    staffId: number;
    name: string;
    email: string;
    phoneNumber: string;
    isActive: boolean;
}

const ManagerEmployeePage: React.FC = () => {
    const [employees, setEmployees] = useState<Employee[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);
    const [showCreateForm, setShowCreateForm] = useState(false);
    const [formData, setFormData] = useState({
        name: "",
        email: "",
        password: "",
        phoneNumber: "",
        isActive: true,
    });
    const navigate = useNavigate();

    // Fetch employees
    useEffect(() => {
        const fetchEmployees = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await axios.get("http://localhost:8080/employees/create");
                setEmployees(response.data);
            } catch (err: any) {
                setError(err.response?.data?.message || "Failed to fetch employees.");
            } finally {
                setLoading(false);
            }
        };

        fetchEmployees();
    }, []);

    // Handle form data change
    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleCheckboxChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, isActive: event.target.checked });
    };

    // Handle form submission
    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        setError(null);

        try {
            const response = await axios.post("http://localhost:8080/employees", formData);
            setEmployees((prevEmployees) => [...prevEmployees, response.data]);
            setFormData({
                name: "",
                email: "",
                password: "",
                phoneNumber: "",
                isActive: true,
            });
            setShowCreateForm(false); // Close the form
        } catch (err: any) {
            setError(err.response?.data?.message || "Failed to create employee.");
        }
    };

    return (
        <div className="p-6 bg-gray-50 min-h-screen">
            {/* Header */}
            <div className="flex justify-between items-center mb-6">
                <h2 className="text-3xl font-semibold text-gray-800">Employees</h2>
                <button
                    className="bg-blue-500 hover:bg-blue-600 text-white font-medium px-5 py-2 rounded-lg shadow-md transition duration-200"
                    onClick={() => setShowCreateForm((prev) => !prev)}
                >
                    {showCreateForm ? "Close Form" : "Create Employee"}
                </button>
            </div>

            {/* Create Employee Form */}
            {showCreateForm && (
                <div className="bg-white p-6 rounded-lg shadow-md mb-6">
                    <h3 className="text-xl font-bold text-gray-800 mb-4">Create New Employee</h3>
                    {error && <p className="text-red-600 mb-4">{error}</p>}
                    <form onSubmit={handleSubmit} className="space-y-4">
                        <div>
                            <label className="block font-medium text-gray-700">Name:</label>
                            <input
                                type="text"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                className="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500"
                                required
                            />
                        </div>
                        <div>
                            <label className="block font-medium text-gray-700">Email:</label>
                            <input
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                className="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500"
                                required
                            />
                        </div>
                        <div>
                            <label className="block font-medium text-gray-700">Password:</label>
                            <input
                                type="password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                className="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500"
                                required
                            />
                        </div>
                        <div>
                            <label className="block font-medium text-gray-700">Phone Number:</label>
                            <input
                                type="text"
                                name="phoneNumber"
                                value={formData.phoneNumber}
                                onChange={handleChange}
                                className="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500"
                            />
                        </div>
                        <div className="flex items-center">
                            <input
                                type="checkbox"
                                name="isActive"
                                checked={formData.isActive}
                                onChange={handleCheckboxChange}
                                className="h-4 w-4 text-blue-600 border-gray-300 rounded"
                            />
                            <label className="ml-2 font-medium text-gray-700">Is Active</label>
                        </div>
                        <button
                            type="submit"
                            className="bg-green-500 hover:bg-green-600 text-white font-medium px-5 py-2 rounded-lg shadow-md transition duration-200"
                        >
                            Submit
                        </button>
                    </form>
                </div>
            )}

            {/* Employee Table */}
            {error && !showCreateForm && (
                <p className="text-red-600 mb-4">{error}</p>
            )}
            {loading ? (
                <p className="text-gray-700 text-lg">Loading employees...</p>
            ) : (
                <div className="overflow-x-auto bg-white rounded-lg shadow-md">
                    <table className="min-w-full border-collapse">
                        <thead className="bg-gray-100 border-b">
                        <tr>
                            <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">ID</th>
                            <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">Name</th>
                            <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">Email</th>
                            <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">Phone</th>
                            <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">Status</th>
                        </tr>
                        </thead>
                        <tbody>
                        {employees.map((employee) => (
                            <tr
                                key={employee.staffId}
                                className="border-b hover:bg-gray-50 cursor-pointer transition"
                                onClick={() => navigate(`/employees/${employee.staffId}`)}
                            >
                                <td className="px-6 py-4 text-sm text-gray-800">{employee.staffId}</td>
                                <td className="px-6 py-4 text-sm text-gray-800">{employee.name}</td>
                                <td className="px-6 py-4 text-sm text-gray-800">{employee.email}</td>
                                <td className="px-6 py-4 text-sm text-gray-800">{employee.phoneNumber}</td>
                                <td className="px-6 py-4 text-sm text-gray-800">
                                    {employee.isActive ? "Active" : "Inactive"}
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

export default ManagerEmployeePage;


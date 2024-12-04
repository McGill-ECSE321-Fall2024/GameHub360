import React, { useEffect, useState } from "react";

interface Employee {
    staffId: number;
    name: string;
    email: string;
    phoneNumber: string;
    isActive: boolean;
}

const EmployeesPage: React.FC = () => {
    const [employees, setEmployees] = useState<Employee[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [showCreateForm, setShowCreateForm] = useState(false);
    const [formData, setFormData] = useState({
        name: "",
        email: "",
        password: "",
        phoneNumber: "",
        isActive: true,
    });

    useEffect(() => {
        const fetchEmployees = async () => {
            try {
                const response = await fetch("http://localhost:8080/manager/employees");
                if (!response.ok) {
                    throw new Error("Failed to fetch employees.");
                }
                const data = await response.json();
                setEmployees(data);
            } catch (err: any) {
                setError(err.message);
            }
        };

        fetchEmployees();
    }, []);

    const handleFormChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleFormSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        try {
            const response = await fetch("http://localhost:8080/employees", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(formData),
            });
            if (!response.ok) {
                throw new Error("Failed to create employee.");
            }
            setShowCreateForm(false);
            setFormData({ name: "", email: "", password: "", phoneNumber: "", isActive: true });
            const newResponse = await fetch("http://localhost:8080/manager/employees");
            const newData = await newResponse.json();
            setEmployees(newData);
        } catch (err: any) {
            setError(err.message);
        }
    };

    return (
        <div className="p-6 bg-gray-50 min-h-screen">
            <div className="flex justify-between items-center mb-6">
                <h2 className="text-3xl font-semibold text-gray-800">Employees</h2>
                {!showCreateForm && (
                    <button
                        onClick={() => setShowCreateForm(true)}
                        className="bg-blue-500 hover:bg-blue-600 text-white font-medium px-5 py-2 rounded-lg shadow-md transition duration-200"
                    >
                        Create Employee
                    </button>
                )}
            </div>

            {error && (
                <div className="mb-4 p-4 text-red-700 bg-red-100 rounded-lg">
                    {error}
                </div>
            )}

            {showCreateForm ? (
                <div className="bg-white p-6 rounded-lg shadow-md mb-6">
                    <h3 className="text-2xl font-semibold mb-4">Create New Employee</h3>
                    <form onSubmit={handleFormSubmit}>
                        <div className="mb-4">
                            <label className="block text-gray-700">Name</label>
                            <input
                                type="text"
                                name="name"
                                value={formData.name}
                                onChange={handleFormChange}
                                className="w-full p-2 border rounded-md"
                                required
                            />
                        </div>
                        <div className="mb-4">
                            <label className="block text-gray-700">Email</label>
                            <input
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleFormChange}
                                className="w-full p-2 border rounded-md"
                                required
                            />
                        </div>
                        <div className="mb-4">
                            <label className="block text-gray-700">Password</label>
                            <input
                                type="password"
                                name="password"
                                value={formData.password}
                                onChange={handleFormChange}
                                className="w-full p-2 border rounded-md"
                                required
                            />
                        </div>
                        <div className="mb-4">
                            <label className="block text-gray-700">Phone Number</label>
                            <input
                                type="text"
                                name="phoneNumber"
                                value={formData.phoneNumber}
                                onChange={handleFormChange}
                                className="w-full p-2 border rounded-md"
                            />
                        </div>
                        <div className="mb-4">
                            <label className="block text-gray-700">Active</label>
                            <input
                                type="checkbox"
                                name="isActive"
                                checked={formData.isActive}
                                onChange={(e) =>
                                    setFormData({ ...formData, isActive: e.target.checked })
                                }
                            />
                        </div>
                        <button
                            type="submit"
                            className="bg-green-500 hover:bg-green-600 text-white font-medium px-4 py-2 rounded-lg shadow-md"
                        >
                            Submit
                        </button>
                    </form>
                    <button
                        onClick={() => setShowCreateForm(false)}
                        className="mt-4 bg-gray-500 hover:bg-gray-600 text-white font-medium px-4 py-2 rounded-lg shadow-md"
                    >
                        Return to Employees Page
                    </button>
                </div>
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
                                Phone
                            </th>
                            <th className="px-6 py-3 text-left text-sm font-semibold text-gray-600 uppercase">
                                Status
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        {employees.map((employee) => (
                            <tr key={employee.staffId} className="hover:bg-gray-50">
                                <td className="px-6 py-4">{employee.staffId}</td>
                                <td className="px-6 py-4">{employee.name}</td>
                                <td className="px-6 py-4">{employee.email}</td>
                                <td className="px-6 py-4">{employee.phoneNumber}</td>
                                <td className="px-6 py-4">
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

export default EmployeesPage;














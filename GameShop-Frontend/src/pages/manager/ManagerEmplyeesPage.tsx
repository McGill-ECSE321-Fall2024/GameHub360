import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";

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

    useEffect(() => {
        fetch("/employees")
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to fetch employees.");
                }
                return response.json();
            })
            .then((data) => setEmployees(data))
            .catch((err) => setError(err.message));
    }, []);

    return (
        <div>
            <h1>Employees</h1>
            <Link to="/employees/create">Create Employee</Link>
            {error && <p style={{ color: "red" }}>{error}</p>}
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {employees.map((employee) => (
                    <tr key={employee.staffId}>
                        <td>{employee.staffId}</td>
                        <td>{employee.name}</td>
                        <td>{employee.email}</td>
                        <td>{employee.phoneNumber}</td>
                        <td>{employee.isActive ? "Active" : "Inactive"}</td>
                        <td>
                            <Link to={`/employees/${employee.staffId}`}>Details</Link>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default EmployeesPage;

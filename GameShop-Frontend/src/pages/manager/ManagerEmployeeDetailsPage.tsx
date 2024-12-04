import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

interface Employee {
    staffId: number;
    name: string;
    email: string;
    phoneNumber: string;
    isActive: boolean;
}

const EmployeeDetailsPage: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const [employee, setEmployee] = useState<Employee | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchEmployeeDetails = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await fetch(`http://localhost:8080/employees/${id}`);
                if (!response.ok) {
                    throw new Error("Failed to fetch employee details.");
                }
                const data = await response.json();
                setEmployee(data);
            } catch (err: any) {
                console.error("Error fetching employee details:", err.message);
                setError("Failed to load employee details.");
            } finally {
                setLoading(false);
            }
        };

        fetchEmployeeDetails();
    }, [id]);

    if (loading) {
        return <p className="text-gray-700 text-lg">Loading...</p>;
    }

    if (error) {
        return (
            <div className="p-4 mb-4 text-red-700 bg-red-100 rounded-lg">
                {error}
            </div>
        );
    }

    if (!employee) {
        return (
            <p className="text-gray-700 text-lg">No employee details available.</p>
        );
    }

    return (
        <div className="p-6 bg-gray-50 min-h-screen">
            {/* Header */}
            <div className="flex justify-between items-center mb-6">
                <h2 className="text-3xl font-semibold text-gray-800">Employee Details</h2>
                <button
                    onClick={() => navigate(-1)}
                    className="bg-blue-500 hover:bg-blue-600 text-white font-medium px-5 py-2 rounded-lg shadow-md transition duration-200"
                >
                    Back
                </button>
            </div>

            {/* Employee Details */}
            <div className="bg-white p-6 rounded-lg shadow-md">
                <p className="mb-4">
                    <strong className="text-gray-700">ID:</strong> {employee.staffId}
                </p>
                <p className="mb-4">
                    <strong className="text-gray-700">Name:</strong> {employee.name}
                </p>
                <p className="mb-4">
                    <strong className="text-gray-700">Email:</strong> {employee.email}
                </p>
                <p className="mb-4">
                    <strong className="text-gray-700">Phone Number:</strong> {employee.phoneNumber}
                </p>
                <p>
                    <strong className="text-gray-700">Status:</strong>{" "}
                    {employee.isActive ? "Active" : "Inactive"}
                </p>
            </div>
        </div>
    );
};

export default EmployeeDetailsPage;

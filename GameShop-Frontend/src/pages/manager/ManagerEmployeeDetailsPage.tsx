import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

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
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        fetch(`/employees/${id}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to fetch employee details.");
                }
                return response.json();
            })
            .then((data) => setEmployee(data))
            .catch((err) => setError(err.message));
    }, [id]);

    if (error) {
        return <p style={{ color: "red" }}>{error}</p>;
    }

    if (!employee) {
        return <p>Loading...</p>;
    }

    return (
        <div>
            <h1>Employee Details</h1>
            <p>
                <strong>ID:</strong> {employee.staffId}
            </p>
            <p>
                <strong>Name:</strong> {employee.name}
            </p>
            <p>
                <strong>Email:</strong> {employee.email}
            </p>
            <p>
                <strong>Phone Number:</strong> {employee.phoneNumber}
            </p>
            <p>
                <strong>Status:</strong> {employee.isActive ? "Active" : "Inactive"}
            </p>
        </div>
    );
};

export default EmployeeDetailsPage;

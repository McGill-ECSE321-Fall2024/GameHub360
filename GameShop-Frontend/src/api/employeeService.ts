import { isAxiosError } from 'axios';
import apiService from '../config/axiosConfig';
import { EmployeeProfile } from '../model/employee/EmployeeProfile';

/**
 * Updates the Employee's profile. Email cannot be changed.
 * @param staffId The ID of the Employee to update.
 * @param profileData The updated Employee profile data (name, phoneNumber, password).
 * @returns A promise that resolves to the updated Employee's profile.
 */
export async function updateEmployeeProfile(
    staffId: number,
    profileData: {
        email: string;
        name?: string;
        phoneNumber?: string;
        password?: string;
    }
): Promise<EmployeeProfile> {
    try {
        const response = await apiService.put(`/employees/${staffId}`, profileData);
        return response.data as EmployeeProfile;
    } catch (error) {
        if (isAxiosError(error)) {
            if (error.response?.status === 404) {
                throw new Error('Employee account not found.');
            }
            if (error.response?.status === 400) {
                throw new Error(
                    'Invalid profile data. Ensure all fields meet the required criteria.'
                );
            }
            if (error.response?.status === 500) {
                throw new Error(
                    'Internal server error: Unable to update Employee profile.'
                );
            }
        }
        throw new Error(
            'An unexpected error occurred while updating the Employee profile.'
        );
    }  }

const EMPLOYEES_ENDPOINT = '/employees';

/**
 * Retrieves the Employee's profile details.
 * @param staffId The ID of the Employee to retrieve.
 * @returns A promise that resolves to the Employee's profile.
 */
export async function getEmployeeProfile(staffId: number): Promise<EmployeeProfile> {
    try {
        const response = await apiService.get(`${EMPLOYEES_ENDPOINT}/${staffId}`);
        return response.data as EmployeeProfile;
    } catch (error) {
        handleEmployeeApiError(error, 'retrieving profile');
    }
}

/**
 * Handles API errors for employee-related requests.
 * @param error The error object thrown by the axios request.
 * @param action The action being performed (e.g., 'retrieving', 'updating', 'creating').
 */
function handleEmployeeApiError(error: any, action: string): never {
    if (isAxiosError(error)) {
        if (error.response?.status === 404) {
            throw new Error(`Employee account not found while ${action}.`);
        }
        if (error.response?.status === 500) {
            throw new Error(`Internal server error: Unable to ${action} Employee profile.`);
        }
    }
    throw new Error(`An unexpected error occurred while ${action} the Employee.`);
}

/**
 * Creates a new Employee profile.
 * @param profileData The data for the new Employee (name, email, phoneNumber, password).
 * @returns A promise that resolves to the newly created Employee's profile.
 */
export async function createEmployeeProfile(
    profileData: {
        name: string;
        email: string;
        phoneNumber?: string;
        password: string;
    }
): Promise<EmployeeProfile> {
    try {
        const response = await apiService.post('/employees', profileData);
        return response.data as EmployeeProfile;
    } catch (error) {
        if (isAxiosError(error)) {
            if (error.response?.status === 400) {
                throw new Error('Invalid employee data. Ensure all fields meet the required criteria.');
            }
            if (error.response?.status === 500) {
                throw new Error('Internal server error: Unable to create employee.');
            }
        }
        throw new Error('An unexpected error occurred while creating the employee.');
    }
}

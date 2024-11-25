import { isAxiosError } from 'axios';
import apiService from '../config/axiosConfig';

export interface ManagerProfile {
  email: string;
  name?: string;
  phoneNumber?: string;
}

/**
 * Retrieves the manager's profile details. Only one manager exists in the system.
 * @returns A promise that resolves to the manager's profile.
 */
export async function getManagerProfile(): Promise<ManagerProfile> {
  try {
    const response = await apiService.get('/manager');
    return response.data as ManagerProfile;
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Manager account not found.');
      }
      if (error.response?.status === 500) {
        throw new Error(
          'Internal server error: Unable to retrieve manager profile.'
        );
      }
    }
    throw new Error(
      'An unexpected error occurred while retrieving the manager profile.'
    );
  }
}

/**
 * Updates the manager's profile. Email cannot be changed.
 * @param profileData The updated manager profile data (name, phoneNumber, password).
 * @returns A promise that resolves to the updated manager's profile.
 */
export async function updateManagerProfile(profileData: {
  email: string;
  name?: string;
  phoneNumber?: string;
  password?: string;
}): Promise<ManagerProfile> {
  try {
    const response = await apiService.put('/manager', profileData);
    return response.data as ManagerProfile;
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Manager account not found.');
      }
      if (error.response?.status === 400) {
        throw new Error(
          'Invalid profile data. Ensure all fields meet the required criteria.'
        );
      }
      if (error.response?.status === 500) {
        throw new Error(
          'Internal server error: Unable to update manager profile.'
        );
      }
    }
    throw new Error(
      'An unexpected error occurred while updating the manager profile.'
    );
  }
}

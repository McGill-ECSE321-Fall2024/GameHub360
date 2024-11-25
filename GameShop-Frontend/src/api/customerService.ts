import { isAxiosError } from 'axios';
import apiService from '../config/axiosConfig';

export interface CustomerProfile {
    customerId: number;
    email: string;
    name?: string;
    phoneNumber?: string;
  }

/**
 * Retrieves the customer's profile details.
 * @param customerId The ID of the customer to retrieve.
 * @returns A promise that resolves to the customer's profile.
 */
export async function getCustomerProfile(customerId: number): Promise<CustomerProfile> {
    try {
      const response = await apiService.get(`/customers/${customerId}`); 
      return response.data as CustomerProfile;
    } catch (error) {
      if (isAxiosError(error)) {
        if (error.response?.status === 404) {
          throw new Error('Customer account not found.');
        }
        if (error.response?.status === 500) {
          throw new Error(
            'Internal server error: Unable to retrieve customer profile.'
          );
        }
      }
      throw new Error(
        'An unexpected error occurred while retrieving the customer profile.'
      );
    }
  }  

/**
 * Updates the customer's profile. Email cannot be changed.
 * @param customerId The ID of the customer to update.
 * @param profileData The updated customer profile data (name, phoneNumber, password).
 * @returns A promise that resolves to the updated customer's profile.
 */
export async function updateCustomerProfile(
    customerId: number,
    profileData: {
      email: string;
      name?: string;
      phoneNumber?: string;
      password?: string;
    }
  ): Promise<CustomerProfile> {
    try {
      const response = await apiService.put(`/customers/${customerId}`, profileData);
      return response.data as CustomerProfile;
    } catch (error) {
      if (isAxiosError(error)) {
        if (error.response?.status === 404) {
          throw new Error('Customer account not found.');
        }
        if (error.response?.status === 400) {
          throw new Error(
            'Invalid profile data. Ensure all fields meet the required criteria.'
          );
        }
        if (error.response?.status === 500) {
          throw new Error(
            'Internal server error: Unable to update customer profile.'
          );
        }
      }
      throw new Error(
        'An unexpected error occurred while updating the customer profile.'
      );
    }
  }
  

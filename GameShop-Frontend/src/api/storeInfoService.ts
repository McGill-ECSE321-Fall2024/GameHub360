import { isAxiosError } from 'axios';
import apiService from '../config/axiosConfig';

export async function getSalesMetrics() {
  try {
    // Make a GET request to the sales metrics endpoint
    const response = await apiService.get('/store/sales/metrics');
    return response.data; // Return the sales metrics data
  } catch (error) {
    if (isAxiosError(error)) {
      // Handle specific HTTP errors
      if (error.response?.status === 404) {
        throw new Error('Sales metrics not found.');
      } else if (error.response?.status === 500) {
        throw new Error(
          'Internal server error: Unable to retrieve sales metrics.'
        );
      }
    }
    // Handle unexpected errors
    throw new Error('An unexpected error occurred. Please try again.');
  }
}

export async function viewStorePolicy() {
  try {
    // Make a GET request to the store policy endpoint
    const response = await apiService.get('/store/policy');
    return response.data.storePolicy; // Return the store policy string
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        return null; // Return null if store policy is not found
      }
    }
    throw new Error('An unexpected error occurred. Please try again.');
  }
}

export async function manageStorePolicy(updatedPolicy: string) {
  try {
    // Make a PUT request to the store policy endpoint
    const response = await apiService.put('/store/policy', {
      storePolicy: updatedPolicy,
    });
    return response.data.storePolicy; // Return the updated store policy
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('Store policy not found. Unable to update.');
      }
    }
    throw new Error(
      'An unexpected error occurred while updating the store policy.'
    );
  }
}

export async function createStorePolicy(newPolicy: string) {
  try {
    // Make a POST request to the store policy endpoint
    const response = await apiService.post('/store/policy', {
      storePolicy: newPolicy,
    });
    return response.data.storePolicy; // Return the newly created store policy
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 409) {
        throw new Error('A store policy already exists. Unable to create.');
      } else if (error.response?.status === 400) {
        throw new Error('Invalid input provided for the store policy.');
      }
    }
    throw new Error(
      'An unexpected error occurred while creating the store policy.'
    );
  }
}

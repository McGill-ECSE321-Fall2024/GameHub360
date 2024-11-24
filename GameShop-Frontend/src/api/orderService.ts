import { isAxiosError } from 'axios';
import apiService from '../config/axiosConfig';
import { CustomerOrder } from '../model/CustomerOrder';

export async function getOrders(): Promise<CustomerOrder[]> {
  try {
    const response = await apiService.get('/orders');
    return response.data.orders as CustomerOrder[];
  } catch (error) {
    if (isAxiosError(error)) {
      if (error.response?.status === 404) {
        throw new Error('No orders found.');
      }
      if (error.response?.status === 500) {
        throw new Error('Internal server error: Unable to retrieve orders.');
      }
    }
    throw new Error('An unexpected error occurred while retrieving orders.');
  }
}

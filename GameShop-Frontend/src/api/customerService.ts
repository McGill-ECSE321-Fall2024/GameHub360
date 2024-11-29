import { isAxiosError } from 'axios';
import apiService from '../config/axiosConfig';

export interface CustomerProfile {
    customerId: number;
    email: string;
    name?: string;
    phoneNumber?: string;
  }

export interface PaymentCard {
    paymentDetailsId: number; 
    cardName: string;         
    cardNumber: string;       
    postalCode: string;       
    expMonth: number;         
    expYear: number;          
    customerId: number;       
    paidOrdersIds: number[];  
  }
  
export interface PaymentCardListResponse {
    paymentCards: PaymentCard[];
    totalCards: number;
  }

export interface PaymentDetailsRequest {
    cardName: string;
    cardNumber: string;
    postalCode: string;
    expMonth: number;
    expYear: number;
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

/**
 * Fetches the list of payment cards associated with a customer.
 * @param customerId The ID of the customer.
 * @returns A promise that resolves to the list of payment cards.
 */
export async function getCustomerPaymentCards(
    customerId: number
  ): Promise<PaymentCardListResponse> {
    try {
      const response = await apiService.get(`/customers/${customerId}/cards`);
      console.log("here is the fetched card list: ", response);
      return response.data as PaymentCardListResponse;
    } catch (error) {
      if (isAxiosError(error)) {
        if (error.response?.status === 404) {
          throw new Error('No payment cards found for this customer.');
        }
        if (error.response?.status === 500) {
          throw new Error('Internal server error while retrieving payment cards.');
        }
      }
      throw new Error(
        'An unexpected error occurred while fetching payment cards.'
      );
    }
  }

/**
 * Creates a new payment card for a specific customer.
 * @param customerId The ID of the customer to whom the card will be added.
 * @param paymentDetails The details of the payment card to be created.
 * @returns A promise resolving to the created payment card details.
 */
export async function createPaymentCard(
  customerId: number,
  paymentDetails: PaymentDetailsRequest
): Promise<PaymentCard> {
  try {
    // Make the POST request to the backend
    const response = await apiService.post(
      `/customers/${customerId}/payment`,
      paymentDetails
    );

    // Return the response data
    return response.data as PaymentCard;
  } catch (error) {
    if (isAxiosError(error)) {
      // Handle specific HTTP errors
      if (error.response?.status === 404) {
        throw new Error("Customer not found.");
      }
      if (error.response?.status === 400) {
        throw new Error("Invalid payment card data.");
      }
      if (error.response?.status === 500) {
        throw new Error(
          "Internal server error: Unable to create payment card."
        );
      }
    }

    // Handle unexpected errors
    throw new Error(
      "An unexpected error occurred while creating the payment card."
    );
  }
}

/**
 * Updates an existing payment card for a specific customer.
 * @param customerId The ID of the customer.
 * @param cardId The ID of the payment card to update.
 * @param paymentDetails The new payment card details.
 * @returns A promise resolving to the updated payment card details.
 */
export async function updatePaymentCard(
  customerId: number,
  cardId: number,
  paymentDetails: PaymentDetailsRequest
): Promise<PaymentDetailsRequest> {
  try {
    // Make the PUT request to the backend
    const response = await apiService.put(`/customers/${customerId}/payment/${cardId}`, paymentDetails);

    // Return the response data
    return response.data;
  } catch (error) {
    if (isAxiosError(error)) {
      // Handle specific HTTP errors
      if (error.response?.status === 404) {
        throw new Error("Payment card not found.");
      }
      if (error.response?.status === 400) {
        throw new Error("Invalid payment card data.");
      }
      if (error.response?.status === 500) {
        throw new Error(
          "Internal server error: Unable to update payment card."
        );
      }
    }

    // Handle unexpected errors
    throw new Error(
      "An unexpected error occurred while updating the payment card."
    );
  }
}
  

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
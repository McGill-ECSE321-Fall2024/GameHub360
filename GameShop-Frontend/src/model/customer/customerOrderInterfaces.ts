export interface CustomerOrderRequest {
    orderedGameIds: number[];
    paymentInformationId: number;
    customerId: number;
  }
  
export interface CustomerOrderResponse {
    orderId: number;
    orderDate: string;
    orderedById: number;
    paymentInformationId: number;
    orderedGamesIds: number[];
    orderStatus: string;
  }
  
  export interface OrderHistoryResponse {
    orders: CustomerOrderResponse[];
  }
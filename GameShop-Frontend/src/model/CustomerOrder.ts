export type OrderStatus = 'SHIPPING' | 'DELIVERED' | 'RETURNED';

export type CustomerOrder = {
  orderId: number;
  orderStatus: OrderStatus;
  orderDate: string;
  orderedGamesIds: number[];
  orderedById: number;
  paymentInformationId: number;
};

export interface CartItem {
  gameId: number;
  name: string;
  price: number;
  imageUrl: string;
  quantity: number;
}

export interface Cart {
  items: CartItem[];
  total: number;
}

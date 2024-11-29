export interface Game {
  gameId: number;
  name: string;
  description: string;
  imageUrl: string;
  quantityInStock: number;
  available: boolean;
  price: number;
  categoryIds: number[];
  wishListIds: number[];
  orderIds: number[];
  promotionIds: number[];
}

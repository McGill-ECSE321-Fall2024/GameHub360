export interface WishlistItem {
    gameId: number;
    name: string;
    price: number;
    imageUrl: string;
    console?: string;
    category?: string;
    isArchived?: boolean;
  }
  
  export interface Wishlist {
    items: WishlistItem[];
  }
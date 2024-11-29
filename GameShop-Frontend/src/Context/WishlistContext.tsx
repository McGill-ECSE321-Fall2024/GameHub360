import React, { createContext, useContext, useState, useEffect } from 'react';
import { Wishlist, WishlistItem } from '../model/user/Wishlist';
import { AuthState } from '../model/AuthState';
import { getAuthState } from '../state/authState';

interface WishlistContextType {
  wishlist: Wishlist;
  addToWishlist: (item: WishlistItem) => void;
  removeFromWishlist: (gameId: number) => void;
  isInWishlist: (gameId: number) => boolean;
}

const WishlistContext = createContext<WishlistContextType | undefined>(undefined);

export const WishlistProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [wishlist, setWishlist] = useState<Wishlist>(() => {
    const savedWishlist = localStorage.getItem('wishlist');
    return savedWishlist ? JSON.parse(savedWishlist) : { items: [] };
  });

  const authState = getAuthState();

  useEffect(() => {
    localStorage.setItem('wishlist', JSON.stringify(wishlist));
  }, [wishlist]);

  const addToWishlist = (item: WishlistItem) => {
    if (authState !== AuthState.CUSTOMER) return;
    setWishlist(prev => ({
      items: [...prev.items, item]
    }));
  };

  const removeFromWishlist = (gameId: number) => {
    if (authState !== AuthState.CUSTOMER) return;
    setWishlist(prev => ({
      items: prev.items.filter(item => item.gameId !== gameId)
    }));
  };

  const isInWishlist = (gameId: number) => {
    return wishlist.items.some(item => item.gameId === gameId);
  };

  // Clear wishlist when user logs out
  useEffect(() => {
    if (authState !== AuthState.CUSTOMER) {
      setWishlist({ items: [] });
    }
  }, [authState]);

  return (
    <WishlistContext.Provider value={{ 
      wishlist, 
      addToWishlist, 
      removeFromWishlist, 
      isInWishlist
    }}>
      {children}
    </WishlistContext.Provider>
  );
};

export const useWishlist = () => {
  const context = useContext(WishlistContext);
  if (context === undefined) {
    throw new Error('useWishlist must be used within a WishlistProvider');
  }
  return context;
};
import React, { createContext, useContext, useState, useEffect } from 'react';
import { Cart } from '../model/user/Cart';
import { CartItem } from '../model/user/CartItem';
interface CartContextType {
  cart: Cart;
  addToCart: (item: CartItem) => void;
  removeFromCart: (gameId: number) => void;
  updateQuantity: (gameId: number, quantity: number) => void;
  clearCart: () => void;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [cart, setCart] = useState<Cart>(() => {
    const savedCart = localStorage.getItem('cart');
    return savedCart ? JSON.parse(savedCart) : { items: [], total: 0 };
  });

  useEffect(() => {
    localStorage.setItem('cart', JSON.stringify(cart));
  }, [cart]);

  const calculateTotal = (items: CartItem[]) => {
    return items.reduce((sum, item) => sum + item.price * item.quantity, 0);
  };

  const addToCart = (newItem: CartItem) => {
    setCart(prevCart => {
      const existingItem = prevCart.items.find(item => item.gameId === newItem.gameId);
      
      if (existingItem) {
        const updatedItems = prevCart.items.map(item =>
          item.gameId === newItem.gameId
            ? { ...item, quantity: item.quantity + newItem.quantity }
            : item
        );
        return { items: updatedItems, total: calculateTotal(updatedItems) };
      }

      const updatedItems = [...prevCart.items, newItem];
      return { items: updatedItems, total: calculateTotal(updatedItems) };
    });
  };

  const removeFromCart = (gameId: number) => {
    setCart(prevCart => {
      const updatedItems = prevCart.items.filter(item => item.gameId !== gameId);
      return { items: updatedItems, total: calculateTotal(updatedItems) };
    });
  };

  const updateQuantity = (gameId: number, quantity: number) => {
    setCart(prevCart => {
      const updatedItems = prevCart.items.map(item =>
        item.gameId === gameId ? { ...item, quantity } : item
      );
      return { items: updatedItems, total: calculateTotal(updatedItems) };
    });
  };

  const clearCart = () => {
    setCart({ items: [], total: 0 });
  };

  return (
    <CartContext.Provider value={{ cart, addToCart, removeFromCart, updateQuantity, clearCart }}>
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => {
  const context = useContext(CartContext);
  if (context === undefined) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
};
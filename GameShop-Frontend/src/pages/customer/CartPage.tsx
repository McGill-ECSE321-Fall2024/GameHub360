import { useCart } from '../../Context/CartContext';
import { useNavigate } from 'react-router-dom';

const CartPage = () => {
  const { cart, removeFromCart, updateQuantity } = useCart();
  const navigate = useNavigate();

  const handleQuantityChange = (gameId: number, newQuantity: number) => {
    if (newQuantity > 0) {
      updateQuantity(gameId, newQuantity);
    }
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Shopping Cart</h1>
      
      {cart.items.length === 0 ? (
        <div className="text-center py-8">
          <p className="text-gray-600 mb-4">Your cart is empty</p>
          <button
            onClick={() => navigate('/browse')}
            className="text-blue-500 hover:text-blue-600"
          >
            Continue Shopping
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 gap-6">
          {cart.items.map((item) => (
            <div
              key={item.gameId}
              className="flex items-center justify-between p-4 border rounded-lg"
            >
              <div className="flex items-center space-x-4">
                <img
                  src={item.imageUrl}
                  alt={item.title}
                  className="w-24 h-24 object-cover rounded"
                />
                <div>
                  <h3 className="font-bold">{item.title}</h3>
                  <p className="text-gray-600">${item.price.toFixed(2)}</p>
                </div>
              </div>
              
              <div className="flex items-center space-x-4">
                <div className="flex items-center space-x-2">
                  <button
                    onClick={() => handleQuantityChange(item.gameId, item.quantity - 1)}
                    className="px-2 py-1 border rounded"
                  >
                    -
                  </button>
                  <span>{item.quantity}</span>
                  <button
                    onClick={() => handleQuantityChange(item.gameId, item.quantity + 1)}
                    className="px-2 py-1 border rounded"
                  >
                    +
                  </button>
                </div>
                <button
                  onClick={() => removeFromCart(item.gameId)}
                  className="text-red-500 hover:text-red-600"
                >
                  Remove
                </button>
              </div>
            </div>
          ))}
          
          <div className="mt-6 p-4 border rounded-lg">
            <div className="flex justify-between items-center mb-4">
              <span className="font-bold">Total:</span>
              <span className="font-bold">${cart.total.toFixed(2)}</span>
            </div>
            <button
              onClick={() => navigate('/checkout')}
              className="w-full bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600"
            >
              Proceed to Checkout
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default CartPage;
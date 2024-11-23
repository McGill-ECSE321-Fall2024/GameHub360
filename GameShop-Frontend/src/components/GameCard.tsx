import { useNavigate } from 'react-router-dom';
import { getAuthState } from '../state/authState';
import { useCart } from '../Context/CartContext';
import { useWishlist } from '../Context/WishlistContext';
import { AuthState } from '../model/AuthState';
import { useToast } from '../Context/ToastContext';

interface Game {
  gameId: number;
  name: string;
  price: number;
  imageUrl: string;
  console?: string;
  category?: string;
}

interface GameCardProps {
  game: Game;
}

const GameCard = ({ game }: GameCardProps) => {
  const navigate = useNavigate();
  const authState = getAuthState();
  const { addToCart } = useCart();
  const { addToWishlist, isInWishlist, removeFromWishlist } = useWishlist();
  const { showToast } = useToast();

  const handleWishlistToggle = async (e: React.MouseEvent) => {
    e.stopPropagation();
    try {
      if (isInWishlist(game.gameId)) {
        removeFromWishlist(game.gameId);
        showToast('Removed from wishlist', 'success');
      } else {
        await addToWishlist({
          gameId: game.gameId,
          name: game.name,
          price: game.price,
          imageUrl: game.imageUrl,
          console: game.console,
          category: game.category
        });
        showToast('Added to wishlist successfully!', 'success');
      }
    } catch (error) {
      console.error('Error updating wishlist:', error);
      showToast('Failed to update wishlist', 'error');
    }
  };

  const handleAddToCart = (e: React.MouseEvent) => {
    e.stopPropagation();
    addToCart({
      gameId: game.gameId,
      title: game.name,
      price: game.price,
      imageUrl: game.imageUrl,
      quantity: 1
    });
    showToast('Added to cart successfully!', 'success');
  };

  return (
    <div className="relative border rounded-lg overflow-hidden shadow-lg hover:shadow-xl transition-shadow">
      <div 
        className="cursor-pointer"
        onClick={() => navigate(`/browse/game/${game.gameId}`)}
      >
        <img 
          src={game.imageUrl} 
          alt={game.name}
          className="w-full h-48 object-cover"
        />
        <div className="p-4">
          <h3 className="font-bold text-lg mb-2">{game.name}</h3>
          <div className="flex justify-between items-center">
            <span className="text-gray-600">{game.console}</span>
            <span className="font-bold">${game.price.toFixed(2)}</span>
          </div>
        </div>
      </div>
      {authState === AuthState.CUSTOMER && (
        <div className="absolute bottom-2 right-2 flex gap-2">
          <button
            onClick={handleWishlistToggle}
            className={`text-white px-2 py-1 rounded ${
              isInWishlist(game.gameId) 
                ? 'bg-red-500 hover:bg-red-600' 
                : 'bg-blue-500 hover:bg-blue-600'
            }`}
          >
            {isInWishlist(game.gameId) ? '♥' : '♡'}
          </button>
          <button
            onClick={handleAddToCart}
            className="bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-600"
          >
            Add to Cart
          </button>
        </div>
      )}
    </div>
  );
};

export default GameCard;


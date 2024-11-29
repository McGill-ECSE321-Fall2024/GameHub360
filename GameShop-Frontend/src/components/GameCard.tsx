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
  categoryId?: number;
}

interface GameCardProps {
  game: Game;
  returnPath?: string;
}

const GameCard = ({ game, returnPath = '/browse' }: GameCardProps) => {
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
        showToast('Removed from Wishlist', 'success');
      } else {
        await addToWishlist({
          gameId: game.gameId,
          name: game.name,
          price: game.price,
          imageUrl: game.imageUrl,
          console: game.console,
          category: game.category
        });
        showToast('Added to Wishlist successfully!', 'success');
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
      name: game.name,
      price: game.price,
      imageUrl: game.imageUrl,
      quantity: 1
    });
    showToast('Added to cart successfully!', 'success');
  };

  const handleClick = () => {
    if (authState === AuthState.UNAUTHENTICATED) {
      return; // Do nothing if user is not authenticated
    }
    
    navigate(`/games/${game.gameId}`, {
      state: { game, returnPath }
    });
  };

  return (
    <div className="relative bg-white border rounded-xl overflow-hidden shadow-lg hover:shadow-xl transition-all duration-300 transform hover:-translate-y-1">
      <div 
        className={`cursor-${authState === AuthState.UNAUTHENTICATED ? 'not-allowed' : 'pointer'}`}
        onClick={handleClick}
      >
        <div className="relative">
          <img 
            src={game.imageUrl} 
            alt={game.name}
            className="w-full h-56 object-cover"
          />
          {game.category && (
            <span className="absolute top-3 right-3 text-sm font-medium text-white bg-blue-500 px-3 py-1.5 rounded-full shadow-md hover:bg-blue-600 transition-colors">
              {game.category}
            </span>
          )}
        </div>
        <div className="p-5">
          <h3 className="font-bold text-xl mb-3 text-gray-800 line-clamp-2">{game.name}</h3>
          <div className="space-y-3">
            <div className="flex flex-wrap gap-2">
              {game.category && (
                <span className="text-sm text-blue-600 bg-blue-50 px-2 py-0.5 rounded-full">
                  {game.category}
                </span>
              )}
            </div>
            <div className="flex items-center justify-between">
              <span className="text-2xl font-bold text-blue-600">${game.price.toFixed(2)}</span>
            </div>
          </div>
        </div>
      </div>
      {authState === AuthState.CUSTOMER && (
        <div className="absolute bottom-4 right-4 flex gap-2">
          <button
            onClick={handleWishlistToggle}
            className={`text-white px-3 py-2 rounded-lg transition-colors ${
              isInWishlist(game.gameId) 
                ? 'bg-red-500 hover:bg-red-600' 
                : 'bg-blue-500 hover:bg-blue-600'
            }`}
          >
            {isInWishlist(game.gameId) ? '♥' : '♡'}
          </button>
          <button
            onClick={handleAddToCart}
            className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors"
          >
            Add to Cart
          </button>
        </div>
      )}
    </div>
  );
};

export default GameCard;


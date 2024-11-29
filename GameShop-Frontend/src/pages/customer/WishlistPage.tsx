import { useNavigate } from 'react-router-dom';
import { useWishlist } from '../../Context/WishlistContext';
import GameCard from '../../components/GameCard';

const WishlistPage = () => {
  const navigate = useNavigate();
  const { wishlist, removeFromWishlist } = useWishlist();

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">My Wishlist</h1>
      
      {wishlist.items.length === 0 ? (
        <div className="text-center py-8">
          <p className="text-gray-600 mb-4">Your wishlist is empty</p>
          <button
            onClick={() => navigate('/browse')}
            className="text-blue-500 hover:text-blue-600"
          >
            Browse Games
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {wishlist.items.map((game) => (
            <div key={game.gameId} className="relative">
              <div className={game.isArchived ? 'opacity-50' : ''}>
                <GameCard game={game} returnPath="/wishlist" />
              </div>
              {game.isArchived && (
                <div className="absolute top-2 right-2 bg-red-500 text-white px-2 py-1 rounded text-sm">
                  Archived
                </div>
              )}
              <button
                onClick={(e) => {
                  e.stopPropagation();
                  removeFromWishlist(game.gameId);
                }}
                className="absolute top-2 left-2 bg-red-500 text-white p-2 rounded-full hover:bg-red-600"
              >
                ×
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default WishlistPage;

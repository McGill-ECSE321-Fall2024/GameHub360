/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;

// line 76 "../../../../../../model.ump"
// line 199 "../../../../../../model.ump"
public class Game extends GameEntity
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Game Attributes
  private int gameId;
  private int quantityInStock;
  private boolean isAvailable;
  private int price;

  //Game Associations
  private GameShop gameShop;
  private List<CustomerAccount> wishlist;
  private List<Order> order;
  private List<Promotion> promotions;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(String aName, String aDescription, String aImageURL, int aGameId, int aQuantityInStock, boolean aIsAvailable, int aPrice, GameShop aGameShop, GameCategory... allCategories)
  {
    super(aName, aDescription, aImageURL, allCategories);
    gameId = aGameId;
    quantityInStock = aQuantityInStock;
    isAvailable = aIsAvailable;
    price = aPrice;
    boolean didAddGameShop = setGameShop(aGameShop);
    if (!didAddGameShop)
    {
      throw new RuntimeException("Unable to create game due to gameShop. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    wishlist = new ArrayList<CustomerAccount>();
    order = new ArrayList<Order>();
    promotions = new ArrayList<Promotion>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setGameId(int aGameId)
  {
    boolean wasSet = false;
    gameId = aGameId;
    wasSet = true;
    return wasSet;
  }

  public boolean setQuantityInStock(int aQuantityInStock)
  {
    boolean wasSet = false;
    quantityInStock = aQuantityInStock;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsAvailable(boolean aIsAvailable)
  {
    boolean wasSet = false;
    isAvailable = aIsAvailable;
    wasSet = true;
    return wasSet;
  }

  public boolean setPrice(int aPrice)
  {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public int getGameId()
  {
    return gameId;
  }

  public int getQuantityInStock()
  {
    return quantityInStock;
  }

  public boolean getIsAvailable()
  {
    return isAvailable;
  }

  public int getPrice()
  {
    return price;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsAvailable()
  {
    return isAvailable;
  }
  /* Code from template association_GetOne */
  public GameShop getGameShop()
  {
    return gameShop;
  }
  /* Code from template association_GetMany */
  public CustomerAccount getWishlist(int index)
  {
    CustomerAccount aWishlist = wishlist.get(index);
    return aWishlist;
  }

  public List<CustomerAccount> getWishlist()
  {
    List<CustomerAccount> newWishlist = Collections.unmodifiableList(wishlist);
    return newWishlist;
  }

  public int numberOfWishlist()
  {
    int number = wishlist.size();
    return number;
  }

  public boolean hasWishlist()
  {
    boolean has = wishlist.size() > 0;
    return has;
  }

  public int indexOfWishlist(CustomerAccount aWishlist)
  {
    int index = wishlist.indexOf(aWishlist);
    return index;
  }
  /* Code from template association_GetMany */
  public Order getOrder(int index)
  {
    Order aOrder = order.get(index);
    return aOrder;
  }

  public List<Order> getOrder()
  {
    List<Order> newOrder = Collections.unmodifiableList(order);
    return newOrder;
  }

  public int numberOfOrder()
  {
    int number = order.size();
    return number;
  }

  public boolean hasOrder()
  {
    boolean has = order.size() > 0;
    return has;
  }

  public int indexOfOrder(Order aOrder)
  {
    int index = order.indexOf(aOrder);
    return index;
  }
  /* Code from template association_GetMany */
  public Promotion getPromotion(int index)
  {
    Promotion aPromotion = promotions.get(index);
    return aPromotion;
  }

  public List<Promotion> getPromotions()
  {
    List<Promotion> newPromotions = Collections.unmodifiableList(promotions);
    return newPromotions;
  }

  public int numberOfPromotions()
  {
    int number = promotions.size();
    return number;
  }

  public boolean hasPromotions()
  {
    boolean has = promotions.size() > 0;
    return has;
  }

  public int indexOfPromotion(Promotion aPromotion)
  {
    int index = promotions.indexOf(aPromotion);
    return index;
  }
  /* Code from template association_SetOneToMany */
  public boolean setGameShop(GameShop aGameShop)
  {
    boolean wasSet = false;
    if (aGameShop == null)
    {
      return wasSet;
    }

    GameShop existingGameShop = gameShop;
    gameShop = aGameShop;
    if (existingGameShop != null && !existingGameShop.equals(aGameShop))
    {
      existingGameShop.removeGame(this);
    }
    gameShop.addGame(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfWishlist()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addWishlist(CustomerAccount aWishlist)
  {
    boolean wasAdded = false;
    if (wishlist.contains(aWishlist)) { return false; }
    wishlist.add(aWishlist);
    if (aWishlist.indexOfWishListedGame(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aWishlist.addWishListedGame(this);
      if (!wasAdded)
      {
        wishlist.remove(aWishlist);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeWishlist(CustomerAccount aWishlist)
  {
    boolean wasRemoved = false;
    if (!wishlist.contains(aWishlist))
    {
      return wasRemoved;
    }

    int oldIndex = wishlist.indexOf(aWishlist);
    wishlist.remove(oldIndex);
    if (aWishlist.indexOfWishListedGame(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aWishlist.removeWishListedGame(this);
      if (!wasRemoved)
      {
        wishlist.add(oldIndex,aWishlist);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addWishlistAt(CustomerAccount aWishlist, int index)
  {  
    boolean wasAdded = false;
    if(addWishlist(aWishlist))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWishlist()) { index = numberOfWishlist() - 1; }
      wishlist.remove(aWishlist);
      wishlist.add(index, aWishlist);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveWishlistAt(CustomerAccount aWishlist, int index)
  {
    boolean wasAdded = false;
    if(wishlist.contains(aWishlist))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWishlist()) { index = numberOfWishlist() - 1; }
      wishlist.remove(aWishlist);
      wishlist.add(index, aWishlist);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addWishlistAt(aWishlist, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrder()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addOrder(Order aOrder)
  {
    boolean wasAdded = false;
    if (order.contains(aOrder)) { return false; }
    order.add(aOrder);
    if (aOrder.indexOfGame(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aOrder.addGame(this);
      if (!wasAdded)
      {
        order.remove(aOrder);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeOrder(Order aOrder)
  {
    boolean wasRemoved = false;
    if (!order.contains(aOrder))
    {
      return wasRemoved;
    }

    int oldIndex = order.indexOf(aOrder);
    order.remove(oldIndex);
    if (aOrder.indexOfGame(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aOrder.removeGame(this);
      if (!wasRemoved)
      {
        order.add(oldIndex,aOrder);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderAt(Order aOrder, int index)
  {  
    boolean wasAdded = false;
    if(addOrder(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrder()) { index = numberOfOrder() - 1; }
      order.remove(aOrder);
      order.add(index, aOrder);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderAt(Order aOrder, int index)
  {
    boolean wasAdded = false;
    if(order.contains(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrder()) { index = numberOfOrder() - 1; }
      order.remove(aOrder);
      order.add(index, aOrder);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderAt(aOrder, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPromotions()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addPromotion(Promotion aPromotion)
  {
    boolean wasAdded = false;
    if (promotions.contains(aPromotion)) { return false; }
    promotions.add(aPromotion);
    if (aPromotion.indexOfPromotedGame(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aPromotion.addPromotedGame(this);
      if (!wasAdded)
      {
        promotions.remove(aPromotion);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removePromotion(Promotion aPromotion)
  {
    boolean wasRemoved = false;
    if (!promotions.contains(aPromotion))
    {
      return wasRemoved;
    }

    int oldIndex = promotions.indexOf(aPromotion);
    promotions.remove(oldIndex);
    if (aPromotion.indexOfPromotedGame(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aPromotion.removePromotedGame(this);
      if (!wasRemoved)
      {
        promotions.add(oldIndex,aPromotion);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPromotionAt(Promotion aPromotion, int index)
  {  
    boolean wasAdded = false;
    if(addPromotion(aPromotion))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPromotions()) { index = numberOfPromotions() - 1; }
      promotions.remove(aPromotion);
      promotions.add(index, aPromotion);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePromotionAt(Promotion aPromotion, int index)
  {
    boolean wasAdded = false;
    if(promotions.contains(aPromotion))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPromotions()) { index = numberOfPromotions() - 1; }
      promotions.remove(aPromotion);
      promotions.add(index, aPromotion);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPromotionAt(aPromotion, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    GameShop placeholderGameShop = gameShop;
    this.gameShop = null;
    if(placeholderGameShop != null)
    {
      placeholderGameShop.removeGame(this);
    }
    ArrayList<CustomerAccount> copyOfWishlist = new ArrayList<CustomerAccount>(wishlist);
    wishlist.clear();
    for(CustomerAccount aWishlist : copyOfWishlist)
    {
      aWishlist.removeWishListedGame(this);
    }
    ArrayList<Order> copyOfOrder = new ArrayList<Order>(order);
    order.clear();
    for(Order aOrder : copyOfOrder)
    {
      if (aOrder.numberOfGames() <= Order.minimumNumberOfGames())
      {
        aOrder.delete();
      }
      else
      {
        aOrder.removeGame(this);
      }
    }
    ArrayList<Promotion> copyOfPromotions = new ArrayList<Promotion>(promotions);
    promotions.clear();
    for(Promotion aPromotion : copyOfPromotions)
    {
      aPromotion.removePromotedGame(this);
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "gameId" + ":" + getGameId()+ "," +
            "quantityInStock" + ":" + getQuantityInStock()+ "," +
            "isAvailable" + ":" + getIsAvailable()+ "," +
            "price" + ":" + getPrice()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "gameShop = "+(getGameShop()!=null?Integer.toHexString(System.identityHashCode(getGameShop())):"null");
  }
}
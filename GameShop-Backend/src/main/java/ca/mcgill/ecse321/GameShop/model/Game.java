/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

// line 61 "../../../../../../model.ump"
// line 178 "../../../../../../model.ump"
@Entity
public class Game extends GameEntity
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Game Attributes
  private int quantityInStock;
  private boolean isAvailable;
  private double price;

  //Game Associations
  @ManyToMany(mappedBy = "wishListedGames")
  private List<CustomerAccount> wishLists;

  @ManyToMany
  @JoinTable(
      name = "order_games",
      joinColumns = @JoinColumn(name = "game_entity_id"),
      inverseJoinColumns = @JoinColumn(name = "order_id")
  )
  private List<CustomerOrder> orders;

  @ManyToMany(mappedBy = "promotedGames")
  private List<Promotion> promotions;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(String aName, String aDescription, String aImageURL, int aQuantityInStock, boolean aIsAvailable, double aPrice, GameCategory... allCategories)
  {
    super(aName, aDescription, aImageURL, allCategories);
    quantityInStock = aQuantityInStock;
    isAvailable = aIsAvailable;
    price = aPrice;
    wishLists = new ArrayList<CustomerAccount>();
    orders = new ArrayList<CustomerOrder>();
    promotions = new ArrayList<Promotion>();
  }

  public Game() {
    wishLists = new ArrayList<CustomerAccount>();
    orders = new ArrayList<CustomerOrder>();
    promotions = new ArrayList<Promotion>();
  }

  //------------------------
  // INTERFACE
  //------------------------

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

  public boolean setPrice(double aPrice)
  {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public int getQuantityInStock()
  {
    return quantityInStock;
  }

  public boolean getIsAvailable()
  {
    return isAvailable;
  }

  public double getPrice()
  {
    return price;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsAvailable()
  {
    return isAvailable;
  }
  /* Code from template association_GetMany */
  public CustomerAccount getWishList(int index)
  {
    CustomerAccount aWishList = wishLists.get(index);
    return aWishList;
  }

  public List<CustomerAccount> getWishLists()
  {
    List<CustomerAccount> newWishLists = Collections.unmodifiableList(wishLists);
    return newWishLists;
  }

  public int numberOfWishLists()
  {
    int number = wishLists.size();
    return number;
  }

  public boolean hasWishLists()
  {
    boolean has = wishLists.size() > 0;
    return has;
  }

  public int indexOfWishList(CustomerAccount aWishList)
  {
    int index = wishLists.indexOf(aWishList);
    return index;
  }
  /* Code from template association_GetMany */
  public CustomerOrder getOrder(int index)
  {
    CustomerOrder aOrder = orders.get(index);
    return aOrder;
  }

  public List<CustomerOrder> getOrders()
  {
    List<CustomerOrder> newOrders = Collections.unmodifiableList(orders);
    return newOrders;
  }

  public int numberOfOrders()
  {
    int number = orders.size();
    return number;
  }

  public boolean hasOrders()
  {
    boolean has = orders.size() > 0;
    return has;
  }

  public int indexOfOrder(CustomerOrder aOrder)
  {
    int index = orders.indexOf(aOrder);
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfWishLists()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addWishList(CustomerAccount aWishList)
  {
    boolean wasAdded = false;
    if (wishLists.contains(aWishList)) { return false; }
    wishLists.add(aWishList);
    if (aWishList.indexOfWishListedGame(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aWishList.addWishListedGame(this);
      if (!wasAdded)
      {
        wishLists.remove(aWishList);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeWishList(CustomerAccount aWishList)
  {
    boolean wasRemoved = false;
    if (!wishLists.contains(aWishList))
    {
      return wasRemoved;
    }

    int oldIndex = wishLists.indexOf(aWishList);
    wishLists.remove(oldIndex);
    if (aWishList.indexOfWishListedGame(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aWishList.removeWishListedGame(this);
      if (!wasRemoved)
      {
        wishLists.add(oldIndex,aWishList);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addWishListAt(CustomerAccount aWishList, int index)
  {  
    boolean wasAdded = false;
    if(addWishList(aWishList))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWishLists()) { index = numberOfWishLists() - 1; }
      wishLists.remove(aWishList);
      wishLists.add(index, aWishList);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveWishListAt(CustomerAccount aWishList, int index)
  {
    boolean wasAdded = false;
    if(wishLists.contains(aWishList))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWishLists()) { index = numberOfWishLists() - 1; }
      wishLists.remove(aWishList);
      wishLists.add(index, aWishList);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addWishListAt(aWishList, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrders()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addOrder(CustomerOrder aOrder)
  {
    boolean wasAdded = false;
    if (orders.contains(aOrder)) { return false; }
    orders.add(aOrder);
    if (aOrder.indexOfGame(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aOrder.addGame(this);
      if (!wasAdded)
      {
        orders.remove(aOrder);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeOrder(CustomerOrder aOrder)
  {
    boolean wasRemoved = false;
    if (!orders.contains(aOrder))
    {
      return wasRemoved;
    }

    int oldIndex = orders.indexOf(aOrder);
    orders.remove(oldIndex);
    if (aOrder.indexOfGame(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aOrder.removeGame(this);
      if (!wasRemoved)
      {
        orders.add(oldIndex,aOrder);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderAt(CustomerOrder aOrder, int index)
  {  
    boolean wasAdded = false;
    if(addOrder(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderAt(CustomerOrder aOrder, int index)
  {
    boolean wasAdded = false;
    if(orders.contains(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
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
    ArrayList<CustomerAccount> copyOfWishLists = new ArrayList<CustomerAccount>(wishLists);
    wishLists.clear();
    for(CustomerAccount aWishList : copyOfWishLists)
    {
      aWishList.removeWishListedGame(this);
    }
    ArrayList<CustomerOrder> copyOfOrders = new ArrayList<CustomerOrder>(orders);
    orders.clear();
    for(CustomerOrder aOrder : copyOfOrders)
    {
      if (aOrder.numberOfGames() <= CustomerOrder.minimumNumberOfGames())
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
            "quantityInStock" + ":" + getQuantityInStock()+ "," +
            "isAvailable" + ":" + getIsAvailable()+ "," +
            "price" + ":" + getPrice()+ "]";
  }
}
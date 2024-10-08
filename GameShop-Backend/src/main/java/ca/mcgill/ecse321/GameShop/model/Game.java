/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;

// line 70 "../../../../../../GameShop.ump"
public class Game extends GameInformation
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Game Attributes
  private int quantityInStock;
  private String isAvailable;
  private int price;

  //Game Associations
  private List<CustomerAccount> inWishList;
  private List<Order> order;
  private List<Promotion> promotions;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(String aName, String aDescription, String aImageURL, String aNameOfX, int aQuantityInStock, String aIsAvailable, int aPrice, GameCategory... allCategories)
  {
    super(aName, aDescription, aImageURL, aNameOfX, allCategories);
    quantityInStock = aQuantityInStock;
    isAvailable = aIsAvailable;
    price = aPrice;
    inWishList = new ArrayList<CustomerAccount>();
    order = new ArrayList<Order>();
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

  public boolean setIsAvailable(String aIsAvailable)
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

  /**
   * this does not make sense... saying that "Game is a GameInformation does not make sense", we need to re-look at this.
   */
  public int getQuantityInStock()
  {
    return quantityInStock;
  }

  public String getIsAvailable()
  {
    return isAvailable;
  }

  public int getPrice()
  {
    return price;
  }
  /* Code from template association_GetMany */
  public CustomerAccount getInWishList(int index)
  {
    CustomerAccount aInWishList = inWishList.get(index);
    return aInWishList;
  }

  public List<CustomerAccount> getInWishList()
  {
    List<CustomerAccount> newInWishList = Collections.unmodifiableList(inWishList);
    return newInWishList;
  }

  public int numberOfInWishList()
  {
    int number = inWishList.size();
    return number;
  }

  public boolean hasInWishList()
  {
    boolean has = inWishList.size() > 0;
    return has;
  }

  public int indexOfInWishList(CustomerAccount aInWishList)
  {
    int index = inWishList.indexOf(aInWishList);
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfInWishList()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addInWishList(CustomerAccount aInWishList)
  {
    boolean wasAdded = false;
    if (inWishList.contains(aInWishList)) { return false; }
    inWishList.add(aInWishList);
    if (aInWishList.indexOfWishListedGame(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aInWishList.addWishListedGame(this);
      if (!wasAdded)
      {
        inWishList.remove(aInWishList);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeInWishList(CustomerAccount aInWishList)
  {
    boolean wasRemoved = false;
    if (!inWishList.contains(aInWishList))
    {
      return wasRemoved;
    }

    int oldIndex = inWishList.indexOf(aInWishList);
    inWishList.remove(oldIndex);
    if (aInWishList.indexOfWishListedGame(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aInWishList.removeWishListedGame(this);
      if (!wasRemoved)
      {
        inWishList.add(oldIndex,aInWishList);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addInWishListAt(CustomerAccount aInWishList, int index)
  {  
    boolean wasAdded = false;
    if(addInWishList(aInWishList))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfInWishList()) { index = numberOfInWishList() - 1; }
      inWishList.remove(aInWishList);
      inWishList.add(index, aInWishList);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveInWishListAt(CustomerAccount aInWishList, int index)
  {
    boolean wasAdded = false;
    if(inWishList.contains(aInWishList))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfInWishList()) { index = numberOfInWishList() - 1; }
      inWishList.remove(aInWishList);
      inWishList.add(index, aInWishList);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addInWishListAt(aInWishList, index);
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
    ArrayList<CustomerAccount> copyOfInWishList = new ArrayList<CustomerAccount>(inWishList);
    inWishList.clear();
    for(CustomerAccount aInWishList : copyOfInWishList)
    {
      aInWishList.removeWishListedGame(this);
    }
    ArrayList<Order> copyOfOrder = new ArrayList<Order>(order);
    order.clear();
    for(Order aOrder : copyOfOrder)
    {
      if (aOrder.numberOfGame() <= Order.minimumNumberOfGame())
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
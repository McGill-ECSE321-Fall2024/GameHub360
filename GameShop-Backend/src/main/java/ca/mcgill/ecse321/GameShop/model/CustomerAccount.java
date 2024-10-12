/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

import java.sql.Date;

// line 12 "../../../../../../model.ump"
// line 152 "../../../../../../model.ump"
@Entity
public class CustomerAccount extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CustomerAccount Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int customerId;

  //CustomerAccount Associations
  @OneToMany(mappedBy = "cardOwner", cascade = CascadeType.ALL)
  private List<PaymentDetails> paymentCards;
  
  @OneToMany(mappedBy = "orderedBy", cascade = CascadeType.ALL)
  private List<CustomerOrder> orderHistory;

  @ManyToMany
  @JoinTable(
      name = "wishlist",
      joinColumns = @JoinColumn(name = "customer_id"),
      inverseJoinColumns = @JoinColumn(name = "game_entity_id")
  )
  private List<Game> wishListedGames;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CustomerAccount(String aEmail, String aPassword)
  {
    super(aEmail, aPassword);
    paymentCards = new ArrayList<PaymentDetails>();
    orderHistory = new ArrayList<CustomerOrder>();
    wishListedGames = new ArrayList<Game>();
  }

  public CustomerAccount() 
  {
    paymentCards = new ArrayList<PaymentDetails>();
    reviews = new ArrayList<Review>();
    orderHistory = new ArrayList<CustomerOrder>();
    wishListedGames = new ArrayList<Game>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public int getCustomerId()
  {
    return customerId;
  }
  /* Code from template association_GetMany */
  public PaymentDetails getPaymentCard(int index)
  {
    PaymentDetails aPaymentCard = paymentCards.get(index);
    return aPaymentCard;
  }

  public List<PaymentDetails> getPaymentCards()
  {
    List<PaymentDetails> newPaymentCards = Collections.unmodifiableList(paymentCards);
    return newPaymentCards;
  }

  public int numberOfPaymentCards()
  {
    int number = paymentCards.size();
    return number;
  }

  public boolean hasPaymentCards()
  {
    boolean has = paymentCards.size() > 0;
    return has;
  }

  public int indexOfPaymentCard(PaymentDetails aPaymentCard)
  {
    int index = paymentCards.indexOf(aPaymentCard);
    return index;
  }
  /* Code from template association_GetMany */
  public CustomerOrder getOrderHistory(int index)
  {
    CustomerOrder aOrderHistory = orderHistory.get(index);
    return aOrderHistory;
  }

  public List<CustomerOrder> getOrderHistory()
  {
    List<CustomerOrder> newOrderHistory = Collections.unmodifiableList(orderHistory);
    return newOrderHistory;
  }

  public int numberOfOrderHistory()
  {
    int number = orderHistory.size();
    return number;
  }

  public boolean hasOrderHistory()
  {
    boolean has = orderHistory.size() > 0;
    return has;
  }

  public int indexOfOrderHistory(CustomerOrder aOrderHistory)
  {
    int index = orderHistory.indexOf(aOrderHistory);
    return index;
  }
  /* Code from template association_GetMany */
  public Game getWishListedGame(int index)
  {
    Game aWishListedGame = wishListedGames.get(index);
    return aWishListedGame;
  }

  public List<Game> getWishListedGames()
  {
    List<Game> newWishListedGames = Collections.unmodifiableList(wishListedGames);
    return newWishListedGames;
  }

  public int numberOfWishListedGames()
  {
    int number = wishListedGames.size();
    return number;
  }

  public boolean hasWishListedGames()
  {
    boolean has = wishListedGames.size() > 0;
    return has;
  }

  public int indexOfWishListedGame(Game aWishListedGame)
  {
    int index = wishListedGames.indexOf(aWishListedGame);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPaymentCards()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public PaymentDetails addPaymentCard(String aCardName, String aPostalCode, int aCardNumber, int aExpMonth, int aExpYear)
  {
    return new PaymentDetails(aCardName, aPostalCode, aCardNumber, aExpMonth, aExpYear, this);
  }

  public boolean addPaymentCard(PaymentDetails aPaymentCard)
  {
    boolean wasAdded = false;
    if (paymentCards.contains(aPaymentCard)) { return false; }
    CustomerAccount existingCardOwner = aPaymentCard.getCardOwner();
    boolean isNewCardOwner = existingCardOwner != null && !this.equals(existingCardOwner);
    if (isNewCardOwner)
    {
      aPaymentCard.setCardOwner(this);
    }
    else
    {
      paymentCards.add(aPaymentCard);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePaymentCard(PaymentDetails aPaymentCard)
  {
    boolean wasRemoved = false;
    //Unable to remove aPaymentCard, as it must always have a cardOwner
    if (!this.equals(aPaymentCard.getCardOwner()))
    {
      paymentCards.remove(aPaymentCard);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPaymentCardAt(PaymentDetails aPaymentCard, int index)
  {  
    boolean wasAdded = false;
    if(addPaymentCard(aPaymentCard))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPaymentCards()) { index = numberOfPaymentCards() - 1; }
      paymentCards.remove(aPaymentCard);
      paymentCards.add(index, aPaymentCard);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePaymentCardAt(PaymentDetails aPaymentCard, int index)
  {
    boolean wasAdded = false;
    if(paymentCards.contains(aPaymentCard))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPaymentCards()) { index = numberOfPaymentCards() - 1; }
      paymentCards.remove(aPaymentCard);
      paymentCards.add(index, aPaymentCard);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPaymentCardAt(aPaymentCard, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrderHistory()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public CustomerOrder addOrderHistory(Date aOrderDate, PaymentDetails aPaymentInformation)
  {
    return new CustomerOrder(aOrderDate, this, aPaymentInformation);
  }

  public boolean addOrderHistory(CustomerOrder aOrderHistory)
  {
    boolean wasAdded = false;
    if (orderHistory.contains(aOrderHistory)) { return false; }
    CustomerAccount existingOrderedBy = aOrderHistory.getOrderedBy();
    boolean isNewOrderedBy = existingOrderedBy != null && !this.equals(existingOrderedBy);
    if (isNewOrderedBy)
    {
      aOrderHistory.setOrderedBy(this);
    }
    else
    {
      orderHistory.add(aOrderHistory);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrderHistory(CustomerOrder aOrderHistory)
  {
    boolean wasRemoved = false;
    //Unable to remove aOrderHistory, as it must always have a orderedBy
    if (!this.equals(aOrderHistory.getOrderedBy()))
    {
      orderHistory.remove(aOrderHistory);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderHistoryAt(CustomerOrder aOrderHistory, int index)
  {  
    boolean wasAdded = false;
    if(addOrderHistory(aOrderHistory))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderHistory()) { index = numberOfOrderHistory() - 1; }
      orderHistory.remove(aOrderHistory);
      orderHistory.add(index, aOrderHistory);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderHistoryAt(CustomerOrder aOrderHistory, int index)
  {
    boolean wasAdded = false;
    if(orderHistory.contains(aOrderHistory))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderHistory()) { index = numberOfOrderHistory() - 1; }
      orderHistory.remove(aOrderHistory);
      orderHistory.add(index, aOrderHistory);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderHistoryAt(aOrderHistory, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfWishListedGames()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addWishListedGame(Game aWishListedGame)
  {
    boolean wasAdded = false;
    if (wishListedGames.contains(aWishListedGame)) { return false; }
    wishListedGames.add(aWishListedGame);
    if (aWishListedGame.indexOfWishList(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aWishListedGame.addWishList(this);
      if (!wasAdded)
      {
        wishListedGames.remove(aWishListedGame);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeWishListedGame(Game aWishListedGame)
  {
    boolean wasRemoved = false;
    if (!wishListedGames.contains(aWishListedGame))
    {
      return wasRemoved;
    }

    int oldIndex = wishListedGames.indexOf(aWishListedGame);
    wishListedGames.remove(oldIndex);
    if (aWishListedGame.indexOfWishList(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aWishListedGame.removeWishList(this);
      if (!wasRemoved)
      {
        wishListedGames.add(oldIndex,aWishListedGame);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addWishListedGameAt(Game aWishListedGame, int index)
  {  
    boolean wasAdded = false;
    if(addWishListedGame(aWishListedGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWishListedGames()) { index = numberOfWishListedGames() - 1; }
      wishListedGames.remove(aWishListedGame);
      wishListedGames.add(index, aWishListedGame);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveWishListedGameAt(Game aWishListedGame, int index)
  {
    boolean wasAdded = false;
    if(wishListedGames.contains(aWishListedGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWishListedGames()) { index = numberOfWishListedGames() - 1; }
      wishListedGames.remove(aWishListedGame);
      wishListedGames.add(index, aWishListedGame);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addWishListedGameAt(aWishListedGame, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (paymentCards.size() > 0)
    {
      PaymentDetails aPaymentCard = paymentCards.get(paymentCards.size() - 1);
      aPaymentCard.delete();
      paymentCards.remove(aPaymentCard);
    }
    
    for(int i=orderHistory.size(); i > 0; i--)
    {
      CustomerOrder aOrderHistory = orderHistory.get(i - 1);
      aOrderHistory.delete();
    }
    ArrayList<Game> copyOfWishListedGames = new ArrayList<Game>(wishListedGames);
    wishListedGames.clear();
    for(Game aWishListedGame : copyOfWishListedGames)
    {
      aWishListedGame.removeWishList(this);
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "customerId" + ":" + getCustomerId()+ "]";
  }
}
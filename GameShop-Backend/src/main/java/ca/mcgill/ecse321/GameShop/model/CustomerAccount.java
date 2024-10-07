/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;
import java.sql.Date;

// line 27 "../../../../../../GameShop.ump"
public class CustomerAccount extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CustomerAccount Associations
  private List<PaymentInformation> paymentCards;
  private Review review;
  private List<Order> orderHistory;
  private List<Game> wishListedGame;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CustomerAccount(String aEmail, String aPassword)
  {
    super(aEmail, aPassword);
    paymentCards = new ArrayList<PaymentInformation>();
    orderHistory = new ArrayList<Order>();
    wishListedGame = new ArrayList<Game>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public PaymentInformation getPaymentCard(int index)
  {
    PaymentInformation aPaymentCard = paymentCards.get(index);
    return aPaymentCard;
  }

  public List<PaymentInformation> getPaymentCards()
  {
    List<PaymentInformation> newPaymentCards = Collections.unmodifiableList(paymentCards);
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

  public int indexOfPaymentCard(PaymentInformation aPaymentCard)
  {
    int index = paymentCards.indexOf(aPaymentCard);
    return index;
  }
  /* Code from template association_GetOne */
  public Review getReview()
  {
    return review;
  }

  public boolean hasReview()
  {
    boolean has = review != null;
    return has;
  }
  /* Code from template association_GetMany */
  public Order getOrderHistory(int index)
  {
    Order aOrderHistory = orderHistory.get(index);
    return aOrderHistory;
  }

  public List<Order> getOrderHistory()
  {
    List<Order> newOrderHistory = Collections.unmodifiableList(orderHistory);
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

  public int indexOfOrderHistory(Order aOrderHistory)
  {
    int index = orderHistory.indexOf(aOrderHistory);
    return index;
  }
  /* Code from template association_GetMany */
  public Game getWishListedGame(int index)
  {
    Game aWishListedGame = wishListedGame.get(index);
    return aWishListedGame;
  }

  public List<Game> getWishListedGame()
  {
    List<Game> newWishListedGame = Collections.unmodifiableList(wishListedGame);
    return newWishListedGame;
  }

  public int numberOfWishListedGame()
  {
    int number = wishListedGame.size();
    return number;
  }

  public boolean hasWishListedGame()
  {
    boolean has = wishListedGame.size() > 0;
    return has;
  }

  public int indexOfWishListedGame(Game aWishListedGame)
  {
    int index = wishListedGame.indexOf(aWishListedGame);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPaymentCards()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public PaymentInformation addPaymentCard(String aCardName, String aPostalCode, int aCardNumber, int aExpMonth, int aExpYear)
  {
    return new PaymentInformation(aCardName, aPostalCode, aCardNumber, aExpMonth, aExpYear, this);
  }

  public boolean addPaymentCard(PaymentInformation aPaymentCard)
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

  public boolean removePaymentCard(PaymentInformation aPaymentCard)
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
  public boolean addPaymentCardAt(PaymentInformation aPaymentCard, int index)
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

  public boolean addOrMovePaymentCardAt(PaymentInformation aPaymentCard, int index)
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
  /* Code from template association_SetOptionalOneToOne */
  public boolean setReview(Review aNewReview)
  {
    boolean wasSet = false;
    if (review != null && !review.equals(aNewReview) && equals(review.getCustomerAccount()))
    {
      //Unable to setReview, as existing review would become an orphan
      return wasSet;
    }

    review = aNewReview;
    CustomerAccount anOldCustomerAccount = aNewReview != null ? aNewReview.getCustomerAccount() : null;

    if (!this.equals(anOldCustomerAccount))
    {
      if (anOldCustomerAccount != null)
      {
        anOldCustomerAccount.review = null;
      }
      if (review != null)
      {
        review.setCustomerAccount(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrderHistory()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Order addOrderHistory(Date aDate, PaymentInformation aPaymentInformation, Game... allGame)
  {
    return new Order(aDate, this, aPaymentInformation, allGame);
  }

  public boolean addOrderHistory(Order aOrderHistory)
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

  public boolean removeOrderHistory(Order aOrderHistory)
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
  public boolean addOrderHistoryAt(Order aOrderHistory, int index)
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

  public boolean addOrMoveOrderHistoryAt(Order aOrderHistory, int index)
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
  public static int minimumNumberOfWishListedGame()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addWishListedGame(Game aWishListedGame)
  {
    boolean wasAdded = false;
    if (wishListedGame.contains(aWishListedGame)) { return false; }
    wishListedGame.add(aWishListedGame);
    if (aWishListedGame.indexOfInWishList(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aWishListedGame.addInWishList(this);
      if (!wasAdded)
      {
        wishListedGame.remove(aWishListedGame);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeWishListedGame(Game aWishListedGame)
  {
    boolean wasRemoved = false;
    if (!wishListedGame.contains(aWishListedGame))
    {
      return wasRemoved;
    }

    int oldIndex = wishListedGame.indexOf(aWishListedGame);
    wishListedGame.remove(oldIndex);
    if (aWishListedGame.indexOfInWishList(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aWishListedGame.removeInWishList(this);
      if (!wasRemoved)
      {
        wishListedGame.add(oldIndex,aWishListedGame);
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
      if(index > numberOfWishListedGame()) { index = numberOfWishListedGame() - 1; }
      wishListedGame.remove(aWishListedGame);
      wishListedGame.add(index, aWishListedGame);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveWishListedGameAt(Game aWishListedGame, int index)
  {
    boolean wasAdded = false;
    if(wishListedGame.contains(aWishListedGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWishListedGame()) { index = numberOfWishListedGame() - 1; }
      wishListedGame.remove(aWishListedGame);
      wishListedGame.add(index, aWishListedGame);
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
      PaymentInformation aPaymentCard = paymentCards.get(paymentCards.size() - 1);
      aPaymentCard.delete();
      paymentCards.remove(aPaymentCard);
    }
    
    Review existingReview = review;
    review = null;
    if (existingReview != null)
    {
      existingReview.delete();
    }
    for(int i=orderHistory.size(); i > 0; i--)
    {
      Order aOrderHistory = orderHistory.get(i - 1);
      aOrderHistory.delete();
    }
    ArrayList<Game> copyOfWishListedGame = new ArrayList<Game>(wishListedGame);
    wishListedGame.clear();
    for(Game aWishListedGame : copyOfWishListedGame)
    {
      aWishListedGame.removeInWishList(this);
    }
    super.delete();
  }

}
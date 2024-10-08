/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.sql.Date;
import java.util.*;

// line 64 "../../../../../../GameShop.ump"
public class Order
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum OrderStatus { SHIPPING, DELIVERED, RETURNED }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Order Attributes
  private OrderStatus orderStatus;
  private Date date;

  //Order Associations
  private Review review;
  private CustomerAccount orderedBy;
  private PaymentInformation paymentInformation;
  private List<Game> game;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Order(Date aDate, CustomerAccount aOrderedBy, PaymentInformation aPaymentInformation, Game... allGame)
  {
    date = aDate;
    boolean didAddOrderedBy = setOrderedBy(aOrderedBy);
    if (!didAddOrderedBy)
    {
      throw new RuntimeException("Unable to create orderHistory due to orderedBy. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddPaymentInformation = setPaymentInformation(aPaymentInformation);
    if (!didAddPaymentInformation)
    {
      throw new RuntimeException("Unable to create orderPayedWith due to paymentInformation. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    game = new ArrayList<Game>();
    boolean didAddGame = setGame(allGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create Order, must have at least 1 game. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setOrderStatus(OrderStatus aOrderStatus)
  {
    boolean wasSet = false;
    orderStatus = aOrderStatus;
    wasSet = true;
    return wasSet;
  }

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public OrderStatus getOrderStatus()
  {
    return orderStatus;
  }

  public Date getDate()
  {
    return date;
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
  /* Code from template association_GetOne */
  public CustomerAccount getOrderedBy()
  {
    return orderedBy;
  }
  /* Code from template association_GetOne */
  public PaymentInformation getPaymentInformation()
  {
    return paymentInformation;
  }
  /* Code from template association_GetMany */
  public Game getGame(int index)
  {
    Game aGame = game.get(index);
    return aGame;
  }

  public List<Game> getGame()
  {
    List<Game> newGame = Collections.unmodifiableList(game);
    return newGame;
  }

  public int numberOfGame()
  {
    int number = game.size();
    return number;
  }

  public boolean hasGame()
  {
    boolean has = game.size() > 0;
    return has;
  }

  public int indexOfGame(Game aGame)
  {
    int index = game.indexOf(aGame);
    return index;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setReview(Review aNewReview)
  {
    boolean wasSet = false;
    if (review != null && !review.equals(aNewReview) && equals(review.getOrder()))
    {
      //Unable to setReview, as existing review would become an orphan
      return wasSet;
    }

    review = aNewReview;
    Order anOldOrder = aNewReview != null ? aNewReview.getOrder() : null;

    if (!this.equals(anOldOrder))
    {
      if (anOldOrder != null)
      {
        anOldOrder.review = null;
      }
      if (review != null)
      {
        review.setOrder(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setOrderedBy(CustomerAccount aOrderedBy)
  {
    boolean wasSet = false;
    if (aOrderedBy == null)
    {
      return wasSet;
    }

    CustomerAccount existingOrderedBy = orderedBy;
    orderedBy = aOrderedBy;
    if (existingOrderedBy != null && !existingOrderedBy.equals(aOrderedBy))
    {
      existingOrderedBy.removeOrderHistory(this);
    }
    orderedBy.addOrderHistory(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setPaymentInformation(PaymentInformation aPaymentInformation)
  {
    boolean wasSet = false;
    if (aPaymentInformation == null)
    {
      return wasSet;
    }

    PaymentInformation existingPaymentInformation = paymentInformation;
    paymentInformation = aPaymentInformation;
    if (existingPaymentInformation != null && !existingPaymentInformation.equals(aPaymentInformation))
    {
      existingPaymentInformation.removeOrderPayedWith(this);
    }
    paymentInformation.addOrderPayedWith(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfGameValid()
  {
    boolean isValid = numberOfGame() >= minimumNumberOfGame();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfGame()
  {
    return 1;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addGame(Game aGame)
  {
    boolean wasAdded = false;
    if (game.contains(aGame)) { return false; }
    game.add(aGame);
    if (aGame.indexOfOrder(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aGame.addOrder(this);
      if (!wasAdded)
      {
        game.remove(aGame);
      }
    }
    return wasAdded;
  }
  /* Code from template association_AddMStarToMany */
  public boolean removeGame(Game aGame)
  {
    boolean wasRemoved = false;
    if (!game.contains(aGame))
    {
      return wasRemoved;
    }

    if (numberOfGame() <= minimumNumberOfGame())
    {
      return wasRemoved;
    }

    int oldIndex = game.indexOf(aGame);
    game.remove(oldIndex);
    if (aGame.indexOfOrder(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aGame.removeOrder(this);
      if (!wasRemoved)
      {
        game.add(oldIndex,aGame);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_SetMStarToMany */
  public boolean setGame(Game... newGame)
  {
    boolean wasSet = false;
    ArrayList<Game> verifiedGame = new ArrayList<Game>();
    for (Game aGame : newGame)
    {
      if (verifiedGame.contains(aGame))
      {
        continue;
      }
      verifiedGame.add(aGame);
    }

    if (verifiedGame.size() != newGame.length || verifiedGame.size() < minimumNumberOfGame())
    {
      return wasSet;
    }

    ArrayList<Game> oldGame = new ArrayList<Game>(game);
    game.clear();
    for (Game aNewGame : verifiedGame)
    {
      game.add(aNewGame);
      if (oldGame.contains(aNewGame))
      {
        oldGame.remove(aNewGame);
      }
      else
      {
        aNewGame.addOrder(this);
      }
    }

    for (Game anOldGame : oldGame)
    {
      anOldGame.removeOrder(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addGameAt(Game aGame, int index)
  {  
    boolean wasAdded = false;
    if(addGame(aGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGame()) { index = numberOfGame() - 1; }
      game.remove(aGame);
      game.add(index, aGame);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveGameAt(Game aGame, int index)
  {
    boolean wasAdded = false;
    if(game.contains(aGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGame()) { index = numberOfGame() - 1; }
      game.remove(aGame);
      game.add(index, aGame);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addGameAt(aGame, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    Review existingReview = review;
    review = null;
    if (existingReview != null)
    {
      existingReview.delete();
    }
    CustomerAccount placeholderOrderedBy = orderedBy;
    this.orderedBy = null;
    if(placeholderOrderedBy != null)
    {
      placeholderOrderedBy.removeOrderHistory(this);
    }
    PaymentInformation placeholderPaymentInformation = paymentInformation;
    this.paymentInformation = null;
    if(placeholderPaymentInformation != null)
    {
      placeholderPaymentInformation.removeOrderPayedWith(this);
    }
    ArrayList<Game> copyOfGame = new ArrayList<Game>(game);
    game.clear();
    for(Game aGame : copyOfGame)
    {
      aGame.removeOrder(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "orderStatus" + "=" + (getOrderStatus() != null ? !getOrderStatus().equals(this)  ? getOrderStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "review = "+(getReview()!=null?Integer.toHexString(System.identityHashCode(getReview())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "orderedBy = "+(getOrderedBy()!=null?Integer.toHexString(System.identityHashCode(getOrderedBy())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "paymentInformation = "+(getPaymentInformation()!=null?Integer.toHexString(System.identityHashCode(getPaymentInformation())):"null");
  }
}
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.sql.Date;
import java.util.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

// line 54 "../../../../../../model.ump"
// line 180 "../../../../../../model.ump"
@Entity
public class CustomerOrder {

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum OrderStatus { SHIPPING, DELIVERED, RETURNED }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CustomerOrder Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int orderId;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  private Date orderDate;

  //CustomerOrder Associations
  @OneToOne(mappedBy = "reviewedOrder", cascade = CascadeType.ALL)
  private Review orderReview;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private CustomerAccount orderedBy;

  @ManyToOne
  @JoinColumn(name = "payment_id")
  private PaymentDetails paymentInformation;

  @ManyToMany(mappedBy = "orders")
  private List<Game> games;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CustomerOrder(Date aOrderDate, Review aOrderReview, CustomerAccount aOrderedBy, PaymentDetails aPaymentInformation, Game... allGames)
  {
    orderDate = aOrderDate;
    if (aOrderReview == null || aOrderReview.getReviewedOrder() != null)
    {
      throw new RuntimeException("Unable to create CustomerOrder due to aOrderReview. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    orderReview = aOrderReview;
    boolean didAddOrderedBy = setOrderedBy(aOrderedBy);
    if (!didAddOrderedBy)
    {
      throw new RuntimeException("Unable to create orderHistory due to orderedBy. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddPaymentInformation = setPaymentInformation(aPaymentInformation);
    if (!didAddPaymentInformation)
    {
      throw new RuntimeException("Unable to create paidOrder due to paymentInformation. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    games = new ArrayList<Game>();
    boolean didAddGames = setGames(allGames);
    if (!didAddGames)
    {
      throw new RuntimeException("Unable to create CustomerOrder, must have at least 1 games. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public CustomerOrder(Date aOrderDate, Date aReviewDateForOrderReview, CustomerAccount aReviewAuthorForOrderReview, CustomerAccount aOrderedBy, PaymentDetails aPaymentInformation, Game... allGames)
  {
    orderDate = aOrderDate;
    orderReview = new Review(aReviewDateForOrderReview, aReviewAuthorForOrderReview, this);
    boolean didAddOrderedBy = setOrderedBy(aOrderedBy);
    if (!didAddOrderedBy)
    {
      throw new RuntimeException("Unable to create orderHistory due to orderedBy. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddPaymentInformation = setPaymentInformation(aPaymentInformation);
    if (!didAddPaymentInformation)
    {
      throw new RuntimeException("Unable to create paidOrder due to paymentInformation. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    games = new ArrayList<Game>();
    boolean didAddGames = setGames(allGames);
    if (!didAddGames)
    {
      throw new RuntimeException("Unable to create CustomerOrder, must have at least 1 games. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public CustomerOrder() {
    games = new ArrayList<Game>();
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

  public boolean setOrderDate(Date aOrderDate)
  {
    boolean wasSet = false;
    orderDate = aOrderDate;
    wasSet = true;
    return wasSet;
  }

  public int getOrderId()
  {
    return orderId;
  }

  public OrderStatus getOrderStatus()
  {
    return orderStatus;
  }

  public Date getOrderDate()
  {
    return orderDate;
  }
  /* Code from template association_GetOne */
  public Review getOrderReview()
  {
    return orderReview;
  }
  /* Code from template association_GetOne */
  public CustomerAccount getOrderedBy()
  {
    return orderedBy;
  }
  /* Code from template association_GetOne */
  public PaymentDetails getPaymentInformation()
  {
    return paymentInformation;
  }
  /* Code from template association_GetMany */
  public Game getGame(int index)
  {
    Game aGame = games.get(index);
    return aGame;
  }

  public List<Game> getGames()
  {
    List<Game> newGames = Collections.unmodifiableList(games);
    return newGames;
  }

  public int numberOfGames()
  {
    int number = games.size();
    return number;
  }

  public boolean hasGames()
  {
    boolean has = games.size() > 0;
    return has;
  }

  public int indexOfGame(Game aGame)
  {
    int index = games.indexOf(aGame);
    return index;
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
  public boolean setPaymentInformation(PaymentDetails aPaymentInformation)
  {
    boolean wasSet = false;
    if (aPaymentInformation == null)
    {
      return wasSet;
    }

    PaymentDetails existingPaymentInformation = paymentInformation;
    paymentInformation = aPaymentInformation;
    if (existingPaymentInformation != null && !existingPaymentInformation.equals(aPaymentInformation))
    {
      existingPaymentInformation.removePaidOrder(this);
    }
    paymentInformation.addPaidOrder(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfGamesValid()
  {
    boolean isValid = numberOfGames() >= minimumNumberOfGames();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfGames()
  {
    return 1;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addGame(Game aGame)
  {
    boolean wasAdded = false;
    if (games.contains(aGame)) { return false; }
    games.add(aGame);
    if (aGame.indexOfOrder(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aGame.addOrder(this);
      if (!wasAdded)
      {
        games.remove(aGame);
      }
    }
    return wasAdded;
  }
  /* Code from template association_AddMStarToMany */
  public boolean removeGame(Game aGame)
  {
    boolean wasRemoved = false;
    if (!games.contains(aGame))
    {
      return wasRemoved;
    }

    if (numberOfGames() <= minimumNumberOfGames())
    {
      return wasRemoved;
    }

    int oldIndex = games.indexOf(aGame);
    games.remove(oldIndex);
    if (aGame.indexOfOrder(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aGame.removeOrder(this);
      if (!wasRemoved)
      {
        games.add(oldIndex,aGame);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_SetMStarToMany */
  public boolean setGames(Game... newGames)
  {
    boolean wasSet = false;
    ArrayList<Game> verifiedGames = new ArrayList<Game>();
    for (Game aGame : newGames)
    {
      if (verifiedGames.contains(aGame))
      {
        continue;
      }
      verifiedGames.add(aGame);
    }

    if (verifiedGames.size() != newGames.length || verifiedGames.size() < minimumNumberOfGames())
    {
      return wasSet;
    }

    ArrayList<Game> oldGames = new ArrayList<Game>(games);
    games.clear();
    for (Game aNewGame : verifiedGames)
    {
      games.add(aNewGame);
      if (oldGames.contains(aNewGame))
      {
        oldGames.remove(aNewGame);
      }
      else
      {
        aNewGame.addOrder(this);
      }
    }

    for (Game anOldGame : oldGames)
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
      if(index > numberOfGames()) { index = numberOfGames() - 1; }
      games.remove(aGame);
      games.add(index, aGame);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveGameAt(Game aGame, int index)
  {
    boolean wasAdded = false;
    if(games.contains(aGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGames()) { index = numberOfGames() - 1; }
      games.remove(aGame);
      games.add(index, aGame);
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
    Review existingOrderReview = orderReview;
    orderReview = null;
    if (existingOrderReview != null)
    {
      existingOrderReview.delete();
    }
    CustomerAccount placeholderOrderedBy = orderedBy;
    this.orderedBy = null;
    if(placeholderOrderedBy != null)
    {
      placeholderOrderedBy.removeOrderHistory(this);
    }
    PaymentDetails placeholderPaymentInformation = paymentInformation;
    this.paymentInformation = null;
    if(placeholderPaymentInformation != null)
    {
      placeholderPaymentInformation.removePaidOrder(this);
    }
    ArrayList<Game> copyOfGames = new ArrayList<Game>(games);
    games.clear();
    for(Game aGame : copyOfGames)
    {
      aGame.removeOrder(this);
    }
  }

  public String toString()
  {
    return super.toString() + "["+
            "orderId" + ":" + getOrderId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "orderStatus" + "=" + (getOrderStatus() != null ? !getOrderStatus().equals(this)  ? getOrderStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "orderDate" + "=" + (getOrderDate() != null ? !getOrderDate().equals(this)  ? getOrderDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "orderReview = "+(getOrderReview()!=null?Integer.toHexString(System.identityHashCode(getOrderReview())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "orderedBy = "+(getOrderedBy()!=null?Integer.toHexString(System.identityHashCode(getOrderedBy())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "paymentInformation = "+(getPaymentInformation()!=null?Integer.toHexString(System.identityHashCode(getPaymentInformation())):"null");
  }
}
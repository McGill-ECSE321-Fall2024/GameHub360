/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

// line 54 "../../../../../../model.ump"
// line 187 "../../../../../../model.ump"
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
  @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL)
  private List<OrderGame> orderedGames;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private CustomerAccount orderedBy;

  @ManyToOne
  @JoinColumn(name = "payment_id")
  private PaymentDetails paymentInformation;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CustomerOrder(Date aOrderDate, CustomerAccount aOrderedBy, PaymentDetails aPaymentInformation)
  {
    orderDate = aOrderDate;
    orderedGames = new ArrayList<OrderGame>();
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
  /* Code from template association_GetMany */
  public OrderGame getOrderedGame(int index)
  {
    OrderGame aOrderedGame = orderedGames.get(index);
    return aOrderedGame;
  }

  public List<OrderGame> getOrderedGames()
  {
    List<OrderGame> newOrderedGames = Collections.unmodifiableList(orderedGames);
    return newOrderedGames;
  }

  public int numberOfOrderedGames()
  {
    int number = orderedGames.size();
    return number;
  }

  public boolean hasOrderedGames()
  {
    boolean has = orderedGames.size() > 0;
    return has;
  }

  public int indexOfOrderedGame(OrderGame aOrderedGame)
  {
    int index = orderedGames.indexOf(aOrderedGame);
    return index;
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
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfOrderedGamesValid()
  {
    boolean isValid = numberOfOrderedGames() >= minimumNumberOfOrderedGames();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrderedGames()
  {
    return 1;
  }
  /* Code from template association_AddMandatoryManyToOne */
  public OrderGame addOrderedGame(Game aGame)
  {
    OrderGame aNewOrderedGame = new OrderGame(this, aGame);
    return aNewOrderedGame;
  }

  public boolean addOrderedGame(OrderGame aOrderedGame)
  {
    boolean wasAdded = false;
    if (orderedGames.contains(aOrderedGame)) { return false; }
    CustomerOrder existingCustomerOrder = aOrderedGame.getCustomerOrder();
    boolean isNewCustomerOrder = existingCustomerOrder != null && !this.equals(existingCustomerOrder);

    if (isNewCustomerOrder && existingCustomerOrder.numberOfOrderedGames() <= minimumNumberOfOrderedGames())
    {
      return wasAdded;
    }
    if (isNewCustomerOrder)
    {
      aOrderedGame.setCustomerOrder(this);
    }
    else
    {
      orderedGames.add(aOrderedGame);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrderedGame(OrderGame aOrderedGame)
  {
    boolean wasRemoved = false;
    //Unable to remove aOrderedGame, as it must always have a customerOrder
    if (this.equals(aOrderedGame.getCustomerOrder()))
    {
      return wasRemoved;
    }

    //customerOrder already at minimum (1)
    if (numberOfOrderedGames() <= minimumNumberOfOrderedGames())
    {
      return wasRemoved;
    }

    orderedGames.remove(aOrderedGame);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderedGameAt(OrderGame aOrderedGame, int index)
  {  
    boolean wasAdded = false;
    if(addOrderedGame(aOrderedGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderedGames()) { index = numberOfOrderedGames() - 1; }
      orderedGames.remove(aOrderedGame);
      orderedGames.add(index, aOrderedGame);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderedGameAt(OrderGame aOrderedGame, int index)
  {
    boolean wasAdded = false;
    if(orderedGames.contains(aOrderedGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderedGames()) { index = numberOfOrderedGames() - 1; }
      orderedGames.remove(aOrderedGame);
      orderedGames.add(index, aOrderedGame);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderedGameAt(aOrderedGame, index);
    }
    return wasAdded;
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

  public void delete()
  {
    for(int i=orderedGames.size(); i > 0; i--)
    {
      OrderGame aOrderedGame = orderedGames.get(i - 1);
      aOrderedGame.delete();
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
  }

  public String toString()
  {
    return super.toString() + "["+
            "orderId" + ":" + getOrderId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "orderStatus" + "=" + (getOrderStatus() != null ? !getOrderStatus().equals(this)  ? getOrderStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "orderDate" + "=" + (getOrderDate() != null ? !getOrderDate().equals(this)  ? getOrderDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "orderedBy = "+(getOrderedBy()!=null?Integer.toHexString(System.identityHashCode(getOrderedBy())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "paymentInformation = "+(getPaymentInformation()!=null?Integer.toHexString(System.identityHashCode(getPaymentInformation())):"null");
  }
}
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;
import java.sql.Date;

// line 46 "../../../../../../model.ump"
// line 218 "../../../../../../model.ump"
public class PaymentDetails
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PaymentDetails Attributes
  private int paymentDetailsId;
  private String cardName;
  private String postalCode;
  private int cardNumber;
  private int expMonth;
  private int expYear;

  //PaymentDetails Associations
  private CustomerAccount cardOwner;
  private List<Order> paidOrders;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PaymentDetails(int aPaymentDetailsId, String aCardName, String aPostalCode, int aCardNumber, int aExpMonth, int aExpYear, CustomerAccount aCardOwner)
  {
    paymentDetailsId = aPaymentDetailsId;
    cardName = aCardName;
    postalCode = aPostalCode;
    cardNumber = aCardNumber;
    expMonth = aExpMonth;
    expYear = aExpYear;
    boolean didAddCardOwner = setCardOwner(aCardOwner);
    if (!didAddCardOwner)
    {
      throw new RuntimeException("Unable to create paymentCard due to cardOwner. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    paidOrders = new ArrayList<Order>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPaymentDetailsId(int aPaymentDetailsId)
  {
    boolean wasSet = false;
    paymentDetailsId = aPaymentDetailsId;
    wasSet = true;
    return wasSet;
  }

  public boolean setCardName(String aCardName)
  {
    boolean wasSet = false;
    cardName = aCardName;
    wasSet = true;
    return wasSet;
  }

  public boolean setPostalCode(String aPostalCode)
  {
    boolean wasSet = false;
    postalCode = aPostalCode;
    wasSet = true;
    return wasSet;
  }

  public boolean setCardNumber(int aCardNumber)
  {
    boolean wasSet = false;
    cardNumber = aCardNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setExpMonth(int aExpMonth)
  {
    boolean wasSet = false;
    expMonth = aExpMonth;
    wasSet = true;
    return wasSet;
  }

  public boolean setExpYear(int aExpYear)
  {
    boolean wasSet = false;
    expYear = aExpYear;
    wasSet = true;
    return wasSet;
  }

  public int getPaymentDetailsId()
  {
    return paymentDetailsId;
  }

  public String getCardName()
  {
    return cardName;
  }

  public String getPostalCode()
  {
    return postalCode;
  }

  public int getCardNumber()
  {
    return cardNumber;
  }

  public int getExpMonth()
  {
    return expMonth;
  }

  public int getExpYear()
  {
    return expYear;
  }
  /* Code from template association_GetOne */
  public CustomerAccount getCardOwner()
  {
    return cardOwner;
  }
  /* Code from template association_GetMany */
  public Order getPaidOrder(int index)
  {
    Order aPaidOrder = paidOrders.get(index);
    return aPaidOrder;
  }

  public List<Order> getPaidOrders()
  {
    List<Order> newPaidOrders = Collections.unmodifiableList(paidOrders);
    return newPaidOrders;
  }

  public int numberOfPaidOrders()
  {
    int number = paidOrders.size();
    return number;
  }

  public boolean hasPaidOrders()
  {
    boolean has = paidOrders.size() > 0;
    return has;
  }

  public int indexOfPaidOrder(Order aPaidOrder)
  {
    int index = paidOrders.indexOf(aPaidOrder);
    return index;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCardOwner(CustomerAccount aCardOwner)
  {
    boolean wasSet = false;
    if (aCardOwner == null)
    {
      return wasSet;
    }

    CustomerAccount existingCardOwner = cardOwner;
    cardOwner = aCardOwner;
    if (existingCardOwner != null && !existingCardOwner.equals(aCardOwner))
    {
      existingCardOwner.removePaymentCard(this);
    }
    cardOwner.addPaymentCard(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPaidOrders()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Order addPaidOrder(int aOrderId, Date aOrderDate, Review aOrderReview, CustomerAccount aOrderedBy, Game... allGames)
  {
    return new Order(aOrderId, aOrderDate, aOrderReview, aOrderedBy, this, allGames);
  }

  public boolean addPaidOrder(Order aPaidOrder)
  {
    boolean wasAdded = false;
    if (paidOrders.contains(aPaidOrder)) { return false; }
    PaymentDetails existingPaymentInformation = aPaidOrder.getPaymentInformation();
    boolean isNewPaymentInformation = existingPaymentInformation != null && !this.equals(existingPaymentInformation);
    if (isNewPaymentInformation)
    {
      aPaidOrder.setPaymentInformation(this);
    }
    else
    {
      paidOrders.add(aPaidOrder);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePaidOrder(Order aPaidOrder)
  {
    boolean wasRemoved = false;
    //Unable to remove aPaidOrder, as it must always have a paymentInformation
    if (!this.equals(aPaidOrder.getPaymentInformation()))
    {
      paidOrders.remove(aPaidOrder);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPaidOrderAt(Order aPaidOrder, int index)
  {  
    boolean wasAdded = false;
    if(addPaidOrder(aPaidOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPaidOrders()) { index = numberOfPaidOrders() - 1; }
      paidOrders.remove(aPaidOrder);
      paidOrders.add(index, aPaidOrder);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePaidOrderAt(Order aPaidOrder, int index)
  {
    boolean wasAdded = false;
    if(paidOrders.contains(aPaidOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPaidOrders()) { index = numberOfPaidOrders() - 1; }
      paidOrders.remove(aPaidOrder);
      paidOrders.add(index, aPaidOrder);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPaidOrderAt(aPaidOrder, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    CustomerAccount placeholderCardOwner = cardOwner;
    this.cardOwner = null;
    if(placeholderCardOwner != null)
    {
      placeholderCardOwner.removePaymentCard(this);
    }
    for(int i=paidOrders.size(); i > 0; i--)
    {
      Order aPaidOrder = paidOrders.get(i - 1);
      aPaidOrder.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "paymentDetailsId" + ":" + getPaymentDetailsId()+ "," +
            "cardName" + ":" + getCardName()+ "," +
            "postalCode" + ":" + getPostalCode()+ "," +
            "cardNumber" + ":" + getCardNumber()+ "," +
            "expMonth" + ":" + getExpMonth()+ "," +
            "expYear" + ":" + getExpYear()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "cardOwner = "+(getCardOwner()!=null?Integer.toHexString(System.identityHashCode(getCardOwner())):"null");
  }
}
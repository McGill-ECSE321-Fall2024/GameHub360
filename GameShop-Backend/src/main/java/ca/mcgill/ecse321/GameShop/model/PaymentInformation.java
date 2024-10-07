/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;
import java.sql.Date;

// line 56 "../../../../../../GameShop.ump"
public class PaymentInformation
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PaymentInformation Attributes
  private String cardName;
  private String postalCode;
  private int cardNumber;
  private int expMonth;
  private int expYear;

  //PaymentInformation Associations
  private CustomerAccount cardOwner;
  private List<Order> orderPayedWith;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PaymentInformation(String aCardName, String aPostalCode, int aCardNumber, int aExpMonth, int aExpYear, CustomerAccount aCardOwner)
  {
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
    orderPayedWith = new ArrayList<Order>();
  }

  //------------------------
  // INTERFACE
  //------------------------

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
  public Order getOrderPayedWith(int index)
  {
    Order aOrderPayedWith = orderPayedWith.get(index);
    return aOrderPayedWith;
  }

  public List<Order> getOrderPayedWith()
  {
    List<Order> newOrderPayedWith = Collections.unmodifiableList(orderPayedWith);
    return newOrderPayedWith;
  }

  public int numberOfOrderPayedWith()
  {
    int number = orderPayedWith.size();
    return number;
  }

  public boolean hasOrderPayedWith()
  {
    boolean has = orderPayedWith.size() > 0;
    return has;
  }

  public int indexOfOrderPayedWith(Order aOrderPayedWith)
  {
    int index = orderPayedWith.indexOf(aOrderPayedWith);
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
  public static int minimumNumberOfOrderPayedWith()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Order addOrderPayedWith(Date aDate, CustomerAccount aOrderedBy, Game... allGame)
  {
    return new Order(aDate, aOrderedBy, this, allGame);
  }

  public boolean addOrderPayedWith(Order aOrderPayedWith)
  {
    boolean wasAdded = false;
    if (orderPayedWith.contains(aOrderPayedWith)) { return false; }
    PaymentInformation existingPaymentInformation = aOrderPayedWith.getPaymentInformation();
    boolean isNewPaymentInformation = existingPaymentInformation != null && !this.equals(existingPaymentInformation);
    if (isNewPaymentInformation)
    {
      aOrderPayedWith.setPaymentInformation(this);
    }
    else
    {
      orderPayedWith.add(aOrderPayedWith);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrderPayedWith(Order aOrderPayedWith)
  {
    boolean wasRemoved = false;
    //Unable to remove aOrderPayedWith, as it must always have a paymentInformation
    if (!this.equals(aOrderPayedWith.getPaymentInformation()))
    {
      orderPayedWith.remove(aOrderPayedWith);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderPayedWithAt(Order aOrderPayedWith, int index)
  {  
    boolean wasAdded = false;
    if(addOrderPayedWith(aOrderPayedWith))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderPayedWith()) { index = numberOfOrderPayedWith() - 1; }
      orderPayedWith.remove(aOrderPayedWith);
      orderPayedWith.add(index, aOrderPayedWith);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderPayedWithAt(Order aOrderPayedWith, int index)
  {
    boolean wasAdded = false;
    if(orderPayedWith.contains(aOrderPayedWith))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderPayedWith()) { index = numberOfOrderPayedWith() - 1; }
      orderPayedWith.remove(aOrderPayedWith);
      orderPayedWith.add(index, aOrderPayedWith);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderPayedWithAt(aOrderPayedWith, index);
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
    for(int i=orderPayedWith.size(); i > 0; i--)
    {
      Order aOrderPayedWith = orderPayedWith.get(i - 1);
      aOrderPayedWith.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "cardName" + ":" + getCardName()+ "," +
            "postalCode" + ":" + getPostalCode()+ "," +
            "cardNumber" + ":" + getCardNumber()+ "," +
            "expMonth" + ":" + getExpMonth()+ "," +
            "expYear" + ":" + getExpYear()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "cardOwner = "+(getCardOwner()!=null?Integer.toHexString(System.identityHashCode(getCardOwner())):"null");
  }
}
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.sql.Date;

// line 45 "../../../../../../model.ump"
// line 182 "../../../../../../model.ump"
@Entity
public class PaymentDetails
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PaymentDetails Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int paymentDetailsId;

  private String cardName;
  private String postalCode;
  private String cardNumber;
  private int expMonth;
  private int expYear;

  //PaymentDetails Associations
  @ManyToOne
  @JoinColumn(name = "customer_id")
  private CustomerAccount cardOwner;

  @OneToMany(mappedBy = "paymentInformation", cascade = CascadeType.ALL)
  private List<CustomerOrder> paidOrders;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PaymentDetails(String aCardName, String aPostalCode, String aCardNumber, int aExpMonth, int aExpYear, CustomerAccount aCardOwner) 
  {
    this.cardName = aCardName;
    this.postalCode = aPostalCode;
    this.cardNumber = aCardNumber;
    this.expMonth = aExpMonth;
    this.expYear = aExpYear;
    boolean didAddCardOwner = setCardOwner(aCardOwner);
    if (!didAddCardOwner) {
        throw new RuntimeException("Unable to create paymentCard due to cardOwner. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    this.paidOrders = new ArrayList<CustomerOrder>();
  }

  public PaymentDetails() {
    paidOrders = new ArrayList<CustomerOrder>();
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

  public boolean setCardNumber(String aCardNumber) 
  {
    boolean wasSet = false;
    this.cardNumber = aCardNumber;
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

  public String getCardNumber() 
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
  public CustomerOrder getPaidOrder(int index)
  {
    CustomerOrder aPaidOrder = paidOrders.get(index);
    return aPaidOrder;
  }

  public List<CustomerOrder> getPaidOrders()
  {
    List<CustomerOrder> newPaidOrders = Collections.unmodifiableList(paidOrders);
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

  public int indexOfPaidOrder(CustomerOrder aPaidOrder)
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
  public CustomerOrder addPaidOrder(Date aOrderDate, CustomerAccount aOrderedBy)
  {
    return new CustomerOrder( aOrderDate, aOrderedBy, this);
  }

  public boolean addPaidOrder(CustomerOrder aPaidOrder)
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

  public boolean removePaidOrder(CustomerOrder aPaidOrder)
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
  public boolean addPaidOrderAt(CustomerOrder aPaidOrder, int index)
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

  public boolean addOrMovePaidOrderAt(CustomerOrder aPaidOrder, int index)
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
      CustomerOrder aPaidOrder = paidOrders.get(i - 1);
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
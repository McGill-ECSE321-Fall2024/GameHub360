/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.sql.Date;
import java.util.*;

// line 113 "../../../../../GameShop.ump"
public class Review
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum GameReviewRating { ONE_STAR, TWO_STARS, THREE_STARS, FOUR_STARS, FIVE_STARS }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Review Attributes
  private GameReviewRating rating;
  private String comment;
  private Date date;

  //Review Associations
  private CustomerAccount customerAccount;
  private Order order;
  private List<ReviewReply> replies;

  //Helper Variables
  private int cachedHashCode;
  private boolean canSetCustomerAccount;
  private boolean canSetOrder;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Review(Date aDate, CustomerAccount aCustomerAccount, Order aOrder)
  {
    cachedHashCode = -1;
    canSetCustomerAccount = true;
    canSetOrder = true;
    comment = null;
    date = aDate;
    boolean didAddCustomerAccount = setCustomerAccount(aCustomerAccount);
    if (!didAddCustomerAccount)
    {
      throw new RuntimeException("Unable to create review due to customerAccount. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddOrder = setOrder(aOrder);
    if (!didAddOrder)
    {
      throw new RuntimeException("Unable to create review due to order. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    replies = new ArrayList<ReviewReply>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRating(GameReviewRating aRating)
  {
    boolean wasSet = false;
    rating = aRating;
    wasSet = true;
    return wasSet;
  }

  public boolean setComment(String aComment)
  {
    boolean wasSet = false;
    comment = aComment;
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

  public GameReviewRating getRating()
  {
    return rating;
  }

  public String getComment()
  {
    return comment;
  }

  public Date getDate()
  {
    return date;
  }
  /* Code from template association_GetOne */
  public CustomerAccount getCustomerAccount()
  {
    return customerAccount;
  }
  /* Code from template association_GetOne */
  public Order getOrder()
  {
    return order;
  }
  /* Code from template association_GetMany */
  public ReviewReply getReply(int index)
  {
    ReviewReply aReply = replies.get(index);
    return aReply;
  }

  public List<ReviewReply> getReplies()
  {
    List<ReviewReply> newReplies = Collections.unmodifiableList(replies);
    return newReplies;
  }

  public int numberOfReplies()
  {
    int number = replies.size();
    return number;
  }

  public boolean hasReplies()
  {
    boolean has = replies.size() > 0;
    return has;
  }

  public int indexOfReply(ReviewReply aReply)
  {
    int index = replies.indexOf(aReply);
    return index;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setCustomerAccount(CustomerAccount aNewCustomerAccount)
  {
    boolean wasSet = false;
    if (!canSetCustomerAccount) { return false; }
    if (aNewCustomerAccount == null)
    {
      //Unable to setCustomerAccount to null, as review must always be associated to a customerAccount
      return wasSet;
    }
    
    Review existingReview = aNewCustomerAccount.getReview();
    if (existingReview != null && !equals(existingReview))
    {
      //Unable to setCustomerAccount, the current customerAccount already has a review, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    CustomerAccount anOldCustomerAccount = customerAccount;
    customerAccount = aNewCustomerAccount;
    customerAccount.setReview(this);

    if (anOldCustomerAccount != null)
    {
      anOldCustomerAccount.setReview(null);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setOrder(Order aNewOrder)
  {
    boolean wasSet = false;
    if (!canSetOrder) { return false; }
    if (aNewOrder == null)
    {
      //Unable to setOrder to null, as review must always be associated to a order
      return wasSet;
    }
    
    Review existingReview = aNewOrder.getReview();
    if (existingReview != null && !equals(existingReview))
    {
      //Unable to setOrder, the current order already has a review, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Order anOldOrder = order;
    order = aNewOrder;
    order.setReview(this);

    if (anOldOrder != null)
    {
      anOldOrder.setReview(null);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfReplies()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ReviewReply addReply(String aContent, Date aDate, ManagerAccount aReviewr)
  {
    return new ReviewReply(aContent, aDate, this, aReviewr);
  }

  public boolean addReply(ReviewReply aReply)
  {
    boolean wasAdded = false;
    if (replies.contains(aReply)) { return false; }
    Review existingReview = aReply.getReview();
    boolean isNewReview = existingReview != null && !this.equals(existingReview);
    if (isNewReview)
    {
      aReply.setReview(this);
    }
    else
    {
      replies.add(aReply);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeReply(ReviewReply aReply)
  {
    boolean wasRemoved = false;
    //Unable to remove aReply, as it must always have a review
    if (!this.equals(aReply.getReview()))
    {
      replies.remove(aReply);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addReplyAt(ReviewReply aReply, int index)
  {  
    boolean wasAdded = false;
    if(addReply(aReply))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReplies()) { index = numberOfReplies() - 1; }
      replies.remove(aReply);
      replies.add(index, aReply);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveReplyAt(ReviewReply aReply, int index)
  {
    boolean wasAdded = false;
    if(replies.contains(aReply))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReplies()) { index = numberOfReplies() - 1; }
      replies.remove(aReply);
      replies.add(index, aReply);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addReplyAt(aReply, index);
    }
    return wasAdded;
  }

  public boolean equals(Object obj)
  {
    if (obj == null) { return false; }
    if (!getClass().equals(obj.getClass())) { return false; }

    Review compareTo = (Review)obj;
  
    if (getCustomerAccount() == null && compareTo.getCustomerAccount() != null)
    {
      return false;
    }
    else if (getCustomerAccount() != null && !getCustomerAccount().equals(compareTo.getCustomerAccount()))
    {
      return false;
    }

    if (getOrder() == null && compareTo.getOrder() != null)
    {
      return false;
    }
    else if (getOrder() != null && !getOrder().equals(compareTo.getOrder()))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    if (cachedHashCode != -1)
    {
      return cachedHashCode;
    }
    cachedHashCode = 17;
    if (getCustomerAccount() != null)
    {
      cachedHashCode = cachedHashCode * 23 + getCustomerAccount().hashCode();
    }
    else
    {
      cachedHashCode = cachedHashCode * 23;
    }
    if (getOrder() != null)
    {
      cachedHashCode = cachedHashCode * 23 + getOrder().hashCode();
    }
    else
    {
      cachedHashCode = cachedHashCode * 23;
    }

    canSetCustomerAccount = false;
    canSetOrder = false;
    return cachedHashCode;
  }

  public void delete()
  {
    CustomerAccount existingCustomerAccount = customerAccount;
    customerAccount = null;
    if (existingCustomerAccount != null)
    {
      existingCustomerAccount.setReview(null);
    }
    Order existingOrder = order;
    order = null;
    if (existingOrder != null)
    {
      existingOrder.setReview(null);
    }
    while (replies.size() > 0)
    {
      ReviewReply aReply = replies.get(replies.size() - 1);
      aReply.delete();
      replies.remove(aReply);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "comment" + ":" + getComment()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "rating" + "=" + (getRating() != null ? !getRating().equals(this)  ? getRating().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "customerAccount = "+(getCustomerAccount()!=null?Integer.toHexString(System.identityHashCode(getCustomerAccount())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "order = "+(getOrder()!=null?Integer.toHexString(System.identityHashCode(getOrder())):"null");
  }
}
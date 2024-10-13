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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

// line 106 "../../../../../../model.ump"
// line 206 "../../../../../../model.ump"
@Entity
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
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int reviewId;

  @Enumerated(EnumType.STRING)
  private GameReviewRating rating;

  private String comment;
  private Date reviewDate;

  //Review Associations
  @OneToMany(mappedBy = "reviewRecord", cascade = CascadeType.ALL)
  private List<Reply> reviewReplies;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private CustomerAccount reviewAuthor;

  @OneToOne
  @JoinColumn(name = "order_id")
  private CustomerOrder reviewedOrder;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Review(Date aReviewDate, CustomerAccount aReviewAuthor, CustomerOrder aReviewedOrder)
  {
    comment = null;
    reviewDate = aReviewDate;
    reviewReplies = new ArrayList<Reply>();
    boolean didAddReviewAuthor = setReviewAuthor(aReviewAuthor);
    if (!didAddReviewAuthor)
    {
      throw new RuntimeException("Unable to create review due to reviewAuthor. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (aReviewedOrder == null || aReviewedOrder.getOrderReview() != null)
    {
      throw new RuntimeException("Unable to create Review due to aReviewedOrder. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    reviewedOrder = aReviewedOrder;
  }

  public Review(Date aReviewDate, CustomerAccount aReviewAuthor, Date aOrderDateForReviewedOrder, CustomerAccount aOrderedByForReviewedOrder, PaymentDetails aPaymentInformationForReviewedOrder, Game... allGamesForReviewedOrder)
  {
    comment = null;
    reviewDate = aReviewDate;
    reviewReplies = new ArrayList<Reply>();
    boolean didAddReviewAuthor = setReviewAuthor(aReviewAuthor);
    if (!didAddReviewAuthor)
    {
      throw new RuntimeException("Unable to create review due to reviewAuthor. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    reviewedOrder = new CustomerOrder(aOrderDateForReviewedOrder, this, aOrderedByForReviewedOrder, aPaymentInformationForReviewedOrder, allGamesForReviewedOrder);
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

  public boolean setReviewDate(Date aReviewDate)
  {
    boolean wasSet = false;
    reviewDate = aReviewDate;
    wasSet = true;
    return wasSet;
  }

  public int getReviewId()
  {
    return reviewId;
  }

  public GameReviewRating getRating()
  {
    return rating;
  }

  public String getComment()
  {
    return comment;
  }

  public Date getReviewDate()
  {
    return reviewDate;
  }
  /* Code from template association_GetMany */
  public Reply getReviewReply(int index)
  {
    Reply aReviewReply = reviewReplies.get(index);
    return aReviewReply;
  }

  public List<Reply> getReviewReplies()
  {
    List<Reply> newReviewReplies = Collections.unmodifiableList(reviewReplies);
    return newReviewReplies;
  }

  public int numberOfReviewReplies()
  {
    int number = reviewReplies.size();
    return number;
  }

  public boolean hasReviewReplies()
  {
    boolean has = reviewReplies.size() > 0;
    return has;
  }

  public int indexOfReviewReply(Reply aReviewReply)
  {
    int index = reviewReplies.indexOf(aReviewReply);
    return index;
  }
  /* Code from template association_GetOne */
  public CustomerAccount getReviewAuthor()
  {
    return reviewAuthor;
  }
  /* Code from template association_GetOne */
  public CustomerOrder getReviewedOrder()
  {
    return reviewedOrder;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfReviewReplies()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Reply addReviewReply(String aContent, Date aReplyDate, ManagerAccount aReviewer)
  {
    return new Reply(aContent, aReplyDate, this, aReviewer);
  }

  public boolean addReviewReply(Reply aReviewReply)
  {
    boolean wasAdded = false;
    if (reviewReplies.contains(aReviewReply)) { return false; }
    Review existingReviewRecord = aReviewReply.getReviewRecord();
    boolean isNewReviewRecord = existingReviewRecord != null && !this.equals(existingReviewRecord);
    if (isNewReviewRecord)
    {
      aReviewReply.setReviewRecord(this);
    }
    else
    {
      reviewReplies.add(aReviewReply);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeReviewReply(Reply aReviewReply)
  {
    boolean wasRemoved = false;
    //Unable to remove aReviewReply, as it must always have a reviewRecord
    if (!this.equals(aReviewReply.getReviewRecord()))
    {
      reviewReplies.remove(aReviewReply);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addReviewReplyAt(Reply aReviewReply, int index)
  {  
    boolean wasAdded = false;
    if(addReviewReply(aReviewReply))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReviewReplies()) { index = numberOfReviewReplies() - 1; }
      reviewReplies.remove(aReviewReply);
      reviewReplies.add(index, aReviewReply);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveReviewReplyAt(Reply aReviewReply, int index)
  {
    boolean wasAdded = false;
    if(reviewReplies.contains(aReviewReply))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReviewReplies()) { index = numberOfReviewReplies() - 1; }
      reviewReplies.remove(aReviewReply);
      reviewReplies.add(index, aReviewReply);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addReviewReplyAt(aReviewReply, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setReviewAuthor(CustomerAccount aReviewAuthor)
  {
    boolean wasSet = false;
    if (aReviewAuthor == null)
    {
      return wasSet;
    }

    CustomerAccount existingReviewAuthor = reviewAuthor;
    reviewAuthor = aReviewAuthor;
    if (existingReviewAuthor != null && !existingReviewAuthor.equals(aReviewAuthor))
    {
      existingReviewAuthor.removeReview(this);
    }
    reviewAuthor.addReview(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    while (reviewReplies.size() > 0)
    {
      Reply aReviewReply = reviewReplies.get(reviewReplies.size() - 1);
      aReviewReply.delete();
      reviewReplies.remove(aReviewReply);
    }
    
    CustomerAccount placeholderReviewAuthor = reviewAuthor;
    this.reviewAuthor = null;
    if(placeholderReviewAuthor != null)
    {
      placeholderReviewAuthor.removeReview(this);
    }
    CustomerOrder existingReviewedOrder = reviewedOrder;
    reviewedOrder = null;
    if (existingReviewedOrder != null)
    {
      existingReviewedOrder.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "reviewId" + ":" + getReviewId()+ "," +
            "comment" + ":" + getComment()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "rating" + "=" + (getRating() != null ? !getRating().equals(this)  ? getRating().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "reviewDate" + "=" + (getReviewDate() != null ? !getReviewDate().equals(this)  ? getReviewDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "reviewAuthor = "+(getReviewAuthor()!=null?Integer.toHexString(System.identityHashCode(getReviewAuthor())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "reviewedOrder = "+(getReviewedOrder()!=null?Integer.toHexString(System.identityHashCode(getReviewedOrder())):"null");
  }
}
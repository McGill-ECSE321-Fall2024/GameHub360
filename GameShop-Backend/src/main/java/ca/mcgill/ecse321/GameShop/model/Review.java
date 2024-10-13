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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

// line 106 "../../../../../../model.ump"
// line 222 "../../../../../../model.ump"
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

  @OneToOne(mappedBy = "review")
  private OrderGame reviewedGame;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Review(Date aReviewDate, OrderGame aReviewedGame)
  {
    comment = null;
    reviewDate = aReviewDate;
    reviewReplies = new ArrayList<Reply>();
    boolean didAddReviewedGame = setReviewedGame(aReviewedGame);
    if (!didAddReviewedGame)
    {
      throw new RuntimeException("Unable to create review due to reviewedGame. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public Review() {
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
  public OrderGame getReviewedGame()
  {
    return reviewedGame;
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
  /* Code from template association_SetOneToOptionalOne */
  public boolean setReviewedGame(OrderGame aNewReviewedGame)
  {
    boolean wasSet = false;
    if (aNewReviewedGame == null)
    {
      //Unable to setReviewedGame to null, as review must always be associated to a reviewedGame
      return wasSet;
    }
    
    Review existingReview = aNewReviewedGame.getReview();
    if (existingReview != null && !equals(existingReview))
    {
      //Unable to setReviewedGame, the current reviewedGame already has a review, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    OrderGame anOldReviewedGame = reviewedGame;
    reviewedGame = aNewReviewedGame;
    reviewedGame.setReview(this);

    if (anOldReviewedGame != null)
    {
      anOldReviewedGame.setReview(null);
    }
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
    
    OrderGame existingReviewedGame = reviewedGame;
    reviewedGame = null;
    if (existingReviewedGame != null)
    {
      existingReviewedGame.setReview(null);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "reviewId" + ":" + getReviewId()+ "," +
            "comment" + ":" + getComment()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "rating" + "=" + (getRating() != null ? !getRating().equals(this)  ? getRating().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "reviewDate" + "=" + (getReviewDate() != null ? !getReviewDate().equals(this)  ? getReviewDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "reviewedGame = "+(getReviewedGame()!=null?Integer.toHexString(System.identityHashCode(getReviewedGame())):"null");
  }
}
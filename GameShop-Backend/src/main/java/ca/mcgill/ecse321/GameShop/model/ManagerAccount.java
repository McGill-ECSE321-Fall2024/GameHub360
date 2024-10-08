/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;
import java.sql.Date;

// line 42 "../../../../../../GameShop.ump"
public class ManagerAccount extends StaffAccount
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ManagerAccount Associations
  private List<ReviewReply> reviewReplies;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ManagerAccount(String aEmail, String aPassword)
  {
    super(aEmail, aPassword);
    reviewReplies = new ArrayList<ReviewReply>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public ReviewReply getReviewReply(int index)
  {
    ReviewReply aReviewReply = reviewReplies.get(index);
    return aReviewReply;
  }

  public List<ReviewReply> getReviewReplies()
  {
    List<ReviewReply> newReviewReplies = Collections.unmodifiableList(reviewReplies);
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

  public int indexOfReviewReply(ReviewReply aReviewReply)
  {
    int index = reviewReplies.indexOf(aReviewReply);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfReviewReplies()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ReviewReply addReviewReply(String aContent, Date aDate, Review aReview)
  {
    return new ReviewReply(aContent, aDate, aReview, this);
  }

  public boolean addReviewReply(ReviewReply aReviewReply)
  {
    boolean wasAdded = false;
    if (reviewReplies.contains(aReviewReply)) { return false; }
    ManagerAccount existingReviewr = aReviewReply.getReviewr();
    boolean isNewReviewr = existingReviewr != null && !this.equals(existingReviewr);
    if (isNewReviewr)
    {
      aReviewReply.setReviewr(this);
    }
    else
    {
      reviewReplies.add(aReviewReply);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeReviewReply(ReviewReply aReviewReply)
  {
    boolean wasRemoved = false;
    //Unable to remove aReviewReply, as it must always have a reviewr
    if (!this.equals(aReviewReply.getReviewr()))
    {
      reviewReplies.remove(aReviewReply);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addReviewReplyAt(ReviewReply aReviewReply, int index)
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

  public boolean addOrMoveReviewReplyAt(ReviewReply aReviewReply, int index)
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

  public void delete()
  {
    for(int i=reviewReplies.size(); i > 0; i--)
    {
      ReviewReply aReviewReply = reviewReplies.get(i - 1);
      aReviewReply.delete();
    }
    super.delete();
  }

}
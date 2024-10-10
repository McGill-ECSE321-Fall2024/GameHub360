/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;
import java.sql.Date;

// line 30 "../../../../../../model.ump"
// line 163 "../../../../../../model.ump"
public class ManagerAccount extends StaffAccount
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ManagerAccount Associations
  private List<Reply> reviewReplies;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ManagerAccount(String aEmail, String aPassword, int aStaffId)
  {
    super(aEmail, aPassword, aStaffId);
    reviewReplies = new ArrayList<Reply>();
  }

  //------------------------
  // INTERFACE
  //------------------------
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfReviewReplies()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Reply addReviewReply(int aReplyId, String aContent, Date aReplyDate, Review aReviewRecord)
  {
    return new Reply(aReplyId, aContent, aReplyDate, aReviewRecord, this);
  }

  public boolean addReviewReply(Reply aReviewReply)
  {
    boolean wasAdded = false;
    if (reviewReplies.contains(aReviewReply)) { return false; }
    ManagerAccount existingReviewer = aReviewReply.getReviewer();
    boolean isNewReviewer = existingReviewer != null && !this.equals(existingReviewer);
    if (isNewReviewer)
    {
      aReviewReply.setReviewer(this);
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
    //Unable to remove aReviewReply, as it must always have a reviewer
    if (!this.equals(aReviewReply.getReviewer()))
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

  public void delete()
  {
    for(int i=reviewReplies.size(); i > 0; i--)
    {
      Reply aReviewReply = reviewReplies.get(i - 1);
      aReviewReply.delete();
    }
    super.delete();
  }

}
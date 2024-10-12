/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// line 116 "../../../../../../model.ump"
// line 227 "../../../../../../model.ump"
@Entity
public class Reply
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Reply Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int replyId;

  private String content;
  private Date replyDate;
  
  //Reply Associations
  @ManyToOne
  @JoinColumn(name = "review_id")
  private Review reviewRecord;

  @ManyToOne
  @JoinColumn(name = "staff_id")
  private ManagerAccount reviewer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Reply(String aContent, Date aReplyDate, Review aReviewRecord, ManagerAccount aReviewer)
  {
    content = aContent;
    replyDate = aReplyDate;
    boolean didAddReviewRecord = setReviewRecord(aReviewRecord);
    if (!didAddReviewRecord)
    {
      throw new RuntimeException("Unable to create reviewReply due to reviewRecord. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddReviewer = setReviewer(aReviewer);
    if (!didAddReviewer)
    {
      throw new RuntimeException("Unable to create reviewReply due to reviewer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setContent(String aContent)
  {
    boolean wasSet = false;
    content = aContent;
    wasSet = true;
    return wasSet;
  }

  public boolean setReplyDate(Date aReplyDate)
  {
    boolean wasSet = false;
    replyDate = aReplyDate;
    wasSet = true;
    return wasSet;
  }

  public int getReplyId()
  {
    return replyId;
  }

  public String getContent()
  {
    return content;
  }

  public Date getReplyDate()
  {
    return replyDate;
  }
  /* Code from template association_GetOne */
  public Review getReviewRecord()
  {
    return reviewRecord;
  }
  /* Code from template association_GetOne */
  public ManagerAccount getReviewer()
  {
    return reviewer;
  }
  /* Code from template association_SetOneToMany */
  public boolean setReviewRecord(Review aReviewRecord)
  {
    boolean wasSet = false;
    if (aReviewRecord == null)
    {
      return wasSet;
    }

    Review existingReviewRecord = reviewRecord;
    reviewRecord = aReviewRecord;
    if (existingReviewRecord != null && !existingReviewRecord.equals(aReviewRecord))
    {
      existingReviewRecord.removeReviewReply(this);
    }
    reviewRecord.addReviewReply(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setReviewer(ManagerAccount aReviewer)
  {
    boolean wasSet = false;
    if (aReviewer == null)
    {
      return wasSet;
    }

    ManagerAccount existingReviewer = reviewer;
    reviewer = aReviewer;
    if (existingReviewer != null && !existingReviewer.equals(aReviewer))
    {
      existingReviewer.removeReviewReply(this);
    }
    reviewer.addReviewReply(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Review placeholderReviewRecord = reviewRecord;
    this.reviewRecord = null;
    if(placeholderReviewRecord != null)
    {
      placeholderReviewRecord.removeReviewReply(this);
    }
    ManagerAccount placeholderReviewer = reviewer;
    this.reviewer = null;
    if(placeholderReviewer != null)
    {
      placeholderReviewer.removeReviewReply(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "replyId" + ":" + getReplyId()+ "," +
            "content" + ":" + getContent()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "replyDate" + "=" + (getReplyDate() != null ? !getReplyDate().equals(this)  ? getReplyDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "reviewRecord = "+(getReviewRecord()!=null?Integer.toHexString(System.identityHashCode(getReviewRecord())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "reviewer = "+(getReviewer()!=null?Integer.toHexString(System.identityHashCode(getReviewer())):"null");
  }
}
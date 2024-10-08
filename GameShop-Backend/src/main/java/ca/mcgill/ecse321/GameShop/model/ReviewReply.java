/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.sql.Date;

// line 126 "../../../../../../GameShop.ump"
public class ReviewReply
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ReviewReply Attributes
  private String content;
  private Date date;

  //ReviewReply Associations
  private Review review;
  private ManagerAccount reviewr;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ReviewReply(String aContent, Date aDate, Review aReview, ManagerAccount aReviewr)
  {
    content = aContent;
    date = aDate;
    boolean didAddReview = setReview(aReview);
    if (!didAddReview)
    {
      throw new RuntimeException("Unable to create reply due to review. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddReviewr = setReviewr(aReviewr);
    if (!didAddReviewr)
    {
      throw new RuntimeException("Unable to create reviewReply due to reviewr. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public String getContent()
  {
    return content;
  }

  public Date getDate()
  {
    return date;
  }
  /* Code from template association_GetOne */
  public Review getReview()
  {
    return review;
  }
  /* Code from template association_GetOne */
  public ManagerAccount getReviewr()
  {
    return reviewr;
  }
  /* Code from template association_SetOneToMany */
  public boolean setReview(Review aReview)
  {
    boolean wasSet = false;
    if (aReview == null)
    {
      return wasSet;
    }

    Review existingReview = review;
    review = aReview;
    if (existingReview != null && !existingReview.equals(aReview))
    {
      existingReview.removeReply(this);
    }
    review.addReply(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setReviewr(ManagerAccount aReviewr)
  {
    boolean wasSet = false;
    if (aReviewr == null)
    {
      return wasSet;
    }

    ManagerAccount existingReviewr = reviewr;
    reviewr = aReviewr;
    if (existingReviewr != null && !existingReviewr.equals(aReviewr))
    {
      existingReviewr.removeReviewReply(this);
    }
    reviewr.addReviewReply(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Review placeholderReview = review;
    this.review = null;
    if(placeholderReview != null)
    {
      placeholderReview.removeReply(this);
    }
    ManagerAccount placeholderReviewr = reviewr;
    this.reviewr = null;
    if(placeholderReviewr != null)
    {
      placeholderReviewr.removeReviewReply(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "content" + ":" + getContent()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "review = "+(getReview()!=null?Integer.toHexString(System.identityHashCode(getReview())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "reviewr = "+(getReviewr()!=null?Integer.toHexString(System.identityHashCode(getReviewr())):"null");
  }
}
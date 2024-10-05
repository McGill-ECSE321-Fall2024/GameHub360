/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse.gameshop.model;
import java.util.*;
import java.sql.Date;

// line 39 "../../../../../../GameShop.ump"
public class ManagerAccount extends StaffAccount
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ManagerAccount Associations
  private List<GameShop> gameShops;
  private List<ReviewReply> reviewReplies;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ManagerAccount(String aEmail, String aPassword)
  {
    super(aEmail, aPassword);
    gameShops = new ArrayList<GameShop>();
    reviewReplies = new ArrayList<ReviewReply>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public GameShop getGameShop(int index)
  {
    GameShop aGameShop = gameShops.get(index);
    return aGameShop;
  }

  public List<GameShop> getGameShops()
  {
    List<GameShop> newGameShops = Collections.unmodifiableList(gameShops);
    return newGameShops;
  }

  public int numberOfGameShops()
  {
    int number = gameShops.size();
    return number;
  }

  public boolean hasGameShops()
  {
    boolean has = gameShops.size() > 0;
    return has;
  }

  public int indexOfGameShop(GameShop aGameShop)
  {
    int index = gameShops.indexOf(aGameShop);
    return index;
  }
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
  public static int minimumNumberOfGameShops()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public GameShop addGameShop(StoreInformation aStoreInformation)
  {
    return new GameShop(this, aStoreInformation);
  }

  public boolean addGameShop(GameShop aGameShop)
  {
    boolean wasAdded = false;
    if (gameShops.contains(aGameShop)) { return false; }
    ManagerAccount existingManagerAccount = aGameShop.getManagerAccount();
    boolean isNewManagerAccount = existingManagerAccount != null && !this.equals(existingManagerAccount);
    if (isNewManagerAccount)
    {
      aGameShop.setManagerAccount(this);
    }
    else
    {
      gameShops.add(aGameShop);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeGameShop(GameShop aGameShop)
  {
    boolean wasRemoved = false;
    //Unable to remove aGameShop, as it must always have a managerAccount
    if (!this.equals(aGameShop.getManagerAccount()))
    {
      gameShops.remove(aGameShop);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addGameShopAt(GameShop aGameShop, int index)
  {  
    boolean wasAdded = false;
    if(addGameShop(aGameShop))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGameShops()) { index = numberOfGameShops() - 1; }
      gameShops.remove(aGameShop);
      gameShops.add(index, aGameShop);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveGameShopAt(GameShop aGameShop, int index)
  {
    boolean wasAdded = false;
    if(gameShops.contains(aGameShop))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGameShops()) { index = numberOfGameShops() - 1; }
      gameShops.remove(aGameShop);
      gameShops.add(index, aGameShop);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addGameShopAt(aGameShop, index);
    }
    return wasAdded;
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
    while (gameShops.size() > 0)
    {
      GameShop aGameShop = gameShops.get(gameShops.size() - 1);
      aGameShop.delete();
      gameShops.remove(aGameShop);
    }
    
    for(int i=reviewReplies.size(); i > 0; i--)
    {
      ReviewReply aReviewReply = reviewReplies.get(i - 1);
      aReviewReply.delete();
    }
    super.delete();
  }

}
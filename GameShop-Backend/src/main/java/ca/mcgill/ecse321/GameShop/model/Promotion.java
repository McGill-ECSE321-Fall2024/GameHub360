/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;

// line 109 "../../../../../../model.ump"
// line 215 "../../../../../../model.ump"
public class Promotion
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum PromotionType { GAME, CATEGORY }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Promotion Attributes
  private int promotionId;
  private PromotionType promotionType;
  private double discountPercentageValue;

  //Promotion Associations
  private StoreInformation info;
  private List<Game> promotedGames;
  private List<GameCategory> promotedCategories;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Promotion(int aPromotionId, double aDiscountPercentageValue, StoreInformation aInfo)
  {
    promotionId = aPromotionId;
    discountPercentageValue = aDiscountPercentageValue;
    boolean didAddInfo = setInfo(aInfo);
    if (!didAddInfo)
    {
      throw new RuntimeException("Unable to create currentPromotion due to info. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    promotedGames = new ArrayList<Game>();
    promotedCategories = new ArrayList<GameCategory>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPromotionId(int aPromotionId)
  {
    boolean wasSet = false;
    promotionId = aPromotionId;
    wasSet = true;
    return wasSet;
  }

  public boolean setPromotionType(PromotionType aPromotionType)
  {
    boolean wasSet = false;
    promotionType = aPromotionType;
    wasSet = true;
    return wasSet;
  }

  public boolean setDiscountPercentageValue(double aDiscountPercentageValue)
  {
    boolean wasSet = false;
    discountPercentageValue = aDiscountPercentageValue;
    wasSet = true;
    return wasSet;
  }

  public int getPromotionId()
  {
    return promotionId;
  }

  public PromotionType getPromotionType()
  {
    return promotionType;
  }

  public double getDiscountPercentageValue()
  {
    return discountPercentageValue;
  }
  /* Code from template association_GetOne */
  public StoreInformation getInfo()
  {
    return info;
  }
  /* Code from template association_GetMany */
  public Game getPromotedGame(int index)
  {
    Game aPromotedGame = promotedGames.get(index);
    return aPromotedGame;
  }

  public List<Game> getPromotedGames()
  {
    List<Game> newPromotedGames = Collections.unmodifiableList(promotedGames);
    return newPromotedGames;
  }

  public int numberOfPromotedGames()
  {
    int number = promotedGames.size();
    return number;
  }

  public boolean hasPromotedGames()
  {
    boolean has = promotedGames.size() > 0;
    return has;
  }

  public int indexOfPromotedGame(Game aPromotedGame)
  {
    int index = promotedGames.indexOf(aPromotedGame);
    return index;
  }
  /* Code from template association_GetMany */
  public GameCategory getPromotedCategory(int index)
  {
    GameCategory aPromotedCategory = promotedCategories.get(index);
    return aPromotedCategory;
  }

  public List<GameCategory> getPromotedCategories()
  {
    List<GameCategory> newPromotedCategories = Collections.unmodifiableList(promotedCategories);
    return newPromotedCategories;
  }

  public int numberOfPromotedCategories()
  {
    int number = promotedCategories.size();
    return number;
  }

  public boolean hasPromotedCategories()
  {
    boolean has = promotedCategories.size() > 0;
    return has;
  }

  public int indexOfPromotedCategory(GameCategory aPromotedCategory)
  {
    int index = promotedCategories.indexOf(aPromotedCategory);
    return index;
  }
  /* Code from template association_SetOneToMany */
  public boolean setInfo(StoreInformation aInfo)
  {
    boolean wasSet = false;
    if (aInfo == null)
    {
      return wasSet;
    }

    StoreInformation existingInfo = info;
    info = aInfo;
    if (existingInfo != null && !existingInfo.equals(aInfo))
    {
      existingInfo.removeCurrentPromotion(this);
    }
    info.addCurrentPromotion(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPromotedGames()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addPromotedGame(Game aPromotedGame)
  {
    boolean wasAdded = false;
    if (promotedGames.contains(aPromotedGame)) { return false; }
    promotedGames.add(aPromotedGame);
    if (aPromotedGame.indexOfPromotion(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aPromotedGame.addPromotion(this);
      if (!wasAdded)
      {
        promotedGames.remove(aPromotedGame);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removePromotedGame(Game aPromotedGame)
  {
    boolean wasRemoved = false;
    if (!promotedGames.contains(aPromotedGame))
    {
      return wasRemoved;
    }

    int oldIndex = promotedGames.indexOf(aPromotedGame);
    promotedGames.remove(oldIndex);
    if (aPromotedGame.indexOfPromotion(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aPromotedGame.removePromotion(this);
      if (!wasRemoved)
      {
        promotedGames.add(oldIndex,aPromotedGame);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPromotedGameAt(Game aPromotedGame, int index)
  {  
    boolean wasAdded = false;
    if(addPromotedGame(aPromotedGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPromotedGames()) { index = numberOfPromotedGames() - 1; }
      promotedGames.remove(aPromotedGame);
      promotedGames.add(index, aPromotedGame);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePromotedGameAt(Game aPromotedGame, int index)
  {
    boolean wasAdded = false;
    if(promotedGames.contains(aPromotedGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPromotedGames()) { index = numberOfPromotedGames() - 1; }
      promotedGames.remove(aPromotedGame);
      promotedGames.add(index, aPromotedGame);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPromotedGameAt(aPromotedGame, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPromotedCategories()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addPromotedCategory(GameCategory aPromotedCategory)
  {
    boolean wasAdded = false;
    if (promotedCategories.contains(aPromotedCategory)) { return false; }
    promotedCategories.add(aPromotedCategory);
    if (aPromotedCategory.indexOfPromotion(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aPromotedCategory.addPromotion(this);
      if (!wasAdded)
      {
        promotedCategories.remove(aPromotedCategory);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removePromotedCategory(GameCategory aPromotedCategory)
  {
    boolean wasRemoved = false;
    if (!promotedCategories.contains(aPromotedCategory))
    {
      return wasRemoved;
    }

    int oldIndex = promotedCategories.indexOf(aPromotedCategory);
    promotedCategories.remove(oldIndex);
    if (aPromotedCategory.indexOfPromotion(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aPromotedCategory.removePromotion(this);
      if (!wasRemoved)
      {
        promotedCategories.add(oldIndex,aPromotedCategory);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPromotedCategoryAt(GameCategory aPromotedCategory, int index)
  {  
    boolean wasAdded = false;
    if(addPromotedCategory(aPromotedCategory))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPromotedCategories()) { index = numberOfPromotedCategories() - 1; }
      promotedCategories.remove(aPromotedCategory);
      promotedCategories.add(index, aPromotedCategory);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePromotedCategoryAt(GameCategory aPromotedCategory, int index)
  {
    boolean wasAdded = false;
    if(promotedCategories.contains(aPromotedCategory))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPromotedCategories()) { index = numberOfPromotedCategories() - 1; }
      promotedCategories.remove(aPromotedCategory);
      promotedCategories.add(index, aPromotedCategory);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPromotedCategoryAt(aPromotedCategory, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    StoreInformation placeholderInfo = info;
    this.info = null;
    if(placeholderInfo != null)
    {
      placeholderInfo.removeCurrentPromotion(this);
    }
    ArrayList<Game> copyOfPromotedGames = new ArrayList<Game>(promotedGames);
    promotedGames.clear();
    for(Game aPromotedGame : copyOfPromotedGames)
    {
      aPromotedGame.removePromotion(this);
    }
    ArrayList<GameCategory> copyOfPromotedCategories = new ArrayList<GameCategory>(promotedCategories);
    promotedCategories.clear();
    for(GameCategory aPromotedCategory : copyOfPromotedCategories)
    {
      aPromotedCategory.removePromotion(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "promotionId" + ":" + getPromotionId()+ "," +
            "discountPercentageValue" + ":" + getDiscountPercentageValue()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "promotionType" + "=" + (getPromotionType() != null ? !getPromotionType().equals(this)  ? getPromotionType().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "info = "+(getInfo()!=null?Integer.toHexString(System.identityHashCode(getInfo())):"null");
  }
}
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;

// line 116 "../../../../../../model.ump"
// line 220 "../../../../../../model.ump"
public class StoreInformation
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //StoreInformation Attributes
  private int storeInfoId;
  private String storePolicy;

  //StoreInformation Associations
  private List<Promotion> currentPromotions;
  private GameShop gameShop;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public StoreInformation(int aStoreInfoId, GameShop aGameShop)
  {
    storeInfoId = aStoreInfoId;
    storePolicy = null;
    currentPromotions = new ArrayList<Promotion>();
    if (aGameShop == null || aGameShop.getStoreInformation() != null)
    {
      throw new RuntimeException("Unable to create StoreInformation due to aGameShop. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    gameShop = aGameShop;
  }

  public StoreInformation(int aStoreInfoId, int aGameShopIdForGameShop, ManagerAccount aManagerAccountForGameShop)
  {
    storeInfoId = aStoreInfoId;
    storePolicy = null;
    currentPromotions = new ArrayList<Promotion>();
    gameShop = new GameShop(aGameShopIdForGameShop, aManagerAccountForGameShop, this);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setStoreInfoId(int aStoreInfoId)
  {
    boolean wasSet = false;
    storeInfoId = aStoreInfoId;
    wasSet = true;
    return wasSet;
  }

  public boolean setStorePolicy(String aStorePolicy)
  {
    boolean wasSet = false;
    storePolicy = aStorePolicy;
    wasSet = true;
    return wasSet;
  }

  public int getStoreInfoId()
  {
    return storeInfoId;
  }

  public String getStorePolicy()
  {
    return storePolicy;
  }
  /* Code from template association_GetMany */
  public Promotion getCurrentPromotion(int index)
  {
    Promotion aCurrentPromotion = currentPromotions.get(index);
    return aCurrentPromotion;
  }

  public List<Promotion> getCurrentPromotions()
  {
    List<Promotion> newCurrentPromotions = Collections.unmodifiableList(currentPromotions);
    return newCurrentPromotions;
  }

  public int numberOfCurrentPromotions()
  {
    int number = currentPromotions.size();
    return number;
  }

  public boolean hasCurrentPromotions()
  {
    boolean has = currentPromotions.size() > 0;
    return has;
  }

  public int indexOfCurrentPromotion(Promotion aCurrentPromotion)
  {
    int index = currentPromotions.indexOf(aCurrentPromotion);
    return index;
  }
  /* Code from template association_GetOne */
  public GameShop getGameShop()
  {
    return gameShop;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCurrentPromotions()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Promotion addCurrentPromotion(int aPromotionId, double aDiscountPercentageValue)
  {
    return new Promotion(aPromotionId, aDiscountPercentageValue, this);
  }

  public boolean addCurrentPromotion(Promotion aCurrentPromotion)
  {
    boolean wasAdded = false;
    if (currentPromotions.contains(aCurrentPromotion)) { return false; }
    StoreInformation existingInfo = aCurrentPromotion.getInfo();
    boolean isNewInfo = existingInfo != null && !this.equals(existingInfo);
    if (isNewInfo)
    {
      aCurrentPromotion.setInfo(this);
    }
    else
    {
      currentPromotions.add(aCurrentPromotion);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCurrentPromotion(Promotion aCurrentPromotion)
  {
    boolean wasRemoved = false;
    //Unable to remove aCurrentPromotion, as it must always have a info
    if (!this.equals(aCurrentPromotion.getInfo()))
    {
      currentPromotions.remove(aCurrentPromotion);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCurrentPromotionAt(Promotion aCurrentPromotion, int index)
  {  
    boolean wasAdded = false;
    if(addCurrentPromotion(aCurrentPromotion))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCurrentPromotions()) { index = numberOfCurrentPromotions() - 1; }
      currentPromotions.remove(aCurrentPromotion);
      currentPromotions.add(index, aCurrentPromotion);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCurrentPromotionAt(Promotion aCurrentPromotion, int index)
  {
    boolean wasAdded = false;
    if(currentPromotions.contains(aCurrentPromotion))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCurrentPromotions()) { index = numberOfCurrentPromotions() - 1; }
      currentPromotions.remove(aCurrentPromotion);
      currentPromotions.add(index, aCurrentPromotion);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCurrentPromotionAt(aCurrentPromotion, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (currentPromotions.size() > 0)
    {
      Promotion aCurrentPromotion = currentPromotions.get(currentPromotions.size() - 1);
      aCurrentPromotion.delete();
      currentPromotions.remove(aCurrentPromotion);
    }
    
    GameShop existingGameShop = gameShop;
    gameShop = null;
    if (existingGameShop != null)
    {
      existingGameShop.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "storeInfoId" + ":" + getStoreInfoId()+ "," +
            "storePolicy" + ":" + getStorePolicy()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "gameShop = "+(getGameShop()!=null?Integer.toHexString(System.identityHashCode(getGameShop())):"null");
  }
}
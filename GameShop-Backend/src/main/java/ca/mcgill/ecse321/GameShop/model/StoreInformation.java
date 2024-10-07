/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;

// line 107 "../../../../../../GameShop.ump"
public class StoreInformation
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //StoreInformation Attributes
  private String storePolicy;

  //StoreInformation Associations
  private List<Promotion> promotion;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public StoreInformation()
  {
    storePolicy = null;
    promotion = new ArrayList<Promotion>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setStorePolicy(String aStorePolicy)
  {
    boolean wasSet = false;
    storePolicy = aStorePolicy;
    wasSet = true;
    return wasSet;
  }

  /**
   * lazy keyword has to be added due to the singleton keyword, if we end up having it
   */
  public String getStorePolicy()
  {
    return storePolicy;
  }
  /* Code from template association_GetMany */
  public Promotion getPromotion(int index)
  {
    Promotion aPromotion = promotion.get(index);
    return aPromotion;
  }

  /**
   * singleton;
   * might need a better role name here
   */
  public List<Promotion> getPromotion()
  {
    List<Promotion> newPromotion = Collections.unmodifiableList(promotion);
    return newPromotion;
  }

  public int numberOfPromotion()
  {
    int number = promotion.size();
    return number;
  }

  public boolean hasPromotion()
  {
    boolean has = promotion.size() > 0;
    return has;
  }

  public int indexOfPromotion(Promotion aPromotion)
  {
    int index = promotion.indexOf(aPromotion);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPromotion()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Promotion addPromotion(double aDiscountPercentageValue)
  {
    return new Promotion(aDiscountPercentageValue, this);
  }

  public boolean addPromotion(Promotion aPromotion)
  {
    boolean wasAdded = false;
    if (promotion.contains(aPromotion)) { return false; }
    StoreInformation existingInfo = aPromotion.getInfo();
    boolean isNewInfo = existingInfo != null && !this.equals(existingInfo);
    if (isNewInfo)
    {
      aPromotion.setInfo(this);
    }
    else
    {
      promotion.add(aPromotion);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePromotion(Promotion aPromotion)
  {
    boolean wasRemoved = false;
    //Unable to remove aPromotion, as it must always have a info
    if (!this.equals(aPromotion.getInfo()))
    {
      promotion.remove(aPromotion);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPromotionAt(Promotion aPromotion, int index)
  {  
    boolean wasAdded = false;
    if(addPromotion(aPromotion))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPromotion()) { index = numberOfPromotion() - 1; }
      promotion.remove(aPromotion);
      promotion.add(index, aPromotion);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePromotionAt(Promotion aPromotion, int index)
  {
    boolean wasAdded = false;
    if(promotion.contains(aPromotion))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPromotion()) { index = numberOfPromotion() - 1; }
      promotion.remove(aPromotion);
      promotion.add(index, aPromotion);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPromotionAt(aPromotion, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (promotion.size() > 0)
    {
      Promotion aPromotion = promotion.get(promotion.size() - 1);
      aPromotion.delete();
      promotion.remove(aPromotion);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "storePolicy" + ":" + getStorePolicy()+ "]";
  }
}
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

// line 100 "../../../../../../model.ump"
// line 199 "../../../../../../model.ump"
@Entity
public class StoreInformation
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //StoreInformation Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int storeInfoId;

  private String storePolicy;

  //StoreInformation Associations
  @OneToMany(mappedBy = "info", cascade = CascadeType.ALL)
  private List<Promotion> currentPromotions;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public StoreInformation(int aStoreInfoId)
  {
    storeInfoId = aStoreInfoId;
    storePolicy = null;
    currentPromotions = new ArrayList<Promotion>();
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
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "storeInfoId" + ":" + getStoreInfoId()+ "," +
            "storePolicy" + ":" + getStorePolicy()+ "]";
  }
}
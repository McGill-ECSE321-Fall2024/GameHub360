/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;

// line 77 "../../../../../../model.ump"
// line 202 "../../../../../../model.ump"
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class GameEntity
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //GameEntity Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int gameEntityId;
  
  private String name;
  private String description;
  private String imageURL;

  //GameEntity Associations
  @ManyToMany(mappedBy = "games")
  private List<GameCategory> categories;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public GameEntity(String aName, String aDescription, String aImageURL, GameCategory... allCategories)
  {
    name = aName;
    description = aDescription;
    imageURL = aImageURL;
    categories = new ArrayList<GameCategory>();
    boolean didAddCategories = setCategories(allCategories);
    if (!didAddCategories)
    {
      throw new RuntimeException("Unable to create GameEntity, must have at least 1 categories. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public GameEntity() {
    categories = new ArrayList<GameCategory>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setImageURL(String aImageURL)
  {
    boolean wasSet = false;
    imageURL = aImageURL;
    wasSet = true;
    return wasSet;
  }

  public int getGameEntityId()
  {
    return gameEntityId;
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    return description;
  }

  public String getImageURL()
  {
    return imageURL;
  }
  /* Code from template association_GetMany */
  public GameCategory getCategory(int index)
  {
    GameCategory aCategory = categories.get(index);
    return aCategory;
  }

  public List<GameCategory> getCategories()
  {
    List<GameCategory> newCategories = Collections.unmodifiableList(categories);
    return newCategories;
  }

  public int numberOfCategories()
  {
    int number = categories.size();
    return number;
  }

  public boolean hasCategories()
  {
    boolean has = categories.size() > 0;
    return has;
  }

  public int indexOfCategory(GameCategory aCategory)
  {
    int index = categories.indexOf(aCategory);
    return index;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfCategoriesValid()
  {
    boolean isValid = numberOfCategories() >= minimumNumberOfCategories();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCategories()
  {
    return 1;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addCategory(GameCategory aCategory)
  {
    boolean wasAdded = false;
    if (categories.contains(aCategory)) { return false; }
    categories.add(aCategory);
    if (aCategory.indexOfGame(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aCategory.addGame(this);
      if (!wasAdded)
      {
        categories.remove(aCategory);
      }
    }
    return wasAdded;
  }
  /* Code from template association_AddMStarToMany */
  public boolean removeCategory(GameCategory aCategory)
  {
    boolean wasRemoved = false;
    if (!categories.contains(aCategory))
    {
      return wasRemoved;
    }

    if (numberOfCategories() <= minimumNumberOfCategories())
    {
      return wasRemoved;
    }

    int oldIndex = categories.indexOf(aCategory);
    categories.remove(oldIndex);
    if (aCategory.indexOfGame(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aCategory.removeGame(this);
      if (!wasRemoved)
      {
        categories.add(oldIndex,aCategory);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_SetMStarToMany */
  public boolean setCategories(GameCategory... newCategories)
  {
    boolean wasSet = false;
    ArrayList<GameCategory> verifiedCategories = new ArrayList<GameCategory>();
    for (GameCategory aCategory : newCategories)
    {
      if (verifiedCategories.contains(aCategory))
      {
        continue;
      }
      verifiedCategories.add(aCategory);
    }

    if (verifiedCategories.size() != newCategories.length || verifiedCategories.size() < minimumNumberOfCategories())
    {
      return wasSet;
    }

    ArrayList<GameCategory> oldCategories = new ArrayList<GameCategory>(categories);
    categories.clear();
    for (GameCategory aNewCategory : verifiedCategories)
    {
      categories.add(aNewCategory);
      if (oldCategories.contains(aNewCategory))
      {
        oldCategories.remove(aNewCategory);
      }
      else
      {
        aNewCategory.addGame(this);
      }
    }

    for (GameCategory anOldCategory : oldCategories)
    {
      anOldCategory.removeGame(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCategoryAt(GameCategory aCategory, int index)
  {  
    boolean wasAdded = false;
    if(addCategory(aCategory))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCategories()) { index = numberOfCategories() - 1; }
      categories.remove(aCategory);
      categories.add(index, aCategory);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCategoryAt(GameCategory aCategory, int index)
  {
    boolean wasAdded = false;
    if(categories.contains(aCategory))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCategories()) { index = numberOfCategories() - 1; }
      categories.remove(aCategory);
      categories.add(index, aCategory);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCategoryAt(aCategory, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    ArrayList<GameCategory> copyOfCategories = new ArrayList<GameCategory>(categories);
    categories.clear();
    for(GameCategory aCategory : copyOfCategories)
    {
      aCategory.removeGame(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "gameEntityId" + ":" + getGameEntityId()+ "," +
            "name" + ":" + getName()+ "," +
            "description" + ":" + getDescription()+ "," +
            "imageURL" + ":" + getImageURL()+ "]";
  }
}
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse.gameshop.model;
import java.util.*;
import java.sql.Date;

// line 33 "../../../../../../GameShop.ump"
public class EmployeeAccount extends StaffAccount
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //EmployeeAccount Attributes
  private String isActive;

  //EmployeeAccount Associations
  private List<ActivityLog> logs;
  private GameShop gameShop;
  private List<GameRequest> request;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public EmployeeAccount(String aEmail, String aPassword, String aIsActive, GameShop aGameShop)
  {
    super(aEmail, aPassword);
    isActive = aIsActive;
    logs = new ArrayList<ActivityLog>();
    boolean didAddGameShop = setGameShop(aGameShop);
    if (!didAddGameShop)
    {
      throw new RuntimeException("Unable to create employeeAccount due to gameShop. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    request = new ArrayList<GameRequest>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setIsActive(String aIsActive)
  {
    boolean wasSet = false;
    isActive = aIsActive;
    wasSet = true;
    return wasSet;
  }

  public String getIsActive()
  {
    return isActive;
  }
  /* Code from template association_GetMany */
  public ActivityLog getLog(int index)
  {
    ActivityLog aLog = logs.get(index);
    return aLog;
  }

  public List<ActivityLog> getLogs()
  {
    List<ActivityLog> newLogs = Collections.unmodifiableList(logs);
    return newLogs;
  }

  public int numberOfLogs()
  {
    int number = logs.size();
    return number;
  }

  public boolean hasLogs()
  {
    boolean has = logs.size() > 0;
    return has;
  }

  public int indexOfLog(ActivityLog aLog)
  {
    int index = logs.indexOf(aLog);
    return index;
  }
  /* Code from template association_GetOne */
  public GameShop getGameShop()
  {
    return gameShop;
  }
  /* Code from template association_GetMany */
  public GameRequest getRequest(int index)
  {
    GameRequest aRequest = request.get(index);
    return aRequest;
  }

  public List<GameRequest> getRequest()
  {
    List<GameRequest> newRequest = Collections.unmodifiableList(request);
    return newRequest;
  }

  public int numberOfRequest()
  {
    int number = request.size();
    return number;
  }

  public boolean hasRequest()
  {
    boolean has = request.size() > 0;
    return has;
  }

  public int indexOfRequest(GameRequest aRequest)
  {
    int index = request.indexOf(aRequest);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLogs()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ActivityLog addLog(String aContent)
  {
    return new ActivityLog(aContent, this);
  }

  public boolean addLog(ActivityLog aLog)
  {
    boolean wasAdded = false;
    if (logs.contains(aLog)) { return false; }
    EmployeeAccount existingEmployee = aLog.getEmployee();
    boolean isNewEmployee = existingEmployee != null && !this.equals(existingEmployee);
    if (isNewEmployee)
    {
      aLog.setEmployee(this);
    }
    else
    {
      logs.add(aLog);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeLog(ActivityLog aLog)
  {
    boolean wasRemoved = false;
    //Unable to remove aLog, as it must always have a employee
    if (!this.equals(aLog.getEmployee()))
    {
      logs.remove(aLog);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addLogAt(ActivityLog aLog, int index)
  {  
    boolean wasAdded = false;
    if(addLog(aLog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLogs()) { index = numberOfLogs() - 1; }
      logs.remove(aLog);
      logs.add(index, aLog);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveLogAt(ActivityLog aLog, int index)
  {
    boolean wasAdded = false;
    if(logs.contains(aLog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLogs()) { index = numberOfLogs() - 1; }
      logs.remove(aLog);
      logs.add(index, aLog);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addLogAt(aLog, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setGameShop(GameShop aGameShop)
  {
    boolean wasSet = false;
    if (aGameShop == null)
    {
      return wasSet;
    }

    GameShop existingGameShop = gameShop;
    gameShop = aGameShop;
    if (existingGameShop != null && !existingGameShop.equals(aGameShop))
    {
      existingGameShop.removeEmployeeAccount(this);
    }
    gameShop.addEmployeeAccount(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRequest()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public GameRequest addRequest(String aName, String aDescription, String aImageURL, String aNameOfX, Date aDate, GameShop aGameShop, GameCategory... allCategories)
  {
    return new GameRequest(aName, aDescription, aImageURL, aNameOfX, aDate, aGameShop, this, allCategories);
  }

  public boolean addRequest(GameRequest aRequest)
  {
    boolean wasAdded = false;
    if (request.contains(aRequest)) { return false; }
    EmployeeAccount existingRequestPlacer = aRequest.getRequestPlacer();
    boolean isNewRequestPlacer = existingRequestPlacer != null && !this.equals(existingRequestPlacer);
    if (isNewRequestPlacer)
    {
      aRequest.setRequestPlacer(this);
    }
    else
    {
      request.add(aRequest);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRequest(GameRequest aRequest)
  {
    boolean wasRemoved = false;
    //Unable to remove aRequest, as it must always have a requestPlacer
    if (!this.equals(aRequest.getRequestPlacer()))
    {
      request.remove(aRequest);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addRequestAt(GameRequest aRequest, int index)
  {  
    boolean wasAdded = false;
    if(addRequest(aRequest))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRequest()) { index = numberOfRequest() - 1; }
      request.remove(aRequest);
      request.add(index, aRequest);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRequestAt(GameRequest aRequest, int index)
  {
    boolean wasAdded = false;
    if(request.contains(aRequest))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRequest()) { index = numberOfRequest() - 1; }
      request.remove(aRequest);
      request.add(index, aRequest);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addRequestAt(aRequest, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (logs.size() > 0)
    {
      ActivityLog aLog = logs.get(logs.size() - 1);
      aLog.delete();
      logs.remove(aLog);
    }
    
    GameShop placeholderGameShop = gameShop;
    this.gameShop = null;
    if(placeholderGameShop != null)
    {
      placeholderGameShop.removeEmployeeAccount(this);
    }
    for(int i=request.size(); i > 0; i--)
    {
      GameRequest aRequest = request.get(i - 1);
      aRequest.delete();
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "isActive" + ":" + getIsActive()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "gameShop = "+(getGameShop()!=null?Integer.toHexString(System.identityHashCode(getGameShop())):"null");
  }
}
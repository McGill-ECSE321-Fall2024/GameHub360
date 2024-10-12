/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;
import java.sql.Date;

// line 24 "../../../../../../model.ump"
// line 162 "../../../../../../model.ump"
public class EmployeeAccount extends StaffAccount
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //EmployeeAccount Attributes
  private boolean isActive;

  //EmployeeAccount Associations
  private List<ActivityLog> logs;
  private List<GameRequest> requests;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public EmployeeAccount(String aEmail, String aPassword, int aStaffId, boolean aIsActive)
  {
    super(aEmail, aPassword, aStaffId);
    isActive = aIsActive;
    logs = new ArrayList<ActivityLog>();
    requests = new ArrayList<GameRequest>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setIsActive(boolean aIsActive)
  {
    boolean wasSet = false;
    isActive = aIsActive;
    wasSet = true;
    return wasSet;
  }

  public boolean getIsActive()
  {
    return isActive;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsActive()
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
  /* Code from template association_GetMany */
  public GameRequest getRequest(int index)
  {
    GameRequest aRequest = requests.get(index);
    return aRequest;
  }

  public List<GameRequest> getRequests()
  {
    List<GameRequest> newRequests = Collections.unmodifiableList(requests);
    return newRequests;
  }

  public int numberOfRequests()
  {
    int number = requests.size();
    return number;
  }

  public boolean hasRequests()
  {
    boolean has = requests.size() > 0;
    return has;
  }

  public int indexOfRequest(GameRequest aRequest)
  {
    int index = requests.indexOf(aRequest);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLogs()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ActivityLog addLog(int aLogId, String aContent)
  {
    return new ActivityLog(aLogId, aContent, this);
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRequests()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public GameRequest addRequest(int aGameEntityId, String aName, String aDescription, String aImageURL, Date aRequestDate, GameCategory... allCategories)
  {
    return new GameRequest(aGameEntityId, aName, aDescription, aImageURL, aRequestDate, this, allCategories);
  }

  public boolean addRequest(GameRequest aRequest)
  {
    boolean wasAdded = false;
    if (requests.contains(aRequest)) { return false; }
    EmployeeAccount existingRequestPlacer = aRequest.getRequestPlacer();
    boolean isNewRequestPlacer = existingRequestPlacer != null && !this.equals(existingRequestPlacer);
    if (isNewRequestPlacer)
    {
      aRequest.setRequestPlacer(this);
    }
    else
    {
      requests.add(aRequest);
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
      requests.remove(aRequest);
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
      if(index > numberOfRequests()) { index = numberOfRequests() - 1; }
      requests.remove(aRequest);
      requests.add(index, aRequest);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRequestAt(GameRequest aRequest, int index)
  {
    boolean wasAdded = false;
    if(requests.contains(aRequest))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRequests()) { index = numberOfRequests() - 1; }
      requests.remove(aRequest);
      requests.add(index, aRequest);
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
    
    for(int i=requests.size(); i > 0; i--)
    {
      GameRequest aRequest = requests.get(i - 1);
      aRequest.delete();
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "isActive" + ":" + getIsActive()+ "]";
  }
}
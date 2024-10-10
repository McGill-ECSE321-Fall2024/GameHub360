/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.sql.Date;
import java.util.*;

// line 84 "../../../../../../model.ump"
// line 204 "../../../../../../model.ump"
public class GameRequest extends GameEntity
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum RequestStatus { SUBMITTED, APPROVED, REFUSED }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //GameRequest Attributes
  private int requestId;
  private RequestStatus requestStatus;
  private Date requestDate;

  //GameRequest Associations
  private List<RequestNote> associatedNotes;
  private GameShop gameShop;
  private EmployeeAccount requestPlacer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public GameRequest(String aName, String aDescription, String aImageURL, int aRequestId, Date aRequestDate, GameShop aGameShop, EmployeeAccount aRequestPlacer, GameCategory... allCategories)
  {
    super(aName, aDescription, aImageURL, allCategories);
    requestId = aRequestId;
    requestDate = aRequestDate;
    associatedNotes = new ArrayList<RequestNote>();
    boolean didAddGameShop = setGameShop(aGameShop);
    if (!didAddGameShop)
    {
      throw new RuntimeException("Unable to create gameRequest due to gameShop. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddRequestPlacer = setRequestPlacer(aRequestPlacer);
    if (!didAddRequestPlacer)
    {
      throw new RuntimeException("Unable to create request due to requestPlacer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRequestId(int aRequestId)
  {
    boolean wasSet = false;
    requestId = aRequestId;
    wasSet = true;
    return wasSet;
  }

  public boolean setRequestStatus(RequestStatus aRequestStatus)
  {
    boolean wasSet = false;
    requestStatus = aRequestStatus;
    wasSet = true;
    return wasSet;
  }

  public boolean setRequestDate(Date aRequestDate)
  {
    boolean wasSet = false;
    requestDate = aRequestDate;
    wasSet = true;
    return wasSet;
  }

  public int getRequestId()
  {
    return requestId;
  }

  public RequestStatus getRequestStatus()
  {
    return requestStatus;
  }

  public Date getRequestDate()
  {
    return requestDate;
  }
  /* Code from template association_GetMany */
  public RequestNote getAssociatedNote(int index)
  {
    RequestNote aAssociatedNote = associatedNotes.get(index);
    return aAssociatedNote;
  }

  public List<RequestNote> getAssociatedNotes()
  {
    List<RequestNote> newAssociatedNotes = Collections.unmodifiableList(associatedNotes);
    return newAssociatedNotes;
  }

  public int numberOfAssociatedNotes()
  {
    int number = associatedNotes.size();
    return number;
  }

  public boolean hasAssociatedNotes()
  {
    boolean has = associatedNotes.size() > 0;
    return has;
  }

  public int indexOfAssociatedNote(RequestNote aAssociatedNote)
  {
    int index = associatedNotes.indexOf(aAssociatedNote);
    return index;
  }
  /* Code from template association_GetOne */
  public GameShop getGameShop()
  {
    return gameShop;
  }
  /* Code from template association_GetOne */
  public EmployeeAccount getRequestPlacer()
  {
    return requestPlacer;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAssociatedNotes()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public RequestNote addAssociatedNote(int aNoteId, String aContent, Date aNoteDate)
  {
    return new RequestNote(aNoteId, aContent, aNoteDate, this);
  }

  public boolean addAssociatedNote(RequestNote aAssociatedNote)
  {
    boolean wasAdded = false;
    if (associatedNotes.contains(aAssociatedNote)) { return false; }
    GameRequest existingGameRequest = aAssociatedNote.getGameRequest();
    boolean isNewGameRequest = existingGameRequest != null && !this.equals(existingGameRequest);
    if (isNewGameRequest)
    {
      aAssociatedNote.setGameRequest(this);
    }
    else
    {
      associatedNotes.add(aAssociatedNote);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAssociatedNote(RequestNote aAssociatedNote)
  {
    boolean wasRemoved = false;
    //Unable to remove aAssociatedNote, as it must always have a gameRequest
    if (!this.equals(aAssociatedNote.getGameRequest()))
    {
      associatedNotes.remove(aAssociatedNote);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addAssociatedNoteAt(RequestNote aAssociatedNote, int index)
  {  
    boolean wasAdded = false;
    if(addAssociatedNote(aAssociatedNote))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAssociatedNotes()) { index = numberOfAssociatedNotes() - 1; }
      associatedNotes.remove(aAssociatedNote);
      associatedNotes.add(index, aAssociatedNote);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveAssociatedNoteAt(RequestNote aAssociatedNote, int index)
  {
    boolean wasAdded = false;
    if(associatedNotes.contains(aAssociatedNote))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAssociatedNotes()) { index = numberOfAssociatedNotes() - 1; }
      associatedNotes.remove(aAssociatedNote);
      associatedNotes.add(index, aAssociatedNote);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addAssociatedNoteAt(aAssociatedNote, index);
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
      existingGameShop.removeGameRequest(this);
    }
    gameShop.addGameRequest(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setRequestPlacer(EmployeeAccount aRequestPlacer)
  {
    boolean wasSet = false;
    if (aRequestPlacer == null)
    {
      return wasSet;
    }

    EmployeeAccount existingRequestPlacer = requestPlacer;
    requestPlacer = aRequestPlacer;
    if (existingRequestPlacer != null && !existingRequestPlacer.equals(aRequestPlacer))
    {
      existingRequestPlacer.removeRequest(this);
    }
    requestPlacer.addRequest(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    while (associatedNotes.size() > 0)
    {
      RequestNote aAssociatedNote = associatedNotes.get(associatedNotes.size() - 1);
      aAssociatedNote.delete();
      associatedNotes.remove(aAssociatedNote);
    }
    
    GameShop placeholderGameShop = gameShop;
    this.gameShop = null;
    if(placeholderGameShop != null)
    {
      placeholderGameShop.removeGameRequest(this);
    }
    EmployeeAccount placeholderRequestPlacer = requestPlacer;
    this.requestPlacer = null;
    if(placeholderRequestPlacer != null)
    {
      placeholderRequestPlacer.removeRequest(this);
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "requestId" + ":" + getRequestId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "requestStatus" + "=" + (getRequestStatus() != null ? !getRequestStatus().equals(this)  ? getRequestStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "requestDate" + "=" + (getRequestDate() != null ? !getRequestDate().equals(this)  ? getRequestDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "gameShop = "+(getGameShop()!=null?Integer.toHexString(System.identityHashCode(getGameShop())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "requestPlacer = "+(getRequestPlacer()!=null?Integer.toHexString(System.identityHashCode(getRequestPlacer())):"null");
  }
}
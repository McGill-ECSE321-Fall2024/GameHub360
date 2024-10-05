/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse.gameshop.model;
import java.sql.Date;
import java.util.*;

// line 74 "../../../../../../GameShop.ump"
public class GameRequest extends GameInformation
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum RequestStatus { SUBMITTED, APPROVED, REFUSED }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //GameRequest Attributes
  private RequestStatus requestStatus;
  private Date date;

  //GameRequest Associations
  private List<RequestNote> requestNotes;
  private GameShop gameShop;
  private EmployeeAccount requestPlacer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public GameRequest(String aName, String aDescription, String aImageURL, String aNameOfX, Date aDate, GameShop aGameShop, EmployeeAccount aRequestPlacer, GameCategory... allCategories)
  {
    super(aName, aDescription, aImageURL, aNameOfX, allCategories);
    date = aDate;
    requestNotes = new ArrayList<RequestNote>();
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

  public boolean setRequestStatus(RequestStatus aRequestStatus)
  {
    boolean wasSet = false;
    requestStatus = aRequestStatus;
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

  public RequestStatus getRequestStatus()
  {
    return requestStatus;
  }

  public Date getDate()
  {
    return date;
  }
  /* Code from template association_GetMany */
  public RequestNote getRequestNote(int index)
  {
    RequestNote aRequestNote = requestNotes.get(index);
    return aRequestNote;
  }

  /**
   * isA does not make sense here too...
   */
  public List<RequestNote> getRequestNotes()
  {
    List<RequestNote> newRequestNotes = Collections.unmodifiableList(requestNotes);
    return newRequestNotes;
  }

  public int numberOfRequestNotes()
  {
    int number = requestNotes.size();
    return number;
  }

  public boolean hasRequestNotes()
  {
    boolean has = requestNotes.size() > 0;
    return has;
  }

  public int indexOfRequestNote(RequestNote aRequestNote)
  {
    int index = requestNotes.indexOf(aRequestNote);
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
  public static int minimumNumberOfRequestNotes()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public RequestNote addRequestNote(String aContent, Date aDate, StaffAccount aNotesWriter)
  {
    return new RequestNote(aContent, aDate, this, aNotesWriter);
  }

  public boolean addRequestNote(RequestNote aRequestNote)
  {
    boolean wasAdded = false;
    if (requestNotes.contains(aRequestNote)) { return false; }
    GameRequest existingGameRequest = aRequestNote.getGameRequest();
    boolean isNewGameRequest = existingGameRequest != null && !this.equals(existingGameRequest);
    if (isNewGameRequest)
    {
      aRequestNote.setGameRequest(this);
    }
    else
    {
      requestNotes.add(aRequestNote);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRequestNote(RequestNote aRequestNote)
  {
    boolean wasRemoved = false;
    //Unable to remove aRequestNote, as it must always have a gameRequest
    if (!this.equals(aRequestNote.getGameRequest()))
    {
      requestNotes.remove(aRequestNote);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addRequestNoteAt(RequestNote aRequestNote, int index)
  {  
    boolean wasAdded = false;
    if(addRequestNote(aRequestNote))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRequestNotes()) { index = numberOfRequestNotes() - 1; }
      requestNotes.remove(aRequestNote);
      requestNotes.add(index, aRequestNote);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRequestNoteAt(RequestNote aRequestNote, int index)
  {
    boolean wasAdded = false;
    if(requestNotes.contains(aRequestNote))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRequestNotes()) { index = numberOfRequestNotes() - 1; }
      requestNotes.remove(aRequestNote);
      requestNotes.add(index, aRequestNote);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addRequestNoteAt(aRequestNote, index);
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
    while (requestNotes.size() > 0)
    {
      RequestNote aRequestNote = requestNotes.get(requestNotes.size() - 1);
      aRequestNote.delete();
      requestNotes.remove(aRequestNote);
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
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "requestStatus" + "=" + (getRequestStatus() != null ? !getRequestStatus().equals(this)  ? getRequestStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "gameShop = "+(getGameShop()!=null?Integer.toHexString(System.identityHashCode(getGameShop())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "requestPlacer = "+(getRequestPlacer()!=null?Integer.toHexString(System.identityHashCode(getRequestPlacer())):"null");
  }
}
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.sql.Date;

// line 51 "../../../../../../GameShop.ump"
public class RequestNote
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //RequestNote Attributes
  private String content;
  private Date date;

  //RequestNote Associations
  private GameRequest gameRequest;
  private StaffAccount notesWriter;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public RequestNote(String aContent, Date aDate, GameRequest aGameRequest, StaffAccount aNotesWriter)
  {
    content = aContent;
    date = aDate;
    boolean didAddGameRequest = setGameRequest(aGameRequest);
    if (!didAddGameRequest)
    {
      throw new RuntimeException("Unable to create requestNote due to gameRequest. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddNotesWriter = setNotesWriter(aNotesWriter);
    if (!didAddNotesWriter)
    {
      throw new RuntimeException("Unable to create writtenNote due to notesWriter. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setContent(String aContent)
  {
    boolean wasSet = false;
    content = aContent;
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

  public String getContent()
  {
    return content;
  }

  public Date getDate()
  {
    return date;
  }
  /* Code from template association_GetOne */
  public GameRequest getGameRequest()
  {
    return gameRequest;
  }
  /* Code from template association_GetOne */
  public StaffAccount getNotesWriter()
  {
    return notesWriter;
  }
  /* Code from template association_SetOneToMany */
  public boolean setGameRequest(GameRequest aGameRequest)
  {
    boolean wasSet = false;
    if (aGameRequest == null)
    {
      return wasSet;
    }

    GameRequest existingGameRequest = gameRequest;
    gameRequest = aGameRequest;
    if (existingGameRequest != null && !existingGameRequest.equals(aGameRequest))
    {
      existingGameRequest.removeRequestNote(this);
    }
    gameRequest.addRequestNote(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMandatoryMany */
  public boolean setNotesWriter(StaffAccount aNotesWriter)
  {
    boolean wasSet = false;
    //Must provide notesWriter to writtenNote
    if (aNotesWriter == null)
    {
      return wasSet;
    }

    if (notesWriter != null && notesWriter.numberOfWrittenNotes() <= StaffAccount.minimumNumberOfWrittenNotes())
    {
      return wasSet;
    }

    StaffAccount existingNotesWriter = notesWriter;
    notesWriter = aNotesWriter;
    if (existingNotesWriter != null && !existingNotesWriter.equals(aNotesWriter))
    {
      boolean didRemove = existingNotesWriter.removeWrittenNote(this);
      if (!didRemove)
      {
        notesWriter = existingNotesWriter;
        return wasSet;
      }
    }
    notesWriter.addWrittenNote(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    GameRequest placeholderGameRequest = gameRequest;
    this.gameRequest = null;
    if(placeholderGameRequest != null)
    {
      placeholderGameRequest.removeRequestNote(this);
    }
    StaffAccount placeholderNotesWriter = notesWriter;
    this.notesWriter = null;
    if(placeholderNotesWriter != null)
    {
      placeholderNotesWriter.removeWrittenNote(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "content" + ":" + getContent()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "gameRequest = "+(getGameRequest()!=null?Integer.toHexString(System.identityHashCode(getGameRequest())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "notesWriter = "+(getNotesWriter()!=null?Integer.toHexString(System.identityHashCode(getNotesWriter())):"null");
  }
}
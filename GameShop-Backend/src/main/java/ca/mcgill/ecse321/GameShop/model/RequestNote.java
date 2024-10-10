/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.sql.Date;

// line 54 "../../../../../../model.ump"
// line 189 "../../../../../../model.ump"
public class RequestNote
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //RequestNote Attributes
  private int noteId;
  private String content;
  private Date noteDate;

  //RequestNote Associations
  private GameRequest gameRequest;
  private StaffAccount notesWriter;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public RequestNote(int aNoteId, String aContent, Date aNoteDate, GameRequest aGameRequest)
  {
    noteId = aNoteId;
    content = aContent;
    noteDate = aNoteDate;
    boolean didAddGameRequest = setGameRequest(aGameRequest);
    if (!didAddGameRequest)
    {
      throw new RuntimeException("Unable to create associatedNote due to gameRequest. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNoteId(int aNoteId)
  {
    boolean wasSet = false;
    noteId = aNoteId;
    wasSet = true;
    return wasSet;
  }

  public boolean setContent(String aContent)
  {
    boolean wasSet = false;
    content = aContent;
    wasSet = true;
    return wasSet;
  }

  public boolean setNoteDate(Date aNoteDate)
  {
    boolean wasSet = false;
    noteDate = aNoteDate;
    wasSet = true;
    return wasSet;
  }

  public int getNoteId()
  {
    return noteId;
  }

  public String getContent()
  {
    return content;
  }

  public Date getNoteDate()
  {
    return noteDate;
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

  public boolean hasNotesWriter()
  {
    boolean has = notesWriter != null;
    return has;
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
      existingGameRequest.removeAssociatedNote(this);
    }
    gameRequest.addAssociatedNote(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMandatoryMany */
  public boolean setNotesWriter(StaffAccount aNotesWriter)
  {
    //
    // This source of this source generation is association_SetOptionalOneToMandatoryMany.jet
    // This set file assumes the generation of a maximumNumberOfXXX method does not exist because 
    // it's not required (No upper bound)
    //   
    boolean wasSet = false;
    StaffAccount existingNotesWriter = notesWriter;

    if (existingNotesWriter == null)
    {
      if (aNotesWriter != null)
      {
        if (aNotesWriter.addWrittenNote(this))
        {
          existingNotesWriter = aNotesWriter;
          wasSet = true;
        }
      }
    } 
    else if (existingNotesWriter != null)
    {
      if (aNotesWriter == null)
      {
        if (existingNotesWriter.minimumNumberOfWrittenNotes() < existingNotesWriter.numberOfWrittenNotes())
        {
          existingNotesWriter.removeWrittenNote(this);
          existingNotesWriter = aNotesWriter;  // aNotesWriter == null
          wasSet = true;
        }
      } 
      else
      {
        if (existingNotesWriter.minimumNumberOfWrittenNotes() < existingNotesWriter.numberOfWrittenNotes())
        {
          existingNotesWriter.removeWrittenNote(this);
          aNotesWriter.addWrittenNote(this);
          existingNotesWriter = aNotesWriter;
          wasSet = true;
        }
      }
    }
    if (wasSet)
    {
      notesWriter = existingNotesWriter;
    }
    return wasSet;
  }
  
  public void delete()
  {
    GameRequest placeholderGameRequest = gameRequest;
    this.gameRequest = null;
    if(placeholderGameRequest != null)
    {
      placeholderGameRequest.removeAssociatedNote(this);
    }
    if (notesWriter != null)
    {
      if (notesWriter.numberOfWrittenNotes() <= 1)
      {
        notesWriter.delete();
      }
      else
      {
        StaffAccount placeholderNotesWriter = notesWriter;
        this.notesWriter = null;
        placeholderNotesWriter.removeWrittenNote(this);
      }
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "noteId" + ":" + getNoteId()+ "," +
            "content" + ":" + getContent()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "noteDate" + "=" + (getNoteDate() != null ? !getNoteDate().equals(this)  ? getNoteDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "gameRequest = "+(getGameRequest()!=null?Integer.toHexString(System.identityHashCode(getGameRequest())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "notesWriter = "+(getNotesWriter()!=null?Integer.toHexString(System.identityHashCode(getNotesWriter())):"null");
  }
}
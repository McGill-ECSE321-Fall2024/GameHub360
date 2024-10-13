/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// line 39 "../../../../../../model.ump"
// line 177 "../../../../../../model.ump"
@Entity
public class RequestNote
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //RequestNote Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int noteId;

  private String content;
  private Date noteDate;

  //RequestNote Associations
  @ManyToOne
  @JoinColumn(name = "game_entity_id")
  private GameRequest gameRequest;

  @ManyToOne
  @JoinColumn(name = "staff_id")
  private StaffAccount notesWriter;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public RequestNote(String aContent, Date aNoteDate, GameRequest aGameRequest, StaffAccount aNotesWriter)
  {
    content = aContent;
    noteDate = aNoteDate;
    boolean didAddGameRequest = setGameRequest(aGameRequest);
    if (!didAddGameRequest)
    {
      throw new RuntimeException("Unable to create associatedNote due to gameRequest. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddNotesWriter = setNotesWriter(aNotesWriter);
    if (!didAddNotesWriter)
    {
      throw new RuntimeException("Unable to create writtenNote due to notesWriter. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public RequestNote() {
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
  /* Code from template association_SetOneToMany */
  public boolean setNotesWriter(StaffAccount aNotesWriter)
  {
    boolean wasSet = false;
    if (aNotesWriter == null)
    {
      return wasSet;
    }

    StaffAccount existingNotesWriter = notesWriter;
    notesWriter = aNotesWriter;
    if (existingNotesWriter != null && !existingNotesWriter.equals(aNotesWriter))
    {
      existingNotesWriter.removeWrittenNote(this);
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
      placeholderGameRequest.removeAssociatedNote(this);
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
            "noteId" + ":" + getNoteId()+ "," +
            "content" + ":" + getContent()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "noteDate" + "=" + (getNoteDate() != null ? !getNoteDate().equals(this)  ? getNoteDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "gameRequest = "+(getGameRequest()!=null?Integer.toHexString(System.identityHashCode(getGameRequest())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "notesWriter = "+(getNotesWriter()!=null?Integer.toHexString(System.identityHashCode(getNotesWriter())):"null");
  }
}
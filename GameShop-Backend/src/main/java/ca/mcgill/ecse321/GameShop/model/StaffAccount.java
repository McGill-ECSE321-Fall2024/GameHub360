/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;
import java.sql.Date;

// line 18 "../../../../../../model.ump"
// line 157 "../../../../../../model.ump"
public abstract class StaffAccount extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //StaffAccount Attributes
  private int staffId;

  //StaffAccount Associations
  private List<RequestNote> writtenNotes;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public StaffAccount(String aEmail, String aPassword, int aStaffId)
  {
    super(aEmail, aPassword);
    staffId = aStaffId;
    writtenNotes = new ArrayList<RequestNote>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setStaffId(int aStaffId)
  {
    boolean wasSet = false;
    staffId = aStaffId;
    wasSet = true;
    return wasSet;
  }

  public int getStaffId()
  {
    return staffId;
  }
  /* Code from template association_GetMany */
  public RequestNote getWrittenNote(int index)
  {
    RequestNote aWrittenNote = writtenNotes.get(index);
    return aWrittenNote;
  }

  public List<RequestNote> getWrittenNotes()
  {
    List<RequestNote> newWrittenNotes = Collections.unmodifiableList(writtenNotes);
    return newWrittenNotes;
  }

  public int numberOfWrittenNotes()
  {
    int number = writtenNotes.size();
    return number;
  }

  public boolean hasWrittenNotes()
  {
    boolean has = writtenNotes.size() > 0;
    return has;
  }

  public int indexOfWrittenNote(RequestNote aWrittenNote)
  {
    int index = writtenNotes.indexOf(aWrittenNote);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfWrittenNotes()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public RequestNote addWrittenNote(int aNoteId, String aContent, Date aNoteDate, GameRequest aGameRequest)
  {
    return new RequestNote(aNoteId, aContent, aNoteDate, aGameRequest, this);
  }

  public boolean addWrittenNote(RequestNote aWrittenNote)
  {
    boolean wasAdded = false;
    if (writtenNotes.contains(aWrittenNote)) { return false; }
    StaffAccount existingNotesWriter = aWrittenNote.getNotesWriter();
    boolean isNewNotesWriter = existingNotesWriter != null && !this.equals(existingNotesWriter);
    if (isNewNotesWriter)
    {
      aWrittenNote.setNotesWriter(this);
    }
    else
    {
      writtenNotes.add(aWrittenNote);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeWrittenNote(RequestNote aWrittenNote)
  {
    boolean wasRemoved = false;
    //Unable to remove aWrittenNote, as it must always have a notesWriter
    if (!this.equals(aWrittenNote.getNotesWriter()))
    {
      writtenNotes.remove(aWrittenNote);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addWrittenNoteAt(RequestNote aWrittenNote, int index)
  {  
    boolean wasAdded = false;
    if(addWrittenNote(aWrittenNote))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWrittenNotes()) { index = numberOfWrittenNotes() - 1; }
      writtenNotes.remove(aWrittenNote);
      writtenNotes.add(index, aWrittenNote);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveWrittenNoteAt(RequestNote aWrittenNote, int index)
  {
    boolean wasAdded = false;
    if(writtenNotes.contains(aWrittenNote))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWrittenNotes()) { index = numberOfWrittenNotes() - 1; }
      writtenNotes.remove(aWrittenNote);
      writtenNotes.add(index, aWrittenNote);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addWrittenNoteAt(aWrittenNote, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    for(int i=writtenNotes.size(); i > 0; i--)
    {
      RequestNote aWrittenNote = writtenNotes.get(i - 1);
      aWrittenNote.delete();
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "staffId" + ":" + getStaffId()+ "]";
  }
}
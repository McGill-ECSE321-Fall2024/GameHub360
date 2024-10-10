/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;

// line 32 "../../../../../../model.ump"
// line 169 "../../../../../../model.ump"
public abstract class StaffAccount extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //StaffAccount Associations
  private List<RequestNote> writtenNotes;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public StaffAccount(String aEmail, String aPassword, RequestNote... allWrittenNotes)
  {
    super(aEmail, aPassword);
    writtenNotes = new ArrayList<RequestNote>();
    boolean didAddWrittenNotes = setWrittenNotes(allWrittenNotes);
    if (!didAddWrittenNotes)
    {
      throw new RuntimeException("Unable to create StaffAccount, must have at least 1 writtenNotes. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
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
    return 1;
  }
  /* Code from template association_AddMNToOptionalOne */
  public boolean addWrittenNote(RequestNote aWrittenNote)
  {
    boolean wasAdded = false;
    if (writtenNotes.contains(aWrittenNote)) { return false; }
    StaffAccount existingNotesWriter = aWrittenNote.getNotesWriter();
    if (existingNotesWriter != null && existingNotesWriter.numberOfWrittenNotes() <= minimumNumberOfWrittenNotes())
    {
      return wasAdded;
    }
    else if (existingNotesWriter != null)
    {
      existingNotesWriter.writtenNotes.remove(aWrittenNote);
    }
    writtenNotes.add(aWrittenNote);
    setNotesWriter(aWrittenNote,this);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeWrittenNote(RequestNote aWrittenNote)
  {
    boolean wasRemoved = false;
    if (writtenNotes.contains(aWrittenNote) && numberOfWrittenNotes() > minimumNumberOfWrittenNotes())
    {
      writtenNotes.remove(aWrittenNote);
      setNotesWriter(aWrittenNote,null);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_SetMNToOptionalOne */
  public boolean setWrittenNotes(RequestNote... newWrittenNotes)
  {
    boolean wasSet = false;
    if (newWrittenNotes.length < minimumNumberOfWrittenNotes())
    {
      return wasSet;
    }

    ArrayList<RequestNote> checkNewWrittenNotes = new ArrayList<RequestNote>();
    HashMap<StaffAccount,Integer> notesWriterToNewWrittenNotes = new HashMap<StaffAccount,Integer>();
    for (RequestNote aWrittenNote : newWrittenNotes)
    {
      if (checkNewWrittenNotes.contains(aWrittenNote))
      {
        return wasSet;
      }
      else if (aWrittenNote.getNotesWriter() != null && !this.equals(aWrittenNote.getNotesWriter()))
      {
        StaffAccount existingNotesWriter = aWrittenNote.getNotesWriter();
        if (!notesWriterToNewWrittenNotes.containsKey(existingNotesWriter))
        {
          notesWriterToNewWrittenNotes.put(existingNotesWriter, Integer.valueOf(existingNotesWriter.numberOfWrittenNotes()));
        }
        Integer currentCount = notesWriterToNewWrittenNotes.get(existingNotesWriter);
        int nextCount = currentCount - 1;
        if (nextCount < 1)
        {
          return wasSet;
        }
        notesWriterToNewWrittenNotes.put(existingNotesWriter, Integer.valueOf(nextCount));
      }
      checkNewWrittenNotes.add(aWrittenNote);
    }

    writtenNotes.removeAll(checkNewWrittenNotes);

    for (RequestNote orphan : writtenNotes)
    {
      setNotesWriter(orphan, null);
    }
    writtenNotes.clear();
    for (RequestNote aWrittenNote : newWrittenNotes)
    {
      if (aWrittenNote.getNotesWriter() != null)
      {
        aWrittenNote.getNotesWriter().writtenNotes.remove(aWrittenNote);
      }
      setNotesWriter(aWrittenNote, this);
      writtenNotes.add(aWrittenNote);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_GetPrivate */
  private void setNotesWriter(RequestNote aWrittenNote, StaffAccount aNotesWriter)
  {
    try
    {
      java.lang.reflect.Field mentorField = aWrittenNote.getClass().getDeclaredField("notesWriter");
      mentorField.setAccessible(true);
      mentorField.set(aWrittenNote, aNotesWriter);
    }
    catch (Exception e)
    {
      throw new RuntimeException("Issue internally setting aNotesWriter to aWrittenNote", e);
    }
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
    for(RequestNote aWrittenNote : writtenNotes)
    {
      setNotesWriter(aWrittenNote,null);
    }
    writtenNotes.clear();
    super.delete();
  }

}
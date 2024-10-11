/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// line 34 "../../../../../../model.ump"
// line 168 "../../../../../../model.ump"
@Entity
public class ActivityLog
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ActivityLog Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int logId;
  private String content;

  //ActivityLog Associations
  @ManyToOne
  @JoinColumn(name = "employee_id")
  private EmployeeAccount employee;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ActivityLog(String aContent, EmployeeAccount aEmployee)
  {
    content = aContent;
    boolean didAddEmployee = setEmployee(aEmployee);
    if (!didAddEmployee)
    {
      throw new RuntimeException("Unable to create log due to employee. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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

  public int getLogId()
  {
    return logId;
  }

  public String getContent()
  {
    return content;
  }
  /* Code from template association_GetOne */
  public EmployeeAccount getEmployee()
  {
    return employee;
  }
  /* Code from template association_SetOneToMany */
  public boolean setEmployee(EmployeeAccount aEmployee)
  {
    boolean wasSet = false;
    if (aEmployee == null)
    {
      return wasSet;
    }

    EmployeeAccount existingEmployee = employee;
    employee = aEmployee;
    if (existingEmployee != null && !existingEmployee.equals(aEmployee))
    {
      existingEmployee.removeLog(this);
    }
    employee.addLog(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    EmployeeAccount placeholderEmployee = employee;
    this.employee = null;
    if(placeholderEmployee != null)
    {
      placeholderEmployee.removeLog(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "logId" + ":" + getLogId()+ "," +
            "content" + ":" + getContent()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "employee = "+(getEmployee()!=null?Integer.toHexString(System.identityHashCode(getEmployee())):"null");
  }
}
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse.gameshop.model;

// line 44 "../../../../../../GameShop.ump"
public class ActivityLog
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ActivityLog Attributes
  private String content;

  //ActivityLog Associations
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
            "content" + ":" + getContent()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "employee = "+(getEmployee()!=null?Integer.toHexString(System.identityHashCode(getEmployee())):"null");
  }
}
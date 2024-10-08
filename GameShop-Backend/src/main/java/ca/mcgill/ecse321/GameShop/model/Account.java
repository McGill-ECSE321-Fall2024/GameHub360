/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;

/**
 * Since we are only developing one game shop system, the root class is unnecessary. Solution: remove the root class (as per the tutorial.)
 * class GameShop
 * {
 * 1 <@>- * CustomerAccount customerAccount;
 * 1 <@>- * EmployeeAccount employeeAccount;
 * <@>- 1 ManagerAccount managerAccount; //(if singleton attribute is added) If I remove the 0..1, there will be a warning related to they keyword singleton;
 * 1 <@>- * GameCategory gameCategory;
 * 1 <@>- * Game game;
 * 1 <@>- * GameRequest gameRequest;
 * 1 <@>- * Order order;
 * <@>- 1 StoreInformation storeInformation;// (if singleton attribute is added) If I remove the 0..1, there will be a warning related to they keyword singleton;
 * 1 <@>- * Review review; //TBC: it's an association class so there should not be multiplicity, but let's try.
 * }
 * // I have removed Boolean + singleton attrs as they were causing many errors.
 */
// line 19 "../../../../../../GameShop.ump"
public abstract class Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Account Attributes
  private String email;
  private String password;
  private String name;
  private String phoneNumber;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Account(String aEmail, String aPassword)
  {
    email = aEmail;
    password = aPassword;
    name = null;
    phoneNumber = null;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setPhoneNumber(String aPhoneNumber)
  {
    boolean wasSet = false;
    phoneNumber = aPhoneNumber;
    wasSet = true;
    return wasSet;
  }

  /**
   * Apparently I am unable to to add two properties here if I add String and unique...
   */
  public String getEmail()
  {
    return email;
  }

  public String getPassword()
  {
    return password;
  }

  public String getName()
  {
    return name;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "email" + ":" + getEmail()+ "," +
            "password" + ":" + getPassword()+ "," +
            "name" + ":" + getName()+ "," +
            "phoneNumber" + ":" + getPhoneNumber()+ "]";
  }
}
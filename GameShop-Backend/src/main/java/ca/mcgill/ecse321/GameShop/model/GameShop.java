/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.GameShop.model;
import java.util.*;
import java.sql.Date;

// line 4 "../../../../../../model.ump"
// line 232 "../../../../../../model.ump"
// line 242 "../../../../../../model.ump"
public class GameShop
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //GameShop Attributes
  private int gameShopId;

  //GameShop Associations
  private List<CustomerAccount> customerAccounts;
  private List<EmployeeAccount> employeeAccounts;
  private ManagerAccount managerAccount;
  private List<GameCategory> gameCategories;
  private List<Game> games;
  private List<GameRequest> gameRequests;
  private List<Order> orders;
  private StoreInformation storeInformation;
  private List<Review> reviews;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public GameShop(int aGameShopId, ManagerAccount aManagerAccount, StoreInformation aStoreInformation)
  {
    gameShopId = aGameShopId;
    customerAccounts = new ArrayList<CustomerAccount>();
    employeeAccounts = new ArrayList<EmployeeAccount>();
    if (aManagerAccount == null || aManagerAccount.getGameShop() != null)
    {
      throw new RuntimeException("Unable to create GameShop due to aManagerAccount. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    managerAccount = aManagerAccount;
    gameCategories = new ArrayList<GameCategory>();
    games = new ArrayList<Game>();
    gameRequests = new ArrayList<GameRequest>();
    orders = new ArrayList<Order>();
    if (aStoreInformation == null || aStoreInformation.getGameShop() != null)
    {
      throw new RuntimeException("Unable to create GameShop due to aStoreInformation. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    storeInformation = aStoreInformation;
    reviews = new ArrayList<Review>();
  }

  public GameShop(int aGameShopId, String aEmailForManagerAccount, String aPasswordForManagerAccount, int aManagerIdForManagerAccount, RequestNote... allWrittenNotesForManagerAccount, int aStoreInfoIdForStoreInformation)
  {
    gameShopId = aGameShopId;
    customerAccounts = new ArrayList<CustomerAccount>();
    employeeAccounts = new ArrayList<EmployeeAccount>();
    managerAccount = new ManagerAccount(aEmailForManagerAccount, aPasswordForManagerAccount, aManagerIdForManagerAccount, this, allWrittenNotesForManagerAccount);
    gameCategories = new ArrayList<GameCategory>();
    games = new ArrayList<Game>();
    gameRequests = new ArrayList<GameRequest>();
    orders = new ArrayList<Order>();
    storeInformation = new StoreInformation(aStoreInfoIdForStoreInformation, this);
    reviews = new ArrayList<Review>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setGameShopId(int aGameShopId)
  {
    boolean wasSet = false;
    gameShopId = aGameShopId;
    wasSet = true;
    return wasSet;
  }

  public int getGameShopId()
  {
    return gameShopId;
  }
  /* Code from template association_GetMany */
  public CustomerAccount getCustomerAccount(int index)
  {
    CustomerAccount aCustomerAccount = customerAccounts.get(index);
    return aCustomerAccount;
  }

  public List<CustomerAccount> getCustomerAccounts()
  {
    List<CustomerAccount> newCustomerAccounts = Collections.unmodifiableList(customerAccounts);
    return newCustomerAccounts;
  }

  public int numberOfCustomerAccounts()
  {
    int number = customerAccounts.size();
    return number;
  }

  public boolean hasCustomerAccounts()
  {
    boolean has = customerAccounts.size() > 0;
    return has;
  }

  public int indexOfCustomerAccount(CustomerAccount aCustomerAccount)
  {
    int index = customerAccounts.indexOf(aCustomerAccount);
    return index;
  }
  /* Code from template association_GetMany */
  public EmployeeAccount getEmployeeAccount(int index)
  {
    EmployeeAccount aEmployeeAccount = employeeAccounts.get(index);
    return aEmployeeAccount;
  }

  public List<EmployeeAccount> getEmployeeAccounts()
  {
    List<EmployeeAccount> newEmployeeAccounts = Collections.unmodifiableList(employeeAccounts);
    return newEmployeeAccounts;
  }

  public int numberOfEmployeeAccounts()
  {
    int number = employeeAccounts.size();
    return number;
  }

  public boolean hasEmployeeAccounts()
  {
    boolean has = employeeAccounts.size() > 0;
    return has;
  }

  public int indexOfEmployeeAccount(EmployeeAccount aEmployeeAccount)
  {
    int index = employeeAccounts.indexOf(aEmployeeAccount);
    return index;
  }
  /* Code from template association_GetOne */
  public ManagerAccount getManagerAccount()
  {
    return managerAccount;
  }
  /* Code from template association_GetMany */
  public GameCategory getGameCategory(int index)
  {
    GameCategory aGameCategory = gameCategories.get(index);
    return aGameCategory;
  }

  public List<GameCategory> getGameCategories()
  {
    List<GameCategory> newGameCategories = Collections.unmodifiableList(gameCategories);
    return newGameCategories;
  }

  public int numberOfGameCategories()
  {
    int number = gameCategories.size();
    return number;
  }

  public boolean hasGameCategories()
  {
    boolean has = gameCategories.size() > 0;
    return has;
  }

  public int indexOfGameCategory(GameCategory aGameCategory)
  {
    int index = gameCategories.indexOf(aGameCategory);
    return index;
  }
  /* Code from template association_GetMany */
  public Game getGame(int index)
  {
    Game aGame = games.get(index);
    return aGame;
  }

  public List<Game> getGames()
  {
    List<Game> newGames = Collections.unmodifiableList(games);
    return newGames;
  }

  public int numberOfGames()
  {
    int number = games.size();
    return number;
  }

  public boolean hasGames()
  {
    boolean has = games.size() > 0;
    return has;
  }

  public int indexOfGame(Game aGame)
  {
    int index = games.indexOf(aGame);
    return index;
  }
  /* Code from template association_GetMany */
  public GameRequest getGameRequest(int index)
  {
    GameRequest aGameRequest = gameRequests.get(index);
    return aGameRequest;
  }

  public List<GameRequest> getGameRequests()
  {
    List<GameRequest> newGameRequests = Collections.unmodifiableList(gameRequests);
    return newGameRequests;
  }

  public int numberOfGameRequests()
  {
    int number = gameRequests.size();
    return number;
  }

  public boolean hasGameRequests()
  {
    boolean has = gameRequests.size() > 0;
    return has;
  }

  public int indexOfGameRequest(GameRequest aGameRequest)
  {
    int index = gameRequests.indexOf(aGameRequest);
    return index;
  }
  /* Code from template association_GetMany */
  public Order getOrder(int index)
  {
    Order aOrder = orders.get(index);
    return aOrder;
  }

  public List<Order> getOrders()
  {
    List<Order> newOrders = Collections.unmodifiableList(orders);
    return newOrders;
  }

  public int numberOfOrders()
  {
    int number = orders.size();
    return number;
  }

  public boolean hasOrders()
  {
    boolean has = orders.size() > 0;
    return has;
  }

  public int indexOfOrder(Order aOrder)
  {
    int index = orders.indexOf(aOrder);
    return index;
  }
  /* Code from template association_GetOne */
  public StoreInformation getStoreInformation()
  {
    return storeInformation;
  }
  /* Code from template association_GetMany */
  public Review getReview(int index)
  {
    Review aReview = reviews.get(index);
    return aReview;
  }

  public List<Review> getReviews()
  {
    List<Review> newReviews = Collections.unmodifiableList(reviews);
    return newReviews;
  }

  public int numberOfReviews()
  {
    int number = reviews.size();
    return number;
  }

  public boolean hasReviews()
  {
    boolean has = reviews.size() > 0;
    return has;
  }

  public int indexOfReview(Review aReview)
  {
    int index = reviews.indexOf(aReview);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCustomerAccounts()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public CustomerAccount addCustomerAccount(String aEmail, String aPassword, int aCustomerId)
  {
    return new CustomerAccount(aEmail, aPassword, aCustomerId, this);
  }

  public boolean addCustomerAccount(CustomerAccount aCustomerAccount)
  {
    boolean wasAdded = false;
    if (customerAccounts.contains(aCustomerAccount)) { return false; }
    GameShop existingGameShop = aCustomerAccount.getGameShop();
    boolean isNewGameShop = existingGameShop != null && !this.equals(existingGameShop);
    if (isNewGameShop)
    {
      aCustomerAccount.setGameShop(this);
    }
    else
    {
      customerAccounts.add(aCustomerAccount);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCustomerAccount(CustomerAccount aCustomerAccount)
  {
    boolean wasRemoved = false;
    //Unable to remove aCustomerAccount, as it must always have a gameShop
    if (!this.equals(aCustomerAccount.getGameShop()))
    {
      customerAccounts.remove(aCustomerAccount);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCustomerAccountAt(CustomerAccount aCustomerAccount, int index)
  {  
    boolean wasAdded = false;
    if(addCustomerAccount(aCustomerAccount))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomerAccounts()) { index = numberOfCustomerAccounts() - 1; }
      customerAccounts.remove(aCustomerAccount);
      customerAccounts.add(index, aCustomerAccount);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCustomerAccountAt(CustomerAccount aCustomerAccount, int index)
  {
    boolean wasAdded = false;
    if(customerAccounts.contains(aCustomerAccount))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomerAccounts()) { index = numberOfCustomerAccounts() - 1; }
      customerAccounts.remove(aCustomerAccount);
      customerAccounts.add(index, aCustomerAccount);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCustomerAccountAt(aCustomerAccount, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfEmployeeAccounts()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public EmployeeAccount addEmployeeAccount(String aEmail, String aPassword, int aEmployeeId, boolean aIsActive, RequestNote... allWrittenNotes)
  {
    return new EmployeeAccount(aEmail, aPassword, aEmployeeId, aIsActive, this, allWrittenNotes);
  }

  public boolean addEmployeeAccount(EmployeeAccount aEmployeeAccount)
  {
    boolean wasAdded = false;
    if (employeeAccounts.contains(aEmployeeAccount)) { return false; }
    GameShop existingGameShop = aEmployeeAccount.getGameShop();
    boolean isNewGameShop = existingGameShop != null && !this.equals(existingGameShop);
    if (isNewGameShop)
    {
      aEmployeeAccount.setGameShop(this);
    }
    else
    {
      employeeAccounts.add(aEmployeeAccount);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeEmployeeAccount(EmployeeAccount aEmployeeAccount)
  {
    boolean wasRemoved = false;
    //Unable to remove aEmployeeAccount, as it must always have a gameShop
    if (!this.equals(aEmployeeAccount.getGameShop()))
    {
      employeeAccounts.remove(aEmployeeAccount);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addEmployeeAccountAt(EmployeeAccount aEmployeeAccount, int index)
  {  
    boolean wasAdded = false;
    if(addEmployeeAccount(aEmployeeAccount))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfEmployeeAccounts()) { index = numberOfEmployeeAccounts() - 1; }
      employeeAccounts.remove(aEmployeeAccount);
      employeeAccounts.add(index, aEmployeeAccount);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveEmployeeAccountAt(EmployeeAccount aEmployeeAccount, int index)
  {
    boolean wasAdded = false;
    if(employeeAccounts.contains(aEmployeeAccount))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfEmployeeAccounts()) { index = numberOfEmployeeAccounts() - 1; }
      employeeAccounts.remove(aEmployeeAccount);
      employeeAccounts.add(index, aEmployeeAccount);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addEmployeeAccountAt(aEmployeeAccount, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfGameCategories()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public GameCategory addGameCategory(int aCategoryId, boolean aIsAvailable, String aName)
  {
    return new GameCategory(aCategoryId, aIsAvailable, aName, this);
  }

  public boolean addGameCategory(GameCategory aGameCategory)
  {
    boolean wasAdded = false;
    if (gameCategories.contains(aGameCategory)) { return false; }
    GameShop existingGameShop = aGameCategory.getGameShop();
    boolean isNewGameShop = existingGameShop != null && !this.equals(existingGameShop);
    if (isNewGameShop)
    {
      aGameCategory.setGameShop(this);
    }
    else
    {
      gameCategories.add(aGameCategory);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeGameCategory(GameCategory aGameCategory)
  {
    boolean wasRemoved = false;
    //Unable to remove aGameCategory, as it must always have a gameShop
    if (!this.equals(aGameCategory.getGameShop()))
    {
      gameCategories.remove(aGameCategory);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addGameCategoryAt(GameCategory aGameCategory, int index)
  {  
    boolean wasAdded = false;
    if(addGameCategory(aGameCategory))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGameCategories()) { index = numberOfGameCategories() - 1; }
      gameCategories.remove(aGameCategory);
      gameCategories.add(index, aGameCategory);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveGameCategoryAt(GameCategory aGameCategory, int index)
  {
    boolean wasAdded = false;
    if(gameCategories.contains(aGameCategory))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGameCategories()) { index = numberOfGameCategories() - 1; }
      gameCategories.remove(aGameCategory);
      gameCategories.add(index, aGameCategory);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addGameCategoryAt(aGameCategory, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfGames()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Game addGame(String aName, String aDescription, String aImageURL, int aGameId, int aQuantityInStock, boolean aIsAvailable, int aPrice, GameCategory... allCategories)
  {
    return new Game(aName, aDescription, aImageURL, aGameId, aQuantityInStock, aIsAvailable, aPrice, this, allCategories);
  }

  public boolean addGame(Game aGame)
  {
    boolean wasAdded = false;
    if (games.contains(aGame)) { return false; }
    GameShop existingGameShop = aGame.getGameShop();
    boolean isNewGameShop = existingGameShop != null && !this.equals(existingGameShop);
    if (isNewGameShop)
    {
      aGame.setGameShop(this);
    }
    else
    {
      games.add(aGame);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeGame(Game aGame)
  {
    boolean wasRemoved = false;
    //Unable to remove aGame, as it must always have a gameShop
    if (!this.equals(aGame.getGameShop()))
    {
      games.remove(aGame);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addGameAt(Game aGame, int index)
  {  
    boolean wasAdded = false;
    if(addGame(aGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGames()) { index = numberOfGames() - 1; }
      games.remove(aGame);
      games.add(index, aGame);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveGameAt(Game aGame, int index)
  {
    boolean wasAdded = false;
    if(games.contains(aGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGames()) { index = numberOfGames() - 1; }
      games.remove(aGame);
      games.add(index, aGame);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addGameAt(aGame, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfGameRequests()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public GameRequest addGameRequest(String aName, String aDescription, String aImageURL, int aRequestId, Date aRequestDate, EmployeeAccount aRequestPlacer, GameCategory... allCategories)
  {
    return new GameRequest(aName, aDescription, aImageURL, aRequestId, aRequestDate, this, aRequestPlacer, allCategories);
  }

  public boolean addGameRequest(GameRequest aGameRequest)
  {
    boolean wasAdded = false;
    if (gameRequests.contains(aGameRequest)) { return false; }
    GameShop existingGameShop = aGameRequest.getGameShop();
    boolean isNewGameShop = existingGameShop != null && !this.equals(existingGameShop);
    if (isNewGameShop)
    {
      aGameRequest.setGameShop(this);
    }
    else
    {
      gameRequests.add(aGameRequest);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeGameRequest(GameRequest aGameRequest)
  {
    boolean wasRemoved = false;
    //Unable to remove aGameRequest, as it must always have a gameShop
    if (!this.equals(aGameRequest.getGameShop()))
    {
      gameRequests.remove(aGameRequest);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addGameRequestAt(GameRequest aGameRequest, int index)
  {  
    boolean wasAdded = false;
    if(addGameRequest(aGameRequest))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGameRequests()) { index = numberOfGameRequests() - 1; }
      gameRequests.remove(aGameRequest);
      gameRequests.add(index, aGameRequest);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveGameRequestAt(GameRequest aGameRequest, int index)
  {
    boolean wasAdded = false;
    if(gameRequests.contains(aGameRequest))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGameRequests()) { index = numberOfGameRequests() - 1; }
      gameRequests.remove(aGameRequest);
      gameRequests.add(index, aGameRequest);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addGameRequestAt(aGameRequest, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrders()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Order addOrder(int aOrderId, Date aOrderDate, Review aOrderReview, CustomerAccount aOrderedBy, PaymentDetails aPaymentInformation, Game... allGames)
  {
    return new Order(aOrderId, aOrderDate, this, aOrderReview, aOrderedBy, aPaymentInformation, allGames);
  }

  public boolean addOrder(Order aOrder)
  {
    boolean wasAdded = false;
    if (orders.contains(aOrder)) { return false; }
    GameShop existingGameShop = aOrder.getGameShop();
    boolean isNewGameShop = existingGameShop != null && !this.equals(existingGameShop);
    if (isNewGameShop)
    {
      aOrder.setGameShop(this);
    }
    else
    {
      orders.add(aOrder);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrder(Order aOrder)
  {
    boolean wasRemoved = false;
    //Unable to remove aOrder, as it must always have a gameShop
    if (!this.equals(aOrder.getGameShop()))
    {
      orders.remove(aOrder);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderAt(Order aOrder, int index)
  {  
    boolean wasAdded = false;
    if(addOrder(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderAt(Order aOrder, int index)
  {
    boolean wasAdded = false;
    if(orders.contains(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderAt(aOrder, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfReviews()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Review addReview(int aReviewId, Date aReviewDate, CustomerAccount aReviewAuthor, Order aReviewedOrder)
  {
    return new Review(aReviewId, aReviewDate, this, aReviewAuthor, aReviewedOrder);
  }

  public boolean addReview(Review aReview)
  {
    boolean wasAdded = false;
    if (reviews.contains(aReview)) { return false; }
    GameShop existingGameShop = aReview.getGameShop();
    boolean isNewGameShop = existingGameShop != null && !this.equals(existingGameShop);
    if (isNewGameShop)
    {
      aReview.setGameShop(this);
    }
    else
    {
      reviews.add(aReview);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeReview(Review aReview)
  {
    boolean wasRemoved = false;
    //Unable to remove aReview, as it must always have a gameShop
    if (!this.equals(aReview.getGameShop()))
    {
      reviews.remove(aReview);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addReviewAt(Review aReview, int index)
  {  
    boolean wasAdded = false;
    if(addReview(aReview))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReviews()) { index = numberOfReviews() - 1; }
      reviews.remove(aReview);
      reviews.add(index, aReview);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveReviewAt(Review aReview, int index)
  {
    boolean wasAdded = false;
    if(reviews.contains(aReview))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReviews()) { index = numberOfReviews() - 1; }
      reviews.remove(aReview);
      reviews.add(index, aReview);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addReviewAt(aReview, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (customerAccounts.size() > 0)
    {
      CustomerAccount aCustomerAccount = customerAccounts.get(customerAccounts.size() - 1);
      aCustomerAccount.delete();
      customerAccounts.remove(aCustomerAccount);
    }
    
    while (employeeAccounts.size() > 0)
    {
      EmployeeAccount aEmployeeAccount = employeeAccounts.get(employeeAccounts.size() - 1);
      aEmployeeAccount.delete();
      employeeAccounts.remove(aEmployeeAccount);
    }
    
    ManagerAccount existingManagerAccount = managerAccount;
    managerAccount = null;
    if (existingManagerAccount != null)
    {
      existingManagerAccount.delete();
    }
    while (gameCategories.size() > 0)
    {
      GameCategory aGameCategory = gameCategories.get(gameCategories.size() - 1);
      aGameCategory.delete();
      gameCategories.remove(aGameCategory);
    }
    
    while (games.size() > 0)
    {
      Game aGame = games.get(games.size() - 1);
      aGame.delete();
      games.remove(aGame);
    }
    
    while (gameRequests.size() > 0)
    {
      GameRequest aGameRequest = gameRequests.get(gameRequests.size() - 1);
      aGameRequest.delete();
      gameRequests.remove(aGameRequest);
    }
    
    while (orders.size() > 0)
    {
      Order aOrder = orders.get(orders.size() - 1);
      aOrder.delete();
      orders.remove(aOrder);
    }
    
    StoreInformation existingStoreInformation = storeInformation;
    storeInformation = null;
    if (existingStoreInformation != null)
    {
      existingStoreInformation.delete();
    }
    while (reviews.size() > 0)
    {
      Review aReview = reviews.get(reviews.size() - 1);
      aReview.delete();
      reviews.remove(aReview);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "gameShopId" + ":" + getGameShopId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "managerAccount = "+(getManagerAccount()!=null?Integer.toHexString(System.identityHashCode(getManagerAccount())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "storeInformation = "+(getStoreInformation()!=null?Integer.toHexString(System.identityHashCode(getStoreInformation())):"null");
  }
}
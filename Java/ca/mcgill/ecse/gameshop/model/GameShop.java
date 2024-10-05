/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse.gameshop.model;
import java.util.*;
import java.sql.Date;

// line 3 "../../../../../../GameShop.ump"
public class GameShop
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //GameShop Associations
  private List<CustomerAccount> customerAccount;
  private List<EmployeeAccount> employeeAccount;
  private ManagerAccount managerAccount;
  private List<GameCategory> gameCategory;
  private List<Game> game;
  private List<GameRequest> gameRequest;
  private List<Order> order;
  private StoreInformation storeInformation;
  private List<Review> review;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public GameShop(ManagerAccount aManagerAccount, StoreInformation aStoreInformation)
  {
    customerAccount = new ArrayList<CustomerAccount>();
    employeeAccount = new ArrayList<EmployeeAccount>();
    boolean didAddManagerAccount = setManagerAccount(aManagerAccount);
    if (!didAddManagerAccount)
    {
      throw new RuntimeException("Unable to create gameShop due to managerAccount. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    gameCategory = new ArrayList<GameCategory>();
    game = new ArrayList<Game>();
    gameRequest = new ArrayList<GameRequest>();
    order = new ArrayList<Order>();
    boolean didAddStoreInformation = setStoreInformation(aStoreInformation);
    if (!didAddStoreInformation)
    {
      throw new RuntimeException("Unable to create gameShop due to storeInformation. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    review = new ArrayList<Review>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public CustomerAccount getCustomerAccount(int index)
  {
    CustomerAccount aCustomerAccount = customerAccount.get(index);
    return aCustomerAccount;
  }

  public List<CustomerAccount> getCustomerAccount()
  {
    List<CustomerAccount> newCustomerAccount = Collections.unmodifiableList(customerAccount);
    return newCustomerAccount;
  }

  public int numberOfCustomerAccount()
  {
    int number = customerAccount.size();
    return number;
  }

  public boolean hasCustomerAccount()
  {
    boolean has = customerAccount.size() > 0;
    return has;
  }

  public int indexOfCustomerAccount(CustomerAccount aCustomerAccount)
  {
    int index = customerAccount.indexOf(aCustomerAccount);
    return index;
  }
  /* Code from template association_GetMany */
  public EmployeeAccount getEmployeeAccount(int index)
  {
    EmployeeAccount aEmployeeAccount = employeeAccount.get(index);
    return aEmployeeAccount;
  }

  public List<EmployeeAccount> getEmployeeAccount()
  {
    List<EmployeeAccount> newEmployeeAccount = Collections.unmodifiableList(employeeAccount);
    return newEmployeeAccount;
  }

  public int numberOfEmployeeAccount()
  {
    int number = employeeAccount.size();
    return number;
  }

  public boolean hasEmployeeAccount()
  {
    boolean has = employeeAccount.size() > 0;
    return has;
  }

  public int indexOfEmployeeAccount(EmployeeAccount aEmployeeAccount)
  {
    int index = employeeAccount.indexOf(aEmployeeAccount);
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
    GameCategory aGameCategory = gameCategory.get(index);
    return aGameCategory;
  }

  public List<GameCategory> getGameCategory()
  {
    List<GameCategory> newGameCategory = Collections.unmodifiableList(gameCategory);
    return newGameCategory;
  }

  public int numberOfGameCategory()
  {
    int number = gameCategory.size();
    return number;
  }

  public boolean hasGameCategory()
  {
    boolean has = gameCategory.size() > 0;
    return has;
  }

  public int indexOfGameCategory(GameCategory aGameCategory)
  {
    int index = gameCategory.indexOf(aGameCategory);
    return index;
  }
  /* Code from template association_GetMany */
  public Game getGame(int index)
  {
    Game aGame = game.get(index);
    return aGame;
  }

  public List<Game> getGame()
  {
    List<Game> newGame = Collections.unmodifiableList(game);
    return newGame;
  }

  public int numberOfGame()
  {
    int number = game.size();
    return number;
  }

  public boolean hasGame()
  {
    boolean has = game.size() > 0;
    return has;
  }

  public int indexOfGame(Game aGame)
  {
    int index = game.indexOf(aGame);
    return index;
  }
  /* Code from template association_GetMany */
  public GameRequest getGameRequest(int index)
  {
    GameRequest aGameRequest = gameRequest.get(index);
    return aGameRequest;
  }

  public List<GameRequest> getGameRequest()
  {
    List<GameRequest> newGameRequest = Collections.unmodifiableList(gameRequest);
    return newGameRequest;
  }

  public int numberOfGameRequest()
  {
    int number = gameRequest.size();
    return number;
  }

  public boolean hasGameRequest()
  {
    boolean has = gameRequest.size() > 0;
    return has;
  }

  public int indexOfGameRequest(GameRequest aGameRequest)
  {
    int index = gameRequest.indexOf(aGameRequest);
    return index;
  }
  /* Code from template association_GetMany */
  public Order getOrder(int index)
  {
    Order aOrder = order.get(index);
    return aOrder;
  }

  public List<Order> getOrder()
  {
    List<Order> newOrder = Collections.unmodifiableList(order);
    return newOrder;
  }

  public int numberOfOrder()
  {
    int number = order.size();
    return number;
  }

  public boolean hasOrder()
  {
    boolean has = order.size() > 0;
    return has;
  }

  public int indexOfOrder(Order aOrder)
  {
    int index = order.indexOf(aOrder);
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
    Review aReview = review.get(index);
    return aReview;
  }

  /**
   * TBC: it's an association class so there should not be multiplicity, but let's try.
   */
  public List<Review> getReview()
  {
    List<Review> newReview = Collections.unmodifiableList(review);
    return newReview;
  }

  public int numberOfReview()
  {
    int number = review.size();
    return number;
  }

  public boolean hasReview()
  {
    boolean has = review.size() > 0;
    return has;
  }

  public int indexOfReview(Review aReview)
  {
    int index = review.indexOf(aReview);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCustomerAccount()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public CustomerAccount addCustomerAccount(String aEmail, String aPassword)
  {
    return new CustomerAccount(aEmail, aPassword, this);
  }

  public boolean addCustomerAccount(CustomerAccount aCustomerAccount)
  {
    boolean wasAdded = false;
    if (customerAccount.contains(aCustomerAccount)) { return false; }
    GameShop existingGameShop = aCustomerAccount.getGameShop();
    boolean isNewGameShop = existingGameShop != null && !this.equals(existingGameShop);
    if (isNewGameShop)
    {
      aCustomerAccount.setGameShop(this);
    }
    else
    {
      customerAccount.add(aCustomerAccount);
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
      customerAccount.remove(aCustomerAccount);
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
      if(index > numberOfCustomerAccount()) { index = numberOfCustomerAccount() - 1; }
      customerAccount.remove(aCustomerAccount);
      customerAccount.add(index, aCustomerAccount);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCustomerAccountAt(CustomerAccount aCustomerAccount, int index)
  {
    boolean wasAdded = false;
    if(customerAccount.contains(aCustomerAccount))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomerAccount()) { index = numberOfCustomerAccount() - 1; }
      customerAccount.remove(aCustomerAccount);
      customerAccount.add(index, aCustomerAccount);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCustomerAccountAt(aCustomerAccount, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfEmployeeAccount()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public EmployeeAccount addEmployeeAccount(String aEmail, String aPassword, String aIsActive)
  {
    return new EmployeeAccount(aEmail, aPassword, aIsActive, this);
  }

  public boolean addEmployeeAccount(EmployeeAccount aEmployeeAccount)
  {
    boolean wasAdded = false;
    if (employeeAccount.contains(aEmployeeAccount)) { return false; }
    GameShop existingGameShop = aEmployeeAccount.getGameShop();
    boolean isNewGameShop = existingGameShop != null && !this.equals(existingGameShop);
    if (isNewGameShop)
    {
      aEmployeeAccount.setGameShop(this);
    }
    else
    {
      employeeAccount.add(aEmployeeAccount);
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
      employeeAccount.remove(aEmployeeAccount);
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
      if(index > numberOfEmployeeAccount()) { index = numberOfEmployeeAccount() - 1; }
      employeeAccount.remove(aEmployeeAccount);
      employeeAccount.add(index, aEmployeeAccount);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveEmployeeAccountAt(EmployeeAccount aEmployeeAccount, int index)
  {
    boolean wasAdded = false;
    if(employeeAccount.contains(aEmployeeAccount))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfEmployeeAccount()) { index = numberOfEmployeeAccount() - 1; }
      employeeAccount.remove(aEmployeeAccount);
      employeeAccount.add(index, aEmployeeAccount);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addEmployeeAccountAt(aEmployeeAccount, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setManagerAccount(ManagerAccount aManagerAccount)
  {
    boolean wasSet = false;
    if (aManagerAccount == null)
    {
      return wasSet;
    }

    ManagerAccount existingManagerAccount = managerAccount;
    managerAccount = aManagerAccount;
    if (existingManagerAccount != null && !existingManagerAccount.equals(aManagerAccount))
    {
      existingManagerAccount.removeGameShop(this);
    }
    managerAccount.addGameShop(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfGameCategory()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public GameCategory addGameCategory(String aIsAvailable, String aName)
  {
    return new GameCategory(aIsAvailable, aName, this);
  }

  public boolean addGameCategory(GameCategory aGameCategory)
  {
    boolean wasAdded = false;
    if (gameCategory.contains(aGameCategory)) { return false; }
    GameShop existingGameShop = aGameCategory.getGameShop();
    boolean isNewGameShop = existingGameShop != null && !this.equals(existingGameShop);
    if (isNewGameShop)
    {
      aGameCategory.setGameShop(this);
    }
    else
    {
      gameCategory.add(aGameCategory);
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
      gameCategory.remove(aGameCategory);
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
      if(index > numberOfGameCategory()) { index = numberOfGameCategory() - 1; }
      gameCategory.remove(aGameCategory);
      gameCategory.add(index, aGameCategory);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveGameCategoryAt(GameCategory aGameCategory, int index)
  {
    boolean wasAdded = false;
    if(gameCategory.contains(aGameCategory))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGameCategory()) { index = numberOfGameCategory() - 1; }
      gameCategory.remove(aGameCategory);
      gameCategory.add(index, aGameCategory);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addGameCategoryAt(aGameCategory, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfGame()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Game addGame(String aName, String aDescription, String aImageURL, String aNameOfX, int aQuantityInStock, String aIsAvailable, int aPrice, GameCategory... allCategories)
  {
    return new Game(aName, aDescription, aImageURL, aNameOfX, aQuantityInStock, aIsAvailable, aPrice, this, allCategories);
  }

  public boolean addGame(Game aGame)
  {
    boolean wasAdded = false;
    if (game.contains(aGame)) { return false; }
    GameShop existingGameShop = aGame.getGameShop();
    boolean isNewGameShop = existingGameShop != null && !this.equals(existingGameShop);
    if (isNewGameShop)
    {
      aGame.setGameShop(this);
    }
    else
    {
      game.add(aGame);
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
      game.remove(aGame);
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
      if(index > numberOfGame()) { index = numberOfGame() - 1; }
      game.remove(aGame);
      game.add(index, aGame);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveGameAt(Game aGame, int index)
  {
    boolean wasAdded = false;
    if(game.contains(aGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGame()) { index = numberOfGame() - 1; }
      game.remove(aGame);
      game.add(index, aGame);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addGameAt(aGame, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfGameRequest()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public GameRequest addGameRequest(String aName, String aDescription, String aImageURL, String aNameOfX, Date aDate, EmployeeAccount aRequestPlacer, GameCategory... allCategories)
  {
    return new GameRequest(aName, aDescription, aImageURL, aNameOfX, aDate, this, aRequestPlacer, allCategories);
  }

  public boolean addGameRequest(GameRequest aGameRequest)
  {
    boolean wasAdded = false;
    if (gameRequest.contains(aGameRequest)) { return false; }
    GameShop existingGameShop = aGameRequest.getGameShop();
    boolean isNewGameShop = existingGameShop != null && !this.equals(existingGameShop);
    if (isNewGameShop)
    {
      aGameRequest.setGameShop(this);
    }
    else
    {
      gameRequest.add(aGameRequest);
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
      gameRequest.remove(aGameRequest);
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
      if(index > numberOfGameRequest()) { index = numberOfGameRequest() - 1; }
      gameRequest.remove(aGameRequest);
      gameRequest.add(index, aGameRequest);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveGameRequestAt(GameRequest aGameRequest, int index)
  {
    boolean wasAdded = false;
    if(gameRequest.contains(aGameRequest))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGameRequest()) { index = numberOfGameRequest() - 1; }
      gameRequest.remove(aGameRequest);
      gameRequest.add(index, aGameRequest);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addGameRequestAt(aGameRequest, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrder()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Order addOrder(Date aDate, CustomerAccount aOrderedBy, PaymentInformation aPaymentInformation, Game... allGame)
  {
    return new Order(aDate, this, aOrderedBy, aPaymentInformation, allGame);
  }

  public boolean addOrder(Order aOrder)
  {
    boolean wasAdded = false;
    if (order.contains(aOrder)) { return false; }
    GameShop existingGameShop = aOrder.getGameShop();
    boolean isNewGameShop = existingGameShop != null && !this.equals(existingGameShop);
    if (isNewGameShop)
    {
      aOrder.setGameShop(this);
    }
    else
    {
      order.add(aOrder);
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
      order.remove(aOrder);
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
      if(index > numberOfOrder()) { index = numberOfOrder() - 1; }
      order.remove(aOrder);
      order.add(index, aOrder);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderAt(Order aOrder, int index)
  {
    boolean wasAdded = false;
    if(order.contains(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrder()) { index = numberOfOrder() - 1; }
      order.remove(aOrder);
      order.add(index, aOrder);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderAt(aOrder, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setStoreInformation(StoreInformation aStoreInformation)
  {
    boolean wasSet = false;
    if (aStoreInformation == null)
    {
      return wasSet;
    }

    StoreInformation existingStoreInformation = storeInformation;
    storeInformation = aStoreInformation;
    if (existingStoreInformation != null && !existingStoreInformation.equals(aStoreInformation))
    {
      existingStoreInformation.removeGameShop(this);
    }
    storeInformation.addGameShop(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfReview()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Review addReview(Date aDate, CustomerAccount aCustomerAccount, Order aOrder)
  {
    return new Review(aDate, this, aCustomerAccount, aOrder);
  }

  public boolean addReview(Review aReview)
  {
    boolean wasAdded = false;
    if (review.contains(aReview)) { return false; }
    GameShop existingGameShop = aReview.getGameShop();
    boolean isNewGameShop = existingGameShop != null && !this.equals(existingGameShop);
    if (isNewGameShop)
    {
      aReview.setGameShop(this);
    }
    else
    {
      review.add(aReview);
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
      review.remove(aReview);
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
      if(index > numberOfReview()) { index = numberOfReview() - 1; }
      review.remove(aReview);
      review.add(index, aReview);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveReviewAt(Review aReview, int index)
  {
    boolean wasAdded = false;
    if(review.contains(aReview))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReview()) { index = numberOfReview() - 1; }
      review.remove(aReview);
      review.add(index, aReview);
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
    while (customerAccount.size() > 0)
    {
      CustomerAccount aCustomerAccount = customerAccount.get(customerAccount.size() - 1);
      aCustomerAccount.delete();
      customerAccount.remove(aCustomerAccount);
    }
    
    while (employeeAccount.size() > 0)
    {
      EmployeeAccount aEmployeeAccount = employeeAccount.get(employeeAccount.size() - 1);
      aEmployeeAccount.delete();
      employeeAccount.remove(aEmployeeAccount);
    }
    
    ManagerAccount existingManagerAccount = managerAccount;
    managerAccount = null;
    if (existingManagerAccount != null)
    {
      existingManagerAccount.delete();
    }
    while (gameCategory.size() > 0)
    {
      GameCategory aGameCategory = gameCategory.get(gameCategory.size() - 1);
      aGameCategory.delete();
      gameCategory.remove(aGameCategory);
    }
    
    while (game.size() > 0)
    {
      Game aGame = game.get(game.size() - 1);
      aGame.delete();
      game.remove(aGame);
    }
    
    while (gameRequest.size() > 0)
    {
      GameRequest aGameRequest = gameRequest.get(gameRequest.size() - 1);
      aGameRequest.delete();
      gameRequest.remove(aGameRequest);
    }
    
    while (order.size() > 0)
    {
      Order aOrder = order.get(order.size() - 1);
      aOrder.delete();
      order.remove(aOrder);
    }
    
    StoreInformation existingStoreInformation = storeInformation;
    storeInformation = null;
    if (existingStoreInformation != null)
    {
      existingStoreInformation.delete();
    }
    while (review.size() > 0)
    {
      Review aReview = review.get(review.size() - 1);
      aReview.delete();
      review.remove(aReview);
    }
    
  }

}
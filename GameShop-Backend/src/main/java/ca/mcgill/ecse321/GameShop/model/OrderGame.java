/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse321.GameShop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;

// line 123 "../../../../../../model.ump"
// line 232 "../../../../../../model.ump"
@Entity
public class OrderGame {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  // OrderGame Associations
  @OneToOne
  @JoinColumn(name = "review_id")
  private Review review;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private CustomerOrder customerOrder;

  @ManyToOne
  @JoinColumn(name = "game_entity_id")
  private Game game;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public OrderGame(CustomerOrder aCustomerOrder, Game aGame) {
    boolean didAddCustomerOrder = setCustomerOrder(aCustomerOrder);
    if (!didAddCustomerOrder) {
      throw new RuntimeException(
          "Unable to create orderedGame due to customerOrder. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddGame = setGame(aGame);
    if (!didAddGame) {
      throw new RuntimeException(
          "Unable to create order due to game. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public OrderGame() {
  }

  // ------------------------
  // INTERFACE
  // ------------------------
  /* Code from template association_GetOne */
  public Review getReview() {
    return review;
  }

  public boolean hasReview() {
    boolean has = review != null;
    return has;
  }

  /* Code from template association_GetOne */
  public CustomerOrder getCustomerOrder() {
    return customerOrder;
  }

  /* Code from template association_GetOne */
  public Game getGame() {
    return game;
  }

  /* Code from template association_SetOptionalOneToOne */
  public boolean setReview(Review aNewReview) {
    boolean wasSet = false;
    if (review != null && !review.equals(aNewReview) && equals(review.getReviewedGame())) {
      // Unable to setReview, as existing review would become an orphan
      return wasSet;
    }

    review = aNewReview;
    OrderGame anOldReviewedGame = aNewReview != null ? aNewReview.getReviewedGame() : null;

    if (!this.equals(anOldReviewedGame)) {
      if (anOldReviewedGame != null) {
        anOldReviewedGame.review = null;
      }
      if (review != null) {
        review.setReviewedGame(this);
      }
    }
    wasSet = true;
    return wasSet;
  }

  /* Code from template association_SetOneToMandatoryMany */
  public boolean setCustomerOrder(CustomerOrder aCustomerOrder) {
    boolean wasSet = false;
    // Must provide customerOrder to orderedGame
    if (aCustomerOrder == null) {
      return wasSet;
    }

    if (customerOrder != null && customerOrder.numberOfOrderedGames() <= CustomerOrder.minimumNumberOfOrderedGames()) {
      return wasSet;
    }

    CustomerOrder existingCustomerOrder = customerOrder;
    customerOrder = aCustomerOrder;
    if (existingCustomerOrder != null && !existingCustomerOrder.equals(aCustomerOrder)) {
      boolean didRemove = existingCustomerOrder.removeOrderedGame(this);
      if (!didRemove) {
        customerOrder = existingCustomerOrder;
        return wasSet;
      }
    }
    customerOrder.addOrderedGame(this);
    wasSet = true;
    return wasSet;
  }

  /* Code from template association_SetOneToMany */
  public boolean setGame(Game aGame) {
    boolean wasSet = false;
    if (aGame == null) {
      return wasSet;
    }

    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame)) {
      existingGame.removeOrder(this);
    }
    game.addOrder(this);
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    Review existingReview = review;
    review = null;
    if (existingReview != null) {
      existingReview.delete();
    }
    CustomerOrder placeholderCustomerOrder = customerOrder;
    this.customerOrder = null;
    if (placeholderCustomerOrder != null) {
      placeholderCustomerOrder.removeOrderedGame(this);
    }
    Game placeholderGame = game;
    this.game = null;
    if (placeholderGame != null) {
      placeholderGame.removeOrder(this);
    }
  }

}
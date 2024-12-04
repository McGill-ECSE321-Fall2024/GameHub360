package ca.mcgill.ecse321.GameShop.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
import ca.mcgill.ecse321.GameShop.model.Reply;
import ca.mcgill.ecse321.GameShop.model.Review;

@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository repo;
    @Autowired
    private ReviewRepository reviewRepo;
    @Autowired
    private OrderGameRepository orderGameRepo;
    @Autowired
    private GameRepository gameRepo;
    @Autowired
    private ManagerAccountRepository managerRepo;
    @Autowired
    private GameCategoryRepository gameCatRepo;
    @Autowired
    private CustomerOrderRepository customerOrdRepo;
    @Autowired
    private PaymentDetailsRepository payementDetRepo;
    @Autowired
    private CustomerAccountRepository customerAccRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        customerAccRepo.deleteAll();
        payementDetRepo.deleteAll();
        customerOrdRepo.deleteAll();
        gameCatRepo.deleteAll();
        gameRepo.deleteAll();
        orderGameRepo.deleteAll();
        reviewRepo.deleteAll(); 
        managerRepo.deleteAll(); 
        repo.deleteAll();
    }

    @Test
    void testPersistAndLoadReply() {

        // ---- Arrange 

        ManagerAccount manager = new ManagerAccount("john.doe@email.com", "1234");
        managerRepo.save(manager);

        Date date = new Date(System.currentTimeMillis());

        CustomerAccount customerAcc = new CustomerAccount("mab_222@mail.com", "password");
        customerAcc = customerAccRepo.save(customerAcc);
        PaymentDetails payementDetails = new PaymentDetails("TDCard", "XXX XXX", "1234", 10, 2025, customerAcc);
        payementDetails = payementDetRepo.save(payementDetails);
        CustomerOrder customerOrder = new CustomerOrder(date, customerAcc, payementDetails);
        customerOrder = customerOrdRepo.save(customerOrder);

        GameCategory category = new GameCategory(true, "Arcade");
        category = gameCatRepo.save(category);
        Game game = new Game("COD", "Call Of Duty", "https://www.url.ca", 10, true, 20.99, category);
        game = gameRepo.save(game);

        OrderGame reviewedGame = new OrderGame(customerOrder, game);
        reviewedGame = orderGameRepo.save(reviewedGame);

        Review review = new Review(date, reviewedGame);
        reviewRepo.save(review);

        String replyContent = "Thank you for your feedback!";
        Reply reply = new Reply(replyContent, date, review, manager);
        repo.save(reply);

        // ---- Act
        Reply retrievedReply = repo.findReplyByReplyId(reply.getReplyId());

        // ---- Assert
        // Asserting the Attributes
        assertNotNull(retrievedReply);
        assertEquals(retrievedReply.getReplyId(), reply.getReplyId());
        assertEquals(retrievedReply.getContent(), reply.getContent());
        assertEquals(retrievedReply.getReplyDate().toString(), reply.getReplyDate().toString());

        // Asserting the Associations
        assertEquals(retrievedReply.getReviewRecord().getReviewId(), reply.getReviewRecord().getReviewId());
        assertEquals(retrievedReply.getReviewer().getStaffId(), reply.getReviewer().getStaffId());
    }

}

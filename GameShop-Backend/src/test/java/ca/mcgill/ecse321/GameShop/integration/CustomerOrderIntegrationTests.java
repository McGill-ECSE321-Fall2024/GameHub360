package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.GameShop.dto.CustomerOrderRequestDto;
import ca.mcgill.ecse321.GameShop.dto.CustomerOrderResponseDto;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.CustomerOrderRepository;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.OrderGameRepository;
import ca.mcgill.ecse321.GameShop.repository.PaymentDetailsRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CustomerOrderIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    @Autowired
    private PaymentDetailsRepository PaymentDetailsRepository;

    @Autowired
    private OrderGameRepository OrderGameRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    private static final String VALID_EMAIL = "employee@example.com";
    private static final String VALID_PASSWORD = "Strong@Pass1";
    private static final String INVALID_PASSWORD = "wrongpassword";
    private static final String NON_EXISTENT_EMAIL = "nonexistent@example.com";
    private static final String VALID_NAME = "John Doe";
    private static final String VALID_PHONE = "123-456-7890";
    private static final Boolean ISACTIVE_TRUE = true;
    private static final Boolean ISACTIVE_FALSE = false;

    private int validCustomerId;
    private int validPaymentId;
    private int validOrderedGameId;

    @BeforeAll
    public static void setupTimezone() {
        // Ensure the application runs in UTC for consistent date handling
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @AfterEach
    public void cleanUp() {
//        customerOrderRepository.deleteAll();
//        customerAccountRepository.deleteAll();
//        PaymentDetailsRepository.deleteAll();
//        OrderGameRepository.deleteAll();
//        gameCategoryRepository.deleteAll();
//        gameRepository.deleteAll();
    }

    @BeforeEach
    public void setup() {
        customerOrderRepository.deleteAll();
        customerAccountRepository.deleteAll();
        PaymentDetailsRepository.deleteAll();
        OrderGameRepository.deleteAll();
        gameCategoryRepository.deleteAll();
        gameRepository.deleteAll();

        GameCategory gameCategory = new GameCategory();
        Game game = new Game();
        game.setCategories(gameCategory);

        OrderGame orderGame = new OrderGame();
        orderGame.setGame(game);

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.addOrderedGame(orderGame);
        orderGame.setCustomerOrder(customerOrder);

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setCardName(VALID_NAME);

        CustomerAccount customer = new CustomerAccount(VALID_EMAIL, VALID_PASSWORD);
        customer.addPaymentCard(paymentDetails);
        customer.addOrderHistory(customerOrder);

        CustomerAccount response = customerAccountRepository.save(customer);

        validCustomerId = response.getCustomerId();
        validOrderedGameId = response.getOrderHistory().getFirst().getOrderedGames().getFirst().getOrderGameId();
        validPaymentId = response.getPaymentCard(0).getPaymentDetailsId();
    }

    @Test
    @Order(1)
    public void testCreateCustomerOrder() {

        CustomerOrderRequestDto requestDto = new CustomerOrderRequestDto(Date.valueOf("2024-11-11"), List.of(validOrderedGameId), validCustomerId, validPaymentId);

        ResponseEntity<CustomerOrderResponseDto> response = client.postForEntity("/orders/", requestDto, CustomerOrderResponseDto.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().getOrderDate(), Date.valueOf("2024-11-11"));
        assertEquals(response.getBody().getOrderedBy().getCustomerId(), validCustomerId);
        assertEquals(response.getBody().getPaymentInformation().getPaymentDetailsId(), validPaymentId);
        assertEquals(1, response.getBody().getOrderedGames().size());
    }

}
// package ca.mcgill.ecse321.GameShop.integration;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.sql.Date;
// import java.time.LocalDate;
// import java.util.List;
// import java.util.TimeZone;

// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.MethodOrderer;
// import org.junit.jupiter.api.Order;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestInstance;
// import org.junit.jupiter.api.TestMethodOrder;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.junit.jupiter.api.TestInstance.Lifecycle;
// import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;

// import ca.mcgill.ecse321.GameShop.dto.CustomerOrderRequestDto;
// import ca.mcgill.ecse321.GameShop.dto.CustomerOrderResponseDto;
// import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
// import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
// import ca.mcgill.ecse321.GameShop.model.Game;
// import ca.mcgill.ecse321.GameShop.model.GameCategory;
// import ca.mcgill.ecse321.GameShop.model.OrderGame;
// import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
// import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;
// import ca.mcgill.ecse321.GameShop.repository.CustomerOrderRepository;
// import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;
// import ca.mcgill.ecse321.GameShop.repository.GameRepository;
// import ca.mcgill.ecse321.GameShop.repository.OrderGameRepository;
// import ca.mcgill.ecse321.GameShop.repository.PaymentDetailsRepository;

// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// @TestInstance(Lifecycle.PER_CLASS)
// public class CustomerOrderIntegrationTests {

//     @Autowired
//     private TestRestTemplate client;

//     @Autowired
//     private CustomerAccountRepository customerAccountRepository;

//     @Autowired
//     private PaymentDetailsRepository PaymentDetailsRepository;

//     @Autowired
//     private OrderGameRepository OrderGameRepository;

//     @Autowired
//     private CustomerOrderRepository customerOrderRepository;

//     @Autowired
//     private GameRepository gameRepository;

//     @Autowired
//     private GameCategoryRepository gameCategoryRepository;

//     private static final String VALID_EMAIL = "employee@example.com";
//     private static final String VALID_PASSWORD = "Strong@Pass1";
//     private static final String INVALID_PASSWORD = "wrongpassword";
//     private static final String NON_EXISTENT_EMAIL = "nonexistent@example.com";
//     private static final String VALID_NAME = "John Doe";
//     private static final String VALID_PHONE = "123-456-7890";
//     private static final Boolean ISACTIVE_TRUE = true;
//     private static final Boolean ISACTIVE_FALSE = false;

//     @BeforeAll
//     public static void setupTimezone() {
//         // Ensure the application runs in UTC for consistent date handling
//         TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//     }

//     @AfterEach
//     public void cleanUp() {
//         customerOrderRepository.deleteAll();
//         customerAccountRepository.deleteAll();
//         PaymentDetailsRepository.deleteAll();
//         OrderGameRepository.deleteAll();
//         gameCategoryRepository.deleteAll();
//         gameRepository.deleteAll();
//     }

//     @BeforeEach
//     public void setup() {
//         customerOrderRepository.deleteAll();
//         customerAccountRepository.deleteAll();
//         PaymentDetailsRepository.deleteAll();
//         OrderGameRepository.deleteAll();
//         gameCategoryRepository.deleteAll();
//         gameRepository.deleteAll();
//     }

//     @Test
//     @Order(1)
//     public void testCreateCustomerOrder() {
//         // Arrange
//         CustomerAccount customer = new CustomerAccount(VALID_EMAIL, VALID_PASSWORD);
//         CustomerAccount customer2 = customerAccountRepository.save(customer);
    
//         Game game1 = new Game();
//         game1.setName("Game1");
//         gameRepository.save(game1);
    
//         // Create PaymentDetails object
//         PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H 1A7", 123456789, 12, 2023, customer);
//         PaymentDetailsRepository.save(paymentDetails);
    
//         // Create CustomerOrder and save it
//         CustomerOrder customerOrder = new CustomerOrder(Date.valueOf("2024-11-11"), customer, paymentDetails);
//         CustomerOrder customerOrder2 = customerOrderRepository.save(customerOrder);
    
//         // Create and save OrderGame object, associating it with CustomerOrder
//         OrderGame orderGame1 = new OrderGame();
//         orderGame1.setGame(game1);
//         orderGame1.setCustomerOrder(customerOrder2);
//         OrderGameRepository.save(orderGame1); // Explicitly save OrderGame
    
//         // Add OrderGame to CustomerOrder's list of games
//         customerOrder2.addOrderedGame(orderGame1);
//         customerOrder2 = customerOrderRepository.save(customerOrder2); // Save CustomerOrder after setting OrderGames
    
//         // Prepare request DTO with the correct OrderGame ID
//         List<Integer> orderedGameIds = List.of(orderGame1.getOrderGameId());
//         CustomerOrderRequestDto requestDto = new CustomerOrderRequestDto(
//             Date.valueOf("2024-11-11"),
//             orderedGameIds,
//             customer2.getCustomerId(),
//             paymentDetails.getPaymentDetailsId()
//         );
    
//         // Act
//         ResponseEntity<CustomerOrderResponseDto> response = client.exchange(
//             "/orders/", 
//             HttpMethod.POST, 
//             new HttpEntity<>(requestDto), 
//             CustomerOrderResponseDto.class
//         );
    
//         // Assert
//         assertNotNull(response);
//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         CustomerOrderResponseDto responseBody = response.getBody();
//         assertNotNull(responseBody);
//         assertEquals(requestDto.getOrderDate().toLocalDate(), responseBody.getOrderDate().toLocalDate());
//         assertEquals(requestDto.getOrderedById(), responseBody.getOrderedBy().getCustomerId());
//         assertEquals(requestDto.getPaymentInformationId(), responseBody.getPaymentInformation().getPaymentDetailsId());
    
//         // Verify that the CustomerOrder has the correct number of OrderGames
//         assertEquals(1, responseBody.getOrderedGames().size());
//         assertTrue(responseBody.getOrderedGames().stream()
//                 .anyMatch(og -> og.getOrderGameId() == orderGame1.getOrderGameId()));
//     }
    

//     /**
//      * @Test
//      *       @Order(1)
//      *       public void testCreateCustomerOrder() {
//      *       // Arrange
//      *       CustomerAccount customer = new CustomerAccount(VALID_EMAIL,
//      *       VALID_PASSWORD);
//      *       customerAccountRepository.save(customer);
//      * 
//      *       Game game1 = new Game();
//      *       game1.setName("Game1");
//      *       gameRepository.save(game1);
//      * 
//      *       // Create the CustomerOrder object
//      *       PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H
//      *       1A7", 123456789, 12, 2023, customer);
//      *       PaymentDetailsRepository.save(paymentDetails);
//      * 
//      *       OrderGame orderGame1 = new OrderGame();
//      *       orderGame1.setGame(game1);
//      * 
//      *       CustomerOrder customerOrder = new
//      *       CustomerOrder(Date.valueOf("2024-11-11"), customer, paymentDetails);
//      *       CustomerOrder customerOrder2 =
//      *       customerOrderRepository.save(customerOrder);
//      * 
//      *       orderGame1.setCustomerOrder(customerOrder2);
//      *       OrderGame orderGame2 = OrderGameRepository.save(orderGame1);
//      * 
//      *       // Ensure CustomerOrder is aware of its OrderGames if it's
//      *       bidirectional
//      *       customerOrder.addOrderedGame(orderGame2);
//      *       customerOrderRepository.save(customerOrder); // Save CustomerOrder
//      *       after setting OrderGames
//      * 
//      *       List<Integer> orderedGameIds = List.of(orderGame2.getOrderGameId());
//      *       CustomerOrderRequestDto requestDto = new
//      *       CustomerOrderRequestDto(Date.valueOf("2024-11-11"), orderedGameIds,
//      *       customer.getCustomerId(), paymentDetails.getPaymentDetailsId());
//      * 
//      *       // Act
//      *       // ResponseEntity<CustomerOrderResponseDto> response =
//      *       // client.postForEntity("/orders/", requestDto,
//      *       CustomerOrderResponseDto.class);
//      *       ResponseEntity<CustomerOrderResponseDto> response =
//      *       client.exchange("/orders/", HttpMethod.POST,
//      *       new HttpEntity<>(requestDto), CustomerOrderResponseDto.class);
//      * 
//      *       // Assert
//      *       assertNotNull(response);
//      *       assertEquals(HttpStatus.OK, response.getStatusCode());
//      *       CustomerOrderResponseDto responseBody = response.getBody();
//      *       assertNotNull(responseBody);
//      *       assertEquals(requestDto.getOrderDate().toLocalDate(),
//      *       responseBody.getOrderDate().toLocalDate());
//      *       assertEquals(requestDto.getOrderedById(),
//      *       responseBody.getOrderedBy().getCustomerId());
//      *       assertEquals(requestDto.getPaymentInformationId(),
//      *       responseBody.getPaymentInformation().getPaymentDetailsId());
//      *       assertEquals(1, responseBody.getOrderedGames().size());
//      *       assertEquals(orderGame1.getOrderGameId(),
//      *       responseBody.getOrderedGames().get(0).getOrderGameId());
//      * 
//      *       }
//      **/

//     /**
//      * @Test
//      *       @Order(1)
//      *       public void testCreateCustomerOrder() {
//      *       // Arrange
//      *       CustomerAccount customer = new CustomerAccount(VALID_EMAIL,
//      *       VALID_PASSWORD);
//      *       customerAccountRepository.save(customer);
//      * 
//      *       Game game1 = new Game();
//      *       game1.setName("Game1");
//      *       gameRepository.save(game1);
//      * 
//      *       // Create PaymentDetails object
//      *       PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H
//      *       1A7", 123456789, 12, 2023, customer);
//      *       PaymentDetailsRepository.save(paymentDetails);
//      * 
//      *       // Create CustomerOrder and save it
//      *       CustomerOrder customerOrder = new
//      *       CustomerOrder(Date.valueOf("2024-11-11"), customer, paymentDetails);
//      *       customerOrderRepository.save(customerOrder);
//      * 
//      *       // Create and associate OrderGame with CustomerOrder
//      *       OrderGame orderGame1 = new OrderGame();
//      *       orderGame1.setGame(game1);
//      *       orderGame1.setCustomerOrder(customerOrder); // Set the CustomerOrder in
//      *       OrderGame
//      *       OrderGameRepository.save(orderGame1);
//      * 
//      *       // Ensure CustomerOrder is aware of its OrderGames if it's
//      *       bidirectional
//      *       customerOrder.addOrderedGame(orderGame1);
//      *       customerOrderRepository.save(customerOrder); // Save CustomerOrder
//      *       after setting OrderGames
//      * 
//      *       List<Integer> orderedGameIds = List.of(orderGame1.getOrderGameId());
//      * 
//      *       CustomerOrderRequestDto requestDto = new CustomerOrderRequestDto(
//      *       Date.valueOf("2024-11-11"), orderedGameIds, customer.getCustomerId(),
//      *       paymentDetails.getPaymentDetailsId());
//      * 
//      *       // Act
//      *       ResponseEntity<CustomerOrderResponseDto> response = client.exchange(
//      *       "/orders/", HttpMethod.POST, new HttpEntity<>(requestDto),
//      *       CustomerOrderResponseDto.class);
//      * 
//      *       // Assert
//      *       assertNotNull(response);
//      *       assertEquals(HttpStatus.OK, response.getStatusCode());
//      *       CustomerOrderResponseDto responseBody = response.getBody();
//      *       assertNotNull(responseBody);
//      *       assertEquals(requestDto.getOrderDate().toLocalDate(),
//      *       responseBody.getOrderDate().toLocalDate());
//      *       assertEquals(requestDto.getOrderedById(),
//      *       responseBody.getOrderedBy().getCustomerId());
//      *       assertEquals(requestDto.getPaymentInformationId(),
//      *       responseBody.getPaymentInformation().getPaymentDetailsId());
//      * 
//      *       // Check that one OrderGame is associated with the CustomerOrder
//      *       assertEquals(1, responseBody.getOrderedGames().size());
//      *       assertEquals(orderGame1.getOrderGameId(),
//      *       responseBody.getOrderedGames().get(0).getOrderGameId());
//      *       }
//      **/

// }
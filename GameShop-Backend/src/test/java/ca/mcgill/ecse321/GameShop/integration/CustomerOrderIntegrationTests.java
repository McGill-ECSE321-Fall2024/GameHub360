package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.mcgill.ecse321.GameShop.dto.CustomerOrderResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ErrorResponseDto;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder.OrderStatus;
import ca.mcgill.ecse321.GameShop.repository.CustomerOrderRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CustomerOrderIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    private CustomerOrder customerOrder;

    @BeforeEach
    public void setUp() {
        customerOrderRepository.deleteAll();

    }

    @AfterEach
    public void cleanUp() {
        customerOrderRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testReturnOrder_Success() {
        // Arrange
        customerOrder = new CustomerOrder();
        customerOrder.setOrderDate(Date.valueOf(LocalDate.now().minusDays(5)));
        customerOrder.setOrderStatus(OrderStatus.DELIVERED);
        customerOrderRepository.save(customerOrder);

        // Act 
        ResponseEntity<CustomerOrderResponseDto> response = client.postForEntity(
                "/orders/" + customerOrder.getOrderId() + "/return", null, CustomerOrderResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CustomerOrderResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(OrderStatus.RETURNED, responseBody.getOrderStatus());
    }

    @Test
    @Order(2)
    public void testReturnOrder_InShipping() {
        // Arrange
        customerOrder = new CustomerOrder();
        customerOrder.setOrderDate(Date.valueOf(LocalDate.now().minusDays(5)));
        customerOrder.setOrderStatus(OrderStatus.SHIPPING);
        customerOrderRepository.save(customerOrder);

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity(
                "/orders/" + customerOrder.getOrderId() + "/return", null, ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Order with ID " + customerOrder.getOrderId() + " is in shipping", errorResponse.getError());
    }

    @Test
    @Order(3)
    public void testReturnOrder_AlreadyReturned() {
        // Arrange
        customerOrder = new CustomerOrder();
        customerOrder.setOrderDate(Date.valueOf(LocalDate.now().minusDays(5)));
        customerOrder.setOrderStatus(OrderStatus.RETURNED);
        customerOrderRepository.save(customerOrder);

        // Act
        ResponseEntity<ErrorResponseDto> response = client.postForEntity(
                "/orders/" + customerOrder.getOrderId() + "/return", null, ErrorResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Order with ID " + customerOrder.getOrderId() + " has already been returned", errorResponse.getError());
    }
}
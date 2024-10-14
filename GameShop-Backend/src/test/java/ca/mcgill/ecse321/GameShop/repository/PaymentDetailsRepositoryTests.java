package ca.mcgill.ecse321.GameShop.repository;

import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PaymentDetailsRepositoryTests {

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        paymentDetailsRepository.deleteAll();
        customerAccountRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testPersistAndLoadPaymentDetails() {
        // Arrange
        CustomerAccount customer = new CustomerAccount("test.customer@gmail.com", "testPassword123");
        customer = customerAccountRepository.save(customer);
        int customerId = customer.getCustomerId();

        String cardName = "John Doe";
        String postalCode = "H2X3Y5";
        int cardNumber = 123456789;
        int expMonth = 12;
        int expYear = 2025;

        PaymentDetails paymentDetails = new PaymentDetails(cardName, postalCode, cardNumber, expMonth, expYear, customer);

        // Act
        paymentDetails = paymentDetailsRepository.save(paymentDetails);
        int paymentDetailsId = paymentDetails.getPaymentDetailsId();
        PaymentDetails paymentDetailsFromDb = paymentDetailsRepository.findById(paymentDetailsId).orElse(null);

        // Assert
        assertNotNull(paymentDetailsFromDb);
        assertEquals(paymentDetailsId, paymentDetailsFromDb.getPaymentDetailsId());
        assertEquals(cardName, paymentDetailsFromDb.getCardName());
        assertEquals(postalCode, paymentDetailsFromDb.getPostalCode());
        assertEquals(cardNumber, paymentDetailsFromDb.getCardNumber());
        assertEquals(expMonth, paymentDetailsFromDb.getExpMonth());
        assertEquals(expYear, paymentDetailsFromDb.getExpYear());
        assertEquals(customerId, paymentDetailsFromDb.getCardOwner().getCustomerId());
        assertTrue(customerAccountRepository.findById(customerId).get().getPaymentCards().contains(paymentDetailsFromDb));
    }
}

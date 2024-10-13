package ca.mcgill.ecse321.GameShop.repository;

import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
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
    public void testPersistAndLoadPaymentDetails() {
        // Create and save a customer account
        CustomerAccount customer = new CustomerAccount("test.customer@gmail.com", "testPassword123");
        customer = customerAccountRepository.save(customer);
        int customerId = customer.getCustomerId();

        // Create and save payment details
        String cardName = "John Doe";
        String postalCode = "H2X3Y5";
        int cardNumber = 123456789;
        int expMonth = 12;
        int expYear = 2025;

        PaymentDetails paymentDetails = new PaymentDetails(cardName, postalCode, cardNumber, expMonth, expYear, customer);
        paymentDetails = paymentDetailsRepository.save(paymentDetails);
        int paymentDetailsId = paymentDetails.getPaymentDetailsId();

        // Read payment details from database
        PaymentDetails paymentDetailsFromDb = paymentDetailsRepository.findById(paymentDetailsId).orElse(null);

        // Assert correct retrieval
        assertNotNull(paymentDetailsFromDb);
        assertEquals(paymentDetailsId, paymentDetailsFromDb.getPaymentDetailsId());
        assertEquals(cardName, paymentDetailsFromDb.getCardName());
        assertEquals(postalCode, paymentDetailsFromDb.getPostalCode());
        assertEquals(cardNumber, paymentDetailsFromDb.getCardNumber());
        assertEquals(expMonth, paymentDetailsFromDb.getExpMonth());
        assertEquals(expYear, paymentDetailsFromDb.getExpYear());
        assertEquals(customerId, paymentDetailsFromDb.getCardOwner().getCustomerId());
    }
}

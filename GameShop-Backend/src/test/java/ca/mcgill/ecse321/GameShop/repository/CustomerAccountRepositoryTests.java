package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.GameShop.model.CustomerAccount;

@SpringBootTest
public class CustomerAccountRepositoryTests {
    @Autowired
    private CustomerAccountRepository repo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
    }

    @Test
    public void testCreateAndRetrieveCustomerAccount() {
        // Arrange
        String email = "email@example.com";
        String password = "password";
        CustomerAccount account = new CustomerAccount(email, password);

        // Act
        account = repo.save(account);
        CustomerAccount accountFromDb = repo.findCustomerAccountByCustomerId(account.getCustomerId());

        // Assert
        assertNotNull(accountFromDb);
        assertEquals(account.getCustomerId(), accountFromDb.getCustomerId());
        assertEquals(email, accountFromDb.getEmail());
        assertEquals(password, accountFromDb.getPassword());
    }
}
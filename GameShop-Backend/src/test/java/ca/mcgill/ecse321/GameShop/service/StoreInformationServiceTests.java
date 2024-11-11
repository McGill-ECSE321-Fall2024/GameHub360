package ca.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.GameShop.dto.StoreInformationRequestDto;
import ca.mcgill.ecse321.GameShop.dto.SalesMetricsDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.StoreInformation;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.CustomerOrderRepository;
import ca.mcgill.ecse321.GameShop.repository.OrderGameRepository;
import ca.mcgill.ecse321.GameShop.repository.StoreInformationRepository;

import java.util.List;

@SpringBootTest
public class StoreInformationServiceTests {

    @Mock
    private StoreInformationRepository storeInformationRepository;

    @Mock
    private CustomerOrderRepository customerOrderRepository;

    @Mock
    private CustomerAccountRepository customerAccountRepository;

    @Mock
    private OrderGameRepository orderGameRepository;

    @InjectMocks
    private StoreInformationService storeInformationService;

    @Test
    public void testCreateStorePolicySuccess() {
        // Arrange
        StoreInformationRequestDto requestDto = new StoreInformationRequestDto(
                "Return policy: No returns after 30 days.");
        when(storeInformationRepository.count()).thenReturn(0L);
        when(storeInformationRepository.save(any(StoreInformation.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        StoreInformation response = storeInformationService.createStorePolicy(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(requestDto.getStorePolicy(), response.getStorePolicy());
        verify(storeInformationRepository, times(1)).save(any(StoreInformation.class));
    }

    @Test
    public void testCreateStorePolicyWhenAlreadyExists() {
        // Arrange
        StoreInformationRequestDto requestDto = new StoreInformationRequestDto("New policy.");
        when(storeInformationRepository.count()).thenReturn(1L);

        // Act
        GameShopException e = assertThrows(GameShopException.class,
                () -> storeInformationService.createStorePolicy(requestDto));

        // Assert
        assertEquals("A store policy already exists. Only one store policy is allowed.", e.getMessage());
        assertEquals(HttpStatus.CONFLICT, e.getStatus());
    }

    @Test
    public void testGetStorePolicySuccess() {
        // Arrange
        StoreInformation storePolicy = new StoreInformation();
        storePolicy.setStorePolicy("Standard policy.");
        when(storeInformationRepository.findFirstByOrderByStoreInfoIdAsc()).thenReturn(storePolicy);

        // Act
        StoreInformation response = storeInformationService.getStorePolicy();

        // Assert
        assertNotNull(response);
        assertEquals(storePolicy.getStorePolicy(), response.getStorePolicy());
        verify(storeInformationRepository, times(1)).findFirstByOrderByStoreInfoIdAsc();
    }

    @Test
    public void testGetStorePolicyNotFound() {
        // Arrange
        when(storeInformationRepository.findFirstByOrderByStoreInfoIdAsc()).thenReturn(null);

        // Act
        GameShopException e = assertThrows(GameShopException.class, () -> storeInformationService.getStorePolicy());

        // Assert
        assertEquals("Store policy not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testUpdateStorePolicySuccess() {
        // Arrange
        StoreInformationRequestDto requestDto = new StoreInformationRequestDto("Updated policy.");
        StoreInformation existingPolicy = new StoreInformation();
        existingPolicy.setStorePolicy("Old policy.");
        when(storeInformationRepository.findFirstByOrderByStoreInfoIdAsc()).thenReturn(existingPolicy);
        when(storeInformationRepository.save(any(StoreInformation.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        StoreInformation response = storeInformationService.updatePolicy(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(requestDto.getStorePolicy(), response.getStorePolicy());
        verify(storeInformationRepository, times(1)).save(existingPolicy);
    }

    @Test
    public void testUpdateStorePolicyNotFound() {
        // Arrange
        StoreInformationRequestDto requestDto = new StoreInformationRequestDto("Updated policy.");
        when(storeInformationRepository.findFirstByOrderByStoreInfoIdAsc()).thenReturn(null);

        // Act
        GameShopException e = assertThrows(GameShopException.class,
                () -> storeInformationService.updatePolicy(requestDto));

        // Assert
        assertEquals("Store policy not found.", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testGetSalesMetrics() {
        // Arrange
        when(customerOrderRepository.count()).thenReturn(10L);
        when(orderGameRepository.count()).thenReturn(50L);
        when(customerAccountRepository.count()).thenReturn(20L);

        // Create the mocked OrderGame objects separately
        OrderGame orderGame1 = createOrderGameWithPrice(29.99);
        OrderGame orderGame2 = createOrderGameWithPrice(49.99);
        OrderGame orderGame3 = createOrderGameWithPrice(19.99);

        // Use the pre-created mocks in the when() call
        when(orderGameRepository.findAll()).thenReturn(List.of(orderGame1, orderGame2, orderGame3));

        // Act
        SalesMetricsDto metrics = storeInformationService.getSalesMetrics();

        // Assert
        assertNotNull(metrics);
        assertEquals(99.97, metrics.getTotalSales(), 0.01);
        assertEquals(10, metrics.getTotalOrders());
        assertEquals(50, metrics.getTotalGamesSold());
        assertEquals(20, metrics.getTotalCustomers());
    }

    // Helper method to create OrderGame with a set price using mocks
    private OrderGame createOrderGameWithPrice(double gamePrice) {
        // Mock Game and set up getPrice()
        Game game = mock(Game.class);
        when(game.getPrice()).thenReturn(gamePrice);

        // Mock OrderGame and set up getGame()
        OrderGame orderGame = mock(OrderGame.class);
        when(orderGame.getGame()).thenReturn(game);

        return orderGame;
    }
}

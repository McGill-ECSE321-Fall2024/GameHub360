package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.OrderRequestDto;
import ca.mcgill.ecse321.GameShop.dto.OrderResponseDto;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Place a new order
     */
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequest) {
        CustomerOrder order = orderService.createOrder(
            orderRequest.getCustomerId(),
            orderRequest.getPaymentId(),
            orderRequest.getGameIds(),
            orderRequest.getOrderDate()
        );
        return ResponseEntity.ok(convertToDto(order));
    }

    /**
     * Get all orders
     */
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<CustomerOrder> orders = orderService.getAllOrders();
        List<OrderResponseDto> orderDtos = orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDtos);
    }

    /**
     * Return an order
     */
    @PostMapping("/{orderId}/return")
    public ResponseEntity<OrderResponseDto> returnOrder(@PathVariable int orderId) {
        CustomerOrder returnedOrder = orderService.returnOrder(orderId);
        return ResponseEntity.ok(convertToDto(returnedOrder));
    }

    /**
     * Get orders by customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByCustomer(@PathVariable int customerId) {
        List<CustomerOrder> orders = orderService.getOrdersByCustomer(customerId);
        List<OrderResponseDto> orderDtos = orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDtos);
    }

    /**
     * Get order by ID
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable int orderId) {
        CustomerOrder order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(convertToDto(order));
    }

    /**
     * Helper method to convert CustomerOrder to OrderResponseDto
     */
    private OrderResponseDto convertToDto(CustomerOrder order) {
        List<OrderResponseDto.OrderGameDto> orderGameDtos = order.getOrderedGames().stream()
                .map(this::convertToOrderGameDto)
                .collect(Collectors.toList());

        return new OrderResponseDto(
                order.getOrderId(),
                order.getOrderStatus(),
                order.getOrderDate(),
                order.getOrderedBy().getCustomerId(),
                order.getOrderedBy().getEmail(),
                order.getPaymentInformation().getPaymentDetailsId(),
                orderGameDtos
        );
    }

    /**
     * Helper method to convert OrderGame to OrderGameDto
     */
    private OrderResponseDto.OrderGameDto convertToOrderGameDto(OrderGame orderGame) {
        return new OrderResponseDto.OrderGameDto(
                orderGame.getOrderGameId(),
                orderGame.getGame().getGameEntityId(),
                orderGame.getGame().getName(),
                orderGame.getGame().getPrice()
        );
    }
}

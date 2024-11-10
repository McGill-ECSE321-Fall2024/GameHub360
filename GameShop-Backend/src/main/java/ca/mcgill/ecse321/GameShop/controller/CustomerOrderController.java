package ca.mcgill.ecse321.GameShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.GameShop.dto.CustomerOrderResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ValidationGroups;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.dto.CustomerOrderRequestDto;

import ca.mcgill.ecse321.GameShop.service.CustomerOrderService;

@RestController
@RequestMapping("/orders")
public class CustomerOrderController {

    @Autowired
    private CustomerOrderService customerOrderService;

    /**
     * Endpoint to create a new customer order.
     *
     * @param customerOrderRequestDto The request object containing the customer order's details.
     * @return A response containing the newly created customer order's details.
     */
    @PostMapping("/")
    public CustomerOrderResponseDto createCustomerOrder(@Validated({ ValidationGroups.Post.class }) @RequestBody CustomerOrderRequestDto customerOrderRequestDto) {
        CustomerOrder customerOrder = customerOrderService.createCustomerOrder(customerOrderRequestDto);
        return new CustomerOrderResponseDto(customerOrder);
    }

    /**
     * endpoint to return an order by its id
     * @param orderId
     * @return CustomerOrderResponseDto
     */
    @PostMapping("/{orderId}/return")
    public CustomerOrderResponseDto returnOrder(@RequestBody Integer orderId) {
        CustomerOrder customerOrder = customerOrderService.returnCustomerOrder(orderId);
        return new CustomerOrderResponseDto(customerOrder);
    }

    /**
     * endpoint to get an order by its id
     * @param orderId
     * @return CustomerOrderResponseDto
     */
    @GetMapping("/orders")
    public CustomerOrderResponseDto getCustomerOrderById(@RequestBody Integer orderId) {
        CustomerOrder customerOrder = customerOrderService.monitorOrderStatuses(orderId);
        return new CustomerOrderResponseDto(customerOrder);
    }

}

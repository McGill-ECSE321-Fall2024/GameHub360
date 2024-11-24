package ca.mcgill.ecse321.GameShop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.GameShop.dto.CustomerOrderListDto;
import ca.mcgill.ecse321.GameShop.dto.CustomerOrderRequestDto;
import ca.mcgill.ecse321.GameShop.dto.CustomerOrderResponseDto;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.service.CustomerOrderService;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/orders")
public class CustomerOrderController {

    @Autowired
    private CustomerOrderService customerOrderService;

    /**
     * Endpoint to create a new order.
     * 
     * @param customerOrderRequestDto
     * @return CustomerOrderResponseDto
     */
    @PostMapping
    public CustomerOrderResponseDto createCustomerOrder(
            @Valid @RequestBody CustomerOrderRequestDto customerOrderRequestDto) {
        CustomerOrder customerOrder = customerOrderService.createCustomerOrder(customerOrderRequestDto);
        return new CustomerOrderResponseDto(customerOrder);
    }

    /**
     * Endpoint to return an order by its id.
     * 
     * @param orderId
     * @return CustomerOrderResponseDto
     */
    @PostMapping("/{orderId}/return")
    public CustomerOrderResponseDto returnOrder(@PathVariable("orderId") Integer orderId) {
        CustomerOrder customerOrder = customerOrderService.returnCustomerOrder(orderId);
        return new CustomerOrderResponseDto(customerOrder);
    }

    /**
     * Endpoint to get an order by its id.
     * 
     * @param orderId
     * @return CustomerOrderResponseDto
     */
    @GetMapping("/{orderId}")
    public CustomerOrderResponseDto getCustomerOrderById(@PathVariable("orderId") Integer orderId) {
        CustomerOrder customerOrder = customerOrderService.getCustomerOrderById(orderId);
        return new CustomerOrderResponseDto(customerOrder);
    }

    /**
     * Endpoint to get all orders.
     * 
     * @return CustomerOrderListDto
     */
    @GetMapping("/")
    public CustomerOrderListDto getAllCustomerOrders() {
        List<CustomerOrderResponseDto> customerOrderDtos = new ArrayList<CustomerOrderResponseDto>();
        for (CustomerOrder customerOrder : customerOrderService.getAllCustomerOrders()) {
            customerOrderDtos.add(new CustomerOrderResponseDto(customerOrder));
        }
        return new CustomerOrderListDto(customerOrderDtos);
    }
}
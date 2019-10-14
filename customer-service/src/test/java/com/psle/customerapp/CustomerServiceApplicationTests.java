package com.psle.customerapp;

import com.psle.customerapp.controller.CustomerController;
import com.psle.customerapp.domain.Customer;
import com.psle.customerapp.exception.CustomerNotFoundException;
import com.psle.customerapp.service.CustomerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerServiceApplicationTests {

    private CustomerController controller;

    @MockBean
    private CustomerService customerService;


    @BeforeEach
    void init() {
        controller = new CustomerController(customerService);
    }

    @Test()
    void verifyCustomerGetAPI() throws CustomerNotFoundException {
        when(customerService.getAllCustomers("")).thenReturn(range(0, 6).mapToObj(Customer::new).collect(toList()));
        List<Customer> customers = controller.getAllCustomers(empty());
        verify(customerService, times(1)).getAllCustomers("");

        assertThat(customers).as("Customer list has elements").hasSizeBetween(0, 6);
    }

    @Disabled
    @Test
    void verifyException() {
        assertThatThrownBy(() -> controller.getAllCustomers(empty())).isInstanceOf(CustomerNotFoundException.class);
    }

}

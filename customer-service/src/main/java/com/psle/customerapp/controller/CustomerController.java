package com.psle.customerapp.controller;

import com.psle.customerapp.domain.Customer;
import com.psle.customerapp.exception.CustomerNotFoundException;
import com.psle.customerapp.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@CrossOrigin
@RestController
public class CustomerController {
    private final CustomerService customerService;


    @GetMapping({"", "/{customerId}"})
    public List<Customer> getAllCustomers(@PathVariable(value = "customerId", required = false) Optional<String> customerId)
            throws CustomerNotFoundException {
        log.info("Searching customer with id {}......", customerId);
        return customerService.getAllCustomers(customerId.orElse(""));
    }


    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ok().body(customerService.addNewCustomer(customer));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCustomers(){
        log.info("Deleting records...");
        customerService.deleteCustomers();
        return ok().build();
    }
}

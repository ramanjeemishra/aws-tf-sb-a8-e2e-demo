package com.psle.customerapp.service;


import com.psle.customerapp.domain.Customer;
import com.psle.customerapp.exception.CustomerNotFoundException;

import java.util.List;

public interface CustomerService {
    Customer addNewCustomer(Customer customer);
    List<Customer> getAllCustomers(String clientReferenceId) throws CustomerNotFoundException;

    void deleteCustomers();
}

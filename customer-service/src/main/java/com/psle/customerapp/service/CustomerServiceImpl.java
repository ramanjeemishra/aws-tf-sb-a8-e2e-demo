package com.psle.customerapp.service;

import com.psle.customerapp.domain.Customer;
import com.psle.customerapp.entity.AddressEntity;
import com.psle.customerapp.entity.CustomerEntity;
import com.psle.customerapp.exception.CustomerNotFoundException;
import com.psle.customerapp.mapper.CustomerMapper;
import com.psle.customerapp.repository.CustomerRepository;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper mapper;
    private final CustomerRepository repository;


    @Transactional(propagation = REQUIRES_NEW)
    public Customer addNewCustomer(Customer customer) {
        log.info("---> Mapped Value -- {}", mapper.toEntity(customer));
        return mapper.fromEntity(repository.save(mapper.toEntity(customer)));
    }

    public List<Customer> getAllCustomers(String customerNumber) throws CustomerNotFoundException {
        log.info("Finding customer for id {} ", customerNumber);
        if (isEmpty(customerNumber))
            return repository.findAll().stream().map(mapper::fromEntity).collect(toList());
        else {
            CustomerEntity customerEntity = repository.findByCustomerNumber(customerNumber);
            if (customerEntity == null) throw new CustomerNotFoundException("Customer not found " + customerNumber);
            return of(customerEntity).map(mapper::fromEntity).collect(Collectors.toList());
        }
    }

    @Transactional(propagation = REQUIRES_NEW)
    public void deleteCustomers() {
        log.info("Deleting customers....");
        repository.deleteAllInBatch();
        log.info("Deletion successful....");
    }
}

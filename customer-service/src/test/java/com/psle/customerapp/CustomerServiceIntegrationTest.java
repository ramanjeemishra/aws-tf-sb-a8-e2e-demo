package com.psle.customerapp;

import com.psle.customerapp.domain.Address;
import com.psle.customerapp.domain.Customer;
import com.psle.customerapp.entity.CustomerEntity;
import com.psle.customerapp.exception.CustomerNotFoundException;
import com.psle.customerapp.mapper.CustomerMapper;
import com.psle.customerapp.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static com.psle.customerapp.domain.AddressType.Residential;
import static com.psle.customerapp.domain.Customer.builder;
import static com.psle.customerapp.domain.Gender.Male;
import static com.psle.customerapp.domain.Segment.RETAIL;
import static java.util.Set.of;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerMapper customerMapper;

    @Test
    void
    verifyCustomerDomainSave() throws CustomerNotFoundException {
        customerService.addNewCustomer(generateCustomerData());

        assertThat( customerService.getAllCustomers(""))
                    .as("Find query")
                    .hasSizeGreaterThanOrEqualTo(1);
    }
    
    @Test
    void verifyMapper() {
        CustomerEntity customerEntity = customerMapper.toEntity(generateCustomerData());
        assertThat(customerEntity.getAddresses()).hasSize(1);
        assertThat(customerEntity.getAddresses()).allSatisfy(e -> assertThat(e.getCustomer()).isNotNull());
    }

    private Customer generateCustomerData() {
        return builder()
                .countryOfBirth("IN")
                .customerNumber("TEST123")
                .countryOfResidence("SG")
                .dateOfBirth(LocalDate.now().minusYears(40))
                .firstName("TEST-F")
                .middleName("TEST-M")
                .lastName("TEST-L")
                .gender(Male)
                .segment(RETAIL)
                .mobileNumber("MOBILE--XXXXXXXX")
                .addresses(of(Address
                        .builder()
                        .addressLine1("Line-1")
                        .addressLine2("Line-2")
                        .zipCode("ZIPXXXX")
                        .sequence(1)
                        .addressType(Residential)
                        .countyCode("SG")
                        .build()))
                .build();
    }

}

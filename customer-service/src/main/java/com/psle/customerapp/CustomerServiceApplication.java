package com.psle.customerapp;

import com.psle.customerapp.controller.CustomerController;
import com.psle.customerapp.domain.Address;
import com.psle.customerapp.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Set;

import static com.psle.customerapp.domain.AddressType.Residential;
import static com.psle.customerapp.domain.Gender.Male;
import static com.psle.customerapp.domain.Segment.RETAIL;
import static java.lang.Math.random;
import static java.lang.Math.round;
import static java.time.LocalDate.now;
import static java.util.Set.of;
import static java.util.stream.IntStream.range;
import static org.springframework.boot.SpringApplication.run;

@Slf4j
@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = run(CustomerServiceApplication.class, args);

        CustomerController controller = context.getBean(CustomerController.class);
        cleanUp(controller);
        log.info("Ingesting list of customers records... during service startup....");
        range(1, 50).forEach(i -> {
            Customer customer = generateCustomerData();
            log.info("Inserting customer {}, {}", i, customer);
            ResponseEntity<Customer> entity = controller.createCustomer(customer);
            log.info("Saved Entity id {}", entity.getBody().getId());
        });

    }

    private static void cleanUp(CustomerController controller) {
        log.info("Deleting customers...");
        controller.deleteCustomers();
        log.info("Deleted customers...");
    }


    private static Customer generateCustomerData() {
        return Customer.builder()
                .countryOfBirth("IN")
                .customerNumber("TEST123" + round(random() * 1000))
                .countryOfResidence("SG")
                .dateOfBirth(now().minusYears(round(random() * 39)))
                .firstName("TEST-F" + round(random() * 1000))
                .middleName("TEST-M" + round(random() * 1000))
                .lastName("TEST-L" + round(random() * 1000))
                .gender(Male)
                .segment(RETAIL)
                .mobileNumber("MOBILE--" + round(random() * 1000000))
                .addresses(of(Address
                        .builder()
                        .addressLine1("Line-1" + round(random() * 100))
                        .addressLine2("Line-2" + round(random() * 1000))
                        .zipCode("ZIP" + round(random() * 1000))
                        .sequence(1)
                        .addressType(Residential)
                        .countyCode("SG")
                        .build()))
                .build();
    }



}

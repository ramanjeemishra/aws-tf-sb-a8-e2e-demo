package com.psle.customerapp.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@JsonPropertyOrder(value = {"firstName", "middleName", "lastName", "dateOfBirth", "mobileNumber",
        "gender",
        "customerNumber",
        "countryOfBirth",
        "countryOfResidence",
        "segment",
        "addresses",
        "first-name",
        "addresses"
}, alphabetic = false)
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Customer {

    @JsonIgnore
    private Long id;

    @JsonProperty("first-name")
    private String firstName;

    @JsonProperty("middle-name")
    private String middleName;

    @JsonProperty("last-name")
    private String lastName;

    @JsonProperty("date-of-birth")
    private LocalDate dateOfBirth;

    @JsonProperty("mobile-number")
    private String mobileNumber;

    @JsonProperty("gender")
    private Gender gender;

    @JsonProperty("customer-number")
    private String customerNumber;

    @JsonProperty("country-of-birth")
    private String countryOfBirth;

    @JsonProperty("country-of-residence")
    private String countryOfResidence;

    @JsonProperty("customer-segment")
    private Segment segment;

    @EqualsAndHashCode.Exclude
    @JsonProperty("addresses")
    private Set<Address> addresses;

    public Customer(long id) {
        this.id = id;
    }
}



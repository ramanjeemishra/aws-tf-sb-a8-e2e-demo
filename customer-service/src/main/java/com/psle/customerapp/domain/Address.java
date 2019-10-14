package com.psle.customerapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@JsonPropertyOrder(alphabetic = false)
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Address {

    Long id;

    @JsonIgnore
    private Long customerId;

    @JsonProperty("sequence")
    private Integer sequence;

    @JsonProperty("type")
    private AddressType addressType;

    @JsonProperty("addr-line1")
    private String addressLine1;

    @JsonProperty("addr-line2")
    private String addressLine2;

    @JsonProperty("addr-line3")
    private String addressLine3;

    @JsonProperty("addr-line4")
    private String addressLine4;

    @JsonProperty("country-code")
    private String countyCode;

    @JsonProperty("zipcode")
    private String zipCode;

    @JsonProperty("state")
    private String state;

    @JsonProperty("city")
    private String city;
}

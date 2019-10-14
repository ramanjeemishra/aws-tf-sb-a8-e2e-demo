package com.psle.customerapp.entity;

import com.psle.customerapp.domain.AddressType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Data
@Table(name = "addresses")
@Entity
public class AddressEntity {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(name = "sequence_number")
    private Integer sequence;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private CustomerEntity customer;


    @Enumerated(STRING)
    @Column(name = "addr_type")
    private AddressType addressType;

    @Column(name = "addr_line1")
    private String addressLine1;

    @Column(name = "addr_line2")
    private String addressLine2;

    @Column(name = "addr_line3")
    private String addressLine3;

    @Column(name = "addr_line4")
    private String addressLine4;

    @Column(name = "country_code")
    private String countyCode;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;
}


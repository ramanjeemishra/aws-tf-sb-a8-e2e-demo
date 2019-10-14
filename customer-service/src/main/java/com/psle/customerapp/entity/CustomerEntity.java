package com.psle.customerapp.entity;

import com.psle.customerapp.domain.Gender;
import com.psle.customerapp.domain.Segment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Data
@Table(name = "customers")
@Entity
public class CustomerEntity {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Enumerated(STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "customer_number")
    private String customerNumber;

    @Column(name = "country_of_birth")
    private String countryOfBirth;

    @Column(name = "country_of_residence")
    private String countryOfResidence;

    @Enumerated(STRING)
    @Column(name = "segment")
    private Segment segment;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "customer", cascade = ALL)
    private Set<AddressEntity> addresses;
}



package com.psle.customerapp.mapper;


import com.psle.customerapp.domain.Address;
import com.psle.customerapp.domain.Customer;
import com.psle.customerapp.entity.AddressEntity;
import com.psle.customerapp.entity.CustomerEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface CustomerMapper {
    Customer fromEntity(CustomerEntity entity);

    CustomerEntity toEntity(Customer customer);


    /*Address fromEntity(AddressEntity entity);

    AddressEntity toEntity(Address address);*/

    @AfterMapping
    default void linkAddressToCustomer(@MappingTarget CustomerEntity customerEntity) {
        customerEntity.getAddresses().stream().forEach(addressEntity -> addressEntity.setCustomer(customerEntity));
    }

}

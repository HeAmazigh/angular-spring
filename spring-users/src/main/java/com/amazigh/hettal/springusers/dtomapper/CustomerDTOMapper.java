package com.amazigh.hettal.springusers.dtomapper;

import com.amazigh.hettal.springusers.domain.Customer;
import com.amazigh.hettal.springusers.domain.User;
import com.amazigh.hettal.springusers.dto.RegisterUserDTO;
import com.amazigh.hettal.springusers.dto.UserDTO;
import com.amazigh.hettal.springusers.dto.customer.CustomerDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CustomerDTOMapper {
   public static CustomerDTO fromCustomer(Customer customer) {
       CustomerDTO customerDTO = new CustomerDTO();
       BeanUtils.copyProperties(customer, customerDTO);
       return customerDTO;
   }

   public static Customer fromCustomerDTO(CustomerDTO customerDTO) {
       Customer customer = new Customer();
       BeanUtils.copyProperties(customerDTO, customer);
       return customer;
   }
}

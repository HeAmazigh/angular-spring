package com.amazigh.hettal.springusers.services;

import com.amazigh.hettal.springusers.domain.Customer;
import com.amazigh.hettal.springusers.domain.User;
import com.amazigh.hettal.springusers.dto.SaveUserDto;
import com.amazigh.hettal.springusers.dto.UserDTO;
import com.amazigh.hettal.springusers.dto.customer.CustomerDTO;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    CustomerDTO addNewCustomer(Customer customer);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO getCustomerById(int id);
    void deleteCustomerById(int id);
    CustomerDTO updatedCustomer(Customer customer);

}

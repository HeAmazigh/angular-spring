package com.amazigh.hettal.springusers.services.implementation;

import com.amazigh.hettal.springusers.domain.Customer;
import com.amazigh.hettal.springusers.dto.customer.CustomerDTO;
import com.amazigh.hettal.springusers.dtomapper.CustomerDTOMapper;
import com.amazigh.hettal.springusers.exception.EmailAddressAlreadyExistsException;
import com.amazigh.hettal.springusers.exception.UserNotFoundException;
import com.amazigh.hettal.springusers.repository.CustomerRepository;
import com.amazigh.hettal.springusers.services.CustomerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(CustomerDTOMapper::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(int id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            throw new UserNotFoundException("Customer not found");
        }
        return customer.map(CustomerDTOMapper::fromCustomer).orElse(null);
    }

    @Override
    public CustomerDTO addNewCustomer(Customer customer) {
        Optional<Customer> checkCustomerEmailExist = customerRepository.findByEmail(customer.getEmail());

        if (checkCustomerEmailExist.isPresent())
            throw new EmailAddressAlreadyExistsException("email address already in use");

        customer.setCreatedAt(LocalDateTime.now());
        return CustomerDTOMapper.fromCustomer(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomerById(int id) {
        Optional<Customer> checkCustomerExist = customerRepository.findById(id);
        if (checkCustomerExist.isEmpty())
            throw new UserNotFoundException("Customer not found");

        customerRepository.deleteById(id);
    }

    @Override
    public CustomerDTO updatedCustomer(Customer customer) {
        Optional<Customer> checkCustomerExist = customerRepository.findById(customer.getId());
        if (checkCustomerExist.isEmpty())
            throw new UserNotFoundException("Customer not found");

        return CustomerDTOMapper.fromCustomer(customerRepository.save(customer));
    }
}

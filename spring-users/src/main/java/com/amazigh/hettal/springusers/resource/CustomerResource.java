package com.amazigh.hettal.springusers.resource;

import com.amazigh.hettal.springusers.domain.Customer;
import com.amazigh.hettal.springusers.domain.HttpResponse;
import com.amazigh.hettal.springusers.dto.customer.CustomerDTO;
import com.amazigh.hettal.springusers.exception.UserNotFoundException;
import com.amazigh.hettal.springusers.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerResource {

    private final CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<HttpResponse> loadAllCustomers() {
        List<CustomerDTO> customerDTOS = customerService.getAllCustomers();
        return ResponseEntity.created(location()).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("List customers")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .data(Map.of("customers", customerDTOS))
                        .build()
                );
    }
    @PostMapping
    public ResponseEntity<HttpResponse> addCustomer(@Valid @RequestBody Customer customer) {
        CustomerDTO customerDTO = customerService.addNewCustomer(customer);
        return ResponseEntity.created(location()).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("Customer created successfully")
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.ACCEPTED)
                        .data(Map.of("customer", customerDTO))
                        .build()
        );
    }

    @PutMapping
    public ResponseEntity<HttpResponse> updateCustomer(@Valid @RequestBody Customer customer) {
        CustomerDTO customerDTO = customerService.updatedCustomer(customer);
        return ResponseEntity.created(location()).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("Customer updated successfully")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .data(Map.of("customer", customerDTO))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpResponse> loadCustomerById(@PathVariable int id) {
        Optional<CustomerDTO> customerDTO = Optional.ofNullable(customerService.getCustomerById(id));

        return ResponseEntity.created(location()).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("Customer info")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .data(Map.of("customer", customerDTO))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> deleteCustomerById(@PathVariable int id) {
        Optional<CustomerDTO> customerDTO = Optional.ofNullable(customerService.getCustomerById(id));

        if (customerDTO.isEmpty()) {
            throw new UserNotFoundException("Customer not found");
        }

        customerService.deleteCustomerById(id);

        return ResponseEntity.created(location()).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("Customer deleted successfully")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    private URI location() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/customers").toUriString());
    }
}

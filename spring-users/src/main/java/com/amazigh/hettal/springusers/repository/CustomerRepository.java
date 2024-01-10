package com.amazigh.hettal.springusers.repository;

import com.amazigh.hettal.springusers.domain.Customer;
import com.amazigh.hettal.springusers.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByEmail(String email);
}

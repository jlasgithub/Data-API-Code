package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.domain.Customer;

public interface CustomersRepository extends CrudRepository<Customer, Long> {

}
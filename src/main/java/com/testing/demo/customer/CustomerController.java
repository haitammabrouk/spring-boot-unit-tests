package com.testing.demo.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.testing.demo.customer.exceptions.CustomerAlreadyExistException;
import com.testing.demo.customer.exceptions.CustomerDoesntExistException;

@RestController
public class CustomerController {
    
    @Autowired
    private CustomerService service;

    @GetMapping(value = "/get/customers")
    public ResponseEntity<List<Customer>> getCustomers() {
        List<Customer> customers = service.getCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @PostMapping(value = "/customers")
    public ResponseEntity<String> createCustomer (@RequestBody CustomerRequest customerRequest) throws CustomerAlreadyExistException {
        service.createCustomer(customerRequest);
        return new ResponseEntity<>("customer created successfully", HttpStatus.CREATED);
    }

    @PutMapping("/update/customer")
    public ResponseEntity<String> updateCustomer(@RequestBody CustomerRequest customerRequest) throws CustomerDoesntExistException {
        service.updateCustomer(customerRequest);
        return new ResponseEntity<>("customer updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") long id) throws CustomerDoesntExistException {
        service.deleteCustomer(id);
        return new ResponseEntity<>("customer has been deleted successfully", HttpStatus.OK);
    }

}

package com.testing.demo.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.testing.demo.customer.exceptions.CustomerAlreadyExistException;
import com.testing.demo.customer.exceptions.CustomerDoesntExistException;

@Service
public class CustomerService {
    
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository customerRepository) {
        repository = customerRepository;
    }

    public List<Customer> getCustomers() {
        return repository.findAll();
    }

    public void createCustomer(CustomerRequest customerRequest) throws CustomerAlreadyExistException {
        Optional<Customer> optional_customer = repository.findByEmail(customerRequest.getEmail());
        if(optional_customer.isPresent()) {
            throw new CustomerAlreadyExistException("this email " + customerRequest.getEmail() + " is invalid");
        }

        Customer customer = 
        new Customer(customerRequest.getId(), customerRequest.getName(), customerRequest.getEmail());
        repository.save(customer);
    }

    public void updateCustomer(CustomerRequest customerRequest) throws CustomerDoesntExistException {
        long id = customerRequest.getId();
        Optional<Customer> optional_customer = repository.findById(id);

        if(optional_customer.isEmpty()) {
            throw new CustomerDoesntExistException("the customer with the given id doesn't exist");
        }

        Customer customer = optional_customer.get();
        customer.setEmail(customerRequest.getEmail());
        customer.setName(customerRequest.getName());
        repository.save(customer);
    }

    public void deleteCustomer(long id) throws CustomerDoesntExistException {
        boolean isExist = repository.existsById(id);
        if(!isExist) {
            throw new CustomerDoesntExistException("the customer with the given id doesn't exist");
        }
        repository.deleteById(id);
    }
}

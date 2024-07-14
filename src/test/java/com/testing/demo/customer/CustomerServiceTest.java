package com.testing.demo.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.testing.demo.customer.exceptions.CustomerAlreadyExistException;
import com.testing.demo.customer.exceptions.CustomerDoesntExistException;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    CustomerService underTest;

    @Mock
    CustomerRepository customerRepository;

    @Captor
    ArgumentCaptor<Customer> customerCaptorValue;
    
    @Test
    void shouldGetAllCustomers() {
        //when
        underTest.getCustomers();
        //then
        verify(customerRepository).findAll();
    }

    @Test
    void checkCreatedCustomer() throws CustomerAlreadyExistException {
        CustomerRequest customerRequest = 
        new CustomerRequest(1L, "haitam", "haitammk0708@gmail.com");
        //when
        underTest.createCustomer(customerRequest);
        //then
        verify(customerRepository).save(customerCaptorValue.capture());
        Customer customerCaptured = customerCaptorValue.getValue();

        assertThat(customerCaptured.getEmail()).isEqualTo(customerRequest.getEmail());
        assertThat(customerCaptured.getName()).isEqualTo(customerRequest.getName());
        assertThat(customerCaptured.getId()).isEqualTo(customerRequest.getId());
    }

    @Test
    void checkInCaseCustomerDoesExist() {
        //given
        CustomerRequest customerRequest = 
        new CustomerRequest(1L, "haitam", "haitammk0708@gmail.com");
        //when
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(new Customer()));
        //then
        assertThatThrownBy(() -> underTest.createCustomer(customerRequest))
        .isInstanceOf(CustomerAlreadyExistException.class)
        .hasMessageContaining("this email " + customerRequest.getEmail() + " is invalid");
    }

    @Test
    void checkIfCustomerGetsUpdated() throws CustomerDoesntExistException {
        //given
        CustomerRequest customerRequest =
        new CustomerRequest(1L, "meriem", "meriemfattah4@gmail.com");

        Customer existingCustomer = 
        new Customer(1L, "haitam", "haitammk0708@gmail.com");

        when(customerRepository.findById(customerRequest.getId())).thenReturn(Optional.of(existingCustomer));

        //when
        underTest.updateCustomer(customerRequest);

        //then
        verify(customerRepository).save(existingCustomer);
        assertThat(existingCustomer.getEmail()).isEqualTo("meriemfattah4@gmail.com");
        assertThat(existingCustomer.getName()).isEqualTo("meriem");
    }

    @Test
    void checkIfCustomerDoesntExistWhenUpdate() {
        //given
        CustomerRequest customerRequest =
        new CustomerRequest(1L, "haitam", "haitammk0708@gmail.com");

        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> underTest.updateCustomer(customerRequest))
        .isInstanceOf(CustomerDoesntExistException.class)
        .hasMessageContaining("the customer with the given id doesn't exist");
    }

    @Test
    void checkIfCustomerDoesntExistWhenDelete() {
        //given
        long id = 1L;
        when(customerRepository.existsById(anyLong())).thenReturn(false);
        //then
        assertThatThrownBy(() -> underTest.deleteCustomer(id))
        .isInstanceOf(CustomerDoesntExistException.class)
        .hasMessageContaining("the customer with the given id doesn't exist");
        //ensure that no customer will be deleted for any given id
        verify(customerRepository, never()).deleteById(anyLong());
    }

    @Test
    void shouldDeleteCustomer() throws CustomerDoesntExistException {
        //given
        long id = 1L;
        when(customerRepository.existsById(id)).thenReturn(true);
        //when
        underTest.deleteCustomer(id);
        //then
        verify(customerRepository).deleteById(id);
    }
}

package com.testing.demo.customer;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@WebMvcTest(controllers = CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getEmployees_thenReturnJSONArray() throws Exception {
        // given
        Customer customer = new Customer(1L, "meriem", "meriemfattah4@gmail.com");
        List<Customer> customers = Arrays.asList(customer);

        // when
        given(customerService.getCustomers()).willReturn(customers);

        // then
        mockMvc.perform(get("/get/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(customer.getName())));
    }

    @Test
    void createCustomer_thenReturnCustomerCreated() throws Exception {
        //given
        CustomerRequest customerRequest = new CustomerRequest(1L, "haitam", "haitammk0708@gmail.com");
        doNothing().when(customerService).createCustomer(customerRequest);

        //then
        mockMvc.perform(post("/customers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(customerRequest)))
        .andExpect(status().isCreated())
        .andExpect(content().string("customer created successfully"));
    }

    @Test
    void updateCustomer_thenReturnCustomerUpdated() throws Exception {
        //given
        CustomerRequest customerRequest = new CustomerRequest(1L, "amir", "amirmabrouk@gmail.com");
        doNothing().when(customerService).updateCustomer(customerRequest);
        //then
        mockMvc.perform(put("/update/customer")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(customerRequest)))
        .andExpect(status().isOk())
        .andExpect(content().string("customer updated successfully"));
    }

    @Test
    void deleteCustomer_thenReturnCustomerDeleted() throws Exception {
        //given
        long id = 1L;
        doNothing().when(customerService).deleteCustomer(id);
        //then
        mockMvc.perform(delete("/delete/"+id)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("customer has been deleted successfully"));
    }
}

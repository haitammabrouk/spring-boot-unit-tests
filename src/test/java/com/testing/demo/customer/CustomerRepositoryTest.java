package com.testing.demo.customer;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = 
    new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    
    @Autowired
    CustomerRepository underTest;

    @BeforeEach
    void setUp() {
        //insert
        Customer customer = new Customer(1L, "haitammk", "haitammk0708@gmail.com");
        underTest.save(customer);
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void checkContainerConnection(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    } 

    @Test
    void shoudlReturnCustomerWhenFindByEmail() {
        //when
        Optional<Customer> optional_customer = underTest.findByEmail("haitammk0708@gmail.com");
        //then
        assertThat(optional_customer).isPresent();
    }
}

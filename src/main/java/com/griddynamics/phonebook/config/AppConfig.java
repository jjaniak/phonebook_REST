package com.griddynamics.phonebook.config;

import com.griddynamics.phonebook.model.Contact;
import com.griddynamics.phonebook.repository.InMemoryRepository;
import com.griddynamics.phonebook.repository.InMemoryRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@ComponentScan(value = {"com.griddynamics.phonebook"})
public class AppConfig {

    @Bean
    public Map<String, Contact> defaultData() {
        Map<String, Contact> data = new LinkedHashMap<>();
        data.put("Peter", new Contact("Peter", new HashSet<>(Arrays.asList("+48504550479"))));
        data.put("John", new Contact("John", new HashSet<>(Arrays.asList("+48607233428", "+48799630596", "+48642908355"))));
        data.put("Maria", new Contact("Maria", new HashSet<>(Arrays.asList("+48683456214", "+48770481295"))));
        return data;
    }

    @Bean(name = "repository")
    public InMemoryRepository inMemoryRepository(Map<String, Contact> defaultData) {
        return new InMemoryRepositoryImpl(defaultData);
    }
}
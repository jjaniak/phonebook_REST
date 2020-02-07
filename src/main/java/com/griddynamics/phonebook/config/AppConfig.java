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
    public Set<Contact> defaultData() {
        Set<Contact> data = new LinkedHashSet<>();
        data.add(new Contact("Peter", new HashSet<>(Arrays.asList("+504550479"))));
        data.add(new Contact("John", new HashSet<>(Arrays.asList("+607233428", "+799630596", "+642908355"))));
        return data;
    }

    @Bean(name = "repository")
    public InMemoryRepository inMemoryRepository(Set<Contact> defaultData) {
        return new InMemoryRepositoryImpl(defaultData);
    }
}
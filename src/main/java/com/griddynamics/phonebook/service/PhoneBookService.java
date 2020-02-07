package com.griddynamics.phonebook.service;

import com.griddynamics.phonebook.model.Contact;
import com.griddynamics.phonebook.repository.InMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class PhoneBookService {

    @Autowired
    private InMemoryRepository repository;

    public PhoneBookService() {
    }

    public PhoneBookService(InMemoryRepository repository) {
        this.repository = repository;
    }

    public Set<Contact> findAll() {
        return repository.findAll();
    }

    public Set<String> findAllPhonesByName(String name) {
        return repository.findAllPhonesByName(name);
    }

    public Contact addContact(Contact contact) {
        return repository.addContact(contact);
    }

    public Contact addPhone(String name, String phoneNumber) {
        return repository.addPhone(name, phoneNumber);
    }

    public void removeContact(String name) throws IllegalArgumentException {
        repository.removeContact(name);
    }
}

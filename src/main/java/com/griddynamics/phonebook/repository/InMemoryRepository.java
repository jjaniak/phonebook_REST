package com.griddynamics.phonebook.repository;

import com.griddynamics.phonebook.model.Contact;

import java.util.Map;
import java.util.Set;


public interface InMemoryRepository {

    Map<String, Contact> findAll();

    Set<String> findAllPhonesByName(String name);

    Contact addContact(Contact contact);

    Contact addPhone(String name, String phoneNumber);

    void removeContact(String name) throws IllegalArgumentException;
}
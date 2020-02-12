package com.griddynamics.phonebook.repository;

import com.griddynamics.phonebook.model.Contact;

import java.util.Collection;
import java.util.Set;


public interface InMemoryRepository {

    Collection<Contact> findAll();

    Set<String> findAllPhonesByName(String name);

    Contact addContact(Contact contact);

    Contact addPhone(String name, String phoneNumber);

    void removeContact(String name) throws IllegalArgumentException;
}
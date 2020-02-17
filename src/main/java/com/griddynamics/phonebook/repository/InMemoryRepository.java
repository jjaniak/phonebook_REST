package com.griddynamics.phonebook.repository;

import com.griddynamics.phonebook.model.Contact;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;


public interface InMemoryRepository {

    Map<String, Contact> getData();

    Collection<Contact> findAll();

    Set<String> findAllPhonesByName(String name) throws NoSuchElementException;

    Contact addContact(Contact contact) throws IllegalArgumentException;

    Contact addPhone(String name, String phoneNumber) throws IllegalArgumentException, NoSuchElementException;

    void removeContact(String name) throws NoSuchElementException;
}
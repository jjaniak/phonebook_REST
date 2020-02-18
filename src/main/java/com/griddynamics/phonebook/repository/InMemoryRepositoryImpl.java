package com.griddynamics.phonebook.repository;

import com.griddynamics.phonebook.model.Contact;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public class InMemoryRepositoryImpl implements InMemoryRepository {

    private Map<String, Contact> data;

    public Map<String, Contact> getData() {
        return data;
    }

    public InMemoryRepositoryImpl() {
        this(new LinkedHashMap<>());
    }

    public InMemoryRepositoryImpl(Map<String, Contact> data) {
        this.data = new LinkedHashMap<>(data);
    }

    @Override
    public Collection<Contact> findAll() {
        return this.data.values();
    }

    @Override
    public Set<String> findAllPhonesByName(String name) throws NoSuchElementException {
        Contact contact = this.data.get(name);
        if (null != contact) {
            return contact.getPhoneNumbers();
        }
        throw new NoSuchElementException("There is no contact with name '" + name + "' in the phone book");
    }

    @Override
    public Contact addContact(Contact contact) {
        Contact createdContact = new Contact(contact.getName(), contact.getPhoneNumbers());
        data.put(contact.getName(), createdContact);
        return createdContact;
    }

    @Override
    public Contact addPhone(String name, String phoneNumber) throws IllegalArgumentException, NoSuchElementException {
        if (null == phoneNumber || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("phone number cannot be empty");
        }

        Contact contact = data.get(name);
        if (null != contact) {
            contact.getPhoneNumbers().add(phoneNumber);
            return contact;
        }
        throw new NoSuchElementException("name '" + name + "' is not in the phone book");
    }

    @Override
    public void removeContact(String name) throws NoSuchElementException {
        Contact contact = data.get(name);
        if (null != contact) {
            data.remove(name);
        } else throw new NoSuchElementException("name '" + name + "' is not in the phone book");
    }
}
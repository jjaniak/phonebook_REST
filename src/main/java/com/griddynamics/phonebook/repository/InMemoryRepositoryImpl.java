package com.griddynamics.phonebook.repository;

import com.griddynamics.phonebook.model.Contact;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public class InMemoryRepositoryImpl implements InMemoryRepository {

    private Map<String, Contact> data;

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
    public Contact addContact(Contact contact) throws IllegalArgumentException {
        if (null == contact.getName() || contact.getName().isEmpty()) {
            throw new IllegalArgumentException("Contact need to have a name");
        }
        if (null == contact.getPhoneNumbers() || contact.getPhoneNumbers().isEmpty()) {
            throw new IllegalArgumentException("Contact need to contain at least one phone number");
        }
        Contact createdContact = new Contact(contact.getName(), contact.getPhoneNumbers());
        data.put(contact.getName(), createdContact);
        return createdContact;
    }

    @Override
    public Contact addPhone(String name, String phoneNumber) throws IllegalArgumentException, NoSuchElementException {
        if (null == phoneNumber || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
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
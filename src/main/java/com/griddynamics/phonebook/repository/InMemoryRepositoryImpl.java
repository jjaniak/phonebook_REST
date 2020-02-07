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
    public Map<String, Contact> findAll() {
        return new LinkedHashMap<>(this.data);  // check this later
    }

    @Override
    public Set<String> findAllPhonesByName(String name) {
        Contact contact = this.data.get(name);
        if (null != contact) {
            return contact.getPhoneNumbers();
        }
        throw new IllegalArgumentException("There is no contact with such a name: '" + name + "' in the phone book");
    }

    @Override
    public Contact addContact(Contact contact) {
        if (contact.getName().isEmpty()) {
            throw new IllegalArgumentException("Contact need to have a name");
        }
        if (contact.getPhoneNumbers().isEmpty()) {
            throw new IllegalArgumentException("Contact need to contain at least one phone number");
        }
        Contact createdContact = new Contact(contact.getName(), contact.getPhoneNumbers());
        data.put(contact.getName(), createdContact);
        return createdContact;
    }

    @Override
    public Contact addPhone(String name, String phoneNumber) {
        Contact contact = data.get(name);
        if (null != contact) {
            contact.getPhoneNumbers().add(phoneNumber);
            return contact;
        }
        throw new IllegalArgumentException("The name '" + name + "' is not in the phone book");
    }

    @Override
    public void removeContact(String name) throws IllegalArgumentException {
        Contact contact = data.get(name);
        if (null != contact) {
            data.remove(name);
        } else throw new IllegalArgumentException("The name '" + name + "' is not in the phone book");
    }
}
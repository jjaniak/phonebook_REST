package com.griddynamics.phonebook.repository;

import com.griddynamics.phonebook.model.Contact;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public class InMemoryRepositoryImpl implements InMemoryRepository {

    private Set<Contact> data;

    public InMemoryRepositoryImpl() {
        this(new LinkedHashSet<>());
    }

    public InMemoryRepositoryImpl(Set<Contact> data) {
        this.data = new LinkedHashSet<>(data);
    }

    @Override
    public Set<Contact> findAll() {
        return new LinkedHashSet<>(this.data);  // check this later
    }

    @Override
    public Set<String> findAllPhonesByName(String name) {
        for (Contact c : data) {
            if (c.getName().equals(name)) {
                return c.getPhoneNumbers();
            }
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
        data.add(createdContact);
        return createdContact;
    }

    @Override
    public Contact addPhone(String name, String phoneNumber) {
        for (Contact c : data) {
            if (c.getName().equals(name)) {
                c.getPhoneNumbers().add(phoneNumber);
                return c;
            }
        }
        throw new IllegalArgumentException("The name '" + name + "' is not in the phone book");
    }

    @Override
    public void removeContact(String name) throws IllegalArgumentException {
        Contact contactToBeRemoved = null;
        for (Contact c : data) {
            if (c.getName().equals(name)) {
                contactToBeRemoved = c;
            }
        }
        if (contactToBeRemoved != null) {
            data.remove(contactToBeRemoved);
        } else throw new IllegalArgumentException("The name '" + name + "' is not in the phone book");
    }
}
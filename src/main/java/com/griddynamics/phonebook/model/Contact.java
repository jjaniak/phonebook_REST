package com.griddynamics.phonebook.model;

import java.util.Set;

public class Contact {

    private String name;
    private Set<String> phoneNumbers;

    public Contact() {
    }

    public Contact(String name, Set<String> phoneNumbers) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
    }

    public String getName() {
        return name;
    }

    public Set<String> getPhoneNumbers() {
        return phoneNumbers;
    }
}

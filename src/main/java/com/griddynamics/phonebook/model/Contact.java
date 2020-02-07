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

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Set<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}

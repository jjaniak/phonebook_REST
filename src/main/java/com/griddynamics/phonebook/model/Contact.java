package com.griddynamics.phonebook.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class Contact {

    @NotBlank(message = "Contact need to have a name")
    private String name;

    @NotEmpty(message = "Contact need to contain at least one phone number")
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

package com.griddynamics.phonebook.controller;

import com.griddynamics.phonebook.model.Contact;
import com.griddynamics.phonebook.service.PhoneBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class ContactController {

    @Autowired
    PhoneBookService phoneBookService;

    @GetMapping("/contacts")
    public Set<Contact> showAllContacts() {
        return phoneBookService.findAll();
    }

    @GetMapping("/contacts/{name}")
    public Set<String> showPhoneNumbersForContact(@PathVariable String name) {
        return phoneBookService.findAllPhonesByName(name);
    }

    // returns 201 instead of 200
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/contacts")
    public Contact createContact(@RequestBody Contact contact) {
        return phoneBookService.addContact(contact);
    }

    @PutMapping("/contacts/{name}")
    public Contact addPhoneToContact(@PathVariable String name, @RequestBody String[] phoneNumber) {
        // phoneNumber is passed as an array ["+12345"] to conform with JSON syntax
        return phoneBookService.addPhone(name, phoneNumber[0]);
    }

    @DeleteMapping("/contacts/{name}")
    public void deleteContact(@PathVariable String name) {
        phoneBookService.removeContact(name);     // needs to throw an error if name not found
    }
}
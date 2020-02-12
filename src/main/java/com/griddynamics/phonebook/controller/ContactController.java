package com.griddynamics.phonebook.controller;

import com.griddynamics.phonebook.model.Contact;
import com.griddynamics.phonebook.service.PhoneBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class ContactController {

    @Autowired
    PhoneBookService phoneBookService;

    @GetMapping("/contacts")
    public Collection<Contact> showAllContacts() {
        return phoneBookService.findAll();
    }

    @GetMapping("/contacts/{name}")
    public Set<String> showPhoneNumbersForContact(@PathVariable String name) {
        return phoneBookService.findAllPhonesByName(name);
    }

    @ResponseStatus(HttpStatus.CREATED)  // returns 201 instead of default 200
    @PostMapping("/contacts")
    public Contact createContact(@RequestBody Contact contact) {
        return phoneBookService.addContact(contact);
    }

    @PutMapping("/contacts/{name}")
    // if you don't want to use String array (necessary for JSON syntax) as parameter, e.g. ["+123456"]
    // then you can pass String in @RequestBody and change MediaType as below:
    //    @PutMapping(value = "/contacts/{name}", consumes = MediaType.TEXT_PLAIN_VALUE)
    public Contact addPhoneToContact(@PathVariable String name, @RequestBody String[] phoneNumber) {
        return phoneBookService.addPhone(name, phoneNumber[0]);
    }

    @DeleteMapping("/contacts/{name}")
    public void deleteContact(@PathVariable String name) {
        phoneBookService.removeContact(name);
    }
}
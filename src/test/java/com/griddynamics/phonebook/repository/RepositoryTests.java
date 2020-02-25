package com.griddynamics.phonebook.repository;

import com.griddynamics.phonebook.model.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTests {

    private final String elvisName = "Elvis";
    private final Contact elvisContact = new Contact(elvisName, new HashSet<>(
            Arrays.asList("+128322771")));

    private final String olgaName = "Olga";
    private final Contact olgaContact = new Contact(olgaName, new HashSet<>(
            Arrays.asList("+500489126", "+529731004", "+671992037")));

    private InMemoryRepository repository;

    @BeforeEach
    public void setup() {
        Map<String, Contact> data = new LinkedHashMap<>();
        data.put(elvisName, elvisContact);
        data.put(olgaName, olgaContact);

        repository = new InMemoryRepositoryImpl(data);
    }


    @Test
    @DisplayName("should get all contacts from repository")
    public void shouldGetAllContacts() {
        Set<Contact> expected = Set.of(elvisContact, olgaContact);
        Set<Contact> actual = new HashSet<>(repository.findAll());

        assertEquals(expected, actual, "found contacts do not match");
    }

    @Test
    @DisplayName("should get all phone numbers for requested name")
    public void shouldGetAllPhoneNumbersByName() {
        final Set<String> expectedPhones1 = Set.of("+500489126", "+529731004", "+671992037");
        assertEquals(expectedPhones1, repository.findAllPhonesByName(olgaName),
                "phone numbers do not match");

        final Set<String> expectedPhones2 = Set.of("+128322771");
        assertEquals(expectedPhones2, repository.findAllPhonesByName(elvisName),
                "phone numbers do not match");
    }

    @Test
    @DisplayName("throw exception when getting phone numbers with invalid name")
    public void shouldThrowExceptionWhenGettingPhonesWithInvalidName() {
        String invalidName = "Renfri";
        Exception exception1 = assertThrows(NoSuchElementException.class,
                ()-> repository.findAllPhonesByName(invalidName));

        assertEquals("name '" + invalidName + "' is not in the phone book",
                exception1.getMessage());
    }

    @Test
    @DisplayName("should add new contact")
    public void shouldAddNewContact() {
        String name = "Witcher";
        Set<String> phoneNumbers = Set.of("+48225196900", "+791482321");
        Contact newContact = new Contact(name, phoneNumbers);

        Contact createdContact = repository.addContact(newContact);

        assertEquals(newContact, createdContact);
        assertEquals(newContact, repository.getData().get(name));
        assertEquals(phoneNumbers, repository.findAllPhonesByName(name));
    }

    @Test
    @DisplayName("should add phone number for correct name")
    public void shouldAddPhoneNumberForName() {
        Set<String> elvisPhones = Set.of("+128322771", "+600345913");
        Contact expectedContact = new Contact(elvisName, elvisPhones);
        Contact actualContact = repository.addPhone(elvisName, "+600345913");

        assertEquals(expectedContact, actualContact);
        assertEquals(elvisPhones, repository.getData().get(elvisName).getPhoneNumbers());
    }

    @Nested
    @DisplayName("throw exception when adding")
    public class AddingPhonesExceptionsTests {

        @DisplayName("phone number with invalid name")
        @Test
        public void shouldThrowExceptionWhenAddingPhoneWithInvalidName() {
            String name = "Harry";
            Exception exception = assertThrows(NoSuchElementException.class,
                    ()-> repository.addPhone(name, "+48522189243"));

            assertEquals("name '" + name + "' is not in the phone book", exception.getMessage());
        }

        @DisplayName("invalid phone number")
        @Test
        public void shouldThrowExceptionWhenAddingInvalidPhone() {
            Exception exception1 = assertThrows(IllegalArgumentException.class,
                    ()-> repository.addPhone(elvisName, null));

            assertEquals("phone number cannot be empty", exception1.getMessage());

            Exception exception2 = assertThrows(IllegalArgumentException.class,
                    ()-> repository.addPhone(elvisName, "    "));

            assertEquals("phone number cannot be empty", exception2.getMessage());
        }
    }

    @Test
    @DisplayName("should remove contact")
    public void shouldRemoveContact() {
        repository.removeContact(olgaName);

        assertFalse(repository.getData().containsValue(olgaContact));

        Set<Contact> expected = Set.of(elvisContact);
        Set<Contact> actual = new HashSet<>(repository.getData().values());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("throw exception when removing contact using invalid name")
    public void shouldThrowExceptionWhenRemovingWithInvalidName() {
        String invalidName = "Yoda";
        Exception exception = assertThrows(NoSuchElementException.class,
                ()-> repository.removeContact(invalidName));

        assertEquals("name '" + invalidName + "' is not in the phone book", exception.getMessage());
    }
}
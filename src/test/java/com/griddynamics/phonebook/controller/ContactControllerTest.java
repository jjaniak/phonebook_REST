package com.griddynamics.phonebook.controller;

import com.griddynamics.phonebook.config.AppConfig;
import com.griddynamics.phonebook.model.Contact;
import com.griddynamics.phonebook.service.PhoneBookService;
import com.griddynamics.phonebook.util.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class})
public class ContactControllerTest {

    private static final String URI = "/api/v1/contacts/";
    private MockMvc mockMvc;

    private final String name = "Donald";
    private final Set<String> phoneNumbers = new HashSet<>(Arrays.asList("+1234567", "+4567890"));
    private final String phoneNumbersJson = "[\"+1234567\", \"+4567890\"]";
    private final Contact contact = new Contact(name, phoneNumbers);
    private final String contactJson = "[ { \"name\": \"Donald\", \"phoneNumbers\": [\"+1234567\", \"+4567890\"] } ]";

    @Mock
    private PhoneBookService mockService;

    @InjectMocks    // it injects mockService to the controller
    private ContactController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);  // to initialize fields annotated with Mockito annotations
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void shouldGetEmptyWhenNoContacts() throws Exception {
        String emptyArray = "[]";

        mockMvc.perform(get(URI))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(emptyArray));

        verify(mockService, times(1)).findAll();
    }

    @Test
    public void shouldGetAllContacts() throws Exception {
        when(mockService
                .findAll())
                .thenReturn(Arrays.asList(contact));

        mockMvc.perform(get(URI))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(contactJson));

        verify(mockService, times(1)).findAll();
    }

    @Test
    public void shouldGetPhoneNumbers() throws Exception {
        when(mockService
                .findAllPhonesByName(name))
                .thenReturn(phoneNumbers);

        mockMvc.perform(get(URI + name))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(phoneNumbersJson));

        verify(mockService, times(1)).findAllPhonesByName(name);
    }

    @Test
    public void shouldThrowExceptionWhenGettingPhonesWithInvalidName() throws Exception {
        when(mockService
                .findAllPhonesByName(name))
                .thenThrow(new NoSuchElementException());

        mockMvc.perform(get(URI + name))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(mockService, times(1)).findAllPhonesByName(name);
    }

    @Test
    @Disabled   // todo fix this test
    public void shouldCreateContact() throws Exception {
        when(mockService
                .addContact(contact))
                .thenReturn(contact);

        mockMvc.perform(post(URI).contentType(MediaType.APPLICATION_JSON).content(contactJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(contactJson));

        verify(mockService, times(1)).addContact(contact);
    }

    // todo add tests:
    // checking throwing an error when creating contact with invalid parameters
    // update contact, check throwinng error when updating contact

    @Test
    public void shouldDeleteContact() throws Exception {
        mockMvc.perform(delete(URI + name))
                .andDo(print())
                .andExpect(status().isOk());

        verify(mockService, times(1)).removeContact(name);
    }

    @Test
    public void shouldThrowExceptionWhenDeletingContactWithInvalidName() throws Exception {
        doThrow(new NoSuchElementException()).when(mockService).removeContact(name);

        mockMvc.perform(delete(URI + name))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(mockService, times(1)).removeContact(name);
    }
}
package com.griddynamics.phonebook.controller;

import com.griddynamics.phonebook.config.AppConfig;
import com.griddynamics.phonebook.model.Contact;
import com.griddynamics.phonebook.service.PhoneBookService;
import com.griddynamics.phonebook.util.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class})
public class ContactControllerTest {

    private static final String URI = "/api/v1/contacts/";
    private MockMvc mockMvc;

    private static final String NAME = "Donald";
    private static final String PHONE = "+520487533";
    private static final String JSON_PHONE = "[\"+520487533\"]";
    private static final Set<String> PHONE_NUMBERS = new HashSet<>(Arrays.asList("+1234567", "+4567890"));
    private static final String PHONE_NUMBERS_JSON = "[\"+1234567\", \"+4567890\"]";
    private static final Contact CONTACT = new Contact(NAME, PHONE_NUMBERS);
    private static final String CONTACT_JSON = "{ \"name\": \"Donald\", \"phoneNumbers\": [\"+1234567\", \"+4567890\"] }";

    private static final String EXCEPTION_MESSAGE = "name '" + NAME + "' is not in the phone book";
    private static final String JSON_ERROR_MESSAGE ="{ \"status\": \"404\", \"message\": \"name '"
            + NAME + "' is not in the phone book\" }";
    private static final String EXCEPTION_MESSAGE_2 = "phone number cannot be empty";
    private static final String JSON_ERROR_MESSAGE_2 = "{ \"status\": \"400\", \"message\": \"phone number cannot be empty\" }";

    @Mock
    private PhoneBookService mockService;

    @InjectMocks    // it injects mockService to the controller
    private ContactController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);  // to initialize fields annotated with Mockito annotations

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setValidator(new LocalValidatorFactoryBean())
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void shouldGetEmptyWhenNoContacts() throws Exception {
        String emptyArray = "[]";

        mockMvc.perform(get(URI))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(emptyArray));

        verify(mockService).findAll();
    }

    @Test
    public void shouldGetAllContacts() throws Exception {
        when(mockService
                .findAll())
                .thenReturn(Arrays.asList(CONTACT));

        mockMvc.perform(get(URI))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[" + CONTACT_JSON + "]"));

        verify(mockService).findAll();
    }

    @Test
    public void shouldGetPhoneNumbers() throws Exception {
        when(mockService
                .findAllPhonesByName(NAME))
                .thenReturn(PHONE_NUMBERS);

        mockMvc.perform(get(URI + NAME))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(PHONE_NUMBERS_JSON));

        verify(mockService).findAllPhonesByName(NAME);
    }

    @Test
    public void shouldThrowExceptionWhenGettingPhonesWithInvalidName() throws Exception {
        when(mockService
                .findAllPhonesByName(NAME))
                .thenThrow(new NoSuchElementException(EXCEPTION_MESSAGE));

        mockMvc.perform(get(URI + NAME))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(JSON_ERROR_MESSAGE));

        verify(mockService).findAllPhonesByName(NAME);
    }

    @Test
    public void shouldCreateContact() throws Exception {
        when(mockService
                .addContact(CONTACT))
                .thenReturn(CONTACT);

        mockMvc.perform(post(URI)
                .content(CONTACT_JSON)
                .contentType(APPLICATION_JSON)
                .characterEncoding(UTF_8.name()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(CONTACT_JSON));

        verify(mockService).addContact(CONTACT);
    }

    @Test
    public void ThrowExceptionWhenCreatingContactWithInvalidValues() throws Exception {
        Contact invalidContact = new Contact("", PHONE_NUMBERS);
        String invalidContactJson = "{ \"name\": \"\", \"phoneNumbers\": [\"+1234567\", \"+4567890\"] }";

        mockMvc.perform(post(URI)
                .content(invalidContactJson)
                .contentType(APPLICATION_JSON)
                .characterEncoding(UTF_8.name()))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(mockService, times(0)).addContact(invalidContact);
    }

    @Test
    public void shouldAddPhoneToContact() throws Exception {
        when(mockService
                .addPhone(NAME, PHONE))
                .thenReturn(CONTACT);

        mockMvc.perform(put(URI + NAME)
                .content(JSON_PHONE)
                .contentType(APPLICATION_JSON)
                .characterEncoding(UTF_8.name()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(CONTACT_JSON));

        verify(mockService).addPhone(NAME, PHONE);
    }

    @Test
    public void shouldThrowExceptionWhenAddingPhoneWithInvalidPhoneNumber() throws Exception {
        when(mockService
                .addPhone(NAME, PHONE))
                .thenThrow(new IllegalArgumentException(EXCEPTION_MESSAGE_2));

        mockMvc.perform(put(URI + NAME)
                .content(JSON_PHONE)
                .contentType(APPLICATION_JSON)
                .characterEncoding(UTF_8.name()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(JSON_ERROR_MESSAGE_2));

        verify(mockService).addPhone(NAME, PHONE);
    }

    @Test
    public void shouldThrowExceptionWhenAddingPhoneWithInvalidName() throws Exception {
        when(mockService
                .addPhone(NAME, PHONE))
                .thenThrow(new NoSuchElementException(EXCEPTION_MESSAGE));

        mockMvc.perform(put(URI + NAME)
                .content(JSON_PHONE)
                .contentType(APPLICATION_JSON)
                .characterEncoding(UTF_8.name()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(JSON_ERROR_MESSAGE));

        verify(mockService).addPhone(NAME, PHONE);
    }

    @Test
    public void shouldDeleteContact() throws Exception {
        mockMvc.perform(delete(URI + NAME))
                .andDo(print())
                .andExpect(status().isOk());

        verify(mockService).removeContact(NAME);
    }

    @Test
    public void shouldThrowExceptionWhenDeletingContactWithInvalidName() throws Exception {
        doThrow(new NoSuchElementException(EXCEPTION_MESSAGE)).when(mockService).removeContact(NAME);

        mockMvc.perform(delete(URI + NAME))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(JSON_ERROR_MESSAGE));

        verify(mockService).removeContact(NAME);
    }
}
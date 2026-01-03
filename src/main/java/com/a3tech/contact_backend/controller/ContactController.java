package com.a3tech.contact_backend.controller;

import com.a3tech.contact_backend.entity.Contact;
import com.a3tech.contact_backend.entity.PhoneNumber;
import com.a3tech.contact_backend.repository.ContactRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactRepository contactRepository;

    public ContactController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {

        // Set the relationship properly
        List<PhoneNumber> phoneNumbers = contact.getPhoneNumbers();
        if (phoneNumbers != null) {
            phoneNumbers.forEach(phone -> phone.setContact(contact));
        }

        Contact savedContact = contactRepository.save(contact);
        return new ResponseEntity<>(savedContact, HttpStatus.CREATED);
    }
}
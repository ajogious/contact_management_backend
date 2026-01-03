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

    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        return contactRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(
            @PathVariable Long id,
            @RequestBody Contact updatedContact) {

        return contactRepository.findById(id).map(existingContact -> {

            // Update basic fields
            existingContact.setName(updatedContact.getName());
            existingContact.setEmail(updatedContact.getEmail());

            // Remove old phone numbers
            existingContact.getPhoneNumbers().clear();

            // Add new phone numbers
            if (updatedContact.getPhoneNumbers() != null) {
                updatedContact.getPhoneNumbers().forEach(phone -> {
                    phone.setContact(existingContact);
                    existingContact.getPhoneNumbers().add(phone);
                });
            }

            Contact savedContact = contactRepository.save(existingContact);
            return ResponseEntity.ok(savedContact);

        }).orElse(ResponseEntity.notFound().build());
    }

}
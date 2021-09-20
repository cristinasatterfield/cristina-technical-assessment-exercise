package com.cristinasatterfield.technicalassessment.contact;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contact")
public class ContactController {
  @Autowired
  private ContactRepository contactRepository;

  /**
   * GET method to fetch all contacts or contacts that match the optional name parameter
   *
   * @param name A string used to search for a contact by name (optional)
   * @returns A array of contacts that match the parameter or an empty array
   */
  @GetMapping
  public List<Contact> getAllContacts(@RequestParam(required = false) String name) {
    if (name == null) {
      return contactRepository.findAll();
    }
    return contactRepository.findByNameContainingIgnoreCase(name);
  }


  /**
   * GET method to fetch contact by contactId
   *
   * @param contactId The id of the contact to fetch
   * @returns The requested contact or 404 NOT FOUND
   */
  @GetMapping("/{contactId}")
  public ResponseEntity<Contact> geContactById(@PathVariable Long contactId) {
    final Optional<Contact> contact = contactRepository.findById(contactId);

    if (!contact.isPresent()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(contact.get(), HttpStatus.OK);
  }

  /**
   * POST method to create a contact
   *
   * @param contact The details of the contact to create
   * @returns The created contact or 500 Internal Service Error
   */
  @PostMapping
  public Contact createContact(@Valid @RequestBody Contact contact) {
      return contactRepository.save(contact);
  }

  /**
   * PUT method to update a contacts's details
   *
   * @param contactId The id of the contact to update
   * @param contactDetails The contact fields to update
   * @returns The updated contact or 404 NOT FOUND
   */
  @PutMapping("/{contactId}")
  public ResponseEntity<Contact> updateContact(@PathVariable Long contactId, @Valid @RequestBody Contact contactDetails) {
    final Optional<Contact> contactQuery = contactRepository.findById(contactId);
    if (!contactQuery.isPresent()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    final Contact contact = contactQuery.get();
    contact.setName(contactDetails.getName());
    return new ResponseEntity<>(contactRepository.save(contact), HttpStatus.OK);
  }

    /**
   * DELETE method to delete a contact
   *
   * @param contactId The id of the contact to delete
   */
  @DeleteMapping("/{contactId}")
  public void deleteContact(@PathVariable Long contactId) {
    contactRepository.deleteById(contactId);
  }

}

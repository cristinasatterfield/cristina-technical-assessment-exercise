package com.cristinasatterfield.technicalassessment.contact;

import java.util.List;
import java.util.Optional;

import com.cristinasatterfield.technicalassessment.contact.dto.CreateContactDto;
import com.cristinasatterfield.technicalassessment.contact.dto.UpdateContactDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
  @Autowired
  private ContactRepository contactRepository;

  public List<Contact> getAllContacts(String name) {
    if (name == null) {
      return contactRepository.findAll();
    }
    return contactRepository.findByNameContainingIgnoreCase(name);
  }

  public Optional<Contact> getContactById(Long contactId) {
    return contactRepository.findById(contactId);
  }

  public Contact createContact(CreateContactDto contactDto) {
    Contact contact = new Contact();
    contact.setName(contactDto.getName());
    return contactRepository.save(contact);
  }

  public Contact updateContact(Contact contact, UpdateContactDto contactDto) {
    contact.setName(contactDto.getName());
    return contactRepository.save(contact);
  }

  public void deleteContact(Long contactId) {
    contactRepository.deleteById(contactId);
  }
}

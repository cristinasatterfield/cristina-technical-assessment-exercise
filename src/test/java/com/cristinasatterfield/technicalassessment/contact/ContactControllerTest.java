package com.cristinasatterfield.technicalassessment.contact;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.cristinasatterfield.technicalassessment.contact.dto.CreateContactDto;
import com.cristinasatterfield.technicalassessment.contact.dto.UpdateContactDto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ContactControllerTest {
  @Autowired
  private TestRestTemplate template;

  // This test has to go first because it checks all items in the database table
  @Test
  @Order(1)
  public void testGetAllContacts() {
    List<Contact> contacts = Arrays.asList(
      createContact("A Smith"),
      createContact("B Smith"),
      createContact("C Smith"),
      createContact("D Smith")
    );

    final ResponseEntity<Contact[]> response = template.getForEntity("/api/v1/contact", Contact[].class);
    final List<Contact> foundContacts = Arrays.asList(response.getBody());

    Assertions.assertThat(foundContacts.size()).isEqualTo(contacts.size());

    for (int i = 0; i < contacts.size(); i++) {
      Assertions.assertThat(foundContacts.get(i).getName()).isEqualTo(contacts.get(i).getName());
    }
  }

  @Test
  @Order(2)
  public void testGetContactById() {
    final Contact originalContact = createContact("E Smith");
    final String url = "/api/v1/contact/" + originalContact.getId();
    final ResponseEntity<Contact> response = template.getForEntity(url, Contact.class);
    final Contact contact = response.getBody();

    Assertions.assertThat(contact.getName()).isEqualTo(originalContact.getName());
    Assertions.assertThat(contact.getId()).isEqualTo(originalContact.getId());
    Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
  }

  @Test
  @Order(3)
  public void testGetContactByIdNotFound() {
    final String url = "/api/v1/contact/1000000000";
    final ResponseEntity<Contact> response = template.getForEntity(url, Contact.class);
    final Contact contact = response.getBody();

    Assertions.assertThat(contact).isNull();
    Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
  }

  @Test
  @Order(4)
  public void testCreateContact() {
    final CreateContactDto dto =  new CreateContactDto("F Smith");
    final ResponseEntity<Contact> response = template.postForEntity("/api/v1/contact", dto, Contact.class);
    final Contact contact = response.getBody();

    Assertions.assertThat(contact.getName()).isEqualTo(dto.getName());
    Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
  }

  @Test
  @Order(5)
  public void testCreateContactBadRequest() {
    final List<String> names = Arrays.asList(null, "", " ");

    for (String name : names) {
      final CreateContactDto dto =  new CreateContactDto(name);
      final ResponseEntity<Contact> response = template.postForEntity("/api/v1/contact", dto, Contact.class);

      Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }
  }

  @Test
  @Order(6)
  public void testUpdateContact() {
    final Long originalContactId = createContact("G Doe").getId();
    final UpdateContactDto dto = new UpdateContactDto("G Smith");
    final ResponseEntity<Contact> response = template.exchange("/api/v1/contact/" + originalContactId, HttpMethod.PUT, new HttpEntity<UpdateContactDto>(dto) , Contact.class);
    final Contact contact = response.getBody();

    Assertions.assertThat(contact.getName()).isEqualTo(dto.getName());
    Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
  }

  @Test
  @Order(7)
  public void testUpdateContactBadRequest() {
    final Long originalContactId = createContact("H Doe").getId();

    final List<String> names = Arrays.asList(null, "", " ");

    for (String name : names) {
      final UpdateContactDto dto = new UpdateContactDto(name);
      final ResponseEntity<Contact> response = template.exchange("/api/v1/contact/" + originalContactId, HttpMethod.PUT, new HttpEntity<UpdateContactDto>(dto) , Contact.class);

      Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }
  }

  @Test
  @Order(8)
  public void testSearchContactByName() {
    createContact("Bruce Wayne");
    List<String> searchTerm = Arrays.asList("bru", "Bruce", "Wayne", "Bruce Wayne", "");

    for (int i = 0; i<searchTerm.size(); i++) {
      final String url = "/api/v1/contact?name=" + searchTerm.get(i);
      final ResponseEntity<Contact[]> response = template.getForEntity(url, Contact[].class);
      final List<String> foundContacts = Arrays
        .asList(response.getBody())
        .stream()
        .map(contact -> contact.getName())
        .collect(Collectors.toList());

      Assertions.assertThat(foundContacts.size()).isPositive();
      Assertions.assertThat(foundContacts).contains("Bruce Wayne");
    }
  }

  @Test
  @Order(9)
  public void testDeleteContact() {
    final Long originalContactId = createContact("H Smith").getId();
    final ResponseEntity<Void> response = template.exchange("/api/v1/contact/" + originalContactId, HttpMethod.DELETE, new HttpEntity<>(null) , Void.class);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    final String url = "/api/v1/contact/" + originalContactId;
    final Contact contact = template.getForEntity(url, Contact.class).getBody();

    Assertions.assertThat(contact).isNull();
  }

  @Test
  @Order(10)
  public void testDeleteContactNotFound() {
    final ResponseEntity<Void> response = template.exchange("/api/v1/contact/1000000000", HttpMethod.DELETE, new HttpEntity<>(null) , Void.class);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  private Contact createContact(String name) {
    final CreateContactDto dto =  new CreateContactDto(name);
    final ResponseEntity<Contact> response = template.postForEntity("/api/v1/contact", dto, Contact.class);
    return response.getBody();
  }
}

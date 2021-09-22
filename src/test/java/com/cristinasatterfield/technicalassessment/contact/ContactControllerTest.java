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
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
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
  public void testCreateContact() {
    final CreateContactDto dto =  new CreateContactDto("E Smith");
    final ResponseEntity<Contact> response = template.postForEntity("/api/v1/contact", dto, Contact.class);
    final Contact contact = response.getBody();

    Assertions.assertThat(contact.getName()).isEqualTo(dto.getName());
    Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
  }

  @Test
  @Order(3)
  public void testUpdateContact() {
    final Long originalContactId = createContact("F Doe").getId();
    final UpdateContactDto dto = new UpdateContactDto("F Smith");
    final ResponseEntity<Contact> response = template.exchange("/api/v1/contact/" + originalContactId, HttpMethod.PUT, new HttpEntity<UpdateContactDto>(dto) , Contact.class);
    final Contact contact = response.getBody();

    Assertions.assertThat(contact.getName()).isEqualTo(dto.getName());
    Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
  }

  @Test
  @Order(4)
  public void testGetContactBySearchString() {
    createContact("Bruce Wayne");

    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/api/v1/contact").queryParam("name", "bru");
    final ResponseEntity<Contact[]> response = template.getForEntity(builder.toUriString(), Contact[].class);
    final List<String> foundContacts = Arrays.asList(response.getBody()).stream().map(contact -> contact.getName()).collect(Collectors.toList());

    Assertions.assertThat(foundContacts.size()).isGreaterThanOrEqualTo(1);
    Assertions.assertThat(foundContacts.contains("Bruce Wayne"));
  }

  private Contact createContact(String name) {
    final CreateContactDto dto =  new CreateContactDto(name);
    final ResponseEntity<Contact> response = template.postForEntity("/api/v1/contact", dto, Contact.class);
    return response.getBody();
  }

}

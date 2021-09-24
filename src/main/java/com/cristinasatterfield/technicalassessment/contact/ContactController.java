package com.cristinasatterfield.technicalassessment.contact;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.cristinasatterfield.technicalassessment.contact.dto.CreateContactDto;
import com.cristinasatterfield.technicalassessment.contact.dto.UpdateContactDto;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/contact")
@Tag(name = "Contact API", description = "CRUD API endpoints for managing contacts.")
public class ContactController {
  @Autowired
  private ContactService contactService;

  /**
   * GET method to fetch all contacts or contacts that match the optional name parameter
   *
   * @param name A string used to search for a contact by name (optional)
   * @returns A array of contacts that match the parameter or an empty array
   */
  @GetMapping
  @Operation(
    summary = "Fetch contacts",
    description = "Fetch all contacts if no name is provided. If a name is provided, fetch all contacts that match."
  )
  public List<Contact> getAllContacts(@RequestParam(required = false) String name) {
    return this.contactService.getAllContacts(name);
  }

  /**
   * GET method to fetch contact by contactId
   *
   * @param contactId The id of the contact to fetch
   * @returns The requested contact or 404 NOT FOUND
   */
  @GetMapping("/{contactId}")
  @Operation(summary = "Fetch a contact by Id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully returned the contact",
      content = { @Content(mediaType = "application/json",
        schema = @Schema(implementation = Contact.class)) }),
    @ApiResponse(responseCode = "404", description = "Contact not found",
      content = @Content) })
  public ResponseEntity<Contact> getContactById(@PathVariable Long contactId) {
    final Optional<Contact> contact = this.contactService.getContactById(contactId);

    if (!contact.isPresent()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(contact.get(), HttpStatus.OK);
  }

  /**
   * POST method to create a contact
   *
   * @param contact The details of the contact to create
   * @returns The created contact, 400 if bad request, or 500 Internal Service Error
   */
  @PostMapping
  @Operation(summary = "Create a new contact")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully created and returned the contact",
      content = { @Content(mediaType = "application/json",
        schema = @Schema(implementation = Contact.class)) }),
    @ApiResponse(responseCode = "400", description = "Invalid request; contact not created ",
      content = @Content) })
  public Contact createContact(@Valid @RequestBody CreateContactDto contact) {
      return this.contactService.createContact(contact);
  }

  /**
   * PUT method to update a contacts's details
   *
   * @param contactId The id of the contact to update
   * @param contactDetails The contact fields to update
   * @returns The updated contact or 404 NOT FOUND
   */
  @PutMapping("/{contactId}")
  @Operation(summary = "Update a contact by id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully updated and returned the contact",
      content = { @Content(mediaType = "application/json",
        schema = @Schema(implementation = Contact.class)) }),
    @ApiResponse(responseCode = "400", description = "Invalid id supplied",
      content = @Content),
    @ApiResponse(responseCode = "404", description = "Contact not found",
      content = @Content) })
  public ResponseEntity<Contact> updateContact(@PathVariable Long contactId, @Valid @RequestBody UpdateContactDto contactDto) {
    final Optional<Contact> contactQuery = this.contactService.getContactById (contactId);
    if (!contactQuery.isPresent()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    final Contact contact = this.contactService.updateContact(contactQuery.get(), contactDto);
    return new ResponseEntity<>(contact, HttpStatus.OK);
  }

  /**
   * DELETE method to delete a contact
   *
   * @param contactId The id of the contact to delete
   */
  @DeleteMapping("/{contactId}")
  @Operation(summary = "Delete a contact by id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Successfully deleted the contact",
      content = { @Content(mediaType = "application/json",
        schema = @Schema(implementation = Void.class)) }),
    @ApiResponse(responseCode = "400", description = "Invalid id supplied",
      content = @Content),
    @ApiResponse(responseCode = "404", description = "Contact not found",
      content = @Content) })
  public ResponseEntity<Void> deleteContact(@PathVariable Long contactId) {
    final Optional<Contact> contactQuery = this.contactService.getContactById (contactId);
    if (!contactQuery.isPresent()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
      this.contactService.deleteContact(contactId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}

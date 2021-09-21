package com.cristinasatterfield.technicalassessment.contact.dto;

public class CreateContactDto {
  private String name;

  public CreateContactDto() {

  }

  public CreateContactDto(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

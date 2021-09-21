package com.cristinasatterfield.technicalassessment.contact.dto;

public class UpdateContactDto {
  private String name;

  public UpdateContactDto() {

  }

  public UpdateContactDto(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

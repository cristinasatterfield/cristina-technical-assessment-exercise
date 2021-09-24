package com.cristinasatterfield.technicalassessment.contact.dto;

import javax.validation.constraints.NotBlank;

public class CreateContactDto {
  @NotBlank
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

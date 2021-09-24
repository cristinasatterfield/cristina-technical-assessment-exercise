package com.cristinasatterfield.technicalassessment.contact.dto;

import javax.validation.constraints.NotBlank;

public class UpdateContactDto {
  @NotBlank
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

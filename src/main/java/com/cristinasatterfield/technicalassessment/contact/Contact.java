package com.cristinasatterfield.technicalassessment.contact;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/*
 * This class will represent our contact and its attributes:
 * - ID
 * - Name
 */
@Entity
@Table(name = "contact")
public class Contact {

  /*
   * The attributes of the contact
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String name;

  /*
   * The getters and setters for the attributes above
   */
  public Long getId() {
    return id;
  }

  public void setId(Long value) {
    this.id = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String value) {
    this.name = value;
  }
}

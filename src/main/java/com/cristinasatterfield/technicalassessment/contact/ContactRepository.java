package com.cristinasatterfield.technicalassessment.contact;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
  List<Contact> findByNameContainingIgnoreCase(String name);
}

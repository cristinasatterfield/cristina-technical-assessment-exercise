package com.cristinasatterfield.technicalassessment.contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * The interface Phone repository.
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

}

package com.bryansparactising.addressbook.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bryansparactising.addressbook.domain.entity.ContactEntity;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
    Optional<ContactEntity> findByUuid(String uuid);
    Optional<ContactEntity> findByFirstNameAndLastNameAndMobileNumber(String firstName, String lastName, Long mobileNumber);
}
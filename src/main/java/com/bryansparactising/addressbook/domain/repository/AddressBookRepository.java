package com.bryansparactising.addressbook.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bryansparactising.addressbook.domain.entity.AddressBookEntity;

@Repository
public interface AddressBookRepository extends JpaRepository<AddressBookEntity, Long> {
    Optional<AddressBookEntity> findByUuid(String uuid);
}
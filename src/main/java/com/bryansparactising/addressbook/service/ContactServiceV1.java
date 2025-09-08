package com.bryansparactising.addressbook.service;

import java.util.List;

import com.bryansparactising.addressbook.domain.entity.ContactEntity;
import com.bryansparactising.addressbook.domain.request.ContactDTO;
import com.bryansparactising.addressbook.domain.response.ContactResponse;

public interface ContactServiceV1 {

    ContactResponse create(ContactDTO dto);

    List<ContactResponse> findAll();

    ContactResponse findByUuid(String uuid);

    ContactEntity findEntityByUuid(String uuid);

    ContactResponse update(String uuid, ContactDTO dto);

    void delete(String uuid);
}

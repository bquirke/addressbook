package com.bryansparactising.addressbook.service;

import java.util.List;

import com.bryansparactising.addressbook.domain.request.Contact;
import com.bryansparactising.addressbook.domain.response.ContactResponse;

public interface ContactService {

    ContactResponse create(Contact dto);

    List<ContactResponse> findAll();

    ContactResponse findByUuid(String uuid);

    ContactResponse update(String uuid, Contact dto);

    void delete(String uuid);
}

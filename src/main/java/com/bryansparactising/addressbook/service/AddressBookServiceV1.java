package com.bryansparactising.addressbook.service;

import java.util.List;

import com.bryansparactising.addressbook.domain.request.AddressBookDTO;
import com.bryansparactising.addressbook.domain.request.ContactDTO;
import com.bryansparactising.addressbook.domain.response.AddressBookResponse;

public interface AddressBookServiceV1 {

    // crud functionality
    AddressBookResponse create(AddressBookDTO dto);

    List<AddressBookResponse> findAll();

    AddressBookResponse findByUuid(String uuid);

    AddressBookResponse update(String uuid, AddressBookDTO dto);

    void delete(String uuid);

    // problem domain functionality
    AddressBookResponse addExistingContact(String addressBookUuid, String contactUuid);
    AddressBookResponse addNewContact(String addressBookUuid, List<ContactDTO> contactDto);
    AddressBookResponse removeContact(String addressBookUuid, String contactUuid);
    AddressBookResponse findByContactMobileNumber(String mobileNumber);
    List<AddressBookResponse> findByContactName(String name);


}

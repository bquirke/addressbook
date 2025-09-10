package com.bryansparactising.addressbook.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bryansparactising.addressbook.domain.entity.AddressBookEntity;
import com.bryansparactising.addressbook.domain.entity.ContactEntity;
import com.bryansparactising.addressbook.domain.repository.AddressBookRepository;
import com.bryansparactising.addressbook.domain.request.AddressBookDTO;
import com.bryansparactising.addressbook.domain.request.ContactDTO;
import com.bryansparactising.addressbook.domain.response.AddressBookResponse;
import com.bryansparactising.addressbook.domain.response.ContactResponse;
import com.bryansparactising.addressbook.service.util.PrefixedUuidService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressBookServiceImpl implements AddressBookServiceV1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressBookServiceImpl.class);


    @NonNull
    private final PrefixedUuidService prefixService;

    @NonNull
    private final AddressBookRepository addressBookRepository;

    @NonNull
    private final ContactServiceV1 contactService;


    @Override
    public AddressBookResponse create(AddressBookDTO dto) {
        String uuid = prefixService.generate();
        AddressBookEntity addressBook = AddressBookEntity.builder()
                .uuid(uuid)
                .name(dto.getName())
                .build();

        Optional<AddressBookEntity> existing = addressBookRepository.findByName(addressBook.getName());

        if (existing.isPresent()) {
            LOGGER.error("Address book with name {} already exists", addressBook.getName());
            throw new IllegalArgumentException("Address book with name " + addressBook.getName() + " already exists");
        }

        LOGGER.info("Creating address book with uuid {}", addressBook.getUuid());
        addressBookRepository.save(addressBook);

        return AddressBookResponse.builder()
                .uuid(addressBook.getUuid())
                .name(addressBook.getName())
                .build();
    }


    @Override
    public List<AddressBookResponse> findAll() {
        return addressBookRepository.findAll().stream()
                .map(ab -> AddressBookResponse.builder()
                        .uuid(ab.getUuid())
                        .name(ab.getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public AddressBookResponse findByUuid(String uuid) {
        AddressBookEntity addressBook = addressBookRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Address book with uuid " + uuid + " not found"));

        return AddressBookResponse.builder()
                .uuid(addressBook.getUuid())
                .name(addressBook.getName())
                .build();
    }


    @Override
    public AddressBookResponse update(String uuid, AddressBookDTO dto) {
        Optional<AddressBookEntity> existing = addressBookRepository.findByUuid(uuid);
        AddressBookEntity addressBook;
        if (existing.isEmpty()) {
            LOGGER.info("Address book with uuid {} not found, creating new one", uuid);
            // create new
            addressBook = AddressBookEntity.builder()
                    .uuid(uuid)
                    .name(dto.getName())
                    .build();
            Optional<AddressBookEntity> byName = addressBookRepository.findByName(addressBook.getName());

            if (byName.isPresent()) {
                LOGGER.error("Address book with UUID {} and name {} already exists", uuid, addressBook.getName());
                throw new IllegalArgumentException("Address book with name " + addressBook.getName() + " already exists");
            }

        } else {
            // update existing
            addressBook = existing.get();
            addressBook.setName(dto.getName());
        }

        LOGGER.info("Saving address book with uuid {}", addressBook.getUuid());
        addressBookRepository.save(addressBook);
        
        return AddressBookResponse.builder()
                    .uuid(addressBook.getUuid())
                    .name(addressBook.getName())
                    .build();   
    }


    @Override
    public void delete(String uuid) {
        Optional<AddressBookEntity> existing = addressBookRepository.findByUuid(uuid);
        if (existing.isEmpty()) {
            LOGGER.error("Address book with uuid {} not found, cannot delete", uuid);
            throw new IllegalArgumentException("Address book with uuid " + uuid + " not found");
        }

        LOGGER.info("Deleting address book with uuid {}", uuid);
        addressBookRepository.delete(existing.get());
    }


    @Override
    @Transactional
    public AddressBookResponse addExistingContact(String addressBookUuid, String contactUuid) {
        Optional<AddressBookEntity> existing = addressBookRepository.findByUuid(addressBookUuid);
        if (existing.isEmpty()) {
            LOGGER.error("Address book with uuid {} not found, cannot add contact", addressBookUuid);
            throw new IllegalArgumentException("Address book with uuid " + addressBookUuid + " not found");
        }
        else {        
            AddressBookEntity addressBook = existing.get();
            ContactEntity contact = contactService.findEntityByUuid(contactUuid);

            addressBook.getContacts().add(contact);

            LOGGER.info("Adding contact with uuid {} to address book with uuid {}", contactUuid, addressBookUuid);
            addressBookRepository.save(addressBook);


            return AddressBookResponse.builder()
                    .uuid(addressBook.getUuid())
                    .name(addressBook.getName())
                    .contacts(addressBook.getContacts().stream().map(c -> 
                        ContactResponse.builder()
                            .uuid(c.getUuid())
                            .firstName(c.getFirstName())
                            .lastName(c.getLastName())
                            .mobileNumber(c.getMobileNumber())
                            .build()
                    ).collect(Collectors.toList()))
                    .build();
        }
    }


    @Override
    public AddressBookResponse addNewContact(String addressBookUuid, List<ContactDTO> contactDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addNewContact'");
    }


    @Override
    public AddressBookResponse removeContact(String addressBookUuid, String contactUuid) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeContact'");
    }


    @Override
    public AddressBookResponse findByContactMobileNumber(String mobileNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByContactMobileNumber'");
    }


    @Override
    public List<AddressBookResponse> findByContactName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByContactName'");
    }


}

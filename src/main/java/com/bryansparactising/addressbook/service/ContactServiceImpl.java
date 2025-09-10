package com.bryansparactising.addressbook.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bryansparactising.addressbook.domain.entity.ContactEntity;
import com.bryansparactising.addressbook.domain.repository.ContactRepository;
import com.bryansparactising.addressbook.domain.request.ContactDTO;
import com.bryansparactising.addressbook.domain.response.ContactResponse;
import com.bryansparactising.addressbook.service.util.PrefixedUuidService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactServiceV1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactServiceImpl.class);

    @NonNull
    private final PrefixedUuidService prefixService;

    @NonNull
    private final ContactRepository contactRepository;

    @Override
    // TODO add Transactional
    public ContactResponse create(ContactDTO dto) {
        // TODO add into entity via annotation
        String uuid = prefixService.generate();
        ContactEntity contact = ContactEntity.builder()
                .uuid(uuid)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .mobileNumber(dto.getMobileNumber())
                .build();

        Optional<ContactEntity> existing = contactRepository.findByFirstNameAndLastNameAndMobileNumber(contact.getFirstName(), contact.getLastName(), contact.getMobileNumber());
        if (existing.isPresent()) {
            LOGGER.error("Contact with name {} {} and mobile number {} already exists", contact.getFirstName(), contact.getLastName(), contact.getMobileNumber());
            throw new IllegalArgumentException("Contact with name " + contact.getFirstName() + " " + contact.getLastName() + " and mobile number " + contact.getMobileNumber() + " already exists");
        }

       LOGGER.info("Creating contact with uuid {}", contact.getUuid());
        contactRepository.save(contact);

         // temp mapping 
        return ContactResponse.builder()
                .uuid(contact.getUuid())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .mobileNumber(contact.getMobileNumber())
                .build();
    }

    @Override
    public List<ContactResponse> findAll() {
        return contactRepository.findAll().stream()
                .map(user -> ContactResponse.builder()
                        .uuid(user.getUuid())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .mobileNumber(user.getMobileNumber())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ContactResponse findByUuid(String uuid) {
        ContactEntity user = findEntityByUuid(uuid);

        // todo needs mapping 
        return ContactResponse.builder()
                .uuid(user.getUuid())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .mobileNumber(user.getMobileNumber())
                .build();
    }

    @Override
    public ContactResponse update(String uuid, ContactDTO dto) {
        Optional<ContactEntity> existing = contactRepository.findByUuid(uuid);
        if (existing.isPresent()) {
            LOGGER.info("Contact with uuid {} found, updating", uuid);
            // update entity
            ContactEntity contact = existing.get();

            contact.setFirstName(dto.getFirstName());
            contact.setLastName(dto.getLastName());
            contact.setMobileNumber(dto.getMobileNumber());

            LOGGER.info("Saving contact with uuid {}", contact.getUuid());
            contactRepository.save(contact);


            return ContactResponse.builder()
                    .uuid(contact.getUuid())
                    .firstName(contact.getFirstName())
                    .lastName(contact.getLastName())
                    .mobileNumber(contact.getMobileNumber())
                    .build();
        }
        else {
            LOGGER.info("Contact with uuid {} not found, creating new one", uuid);
            // create it and check existing
            Optional<ContactEntity> newContact = contactRepository.findByFirstNameAndLastNameAndMobileNumber(dto.getFirstName(), dto.getLastName(), dto.getMobileNumber());
            if (newContact.isPresent()) {
                LOGGER.error("Contact with name {} {} and mobile number {} already exists", dto.getFirstName(), dto.getLastName(), dto.getMobileNumber());
                throw new IllegalArgumentException("Contact with name " + dto.getFirstName() + " " + dto.getLastName() + " and mobile number " + dto.getMobileNumber() + " already exists");
            }

            ContactEntity contact = ContactEntity.builder()
                    .uuid(uuid)
                    .firstName(dto.getFirstName())
                    .lastName(dto.getLastName())
                    .mobileNumber(dto.getMobileNumber())
                    .build();

            LOGGER.info("Creating contact with uuid {}", contact.getUuid());
            contactRepository.save(contact);


            return ContactResponse.builder()
                    .uuid(contact.getUuid())
                    .firstName(contact.getFirstName())
                    .lastName(contact.getLastName())
                    .mobileNumber(contact.getMobileNumber())
                    .build();
        }
    }

    @Override
    public void delete(String uuid) {
        Optional<ContactEntity> existing = contactRepository.findByUuid(uuid);
        if (existing.isPresent()) {
            LOGGER.info("Deleting contact with uuid {}", uuid);
            contactRepository.delete(existing.get());
        }
        else {
            LOGGER.error("Contact with uuid {} not found", uuid);
            throw new IllegalArgumentException("Contact with uuid " + uuid + " not found");
        }
    }

    @Override
    public ContactEntity findEntityByUuid(String uuid) {
        return contactRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Contact with uuid " + uuid + " not found"));
    }

}

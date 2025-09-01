package com.bryansparactising.addressbook.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bryansparactising.addressbook.domain.entity.ContactEntity;
import com.bryansparactising.addressbook.domain.repository.ContactRepository;
import com.bryansparactising.addressbook.domain.request.Contact;
import com.bryansparactising.addressbook.domain.response.ContactResponse;
import com.bryansparactising.addressbook.service.util.PrefixedUuidService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    @NonNull
    private final PrefixedUuidService prefixService;

    @NonNull
    private final ContactRepository contactRepository;

    @Override
    // TODO add Transactional
    public ContactResponse create(Contact dto) {
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
            throw new IllegalArgumentException("Contact with name " + contact.getFirstName() + " " + contact.getLastName() + " and mobile number " + contact.getMobileNumber() + " already exists");
        }

       
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
        Optional<ContactEntity> user = contactRepository.findByUuid(uuid);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Contact with uuid " + uuid + " not found");
        }

        // todo needs mapping 
        return ContactResponse.builder()
                .uuid(user.get().getUuid())
                .firstName(user.get().getFirstName())
                .lastName(user.get().getLastName())
                .mobileNumber(user.get().getMobileNumber())
                .build();
    }

    @Override
    public ContactResponse update(String uuid, Contact dto) {
        Optional<ContactEntity> existing = contactRepository.findByUuid(uuid);
        if (existing.isPresent()) {
            // update entity
            ContactEntity contact = existing.get();

            contact.setFirstName(dto.getFirstName());
            contact.setLastName(dto.getLastName());
            contact.setMobileNumber(dto.getMobileNumber());
            contactRepository.save(contact);


            return ContactResponse.builder()
                    .uuid(contact.getUuid())
                    .firstName(contact.getFirstName())
                    .lastName(contact.getLastName())
                    .mobileNumber(contact.getMobileNumber())
                    .build();
        }
        else {
            // create it and check existing
            Optional<ContactEntity> newContact = contactRepository.findByFirstNameAndLastNameAndMobileNumber(dto.getFirstName(), dto.getLastName(), dto.getMobileNumber());
            if (newContact.isPresent()) {
                throw new IllegalArgumentException("Contact with name " + dto.getFirstName() + " " + dto.getLastName() + " and mobile number " + dto.getMobileNumber() + " already exists");
            }

            ContactEntity contact = ContactEntity.builder()
                    .uuid(uuid)
                    .firstName(dto.getFirstName())
                    .lastName(dto.getLastName())
                    .mobileNumber(dto.getMobileNumber())
                    .build();

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
            contactRepository.delete(existing.get());
        }
        else {
            throw new IllegalArgumentException("Contact with uuid " + uuid + " not found");
        }
    }

}

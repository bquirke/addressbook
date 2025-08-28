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
    private final ContactRepository userDao;

    @Override
    public ContactResponse create(Contact dto) {
        String uuid = dto.getUuid() != null ? dto.getUuid() : prefixService.generate();
        ContactEntity user = ContactEntity.builder()
                .uuid(uuid)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .mobileNumber(dto.getMobileNumber())
                .build();
        Optional<ContactEntity> existing = userDao.findByUuid(uuid);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Resource with uuid " + uuid + " already exists");
        }

        // temp mapping 

        userDao.save(user);
        return ContactResponse.builder()
                .uuid(user.getUuid())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .mobileNumber(user.getMobileNumber())
                .build();
    }

    @Override
    public List<ContactResponse> findAll() {
        return userDao.findAll().stream()
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
        Optional<ContactEntity> user = userDao.findByUuid(uuid);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with uuid " + uuid + " not found");
        }
        return ContactResponse.builder()
                .uuid(user.get().getUuid())
                .firstName(user.get().getFirstName())
                .lastName(user.get().getLastName())
                .mobileNumber(user.get().getMobileNumber())
                .build();
    }

    @Override
    public ContactResponse update(String uuid, Contact dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(String uuid) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}

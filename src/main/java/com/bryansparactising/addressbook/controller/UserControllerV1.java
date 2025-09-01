package com.bryansparactising.addressbook.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bryansparactising.addressbook.domain.request.Contact;
import com.bryansparactising.addressbook.domain.response.ContactResponse;
import com.bryansparactising.addressbook.service.ContactService;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
public class UserControllerV1 {


    // TODO should be called v1
    @NonNull
    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactResponse> create(@Valid @RequestBody Contact contact) {
        ContactResponse created = contactService.create(contact);
        return ResponseEntity.created(URI.create("/api/v1/contact/" + created.getUuid())).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ContactResponse>> list() {
        return ResponseEntity.ok(contactService.findAll());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ContactResponse> find(@PathVariable("uuid") String uuid) {
        return ResponseEntity.ok(contactService.findByUuid(uuid));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ContactResponse> update(@PathVariable("uuid") String uuid, @Valid @RequestBody Contact contact) {
        ContactResponse updated = contactService.update(uuid, contact);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") String uuid) {
        contactService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

}

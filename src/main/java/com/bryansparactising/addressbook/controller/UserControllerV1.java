package com.bryansparactising.addressbook.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserControllerV1 {

    @NonNull
    private final ContactService userService;

    @PostMapping
    public ResponseEntity<ContactResponse> create(@Valid @RequestBody Contact dto) {
        ContactResponse created = userService.create(dto);
        return ResponseEntity.created(URI.create("/api/v1/users/" + created.getUuid())).body(created);  
    }

    @GetMapping
    public ResponseEntity<List<ContactResponse>> list() {
        return ResponseEntity.ok(userService.findAll());
    }
}

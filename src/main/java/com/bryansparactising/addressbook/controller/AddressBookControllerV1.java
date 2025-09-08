package com.bryansparactising.addressbook.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bryansparactising.addressbook.domain.request.AddressBookDTO;
import com.bryansparactising.addressbook.domain.response.AddressBookResponse;
import com.bryansparactising.addressbook.service.AddressBookServiceV1;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/addressbook")
@RequiredArgsConstructor
public class AddressBookControllerV1 {


    @NonNull
    private final AddressBookServiceV1 addressBookService;

    // crud functionality

    @PostMapping
    public ResponseEntity<AddressBookResponse> createAddressBook(@RequestBody AddressBookDTO addressBookDTO) {
        return ResponseEntity.ok(addressBookService.create(addressBookDTO));
    }

    @GetMapping
    public ResponseEntity<List<AddressBookResponse>> listAddressBooks() {
        return ResponseEntity.ok(addressBookService.findAll());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<AddressBookResponse> getAddressBookByUuid(@PathVariable("uuid") String uuid) {
        return ResponseEntity.ok(addressBookService.findByUuid(uuid));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<AddressBookResponse> updateAddressBook(@PathVariable("uuid") String uuid, @RequestBody AddressBookDTO addressBookDTO) {
        return ResponseEntity.ok(addressBookService.update(uuid, addressBookDTO));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteAddressBook(@PathVariable("uuid") String uuid) {
        addressBookService.delete(uuid);
        return ResponseEntity.noContent().build();  
    }

    // problem domain functionality
    @PostMapping("/{addressBookUuid}/contact/{contactUuid}")
    public ResponseEntity<AddressBookResponse> addExistingContact(@PathVariable("addressBookUuid") String addressBookUuid, @PathVariable("contactUuid") String contactUuid) {
        return ResponseEntity.ok(addressBookService.addExistingContact(addressBookUuid, contactUuid));
    }

}

package com.bryansparactising.addressbook.domain.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressBookResponse {
    private String uuid;
    private String name;
    private List<ContactResponse> contacts;
}

package com.bryansparactising.addressbook.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactResponse {
    private String uuid;
    private String firstName;
    private String lastName;
    private Long mobileNumber;
}

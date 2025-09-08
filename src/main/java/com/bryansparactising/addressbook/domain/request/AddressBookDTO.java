package com.bryansparactising.addressbook.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressBookDTO {

    private String uuid;

    @NotBlank(message = "Addressbook name is required")
    @Size(max = 100, message = "Addressbook name must be at most 100 characters")
    private String name;

}

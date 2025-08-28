package com.bryansparactising.addressbook.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contact {

    private String uuid;

    @NotBlank(message = "firstName is required")
    @Size(max = 100, message = "firstName must be at most 100 characters")
    private String firstName;

    @NotBlank(message = "lastName is required")
    @Size(max = 100, message = "lastName must be at most 100 characters")
    private String lastName;

    @NotNull(message = "mobileNumber is required")
    @PositiveOrZero(message = "mobileNumber must be zero or positive")
    private Long mobileNumber;
}

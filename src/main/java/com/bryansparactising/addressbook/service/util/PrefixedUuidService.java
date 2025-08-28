package com.bryansparactising.addressbook.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PrefixedUuidService {

    @Value("${app.id.prefix:bq}") // configurable via application.properties
    private String prefix;

    public String generate() {
        return prefix + "-" + UUID.randomUUID();
    }
}

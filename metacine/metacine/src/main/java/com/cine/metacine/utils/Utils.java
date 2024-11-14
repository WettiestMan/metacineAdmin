package com.cine.metacine.utils;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class Utils {
    // no estoy seguro que tan correcto sea esto...
    public static ResponseEntity<Resource> stringToResourceResponse(String msg, MediaType type, HttpStatus code) {
        final var bytes = new ByteArrayResource(msg.getBytes());
        return ResponseEntity.status(code).contentType(type).body(bytes);
    }
}

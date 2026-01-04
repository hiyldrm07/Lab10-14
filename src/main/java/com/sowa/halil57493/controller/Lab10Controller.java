package com.sowa.halil57493.controller;

import com.sowa.halil57493.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/lab10")
public class Lab10Controller {

    @GetMapping("/header")
    public ResponseEntity<String> readHeader(@RequestHeader("X-Lab-Token") String token) {
        return ResponseEntity.ok("Received token: " + token);
    }

    @PostMapping("/json")
    public ResponseEntity<Map<String, String>> processJson(@Valid @RequestBody UserDTO userDTO) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "JSON processed successfully");
        response.put("username", userDTO.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/form", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> processForm(@Valid @ModelAttribute UserDTO userDTO) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Form data processed successfully");
        response.put("username", userDTO.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{code}")
    public ResponseEntity<String> returnStatus(@PathVariable int code) {
        return ResponseEntity.status(code).body("Returned status code: " + code);
    }
}

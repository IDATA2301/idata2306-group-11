package com.roamroute.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roamroute.backend.entity.ContactMessage;
import com.roamroute.backend.repository.ContactMessageRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/contact")
@Tag(name = "Contact", description = "Contact form submissions")
public class ContactMessageController {

  private final ContactMessageRepository contactMessageRepository;

  public ContactMessageController(ContactMessageRepository contactMessageRepository) {
    this.contactMessageRepository = contactMessageRepository;
  }

  @GetMapping
  @Operation(summary = "List all contact form submissions (admin)")
  public List<ContactMessage> getAllContactMessages() {
    return contactMessageRepository.findAll();
  }

  @PostMapping
  @SecurityRequirements
  @Operation(summary = "Submit a contact form message")
  public ContactMessage createContactMessage(@RequestBody ContactMessage contactMessage) {
    return contactMessageRepository.save(contactMessage);
  }

}

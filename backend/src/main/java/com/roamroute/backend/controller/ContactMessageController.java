package com.roamroute.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roamroute.backend.entity.ContactMessage;
import com.roamroute.backend.repository.ContactMessageRepository;
import com.roamroute.backend.service.EmailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/contact")
@Tag(name = "Contact", description = "Contact form submissions")
public class ContactMessageController {

  private static final String SUPPORT_EMAIL = "post@ntnuroamroute.tech";

  private final ContactMessageRepository contactMessageRepository;
  private final EmailService emailService;

  public ContactMessageController(ContactMessageRepository contactMessageRepository, EmailService emailService) {
    this.contactMessageRepository = contactMessageRepository;
    this.emailService = emailService;
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
    ContactMessage saved = contactMessageRepository.save(contactMessage);

    String from = contactMessage.getSenderEmail() != null ? contactMessage.getSenderEmail() : "unknown";
    String subject = "New contact form submission: " + contactMessage.getContactmessage_subject();
    String body = "From: " + from + "\n\n" + contactMessage.getContactmessage_message();
    emailService.sendEmail(SUPPORT_EMAIL, subject, body).subscribe();

    if (contactMessage.getSenderEmail() != null && !contactMessage.getSenderEmail().isBlank()) {
      String confirmationBody = "Hi,\n\nWe've received your message and will get back to you as soon as possible.\n\n" +
          "---\nSubject: " + contactMessage.getContactmessage_subject() + "\n" +
          contactMessage.getContactmessage_message() + "\n---\n\n" +
          "Best regards,\nThe RoamRoute Team";
      emailService.sendEmail(contactMessage.getSenderEmail(), "We received your message", confirmationBody).subscribe();
    }

    return saved;
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a contact message (admin)")
  public ResponseEntity<Void> deleteContactMessage(@PathVariable int id) {
    if (!contactMessageRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    contactMessageRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}

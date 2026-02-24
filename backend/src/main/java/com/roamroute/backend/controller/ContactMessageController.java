package com.roamroute.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roamroute.backend.entity.ContactMessage;
import com.roamroute.backend.repository.ContactMessageRepository;

@RestController
@RequestMapping("/api/contact")
public class ContactMessageController {

  private final ContactMessageRepository contactMessageRepository;

  public ContactMessageController(ContactMessageRepository contactMessageRepository) {
    this.contactMessageRepository = contactMessageRepository;
  }

  @GetMapping
  public List<ContactMessage> getAllContactMessages() {
    return contactMessageRepository.findAll();
  }

  @PostMapping
  public ContactMessage createContactMessage(@RequestBody ContactMessage contactMessage) {
    return contactMessageRepository.save(contactMessage);
  }

}

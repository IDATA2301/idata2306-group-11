package com.roamroute.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roamroute.backend.entity.ContactMessage;

/**
 * Spring Data JPA repository for ContactMessage entity.
 */
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Integer> {

}

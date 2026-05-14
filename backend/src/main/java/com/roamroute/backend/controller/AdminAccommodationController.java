package com.roamroute.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roamroute.backend.dto.AccommodationDTO;
import com.roamroute.backend.dto.AccommodationRequest;
import com.roamroute.backend.service.AccommodationService;

@RestController
@RequestMapping("/api/admin/accommodations")
public class AdminAccommodationController {

  private final AccommodationService accommodationService;

  public AdminAccommodationController(AccommodationService accommodationService) {
    this.accommodationService = accommodationService;
  }

  @GetMapping
  public List<AccommodationDTO> list() {
    return accommodationService.list();
  }

  @GetMapping("/{id}")
  public AccommodationDTO get(@PathVariable int id) {
    return accommodationService.get(id);
  }

  @PostMapping
  public AccommodationDTO create(@RequestBody AccommodationRequest request) {
    return accommodationService.create(request);
  }

  @PutMapping("/{id}")
  public AccommodationDTO update(@PathVariable int id, @RequestBody AccommodationRequest request) {
    return accommodationService.update(id, request);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    accommodationService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

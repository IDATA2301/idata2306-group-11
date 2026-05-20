package com.roamroute.backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.roamroute.backend.service.ImageStorageService;
import com.roamroute.backend.service.ImageStorageService.Category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/uploads")
@Tag(name = "Admin / Uploads", description = "Image uploads for trips and destinations")
public class AdminUploadController {

  private final ImageStorageService imageStorageService;

  public AdminUploadController(ImageStorageService imageStorageService) {
    this.imageStorageService = imageStorageService;
  }

  @PostMapping("/trip-image")
  @Operation(summary = "Upload an image for a trip and return the stored filename")
  public Map<String, String> uploadTripImage(@RequestParam("file") MultipartFile file,
                                             @RequestParam(value = "filename", required = false) String filename) {
    String saved = imageStorageService.store(file, Category.TRIP, filename);
    return Map.of("filename", saved);
  }

  @PostMapping("/destination-image")
  @Operation(summary = "Upload an image for a destination and return the stored filename")
  public Map<String, String> uploadDestinationImage(@RequestParam("file") MultipartFile file,
                                                    @RequestParam(value = "filename", required = false) String filename) {
    String saved = imageStorageService.store(file, Category.DESTINATION, filename);
    return Map.of("filename", saved);
  }
}

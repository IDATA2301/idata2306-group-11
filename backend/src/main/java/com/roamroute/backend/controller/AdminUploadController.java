package com.roamroute.backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.roamroute.backend.service.ImageStorageService;
import com.roamroute.backend.service.ImageStorageService.Category;

@RestController
@RequestMapping("/api/admin/uploads")
public class AdminUploadController {

  private final ImageStorageService imageStorageService;

  public AdminUploadController(ImageStorageService imageStorageService) {
    this.imageStorageService = imageStorageService;
  }

  @PostMapping("/trip-image")
  public Map<String, String> uploadTripImage(@RequestParam("file") MultipartFile file,
                                             @RequestParam(value = "filename", required = false) String filename) {
    String saved = imageStorageService.store(file, Category.TRIP, filename);
    return Map.of("filename", saved);
  }

  @PostMapping("/destination-image")
  public Map<String, String> uploadDestinationImage(@RequestParam("file") MultipartFile file,
                                                    @RequestParam(value = "filename", required = false) String filename) {
    String saved = imageStorageService.store(file, Category.DESTINATION, filename);
    return Map.of("filename", saved);
  }
}

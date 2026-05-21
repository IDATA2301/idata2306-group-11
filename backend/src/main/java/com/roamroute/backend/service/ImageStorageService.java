package com.roamroute.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
/**
 * Service for storing uploaded images on disk with validation, sanitization, and support for multiple image categories.
 */
public class ImageStorageService {

  private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp", "gif");

  public enum Category {
    TRIP("trip"),
    DESTINATION("destination");

    private final String dir;

    Category(String dir) {
      this.dir = dir;
    }

    public String dir() {
      return dir;
    }
  }

  private final Path baseDir;

  public ImageStorageService(@Value("${app.upload.dir}") String baseDir) {
    this.baseDir = Paths.get(baseDir).toAbsolutePath().normalize();
  }

  public String store(MultipartFile file, Category category, String desiredFilename) {
    if (file == null || file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No file provided");
    }

    String uploadedExtension = extractExtension(file.getOriginalFilename());
    if (!uploadedExtension.isEmpty() && !ALLOWED_EXTENSIONS.contains(uploadedExtension)) {
      throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
          "Unsupported file type. Allowed: " + ALLOWED_EXTENSIONS);
    }

    String filename = buildFilename(desiredFilename, uploadedExtension);
    Path targetDir = baseDir.resolve(category.dir()).normalize();
    Path target = targetDir.resolve(filename).normalize();

    if (!target.startsWith(baseDir)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid target path");
    }

    try {
      Files.createDirectories(targetDir);
      try (var input = file.getInputStream()) {
        Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store file", e);
    }

    return filename;
  }

  private String buildFilename(String desiredFilename, String uploadedExtension) {
    String fallbackExtension = uploadedExtension.isEmpty() ? "webp" : uploadedExtension;

    if (desiredFilename == null || desiredFilename.isBlank()) {
      return UUID.randomUUID() + "." + fallbackExtension;
    }

    String sanitized = sanitizeFilename(desiredFilename);
    if (sanitized.isEmpty()) {
      return UUID.randomUUID() + "." + fallbackExtension;
    }

    String base = sanitized;
    String ext = fallbackExtension;
    int dot = sanitized.lastIndexOf('.');
    if (dot > 0 && dot < sanitized.length() - 1) {
      base = sanitized.substring(0, dot);
      String candidate = sanitized.substring(dot + 1).toLowerCase(Locale.ROOT);
      if (ALLOWED_EXTENSIONS.contains(candidate)) {
        ext = candidate;
      }
    }

    if (base.isEmpty()) base = UUID.randomUUID().toString();
    return base + "." + ext;
  }

  private String sanitizeFilename(String input) {
    String trimmed = input.trim();
    // strip path separators and anything weird; keep letters, digits, dash, underscore, dot
    StringBuilder out = new StringBuilder(trimmed.length());
    for (int i = 0; i < trimmed.length(); i++) {
      char c = trimmed.charAt(i);
      if (Character.isLetterOrDigit(c) || c == '-' || c == '_' || c == '.') {
        out.append(c);
      }
    }
    return out.toString();
  }

  private String extractExtension(String originalFilename) {
    if (originalFilename == null) return "";
    int dot = originalFilename.lastIndexOf('.');
    if (dot < 0 || dot == originalFilename.length() - 1) return "";
    return originalFilename.substring(dot + 1).toLowerCase(Locale.ROOT);
  }
}

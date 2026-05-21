package com.roamroute.backend.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC configuration handling static resource exposure for uploaded files.
 *
 * <p>Maps a configured upload directory (from `app.upload.dir`) to a public URL
 * prefix (from `app.upload.url-prefix`) so uploaded images can be served as
 * static resources.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final String uploadDir;
  private final String urlPrefix;

  public WebMvcConfig(@Value("${app.upload.dir}") String uploadDir,
                      @Value("${app.upload.url-prefix}") String urlPrefix) {
    this.uploadDir = uploadDir;
    this.urlPrefix = urlPrefix.endsWith("/") ? urlPrefix : urlPrefix + "/";
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    Path location = Paths.get(uploadDir).toAbsolutePath().normalize();
    String pattern = urlPrefix + "**";
    registry.addResourceHandler(pattern)
        .addResourceLocations(location.toUri().toString());
  }
}

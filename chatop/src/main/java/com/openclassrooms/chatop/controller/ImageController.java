package com.openclassrooms.chatop.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
public class ImageController {

  private final Path uploadDirectory;

  public ImageController(@Value("${app.upload.dir:uploads}") String uploadDir) {
    this.uploadDirectory = Paths.get(uploadDir).toAbsolutePath().normalize();
  }

  @GetMapping("/{filename:.+}")
  public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
    Path imagePath = uploadDirectory.resolve(filename).normalize();
    if (!imagePath.startsWith(uploadDirectory)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    Resource image = new UrlResource(imagePath.toUri());
    if (!image.exists() || !image.isReadable() || !imagePath.toFile().isFile()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image introuvable");
    }

    MediaType mediaType = MediaTypeFactory.getMediaType(image)
      .orElse(MediaType.APPLICATION_OCTET_STREAM);
    return ResponseEntity.ok().contentType(mediaType).body(image);
  }
}

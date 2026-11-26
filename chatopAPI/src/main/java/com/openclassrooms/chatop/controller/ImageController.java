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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
@Tag(name = "Images", description = "Accès aux images des locations")
public class ImageController {

  private final Path uploadDirectory;

  public ImageController(@Value("${app.upload.dir:uploads}") String uploadDir) {
    this.uploadDirectory = Paths.get(uploadDir).toAbsolutePath().normalize();
  }

  /** Retourne une image publique à partir de son nom de fichier. */
  @Operation(summary = "Récupérer une image", description = "Retourne l'image publique identifiée par son nom de fichier.")
  @ApiResponse(responseCode = "200", description = "Image retournée")
  @ApiResponse(responseCode = "404", description = "Image introuvable")
  @ApiResponse(responseCode = "500", description = "Erreur lors de la lecture de l'image")
  @GetMapping("/{filename:.+}")
  public ResponseEntity<Resource> getImage(
    @io.swagger.v3.oas.annotations.Parameter(description = "Nom du fichier image, extension comprise", example = "photo.jpg")
    @PathVariable String filename
  ) throws IOException {
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

package com.communitter.api.controller;

import com.communitter.api.model.Post;
import com.communitter.api.service.ImageService;
import com.communitter.api.service.PostService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/Images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // POST endpoint for uploading a user's profile picture
    @PostMapping("/user/{userId}/profile-picture")
    public ResponseEntity<String> uploadUserProfilePicture(@PathVariable Long userId, @RequestParam("image") MultipartFile file) throws IOException {
        String response = imageService.uploadProfilePicture(userId, file);
        return ResponseEntity.ok(response);
    }

    // POST endpoint for uploading a community's profile picture
    @PostMapping("/community/{communityId}/profile-picture")
    public ResponseEntity<String> uploadCommunityProfilePicture(@PathVariable Long communityId, @RequestParam("image") MultipartFile file) throws IOException {
        String response = imageService.uploadCommunityPicture(communityId, file);
        return ResponseEntity.ok(response);
    }

    // GET endpoint to retrieve a user's profile picture by user ID
    @GetMapping("/user/{userId}/profile-picture")
    public ResponseEntity<byte[]> getUserProfilePicture(@PathVariable Long userId) {
        byte[] imageData = imageService.downloadImageByUserId(userId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageData);
    }

    // GET endpoint to retrieve a community's profile picture by community ID
    @GetMapping("/community/{communityId}/profile-picture")
    public ResponseEntity<byte[]> getCommunityProfilePicture(@PathVariable Long communityId) {
        byte[] imageData = imageService.downloadImageByCommunityId(communityId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageData);
    }
}
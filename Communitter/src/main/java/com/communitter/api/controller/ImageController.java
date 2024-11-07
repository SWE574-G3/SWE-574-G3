package com.communitter.api.controller;

import com.communitter.api.model.Post;
import com.communitter.api.service.ImageService;
import com.communitter.api.service.PostService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.communitter.api.dto.ImageDTO;
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

    
    @GetMapping("/user/{userId}/profile-picture")
    public ResponseEntity<?> downloadUserProfilePicture(@PathVariable Long userId) {
    try {
        ImageDTO imageDataDTO = imageService.getUserProfilePicture(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(imageDataDTO.getMimeType())) // Sets the correct MIME type
                .body(imageDataDTO.getData()); // Returns binary data directly

    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
    }
}


    // @GetMapping("/{fileName}")
	// public ResponseEntity<?> downloadImage(@PathVariable String fileName){
	// 	byte[] imageData=service.downloadImage(fileName);
	// 	return ResponseEntity.status(HttpStatus.OK)
	// 			.contentType(MediaType.valueOf("image/png"))
	// 			.body(imageData);

	// }
    // // POST endpoint for uploading a community's profile picture
    // @PostMapping("/community/{communityId}/profile-picture")
    // public ResponseEntity<String> uploadCommunityProfilePicture(@PathVariable Long communityId, @RequestParam("image") MultipartFile file) throws IOException {
    //     String response = imageService.uploadCommunityPicture(communityId, file);
    //     return ResponseEntity.ok(response);
    // }

}
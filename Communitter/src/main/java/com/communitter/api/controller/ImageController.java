package com.communitter.api.controller;

import com.communitter.api.model.Post;
import com.communitter.api.model.User;
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
import com.communitter.api.dto.request.UserRequest;
import com.communitter.api.service.ImageService;


import java.io.IOException;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/Images")
public class ImageController {

    private final ImageService imageService;


    // POST endpoint for uploading a user's profile picture
    @PreAuthorize("@authorizer.authorizerForUser(#root,#userId)")
    @PostMapping("/user/{userId}/profile-picture")
    public ResponseEntity<String> uploadUserProfilePicture(@P ("userId") @PathVariable Long userId, @RequestParam("image") MultipartFile file) throws IOException {
        String response = imageService.uploadProfilePicture(userId, file);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/user/{userId}/profile-picture")
    public ResponseEntity<String> downloadUserProfilePicture(@PathVariable Long userId) {
        // Retrieve the image data DTO
        ImageDTO imageDataDTO = imageService.getUserProfilePicture(userId);

        // Prepare the response with Base64-encoded data and MIME type
        String base64ImageData = "data:" + imageDataDTO.getMimeType() + ";base64," + imageDataDTO.getBase64Image();

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN) // Content type for Base64 strings
                .body(base64ImageData);
    }


    //______________________________________________________________________________________________________________________
    // POST and GET endpoint for uploading a communities' profile picture
    //______________________________________________________________________________________________________________________
    @PreAuthorize("@authorizer.checkCommunityRole(#root,#communityId)")
    @PostMapping("/community/{communityId}/community-picture")
    public ResponseEntity<String> uploadCommunityPicture(@P ("communityId") @PathVariable Long communityId, @RequestParam("image") MultipartFile file) throws IOException {
        String response = imageService.uploadCommunity_picture(communityId, file);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/community/{community_image_id}/community-picture")
    public ResponseEntity<String> downloadCommunityPicture(@PathVariable Long community_image_id) {
        // Retrieve the image data DTO
        ImageDTO imageDataDTO = imageService.getCommunityImage(community_image_id);

        // Format the Base64 image data as a Data URI
        String base64ImageData = "data:" + imageDataDTO.getMimeType() + ";base64," + imageDataDTO.getBase64Image();

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN) // Content type for Base64 strings
                .body(base64ImageData);
    }



        //______________________________________________________________________________________________________________________
    // DELETE endpoint for deleting a user's and communities' profile picture
    //______________________________________________________________________________________________________________________
    @PreAuthorize("@authorizer.authorizerForUser(#root,#userId)")
    @DeleteMapping("/user/{userId}/profile-picture")
    public ResponseEntity<String> deleteUserProfilePicture(@PathVariable Long userId) {
        String response = imageService.deleteProfilePicture(userId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@authorizer.checkCommunityRole(#root,#communityId)")
    @DeleteMapping("/community/{communityId}/community-picture")
    public ResponseEntity<String> deleteCommunityProfilePicture(@PathVariable Long communityId) {
        String response = imageService.deleteCommunityPicture(communityId);
        return ResponseEntity.ok(response);
    }
}

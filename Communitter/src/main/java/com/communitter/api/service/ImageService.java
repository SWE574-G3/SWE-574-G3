package com.communitter.api.service;

import com.communitter.api.model.Community;
import com.communitter.api.model.ImageData;
import com.communitter.api.repository.CommunityRepository;
import com.communitter.api.repository.ImageRepository;
import com.communitter.api.repository.UserRepository;
import com.communitter.api.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;
import com.communitter.api.model.User;
import com.communitter.api.dto.*;


@Service
public class ImageService {

    private  ImageRepository imagerepository;
    private  UserRepository userRepository;
    private  CommunityRepository communityRepository;

    public ImageService(ImageRepository imagerepository, UserRepository userRepository, CommunityRepository communityRepository) {
        this.imagerepository = imagerepository;
        this.userRepository = userRepository;
        this.communityRepository = communityRepository;
    }


    public String uploadProfilePicture(Long userId, MultipartFile file) throws IOException {
        // Retrieve user or throw an error if user not found
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    
        // Get original file name and MIME type
        String originalFileName = file.getOriginalFilename();
        String mimeType = file.getContentType();
    
        // Validate MIME type to ensure it's an image (optional but recommended)
        if (mimeType == null || !mimeType.startsWith("image/")) {
            throw new IllegalArgumentException("File is not a valid image type");
        }
    
        // Extract file extension
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
    
        // Check if the user already has a profile picture and delete it if exists
        ImageData existingImage = imagerepository.findByUserId(userId);
        if (existingImage != null) {
            imagerepository.delete(existingImage);
        }
    
        // Generate a unique file name with the user ID
        String uniqueFileName = "user_" + userId + fileExtension;
    
        // Compress and save the new image data
        ImageData imageData = imagerepository.save(
                ImageData.builder()
                        .name(uniqueFileName)
                        .type(mimeType) // Save MIME type to dynamically serve it later
                        .data_image(ImageUtils.compressImage(file.getBytes()))
                        .user(user)
                        .build()
        );
    
        // Link the new profile image to the user
        user.setProfileImage(imageData);
        userRepository.save(user);
    
        return "Profile picture uploaded successfully: " + originalFileName;
    }

    

    public ImageDTO getUserProfilePicture(Long userId) {
        // Retrieve the user and handle if not found
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    
        // Retrieve ImageData for the given userId
        ImageData imageData = imagerepository.findByUserId(userId);
        if (imageData == null || imageData.getData_image() == null) {
            throw new RuntimeException("Profile picture not found for user");
        }
    
        // Decompress the image data
        byte[] decompressedImageData = ImageUtils.decompressImage(imageData.getData_image());
    
        // Return a DTO with decompressed image data and MIME type
        return ImageDTO.builder()
                .data(decompressedImageData)
                .mimeType(imageData.getType()) // MIME type stored in the database (e.g., "image/png")
                .build();
    }
    
}
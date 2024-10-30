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

@Service
public class ImageService {

    private  ImageRepository repository;
    private  UserRepository userRepository;
    private  CommunityRepository communityRepository;

    public ImageService(ImageRepository repository, UserRepository userRepository, CommunityRepository communityRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.communityRepository = communityRepository;
    }

    public String uploadProfilePicture(Long userId, MultipartFile file) throws IOException {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ImageData imageData = repository.save(
                    ImageData.builder()
                            .name(file.getOriginalFilename())
                            .type(file.getContentType())
                            .imageData(ImageUtils.compressImage(file.getBytes()))
                            .user(user)
                            .build());

            user.setProfileImage(imageData);
            userRepository.save(user);

            return "Profile picture uploaded successfully: " + file.getOriginalFilename();
    }


    public String uploadCommunityPicture(Long communityId, MultipartFile file) throws IOException {
        // Fetch the community by ID
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));

        // Save the image data
        ImageData imageData = repository.save(
                ImageData.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .imageData(ImageUtils.compressImage(file.getBytes()))
                        .community(community)  // Associate the image with the community
                        .build());

        // Set the image as the community's profile picture
        community.setCommunityImage(imageData);
        communityRepository.save(community);  // Update the community with the new profile picture

        return "Community profile picture uploaded successfully: " + file.getOriginalFilename();
    }

public byte[] downloadImageByUserId(Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    ImageData imageData = user.getProfileImage();
    return ImageUtils.decompressImage(imageData.getImageData());
}

public byte[] downloadImageByCommunityId(Long communityId) {
    Community community = communityRepository.findById(communityId)
            .orElseThrow(() -> new RuntimeException("Community not found"));
    ImageData imageData = community.getCommunityImage();
    return ImageUtils.decompressImage(imageData.getImageData());
}


}
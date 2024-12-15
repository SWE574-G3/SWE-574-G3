package com.communitter.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.communitter.api.dto.ImageDTO;
import com.communitter.api.model.Community;
import com.communitter.api.model.ImageData;
import com.communitter.api.model.User;
import com.communitter.api.repository.CommunityRepository;
import com.communitter.api.repository.ImageRepository;
import com.communitter.api.repository.UserRepository;
import com.communitter.api.util.ImageUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.Optional;

class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommunityRepository communityRepository;

    @Mock
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadProfilePicture_shouldUploadImage_whenValidInput() throws IOException {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        byte[] fileBytes = new byte[]{1, 2, 3};
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(file.getOriginalFilename()).thenReturn("profile.jpg");
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getBytes()).thenReturn(fileBytes);
        when(imageRepository.findByUserId(userId)).thenReturn(null);
        when(imageRepository.save(any(ImageData.class))).thenReturn(new ImageData());

        String result = imageService.uploadProfilePicture(userId, file);

        assertTrue(result.contains("Profile picture uploaded successfully"));
        verify(imageRepository, times(1)).save(any(ImageData.class));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void uploadProfilePicture_shouldThrowException_whenInvalidMimeType() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(file.getContentType()).thenReturn("text/plain");

        assertThrows(IllegalArgumentException.class, () -> imageService.uploadProfilePicture(userId, file));
    }

    @Test
    void getUserProfilePicture_shouldReturnImageDTO_whenImageExists() {
        Long userId = 1L;
        byte[] originalImageDataBytes = new byte[]{1, 2, 3}; // Original uncompressed data
        byte[] compressedImageDataBytes = ImageUtils.compressImage(originalImageDataBytes); // Compressed data

        // Mock user and repository interactions
        User mockUser = new User();
        mockUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(imageRepository.findByUserId(userId)).thenReturn(
                ImageData.builder()
                        .name("imageName")
                        .type("image/jpeg")
                        .data_image(compressedImageDataBytes) // Save the compressed data
                        .build()
        );

        // Call the method under test
        ImageDTO result = imageService.getUserProfilePicture(userId);

        // Verify the result
        assertNotNull(result);
        assertEquals("image/jpeg", result.getMimeType());
        assertEquals(
                Base64.getEncoder().encodeToString(originalImageDataBytes), // Encode original (uncompressed) data
                result.getBase64Image()
        );

        // Verify interactions with mocks
        verify(userRepository).findById(userId);
        verify(imageRepository).findByUserId(userId);
    }



    @Test
    void getUserProfilePicture_shouldThrowException_whenImageNotFound() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(imageRepository.findByUserId(userId)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> imageService.getUserProfilePicture(userId));
    }

    @Test
    void deleteProfilePicture_shouldDeleteImage_whenImageExists() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        ImageData imageData = new ImageData();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(imageRepository.findByUserId(userId)).thenReturn(imageData);

        String result = imageService.deleteProfilePicture(userId);

        assertEquals("User profile picture deleted successfully.", result);
        verify(imageRepository, times(1)).delete(imageData);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteProfilePicture_shouldReturnMessage_whenImageNotFound() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(imageRepository.findByUserId(userId)).thenReturn(null);

        String result = imageService.deleteProfilePicture(userId);

        assertEquals("No profile picture found for this user.", result);
        verify(imageRepository, times(0)).delete(any(ImageData.class));
    }

    @Test
    void uploadCommunityPicture_shouldUploadImage_whenValidInput() throws IOException {
        Long communityId = 1L;
        Community community = new Community();
        community.setId(communityId);

        byte[] fileBytes = new byte[]{1, 2, 3};
        when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
        when(file.getOriginalFilename()).thenReturn("community.jpg");
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getBytes()).thenReturn(fileBytes);
        when(imageRepository.findByCommunityId(communityId)).thenReturn(null);
        when(imageRepository.save(any(ImageData.class))).thenReturn(new ImageData());

        String result = imageService.uploadCommunity_picture(communityId, file);

        assertTrue(result.contains("Profile picture uploaded successfully"));
        verify(imageRepository, times(1)).save(any(ImageData.class));
        verify(communityRepository, times(1)).save(community);
    }

    @Test
    void getCommunityImage_shouldReturnImageDTO_whenImageExists() {
        Long communityId = 1L;
        byte[] originalImageDataBytes = new byte[]{1, 2, 3}; // Original uncompressed data
        byte[] compressedImageDataBytes = ImageUtils.compressImage(originalImageDataBytes); // Compressed data

        // Create a mock community object
        Community community = new Community();
        community.setId(communityId);

        // Mock the community repository to return the community
        when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));

        // Mock the image repository to return compressed ImageData
        when(imageRepository.findByCommunityId(communityId)).thenReturn(
                ImageData.builder()
                        .name("imageName")
                        .type("image/jpeg")
                        .data_image(compressedImageDataBytes) // Store the compressed data
                        .build()
        );

        // Call the service method
        ImageDTO result = imageService.getCommunityImage(communityId);

        // Assertions
        assertNotNull(result); // Ensure the result is not null
        assertEquals("image/jpeg", result.getMimeType()); // Verify the MIME type
        assertEquals(
                Base64.getEncoder().encodeToString(originalImageDataBytes), // Encode the original uncompressed data
                result.getBase64Image()
        ); // Verify the Base64-encoded image data matches

        // Verify the interactions with the mocked repositories
        verify(communityRepository).findById(communityId);
        verify(imageRepository).findByCommunityId(communityId);
    }



    @Test
    void deleteCommunityPicture_shouldDeleteImage_whenImageExists() {
        Long communityId = 1L;
        Community community = new Community();
        community.setId(communityId);
        ImageData imageData = new ImageData();

        when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
        when(imageRepository.findByCommunityId(communityId)).thenReturn(imageData);

        String result = imageService.deleteCommunityPicture(communityId);

        assertEquals("Community profile picture deleted successfully.", result);
        verify(imageRepository, times(1)).delete(imageData);
        verify(communityRepository, times(1)).save(community);
    }
}

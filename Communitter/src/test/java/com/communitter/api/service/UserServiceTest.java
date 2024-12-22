package com.communitter.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.communitter.api.dto.request.UserRequest;
import com.communitter.api.model.User;
import com.communitter.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserInfo_shouldReturnUser_whenExists() {
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        User result = userService.getUserInfo(userId);

        assertEquals(userId, result.getId());
        verify(userRepository).findById(userId);
    }

    @Test
    void getUserInfo_shouldThrowException_whenNotExists() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserInfo(userId));
    }

    @Test
    void deleteUser_shouldDeleteUser_whenExists() {
        Long userId = 1L;

        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void updateUserProfile_shouldUpdateProfile_whenExists() {
        Long userId = 1L;
        UserRequest request = new UserRequest();
        request.setAbout("Updated bio");
        request.setAvatar("updated-avatar.jpg");
        request.setHeader("Updated header");

        User mockUser = new User();
        mockUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = userService.updateUserProfile(userId, request);

        assertEquals("Updated bio", result.getAbout());
        assertEquals("updated-avatar.jpg", result.getAvatar());
        assertEquals("Updated header", result.getHeader());
        verify(userRepository).save(mockUser);
    }

    @Test
    void updateUserEmail_shouldUpdateEmail_whenExists() {
        Long userId = 1L;
        UserRequest request = new UserRequest();
        request.setEmail("newemail@example.com");

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("oldemail@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = userService.updateUserEmail(userId, request);

        assertEquals("newemail@example.com", result.getEmail());
        verify(userRepository).save(mockUser);
    }


    @Test
    void updateUserHeader_shouldUpdateHeader_whenExists() {
        Long userId = 1L;
        String newHeader = "Updated header";

        User mockUser = new User();
        mockUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = userService.updateUserHeader(userId, newHeader);

        assertEquals(newHeader, result.getHeader());
        verify(userRepository).save(mockUser);
    }

    @Test
    void updateUserHeader_shouldThrowException_whenNotFound() {
        Long userId = 1L;
        String newHeader = "Updated header";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.updateUserHeader(userId, newHeader));
    }

    @Test
    void updateUserAbout_shouldUpdateAbout_whenExists() {
        Long userId = 1L;
        String newAbout = "Updated bio";

        User mockUser = new User();
        mockUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = userService.updateUserAbout(userId, newAbout);

        assertEquals(newAbout, result.getAbout());
        verify(userRepository).save(mockUser);
    }
}

package com.communitter.api.utils;

import com.communitter.api.exception.NotAuthorizedException;
import com.communitter.api.model.User;
import com.communitter.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BasicAuthorizationUtil {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    public boolean isCurrentUserEqualsToActionUser(User actionUser){
        User currentUser = getCurrentUser();
        if (!actionUser.equals(currentUser)){
            throw new NotAuthorizedException("You are not authorized to edit this post!");
        }
        return true;
    }
}

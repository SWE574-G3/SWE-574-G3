package com.communitter.api.auth;

import com.communitter.api.service.JwtService;
import com.communitter.api.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {


    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    public Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);


    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request,
           @NonNull HttpServletResponse response,
           @NonNull FilterChain filterChain) throws ServletException, IOException {
           String authHeader =request.getHeader("Authorization");
        if(authHeader ==null || !authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request,response);
                return;
            }
        String jwt = authHeader.substring(7);

        String userMail= jwtService.extractEmail(jwt);
        if(userMail!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            logger.info("Entered userDetails check");
            UserDetails userDetails =(userDetailsService.loadUserByUsername(userMail));
            logger.info("User Details are: "+userDetails.toString());
            logger.info("Is Token Valid: "+jwtService.isTokenValid(jwt,(User) userDetails));
            if(jwtService.isTokenValid(jwt,(User) userDetails)){
                UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("Auth Object is: "+SecurityContextHolder.getContext().getAuthentication().toString());
            }
        }
        filterChain.doFilter(request,response);

    }


}

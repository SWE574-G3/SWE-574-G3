package com.communitter.api.exception;

import org.hibernate.DuplicateMappingException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSucElement(NoSuchElementException exc){
       return new ResponseEntity<>(exc.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException exc){
        if(exc.getMessage().toLowerCase().contains("duplicate")) return new ResponseEntity<>("Duplicate entry", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UsernameNotFoundException exc){
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException exc){
        return new ResponseEntity<>("Authentication Failed", HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<String> handleNotAuthorizedException(NotAuthorizedException exc){
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException exc){
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception exc){
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

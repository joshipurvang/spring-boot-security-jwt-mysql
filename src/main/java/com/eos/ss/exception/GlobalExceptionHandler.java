package com.eos.ss.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.eos.ss.AppConstant;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler  {

    @ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<Object> handleCustomerNotFoundException(CustomerNotFoundException ex, WebRequest request) {
		log.error(ex.getMessage());
		ExceptionResponse response = ExceptionResponse.builder().message(ex.getMessage()).timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.toString()).details(request.getDescription(false)).build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

    @ExceptionHandler({UsernameNotFoundException.class,BadCredentialsException.class})
    public ResponseEntity<Object> handleUsernameNotFoundException(Exception ex,WebRequest request) {
    	log.error(ex.getMessage());
    	ExceptionResponse response = ExceptionResponse.builder().message("USER NAME NOT FOUND").timestamp(LocalDateTime.now())
				.status(HttpStatus.UNAUTHORIZED.toString()).details(request.getDescription(false)).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    @ExceptionHandler({ AccountStatusException.class })
    public ResponseEntity<Object> handleAccounAbnormalException(AccountStatusException ex,WebRequest request) {

    	log.error(ex.getMessage());
    	ExceptionResponse response = ExceptionResponse.builder().message("ACCOUNT STATUS EXCEPTION").timestamp(LocalDateTime.now())
				.status(HttpStatus.UNAUTHORIZED.toString()).details(request.getDescription(false)).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    @ExceptionHandler({ LockedException.class })    
    public ResponseEntity<Object> handleAccountExpiredExceptionException(LockedException ex,WebRequest request) {

    	log.error(ex.getMessage());
    	ExceptionResponse response = ExceptionResponse.builder().message("ACCOUNT EXPIRED").timestamp(LocalDateTime.now())
				.status(HttpStatus.UNAUTHORIZED.toString()).details(request.getDescription(false)).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    @ExceptionHandler({ InvalidBearerTokenException.class })
    
    public ResponseEntity<Object> handleInvalidBearerTockerException(InvalidBearerTokenException ex,WebRequest request) {

    	log.error(ex.getMessage());
    	ExceptionResponse response = ExceptionResponse.builder().message("BEARER TOKEN EXCEPTION").timestamp(LocalDateTime.now())
				.status(HttpStatus.UNAUTHORIZED.toString()).details(request.getDescription(false)).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    @ExceptionHandler({ AccessDeniedException.class })
   
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex,WebRequest request) {

    	log.error(ex.getMessage());
    	ExceptionResponse response = ExceptionResponse.builder().message(ex.getMessage()).timestamp(LocalDateTime.now())
				.status(HttpStatus.FORBIDDEN.toString()).details(request.getDescription(false)).build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
    
   

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex,WebRequest request) {
    	log.error(AppConstant.EXCEPTION_ERROR_MESSAGE);
    	ExceptionResponse response = ExceptionResponse.builder().message(AppConstant.EXCEPTION_ERROR_MESSAGE).timestamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.toString()).details(request.getDescription(false)).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    
}


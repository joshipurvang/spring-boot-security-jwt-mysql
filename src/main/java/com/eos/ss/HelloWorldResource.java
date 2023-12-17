package com.eos.ss;

import com.eos.ss.exception.CustomerNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldResource {
	
	@GetMapping("/hello")
	public ResponseEntity<Object> helloWorld() {
		throw new CustomerNotFoundException("User not found with username: " );
		
	}

}

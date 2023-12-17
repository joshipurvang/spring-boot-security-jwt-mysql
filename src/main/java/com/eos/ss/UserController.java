package com.eos.ss;

import java.util.List;

import com.eos.ss.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping(value = AppConstant.BASE_CONTEXT)
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping
	@Operation(summary = "Api to create User")
    public ResponseEntity<User> createUser(@RequestBody User user) {
		User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary ="Update User")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
    	User updatedUser = userService.updateUser(id, user);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    
    @GetMapping("/{id}")
    @Operation(summary ="Get User by ID")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
    	User user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        throw new CustomerNotFoundException("User not found for provided ID !!!");
    }

    @GetMapping
    @Operation(summary ="Get All User")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    //@Secured({"SCOPE_ROLE_ADMIN"})
    public ResponseEntity<List<User>> getAllCustomers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            throw new CustomerNotFoundException(AppConstant.NO_DATA_FOUND_ERROR_MESSAGE);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary ="Delete User by Id")
    public void deleteUserById(@PathVariable Long id) {
    	userService.deleteUserById(id);
    }
    


}

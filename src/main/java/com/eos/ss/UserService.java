package com.eos.ss;

import java.util.List;
import java.util.Optional;

import com.eos.ss.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

	
	@Autowired
	UserRepository userRepository;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public User createUser(User user) {
		user.setPassword(passwordEncoder().encode(user.getPassword()));
		
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
        	updatedUser.setId(id);
            return userRepository.save(updatedUser);
        }
        throw new CustomerNotFoundException("Requested data not found in db !!!");
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserById(Long id) {
    	userRepository.deleteById(id);
    }



}

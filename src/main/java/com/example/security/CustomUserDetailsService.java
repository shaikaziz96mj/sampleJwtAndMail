package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.User;
import com.example.repository.UserDetailsRepository;
import javassist.NotFoundException;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserDetailsRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String externalId) {
		User user = userRepository.getUserByExternalId(externalId);
		if(user == null) {
			return (UserDetails) new NotFoundException("User not found [id: " + externalId + "]");
		}
		return UserPrincipal.create(user);
	}
	
	public UserDetails loadUserById(Long id) throws NotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("User not found [id: " + id + "]"));
		return UserPrincipal.create(user);
	}
	
}

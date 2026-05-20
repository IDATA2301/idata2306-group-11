package com.roamroute.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.roamroute.backend.entity.User;
import com.roamroute.backend.repository.UserRepository;
import com.roamroute.backend.service.CustomerUserDetailsService;

@ExtendWith(MockitoExtension.class)
class CustomerUserDetailsServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CustomerUserDetailsService service;

  /**
   * loadUserByUsername should return UserDetails for a valid email and throw UsernameNotFoundException for an invalid email.
   * This test verifies that the service correctly loads user details when a valid email is provided, including the username, password, and roles. 
   * It also checks that when an email that does not exist in the repository is used, the service throws the appropriate exception, ensuring that it handles both success and failure scenarios correctly.
   */
	@Test
	void loadUserByUsername_found_returnsUserDetails() {
		User u = new User();
		u.setEmail("alice@example.com");
		u.setUser_password("s3cr3t");
		u.setUser_role("USER");

		when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(u));

		UserDetails details = service.loadUserByUsername("alice@example.com");

		assertEquals("alice@example.com", details.getUsername());
		assertEquals("s3cr3t", details.getPassword());
		// roles are converted to authorities with ROLE_ prefix by the builder
		boolean hasRole = details.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().endsWith("USER"));
		assertEquals(true, hasRole);
	}

  /**
   * loadUserByUsername should throw UsernameNotFoundException when the email is not found in the repository.
   * This test verifies that the service correctly identifies when a user does not exist for the given email and throws the appropriate exception, ensuring that it handles error scenarios gracefully.
   */
	@Test
	void loadUserByUsername_notFound_throws() {
		when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("missing@example.com"));
	}
}


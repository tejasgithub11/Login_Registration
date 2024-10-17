package org.example.Service;


import org.example.Model.User;
import org.example.Repository.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;


@Service
public class userservice {
	
	
	@Autowired
    private org.example.Repository.userRepo userRepo;
	public boolean existsByEmail(String email) {
		User user = userRepo.findByEmail(email);
		return user != null;
	}
	public void saveUser(User user) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashedPassword = md.digest(user.getPassword().getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : hashedPassword) {
				sb.append(String.format("%02x", b));
			}

			user.setPassword(sb.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		userRepo.save(user);
		return;

	}
	
	public User findByemail(String email) {
		
		return userRepo.findByEmail(email);
	}
	public boolean verifyPassword(String providedPassword, String storedHash) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] providedPasswordHash = md.digest(providedPassword.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : providedPasswordHash) {
				sb.append(String.format("%02x", b));
			}

			return sb.toString().equals(storedHash);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public List<User> getAllUsers() {
		List<User> users=userRepo.findAll();
		return users;
	}
	
	public Optional<User> findById(long id) {
        return userRepo.findById(id);
    }
	
	
}

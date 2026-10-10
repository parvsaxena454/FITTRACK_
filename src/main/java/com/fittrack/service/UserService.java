package com.fittrack.service;

import com.fittrack.entity.User;
import com.fittrack.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivityLogService activityLogService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ActivityLogService activityLogService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.activityLogService = activityLogService;
    }

    @Transactional
    public User registerUser(String username, String email, String rawPassword, String fullName) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email '" + email + "' is already registered.");
        }

        User user = new User();
        user.setUsername(username.trim().toLowerCase());
        user.setEmail(email.trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFullName(fullName.trim());
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        user.setFitnessLevel("Beginner");

        User savedUser = userRepository.save(user);
        activityLogService.logActivity(savedUser, "REGISTER", "New user registered: @" + savedUser.getUsername(), null);
        return savedUser;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username.toLowerCase());
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateProfile(Long userId, String fullName, Integer age, Double weight, Double height,
                              String fitnessLevel, String targetGoalSummary, String bio) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        user.setFullName(fullName != null ? fullName.trim() : user.getFullName());
        user.setAge(age);
        user.setWeight(weight);
        user.setHeight(height);
        user.setFitnessLevel(fitnessLevel != null ? fitnessLevel : user.getFitnessLevel());
        user.setTargetGoalSummary(targetGoalSummary);
        user.setBio(bio);

        User updated = userRepository.save(user);
        activityLogService.logActivity(updated, "USER_UPDATED", "Updated fitness profile metrics", null);
        return updated;
    }

    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect current password.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        activityLogService.logActivity(user, "USER_UPDATED", "Changed account password", null);
    }

    @Transactional
    public boolean toggleUserStatus(Long userId, String adminUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
        activityLogService.logActivity(user, "USER_STATUS_CHANGE",
                "User @" + user.getUsername() + " status set to " + (user.isEnabled() ? "ACTIVE" : "SUSPENDED") + " by @" + adminUsername, null);
        return user.isEnabled();
    }

    @Transactional
    public void updateUserRole(Long userId, String newRole, String adminUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        user.setRole(newRole);
        userRepository.save(user);
        activityLogService.logActivity(user, "USER_STATUS_CHANGE",
                "User @" + user.getUsername() + " role changed to " + newRole + " by @" + adminUsername, null);
    }

    @Transactional(readOnly = true)
    public long getTotalUsersCount() {
        return userRepository.count();
    }
}

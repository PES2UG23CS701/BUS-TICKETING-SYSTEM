package com.busbooking.busbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busbooking.busbooking.model.User;
import com.busbooking.busbooking.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public User register(User user) {
        return repo.save(user);
    }

    public User login(String email, String password) {
        if (email == null || password == null) {
            return null;
        }
        
        User user = repo.findByEmail(email.trim());

        if (user != null && user.getPassword() != null && 
            user.getPassword().equals(password.trim())) {
            return user;
        }

        return null;
    }

    public User getUserById(int id) {
        return repo.findById(id).orElse(null);
    }

    public User updateUser(User user) {
        return repo.save(user);
    }
}
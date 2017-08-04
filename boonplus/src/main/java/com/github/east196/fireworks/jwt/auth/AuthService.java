package com.github.east196.fireworks.jwt.auth;


import com.github.east196.fireworks.jwt.user.User;

public interface AuthService {
    User register(User userToAdd);
    String login(String username, String password);
    String refresh(String oldToken);
}

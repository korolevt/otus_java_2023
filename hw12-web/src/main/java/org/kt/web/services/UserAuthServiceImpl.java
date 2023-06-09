package org.kt.web.services;

public class UserAuthServiceImpl implements UserAuthService {
    @Override
    public boolean authenticate(String login, String password) {
        // Hardcode
        return (login.equals("admin") && password.equals("12345678"));
    }

}

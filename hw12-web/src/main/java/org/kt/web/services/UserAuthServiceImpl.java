package org.kt.web.services;

public class UserAuthServiceImpl implements UserAuthService {

    public UserAuthServiceImpl() {
    }

    @Override
    public boolean authenticate(String login, String password) {
        // Hardcode
        return (login.equals("admin") && password.equals("12345678"));
    }

}

package org.kt.web.services;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}

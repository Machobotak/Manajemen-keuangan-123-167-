package org.example.Login;

public class User {
    private String username;
    private String Password;

    public User(String username, String password) {
        this.username = username;
        Password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return Password;
    }
}

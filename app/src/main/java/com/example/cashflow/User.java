package com.example.cashflow;

public class User {
    private String userToken, username; // password;

    public User(String uToken, String uName) { // String uPass) {
        this.userToken = uToken;
        this.username = uName;
        // this.password = uPass;
    }

    public String getToken() {
        return userToken;
    }

    public void setToken(String uToken) {
        this.userToken = uToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String uName) {
        this.username = uName;
    }
}

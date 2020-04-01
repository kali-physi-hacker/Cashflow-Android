package com.example.cashflow;

public class UserModel {
    private String username, password;

    public UserModel(String uname, String pword) {
        username = uname;
        password = pword;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}

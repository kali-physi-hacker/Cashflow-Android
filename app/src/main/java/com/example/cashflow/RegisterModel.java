package com.example.cashflow;

public class RegisterModel {
    private String username, email, password;

    public RegisterModel(String uname, String mail, String pword) {
        username = uname;
        password = pword;
        email = mail;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}

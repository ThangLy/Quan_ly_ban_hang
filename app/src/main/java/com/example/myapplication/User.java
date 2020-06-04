package com.example.myapplication;

public class User {

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String username;
    String password;
    String email;

    public User(){

    }

    public User(String u, String p, String e) {
        username = u;
        password = p;
        email = e;
    }

}

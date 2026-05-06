package com.example.ecommerce_task.LoginRegister;

import com.google.gson.annotations.SerializedName;

public class Register {
    @SerializedName("fullName")  private String fullName;
    @SerializedName("username")  private String username;
    @SerializedName("email")     private String email;
    @SerializedName("password")  private String password;

    public Register(String fullName, String username, String email, String password) {
        this.fullName = fullName;
        this.username = username;
        this.email    = email;
        this.password = password;
    }
}

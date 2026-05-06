package com.example.ecommerce_task.LoginRegister;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("_id")       private String id;
    @SerializedName("fullName")  private String fullName;
    @SerializedName("username")  private String username;
    @SerializedName("email")     private String email;
    @SerializedName("password")  private String password;
    @SerializedName("phone")     private String phone;
    @SerializedName("address")   private String address;

    public String getId()       { return id; }
    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getEmail()    { return email; }
    public String getPhone()    { return phone; }
    public String getAddress()  { return address; }
}

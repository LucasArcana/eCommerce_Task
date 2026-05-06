package com.example.ecommerce_task.API;

import com.google.gson.annotations.SerializedName;

public class Authresponse {
    @SerializedName("token")
    private String token;

    @SerializedName("message")
    private String message;

    public String getToken()   { return token; }
    public String getMessage() { return message; }
}

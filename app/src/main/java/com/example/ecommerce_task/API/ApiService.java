package com.example.ecommerce_task.API;

import com.example.ecommerce_task.LoginRegister.Login;
import com.example.ecommerce_task.Product.Product;
import com.example.ecommerce_task.LoginRegister.Register;
import com.example.ecommerce_task.LoginRegister.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("login")
    Call<Authresponse> login(@Body Login request);

    @POST("register")
    Call<Authresponse> register(@Body Register request);

    @GET("profile")
    Call<User> getProfile(@Header("Authorization") String token);

    @PUT("users/me")
    Call<User> updateProfile(@Header("Authorization") String token, @Body User user);

    @GET("products")
    Call<List<Product>> getProducts(@Header("Authorization") String token);

    @PUT("products/{id}/buy")
    Call<Product> buyProduct(@Header("Authorization") String token, @Path("id") String productId);
}

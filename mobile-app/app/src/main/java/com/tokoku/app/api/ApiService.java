package com.tokoku.app.api;

import com.tokoku.app.model.LoginRequest;
import com.tokoku.app.model.LoginResponse;
import com.tokoku.app.model.Order;
import com.tokoku.app.model.OrderRequest;
import com.tokoku.app.model.Product;
import com.tokoku.app.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("auth/register")
    Call<User> register(@Body User user);

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("auth/me")
    Call<User> getMe();

    @GET("products/")
    Call<List<Product>> getProducts();

    @GET("products/")
    Call<List<Product>> searchProducts(@Query("search") String query);

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int id);

    @POST("orders/")
    Call<Order> createOrder(@Body OrderRequest request);

    @GET("orders/my")
    Call<List<Order>> getMyOrders();
}
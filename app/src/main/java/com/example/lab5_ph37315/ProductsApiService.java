package com.example.lab5_ph37315;

import com.example.lab5_ph37315.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductsApiService {
    @GET("/products/")
    Call<List<Product>> getProducts();

    @POST("/products/add/")
    Call<Product> newProduct(@Body Product pro);
    @DELETE("/products/{id}")
    Call<Void> deletePro(@Path("id")String productId);
    @PUT("/products/{id}")
    Call<Product> updatePro(@Path("id") String productId, @Body Product updatePro);
}

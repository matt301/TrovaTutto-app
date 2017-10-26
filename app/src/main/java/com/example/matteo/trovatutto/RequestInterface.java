package com.example.matteo.trovatutto;

import   com.example.matteo.trovatutto.models.ServerRequest;
import  com.example.matteo.trovatutto.models.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("App/")
    Call<ServerResponse> operation(@Body ServerRequest request);

}

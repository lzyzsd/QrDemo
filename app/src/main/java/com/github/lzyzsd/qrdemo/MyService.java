package com.github.lzyzsd.qrdemo;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by bruce on 15/3/30.
 */
public interface MyService {
    @GET("/asset/indexbykeeper")
    Observable<List<Product>> searchByKeeperName(@Query("keeper") String userName);

    @GET("/asset/indexbyname")
    Observable<List<Product>> searchByProductName(@Query("name") String productName);
}

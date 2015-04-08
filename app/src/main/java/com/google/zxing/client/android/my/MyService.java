package com.google.zxing.client.android.my;

import java.util.ArrayList;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by bruce on 15/3/30.
 */
public interface MyService {
    @GET("/asset/indexbykeeper")
    Observable<ArrayList<Product>> searchByKeeperName(@Query("keeper") String userName);

    @GET("/asset/indexbyname")
    Observable<ArrayList<Product>> searchByProductName(@Query("name") String productName);

    @GET("/asset/{id}")
    Observable<Product> searchById(@Path("id") int id);
}

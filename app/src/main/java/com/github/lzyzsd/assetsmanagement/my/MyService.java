package com.github.lzyzsd.assetsmanagement.my;

import com.squareup.okhttp.Response;

import java.util.ArrayList;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
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

    @GET("/asset")
    Observable<ArrayList<Product>> searchByAssetState(@Query("assetState") int state);

    @POST("/asset/{id}")
    @FormUrlEncoded
    Observable<Product> updateAssetState(@Path("id")int id, @Field("assetState") int state);
}

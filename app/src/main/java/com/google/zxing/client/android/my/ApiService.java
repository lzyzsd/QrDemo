package com.google.zxing.client.android.my;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by bruce on 15/4/11.
 */
public class ApiService {
    static MyService myService;

    private static void initMyService() {
        RestAdapter.Builder builder = new RestAdapter.Builder();
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(DateTime.class, new Product.DateTimeTypeAdapter())
            .create();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        OkClient okClient = new OkClient(okHttpClient);

        builder.setClient(okClient);
        RestAdapter restAdapter = builder.setClient(new OkClient())
            .setEndpoint("http://" + Constants.IP + ":" + Constants.PORT)
            .setLogLevel(RestAdapter.LogLevel.HEADERS)
            .setConverter(new GsonConverter(gson))
            .build();

        myService = restAdapter.create(MyService.class);
    }

    public static MyService getMyService() {
        if (myService == null) {
            initMyService();
        }

        return myService;
    }
}

package com.github.lzyzsd.qrdemo;

import com.loopj.android.http.*;

import org.apache.http.Header;
import org.json.JSONArray;

/**
 * Created by bruce on 15/3/30.
 */
public class MyService {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void searchByUserName(String userName, JsonHttpResponseHandler jsonHttpResponseHandler) {
        String url = "http://121.40.130.130:1337/asset/indexbykeeper";
        RequestParams requestParams = new RequestParams();
        requestParams.add("keeper", userName);
        client.get(url, requestParams, jsonHttpResponseHandler);
    }
}

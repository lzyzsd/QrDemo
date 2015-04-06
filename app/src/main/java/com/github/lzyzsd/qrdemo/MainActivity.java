package com.github.lzyzsd.qrdemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    Button searchButton;
    EditText nameText;
    ProgressBar progressBar;
    Spinner typeSpinner;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = (Button) findViewById(R.id.btn_search);
        nameText = (EditText) findViewById(R.id.et_name);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading);
        recyclerView = (RecyclerView) findViewById(R.id.rv_recycler_view);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MyAdapter());

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                MyService.searchByUserName(name, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        System.out.println(response.toString());
                        progressBar.setVisibility(View.GONE);
                        List<Product> products = new ArrayList<Product>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Product product = new Product(jsonObject);
                                products.add(product);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ((MyAdapter) recyclerView.getAdapter()).setProducts(products);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

}

package com.google.zxing.client.android.my;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.R;
import com.google.zxing.client.android.clipboard.ClipboardInterface;
import com.squareup.okhttp.OkHttpClient;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import rx.Subscriber;
import rx.Subscription;
import rx.android.observables.AndroidObservable;


public class MainActivity extends ActionBarActivity {
    Button searchButton;
    EditText nameText;
    ProgressBar progressBar;
    Spinner typeSpinner;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    MyService myService;

    Subscription searchByKeeperSubscription, searchByNameSubScription, searchByIdSubscription;

    private void initMyService() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMyService();

        searchButton = (Button) findViewById(R.id.btn_search);
        nameText = (EditText) findViewById(R.id.et_name);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading);
        recyclerView = (RecyclerView) findViewById(R.id.rv_recycler_view);
        typeSpinner = (Spinner) findViewById(R.id.s_search_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.search_by_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MyAdapter());

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if ((typeSpinner.getSelectedItem()).equals("保管人")) {
                    searchByKeeperName(name);
                } else {
                    searchByProductName(name);
                }
            }
        });
    }

    private void unsubscribe() {
        if (searchByIdSubscription != null) {
            searchByIdSubscription.unsubscribe();
        }

        if (searchByNameSubScription != null) {
            searchByNameSubScription.unsubscribe();
        }

        if (searchByKeeperSubscription != null) {
            searchByKeeperSubscription.unsubscribe();
        }
    }

    private void searchByKeeperName(String name) {
        unsubscribe();
        searchByKeeperSubscription = AndroidObservable.bindActivity(this, myService.searchByKeeperName(name))
                .subscribe(new Subscriber<List<Product>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(List<Product> products) {
                        progressBar.setVisibility(View.GONE);
                        ((MyAdapter) recyclerView.getAdapter()).setProducts(products);
                    }
                });
    }

    private void searchByProductName(String name) {
        unsubscribe();
        searchByNameSubScription = AndroidObservable.bindActivity(this, myService.searchByProductName(name))
                .subscribe(new Subscriber<List<Product>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(List<Product> products) {
                        progressBar.setVisibility(View.GONE);
                        ((MyAdapter) recyclerView.getAdapter()).setProducts(products);
                    }
                });
    }

    private void searchById(int id) {
        unsubscribe();
        nameText.setText("");
        searchByIdSubscription = AndroidObservable.bindActivity(this, myService.searchById(id))
                .subscribe(new Subscriber<Product>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Product product) {
                        progressBar.setVisibility(View.GONE);
                        List<Product> products = new ArrayList<>(1);
                        products.add(product);
                        ((MyAdapter) recyclerView.getAdapter()).setProducts(products);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_scan) {
            startActivityForResult(new Intent(this, CaptureActivity.class), Constants.REQUEST_CODE_CAPTURE_ACTIVITY);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_CAPTURE_ACTIVITY && !TextUtils.isEmpty(ClipboardInterface.getText(this))) {
            String url = ClipboardInterface.getText(this).toString();
            Uri uri = Uri.parse(url);
            if (uri.getHost() == null || !uri.getHost().equals(Constants.IP)) {
                return;
            }
            List<String> paths = uri.getPathSegments();
            int id = Integer.parseInt(paths.get(paths.size() - 1));
            searchById(id);
        }
    }
}

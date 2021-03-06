package com.github.lzyzsd.assetsmanagement.my;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.github.lzyzsd.assetsmanagement.clipboard.ClipboardInterface;
import com.github.lzyzsd.assetsmanagement.R;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.observables.AndroidObservable;

public class SearchTabFragment extends Fragment {
    Button searchButton;
    EditText nameText;
    ProgressBar progressBar;
    Spinner typeSpinner;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    MyService myService = ApiService.getMyService();

    Subscription searchByKeeperSubscription, searchByNameSubScription, searchByIdSubscription;

    public SearchTabFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ApiService.bus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_tab, container, false);

        searchButton = (Button) view.findViewById(R.id.btn_search);
        nameText = (EditText) view.findViewById(R.id.et_name);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_recycler_view);
        typeSpinner = (Spinner) view.findViewById(R.id.s_search_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
            R.array.search_by_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(this.getActivity());
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
        return view;
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
        searchByKeeperSubscription = AndroidObservable.bindFragment(this, myService.searchByKeeperName(name))
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
        searchByNameSubScription = AndroidObservable.bindFragment(this, myService.searchByProductName(name))
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
        searchByIdSubscription = AndroidObservable.bindFragment(this, myService.searchById(id))
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

    private void updateAssetState(int id, int state) {
        unsubscribe();
        nameText.setText("");
        AndroidObservable.bindFragment(this, myService.updateAssetState(id, state))
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
                    ((MyAdapter) recyclerView.getAdapter()).update(product);
                }
            });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (menu.findItem(R.id.menu_scan) == null) {
            menu.add(0, R.id.menu_scan, 0, "扫码");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiService.bus.register(this);
    }

    @Subscribe
    public void onAssetStateUpdateEvent(UpdateAssetStateEvent updateAssetStateEvent) {
        updateAssetState(updateAssetStateEvent.id, updateAssetStateEvent.state);
    }

    @Subscribe
    public void onScanResultEvent(ScanResultEvent scanResultEvent) {
        searchById(scanResultEvent.id);
    }
}

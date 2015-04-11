package com.google.zxing.client.android.my;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.zxing.client.android.R;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.observables.AndroidObservable;

public class ObsoleteTabFragment extends Fragment {
    ProgressBar progressBar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    MyService myService = ApiService.getMyService();

    Subscription subscription;

    public ObsoleteTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_obsolete_tab, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_recycler_view);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MyAdapter());
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getObsoleteProducts();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.removeItem(R.id.menu_scan);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }

    private void unsubscribe() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    private void getObsoleteProducts() {
        unsubscribe();
        progressBar.setVisibility(View.VISIBLE);
        subscription = AndroidObservable.bindFragment(this, myService.searchByAssetState(4))//4是报废
            .subscribe(new Subscriber<ArrayList<Product>>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onNext(ArrayList<Product> products) {
                    progressBar.setVisibility(View.GONE);
                    ((MyAdapter) recyclerView.getAdapter()).setProducts(products);
                }
            });
    }
}

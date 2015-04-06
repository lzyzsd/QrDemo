package com.github.lzyzsd.qrdemo;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce on 15/4/6.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Product> products = new ArrayList<>();
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView specificTypeText;
        TextView buyPriceText;
        TextView deprecationPeriodText;
        TextView keeperText;
        TextView keepPlaceText;
        TextView numberText;
        TextView manufacturerText;
        TextView assetStateText;
        TextView createdAtText;
        public ViewHolder(View v) {
            super(v);
            nameText = (TextView) v.findViewById(R.id.tv_name);
            specificTypeText = (TextView) v.findViewById(R.id.tv_specific_type);
            buyPriceText = (TextView) v.findViewById(R.id.tv_buy_price);
            deprecationPeriodText = (TextView) v.findViewById(R.id.tv_depreciation_period);
            keeperText = (TextView) v.findViewById(R.id.tv_keeper);
            keepPlaceText = (TextView) v.findViewById(R.id.tv_keep_place);
            numberText = (TextView) v.findViewById(R.id.tv_number);
            manufacturerText = (TextView) v.findViewById(R.id.tv_manufacturer);
            assetStateText = (TextView) v.findViewById(R.id.tv_asset_state);
            createdAtText = (TextView) v.findViewById(R.id.tv_created_at);
        }

        public void bind(Product product) {
            nameText.setText(product.name);
            specificTypeText.setText(product.specificType);
            buyPriceText.setText(String.valueOf(product.buyPrice));
            deprecationPeriodText.setText(String.valueOf(product.depreciationPeriod));
            keeperText.setText(product.keeper);
            keepPlaceText.setText(product.keepPlace);
            numberText.setText(String.valueOf(product.number));
            manufacturerText.setText(product.manufacturer);
            assetStateText.setText(String.valueOf(product.assetState));
            createdAtText.setText(product.createdAt.toString("YYYY-MM-DD HH:mm:ss"));
        }
    }

    public MyAdapter() {
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        this.notifyDataSetChanged();
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
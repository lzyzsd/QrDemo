package com.github.lzyzsd.assetsmanagement.my;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.github.lzyzsd.assetsmanagement.R;

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
        TextView buyTimeText;
        TextView buyPriceText;
        TextView deprecationPeriodText;
        TextView keeperText;
        TextView keepPlaceText;
        TextView typeText;
        TextView numberText;
        TextView manufacturerText;
        TextView remarkText;
        TextView classFieldText;
        TextView assetStateText;
        TextView expireTimeText;
        ImageView action;
        public ViewHolder(View v) {
            super(v);
            nameText = (TextView) v.findViewById(R.id.tv_name);
            specificTypeText = (TextView) v.findViewById(R.id.tv_specific_type);
            buyTimeText = (TextView) v.findViewById(R.id.tv_buy_time);
            buyPriceText = (TextView) v.findViewById(R.id.tv_buy_price);
            deprecationPeriodText = (TextView) v.findViewById(R.id.tv_depreciation_period);
            keeperText = (TextView) v.findViewById(R.id.tv_keeper);
            keepPlaceText = (TextView) v.findViewById(R.id.tv_keep_place);
            typeText = (TextView) v.findViewById(R.id.tv_type);
            numberText = (TextView) v.findViewById(R.id.tv_number);
            manufacturerText = (TextView) v.findViewById(R.id.tv_manufacturer);
            remarkText = (TextView) v.findViewById(R.id.tv_remark);
            classFieldText = (TextView) v.findViewById(R.id.tv_class_field);
            assetStateText = (TextView) v.findViewById(R.id.tv_asset_state);
            expireTimeText = (TextView) v.findViewById(R.id.tv_expire_time);
            action = (ImageView) v.findViewById(R.id.btn_card_action);
        }

        public void bind(final Product product) {
            nameText.setText(product.name);
            specificTypeText.setText(product.specificType);
            buyPriceText.setText(String.valueOf(product.buyPrice));
            deprecationPeriodText.setText(String.valueOf(product.depreciationPeriod));
            keeperText.setText(product.keeper);
            keepPlaceText.setText(product.keepPlace);
            typeText.setText(product.type);
            numberText.setText(String.valueOf(product.number));
            manufacturerText.setText(product.manufacturer);
            remarkText.setText(product.remark);
            classFieldText.setText(product.getClassFieldString());
            assetStateText.setText(String.valueOf(product.getAssetStateString()));
            if (product.expireTime != null) {
                expireTimeText.setText(product.expireTime.toString("YYYY-MM-DD HH:mm:ss"));
            }

            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), action);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_actions, popupMenu.getMenu());
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.mark_as_fix:
                                    System.out.println("-----------------------fix");
                                    ApiService.bus.post(new UpdateAssetStateEvent(product.id, 3));
                                    return true;
                                case R.id.mark_as_obsolete:
                                    System.out.println("-----------------------obsolete");
                                    ApiService.bus.post(new UpdateAssetStateEvent(product.id, 4));
                                    return true;
                            }
                            return false;
                        }
                    });
                }
            });
        }
    }

    public MyAdapter() {
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        this.notifyDataSetChanged();
    }

    public void update(Product product) {
        int position = -1;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).id == product.id) {
                products.set(i, product);
                position = i;
                break;
            }
        }

        if (position != -1) {
            notifyItemChanged(position);
        }
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
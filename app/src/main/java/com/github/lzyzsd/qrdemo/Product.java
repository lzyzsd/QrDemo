package com.github.lzyzsd.qrdemo;

import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by bruce on 15/4/6.
 */
public class Product {
    public String name;
    public String specificType;
    public DateTime buyTime;
    public double buyPrice;
    public int depreciationPeriod;
    public String keeper;
    public String type;
    public String keepPlace;
    public DateTime expireTime;
    public int number;
    public String manufacturer;
    public String remark;
    public String classField;
    public int assetState;
    public int id;
    public DateTime createdAt;
    public DateTime updatedAt;

    public Product() {}

    public Product(JSONObject jsonObject) {
        try {
            this.name = jsonObject.getString("name");
            this.specificType = jsonObject.getString("specificType");
            this.buyTime = new DateTime(jsonObject.getString("buyTime"));
            this.buyPrice = jsonObject.getDouble("buyPrice");
            this.depreciationPeriod = jsonObject.getInt("depreciationPeriod");
            this.keeper = jsonObject.getString("keeper");
            this.type = jsonObject.getString("type");
            this.keepPlace = jsonObject.getString("keepPlace");
            this.expireTime = jsonObject.isNull("expireTime") ? null : new DateTime(jsonObject.getString("expireTime"));
            this.number = jsonObject.getInt("number");
            this.manufacturer = jsonObject.getString("manufacturer");
            this.remark = jsonObject.getString("remark");
            this.classField = jsonObject.getString("classField");
            this.assetState = jsonObject.getInt("assetState");
            this.id = jsonObject.getInt("id");
            this.createdAt = new DateTime(jsonObject.getString("createdAt"));
            this.updatedAt = new DateTime(jsonObject.getString("updatedAt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class DateTimeTypeAdapter extends TypeAdapter<DateTime> {
        @Override
        public void write(JsonWriter out, DateTime value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            out.value(value.getMillis());
        }

        @Override
        public DateTime read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return deserializeToDate(in.nextString());
        }

        private synchronized DateTime deserializeToDate(String json) {
            try {
                return DateTime.parse(json);
            } catch (Exception e){}

            return DateTime.now();
        }
    }
}

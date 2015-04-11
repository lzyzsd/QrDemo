package com.github.lzyzsd.assetsmanagement.my;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.joda.time.DateTime;

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
    public int classField;
    public int assetState;
    public int id;
    public DateTime createdAt;
    public DateTime updatedAt;

    public String getClassFieldString() {
        switch (this.classField) {
            case 0:
                return "固定资产";
            default:
                return "耗材";
        }
    }

    public String getAssetStateString() {
        switch (this.assetState) {
            case 2:
                return "借用";
            case 3:
                return "保修";
            case 4:
                return "报废";
            case 5:
                return "维修";
            default:
                return "库存";
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

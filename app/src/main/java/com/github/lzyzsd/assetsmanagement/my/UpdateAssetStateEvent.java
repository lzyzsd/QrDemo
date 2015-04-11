package com.github.lzyzsd.assetsmanagement.my;

/**
 * Created by bruce on 15/4/12.
 */
public class UpdateAssetStateEvent {
    public int id;
    public int state;

    public UpdateAssetStateEvent(int id, int state) {
        this.id = id;
        this.state = state;
    }
}

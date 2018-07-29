package com.yww.shupian.PictureAbout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 杨旺旺 on 2017/11/24.
 */

public class ItemEntity {

    private String galleryid;
    private String galleryname;
    private String temperature;
    private String coverImageUrl;
    private String address;
    private String galleryinro;
    private String time;
    private String mapImageUrl;

    public ItemEntity(JSONObject jsonObject) {
        try {
            this.galleryname = jsonObject.getString("galleryname");
            this.temperature = jsonObject.getString("temperature");
            if (jsonObject.optString("coverImageUrl").equals("NOCOVER")) {
                this.coverImageUrl = "http://192.168.56.1:8080/ServletFirst/Pic/nocover.jpg";
                this.mapImageUrl = "http://img.hb.aicdn.com/3f04db36f22e2bf56d252a3bc1eacdd2a0416d75221a7c-rpihP1_fw658";
            } else {
                this.coverImageUrl = jsonObject.getString("coverImageUrl");
                this.mapImageUrl = jsonObject.getString("mapImageUrl");
            }

            this.address = jsonObject.getString("address");
            this.galleryinro = jsonObject.getString("galleryintro");
            this.time = jsonObject.getString("time");
        }catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public String getGalleryid() {
        return galleryid;
    }

    public void setGalleryid(String galleryid) {
        this.galleryid = galleryid;
    }

    public String getgalleryname() {
        return galleryname;
    }

    public void setgalleryname(String galleryname) {
        this.galleryname = galleryname;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getgalleryinro() {
        return galleryinro;
    }

    public void setgalleryinro(String galleryinro) {
        this.galleryinro = galleryinro;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMapImageUrl() {
        return mapImageUrl;
    }

    public void setMapImageUrl(String mapImageUrl) {
        this.mapImageUrl = mapImageUrl;
    }
}

package com.yww.shupian.Widget;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by 杨旺旺 on 2017/12/30.
 */

public class item_photoer {
    private String photoer_id;
    private String photoer_name;
    private String photoer_city;
    private String photoer_school;
    private Bitmap photoer_head_icon;
    private boolean hasIcon;
    private ArrayList<String> picURLlist;


    public item_photoer(String photoer_id, String photoer_name, String photoer_city, String photoer_school,boolean hasIcon) {
        this.photoer_id = photoer_id;
        this.photoer_name = photoer_name;
        this.photoer_city = photoer_city;
        this.photoer_school = photoer_school;
        this.hasIcon=hasIcon;
        this.picURLlist=new ArrayList<String>();
    }

    public ArrayList<String> getPicURLlist() {
        return picURLlist;
    }

    public void add2PicURLlist(String newitem) {
        this.picURLlist.add(newitem);
    }

    public String getPhotoer_id() {
        return photoer_id;
    }

    public void setPhotoer_id(String photoer_id) {
        this.photoer_id = photoer_id;
    }

    public String getPhotoer_name() {
        return photoer_name;
    }

    public void setPhotoer_name(String photoer_name) {
        this.photoer_name = photoer_name;
    }

    public String getPhotoer_city() {
        return photoer_city;
    }

    public void setPhotoer_city(String photoer_city) {
        this.photoer_city = photoer_city;
    }

    public String getPhotoer_school() {
        return photoer_school;
    }

    public void setPhotoer_school(String photoer_school) {
        this.photoer_school = photoer_school;
    }

    public Bitmap getPhotoer_head_icon() {
        return photoer_head_icon;
    }

    public void setPhotoer_head_icon(Bitmap photoer_head_icon) {
        this.photoer_head_icon = photoer_head_icon;
    }

    public boolean isHasIcon() {
        return hasIcon;
    }

    public void setHasIcon(boolean hasIcon) {
        this.hasIcon = hasIcon;
    }
}

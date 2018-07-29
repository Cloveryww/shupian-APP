package com.yww.shupian.ChatAbout;

import android.graphics.Bitmap;

/**
 * Created by 杨旺旺 on 2017/11/27.
 */

public class item_friend {
    private String ifriendId;
    private String ifriendname;
    private Bitmap iImagebm;
    private boolean hasIcon;

    public item_friend(String ifriendId,String ifriendname,Bitmap iImagebm,boolean ihasIcon) {
        this.ifriendId = ifriendId;
        this.ifriendname=ifriendname;
        this.iImagebm=iImagebm;
        this.hasIcon=ihasIcon;
    }

    public String getfriendId() {
        return ifriendId;
    }

    public void setfriendId(String ifriendId) {
        this.ifriendId = ifriendId;
    }

    public String getfriendname() {
        return ifriendname;
    }

    public boolean gethasIcon() {
        return hasIcon;
    }

    public void setfriendname(String ifriendname) {
        this.ifriendname = ifriendname;
    }

    public Bitmap getImagebm() {
        return iImagebm;
    }

    public void setImagebm(Bitmap iImagebm) {
        this.iImagebm = iImagebm;
    }

    public void setImagebm(boolean i) {
        this.hasIcon = i;
    }


}

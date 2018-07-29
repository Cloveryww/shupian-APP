package com.yww.shupian.PictureAbout;

/**
 * Created by 杨旺旺 on 2017/11/23.
 */

public class picture_entity {

    private String iPicId;
    private String iPicURL;


    public picture_entity() {
    }

    public picture_entity(String iPicId,String iPicURL) {
        this.iPicId = iPicId;
        this.iPicURL=iPicURL;
    }

    public String getiPicId() {
        return iPicId;
    }
    public String getiPicURL() {
        return iPicURL;
    }

    public void setiPicId(String iPicId) {
        this.iPicId=iPicId;
    }
    public void setiPicURL(String iPicURL) {
        this.iPicURL = iPicURL;
    }

}


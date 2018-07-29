package com.yww.shupian.PictureAbout;

/**
 * Created by 杨旺旺 on 2017/12/14.
 */

public class item_in_show_pictureInfo {
    private String iPicId;
    private String iGalleryId;
    private String iPicURL;


    public item_in_show_pictureInfo() {
    }

    public item_in_show_pictureInfo(String iPicId,String iGalleryId,String iPicURL) {
        this.iPicId = iPicId;
        this.iGalleryId=iGalleryId;
        this.iPicURL=iPicURL;
    }

    public String getiPicId() {
        return iPicId;
    }
    public String getiGalleryId() {
        return iGalleryId;
    }
    public String getiPicURL() {
        return iPicURL;
    }

    public void setiPicId(String iPicId) {
        this.iPicId=iPicId;
    }
    public void setiGalleryId(String iGalleryId) {
        this.iGalleryId=iGalleryId;
    }
    public void setiPicURL(String iPicURL) {
        this.iPicURL = iPicURL;
    }

}

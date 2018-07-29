package com.yww.shupian.PictureAbout;

/**
 * Created by 杨旺旺 on 2017/11/20.
 */

public class gallery_entity {
    private int iId;
    private int iNo;
    private String iGalleryId;
    private String iName;
    private String iIntroduction;
    private String iImageURL;

    public gallery_entity() {
    }

    public gallery_entity(int iId,int iNo,String iGalleryId, String iName,String iInroduction,String iImageURL) {
        this.iId = iId;
        this.iNo=iNo;
        this.iGalleryId=iGalleryId;
        this.iName = iName;
        this.iIntroduction=iInroduction;
        this.iImageURL=iImageURL;
    }

    public int getiId() {
        return iId;
    }
    public int getiNo() {
        return iNo;
    }
    public String getiGalleryId(){
        return iGalleryId;
    }
    public String getiName() {
        return iName;
    }
    public String getiIntroduction() {
        return iIntroduction;
    }
    public String getiImageURL() {
        return iImageURL;
    }

    public void setiId(int iId) {
        this.iId = iId;
    }
    public void setiNo(int iNo) {
        this.iNo = iNo;
    }
    public void setiGalleryId(String iGalleryId)
    {
        this.iGalleryId=iGalleryId;
    }
    public void setiName(String iName) {
        this.iName = iName;
    }
    public void setiIntroduction(String iIntroduction) {
        this.iIntroduction = iIntroduction;
    }
    public void setiImageURL(String iImageURL){this.iImageURL=iImageURL;}
}

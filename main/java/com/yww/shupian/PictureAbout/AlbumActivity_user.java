package com.yww.shupian.PictureAbout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yww.shupian.MainActivity;
import com.yww.shupian.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.yww.shupian.Util.Upload_info;
import com.maning.imagebrowserlibrary.MNImageBrowser;//引用的第三方图片操作库
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AlbumActivity_user extends AppCompatActivity {
    protected GridView gvImages;
    protected TextView tv_title;
    protected ImageView btn_back;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;

    private ArrayList<picture_entity> sourceImageList;
    private ArrayList<String>sourceImageURLList;
    private NineGridAdapter mNineGridAdapter;
    private JSONArray PicturesJsonResult;

    private String UserId;
    private Bitmap mBitmap;
    private Context context;
    private String GalleryId;
    private String GalleryName;
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                {
                    Toast.makeText(AlbumActivity_user.this,"图片上传成功,请稍后查看",Toast.LENGTH_SHORT).show();
                    mNineGridAdapter.notifyDataSetChanged();
                }
                break;
                case 2:
                {
                    Toast.makeText(AlbumActivity_user.this,"图片上传失败，请重新上传",Toast.LENGTH_SHORT).show();
                }
                break;
                case 3:
                {
                    Toast.makeText(AlbumActivity_user.this,"图片下载成功",Toast.LENGTH_SHORT).show();
                    mNineGridAdapter.notifyDataSetChanged();
                }
                break;
                case 4:
                {
                    Toast.makeText(AlbumActivity_user.this,"此相册为空",Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                    Toast.makeText(AlbumActivity_user.this,"网络错误",Toast.LENGTH_SHORT).show();

            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_user);
        context = this;
        UserId=getIntent().getStringExtra("UserId");
        GalleryId=getIntent().getStringExtra("GalleryId");
        GalleryName=getIntent().getStringExtra("GalleryName");
        intiview();
        tv_title.setText(GalleryName);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });//设置返回键


        initDataList();
        /*sourceImageList.add("http://pic6.nipic.com/20100414/3871838_093646015032_2.jpg");
        sourceImageList.add("http://pic15.nipic.com/20110616/2707401_224254882000_2.jpg");
        sourceImageList.add("http://pic138.nipic.com/file/20170816/22554547_123534011000_2.jpg");
        sourceImageList.add("http://pic138.nipic.com/file/20170814/25725094_081819905296_2.jpg");
        sourceImageList.add("http://pic138.nipic.com/file/20170814/2059536_095931176031_2.jpg");
        sourceImageList.add("http://pic138.nipic.com/file/20170815/25700412_023451594000_2.jpg");
        sourceImageList.add("http://pic138.nipic.com/file/20170811/21847396_114517617036_2.jpg");
        sourceImageList.add("http://pic138.nipic.com/file/20170811/21847396_110209479034_2.jpg");
        sourceImageList.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-12-18380140_455327614813449_854681840315793408_n.jpg");
        sourceImageList.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-11-18380166_305443499890139_8426655762360565760_n.jpg");
        sourceImageList.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-10-18382517_1955528334668679_3605707761767153664_n.jpg");
        sourceImageList.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-09-18443931_429618670743803_5734501112254300160_n.jpg");
        sourceImageList.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-08-18252341_289400908178710_9137908350942445568_n.jpg");
        sourceImageList.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-05-18251898_1013302395468665_8734429858911748096_n.jpg");
        sourceImageList.add("http://ww1.sinaimg.cn/large/61e74233ly1feuogwvg27j20p00zkqe7.jpg");
        sourceImageList.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-02-926821_1453024764952889_775781470_n.jpg");
        sourceImageList.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-28-18094719_120129648541065_8356500748640452608_n.jpg");
        sourceImageList.add("https://ws1.sinaimg.cn/mw690/610dc034ly1ffwb7npldpj20u00u076z.jpg");
        sourceImageList.add("https://ws1.sinaimg.cn/large/610dc034ly1ffv3gxs37oj20u011i0vk.jpg");
        sourceImageList.add("https://ws1.sinaimg.cn/large/610dc034ly1fftusiwb8hj20u00zan1j.jpg");
        sourceImageList.add("http://ww1.sinaimg.cn/large/610dc034ly1ffmwnrkv1hj20ku0q1wfu.jpg");
        sourceImageList.add("https://ws1.sinaimg.cn/large/610dc034ly1ffyp4g2vwxj20u00tu77b.jpg");
        sourceImageList.add("https://ws1.sinaimg.cn/large/610dc034ly1ffxjlvinj5j20u011igri.jpg");
        sourceImageList.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-11-17881546_248332202297978_2420944671002853376_n.jpg");
        sourceImageList.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-12-17662441_1675934806042139_7236493360834281472_n.jpg");
        sourceImageList.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-13-17882785_926451654163513_7725522121023029248_n.jpg");
        sourceImageList.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-14-17881962_1329090457138411_8289893708619317248_n.jpg");
        sourceImageList.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-16-17934400_1738549946443321_2924146161843437568_n.jpg");
    */
        mNineGridAdapter=new NineGridAdapter();
        gvImages.setAdapter(mNineGridAdapter);
    }

    private class NineGridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return sourceImageList.size();
        }

        @Override
        public Object getItem(int position) {
            return sourceImageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.image_item, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            Picasso.with(context).load(sourceImageList.get(position).getiPicURL()).into(viewHolder.imageView);//通过URL下载网络图片

            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MNImageBrowser.showImageBrowser(context, viewHolder.imageView, position, sourceImageURLList);
                }
            });


            return convertView;
        }

        public final class ViewHolder {
            ImageView imageView;
        }
    }
    private void intiview() {
        gvImages = (GridView) findViewById(R.id.gv_images2);
        tv_title=(TextView)findViewById(R.id.InAlbum2_name);
        btn_back=(ImageView)findViewById(R.id.InAlbum2_backbtn);
    }

    private void initDataList()
    {
        sourceImageList = new ArrayList<>();
        sourceImageURLList=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                PicturesJsonResult = new Upload_info().upload_Pictures(UserId,GalleryId);
                if(PicturesJsonResult==null)
                {
                    Message msg = Message.obtain();
                    msg.what = 4;
                    handler.sendMessage(msg);
                }
                else {//初始化mData
                    try {
                        for (int i = 0; i < PicturesJsonResult.length(); i++) {
                            JSONObject getJsonObj = PicturesJsonResult.getJSONObject(i);//获取json数组中的一项
                            sourceImageList.add(new picture_entity(getJsonObj.getString("picId"),getJsonObj.getString("picPath") ));
                            sourceImageURLList.add(getJsonObj.getString("picPath"));
                        }
                    }catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(AlbumActivity_user.this.getContentResolver(), data.getData());
            }catch (IOException e)
            {
                e.printStackTrace();
            }
            final String strimg=bitmap2String();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stu
                    Boolean upLoadResult =  new Upload_info().upLoadPhoto(UserId,strimg,Integer.parseInt(GalleryId));
                    System.out.println(upLoadResult.toString());
                    Message msg = Message.obtain();
                    if (upLoadResult)
                        msg.what = 1;
                    else
                        msg.what = 2;
                    handler.sendMessage(msg);
                }
            }).start();

        }
    }
    private String bitmap2String() {

        if(mBitmap==null){
            Toast.makeText(AlbumActivity_user.this, "先选取照片",Toast.LENGTH_SHORT).show();
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        boolean b = mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        if(b==false){
            return null;
        }
        byte[] bs = stream.toByteArray();
        String s = Base64.encodeToString(bs, Base64.DEFAULT);

        return s;
    }
}


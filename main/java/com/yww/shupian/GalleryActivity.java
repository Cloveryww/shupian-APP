package com.yww.shupian;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yww.shupian.PictureAbout.AlbumActivity;
import com.yww.shupian.PictureAbout.gallery_entity;
import com.yww.shupian.Util.Upload_info;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private String UserId;//当前相册的用户Id
    private ImageView btn_back;
    private TextView btn_new;
    private Context mContext;
    private ListView list_gallery;
    private ArrayList<gallery_entity> mData = null;
    private JSONArray GalleryJsonResult;
    private  Gallery_Adapter myAdapter;
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                {
                    Toast.makeText(GalleryActivity.this,"用户相册为空",Toast.LENGTH_SHORT).show();
                }
                break;
                case 1:
                {
                    myAdapter.notifyDataSetChanged();
                    Toast.makeText(GalleryActivity.this, "相册建立成功", Toast.LENGTH_SHORT).show();
                }
                break;
                case 2:
                {
                    myAdapter.notifyDataSetChanged();
                    Toast.makeText(GalleryActivity.this,"用户相册下载成功",Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                {
                    Toast.makeText(GalleryActivity.this, "相册建立失败", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mContext = GalleryActivity.this;
        UserId=getIntent().getStringExtra("UserId");
        initview();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View dialogEdit=(LinearLayout) getLayoutInflater().inflate(R.layout.dialog_new_gallery,null);
                final EditText new_et1 =dialogEdit.findViewById(R.id.dialog_new_gallery_name) ;//新建相册的时候EditText
                final EditText new_et2 = dialogEdit.findViewById(R.id.dialog_new_gallery_intro);//新建相册的时候EditText
                new AlertDialog.Builder(GalleryActivity.this).setTitle("新建相册")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(dialogEdit)
                        .setPositiveButton("新建", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String name = new_et1.getText().toString();
                                        String introduction=new_et2.getText().toString();
                                        if (name.equals("")||introduction.equals("")) {
                                            Toast.makeText(getApplicationContext(), "相册建立失败，相册名称和相册简介不能为空,请重新建立", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            create_new_gallery(name,introduction);
                                        }
                                    }
                                }).start();

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

       initDataList();//初始化mData
        //设置ListView的数据适配器
        myAdapter=new Gallery_Adapter(this,mData);
        list_gallery.setAdapter(myAdapter);

        list_gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(GalleryActivity.this,AlbumActivity.class);
                intent.putExtra("GalleryId",mData.get(position).getiGalleryId());
                intent.putExtra("GalleryName",mData.get(position).getiName());
                intent.putExtra("UserId",UserId);
                startActivity(intent);
            }
        });



    }
    private void initview()
    {
        btn_back=(ImageView)findViewById(R.id.Inmygallery_backbtn);
        btn_new=(TextView)findViewById(R.id.Inmygallery_newbtn);
        list_gallery = (ListView) findViewById(R.id.list_gallery);

    }
    private void initDataList()
    {
        mData = new ArrayList<gallery_entity>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                GalleryJsonResult = new Upload_info().upload_Gallerys(UserId);
                if(GalleryJsonResult==null)
                {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
                else {//初始化mData
                    try {
                        for (int i = 0; i < GalleryJsonResult.length(); i++) {
                            JSONObject getJsonObj = GalleryJsonResult.getJSONObject(i);//获取json数组中的第一项
                            mData.add(new gallery_entity(R.mipmap.shupianicon, i+1,getJsonObj.getString("galleryId"),
                                    Integer.toString(i+1)+". "+getJsonObj.getString("galleryName"),getJsonObj.getString("galleryInfo") ,getJsonObj.getString("galleryCoverURL")));
                        }
                    }catch(JSONException e) {
                        e.printStackTrace();
                    }
                    Message msg = Message.obtain();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        }).start();

    }
    private void  create_new_gallery(String name,String intro){
        String new_galleryId=new Upload_info().upload_new_Gallery(UserId,name,intro);
        if(new_galleryId!=null)
        {
            Message msg = Message.obtain();
            msg.what = 1;

            //成功了还得在我的相册界面增加新的相册
            mData.add(new gallery_entity(R.mipmap.shupianicon,mData.size()+1,new_galleryId, Integer.toString(mData.size()+1)+". "+name,intro,null));
            handler.sendMessage(msg);
        }
        else {
            Message msg = Message.obtain();
            msg.what = 5;
            handler.sendMessage(msg);
        }
    }
    public class Gallery_Adapter extends BaseAdapter {
        private List<gallery_entity> mList;//数据源
        private LayoutInflater mInflater;//布局装载器对象

        // 通过构造方法将数据源与数据适配器关联起来
        // context:要使用当前的Adapter的界面对象
        public Gallery_Adapter(Context context, List<gallery_entity> list) {
            mList = list;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        //ListView需要显示的数据数量
        public int getCount() {
            return mList.size();
        }

        @Override
        //指定的索引对应的数据项
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        //指定的索引对应的数据项ID
        public long getItemId(int position) {
            return position;
        }

        @Override
        //返回每一项的显示内容
        public View getView(int position, View convertView, ViewGroup parent) {
            Gallery_Adapter.ViewHolder viewHolder;
            //如果view未被实例化过，缓存池中没有对应的缓存
            if (convertView == null) {
                viewHolder = new Gallery_Adapter.ViewHolder();
                // 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
                convertView = mInflater.inflate(R.layout.item_gallerys, null);

                //对viewHolder的属性进行赋值
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.gallery_icon);
                viewHolder.gallery_name = (TextView) convertView.findViewById(R.id.gallery_name);
                viewHolder.gallery_intro = (TextView) convertView.findViewById(R.id.gallery_introduction);

                //通过setTag将convertView与viewHolder关联
                convertView.setTag(viewHolder);
            }else{//如果缓存池中有对应的view缓存，则直接通过getTag取出viewHolder
                viewHolder = (Gallery_Adapter.ViewHolder) convertView.getTag();
            }
            // 取出gallery_entity对象
            gallery_entity bean = mList.get(position);

            // 设置控件的数据
            if(bean.getiImageURL().equals("NOCOVER")) {
                viewHolder.imageView.setImageResource(bean.getiId());
            }
            else
                Picasso.with(GalleryActivity.this).load(bean.getiImageURL()).into(viewHolder.imageView);//通过URL下载网络图片
            viewHolder.gallery_name.setText(bean.getiName());
            viewHolder.gallery_intro.setText(bean.getiIntroduction());

            return convertView;
        }
        // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
        class ViewHolder{
            public ImageView imageView;
            public TextView gallery_name;
            public TextView gallery_intro;
        }
    }

}

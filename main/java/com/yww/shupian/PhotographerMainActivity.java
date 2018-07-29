package com.yww.shupian;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yww.shupian.Util.Upload_info;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PhotographerMainActivity extends AppCompatActivity {

    private String PhotographerId;
    private String UserId;
    private String PhotographerName;
    private String City;
    private String School;
    private Bitmap PhotographerIcon;

    private ArrayList<String> picURLlist;

    private ImageButton love;
    private ImageButton contect;
    private ImageView return_back;
    private ImageView Icon;
    private TextView tv_PhotographerName;
    private TextView loved;
    private TextView tv_city;
    private TextView tv_school;

    private ImageView pic1;
    private ImageView pic2;
    private ImageView pic3;
    private ImageView more;
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                {
                    loved.setText("已关注");
                    love.setImageResource(R.mipmap.loved);
                    Toast.makeText(PhotographerMainActivity.this,"关注成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 1:
                {
                    loved.setText("已关注");
                    love.setImageResource(R.mipmap.loved);
                    Toast.makeText(PhotographerMainActivity.this,"关注失败，已经关注过了",Toast.LENGTH_SHORT).show();
                }
                break;
                case 2:
                {
                    Toast.makeText(PhotographerMainActivity.this,"关注失败，请重新关注",Toast.LENGTH_SHORT).show();
                }
                break;
                case 3:
                {
                    loved.setText("关注");
                    love.setImageResource(R.mipmap.love);
                    Toast.makeText(PhotographerMainActivity.this,"取消关注成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 4:
                {
                    Toast.makeText(PhotographerMainActivity.this,"取消关注失败",Toast.LENGTH_SHORT).show();
                }
                break;
                case 5:
                {
                    appear_three_pic();
                }
                break;
                default:
                {
                    Toast.makeText(PhotographerMainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photographer_main);

        PhotographerId= getIntent().getStringExtra("photoer_Id");
        PhotographerName=getIntent().getStringExtra("photoer_Name");
        UserId=getIntent().getStringExtra("UserId");
        City=getIntent().getStringExtra("City");
        School=getIntent().getStringExtra("School");
        PhotographerIcon=string2Bitmap(getIntent().getStringExtra("photoer_Icon"));

        initView();
        initData();
        initViewListener();
    }
    private void initView()
    {
        tv_city=(TextView)findViewById(R.id.textView2);
        tv_school=(TextView)findViewById(R.id.textView4);
        love = (ImageButton)findViewById(R.id.love);
        contect = (ImageButton)findViewById(R.id.contect);
        Icon=(ImageView)findViewById(R.id.imageView33);
        return_back = (ImageView) findViewById(R.id.return_0);
        tv_PhotographerName = (TextView)findViewById(R.id.textView);
        loved = (TextView)findViewById(R.id.textView6);
        pic1=(ImageView)findViewById(R.id.Inphotomain_pic1);
        pic2=(ImageView)findViewById(R.id.Inphotomain_pic2);
        pic3=(ImageView)findViewById(R.id.Inphotomain_pic3);
        more=(ImageView)findViewById(R.id.Inphotomain_more);
    }
    private void initData()
    {
        tv_PhotographerName.setText(PhotographerName);
        tv_city.setText(City);
        tv_school.setText(School);
        Icon.setImageBitmap(PhotographerIcon);
        //下载三张图片展示
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray result = new Upload_info().upload_three_picUrl(PhotographerId);
                    picURLlist=new ArrayList<String>();
                    if(result!=null) {
                        for(int i=0;i<result.length();i++)
                        {
                            picURLlist.add(result.getJSONObject(i).getString("picURL"));
                        }
                    }
                    else
                    {
                        picURLlist.add("http://pic6.nipic.com/20100414/3871838_093646015032_2.jpg");
                        picURLlist.add("http://pic15.nipic.com/20110616/2707401_224254882000_2.jpg");
                        picURLlist.add("http://pic138.nipic.com/file/20170816/22554547_123534011000_2.jpg");
                    }
                    Message msg = Message.obtain();
                    msg.what = 5;
                    handler.sendMessage(msg);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void initViewListener()
    {
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PhotographerMainActivity.this,UsergalleryActivity.class);
                intent.putExtra("PhotographerId",PhotographerId);
                startActivity(intent);
            }
        });
        return_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //此处和“喜欢”导向的activity相关
                if(loved.getText().toString().matches("关注")){
                    add_friend(PhotographerId);
                }
                else{
                    delete_friend(PhotographerId);
                }

            }
        });

        contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //此处和“找ta”导向的activity相关
                Intent intent=new Intent(PhotographerMainActivity.this,ChatActivity.class);
                intent.putExtra("MyuserId",UserId);
                intent.putExtra("FriendId",PhotographerId);
                intent.putExtra("FriendName",PhotographerName);
                startActivity(intent);
            }
        });
    }
    private void add_friend(final String friendId)
    {
                if(!friendId.equals(""))
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int result;
                            result=new Upload_info().upload_addfriend(UserId,friendId);
                            if(result==1)//添加好友成功
                            {
                                Message msg = Message.obtain();
                                msg.what = 0;
                                handler.sendMessage(msg);
                            }
                            else if(result==2)//已经是好友
                            {
                                Message msg = Message.obtain();
                                msg.what = 1;
                                handler.sendMessage(msg);
                            }
                            else//添加好友失败
                            {
                                Message msg = Message.obtain();
                                msg.what = 2;
                                handler.sendMessage(msg);
                            }
                        }
                    }).start();
                }
    }
    private void delete_friend(final String friendId)
    {
        if(!friendId.equals(""))
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int result;
                    result=new Upload_info().upload_deletefriend(UserId,friendId);
                    if(result==1)//删除好友成功
                    {
                        Message msg = Message.obtain();
                        msg.what = 3;
                        handler.sendMessage(msg);
                    }
                    else//删除好友失败
                    {
                        Message msg = Message.obtain();
                        msg.what = 4;
                        handler.sendMessage(msg);
                    }
                }
            }).start();
        }
    }
    private void appear_three_pic()
    {
        if(picURLlist.size()==1)
        {
            Picasso.with(this).load(picURLlist.get(0)).into(pic1);//通过URL下载网络图片
        }
        else if(picURLlist.size()==2)
        {
            Picasso.with(this).load(picURLlist.get(0)).into(pic1);//通过URL下载网络图片
            Picasso.with(this).load(picURLlist.get(1)).into(pic2);//通过URL下载网络图片
        }
        else//==3
        {
            Picasso.with(this).load(picURLlist.get(0)).into(pic1);//通过URL下载网络图片
            Picasso.with(this).load(picURLlist.get(1)).into(pic2);//通过URL下载网络图片
            Picasso.with(this).load(picURLlist.get(2)).into(pic3);//通过URL下载网络图片
        }
    }
    private Bitmap string2Bitmap(String st)
    {
        // OutputStream out;
        Bitmap bitmap = null;
        try
        {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}

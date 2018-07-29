package com.yww.shupian;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yww.shupian.PictureAbout.AlbumActivity_user;
import com.yww.shupian.PictureAbout.ItemEntity;
import com.yww.shupian.Util.Upload_info;
import com.stone.pile.libs.PileLayout;
import com.yww.shupian.Util.Utils;
import com.yww.shupian.Widget.FadeTransitionImageView;
import com.yww.shupian.Widget.HorizontalTransitionLayout;
import com.yww.shupian.Widget.VerticalTransitionLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UsergalleryActivity extends AppCompatActivity {

    private String photographerId;
    private View positionView;
    private PileLayout pileLayout;
    private List<ItemEntity> dataList;

    private int lastDisplay = -1;

    private ObjectAnimator transitionAnimator;
    private float transitionValue;
    private HorizontalTransitionLayout countryView, temperatureView;
    private VerticalTransitionLayout addressView, timeView;
    private FadeTransitionImageView bottomView;
    private Animator.AnimatorListener animatorListener;
    private TextView descriptionView;
    private PileLayout.Adapter mAdapter;
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://下载成功
                {
                    mAdapter.notifyAll();
                }
                break;
                default:
                {
                    Toast.makeText(UsergalleryActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usergallery);
        photographerId=getIntent().getStringExtra("PhotographerId");

        positionView = findViewById(R.id.positionView);
        countryView = (HorizontalTransitionLayout) findViewById(R.id.countryView);
        temperatureView = (HorizontalTransitionLayout) findViewById(R.id.temperatureView);
        pileLayout = (PileLayout) findViewById(R.id.pileLayout);
        addressView = (VerticalTransitionLayout) findViewById(R.id.addressView);
        descriptionView = (TextView) findViewById(R.id.descriptionView);
        timeView = (VerticalTransitionLayout) findViewById(R.id.timeView);
        bottomView = (FadeTransitionImageView) findViewById(R.id.bottomImageView);
        // 0. 给descriptionView增加滚轮效果android.text.method.ScrollingMovementMethod.getInstance()
        descriptionView.setMovementMethod(ScrollingMovementMethod.getInstance());
        // 1. 状态栏侵入
        boolean adjustStatusHeight = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            adjustStatusHeight = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }

        // 2. 状态栏占位View的高度调整
        String brand = Build.BRAND;
        if (brand.contains("Xiaomi")) {
            Utils.setXiaomiDarkMode(this);
        } else if (brand.contains("Meizu")) {
            Utils.setMeizuDarkMode(this);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            adjustStatusHeight = false;
        }
        if (adjustStatusHeight) {
            adjustStatusBarHeight(); // 调整状态栏高度
        }

        animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                countryView.onAnimationEnd();
                temperatureView.onAnimationEnd();
                addressView.onAnimationEnd();
                bottomView.onAnimationEnd();
                timeView.onAnimationEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };


        // 3. PileLayout绑定Adapter
        initDataList();
        mAdapter=new PileLayout.Adapter() {
            @Override
            public int getLayoutId() {
                return R.layout.item_layout;
            }

            @Override
            public void bindView(View view, int position) {
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                if (viewHolder == null) {
                    viewHolder = new ViewHolder();
                    viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);
                    view.setTag(viewHolder);
                }

                Glide.with(UsergalleryActivity.this).load(dataList.get(position).getCoverImageUrl()).into(viewHolder.imageView);
            }

            @Override
            public int getItemCount() {
                return dataList.size();
            }

            @Override
            public void displaying(int position) {
                descriptionView.setText(dataList.get(position).getgalleryinro());
                if (lastDisplay < 0) {
                    initSecene(position);
                    lastDisplay = 0;
                } else if (lastDisplay != position) {
                    transitionSecene(position);
                    lastDisplay = position;
                }
            }

            @Override
            public void onItemClick(View view, int position) {
                super.onItemClick(view, position);
                Intent intent=new Intent(UsergalleryActivity.this, AlbumActivity_user.class);
                intent.putExtra("UserId",photographerId);
                intent.putExtra("GalleryId",dataList.get(position).getGalleryid());
                intent.putExtra("GalleryName",dataList.get(position).getgalleryname());
                startActivity(intent);
            }
        };
        pileLayout.setAdapter(mAdapter);
    }

    private void initSecene(int position) {
        countryView.firstInit(dataList.get(position).getgalleryname());
        temperatureView.firstInit(dataList.get(position).getTemperature());
        addressView.firstInit(dataList.get(position).getAddress());
        bottomView.firstInit(dataList.get(position).getMapImageUrl());
        timeView.firstInit(dataList.get(position).getTime());
    }

    private void transitionSecene(int position) {
        if (transitionAnimator != null) {
            transitionAnimator.cancel();
        }

        countryView.saveNextPosition(position, dataList.get(position).getgalleryname() + "-" + position);
        temperatureView.saveNextPosition(position, dataList.get(position).getTemperature());
        addressView.saveNextPosition(position, dataList.get(position).getAddress());
        bottomView.saveNextPosition(position, dataList.get(position).getMapImageUrl());
        timeView.saveNextPosition(position, dataList.get(position).getTime());

        transitionAnimator = ObjectAnimator.ofFloat(this, "transitionValue", 0.0f, 1.0f);
        transitionAnimator.setDuration(300);
        transitionAnimator.start();
        transitionAnimator.addListener(animatorListener);

    }

    /**
     * 调整沉浸状态栏
     */
    private void adjustStatusBarHeight() {
        int statusBarHeight = Utils.getStatusBarHeight(this);
        ViewGroup.LayoutParams lp = positionView.getLayoutParams();
        lp.height = statusBarHeight;
        positionView.setLayoutParams(lp);
    }


    /**
     * 从asset读取文件json数据
     */
    private void initDataList() {
        dataList = new ArrayList<>();

            /*
            InputStream in = getAssets().open("preset.config");
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            String jsonStr = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.optJSONArray("result");*/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONArray jsonArray = new Upload_info().upload_Gallerys_user(photographerId);
                        if (null != jsonArray) {
                            jsonArray=TransforJSONArray(jsonArray);
                            int len = jsonArray.length();
                            for (int j = 0; j < 3; j++) {
                                for (int i = 0; i < len; i++) {
                                    JSONObject itemJsonObject = jsonArray.getJSONObject(i);
                                    ItemEntity itemEntity = new ItemEntity(itemJsonObject);
                                    itemEntity.setGalleryid(itemJsonObject.getString("galleryid"));
                                    dataList.add(itemEntity);
                                }
                            }

                        } else {
                            try {
                                InputStream in = getAssets().open("preset.config");
                                int size = in.available();
                                byte[] buffer = new byte[size];
                                in.read(buffer);
                                String jsonStr = new String(buffer, "UTF-8");
                                JSONObject jsonObject = new JSONObject(jsonStr);
                                jsonArray = jsonObject.optJSONArray("result");
                                int len = jsonArray.length();
                                for (int j = 0; j < 3; j++) {
                                    for (int i = 0; i < len; i++) {
                                        JSONObject itemJsonObject = jsonArray.getJSONObject(i);
                                        ItemEntity itemEntity = new ItemEntity(itemJsonObject);
                                        dataList.add(itemEntity);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

    }

    /**
     * 属性动画
     */
    public void setTransitionValue(float transitionValue) {
        this.transitionValue = transitionValue;
        countryView.duringAnimation(transitionValue);
        temperatureView.duringAnimation(transitionValue);
        addressView.duringAnimation(transitionValue);
        bottomView.duringAnimation(transitionValue);
        timeView.duringAnimation(transitionValue);
    }

    public float getTransitionValue() {
        return transitionValue;
    }

    class ViewHolder {
        ImageView imageView;
    }
    private JSONArray TransforJSONArray(JSONArray pre)
    {
        JSONArray re=new JSONArray();
        try {
            for (int i = 0; i < pre.length(); i++) {
                JSONObject pretemp = pre.getJSONObject(i);
                JSONObject retemp=new JSONObject();
                retemp.put("galleryid",pretemp.getString("galleryId"));
                retemp.put("galleryname",pretemp.getString("galleryName"));
                retemp.put("temperature","25-39℃");
                retemp.put("coverImageUrl",pretemp.getString("galleryCoverURL"));
                retemp.put("address","China Beijing");
                retemp.put("galleryintro",pretemp.getString("galleryInfo"));
                retemp.put("time","6.10~3.31      8:00-20:00");
                retemp.put("mapImageUrl",pretemp.getString("galleryCoverURL"));

                re.put(retemp);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return re;
    }
}

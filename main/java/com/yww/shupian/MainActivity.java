package com.yww.shupian;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
                           implements  View.OnClickListener,
                                        ShowFragment.OnFragmentInteractionListener,
                                        PhotographersFragment.OnFragmentInteractionListener,
                                        MessageFragment.OnFragmentInteractionListener,
                                        SettingFragment.OnFragmentInteractionListener{

    /**
     * 用于展示精美照片的Fragment
     */
    private ShowFragment showFragment;

    /**
     * 用于展示摄像师的Fragment
     */

    private PhotographersFragment photographersFragment;
    /**
     * 用于展示个人消息的Fragment
     */

    private MessageFragment messageFragment;
    /**
     * 用于展示设置的Fragment
     */
    private SettingFragment settingFragment;

    /**
     * 展示照片界面布局
     */
    private View showLayout;

    /**
     * 展示摄像师界面布局
     */
    private View photographersLayout;

    /**
     * 消息界面布局
     */
    private View messageLayout;

    /**
     * 设置界面布局
     */
    private View settingLayout;



    /**
     * 在Tab布局上显示联系人图标的控件
     */
    private ImageView showImage;

    /**
     * 在Tab布局上显示动态图标的控件
     */
    private ImageView photographersImage;
    /**
     * 在Tab布局上显示消息图标的控件
     */
    private ImageView messageImage;
    /**
     * 在Tab布局上显示设置图标的控件
     */
    private ImageView settingImage;

    /**
     * 在Tab布局上显示照片展示标题的控件
     */
    private TextView showText;
    /**
     * 在Tab布局上显示摄像师展示标题的控件
     */
    private TextView photographersText;
    /**
     * 在Tab布局上显示消息标题的控件
     */
    private TextView messageText;
    /**
     * 在Tab布局上显示设置标题的控件
     */
    private TextView settingText;

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    public String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        // 初始化布局元素
        initViews();

        UserId=getIntent().getStringExtra("UserID");//获取用户ID
        fragmentManager = getFragmentManager();
        // 第一次启动时选中第0个tab
        setTabSelection(0);
    }

    /**
     * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
     */
    private void initViews() {
        messageLayout = findViewById(R.id.message_layout);
        showLayout = findViewById(R.id.show_layout);
        photographersLayout = findViewById(R.id.photographers_layout);
        settingLayout = findViewById(R.id.setting_layout);
        messageImage = (ImageView) findViewById(R.id.message_image);
        showImage = (ImageView) findViewById(R.id.show_image);
        photographersImage = (ImageView) findViewById(R.id.photographers_image);
        settingImage = (ImageView) findViewById(R.id.setting_image);
        messageText = (TextView) findViewById(R.id.message_text);
        showText = (TextView) findViewById(R.id.show_text);
        photographersText = (TextView) findViewById(R.id.photographers_text);
        settingText = (TextView) findViewById(R.id.setting_text);
        messageLayout.setOnClickListener(this);
        showLayout.setOnClickListener(this);
        photographersLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_layout:
                // 当点击了消息tab时，选中第3个tab
                setTabSelection(2);
                break;
            case R.id.show_layout:
                // 当点击了联系人tab时，选中第1个tab
                setTabSelection(0);
                break;
            case R.id.photographers_layout:
                // 当点击了动态tab时，选中第2个tab
                setTabSelection(1);
                break;
            case R.id.setting_layout:
                // 当点击了设置tab时，选中第4个tab
                setTabSelection(3);
                break;
            default:
                break;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     *            每个tab页对应的下标。0表示show，1表示photographers，2表示message，3表示settings。
     */
    private void setTabSelection(int index) {
        // 每次选中之前先清除掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {

            case 0:
                // 当点击了联系人tab时，改变控件的图片和文字颜色
                showImage.setImageResource(R.drawable.ic_home_blue_24dp);
                showText.setTextColor(Color.parseColor("#007FFF"));
                if (showFragment == null) {
                    // 如果showFragment为空，则创建一个并添加到界面上
                    showFragment = new ShowFragment();
                    transaction.add(R.id.content, showFragment);
                    Bundle bundle=new Bundle();
                    bundle.putString("UserId",UserId);
                   showFragment.setArguments(bundle);
                } else {
                    // 如果showFragment不为空，则直接将它显示出来
                    transaction.show(showFragment);
                }
                break;
            case 1:
                // 当点击了动态tab时，改变控件的图片和文字颜色
                photographersImage.setImageResource(R.drawable.ic_dashboard_blue_24dp);
                photographersText.setTextColor(Color.parseColor("#007FFF"));
                if (photographersFragment == null) {
                    // 如果photographersFragment为空，则创建一个并添加到界面上
                    photographersFragment = new PhotographersFragment();
                    transaction.add(R.id.content, photographersFragment);
                    Bundle bundle=new Bundle();
                    bundle.putString("UserId",UserId);
                    photographersFragment.setArguments(bundle);
                } else {
                    // 如果photographersFragment不为空，则直接将它显示出来
                    transaction.show(photographersFragment);
                }
                break;
            case 2:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                messageImage.setImageResource(R.drawable.ic_notifications_blue_24dp);
                messageText.setTextColor(Color.parseColor("#007FFF"));
                if (messageFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.content, messageFragment);
                    Bundle bundle=new Bundle();
                    bundle.putString("UserId",UserId);
                    messageFragment.setArguments(bundle);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(messageFragment);
                }
                break;
            case 3:
            default:
                // 当点击了设置tab时，改变控件的图片和文字颜色
                settingImage.setImageResource(R.drawable.ic_view_comfy_blue_24dp);
                settingText.setTextColor(Color.parseColor("#007FFF"));
                if (settingFragment == null) {
                    // 如果SettingFragment为空，则创建一个并添加到界面上
                    settingFragment = new SettingFragment();
                    transaction.add(R.id.content, settingFragment);
                    Bundle bundle=new Bundle();
                    bundle.putString("UserId",UserId);
                    settingFragment.setArguments(bundle);
                } else {
                    // 如果SettingFragment不为空，则直接将它显示出来
                    transaction.show(settingFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection () {
        messageImage.setImageResource(R.drawable.ic_notifications_black_24dp);
        messageText.setTextColor(Color.parseColor("#82858b"));
        showImage.setImageResource(R.drawable.ic_home_black_24dp);
        showText.setTextColor(Color.parseColor("#82858b"));
        photographersImage.setImageResource(R.drawable.ic_dashboard_black_24dp);
        photographersText.setTextColor(Color.parseColor("#82858b"));
        settingImage.setImageResource(R.drawable.ic_view_comfy_black_24dp);
        settingText.setTextColor(Color.parseColor("#82858b"));
    }


    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (showFragment != null) {
            transaction.hide(showFragment);
        }
        if (photographersFragment != null) {
            transaction.hide(photographersFragment);
        }
        if (settingFragment != null) {
            transaction.hide(settingFragment);
        }
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this,"交流,角楼",Toast.LENGTH_LONG).show();
    }

}


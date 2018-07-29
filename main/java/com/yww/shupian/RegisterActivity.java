package com.yww.shupian;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    private static String TAG = "RegisterActivity";
    private LinearLayout mMyRegister;
    private EditText mUserET;
    private EditText mPassWordET1;
    private EditText mPassWordET2;
    private EditText mCityET;
    private EditText mSchoolET;
    private Button mSubmitBtn;
    private ProgressBar mLoadingPB;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 200){//注册成功，转到LoginActivity
                toast("注册成功");
                finish();
            }
            else if(msg.what==201)
            {
                mUserET.setText("");
                mPassWordET1.setText("");
                mPassWordET2.setText("");
                mLoadingPB.setVisibility(View.GONE);
                toast("该用户名已经被注册过了，请更换用户名重新注册！");
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initListener();
    }
    private void initView(){
        mMyRegister=(LinearLayout) findViewById(R.id.MyRegister);
        this.setAlphaAnimation(mMyRegister);
        mUserET=(EditText) findViewById(R.id.R_user);
        mPassWordET1=(EditText) findViewById(R.id.R_password_input1);
        mPassWordET2=(EditText) findViewById(R.id.R_password_input2);
        mCityET=(EditText) findViewById(R.id.R_city_input);
        mSchoolET=(EditText) findViewById(R.id.R_school_input);
        mSubmitBtn=(Button)findViewById(R.id.R_submitbtn);
        mLoadingPB=(ProgressBar)findViewById(R.id.R_register_pb);

    }
    private void setAlphaAnimation(View v) {
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(2000);
        v.startAnimation(aa);
    }
    private void initListener() {
        this.mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingPB.setVisibility(View.VISIBLE);
                beginRegister();
            }
        });
    }
    private void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private  void beginRegister(){
        if(mUserET.getText().toString().equals("")||mPassWordET1.getText().toString().equals("")||mPassWordET2.getText().toString().equals(""))
        {
            mLoadingPB.setVisibility(View.GONE);
            toast("用户名和密码不能为空，请输入用户名和密码！");
        }
        else if(!mPassWordET1.getText().toString().equals(mPassWordET2.getText().toString()))
        {
            mLoadingPB.setVisibility(View.GONE);
            mPassWordET1.setText("");
            mPassWordET2.setText("");
            toast("两次输入的密码不一致，请重新输入！");
        }
        else
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connection = null;
                    JSONObject requestJson = new JSONObject(); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据

                    try {
                        try{
                            requestJson.put("account",mUserET.getText().toString());
                            requestJson.put("password",mPassWordET1.getText().toString());
                            requestJson.put("city",mCityET.getText().toString());
                            requestJson.put("school",mSchoolET.getText().toString());
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        byte[] data = requestJson.toString().getBytes();//获得请求体
                        URL url = new URL("http://192.168.137.1:8080/ServletFirst/RegisterServlet"); // 声明一个URL
                        connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
                        connection.setRequestMethod("POST"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
                        connection.setConnectTimeout(8000); // 设置连接建立的超时时间
                        connection.setReadTimeout(8000); // 设置网络报文收发超时时间
                        connection.setUseCaches(false);               //使用Post方式不能使用缓存
                        //设置请求体的类型是文本类型
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        //设置请求体的长度
                        connection.setRequestProperty("Content-Length", String.valueOf(data.length));
                        //获得输出流，向服务器写入数据
                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(data);

                        InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null){
                            response.append(line);
                        }

                        try {
                            JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                            String resCode;
                            resCode=resultJson.getString("resCode");
                            Message msg = new Message();
                            if(resCode.equals("200"))//注册成功，跳转到登陆界面
                            {
                                msg.what = 200;
                            }
                            else if(resCode.equals("201"))//注册失败，用户名已经被注册过了，重新注册
                            {
                                msg.what=201;
                            }
                            else {
                                Log.e(TAG,"Register fulure:resCode!=200 or 201\n");
                            }
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}

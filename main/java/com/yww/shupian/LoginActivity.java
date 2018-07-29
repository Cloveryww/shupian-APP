package com.yww.shupian;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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

public class LoginActivity extends AppCompatActivity {

    private static String TAG = "LoginActivity";
    private String UserID = "";
    private LinearLayout mMyLogin;
    private EditText mUserET;
    private EditText mPassWordET;
    private Button mRegisterBtn;
    private Button mLoginBtn;
    private ProgressBar mLoadingPB;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 200) {//登陆成功，转到MainActivity
                UserID = msg.obj.toString();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("UserID", UserID);
                toast("登陆成功");
                startActivity(intent);
                finish();
            } else if (msg.what == 201)//登录失败，密码错误
            {
                mPassWordET.setText("");
                mLoadingPB.setVisibility(View.GONE);
                toast("密码错误，请输入正确的密码！");
            } else if (msg.what == 202)//登录失败，账号不存在
            {
                mUserET.setText("");
                mPassWordET.setText("");
                mLoadingPB.setVisibility(View.GONE);
                toast("不存在此用户名，请输入正确的用户名或重新注册用户！");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();

    }

    private void initView() {
        mMyLogin = (LinearLayout) findViewById(R.id.Mylogin);
        this.setAlphaAnimation(mMyLogin);
        mUserET = (EditText) findViewById(R.id.user_edit);
        mPassWordET = (EditText) findViewById(R.id.password_edit);
        mRegisterBtn = (Button) findViewById(R.id.register_btn);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mLoadingPB = (ProgressBar) findViewById(R.id.login_pb);
        return;
    }

    private void setAlphaAnimation(View v) {
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(2000);
        v.startAnimation(aa);
    }

    private void initListener() {
        this.mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterRegisterActivity();
            }
        });
        this.mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingPB.setVisibility(View.VISIBLE);
                beginLogin();
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void enterRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void beginLogin() {
        if (mUserET.getText().toString().equals("") || mPassWordET.getText().toString().equals("")) {
            mLoadingPB.setVisibility(View.GONE);
            toast("用户名和密码不能为空，请输入用户名和密码！");
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connection = null;
                    JSONObject requestJson = new JSONObject(); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据

                    try {
                        try {
                            requestJson.put("account", mUserET.getText().toString());
                            requestJson.put("password", mPassWordET.getText().toString());
                            requestJson.put("ReqType","Login");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        byte[] data = requestJson.toString().getBytes();//获得请求体
                        URL url = new URL("http://192.168.137.1:8080/ServletFirst/LoginServlet"); // 声明一个URL
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
                        outputStream.close();

                            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        try {
                            JSONObject resultJson = new JSONObject(response.toString()); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据
                            String resCode;
                            resCode = resultJson.getString("resCode");
                            Message msg = new Message();
                            if (resCode.equals("200"))//登陆成功，跳转到首页
                            {
                                msg.what = 200;
                                msg.obj = resultJson.getString("userId");
                            } else if (resCode.equals("202")) //账号错误，重新输入
                            {
                                msg.what = 202;
                            } else if (resCode.equals("201")) //密码错误，重新输入
                            {
                                msg.what = 201;
                            } else {
                                Log.e(TAG, "Login fulure:resCode!=200 or 201 or 202\n");
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

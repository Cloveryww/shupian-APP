package com.yww.shupian;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yww.shupian.ChatAbout.MsgAdapter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity{
        private static final String HOST = "192.168.137.1";//服务器地址
        private static final int PORT = 8888;//连接端口号
        private List<Msg> msgList=new ArrayList<>();
        private EditText inputText;
        private TextView tv_friendName;
        private String MyuserId;
        private String FriendId;
        private String FriendName;
        private boolean isStartRecieveMsg;
        private Socket mSocket;

        private Button send_btn;
        private ImageView back_btn;
        private RecyclerView msgRecyclerView;
        private MsgAdapter adapter;

        protected BufferedReader mReader;//BufferedWriter 用于推送消息
        protected BufferedWriter mWriter;//BufferedReader 用于接收消息
        private String  sentcontent=null;
        public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://自己发送的消息
                    try {
                        /*
                        //将handler中发送过来的消息创建json对象
                        JSONObject json = new JSONObject((String)msg.obj);
                        mConsoleStr.append(json.getString("from")+":" +json.getString("msg")+"  "+getTime(System.currentTimeMillis())+"\n");
                        //将json数据显示在TextView中
                        mConsoleTxt.setText(mConsoleStr);*/
                        Msg recmsg=new Msg((String) msg.obj+"\n"+getTime(System.currentTimeMillis()),Msg.TYPE_SENT);
                        msgList.add(recmsg);
                        adapter.notifyItemInserted(msgList.size()-1);           //调用适配器的notifyItemInserted()用于通知列表有新的数据插入，这样新增的一条消息才能在RecyclerView中显示
                        msgRecyclerView.scrollToPosition(msgList.size()-1);     //调用scrollToPosition()方法将显示的数据定位到最后一行，以保证可以看到最后发出的一条消息
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1://接受的消息
                    try{
                        JSONObject json = new JSONObject((String)msg.obj);
                        String tempmsd=json.getString("from")+":" +json.getString("msg")+"\n"+getTime(System.currentTimeMillis())+"\n";
                        Msg recmsg=new Msg(tempmsd,Msg.TYPE_RECEIVED);
                        msgList.add(recmsg);
                        adapter.notifyItemInserted(msgList.size()-1);           //调用适配器的notifyItemInserted()用于通知列表有新的数据插入，这样新增的一条消息才能在RecyclerView中显示
                        msgRecyclerView.scrollToPosition(msgList.size()-1);     //调用scrollToPosition()方法将显示的数据定位到最后一行，以保证可以看到最后发出的一条消息
                    } catch (Exception e) {
                        e.printStackTrace();
                 }
                 break;
                default:
                    break;
            }
        }
    };

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chat);
            MyuserId=getIntent().getStringExtra("MyuserId");
            FriendId=getIntent().getStringExtra("FriendId");
            FriendName=getIntent().getStringExtra("FriendName");
                                                                   //初始化消息数据
            inputText=(EditText)findViewById(R.id.input_text);
            tv_friendName=(TextView)findViewById(R.id.Inchat_friendName);
            tv_friendName.setText(FriendName);
            send_btn=(Button)findViewById(R.id.send);
            back_btn=(ImageView)findViewById(R.id.Inchat_backbtn);
            msgRecyclerView=(RecyclerView)findViewById(R.id.msg_recycler_view);

            LinearLayoutManager layoutManager=new LinearLayoutManager(this);    //LinearLayoutLayout即线性布局，创建对象后把它设置到RecyclerView当中
            msgRecyclerView.setLayoutManager(layoutManager);

            adapter=new MsgAdapter(msgList);                                    //创建MsgAdapter的实例并将数据传入到MsgAdapter的构造函数中
            msgRecyclerView.setAdapter(adapter);
            back_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sentcontent="Stop socket";
                    send();//向服务器发送消息
                    isStartRecieveMsg=false;//关闭Socket链接
                    finish();
                }
            });

            send_btn.setOnClickListener(new View.OnClickListener(){                 //发送按钮点击事件
                @Override
                public void onClick(View v){
                    sentcontent=inputText.getText().toString();              //获取EditText中的内容
                    if(!"".equals(sentcontent)){                                    //内容不为空则创建一个新的Msg对象，并把它添加到msgList列表中
                        if (mSocket.isConnected()) {//如果服务器连接
                            if (!mSocket.isOutputShutdown()) {//如果输出流没有断开
                                send();//向服务器发送消息
                                inputText.setText("");                                  //调用EditText的setText()方法将输入的内容清空
                                Message message=new Message();
                                message.obj=sentcontent;
                                message.what=0;//发送的消息
                                mHandler.sendMessage(message);//通知UI更新
                            }
                        }
                    }
                }
            });
            //新建一个线程，用于初始化socket和检测是否有接收到新的消息
            initSocket();
    }

    /**
     * 初始化socket
     */
    private void initSocket() {
        //新建一个线程，用于初始化socket和检测是否有接收到新的消息
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    isStartRecieveMsg = true;
                    mSocket = new Socket(HOST, PORT);
                    mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "utf-8"));
                    mWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), "utf-8"));
                    //先注册自己的SocktID
                    sentcontent="Register SocketId";
                    send();
                    while(isStartRecieveMsg) {
                        if(mReader.ready()) {
              /*读取一行字符串，读取的内容来自于客户机
              reader.readLine()方法是一个阻塞方法，
              从调用这个方法开始，该线程会一直处于阻塞状态，
              直到接收到新的消息，代码才会往下走*/
                            String data = mReader.readLine();
                            //handler发送消息，在handleMessage()方法中接收
                            mHandler.obtainMessage(1, data).sendToTarget();
                        }
                        Thread.sleep(200);
                    }
                    mSocket.getInputStream().close();
                    mWriter.close();
                    mReader.close();
                    mSocket.close();
                    System.out.println("Socket has close+++++++++++++++");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    /**
     * 发送
     */
    private void send() {
        new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {
                sendMsg();
                return null;
            }
        }.execute();
    }
    /**
     * 发送消息
     */
    protected void sendMsg() {
        try {
            JSONObject json = new JSONObject();
            json.put("from",MyuserId);
            json.put("to", FriendId);
            json.put("msg", sentcontent);
            mWriter.write(json.toString()+"\n");
            mWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class Msg {
        public static final int TYPE_RECEIVED=0;
        public static final int TYPE_SENT=1;
        private String content;
        private int type;
        public Msg(String content,int type){
            this.content=content;
            this.type=type;
        }
        public String getContent(){
            return content;
        }

        public int getType(){
            return type;
        }
    }
    private static String getTime(long millTime) {
        Date d = new Date(millTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }
}

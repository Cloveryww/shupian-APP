package com.yww.shupian;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yww.shupian.ChatAbout.item_friend;
import com.yww.shupian.Util.Upload_info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    private String UserId;
    private  View view;

    private ListView list_friends;
    private ArrayList<item_friend> mData = null;
    private JSONArray FriendsJsonResult;
    private  Friend_Adapter myAdapter;



    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                {
                    Toast.makeText(getActivity(),"好友列表为空",Toast.LENGTH_SHORT).show();
                }
                break;
                case 1:
                {
                    myAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(),"好友列表加载成功",Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                {
                    Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    };

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            UserId = getArguments().getString("UserId");//SettingsSegment获取UserId
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_message, container, false);
        initview();
        initDataList();//初始化mData
        //设置ListView的数据适配器
        myAdapter=new Friend_Adapter(getActivity(),mData);
        list_friends.setAdapter(myAdapter);

        list_friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),ChatActivity.class);
                intent.putExtra("MyuserId",UserId);
                intent.putExtra("FriendId",mData.get(position).getfriendId());
                intent.putExtra("FriendName",mData.get(position).getfriendname());
                startActivity(intent);
            }
        });



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class Friend_Adapter extends BaseAdapter {
        private List<item_friend> mList;//数据源
        private LayoutInflater mInflater;//布局装载器对象

        // 通过构造方法将数据源与数据适配器关联起来
        // context:要使用当前的Adapter的界面对象
        public Friend_Adapter(Context context, List<item_friend> list) {
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
            ViewHolder viewHolder;
            //如果view未被实例化过，缓存池中没有对应的缓存
            if (convertView == null) {
                viewHolder = new ViewHolder();
                // 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
                convertView = mInflater.inflate(R.layout.item_friends, null);

                //对viewHolder的属性进行赋值
                viewHolder.friendIcon = (ImageView) convertView.findViewById(R.id.friend_icon);
                viewHolder.friendId = (TextView) convertView.findViewById(R.id.friendId);
                viewHolder.friendName = (TextView) convertView.findViewById(R.id.friendName);

                //通过setTag将convertView与viewHolder关联
                convertView.setTag(viewHolder);
            }else{//如果缓存池中有对应的view缓存，则直接通过getTag取出viewHolder
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // 取出gallery_entity对象
            item_friend bean = mList.get(position);

            // 设置控件的数据
            if(!bean.gethasIcon()) {
                viewHolder.friendIcon.setImageResource(R.mipmap.user_icon);
            }
            else
            {
                viewHolder.friendIcon.setImageBitmap(bean.getImagebm());
            }
            viewHolder.friendId.setText("好友Id:"+bean.getfriendId());
            viewHolder.friendName.setText(bean.getfriendname());

            return convertView;
        }
        // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
        class ViewHolder{
            public ImageView friendIcon;
            public TextView friendId;
            public TextView friendName;
        }
    }
    private  void initview()
    {
        list_friends=(ListView)view.findViewById(R.id.list_friends);
    }
    private  void initDataList()
    {
        mData = new ArrayList<item_friend>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                FriendsJsonResult = new Upload_info().upload_friendslist(UserId);
                if(FriendsJsonResult==null)
                {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
                else {//初始化mData
                    try {
                        for (int i = 0; i < FriendsJsonResult.length(); i++) {
                            JSONObject getJsonObj = FriendsJsonResult.getJSONObject(i);//获取json数组中的第一项
                            if(getJsonObj.getString("friendIcon").equals("FriendNOIcon"))
                            {
                                mData.add(new item_friend(getJsonObj.getString("friendId"),getJsonObj.getString("friendName"),null,false));
                            }
                            else//String转bitmap
                            {
                                Bitmap temp=string2Bitmap(getJsonObj.getString("friendIcon"));
                                mData.add(new item_friend(getJsonObj.getString("friendId"),getJsonObj.getString("friendName"),temp,true));
                            }
                        }
                    }catch(JSONException e) {
                        e.printStackTrace();
                    }
                    Message msg = Message.obtain();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        }).start();
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

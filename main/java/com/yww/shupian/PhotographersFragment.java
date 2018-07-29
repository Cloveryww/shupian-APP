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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.yww.shupian.Util.Upload_info;
import com.yww.shupian.Widget.item_photoer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotographersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotographersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotographersFragment extends Fragment {
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
    //

    private SearchView searchView ;
    private ListView mlistView;
    private ArrayList<item_photoer> mDataList;
    private photographersAdapter mAdapter;
    private JSONArray photoerJSON;
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                {
                    Toast.makeText(getActivity(),"摄像师列表为空",Toast.LENGTH_SHORT).show();
                }
                break;
                case 1:
                {
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(),"摄像师列表加载成功",Toast.LENGTH_SHORT).show();
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
    //


    public PhotographersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotographersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotographersFragment newInstance(String param1, String param2) {
        PhotographersFragment fragment = new PhotographersFragment();
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
            UserId=getArguments().getString("UserId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_photographers, container, false);
        initView();
        searchView.setSubmitButtonEnabled(true);
        mlistView.requestFocus();
        initDatalist();
        mAdapter = new photographersAdapter(getActivity(),mDataList);
        mlistView.setAdapter(mAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                for(int i=0;i<mDataList.size();i++)
                {
                    if(mDataList.get(i).getPhotoer_name().equals(s))
                    {
                        item_photoer t=mDataList.get(i);
                        mDataList.clear();
                        mDataList.add(t);
                        mAdapter.notifyDataSetChanged();
                        return false;
                    }
                }
                Toast.makeText(getActivity(),"找不到该摄像师",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
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
    private void initView()
    {
        mlistView=(ListView)view.findViewById(R.id.search_result_list);
        searchView = (SearchView) view.findViewById(R.id.Search_photographer);
    }

    private  void initDatalist()
    {
        mDataList=new ArrayList<item_photoer>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                photoerJSON = new Upload_info().upload_photoer_info(UserId);
                if(photoerJSON==null)
                {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
                else {//初始化mData

                    try {
                        for (int i = 0; i < photoerJSON.length(); i++) {
                            JSONObject getJsonObj = photoerJSON.getJSONObject(i);//获取json数组中的第一项
                            if(getJsonObj.getString("photoer_Icon").equals("NOIcon"))
                            {
                                item_photoer t=new item_photoer(getJsonObj.getString("photoer_Id"),getJsonObj.getString("photoer_Name"),
                                        getJsonObj.getString("photoer_City"),getJsonObj.getString("photoer_School"),false);
                                JSONArray result = new Upload_info().upload_three_picUrl(getJsonObj.getString("photoer_Id"));
                                if(result!=null) {
                                    for(int m=0;m<result.length();m++)
                                    {
                                        t.getPicURLlist().add(result.getJSONObject(m).getString("picURL"));
                                    }
                                }
                                else
                                {
                                    t.getPicURLlist().add("http://pic6.nipic.com/20100414/3871838_093646015032_2.jpg");
                                    t.getPicURLlist().add("http://pic15.nipic.com/20110616/2707401_224254882000_2.jpg");
                                    t.getPicURLlist().add("http://pic138.nipic.com/file/20170816/22554547_123534011000_2.jpg");
                                }
                                mDataList.add(t);
                            }
                            else//String转bitmap
                            {
                                Bitmap temp=string2Bitmap(getJsonObj.getString("photoer_Icon"));
                                item_photoer t=new item_photoer(getJsonObj.getString("photoer_Id"),getJsonObj.getString("photoer_Name"),
                                    getJsonObj.getString("photoer_City"),getJsonObj.getString("photoer_School"),true);
                                t.setPhotoer_head_icon(temp);
                                JSONArray result = new Upload_info().upload_three_picUrl(getJsonObj.getString("photoer_Id"));
                                if(result!=null) {
                                    for(int m=0;m<result.length();m++)
                                    {
                                        t.getPicURLlist().add(result.getJSONObject(m).getString("picURL"));
                                    }
                                }
                                else
                                {
                                    t.getPicURLlist().add("http://pic6.nipic.com/20100414/3871838_093646015032_2.jpg");
                                    t.getPicURLlist().add("http://pic15.nipic.com/20110616/2707401_224254882000_2.jpg");
                                    t.getPicURLlist().add("http://pic138.nipic.com/file/20170816/22554547_123534011000_2.jpg");
                                }
                                mDataList.add(t);
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

    public class photographersAdapter extends BaseAdapter {
        private List<item_photoer> mList;//数据源
        private LayoutInflater mInflater;//布局装载器对象

        // 通过构造方法将数据源与数据适配器关联起来
        // context:要使用当前的Adapter的界面对象
        public photographersAdapter(Context context, List<item_photoer> list) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            photographersAdapter.ViewHolder viewHolder;
            //如果view未被实例化过，缓存池中没有对应的缓存
            if (convertView == null) {
                viewHolder = new photographersAdapter.ViewHolder();
                // 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
                convertView = mInflater.inflate(R.layout.item_photoer, null);

                //对viewHolder的属性进行赋值
                viewHolder.img = (ImageView) convertView.findViewById(R.id.imageView4);
                viewHolder.name = (TextView) convertView.findViewById(R.id.textView5);
                viewHolder.city = (TextView) convertView.findViewById(R.id.textView8);
                viewHolder.school = (TextView) convertView.findViewById(R.id.textView9);
                viewHolder.pic1 = (ImageView) convertView.findViewById(R.id.Inphotolist_pic1);
                viewHolder.pic2 = (ImageView) convertView.findViewById(R.id.Inphotolist_pic2);
                viewHolder.pic3 = (ImageView) convertView.findViewById(R.id.Inphotolist_pic3);

                viewHolder.index = (LinearLayout) convertView.findViewById(R.id.relativeLayout);
                viewHolder.more=(ImageView)convertView.findViewById(R.id.Inphotolist_more);

                //通过setTag将convertView与viewHolder关联
                convertView.setTag(viewHolder);
            }else{//如果缓存池中有对应的view缓存，则直接通过getTag取出viewHolder
                viewHolder = (photographersAdapter.ViewHolder) convertView.getTag();
            }
            // 取出Map对象
            item_photoer bean = mList.get(position);

            // 设置控件的数据
            if(bean.isHasIcon())//有头像
            {
                viewHolder.img.setImageBitmap(bean.getPhotoer_head_icon());
            }
            else
            {
                viewHolder.img.setImageResource(R.mipmap.love);
            }
            viewHolder.name.setText(bean.getPhotoer_name());
            viewHolder.city.setText(bean.getPhotoer_city());
            viewHolder.school.setText(bean.getPhotoer_school());

            if(bean.getPicURLlist().size()==1)
            {
                Picasso.with(getActivity()).load(bean.getPicURLlist().get(0)).into(viewHolder.pic1);//通过URL下载网络图片
            }
            else if(bean.getPicURLlist().size()==2)
            {
                Picasso.with(getActivity()).load(bean.getPicURLlist().get(0)).into(viewHolder.pic1);//通过URL下载网络图片
                Picasso.with(getActivity()).load(bean.getPicURLlist().get(1)).into(viewHolder.pic2);//通过URL下载网络图片
            }
            else//==3
            {
                Picasso.with(getActivity()).load(bean.getPicURLlist().get(0)).into(viewHolder.pic1);//通过URL下载网络图片
                Picasso.with(getActivity()).load(bean.getPicURLlist().get(1)).into(viewHolder.pic2);//通过URL下载网络图片
                Picasso.with(getActivity()).load(bean.getPicURLlist().get(2)).into(viewHolder.pic3);//通过URL下载网络图片
            }
            viewHolder.index.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),PhotographerMainActivity.class);
                    intent.putExtra("photoer_Id",mDataList.get(position).getPhotoer_id());
                    intent.putExtra("photoer_Name",mDataList.get(position).getPhotoer_name());
                    intent.putExtra("UserId",UserId);
                    intent.putExtra("City",mDataList.get(position).getPhotoer_city());
                    intent.putExtra("School",mDataList.get(position).getPhotoer_school());
                    intent.putExtra("photoer_Icon",bitmap2String(mDataList.get(position).getPhotoer_head_icon()));
                    startActivity(intent);
                }
            });
            viewHolder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(),UsergalleryActivity.class);
                    intent.putExtra("PhotographerId",mDataList.get(position).getPhotoer_id());
                    startActivity(intent);
                }
            });

            return convertView;
        }
        // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
        class ViewHolder{
            public ImageView img;
            public TextView school;
            public TextView city;
            public TextView name;
            public ImageView pic1;
            public ImageView pic2;
            public ImageView pic3;
            public LinearLayout index;
            public ImageView more;
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
    private String bitmap2String(Bitmap mBitmap) {

        if(mBitmap==null){
            Toast.makeText(getActivity(), "先选取照片",Toast.LENGTH_SHORT).show();
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

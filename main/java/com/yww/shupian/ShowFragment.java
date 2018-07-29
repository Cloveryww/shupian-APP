package com.yww.shupian;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.yww.shupian.PictureAbout.ScaleImageView;
import com.yww.shupian.PictureAbout.item_in_show_pictureInfo;
import com.yww.shupian.Util.Upload_info;
import com.huewu.pla.lib.MultiColumnListView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<String> news_urlList;
    private  View view;
    private ImageView im_news1;
    private ImageView im_news2;
    private ImageView im_news3;
    private MultiColumnListView mAdapterView = null;
    private ArrayList<item_in_show_pictureInfo> imageinfoList;
    private JSONArray picsinfoJsonArrayResult;
    private ImageGridAdapter adapter;
    private String UserId;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                {
                    Toast.makeText(getActivity(),"网络错误，下载图片失败",Toast.LENGTH_SHORT).show();
                }
                break;
                case 1:
                {
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(),"下载图片成功",Toast.LENGTH_SHORT).show();
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

    public ShowFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowFragment newInstance(String param1, String param2) {
        ShowFragment fragment = new ShowFragment();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_show, container, false);
        news_urlList=new ArrayList<String>();
        news_urlList.add("http://mp.weixin.qq.com/s/b9AkxbJcgPWXSbxGLeeHLQ");
        news_urlList.add("http://mp.weixin.qq.com/s/gNnB_48nkr2kD7pEixVpag");
        news_urlList.add("http://mp.weixin.qq.com/s/ncgGnFUxGLupD1oFMy5BqA");
        mAdapterView = (MultiColumnListView) view.findViewById(R.id.Pintrests);
        im_news1=(ImageView)view.findViewById(R.id.Innews_pic1);
        im_news2=(ImageView)view.findViewById(R.id.Innews_pic2);
        im_news3=(ImageView)view.findViewById(R.id.Innews_pic3);
        im_news1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),NewsActivity.class);
                intent.putExtra("news_url",news_urlList.get(0));
                startActivity(intent);
            }
        });
        im_news2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),NewsActivity.class);
                intent.putExtra("news_url",news_urlList.get(1));
                startActivity(intent);
            }
        });
        im_news3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),NewsActivity.class);
                intent.putExtra("news_url",news_urlList.get(2));
                startActivity(intent);
            }
        });
        imageinfoList = new ArrayList<item_in_show_pictureInfo>();
        adapter = new ImageGridAdapter(getActivity(), imageinfoList);
        mAdapterView.setAdapter(adapter);
        initimageinfoList();
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
    public class ImageGridAdapter extends BaseAdapter {
        private static final String TAG = "ImageGridAdapter";
        private static final boolean DEBUG = true;
        private ArrayList<item_in_show_pictureInfo> mImageList;
        private LayoutInflater mLayoutInflater;
        public ImageGridAdapter(Context context,
                                ArrayList<item_in_show_pictureInfo> list) {
            mImageList = list;
            mLayoutInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return mImageList.size();
        }
        public Object getItem(int arg0) {
            return null;
        }
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (DEBUG)
                Log.i(TAG, "position = " + position);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.item_image_pintrest,
                        null);
                holder = new ViewHolder();
                holder.imageView = (ScaleImageView) convertView .findViewById(R.id.ScaleimageView);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            Picasso.with(getActivity()).load(imageinfoList.get(position).getiPicURL()).into(holder.imageView);//通过URL下载网络图片
            return convertView;
        }

        class ViewHolder {
            ScaleImageView imageView;
        }
    }
    private void initimageinfoList()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                picsinfoJsonArrayResult=new Upload_info().upload_some_pictures(UserId,100);
                if(picsinfoJsonArrayResult==null)
                {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
                else {//初始化mData
                    try {
                        for (int i = 0; i < picsinfoJsonArrayResult.length(); i++) {
                            JSONObject getJsonObj = picsinfoJsonArrayResult.getJSONObject(i);//获取json数组中的第一项
                            imageinfoList.add(new item_in_show_pictureInfo(getJsonObj.getString("picId"),getJsonObj.getString("galleryId"),getJsonObj.getString("picPath")));
                        }
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://pic15.nipic.com/20110616/2707401_224254882000_2.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://pic138.nipic.com/file/20170816/22554547_123534011000_2.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://pic138.nipic.com/file/20170814/25725094_081819905296_2.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://pic138.nipic.com/file/20170814/2059536_095931176031_2.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://pic138.nipic.com/file/20170815/25700412_023451594000_2.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://pic138.nipic.com/file/20170811/21847396_114517617036_2.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://pic138.nipic.com/file/20170811/21847396_110209479034_2.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-12-18380140_455327614813449_854681840315793408_n.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-11-18380166_305443499890139_8426655762360565760_n.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-10-18382517_1955528334668679_3605707761767153664_n.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-09-18443931_429618670743803_5734501112254300160_n.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-08-18252341_289400908178710_9137908350942445568_n.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-05-18251898_1013302395468665_8734429858911748096_n.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://ww1.sinaimg.cn/large/61e74233ly1feuogwvg27j20p00zkqe7.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-02-926821_1453024764952889_775781470_n.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-28-18094719_120129648541065_8356500748640452608_n.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","https://ws1.sinaimg.cn/mw690/610dc034ly1ffwb7npldpj20u00u076z.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","https://ws1.sinaimg.cn/large/610dc034ly1ffv3gxs37oj20u011i0vk.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","https://ws1.sinaimg.cn/large/610dc034ly1fftusiwb8hj20u00zan1j.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://ww1.sinaimg.cn/large/610dc034ly1ffmwnrkv1hj20ku0q1wfu.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","https://ws1.sinaimg.cn/large/610dc034ly1ffyp4g2vwxj20u00tu77b.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","https://ws1.sinaimg.cn/large/610dc034ly1ffxjlvinj5j20u011igri.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-11-17881546_248332202297978_2420944671002853376_n.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-12-17662441_1675934806042139_7236493360834281472_n.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-13-17882785_926451654163513_7725522121023029248_n.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-14-17881962_1329090457138411_8289893708619317248_n.jpg"));
                        imageinfoList.add(new item_in_show_pictureInfo("-1","1","http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-16-17934400_1738549946443321_2924146161843437568_n.jpg"));
                        Message msg = Message.obtain();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

package com.yww.shupian;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yww.shupian.Util.Upload_info;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    private  View view;
    private View myGallery;
    private TextView mUserName;
    private TextView mUserId;
    private ImageView mUserImage;
    private  View myUniv;
    private View myStyle;
    private  View myBecomePhotographer;
    JSONObject resultJson;


    private String UserId;
    private String UserName;
    private Image UserImage;

    private Bitmap mBitmap;
    protected static Uri tempUri;
    private Uri contentUri;
    private File newFile;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                {
                    Toast.makeText(getActivity(),"头像上传成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 2:
                {
                    Toast.makeText(getActivity(),"我所在的大学设置成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 3:
                {
                    Toast.makeText(getActivity(),"我所在的大学设置失败",Toast.LENGTH_SHORT).show();
                }
                break;
                case 4:
                {
                    Toast.makeText(getActivity(),"我的拍照风格设置成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 5:
                {
                    Toast.makeText(getActivity(),"我的拍照风格设置失败",Toast.LENGTH_SHORT).show();
                }
                break;
                case 6:
                {
                    Toast.makeText(getActivity(),"成功成为摄影师",Toast.LENGTH_SHORT).show();
                }
                break;
                case 7:
                {
                    Toast.makeText(getActivity(),"已经是摄影师，不必重新申请",Toast.LENGTH_SHORT).show();
                }
                break;
                case 8:
                {
                    Toast.makeText(getActivity(),"申请成为摄影师失败，请重新申请",Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                {
                    Toast.makeText(getActivity(),"头像上传失败，请重新上传",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    };

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
        view=inflater.inflate(R.layout.fragment_setting, container, false);

        initViews();//初始化
        initViewContents();//初始化Setting界面中的信息
        myGallery.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                //响应事件
                Intent intent=new Intent(getActivity(),GalleryActivity.class);
                intent.putExtra("UserId",UserId);
                startActivity(intent);

            }
        });
        mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
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
    private void initViews() {
        myGallery=view.findViewById(R.id.InSetting_myGallery);//获取我的相册View
        mUserName=view.findViewById(R.id.InSetting_userName);
        mUserId=view.findViewById(R.id.InSetting_userId);
        mUserImage=view.findViewById(R.id.InSetting_userImage);
        myUniv=view.findViewById(R.id.InSetting_myUniv);

        myStyle=view.findViewById(R.id.InSetting_mystyle);
        myBecomePhotographer=view.findViewById(R.id.InSetting_becomePhotographer);
    }
    //更新Setting界面信息
    private void initViewContents() {

        mUserId.setText(UserId);//填充Setting界面的用户Id区域
        new Thread(new Runnable() {//网络下载线程

            @Override
            public void run() {
                // TODO Auto-generated method stub
                resultJson = new Upload_info().upload_userIcon_account(UserId);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String resCode = resultJson.getString("resCode");
                            if (resCode.equals("200"))//头像和账户下载成功
                            {
                                mUserName.setText(resultJson.getString("userAccount"));
                                Bitmap bm = string2Bitmap(resultJson.getString("userIcon"));
                                mUserImage.setImageBitmap(bm);
                            } else if (resCode.equals("201")) //下载错误
                            {
                                mUserName.setText("网络错误，无法获取用户名");
                            } else if (resCode.equals("202")) //下载成功，但用户没有个人头像
                            {
                                mUserName.setText(resultJson.getString("userAccount"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        }).start();
        //初始化我的大学
        myUniv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View dialogEdit=(LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_set_myuniv,null);
                final EditText new_et1 =dialogEdit.findViewById(R.id.dialog_myUniv) ;//设置我的大学的时候EditText
                new AlertDialog.Builder(getActivity()).setTitle("设置我所在的大学")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(dialogEdit)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final String  myuniv = new_et1.getText().toString();
                                        if (myuniv.equals("")) {
                                            Toast.makeText(getActivity().getApplicationContext(), "我所在的大学设置失败，大学名称不能为空,请重新设置", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            new Thread(new Runnable() {//网络下载线程

                                                @Override
                                                public void run() {
                                                    // TODO Auto-generated method stub
                                                    if(new Upload_info().setMyUniv(UserId,myuniv))
                                                    {
                                                        Message msg = Message.obtain();
                                                        msg.what = 2;
                                                        handler.sendMessage(msg);
                                                    }
                                                    else
                                                    {
                                                        Message msg = Message.obtain();
                                                        msg.what = 3;
                                                        handler.sendMessage(msg);
                                                    }
                                                }
                                            }).start();
                                        }
                                    }
                                }).start();

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        myStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View dialogEdit=(LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_set_mystyle,null);
                final EditText new_et1 =dialogEdit.findViewById(R.id.dialog_myStyle) ;//设置我的大学的时候EditText
                new AlertDialog.Builder(getActivity()).setTitle("设置我的拍照风格")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(dialogEdit)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final String  mystyle = new_et1.getText().toString();
                                        if (mystyle.equals("")) {
                                            Toast.makeText(getActivity().getApplicationContext(), "我的拍照风格设置失败，我的拍照风格不能为空,请重新设置", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            new Thread(new Runnable() {//网络下载线程

                                                @Override
                                                public void run() {
                                                    // TODO Auto-generated method stub
                                                    if(new Upload_info().setMySyle(UserId,mystyle))
                                                    {
                                                        Message msg = Message.obtain();
                                                        msg.what = 4;
                                                        handler.sendMessage(msg);
                                                    }
                                                    else
                                                    {
                                                        Message msg = Message.obtain();
                                                        msg.what = 5;
                                                        handler.sendMessage(msg);
                                                    }
                                                }
                                            }).start();
                                        }
                                    }
                                }).start();

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        myBecomePhotographer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int result=new Upload_info().BecomePhotographer(UserId);
                        if(result==1)//申请成功
                        {
                            Message msg = Message.obtain();
                            msg.what = 6;
                            handler.sendMessage(msg);
                        }
                        else if(result==0)
                        {
                            Message msg = Message.obtain();
                            msg.what = 7;
                            handler.sendMessage(msg);
                        }
                        else
                        {
                            Message msg = Message.obtain();
                            msg.what = 8;
                            handler.sendMessage(msg);
                        }
                    }
                }).start();
            }
        });


    }
    //上传用户头像
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("添加图片");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent;
                        if (Build.VERSION.SDK_INT < 19) {
                            openAlbumIntent = new Intent(
                                    Intent.ACTION_GET_CONTENT);
                            openAlbumIntent.setType("image/*");
                        } else {
                            openAlbumIntent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        }
                        //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Toast.makeText(getActivity(), "目前不支持相机拍照上传图片", Toast.LENGTH_SHORT).show();
                        // startCamera();
                            /*Intent openCameraIntent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            tempUri = Uri.fromFile(new File(Environment
                                    .getExternalStorageDirectory(), "temp_image.jpg"));
                            // 将拍照所得的相片保存到SD卡根目录
                            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                            startActivityForResult(openCameraIntent, TAKE_PICTURE);
                            break;
                            */
                }
            }
        });
        builder.show();
    }
    private void startCamera() {
        File imagePath = new File(Environment.getExternalStorageDirectory(), "images");
        if (!imagePath.exists()) imagePath.mkdirs();
        newFile = new File(imagePath, "default_image.jpg");

        //第二参数是在manifest.xml定义 provider的authorities属性
        contentUri = FileProvider.getUriForFile(getActivity(), "com.example.android.fileprovider", newFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //兼容版本处理，因为 intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION) 只在5.0以上的版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ClipData clip =
                    ClipData.newUri(getActivity().getContentResolver(), "A photo", contentUri);
            intent.setClipData(clip);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList =
                    getActivity().getPackageManager()
                            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                getActivity().grantUriPermission(packageName, contentUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    cutImage(tempUri); // 对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // 对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }
    /**
     * 裁剪图片方法实现
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");//这里图片是方形的
            mUserImage.setImageBitmap(mBitmap);//显示图片
            UploadUserIcon();//上传该头像图片到服务器的代码
        }
    }
    private void UploadUserIcon()
    {
        if(mBitmap!=null) {
            final String imgStr = bitmap2String();

            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Boolean upLoadResult = new Upload_info().upLoadPhoto(UserId, imgStr, -1);
                    System.out.println(upLoadResult.toString());
                    Message msg = Message.obtain();
                    if (upLoadResult)
                        msg.what = 1;
                    else
                        msg.what = 2;
                    handler.sendMessage(msg);
                }
            }).start();
        }
        return ;
    }
    private String bitmap2String() {

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

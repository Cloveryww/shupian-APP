package com.yww.shupian;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.yww.shupian.Util.Upload_info;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class UppicActivity extends AppCompatActivity {
        private String userId;
        private ImageView mImage;
        private Button mAddImage;
        private Bitmap mBitmap;
        protected static final int CHOOSE_PICTURE = 0;
        protected static final int TAKE_PICTURE = 1;
        protected static Uri tempUri;
        private static final int CROP_SMALL_PICTURE = 2;
        private Uri contentUri;
        private File newFile;
        Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                {
                    Toast.makeText(UppicActivity.this,"头像上传成功",Toast.LENGTH_SHORT).show();;
                }
                break;
                default:
                {
                    Toast.makeText(UppicActivity.this,"头像上传失败，请重新上传",Toast.LENGTH_SHORT).show();;
                }
                break;
            }
        }

    };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_uppic);
            initUI();
            initListeners();
        }

        private void initUI() {
            mImage= (ImageView) findViewById(R.id.iv_image);
            mAddImage= (Button) findViewById(R.id.btn_add_image);
        }
        private void initListeners() {
            mAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showChoosePicDialog();
                }
            });
        }
        /**
         * 显示修改图片的对话框
         */
        protected void showChoosePicDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(UppicActivity.this);
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
                            Toast.makeText(UppicActivity.this, "目前不支持相机拍照上传图片", Toast.LENGTH_SHORT).show();
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
            contentUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", newFile);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //兼容版本处理，因为 intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION) 只在5.0以上的版本有效
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ClipData clip =
                        ClipData.newUri(getContentResolver(), "A photo", contentUri);
                intent.setClipData(clip);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                List<ResolveInfo> resInfoList =
                        getPackageManager()
                                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, contentUri,
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
                mBitmap = extras.getParcelable("data");
                //这里图片是方形的，可以用一个工具类处理成圆形（很多头像都是圆形，这种工具类网上很多不再详述）
                mImage.setImageBitmap(mBitmap);//显示图片
                //在这个地方可以写上上传该图片到服务器的代码，后期将单独写一篇这方面的博客，敬请期待...
                UploadUserIcon();
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
                    Boolean upLoadResult = new Upload_info().upLoadPhoto(userId, imgStr, -1);
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
            Toast.makeText(this, "先选取照片",Toast.LENGTH_SHORT).show();
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
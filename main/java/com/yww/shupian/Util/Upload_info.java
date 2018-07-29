package com.yww.shupian.Util;

import org.json.JSONArray;
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

/**
 * Created by 杨旺旺 on 2017/11/20.
 */

public class Upload_info {
    public static final String login_url = "http://192.168.137.1:8080/ServletFirst/LoginServlet";
    public static final String pic_url="http://192.168.137.1:8080/ServletFirst/PictureAboutServlet";
    public static final String friend_url="http://192.168.137.1:8080/ServletFirst/FriendServlet";
    //下载用户账户名和头像
    public JSONObject upload_userIcon_account(String userId) {
        if (Integer.parseInt(userId)<0) {
            System.out.println("用户Id不合法");
            return null;
        }
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();

        try {
            try {
                requestJson.put("UserId", userId);
                requestJson.put("ReqType", "UserIconAndUserAccount");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(login_url); // 声明一个URL
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
                return resultJson;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    //申请成为摄像师（申请之后才能再摄像师列表中出现）
    public int BecomePhotographer(String userId)
    {
        if (Integer.parseInt(userId)<0) {
            System.out.println("用户Id不合法");
            return -1;
        }
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();

        try {
            try {
                requestJson.put("UserId", userId);
                requestJson.put("ReqType", "BecomePhotographer");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(login_url); // 声明一个URL
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
                if(resultJson.getString("resCode").equals("200"))//申请成功
                {
                    return 1;
                }
                else if(resultJson.getString("resCode").equals("202"))
                {
                    return 0;
                }
                else
                {
                    return -1;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }
    //上传图片到用户对应相册
    public Boolean upLoadPhoto(String userId, String imgStr, int GalleryNo) {//GalleryNo=-1则为上传头像，>=0则是上传相册图片
        if (imgStr == null) {
            System.out.println("imgStr is null");
        }
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject(); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据

        try {
            try {
                requestJson.put("UserId", userId);
                requestJson.put("ReqType", "Picture");
                requestJson.put("GalleryNo", Integer.toString(GalleryNo));
                requestJson.put("Img", imgStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(pic_url); // 声明一个URL
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
                String resCode = resultJson.getString("resCode");
                if (resCode.equals("200"))//上传成功
                {
                    return true;
                } else if (resCode.equals("201")) //上传错误
                {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    //下载用户相册信息
    public JSONArray upload_Gallerys(String userId) {
        if (Integer.parseInt(userId)<0) {
            System.out.println("用户Id不合法");
            return null;
        }
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();

        try {
            try {
                requestJson.put("UserId", userId);
                requestJson.put("ReqType", "GallerysInfo");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(pic_url); // 声明一个URL
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
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//相册信息下载成功
                {
                    return new JSONArray(resultJson.getString("GallerysArray"));
                }
                else if(resCode.equals("201"))//用户没有相册
                {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    //下载用户相册信息(用户角度)
    public JSONArray upload_Gallerys_user(String userId) {//这里的UserId是摄像师的Id
        if (Integer.parseInt(userId)<0) {
            System.out.println("用户Id不合法");
            return null;
        }
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();

        try {
            try {
                requestJson.put("UserId", userId);
                requestJson.put("ReqType", "GallerysInfo_user");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(pic_url); // 声明一个URL
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
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//相册信息下载成功
                {
                    return new JSONArray(resultJson.getString("GallerysArray"));
                }
                else if(resCode.equals("201"))//用户没有相册
                {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    //上传相册（空的）不是上传图片
    public String upload_new_Gallery(String userId,String gallery_name,String gallery_intro){
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject(); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据

        try {
            try {
                requestJson.put("UserId", userId);
                requestJson.put("ReqType", "CreateNewGallery");
                requestJson.put("GalleryName", gallery_name);
                requestJson.put("GalleryIntro", gallery_intro);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(pic_url); // 声明一个URL
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
                String resCode = resultJson.getString("resCode");
                if (resCode.equals("200"))//新建成功
                {
                    return resultJson.getString("galleryId");
                } else if (resCode.equals("201")) //新建失败
                {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    //下载用户某个相册的图片URL和picID集合
    public JSONArray upload_Pictures(String UserId,String GalleryId) {
        if (Integer.parseInt(GalleryId)<0) {
            System.out.println("GalleryId不合法");
            return null;
        }
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();

        try {
            try {
                requestJson.put("UserId", UserId);
                requestJson.put("GalleryId", GalleryId);
                requestJson.put("ReqType", "PicturesInfo");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(pic_url); // 声明一个URL
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
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//图片信息下载成功
                {
                    return new JSONArray(resultJson.getString("PicturesArray"));
                }
                else if(resCode.equals("201"))//改相册没有图片
                {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    //下载用户的好友列表信息
    public JSONArray upload_friendslist(String UserId)
    {
        if (Integer.parseInt(UserId)<0) {
            System.out.println("UserId不合法");
            return null;
        }
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();

        try {
            try {
                requestJson.put("UserId", UserId);
                requestJson.put("ReqType", "FriendsList");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(friend_url); // 声明一个URL
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
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//好友信息下载成功
                {
                    return new JSONArray(resultJson.getString("friendsinfoArray"));
                }
                else if(resCode.equals("201"))//好友列表为空
                {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    //下载摄像师列表信息
    public JSONArray upload_photoer_info(String UserId)
    {
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();
        try {
            try {
                requestJson.put("UserId", UserId);
                requestJson.put("ReqType", "PhotoersList");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(friend_url); // 声明一个URL
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
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//好友信息下载成功
                {
                    return new JSONArray(resultJson.getString("photoersinfoArray"));
                }
                else if(resCode.equals("201"))//好友列表为空
                {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    //添加一个新的好友
    public int upload_addfriend(String UserId,String FriendId)
    {
        if (Integer.parseInt(UserId)<0) {
            System.out.println("UserId不合法");
            return -1;
        }
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();

        try {
            try {
                requestJson.put("UserId", UserId);
                requestJson.put("FriendId", FriendId);
                requestJson.put("ReqType", "Addfriend");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(friend_url); // 声明一个URL
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
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//添加好友成功
                {
                    return 1;
                }
                else if(resCode.equals("201"))//已经是好友
                {
                    return 2;
                }
                else if(resCode.equals("202"))//添加好友失败
                {
                    return -1;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }
    //删除一个好友
    public int upload_deletefriend(String UserId,String FriendId)
    {
        if (Integer.parseInt(UserId)<0) {
            System.out.println("UserId不合法");
            return -1;
        }
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();

        try {
            try {
                requestJson.put("UserId", UserId);
                requestJson.put("FriendId", FriendId);
                requestJson.put("ReqType", "Deletefriend");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(friend_url); // 声明一个URL
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
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//删除好友成功
                {
                    return 1;
                }

                else if(resCode.equals("201"))//删除好友失败
                {
                    return -1;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }
    //下载一些照片，返回照片的URL
    public JSONArray upload_some_pictures(String UserId,int maxnum)
    {
        if (maxnum<0) {
            System.out.println("maxnum不合法");
            return null;
        }
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();

        try {
            try {
                requestJson.put("UserId", UserId);
                requestJson.put("NeedMaxNum", maxnum);
                requestJson.put("ReqType", "GetSomePicsInfo");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL( pic_url); // 声明一个URL
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
                String resCode=resultJson.getString("resCode");
                if(resCode.equals("200"))//图片信息下载成功
                {
                    return new JSONArray(resultJson.getString("PicsInfoArray"));
                }
                else if(resCode.equals("201"))//图片信息下载失败
                {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    //设置我的大学
    public Boolean setMyUniv(String userId,String myuniv){
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject(); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据

        try {
            try {
                requestJson.put("UserId", userId);
                requestJson.put("ReqType", "SetMyUniversity");
                requestJson.put("MyunivName", myuniv);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(login_url); // 声明一个URL
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
                String resCode = resultJson.getString("resCode");
                if (resCode.equals("200"))//新建成功
                {
                    return true;
                } else if (resCode.equals("201")) //新建失败
                {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    //设置我的风格
    public Boolean setMySyle(String userId,String mystyle){
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject(); // 此处就可以将服务端返回的Json的字符串还原成Json格式的数据

        try {
            try {
                requestJson.put("UserId", userId);
                requestJson.put("ReqType", "SetMyStyle");
                requestJson.put("MyStyle", mystyle);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(login_url); // 声明一个URL
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
                String resCode = resultJson.getString("resCode");
                if (resCode.equals("200"))//新建成功
                {
                    return true;
                } else if (resCode.equals("201")) //新建失败
                {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    //下载三张图片URL用来展示
    public JSONArray upload_three_picUrl(String photoerId)
    {
        if (Integer.parseInt(photoerId)<0) {
            System.out.println("用户Id不合法");
            return null;
        }
        HttpURLConnection connection = null;
        JSONObject requestJson = new JSONObject();

        try {
            try {
                requestJson.put("UserId", photoerId);
                requestJson.put("ReqType", "Download3PicURL");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] data = requestJson.toString().getBytes();//获得请求体
            URL url = new URL(pic_url); // 声明一个URL
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
                if(resultJson.getString("resCode").equals("200"))
                {
                    return new JSONArray(resultJson.getString("content"));
                }
                else
                {
                    return null;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}

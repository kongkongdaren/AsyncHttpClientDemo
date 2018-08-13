package com.wen.asyl.asynchttpclientdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mBtnGet,mBtnPost,mBtnDownLoad,mBtnUpload;
    private   AsyncHttpClient asyncHttpClient;
    private ImageView mIvPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        asyncHttpClient=new AsyncHttpClient();
        initViews();
    }

    private void initViews() {
        mBtnGet= findViewById(R.id.btn_get);
        mBtnPost= findViewById(R.id.btn_post);
        mBtnDownLoad= findViewById(R.id.btn_download);
        mBtnUpload= findViewById(R.id.btn_upload);
        mIvPhoto= findViewById(R.id.iv_photo);

        mBtnGet.setOnClickListener(this);
        mBtnPost.setOnClickListener(this);
        mBtnDownLoad.setOnClickListener(this);
        mBtnUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get:
                String url="http://apis.juhe.cn/mobile/get?phone=15711492842&key=4e12ebd27315d998b61d2606f463b50d";
                asyncHttpClient.get(url, null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.e("ssss",new String(responseBody));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
                break;
            case R.id.btn_post:
                String postUrl="http://v.juhe.cn/toutiao/index";
                RequestParams params=new RequestParams();
                params.put("type","社会");
                params.put("key","4867f81a3bcde50c94e6103a95cde181");
                asyncHttpClient.post(postUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.e("ssss",new String(responseBody));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
                break;
            case R.id.btn_download:
                String imgUrl="http://img.lanrentuku.com/img/allimg/1707/14988864745279.jpg";
                asyncHttpClient.get(imgUrl, null, new BinaryHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
                        Bitmap compressBitmap = compressImage(bitmap);
                        mIvPhoto.setImageBitmap(compressBitmap);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {

                    }
                });
                break;
            case R.id.btn_upload:
                String uploadUrl="http://192.168.1.92:8080/webapps/ROOT";
                File file=new File("/storage/emulated/0/66666.png");
                RequestParams paramsFile=new RequestParams();
                try {
                    paramsFile.put("hhhh",file);
                    asyncHttpClient.post(uploadUrl, paramsFile, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Log.e("success",new String(responseBody));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.e("failure",error.getMessage());
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                break;
        }
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}

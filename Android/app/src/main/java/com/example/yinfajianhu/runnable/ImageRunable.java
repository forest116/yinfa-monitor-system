package com.example.yinfajianhu.runnable;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;

import com.example.yinfajianhu.constants.MessageValue;

/**
 * @ClassName: ImageRunable
 * @Author: HuangKe
 * @Time: 2022/3/26 23:56
 * @Description: 获取摄像头视频流子线程
 */
public class ImageRunable implements Runnable {

    ImageView mImage;
    WebView mWebView;
    //byte[] bys ;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MessageValue.UPDATE_IMAGE :
                   // Bitmap bitmap = BitmapFactory.decodeByteArray(bys, 0, bys.length);
                    Log.d("--------main----------","转图片");
                    mImage.setImageBitmap((Bitmap) msg.obj);

                default:
                    break;
            }
        }
    };

    public ImageRunable(ImageView imageView){
        mImage = imageView;
    }

    public ImageRunable(ImageView imageView,WebView webView){
        mImage = imageView;
        mWebView = webView;
    }

    @Override
    public void run() {
//        try {
//
//            while(true){
//            HttpURLConnection connection;
//            URL url = new URL("http://114.116.196.117/hk/rtPic.jpg");
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setConnectTimeout(8000);
//            connection.setReadTimeout(8000);
//            connection.connect();
//
//
//                if (connection.getResponseCode() == 200){
//                    InputStream inputStream = connection.getInputStream();
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    Message message = new Message();
//                    message.what = MessageValue.UPDATE_IMAGE;
//                    message.obj = bitmap;
//                    handler.sendMessage(message);
//                    Thread.sleep(100);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }




    }


}

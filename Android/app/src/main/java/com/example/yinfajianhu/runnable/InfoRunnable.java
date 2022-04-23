package com.example.yinfajianhu.runnable;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.example.yinfajianhu.constants.MessageValue;
import com.example.yinfajianhu.constants.NetProperty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @ClassName: InfoRunnable
 * @Author: HuangKe
 * @Time: 2022/3/26 23:29
 * @Description: 获取传感器环境数据子线程
 */
public class InfoRunnable implements Runnable {

    TextView mInfo, mTime, mGas, mSmog, mInvade, mFall,mDementia;
    String info, time, gas, smog, invade, fall,dementia;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MessageValue.UPDATE_TEXT :
                    //mInfo.setText(info);
                    mTime.setText("时间："+time);
                    mSmog.setText("可燃烟雾含量："+smog+"%");
                    if (Integer.parseInt(invade) == 1)
                        mInvade.setText("是否入侵？ "+"否");
                    else
                        mInvade.setText("是否入侵？"+"是");
                    if (Integer.parseInt(fall) == 1)
                        mFall.setText("是否跌倒？"+"否");
                    else
                        mFall.setText("是否跌倒？"+"是");
                    if (Integer.parseInt(dementia) == 1)
                        mDementia.setText("是否失智? "+"否");
                    else
                        mDementia.setText("是否失智? "+"是");
                default:
                    break;
            }
        }
    };

    public InfoRunnable(TextView textView){
        mInfo = textView;
    }

    public InfoRunnable(TextView info, TextView time, TextView smog, TextView invade, TextView fall,TextView dementia){
        mInfo = info;
        mTime = time;
        mSmog = smog;
        mInvade = invade;
        mFall = fall;
        mDementia = dementia;
    }

    @Override
    public void run() {
        try {
            int len;
            byte[] bys ;
            Socket socket;
            InputStream inputStream;
            OutputStream outputStream;

            socket = new Socket(NetProperty.ip, NetProperty.infoPort);

            while(true){
                inputStream = socket.getInputStream();
                bys = new byte[4096];
                len = inputStream.read(bys);
                info = new String(bys,0,len);
                Log.d("------main-----",info);

                //解析JSON数据
                parseJSON(info);

                outputStream = socket.getOutputStream();
                outputStream.write("received".getBytes());
                Message message = new Message();
                message.what = MessageValue.UPDATE_TEXT;
                handler.sendMessage(message);
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJSON(String info) {
        try {

                JSONObject jsonObject = new JSONObject(info);
                time = jsonObject.getString("time");
                smog = jsonObject.getString("smog");
                invade = jsonObject.getString("invade");
                fall = jsonObject.getString("fall");
                dementia = jsonObject.getString("dementia");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

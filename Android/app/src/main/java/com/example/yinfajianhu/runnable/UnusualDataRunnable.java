package com.example.yinfajianhu.runnable;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.yinfajianhu.activity.UnusualDataActivity;
import com.example.yinfajianhu.activity.UnusualImgActivity;
import com.example.yinfajianhu.constants.MessageValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: UnusualDataRunnable
 * @Author: HuangKe
 * @Time: 2022/4/8 20:18
 * @Description: 异常数据Runnable
 */
public class UnusualDataRunnable implements Runnable {

    ListView dataList;
    StringBuilder response = null;
    ArrayList<String> data = new ArrayList<String> ();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MessageValue.UPDATE_LIST :
                    Log.d("---",response.toString());
                    parseJSON(response.toString());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(UnusualDataActivity.getContext(),android.R.layout.simple_list_item_1,data);
                    dataList.setAdapter(adapter);

                default:
                    break;
            }
        }
    };


    public UnusualDataRunnable(ListView listView){
        dataList = listView;
        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("---------",data.get(position));
                Intent intent = new Intent(UnusualDataActivity.getContext(), UnusualImgActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                intent.putExtra("data",data.get(position));
                UnusualDataActivity.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("http://ksjddg.cn/hk/unusual-info.txt");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            //获取输入流
            InputStream inputStream = connection.getInputStream();
            //下面读取输入流
            reader = new BufferedReader(new InputStreamReader(inputStream));
            response = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                response.append(line);
            }
            response.delete(response.length()-1,response.length());
            response.append("]");
            //Log.d("Unusaul-------------",response.toString());
            Message message = new Message();
            message.what = MessageValue.UPDATE_LIST;
            handler.sendMessage(message);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null){
                connection.disconnect();
            }
        }
    }

    private void parseJSON(String jsonData){
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            int j = 0;
            for(int i = jsonArray.length()-1 ; i >= 0 ; i--){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                Log.d("Unuaul--------",jsonObject.toString());

                data.add(j,jsonObject.toString());
                j++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

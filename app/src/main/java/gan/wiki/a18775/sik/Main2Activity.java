package gan.wiki.a18775.sik;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class Main2Activity extends AppCompatActivity {

    public String devicecode = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d("debug","main2ac");
        //SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        //devicecode = sharedPreferences.getString("clientCode","");
        Log.d("debug","dasfjafd");
        loadGroup();

    }
    //TODO: 加载分组
    public void loadGroup(){
        Log.d("debug","loadgroup");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("debug","run");
                URL url = null;
                try {
                    Log.d("debug","try");
                    url = new URL("http://sik.gan/key/index.php?route=client/getkeygroup");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setConnectTimeout(10*1000);
                    String body = "devicecode="+ URLEncoder.encode(devicecode,"UTF-8");
                    httpURLConnection.setRequestProperty("Content-length",String.valueOf(body.length()));
                    httpURLConnection.setRequestProperty("Cache-Control","max-age=0");
                    //httpURLConnection.setRequestProperty("Origin","http");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.getOutputStream().write(body.getBytes());
                    int code = httpURLConnection.getResponseCode();
                    Log.d("debug", String.valueOf(code));
                    if(code==200){
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                        Bundle bundle = new Bundle();
                        bundle.putString("json",br.readLine());
                        Message message = new Message();
                        message.what = 0;
                        message.setData(bundle);
                        handler.sendMessage(message);

                        Log.e("input String",br.readLine());
                    }else{
                        Log.e("input String","11111111111111");
                        Message message = new Message();
                        message.what=1;
                        handler.sendMessage(message);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            //TODO: 显示分组
            switch (msg.what) {
                case 0:
                    Bundle bundle = new Bundle();
                    bundle = msg.getData();
                    String json = bundle.getString("json");
                    Log.e("json",json);
                    try {
                        JSONObject jsonObject = new JSONObject(json);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    break;
            }
        }
    };
}

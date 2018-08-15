package gan.wiki.a18775.sik;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
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

public class MainActivity extends AppCompatActivity {

    private String emailphone=null;
    private String password =null;
    private TextView textView = null;
    private Activity activity = null;
  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editText = (EditText) findViewById(R.id.editText);
        final EditText editText2 = (EditText) findViewById(R.id.editText2);
        activity = this;
        textView = (TextView) findViewById(R.id.textView);
        Button button = (Button) this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailphone=editText.getText().toString();
                password = editText2.getText().toString();
                login();
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    Bundle bundle = new Bundle();
                    bundle = msg.getData();
                    String json = bundle.getString("json");
                    Log.d("json",json);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        String userorpass_che = jsonObject.getString("islogin");
                        String clientCode = jsonObject.getString("clientcode");
                        Log.e("userorpass_che",userorpass_che);
                        if (userorpass_che.equals("false")){
                            //TODO: dialog login error
                            textView.setText(R.string.loginerr);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText("");
                                }
                            },3000);
                        }else if(userorpass_che.equals("true"))
                        {
                            //TODO: keep clientCode to driver
                            SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("clientCode",clientCode);
                            editor.commit();
                            //TODO: new group activity
                            Intent intent = new Intent();
                            intent.setClass(activity,Main2Activity.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    break;
            }
        }
    };

    private void login(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL("http://sik.gan/accounts/index.php?route=client/keylogin");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setConnectTimeout(10*1000);
                    String body = "emailphone="+ URLEncoder.encode(emailphone,"UTF-8")+"&password="+URLEncoder.encode(password,"UTF-8");
                    httpURLConnection.setRequestProperty("Content-length",String.valueOf(body.length()));
                    httpURLConnection.setRequestProperty("Cache-Control","max-age=0");
                    //httpURLConnection.setRequestProperty("Origin","http");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.getOutputStream().write(body.getBytes());
                    int code = httpURLConnection.getResponseCode();
                    if(code==200){
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                        Bundle bundle = new Bundle();
                        bundle.putString("json",br.readLine());
                        Message message = new Message();
                        message.what = 0;
                        message.setData(bundle);
                        handler.sendMessage(message);

                        //Log.e("input String",br.readLine());
                    }else{
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
        }).start();
    }

}

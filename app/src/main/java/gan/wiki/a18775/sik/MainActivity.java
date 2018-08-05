package gan.wiki.a18775.sik;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private String emailphone=null;
    private String password =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editText = (EditText) findViewById(R.id.editText);
        final EditText editText2 = (EditText) findViewById(R.id.editText2);
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

    private void login(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL("http://sik.gan");
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
                        Log.e("input String",inputStream.toString());
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

package gan.wiki.a18775.sik;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }

    //TODO: 加载密码
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    //TODO:显示密码
                    break;
            }
        }
    };

    public void loadkey(){
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }


}
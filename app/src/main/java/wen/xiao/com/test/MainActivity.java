package wen.xiao.com.test;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button but_log,but_get;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InItView();
    }
    private void InItView(){
        but_log= (Button) findViewById(R.id.but_log);
        but_log.setOnClickListener(this);
        but_get= (Button) findViewById(R.id.but_get);
        but_get.setOnClickListener(this);
        textView= (TextView) findViewById(R.id.textview);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.but_log:
                OkGo.<String>post(Urls.URL_METHOD)//
                        .tag(this)//
                        .params("loginName", "17671623091")
                        .params("loginPwd", "E67C10A4C8FBFC0C400E047BB9A056A1")
                        .isMultipart(false)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                textView.setText(response.body());
                                Log.d("xiaowen",response.body());
                            }
                        });
            break;
            case  R.id.but_get:
                break;
        }
    }

}

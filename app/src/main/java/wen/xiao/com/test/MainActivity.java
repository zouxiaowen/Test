package wen.xiao.com.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

import wen.xiao.com.test.callback.JsonCallback;
import wen.xiao.com.test.callback.LzyResponse;
import wen.xiao.com.test.entity.brow;
import wen.xiao.com.test.entity.use;

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
                final SPUtil spUtil=new SPUtil(MainActivity.this,"Test");
                final SPUtil spUtil_id=new SPUtil(MainActivity.this,"userId");
                spUtil.clear();
                spUtil_id.clear();
//                OkGo.<String>post(Urls.URL_METHOD)//
//                        .tag(this)//
//                        .params("loginName", "17671623091")
//                        .params("loginPwd", "E67C10A4C8FBFC0C400E047BB9A056A1")
//                        .isMultipart(false)
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onSuccess(Response<String> response) {
//                                Gson gson = new Gson();
//                                use app = gson.fromJson(response.body().toString(), use.class);
//                                if(app.getData()!=null){
//                                    spUtil.putString("Token",app.getData().getToken());
//                                    spUtil_id.putInt("useId",app.getData().getUserId());
//                                    textView.setText(app.getData().getToken()+"");
//                                    Log.d("===",response.body().toString());
//                                }
//
//
//
//                            }
//                        });
                OkGo.<LzyResponse<use>>post(Urls.URL_METHOD)//
                        .tag(this)//
                        .params("loginName", "17671623091")
                        .params("loginPwd", "E67C10A4C8FBFC0C400E047BB9A056A1")
                        .isMultipart(false)
                        .execute(new JsonCallback<LzyResponse<use>>() {
                            @Override
                            public void onSuccess(Response<LzyResponse<use>> response) {
                                Log.d("JsonCallback",response.body().toString());
                                spUtil.putString("Token",response.body().data.getToken());
                                    spUtil_id.putInt("useId",response.body().data.getUserId());
                                textView.setText(response.body().data.getUserId()+"");
                            }
                        });
            break;
            case  R.id.but_get:
                SPUtil sp=new SPUtil(this,"Test");
                String token =sp.getString("Token","");
                OkGo.<LzyResponse<brow>>post(Urls.URL_Token)//
                        .tag(this)//
                        .isMultipart(false)
                        .params("current",1)
                        .params("pageSize",10)
                        .params("pages",0)
                        .params("total",0)
//                        .params("token",token)
//                        .params("userId","953")
                        .execute(new JsonCallback<LzyResponse<brow>>() {
                            @Override
                            public void onSuccess(Response<LzyResponse<brow>> response) {
                                textView.setText(response.body().toString());
                                Log.d("xiaowen",response.body().toString());
                            }
                        });

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}

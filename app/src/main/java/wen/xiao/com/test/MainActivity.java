package wen.xiao.com.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.List;

import wen.xiao.com.test.callback.JsonCallback;
import wen.xiao.com.test.callback.JsonCallback_two;
import wen.xiao.com.test.callback.LzyResponse;
import wen.xiao.com.test.entity.Bank;
import wen.xiao.com.test.entity.GG;
import wen.xiao.com.test.entity.brow;
import wen.xiao.com.test.entity.use;
import wen.xiao.com.test.utils.ToastUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button but_log,but_post,but_get,but_agreement,but_image;
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
        but_post= (Button) findViewById(R.id.but_post);
        but_post.setOnClickListener(this);
        but_get= (Button) findViewById(R.id.but_get);
        but_get.setOnClickListener(this);
        but_agreement= (Button) findViewById(R.id.but_agreement);
        but_agreement.setOnClickListener(this);
        but_image= (Button) findViewById(R.id.but_image);
        but_image.setOnClickListener(this);
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
                OkGo.<LzyResponse<use>>post(Urls.URL_METHOD)//
                        .tag(this)//
                        .params("loginName", "17671623091")
                        .params("loginPwd", "E67C10A4C8FBFC0C400E047BB9A056A1")
                        .isMultipart(false)
                        .execute(new JsonCallback<LzyResponse<use>>(this) {
                            @Override
                            public void onSuccess(Response<LzyResponse<use>> response) {
                                spUtil.putString("Token",response.body().data.getToken());
                                    spUtil_id.putInt("useId",response.body().data.getUserId());
                                textView.setText(response.body().data.getUserId()+"");
                            }
                        });
            break;
            case  R.id.but_post:
                SPUtil sp=new SPUtil(this,"Test");
                String token =sp.getString("Token","");
                OkGo.<LzyResponse<brow>>post(Urls.URL_Token)//
                        .tag(this)//
                        .isMultipart(false)
                        .params("current",1)
                        .params("pageSize",10)
                        .params("pages",0)
                        .params("total",0)
                        .execute(new JsonCallback<LzyResponse<brow>>(this) {
                            @Override
                            public void onSuccess(Response<LzyResponse<brow>> response) {
                                textView.setText(response.body().toString());
                            }
                        });

                break;
            case R.id.but_get:

                OkGo.<LzyResponse<GG>>get(Urls.URL_Get)
                        .tag(this)
                        .params("type","BANK_TYPE")
                        .execute(new JsonCallback<LzyResponse<GG>>(this) {
                            @Override
                            public void onSuccess(Response<LzyResponse<GG>> response) {
                                try {
                                    textView.setText(response.body().data.getBankTypeList().get(0).getValue());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                break;
            case R.id.but_agreement:
                OkGo.<String>get(Urls.URL_XIEYI)
                        .tag(this)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                try{
                                    textView.setText(response.body().toString());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                break;
            case R.id.but_image:
                OkGo.<String>post(Urls.URL_FILE)//
                        .tag(this)//
                        .params("confidence","1")
                        .params("livingImg","1")
                        .params("userId","1")
                        .isSpliceUrl(true)
                        .isMultipart(false)
                        .upFile(new File(BaseParams.FACE_PHOTO_PATH + "/" + BaseParams.PHOTO_ALIVE))//
                        .execute(new JsonCallback_two<String>(this) {
                            @Override
                            public void onSuccess(Response<String> response) {

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

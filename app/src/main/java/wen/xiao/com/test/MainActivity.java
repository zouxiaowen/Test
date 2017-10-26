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
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
//                SPUtil sps=new SPUtil(this,"Test");
//                String tokens =sps.getString("Token","");
//                TreeMap map = new TreeMap();
//                map.put("confidence", "123");
//                map.put("userId", "953");
//                map.put("livingImg", new File("/storage/emulated/0/wu.png"));
//                Map<String, String> head = new TreeMap<>();
//                head.put("token", tokens);
//                head.put("uuid",loggingInterceptor.getUUid());
//                head.put("signMsg",signParams(map,head.get("uuid")));

                OkGo.<String>post(Urls.URL_FILE)//
                        .tag(this)//
                        .params("confidence","1")
                        .params("userId","131")
                        .params("livingImg", new File("/storage/emulated/0/wu.png"))
                        .isSpliceUrl(true)
                        .isMultipart(false)
                        .execute(new JsonCallback_two<String>(this) {
                            @Override
                            public void onSuccess(Response<String> response) {
                            try {
                                textView.setText(response.body().toString());
                            }catch (Exception e) {
                                e.printStackTrace();
                            }}
                        });
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }


    /**
     * 提交文件 对Map数据进行签名
     */
    public String signParams(TreeMap<String, String> treeMap,String uuid) {
        TreeMap<String, String> commonParamsTreeMap = new TreeMap<>();
        commonParamsTreeMap.put("mobileType", "2");
        commonParamsTreeMap.put("versionNumber", "1.0.3");
        treeMap.putAll(commonParamsTreeMap);
        String sign = getSign(treeMap,uuid);
        return sign;
    }
    /**
     * 一般接口调用-signa签名生成规则
     *
     * @param map
     *         有序请求参数map
     */
    private String getSign(TreeMap map,String uuid) {
        String signa = "";
        try {
            Iterator it = map.entrySet().iterator();
            StringBuilder sb = new StringBuilder();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                if (entry.getValue() instanceof File)
                    continue;//URLEncoder.encode(, "UTF-8")
                sb.append(entry.getKey()).append("=").append(URLDecoder.decode(entry.getValue().toString(), "UTF-8")).append("|");
            }
            // 所有请求参数排序后的字符串后进行MD5（32）
            //signa = MDUtil.encode(MDUtil.TYPE.MD5, sb.toString());
            // 得到的MD5串拼接appsecret再次MD5，所得结果转大写
            String sign = "";
            if (sb.toString().length() > 1) {
                sign = sb.toString().substring(0, sb.length() - 1);
            } else {
                sign = sb.toString();
            }
//            signa = MDUtil.encode(MDUtil.TYPE.MD5, AppConfig.APP_KEY + getToken() + sign + uuid).toUpperCase();
            SPUtil sp=new SPUtil(this,"Test");
            String token =sp.getString("Token","");
            signa = MDUtil.encode(MDUtil.TYPE.MD5, "wI3Ri3pntEs6CXp5VlLGlQtxHLKqONp5OQ4Yk6WxcZcAZGYYnyycRJo895qf" +token+ sign + uuid).toUpperCase();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return signa;
    }
}

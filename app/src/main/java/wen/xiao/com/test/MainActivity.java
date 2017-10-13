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
                Map<String, String> treeMap=new TreeMap<>();

                treeMap.put("mobileType","2");
                treeMap.put("versionNumber","1.0.3");
                treeMap.put("loginName","17671623091");
                treeMap.put("loginPwd","E67C10A4C8FBFC0C400E047BB9A056A1");
                treeMap.put("serialVersionUID","402476310254065018");
                TreeMap map = new TreeMap();
                map.putAll(treeMap);
                String uuid = getUUid();
                String sign = getSign(map,uuid);
                OkGo.<String>post(Urls.URL_METHOD)//
                        .tag(this)//
                        .params("versionNumber", "1.0.3")
                        .params("loginName", "17671623091")
                        .params("loginPwd", "E67C10A4C8FBFC0C400E047BB9A056A1")
                        .params("mobileType", "2")
                        .params("serialVersionUID", "402476310254065018")
                        .headers("signMsg", sign)
                        .headers("token","")
                        .headers("uuid", uuid)
                        .isMultipart(true)
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
    public String getUUid(){
        return getRandomString(32);
    }
    public  String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    /**
     * 一般接口调用-signa签名生成规则
     *
     * @param map
     *         有序请求参数map
     */
    private String getSign(TreeMap map, String uuid) {
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
            signa = MDUtil.encode(MDUtil.TYPE.MD5, "wI3Ri3pntEs6CXp5VlLGlQtxHLKqONp5OQ4Yk6WxcZcAZGYYnyycRJo895qf"  + sign + uuid).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signa;
    }
}

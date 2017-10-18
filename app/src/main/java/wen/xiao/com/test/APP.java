package wen.xiao.com.test;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/10/13.
 */

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SPUtil sp=new SPUtil(this,"Test");
        String token =sp.getString("Token","");
        SPUtil sp_id=new SPUtil(this,"useId");
        int useId =sp_id.getInt("useId",0);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpParams params = new HttpParams();
        params.put("versionNumber", "1.0.3");
        params.put("mobileType", "2");
        params.put("serialVersionUID", "402476310254065018");
        params.put("userId",useId);
        params.put("Token",token);
        loggingInterceptor lp=new loggingInterceptor(getApplicationContext());
        builder.addInterceptor(lp);

        OkGo.getInstance()
                .setOkHttpClient(builder.build())
                .addCommonParams(params)
                .init(this);
    }
}

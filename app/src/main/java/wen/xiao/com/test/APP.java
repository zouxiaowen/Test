package wen.xiao.com.test;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpParams;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/10/13.
 */

public class APP extends Application {
    public   final boolean IS_DEBUG = BuildConfig.LOG_ENABLE;

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
        //log相关
        LogHttp loggingInterceptor = new LogHttp("OkGo");
        if (IS_DEBUG){
            loggingInterceptor.setPrintLevel(LogHttp.Level.BODY);        //log打印级别，决定了log显示的详细程度
        }else {
            loggingInterceptor.setPrintLevel(LogHttp.Level.NONE);        //log打印级别，决定了log显示的详细程度

        }
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        OkGo.getInstance()
                .setOkHttpClient(builder.build())
                .addCommonParams(params)
                .init(this);
    }
}

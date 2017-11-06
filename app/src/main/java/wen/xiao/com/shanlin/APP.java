package wen.xiao.com.shanlin;

import android.app.Application;

import com.lzy.okgo.OkGo;

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
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        loggingInterceptor lp=new loggingInterceptor(getApplicationContext());
        builder.addInterceptor(lp);
        //log相关
        LogHttp loggingInterceptor = new LogHttp("OkGo");
        if (IS_DEBUG){
            loggingInterceptor.setPrintLevel(LogHttp.Level.BODY);        //log打印级别，决定了log显示的详细程度
        }else {
            loggingInterceptor.setPrintLevel(LogHttp.Level.NONE);        //log打印级别，决定了log显示的详细程度

        }
        loggingInterceptor.setColorLevel(Level.WARNING);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        OkGo.getInstance()
                .setOkHttpClient(builder.build())
                .init(this);
    }
}

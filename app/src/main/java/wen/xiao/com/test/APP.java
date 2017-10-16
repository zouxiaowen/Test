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
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpParams params = new HttpParams();
        params.put("versionNumber", "1.0.3");     //param支持中文,直接传,不要自己编码
        params.put("mobileType", "2");
        params.put("serialVersionUID", "402476310254065018");
        loggingInterceptor lp=new loggingInterceptor();
        builder.addInterceptor(lp);
        //添加拦截器
//        builder.addInterceptor(new CommonParamsInterceptor() {
//            @Override
//            public Map<String, String> getHeaderMap() {
//                Map<String, String> headersMap =  new HashMap<>();
////                headersMap.put("v", "1.0");
//
//                Map<String, String> treeMap=new TreeMap<>();
//                treeMap.put("mobileType","2");
//                treeMap.put("versionNumber","1.0.3");
//                treeMap.put("loginName","17671623091");
//                treeMap.put("loginPwd","E67C10A4C8FBFC0C400E047BB9A056A1");
//                treeMap.put("serialVersionUID","402476310254065018");
//                TreeMap map = new TreeMap();
//                map.putAll(treeMap);
//
//                String uuid = loggingInterceptor.getUUid();
//                String sign =loggingInterceptor.getSign(map,uuid);
//                headersMap.put("uuid",uuid);
//                headersMap.put("token","");
//                headersMap.put("signMsg",sign);
//                return headersMap;
//            }
//
//            @Override
//            public Map<String, String> getQueryParamMap() {
//                return null;
//            }
//
//            @Override
//            public Map<String, String> getFormBodyParamMap() {
//                return null;
//            }
//        });
        OkGo.getInstance()
                .setOkHttpClient(builder.build())
                .addCommonParams(params)
                .init(this);
        ; //建议设置OkHttpClient，不设置会使用默认的;
    }
}

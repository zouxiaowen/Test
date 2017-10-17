package wen.xiao.com.test;

import android.content.Context;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by Administrator on 2017/10/16.
 */

public class loggingInterceptor implements Interceptor {

     static Context context;
    public loggingInterceptor(Context app) {
        this.context=app;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originRequest = chain.request();
        Request.Builder requestBuilder= originRequest.newBuilder();
        //获取的不是表单提交的url进行拆分
        String   postBodyString = bodyToString(originRequest.body());
        TreeMap map = new TreeMap();
        map.putAll(dynamicParams(postBodyString));
        String uuid = getUUid();
        String sign = getSign(map,uuid);
        SPUtil sp=new SPUtil(context,"Test");
        String token =sp.getString("Token","");
//        if (token==null||token.endsWith("token")){
//            requestBuilder = chain.request().newBuilder().addHeader("signMsg", sign).addHeader("token", "").addHeader("uuid", uuid);
//
//        }else
//        //添加到header里面
        requestBuilder = chain.request().newBuilder().addHeader("signMsg", sign).addHeader("token", token).addHeader("uuid", uuid);
        return chain.proceed(requestBuilder.build());
    }

    /**
     * 对字符串进行签名
     */
    public TreeMap<String, String> dynamicParams(String postBodyString) {
        TreeMap<String, String> treeMap = splitPostString(postBodyString);
        //treeMap = dynamicParams(treeMap);
        //String sign = getSign(treeMap);
        //treeMap.put(Constant.SIGNA, sign);
        return treeMap;
    }
    /**
     * 动态拼接请求参数
     */
    public TreeMap<String, String> dynamicParams(TreeMap<String, String> map) {
        String ts = String.valueOf(System.currentTimeMillis() / 1000);
        //map.put(Constant.TS, ts);
//        String token  = getToken();
//        String userId = getUserId();
//        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(userId)) {
//            map.put(Constant.TOKEN, token);
//            map.put(Constant.USER_ID, userId);
//        }
        return map;
    }
    /**
     * 分割请求参数，放入treeMap中,拼接动态参数
     *
     * @param postBodyString
     *         请求参数
     */
    private TreeMap<String, String> splitPostString(String postBodyString) {
        TreeMap<String, String> map = new TreeMap<>();
        for (String s : postBodyString.split("&")) {
            String[] keyValue = s.split("=");
            map.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : "");
        }
        return map;
    }

    // RequestBody to String
    private String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            if (request != null)
                request.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (IOException e) {
            return "did not work";
        }
    }






    public static  String getUUid() {
        return getRandomString(32);
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
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
     * @param map 有序请求参数map
     */
    static  String getSign(TreeMap map, String uuid) {
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
            SPUtil sp=new SPUtil(context,"Test");
            String token =sp.getString("Token","");
            signa = MDUtil.encode(MDUtil.TYPE.MD5, "wI3Ri3pntEs6CXp5VlLGlQtxHLKqONp5OQ4Yk6WxcZcAZGYYnyycRJo895qf" +token+ sign + uuid).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signa;
    }

}

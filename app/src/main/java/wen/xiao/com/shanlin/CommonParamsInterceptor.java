package wen.xiao.com.shanlin;

/**
 * Created by Administrator on 2017/10/16.
 */

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 公共参数
 * 1) Header
 * 2) Query Param
 * 3) POST Param form-data
 * 4) POST Param x-www-form-urlencoded
 */
public abstract class CommonParamsInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request originRequest = chain.request();
        Request.Builder newRequest = originRequest.newBuilder();

        // Header
        Headers.Builder newHeaderBuilder = originRequest.headers().newBuilder();
        Map<String, String> headerMap = getHeaderMap();
        if (headerMap != null && !headerMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                newHeaderBuilder.add(entry.getKey(), entry.getValue());
            }
            newRequest.headers(newHeaderBuilder.build());
        }

        // Query Param
        if ("GET".equals(originRequest.method())) {
            HttpUrl.Builder newUrlBuilder = originRequest.url().newBuilder();
            Map<String, String> queryParamMap = getQueryParamMap();
            if (queryParamMap != null && !queryParamMap.isEmpty()) {
                for (Map.Entry<String, String> entry : queryParamMap.entrySet()) {
                    newUrlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }
                newRequest.url(newUrlBuilder.build());
            }
        } else if ("POST".equals(originRequest.method())) {
            RequestBody body = originRequest.body();
            if (body != null && body instanceof FormBody) {
                // POST Param x-www-form-urlencoded
                FormBody formBody = (FormBody) body;
                Map<String, String> formBodyParamMap = new HashMap<>();
                int bodySize = formBody.size();
                for(int i = 0; i < bodySize; i++) {
                    formBodyParamMap.put(formBody.name(i), formBody.value(i));
                }

                Map<String, String> newFormBodyParamMap = getFormBodyParamMap();
                if (newFormBodyParamMap != null) {
                    formBodyParamMap.putAll(newFormBodyParamMap);
                    FormBody.Builder bodyBuilder = new FormBody.Builder();
                    for (Map.Entry<String, String> entry : formBodyParamMap.entrySet()) {
                        bodyBuilder.add(entry.getKey(), entry.getValue());
                    }
                    newRequest.method(originRequest.method(), bodyBuilder.build());
                }
            } else if (body != null && body instanceof MultipartBody) {
                // POST Param form-data
                MultipartBody multipartBody = (MultipartBody) body;
                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                Map<String, String> extraFormBodyParamMap = getFormBodyParamMap();
                for (Map.Entry<String, String> entry : extraFormBodyParamMap.entrySet()) {
                    builder.addFormDataPart(entry.getKey(), entry.getValue());
                }
                List<MultipartBody.Part> parts = multipartBody.parts();
                for (MultipartBody.Part part : parts) {
                    builder.addPart(part);
                }
                newRequest.post(builder.build());
            }
        }

        return chain.proceed(newRequest.build());
    }

    public abstract Map<String, String> getHeaderMap();
    public abstract Map<String, String> getQueryParamMap();
    public abstract Map<String, String> getFormBodyParamMap();
}



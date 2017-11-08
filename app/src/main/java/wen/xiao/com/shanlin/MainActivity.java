package wen.xiao.com.shanlin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import wen.xiao.com.shanlin.callback.JsonCallback;
import wen.xiao.com.shanlin.entity.GG;
import wen.xiao.com.shanlin.entity.brow;
import wen.xiao.com.shanlin.entity.use;

import wen.xiao.com.shanlin.callback.JsonCallback_two;
import wen.xiao.com.shanlin.callback.LzyResponse;
import wen.xiao.com.shanlin.utils.ToastUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button but_log, but_post, but_get, but_agreement, but_image, but_ocr;
    private TextView textView;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InItView();
    }

    private void InItView() {
        image= (ImageView) findViewById(R.id.image);
        but_log = (Button) findViewById(R.id.but_log);
        but_log.setOnClickListener(this);
        but_post = (Button) findViewById(R.id.but_post);
        but_post.setOnClickListener(this);
        but_get = (Button) findViewById(R.id.but_get);
        but_get.setOnClickListener(this);
        but_agreement = (Button) findViewById(R.id.but_agreement);
        but_ocr = (Button) findViewById(R.id.but_ocr);
        but_ocr.setOnClickListener(this);
        but_agreement.setOnClickListener(this);
        but_image = (Button) findViewById(R.id.but_image);
        but_image.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.textview);
        // 在Activity：
        // 申请多个权限。
        AndPermission.with(this)
                .requestCode(100)//WRITE_EXTERNAL_STORAGE
                .permission(Permission.MICROPHONE, Permission.STORAGE, Permission.STORAGE)
                .callback(this)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(MainActivity.this, rationale).show();
                    }
                })
                .start();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_ocr:
                OkGo.<String>post("https://aip.baidubce.com/oauth/2.0/token")
                        .tag(this)
                        .headers("grant_type", "client_credentials")
                        .headers("client_id", "hediqweNkMHpgiEQIvabO1RP")
                        .headers("client_secret", "N1BsT2NmCCdQtTVttGBGGijUYHMvo2Fx")
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                            }
                        });

                break;
            case R.id.but_log:
                final SPUtil spUtil = new SPUtil(MainActivity.this, "Test");
                final SPUtil spUtil_id = new SPUtil(MainActivity.this, "userId");
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
                                spUtil.putString("Token", response.body().data.getToken());
                                spUtil_id.putInt("useId", response.body().data.getUserId());
                                textView.setText(response.body().data.getUserId() + "");
                            }
                        });
                break;
            case R.id.but_post:
                SPUtil sp = new SPUtil(this, "Test");
                String token = sp.getString("Token", "");
                OkGo.<LzyResponse<brow>>post(Urls.URL_Token)//
                        .tag(this)//
                        .isMultipart(false)
                        .params("current", 1)
                        .params("pageSize", 10)
                        .params("pages", 0)
                        .params("total", 0)
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
                        .params("type", "BANK_TYPE")
                        .execute(new JsonCallback<LzyResponse<GG>>(this) {
                            @Override
                            public void onSuccess(Response<LzyResponse<GG>> response) {
                                try {
                                    textView.setText(response.body().data.getBankTypeList().get(0).getValue());
                                } catch (Exception e) {
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
                                try {
                                    textView.setText(response.body().toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                break;
            case R.id.but_image:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 0);





//                OkGo.<String>post(Urls.URL_FILE)//
//                        .tag(this)//
//                        .params("confidence","1")
//                        .params("userId","131")
//                        .params("livingImg", new File("/storage/emulated/0/wu.png"))
//                        .isSpliceUrl(true)
//                        .isMultipart(false)
//                        .execute(new JsonCallback_two<String>(this) {
//                            @Override
//                            public void onSuccess(Response<String> response) {
//                            try {
//                                textView.setText(response.body().toString());
//                            }catch (Exception e) {
//                                e.printStackTrace();
//                            }}
//                        });
                break;
        }
    }

    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) { // api >= 19
            return getRealPathFromUriAboveApi19(context, uri);
        } else { // api < 19
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())){
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== 0&&Activity.RESULT_OK==resultCode&& null!=data){

            Uri uri = data.getData();
            String path=getRealPathFromUri(this,uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap img = BitmapFactory.decodeFile(path,options);
            image.setImageBitmap(img);
            ToastUtil.toast(path);
            OkGo.<String>post(Urls.URL_FILE)//
                    .tag(this)//
                    .params("confidence","1")
                    .params("userId","131")
                    .params("livingImg", new File(path))
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
    public String signParams(TreeMap<String, String> treeMap, String uuid) {
        TreeMap<String, String> commonParamsTreeMap = new TreeMap<>();
        commonParamsTreeMap.put("mobileType", "2");
        commonParamsTreeMap.put("versionNumber", "1.0.3");
        treeMap.putAll(commonParamsTreeMap);
        String sign = getSign(treeMap, uuid);
        return sign;
    }

    /**
     * 一般接口调用-signa签名生成规则
     *
     * @param map 有序请求参数map
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
//            signa = MDUtil.encode(MDUtil.TYPE.MD5, AppConfig.APP_KEY + getToken() + sign + uuid).toUpperCase();
            SPUtil sp = new SPUtil(this, "Test");
            String token = sp.getString("Token", "");
            signa = MDUtil.encode(MDUtil.TYPE.MD5, "wI3Ri3pntEs6CXp5VlLGlQtxHLKqONp5OQ4Yk6WxcZcAZGYYnyycRJo895qf" + token + sign + uuid).toUpperCase();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return signa;
    }
}

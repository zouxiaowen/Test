package wen.xiao.com.shanlin;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;

import wen.xiao.com.shanlin.Service.UpdataService;
import wen.xiao.com.shanlin.callback.JsonCallback;
import wen.xiao.com.shanlin.callback.JsonCallback_two;
import wen.xiao.com.shanlin.callback.LzyResponse;
import wen.xiao.com.shanlin.entity.use;

/**
 * Author: 邹小文
 * E-mail: 584886576@qq.com
 * Data  : 2017/11/22.
 */

public class Activity extends AppCompatActivity implements View.OnClickListener {
    private Button but_image, but_log, but_App,but_Apps;
    private  String   ApkPath = "/sdcard/download/";
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        but_image = (Button) findViewById(R.id.but_image);
        but_image.setOnClickListener(this);
        but_log = (Button) findViewById(R.id.but_log);
        but_log.setOnClickListener(this);
        but_App = (Button) findViewById(R.id.but_App);
        but_App.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                uploadApp();
            }
        });
        but_Apps = (Button) findViewById(R.id.but_Apps);
        but_Apps.setOnClickListener(this);
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
                        AndPermission.rationaleDialog(Activity.this, rationale).show();
                    }
                })
                .start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_log://登录
                logApp();
                break;
            case R.id.but_image://上传图片
                uploadImage();
                break;
            case R.id.but_App://版本更新
                uploadApp();
                break;
                case R.id.but_Apps://后台
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //启动服务
                            Intent service = new Intent( Activity.this,UpdataService.class);
                            startService(service);
                        }
                    }).start();
                    break;
        }
    }

    /**
     * 版本更新
     */
    private void uploadApp() {
        OkGo.<File>get("http://p3.exmmw.cn/p1/wq/360yingshidaquan.apk")
                .tag("hehe")
                .execute(new FileCallback(ApkPath,"xiaomo.apk") {
                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);
                        if (materialDialog == null) {
                            materialDialog = new MaterialDialog.Builder(Activity.this)
                                    .title("版本升级")
                                    .content("正在下载安装包，请稍候")
                                    .progress(false, 100, false)
                                    .cancelable(false)
                                    .show();
                        }
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        materialDialog.dismiss();
                        installAPK(ApkPath+"/xiaomo.apk");
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        Toast.makeText(Activity.this,"下载出错请重试",Toast.LENGTH_LONG).show();
                        materialDialog.dismiss();
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        materialDialog.setProgress( (int)(progress.fraction * 100));
                        Log.d("xaiowen",(int)(progress.fraction * 100)+"");
                    }
                });
    }

    /**
     * 上传图片
     */
    private void uploadImage() {
        OkGo.<String>post(Urls.URL_FILES)//
                .tag(this)//
                .params("image", new File("/storage/emulated/0/wu.png"))
                .isSpliceUrl(true)
                .isMultipart(false)
                .execute(new JsonCallback_two<String>(this) {
                    @Override
                    public void onSuccess(Response<String> response) {
                    }
                });
    }

    /**
     * 登录
     */
    private void logApp() {
        final SPUtil spUtil = new SPUtil(Activity.this, "Test");
        final SPUtil spUtil_id = new SPUtil(Activity.this, "userId");
        spUtil.clear();
        spUtil_id.clear();
        OkGo.<LzyResponse<use>>post(Urls.URL_METHOD)//
                .tag(this)//
                .params("loginName", "15821145929")
                .params("loginPwd", "200820e3227815ed1756a6b531e7e0d2")
                .isMultipart(false)
                .execute(new JsonCallback<LzyResponse<use>>(this) {
                    @Override
                    public void onSuccess(Response<LzyResponse<use>> response) {
                        spUtil.putString("Token", response.body().data.getToken());
                        spUtil_id.putInt("useId", response.body().data.getUserId());

                    }
                });
    }
    /** 下载完成后自动安装apk */
    public void installAPK(String Apkpath) {
        File apkFile = new File( Apkpath);

        if (!apkFile.exists()) {
            return;
        }
        if (Build.VERSION.SDK_INT>=24){
            Uri apkUri = FileProvider.getUriForFile(this, "com.mydomain.fileprovider", apkFile);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.addCategory(Intent.CATEGORY_DEFAULT);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            startActivity(install);
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setType("application/vnd.android.package-archive");
            intent.setData(Uri.fromFile(apkFile));
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }
    //打开APK程序代码

    private void openFile(File file) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }
}

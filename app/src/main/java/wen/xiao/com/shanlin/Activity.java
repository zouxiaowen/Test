package wen.xiao.com.shanlin;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.io.File;

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
    private Button but_image, but_log, but_App;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        but_image = (Button) findViewById(R.id.but_image);
        but_image.setOnClickListener(this);
        but_log = (Button) findViewById(R.id.but_log);
        but_log.setOnClickListener(this);
        but_App = (Button) findViewById(R.id.but_App);
        but_App.setOnClickListener(this);
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
        }
    }

    /**
     * 版本更新
     */
    private void uploadApp() {
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
}

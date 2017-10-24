package wen.xiao.com.test;

import android.os.Environment;

import java.io.File;

import wen.xiao.com.test.utils.ContextHolder;

/**
 * Created by Administrator on 2017/10/24.
 */

class BaseParams {
    public static final  String    FACE_PHOTO_PATH   = getPrivateDir() + "/photo";
    public static        String    PHOTO_ALIVE       = "alive";

    /**
     * 获取sdcard私有目录
     */
    public static String getPrivateDir() {
        File privateApkDir = null;
        // 判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            // 获取跟目录
            privateApkDir = ContextHolder.getContext().getExternalFilesDir(null);
            if (privateApkDir!=null&&!privateApkDir.exists()){
                privateApkDir.mkdirs();
            }
        }
        if (privateApkDir == null) {
            return "";
        } else {
            return privateApkDir.getPath();
        }
    }
}

package com.rtmap.bugtags;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

/**
 * Created by silver on 16-3-17.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {


    private Context mContext;
    private static CrashHandler mCrashHandler = new CrashHandler();

    private String app_id;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public static CrashHandler getInstance() {
        return mCrashHandler;
    }


    /**
     * 设置当线程由于未捕获到异常而突然终止的默认处理程序。
     */
    public void setCrashHandler(Context context) {
        mContext = context;
        this.initUploadCrash();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    /**
     * 当发生Crash时调用该方法
     */
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        //1 保存错误日志到SD卡
        CrashUtils.saveCrashInfoToSDCard(mContext, throwable, thread);
        // LogUtil.e("thread:" + thread.getName()+"thread state:"+th + "class:" + throwable.getClass());

        //2 提示Crash信息
        showCrashTipToast();
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
        }
        //3 退出应用
        System.exit(0);
    }

    private void showCrashTipToast() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "I am very sorry", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }).start();
    }


    private void initUploadCrash() {
        DBManager dbManager = new DBManager(mContext);
        List<Map> mapList = dbManager.getCrashList();
        for (final Map<String, String> mapCrash : mapList) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CrashHttp.uploadCrash(mapCrash, mContext);
                }
            }).start();
        }
    }

}

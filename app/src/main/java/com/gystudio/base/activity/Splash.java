package com.gystudio.base.activity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.gystudio.ksoap2.serialization.SoapPrimitive;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gystudio.base.R;
import com.gystudio.service.SOAPWebServiceTask;
import com.gystudio.utils.PreferencesUtil;
import com.gystudio.utils.ScreenUtil;
import com.gystudio.utils.StringUtil;
import com.gystudio.utils.SysConfig;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Splash extends Activity {
    //	private Timer timer = new Timer();
    private ImageView splashview = null;
    private AnimationDrawable animation = null;
    public static final String APPABOUTGROUP = "AppAboutGroup";// 组名
    public static final String APPABOUTCOPYRIGHT = "AppAbout_Copyright";// 版权
    public static final String APPABOUTTECSUP = "AppAbout_TecSup ";// 技术支持
    public static final String APPABOUTTEL = "AppAbout_Tel";// 联系电话
    public static final String APPABOUTURL = "AppAbout_Url";// 网址

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtil.FullScreen(this);
        setContentView(R.layout.splash);
        Initialization();

    }

    private void Initialization() {
        // 初始化系统配置
        SysConfig.getInstance(Splash.this);
        splashview = (ImageView) findViewById(R.id.splashview);
        animation = (AnimationDrawable) splashview.getBackground();
        StartActivity();
//		timer.schedule(task, 3000);
        //	getAppAboutInfo();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        (new Timer(false)).schedule(new AnimationTimer(animation), 100);
        animation.start();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        animation.stop();
    }

    private static class AnimationTimer extends TimerTask {
        AnimationDrawable animation;

        public AnimationTimer(AnimationDrawable animation) {
            this.animation = animation;
        }

        @Override
        public void run() {
            animation.start();
            this.cancel();
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent(Splash.this, Login.class);
                    Splash.this.startActivity(intent);
                    // Splash.this.startActivityForResult(intent, 0);
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:// 退出系统
                // finish();
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // System.exit(0);
        // 或者下面这种方式
        // android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 获得应用程序“关于”信息
     */
    private void getAppAboutInfo() {
        String serviceUrl = SysConfig.getServiceUrl();// WebSerivce地址
        String nameSpace = SysConfig.getNameSpace(); // 空间名,可修改
        String methodName = "GetSysAbout"; // 需调用WebService名称
        HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            new SOAPWebServiceTask(this, null) {
                @Override
                public boolean handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            buildData((SoapPrimitive) msg.obj);
                            break;
                        case 2:
                            //启动登录页面
                            StartActivity();
                            break;
                    }
                    return false;
                }
            }.execute(serviceUrl, nameSpace, methodName, hashMap);
        } catch (Exception e) {
            //启动登录页面
            StartActivity();
        }
    }

    public void buildData(SoapPrimitive soapObject) {
        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(StringUtil
                    .FilterJsonChar(soapObject.toString()));
            Log.e("JsonData", soapObject.toString());
            int success = jsonObject.get("success").getAsInt();
            if (success == 1) {
                try {
                    String copyright = new String(jsonObject.get("list")
                            .getAsJsonArray().get(0).getAsJsonObject().get(
                                    "all_rights").getAsString().toString()
                            .getBytes(), "utf-8");
                    String tecSup = new String(jsonObject.get("list")
                            .getAsJsonArray().get(0).getAsJsonObject().get(
                                    "support").getAsString().toString()
                            .getBytes(), "utf-8");
                    String tel = new String(jsonObject.get("list")
                            .getAsJsonArray().get(0).getAsJsonObject().get(
                                    "contact").getAsString().toString()
                            .getBytes(), "utf-8");
                    String url = new String(jsonObject.get("list")
                            .getAsJsonArray().get(0).getAsJsonObject().get(
                                    "web_site").getAsString().toString()
                            .getBytes(), "utf-8");
                    /* 将关于信息保存到SharedPreferences中 */
                    PreferencesUtil pu = new PreferencesUtil(Splash.this);
                    pu.Write(APPABOUTGROUP, APPABOUTCOPYRIGHT, copyright);
                    pu.Write(APPABOUTGROUP, APPABOUTTECSUP, tecSup);
                    pu.Write(APPABOUTGROUP, APPABOUTTEL, tel);
                    pu.Write(APPABOUTGROUP, APPABOUTURL, url);
                } catch (UnsupportedEncodingException e) {
                    //启动登录页面
                    StartActivity();
                }
            }
            //启动登录页面
            StartActivity();
        } catch (Exception e) {
            //启动登录页面
            StartActivity();
        }
    }

    /**
     * 启动登录页面
     */
    private void StartActivity() {
        Intent intent = new Intent(Splash.this, Login.class);
        Splash.this.startActivity(intent);
        finish();
    }

    /**
     * 响应对话框“确定”按钮监听器
     */
    OnClickListener oKClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            getAppAboutInfo();
        }
    };

    /**
     * 响应对话框“取消”按钮监听器
     */
    OnClickListener cancelClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }
    };

}

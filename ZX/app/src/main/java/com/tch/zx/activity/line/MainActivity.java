package com.tch.zx.activity.line;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.tch.zx.R;
import com.tch.zx.activity.ChiefActivity;
import com.tch.zx.activity.login_register.GuideActivity;
import com.tch.zx.application.MyApplication;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBean;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.util.RongUtil;

import java.util.List;

import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 主页页面
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 跳转
     */
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏,两种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        //集成使用Butterknife
        ButterKnife.bind(this);

        ifLogin();

    }

    /**
     * 判断是否第一次进入
     */
    private void ifLogin() {
        DaoSession daoSession = ((MyApplication) getApplication()).getDaoSession();
        UserBeanDao userBeanDao = daoSession.getUserBeanDao();
        List<UserBean> list = userBeanDao.loadAll();

        if (list != null && list.size() > 0) {
            Log.e("Main", "Token=====" + list.get(0).getYunToken());
//            connect(list.get(0).getYunToken());
            RongUtil.connect(list.get(0).getYunToken());
            intent = new Intent(this, ChiefActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 连接服务器，在整个应用程序全局，只需要调用一次
     * 如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     *              //     * @param callback 连接回调。
     * @return RongIM  客户端核心类的实例。
     */
    private void connect(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                Log.e("TAG", "--onIncorrect");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                Log.e("TAG", "--onSuccess" + userid);
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("TAG", "--onError" + errorCode);
            }
        });
    }

}

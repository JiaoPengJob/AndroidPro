package com.tch.youmuwa.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.tch.youmuwa.R;
import com.tch.youmuwa.application.MyApplication;
import com.tch.youmuwa.bean.parameters.CheckVersionParam;
import com.tch.youmuwa.bean.result.BaseBean;
import com.tch.youmuwa.bean.result.CheckVersionResult;
import com.tch.youmuwa.dao.DaoSession;
import com.tch.youmuwa.dao.UserInfo;
import com.tch.youmuwa.dao.UserInfoDao;
import com.tch.youmuwa.http.presenter.PresenterImpl;
import com.tch.youmuwa.http.view.ClientBaseView;
import com.tch.youmuwa.ui.activity.employer.EmployerActivity;
import com.tch.youmuwa.ui.activity.login.GuideActivity;
import com.tch.youmuwa.ui.activity.login.LoginActivity;
import com.tch.youmuwa.ui.activity.login.WorkerPerfectDataActivity;
import com.tch.youmuwa.ui.activity.worker.WorkerMainActivity;
import com.tch.youmuwa.util.GsonUtil;
import com.tch.youmuwa.util.HelperUtil;
import com.tch.youmuwa.util.SharedPrefsUtil;

import java.util.List;

/**
 * 首页
 * 每次启动展示页面
 */
public class MainActivity extends BaseActivtiy {

    private Intent intent;
    private DaoSession daoSession;
    private UserInfoDao userInfoDao;

    private PresenterImpl<Object> presenter;
    private CheckVersionParam cvp;
    private CheckVersionResult cvr = new CheckVersionResult();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkversion();
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        userInfoDao = daoSession.getUserInfoDao();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                boolean first = SharedPrefsUtil.getValue(MainActivity.this, "appFirst", true);
                if (first) {
                    intent = new Intent(MainActivity.this, GuideActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("cvr", cvr);
                    intent.putExtras(b);
                    startActivity(intent);
                    MainActivity.this.finish();
                    SharedPrefsUtil.putValue(MainActivity.this, "appFirst", false);
                } else {
                    List<UserInfo> userInfos = userInfoDao.loadAll();
                    if (userInfos != null && userInfos.size() > 0) {
                        if (userInfos.get(0).getResult() == 0) {
                            intent = new Intent(MainActivity.this, WorkerPerfectDataActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("phone", userInfos.get(0).getPhone());
                            bundle.putString("pwd", userInfos.get(0).getPwd());
                            bundle.putInt("type", userInfos.get(0).getType());
                            intent.putExtras(bundle);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            if (SharedPrefsUtil.getValue(MainActivity.this, "isEmployer", true)) {
                                intent = new Intent(MainActivity.this, EmployerActivity.class);
                                Bundle b = new Bundle();
                                b.putSerializable("cvr", cvr);
                                intent.putExtras(b);
                                startActivity(intent);
                            } else {
                                intent = new Intent(MainActivity.this, WorkerMainActivity.class);
                                Bundle b = new Bundle();
                                b.putSerializable("cvr", cvr);
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        }
                        MainActivity.this.finish();
                    } else {
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }
                }
            }
        }, 2000);
    }

    /**
     * 检查版本
     */
    private void checkversion() {
        try {
            cvp = new CheckVersionParam(HelperUtil.getVersionName(MainActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        presenter = new PresenterImpl<Object>(MainActivity.this);
        presenter.onCreate();
        presenter.checkversion(cvp);
        presenter.attachView(versionView);
    }

    private ClientBaseView<Object> versionView = new ClientBaseView<Object>() {
        @Override
        public void onSuccess(BaseBean<Object> baseBean) {
            if (baseBean.getState() == 1 && baseBean.getData() != null) {
                cvr = (CheckVersionResult) GsonUtil.parseJson(baseBean.getData(), CheckVersionResult.class);
            }
        }

        @Override
        public void onError(String result) {
            Log.e("Error", "versionView--" + result);
        }
    };

}

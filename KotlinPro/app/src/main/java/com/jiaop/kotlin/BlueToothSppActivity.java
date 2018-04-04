package com.jiaop.kotlin;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.jiaop.libs.base.JPBaseActivity;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BlueToothSppActivity extends JPBaseActivity {

    @BindView(R.id.rvBlueToothList)
    RecyclerView rvBlueToothList;

    @Override
    protected void initView() {
        AndPermission.with(this)
                .permission(Permission.ACCESS_FINE_LOCATION)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        // TODO what to do.
                    }
                }).onDenied(new Action() {
            @Override
            public void onAction(List<String> permissions) {
                // TODO what to do
            }
        }).start();

        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setMaxConnectCount(8)
                .setOperateTimeout(5000);

        initBlueToothList();
    }

    private CommonAdapter blueAdapter;

    private void initBlueToothList() {
        rvBlueToothList.setLayoutManager(new LinearLayoutManager(this));
        rvBlueToothList.setAdapter(blueAdapter = new CommonAdapter<BleDevice>(this,
                R.layout.item_bluetooth, new ArrayList<BleDevice>()) {
            @Override
            protected void convert(ViewHolder holder, BleDevice item, int position) {
                holder.setText(R.id.tvName, "Name = " + item.getName());
                holder.setText(R.id.tvMac, "Mac = " + item.getMac());
                holder.setText(R.id.tvRssi, "Rssi = " + item.getRssi());
            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_blue_tooth_spp;
    }

    @Override
    protected void initWiFiData() {

    }

    @Override
    protected void initNetData() {

    }

    @Override
    protected void initOfflineData() {

    }

    @Override
    protected int statusBarColor() {
        return R.color.colorAccent;
    }

    @OnClick(R.id.btBlue)
    void btBlueClick() {
        if (BleManager.getInstance().isSupportBle()) {
            Toast.makeText(this, "当前设备支持BLE", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "当前设备不支持BLE", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btBlueOpen)
    void openBlueTooth() {
        //打开蓝牙
        BleManager.getInstance().enableBluetooth();
    }

    @OnClick(R.id.btBlueClose)
    void closeBlueTooth() {
        //关闭蓝牙
        BleManager.getInstance().disableBluetooth();
    }

    @OnClick(R.id.btBlueScan)
    void scanBluetooth() {
        //配置扫描规则,无需配置，开启默认扫描规则
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
//                .setServiceUuids(serviceUuids)      // 只扫描指定的服务的设备，可选
//                .setDeviceName(true, names)         // 只扫描指定广播名的设备，可选
//                .setDeviceMac(mac)                  // 只扫描指定mac的设备，可选
                .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(0)              // 单位：毫秒，扫描超时时间，可选，默认10秒；小于等于0表示不限制扫描时间
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
        //进行扫描
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                //开始扫描
                Toast.makeText(BlueToothSppActivity.this, "开始扫描...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScanning(BleDevice result) {
                //扫描到一个复合规则的蓝牙设备
                Log.e("LOG -- ", "\nDevice = " + result.getDevice()
                        + "\nName = " + result.getName()
                        + "\nKey = " + result.getKey()
                        + "\nMac = " + result.getMac()
                        + "\nRssi = " + result.getRssi()
                        + "\nScanRecord = " + result.getScanRecord()
                        + "\nTimestampNanos = " + result.getTimestampNanos());

                blueAdapter.getDatas().add(result);
                blueAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                //扫描结束，列出所有符合扫描规则的蓝牙设备
                Toast.makeText(BlueToothSppActivity.this,
                        "一共扫描到" + scanResultList.size() + "台设备", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btBlueSend)
    void btBlueSend() {

    }

    @OnClick(R.id.btBlueGet)
    void btBlueGet() {

    }

}

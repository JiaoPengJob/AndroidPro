package com.tch.zx.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import com.tch.zx.bean.PeoplePhoneInfo;
import com.tch.zx.util.HelperUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * 后台匹配通讯录
 */

public class FindNewFriendsService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<PeoplePhoneInfo> peos = getDataList();
        for (PeoplePhoneInfo p : peos) {
            Log.d("TAG", p.getName());
            for (String s : p.getPhone()) {
                Log.d("TAG", s);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 加载数据
     *
     * @return
     */
    private List<PeoplePhoneInfo> getDataList() {
        List<PeoplePhoneInfo> list = new ArrayList<PeoplePhoneInfo>();

        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.RawContacts.CONTENT_URI,
                new String[]{ContactsContract.RawContacts._ID, ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY}//
                , null, null, null);
        while (cursor.moveToNext()) {
            PeoplePhoneInfo info = new PeoplePhoneInfo();
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts._ID));// "_id"
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY));// "display_name"
            info.setName(name);
            List<String> phones = new ArrayList<String>();
            // 联系人号码
            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI//
                    , new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}// "data1"
                    , "raw_contact_id=?", new String[]{id}, null);
            if (phoneCursor.moveToNext()) {
                String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phones.add(HelperUtil.dealPhoneNumber(number));
            }
            info.setPhone(phones);
            list.add(info);
        }
        return list;
    }
}

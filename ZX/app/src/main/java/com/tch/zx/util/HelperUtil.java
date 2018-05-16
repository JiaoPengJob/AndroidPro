package com.tch.zx.util;

import android.app.Activity;
import android.app.Notification;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.gson.Gson;
import com.tch.zx.application.MyApplication;
import com.tch.zx.bean.PeoplePhoneInfo;
import com.tch.zx.dao.green.DaoSession;
import com.tch.zx.dao.green.UserBean;
import com.tch.zx.dao.green.UserBeanDao;
import com.tch.zx.http.bean.parameter.BaseBean;
import com.tch.zx.http.bean.parameter.Header;
import com.tch.zx.http.bean.result.LoginResultBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 工具类
 */

public class HelperUtil {

    private static Time timeT;

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        if (context == null) {
            return 0;
        }
        return ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        if (context == null) {
            return 0;
        }
        return ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
    }

    /**
     * 是否是正确的手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][3578]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * 毫秒转换成时分秒
     *
     * @param time
     * @param type
     * @return
     */
    public static String timeFormat(long time, String type) {
        SimpleDateFormat formatter = new SimpleDateFormat(type);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return formatter.format(time);
    }

    /**
     * 拨打电话,调用页面,不拨出
     *
     * @param context
     * @param phoneNum
     */
    public static void callPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Bitmap转换成byte[]
     *
     * @param bitmap
     * @return
     */
    public static byte[] bmpToByteArray(Bitmap bitmap) {
        int bytes = bitmap.getByteCount();
        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buf);
        return buf.array();
    }

    /**
     * byte[]转换成Bitmap
     *
     * @param byteArray 数据
     * @param width     宽度
     * @param height    高度
     * @param config    图片类型
     * @return
     */
    public static Bitmap ByteArrayToBmp(byte[] byteArray, int width, int height, Bitmap.Config config) {
        Bitmap stitchBmp = Bitmap.createBitmap(width, height, config);

        stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(byteArray));
        return stitchBmp;
    }

    /**
     * 设置输入框在输入法上面
     *
     * @param decorView
     * @param contentView
     * @return
     */
    public static ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
    }

    /**
     * 获取通讯录信息
     *
     * @param context
     * @param uri
     * @return
     */
    public static PeoplePhoneInfo getPhoneContacts(Context context, Uri uri) {
        PeoplePhoneInfo info = new PeoplePhoneInfo();
        List<String> list = new ArrayList<String>();

        //得到ContentResolver对象
        ContentResolver cr = context.getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            info.setName(cursor.getString(nameFieldColumnIndex));
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            // 获得联系人的电话号码列表
            Cursor phoneCursor = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            + "=" + ContactId, null, null);
            if (phoneCursor.moveToFirst()) {
                do {
                    //遍历所有的联系人下面所有的电话号码
                    String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    list.add(HelperUtil.dealPhoneNumber(phoneNumber));
                } while (phoneCursor.moveToNext());
            }
            info.setPhone(list);
            phoneCursor.close();
            cursor.close();
        } else {
            return null;
        }
        return info;
    }

    /**
     * 处理手机格式
     *
     * @param phone
     * @return
     */
    public static String dealPhoneNumber(String phone) {
        String str;
        str = phone.replace("-", "");
        str = str.replaceAll("\\s*", "");
        return str;
    }

    /**
     * 获取指定格式时间
     *
     * @param format
     * @return
     */
    public static String getTime(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    /**
     * 获取请求内容头
     *
     * @param map
     * @return
     */
    public static String getParameter(Map<String, Object> map) {

        BaseBean baseBean = new BaseBean();

        Header header = new Header();
        header.setChannel("android");
        header.setSign(SignUtil.getSign(map));
        header.setTimestamp(HelperUtil.getTime("yyyyMMddHHmmss"));

        baseBean.setBody(map);
        baseBean.setHeader(header);

        return new Gson().toJson(baseBean);
    }

    /**
     * 将获取到的用户信息转换为数据库形式,进行长保存
     *
     * @param loginType
     * @param loginBean
     * @return
     */
    public static UserBean loginBeanToUserBean(int loginType, LoginResultBean.ResultBean.ResponseObjectBean loginBean) {
        UserBean userBean = new UserBean();

        userBean.setLoginType(String.valueOf(loginType));
        userBean.setAdd_frid_set(loginBean.getAdd_frid_set());
        userBean.setAdrCity(loginBean.getAdrCity());
        userBean.setAdrCounty(loginBean.getAdrCounty());
        userBean.setAdrProvince(loginBean.getAdrProvince());
        userBean.setAppUserId(loginBean.getAppUserId());
        userBean.setAppUserName(loginBean.getAppUserName());
        userBean.setAppUserPic(loginBean.getAppUserPic());
        userBean.setCategoryName(loginBean.getCategoryName());
        userBean.setCompanyAddress(loginBean.getCompanyAddress());
        userBean.setCompanyGif(loginBean.getCompanyGif());
        userBean.setCompanyId(loginBean.getCompanyId());
        userBean.setCompanyIndustryFType(loginBean.getCompanyIndustryFType());
        userBean.setCompanyIndustrySType(loginBean.getCompanyIndustrySType());
        userBean.setCompanyIntroduce(loginBean.getCompanyIntroduce());
        userBean.setCompanyLogo(loginBean.getCompanyLogo());
        userBean.setCompanyName(loginBean.getCompanyName());
        userBean.setCompanyPosition(loginBean.getCompanyPosition());
        userBean.setCompanyVideo(loginBean.getCompanyVideo());
        userBean.setEmail(loginBean.getEmail());
        userBean.setFollowIndustry(loginBean.getFollowIndustry());
        userBean.setNeedIndustry(loginBean.getNeedIndustry());
        userBean.setOfferIndustry(loginBean.getOfferIndustry());
        userBean.setAppUserSex(loginBean.getAppUserSex());
        userBean.setStypeName(loginBean.getStypeName());
        userBean.setUserFreeType(loginBean.getUserFreeType());
        userBean.setAppUserIntroduce(loginBean.getAppUserIntroduce());
        userBean.setUserType(loginBean.getUserType());
        userBean.setYunToken(loginBean.getYunToken());
        userBean.setOnly_dk(loginBean.getOnly_dk());

        String picList = new Gson().toJson(loginBean.getCompanyPicList());
        userBean.setCompanyPicList(picList);

        return userBean;
    }

    /**
     * 截取时间
     *
     * @param time
     * @return
     */
    public static String getShortTime(String time) {
        if (time != null && !time.equals("")) {
            return time.substring(0, 10);
        } else {
            return "";
        }

    }

    /**
     * 获取用户id
     *
     * @param context
     * @return
     */
    public static String getAppUserId(Activity context) {
        DaoSession daoSession = ((MyApplication) context.getApplication()).getDaoSession();
        UserBeanDao userBeanDao = daoSession.getUserBeanDao();
        return userBeanDao.loadAll().get(0).getAppUserId();
    }

    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            //判断file的类型
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }

    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param str1 the first date
     * @param str2 the second date
     * @return true <br/>false
     */
    public static boolean isDateOneBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    /**
     * 是否是正确的邮箱地址
     *
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
        String checkemail = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(checkemail);
        Matcher matcher = pattern.matcher(strEmail);
        return matcher.matches();
    }

    /**
     * 压缩图片（质量压缩）
     *
     * @param bitmap
     */
    public static File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        File file = new File(Environment.getExternalStorageDirectory(), filename + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                Log.e("TAG", e.getMessage());
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            Log.e("TAG", e.getMessage());
            e.printStackTrace();
        }
//        recycleBitmap(bitmap);
        return file;
    }

    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps == null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }

    /**
     * 通过某个字符切割字符串
     *
     * @param str
     * @param key
     * @return
     */
    public static String[] stringToArray(String str, String key) {
        String[] temp = null;
        temp = str.split(key);
        return temp;
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分00秒"）
     *
     * @param time
     * @return
     */
    public static String times(String time, String str) {
        SimpleDateFormat sdr = new SimpleDateFormat(str);
        Long i = Long.parseLong(time);
        String times = sdr.format(new Date(i));
        return times;
    }

    /**
     * 判断时间区
     */
    public static String changeDate(String time) {
        String newTime;
        timeT = new Time();
        timeT.setToNow();
        if (time == null || time.equals("")) {
            newTime = "";
        } else {
            long diff = (System.currentTimeMillis() - Long.parseLong(time)) * 1000;
            if (diff < 60) {
                newTime = "刚刚";
            } else if (diff < 60 * 60) {
                newTime = String.valueOf(diff / 60) + "分钟前";
            } else if (diff < 60 * 60 * 24) {
                newTime = String.valueOf(diff / (60 * 60)) + "小时前";
            } else if (diff < 60 * 60 * 24 * 7) {
                newTime = String.valueOf(diff / (60 * 60 * 24)) + "天前";
            } else if (diff < 60 * 60 * 24 * 7 * 4) {
                newTime = String.valueOf(diff / (60 * 60 * 24 * 7)) + "星期前";
            } else if ((timeT.year - Integer.parseInt(times(time, "yyyy"))) < 1) {
                newTime = times(time, "MM-dd HH:mm");
            } else {
                newTime = times(time, "yyyy-MM-dd HH:mm");
            }
        }
        return newTime;
    }

}

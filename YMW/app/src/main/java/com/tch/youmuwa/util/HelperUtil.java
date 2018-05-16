package com.tch.youmuwa.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.tch.youmuwa.dao.EmployerSearchHistory;
import com.tch.youmuwa.dao.WorkerSearchHistory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tch.youmuwa.util.ImageUtils.calculateInSampleSize;
import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/**
 * 工具帮助类
 */

public class HelperUtil {

    static Pattern p = Pattern.compile("[0-9]*");

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
     * 读取asset文件夹中文件内容
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String readAssert(Context context, String fileName) {
        String resultString = "";
        try {
            InputStream inputStream = context.getResources().getAssets().open(fileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            resultString = new String(buffer, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

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
     * 将View转换成Bitmap
     *
     * @return
     */
    public static Bitmap getViewBitmap(View comBitmap) {
        Bitmap bitmap = null;
        if (comBitmap != null) {
            comBitmap.clearFocus();
            comBitmap.setPressed(false);

            boolean willNotCache = comBitmap.willNotCacheDrawing();
            comBitmap.setWillNotCacheDrawing(false);

            // Reset the drawing cache background color to fully transparent
            // for the duration of this operation
            int color = comBitmap.getDrawingCacheBackgroundColor();
            comBitmap.setDrawingCacheBackgroundColor(0);
            float alpha = comBitmap.getAlpha();
            comBitmap.setAlpha(1.0f);

            if (color != 0) {
                comBitmap.destroyDrawingCache();
            }

            int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY);
            comBitmap.measure(widthSpec, heightSpec);
            comBitmap.layout(0, 0, 0, 0);

            comBitmap.buildDrawingCache();
            Bitmap cacheBitmap = comBitmap.getDrawingCache();
            comBitmap.setDrawingCacheEnabled(false);
            if (cacheBitmap == null) {
                Log.e("view.ProcessImageToBlur", "failed getViewBitmap(" + comBitmap + ")",
                        new RuntimeException());
                return null;
            }
            bitmap = Bitmap.createBitmap(cacheBitmap);
            // Restore the view
            comBitmap.setAlpha(alpha);
            comBitmap.destroyDrawingCache();
            comBitmap.setWillNotCacheDrawing(willNotCache);
            comBitmap.setDrawingCacheBackgroundColor(color);
        }
        return bitmap;
    }

    /**
     * 是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isDigital(String str) {
        return p.matcher(str).matches();
    }

    /**
     * Date转换为String
     *
     * @param date
     * @return
     */
    public static String simpleDate(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * String转换为Date
     *
     * @param data
     * @return
     */
    public static Date simpleDate(String data) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String StringDateToSingle(String date) {
        String str = date.substring(0, date.indexOf(" "));
        return str.replaceAll("-", "/");
    }

    /**
     * 根据Uri获取对应的path
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 获取图片的Uri
     *
     * @param context
     * @param path
     * @return
     */
    public static Uri getUri(Context context, String path) {
        Uri uri = null;
        File file = new File(path);
        uri = Uri.fromFile(file);
        return uri;
    }

    /**
     * 获取当前时间戳，10位数
     *
     * @return
     */
    public static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    //把bitmap转换成String
    public static String bitmapToString(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
        bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

    /**
     * 获取当前时间
     *
     * @param format 时间格式
     * @return
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    /**
     * 是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 雇主端搜索历史list去重复
     *
     * @param list
     */
    public static List<EmployerSearchHistory> removeESHuplicate(List<EmployerSearchHistory> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getEmployerSearchHistory().equals(list.get(i).getEmployerSearchHistory())) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    /**
     * 工人端搜索历史list去重复
     *
     * @param list
     */
    public static List<WorkerSearchHistory> removeWSHuplicate(List<WorkerSearchHistory> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getWorkerSearchHistory().equals(list.get(i).getWorkerSearchHistory())) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager       设置RecyclerView对应的manager
     * @param mRecyclerView 当前的RecyclerView
     * @param n             要跳转的位置
     */
    public static void moveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }
    }

    /**
     * 将奇数个转义字符变为偶数个
     *
     * @param s
     * @return
     */
    public static String getDecodeJSONStr(String s) {
        StringBuilder sb = new StringBuilder();
        char c;
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            switch (c) {
                case '\\':
                    sb.append("\\\\");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前APP版本号
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static String getVersionName(Context context) throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    /**
     * 根据身份证号码计算年龄
     *
     * @param idNumber 考虑到了15位身份证，但不一定存在
     */
    public static int getAgeByIDNumber(String idNumber) {
        String dateStr;
        if (idNumber.length() == 15) {
            dateStr = "19" + idNumber.substring(6, 12);
        } else if (idNumber.length() == 18) {
            dateStr = idNumber.substring(6, 14);
        } else {//默认是合法身份证号，但不排除有意外发生
            return 0;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date birthday = simpleDateFormat.parse(dateStr);
            return getAgeByDate(birthday);
        } catch (ParseException e) {
            return 0;
        }
    }

    public static int getAgeByDate(Date birthday) {
        Calendar calendar = Calendar.getInstance();
        //calendar.before()有的点bug
        if (calendar.getTimeInMillis() - birthday.getTime() < 0L) {
            return 0;
        }

        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(birthday);

        int yearBirthday = calendar.get(Calendar.YEAR);
        int monthBirthday = calendar.get(Calendar.MONTH);
        int dayOfMonthBirthday = calendar.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirthday;

        if (monthNow <= monthBirthday && monthNow == monthBirthday && dayOfMonthNow < dayOfMonthBirthday || monthNow < monthBirthday) {
            age--;
        }
        return age;
    }

    /**
     * 验证输入的身份证号是否合法
     */
    public static boolean isLegalId(String id) {
        if (id.toUpperCase().matches("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 隐藏键盘
     *
     * @param context
     * @param View
     */
    public static void hideInput(Context context, View View) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //若返回true，则表示输入法打开
        if (im.isActive()) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(View, InputMethodManager.SHOW_FORCED);
            imm.hideSoftInputFromWindow(View.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    private static long ONE_DAY_MS = 24 * 60 * 60 * 1000;

    /**
     * 计算两个日期之间的日期
     */
    public static List<String> betweenDays(Date date_start, Date date_end) {

        List<String> date = new ArrayList<String>();

        //计算日期从开始时间于结束时间的0时计算
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(date_start);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(date_end);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        int s = (int) ((toCalendar.getTimeInMillis() - fromCalendar.getTimeInMillis()) / (ONE_DAY_MS));
        if (s > 0) {
            for (int i = 0; i <= s; i++) {
                long todayDate = fromCalendar.getTimeInMillis() + i * ONE_DAY_MS;
                Date d1 = new Date(todayDate);
                /**
                 * yyyy-MM-dd E :2012-09-01
                 */
                date.add(getCustonFormatTime(d1, "yyyy-MM-dd"));
            }
        } else {//此时在同一天之内
            date.add(getCustonFormatTime(date_start, "yyyy-MM-dd"));
        }
        return date;
    }

    /**
     * 格式化传入的时间
     *
     * @param time      需要格式化的时间
     * @param formatStr 格式化的格式
     * @return
     */
    public static String getCustonFormatTime(Date time, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(time);
    }

}

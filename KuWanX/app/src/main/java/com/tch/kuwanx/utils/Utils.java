package com.tch.kuwanx.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tch.kuwanx.bean.Income;
import com.tch.kuwanx.bean.MyConversation;
import com.tch.kuwanx.bean.Points;
import com.tch.kuwanx.result.UserIntegralResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rong.imlib.model.Conversation;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/**
 * Created by Jiaop on 2017/10/23.
 * 帮助类
 */

public class Utils {

    /**
     * 判断是否是手机号
     *
     * @param str
     * @return
     */
    public static boolean isPhone(String str) {
        Pattern pattern = Pattern.compile("1[0-9]{10}");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
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
     * 判断软键盘是否弹出
     */
    public static boolean isSHowKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        if (imm.hideSoftInputFromWindow(view.getWindowToken(), 0)) {
            imm.showSoftInput(view, 0);
            return true;
            //软键盘已弹出
        } else {
            return false;
            //软键盘未弹出
        }
    }

    public static void showInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏键盘
     */
    public static void hindKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 获取drawable的路径
     *
     * @param context
     * @param id
     * @return
     */
    public static String getDrawableResPath(Context context, int id) {
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + context.getResources().getResourcePackageName(id) + "/"
                + context.getResources().getResourceTypeName(id) + "/"
                + context.getResources().getResourceEntryName(id);
    }

    /**
     * 获取drawable的uri
     *
     * @param context
     * @param id
     * @return
     */
    public static Uri getUriFromDrawableRes(Context context, int id) {
        return Uri.parse(getDrawableResPath(context, id));
    }

    /**
     * EditText竖直方向能否够滚动
     *
     * @param editText 须要推断的EditText
     * @return true：能够滚动   false：不能够滚动
     */
    public static boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;
        if (scrollDifference == 0) {
            return false;
        }
        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * byteArr转bitmap
     *
     * @param imgByte 字节数组
     * @return bitmap对象
     */
    public static Bitmap bytes2Bitmap(byte[] imgByte) {
        InputStream input = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        input = new ByteArrayInputStream(imgByte);
        SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
                input, null, options));
        bitmap = (Bitmap) softRef.get();
        if (imgByte != null) {
            imgByte = null;
        }
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * path转bitmap
     *
     * @param path
     * @return
     */
    public static Bitmap GetBitmap(String path) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = false;
        opt.inInputShareable = false;
        //获取资源图片
        return BitmapFactory.decodeFile(path, opt);
    }

    /**
     * 读取路径中的图片，然后将其转化为缩放后的bitmap
     *
     * @param path
     */
    public static Bitmap saveBefore(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回bm为空
        options.inJustDecodeBounds = false;
        options.inSampleSize = 2; // 图片长宽各缩小二分之一
        bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    /**
     * 保存图片到sd卡
     *
     * @param photoBitmap 位图
     */
    public static String savePhotoToSDCard(Bitmap photoBitmap) {
        String sdCardDir = Environment.getExternalStorageDirectory().getPath() + "/imgs/";
        File dirFile = new File(sdCardDir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File f = new File(sdCardDir, String.valueOf(System.currentTimeMillis() / 1000) + ".png");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            photoBitmap.compress(Bitmap.CompressFormat.PNG, 20, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f.getPath();
    }

    /**
     * 压缩图片（质量压缩）
     *
     * @param bitmap
     */
    public static String compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 300) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        String sdCardDir = Environment.getExternalStorageDirectory().getPath() + "/imgs/";
        File file = new File(sdCardDir, String.valueOf(System.currentTimeMillis() / 1000) + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        recycleBitmap(bitmap);
        return file.getPath();
    }

    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps == null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
                bm = null;
            }
        }
    }

    /**
     * 删除缓存拍照
     */
    public static void deleteFiles(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFiles(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 比较真实完整的判断身份证号码的工具
     *
     * @param IdCard 用户输入的身份证号码
     * @return [true符合规范, false不符合规范]
     */
    public static boolean isRealIDCard(String IdCard) {
        if (IdCard != null) {
            int correct = new IdCardUtil(IdCard).isCorrect();
            if (0 == correct) {// 符合规范
                return true;
            }
        }
        return false;
    }

    /**
     * 手机号中间4位*号显示
     *
     * @param phone
     * @return
     */
    public static String centerGonePhone(String phone) {
        StringBuilder sb = new StringBuilder();
        sb.replace(3, 7, "****");
        sb.substring(7, 11);
        return sb.toString();
    }

    public static String getVersionName(Context context) throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    /**
     * 判断时间区
     */
    public static String getDateArea(String time) {
        String newTime;
        Time timeT = new Time();
        timeT.setToNow();
        if (time == null || time.equals("")) {
            newTime = "";
        } else {
            long diff = (System.currentTimeMillis() - Long.parseLong(time)) / 1000;
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
                newTime = times(time, "MM.dd HH:mm");
            } else {
                newTime = times(time, "yyyy.MM.dd HH:mm");
            }
        }
        return newTime;
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
     * 判断是否为有效的网站格式
     *
     * @param uri
     * @return
     */
    public static boolean isUri(String uri) {
        Pattern pattern = Pattern
                .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        return pattern.matcher(uri).matches();
    }

    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    public static long dateToLong(Date date) {
        return date.getTime();
    }

    public static String time2DayString(String time) {
        String newTime = time.substring(0, time.indexOf(" "));
        return newTime;
    }

    /**
     * 转换HTML为可显示的字符串
     *
     * @param html
     * @return
     */
    public static Spanned html2Spanned(String html) {
        return Html.fromHtml(html);
    }

    /**
     * List转换为字符串，并以固定分隔符分割
     *
     * @param list
     * @param separator
     * @return
     */
    public static String join(List<String> list, char separator) {
        return join(list, new String(new char[]{separator}));
    }

    public static String join(List<String> list, String separator) {
        return list == null ? "" : TextUtils.join(separator, list);
    }

    public static List<Income> getIncome(List<Points> points) {
        List<Income> ins = new ArrayList<>();
        List<Points> list = new ArrayList<>();
        list.addAll(points);
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getDate().equals(list.get(i).getDate())) {
                    list.remove(j);
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            ins.add(new Income());
            for (Points ps : points) {
                if (ps.getDate().equals(list.get(i).getDate())) {
                    UserIntegralResult.ResultBean item = (UserIntegralResult.ResultBean) ps.getItem();
                    if (item.getIntegral_value().indexOf("+") != -1) {
                        ins.get(i).setPlus(ins.get(i).getPlus() + Integer.parseInt(
                                item.getIntegral_value().substring(1, item.getIntegral_value().length())
                        ));
                    } else {
                        ins.get(i).setLess(ins.get(i).getLess() + Integer.parseInt(
                                item.getIntegral_value().substring(1, item.getIntegral_value().length())
                        ));
                    }
                }
            }
        }
        return ins;
    }

    /**
     * 获取消息
     *
     * @param c
     * @return
     */
    public static MyConversation getMyMc(Conversation c) {
        MyConversation mc = new MyConversation();
        mc.setConversationTitle(c.getConversationTitle());
        mc.setConversationType(c.getConversationType());
        mc.setDraft(c.getDraft());
        mc.setLatestMessage(c.getLatestMessage());
        mc.setLatestMessageId(c.getLatestMessageId());
        mc.setMentionedCount(c.getMentionedCount());
        mc.setNotificationStatus(c.getNotificationStatus());
        mc.setObjectName(c.getObjectName());
        mc.setPortraitUrl(c.getPortraitUrl());
        mc.setReceivedStatus(c.getReceivedStatus());
        mc.setReceivedTime(c.getReceivedTime());
        mc.setSenderUserId(c.getSenderUserId());
        mc.setSenderUserName(c.getSenderUserName());
        mc.setSentStatus(c.getSentStatus());
        mc.setSentTime(c.getSentTime());
        mc.setTargetId(c.getTargetId());
        mc.setTop(c.isTop());
        mc.setUnreadMessageCount(c.getUnreadMessageCount());
        return mc;
    }
}

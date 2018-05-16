package com.tch.kuwanx.message;

import android.os.Parcel;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by jiaop on 2017/11/24.
 */

/**
 * 注解名：MessageTag ；属性：value ，flag； value 即 ObjectName 是消息的唯一标识不可以重复，
 * 开发者命名时不能以 RC 开头，避免和融云内置消息冲突；flag 是用来定义消息的可操作状态。
 * 如下面代码段，自定义消息名称 KwMessage ，vaule 是 app:custom ，
 * flag 是 MessageTag.ISCOUNTED | MessageTag.ISPERSISTED 表示消息计数且存库。
 * RCD:KWMsg: 这是自定义消息类型的名称，测试的时候用"RCD:KWMsg"；
 */
@MessageTag(value = "RCD:KWMsg", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class KwMessage extends MessageContent {

    private final static String TAG = "KwMessage";

    private String message_id;
    private String title;
    private String content;
    private String img_url;
    private String post_type;
    private String money;
    private String state;

    public KwMessage() {

    }

    /**
     * 实现 encode() 方法，该方法的功能是将消息属性封装成 json 串，
     * 再将 json 串转成 byte 数组，该方法会在发消息时调用，如下面示例代码：
     */
    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            if (!TextUtils.isEmpty(getMessage_id()))
                jsonObj.put("message_id", getMessage_id());

            if (!TextUtils.isEmpty(getMessage_id()))
                jsonObj.put("title", getTitle());

            if (!TextUtils.isEmpty(getMessage_id()))
                jsonObj.put("content", getContent());

            if (!TextUtils.isEmpty(getMessage_id()))
                jsonObj.put("img_url", getImg_url());

            if (!TextUtils.isEmpty(getMessage_id()))
                jsonObj.put("post_type", getPost_type());

            if (!TextUtils.isEmpty(getMessage_id()))
                jsonObj.put("money", getMoney());

            if (!TextUtils.isEmpty(getMessage_id()))
                jsonObj.put("state", getState());

        } catch (JSONException e) {
            Logger.wtf(TAG + "      JSONException:" + e.getMessage());
        }
        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 覆盖父类的 MessageContent(byte[] data) 构造方法，该方法将对收到的消息进行解析，
     * 先由 byte 转成 json 字符串，再将 json 中内容取出赋值给消息属性。
     */
    public KwMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("message_id"))
                setMessage_id(jsonObj.optString("message_id"));

            if (jsonObj.has("title"))
                setTitle(jsonObj.optString("title"));

            if (jsonObj.has("content"))
                setContent(jsonObj.optString("content"));

            if (jsonObj.has("img_url"))
                setImg_url(jsonObj.optString("img_url"));

            if (jsonObj.has("post_type"))
                setPost_type(jsonObj.optString("post_type"));

            if (jsonObj.has("money"))
                setMoney(jsonObj.optString("money"));

            if (jsonObj.has("state"))
                setState(jsonObj.optString("state"));

        } catch (JSONException e) {
            Logger.wtf(TAG + "      JSONException:" + e.getMessage());
        }
    }

    /**
     * 给消息赋值
     */
    public KwMessage(Parcel in) {
        //这里可继续增加你消息的属性
        setMessage_id(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setTitle(ParcelUtils.readFromParcel(in));
        setContent(ParcelUtils.readFromParcel(in));
        setImg_url(ParcelUtils.readFromParcel(in));
        setPost_type(ParcelUtils.readFromParcel(in));
        setMoney(ParcelUtils.readFromParcel(in));
        setState(ParcelUtils.readFromParcel(in));
    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<KwMessage> CREATOR = new Creator<KwMessage>() {

        @Override
        public KwMessage createFromParcel(Parcel source) {
            return new KwMessage(source);
        }

        @Override
        public KwMessage[] newArray(int size) {
            return new KwMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 将类的数据写入外部提供的 Parcel 中。
     *
     * @param dest  对象被写入的 Parcel。
     * @param flags 对象如何被写入的附加标志。
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, getMessage_id());
        ParcelUtils.writeToParcel(dest, getTitle());
        ParcelUtils.writeToParcel(dest, getContent());
        ParcelUtils.writeToParcel(dest, getImg_url());
        ParcelUtils.writeToParcel(dest, getPost_type());
        ParcelUtils.writeToParcel(dest, getMoney());
        ParcelUtils.writeToParcel(dest, getState());
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

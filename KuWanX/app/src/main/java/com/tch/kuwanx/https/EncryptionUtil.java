package com.tch.kuwanx.https;

import android.content.Context;

import com.google.gson.Gson;
import com.tch.kuwanx.https.encryption.Base64;
import com.tch.kuwanx.https.encryption.BaseBean;
import com.tch.kuwanx.https.encryption.Header;
import com.tch.kuwanx.https.encryption.MD5;
import com.tch.kuwanx.https.encryption.RSAEncrypt;
import com.tch.kuwanx.https.encryption.RSAmcl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by 马超龙
 * 对参数进行加密 工具
 */

public class EncryptionUtil {

    /**
     * 获取请求内容头
     *
     * @param map
     * @return
     */
    public static String getParameter(Context context, Map<String, Object> map) {
        BaseBean baseBean = new BaseBean();
        String publicKey = null;
        byte[] cipherData = new byte[0];
        try {
            publicKey = RSAEncrypt.loadPublicKeyByFile(context);
            String md5Val = MD5.getMD5Encode(createLinkString(map)).toUpperCase();
            cipherData = RSAmcl.encryptByPublicKeyForSpilt(md5Val.getBytes(), Base64.decode(publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Header header = new Header();
        header.setChannel("android");
        header.setSign(Base64.encode(cipherData));
        String date = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        header.setTimestamp(date);
        baseBean.setBody(map);
        baseBean.setHeader(header);
        return new Gson().toJson(baseBean);
    }

    public static String createLinkString(Map<String, Object> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = null;
            if (params.get(key).equals("null")) {
                continue;
            } else {
                value = params.get(key);
            }
            if (params.get(key) instanceof String || params.get(key) instanceof Integer
                    || params.get(key) instanceof BigDecimal || params.get(key) instanceof Double
                    || params.get(key) instanceof Long) {
                if (i == keys.size() - 1) {
                    prestr = prestr + key + "=" + value;
                } else {
                    prestr = prestr + key + "=" + value + "&";
                }
            }
        }
        return prestr;
    }
}
package com.tch.zx.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * @author maorw   maorw@tctogether.com
 * @Description:(签名工具类)
 * @ClassName: SignUtil
 * @date 2016年3月13日 下午2:19:59
 * <p>北京天成合科技有限公司版权所有</p>
 */
public class SignUtil {
    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
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
                if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                    prestr = prestr + key + "=" + value;
                } else {
                    prestr = prestr + key + "=" + value + "&";
                }
            }
        }
        return prestr;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * <p>
     * 需要排序并参与字符拼接的参数组
     *
     * @return 拼接后字符串
     */
    public static String createLinkString(List<Map<String, Object>> data) {

        String prestr = "";
        for (int i = 0; i < data.size(); i++) {
            List<String> keys = new ArrayList<String>(data.get(i).keySet());
            Collections.sort(keys);
            for (int j = 0; j < keys.size(); j++) {
                String key = keys.get(j);
                String value = data.get(i).get(key).toString();
                if (i == data.size() - 1 && j == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                    prestr = prestr + key + "=" + value;
                } else {
                    prestr = prestr + key + "=" + value + "&";
                }
            }
        }
        return prestr;
    }

    /**
     * 获取验证签名
     *
     * @param params
     * @return
     */
//    public static String getSign(Map<String, Object> params, String timeStamp) {
//        String sign = Md5Encrypt.md5(timeStamp + createLinkString(params) + ConstantData.PRIVATE_HTTP_KEY, "utf-8");
//        return sign;
//    }

    /**
     * 获取验证签名
     *
     * @param params
     * @return
     */
    public static String getSign(Map<String, Object> params) {
        String sign = Md5Encrypt.md5(createLinkString(params) + ConstantData.PRIVATE_HTTP_KEY, "utf-8");
        return sign;
    }

    /**
     * 获取验证签名
     *
     * @param params
     * @return
     */
    public static String getSign(List<Map<String, Object>> params, String timeStamp) {
        String sign = Md5Encrypt.md5(createLinkString(params) + ConstantData.PRIVATE_HTTP_KEY, "utf-8");
        return sign;
    }


}
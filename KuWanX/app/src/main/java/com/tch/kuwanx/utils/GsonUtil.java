package com.tch.kuwanx.utils;

import android.content.ContentValues;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * Created by JiaoP
 * Gson帮助类
 */

public class GsonUtil {

    private static Gson gson = new Gson();

    /**
     * 将一个对象转换成一个Json字符串
     *
     * @param t
     * @return
     */
    public static <T> String object2Json(T t) {
        if (t instanceof String) {
            return t.toString();
        } else {
            return gson.toJson(t);
        }
    }

    /**
     * 将Json字符串转换成对应对象
     *
     * @param jsonString Json字符串
     * @param clazz      对应字节码文件.class
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T json2Object(String jsonString, Class<T> clazz) {
        if (clazz == String.class) {
            return (T) jsonString;
        } else {
            return (T) gson.fromJson(jsonString, clazz);
        }
    }

    /**
     * 将Json的Object转换成对应对象
     *
     * @param response
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseJson(Object response, Class<T> clazz) {
        try {
            return gson.fromJson(gson.toJson(response), clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Json字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        try {
            return gson.toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将List集合转换为json字符串
     *
     * @param list List集合
     * @return
     */
    public static <T> String list2Json(List<T> list) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        try {
            for (int i = 0; i < list.size(); i++) {
                jsonObject = new JSONObject(object2Json(list.get(i)));
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (jsonObject != null) {
                jsonObject = null;
            }
        }
        return jsonArray.toString();
    }

    /**
     * 将数组转换成json字符串
     *
     * @param array 数组
     * @return
     */
    public static <T> String array2Json(T[] array) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        try {
            for (int i = 0; i < array.length; i++) {
                jsonObject = new JSONObject(object2Json(array[i]));
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (jsonObject != null) {
                jsonObject = null;
            }
        }
        return jsonArray.toString();
    }

    /**
     * 获取json字符串中的值
     *
     * @param json  json字符串
     * @param key   键值
     * @param clazz 所取数据类型，例如：Integer.class，String.class，Double.class，JSONObject.class
     * @return 存在则返回正确值，不存在返回null
     */
    public static <T> T getJsonObjectValue(String json, String key, Class<T> clazz) {
        try {
            return getJsonObjectValue(new JSONObject(json), key, clazz);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取jsonObject对象中的值
     *
     * @param jsonObject jsonObject对象
     * @param key        键值
     * @param clazz      所取数据类型，例如：Integer.class，String.class，Double.class，JSONObject.class
     * @return 存在则返回正确值，不存在返回null
     */
    @SuppressWarnings("unchecked")
    public static <T> T getJsonObjectValue(JSONObject jsonObject, String key, Class<T> clazz) {
        T t = null;
        try {
            if (clazz == Integer.class) {
                t = (T) Integer.valueOf(jsonObject.getInt(key));
            } else if (clazz == Boolean.class) {
                t = (T) Boolean.valueOf(jsonObject.getBoolean(key));
            } else if (clazz == String.class) {
                t = (T) String.valueOf(jsonObject.getString(key));
            } else if (clazz == Double.class) {
                t = (T) Double.valueOf(jsonObject.getDouble(key));
            } else if (clazz == JSONObject.class) {
                t = (T) jsonObject.getJSONObject(key);
            } else if (clazz == JSONArray.class) {
                t = (T) jsonObject.getJSONArray(key);
            } else if (clazz == Long.class) {
                t = (T) Long.valueOf(jsonObject.getLong(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * json字符串转换为ContentValues
     *
     * @param json json字符串
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static ContentValues json2ContentValues(String json) {
        ContentValues contentValues = new ContentValues();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator iterator = jsonObject.keys();
            String key;
            Object value;
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                value = jsonObject.get(key);
                String valueString = value.toString();
                if (value instanceof String) {
                    contentValues.put(key, valueString);
                } else if (value instanceof Integer) {
                    contentValues.put(key, Integer.valueOf(valueString));
                } else if (value instanceof Long) {
                    contentValues.put(key, Long.valueOf(valueString));
                } else if (value instanceof Double) {
                    contentValues.put(key, Double.valueOf(valueString));
                } else if (value instanceof Float) {
                    contentValues.put(key, Float.valueOf(valueString));
                } else if (value instanceof Boolean) {
                    contentValues.put(key, Boolean.valueOf(valueString));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new Error("Json字符串不合法：" + json);
        }
        return contentValues;
    }

}

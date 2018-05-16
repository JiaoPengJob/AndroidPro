package com.tch.zx.http;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * http发送文件到服务器
 */
public class NetworkImgAsyncTask extends AsyncTask<String, Integer, String> {

    String authorization;
    String urlStr;
    Map<String, String> param;
    Handler handler;
    int index;
    Map<String, File> files;

    public NetworkImgAsyncTask(String urlStr, String authorization, Map<String, String> param, Map<String, File> files, Handler handler, int index) {
        this.urlStr = urlStr;
        this.authorization = authorization;
        this.param = param;
        this.handler = handler;
        this.index = index;
        this.files = files;
    }

    @Override
    protected String doInBackground(String... params) {
        URL url = null;//请求的URL地址
        HttpURLConnection conn = null;
        String result = "";
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(60 * 1000); // 缓存的最长时间
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST");
            //根据需求修改为自己的请求头
            conn.setRequestProperty("Authorization", "Bearer " + authorization);
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

            // 首先组拼文本类型的参数
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : param.entrySet()) {
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);
                sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                sb.append(LINEND);
                sb.append(entry.getValue());
                sb.append(LINEND);
            }


            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(sb.toString().getBytes());

            // 发送文件数据
            if (files != null)
                for (Map.Entry<String, File> file : files.entrySet()) {
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(PREFIX);
                    sb1.append(BOUNDARY);
                    sb1.append(LINEND);
                    sb1.append("Content-Disposition: form-data; name=\"zp\"; filename=\""
                            + file.getValue().getName() + "\"" + LINEND);
                    sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                    sb1.append(LINEND);
                    outStream.write(sb1.toString().getBytes());

                    InputStream is = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    is.close();
                    outStream.write(LINEND.getBytes());
                }

            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();
            // 得到响应码
            int res = conn.getResponseCode();
            InputStream in = conn.getInputStream();
            StringBuilder sb2 = new StringBuilder();
            if (res == 200) {
                int ch;
                while ((ch = in.read()) != -1) {
                    sb2.append((char) ch);
                }
            }
            outStream.close();
            conn.disconnect();
            return sb2.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //最后将conn断开连接
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Message msg = handler.obtainMessage();
        if(s!=null){
            msg.what = index;
            msg.obj = s;
        }else{
            msg.what = 0;
        }
        handler.sendMessage(msg);
    }
}

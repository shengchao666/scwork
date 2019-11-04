package hsdx.com.wjcxapp.Http;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class HttpThread extends Thread {
    //http://bjhsdx.com:9000/sggl
    //http://192.168.0.121:7001/sggl/
    //http://127.0.0.1:7001/ywpt/
    //http://192.168.1.100:7001/ywpt/
    private final String BASEURL = "http://192.168.1.100:7001/ywpt/";
    public final String ip_link = BASEURL;


    public String urlMethod(String url, String para) {
        String result = "";
        try {
            String url_R = ip_link + url + para;

            Log.e("url_R", url_R);
            URL U = new URL(url_R);
            HttpURLConnection connection = (HttpURLConnection) U.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
//                result += line;
            }
            result = stringBuilder.toString();
            in.close();
        } catch (Exception e) {
            System.out.println("接口连接异常！" + e);
            result = "no";
        }
        return result;
    }

    /**
     * 用于发送图片,超长字符串等
     *
     * @return res
     */
    public String post(String url, String data) {

        String result;
        try {
            String url_R = BASEURL + url;
            URL U = new URL(url_R);
            HttpURLConnection connection = (HttpURLConnection) U.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);               //使用Post方式不能使用缓存

            //设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            //设置请求体的长度
            connection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());

            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
            }
            result = stringBuilder.toString();
            in.close();
        } catch (Exception e) {
            System.out.println("接口连接异常！" + e);
            result = "no";
        }
        return result;
    }

    /**
     * 获取http请求结果（JsonObject）
     *
     * @param url    url
     * @param params params
     * @return JSONObject
     */
    public JSONObject getJsonObject(String url, String... params) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(urlMethod(url, parseParam(params)));
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject = null;
        }
        return jsonObject;
    }

    /**
     * 格式化参数
     * 使用方式 String para = parseParam("id=#001","name=ZhangSan","sex=1" ... );
     *
     * @return param
     */
    public String parseParam(String... params) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?");
        for (String param : params) {
            String[] pa = param.split("=");
            if (pa.length > 1) {
                param = pa[0] + "=" + encodeUrl(pa[1]);
            }
            stringBuilder.append(param);
            stringBuilder.append("&");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public static String encodeUrl(String url) {
        String res;
        try {
            res = URLEncoder.encode(url, "UTF-8");
        } catch (Exception e) {
            return null;
        }
        return res;
    }

}

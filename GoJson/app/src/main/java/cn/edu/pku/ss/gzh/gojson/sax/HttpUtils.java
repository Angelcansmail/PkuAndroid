package cn.edu.pku.ss.gzh.gojson.sax;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//工具类：通过URLHttpConnection获取服务器上的XML流
public class HttpUtils {

    public HttpUtils() {
    }
    
    //方法：返回的InputStream对象就是服务器返回的XML流。
    public static InputStream getXML(String path) {//参数path：之后将在MainActivity中指定具体的url链接
        try {
            URL url=new URL(path);
            if(url!=null)
            {
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                int requesetCode=connection.getResponseCode();
                if(requesetCode==200)
                {
                    //如果执行成功，返回HTTP响应流
                    return connection.getInputStream();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }        
        return null;
    }
}
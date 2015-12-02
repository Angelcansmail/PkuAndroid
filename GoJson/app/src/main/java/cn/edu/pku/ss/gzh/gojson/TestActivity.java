package cn.edu.pku.ss.gzh.gojson;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Angel on 2015/11/25.
 */
public class TestActivity extends Activity {
    public String readJSONFeed(String URL) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } else {
                Log.e("JSON", "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return readJSONFeed(urls[0]);
        }

        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                Log.i("JSON", "Number of surveys in feed: " +
                        jsonArray.length());

                //---print out the content of the json feed---
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
    				/*
    				Toast.makeText(getBaseContext(), jsonObject.getString("appeId") +
    						" - " + jsonObject.getString("inputTime"),
    						Toast.LENGTH_SHORT).show();
    				*/

                    Toast.makeText(getBaseContext(), jsonObject.getString("text") +
                                    " - " + jsonObject.getString("created_at"),
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        try{
            JSONObject person = new JSONObject();
            JSONArray phone = new JSONArray();
            phone.put("12345678").put("87654321");
            person.put("phone", phone);
            person.put("name", "zhangsan");
            person.put("age", 20);
            JSONObject address = new JSONObject();
            address.put("country","china");
            address.put("province","beiijing");
            person.put("address",address);
            person.put("married",false);
            Toast.makeText(this,"JSONObject:" + person,Toast.LENGTH_LONG).show();
        }catch (JSONException e){
            e.printStackTrace();
        }
		/*
		new ReadJSONFeedTask().execute("http://extjs.org.cn/extjs/examples/grid/survey.html");
		*/
       //new ReadJSONFeedTask().execute("http://apistore.baidu.com/microservice/weather?citypinyin=beijing");
    }
}

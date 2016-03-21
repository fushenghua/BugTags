package com.rtmap.bugtags;

import android.content.Context;
import android.text.TextUtils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by silver on 16-3-17.
 */
public class CrashHttp {

    static String CRASH_SERVER = "http://139.129.46.115:80/crash/saveCrash";


    public static boolean uploadCrash(Map<String, String> paramsMap, Context context) {
        List<NameValuePair> paramPairs = new ArrayList<NameValuePair>();
        if (paramsMap != null && !paramsMap.isEmpty()) {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                paramPairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        try {
            HttpPost post = new HttpPost(CRASH_SERVER);
            UrlEncodedFormEntity entitydata = new UrlEncodedFormEntity(paramPairs, "UTF-8");
            post.setEntity(entitydata);

            DefaultHttpClient client = new DefaultHttpClient();
            // 请求超时
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
            // 读取超时
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                if (!TextUtils.isEmpty(paramsMap.get("cid"))) {
                    DBManager dbManager = new DBManager(context);
                    dbManager.delete(paramsMap.get("cid"));
                    dbManager.closeDB();

                }
                return true;
            }

        } catch (Exception e) {
//            e.printStackTrace();
            if (TextUtils.isEmpty(paramsMap.get("cid"))) {
                DBManager dbManager = new DBManager(context);
                dbManager.add(paramsMap);
                dbManager.closeDB();
            }
        }
        return false;
    }
}


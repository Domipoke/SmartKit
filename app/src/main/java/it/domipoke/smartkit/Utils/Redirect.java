package it.domipoke.smartkit.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import it.domipoke.smartkit.MainActivity;
import it.domipoke.smartkit.TikTokViewer;

public class Redirect implements Runnable {
    public URL url;
    public TikTokUrl ttk;
    public Handler h;
    public Redirect(TikTokUrl self, URL u, Handler mh) {
        url = u;
        ttk = self;
        h = mh;
    }

    @Override
    public void run() {
        try {
            if (ttk.type==TikTokUrl.TYPE_VM) {
                URLConnection uc = url.openConnection();
                uc.connect();
                System.out.println(uc.getHeaderField("Location"));
                while (uc.getURL().toString().equalsIgnoreCase(url.toString())) {
                    //TODO
                }
                System.out.println(uc.getURL());
                ttk.setup(uc.getURL().toString());
            }
            if (ttk.type==TikTokUrl.TYPE_TT) {
                try {
                    URL u = new URL("https://www.tiktok.com/oembed"+"?url="+"https://www.tiktok.com/@"+ttk.username+"/video/"+ttk.video_id);
                    HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
                    urlConnection.setRequestMethod("GET");
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String inputLine;
                    StringBuffer content = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();
                    urlConnection.disconnect();
                    System.out.println("CONTENT -> "+content);
                    JSONObject obj = new JSONObject(content.toString());
                    String html = obj.get("html").toString();
                    System.out.println("START DT -> "+ html);
                    System.out.println("START DT -> "+ html.length());
                    ttk.html=html;
                    Message m = new Message();
                    Bundle b = new Bundle();
                    b.putString("url",url.toString());
                    b.putString("nurl",ttk.url);
                    b.putString("html",html);
                    b.putInt("type",ttk.type);
                    m.setData(b);
                    h.sendMessage(m);
                } catch (MalformedURLException e) {
                    System.out.println("ERROR 0");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("ERROR 1");
                    e.printStackTrace();
                } catch (JSONException e) {
                    System.out.println("ERROR 2");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public TikTokUrl GetTTK() {
        return ttk;
    }
}

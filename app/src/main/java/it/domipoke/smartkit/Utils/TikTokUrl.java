package it.domipoke.smartkit.Utils;

import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class TikTokUrl {
    public String url;
    public String username;
    public String video_id;
    public int type;
    public final int TYPE_VM = 0;
    public final int TYPE_TT = 1;
    public final Pattern vm = Pattern.compile("https?://vm[.]tiktok[.]com/[A-z0-9]*/?",Pattern.CASE_INSENSITIVE);
    public final Pattern tt = Pattern.compile("https?://www[.]tiktok[.]com/@[A-z0-9]*/video/[0-9]*/?",Pattern.CASE_INSENSITIVE);
    public final String UserAgent = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; it-IT; rv:1.9.2.2) Gecko/29199316 Firefox/3.6.2";

    public TikTokUrl(String u) {
        System.out.println("START TikTokUrl");
        type=-1;
        url=u;
        setup(u);
    }

    private void setup(String u) {
        System.out.println("START setup");
        System.out.println(u);
        System.out.println(vm.matcher(u).find());
        System.out.println(tt.matcher(u).find());
        Matcher mvm = vm.matcher(u);
        Matcher mtt = tt.matcher(u);
        if (
                mvm.find()
        ) {
            type=TYPE_VM;
            url=mvm.group();
        } else if (
                mtt.find()
        ) {
            type=TYPE_TT;
            url=mtt.group();
        }
    }

    public boolean isValidUrl() {
        System.out.println("START isValidUrl");
        if (
                type>=0
        ) {
            return true;
        }
        return false;
    }

    public void toTT() {
        System.out.println("START toTT");
        new Thread(new Runnable() {
            @Override
            public void run(URL u) {
                try {
                    URLConnection uc = u.openConnection();
                    uc.connect();
                    String s = uc.getHeaderField("Location");
                    System.out.println("OUT -> "+s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void setWebView(WebView w) {
        System.out.println("START setWebView");
        w.getSettings().setJavaScriptEnabled(true);
        String dt = GenerateUrl();
        System.out.println("START DT -> "+dt);
        System.out.println("START DT -> "+dt.length());
        if (dt.length()>0) {
            w.loadData(dt,"text/html","base64");
        } else {
            //w.loadUrl(url);
        }
    }

    public String GenerateUrl() {
        System.out.println("START GenerateUrl");
        System.out.println(type);
        if (type==TYPE_TT) {
            try {
                URL u = new URL("https://www.tiktok.com/oembed"+"?url="+"https://www.tiktok.com/@"+username+"/video/"+video_id);
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
                return html;
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
        return "";
    }
    public void test1(HttpURLConnection urlConnection) {
        try {
            int r = urlConnection.getResponseCode();
            System.out.println(r);
            String temp = urlConnection.getHeaderField("Location");
            System.out.println(temp);
            setup(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
            if (type==TYPE_TT) {
                Matcher slicevid = Pattern.compile("https://www[.]tiktok[.]com/@[A-z0-9]*/video/",Pattern.CASE_INSENSITIVE).matcher(url);
                if (slicevid.find()) {
                    video_id=url.substring(slicevid.end());
                }
                Matcher sliceuss = Pattern.compile("https://www[.]tiktok[.]com/@",Pattern.CASE_INSENSITIVE).matcher(url);
                Matcher sliceuse = Pattern.compile("https://www[.]tiktok[.]com/@[A-z0-9]*",Pattern.CASE_INSENSITIVE).matcher(url);
                if (sliceuss.find()&&sliceuse.find()) {
                    username=url.substring(sliceuss.end(),sliceuse.end());
                }
                System.out.println("VIDEO AND USERNAME");
                System.out.println(video_id);
                System.out.println(username);
            }
        }
    }
}

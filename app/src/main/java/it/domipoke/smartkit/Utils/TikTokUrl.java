package it.domipoke.smartkit.Utils;

import android.os.Handler;
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
    public String html;
    public String url;
    public String username;
    public String video_id;
    public int type;
    public static final int TYPE_VM = 0;
    public static final int TYPE_TT = 1;
    public static final Pattern vm = Pattern.compile("https?://vm[.]tiktok[.]com/[A-z0-9]*/?",Pattern.CASE_INSENSITIVE);
    public static final Pattern tt = Pattern.compile("https?://www[.]tiktok[.]com/@[A-z0-9]*/video/[0-9]*/?",Pattern.CASE_INSENSITIVE);
    public final String UserAgent = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; it-IT; rv:1.9.2.2) Gecko/29199316 Firefox/3.6.2";

    public TikTokUrl(String u) {
        System.out.println("START TikTokUrl");
        type=-1;
        url=u;
        setup(u);
    }

    public void setup(String u) {
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

    public boolean isValidUrl() {
        System.out.println("START isValidUrl");
        if (
                type>=0
        ) {
            return true;
        }
        return false;
    }
    public static boolean isValidUrl(String url) {
        System.out.println("START isValidUrl");
        Matcher mvm = TikTokUrl.vm.matcher(url);
        Matcher mtt = TikTokUrl.tt.matcher(url);
        if (
            mvm.find()||mtt.find()
        ) {
            return true;
        }
        return false;
    }
    public void setWebView(Handler mh) {
        System.out.println("START toTT");
        try {
            Thread thread = new Thread(new Redirect(this, new URL(url), mh));
            thread.start();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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

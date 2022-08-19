package it.domipoke.smartkit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Xml;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

import org.intellij.lang.annotations.RegExp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.regex.Pattern;

import it.domipoke.smartkit.Utils.TikTokUrl;

public class TikTokViewer extends AppCompatActivity {

    WebView ttkwebview;
    Context ctx;
    Handler mh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx=this;
        setContentView(R.layout.activity_tik_tok_viewer);
        ttkwebview=findViewById(R.id.ttkwebview);
        ttkwebview.getSettings().setJavaScriptEnabled(true);
        ttkwebview.getSettings().setDomStorageEnabled(true);

        mh = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Bundle bb = msg.getData();
                String url = bb.getString("url");
                String nurl = bb.getString("nurl");
                String html = bb.getString("html");
                int type = bb.getInt("type");
                if (type==TikTokUrl.TYPE_TT) {
                    ttkwebview.loadData(Base64.getEncoder().encodeToString(html.getBytes()),"text/html", "base64");
                } else {
                    ttkwebview.loadUrl(url);
                }
            }
        };
        Bundle b = getIntent().getExtras();
        String url = (String) b.get("url");
        TTKloadUrl(url);
        findViewById(R.id.urlinput).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder bu = new AlertDialog.Builder(ctx);
                EditText e = new EditText(bu.getContext());
                String def = "";
                ClipboardManager cb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (cb.hasPrimaryClip()) {
                    ClipData pc = cb.getPrimaryClip();
                    for (int c = 0; c<pc.getItemCount();c++) {
                        ClipData.Item ia = pc.getItemAt(c);
                        if (TikTokUrl.isValidUrl(ia.getText().toString())) {
                            def = ia.getText().toString();
                            break;
                        }
                    }
                }
                e.setText(def);
                bu.setView(e);
                bu.setPositiveButton("OK",(dialogInterface, i) -> {
                    if (e.getText().toString().length()>0) {
                        TTKloadUrl(e.getText().toString());
                    }
                });
                bu.create().show();
            }
        });
    }

    private void TTKloadUrl(String url) {
        TikTokUrl nurl = new TikTokUrl(url);
        if (nurl.isValidUrl()) {
            nurl.setWebView(mh);
        }
    }
}
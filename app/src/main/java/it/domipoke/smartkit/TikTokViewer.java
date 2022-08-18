package it.domipoke.smartkit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

import org.intellij.lang.annotations.RegExp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import it.domipoke.smartkit.Utils.TikTokUrl;

public class TikTokViewer extends AppCompatActivity {

    WebView ttkwebview;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx=this;
        setContentView(R.layout.activity_tik_tok_viewer);
        ttkwebview=findViewById(R.id.ttkwebview);
        Bundle b = getIntent().getExtras();
        String url = (String) b.get("url");
        TTKloadUrl(url);
        findViewById(R.id.urlinput).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder bu = new AlertDialog.Builder(ctx);
                EditText e = new EditText(bu.getContext());
                e.setText("https://vm.tiktok.com/ZMNt4AN4k/");
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
            nurl.toTT();
        }
        nurl.setWebView(ttkwebview);
    }
}
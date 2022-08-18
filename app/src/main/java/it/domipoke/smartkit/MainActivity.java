package it.domipoke.smartkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    TableLayout tbl;
    Context ctx;
    Map<String, Drawable> functions;
    int MAX_CH=4;

    private Map<String, Drawable> GetAllFunctions() {
        Map<String, Drawable> res = new HashMap();
        res.put("TikTokViewer", ctx.getDrawable(R.drawable.ic_launcher_background));
        return res;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchDeepLinks();
        ctx=this;
        functions=GetAllFunctions();
        tbl=findViewById(R.id.tbl);
        List<String> ks = functions.keySet().stream().sorted().collect(Collectors.toList());
        ArrayList<String> cells = getAllCells();
        for (String k : ks) {
            if (!cells.contains(k)) pushActionButton(k,functions.get(k));
        }
    }

    private ArrayList<String> getAllCells() {
        ArrayList<String> res = new ArrayList<String>();
        for (int q = 0; q<tbl.getChildCount();q++) {
            TableRow tr = (TableRow) tbl.getChildAt(q);
            for (int w = 0; w<tr.getChildCount();w++) {
                LinearLayout ll = (LinearLayout) tr.getChildAt(w);
                if (ll.getChildCount()>1) {
                    TextView tv = (TextView) ll.getChildAt(1);
                    res.add(tv.getText().toString());
                }
            }
        }
        return res;
    }

    private void fetchDeepLinks() {
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri!=null) {
            String url = uri.toString();
            if (url.startsWith("https://vm.tiktok.com/")) {
                Intent i = new Intent(ctx, TikTokViewer.class);
                i.putExtra("url", url);
                startActivity(i);
            }
        }
    }

    public TableRow getLastTableRow() {
        int childs = tbl.getChildCount();
        if (childs>0) {
            TableRow tr = (TableRow) tbl.getChildAt(childs-1);
            int trc = tr.getChildCount();
            if (trc<MAX_CH) {
                return tr;
            } else {
                TableRow trn = new TableRow(ctx);
                tbl.addView(trn);
                return trn;
            }
        } else {
            TableRow tr = new TableRow(ctx);
            tbl.addView(tr);
            return getLastTableRow();
        }
    }
    public void pushActionButton(String name, Drawable image) {

        ImageButton bt = new ImageButton(ctx);
        bt.setMaxHeight(32);
        bt.setMaxWidth(32);
        bt.setMinimumHeight(32);
        bt.setMinimumWidth(32);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, getActivityFromString(name));
                i.putExtra("url","");
                startActivity(i);
            }
        });
        bt.setImageDrawable(image);
        TextView lb = new TextView(ctx);
        lb.setText(name);
        lb.setTextColor(Color.WHITE);
        lb.setTextSize(8.0F);
        LinearLayout ll = new LinearLayout(ctx);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(bt);
        ll.addView(lb);
        ll.setGravity(Gravity.CENTER);
        getLastTableRow().addView(ll);
    }

    private Class getActivityFromString(String n) {
        switch (n.toLowerCase(Locale.ROOT)) {
            case "tiktokviewer":
                return TikTokViewer.class;
            default:
                return MainActivity.class;
        }
    }
}
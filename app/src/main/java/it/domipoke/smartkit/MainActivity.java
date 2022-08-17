package it.domipoke.smartkit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TableLayout;

public class MainActivity extends AppCompatActivity {
    TableLayout tbl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tbl=findViewById(R.id.tbl);
    }

    public void x() {
        int childs = tbl.getChildCount();
        if (childs==0) {

        }
    }
}
package com.focus.callblock;


import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

public class CallBlockActivity extends TabActivity {
    
	Button serv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startService(new Intent(this, CallService.class));
        Resources res = getResources();
        TabHost tabhost = getTabHost();
        TabHost.TabSpec spec;
        
        spec = tabhost.newTabSpec("filters");
        spec.setContent(new Intent().setClass(this, FiltersActivity.class));
        spec.setIndicator("Filters");
        tabhost.addTab(spec);
        
        spec = tabhost.newTabSpec("Logs");
        spec.setContent(new Intent().setClass(this, LogsActivity.class));
        spec.setIndicator("Logs");
        tabhost.addTab(spec);
        
        DBHandle db = new DBHandle(this);
        db.deleteAll();
        Filter fil = new Filter("*123*", "1234567", 23*60, 2*60);
        fil.write(this);
    }
}
package com.focus.callblock;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class FiltersActivity extends Activity{

	private Button serv;
	private ListView lv;
	private FiltersAdapter adapter;
	ArrayList<Filter> filters;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filters);
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        serv = (Button) findViewById(R.id.service_switch);
        if(pref.getBoolean("service", false))
        	serv.setText("Stop");
        else
        	serv.setText("Start");
        
        lv = (ListView) findViewById(R.id.filterslist);
        DBHandle db = new DBHandle(this);
        filters = db.getAllFilters();
        adapter = new FiltersAdapter(this, filters);
        lv.setAdapter(adapter);
        
        lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){
        	public void onCreateContextMenu(ContextMenu cm, View v, ContextMenuInfo cmi) {
				String[] menuitems = new String[]{"Edit", "Remove"};
			    for (int i = 0; i<menuitems.length; i++) {
			      cm.add(cm.NONE, i, i, menuitems[i]);
			    }
			}	        	
        });
        
        lv.setOnItemClickListener(
	  	          new OnItemClickListener(){
	  		        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	  		    			Filter filter = (Filter) lv.getItemAtPosition(position);
	  		    			Intent intent = new Intent(v.getContext(), FilterEditor.class);
	  		    			intent.putExtra("numbers", filter.numbers);
	  		    			intent.putExtra("days", filter.days);
	  		    			intent.putExtra("start", filter.start);
	  		    			intent.putExtra("stop", filter.stop);
	  		    			startActivity(intent);
	  		        	}
	  		        });
        
	}
	
	@Override
 	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo minfo = (AdapterContextMenuInfo) item.getMenuInfo();
 		switch(item.getItemId()){
 		case 0:
 			Filter filter = (Filter) adapter.getItem(minfo.position);
 			Intent intent = new Intent(this, FilterEditor.class);
			intent.putExtra("numbers", filter.numbers);
			intent.putExtra("days", filter.days);
			intent.putExtra("start", filter.start);
			intent.putExtra("stop", filter.stop);
			startActivity(intent);
			break;
 		case 1:
 			Filter fil = (Filter) adapter.getItem(minfo.position);
 			fil.delete(this);
 			DBHandle db = new DBHandle(this);
 			filters = db.getAllFilters();
 	        adapter = new FiltersAdapter(this, filters);
 	        lv.setAdapter(adapter);
 	        lv.invalidateViews();
 			break;
 		}
		return super.onContextItemSelected(item);
	}
	
    public void switchService(View button){
    	SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
    	if(pref.getBoolean("service", false)){
        	stopService(new Intent(this, CallService.class));
        	serv.setText("Start");
    	}
        else{
        	startService(new Intent(this, CallService.class));
        	serv.setText("Stop");
        }
    }
    
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.filters_menu, menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.add:
			Intent intent = new Intent(this, FilterEditor.class);
			intent.putExtra("numbers", "");
			intent.putExtra("days", "1234567");
			intent.putExtra("start", 0);
			intent.putExtra("stop", 0);
			startActivity(intent);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	@Override
	public void onResume(){
		super.onResume();
		DBHandle db = new DBHandle(this);
		filters = db.getAllFilters();
        adapter = new FiltersAdapter(this, filters);
        lv.setAdapter(adapter);
        lv.invalidateViews();
	}
	
}

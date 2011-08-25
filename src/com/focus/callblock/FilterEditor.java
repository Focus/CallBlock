package com.focus.callblock;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.CheckBox;

public class FilterEditor extends Activity{
	
	Filter oldfilter;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getIntent().getExtras();
		oldfilter = new Filter(bundle.getString("numbers"), bundle.getString("days"), bundle.getInt("start"), bundle.getInt("stop"));
		setContentView(R.layout.filter_editor);
		setForm(oldfilter);
	}
	
	public void buttonPress(View v){
		if(v.getId() == R.id.filter_save){
			Filter filter = new Filter();
			
			RadioGroup conditions = (RadioGroup) findViewById(R.id.radio_group);
			EditText number = (EditText) findViewById(R.id.number);
			switch(conditions.getCheckedRadioButtonId()){
			case R.id.radio_all:
				filter.numbers="";
				break;
			case R.id.radio_begin:
				filter.numbers = "*" + number.getText().toString();
				break;
			case R.id.radio_contains:
				filter.numbers = "*" + number.getText().toString() + "*";
				break;
			case R.id.radio_end:
				filter.numbers = number.getText().toString() + "*";
				break;
			case R.id.radio_exact:
				filter.numbers = number.getText().toString();
				break;
			}
			TimePicker tpstart = (TimePicker) findViewById(R.id.start);
			filter.start = tpstart.getCurrentHour()*60 + tpstart.getCurrentMinute();
			TimePicker tpstop = (TimePicker) findViewById(R.id.stop);
			filter.stop = tpstop.getCurrentHour()*60 + tpstop.getCurrentMinute();
			
			int numToId[] = new int[]{R.id.check_sun, R.id.check_mon, R.id.check_tue, R.id.check_wed, R.id.check_thu, R.id.check_fri, R.id.check_sat};
			filter.days = "";
			for(int i = 1; i < 8; i++){
				CheckBox ch = (CheckBox) findViewById(numToId[i-1]);
				if(ch.isChecked())
					filter.days += i;
			}
			filter.write(this);
			oldfilter.delete(this);
			this.finish();
		}
		else if(v.getId() == R.id.filter_discard){
			this.finish();
		}
	}
	
	public void setForm(Filter filter){
		//Set the radio buttons
		RadioGroup conditions = (RadioGroup) findViewById(R.id.radio_group);
		EditText number = (EditText) findViewById(R.id.number);
		if(filter.numbers.length() <= 0)
			conditions.check(R.id.radio_all);
		else if( filter.numbers.startsWith("*") && filter.numbers.endsWith("*") ){
			conditions.check(R.id.radio_contains);
			number.setText(filter.numbers.substring(1, filter.numbers.length()-1));
		}
		else if( filter.numbers.startsWith("*") ){
			conditions.check(R.id.radio_begin);
			number.setText(filter.numbers.substring(1));
		}
		else if( filter.numbers.endsWith("*") ){
			conditions.check(R.id.radio_end);
			number.setText(filter.numbers.substring(0, filter.numbers.length()-1));
		}
		else{
			conditions.check(R.id.radio_exact);
			number.setText(filter.numbers);
		}
		
		
		//Set the start and stop dates (keep in mind that we store them as minutes)
		TimePicker tpstart = (TimePicker) findViewById(R.id.start);
		tpstart.setCurrentHour((int)filter.start/60);
		tpstart.setCurrentMinute(filter.start % 60);
		tpstart.setIs24HourView(true);
		TimePicker tpstop = (TimePicker) findViewById(R.id.stop);
		tpstop.setCurrentHour((int)filter.stop/60);
		tpstop.setCurrentMinute(filter.stop % 60);
		tpstop.setIs24HourView(true);
		
		//Check the checkboxes for the days of the week
		int numToId[] = new int[]{R.id.check_sun, R.id.check_mon, R.id.check_tue, R.id.check_wed, R.id.check_thu, R.id.check_fri, R.id.check_sat};
		for(int i = 1; i < 8; i++){
			if(filter.days.indexOf(""+i) != -1){
				CheckBox ch = (CheckBox) findViewById(numToId[i-1]);
				ch.setChecked(true);
			}
		}
	}
}

package com.focus.callblock;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FiltersAdapter extends BaseAdapter{

	private ArrayList<Filter> items;
	private Context context;
	private LayoutInflater inflater;
	
	public FiltersAdapter(Context context, ArrayList<Filter> items){
		super();
		this.context = context;
		this.items = items;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount() {
		return items.size();
	}

	public Object getItem(int arg0) {
		return items.get(arg0);
	}


	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.filters_element, null, false);
		TextView number = (TextView) view.findViewById(R.id.number);
		TextView conditions = (TextView) view.findViewById(R.id.conditions);
		
		Filter fil = (Filter) items.get(position);
		
		number.setText(fil.getNumber());
		conditions.setText(fil.getConditions());
		return view;
	}

}

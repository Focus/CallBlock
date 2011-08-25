package com.focus.callblock;

import android.content.Context;

public class Filter {
	
	public String numbers;
	public int start;
	public int stop;
	public String days;
	
	public Filter(){
		numbers = "";
		start = 0;
		stop = 60*24;
		days = "1234567";
	}
	public Filter(String nos){
		numbers = nos;
		start = 0;
		stop = 60*24;
		days = "1234567";
	}
	public Filter(String inumbers, String idays, int istart, int istop){
		numbers = inumbers;
		start = istart;
		stop = istop;
		days = idays;
	}
	
	public boolean matchWithNumber(String in){
		if(numbers.length() <= 0)
			return false;
		if( numbers.startsWith("*") && numbers.endsWith("*") ){
			if(in.indexOf( numbers.substring(1, numbers.length()-1) ) == -1)
				return false;
			else
				return true;
		}
		else if( numbers.startsWith("*") ){
			if(in.indexOf( numbers.substring(1) ) == -1)
				return false;
			else
				return true;
		}
		else if( numbers.endsWith("*") ){
			if(in.indexOf( numbers.substring(0, numbers.length()-1) ) == -1)
				return false;
			else
				return true;
		}
		return in.equals(numbers);
	}
	
	public void write(Context context){
		DBHandle db = new DBHandle(context);
		db.insert(numbers, days, start, stop);
	}
	
	public void delete(Context context){
		DBHandle db = new DBHandle(context);
		db.delete(numbers, days, start, stop);
	}
	
	public String getNumber(){
		if(numbers.length() <= 0)
			return "";
		if( numbers.startsWith("*") && numbers.endsWith("*") )
			return "Containing "+numbers.substring(1, numbers.length()-1);
		else if( numbers.startsWith("*") )
			return "Starts with "+numbers.substring(1); 
		else if( numbers.endsWith("*") )
			return "Ends with "+numbers.substring(0, numbers.length()-1);
		return numbers;
	}
	
	public String getDow(){
		String numToDay[] = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
		String ret = "";
		for(int i = 1; i < 8; i++){
			if(days.indexOf(""+i) != -1)
				ret += numToDay[i-1] + ", ";
		}
		if(ret.length() > 2)
			ret = ret.substring(0, ret.length() - 2);
		
		return ret;
	}
	
	public String getConditions(){
		String ret;
		if(start != 0 || stop != 24*60)
			ret = "Between " + (int) start/60 + ":" + ((""+start % 60).length()==1 ? "0" : "") +
			(start % 60) + " and " +(int) stop/60 + ":" + ((""+stop % 60).length()==1 ? "0" : "")+
			(stop % 60) + "\n";
		else
			ret = "Allday\n";
		
		ret += this.getDow();
		
		return ret;
	}
}

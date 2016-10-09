package com.gystudio.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

/**
 * 
 * 
 * 类名称：ExtDate
 * 类描述：
 * 创建人：yuanbf
 * 创建时间：2012-7-12 下午10:16:03
 * @version 
 *
 */
public final class ExtDate implements Comparable<ExtDate> {
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    
  //  private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss SSS";
    
   // private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss SSS";
	private static String [][] QStrs={{"0101","0331"},{"0401","0630"},{"0701","0930"},{"1001","1231"}};
    
    
	private Date date;
	
	private Calendar calendar =  Calendar.getInstance();  
	

	public ExtDate(){
		super();
		this.date = new Date();
	}
	
	
	public ExtDate(Date date) {
		super();
		this.date = date;
		calendar.setTime(this.date);
	}
	public ExtDate(String datestring,String partten) {
		super();
		try {
			this.date=DateUtils.parseDate(datestring, new String[]{partten});
			calendar.setTime(this.date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("日期格式错误");
		}
	}


	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public ExtDate addMonth(){
		return this.addMonth(1);
	}
	public ExtDate addYear(){
		
		return this.addYear(1);
	}
	public ExtDate addDay(){
		
		return this.addDay(1);
	}
	
	public ExtDate addHour(){
		return this.addDay(1);
	}
	
	public ExtDate addSeconds(){
		return this.addSeconds(1);
	}
	
	public ExtDate addMonth(int month){
		calendar.add(Calendar.MONTH,month);  
		this.date=calendar.getTime();
		return this;
	}
	public ExtDate addYear(int year){  
		calendar.add(Calendar.YEAR,year);  
		this.date=calendar.getTime();
		return this;
	}
	public ExtDate addDay(int day){ 
		calendar.add(Calendar.DATE,day);  
		this.date=calendar.getTime();
		return this;
	}
	
	public ExtDate addHour(int hour){
		calendar.add(Calendar.HOUR,hour); 
		this.date=calendar.getTime();
		return this;
	}
	
	public ExtDate addSeconds(int sec){
		calendar.add(Calendar.SECOND,sec); 
		this.date=calendar.getTime();
		return this;
	}
	
	public ExtDate getThisWeek(int week){
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.date);
		 int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;
		 cal.add(Calendar.DATE, -day_of_week);
		 cal.add(Calendar.DATE, (week-1));
		 return new ExtDate(cal.getTime());
	}
	
	public int getThisQuarter(){
		return (calendar.get(calendar.MONTH)-1)/3+1;
	}
	public ExtDate getThisQuarterBegin(){
		return new ExtDate(this.format("yyyy")+ExtDate.QStrs[this.getThisQuarter()-1][0],"yyyyMMdd");
	}
	public ExtDate getThisQuarterEnd(){
		return new ExtDate(this.format("yyyy")+ExtDate.QStrs[this.getThisQuarter()-1][1],"yyyyMMdd");
	}
	public ExtDate getThisHalfYearBegin(){
		if(this.getThisQuarter()==1 ||this.getThisQuarter()==2){
			return new ExtDate(this.format("yyyy")+ExtDate.QStrs[0][0],"yyyyMMdd");
		}else{
			return new ExtDate(this.format("yyyy")+ExtDate.QStrs[2][0],"yyyyMMdd");
		}
	}
	public ExtDate getThisHalfYearEnd(){
		if(this.getThisQuarter()==1 ||this.getThisQuarter()==2){
			return new ExtDate(this.format("yyyy")+ExtDate.QStrs[1][1],"yyyyMMdd");
		}else{
			return new ExtDate(this.format("yyyy")+ExtDate.QStrs[3][1],"yyyyMMdd");
		}
	}
	public String format(){
		return this.format(null);
	}
	
	public String format(String format){
        if(format == null)
            format = DEFAULT_DATE_FORMAT;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(this.date);
	}
	@Override
	public int compareTo(ExtDate o) {
		// TODO Auto-generated method stub
		return this.date.compareTo(o.date);
	}
	
	public static void main(String[] args) {
		ExtDate c=new ExtDate("1980-01","yyyy-MM");
		for (int i = 0; i <100; i++) {
			c.addYear();
			System.out.println(c.format("yyyyMMdd"));
		}
		  c=new ExtDate(new Date());
		 System.out.println("今天的日期: " + c.format("yyyyMMdd"));
		 System.out.println("本周第一天: " + c.getThisWeek(1).format("yyyyMMdd"));
		 System.out.println("本周末: " + c.getThisWeek(7).format("yyyyMMdd"));
		// c=new ExtDate("2012-12","yyyy-MM");
		 System.out.println("几季度: " + c.getThisQuarter());
		 c=new ExtDate(new Date());
		 System.out.println("季度开始: " + c.getThisQuarterBegin().format("yyyyMMdd"));
		 System.out.println("季度结束: " + c.getThisQuarterEnd().format("yyyyMMdd"));
		 System.out.println("半年开始: " + c.getThisHalfYearBegin().format("yyyyMMdd"));
		 System.out.println("半年结束: " + c.getThisHalfYearEnd().format("yyyyMMdd"));
		    
	}
}

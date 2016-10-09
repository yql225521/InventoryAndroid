/*
 * $RCSfile: DateUtil.java,v $
 * $Revision: 1.1  $
 * $Date: 2006-10-21  $
 */
package com.gystudio.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>Title: DateUtil</p>
 * <p>Description:</p>
 * @author yuanbf
 * @version 1.0
 */
public class DateUtil
{
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    
    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss SSS";
    
    private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss SSS";
    
    private static DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
    
    private static TimeZone tz = TimeZone.getTimeZone("GMT");
    
    public static long TIME_PER_DAY = 24 * 60 * 60 * 1000L;
    
    static
    {
        df.setTimeZone(tz);
    }

    private DateUtil(){}
   
    public static String toGMTString(Date date)
    {
        return df.format(date);
    }

    /**
     * @return
     * @author: yuanbf
     */
    public static String getDate()
    {
        return getDate(new Date());
    }

    /**
     * @param timestamp
     * @return - String
     * @author: yuanbf
     */
    public static String getDate(Timestamp timestamp)
    {
        if(timestamp == null)
        {
            return "";
        }
        
        DateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        
        return df.format(timestamp);
    }

    /**
     * @param timestamp
     * @return - String
     * @author: yuanbf
     */
    public static String getDate(Date date)
    {
        if(date == null)
        {
            return "";
        }
        
        DateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        
        return df.format(date);
    }

    /**
     * @param timestamp
     * @return - String
     * @author: yuanbf
     */
    public static String getDate(java.sql.Date date)
    {
        if(date == null)
        {
            return "";
        }
        
        DateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        
        return df.format(date);
    }

    /**
     * @param timestamp
     * @return - String
     * @author: yuanbf
     */
    public static String getTime(Timestamp timestamp)
    {
        if(timestamp == null)
        {
            return "";
        }
        
        DateFormat df = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
        
        return df.format(timestamp);
    }

    /**
     * @param timestamp
     * @return - String
     * @author: yuanbf
     */
    public static String getTime(Date date)
    {
        if(date == null)
        {
            return "";
        }
        
        DateFormat df = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
        
        return df.format(date);
    }

    /**
     * @param timestamp
     * @return - String
     * @author: yuanbf
     */
    public static String getTime(java.sql.Date date)
    {
        if(date == null)
        {
            return "";
        }
        
        DateFormat df = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
        
        return df.format(date);
    }

    /**
     * @return - java.lang.String
     * 
     */
    public static String getDateTime()
    {
        DateFormat df = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
        
        return df.format(new Date());
    }

    /**
     * @param format
     * @return
     * 
     */
    public static String getDateTime(String format)
    {
        DateFormat df = new SimpleDateFormat(format);
        
        return df.format(new Date());
    }
    
    /**
     * @param date
     * @return
     * @author: yuanbf
     */
    public static String getDateTime(Date date)
    {
        if(date == null)
        {
            return "";
        }
        
        DateFormat df = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
        
        return df.format(date);
    }
    
    /**
     * @param date
     * @return
     * @author: yuanbf
     */
    public static String getDateTime(java.sql.Date date)
    {
        if(date == null)
        {
            return "";
        }
        
        DateFormat df = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
        
        return df.format(date);
    }
    

    /**
     * @param date
     * @param format
     * @return
     * @author: yuanbf
     */
    public static String getDateTime(Date date, String format)
    {
        if(date == null)
        {
            return "";
        }
        
        if(format == null)
        {
            format = DEFAULT_DATETIME_FORMAT;
        }
        
        DateFormat df = new SimpleDateFormat(format);
        
        return df.format(date);
    }
    
    /**
     * @param date
     * @param format
     * @return
     * @author: yuanbf
     */
    public static String getDateTime(java.sql.Date date, String format)
    {
        if(date == null)
        {
            return "";
        }
        
        if(format == null)
        {
            format = DEFAULT_DATETIME_FORMAT;
        }
        
        DateFormat df = new SimpleDateFormat(format);
        
        return df.format(date);
    }

    /**
     * @param time
     * @param format
     * @return - String
     * 
     * @author: yuanbf
     */
    public static String getDateTime(Timestamp time)
    {
        if(time == null)
        {
            return "";
        }
        
        DateFormat df = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
        
        return df.format(time);
    }

    /**
     * @param time
     * @param format
     * @return - String
     * 
     * @author: yuanbf
     */
    public static String getDateTime(Timestamp time, String format)
    {
        if(time == null)
        {
            return "";
        }
        
        DateFormat df = new SimpleDateFormat(format);
        
        return df.format(time);
    }

    /**
     * @param time
     * @param format
     * @param y
     * @return - String
     * 
     * @author: yuanbf
     */
    public static String getDateTime(Timestamp time,String format,String y)
    {
        if(time == null)
        {
            return y;
        }
        
        DateFormat df = new SimpleDateFormat(format);
        
        return df.format(time);
    }
    
    /**
     * @param date
     * @return
     * @author: yuanbf
     */
    public static String toString(Date date)
    {
        return getDateTime(date);
    }
    
    /**
     * @param date
     * @param format
     * @return
     * @author: yuanbf
     */
    public static String toString(Date date, String format)
    {
        return getDateTime(date, format);
    }
    
    /**
     * @param date
     * @return
     * @author: yuanbf
     */
    public static String toString(java.sql.Date date)
    {
        return getDateTime(date);
    }
    
    /**
     * @param date
     * @param format
     * @return
     * @author: yuanbf
     */
    public static String toString(java.sql.Date date, String format)
    {
        return getDateTime(date, format);
    }
    
    
    public static String toString(Timestamp timestamp)
    {
        return getDateTime(timestamp);
    }
    
    /**
     * @param date
     * @param format
     * @return
     * @author: yuanbf
     */
    public static String toString(Timestamp timestamp, String format)
    {
        return getDateTime(timestamp, format);
    }

    /**
     * @param year
     * @param month
     * @param day
     * @return
     * @author: yuanbf
     */
    public static Timestamp getTimestamp(int year, int month, int day)
    {
        Calendar calendar = Calendar.getInstance();
        
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        return new Timestamp(calendar.getTime().getTime());
    }

    /**
     * @Author:Joe
     * @Date: Nov 13, 2007
     * @param strDate
     * @param format
     * @return
     * @throws ParseException
     */
    public static Timestamp getTimeFromString(String strDate, String format) throws ParseException
    {
        if(strDate == null || strDate.trim().equals(""))
            return null;
        
        if(format == null)
            format = DEFAULT_DATE_FORMAT;
        
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        
        Date date = formatter.parse(strDate);
        
        return new Timestamp(date.getTime());
    }

    /**
     * @param year
     * @param month
     * @param day
     * @return
     * @author: yuanbf
     */
    public static Date getDate(int year, int month, int day)
    {
        Calendar calendar = Calendar.getInstance();
        
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        return calendar.getTime();
    }

    /**
     * @param time
     * @return - java.util.Date
     * 
     * @author: yuanbf
     * @throws ParseException
     */
    public static Date parseDateTime(String dateTime) throws ParseException
    {
        DateFormat df = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
        
        return df.parse(dateTime);
    }

    /**
     * @param time
     * @param format
     * @return
     * @throws ParseException -
     *             java.util.Date
     * 
     * @author: yuanbf
     */
    public static Date parseDateTime(String dateTime, String format) throws ParseException
    {
        DateFormat df = new SimpleDateFormat(format);
        
        return df.parse(dateTime);
    }

    /**
     * @return - java.util.Date
     * 
     * @author: yuanbf
     */
    public static Date getTomorrow()
    {
        return new Date(System.currentTimeMillis() + 86400000);
    }

    /**
     * @param format
     * @return - String
     * 
     * @author: yuanbf
     */
    public static String getTomorrow(String format)
    {
        DateFormat df = new SimpleDateFormat(format);
        
        return df.format(new Date(System.currentTimeMillis() + 86400000L));
    }

    /**
     * @param amount
     * @return - String[]
     * @author: yuanbf
     */
    public static Date[] getMonths(int amount)
    {
        Calendar calendar = Calendar.getInstance();
        Date[] dates = new Date[amount];
        
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, 1);
        
        for(int i = 0; i < amount; i++)
        {
            calendar.add(Calendar.MONTH, -1);
            
            dates[i] = calendar.getTime();
        }
        
        return dates;
    }
    
    /**
     * 
     * @param date
     * @return
     * @author: yuanbf
     */
    public static Calendar getCalendar(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        
        calendar.setTimeInMillis(date.getTime());
        
        return calendar;
    }
    
    /**
     * 
     * @param date
     * @return
     * @author: yuanbf
     */
    public static Calendar getCalendar(java.sql.Date date)
    {
        Calendar calendar = Calendar.getInstance();
        
        calendar.setTimeInMillis(date.getTime());
        
        return calendar;
    }
    
    /**
     * @param timestamp
     * @return
     * @author: yuanbf
     */
    public static Calendar getCalendar(Timestamp timestamp)
    {
        Calendar calendar = Calendar.getInstance();
        
        calendar.setTimeInMillis(timestamp.getTime());
        
        return calendar;
    }

    /**
     * @return - int
     * @author: yuanbf
     */
    public static int getYear()
    {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * @return
     * @author: yuanbf
     */
    public static int getMonth()
    {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }
    
    /**
     * @return
     * @author: yuanbf
     */
    public static int getDay()
    {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @return - int
     * @author: yuanbf
     */
    public static int getYear(Timestamp timestamp)
    {
        return getCalendar(timestamp).get(Calendar.YEAR);
    }

    /**
     * @return
     * @author: yuanbf
     */
    public static int getMonth(Timestamp timestamp)
    {
        return getCalendar(timestamp).get(Calendar.MONTH) + 1;
    }
    
    /**
     * @return
     * @author: yuanbf
     */
    public static int getDay(Timestamp timestamp)
    {
        return getCalendar(timestamp).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @return - int
     * @author: yuanbf
     */
    public static int getYear(Date date)
    {
        return getCalendar(date).get(Calendar.YEAR);
    }

    /**
     * @return
     * @author: yuanbf
     */
    public static int getMonth(Date date)
    {
        return getCalendar(date).get(Calendar.MONTH) + 1;
    }
    
    /**
     * @return
     * @author: yuanbf
     */
    public static int getDay(Date date)
    {
        return getCalendar(date).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @return - int
     * @author: yuanbf
     */
    public static int getYear(java.sql.Date date)
    {
        return getCalendar(date).get(Calendar.YEAR);
    }

    /**
     * @return
     * @author: yuanbf
     */
    public static int getMonth(java.sql.Date date)
    {
        return getCalendar(date).get(Calendar.MONTH) + 1;
    }
    
    /**
     * @return
     * @author: yuanbf
     */
    public static int getDay(java.sql.Date date)
    {
        return getCalendar(date).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @param startYear
     * @param endYear
     * @return - int[]
     * @author: yuanbf
     */
    public static int[] getYears(int startYear, int endYear)
    {
        if(startYear < 0 || endYear < 0)
        {
            return new int[0];
        }
        
        if(endYear < startYear)
        {
            startYear = startYear ^ endYear;
            endYear = startYear ^ endYear;
            startYear = startYear ^ endYear;
        }
        
        int[] years = new int[endYear - startYear];
        
        for(int i = 0; i < years.length; i++)
        {
            years[i] = startYear + i;
        }
        
        return years;
    }

    /**
     * @param year
     * @param month
     * @param day
     * @return
     * @author: yuanbf
     */
    public static int getDayOfWeek(int year, int month, int day)
    {
        return getDayOfWeek(getTimestamp(year, month, day));
    }

    /**
     * @param timestamp
     * @return
     * @author: yuanbf
     */
    public static int getDayOfWeek(Timestamp timestamp)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * @param year
     * @param month
     * @return
     * @author: yuanbf
     */
    public static int getDaysOfMonth(int year, int month)
    {
        if(year < 1)
        {
            throw new IllegalArgumentException("the year must be > 0");
        }
        if(month < 1 || month > 12)
        {
            throw new IllegalArgumentException("the year must be >= 1 && <= 12");
        }
        int[] days = new int[]
        {
            31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
        };
        
        // ((y % 4 == 0) && (y % 100 != 0) || (y % 400 == 0))? 29: 28;
        if(month == 2)
        {
            return (((year % 4 == 0) && (year % 100 != 0)) || (year % 400) == 0) ? 29 : 28;
        }
        
        return days[month - 1];
    }
    
    /**
     * @param year
     * @param startDate
     * @param endDate
     * @return
     * @author: yuanbf
     */
    public static int[] getMonths(int year, Timestamp startDate, Timestamp endDate)
    {
        if(startDate == null && endDate == null)
        {
            return null;
        }
        
        if(startDate == null)
        {
            if(year == getYear(endDate))
            {
                return new int[]{getMonth(endDate)};
            }
            else
            {
                return null;
            }
        }
        
        if(endDate == null)
        {
            if(year == getYear(startDate))
            {
                return new int[]{getMonth(startDate)};
            }
            else
            {
                return null;
            }
        }
        
        int s = 1;
        int e = 13;
        
        int sy = getYear(startDate);
        int ey = getYear(endDate);
        
        if(sy < year)
        {
            s = 1;
        }
        
        if(sy > year)
        {
            s = 13;
        }
        
        if(sy == year)
        {
            s = getMonth(startDate);
        }
        
        if(ey < year)
        {
            e = 0;
        }
        
        if(ey > year)
        {
            e = 12;
        }
        
        if(ey == year)
        {
            e = getMonth(endDate);
        }
        
        if(s <= e)
        {
            int[] months = new int[e - s + 1];
            
            for(int i = s; i <= e; i++)
            {
                months[i - s] = i;
            }
            
            return months;
        }
        
        return null;
    }

    /**
     * SUN MON TUE WED THU FRI SAT <br/> 
     * -1 -1 -1 -1 1 2 3<br/> 
     * 4 5 6 7 8 9 10 <br/> 
     * 11 12 13 14 15 16 17 <br/> 
     * 18 19 20 21 22 23 24 <br/> 
     * 25 26 27 28 29 30 -1 <br/>
     * 
     * @param year
     * @param month
     * @return
     * @author: yuanbf
     */
    public static int[] getDaysOfMonthArray1(int year,int month)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int daysOfMonth = getDaysOfMonth(year, month);
        
        int rows = (daysOfMonth + dayOfWeek + (7 - 1)) / 7;
        int[] days = new int[rows * 7];
        
        for(int i = 0; i < dayOfWeek; i++)
        {
            days[i] = -1;
        }
        
        for(int i = 0; i < daysOfMonth; i++)
        {
            days[i + dayOfWeek] = i + 1;
        }
        
        for(int i = daysOfMonth + dayOfWeek; i < days.length; i++)
        {
            days[i] = -1;
        }
        
        return days;
    }

    /**
     * SUN MON TUE WED THU FRI SAT <br/> 
     * -1 -1 -1 -1 1 2 3 <br/> 
     * 4 5 6 7 8 9 10 <br/> 
     * 11 12 13 14 15 16 17 <br/> 
     * 18 19 20 21 22 23 24 <br/> 
     * 25 26 27 28 29 30 -1 <br/>
     * 
     * @param year
     * @param month
     * @return
     * @author: yuanbf
     */
    public static int[][] getDaysOfMonthArray2(int year,int month)
    {
        int[] days = getDaysOfMonthArray1(year,month);
        int[][] array = new int[days.length / 7][7];
        
        for(int i = 0; i < days.length; i++)
        {
            array[i / 7][i % 7] = days[i];
        }
        
        return array;
    }

    /**
     * @param year
     * @return
     * @author: yuanbf
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static List getWeekDaysByYear(int year)
    {
        List list = new ArrayList();
        
        for(int i = 1; i <= 12; i++)
        {
            int[][] days = getDaysOfMonthArray2(year,i);
            
            for(int j = 0; j < days.length; j++)
            {
                if(days[j][0] > 0)
                {
                    list.add(DateUtil.getTimestamp(year,i,days[j][0]));
                }
                if(days[j][6] > 0)
                {
                    list.add(DateUtil.getTimestamp(year,i,days[j][6]));
                }
            }
        }
        
        return list;
    }

    /**
     * @param year
     * @param month
     * @return
     * @author: yuanbf
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static List getWeekDaysByMonth(int year,int month)
    {
        int[][] days = getDaysOfMonthArray2(year,month);
        List list = new ArrayList();
        
        for(int j = 0; j < days.length; j++)
        {
            if(days[j][0] > 0)
            {
                list.add(DateUtil.getTimestamp(year,month,days[j][0]));
            }
            if(days[j][6] > 0)
            {
                list.add(DateUtil.getTimestamp(year,month,days[j][6]));
            }
        }
        
        return list;
    }

    public static void printDays(int[] days)
    {
        System.out.println("SUN  MON  TUE  WED  THU  FRI  SAT");
        
        for(int i = 0; i < days.length; i++)
        {
            if(days[i] < 1)
            {
                System.out.print("      ");
            }
            else
            {
                if(days[i] < 10)
                {
                    System.out.print(days[i] + "     ");
                }
                else
                {
                    System.out.print(days[i] + "    ");
                }
            }
            if((i + 1) % 7 == 0)
            {
                System.out.println();
            }
        }
    }

    public static void printDays(int[][] days)
    {
        System.out.println("SUN  MON  TUE  WED  THU  FRI  SAT");
        for(int i = 0; i < days.length; i++)
        {
            for(int j = 0; j < days[i].length; j++)
            {
                if(days[i][j] < 1)
                {
                    System.out.print("     ");
                }
                else
                {
                    if(days[i][j] < 10)
                    {
                        System.out.print(days[i][j] + "    ");
                    }
                    else
                    {
                        System.out.print(days[i][j] + "   ");
                    }
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
    }
}

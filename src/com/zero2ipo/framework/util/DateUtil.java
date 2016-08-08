/**
 * Copyright (c) 2010 CEPRI,Inc.All rights reserved.
 * Created by 2010-9-9
 */
package com.zero2ipo.framework.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @title :时间日期操作工具类
 * @description :
 * @author: zhengYunFei
 * @date: 2010-9-9
 */
public class DateUtil {

    // allStyle eg:"yyyy年M月d日 HH:mm:ss E a" => 2011年7月25日 11:18:46 星期一 上午
    public static final String _DEFAULT1 = "yyyy-MM-dd HH:mm";			//2011-07-25 11:18

    public static final String _DEFAULT2 = "yyyy-MM-dd HH:mm:ss";		//2011-07-25 11:18:46

    public static final String _DEFAULT3 = "yyyyMMdd";					//20110725

    public static final String _DEFAULT4 = "yyyy-MM-dd";				//2014-09-16

    public static final String _DEFAULT_CN = "yyyy年M月d日 HH:mm:ss";	//2011年7月25日 11:18:46

    public static final String _DEFAULT_CN_WEEK = "yyyy年M月d日 E";		//2011年7月25日 11:18:46 星期一

    public static final String _DEFAULT_CN_WEEK1 = "E";//星期一


    /**
	 * 格式化日期为String型(yyyy-MM-dd)
	 *
	 * @param date
	 * @return
	 */
	public static String format(Date date){
		return formatDate(date,_DEFAULT4);
	}

    /**
     * 日期时间格式转换
     * */
    public static String formatDate(Date date){
		return formatDate(date,_DEFAULT1);
    }

    /**
     * 字符串日期格式化
     * @param date
     * @param format
     * @return
     */
    public static String strFormatDate(String date,String format){
        SimpleDateFormat sdf=new SimpleDateFormat(_DEFAULT2);//小写的mm表示的是分钟
        try {
            Date time=sdf.parse(date);
            return formatDate(time,format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期时间格式转换
     * @param:String style:_DEFAULT1.2...
     * */
    public static String formatDate(Date date,String style){
		return new SimpleDateFormat(style).format(date);
    }

    /**
     * 获取数据库当前日期时间
     * */
    public static String getCurrentDate(){
        return getCurrentDate(_DEFAULT2);
    }

    /**
     * 获取当前日期时间
     * @param:String style:_DEFAULT1.2...
     * */
    public static String getCurrentDate(String style){
//		return new SimpleDateFormat(style).format(getDataBaseDate());
		// 统一取服务器时间，关键数据的时间 请通过sql 中的sysdate统一处理
        Calendar mycd = Calendar.getInstance();
        return new SimpleDateFormat(style).format(mycd.getTime());
    }



    /**
     * @title:获取所传日期的相应星期
     * @param:String date: yyyy-MM-dd HH:mm
     * */
    public static String getWeek(String date){
    	Date dates = null;
    	String week = null;
    	try {
			dates = new SimpleDateFormat(_DEFAULT1).parse(date);
			week = formatDate(dates,_DEFAULT_CN_WEEK1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return week;
    }

    /**
     * 得到日期格式为YYYY $sStr MM $sStr DD其中sStr为分割字符
     **/
    public static String getMiddle(String sStr) {
        Calendar mycd = Calendar.getInstance();
        int year = mycd.get(Calendar.YEAR);
        int month = mycd.get(Calendar.MONTH) + 1;
        int date = mycd.get(Calendar.DATE);
        String re = "" + String.valueOf(year);
        if (month < 10)
            re += sStr + "0" + String.valueOf(month);
        else
            re += sStr + String.valueOf(month);
        if (date < 10)
            re += sStr + "0" + String.valueOf(date);
        else
            re += sStr + String.valueOf(date);
        return re;
    }

    /**
     * 获取当前时间+1日期时间
     * */
    public static String getAfterDate(String style){
        Calendar mycd = Calendar.getInstance();
        mycd.add(Calendar.DATE, +1);
        Date date = mycd.getTime();
        return new SimpleDateFormat(style).format(date);
    }

    /**
     * 获取日期处理后的日期
     * @author： 张志强
     */
    public static Date getAddedDate(Date date, int addYear, int addMonth,
            int addDay, int addHour, int addMinute, int addSecond) {
        Calendar nowdate=new GregorianCalendar();
        nowdate.setTime(date);
        nowdate.add(Calendar.YEAR,addYear);
        nowdate.add(Calendar.MONTH,addMonth);
        nowdate.add(Calendar.DAY_OF_MONTH,addDay);
        nowdate.add(Calendar.HOUR_OF_DAY,addHour);
        nowdate.add(Calendar.MINUTE,addMinute);
        nowdate.add(Calendar.SECOND,addSecond);
        return nowdate.getTime();
    }

    /** 得到系统当前日期及时间 格式为 yyyy-MM-dd HH:mm:ss */
    public static String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /** 得到long型的日期值 * */
    public static Time getSqlTime() {
        return new Time(getTime());
    }

    /** 获取系统当前时间 * */
    public static long getTime() {
        Date dt = new Date();
        return dt.getTime();
    }

    /** 获取系统当前时间 * */
    public static Date getJavaDate() {
        return new Date();
    }

    /** 获取系统当前日期 得到的日期格式如：2004-10-09 * */
    public static java.sql.Date getSqlDate() {
        return new java.sql.Date(getTime());
    }

    /** 取得Timestamp类型时间 * */
    public static Timestamp getTimestamp() {
        return new Timestamp(getTime());
    }

    /** 得到Calendar对象 * */
    public static Calendar getCD() {
        Calendar mycd = Calendar.getInstance();
        return mycd;
    }

    /** 得到sStr格式日期 * */
    public static String getAll(String sStr) {
        Calendar mycd = Calendar.getInstance();
        return mycd.get(Calendar.YEAR) + sStr + (mycd.get(Calendar.MONTH) + 1)
                + sStr + mycd.get(Calendar.DATE);
    }

    /** 得到日期,以-为分割符 * */
    public static String getAll() {
        return getAll("-");
    }

    /** 得到系统当前年 * */
    public static int getYear() {
        Calendar mycd = Calendar.getInstance();
        return mycd.get(Calendar.YEAR);
    }

    /** 得到系统当前月 * */
    public static int getMonth() {
        Calendar mycd = Calendar.getInstance();
        return mycd.get(Calendar.MONTH) + 1;
    }

    /** 得到系统当前日 * */
    public static int getDate() {
        Calendar mycd = Calendar.getInstance();
        return mycd.get(Calendar.DATE);
    }

    /** 得到系统年 * */
    public static int getAddYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /** 得到系统月 * */
    public static int getAddMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /** 得到系统日 * */
    public static int getAddDate() {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    /** 得到日期格式为yyyy-mm-dd * */
    public static String getMiddle() {
        return getMiddle("-");
    }

    /** 得到日期格式为YYYY $sStr MM 其中sStr为分割字符 * */
    public static String getMiddleYM(String sStr) {
        Calendar mycd = Calendar.getInstance();
        int year = mycd.get(Calendar.YEAR);
        int month = mycd.get(Calendar.MONTH) + 1;
        String re = "" + String.valueOf(year);
        if (month < 10)
            re += sStr + "0" + String.valueOf(month);
        else
            re += sStr + String.valueOf(month);
        return re;
    }

    /** 得到日期格式为 YYYY $sStr MM $sStr DD $sStr hh:mm:ss其中sStr为分割字符 * */
    public static String getTimeStr(String sStr) {

        Calendar mycd = Calendar.getInstance();
        String re = "" + mycd.get(Calendar.YEAR);
        if (mycd.get(Calendar.MONTH) + 1 < 10)
            re += sStr + "0" + String.valueOf(mycd.get(Calendar.MONTH) + 1);
        else
            re += sStr + String.valueOf(mycd.get(Calendar.MONTH) + 1);
        if (mycd.get(Calendar.DATE) < 10)
            re += sStr + "0" + String.valueOf(mycd.get(Calendar.DATE));
        else
            re += sStr + String.valueOf(mycd.get(Calendar.DATE));

        if (mycd.get(Calendar.HOUR) < 10)
            re += " " + "0" + String.valueOf(mycd.get(Calendar.HOUR));
        else
            re += " " + String.valueOf(mycd.get(Calendar.HOUR));
        if (mycd.get(Calendar.MINUTE) < 10)
            re += ":0" + String.valueOf(mycd.get(Calendar.MINUTE));
        else
            re += ":" + String.valueOf(mycd.get(Calendar.MINUTE));
        if (mycd.get(Calendar.SECOND) < 10)
            re += ":0" + String.valueOf(mycd.get(Calendar.SECOND));
        else
            re += ":" + String.valueOf(mycd.get(Calendar.SECOND));
        return re;
    }

    /** 取得两个日期的相隔天数 * */
    public static int getDays(Date sd, Date ed) {
        return (int)((ed.getTime() - sd.getTime()) / (3600 * 24 * 1000));
    }

	/**
	 * 计算日期间隔的天数
	 * @param beginDate :  开始日期  beginDate != null
	 * @param endDate   :  结束日期
	 * @return int : 天数
	 */
	public static int daysBetweenDates(Date beginDate, Date endDate) {
		int days = 0;
		Calendar calo = Calendar.getInstance();
		Calendar caln = Calendar.getInstance();
		calo.setTime(beginDate);
		caln.setTime(endDate);
		int oday = calo.get(6);
		int nyear = caln.get(1);
		for (int oyear = calo.get(1); nyear > oyear;) {
			calo.set(2, 11);
			calo.set(5, 31);
			days += calo.get(6);
			oyear++;
			calo.set(1, oyear);
		}
		int nday = caln.get(6);
		days = (days + nday) - oday;
		return days;
	}

	/**
	 * 字符转化成时间
	 * @param strDate   时间字符串
	 * @param oracleFormat  格式
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Date stringToDate(String strDate, String oracleFormat) {
		if (strDate == null)
			return null;
		Hashtable h = new Hashtable();
		String javaFormat = "";
		String s = oracleFormat.toLowerCase();
		if (s.indexOf("yyyy") != -1)
			h.put(new Integer(s.indexOf("yyyy")), "yyyy");
		else if (s.indexOf("yy") != -1)
			h.put(new Integer(s.indexOf("yy")), "yy");
		if (s.indexOf("mm") != -1)
			h.put(new Integer(s.indexOf("mm")), "MM");
		if (s.indexOf("dd") != -1)
			h.put(new Integer(s.indexOf("dd")), "dd");
		if (s.indexOf("hh24") != -1)
			h.put(new Integer(s.indexOf("hh24")), "HH");
		if (s.indexOf("mi") != -1)
			h.put(new Integer(s.indexOf("mi")), "mm");
		if (s.indexOf("ss") != -1)
			h.put(new Integer(s.indexOf("ss")), "ss");
		for (int intStart = 0; s.indexOf("-", intStart) != -1; intStart++) {
			intStart = s.indexOf("-", intStart);
			h.put(new Integer(intStart), "-");
		}
		for (int intStart = 0; s.indexOf("/", intStart) != -1; intStart++) {
			intStart = s.indexOf("/", intStart);
			h.put(new Integer(intStart), "/");
		}
		for (int intStart = 0; s.indexOf(" ", intStart) != -1; intStart++) {
			intStart = s.indexOf(" ", intStart);
			h.put(new Integer(intStart), " ");
		}
		for (int intStart = 0; s.indexOf(":", intStart) != -1; intStart++) {
			intStart = s.indexOf(":", intStart);
			h.put(new Integer(intStart), ":");
		}
		if (s.indexOf("/u5E74") != -1)
			h.put(new Integer(s.indexOf("/u5E74")), "/u5E74");
		if (s.indexOf("/u6708") != -1)
			h.put(new Integer(s.indexOf("/u6708")), "/u6708");
		if (s.indexOf("/u65E5") != -1)
			h.put(new Integer(s.indexOf("/u65E5")), "/u65E5");
		if (s.indexOf("/u65F6") != -1)
			h.put(new Integer(s.indexOf("/u65F6")), "/u65F6");
		if (s.indexOf("/u5206") != -1)
			h.put(new Integer(s.indexOf("/u5206")), "/u5206");
		if (s.indexOf("/u79D2") != -1)
			h.put(new Integer(s.indexOf("/u79D2")), "/u79D2");
		int i = 0;
		while (h.size() != 0) {
			Enumeration e = h.keys();
			int n = 0;
			while (e.hasMoreElements()) {
				i = ((Integer) e.nextElement()).intValue();
				if (i >= n)
					n = i;
			}
			String temp = (String) h.get(new Integer(n));
			h.remove(new Integer(n));
			javaFormat = temp + javaFormat;
		}
		SimpleDateFormat df = new SimpleDateFormat(javaFormat);
		Date myDate = new Date();
		try {
			myDate = df.parse(strDate);
		} catch (Exception e) {
			return null;
		}
		return myDate;
	}

	/**
	 * 计算 月  有多少天
	 * @param year：  年
	 * @param month：月
	 * @return 天数
	 */
	public static int countDay(int year, int month) {
        if ((year % 400 == 0) || ((year % 4 == 0)&&(year % 100 != 0))) {
            switch (month) {
	            case 1: return 31;
	            case 2: return 29;
	            case 3: return 31;
	            case 4: return 30;
	            case 5: return 31;
	            case 6: return 30;
	            case 7: return 31;
	            case 8: return 31;
	            case 9: return 30;
	            case 10:return 31;
	            case 11:return 30;
	            case 12:return 31;
            }
        } else {
            switch (month) {
	            case 1:	return 31;
	            case 2:	return 28;
	            case 3:	return 31;
	            case 4:	return 30;
	            case 5:	return 31;
	            case 6:	return 30;
	            case 7:	return 31;
	            case 8:	return 31;
	            case 9:	return 30;
	            case 10:return 31;
	            case 11:return 30;
	            case 12:return 31;
            }
        }
        return 31;
    }

	/**
	 * 添加N天
	 * @param strDate : 日期
	 * @param a ：需要增加的天数
	 * @return　日期字符串
	 */
	public static String dateadd(Date strDate, int a) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strDate1 = sdf.format(strDate);
		int year = Integer.parseInt(strDate1.substring(0, 4));
		int month = Integer.parseInt(strDate1.substring(5, 7));
		int day = Integer.parseInt(strDate1.substring(8, 10));
		int md = countDay(year,month);
		int i = (day + a) / md;
		int j = (day + a) % md;
		if (j == 0) {
			i = i - 1;
			j = md;
		}
		String strmon = "";
		String strday = "";
		String mondiff = "";
		if (i < 2) {
			if (Integer.toString(j).length() == 1) {
				strday = "0" + Integer.toString(j);
			} else {
				strday = Integer.toString(j);
			}
			if ((month + i) > 12) {
				int yeardiff = (month + i) / 12;
				int monthmod = (month + i) % 12;
				mondiff = Integer.toString(monthmod);
				if (Integer.toString(monthmod).length() == 1) {
					mondiff = "0" + Integer.toString(monthmod);
				}
				str = Integer.toString(year + yeardiff) + "-" + mondiff + "-"
						+ strday;
			} else {
				strmon = Integer.toString(month + i);
				if (Integer.toString(month + i).length() == 1) {
					strmon = "0" + Integer.toString(month + i);
				}
				str = Integer.toString(year) + "-" + strmon + "-" + strday;
			}
		} else {
			// 主要判断假如天数，月份溢出的处理，
			System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeee");
		}
		return str;
	}

	/**
	 * 添加一天
	 * @param strDate
	 * @return
	 */
	public static String addOneDay(String strDate){ // YYYY-MM-DD
		int[] standardDays = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int[] leapyearDays = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int y = Integer.parseInt(strDate.substring(0, 4));
		int m = Integer.parseInt(strDate.substring(4, 6));
		int d = Integer.parseInt(strDate.substring(6, 8)) + 1;
		int maxDateCount = 0;
		System.out.println(y);
		System.out.println(m);
		System.out.println(d);
		if ((y % 4 == 0 && y % 100 != 0) || y % 400 == 0) {
			maxDateCount = leapyearDays[m - 1];
		} else {
			maxDateCount = standardDays[m - 1];
		}
		if (d > maxDateCount) {
			d = 1;
			m++;
		}
		if (m > 12) {
			m = 1;
			y++;
		}
		java.text.DecimalFormat yf = new java.text.DecimalFormat("0000");
		java.text.DecimalFormat mdf = new java.text.DecimalFormat("00");
		return yf.format(y) + mdf.format(m) + mdf.format(d);
	}

	/**
	 * 增加月
	 * @param strDate  初始年月 yyyyMM
	 * @param intDiff 增加的数量
	 * @return
	 */
	public static String getMonthBetween(String strDate, int intDiff) {
		try {
			int intYear = Integer.parseInt(strDate.substring(0, 4));
			int intMonth = Integer.parseInt(strDate.substring(4, 6));
			String strDay = "";
			if (strDate.length() > 6)
				strDay = strDate.substring(6, strDate.length());
			for (intMonth += intDiff; intMonth <= 0; intMonth += 12)
				intYear--;
			for (; intMonth > 12; intMonth -= 12)
				intYear++;
			if (intMonth < 10) {
				return Integer.toString(intYear) + "-0"
						+ Integer.toString(intMonth) + strDay;
			}
			return Integer.toString(intYear) + "-" + Integer.toString(intMonth)
					+ strDay;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 计算两个年月之间的相差月数
	 * @param strDateBegin
	 * @param strDateEnd
	 * @return
	 */
	public static String getMonthBetween(String strDateBegin, String strDateEnd) {
		try {
			String strOut;
			if (strDateBegin.equals("") || strDateEnd.equals("")
					|| strDateBegin.length() != 6 || strDateEnd.length() != 6) {
				strOut = "";
			} else {
				int intMonthBegin = Integer.parseInt(strDateBegin.substring(0,
						4))
						* 12 + Integer.parseInt(strDateBegin.substring(4, 6));
				int intMonthEnd = Integer.parseInt(strDateEnd.substring(0, 4))
						* 12 + Integer.parseInt(strDateEnd.substring(4, 6));
				strOut = String.valueOf(intMonthBegin - intMonthEnd);
			}
			return strOut;
		} catch (Exception e) {
			return "0";
		}
	}

	/**
	 * 计算两个年月之间的相差月数
	 * @param strDateBegin yyyyMM
	 * @param strDateEnd	yyyyMM
	 * @return
	 */
	public static int getMonths(String strDateBegin, String strDateEnd) {
		try {
			int strOut;
			if (strDateBegin.equals("") || strDateEnd.equals("")
					|| strDateBegin.length() != 6 || strDateEnd.length() != 6) {
				strOut = 0;
			} else {
				int intMonthBegin = Integer.parseInt(strDateBegin.substring(0,
						4))
						* 12 + Integer.parseInt(strDateBegin.substring(4, 6));
				int intMonthEnd = Integer.parseInt(strDateEnd.substring(0, 4))
						* 12 + Integer.parseInt(strDateEnd.substring(4, 6));
				strOut = intMonthEnd - intMonthBegin;
			}
			return strOut;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 格式化小时
	 * @param date
	 * @return(HH:00)
	 */
	public static String formatHour(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:00");
		return sdf.format(date);
	}

	/**
	 * 格式化时分秒
	 * @param date
	 * @return(yyyy-MM-dd HH:mm:ss)
	 */
	public static String formatStandard(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 格式化天
	 * @param date
	 * @return(yyyy-MM-dd)
	 */
	public static String formatDay(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	/**
	 * 格式化月
	 * @param date （yyyy-MM-dd HH:mm:ss）
	 * @return(yyyy-MM)
	 */
	public static String formatMonth(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(date);
	}

	/**
	 * 格式化年
	 * @param date
	 * @return(yyyy)
	 */
	public static String formatYear(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(date);
	}

	/**
	 *获得日期字符串数组
	 *计算2个日期之间的每一天
	 *@author ZhengYunfei
	 *@date 2012/07/19
	 *@param beginDate 开始日期
	 *@param endDate 结束日期
     * */
	 public static List<String> TwoDay(String beginDate,String endDate) {
		List<String> list=new ArrayList<String>();
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startD = myFormatter.parse(beginDate);
            Date endD = myFormatter.parse(endDate);
            long day = (endD.getTime() - startD.getTime())
                    / (24 * 60 * 60 * 1000);
            long dayMill = 24 * 60 * 60 * 1000;
            int d = Integer.parseInt(String.valueOf(day));
            String updateTime;
            long longTime;
            for (int i = 0; i <= d; i++) {
                longTime = startD.getTime() + dayMill * i;
                updateTime = myFormatter.format(new Date(longTime));
                list.add(updateTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

	/**
	 * 昨天当前时间
	 */
	public static String getYesterdayTimes(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		long time = (date.getTime() / 1000) - 60 * 60 * 24;
		date.setTime(time * 1000);
		return sdf.format(date);
	}

	/**
	 * 昨天当前日期
	 */
	public static String getYesterday(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		long time = (date.getTime() / 1000) - 60 * 60 * 24;
		date.setTime(time * 1000);
		return sdf.format(date);
	}

	/**
	 * 当前时间 yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return sdf.format(date);
	}

	/**
	 * 当前时间 HH:mm:ss
	 */
	public static String getCurrentHMS(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return sdf.format(date);
	}

	 /**
     * 功能:得到当前月份月初 格式为：xxxx-yy-zz (eg:2013-12-01)
     * @return：String
     * @author ZhengYunfei
     * @date 2013-03-18
     */
	 public static String getThisMonthStart(){
		 String strY=null;
		 Calendar localTime=Calendar.getInstance();
		 int x=localTime.get(Calendar.YEAR);
		 int y=localTime.get(Calendar.MONTH)+1;
		 strY=y>=10?String.valueOf(y):("0"+y);
		 return x+"-"+strY+"-01";
	 }

	 /**
	  * 功能：得到当前月份月底 格式为：xxxx-yy-zz (eg: 2007-12-31)
	  * @return String
	 * @author ZhengYunfei
	 * @date 2013-03-18
	  **/
	public static String getThisMonthEnd() {
	    String strY = null;
	    String strZ = null;
	    boolean leap = false;
	    Calendar localTime=Calendar.getInstance();
	    int x = localTime.get(Calendar.YEAR);
	    int y = localTime.get(Calendar.MONTH) + 1;
	    if (y == 1 || y == 3 || y == 5 || y == 7 || y == 8 || y == 10 || y == 12) {
	        strZ = "31";
	    }
	    if (y == 4 || y == 6 || y == 9 || y == 11) {
	        strZ = "30";
	    }
	    if (y == 2) {
	        leap = leapYear(x);
	        if (leap) {
	            strZ = "29";
	        }else {
	            strZ = "28";
	        }
	    }
	    strY = y >= 10 ? String.valueOf(y) : ("0" + y);
	    return x + "-" + strY + "-" + strZ;
	}

	/**
	 * 功能：判断输入年份是否为闰年
	 * @param year
	 * @return 是：true  否：false
	 * @author ZhengYunfei
	 * @date 2013-03-18
	 */
	public static boolean leapYear(int year) {
	   boolean leap;
	   if (year % 4 == 0) {
	       if (year % 100 == 0) {
	           if (year % 400 == 0) leap = true;
	               else leap = false;
	           }
	       else leap = true;
	   } else leap = false;
        return leap;
	}
	public static void main(String args[]){
        System.out.println("a:"+strFormatDate("2015-12-25 23:22:36",_DEFAULT3));
    }
}

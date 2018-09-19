/**
 * <p>Title: 日期常用工具方法类</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 源天软件</p>
 * @author Rockey 2006-4-10
 * @version 1.0
 */
package cc.alpha.base.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.DateUtil;

import com.velcro.base.BaseContext;

/**
 * 日期常用工具方法类
 */
public final class DateHelper {
	/**
	 * "YYYY-MM-DD"日期格式
	 */
	public static final String DATE_YYYYMMMMDD = "yyyy-MM-dd";

	/**
	 * "HH:MM:SS"时间格式
	 */
	public static final String TIME_HHCMMCSS = "HH:mm:ss";
	/**
	 * "HH:MM"时间格式
	 */
	public static final String TIME_HHCMM = "HH:MM";
	/**
	 * "HH:MM:SS AMPM"时间格式
	 */
	public static final String TIME_HHCMMCSSAMPM = "HH:MM:SS AMPM";

	private static SimpleDateFormat m_dtFormater = null;

	private static SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static String[] weekday = {"日", "一", "二", "三", "四", "五", "六"};

	public static String convertDateIntoYYYYMMDD_HHCMMCSSStr(Date date) {
		return convertDateIntoDisplayStr(date, DATE_YYYYMMMMDD + " " + TIME_HHCMMCSS);
	}

	public static String convertDateIntoYYYYMMDDStr(Date date) {
		return convertDateIntoDisplayStr(date, null);
	}

	public static String convertDateIntoYYYYMMDD_HHMMStr(Date date) {
		return convertDateIntoDisplayStr(date, DATE_YYYYMMMMDD + " " + TIME_HHCMM);
	}

	public static String convertDateIntoDisplayStr(Date date, String format) {
		String dateStr = null;
		if (format == null)
			format = DATE_YYYYMMMMDD;
		if (m_dtFormater == null) {
			m_dtFormater = new SimpleDateFormat();
		}
		m_dtFormater.applyPattern(format);
		if (date != null) {
			dateStr = m_dtFormater.format(date);
		}
		return dateStr;
	}

	public static String getCurDateTimeStr() {
		Date newdate = new Date();
		long datetime = newdate.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		String str = timestamp.toString();
		return new StringBuffer().append(datetime).toString();
	}

	/**
	 * 获得当前周是当前年的第几周。
	 */
	public static Integer getWeekOfYear() {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setMinimalDaysInFirstWeek(1);
		int weekint = c.get(Calendar.WEEK_OF_YEAR);

		return Integer.valueOf(weekint);
	}

	/**
	 * @return yyyy-MM-dd hh:mm:ss
	 */
	public static String getCurDateTime() {
		Date newdate = new Date();
		long datetime = newdate.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		return (timestamp.toString()).substring(0, 19);
	}

	public static String getDateTimeStr() {
		Date newdate = new Date();
		long datetime = newdate.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		return timestamp.toString();
	}

	/**
	 * @return "yyyy-MM-dd"格式日期
	 */
	public static String getCurrentDate() {
		Date newdate = new Date();
		long datetime = newdate.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		String currentdate = (timestamp.toString()).substring(0, 4) + "-" + (timestamp.toString()).substring(5, 7) + "-" + (timestamp.toString()).substring(8, 10);
		return currentdate;
	}
	
	/**
	 * @return "yyyyMMdd"格式日期
	 */
	public static String getCurDate() {
		Date newdate = new Date();
		long datetime = newdate.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		String currentdate = (timestamp.toString()).substring(0, 4) + (timestamp.toString()).substring(5, 7) + (timestamp.toString()).substring(8, 10);
		return currentdate;
	}

	/**
	 * @param newdate
	 * @return "yyyy-MM-dd"格式日期
	 */
	public static String getDate(Date newdate) {
		if (newdate == null)
			return "";
		long datetime = newdate.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		String currentdate = (timestamp.toString()).substring(0, 4) + "-" + (timestamp.toString()).substring(5, 7) + "-" + (timestamp.toString()).substring(8, 10);
		return currentdate;

	}

	/**
	 * @param date
	 * @return "yyyy-MM-dd HH:MM:SS"格式日期
	 */
	public static String getDateTime(Date date) {
		if (date == null)
			return "";
		long datetime = date.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		return (timestamp.toString()).substring(0, 19);
	}

	/**
	 * @param date
	 * @return "yyyy-MM-dd HH:MM"格式日期
	 */
	public static String getDateHHMM(Date date) {
		if (date == null)
			return "";
		long datetime = date.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		return (timestamp.toString()).substring(0, 16);
	}

	/**
	 * @param datebegin
	 * @param dateend
	 * @return 获取开始时间和结束时间之间的天数
	 */
	public static long getDisDays(String datebegin, String dateend) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dateBegin = sdf.parse(datebegin);
			Date dateEnd = sdf.parse(dateend);
			return (dateEnd.getTime() - dateBegin.getTime()) / (3600 * 24 * 1000) + 1;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 获取开始时间和结束时间之间的月份
	 * 
	 * @param datebegin
	 * @param dateend
	 * @return
	 */
	public static int getDisMonth(String datebegin, String dateend) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dateBegin = sdf.parse(datebegin);
			Calendar cBegin = Calendar.getInstance();
			cBegin.setTime(dateBegin);
			int yearBegin = cBegin.get(Calendar.YEAR);
			int monthBegin = cBegin.get(Calendar.MONTH) + 1;
			Date dateEnd = sdf.parse(dateend);
			Calendar cEnd = Calendar.getInstance();
			cEnd.setTime(dateEnd);
			int yearEnd = cEnd.get(Calendar.YEAR);
			int monthEnd = cEnd.get(Calendar.MONTH) + 1;
			int months = monthEnd - monthBegin + 12 * (yearEnd - yearBegin);
			return months;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * @return hh:mm:ss
	 */
	public static String getCurrentTime() {
		Date newdate = new Date();
		long datetime = newdate.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		String currenttime = (timestamp.toString()).substring(11, 13) + ":" + (timestamp.toString()).substring(14, 16) + ":" + (timestamp.toString()).substring(17, 19);
		return currenttime;
	}

	/**
	 * 转换当前日期时间，如："2012-08-01 23:59:59.999"
	 * @return 20120801235959
	 */
	public static String getCurrentDateTime(){
		Date newdate = new Date();
		long datetime = newdate.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		String currentdate = (timestamp.toString()).substring(0, 4) + (timestamp.toString()).substring(5, 7) + (timestamp.toString()).substring(8, 10);
		String currenttime = (timestamp.toString()).substring(11, 13) + (timestamp.toString()).substring(14, 16) + (timestamp.toString()).substring(17, 19);
		
		return currentdate + currenttime;
	}
	
	/**
	 * 转换当前日期时间，如："2012-08-01 23:59:59.999"
	 * @return 20120801_235959
	 */
	public static String getCurDatetime(){
		Date newdate = new Date();
		long datetime = newdate.getTime();
		Timestamp timestamp = new Timestamp(datetime);
		String currentdate = (timestamp.toString()).substring(0, 4) + (timestamp.toString()).substring(5, 7) + (timestamp.toString()).substring(8, 10);
		String currenttime = (timestamp.toString()).substring(11, 13) + (timestamp.toString()).substring(14, 16) + (timestamp.toString()).substring(17, 19);
		
		return currentdate + "_" + currenttime;
	}
	
	/**
	 * 计算2个日期之间间隔天数方法
	 * 
	 * @param d1
	 *            The first Calendar.
	 * @param d2
	 *            The second Calendar.
	 * 
	 * @return 天数
	 */
	public static long getDaysBetween(java.util.Calendar d1, java.util.Calendar d2) {
		return (d1.getTime().getTime() - d2.getTime().getTime()) / (3600 * 24 * 1000);
	}

	/**
	 * 计算2个日期之间间隔天数方法
	 * 
	 * @param d1
	 *            The first Calendar. 格式yyyy-MM-dd
	 * @param d2
	 *            The second Calendar.
	 * 
	 * @return 天数
	 */
	public static long getDaysBetween(String d1, String d2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = sdf.parse(d1);
			Date dt2 = sdf.parse(d2);
			return (dt1.getTime() - dt2.getTime()) / (3600 * 24 * 1000);
		} catch (Exception e) {
			return 0;
		}

	}

	/**
	 * 遍历两个日期间的天数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getMontghBetween(String d1, String d2) {
		StringBuffer daystr = new StringBuffer();
		Map monthmap = new LinkedHashMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int i = 1;
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(d1));
			
			String month = d1;
			while(i < 36){
				if(d1.substring(0,7).equals(d2.substring(0,7))){
					break;
				}
				cal.setTime(sdf.parse(d1));
				cal.add(Calendar.MONTH, 1);
				d1 = sdf.format(cal.getTime());
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * @param d1
	 * @param d2
	 * @param onlyWorkDay
	 *            是否只计算工作日
	 * @return 计算两个日期之间的时间间隔(d1-d2)，可选择是否计算工作日
	 */
	public static long getDaysBetween(String d1, String d2, boolean onlyWorkDay) {
		if (!onlyWorkDay) {
			return getDaysBetween(d1, d2);
		} else {
			long days = 0;
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date dt1 = sdf.parse(d1);
				Date dt2 = sdf.parse(d2);
				days = (dt1.getTime() - dt2.getTime()) / (3600 * 24 * 1000);
				for (calendar.setTime(dt1); !calendar.getTime().before(dt2); calendar.add(Calendar.DAY_OF_YEAR, -1)) {
					int week = calendar.get(Calendar.DAY_OF_WEEK);
					if (week == Calendar.SUNDAY || week == Calendar.SATURDAY) {
						days--;
					}
				}
				if (days < 0) {
					days = 0;
				}
			} catch (Exception e) {
			}
			return days;
		}
	}

	/**
	 * @param enddate
	 * @param startdate
	 * @param onlyWorkDay
	 *            是否只计算工作日
	 * @return 计算两个日期之间的时间间隔(d1-d2)，可选择是否计算工作日 和getDaysBetween的区别在于,这个方法会出负数
	 */
	public static long getDaysBetween2(String enddate, String startdate, boolean onlyWorkDay) {
		if (!onlyWorkDay) {
			return getDaysBetween(startdate, enddate);
		} else {
			long days = 0;
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date dt1 = sdf.parse(startdate);
				Date dt2 = sdf.parse(enddate);
				days = (dt1.getTime() - dt2.getTime()) / (3600 * 24 * 1000);
				if (days >= 0) {
					for (calendar.setTime(dt1); !calendar.getTime().before(dt2); calendar.add(Calendar.DAY_OF_YEAR, -1)) {
						int week = calendar.get(Calendar.DAY_OF_WEEK);
						if (week == Calendar.SUNDAY || week == Calendar.SATURDAY) {
							days--;
						}
					}
				} else {
					for (calendar.setTime(dt1); calendar.getTime().before(dt2); calendar.add(Calendar.DAY_OF_YEAR, +1)) {
						int week = calendar.get(Calendar.DAY_OF_WEEK);
						if (week == Calendar.SUNDAY || week == Calendar.SATURDAY) {
							days++;
						}
					}
				}

			} catch (Exception e) {
			}
			return days;
		}
	}

	/**
	 * @param date
	 * @return 判断日期是否为工作日(周六和周日为非工作日)
	 */
	public static boolean isWorkDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		if (week == Calendar.SUNDAY || week == Calendar.SATURDAY) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * @param date
	 * @return 判断日期是否为周末
	 */
	public static boolean isWeekDay(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Date d = sdf.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			int week = calendar.get(Calendar.DAY_OF_WEEK);
			if (week == Calendar.SUNDAY || week == Calendar.SATURDAY) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * @param date
	 * @return 判断是否为周日
	 */
	public static boolean isSunDay(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Date d = sdf.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			int week = calendar.get(Calendar.DAY_OF_WEEK);
			if (week == Calendar.SUNDAY) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 计算两个时间之间的间隔 单位：分钟(minutes) 格式 yyyy-MM-dd/HH:mm:ss
	 * */
	public static long getMinutesBetween(String s1, String s2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
		try {
			Date dt1 = sdf.parse(s1);
			Date dt2 = sdf.parse(s2);
			return (dt1.getTime() - dt2.getTime()) / (60 * 1000);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * @param d1 开始日期 格式yyyy-MM-dd
	 * 
	 * @param d2 结束日期.
	 * 
	 * @return 按月分隔的list，list里面每个月一个map,第一天begindate，最后一天enddate
	 */
	public static List<HashMap> getDateBetween(String d1, String d2) {
		ArrayList<HashMap> list = new ArrayList<HashMap>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal1.setTime(sdf.parse(d1));
			cal2.setTime(sdf.parse(d2));

			int monthnum = (cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) * 12 + cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
			for (int i = 0; i < monthnum; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("begindate", sdf.format(cal1.getTime()));
				cal1.add(Calendar.DATE, cal1.getActualMaximum(Calendar.DATE) - cal1.get(Calendar.DATE));
				map.put("enddate", sdf.format(cal1.getTime()));
				list.add(map);
				cal1.add(Calendar.MONTH, 1);
				cal1.add(Calendar.DATE, 1 - cal1.get(Calendar.DATE));
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("begindate", sdf.format(cal1.getTime()));
			map.put("enddate", d2);
			list.add(map);
		} catch (Exception e) {
			return list;
		}
		return list;
	}

	/**
	 * 两个时间段得相交的天数 格式yyyy-MM-dd
	 * 
	 * 
	 * @return 天数
	 */
	public static long getDays(String b1, String e1, String b2, String e2) {
		long ret = 0;
		String begindate = "";
		String enddate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Calendar begin1 = Calendar.getInstance();
			Calendar end1 = Calendar.getInstance();
			Calendar begin2 = Calendar.getInstance();
			Calendar end2 = Calendar.getInstance();
			begin1.setTime(sdf.parse(b1));
			end1.setTime(sdf.parse(e1));
			begin2.setTime(sdf.parse(b2));
			end2.setTime(sdf.parse(e2));
			// 时间段不相交
			if ((begin2.getTime().getTime() > end1.getTime().getTime() && end2.getTime().getTime() > end1.getTime().getTime())
					|| (begin2.getTime().getTime() < begin1.getTime().getTime() && end2.getTime().getTime() < begin1.getTime().getTime())) {
				// System.out.println("b"+ret);
				return ret;
			}

			if (begin2.getTime().getTime() >= begin1.getTime().getTime()) {
				begindate = sdf.format(begin2.getTime());
			} else {
				begindate = sdf.format(begin1.getTime());
			}
			if (end2.getTime().getTime() >= end1.getTime().getTime()) {
				enddate = sdf.format(end1.getTime());
			} else {
				enddate = sdf.format(end2.getTime());
			}

			if (!begindate.equals("") && !enddate.equals("")) {
				ret = getDisDays(begindate, enddate);
			}
		} catch (Exception e) {

		}
		// System.out.println("e"+ret);
		return ret;
	}

	/**
	 * 比较2个格式为yyyy-MM-dd的日期<br>
	 * 若d1小于d2返回true<br>
	 * d1=2007-10-01,d2=2007-10-15,则返回true
	 * 
	 * @return
	 */
	public static boolean after(String d1, String d2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = sdf.parse(d1);
			Date dt2 = sdf.parse(d2);
			return dt1.getTime() < dt2.getTime();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 比较两个月分的先后.如果d2>d1,返回true
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean afterMonth(String d1, String d2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		try {
			Date dt1 = sdf.parse(d1);
			Date dt2 = sdf.parse(d2);
			return dt1.getTime() < dt2.getTime();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 比较2个日期
	 * 
	 * @return
	 */
	public static boolean afterAndEqual(String d1, String d2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = sdf.parse(d1);
			Date dt2 = sdf.parse(d2);
			return dt1.getTime() <= dt2.getTime();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 移动日期
	 * 
	 * @param date 原日期
	 * 
	 * @param len 移动量
	 * 
	 * @return 移动后日期
	 */
	public static String dayMove(String date, int len) {
		SimpleDateFormat sdf = null;
		if (date.length() > 10) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date));
			cal.add(Calendar.DATE, len);
			return sdf.format(cal.getTime());
		} catch (Exception e) {
			return date;
		}
	}

	/**
	 * 移动日期
	 * 
	 * @param date 原日期
	 * 
	 * @param len 移动量
	 * 
	 * @return 移动后日期
	 */
	public static String dayMove(String date, int len, boolean flag) {
		SimpleDateFormat sdf = null;
		if (date.length() > 10) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date));
			if (flag) {// 如果要排除双休日
				int addnum = 1;
				if (len < 0) {
					// len为负数的情况
					len = -1 * len;
					addnum = -1;
				}
				for (int i = 1; i <= len; i++) {
					cal.add(Calendar.DATE, addnum);
					int week = cal.get(Calendar.DAY_OF_WEEK);
					week = cal.get(Calendar.DAY_OF_WEEK);
					if (week == 7 || week == 1) {
						len += 1;
					}
				}
			} else {
				cal.add(Calendar.DATE, len);
			}
			return sdf.format(cal.getTime());
		} catch (Exception e) {
			return date;
		}
	}

	public static String dateTimeMove(String datetime, int len) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(datetime));
			cal.add(Calendar.MINUTE, len);
			return sdf.format(cal.getTime());
		} catch (Exception e) {
			return datetime;
		}
	}

	public static String getCurrentMonth() {
		Calendar today = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String curmonth = sdf.format(today.getTime());
		return curmonth;
	}

	/**
	 * 移动月份
	 * 
	 * @param date 原日期
	 * 
	 * @param len 移动量
	 * 
	 * @return 移动后日期
	 */
	public static String monthMove(String date, int len) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date));
			cal.add(Calendar.MONTH, len);
			return sdf.format(cal.getTime());
		} catch (Exception e) {
			return date;
		}
	}

	/**
	 * 移动月份
	 * 
	 * @param date 原日期
	 * 
	 * @param len 移动量
	 * 
	 * @return 移动后日期
	 */
	public static String monthMove2(String date, int len) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date));
			cal.add(Calendar.MONTH, len);
			return sdf.format(cal.getTime());
		} catch (Exception e) {
			return date;
		}
	}
	
	/**
	 * 移动年
	 * @param date 原日期
	 * @param len 移动量
	 * @return 移动后日期
	 */
	public static String yearMove(String date, int len) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date));
			cal.add(Calendar.YEAR, len);
			return sdf.format(cal.getTime());
		} catch (Exception e) {
			return date;
		}
	}

	/**
	 * 截取2个时间段相交的时间段
	 * 
	 * @return String[] = {array[0]=begindate ,array[1]=enddate} 不相交 array[0]=""
	 */
	public static String[] getBetweenDate(String b1, String e1, String b2, String e2) {
		String[] date = new String[2];
		String begindate = "";
		String enddate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Calendar begin1 = Calendar.getInstance();
			Calendar end1 = Calendar.getInstance();
			Calendar begin2 = Calendar.getInstance();
			Calendar end2 = Calendar.getInstance();
			begin1.setTime(sdf.parse(b1));
			end1.setTime(sdf.parse(e1));
			begin2.setTime(sdf.parse(b2));
			end2.setTime(sdf.parse(e2));
			if ((begin2.getTime().getTime() >= end1.getTime().getTime() && end2.getTime().getTime() >= end1.getTime().getTime())
					|| (begin2.getTime().getTime() <= begin1.getTime().getTime() && end2.getTime().getTime() <= begin1.getTime().getTime())) {
				date[0] = "";
				return date;
			}
			if (begin2.getTime().getTime() >= begin1.getTime().getTime()) {
				begindate = sdf.format(begin2.getTime());
			} else {
				begindate = sdf.format(begin1.getTime());
			}
			if (end2.getTime().getTime() >= end1.getTime().getTime()) {
				enddate = sdf.format(end1.getTime());
			} else {
				enddate = sdf.format(end2.getTime());
			}
			if (!begindate.equals("") && !enddate.equals("")) {
				date[0] = begindate;
				date[1] = enddate;
			}
		} catch (Exception e) {
		}
		return date;
	}
	/**
	 * 移动月份
	 * 
	 * @param date
	 *            时间
	 * @param month
	 *            移动月份
	 * @return
	 */
	public static Date getMonth(Date date, int month) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}

	/**
	 * 取得当前星期的第一天
	 * 
	 * @return
	 */
	public static Date getCurrentWeekFirstDay() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
		Date first = calendar.getTime();// getDay(calendar.getTime(), 1);
		if (getDayOfWeek(date) == 1) {// 如果今天是星期日
			first = getDay(calendar.getTime(), -6);
		} else {
			first = getDay(calendar.getTime(), 1);
		}
		return first;
	}

	/**
	 * 取得当前日期为本周的第几天
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 取当前日期所在年的第几周，传入周日是否为每周第一天
	 * @param datestr
	 * @param sundayIsFirst
	 * @return
	 */
	public static int getWeekOfYear(String datestr, boolean sundayIsFirst) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(datestr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();
		if(sundayIsFirst){
			calendar.setFirstDayOfWeek(Calendar.SUNDAY);
		}else{
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
		}
		calendar.setTime(date);

		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 返回当前月第一天，格式yyyy-MM-dd
	 * @return
	 */
	public static String getCurMonthFirstDayStr(){
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(calendar.getTime());
		return dateStr;
	}
	
	/**
	 * 返回当前月最后一天，格式yyyy-MM-dd
	 * @return
	 */
	public static String getCurMonthLastDayStr(){
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(calendar.getTime());
		return dateStr;
	}
	
	/**
	 * 取得当前月份的第一天
	 * 
	 * @return
	 */
	public static Date getCurrentMonthFirstDay() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	/**
	 * 取得当前月份的最后一天
	 * 
	 * @return
	 */
	public static Date getCurrentMonthLastDay() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	/**
	 * 取得当月的第一天
	 * 
	 * @return
	 */
	public static Date getMonthFirstDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	/**
	 * 取得当月的最后一天
	 * 
	 * @return
	 */
	public static Date getMonthLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	/**
	 * 返回指定日期的前后的i天
	 * 
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date getDay(Date date, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, i);
		return calendar.getTime();
	}

	/**
	 * 读取查询时的开始时间
	 * 
	 * @param start
	 * @return
	 */
	public static Date getStart(Date start) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * String 转为date型
	 */

	public static Date stringtoDate(String stringDate) {
		if (StringHelper.isEmpty(stringDate)) {
			return new Date();
		}
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format1.parse(stringDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将字符串型（2012-8-8）或日期格式型格式化为yyyy-MM-dd格式
	 * @param stringDate
	 * @return
	 */
	public static String stringtoString(String stringDate) {
		double d = NumberHelper.string2Double(stringDate);
		Date date = DateUtil.getJavaDate(d);
		if(d== -1){
			date = DateHelper.stringtoDate(stringDate);
		}
		SimpleDateFormat datetemp = new SimpleDateFormat("yyyy-MM-dd");
		try {
			stringDate = datetemp.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringDate;
	}
	
	/**
	 * 转化数据库通配符
	 * 
	 * @param str
	 * @return
	 */
	public static String wildcard(String str) {
		if (StringHelper.isEmpty(str)) {
			return "";
		}
		String dialectname = StringHelper.null2String(BaseContext.getDialectname());
		if (dialectname.indexOf("SQLServer") != -1) {
			if (str.indexOf("[") != -1) {
				str = StringHelper.replaceString(str, "[", "[[]");
			}
			if (str.indexOf("%") != -1) {
				str = StringHelper.replaceString(str, "%", "[%]");
			}
			if (str.indexOf("_") != -1) {
				str = StringHelper.replaceString(str, "_", "[_]");
			}
		}
		if (dialectname.indexOf("Oracle") != -1) {

		}
		return str;
	}

	/**
	 * 获得两个日期差，差的形式为毫秒 比如：现在是2004-03-26 13：31：40 过去是：2004-01-02 11：30：24
	 * 
	 * @param begin
	 * @param end
	 * @param flog
	 *            是否包含节假日
	 * @return
	 */
	public static long getTimeBetween(String begintime, String endtime, boolean flag) {
		if (StringHelper.isEmpty(begintime) || StringHelper.isEmpty(endtime)) {
			return 0;
		}
		java.util.Date begin = null;
		java.util.Date end = null;
		try {
			begin = dfs.parse(begintime);
			end = dfs.parse(endtime);
		} catch (ParseException e) {
			//只有时间的情况，格式化出错，默认加当前日期
			try{
				begintime = getCurrentDate()+" "+begintime;
				endtime = getCurrentDate()+" "+endtime;
				begin = dfs.parse(begintime);
				end = dfs.parse(endtime);
			}catch(ParseException e1){
				e1.printStackTrace();
			}
		}
		long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒

		if (!flag) {// 去除周末
			String tmpdatestr = begintime;
			Date tmpdate = null;
			do {
				try {
					tmpdate = dfs.parse(tmpdatestr);
					if (!DateHelper.isWorkDay(tmpdate)) {
						between = between - 24 * 3600;
					}
					tmpdatestr = DateHelper.dayMove(tmpdatestr, 1);
					tmpdate = dfs.parse(tmpdatestr);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} while (tmpdate.getTime() < end.getTime());
		}
		if (between < 0) {
			between = 0;
		}
		return between;
	}

	public static String getTimeStrBetween(long longtime) {
		long day1 = longtime / (24 * 3600);
		long hour1 = longtime % (24 * 3600) / 3600;
		long minute1 = longtime % 3600 / 60;
		long second1 = longtime - (day1 * 24 * 3600 + hour1 * 3600 + minute1 * 60);
		StringBuffer rtn = new StringBuffer();
		if (day1 > 0) {
			rtn.append(day1).append("天");
		}
		if (hour1 > 0) {
			rtn.append(hour1).append("小时");
		}
		if (minute1 > 0) {
			rtn.append(minute1).append("分");
		}
		if (second1 > 0) {
			rtn.append(second1).append("秒");
		}
		return rtn.toString();
	}

	public static String getMonday(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date));
			// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
			int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
			if (1 == dayWeek) {
				cal.add(Calendar.DAY_OF_MONTH, -1);
			}
			cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
			int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
			cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
			return sdf.format(cal.getTime());
		} catch (Exception e) {
			return date;
		}
	}
	
	public static String getMondayByYearandWeek(int year, int week) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		try {
			Calendar cal = Calendar.getInstance();
			cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.WEEK_OF_YEAR, week);
			cal.set(Calendar.DAY_OF_WEEK,1);
			return sdf.format(cal.getTime());
		} catch (Exception e) {
			return "1970-01-01";
		}
	}

	public static String getSunday(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date));
			// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
			int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
			if (1 == dayWeek) {
				cal.add(Calendar.DAY_OF_MONTH, -1);
			}
			cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
			int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
			cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
			cal.add(Calendar.DATE, 6);
			return sdf.format(cal.getTime());
		} catch (Exception e) {
			return date;
		}
	}

	/**
	 * 取得当月的第一天
	 * 
	 * @return
	 */
	public static String getMonthFirstDay(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(date));
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			return sdf.format(calendar.getTime());
		} catch (Exception e) {
			return date;
		}
	}

	/**
	 * 取得当月的最后一天
	 * 
	 * @return
	 */
	public static String getMonthLastDay(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(date));
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			return sdf.format(calendar.getTime());
		} catch (Exception e) {
			return date;
		}
	}

	/**
	 * 移动月份
	 * 
	 * @param date 原日期
	 * 
	 * @param len 移动量
	 * 
	 * @return 移动后日期
	 */
	public static String dayMovemonth(String date, int len) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date));
			cal.add(Calendar.MONTH, len);
			return sdf.format(cal.getTime());
		} catch (Exception e) {
			return date;
		}
	}

	public static boolean checkDate(String date) {
		Date d = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setLenient(false);// 这个的功能是不把1996-13-3 转换为1997-1-3
		try {
			d = df.parse(date);
		} catch (Exception e) {
			d = new Date();
			return false;
		}
		return true;
	}
	
	/**
	 * 遍历两个日期间的天数
	 * @param d1
	 * @param d2(不包含D2)
	 * @return
	 */
	public static String[] getDateStrBetween(String d1, String d2) {
		StringBuffer daystr = new StringBuffer(d1);
		Map monthmap = new LinkedHashMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(d1));
			int week = cal.get(Calendar.DAY_OF_WEEK);
			if (week == Calendar.SUNDAY || week == Calendar.SATURDAY) {
				daystr = new StringBuffer(d1 + ",1");
			}
			int i = 0;
			int k = 0;
			String month = d1;
			while(i < 200){
				String tempmonth = d1.substring(0, 7);
				if(month.substring(0, 7).equals(tempmonth)){
					monthmap.put(month.substring(0, 7), ++k);
				}else{
					month = tempmonth;
					k = 0;
					monthmap.put(month.substring(0, 7), ++k);
				}
				cal.setTime(sdf.parse(d1));
				cal.add(Calendar.DATE, 1);
				d1 = sdf.format(cal.getTime());
				
				if(!d1.equals(d2)){
					week = cal.get(Calendar.DAY_OF_WEEK);
					if (week == Calendar.SUNDAY || week == Calendar.SATURDAY) {
						daystr.append(";").append(d1 + "," + week);
					}else{
						daystr.append(";").append(d1);
					}
				}else{
					break;
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    String monthstr = "";
	    Iterator keyit = monthmap.keySet().iterator();
		while (keyit.hasNext()) {
			String key = (String)keyit.next();
			monthstr += ";" + key + "," + monthmap.get(key);
		}
		String[] retstr = {monthstr.substring(1),daystr.toString()};
		return retstr;
	}
	
	/**
	 * 遍历两个日期间的天数
	 * @param d1
	 * @param d2(不包含D2)
	 * @return
	 */
	public static String[] getWeekStrBetween(String d1, String d2) {
		StringBuffer daystr = new StringBuffer();
		Map monthmap = new LinkedHashMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(d1));
			int week = cal.get(Calendar.DAY_OF_WEEK);
			if (week == Calendar.SUNDAY) {
				daystr.append(";").append(cal.get(Calendar.WEEK_OF_YEAR));
			}
			int i = 0;
			int k = 0;
			String month = d1;
			while(i < 360){
				String tempmonth = d1.substring(0, 7);
				if(month.substring(0, 7).equals(tempmonth)){
					monthmap.put(month.substring(0, 7), ++k);
				}else{
					month = tempmonth;
					k = 0;
					monthmap.put(month.substring(0, 7), ++k);
				}
				cal.setTime(sdf.parse(d1));
				cal.add(Calendar.DATE, 1);
				d1 = sdf.format(cal.getTime());
				
				if(!d1.equals(d2)){
					week = cal.get(Calendar.DAY_OF_WEEK);
					if (week == Calendar.SUNDAY) {
						daystr.append(";").append(cal.get(Calendar.WEEK_OF_YEAR));
					}
				}else{
					break;
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    String monthstr = "";
	    Iterator keyit = monthmap.keySet().iterator();
		while (keyit.hasNext()) {
			String key = (String)keyit.next();
			monthstr += ";" + key + "," + monthmap.get(key);
		}
		String[] retstr = {monthstr.substring(1),daystr.toString().substring(1)};
		return retstr;
	}
	
	/**
	 * 遍历两个日期间的天数
	 * @param d1
	 * @param d2(不包含D2)
	 * @return
	 */
	public static String[] getMonthStrBetween(String d1, String d2) {
		StringBuffer daystr = new StringBuffer();
		Map monthmap = new LinkedHashMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(d1));
			daystr.append(";").append(d1.substring(5, 7));
			int i = 0;
			int k = 0;
			String month = d1;
			while(i < 36){
				String tempmonth = d1.substring(0, 4);
				if(month.substring(0, 4).equals(tempmonth)){
					monthmap.put(month.substring(0, 4), ++k);
				}else{
					month = tempmonth;
					k = 0;
					monthmap.put(month.substring(0, 4), ++k);
				}
				cal.setTime(sdf.parse(d1));
				cal.add(Calendar.MONTH, 1);
				d1 = sdf.format(cal.getTime());
				
				if(!d1.equals(d2)){
					daystr.append(";").append(d1.substring(5, 7));
				}else{
					break;
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    String monthstr = "";
	    Iterator keyit = monthmap.keySet().iterator();
		while (keyit.hasNext()) {
			String key = (String)keyit.next();
			monthstr += ";" + key + "," + monthmap.get(key);
		}
		String[] retstr = {monthstr.substring(1),daystr.toString().substring(1)};
		return retstr;
	}
	
	public static String getYearmonth(String ads_date){
		if(StringHelper.isEmpty(ads_date))
			return "";
		String year = ads_date.substring(0, 4);
		String month = ads_date.substring(5, 7);
		return year+month;
	}
	
	public static String getdatename(String date){
		if(StringHelper.isEmpty(date))
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datename = "";
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date));
			int year = cal.get(Calendar.YEAR);
			datename += year + "年";
			int month = cal.get(Calendar.MONTH) + 1;
			datename += month + "月";
			int day = cal.get(Calendar.DAY_OF_MONTH);
			datename += day + "日";
			int week = cal.get(Calendar.DAY_OF_WEEK);
			datename += " 星期" + weekday[week-1];
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return datename;
	}
	
	/**
	 * 遍历两个日期间的日期，包含开始结束日期
	 * @param startdate
	 * @param enddate
	 * @return 所有日期数组
	 * @throws ParseException
	 */
	public static String[] getDayBetween(String startdate, String enddate, boolean onlyWorkDay) throws ParseException{
		List<String> s = getDayListBetween(startdate, enddate, onlyWorkDay);
		return s.toArray(new String[s.size()]);
	}
	
	/**
	 * @Title getDayListBetween
	 * @Description 遍历两个日期间的日期，包含开始结束日期
	 * @author Alpha
	 * @date 2018年9月7日 上午11:32:44
	 * @param startdate
	 * @param enddate
	 * @param onlyWorkDay 只有工作日
	 * @throws ParseException
	 * @return List<String> 所有日期List
	 * @throws
	 */
	public static List<String> getDayListBetween(String startdate, String enddate, boolean onlyWorkDay) throws ParseException{
		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = df.parse(startdate);
		startCalendar.setTime(startDate);
		startCalendar.add(Calendar.DAY_OF_MONTH, -1);
		Date endDate = df.parse(enddate);
		endCalendar.setTime(endDate);
		List<String> s = new ArrayList<String>();
		while(true){
			startCalendar.add(Calendar.DAY_OF_MONTH, 1);
			if(startCalendar.getTimeInMillis() <= endCalendar.getTimeInMillis()){
				String date = df.format(startCalendar.getTime());
				if(onlyWorkDay){//只有工作日
					if(!isWeekDay(date)){//不是周末
						s.add(df.format(startCalendar.getTime()));
					}
				}else{
					s.add(df.format(startCalendar.getTime()));
				}
			}else{
				break;
			}
		}
		return s;
	}
	
	/**
	 * 获取2个日期之间的月份
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws ParseException
	 */
	public static String[] getMonthBetween(String startdate, String enddate) throws ParseException{
		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		Date startDate = df.parse(startdate);
		startCalendar.setTime(startDate);
		startCalendar.add(Calendar.MONTH, -1);
		Date endDate = df.parse(enddate);
		endCalendar.setTime(endDate);
		List<String> s = new ArrayList<String>();
		while(true){
			startCalendar.add(Calendar.MONTH, 1);
			if(startCalendar.getTimeInMillis() <= endCalendar.getTimeInMillis()){
				String date = df.format(startCalendar.getTime());
				s.add(df.format(startCalendar.getTime()));
			}else{
				break;
			}
		}
		return s.toArray(new String[s.size()]);
	}
	
	/**
	 * 获取指定日期移动的月份
	 * @param date
	 * @param len
	 * @return
	 * @throws ParseException
	 */
	public static String getMoveMonth(String date, int len) throws ParseException{
		Calendar startCalendar = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		Date startDate = df.parse(date);
		startCalendar.setTime(startDate);
		startCalendar.add(Calendar.MONTH, len);
		return df.format(startCalendar.getTime());
	}
	
	public static void main(String[] args) {
		//System.out.println(DateHelper.getDateStrBetween("2012-06-14", "2012-10-14"));
		
		//System.out.println(DateHelper.getWeekStrBetween("2012-06-03", "2012-10-14"));
		
		//System.out.println(DateHelper.getMonthStrBetween("2011-06-03", "2012-10-03"));
		//System.out.println(15/7);
		
		System.out.println(DateHelper.getdatename("2014-03-05s"));
	}
}

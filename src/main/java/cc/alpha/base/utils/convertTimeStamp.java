package cc.alpha.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class convertTimeStamp {

	public static void main(String[] args) throws ParseException {
		//时间戳转化为Sting或Date

		 SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

		 Long time=new Long(445555555);

		 String d = format.format(time);

		 Date date=format.parse(d);

		 System.out.println("Format To String(Date):"+d);

		 System.out.println("Format To Date:"+date);

	}

}

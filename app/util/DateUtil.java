package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static final DateFormat defaultDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String format(long time,String pt){
		DateFormat df = new SimpleDateFormat(pt);
		return df.format(new Date(time));
	}
	
	public static String format(long time){
		return defaultDf.format(new Date(time));
	}
}

package org.diyfr.alertermoi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper
{

	public static final String DATE_FORMAT_WS = "yyyy-MM-dd kk:mm:ss";
	private static final SimpleDateFormat DATE_WS;
	static
	{
		DATE_WS = new SimpleDateFormat(DATE_FORMAT_WS, Locale.getDefault());
	}

	public static Date parse(String paramString)
	{
		synchronized (DATE_WS)
		{
			Date localDate;
			try
			{
				localDate = DATE_WS.parse(paramString);
				return localDate;
			}
			catch (ParseException e)
			{
				e.printStackTrace();
				return null;
			}

		}
	}
}

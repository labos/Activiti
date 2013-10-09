package org.sr.activiti.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	public String formatSimpleDate(Date aDate) {
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

		return dt.format(aDate);
	}

}

package com.prairiefarms;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Terms {

	private static final Calendar calendar = Calendar.getInstance();
	private static final SimpleDateFormat usaFormat = new SimpleDateFormat("MM/dd/yyyy");

	private int id;
	private String description;
	private int dueDays;
	private String type;
	private Date dueByDate;
	private String status;


	public int getID() {
		return id;
	}

	public void setID(int iD) {
		this.id = iD;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDueDays() {
		return dueDays;
	}

	public void setDueDays(int dueDays) {
		this.dueDays = dueDays;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getDueByDate() {
		return dueByDate;
	}

	public String getDueByDate_USA() {
		return dueByDate != null ? usaFormat.format(dueByDate) : "" ;
	}

	public void setDueByDate(Date deliveryDate) {
		calendar.setTime(deliveryDate);
		calendar.add(Calendar.DAY_OF_MONTH, dueDays);

		this.dueByDate = calendar.getTime();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

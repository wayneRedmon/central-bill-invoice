package com.prairiefarms;

import com.ibm.as400.access.*;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

public class Location {

	private int id;
	private String description;
	private String schema;
	private String hostLocal;
	private String hostURL;
	private String hostEmail;
	private String user;
	private String password;
	private String xlsPassword;
	private String dataArea;
	private String frequency;
	private Date invoiceDate;
	private String logo;
	private String emailCarbonCopy;
	private String emailBoxOutgoing;
	private String emailBoxSent;
	private String disclaimer;
	private String textFrequency;
	private String status;

	private static SimpleDateFormat usaFormat = new SimpleDateFormat("MM/dd/yyyy");
	private static SimpleDateFormat isoFormat = new SimpleDateFormat("yyyyMMdd");
	private static Properties configuration;
	private static AS400 system;
	private static QSYSObjectPathName path;
	private static CharacterDataArea data;
	private static Scanner scan;


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

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getHostLocal() {
		return hostLocal;
	}

	public void setHostLocal(String hostLocal) {
		this.hostLocal = hostLocal;
	}

	public String getHostURL() {
		return hostURL;
	}

	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}

	public String getHostEmail() {
		return hostEmail;
	}

	public void setHostEmail(String hostEmail) {
		this.hostEmail = hostEmail;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public String getInvoiceDate_USA() {
		return usaFormat.format(invoiceDate);
	}

	public String getInvoiceDate_ISO() {
		return isoFormat.format(invoiceDate);
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getLogo() {
		scan = new Scanner(description);

		if (scan.hasNext("Prairie")) {
			logo = "img/logo/Prairie Farms.png";
		} else if (scan.hasNext("Hiland") || scan.hasNext("Roberts")) {
			logo = "img/logo/Hiland.png";
		} else if (scan.hasNext("Belfonte")) {
			logo = "img/logo/Belfonte.png";
		}

		scan.close();

		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getXLSpassword() {
		return xlsPassword;
	}

	public void setXLSpassword(String xlsPassword) {
		this.xlsPassword = xlsPassword;
	}

	public String getEmailCarbonCopy() {
		return emailCarbonCopy;
	}

	public void setEmailCarbonCopy(String emailCarbonCopy) {
		this.emailCarbonCopy = emailCarbonCopy;
	}

	public String getEmailBoxOutgoing() {
		return emailBoxOutgoing;
	}

	public void setEmailBoxOutgoing(String emailBoxOutgoing) {
		this.emailBoxOutgoing = emailBoxOutgoing;
	}

	public String getEmailBoxSent() {
		return emailBoxSent;
	}

	public void setEmailBoxSent(String emailBoxSent) {
		this.emailBoxSent = emailBoxSent;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getTextFrequency() {
		return textFrequency;
	}

	public void setTextFrequency(String textFrequency) {
		this.textFrequency = textFrequency;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setProperty() {
		configuration = new Properties();

		try {
			configuration.load(new FileInputStream("prairiefarmsApplication.properties"));
			hostEmail = configuration.getProperty("emailServer");
			xlsPassword = configuration.getProperty("xlsxPassword");
			disclaimer = configuration.getProperty("disclaimer");

			hostURL = "jdbc:as400://" + hostLocal.trim() + "/" + schema.trim() + ";naming=system;errors=full;socket timeout=30000;thread used=false";
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void setPath(String documentType) {
		textFrequency = " ";

		if (frequency.equals("W")) {
			textFrequency = "Weekly";

		} else {
			textFrequency = "Monthly";
		}

		try {
			system = new AS400(getHostLocal());

			system.setUserId(user);
			system.setPassword(password);

			path = new QSYSObjectPathName(schema.trim(), "DSINFO", "DTAARA");
			data = new CharacterDataArea(system, path.getPath());

			description = data.read().substring(12, 37);

			path = new QSYSObjectPathName(schema.trim(), getDataArea().trim(), "DTAARA");
			data = new CharacterDataArea(system, path.getPath());

			emailCarbonCopy = data.read().substring(0, 50);

			emailBoxOutgoing = "mail/" + schema.trim() + "/" + documentType.trim() + "/" + textFrequency.trim() + "/";

			emailBoxSent = "mail/" + schema.trim() + "/" + documentType.trim() + "/" + textFrequency.trim() + "/sent/";

		} catch (PropertyVetoException e) {
			e.printStackTrace();

		} catch (AS400SecurityException e) {
			e.printStackTrace();

		} catch (ErrorCompletingRequestException e) {
			e.printStackTrace();

		} catch (IllegalObjectTypeException e) {
			e.printStackTrace();

		} catch (InterruptedException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} catch (ObjectDoesNotExistException e) {
			e.printStackTrace();
		}
	}

	public String getDataArea() {
		return dataArea;
	}

	public void setDataArea(String dataArea) {
		this.dataArea = dataArea;
	}

}

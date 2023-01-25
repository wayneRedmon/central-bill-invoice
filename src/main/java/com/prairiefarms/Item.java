package com.prairiefarms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Item {
	private static final String sql = "SELECT * FROM ds_PRD WHERE prd# = ? FETCH FIRST ROW ONLY";

	private Connection connection;
	private static PreparedStatement preparedStatement;
	private static ResultSet resultset;
	
	private int id;
	
	private String description;
	
	private String salesType;
	
	private long upcCode;
	private int quantity;
	private int unitsPerCase;
	
	private double price;
	private double cost;
	private String promotion;
	private double extension;
	
	private double points;
	private double weight;
	private double gallons;
	
	private int glAccount;
	private double glAmount;
	
	private String status;

	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void initDB(int productID) {
	    try {
	    	preparedStatement = connection.prepareStatement(sql);
	    	preparedStatement.setInt(1, productID);
	
	    	resultset = preparedStatement.executeQuery();
	    	if (resultset.next()) {
	    		id = resultset.getInt("PRD#");
	    		
	    		description = resultset.getString("PRDDESC");
	    		
	    		upcCode = resultset.getInt("PRDUPCCD");
	    		
	    		unitsPerCase = resultset.getInt("PRDUNTSCSE");
	    		
	    	}
	    	
	    } catch (SQLException e) {
	    	e.printStackTrace(System.err);
	    }
	    
	    finally {
	    	try {
				resultset.close();
		    	preparedStatement.close();
		    	
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	}
	
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

	public String getSalesType() {
		return salesType;
	}

	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}

	public long getUPCcode() {
		return upcCode;
	}
	
	public void setUPCcode(long upcCode) {
		this.upcCode = upcCode;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public int getUnitsPerCase() {
		return unitsPerCase;
	}
	
	public void setUnitsPerCase(int unitsPerCase) {
		this.unitsPerCase = unitsPerCase;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public double getCost() {
		return cost;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public String getPromotion() {
		return promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	public double getExtension() {
		return extension;
	}

	public void setExtension(double extension) {
		this.extension = extension;
	}

	public double getPoints() {
		return points;
	}
	
	public void setPoints(double points) {
		this.points = points;
	}

	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getGallons() {
		return gallons;
	}
	
	public void setGallons(double gallons) {
		this.gallons = gallons;
	}

	public int getGLaccount() {
		return glAccount;
	}
	
	public void setGLaccount(int glAccount) {
		this.glAccount = glAccount;
	}
	
	public double getGLamount() {
		return glAmount;
	}
	
	public void setGLamount(double glAmount) {
		this.glAmount = glAmount;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

}

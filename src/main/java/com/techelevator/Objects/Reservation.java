package com.techelevator.Objects;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.ParseException;

import java.time.LocalDate;

public class Reservation {
	
	private String startDate;
	private int numberOfDays;
	private int occupancy;
	private int reservationId;
	private int spaceId;
	private String reservedFor;
	private int venueId;
	private String spaceName;
	private String venueName;
	private double dailyRate;
	private boolean isAccessible;
	private int categoryId;
	private String categoryName;
	
	
	
	public double getDailyRate() {
		return dailyRate;
	}
	public void setDailyRate(double dailyRate) {
		this.dailyRate = dailyRate;
	}
	public boolean isAccessible() {
		return isAccessible;
	}
	public void setAccessible(String response) {
		if (response.equalsIgnoreCase("Y")) {
			this.isAccessible = true;
		} else if (response.equalsIgnoreCase("N")) {
			this.isAccessible = false;
		}
		
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getSpaceName() {
		return spaceName;
	}
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}
	public String getVenueName() {
		return venueName;
	}
	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}
	public int getNumberOfDays() {
		return numberOfDays;
	}
	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	public int getVenueId() {
		return venueId;
	}
	public void setVenueId(int venueId) {
		this.venueId = venueId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try{
		   //Setting the date to the given date
		   c.setTime(sdf.parse(this.startDate));
		}catch(ParseException e){
			e.printStackTrace();
		 }
		c.add(Calendar.DAY_OF_MONTH, this.numberOfDays);  
		
		String endDate = sdf.format(c.getTime());  
		
		return endDate;
	}
	
	
	public int getOccupancy() {
		return occupancy;
	}
	public void setOccupancy(int occupancy) {
		this.occupancy = occupancy;
	}
	public int getReservationId() {
		return reservationId;
	}
	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}
	public int getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(int spaceId) {
		this.spaceId = spaceId;
	}
	public String getReservedFor() {
		return reservedFor;
	}
	public void setReservedFor(String reservedFor) {
		this.reservedFor = reservedFor;
	}
	
}
	
	
package com.techelevator.Objects;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.time.LocalDate;

public class Space {
	private int id;
	private int venue_id;
	private String venue_name;
	private String name;
	private boolean is_accessible;
	private int open_from;
	private int open_to;
	private double daily_rate;
	private int max_occupancy;
	
	
	public String getOpenMonth() {
		if(this.open_from == 0) {
			return "";
		}
		return new DateFormatSymbols().getMonths()[this.open_from-1];
	}
	
public String getClosedMonth() {
	if(this.open_to == 0) {
		return "";
	}
	return new DateFormatSymbols().getMonths()[this.open_to-1];
	}
	
	public String getVenue_name() {
		return venue_name;
	}
	public void setVenue_name(String venue_name) {
		this.venue_name = venue_name;
	}
	public int getOpen_from() {
		return open_from;
	}
	public void setOpen_from(int open_from) {
		this.open_from = open_from;
	}
	public int getOpen_to() {
		return open_to;
	}
	public void setOpen_to(int open_to) {
		this.open_to = open_to;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getVenue_id() {
		return venue_id;
	}
	public void setVenue_id(int venue_id) {
		this.venue_id = venue_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getIs_accessible() {
		return is_accessible;
	}
	public void setIs_accessible(boolean is_accessible) {
		this.is_accessible = is_accessible;
	}
	
	public double getDaily_rate() {
		return daily_rate;
	}
	public void setDaily_rate(double daily_rate) {
		this.daily_rate = daily_rate;
	}
	public int getMax_occupancy() {
		return max_occupancy;
	}
	public void setMax_occupancy(int max_occupancy) {
		this.max_occupancy = max_occupancy;
	}
}

package com.techelevator.Objects;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Venue {
	private int id;
	private String name;
	private int city_id;
	private String description;
	private List<Category> categoryList;
	private String city;
	private String state;

	public String printCategories() {
		if (categoryList.isEmpty() || categoryList == null) {
			return "None";
		} else {
			String categories = "";
			
			for (Category c : categoryList) {
				categories = categories + c.getName() + ", ";
			}

			return categories;
		}

	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

}

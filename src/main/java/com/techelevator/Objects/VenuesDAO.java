package com.techelevator.Objects;

import java.util.List;
import java.util.Map;

public interface VenuesDAO {

	
	
	
	public List<Venue> listAllVenues();
	public List<Category> getCategories(int id);
	public List<Space> listVenueSpaces(int id);
	public List<Category> getAllCategories();
}


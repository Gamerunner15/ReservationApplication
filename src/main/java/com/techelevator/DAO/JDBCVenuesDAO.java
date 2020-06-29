package com.techelevator.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.Objects.Category;
import com.techelevator.Objects.Space;
import com.techelevator.Objects.Venue;
import com.techelevator.Objects.VenuesDAO;

public class JDBCVenuesDAO implements VenuesDAO {
	private BasicDataSource dataSource;
	private JdbcTemplate template;

	public JDBCVenuesDAO(DataSource dataSource) {
		template = new JdbcTemplate(dataSource);
	}

	public List<Venue> listAllVenues() {
		List<Venue> venueList = new ArrayList<Venue>();
		String sql = "SELECT * FROM venue";
		SqlRowSet venueRows = template.queryForRowSet(sql);
		while (venueRows.next()) {
			Venue venue = new Venue();
			venue.setId(venueRows.getInt("id"));
			venue.setCategoryList(this.getCategories(venueRows.getInt("id")));
			venue.setName(venueRows.getString("name"));
			venue.setCity_id(venueRows.getInt("city_id"));
			venue.setDescription(venueRows.getString("description"));
			venue.setCity(this.getCity(venueRows.getInt("id")));
			venue.setState(this.getState(venueRows.getInt("id")));
			venueList.add(venue);
		}
		
		List<Venue> finalVenueList = this.venuesAlphabeticalList(venueList);
		
		return finalVenueList;
	}

	public List<Category> getCategories(int id) {
		String sql = "SELECT * FROM category JOIN category_venue ON category.id = category_venue.category_id "
				+ "WHERE category_venue.venue_id = ?;";
		SqlRowSet categoryRow = template.queryForRowSet(sql, id);
		List<Category> categoryList = new ArrayList<Category>();
		while (categoryRow.next()) {
			Category category = new Category();
			category.setId(categoryRow.getInt("id"));
			category.setName(categoryRow.getString("name"));
			categoryList.add(category);
		}
		return categoryList;
	}
	
	
	public List<Category> getAllCategories() {
		String sql = "SELECT * FROM category ORDER BY id";
		SqlRowSet categoryRow = template.queryForRowSet(sql);
		List<Category> categoryList = new ArrayList<Category>();
		while (categoryRow.next()) {
			Category category = new Category();
			category.setId(categoryRow.getInt("id"));
			category.setName(categoryRow.getString("name"));
			categoryList.add(category);
		}
		return categoryList;
	}
	

	public String getCity(int id) {
		String sql = "SELECT city.name FROM city JOIN venue ON venue.city_id = city.id WHERE venue.id = ?;";
		SqlRowSet cityRows = template.queryForRowSet(sql, id);
		cityRows.next();
		String city = cityRows.getString("name");
		return city;
	}

	public String getState(int id) {
		String sql = "SELECT state.name FROM state JOIN city ON city.state_abbreviation = state.abbreviation JOIN venue ON venue.city_id = city.id WHERE venue.id = ?;";
		SqlRowSet stateRows = template.queryForRowSet(sql, id);
		stateRows.next();
		String state = stateRows.getString("name");
		return state;
	}

	

	@Override
	public List<Space> listVenueSpaces(int id) {
		List<Space> spaceList = new ArrayList();
		String sql = "SELECT id,venue_id, name, is_accessible, open_from, open_to, CAST(daily_rate AS decimal), max_occupancy FROM space WHERE venue_id = ? ";
		SqlRowSet spaceRows = template.queryForRowSet(sql, id);
		
		while(spaceRows.next()) {
			Space space = new Space();
			space.setId(spaceRows.getInt("id"));
			space.setDaily_rate(spaceRows.getDouble("daily_rate"));
			space.setIs_accessible(spaceRows.getBoolean("is_accessible"));
			space.setMax_occupancy(spaceRows.getInt("max_occupancy"));
			space.setName(spaceRows.getString("name"));
			space.setOpen_from(spaceRows.getInt("open_from"));
			space.setOpen_to(spaceRows.getInt("open_to"));
			space.setVenue_id(spaceRows.getInt("venue_id"));
			
			spaceList.add(space);
			
		}
		
		List<Space> finalSpaceList = this.spacesAlphabeticalList(spaceList);
		
		return finalSpaceList;
	}
	
	
	public List<Venue> venuesAlphabeticalList(List<Venue> venueList) {
		int count = venueList.size();
		
		for (int i = 0; i < count; i++) {
			
			for (int j = i + 1; j < count; j++) {
				if (venueList.get(i).getName().compareTo(venueList.get(j).getName()) > 0) {
                    Venue temp = venueList.get(i);
                    venueList.set(i, venueList.get(j));
                    venueList.set(j, temp);
                }
			}
			
		}
		
		return venueList;
	}
	
	public List<Space> spacesAlphabeticalList(List<Space> spaceList) {
		int count = spaceList.size();
		
		for (int i = 0; i < count; i++) {
			
			for (int j = i + 1; j < count; j++) {
				if (spaceList.get(i).getName().compareTo(spaceList.get(j).getName()) > 0) {
                    Space temp = spaceList.get(i);
                    spaceList.set(i, spaceList.get(j));
                    spaceList.set(j, temp);
                }
			}
			
		}
		
		return spaceList;
	}
	

}

package com.techelevator;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.DAO.JDBCVenuesDAO;
import com.techelevator.Objects.Category;
import com.techelevator.Objects.Space;
import com.techelevator.Objects.Venue;

public class JDBCVenueDAOTest {
	
	private static SingleConnectionDataSource dataSource;
	JDBCVenuesDAO instance;
	JdbcTemplate template;
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/excelsior-venues");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
	
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	
	@Before
	public void before() {
		instance = new JDBCVenuesDAO(dataSource);
		template = new JdbcTemplate(dataSource);
		
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void testListAllVenues() {
		int[] venueIds = this.insertVenues();
		int id1 = venueIds[0];
		int id2 = venueIds[1];
		int id3 = venueIds[2];
		List<Venue> venueList = instance.listAllVenues();
		for (Venue v : venueList) {
			if (v.getId() == id1) {
				Assert.assertEquals(id1, v.getId());
			}
			if (v.getId() == id2) {
				Assert.assertEquals(id2, v.getId());
			}
			if (v.getId() == id3) {
				Assert.assertEquals(id3, v.getId());
			}
		}
	}
	
	@Test
	public void testGetCategories() {
		int[] venueIds = this.insertVenues();
		
		String sql2 = "INSERT INTO category_venue (venue_id, category_id) VALUES (?, ?)";
		template.update(sql2, venueIds[0], 1);
		template.update(sql2, venueIds[1], 2);
		template.update(sql2, venueIds[2], 3);
		
		List<Category> categoryList1 = instance.getCategories(venueIds[0]);
		List<Category> categoryList2 = instance.getCategories(venueIds[1]);
		List<Category> categoryList3 = instance.getCategories(venueIds[2]);

				
		Assert.assertEquals(1, categoryList1.get(0).getId()); 
		Assert.assertEquals(2, categoryList2.get(0).getId()); 
		Assert.assertEquals(3, categoryList3.get(0).getId()); 
	}
	
	@Test
	public void testGetCity() {
		int[] venueIds = this.insertVenues();
		String cityName1 = instance.getCity(venueIds[0]);
		String cityName2 = instance.getCity(venueIds[1]);
		String cityName3 = instance.getCity(venueIds[2]);
		
		Assert.assertEquals("Bona", cityName1);
		Assert.assertEquals("Srulbury", cityName2);
		Assert.assertEquals("Yepford", cityName3);

	}
	
	@Test
	public void testGetState() {
		int[] venueIds = this.insertVenues();
		String stateName1 = instance.getState(venueIds[0]);
		String stateName2 = instance.getState(venueIds[1]);
		String stateName3 = instance.getState(venueIds[2]);
		
		Assert.assertEquals("Michigan", stateName1);
		Assert.assertEquals("Ohio", stateName2);
		Assert.assertEquals("Iowa", stateName3);

	}
	
	@Test
	public void testListVenueSpaces() {
		int[] venueIds = this.insertVenues();
		
		String sql = "INSERT INTO space (venue_id, name, is_accessible, daily_rate, max_occupancy) VALUES (?, ?, ?, CAST(? AS MONEY), ?) Returning id;";
		SqlRowSet row1 = template.queryForRowSet(sql, venueIds[0], "Shallow Shack", true, 1000, 100);
		SqlRowSet row2 = template.queryForRowSet(sql, venueIds[1], "Renegade Floor", true, 1000, 100);
		SqlRowSet row3 = template.queryForRowSet(sql, venueIds[2], "Chasm of Darkness", true, 1000, 100);
		row1.next();
		row2.next();
		row3.next();
		int id1 = row1.getInt("id");
		int id2 = row2.getInt("id");
		int id3 = row3.getInt("id");
		
		List<Space> spaceList1 = instance.listVenueSpaces(venueIds[0]);
		List<Space> spaceList2 = instance.listVenueSpaces(venueIds[1]);
		List<Space> spaceList3 = instance.listVenueSpaces(venueIds[2]);
		
		Assert.assertEquals("Shallow Shack", spaceList1.get(0).getName());
		Assert.assertEquals("Renegade Floor", spaceList2.get(0).getName());
		Assert.assertEquals("Chasm of Darkness", spaceList3.get(0).getName());
		
	}
	

	
	
	public int[] insertVenues() {
		String sql = "INSERT INTO venue (name, city_id, description) VALUES (?, ?, ?) Returning id";
		SqlRowSet row1 = template.queryForRowSet(sql, "Hooligan Express Way", 1, "The best place on earth for hooligans.");
		SqlRowSet row2 = template.queryForRowSet(sql, "Prancing Pony", 2, "Hobbits love this place.");
		SqlRowSet row3 = template.queryForRowSet(sql, "Green Dragon", 3, "The only ale for the brave and true.");
		row1.next();
		row2.next();
		row3.next();
		int id1 = row1.getInt("id");
		int id2 = row2.getInt("id");
		int id3 = row3.getInt("id");
		int[] idArray= new int[3];
		idArray[0] = id1;
		idArray[1] = id2;
		idArray[2] = id3;
		return idArray;
	}
	
	
	
	
}

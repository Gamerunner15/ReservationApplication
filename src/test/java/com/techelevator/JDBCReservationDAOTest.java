package com.techelevator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.DAO.JDBCReservationDAO;
import com.techelevator.DAO.JDBCVenuesDAO;
import com.techelevator.Objects.Reservation;
import com.techelevator.Objects.Space;



public class JDBCReservationDAOTest {
	
	private static SingleConnectionDataSource dataSource;
	JDBCReservationDAO instance;
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
		instance = new JDBCReservationDAO(dataSource);
		template = new JdbcTemplate(dataSource);
		
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void testGetAvailableReservations() {
		int[] venueIds = this.insertVenues();
		int[] spaceIds = this.insertSpaces(venueIds);
		List<Reservation> reservationList = this.generateReservations(venueIds, spaceIds);
		instance.createReservation(reservationList.get(0));
		instance.createReservation(reservationList.get(1));
		instance.createReservation(reservationList.get(2));
		//This one succeeds
		Reservation reservation1 = new Reservation();
		reservation1.setStartDate("2021-01-09");
		reservation1.setNumberOfDays(7);
		reservation1.setOccupancy(50);
		reservation1.setVenueId(venueIds[0]);
		reservation1.setSpaceId(spaceIds[0]);
		List<Space> spaceList1 = instance.getAvalibleReservations(reservation1);
		//This one fails
		Reservation reservation2 = new Reservation();
		reservation2.setStartDate("2021-01-04");
		reservation2.setNumberOfDays(7);
		reservation2.setOccupancy(50);
		reservation2.setVenueId(venueIds[0]);
		reservation2.setSpaceId(spaceIds[0]);
		List<Space> spaceList2 = instance.getAvalibleReservations(reservation2);
		
		Assert.assertEquals(spaceIds[0], spaceList1.get(0).getId());
		Assert.assertTrue(spaceList2.isEmpty());
		
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
	
	public int[] insertSpaces(int[] venueIds) {
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
		int[] idArray= new int[3];
		idArray[0] = id1;
		idArray[1] = id2;
		idArray[2] = id3;
		return idArray;
	}
	
	public List<Reservation> generateReservations(int[] venueIds, int[] spaceIds){
		List<Reservation> reservationList = new ArrayList<Reservation>();
		Reservation reservation1 = new Reservation();
		reservation1.setStartDate("2021-01-01");
		reservation1.setNumberOfDays(7);
		reservation1.setOccupancy(50);
		reservation1.setVenueId(venueIds[0]);
		reservation1.setSpaceId(spaceIds[0]);
		reservation1.setReservedFor("Kolton");
		
		Reservation reservation2 = new Reservation();
		reservation2.setStartDate("2021-01-17");
		reservation2.setNumberOfDays(7);
		reservation2.setOccupancy(50);
		reservation2.setVenueId(venueIds[0]);
		reservation2.setSpaceId(spaceIds[0]);
		reservation2.setReservedFor("Kolton");
		
		Reservation reservation3 = new Reservation();
		reservation3.setStartDate("2021-01-28");
		reservation3.setNumberOfDays(7);
		reservation3.setOccupancy(50);
		reservation3.setVenueId(venueIds[0]);
		reservation3.setSpaceId(spaceIds[0]);
		reservation3.setReservedFor("Kolton");
		
		reservationList.add(reservation1);
		reservationList.add(reservation2);
		reservationList.add(reservation3);

		return reservationList;
	}
	
	
}

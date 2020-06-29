package com.techelevator.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.Objects.Reservation;
import com.techelevator.Objects.ReservationDAO;
import com.techelevator.Objects.Space;

public class JDBCReservationDAO implements ReservationDAO {

	
	private JdbcTemplate template;

	public JDBCReservationDAO(DataSource dataSource) {
		template = new JdbcTemplate(dataSource);
	}
	
	
	

	public List<Space> getAvalibleReservations(Reservation reservation) {
		int venueId = reservation.getVenueId();
		int occupancy = reservation.getOccupancy();
		String startDate = reservation.getStartDate();
		String endDate = reservation.getEndDate();

		List<Space> spaceList = new ArrayList();
		String sql = "SELECT space.id, space.name, venue.name AS venue_name, CAST(space.daily_rate AS decimal), space.open_from, space.open_to, space.max_occupancy, space.is_accessible FROM space "
				+ "JOIN venue ON venue.id = space.venue_id WHERE venue_id = ? AND max_occupancy >= ? "
				+ "AND NOT EXISTS (SELECT * FROM reservation "
				+ "WHERE (CAST(? AS DATE) BETWEEN reservation.start_date AND reservation.end_date "
				+ "OR CAST(? AS DATE) BETWEEN reservation.start_date AND reservation.end_date) "
				+ "AND reservation.space_id = space.id) "
				+ "AND ((EXTRACT(MONTH from CAST(? AS DATE)) BETWEEN space.open_from AND space.open_to) OR space.open_from IS NULL AND space.open_to IS NULL) "
				+ "AND ((EXTRACT(MONTH from CAST(? AS DATE)) BETWEEN space.open_from AND space.open_to) OR space.open_from IS NULL AND space.open_to IS NULL) "
				+ "GROUP BY space.id, venue.name ORDER BY CAST(space.daily_rate AS decimal) ASC LIMIT 5;";
		SqlRowSet spaceRows = template.queryForRowSet(sql, venueId, occupancy, startDate, endDate, startDate, endDate);
		
		while (spaceRows.next()) {
			Space space = new Space();
			space.setId(spaceRows.getInt("id"));
			space.setDaily_rate(spaceRows.getDouble("daily_rate"));
			space.setIs_accessible(spaceRows.getBoolean("is_accessible"));
			space.setMax_occupancy(spaceRows.getInt("max_occupancy"));
			space.setName(spaceRows.getString("name"));
			space.setOpen_from(spaceRows.getInt("open_from"));
			space.setOpen_to(spaceRows.getInt("open_to"));
			space.setVenue_name(spaceRows.getString("venue_name"));

			spaceList.add(space);
			
		}
		return spaceList;
	}
	
	public int createReservation(Reservation reservation) {
		int space_id = reservation.getSpaceId();
		int number_of_attendees = reservation.getOccupancy();
		String start_date = reservation.getStartDate();
		String end_date = reservation.getEndDate();
		String reserved_for = reservation.getReservedFor();
		
		String sql = "INSERT INTO reservation (space_id, number_of_attendees, start_date, end_date, reserved_for) VALUES (?, ?, CAST(? AS DATE), CAST(? AS DATE), ?) Returning reservation_id;";
		SqlRowSet row = template.queryForRowSet(sql, space_id, number_of_attendees, start_date, end_date, reserved_for);
		row.next();
		int reservationId = row.getInt("reservation_id");
		
		return reservationId;
	}
	
	//Currently not functional (thanks $7)
	public List<Space> advancedReservationSearch(Reservation reservation) {
		
		int occupancy = reservation.getOccupancy();
		String startDate = reservation.getStartDate();
		String endDate = reservation.getEndDate();
		int categoryId = reservation.getCategoryId();
		String isAccessible = String.valueOf(reservation.isAccessible());
		String dailyRate = String.valueOf(reservation.getDailyRate());
		

		List<Space> spaceList = new ArrayList();
		String sql = "SELECT  venue.name AS venue_name, space.id, space.name, CAST(space.daily_rate AS decimal), space.open_from, space.open_to, space.max_occupancy, space.is_accessible, category.name FROM space " + 
				"JOIN venue ON venue.id = space.venue_id " + 
				"JOIN category_venue ON category_venue.venue_id = venue.id " + 
				"JOIN category ON category.id = category_venue.category_id " + 
				"WHERE max_occupancy >= ? " + 
				"AND category.id = ? " + 
				"AND (space.is_accessible = ? OR space.is_accessible = True) " + 
				"AND CAST(space.daily_rate AS decimal) <= CAST(? AS decimal) " + 
				"AND NOT EXISTS (SELECT reservation_id FROM reservation " + 
				"WHERE (CAST(? AS DATE) BETWEEN reservation.start_date AND reservation.end_date " + 
				"OR CAST(? AS DATE) BETWEEN reservation.start_date AND reservation.end_date) " + 
				"AND reservation.space_id = space.id) " + 
				"AND ((EXTRACT(MONTH from DATE ?) BETWEEN space.open_from AND space.open_to) OR space.open_from IS NULL AND space.open_to IS NULL) " + 
				"AND ((EXTRACT(MONTH from DATE ?) BETWEEN space.open_from AND space.open_to) OR space.open_from IS NULL AND space.open_to IS NULL) " + 
				"GROUP BY space.id, venue.name, venue.id, category.name ORDER BY venue.id, CAST(space.daily_rate AS decimal);";
		SqlRowSet spaceRows = template.queryForRowSet(sql, occupancy, categoryId, isAccessible, dailyRate, startDate, endDate, startDate, endDate);
		
		while (spaceRows.next()) {
			Space space = new Space();
			space.setId(spaceRows.getInt("id"));
			space.setDaily_rate(spaceRows.getDouble("daily_rate"));
			space.setIs_accessible(spaceRows.getBoolean("is_accessible"));
			space.setMax_occupancy(spaceRows.getInt("max_occupancy"));
			space.setName(spaceRows.getString("name"));
			space.setOpen_from(spaceRows.getInt("open_from"));
			space.setOpen_to(spaceRows.getInt("open_to"));
			space.setVenue_name(spaceRows.getString("venue_name"));

			spaceList.add(space);
			
		}
		return spaceList;
	}
	
	
	
	
	
		
}

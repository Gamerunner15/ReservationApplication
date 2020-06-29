package com.techelevator.Objects;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface ReservationDAO {
	
	public List<Space> getAvalibleReservations(Reservation reservation);
	public int createReservation(Reservation reservation);
	public List<Space> advancedReservationSearch(Reservation reservation);
}
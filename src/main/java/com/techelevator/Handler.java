package com.techelevator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.techelevator.DAO.JDBCReservationDAO;
import com.techelevator.DAO.JDBCVenuesDAO;
import com.techelevator.Objects.Category;
import com.techelevator.Objects.Reservation;
import com.techelevator.Objects.ReservationDAO;
import com.techelevator.Objects.Space;
import com.techelevator.Objects.Venue;
import com.techelevator.Objects.VenuesDAO;
import com.techelevator.view.Menu;

public class Handler {
	private VenuesDAO venuesDAO;
	private ReservationDAO reservationDAO;
	private Menu menu;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

	public Handler(VenuesDAO venuesDAO, ReservationDAO reservationDAO) {
		this.reservationDAO = reservationDAO;
		this.venuesDAO = venuesDAO;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	//-----------------------------Start Handler Methods------------------------
	
	//Called from Main Menu [2]
	public boolean mainMenuHandler(String response) {

		if (response.equals("1")) {

			List<Venue> alphabeticalVenues = venuesDAO.listAllVenues();
			menu.venuesMenu(alphabeticalVenues);
			return true;
		} else if (response.equals("2")) {
			Reservation reservation = menu.advancedSearchMenu();
			List<Space> spaceList = reservationDAO.advancedReservationSearch(reservation);
			menu.finalMenu(spaceList, reservation);
			return true;

		} else if (response.equalsIgnoreCase("Q")) {
			return false;
		} else {
			System.out.println("Please enter a valid response.");
			System.out.println("----------------------------------------------------");
			System.out.println("\n");
			return true;
		}

	}
	
	//Called from the venuesMenu [4]
	public Venue chooseVenue(int response) {
		Venue chosenVenue = new Venue();
		List<Venue> venuesList = venuesDAO.listAllVenues();
		for (int i = 1; i <= venuesList.size(); i++) {
			if (response == i) {
				chosenVenue = venuesList.get(i - 1);
			}
		}
		return chosenVenue;
	}

	//Called from the subMenu [6]
	public boolean subMenuHandler(String response, int id) {
		if (response.equals("1")) {
			List<Space> spaceList = venuesDAO.listVenueSpaces(id);
			menu.spacesMenu(spaceList);
			return true;
		} else if (response.equals("2")) {
			// -------------------------------------COME BACK
			System.out.println("In Production...");
			System.out.println("----------------------------------------------------");
			System.out.println("\n");
			return true;

		} else if (response.equalsIgnoreCase("R")) {
			return false;

		} else {
			System.out.println("Please enter a valid response.");
			System.out.println("----------------------------------------------------");
			System.out.println("\n");
			return true;
		}

	}

	
	//called from the spacesMenu [8]
	public boolean reserveASpace(String response) {

		if (response.equals("1")) {
			//Calls the reservationMenu
			Reservation reservation = menu.reservationMenu();
			//calls the finalMenu
			menu.finalMenu(reservationDAO.getAvalibleReservations(reservation), reservation);

			return false;
		} else if (response.equalsIgnoreCase("R")) {
			return false;

		} else {
			System.out.println("Please enter a valid response.");
			System.out.println("----------------------------------------------------");
			System.out.println("\n");
			return true;
		}

	}
	
	//Called from finalMenu [11]
	public Space chooseSpace(int response, Reservation reservation) {
		Space chosenSpace = new Space();
		List<Space> spacesList = reservationDAO.getAvalibleReservations(reservation);

		for (Space s : spacesList) {
			if (response == s.getId()) {
				chosenSpace = s;
			}
		}

		return chosenSpace;
	}
	
	//called from finalMenu [12]
	public int makeReservation(Reservation reservation) {
		int reservationId = reservationDAO.createReservation(reservation);
		return reservationId;
	}
	
	//Called from advancedSearchMenu [ALT4]
	public List<Category> getCategories() {
		return venuesDAO.getAllCategories();
	}
	
	//--------------------------------Methods to validate responses and avoid crashes---------------------
	
	public boolean chosenSpaceIsValid(int response, List<Space> spaceList) {
		boolean isValid = false;
		if (response == 0) {
			isValid = true;
		}
		for (Space s : spaceList) {
			if (s.getId() == response) {
				isValid = true;
			}
		}
		return isValid;
	}
	
	public boolean isDateValid(String date) {
		ZoneId z = ZoneId.of("America/Montreal");
		LocalDate today = LocalDate.now(z);

		try {
			LocalDate newDate = LocalDate.parse(date);

			if (newDate.isBefore(today)) {
				throw new Exception();
			}

		} catch (Exception e) {
			System.out.println("Please enter a valid date. Example: YYYY-MM-DD");
			return false;
		}
		return true;
	}


	public boolean responseIsYesOrNo(String response) {
		if (response.equalsIgnoreCase("Y") || response.equalsIgnoreCase("N")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean numberIsValid(String response) {
		try {
			int intResponse = Integer.valueOf(response);
			if (intResponse < 1) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("Please input a valid number.");
			System.out.println("----------------------------------------------------");
			return false;
		}

		return true;
	}
	
	public boolean isCategoryValid(String response, List<Category> categoryList) {
		boolean isValid = false;

		try {
			int intResponse = Integer.valueOf(response);

			for (Category c : categoryList) {
				if (c.getId() == intResponse) {
					isValid = true;
				}
			}
			if(!isValid) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("Please input a valid Category ID.");
			return false;
		}

		return true;

	}

}

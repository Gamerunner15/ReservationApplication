package com.techelevator.view;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;
import java.util.Scanner;

import com.techelevator.Handler;
import com.techelevator.Objects.Category;
import com.techelevator.Objects.Reservation;
import com.techelevator.Objects.Space;
import com.techelevator.Objects.Venue;

public class Menu {
	private DecimalFormat decimalFormat = new DecimalFormat("##.00");
	private Scanner userScanner = new Scanner(System.in);
	private Handler handler;
	private int venueId;

	public Menu(Handler handler) {
		this.handler = handler;
	}

	// MAIN MENU v  [1]
	public boolean mainMenu() {
		boolean run = true;
		System.out.println("What would you like to do?");
		System.out.println("1) List Venues");
		System.out.println("2) Advanced Search for Space");
		System.out.println("Q) Quit");
		String mainMenuResponse = userScanner.nextLine();
		// To the MainMenuHandler! v
		run = handler.mainMenuHandler(mainMenuResponse);

		return run;
	}
	
	//You land here if you select "1" on Main Menu [3]
	public void venuesMenu(List<Venue> alphabeticalVenues) {
		boolean responseIsValid = false;
		Venue chosenVenue = new Venue();
		String venuesResponse = "";
		while (!responseIsValid) {
			for (int i = 0; i < alphabeticalVenues.size(); i++) {
				int number = i + 1;
				System.out.println(number + ") " + alphabeticalVenues.get(i).getName());
			}
			System.out.println("R) Return to Main Menu");
			venuesResponse = userScanner.nextLine();
			if (venuesResponse.equalsIgnoreCase("R")) {
				return;
			}
			responseIsValid = handler.numberIsValid(venuesResponse);
			chosenVenue = handler.chooseVenue(Integer.valueOf(venuesResponse));
			if (chosenVenue.getName() == null) {
				System.out.println("Please select a valid Venue ID");
				System.out.println("----------------------------------------------------");
				responseIsValid = false;
			}
		}

		System.out.println("----------------------------------------------------");
		System.out.println(chosenVenue.getName());
		System.out.println("Location: " + chosenVenue.getCity() + ", " + chosenVenue.getState());
		System.out.println("Categories: " + chosenVenue.printCategories());
		System.out.println("\n");
		System.out.println(chosenVenue.getDescription());
		System.out.println("\n");
		System.out.println("----------------------------------------------------");
		this.venueId = chosenVenue.getId();
		this.subMenu(chosenVenue.getId());

	}
	
	//You land here after selecting a Venue in the venuesMenu [5]
	public void subMenu(int id) {
		boolean run = true;
		while (run) {
			System.out.println("What would you like to do next?");
			System.out.println("1) View Spaces");
			//This doesn't work v
			System.out.println("2) Search for Reservations");
			System.out.println("R) Return to Main Menu");
			String response = userScanner.nextLine();
			run = handler.subMenuHandler(response, id);

		}

	}
	
	//You land here after selecting "1" in the subMenu [7]
	public void spacesMenu(List<Space> list) {
		boolean run = true;
		System.out.println(String.format("%-8s %-30s %-8s %-8s %-12s %-12s", "Space#", "Name", "Open", "Closed",
				"Daily Rate", "Max Occupancy"));
		System.out
				.println("-------------------------------------------------------------------------------------------");
		while (run) {
			for (int i = 0; i < list.size(); i++) {
				int number = i + 1;

				System.out.println(String.format("%-8s %-30s %-8s %-8s %-12s %-12d", number + ") ",
						list.get(i).getName(), list.get(i).getOpenMonth(), list.get(i).getClosedMonth(),
						"$" + decimalFormat.format(list.get(i).getDaily_rate()), list.get(i).getMax_occupancy()));
			}
			System.out.println(
					"-------------------------------------------------------------------------------------------");
			System.out.println("What would you like to do next?");
			System.out.println("1) Reserve a space");
			System.out.println("R) Return to previous screen");
			String response = userScanner.nextLine();
			run = handler.reserveASpace(response);
		}

	}
	
	//You land here after selecting "1" in the spacesMenu [9] (called by reserveASpace method in handler)
	public Reservation reservationMenu() {
		Reservation reservation = new Reservation();
		// Venue ID
		reservation.setVenueId(venueId);

		// Start Date loop
		boolean dateIsValid = false;
		String startDate = "";
		while (!dateIsValid) {
			System.out.println("When do you need this space? (YYYY-MM-DD)");
			startDate = userScanner.nextLine();
			dateIsValid = handler.isDateValid(startDate);
		}

		// Number of Days Loop
		boolean daysIsValid = false;
		String daysResponse = "";
		while (!daysIsValid) {
			System.out.println("How many days will you need the space?");
			daysResponse = userScanner.nextLine();
			daysIsValid = handler.numberIsValid(daysResponse);
		}
		// Occupancy Loop
		boolean occupancyIsValid = false;
		String occupancyResponse = "";
		while (!occupancyIsValid) {
			System.out.println("How many people will be in attendance?");
			occupancyResponse = userScanner.nextLine();
			occupancyIsValid = handler.numberIsValid(occupancyResponse);
		}

		reservation.setStartDate(startDate);
		reservation.setNumberOfDays(Integer.valueOf(daysResponse));
		reservation.setOccupancy(Integer.valueOf(occupancyResponse));

		return reservation;
	}
	
	//You land here by pressing "2" on the Main Menu [ALT 3]-->[10]
	public Reservation advancedSearchMenu() {
		Reservation reservation = new Reservation();
		// Start Date loop
		boolean dateIsValid = false;
		String startDate = "";
		while (!dateIsValid) {
			System.out.println("When do you need this space? (YYYY-MM-DD)");
			startDate = userScanner.nextLine();
			dateIsValid = handler.isDateValid(startDate);
		}

		// Number of Days
		boolean daysIsValid = false;
		String daysResponse = "";
		while (!daysIsValid) {
			System.out.println("How many days will you need the space?");
			daysResponse = userScanner.nextLine();
			daysIsValid = handler.numberIsValid(daysResponse);
		}
		// Occupancy Question
		boolean occupancyIsValid = false;
		String occupancyResponse = "";
		while (!occupancyIsValid) {
			System.out.println("How many people will be in attendance?");
			occupancyResponse = userScanner.nextLine();
			occupancyIsValid = handler.numberIsValid(occupancyResponse);
		}
		// Accessibility Question
		boolean accessibleIsValid = false;
		String accessibleResponse = "";
		while (!accessibleIsValid) {
			System.out.println("Does this space require accessibility accomodations? (Y/N)");
			accessibleResponse = userScanner.nextLine();
			accessibleIsValid = handler.responseIsYesOrNo(accessibleResponse);
		}
		// Budget Question
		boolean budgetIsValid = false;
		String budgetResponse = "";
		while (!budgetIsValid) {
			System.out.println("What is your daily budget for the event?");
			budgetResponse = userScanner.nextLine();
			budgetIsValid = handler.numberIsValid(budgetResponse);
		}
		// Categories Question
		boolean categoryIsValid = false;
		String categoryResponse = "";
		while (!categoryIsValid) {
			System.out.println("Which of the categories would you like to include?");
			List<Category> categoryList = handler.getCategories();
			for (Category c : categoryList) {
				System.out.println(c.getId() + ") " + c.getName());
			}
			categoryResponse = userScanner.nextLine();
			categoryIsValid = handler.isCategoryValid(categoryResponse, categoryList);
		}

		reservation.setStartDate(startDate);
		reservation.setNumberOfDays(Integer.valueOf(daysResponse));
		reservation.setOccupancy(Integer.valueOf(occupancyResponse));
		reservation.setDailyRate(Double.valueOf(budgetResponse));
		reservation.setAccessible(accessibleResponse);
		reservation.setCategoryId(Integer.valueOf(categoryResponse));
		return reservation;
	}

	//Every User ends up here to finish their reservation. [10]
	public void finalMenu(List<Space> spaceList, Reservation reservation) {
		if (spaceList.isEmpty()) {
			System.out.println("Sorry! There are no results to display based on your criteria.");
			return;
		} else {
			System.out.println("These Spaces are available based on your criteria: ");
			System.out.println(" ");
			System.out.println(
					"-------------------------------------------------------------------------------------------");

			System.out.println(String.format("%-8s %-30s %-12s %-12s %-12s %-12s", "Space#", "Name", "Daily Rate",
					"Max Occup.", "Accessible?", "Total Cost"));
			System.out.println(
					"-------------------------------------------------------------------------------------------");
			String isAccessible;
			// Looping through and printing all available spaces based on their inputs
			for (Space s : spaceList) {
				// Setting True to Yes and False to No
				if (s.getIs_accessible() == true) {
					isAccessible = "yes";
				} else {
					isAccessible = "no";
				}

				double totalPrice = reservation.getNumberOfDays() * s.getDaily_rate();
				System.out.println(String.format("%-8d %-30s %-12s %-12d %-12s %-12s", s.getId(), s.getName(),
						"$" + decimalFormat.format(s.getDaily_rate()), s.getMax_occupancy(), isAccessible,
						"$" + decimalFormat.format(totalPrice)));
			}
		}
		// Check to see if they selected a valid space ID
		boolean spaceIsValid = false;
		int spaceResponse = 0;
		while (!spaceIsValid) {

			System.out.println("Which Space would you like to reserve? (Enter 0 to cancel)");
			spaceResponse = userScanner.nextInt();
			userScanner.nextLine();
			spaceIsValid = handler.chosenSpaceIsValid(spaceResponse, spaceList);
			if (!spaceIsValid) {
				System.out.println("Please enter a valid Space ID.");
			}
		}
		if (spaceResponse == 0) {
			return;
		}
		// Next time we ask them stuff
		System.out.println(" ");
		System.out.println("Who is this reservation for?");
		String nameResponse = userScanner.nextLine();
		reservation.setReservedFor(nameResponse);
		
		//Calls the chooseSpace method from handler v
		Space chosenSpace = handler.chooseSpace(spaceResponse, reservation);
		reservation.setSpaceId(chosenSpace.getId());
		//calls makeReservation method to insert into database
		int reservationId = handler.makeReservation(reservation);
		reservation.setReservationId(reservationId);
		double totalPrice = reservation.getNumberOfDays() * chosenSpace.getDaily_rate();

		System.out.println("Thank you for submitting your reservation! The details for your event are listed below:");
		System.out.println(" ");

		System.out.println(String.format("%20s %-30s", "Confirmation #:  ", reservation.getReservationId()));
		System.out.println(String.format("%20s %-30s", "Venue:  ", chosenSpace.getVenue_name()));
		System.out.println(String.format("%20s %-30s", "Space:  ", chosenSpace.getName()));
		System.out.println(String.format("%20s %-30s", "Reserved For:  ", reservation.getReservedFor()));
		System.out.println(String.format("%20s %-30s", "Attendees:  ", reservation.getOccupancy()));
		System.out.println(String.format("%20s %-30s", "Arrival Date:  ", reservation.getStartDate()));
		System.out.println(String.format("%20s %-30s", "Depart Date:  ", reservation.getEndDate()));
		System.out.println(String.format("%20s %-30s", "Total Cost:  ", "$" + totalPrice));
		System.out.println("----------------------------------------------------");

	}

}

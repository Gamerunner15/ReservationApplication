package com.techelevator;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.DAO.JDBCReservationDAO;
import com.techelevator.DAO.JDBCVenuesDAO;
import com.techelevator.Objects.ReservationDAO;
import com.techelevator.Objects.VenuesDAO;
import com.techelevator.view.Menu;

public class ExcelsiorCLI {
	VenuesDAO venuesDAO;
	ReservationDAO reservationDAO;
	Handler handler;
	Menu menu;
	

	public static void main(String[] args) {
		//Make Connection to Database
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/excelsior-venues");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		//Create instance of CLI and -run- to start the menu
		ExcelsiorCLI application = new ExcelsiorCLI();
		application.run(dataSource);
		
	}


	public void run(BasicDataSource dataSource) {
		//create instances of DAOs and Menu
		venuesDAO = new JDBCVenuesDAO(dataSource);
		reservationDAO = new JDBCReservationDAO(dataSource);
		handler = new Handler(venuesDAO, reservationDAO);
		menu = new Menu(handler);
		handler.setMenu(menu);
		//Looping through the menu
		boolean continueMenu = true;
		while(continueMenu) {
			continueMenu = menu.mainMenu();
		}
		

	}
}

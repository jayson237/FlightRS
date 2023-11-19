/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateful.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.SeatInventorySessionBeanRemote;
import entity.Employee;
import entity.Flight;
import entity.FlightReservation;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.Passenger;
import entity.SeatInventory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import javafx.util.Pair;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CabinClassType;
import static util.enumeration.CabinClassType.J;
import static util.enumeration.CabinClassType.Y;
import util.exception.FlightNotFoundException;

/**
 *
 * @author jayso
 */
public class SalesManagementModule {

    private Employee employee;
    private FlightReservationSessionBeanRemote flightReservationSessionBean;
    private SeatInventorySessionBeanRemote seatInventorySessionBean;
    private FlightSessionBeanRemote flightSessionBean;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private MainApp mainApp;

    public SalesManagementModule(MainApp mainApp, Employee employee, FlightReservationSessionBeanRemote flightReservationSessionBean, SeatInventorySessionBeanRemote seatInventorySessionBean, FlightSessionBeanRemote flightSessionBean) {
        this.mainApp = mainApp;
        this.employee = employee;
        this.flightReservationSessionBean = flightReservationSessionBean;
        this.seatInventorySessionBean = seatInventorySessionBean;
        this.flightSessionBean = flightSessionBean;
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public void menuSalesManagement() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Merlion FlightRS Sales Management Module ===\n");
        System.out.println("1. View Seats Inventory");
        System.out.println("2. View Flight Reservations");
        System.out.println("3. Log out");
        System.out.print("> ");
        int input = sc.nextInt();
        sc.nextLine();

        switch (input) {
            case 1:
                doViewSeatsInventory(sc);
                break;
            case 2:
                doViewReservations(sc);
                break;
            case 3:
                mainApp.doLogOut();
                break;
            default:
                System.out.println("Invalid input, please try again");
                menuSalesManagement();
                break;
        }
    }

    private void doViewSeatsInventory(Scanner sc) {
        try {
            System.out.println("=== View Seats Inventory ===");
            System.out.print("Enter Flight Number> ");
            String flightNum = sc.nextLine().trim();
            Flight flight = flightSessionBean.retrieveFlightByNumber(flightNum);
            if (flight.getFlightSchedulePlans().isEmpty()) {
                System.out.println("Error: The selected flight has no flight schedule plans associated with it\n");
                return;
            }
            System.out.println("Displaying all flight schedules for Flight " + flightNum + ": " + flight.getFlightRoute().getOriginAirport().getAirportCode() + " -> " + flight.getFlightRoute().getDestinationAirport().getAirportCode());
            System.out.printf("%25s%30s%20s\n", "Flight Schedule ID", "Departure Date Time", "Duration (HRS)");
            for (FlightSchedulePlan fsp : flight.getFlightSchedulePlans()) {
                for (FlightSchedule fs : fsp.getFlightSchedules()) {
                    System.out.printf("%25s%30s%20s\n", fs.getFlightScheduleId().toString(), fs.getDepartureDateTime().toString().substring(0, 19), String.valueOf(fs.getEstimatedDuration()));
                }
            }
            System.out.print("Select flight schedule (BY ID)>  ");
            Long chosenFlightScheduleId = sc.nextLong();
            sc.nextLine();

            FlightSchedule flightSchedule = null;
            for (FlightSchedulePlan fsp : flight.getFlightSchedulePlans()) {
                for (FlightSchedule fs : fsp.getFlightSchedules()) {
                    if (Objects.equals(fs.getFlightScheduleId(), chosenFlightScheduleId)) {
                        flightSchedule = fs;
                    }
                }
            }
            if (flightSchedule == null) {
                System.out.println("Error: Flight Schedule with ID " + chosenFlightScheduleId + " does not exist with flight " + flightNum + "\n");
                return;
            }

            int totalAvailSeats = 0;
            int totalReservedSeats = 0;
            int totalBalanceSeats = 0;
            for (SeatInventory seatInventory : flightSchedule.getSeatInventory()) {
                totalAvailSeats += seatInventory.getAvailableSeats();
                totalReservedSeats += seatInventory.getReservedSeats();
                totalBalanceSeats += seatInventory.getBalanceSeats();

                char[][] seats = seatInventory.getSeats();
                String cabinClassConfig = seatInventory.getCabin().getSeatingConfiguration();

                CabinClassType type = seatInventory.getCabin().getType();
                

                System.out.println("Cabin Class" + type);
                System.out.println("=============================");
                System.out.print("Row  ");
                int count = 0;
                int no = 0;
                for (int i = 0; i < cabinClassConfig.length(); i++) {
                    if (Character.isDigit(cabinClassConfig.charAt(i))) {
                        no += Integer.parseInt(String.valueOf(cabinClassConfig.charAt(i)));
                        while (count < no) {
                            System.out.print((char) ('A' + count) + "  ");
                            count++;
                        }
                    } else {
                        System.out.print("   ");
                    }
                }
                System.out.println();

                for (int j = 0; j < seats.length; j++) {
                    System.out.printf("%-5s", String.valueOf(j + 1));
                    int count2 = 0;
                    int no2 = 0;
                    for (int i = 0; i < cabinClassConfig.length(); i++) {
                        if (Character.isDigit(cabinClassConfig.charAt(i))) {
                            no2 += Integer.parseInt(String.valueOf(cabinClassConfig.charAt(i)));
                            while (count2 < no2) {
                                System.out.print(seats[j][count2] + "  ");
                                count2++;
                            }
                        } else {
                            System.out.print("   ");
                        }
                    }
                    System.out.println();
                }

                System.out.println("\nNumber of available seats: " + seatInventory.getAvailableSeats());
                System.out.println("Number of reserved seats: " + seatInventory.getReservedSeats());
                System.out.println("Number of balance seats: " + seatInventory.getBalanceSeats() + "\n");

            }

            System.out.println("=== Total ===   ");
            System.out.println("Number of available seats: " + totalAvailSeats);
            System.out.println("Number of reserved seats: " + totalReservedSeats);
            System.out.println("Number of balance seats: " + totalBalanceSeats);

        } catch (Exception ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }

    private void doViewReservations(Scanner sc) {
        try {
            System.out.println("=== View Flight Reservations ===");
            System.out.print("Enter Flight Number> ");
            String flightNum = sc.nextLine().trim().toUpperCase();
            Flight flight = flightSessionBean.retrieveFlightByNumber(flightNum);
            if (flight.getFlightSchedulePlans().isEmpty()) {
                System.out.println("Error: The selected flight has no flight schedule plans associated with it\n");
                return;
            }
            System.out.println("Displaying all flight schedules for Flight " + flightNum + ": " + flight.getFlightRoute().getOriginAirport().getAirportCode() + " -> " + flight.getFlightRoute().getDestinationAirport().getAirportCode());
            System.out.printf("%25s%30s%20s\n", "Flight Schedule ID", "Departure Date Time", "Duration (HRS)");
            for (FlightSchedulePlan fsp : flight.getFlightSchedulePlans()) {
                for (FlightSchedule fs : fsp.getFlightSchedules()) {
                    System.out.printf("%25s%30s%20s\n", fs.getFlightScheduleId().toString(), fs.getDepartureDateTime().toString().substring(0, 19), String.valueOf(fs.getEstimatedDuration()));
                }
            }
            System.out.print("Select flight schedule (BY ID)>  ");
            Long chosenFlightScheduleId = sc.nextLong();
            sc.nextLine();

            FlightSchedule flightSchedule = null;
            for (FlightSchedulePlan fsp : flight.getFlightSchedulePlans()) {
                for (FlightSchedule fs : fsp.getFlightSchedules()) {
                    if (Objects.equals(fs.getFlightScheduleId(), chosenFlightScheduleId)) {
                        flightSchedule = fs;
                    }
                }
            }
            if (flightSchedule == null) {
                System.out.println("Error: Flight Schedule with ID " + chosenFlightScheduleId + " does not exist with flight " + flightNum + "\n");
                return;
            }

            List<CabinClassType> cabinTypes = new ArrayList<>();
            for (FlightReservation reservations : flightSchedule.getFlightReservations()) {
                if (!cabinTypes.contains(reservations.getCabinClassType())) {
                    cabinTypes.add(reservations.getCabinClassType());
                }
            }

            List<List<Pair<Passenger, String>>> res = new ArrayList<>();
            for (int i = 0; i < cabinTypes.size(); i++) {
                res.add(new ArrayList<>());
                for (FlightReservation reservations : flightSchedule.getFlightReservations()) {
                    if (reservations.getCabinClassType() == cabinTypes.get(i)) {
                        String fareBasisCode = reservations.getFareBasisCode();
                        for (Passenger passenger : reservations.getPassengers()) {
                            res.get(i).add(new Pair<>(passenger, fareBasisCode));
                        }
                    }
                }
            }

            for (int i = 0; i < cabinTypes.size(); i++) {
                Collections.sort(res.get(i), (o1, o2)
                        -> o1.getKey().getSeatNumber().compareTo(o2.getKey().getSeatNumber())
                );
            }

            System.out.println("\nAll Reservations for Flight Schedule (ID: " + chosenFlightScheduleId + "\n");
            if (cabinTypes.isEmpty()) {
                System.out.println("No existing reservations for this flight schedule\n");
            }
            for (int i = 0; i < cabinTypes.size(); i++) {
                System.out.println("Cabin Class " + cabinTypes.get(i));
                System.out.println("==========================");
                System.out.println();
                for (Pair<Passenger, String> pair : res.get(i)) {
                    Passenger pass = pair.getKey();
                    String fareCode = pair.getValue();
                    System.out.println(pass.getFirstName() + " " + pass.getLastName() + ", Seat " + pass.getSeatNumber() + ", " + fareCode);
                }
                System.out.println();
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsreservationclient;

import ejb.session.stateful.FlightReservationSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;

import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.SeatInventorySessionBeanRemote;
import ejb.session.stateless.TransactionSessionBeanRemote;
import entity.Customer;
import entity.Fare;
import entity.FlightReservation;
import entity.FlightSchedule;
import entity.Passenger;
import entity.SeatInventory;
import entity.Transaction;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javafx.util.Pair;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CabinClassType;
import static util.enumeration.CabinClassType.J;
import util.exception.CabinClassNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.FlightNotFoundException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.InvalidLoginException;
import util.exception.TransactionNotFoundException;

/**
 *
 * @author jayso
 */
public class MainApp {

    private CustomerSessionBeanRemote customerSessionBean;
    private FlightSessionBeanRemote flightSessionBean;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBean;
    private SeatInventorySessionBeanRemote seatInventorySessionBean;
    private FlightReservationSessionBeanRemote flightReservationSessionBean;
    private TransactionSessionBeanRemote transactionSessionBean;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Customer customer;

    public MainApp(CustomerSessionBeanRemote customerSessionBean) {
        this.customerSessionBean = customerSessionBean;
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    public void run() throws CustomerNotFoundException, InvalidLoginException {

        while (true) {
            Scanner sc = new Scanner(System.in);
            if (customer == null) {
                System.out.println("*** Welcome to Merlion Flight Reservation System ***\n");
                System.out.println("1. Login");
                System.out.println("2. Sign up");
                System.out.println("3. Search Flight");
                System.out.print("> ");
                int input = sc.nextInt();
                sc.nextLine();

                switch (input) {
                    case 1:
                        doLogin(sc);
                        break;
                    case 2:
                        doSignUp(sc);
                        break;
                    case 3:
                        doSearchFlight(sc);
                        break;
                    default:
                        System.out.println("Invalid Input please try again");
                        run();
                        break;
                }
            } else {
                doReservationMenu(sc);

            }

        }
    }

    private void doLogin(Scanner sc) throws CustomerNotFoundException, InvalidLoginException {
        System.out.println("=== Merlion Flight RS :: Customer Login ===\n");
        System.out.print("email: ");
        String email = sc.nextLine().trim();

        try {
            Customer currCustomer = customerSessionBean.retrieveCustomerByEmail(email);
            System.out.print("password: ");
            String password = sc.nextLine();
            if (customerSessionBean.checkCustomerCredentials(email, password)) {
                this.customer = currCustomer;
                System.out.println("Welcome to Merlion Flight RS");
                System.out.println("You are logged in as " + customer.getFirstName() + "\n");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + "\n");
            doLogin(sc);
        }
    }

    private void doSignUp(Scanner sc) {
        Customer c = new Customer();
        System.out.println("=== Merlion Flight RS :: Customer Registration ===\n");
        System.out.print("Enter First Name> ");
        c.setFirstName(sc.nextLine().trim());
        System.out.print("Enter Last Name> ");
        c.setLastName(sc.nextLine().trim());
        System.out.print("Enter Email> ");
        c.setEmail(sc.nextLine().trim());
        System.out.print("Enter Mobile Number> ");
        c.setMobileNumber(sc.nextLine().trim());
        System.out.print("Enter address> ");
        c.setAddress(sc.nextLine().trim());
        System.out.print("Enter password> ");
        c.setPassword(sc.nextLine().trim());
        Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(c);
        if (constraintViolations.isEmpty()) {
            try {
                Customer newCustomer = customerSessionBean.registerCustomer(c);
                System.out.println("Customer " + newCustomer.getCustomerId() + " is registered successfully\n");
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForCustomer(constraintViolations);
        }
    }

    private void doSearchFlight(Scanner sc) {
        System.out.println("=== Merlion Flight RS :: Search Flight===\n");
        System.out.print("Enter trip type (1: One Way, 2: Round)> ");
        int type = sc.nextInt();
        while (true) {
            System.out.print("Enter Trip Type (1. One-Way 2. Round-Trip/Return)> ");
            type = sc.nextInt();
            sc.nextLine();
            if (type < 1 || type > 2) {
                System.out.println("Error: Invalid option\nPlease try again!");
            } else {
                break;
            }
        }
        System.out.print("Enter departure airport> ");
        String departureAirport = sc.nextLine().trim();

        System.out.print("Enter destination airport> ");
        String destinationAirport = sc.nextLine().trim();

        Date departureDate = null;
        while (departureDate != null) {
            System.out.print("Enter departure date (e.g., 6 Dec)> ");
            String userInputDate = sc.nextLine().trim();
            departureDate = parseDate(userInputDate);
        }

        System.out.print("Enter number of passengers> ");
        int numOfPassengers = sc.nextInt();

        int directOrConnect;
        while (true) {
            System.out.println("Any preference for direct flight or connecting flight? (1: Direct Flight, 2: Connecting Flight, 3: No)> ");
            directOrConnect = sc.nextInt();
            if (directOrConnect < 1 || directOrConnect > 3) {
                System.out.println("Error: Invalid option\nPlease try again!");
            } else {
                break;
            }
        }

        CabinClassType cabinClassPreference;
        while (true) {
            System.out.println("Any cabin class preference? (F: First Class, J: Business Class, W: Premium Economy Class, Y: Economy Class, 1: No)> ");
            String cabinClassPreferenceInput = sc.nextLine().trim();
            if (cabinClassPreferenceInput.equals("F")) {
                cabinClassPreference = CabinClassType.F;
                break;
            } else if (cabinClassPreferenceInput.equals("J")) {
                cabinClassPreference = CabinClassType.J;
                break;
            } else if (cabinClassPreferenceInput.equals("W")) {
                cabinClassPreference = CabinClassType.W;
                break;
            } else if (cabinClassPreferenceInput.equals("Y")) {
                cabinClassPreference = CabinClassType.Y;
                break;
            } else if (cabinClassPreferenceInput.equals(1)) {
                cabinClassPreference = null;
                break;
            } else {
                System.out.println("Error: Invalid option\nPlease try again!");
            }
        }

        if (directOrConnect == 1) {
            try {
                List<FlightSchedule> departureDateInput = flightScheduleSessionBean.searchFlightAll(departureAirport, destinationAirport, departureDate, cabinClassPreference);

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> oneDayBefore = flightScheduleSessionBean.searchFlightAll(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> twoDayBefore = flightScheduleSessionBean.searchFlightAll(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> threeDayBefore = flightScheduleSessionBean.searchFlightAll(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> oneDayAfter = flightScheduleSessionBean.searchFlightAll(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> twoDayAfter = flightScheduleSessionBean.searchFlightAll(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> threeDayAfter = flightScheduleSessionBean.searchFlightAll(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);

                System.out.println("=== Available Direct Flights === ");
                System.out.println("");
                System.out.println("=== Desired Input Date === ");
                flightScheduleInformation(departureDateInput, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 1 Day Before Desired Input Date ===");
                flightScheduleInformation(oneDayBefore, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 2 Day Before Desired Input Date ===");
                flightScheduleInformation(twoDayBefore, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 3 Day Before Desired Input Date ===");
                flightScheduleInformation(threeDayBefore, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 1 Day After Desired Input Date ===");
                flightScheduleInformation(oneDayAfter, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 2 Day After Desired Input Date ===");
                flightScheduleInformation(twoDayAfter, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 3 Day After Desired Input Date ===");
                flightScheduleInformation(threeDayAfter, cabinClassPreference, numOfPassengers);
            } catch (FlightNotFoundException ex) {
                System.out.print("There are no flights with your desired flight route\n");
                return;
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        if (directOrConnect == 2) {
            try {
                List<Pair<FlightSchedule, FlightSchedule>> departureDateInput1 = flightScheduleSessionBean.getConnectingFlightSchedules(departureAirport, destinationAirport, departureDate, cabinClassPreference);

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, -1);
                List<Pair<FlightSchedule, FlightSchedule>> oneDayBefore1 = flightScheduleSessionBean.getConnectingFlightSchedules(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<Pair<FlightSchedule, FlightSchedule>> twoDayBefore1 = flightScheduleSessionBean.getConnectingFlightSchedules(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<Pair<FlightSchedule, FlightSchedule>> threeDayBefore1 = flightScheduleSessionBean.getConnectingFlightSchedules(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, 1);
                List<Pair<FlightSchedule, FlightSchedule>> oneDayAfter1 = flightScheduleSessionBean.getConnectingFlightSchedules(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<Pair<FlightSchedule, FlightSchedule>> twoDayAfter1 = flightScheduleSessionBean.getConnectingFlightSchedules(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<Pair<FlightSchedule, FlightSchedule>> threeDayAfter1 = flightScheduleSessionBean.getConnectingFlightSchedules(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);

                System.out.println("=== Available Connecting Flights === ");
                System.out.println("");
                System.out.println("=== Desired Input Date === ");
                connectingFlightInformation(departureDateInput1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 1 Day Before Desired Input Date ===");
                connectingFlightInformation(oneDayBefore1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 2 Day Before Desired Input Date ===");
                connectingFlightInformation(twoDayBefore1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 3 Day Before Desired Input Date ===");
                connectingFlightInformation(threeDayBefore1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 1 Day After Desired Input Date ===");
                connectingFlightInformation(oneDayAfter1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 2 Day After Desired Input Date ===");
                connectingFlightInformation(twoDayAfter1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 3 Day After Desired Input Date ===");
                connectingFlightInformation(threeDayAfter1, cabinClassPreference, numOfPassengers);
            } catch (FlightNotFoundException ex) {
                System.out.print("There are no flights with your desired flight route\n");
                return;
            } catch (FlightScheduleNotFoundException | CabinClassNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }

        if (directOrConnect == 3) {
            boolean exit = false;
            boolean exit2 = false;
            try {
                List<FlightSchedule> departureDateInput = flightScheduleSessionBean.searchFlightAll(departureAirport, destinationAirport, departureDate, cabinClassPreference);

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> oneDayBefore = flightScheduleSessionBean.searchFlightAll(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> twoDayBefore = flightScheduleSessionBean.searchFlightAll(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> threeDayBefore = flightScheduleSessionBean.searchFlightAll(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> oneDayAfter = flightScheduleSessionBean.searchFlightAll(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> twoDayAfter = flightScheduleSessionBean.searchFlightAll(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> threeDayAfter = flightScheduleSessionBean.searchFlightAll(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);

                System.out.println("=== Available Direct Flights === ");
                System.out.println("");
                System.out.println("=== Desired Input Date === ");
                flightScheduleInformation(departureDateInput, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 1 Day Before Desired Input Date ===");
                flightScheduleInformation(oneDayBefore, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 2 Day Before Desired Input Date ===");
                flightScheduleInformation(twoDayBefore, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 3 Day Before Desired Input Date ===");
                flightScheduleInformation(threeDayBefore, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 1 Day After Desired Input Date ===");
                flightScheduleInformation(oneDayAfter, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 2 Day After Desired Input Date ===");
                flightScheduleInformation(twoDayAfter, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 3 Day After Desired Input Date ===");
                flightScheduleInformation(threeDayAfter, cabinClassPreference, numOfPassengers);
            } catch (FlightNotFoundException ex) {
                System.out.print("There are no flights with your desired flight route\n");
                exit = true;
            } catch (FlightScheduleNotFoundException | CabinClassNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
            }

            try {
                List<Pair<FlightSchedule, FlightSchedule>> departureDateInput1 = flightScheduleSessionBean.getConnectingFlightSchedules(departureAirport, destinationAirport, departureDate, cabinClassPreference);

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, -1);
                List<Pair<FlightSchedule, FlightSchedule>> oneDayBefore1 = flightScheduleSessionBean.getConnectingFlightSchedules(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<Pair<FlightSchedule, FlightSchedule>> twoDayBefore1 = flightScheduleSessionBean.getConnectingFlightSchedules(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<Pair<FlightSchedule, FlightSchedule>> threeDayBefore1 = flightScheduleSessionBean.getConnectingFlightSchedules(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, 1);
                List<Pair<FlightSchedule, FlightSchedule>> oneDayAfter1 = flightScheduleSessionBean.getConnectingFlightSchedules(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<Pair<FlightSchedule, FlightSchedule>> twoDayAfter1 = flightScheduleSessionBean.getConnectingFlightSchedules(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<Pair<FlightSchedule, FlightSchedule>> threeDayAfter1 = flightScheduleSessionBean.getConnectingFlightSchedules(departureAirport, destinationAirport, calendar.getTime(), cabinClassPreference);

                System.out.println("=== Available Connecting Flights === ");
                System.out.println("");
                System.out.println("=== Desired Input Date === ");
                connectingFlightInformation(departureDateInput1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 1 Day Before Desired Input Date ===");
                connectingFlightInformation(oneDayBefore1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 2 Day Before Desired Input Date ===");
                connectingFlightInformation(twoDayBefore1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 3 Day Before Desired Input Date ===");
                connectingFlightInformation(threeDayBefore1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 1 Day After Desired Input Date ===");
                connectingFlightInformation(oneDayAfter1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 2 Day After Desired Input Date ===");
                connectingFlightInformation(twoDayAfter1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 3 Day After Desired Input Date ===");
                connectingFlightInformation(threeDayAfter1, cabinClassPreference, numOfPassengers);
            } catch (FlightNotFoundException ex) {
                System.out.print("There are no flights with your desired flight route\n");
                exit = true;
            } catch (FlightScheduleNotFoundException | CabinClassNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
            if (exit && exit2) {
                return;
            }
        }

        System.out.println("");

        if (type == 2 && directOrConnect == 1) {
            Date returnDate = null;

            while (returnDate != null) {
                System.out.println("Enter return date (e.g., 13 Dec)> ");
                String userReturnDate = sc.nextLine().trim();
                returnDate = parseDate(userReturnDate);
            }

            try {
                List<FlightSchedule> departureDateInput = flightScheduleSessionBean.searchFlightAll(destinationAirport, departureAirport, returnDate, cabinClassPreference);

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(returnDate);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> oneDayBefore = flightScheduleSessionBean.searchFlightAll(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> twoDayBefore = flightScheduleSessionBean.searchFlightAll(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> threeDayBefore = flightScheduleSessionBean.searchFlightAll(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> oneDayAfter = flightScheduleSessionBean.searchFlightAll(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> twoDayAfter = flightScheduleSessionBean.searchFlightAll(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> threeDayAfter = flightScheduleSessionBean.searchFlightAll(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);

                System.out.println("=== Available Direct Return Flights === ");
                System.out.println("");
                System.out.println("=== Desired Input Date === ");
                flightScheduleInformation(departureDateInput, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 1 Day Before Desired Input Date ===");
                flightScheduleInformation(oneDayBefore, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 2 Day Before Desired Input Date ===");
                flightScheduleInformation(twoDayBefore, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 3 Day Before Desired Input Date ===");
                flightScheduleInformation(threeDayBefore, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 1 Day After Desired Input Date ===");
                flightScheduleInformation(oneDayAfter, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 2 Day After Desired Input Date ===");
                flightScheduleInformation(twoDayAfter, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 3 Day After Desired Input Date ===");
                flightScheduleInformation(threeDayAfter, cabinClassPreference, numOfPassengers);
            } catch (FlightNotFoundException ex) {
                System.out.print("There are no flights with your desired flight route\n");
                return;
            } catch (FlightScheduleNotFoundException | CabinClassNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }

        if (type == 2 && directOrConnect == 2) {
            Date returnDate = null;

            while (returnDate != null) {
                System.out.println("Enter return date (e.g., 13 Dec)> ");
                String userReturnDate = sc.nextLine().trim();
                returnDate = parseDate(userReturnDate);
            }

            try {
                List<Pair<FlightSchedule, FlightSchedule>> departureDateInput1 = flightScheduleSessionBean.getConnectingFlightSchedules(destinationAirport, departureAirport, departureDate, cabinClassPreference);

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, -1);
                List<Pair<FlightSchedule, FlightSchedule>> oneDayBefore1 = flightScheduleSessionBean.getConnectingFlightSchedules(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<Pair<FlightSchedule, FlightSchedule>> twoDayBefore1 = flightScheduleSessionBean.getConnectingFlightSchedules(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<Pair<FlightSchedule, FlightSchedule>> threeDayBefore1 = flightScheduleSessionBean.getConnectingFlightSchedules(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, 1);
                List<Pair<FlightSchedule, FlightSchedule>> oneDayAfter1 = flightScheduleSessionBean.getConnectingFlightSchedules(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<Pair<FlightSchedule, FlightSchedule>> twoDayAfter1 = flightScheduleSessionBean.getConnectingFlightSchedules(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<Pair<FlightSchedule, FlightSchedule>> threeDayAfter1 = flightScheduleSessionBean.getConnectingFlightSchedules(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);

                System.out.println("=== Available Connecting Return Flights === ");
                System.out.println("");
                System.out.println("=== Desired Input Date === ");
                connectingFlightInformation(departureDateInput1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 1 Day Before Desired Input Date ===");
                connectingFlightInformation(oneDayBefore1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 2 Day Before Desired Input Date ===");
                connectingFlightInformation(twoDayBefore1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 3 Day Before Desired Input Date ===");
                connectingFlightInformation(threeDayBefore1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 1 Day After Desired Input Date ===");
                connectingFlightInformation(oneDayAfter1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 2 Day After Desired Input Date ===");
                connectingFlightInformation(twoDayAfter1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 3 Day After Desired Input Date ===");
                connectingFlightInformation(threeDayAfter1, cabinClassPreference, numOfPassengers);
            } catch (FlightNotFoundException ex) {
                System.out.print("There are no flights with your desired flight route\n");
                return;
            } catch (FlightScheduleNotFoundException | CabinClassNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }

        if (type == 2 && directOrConnect == 3) {
            Date returnDate = null;

            while (returnDate != null) {
                System.out.println("Enter return date (e.g., 13 Dec)> ");
                String userReturnDate = sc.nextLine().trim();
                returnDate = parseDate(userReturnDate);
            }

            boolean exit = false;
            boolean exit2 = false;
            try {
                List<FlightSchedule> departureDateInput = flightScheduleSessionBean.searchFlightAll(destinationAirport, departureAirport, returnDate, cabinClassPreference);

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(returnDate);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> oneDayBefore = flightScheduleSessionBean.searchFlightAll(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> twoDayBefore = flightScheduleSessionBean.searchFlightAll(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> threeDayBefore = flightScheduleSessionBean.searchFlightAll(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> oneDayAfter = flightScheduleSessionBean.searchFlightAll(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> twoDayAfter = flightScheduleSessionBean.searchFlightAll(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> threeDayAfter = flightScheduleSessionBean.searchFlightAll(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);

                System.out.println("=== Available Direct Return Flights === ");
                System.out.println("");
                System.out.println("=== Desired Input Date === ");
                flightScheduleInformation(departureDateInput, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 1 Day Before Desired Input Date ===");
                flightScheduleInformation(oneDayBefore, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 2 Day Before Desired Input Date ===");
                flightScheduleInformation(twoDayBefore, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 3 Day Before Desired Input Date ===");
                flightScheduleInformation(threeDayBefore, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 1 Day After Desired Input Date ===");
                flightScheduleInformation(oneDayAfter, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 2 Day After Desired Input Date ===");
                flightScheduleInformation(twoDayAfter, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 3 Day After Desired Input Date ===");
                flightScheduleInformation(threeDayAfter, cabinClassPreference, numOfPassengers);
            } catch (FlightNotFoundException ex) {
                System.out.print("There are no flights with your desired flight route\n");
                exit = true;
            } catch (FlightScheduleNotFoundException | CabinClassNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
            }

            try {
                List<Pair<FlightSchedule, FlightSchedule>> departureDateInput1 = flightScheduleSessionBean.getConnectingFlightSchedules(destinationAirport, departureAirport, departureDate, cabinClassPreference);

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, -1);
                List<Pair<FlightSchedule, FlightSchedule>> oneDayBefore1 = flightScheduleSessionBean.getConnectingFlightSchedules(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<Pair<FlightSchedule, FlightSchedule>> twoDayBefore1 = flightScheduleSessionBean.getConnectingFlightSchedules(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<Pair<FlightSchedule, FlightSchedule>> threeDayBefore1 = flightScheduleSessionBean.getConnectingFlightSchedules(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, 1);
                List<Pair<FlightSchedule, FlightSchedule>> oneDayAfter1 = flightScheduleSessionBean.getConnectingFlightSchedules(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<Pair<FlightSchedule, FlightSchedule>> twoDayAfter1 = flightScheduleSessionBean.getConnectingFlightSchedules(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<Pair<FlightSchedule, FlightSchedule>> threeDayAfter1 = flightScheduleSessionBean.getConnectingFlightSchedules(destinationAirport, departureAirport, calendar.getTime(), cabinClassPreference);

                System.out.println("=== Available Connecting Return Flights === ");
                System.out.println("");
                System.out.println("=== Desired Input Date === ");
                connectingFlightInformation(departureDateInput1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 1 Day Before Desired Input Date ===");
                connectingFlightInformation(oneDayBefore1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 2 Day Before Desired Input Date ===");
                connectingFlightInformation(twoDayBefore1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 3 Day Before Desired Input Date ===");
                connectingFlightInformation(threeDayBefore1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 1 Day After Desired Input Date ===");
                connectingFlightInformation(oneDayAfter1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 2 Day After Desired Input Date ===");
                connectingFlightInformation(twoDayAfter1, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("=== Departing 3 Day After Desired Input Date ===");
                connectingFlightInformation(threeDayAfter1, cabinClassPreference, numOfPassengers);
            } catch (FlightNotFoundException ex) {
                System.out.print("There are no flights with your desired flight route\n");
                exit2 = true;
            } catch (FlightScheduleNotFoundException | CabinClassNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
            if (exit && exit2) {
                return;
            }
        }

        System.out.println("");

        System.out.println("Make a flight frervation? (Y/N)> ");
        String frponse = sc.nextLine().trim();

        if (frponse.equalsIgnoreCase("y") && customer == null) {
            try {
                doLogin(sc);
                System.out.println("*** Welcome to Merlion Airlines ***\n");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return;
            }
        } else if (frponse.equalsIgnoreCase("n")) {
            return;
        }

        Long directDepartureFlightId;
        Long connectDepartureFlightId;
        Long returnDepartureFlightId;
        Long returnConnectFlightId;
        if (type == 1 && directOrConnect == 1) {
            System.out.print("Enter flight id that you would like to frerve> ");
            directDepartureFlightId = sc.nextLong();
            connectDepartureFlightId = null;
            returnDepartureFlightId = null;
            returnConnectFlightId = null;
            sc.nextLine();
        } else if (type == 2 && directOrConnect == 1) {
            System.out.print("Enter the departure flight you would like to frerve> ");
            directDepartureFlightId = sc.nextLong();
            System.out.print("Enter the return flight you would like to frerve> ");
            returnDepartureFlightId = sc.nextLong();
            connectDepartureFlightId = null;
            returnConnectFlightId = null;
            sc.nextLine();
        } else if (type == 1 && directOrConnect == 2) {
            System.out.print("Enter the first flight id you would like to frerve> ");
            directDepartureFlightId = sc.nextLong();
            System.out.print("Enter the connecting flight id you would like to frerve> ");
            connectDepartureFlightId = sc.nextLong();
            returnDepartureFlightId = null;
            returnConnectFlightId = null;
            sc.nextLine();
        } else if (type == 2 && directOrConnect == 2) {
            System.out.print("Enter the first flight id you would like to frerve> ");
            directDepartureFlightId = sc.nextLong();
            System.out.print("Enter the connecting outbound flight you would like to frerve> ");
            connectDepartureFlightId = sc.nextLong();
            System.out.print("Enter the first return flight you would like to frerve> ");
            returnDepartureFlightId = sc.nextLong();
            System.out.print("Enter the connecting return flight you would like to frerve> ");
            returnConnectFlightId = sc.nextLong();
        } else if (directOrConnect == 0) {
            System.out.print("Enter type of flight you would like to frerve (1. Direct Flight, 2.Connecting Flight)> ");
            int input = sc.nextInt();
            sc.nextLine();
            if (type == 1 && input == 1) {
                System.out.print("Enter flight id that you would like to frerve> ");
                directDepartureFlightId = sc.nextLong();
                connectDepartureFlightId = null;
                returnDepartureFlightId = null;
                returnConnectFlightId = null;
                sc.nextLine();
            } else if (type == 2 && input == 1) {
                System.out.print("Enter the departure flight you would like to frerve> ");
                directDepartureFlightId = sc.nextLong();
                System.out.print("Enter the return flight you would like to frerve> ");
                returnDepartureFlightId = sc.nextLong();
                connectDepartureFlightId = null;
                returnConnectFlightId = null;
                sc.nextLine();
            } else if (type == 1 && input == 2) {
                System.out.print("Enter the first flight id you would like to frerve> ");
                directDepartureFlightId = sc.nextLong();
                System.out.print("Enter the connecting flight id you would like to frerve> ");
                connectDepartureFlightId = sc.nextLong();
                returnDepartureFlightId = null;
                returnConnectFlightId = null;
                sc.nextLine();
            } else if (type == 2 && input == 2) {
                System.out.print("Enter the first flight id you would like to frerve> ");
                directDepartureFlightId = sc.nextLong();
                System.out.print("Enter the connecting outbound flight you would like to frerve> ");
                connectDepartureFlightId = sc.nextLong();
                System.out.print("Enter the first return flight you would like to frerve> ");
                returnDepartureFlightId = sc.nextLong();
                System.out.print("Enter the connecting return flight you would like to frerve> ");
                returnConnectFlightId = sc.nextLong();
            } else {
                System.out.println("Error: Invalid option\nPlease try again!\n");
                return;
            }
        } else {
            return;
        }
        frerveFlight(directDepartureFlightId, connectDepartureFlightId, returnDepartureFlightId, returnConnectFlightId, cabinClassPreference, numOfPassengers);
    }

    private void frerveFlight(Long firstFlight, Long connectFlight, Long returnFlight, Long returnConnectFlight, CabinClassType cabinClassType, int numOfPassengers) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("*** Reserve Flight ***\n");

            FlightSchedule firstFlightSchedule;
            List<String> firstSeatSelection;
            Fare firstFare;
            SeatInventory firstSeats;
            FlightReservation firstReservation;

            FlightSchedule connectFlightSchedule;
            List<String> connectSeatSelection;
            Fare connectFare;
            SeatInventory connectSeats;
            FlightReservation connectReservation;

            FlightSchedule returnFirstFlightSchedule;
            List<String> returnFirstSeatSelection;
            Fare returnFirstFare;
            SeatInventory returnFirstSeats;
            FlightReservation returnFirstReservation;

            FlightSchedule returnConnectFlightSchedule;
            List<String> returnConnectSeatSelection;
            Fare returnConnectFare;
            SeatInventory returnConnectSeats;
            FlightReservation returnConnectReservation;

            BigDecimal pricePerPassenger;

            Transaction transaction = new Transaction();

            if (connectFlight == null && returnFlight == null && returnConnectFlight == null) {
                firstFlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(firstFlight);
                System.out.println("Select seat for your flight " + firstFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    firstSeats = getSeatInventory(firstFlightSchedule);
                } else {
                    firstSeats = flightScheduleSessionBean.getSeatInventory(firstFlightSchedule, cabinClassType);
                }
                firstFare = flightScheduleSessionBean.getSmallestFare(firstFlightSchedule, firstSeats.getCabin().getType());
                firstSeatSelection = bookSeats(firstSeats, numOfPassengers);

                firstReservation = new FlightReservation(firstFare.getFareCode(), firstFare.getAmount(), firstSeats.getCabin().getType());

                pricePerPassenger = firstFare.getAmount();
                System.out.println("Price per person : $" + pricePerPassenger.toString());
                System.out.println("Total Amount : $" + pricePerPassenger.multiply(new BigDecimal(numOfPassengers)));

                System.out.print("Enter Credit Card Number> ");
                String creditCardNum = sc.nextLine().trim();
                System.out.print("Enter cvv> ");
                String cvv = sc.nextLine().trim();
                transaction.setCcNumber(creditCardNum);
                transaction.setCvv(cvv);

                Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(transaction);
                if (constraintViolations.isEmpty()) {
                    transaction = transactionSessionBean.createNewTransaction(transaction, customer.getCustomerId());
                } else {
                    showInputDataValidationErrorsForTransaction(constraintViolations);
                    return;
                }

                List<Passenger> passengers = getPassengerInformation(numOfPassengers);

                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(firstSeatSelection.get(i));
                }
                flightReservationSessionBean.createNewReservation(firstReservation, passengers, firstFlightSchedule.getFlightScheduleId(), transaction.getTransactionId());

                System.out.println("Flight Reservation (Transaction Id: " + transaction.getTransactionId() + ") created successfully for customer " + customer.getCustomerId() + "!\n");

            } else if (connectFlight == null && returnConnectFlight == null) {
                firstFlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(firstFlight);
                System.out.println("Select seat for departure flight " + firstFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    firstSeats = getSeatInventory(firstFlightSchedule);
                } else {
                    firstSeats = flightScheduleSessionBean.getSeatInventory(firstFlightSchedule, cabinClassType);
                }
                firstFare = flightScheduleSessionBean.getSmallestFare(firstFlightSchedule, firstSeats.getCabin().getType());
                firstSeatSelection = bookSeats(firstSeats, numOfPassengers);
                firstReservation = new FlightReservation(firstFare.getFareCode(), firstFare.getAmount(), firstSeats.getCabin().getType());

                returnFirstFlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(returnFlight);
                System.out.println("Select seat for return flight " + returnFirstFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    returnFirstSeats = getSeatInventory(returnFirstFlightSchedule);
                } else {
                    returnFirstSeats = flightScheduleSessionBean.getSeatInventory(returnFirstFlightSchedule, cabinClassType);
                }
                returnFirstFare = flightScheduleSessionBean.getSmallestFare(returnFirstFlightSchedule, returnFirstSeats.getCabin().getType());
                returnFirstSeatSelection = bookSeats(returnFirstSeats, numOfPassengers);
                returnFirstReservation = new FlightReservation(returnFirstFare.getFareCode(), returnFirstFare.getAmount(),
                        returnFirstSeats.getCabin().getType());

                pricePerPassenger = firstFare.getAmount().add(returnFirstFare.getAmount());
                List<Passenger> passengers = getPassengerInformation(numOfPassengers);

                System.out.println("Price per person : $" + pricePerPassenger.toString());
                System.out.println("Total Amount : $" + pricePerPassenger.multiply(new BigDecimal(numOfPassengers)));

                System.out.print("Enter Credit Card Number> ");
                String creditCardNum = sc.nextLine().trim();
                System.out.print("Enter cvv> ");
                String cvv = sc.nextLine().trim();
                transaction.setCcNumber(creditCardNum);
                transaction.setCvv(cvv);

                Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(transaction);
                if (constraintViolations.isEmpty()) {
                    transaction = transactionSessionBean.createNewTransaction(transaction, customer.getCustomerId());
                } else {
                    showInputDataValidationErrorsForTransaction(constraintViolations);
                    return;
                }

                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(firstSeatSelection.get(i));
                }
                flightReservationSessionBean.createNewReservation(firstReservation, passengers, firstFlightSchedule.getFlightScheduleId(), transaction.getTransactionId());

                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(returnFirstSeatSelection.get(i));
                }
                flightReservationSessionBean.createNewReservation(returnFirstReservation, passengers, returnFirstFlightSchedule.getFlightScheduleId(), transaction.getTransactionId());

                System.out.println("Flight Reservation (Transaction Id: " + transaction.getTransactionId() + ") created successfully for customer " + customer.getCustomerId() + "!\n");

            } else if (returnFlight == null && returnConnectFlight == null) {
                firstFlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(firstFlight);
                System.out.println("Select seat for first departure flight " + firstFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    firstSeats = getSeatInventory(firstFlightSchedule);
                } else {
                    firstSeats = flightScheduleSessionBean.getSeatInventory(firstFlightSchedule, cabinClassType);
                }
                firstFare = flightScheduleSessionBean.getSmallestFare(firstFlightSchedule, firstSeats.getCabin().getType());
                firstSeatSelection = bookSeats(firstSeats, numOfPassengers);
                firstReservation = new FlightReservation(firstFare.getFareCode(), firstFare.getAmount(), firstSeats.getCabin().getType());

                connectFlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(connectFlight);
                System.out.println("Select seat for connecting flight " + connectFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    connectSeats = getSeatInventory(connectFlightSchedule);
                } else {
                    connectSeats = flightScheduleSessionBean.getSeatInventory(connectFlightSchedule, cabinClassType);
                }
                connectFare = flightScheduleSessionBean.getSmallestFare(connectFlightSchedule, connectSeats.getCabin().getType());
                connectSeatSelection = bookSeats(connectSeats, numOfPassengers);
                connectReservation = new FlightReservation(connectFare.getFareCode(), connectFare.getAmount(), connectSeats.getCabin().getType());

                pricePerPassenger = firstFare.getAmount().add(connectFare.getAmount());
                List<Passenger> passengers = getPassengerInformation(numOfPassengers);

                System.out.println("Price per person : $" + pricePerPassenger.toString());
                System.out.println("Total Amount : $" + pricePerPassenger.multiply(new BigDecimal(numOfPassengers)));

                System.out.print("Enter Credit Card Number> ");
                String creditCardNum = sc.nextLine().trim();
                System.out.print("Enter cvv> ");
                String cvv = sc.nextLine().trim();
                transaction.setCcNumber(creditCardNum);
                transaction.setCvv(cvv);

                Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(transaction);
                if (constraintViolations.isEmpty()) {
                    transaction = transactionSessionBean.createNewTransaction(transaction, customer.getCustomerId());
                } else {
                    showInputDataValidationErrorsForTransaction(constraintViolations);
                    return;
                }

                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(firstSeatSelection.get(i));
                }
                flightReservationSessionBean.createNewReservation(firstReservation, passengers, firstFlightSchedule.getFlightScheduleId(), transaction.getTransactionId());

                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(connectSeatSelection.get(i));
                }
                flightReservationSessionBean.createNewReservation(connectReservation, passengers, connectFlightSchedule.getFlightScheduleId(), transaction.getTransactionId());

                System.out.println("Flight Reservation (Transaction Id: " + transaction.getTransactionId() + ") created successfully for customer" + customer.getCustomerId() + "!\n");
            } else {
                firstFlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(firstFlight);
                System.out.println("Select seat for first departure flight " + firstFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    firstSeats = getSeatInventory(firstFlightSchedule);
                } else {
                    firstSeats = flightScheduleSessionBean.getSeatInventory(firstFlightSchedule, cabinClassType);
                }
                firstFare = flightScheduleSessionBean.getSmallestFare(firstFlightSchedule, firstSeats.getCabin().getType());
                firstSeatSelection = bookSeats(firstSeats, numOfPassengers);
                firstReservation = new FlightReservation(firstFare.getFareCode(), firstFare.getAmount(), firstSeats.getCabin().getType());

                connectFlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(connectFlight);
                System.out.println("Select seat for connecting flight " + connectFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    connectSeats = getSeatInventory(connectFlightSchedule);
                } else {
                    connectSeats = flightScheduleSessionBean.getSeatInventory(connectFlightSchedule, cabinClassType);
                }
                connectFare = flightScheduleSessionBean.getSmallestFare(connectFlightSchedule, connectSeats.getCabin().getType());
                connectSeatSelection = bookSeats(connectSeats, numOfPassengers);
                connectReservation = new FlightReservation(connectFare.getFareCode(), connectFare.getAmount(), connectSeats.getCabin().getType());

                returnFirstFlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(returnFlight);
                System.out.println("Select seat for first return flight " + returnFirstFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    returnFirstSeats = getSeatInventory(returnFirstFlightSchedule);
                } else {
                    returnFirstSeats = flightScheduleSessionBean.getSeatInventory(returnFirstFlightSchedule, cabinClassType);
                }
                returnFirstFare = flightScheduleSessionBean.getSmallestFare(returnFirstFlightSchedule, returnFirstSeats.getCabin().getType());
                returnFirstSeatSelection = bookSeats(returnFirstSeats, numOfPassengers);
                returnFirstReservation = new FlightReservation(returnFirstFare.getFareCode(), returnFirstFare.getAmount(), returnFirstSeats.getCabin().getType());

                returnConnectFlightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(returnConnectFlight);
                System.out.println("Select seat for connecting return flight " + returnConnectFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    returnConnectSeats = getSeatInventory(returnConnectFlightSchedule);
                } else {
                    returnConnectSeats = flightScheduleSessionBean.getSeatInventory(returnConnectFlightSchedule, cabinClassType);
                }
                returnConnectFare = flightScheduleSessionBean.getSmallestFare(returnConnectFlightSchedule, returnConnectSeats.getCabin().getType());
                returnConnectSeatSelection = bookSeats(returnConnectSeats, numOfPassengers);
                returnConnectReservation = new FlightReservation(returnConnectFare.getFareCode(), returnConnectFare.getAmount(), returnConnectSeats.getCabin().getType());

                pricePerPassenger = firstFare.getAmount().add(connectFare.getAmount()).add(returnFirstFare.getAmount()).add(returnConnectFare.getAmount());
                List<Passenger> passengers = getPassengerInformation(numOfPassengers);

                System.out.println("Price per person : $" + pricePerPassenger.toString());
                System.out.println("Total Amount : $" + pricePerPassenger.multiply(new BigDecimal(numOfPassengers)));

                System.out.print("Enter Credit Card Number> ");
                String creditCardNum = sc.nextLine().trim();
                System.out.print("Enter cvv> ");
                String cvv = sc.nextLine().trim();
                transaction.setCcNumber(creditCardNum);
                transaction.setCvv(cvv);

                Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(transaction);
                if (constraintViolations.isEmpty()) {
                    transaction = transactionSessionBean.createNewTransaction(transaction, customer.getCustomerId());
                } else {
                    showInputDataValidationErrorsForTransaction(constraintViolations);
                    return;
                }

                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(firstSeatSelection.get(i));
                }
                flightReservationSessionBean.createNewReservation(firstReservation, passengers, firstFlightSchedule.getFlightScheduleId(), transaction.getTransactionId());

                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(connectSeatSelection.get(i));
                }
                flightReservationSessionBean.createNewReservation(connectReservation, passengers, connectFlightSchedule.getFlightScheduleId(), transaction.getTransactionId());

                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(returnFirstSeatSelection.get(i));
                }
                flightReservationSessionBean.createNewReservation(returnFirstReservation, passengers, returnFirstFlightSchedule.getFlightScheduleId(), transaction.getTransactionId());

                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(returnConnectSeatSelection.get(i));
                }
                flightReservationSessionBean.createNewReservation(returnConnectReservation, passengers, returnConnectFlightSchedule.getFlightScheduleId(), transaction.getTransactionId());

                System.out.println("Flight Reservation (Transaction ID: " + transaction.getTransactionId() + ") created successfully for customer " + customer.getCustomerId() + "!\n");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
        }
    }

    private List<Passenger> getPassengerInformation(int numOfPassengers) {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Passenger Details ***\n");
        List<Passenger> passengers = new ArrayList<>();
        for (int i = 1; i <= numOfPassengers; i++) {
            System.out.print("Enter passenger " + (i) + " first name> ");
            String firstName = sc.nextLine().trim();
            System.out.print("Enter passenger " + (i) + " last name> ");
            String lastName = sc.nextLine().trim();
            System.out.print("Enter passenger " + (i) + " passport number> ");
            String passport = sc.nextLine().trim();
            passengers.add(new Passenger(firstName, lastName, passport, null));
        }
        return passengers;
    }

    private List<String> bookSeats(SeatInventory seatInventory, int numOfPassengers) {
        Scanner sc = new Scanner(System.in);
        int totalAvailSeats = seatInventory.getAvailableSeats();
        int totalReservedSeats = seatInventory.getReservedSeats();
        int totalBalanceSeats = seatInventory.getBalanceSeats();

        char[][] seats = seatInventory.getSeats();
        String cabinClassConfig = seatInventory.getCabin().getSeatingConfiguration();

        String type = "";
        if (null != seatInventory.getCabin().getType()) {
            switch (seatInventory.getCabin().getType()) {
                case F:
                    type = "First Class";
                    break;
                case J:
                    type = "Business Class";
                    break;
                case W:
                    type = "Premium Economy Class";
                    break;
                case Y:
                    type = "Economy Class";
                    break;
                default:
                    break;
            }
        }

        System.out.println("== " + type + " ==");
        System.out.print("Row  ");
        int count = 0;
        int num = 0;
        for (int i = 0; i < cabinClassConfig.length(); i++) {
            if (Character.isDigit(cabinClassConfig.charAt(i))) {
                num += Integer.parseInt(String.valueOf(cabinClassConfig.charAt(i)));
                while (count < num) {
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
            int num2 = 0;
            for (int i = 0; i < cabinClassConfig.length(); i++) {
                if (Character.isDigit(cabinClassConfig.charAt(i))) {
                    num2 += Integer.parseInt(String.valueOf(cabinClassConfig.charAt(i)));
                    while (count2 < num2) {
                        System.out.print(seats[j][count2] + "  ");
                        count2++;
                    }
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
        System.out.println("=== Total ===");
        System.out.println("Number of available seats: " + totalAvailSeats);
        System.out.println("Number of frerved seats: " + totalReservedSeats);
        System.out.println("Number of balance seats: " + totalBalanceSeats);

        List<String> seatSelection = new ArrayList<>();
        while (true) {
            for (int i = 0; i < numOfPassengers; i++) {
                String seatNumber;
                while (true) {
                    System.out.print("\nEnter seat to frerve for Passenger " + (i + 1) + "(Eg. A10)> ");
                    seatNumber = sc.nextLine().trim();
                    boolean booked = seatInventorySessionBean.isBooked(seatInventory, seatNumber);
                    if (booked) {
                        System.out.println("Seat is already taken!, Please choose another seat");
                    } else {
                        break;
                    }
                }
                seatSelection.add(seatNumber);
            }
            boolean distinct = seatSelection.stream().distinct().count() == seatSelection.size();
            if (distinct) {
                return seatSelection;
            } else {
                System.out.println("Duplicate seats detected!, Please try again");
            }
        }
    }

    private SeatInventory getSeatInventory(FlightSchedule flightSchedule) {
        Scanner sc = new Scanner(System.in);
        int i = 1;
        System.out.println("=== Available Cabin Classes ===");
        for (SeatInventory seat : flightSchedule.getSeatInventory()) {
            String cabinClass;
            if (seat.getCabin().getType() == CabinClassType.F) {
                cabinClass = "First Class";
            } else if (seat.getCabin().getType() == CabinClassType.J) {
                cabinClass = "Business Class";
            } else if (seat.getCabin().getType() == CabinClassType.W) {
                cabinClass = "Premium Economy Class";
            } else {
                cabinClass = "Economy Class";
            }

            System.out.println(i + ") " + cabinClass);
            i++;
        }
        while (true) {
            System.out.print("Select your cabin class> ");
            int frponse = sc.nextInt();
            sc.nextLine();
            if (frponse <= flightSchedule.getSeatInventory().size() && frponse >= 1) {
                return flightSchedule.getSeatInventory().get(frponse - 1);
            } else {
                System.out.println("Error: Invalid Option\n");
            }
        }

    }

    private void flightScheduleInformation(List<FlightSchedule> flightSchedules, CabinClassType cabinClassPreference, int passengers) throws CabinClassNotFoundException, FlightScheduleNotFoundException {

        for (FlightSchedule flightSchedule : flightSchedules) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(flightSchedule.getDepartureDateTime());
            double duration = flightSchedule.getEstimatedDuration();
            int hour = (int) duration;
            int min = (int) (duration % 1 * 60);
            calendar.add(Calendar.HOUR_OF_DAY, hour);
            calendar.add(Calendar.MINUTE, min);
            Date arrival = calendar.getTime();
            for (SeatInventory seat : flightSchedule.getSeatInventory()) {
                String cabinClassType;
                if (seat.getCabin().getType() == CabinClassType.F && (cabinClassPreference == CabinClassType.F || cabinClassPreference == null)) {
                    cabinClassType = "First Class";
                } else if (seat.getCabin().getType() == CabinClassType.J && (cabinClassPreference == CabinClassType.J || cabinClassPreference == null)) {
                    cabinClassType = "Business Class";
                } else if (seat.getCabin().getType() == CabinClassType.W && (cabinClassPreference == CabinClassType.W || cabinClassPreference == null)) {
                    cabinClassType = "Premium Economy Class";
                } else if (seat.getCabin().getType() == CabinClassType.Y && (cabinClassPreference == CabinClassType.Y || cabinClassPreference == null)) {
                    cabinClassType = "Economy Class";
                } else {
                    continue;
                }

                System.out.println("Flight ID: " + flightSchedule.getFlightScheduleId());
                System.out.println("Flight Number: " + flightSchedule.getFlightSchedulePlan().getFlightNumber());
                System.out.println("Origin Airport: " + flightSchedule.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getAirportName());
                System.out.println("Destination Airport: " + flightSchedule.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getAirportName());
                System.out.println("Departure Time: " + flightSchedule.getDepartureDateTime().toString().substring(0, 19));
                System.out.println("Flight Duration: " + flightSchedule.getEstimatedDuration());
                System.out.println("Arrival Time: " + flightSchedule.getArrivalDateTime().toString().substring(0, 19));
                System.out.println("Cabin Type: " + cabinClassType);
                System.out.println("Number of Balance Seats: " + seat.getBalanceSeats());
                System.out.println("Price Per Passenger: " + flightScheduleSessionBean.getSmallestFare(flightSchedule, seat.getCabin().getType()).getAmount());
                System.out.println("Total Price: " + flightScheduleSessionBean.getSmallestFare(flightSchedule, seat.getCabin().getType()).getAmount().multiply(BigDecimal.valueOf(passengers)));
                System.out.println("================================");
            }
        }
    }

    private void connectingFlightInformation(List<Pair<FlightSchedule, FlightSchedule>> flightSchedulePairs, CabinClassType cabin, int passengers) throws FlightScheduleNotFoundException, CabinClassNotFoundException {
        for (Pair<FlightSchedule, FlightSchedule> pair : flightSchedulePairs) {
            FlightSchedule flight1 = pair.getKey();
            FlightSchedule flight2 = pair.getValue();

            Calendar c2 = Calendar.getInstance();
            c2.setTime(flight1.getDepartureDateTime());
            double duration = flight1.getEstimatedDuration();
            int hour = (int) duration;
            int min = (int) (duration % 1 * 60);
            c2.add(Calendar.HOUR_OF_DAY, hour);
            c2.add(Calendar.MINUTE, min);
            Date arrival1 = c2.getTime();

            Calendar c3 = Calendar.getInstance();
            c3.setTime(flight2.getDepartureDateTime());
            double duration1 = flight1.getEstimatedDuration();
            int hour2 = (int) duration1;
            int min2 = (int) (duration1 % 1 * 60);
            c3.add(Calendar.HOUR_OF_DAY, hour2);
            c3.add(Calendar.MINUTE, min2);
            Date arrival2 = c3.getTime();
            for (SeatInventory seats1 : flight1.getSeatInventory()) {
                for (SeatInventory seats2 : flight2.getSeatInventory()) {
                    String cabinClassType1, cabinClassType2;
                    if (cabin == null) {
                        if (seats1.getCabin().getType() == CabinClassType.F) {
                            cabinClassType1 = "First Class";
                        } else if (seats1.getCabin().getType() == CabinClassType.J) {
                            cabinClassType1 = "Business Class";
                        } else if (seats1.getCabin().getType() == CabinClassType.W) {
                            cabinClassType1 = "Premium Economy Class";
                        } else {
                            cabinClassType1 = "Economy Class";
                        }
                        if (seats2.getCabin().getType() == CabinClassType.F) {
                            cabinClassType2 = "First Class";
                        } else if (seats2.getCabin().getType() == CabinClassType.J) {
                            cabinClassType2 = "Business Class";
                        } else if (seats2.getCabin().getType() == CabinClassType.W) {
                            cabinClassType2 = "Premium Economy Class";
                        } else {
                            cabinClassType2 = "Economy Class";
                        }
                    } else if (seats1.getCabin().getType() == CabinClassType.F && seats2.getCabin().getType() == CabinClassType.F && cabin == CabinClassType.F) {
                        cabinClassType1 = "First Class";
                        cabinClassType2 = "First Class";
                    } else if (seats1.getCabin().getType() == CabinClassType.J && seats2.getCabin().getType() == CabinClassType.J && cabin == CabinClassType.J) {
                        cabinClassType1 = "Business Class";
                        cabinClassType2 = "Business Class";
                    } else if (seats1.getCabin().getType() == CabinClassType.W && seats2.getCabin().getType() == CabinClassType.W && cabin == CabinClassType.W) {
                        cabinClassType1 = "Premium Economy Class";
                        cabinClassType2 = "Premium Economy Class";
                    } else if (seats1.getCabin().getType() == CabinClassType.Y && seats2.getCabin().getType() == CabinClassType.Y && cabin == CabinClassType.Y) {
                        cabinClassType1 = "Economy Class";
                        cabinClassType2 = "Economy Class";
                    } else {
                        continue;
                    }
                    System.out.printf("Flight Id: " + flight1.getFlightScheduleId());
                    System.out.println("Flight Number: " + flight1.getFlightSchedulePlan().getFlightNumber());
                    System.out.println("Origin Airport: " + flight1.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getAirportName());
                    System.out.println("Destination Airport: " + flight1.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getAirportName());
                    System.out.println("Departure Time: " + flight1.getDepartureDateTime().toString().substring(0, 19));
                    System.out.println("Flight Duration: " + flight1.getEstimatedDuration());

                    System.out.println("Arrival Time: " + flight1.getArrivalDateTime().toString().substring(0, 19));
                    System.out.println("Cabin Type: " + cabinClassType1);
                    System.out.println("Number of Balance Seats: " + seats1.getBalanceSeats());
                    System.out.println("Price Per Passenger: " + flightScheduleSessionBean.getSmallestFare(flight1, seats1.getCabin().getType()).getAmount());
                    System.out.println("Total Price: " + flightScheduleSessionBean.getSmallestFare(flight1, seats1.getCabin().getType()).getAmount().multiply(BigDecimal.valueOf(passengers)));
                    System.out.println("Connecting Flight Id: " + flight2.getFlightScheduleId());
                    System.out.println("Connecting Flight Number: " + flight2.getFlightSchedulePlan().getFlightNumber());
                    System.out.println("Connecting Origin Airport: " + flight2.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getAirportName());
                    System.out.println("Connecting Destination Airport: " + flight2.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getAirportName());
                    System.out.println("Connecting Departure Time: " + flight2.getDepartureDateTime().toString().substring(0, 19));
                    System.out.println("Connecting Estimated Duration: " + flight2.getEstimatedDuration());
                    System.out.println("Connecting Ariival: " + flight2.getArrivalDateTime().toString().substring(0, 19));
                    System.out.println("Connecting Cabin Type: " + cabinClassType2);
                    System.out.println("Connecting Number of Balanced Seats: " + seats2.getBalanceSeats());
                    System.out.println("Connecting Price Per Passenger: " + flightScheduleSessionBean.getSmallestFare(flight2, seats2.getCabin().
                            getType()).getAmount());
                    System.out.println("Connnecting Total Price: " + flightScheduleSessionBean.getSmallestFare(flight2, seats2.getCabin().getType()).getAmount().multiply(BigDecimal.valueOf(passengers)));
                    System.out.println("================================");

                }
            }
        }
    }

    private Date parseDate(String userInputDate) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("d MMM");

            return inputDateFormat.parse(userInputDate);
        } catch (Exception e) {
            System.out.println("Error. Please enter the date in the format 'd MMM'");
            return null;
        }
    }

    private void doViewMyReservations(Scanner sc) {
        System.out.println("=== View Flight Reservations ===\n");
        List<Transaction> transactionList = transactionSessionBean.retrieveTransactionsByCustomerId(customer.getCustomerId());
        for (Transaction transaction : transactionList) {
            System.out.println("Itinerary Reservation ID: " + transaction.getTransactionId());
            System.out.println("");
            for (FlightReservation fr : transaction.getFlightReservations()) {
                String journey = fr.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getAirportCode() + " -> " + fr.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getAirportCode();
                String departureDateTime = fr.getFlightSchedule().getDepartureDateTime().toString().substring(0, 19);
                String duration = String.valueOf(fr.getFlightSchedule().getEstimatedDuration()) + " Hours";
                String flightNum = fr.getFlightSchedule().getFlightSchedulePlan().getFlightNumber();
                System.out.println("\t" + flightNum + ", " + journey + ", " + departureDateTime + ", " + duration);
            }
            System.out.println();
        }
        System.out.print("Pfrs any key to continue...> ");
        sc.nextLine();
    }

    private void doViewMyFlightReservationDetails(Scanner sc) {
        try {
            System.out.println("*** View My Flight Reservations Details ***\n");

            System.out.print("Enter Reservation Id of frervation to view in detail> ");
            long id = sc.nextLong();
            sc.nextLine();
            System.out.println();
            Transaction transaction = transactionSessionBean.retrieveTransactionById(id);

            BigDecimal totalPaid = new BigDecimal(0);
            for (FlightReservation fr : transaction.getFlightReservations()) {
                totalPaid = totalPaid.add(fr.getTotalAmount().multiply(new BigDecimal(fr.getPassengers().size())));
                String journey = fr.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getAirportCode() + " -> " + fr.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getAirportCode();
                String departureDateTime = fr.getFlightSchedule().getDepartureDateTime().toString().substring(0, 19);
                String duration = String.valueOf(fr.getFlightSchedule().getEstimatedDuration()) + " Hours";
                String flightNum = fr.getFlightSchedule().getFlightSchedulePlan().getFlightNumber();
                String cabinClass;
                if (fr.getCabinClassType() == CabinClassType.F) {
                    cabinClass = "First Class";
                } else if (fr.getCabinClassType() == CabinClassType.J) {
                    cabinClass = "Business Class";
                } else if (fr.getCabinClassType() == CabinClassType.W) {
                    cabinClass = "Premium Economy Class";
                } else {
                    cabinClass = "Economy Class";
                }
                System.out.println("Flight: " + flightNum + ", " + journey + ", " + departureDateTime + ", " + duration);
                System.out.println();
                for (Passenger passenger : fr.getPassengers()) {
                    String name = passenger.getFirstName() + " " + passenger.getLastName();
                    String seatNumber = passenger.getSeatNumber();
                    System.out.println("\t" + name + ", " + cabinClass + ", Seat " + seatNumber);
                }
                System.out.println();
            }
            System.out.println("Total amount paid: $" + totalPaid.toString());
            System.out.print("Press any key to continue...> ");
            sc.nextLine();

        } catch (TransactionNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
        }
    }

    private void doReservationMenu(Scanner sc) {
        System.out.println("=== Merlion Flight RS :: Main Menu ===\n");
        System.out.println("1. Reserve Flight");
        System.out.println("2. View My Flight Reservations");
        System.out.println("3. View My Flight Reservation Details");
        System.out.println("4. Log out");
        System.out.print("> ");
        int input = sc.nextInt();
        sc.nextLine();

        switch (input) {
            case 1:
                doSearchFlight(sc);
                break;
            case 2:
                doViewMyReservations(sc);
                break;
            case 3:
                doViewMyFlightReservationDetails(sc);
                break;
            case 4:
                doLogout();
                break;
            default:
                System.out.println("Invalid Input please try again");
                doReservationMenu(sc);
                break;
        }
    }

    public void doLogout() {
        customer = null;
        System.out.println("You have exited. Goodbye!\n");
    }

    private void showInputDataValidationErrorsForCustomer(Set<ConstraintViolation<Customer>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

    private void showInputDataValidationErrorsForTransaction(Set<ConstraintViolation<Transaction>> constraintViolations) {
        System.out.println("\nInput data validation error!:");
        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }
        System.out.println("\nPlease try again......\n");
    }
}

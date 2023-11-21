/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package holidayreservationsystemclient;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author timothy
 */
public class MainApp {
    boolean login;
    Long currentPartner;

    public MainApp() {
    }

    public void runApp() {
        while (true) {
            if (!login) {
                Scanner sc = new Scanner(System.in);

                System.out.println(" === Welcome to Holiday Reservation System ===\n");
                System.out.println("*** Login ***");
                System.out.print("Enter username> ");
                String username = sc.nextLine().trim();
                System.out.print("Enter password> ");
                String password = sc.nextLine().trim();

                if (username.length() > 0 && password.length() > 0) {
                    try {
                        currentPartner = doLogin(username, password);
                        login = true;
                    } catch (InvalidLoginException_Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            } else {
                mainMenu();
            }
        }
    }

    private void mainMenu() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (login) {
            System.out.println("Welcome to Holiday Reservation System!\n");
            System.out.println("1: Search Flight");
            System.out.println("2: Reserve Flight");
            System.out.println("3: View My Flight Reservation");
            System.out.println("4: View My Flight Reservation Details");
            System.out.println("5: Log Out\n");

            response = 0;
            while (response < 1 || response > 5) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    doSearchFlight(sc);
                } else if (response == 2) {
                    doSearchFlight(sc);
                } else if (response == 3) {
                    doViewMyReservations(sc);
                } else if (response == 4) {
                    doViewMyFlightReservationDetails(sc);
                } else if (response == 5) {
                    doLogout();
                    System.out.println("Log out successful.\n");
                    break;
                } else {
                    System.out.println("Invalid Option, please try again!");
                }
            }
            if (response == 5) {
                break;
            }
        }
    }

    private void doLogout() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Confirm logout? (Y or N)> ");
        String reply = sc.nextLine().trim();

        if ((reply.equals("Y") || reply.equals("y")) && login) {
            currentPartner = null;
            login = false;
        }
    }
    
    private void doSearchFlight(Scanner sc) {
        System.out.println("=== Merlion Flight RS :: Search Flight===\n");
        SimpleDateFormat inputFormat = new SimpleDateFormat("d MMM yy");

        int type;
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

        Date departureDate;
        while (true) {
            try {
                System.out.print("Enter departure date (e.g., 6 Dec 23)> ");
                String date = sc.nextLine().trim();
                departureDate = inputFormat.parse(date);
                break;
            } catch (java.text.ParseException ex) {
                System.out.println("Error: Invalid date format\nPlease try again!");
            }
        }

        System.out.print("Enter number of passengers> ");
        int numOfPassengers = sc.nextInt();

        int directOrConnect;
        while (true) {
            System.out.print("Any preference for direct flight or connecting flight? (1: Direct Flight, 2: Connecting Flight, 3: No)> ");
            directOrConnect = sc.nextInt();
            sc.nextLine();
            if (directOrConnect < 1 || directOrConnect > 3) {
                System.out.println("Error: Invalid option\nPlease try again!");
            } else {
                break;
            }
        }

        CabinClassType cabinClassPreference;
        while (true) {
            System.out.print("Any cabin class preference? (1: First Class, 2: Business Class, 3: Premium Economy Class, 4: Economy Class, 5: No)> ");
            int cabinClassPreferenceInput = sc.nextInt();
            sc.nextLine();
            if (cabinClassPreferenceInput == 1) {
                cabinClassPreference = CabinClassType.F;
                break;
            } else if (cabinClassPreferenceInput == 2) {
                cabinClassPreference = CabinClassType.J;
                break;
            } else if (cabinClassPreferenceInput == 3) {
                cabinClassPreference = CabinClassType.W;
                break;
            } else if (cabinClassPreferenceInput == 4) {
                cabinClassPreference = CabinClassType.Y;
                break;
            } else if (cabinClassPreferenceInput == 5) {
                cabinClassPreference = null;
                break;
            } else {
                System.out.println("Error: Invalid option\nPlease try again!");
            }
        }

        if (directOrConnect == 1) {
            try {
                List<FlightSchedule> departureDateInput = getFlightSchedules(departureAirport, destinationAirport, departureDate.toString(), cabinClassPreference);

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> oneDayBefore = getFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> twoDayBefore = getFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> threeDayBefore = getFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> oneDayAfter = getFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> twoDayAfter = getFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> threeDayAfter = getFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);

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
            } catch (FlightNotFoundException_Exception | AirportNotFoundException_Exception ex) {
                System.out.print("There are no flights with your desired flight route\n");
                return;
            } catch (FlightScheduleNotFoundException_Exception | CabinClassNotFoundException_Exception | ParseException_Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        if (directOrConnect == 2) {
            try {
                List<MyPair> departureDateInput1 = getIndirectFlightSchedules(departureAirport, destinationAirport, departureDate.toString(), cabinClassPreference);

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, -1);
                List<MyPair> oneDayBefore1 = getIndirectFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<MyPair> twoDayBefore1 = getIndirectFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<MyPair> threeDayBefore1 = getIndirectFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, 1);
                List<MyPair> oneDayAfter1 = getIndirectFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<MyPair> twoDayAfter1 = getIndirectFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<MyPair> threeDayAfter1 = getIndirectFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);

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
            } catch (FlightNotFoundException_Exception ex) {
                System.out.print("There are no flights with your desired flight route\n");
                return;
            } catch (FlightScheduleNotFoundException_Exception | CabinClassNotFoundException_Exception | ParseException_Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }

        if (directOrConnect == 3) {

            boolean exit = false;
            boolean exit2 = false;
            try {
                List<FlightSchedule> departureDateInput = getFlightSchedules(departureAirport, destinationAirport, departureDate.toString(), cabinClassPreference);

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> oneDayBefore = getFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> twoDayBefore = getFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> threeDayBefore = getFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> oneDayAfter = getFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> twoDayAfter = getFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> threeDayAfter = getFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);

                System.out.println("=== Available Direct Flights === ");
                System.out.println("");
                System.out.println("");

                System.out.println("=== Desired Input Date === ");
                flightScheduleInformation(departureDateInput, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("");
                System.out.println("=== Departing 1 Day Before Desired Input Date ===");
                flightScheduleInformation(oneDayBefore, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("");
                System.out.println("=== Departing 2 Day Before Desired Input Date ===");
                flightScheduleInformation(twoDayBefore, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("");
                System.out.println("=== Departing 3 Day Before Desired Input Date ===");
                flightScheduleInformation(threeDayBefore, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("");
                System.out.println("=== Departing 1 Day After Desired Input Date ===");
                flightScheduleInformation(oneDayAfter, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("");
                System.out.println("=== Departing 2 Day After Desired Input Date ===");
                flightScheduleInformation(twoDayAfter, cabinClassPreference, numOfPassengers);

                System.out.println("");
                System.out.println("");
                System.out.println("=== Departing 3 Day After Desired Input Date ===");
                flightScheduleInformation(threeDayAfter, cabinClassPreference, numOfPassengers);
            } catch (FlightNotFoundException_Exception | AirportNotFoundException_Exception ex) {
                System.out.println("\nThere are no flights with your desired flight route\n");
                exit = true;
            } catch (FlightScheduleNotFoundException_Exception | CabinClassNotFoundException_Exception | ParseException_Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }

            try {
                List<MyPair> departureDateInput1 = getIndirectFlightSchedules(departureAirport, destinationAirport, departureDate.toString(), cabinClassPreference);

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, -1);
                List<MyPair> oneDayBefore1 = getIndirectFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<MyPair> twoDayBefore1 = getIndirectFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<MyPair> threeDayBefore1 = getIndirectFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);

                calendar.setTime(departureDate);
                calendar.add(Calendar.DATE, 1);
                List<MyPair> oneDayAfter1 = getIndirectFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<MyPair> twoDayAfter1 = getIndirectFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<MyPair> threeDayAfter1 = getIndirectFlightSchedules(departureAirport, destinationAirport, calendar.getTime().toString(), cabinClassPreference);

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
            } catch (FlightNotFoundException_Exception ex) {
                System.out.print("There are no flights with your desired flight route\n");
                exit = true;
            } catch (FlightScheduleNotFoundException_Exception | CabinClassNotFoundException_Exception | ParseException_Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
            if (exit && exit2) {
                return;
            }
        }

        System.out.println("");

        if (type == 2 && directOrConnect == 1) {
            Date returnDate;

            while (true) {
                try {
                    System.out.print("Enter return date (e.g., 13 Dec 23)> ");
                    String userReturnDate = sc.nextLine().trim();
                    returnDate = inputFormat.parse(userReturnDate);
                    break;
                } catch (java.text.ParseException ex) {
                    System.out.println("Invalid date format, Please try again");
                }

            }

            try {
                List<FlightSchedule> departureDateInput = getFlightSchedules(destinationAirport, departureAirport, returnDate.toString(), cabinClassPreference);

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(returnDate);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> oneDayBefore = getFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> twoDayBefore = getFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> threeDayBefore = getFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);

                calendar.setTime(returnDate);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> oneDayAfter = getFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> twoDayAfter = getFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> threeDayAfter = getFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);

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
            } catch (FlightNotFoundException_Exception | AirportNotFoundException_Exception ex) {
                System.out.print("There are no flights with your desired flight route\n");
                return;
            } catch (FlightScheduleNotFoundException_Exception | CabinClassNotFoundException_Exception | ParseException_Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }

        if (type == 2 && directOrConnect == 2) {
            Date returnDate;

            while (true) {
                try {
                    System.out.print("Enter return date (e.g., 13 Dec 23)> ");
                    String userReturnDate = sc.nextLine().trim();
                    returnDate = inputFormat.parse(userReturnDate);
                    break;
                } catch (java.text.ParseException ex) {
                    System.out.println("Invalid date format, Please try again\n");
                }

            }

            try {
                List<MyPair> departureDateInput1 = getIndirectFlightSchedules(destinationAirport, departureAirport, returnDate.toString(), cabinClassPreference);

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(returnDate);
                calendar.add(Calendar.DATE, -1);
                List<MyPair> oneDayBefore1 = getIndirectFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<MyPair> twoDayBefore1 = getIndirectFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<MyPair> threeDayBefore1 = getIndirectFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);

                calendar.setTime(returnDate);
                calendar.add(Calendar.DATE, 1);
                List<MyPair> oneDayAfter1 = getIndirectFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<MyPair> twoDayAfter1 = getIndirectFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<MyPair> threeDayAfter1 = getIndirectFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);

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
            } catch (FlightNotFoundException_Exception ex) {
                System.out.print("There are no flights with your desired flight route\n");
                return;
            } catch (FlightScheduleNotFoundException_Exception | CabinClassNotFoundException_Exception | ParseException_Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }

        if (type == 2 && directOrConnect == 3) {
            Date returnDate;

            while (true) {
                try {
                    System.out.print("Enter return date (e.g., 13 Dec 23)> ");
                    String userReturnDate = sc.nextLine().trim();
                    returnDate = inputFormat.parse(userReturnDate);
                    break;
                } catch (java.text.ParseException ex) {
                    System.out.println("Invalid date format, Please try again\n");
                }

            }

            boolean exit = false;
            boolean exit2 = false;
            try {
                List<FlightSchedule> departureDateInput = getFlightSchedules(destinationAirport, departureAirport, returnDate.toString(), cabinClassPreference);

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(returnDate);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> oneDayBefore = getFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> twoDayBefore = getFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<FlightSchedule> threeDayBefore = getFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);

                calendar.setTime(returnDate);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> oneDayAfter = getFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> twoDayAfter = getFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<FlightSchedule> threeDayAfter = getFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);

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
            } catch (FlightNotFoundException_Exception | AirportNotFoundException_Exception ex) {
                System.out.print("There are no flights with your desired flight route\n");
                return;
            } catch (FlightScheduleNotFoundException_Exception | CabinClassNotFoundException_Exception | ParseException_Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }

            try {
                List<MyPair> departureDateInput1 = getIndirectFlightSchedules(destinationAirport, departureAirport, returnDate.toString(), cabinClassPreference);
                Calendar calendar = Calendar.getInstance();

                calendar.setTime(returnDate);
                calendar.add(Calendar.DATE, -1);
                List<MyPair> oneDayBefore1 = getIndirectFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<MyPair> twoDayBefore1 = getIndirectFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, -1);
                List<MyPair> threeDayBefore1 = getIndirectFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);

                calendar.setTime(returnDate);
                calendar.add(Calendar.DATE, 1);
                List<MyPair> oneDayAfter1 = getIndirectFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<MyPair> twoDayAfter1 = getIndirectFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);
                calendar.add(Calendar.DATE, 1);
                List<MyPair> threeDayAfter1 = getIndirectFlightSchedules(destinationAirport, departureAirport, calendar.getTime().toString(), cabinClassPreference);

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
            } catch (FlightNotFoundException_Exception ex) {
                System.out.print("\nThere are no flights with your desired flight route or date\n");
                exit2 = true;
            } catch (FlightScheduleNotFoundException_Exception | CabinClassNotFoundException_Exception | ParseException_Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
            if (exit && exit2) {
                return;
            }
        }

        System.out.println("");

        System.out.print("Make a flight reservation? (Y/N)> ");
        String ans = sc.nextLine().trim();

        if (ans.equalsIgnoreCase("n")) {
            return;
        }

        Long directDepartureFlightId;
        Long connectDepartureFlightId;
        Long returnDepartureFlightId;
        Long returnConnectFlightId;
        if (type == 1 && directOrConnect == 1) {
            System.out.print("Enter flight id that you would like to reserve> ");
            directDepartureFlightId = sc.nextLong();
            connectDepartureFlightId = null;
            returnDepartureFlightId = null;
            returnConnectFlightId = null;
            sc.nextLine();
        } else if (type == 2 && directOrConnect == 1) {
            System.out.print("Enter the departure flight id you would like to reserve> ");
            directDepartureFlightId = sc.nextLong();
            System.out.print("Enter the return flight you would like to reserve> ");
            returnDepartureFlightId = sc.nextLong();
            connectDepartureFlightId = null;
            returnConnectFlightId = null;
            sc.nextLine();
        } else if (type == 1 && directOrConnect == 2) {
            System.out.print("Enter the first flight id you would like to reserve> ");
            directDepartureFlightId = sc.nextLong();
            System.out.print("Enter the connecting flight id you would like to reserve> ");
            connectDepartureFlightId = sc.nextLong();
            returnDepartureFlightId = null;
            returnConnectFlightId = null;
            sc.nextLine();
        } else if (type == 2 && directOrConnect == 2) {
            System.out.print("Enter the first flight id you would like to reserve> ");
            directDepartureFlightId = sc.nextLong();
            System.out.print("Enter the connecting flight id you would like to reserve> ");
            connectDepartureFlightId = sc.nextLong();
            System.out.print("Enter the first return flight id you would like to reserve> ");
            returnDepartureFlightId = sc.nextLong();
            System.out.print("Enter the connecting return flight id you would like to reserve> ");
            returnConnectFlightId = sc.nextLong();
        } else if (directOrConnect == 3) {
            System.out.print("Enter type of flight you would like to reserve (1. Direct Flight, 2.Connecting Flight)> ");
            int input = sc.nextInt();
            sc.nextLine();
            if (type == 1 && input == 1) {
                System.out.print("Enter flight id that you would like to reserve> ");
                directDepartureFlightId = sc.nextLong();
                connectDepartureFlightId = null;
                returnDepartureFlightId = null;
                returnConnectFlightId = null;
                sc.nextLine();
            } else if (type == 2 && input == 1) {
                System.out.print("Enter the departure flight id you would like to reserve> ");
                directDepartureFlightId = sc.nextLong();
                System.out.print("Enter the return flight id you would like to reserve> ");
                returnDepartureFlightId = sc.nextLong();
                connectDepartureFlightId = null;
                returnConnectFlightId = null;
                sc.nextLine();
            } else if (type == 1 && input == 2) {
                System.out.print("Enter the first flight id you would like to reserve> ");
                directDepartureFlightId = sc.nextLong();
                System.out.print("Enter the connecting flight id you would like to reserve> ");
                connectDepartureFlightId = sc.nextLong();
                returnDepartureFlightId = null;
                returnConnectFlightId = null;
                sc.nextLine();
            } else if (type == 2 && input == 2) {
                System.out.print("Enter the first flight id you would like to reserve> ");
                directDepartureFlightId = sc.nextLong();
                System.out.print("Enter the connecting flight id you would like to reserve> ");
                connectDepartureFlightId = sc.nextLong();
                System.out.print("Enter the first return flight id you would like to reserve> ");
                returnDepartureFlightId = sc.nextLong();
                System.out.print("Enter the connecting return flight id you would like to reserve> ");
                returnConnectFlightId = sc.nextLong();
            } else {
                System.out.println("Error: Invalid option\nPlease try again!\n");
                return;
            }
        } else {
            return;
        }
        reserveFlight(directDepartureFlightId, connectDepartureFlightId, returnDepartureFlightId, returnConnectFlightId, cabinClassPreference, numOfPassengers);

        doReservationMenu(sc);
    }
    
    private void reserveFlight(Long firstFlight, Long connectFlight, Long returnFlight, Long returnConnectFlight, CabinClassType cabinClassType, int numOfPassengers) {
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
            SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");

            if (connectFlight == null && returnFlight == null && returnConnectFlight == null) {
                firstFlightSchedule = retrieveFlightScheduleById(firstFlight);
                System.out.println("Select seat for your flight " + firstFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    firstSeats = getSeatInventory(firstFlightSchedule);
                } else {
                    firstSeats = getSeatInventory(firstFlightSchedule, cabinClassType);
                }
                firstFare = getBiggestFare(firstFlightSchedule, firstSeats.getCabin().getType());
                firstSeatSelection = bookSeats(firstSeats, numOfPassengers);

                firstReservation = new FlightReservation();
                firstReservation.setCabinClassType(firstSeats.getCabin().getType());       
                firstReservation.setFareBasisCode(firstFare.getFareCode());
                firstReservation.setTotalAmount(firstFare.getAmount());

                pricePerPassenger = firstFare.getAmount();
                System.out.println("Price per person : $" + pricePerPassenger.toString());
                System.out.println("Total Amount : $" + pricePerPassenger.multiply(new BigDecimal(numOfPassengers)));

                System.out.print("Enter Credit Card Number> ");
                String creditCardNum = sc.nextLine().trim();
                System.out.print("Enter cvv> ");
                String cvv = sc.nextLine().trim();
                System.out.print("Enter credit card expiry date (e.g., 23 Dec 2025)>");
                String expiryDate = sc.nextLine().trim();
                Date ccExpiryDate = sdf.parse(expiryDate);
                transaction.setCcNumber(creditCardNum);
                transaction.setCvv(cvv);
                XMLGregorianCalendar xmlGregorianCalendar = convertDateToXMLGregorianCalendar(ccExpiryDate);
                transaction.setCcExpiryDate(xmlGregorianCalendar);

                Long customerId = createNewTransaction(creditCardNum, cvv, ccExpiryDate, currentPartner);
                    
                List<Passenger> passengers = getPassengerInformation(numOfPassengers);

                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(firstSeatSelection.get(i));
                }
                createNewReservation(firstReservation, passengers, firstFlightSchedule.getFlightScheduleId(), transaction.getTransactionId(), customerId);

                System.out.println("Flight Reservation (Transaction Id: " + transaction.getTransactionId() + ") created successfully");

            } else if (connectFlight == null && returnConnectFlight == null) {
                firstFlightSchedule = retrieveFlightScheduleById(firstFlight);
                System.out.println("Select seat for departure flight " + firstFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    firstSeats = getSeatInventory(firstFlightSchedule);
                } else {
                    firstSeats = getSeatInventory(firstFlightSchedule, cabinClassType);
                }
                firstFare = getBiggestFare(firstFlightSchedule, firstSeats.getCabin().getType());
                firstSeatSelection = bookSeats(firstSeats, numOfPassengers);
                firstReservation = new FlightReservation();
                firstReservation.setCabinClassType(firstSeats.getCabin().getType());       
                firstReservation.setFareBasisCode(firstFare.getFareCode());
                firstReservation.setTotalAmount(firstFare.getAmount());
                returnFirstFlightSchedule = retrieveFlightScheduleById(returnFlight);
                System.out.println("Select seat for return flight " + returnFirstFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    returnFirstSeats = getSeatInventory(returnFirstFlightSchedule);
                } else {
                    returnFirstSeats = getSeatInventory(returnFirstFlightSchedule, cabinClassType);
                }
                returnFirstFare = getBiggestFare(returnFirstFlightSchedule, returnFirstSeats.getCabin().getType());
                returnFirstSeatSelection = bookSeats(returnFirstSeats, numOfPassengers);
                returnFirstReservation = new FlightReservation();
                returnFirstReservation.setCabinClassType(returnFirstSeats.getCabin().getType());       
                returnFirstReservation.setFareBasisCode(returnFirstFare.getFareCode());
                returnFirstReservation.setTotalAmount(returnFirstFare.getAmount());

                pricePerPassenger = firstFare.getAmount().add(returnFirstFare.getAmount());
                List<Passenger> passengers = getPassengerInformation(numOfPassengers);

                System.out.println("Price per person : $" + pricePerPassenger.toString());
                System.out.println("Total Amount : $" + pricePerPassenger.multiply(new BigDecimal(numOfPassengers)));

                System.out.print("Enter Credit Card Number> ");
                String creditCardNum = sc.nextLine().trim();
                System.out.print("Enter cvv> ");
                String cvv = sc.nextLine().trim();
                System.out.print("Enter credit card expiry date (e.g., 23 Dec 2025)>");
                String expiryDate = sc.nextLine().trim();
                Date ccExpiryDate = sdf.parse(expiryDate);
                transaction.setCcNumber(creditCardNum);
                transaction.setCvv(cvv);
                XMLGregorianCalendar xmlGregorianCalendar = convertDateToXMLGregorianCalendar(ccExpiryDate);

                transaction.setCcExpiryDate(xmlGregorianCalendar);

               
                Long customerId = createNewTransaction(creditCardNum, cvv, ccExpiryDate, currentPartner);
                

                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(firstSeatSelection.get(i));
                }
                long f = createNewReservation(firstReservation, passengers, firstFlightSchedule.getFlightScheduleId(), transaction.getTransactionId(), customerId);
                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(returnFirstSeatSelection.get(i));
                }
                long returnf = createNewReservation(returnFirstReservation, passengers, returnFirstFlightSchedule.getFlightScheduleId(), transaction.getTransactionId(), customerId);
                System.out.println("Flight Reservation (Transaction Id: " + transaction.getTransactionId() + ") created successfully");

            } else if (returnFlight == null && returnConnectFlight == null) {
                firstFlightSchedule = retrieveFlightScheduleById(firstFlight);
                System.out.println("Select seat for first departure flight " + firstFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    firstSeats = getSeatInventory(firstFlightSchedule);
                } else {
                    firstSeats = getSeatInventory(firstFlightSchedule, cabinClassType);
                }
                firstFare = getBiggestFare(firstFlightSchedule, firstSeats.getCabin().getType());
                firstSeatSelection = bookSeats(firstSeats, numOfPassengers);
                firstReservation = new FlightReservation();
                firstReservation.setCabinClassType(firstSeats.getCabin().getType());       
                firstReservation.setFareBasisCode(firstFare.getFareCode());
                firstReservation.setTotalAmount(firstFare.getAmount());

                connectFlightSchedule = retrieveFlightScheduleById(connectFlight);
                System.out.println("Select seat for connecting flight " + connectFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    connectSeats = getSeatInventory(connectFlightSchedule);
                } else {
                    connectSeats = getSeatInventory(connectFlightSchedule, cabinClassType);
                }
                connectFare = getBiggestFare(connectFlightSchedule, connectSeats.getCabin().getType());
                connectSeatSelection = bookSeats(connectSeats, numOfPassengers);
                connectReservation = new FlightReservation();
                connectReservation.setCabinClassType(connectSeats.getCabin().getType());       
                connectReservation.setFareBasisCode(connectFare.getFareCode());
                connectReservation.setTotalAmount(connectFare.getAmount());

                pricePerPassenger = firstFare.getAmount().add(connectFare.getAmount());
                List<Passenger> passengers = getPassengerInformation(numOfPassengers);

                System.out.println("Price per person : $" + pricePerPassenger.toString());
                System.out.println("Total Amount : $" + pricePerPassenger.multiply(new BigDecimal(numOfPassengers)));

                System.out.print("Enter Credit Card Number> ");
                String creditCardNum = sc.nextLine().trim();
                System.out.print("Enter cvv> ");
                String cvv = sc.nextLine().trim();
                System.out.print("Enter credit card expiry date (e.g., 23 Dec 2025)>");
                String expiryDate = sc.nextLine().trim();
                Date ccExpiryDate = sdf.parse(expiryDate);
                transaction.setCcNumber(creditCardNum);
                transaction.setCvv(cvv);
                XMLGregorianCalendar xmlGregorianCalendar = convertDateToXMLGregorianCalendar(ccExpiryDate);
                transaction.setCcExpiryDate(xmlGregorianCalendar);

               
                Long customerId = createNewTransaction(creditCardNum, cvv, ccExpiryDate, currentPartner);
                

                
                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(firstSeatSelection.get(i));
                }
                createNewReservation(firstReservation, passengers, firstFlightSchedule.getFlightScheduleId(), transaction.getTransactionId(), customerId);

                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(connectSeatSelection.get(i));
                }
                createNewReservation(connectReservation, passengers, connectFlightSchedule.getFlightScheduleId(), transaction.getTransactionId(), customerId);

                System.out.println("Flight Reservation (Transaction Id: " + transaction.getTransactionId() + ") created successfully");
            } else {
                firstFlightSchedule = retrieveFlightScheduleById(firstFlight);
                System.out.println("Select seat for first departure flight " + firstFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    firstSeats = getSeatInventory(firstFlightSchedule);
                } else {
                    firstSeats = getSeatInventory(firstFlightSchedule, cabinClassType);
                }
                firstFare = getBiggestFare(firstFlightSchedule, firstSeats.getCabin().getType());
                firstSeatSelection = bookSeats(firstSeats, numOfPassengers);
                firstReservation = new FlightReservation();
                firstReservation.setCabinClassType(firstSeats.getCabin().getType());       
                firstReservation.setFareBasisCode(firstFare.getFareCode());
                firstReservation.setTotalAmount(firstFare.getAmount());

                connectFlightSchedule = retrieveFlightScheduleById(connectFlight);
                System.out.println("Select seat for connecting flight " + connectFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    connectSeats = getSeatInventory(connectFlightSchedule);
                } else {
                    connectSeats = getSeatInventory(connectFlightSchedule, cabinClassType);
                }
                connectFare = getBiggestFare(connectFlightSchedule, connectSeats.getCabin().getType());
                connectSeatSelection = bookSeats(connectSeats, numOfPassengers);
                connectReservation = new FlightReservation();
                connectReservation.setCabinClassType(connectSeats.getCabin().getType());       
                connectReservation.setFareBasisCode(connectFare.getFareCode());
                connectReservation.setTotalAmount(connectFare.getAmount());

                returnFirstFlightSchedule = retrieveFlightScheduleById(returnFlight);
                System.out.println("Select seat for first return flight " + returnFirstFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    returnFirstSeats = getSeatInventory(returnFirstFlightSchedule);
                } else {
                    returnFirstSeats = getSeatInventory(returnFirstFlightSchedule, cabinClassType);
                }
                returnFirstFare = getBiggestFare(returnFirstFlightSchedule, returnFirstSeats.getCabin().getType());
                returnFirstSeatSelection = bookSeats(returnFirstSeats, numOfPassengers);
                returnFirstReservation = new FlightReservation();
                returnFirstReservation.setCabinClassType(returnFirstSeats.getCabin().getType());       
                returnFirstReservation.setFareBasisCode(returnFirstFare.getFareCode());
                returnFirstReservation.setTotalAmount(returnFirstFare.getAmount());

                returnConnectFlightSchedule = retrieveFlightScheduleById(returnConnectFlight);
                System.out.println("Select seat for connecting return flight " + returnConnectFlightSchedule.getFlightSchedulePlan().getFlightNumber());
                if (cabinClassType == null) {
                    returnConnectSeats = getSeatInventory(returnConnectFlightSchedule);
                } else {
                    returnConnectSeats = getSeatInventory(returnConnectFlightSchedule, cabinClassType);
                }
                returnConnectFare = getBiggestFare(returnConnectFlightSchedule, returnConnectSeats.getCabin().getType());
                returnConnectSeatSelection = bookSeats(returnConnectSeats, numOfPassengers);
                returnConnectReservation = new FlightReservation();
                returnConnectReservation.setCabinClassType(returnConnectSeats.getCabin().getType());       
                returnConnectReservation.setFareBasisCode(returnConnectFare.getFareCode());
                returnConnectReservation.setTotalAmount(returnConnectFare.getAmount()); 

                pricePerPassenger = firstFare.getAmount().add(connectFare.getAmount()).add(returnFirstFare.getAmount()).add(returnConnectFare.getAmount());
                List<Passenger> passengers = getPassengerInformation(numOfPassengers);

                System.out.println("Price per person : $" + pricePerPassenger.toString());
                System.out.println("Total Amount : $" + pricePerPassenger.multiply(new BigDecimal(numOfPassengers)));

                System.out.print("Enter Credit Card Number> ");
                String creditCardNum = sc.nextLine().trim();
                System.out.print("Enter cvv> ");
                String cvv = sc.nextLine().trim();
                System.out.print("Enter credit card expiry date (e.g., 23 Dec 2025)>");
                String expiryDate = sc.nextLine().trim();
                Date ccExpiryDate = sdf.parse(expiryDate);
                transaction.setCcNumber(creditCardNum);
                transaction.setCvv(cvv);
                XMLGregorianCalendar xmlGregorianCalendar = convertDateToXMLGregorianCalendar(ccExpiryDate);

                transaction.setCcExpiryDate(xmlGregorianCalendar);

               
                Long customerId = createNewTransaction(creditCardNum, cvv, ccExpiryDate, currentPartner);
               

                

                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(firstSeatSelection.get(i));
                }
                createNewReservation(firstReservation, passengers, firstFlightSchedule.getFlightScheduleId(), transaction.getTransactionId(), customerId);

                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(connectSeatSelection.get(i));
                }
                createNewReservation(connectReservation, passengers, connectFlightSchedule.getFlightScheduleId(), transaction.getTransactionId(), customerId);

                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(returnFirstSeatSelection.get(i));
                }
                createNewReservation(returnFirstReservation, passengers, returnFirstFlightSchedule.getFlightScheduleId(), transaction.getTransactionId(), customerId);

                for (int i = 0; i < passengers.size(); i++) {
                    passengers.get(i).setSeatNumber(returnConnectSeatSelection.get(i));
                }
                createNewReservation(returnConnectReservation, passengers, returnConnectFlightSchedule.getFlightScheduleId(), transaction.getTransactionId(), customerId);

                System.out.println("Flight Reservation (Transaction ID: " + transaction.getTransactionId() + ") created successfully");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage() + "\nPlease try again!\n");
        }
    }
    
    private List<Passenger> getPassengerInformation(int noOfPassengers) {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Passenger Details ***\n");
        List<Passenger> passengers = new ArrayList<>();
        for (int i = 1; i <= noOfPassengers; i++) {
            System.out.print("Enter passenger " + (i) + " first name> ");
            String firstName = sc.nextLine().trim();
            System.out.print("Enter passenger " + (i) + " last name> ");
            String lastName = sc.nextLine().trim();
            System.out.print("Enter passenger " + (i) + " passport number> ");
            String passport = sc.nextLine().trim();
            Passenger person = new Passenger();
            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setPassportNumber(passport);
            person.setSeatNumber(null);
            passengers.add(person);
        }
        return passengers;
    }

    private List<String> bookSeats(SeatInventory seatInventory, int numOfPassengers) {
        Scanner sc = new Scanner(System.in);
        int totalAvailSeats = seatInventory.getAvailableSeats();
        int totalReservedSeats = seatInventory.getReservedSeats();
        int totalBalanceSeats = seatInventory.getBalanceSeats();

        char[][] seats = new char[seatInventory.getCabin().getNumOfRows()][seatInventory.getCabin().getNumOfSeatsAbreast()];
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
        System.out.println("Number of reserved seats: " + totalReservedSeats);
        System.out.println("Number of balance seats: " + totalBalanceSeats);

        List<String> seatSelection = new ArrayList<>();
        while (true) {
            for (int i = 0; i < numOfPassengers; i++) {
                String seatNumber;
                while (true) {
                    System.out.print("\nEnter seat to reserve for Passenger " + (i + 1) + "(Eg. A10)> ");
                    seatNumber = sc.nextLine().trim();
                    boolean booked = isBooked(seatInventory, seatNumber);
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

    private void flightScheduleInformation(List<FlightSchedule> flightSchedules, CabinClassType cabinClassPreference, int passengers) throws CabinClassNotFoundException_Exception, FlightScheduleNotFoundException_Exception {

        System.out.printf("%15s%20s%30s%30s%40s%20s%20s%20s%30s%25s%25s\n",
                "Flight ID",
                "Flight Number",
                "Departure Airport",
                "Arrival Airport",
                "Departure Date & Time",
                "Estimated Duration",
                "Arrival Date & Time",
                "Cabin Type",
                "Number of balance seats",
                "Price per passenger",
                "Total Price");
        for (FlightSchedule flightSchedule : flightSchedules) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(flightSchedule.getDepartureDateTime().toGregorianCalendar().getTime());
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

                System.out.printf("%15s%20s%30s%30s%40s%20s%20s%20s%30s%25s%25s\n",
                        flightSchedule.getFlightScheduleId(),
                        flightSchedule.getFlightSchedulePlan().getFlightNumber(),
                        flightSchedule.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getAirportName(),
                        flightSchedule.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getAirportName(),
                        flightSchedule.getDepartureDateTime().toString().substring(0, 19),
                        flightSchedule.getEstimatedDuration(),
                        arrival.toString().substring(0, 19),
                        cabinClassType,
                        seat.getBalanceSeats(),
                        getBiggestFare(flightSchedule, seat.getCabin().getType()).getAmount(),
                        getBiggestFare(flightSchedule, seat.getCabin().getType()).getAmount().multiply(BigDecimal.valueOf(passengers))
                );
            }
        }
    }

    private void connectingFlightInformation(List<MyPair> flightSchedulePairs, CabinClassType cabin, int passengers) throws FlightScheduleNotFoundException_Exception, CabinClassNotFoundException_Exception {
        System.out.printf("%15s%20s%40s%40s%30s%20s%30s%30s%30s%25s%25s%25s%30s%45s%45s%40s%20s%30s%30s%30s%25s%25s\n",
                "Flight ID",
                "Flight Number",
                "Departure Airport",
                "Arrival Airport",
                "Departure Date & Time",
                "Estimated Duration",
                "Arrival Date & Time",
                "Cabin Type",
                "Number of balance seats",
                "Price per passenger",
                "Total Price",
                "Connecting Flight ID",
                "Connecting Flight Number",
                "Connecting Departure Airport",
                "Connecting Arrival Airport",
                "Connecting Departure Date & Time",
                "Connecting Estimated Duration",
                "Connecting Arrival Date & Time",
                "Connecting Cabin Type",
                "Connecting Number of balance seats",
                "Connecting Price per passenger",
                "Connecting Total Price");

        for (MyPair pair : flightSchedulePairs) {
            FlightSchedule flight1 = pair.getFs1();
            FlightSchedule flight2 = pair.getFs2();

            Calendar c2 = Calendar.getInstance();
            c2.setTime(flight1.getDepartureDateTime().toGregorianCalendar().getTime());
            double duration = flight1.getEstimatedDuration();
            int hour = (int) duration;
            int min = (int) (duration % 1 * 60);
            c2.add(Calendar.HOUR_OF_DAY, hour);
            c2.add(Calendar.MINUTE, min);
            Date arrival1 = c2.getTime();

            Calendar c3 = Calendar.getInstance();

            c3.setTime(flight2.getDepartureDateTime().toGregorianCalendar().getTime());
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
                    
                    System.out.printf("%15s%20s%40s%40s%30s%20s%30s%30s%30s%25s%25s%25s%30s%45s%45s%40s%20s%30s%30s%30s%25s%25s\n",
                            flight1.getFlightScheduleId(),
                            flight1.getFlightSchedulePlan().getFlightNumber(),
                            flight1.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getAirportName(),
                            flight1.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getAirportName(),
                            flight1.getDepartureDateTime().toString().substring(0, 19),
                            flight1.getEstimatedDuration(),
                            arrival1.toString().substring(0, 19),
                            cabinClassType1,
                            seats1.getBalanceSeats(),
                            getBiggestFare(flight1, seats1.getCabin().getType()).getAmount(),
                            getBiggestFare(flight1, seats1.getCabin().getType()).getAmount().multiply(BigDecimal.valueOf(passengers)),
                            flight2.getFlightScheduleId(),
                            flight2.getFlightSchedulePlan().getFlightNumber(),
                            flight2.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getAirportName(),
                            flight2.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getAirportName(),
                            flight2.getDepartureDateTime().toString().substring(0, 19),
                            flight2.getEstimatedDuration(),
                            arrival2.toString().substring(0, 19),
                            cabinClassType2,
                            seats2.getBalanceSeats(),
                            getBiggestFare(flight2, seats2.getCabin().getType()).getAmount(),
                            getBiggestFare(flight2, seats2.getCabin().getType()).getAmount().multiply(BigDecimal.valueOf(passengers)));
                } 
            }
        }
    }
    
    private void doViewMyReservations(Scanner sc) {
        System.out.println("=== View Flight Reservations ===\n");
        List<Transaction> transactionList = retrieveTransactionsByCustomerId(currentPartner);
        for (Transaction transaction : transactionList) {
            System.out.println("Transaction Reservation ID: " + transaction.getTransactionId());
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

        int response = 0;
        while (true) {
            System.out.println("1. Back");
            response = 0;

            while (response != 1) {
                System.out.print("> ");
                response = sc.nextInt();
                if (response == 1) {
                    doReservationMenu(sc);
                    break;
                } else {
                    System.out.println("Invalid options, Please try again\n");
                }
            }
            if (response == 1) {
                break;
            }
        }
    }

    private void doViewMyFlightReservationDetails(Scanner sc) {
        try {
            System.out.println("*** View My Flight Reservations Details ***\n");
            
            System.out.print("Enter ID of reservation to view in detail> ");
            long id = sc.nextLong();
            sc.nextLine();
            System.out.println();
            Transaction trans = retrieveTransactionById(id);
            

            BigDecimal totalPaid = new BigDecimal(0);
            for (FlightReservation fr : trans.getFlightReservations()) {
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
            System.out.println("");
            int response = 0;
            while (true) {
                System.out.println("1. Back");
                response = 0;

                while (response != 1) {
                    System.out.print("> ");
                    response = sc.nextInt();

                    if (response == 1) {
                        doReservationMenu(sc);
                    } else {
                        System.out.println("Invalid options, Please try again\n");
                    }
                }
                if (response == 1) {
                    break;
                }
            }

        } catch (TransactionNotFoundException_Exception ex) {
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
    
    private static Long doLogin(java.lang.String username, java.lang.String password) throws InvalidLoginException_Exception {
        FlightReservationWebService_Service service = new FlightReservationWebService_Service();
        FlightReservationWebService port = service.getFlightReservationWebServicePort();
        return port.doLogin(username, password);
    }
    
    private static Fare getBiggestFare(FlightSchedule flightscheduleentity, CabinClassType cabinclasstype) throws FlightScheduleNotFoundException_Exception, CabinClassNotFoundException_Exception {
        FlightReservationWebService_Service service = new FlightReservationWebService_Service();
        FlightReservationWebService port = service.getFlightReservationWebServicePort();
        return port.getBiggestFare(flightscheduleentity, cabinclasstype);
    }
    
    private static boolean isBooked(SeatInventory seatinventoryentity, java.lang.String seatnumber) {
        FlightReservationWebService_Service service = new FlightReservationWebService_Service();
        FlightReservationWebService port = service.getFlightReservationWebServicePort();
        return port.isBooked(seatinventoryentity, seatnumber);
    }
    
    private static SeatInventory getSeatInventory(FlightSchedule flightschedule, CabinClassType cabinclasstype) throws SeatInventoryNotFoundException_Exception, FlightScheduleNotFoundException_Exception {
        FlightReservationWebService_Service service = new FlightReservationWebService_Service();
        FlightReservationWebService port = service.getFlightReservationWebServicePort();
        return port.getSeatInventory(flightschedule, cabinclasstype);
    }


    private static FlightSchedule retrieveFlightScheduleById(long flightscheduleid) throws FlightScheduleNotFoundException_Exception {
        FlightReservationWebService_Service service = new FlightReservationWebService_Service();
        FlightReservationWebService port = service.getFlightReservationWebServicePort();
        return port.retrieveFlightScheduleById(flightscheduleid);
    }

    private static long createNewReservation(FlightReservation reservation, java.util.List<Passenger> passengers, long flightscheduleid, long transactionid, long customerid) throws FlightReservationExistException_Exception, InputDataValidationException_Exception, FlightScheduleNotFoundException_Exception, UnknownPersistenceException_Exception, SeatInventoryNotFoundException_Exception, SeatBookedException_Exception, TransactionNotFoundException_Exception, CustomerNotFoundException_Exception{
        FlightReservationWebService_Service service = new FlightReservationWebService_Service();
        FlightReservationWebService port = service.getFlightReservationWebServicePort();
        return port.createNewReservation(reservation, passengers, flightscheduleid, transactionid, customerid);
    }

    private static long createNewTransaction(java.lang.String creditcardnumber, java.lang.String cvv, java.util.Date ccDate, long userid) throws UnknownPersistenceException_Exception, TransactionExistException_Exception, InputDataValidationException_Exception, CustomerNotFoundException_Exception {
        FlightReservationWebService_Service service = new FlightReservationWebService_Service();
        FlightReservationWebService port = service.getFlightReservationWebServicePort();
        XMLGregorianCalendar xmlGregorianCalendar = convertDateToXMLGregorianCalendar(ccDate);
        return port.createNewTransaction(creditcardnumber, cvv, xmlGregorianCalendar, userid);
    }

    private static java.util.List<Transaction> retrieveTransactionsByCustomerId(long userid) {
        FlightReservationWebService_Service service = new FlightReservationWebService_Service();
        FlightReservationWebService port = service.getFlightReservationWebServicePort();
        return port.retrieveTransactionsByCustomerId(userid);
    }

    private static Transaction retrieveTransactionById(long itineraryid) throws TransactionNotFoundException_Exception {
        FlightReservationWebService_Service service = new FlightReservationWebService_Service();
        FlightReservationWebService port = service.getFlightReservationWebServicePort();
        return port.retreiveTransactionById(itineraryid);
    }

    private static XMLGregorianCalendar convertDateToXMLGregorianCalendar(Date date) {
    try {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
    } catch (DatatypeConfigurationException e) {
        // Handle the exception according to your requirements
        e.printStackTrace();
        return null;
    }
}

    private static java.util.List<FlightSchedule> getFlightSchedules(java.lang.String origin, java.lang.String destination, java.lang.String date, CabinClassType cabinclasstype) throws AirportNotFoundException_Exception, FlightNotFoundException_Exception, ParseException_Exception {
        FlightReservationWebService_Service service = new FlightReservationWebService_Service();
        FlightReservationWebService port = service.getFlightReservationWebServicePort();
        return port.getFlightSchedules(origin, destination, date, cabinclasstype);
    }

    private static java.util.List<MyPair> getIndirectFlightSchedules(java.lang.String origin, java.lang.String destination, java.lang.String date, CabinClassType cabinclasstype) throws FlightNotFoundException_Exception, ParseException_Exception {
        FlightReservationWebService_Service service = new FlightReservationWebService_Service();
        FlightReservationWebService port = service.getFlightReservationWebServicePort();
        return port.getIndirectFlightSchedules(origin, destination, date, cabinclasstype);
    }
}

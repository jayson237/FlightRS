/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.CabinClass;
import entity.Employee;
import entity.Fare;
import entity.Flight;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.FlightScheduleType;
import util.exception.FlightNotFoundException;

/**
 *
 * @author jayso
 */
public class FlightOperationModule {

    private Employee employee;
    private FlightSessionBeanRemote flightSessionBean;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private MainApp mainApp;

    public FlightOperationModule(MainApp mainApp, Employee employee, FlightSessionBeanRemote flightSessionBean, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean) {
        this.mainApp = mainApp;
        this.employee = employee;
        this.flightSessionBean = flightSessionBean;
        this.flightSchedulePlanSessionBean = flightSchedulePlanSessionBean;
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public void menuScheduleManager() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Merlion FlightRS Flight Operation Module ===\n");
        System.out.println("1. Create Flight");
        System.out.println("2. View All Flights");
        System.out.println("3. View Flight Details");
        System.out.println("4. Create Flight Schedule Plan");
        System.out.println("5. View All Flight Schedule Plan");
        System.out.println("6. View Flight Schedule Plan Details");
        System.out.println("7. Log out");
        System.out.print("> ");
        int input = sc.nextInt();
        sc.nextLine();
        switch (input) {
            case 1:
                doCreateFlight(sc);
                break;
            case 2:
                doViewAllFlights(sc);
                break;
            case 3:
                doViewFlightDetails(sc);
                break;
            case 4:
                doCreateNewFlightPlan(sc);
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                mainApp.doLogOut();
                break;
            default:
                System.out.println("Invalid input, please try again");
                menuScheduleManager();
                break;
        }
    }

    private void doCreateFlight(Scanner sc) {
        System.out.print("Enter new flight number> ");
        String flightNumber = sc.nextLine().toUpperCase();
        System.out.print("Enter origin> ");
        String origin = sc.nextLine().toUpperCase();
        System.out.print("Enter destination> ");
        String destination = sc.nextLine().toUpperCase();
        System.out.print("Enter configuration name> ");
        String configName = sc.nextLine().trim().toUpperCase();

        Flight newFlight = new Flight(flightNumber, false, null);
        Set<ConstraintViolation<Flight>> constraintViolations = validator.validate(newFlight);
        if (constraintViolations.isEmpty()) {
            try {
                boolean hasReturn = flightSessionBean.retrieveRouteStatus(origin, destination);
                if (hasReturn) {
                    System.out.println("The flight route " + origin + " to " + destination + " has a complementary return flight");
                    System.out.print("This route has a complementary return route, do you want to create one [Y/N]?> ");
                    String response = sc.nextLine().trim().toUpperCase();
                    if (response.equals("Y")) {
                        System.out.print("Enter new return flight number> ");
                        String returnFlightNumber = sc.nextLine().toUpperCase();
                        Flight returnFlight = new Flight(returnFlightNumber, false, flightNumber);

                        newFlight.setReturnFlightNumber(returnFlightNumber);
                        Flight flight = flightSessionBean.createNewFlight(newFlight, origin, destination, configName);
                        Flight newReturnFlight = flightSessionBean.createNewFlight(returnFlight, destination, origin, configName);

                        if (flight.getFlightRoute() == null || newReturnFlight.getFlightRoute() == null) {
                            System.out.println("Either one or both of the Flight Route is disabled\n");
                        } else {
                            System.out.println("Return flight " + flight.getFlightId() + " and " + newReturnFlight.getFlightId() + " are created successfully!\n");
                        }
                    } else {
                        Flight flight = flightSessionBean.createNewFlight(newFlight, origin, destination, configName);
                        if (flight.getFlightRoute() == null) {
                            System.out.println("Flight Route " + origin + " to " + destination + " is disabled\n");
                        } else {
                            System.out.println("Return flight " + flight.getFlightId() + " is created successfully!\n");
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + "\n");
            }
//            catch (FlightRouteNotFoundException ex) {
//                System.out.println("Flight route with origin airport " + origin + " and " + "destination airport " + destination + " does not exist");
//            } catch (AirportNotFoundException ex) {
//                System.out.println("Either one or both airport codes does/do not exist");
//            } catch (AircraftConfigurationNotFoundException ex) {
//                System.out.println("Aircraft Configuration name: " + configName + " does not exist");
//            } catch (FlightExistException ex) {
//                System.out.println("Such flight already exist");
//            } catch (GeneralException ex) {
//                System.out.println(ex.getMessage() + "\n");
//            } catch (InputDataValidationException ex) {
//                System.out.println(ex.getMessage() + "\n");
//            }
        } else {
            showInputDataValidationErrorsForFlight(constraintViolations);
        }
    }

    private void doViewAllFlights(Scanner sc) {
        List<Flight> flights = flightSessionBean.retrieveAllFlights();
        if (!flights.isEmpty()) {
            int size = flights.size();
            for (int i = 0; i < size; i++) {
                Flight fr = flights.get(i);
                System.out.println("ID: " + fr.getFlightId()
                        + ", Flight Number: " + fr.getFlightNumber());

                if (i == size - 1) {
                    System.out.println("");
                }
            }
        } else {
            System.out.println("There is no existing Flight record\n");
        }
    }

    private void doViewFlightDetails(Scanner sc) {
        System.out.print("Enter flight number> ");
        String number = sc.nextLine().toUpperCase();
        try {
            Flight f = flightSessionBean.retrieveFlightByNumber(number);
            System.out.println("ID: " + f.getFlightId() + ", Origin: " + f.getFlightRoute().getOriginAirport().getAirportCode() + ", Destination: " + f.getFlightRoute().getDestinationAirport().getAirportCode());
            AircraftConfiguration ac = f.getAircraftConfiguration();
            List<CabinClass> ccs = ac.getCabinClasses();
            System.out.println("Available cabin classes: ");
            int size = ccs.size();
            for (int i = 0; i < size; i++) {
                CabinClass cc = ccs.get(i);
                System.out.println(cc.getType() + ": " + cc.getMaxCapacity() + " available seats");
                if (i == size - 1) {
                    System.out.println("");
                }
            }
            doUpdateDeleteFlight(f.getFlightId(), sc);
        } catch (FlightNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }

    private void doUpdateDeleteFlight(Long flightId, Scanner sc) {
        System.out.println("1. Update Flight");
        System.out.println("2. Delete Flight");
        System.out.println("3. Back");
        System.out.print("> ");
        int input = sc.nextInt();
        switch (input) {
            case 1:
                doUpdateFlight(flightId, sc);
                break;
            case 2:
                doDeleteFlight(flightId);
                break;
            case 3:
                menuScheduleManager();
                break;
            default:
                System.out.println("Invalid input, please try again");
                doUpdateDeleteFlight(flightId, sc);
                break;
        }
    }

    private void doUpdateFlight(Long flightId, Scanner sc) {
        try {
            Flight flight = flightSessionBean.retrieveFlightById(flightId);
            System.out.print("Enter new flight number (press Enter to keep the existing number)> ");
            String newNumber = sc.nextLine().toUpperCase();
            if (!newNumber.isEmpty()) {
                flight.setFlightNumber(newNumber);
            }

            sc.nextLine();

            if (flight.getIsDisabled()) {
                System.out.print("Would you like to enable this flight? [Y/N]");
            } else {
                System.out.print("Would you like to disable this flight? [Y/N]");
            }

            String response = sc.nextLine().trim().toUpperCase();
            if (response.equals("Y")) {
                flight.setIsDisabled(!flight.getIsDisabled());
            }

            try {
                flightSessionBean.updateFlight(flight);
                System.out.println("Flight id: " + flight.getFlightId() + " is updated successfully");
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + "\n");
            }
//            catch (UpdateFlightException ex) {
//                System.out.println("Flight number of the existing flight does not match the existing record");
//            } catch (InputDataValidationException ex) {
//                System.out.println(ex.getMessage() + "\n");
//            }
        } catch (FlightNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }

    private void doDeleteFlight(Long flightId) {
        try {
            flightSessionBean.deleteFlight(flightId);
            System.out.println("Flight id: " + flightId + " is successfully deleted\n");
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + "\n");
        }
//        catch (FlightNotFoundException ex) {
//            System.out.println("Flight id: " + flightId + " does not exist");
//        } catch (DeleteFlightException ex) {
//            System.out.println("Flight id: " + flightId + " is in use and cannot be deleted! It will be disabled");
//        }
    }

    private void doCreateNewFlightPlan(Scanner sc) {
        FlightSchedulePlan fsp = new FlightSchedulePlan();

        System.out.print("Enter flight number> ");
        String flightNumber = sc.nextLine().toUpperCase();
        try {
            Flight flight = flightSessionBean.retrieveFlightByNumber(flightNumber);
            while (true) {
                System.out.print("Select Flight Schedule Type (1: Manual Single, 2: Manual Multiple, 3: Recurrent Weekly, 4: Recurrent NDay)> ");
                Integer scheduleTypeInt = sc.nextInt();
                sc.nextLine();

                if (scheduleTypeInt >= 1 && scheduleTypeInt <= 4) {
                    fsp.setType(FlightScheduleType.values()[scheduleTypeInt - 1]);
                    fsp.setFlight(flight);
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            ArrayList<FlightSchedule> fsList = new ArrayList<FlightSchedule>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");

            System.out.println("*** Merlion Flight RS System :: Create Schedule ***");
            if (fsp.getType() == FlightScheduleType.MANUALSINGLE) {
                FlightSchedule fs = new FlightSchedule();
                System.out.print("Enter Departure Date (e.g., 1 Dec 23): ");
                String departureDateInput = sc.nextLine().trim();

                System.out.print("Enter Departure Time (e.g., 9:00 AM): ");
                String departureTimeInput = sc.nextLine().trim();

                System.out.print("Enter Flight Duration (e.g., 6 Hours 30 Minutes): ");
                String durationInput = sc.nextLine().trim();

                try {
                    Calendar departureCalendar = Calendar.getInstance();
                    departureCalendar.setTime(dateFormat.parse(departureDateInput));

                    Calendar departureTimeCalendar = Calendar.getInstance();
                    departureTimeCalendar.setTime(timeFormat.parse(departureTimeInput));

                    // Set the time components of departureCalendar to match departureTimeCalendar
                    departureCalendar.set(Calendar.HOUR_OF_DAY, departureTimeCalendar.get(Calendar.HOUR_OF_DAY));
                    departureCalendar.set(Calendar.MINUTE, departureTimeCalendar.get(Calendar.MINUTE));

                    // Calculate arrival date-time based on departure date-time and duration
                    Calendar arrivalCalendar = (Calendar) departureCalendar.clone();
                    arrivalCalendar.add(Calendar.HOUR_OF_DAY, parseHours(durationInput));
                    arrivalCalendar.add(Calendar.MINUTE, parseMinutes(durationInput));

                    fs.setDepartureDateTime(departureCalendar.getTime());
                    fs.setArrivalDateTime(arrivalCalendar.getTime());
                    fs.setEstimatedDuration(durationInput);

                    fsList.add(fs);
                } catch (ParseException e) {
                    System.out.println("Invalid date or time format");
                }
            }
            ArrayList<Fare> fareList = new ArrayList<Fare>();
            for (int i = 0; i < fsp.getFlight().getAircraftConfiguration().getCabinClasses().size(); i++) {
                Fare fare = new Fare();
                System.out.print("Enter fare amount for cabin class " + fsp.getFlight().getAircraftConfiguration().getCabinClasses().get(i).getType() + ": ");
                BigDecimal amount = sc.nextBigDecimal();
                fare.setAmount(amount);
                fareList.add(fare);
            }
            try {
                FlightSchedulePlan fspResult = flightSchedulePlanSessionBean.createNewFlightSchedulePlan(fsList, fsp, flight.getFlightId(), fareList);
                System.out.println("Flight Schedule Plan with id: " + fspResult.getFlightSchedulePlanId() + " successfully created");
                if (fspResult.getFlight().getFlightRoute().isHasReturnFlight()) {
                    System.out.print("Would you like to create a complementary return flight schedule plan [Y/N]? ");
                    String response = sc.nextLine().toUpperCase();
                    if (response.equals("Y")) {

                    }
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } catch (FlightNotFoundException ex) {
            System.out.println("Flight number: " + flightNumber + " not found\n");
        }
    }

    private int parseHours(String durationInput) {
        int hours = 0;
        String[] parts = durationInput.split("\\s+");

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equalsIgnoreCase("hours") || parts[i].equalsIgnoreCase("hour")) {
                hours = Integer.parseInt(parts[i - 1]);
                break;
            }
        }

        return hours;
    }

    private int parseMinutes(String durationInput) {
        int minutes = 0;
        String[] parts = durationInput.split("\\s+");

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equalsIgnoreCase("minutes") || parts[i].equalsIgnoreCase("minute")) {
                minutes = Integer.parseInt(parts[i - 1]);
                break;
            }
        }

        return minutes;
    }

    private void showInputDataValidationErrorsForFlight(Set<ConstraintViolation<Flight>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

}

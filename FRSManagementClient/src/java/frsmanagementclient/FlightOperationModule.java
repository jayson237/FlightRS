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
import entity.Flight;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AirportNotFoundException;
import util.exception.DeleteFlightException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.UpdateFlightException;

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
        System.out.println("7. Exit");
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
        String flightNumber = sc.nextLine();
        System.out.print("Enter origin> ");
        String origin = sc.nextLine();
        System.out.print("Enter destination> ");
        String destination = sc.nextLine();
        System.out.print("Enter Configuration name> ");
        String configName = sc.nextLine();

        Flight newFlight = new Flight(flightNumber, false);
        Set<ConstraintViolation<Flight>> constraintViolations = validator.validate(newFlight);
        if (constraintViolations.isEmpty()) {
            try {
                Flight flight = flightSessionBean.createNewFlight(newFlight, origin, destination, configName);
                System.out.println("Flight " + flight.getFlightId() + " is created successfully!\n");
                if (flight.getFlightRoute().isHasReturnFlight()) {
                    System.out.println("The flight route " + origin + " to " + destination + " has a complementary return flight");
                    System.out.print("Do you want to have the same flight number for the return flight [Y/N]> ");
                    String response = sc.nextLine().trim().toUpperCase();
                    if (response.equals("Y")) {
                        Flight returnFlight = flightSessionBean.createNewFlight(newFlight, destination, origin, configName);
                        System.out.println("Return flight " + returnFlight.getFlightId() + " is created successfully!\n");
                    } else {
                        System.out.print("Enter new return flight number> ");
                        String newFlightNumber = sc.nextLine();
                        newFlight = new Flight(newFlightNumber, false);
                        Flight returnFlight = flightSessionBean.createNewFlight(newFlight, destination, origin, configName);
                        System.out.println("Return flight " + returnFlight.getFlightId() + " is created successfully!\n");
                    }
                }
            } catch (FlightRouteNotFoundException ex) {
                System.out.println("Flight route with origin airport " + origin + " and " + "destination airport " + destination + " does not exist");
            } catch (AirportNotFoundException ex) {
                System.out.println("Either one or both airport codes does/do not exist");
            } catch (AircraftConfigurationNotFoundException ex) {
                System.out.println("Aircraft Configuration name: " + configName + " does not exist");
            } catch (FlightExistException ex) {
                System.out.println("Such flight already exist");
            } catch (GeneralException ex) {
                System.out.println(ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
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
        String number = sc.nextLine();
        try {
            Flight f = flightSessionBean.retrieveFlightByNumber(number);
            System.out.println("ID: " + f.getFlightId() + ", Origin: " + f.getFlightRoute().getOriginAirport().getAirportCode() + ", Destination: " + f.getFlightRoute().getDestinationAirport().getAirportCode());
            AircraftConfiguration ac = f.getAircraftConfiguration();
            List<CabinClass> ccs = ac.getCabinClasses();
            System.out.println("Available cabin classes: ");
            int size = ccs.size();
            for (int i = 0; i < size; i++) {
                CabinClass cc = ccs.get(i);
                System.out.println(cc.getType() + ": " + cc.getAvailableSeats() + " available seats");
                if (i == size - 1) {
                    System.out.println("");
                }
            }
            doUpdateDeleteFlight(f.getFlightId(), sc);
        } catch (FlightNotFoundException ex) {
            System.out.println("Flight number: " + number + " does not exist\n");
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
            String newNumber = sc.nextLine().trim();
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
            } catch (UpdateFlightException ex) {
                System.out.println("Flight number of the existing flight does not match the existing record");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } catch (FlightNotFoundException ex) {
            System.out.println("Flight id: " + flightId + " does not exist");
        }
    }

    private void doDeleteFlight(Long flightId) {
        try {
            flightSessionBean.deleteFlight(flightId);
            System.out.println("Flight id: " + flightId + " is successfully deleted\n");
        } catch (FlightNotFoundException ex) {
            System.out.println("Flight id: " + flightId + " does not exist");
        } catch (DeleteFlightException ex) {
            System.out.println("Flight id: " + flightId + " is in use and cannot be deleted! It will be disabled");
        }
    }

    private void showInputDataValidationErrorsForFlight(Set<ConstraintViolation<Flight>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

}

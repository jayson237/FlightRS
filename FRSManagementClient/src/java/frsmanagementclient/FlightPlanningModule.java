/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.CabinClass;
import entity.Employee;
import entity.FlightRoute;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;
import util.enumeration.CabinClassType;
import util.exception.AircraftConfigurationExistException;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.AirportNotFoundException;
import util.exception.CabinClassExistException;
import util.exception.DeleteFlightRouteException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;

/**
 *
 * @author jayso
 */
public class FlightPlanningModule {

    private Employee employee;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean;
    private FlightRouteSessionBeanRemote flightRouteSessionBean;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private MainApp mainApp;

    public FlightPlanningModule(MainApp mainApp, Employee employee, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean) {
        this.mainApp = mainApp;
        this.employee = employee;
        this.aircraftConfigurationSessionBean = aircraftConfigurationSessionBean;
        this.flightRouteSessionBean = null;
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public FlightPlanningModule(MainApp mainApp, Employee employee, FlightRouteSessionBeanRemote flightRouteSessionBean) {
        this.mainApp = mainApp;
        this.employee = employee;
        this.aircraftConfigurationSessionBean = null;
        this.flightRouteSessionBean = flightRouteSessionBean;
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public void menuFleetManager() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Merlion FlightRS Flight Planning Module ===\n");
        System.out.println("1. Create New Aircraft Configuration");
        System.out.println("2. View All Aircraft Configurations");
        System.out.println("3. View Aircraft Configuration Details");
        System.out.println("4. Exit");
        System.out.print("> ");
        int input = sc.nextInt();
        sc.nextLine();
        switch (input) {
            case 1:
                doCreateAircraftConfiguration(sc);
                break;
            case 2:
                doViewAllAircraftConfigurations();
                break;
            case 3:
                doViewAircraftConfigurationDetails(sc);
                break;
            case 4:
                mainApp.doLogOut();
                break;
            default:
                System.out.println("Invalid input, please try again");
                menuFleetManager();
                break;
        }
    }

    public void menuRoutePlanner() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Merlion FlightRS Flight Planning Module ===\n");
        System.out.println("1. Create New Flight Route");
        System.out.println("2. View All Flight Routes");
        System.out.println("3. Delete Flight Route");
        System.out.println("4. Exit");
        System.out.print("> ");
        int input = sc.nextInt();
        sc.nextLine();

        switch (input) {
            case 1:
                doCreateFlightRoute(sc);
                break;
            case 2:
                doViewAllFlightRoutes(sc);
                break;
            case 3:
                doDeleteFlightRoute(sc);
                break;
            case 4:
                mainApp.doLogOut();
                break;
            default:
                System.out.println("Invalid input, please try again");
                menuRoutePlanner();
                break;
        }
    }

    // Aircraft Configuration
    private void doCreateAircraftConfiguration(Scanner sc) {
        AircraftConfiguration ac = new AircraftConfiguration();
        List<CabinClass> ccs = new ArrayList<>();
        System.out.println("=== Merlion FlightRS :: New Air Configuration ===\n");
        System.out.print("Enter configuration name> ");
        ac.setName(sc.nextLine());
        System.out.print("Enter number of cabin class> ");
        int numOfCabinClass = sc.nextInt();
        ac.setNumOfCabinClass(numOfCabinClass);
        System.out.print("Enter max number capacity for the configuration> ");
        int configMaxCap = sc.nextInt();
        ac.setMaxSeats(configMaxCap);
        int cabinClassCap = 0;

        for (int i = 1; i <= numOfCabinClass; i++) {
            CabinClass cc = new CabinClass();
            System.out.println("=====================================");
            System.out.println("Create Cabin Class " + i);
            System.out.print("Enter cabin class type [1: F / 2: J / 3: W / 4: Y]> ");
            CabinClassType cabinClassType = CabinClassType.values()[sc.nextInt() - 1];
            cc.setType(cabinClassType);
            System.out.print("Enter number of aisle> ");
            cc.setNumberOfAisles(sc.nextInt());
            System.out.print("Enter number of row> ");
            int rows = sc.nextInt();
            cc.setNumOfRows(rows);
            System.out.print("Enter number of seat abreast> ");
            int seatsAbreast = sc.nextInt();
            cc.setNumOfSeatsAbreast(seatsAbreast);
            sc.nextLine();
            System.out.print("Enter seat configuration> ");
            cc.setSeatingConfiguration(sc.nextLine());
            System.out.print("Enter max capacity> ");
            cc.setMaxCapacity(sc.nextInt());
            int max = seatsAbreast * rows;
            cabinClassCap += max;
            cc.setMaxCapacity(max);
            cc.setAvailableSeats(max);
            cc.setReservedSeats(0);

            ccs.add(cc);
        }

        Set<ConstraintViolation<AircraftConfiguration>> constraintViolations = validator.validate(ac);
        if (constraintViolations.isEmpty() && cabinClassCap == configMaxCap) {
            try {
                AircraftConfiguration newAc = aircraftConfigurationSessionBean.createNewAircraftConfig(ac, ccs);
                System.out.println("Aircraft Configuration is created successfully!\n");
            } catch (AircraftTypeNotFoundException ex) {
                System.out.println("Aircraft type does not exist");
            } catch (CabinClassExistException ex) {
                System.out.println("Detected cabin class that already exist\n");
            } catch (AircraftConfigurationExistException ex) {
                System.out.println("Aircraft configuration already exist\n");
            } catch (GeneralException ex) {
                System.out.println(ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForAircraftConfiguration(constraintViolations);
        }
    }

    private void doViewAllAircraftConfigurations() {
        List<AircraftConfiguration> airConfigs = aircraftConfigurationSessionBean.retrieveAllAirConfigurations();
        if (!airConfigs.isEmpty()) {
            int size = airConfigs.size();
            for (int i = 0; i < size; i++) {
                AircraftConfiguration ac = airConfigs.get(i);
                System.out.println("ID: " + ac.getAircraftConfigurationId()
                        + ", Name: " + ac.getName());

                if (i == size - 1) {
                    System.out.println("");
                }
            }
        } else {
            System.out.println("There is no existing Aircraft Configuration record\n");
        }
    }

    private void doViewAircraftConfigurationDetails(Scanner sc) {
        System.out.print("Enter Aircraft Configuration ID to view> ");
        Long id = sc.nextLong();
        sc.nextLine();

        try {
            AircraftConfiguration ac = aircraftConfigurationSessionBean.retrieveAirConfigById(id);
            System.out.println("Name: " + ac.getName());
            System.out.println("Max Capacity: " + ac.getMaxSeats());
            List<CabinClass> ccs = ac.getCabinClasses();
            int size = ccs.size();
            for (int i = 0; i < size; i++) {
                CabinClass cc = ccs.get(i);
                System.out.println("Cabin Class: " + cc.getType().toString());
                System.out.println("Num of Aisle: " + cc.getNumberOfAisles());
                System.out.println("Num of Seat Abreast: " + cc.getNumOfSeatsAbreast());
                System.out.println("Seat Configuration: " + cc.getSeatingConfiguration());
                System.out.println("Num Of Row: " + cc.getNumOfRows());
                System.out.println("Max Seat: " + cc.getMaxCapacity());
                System.out.println("================================");
                if (i == size - 1) {
                    System.out.println("");
                }
            }

        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println("Aircraft Configuration with id: " + id + " does not exist");
        }

    }

    // Route
    private void doCreateFlightRoute(Scanner sc) {
        System.out.print("Enter origin> ");
        String origin = sc.nextLine().trim().toUpperCase();
        System.out.print("Enter destination> ");
        String destination = sc.nextLine().trim().toUpperCase();
        System.out.print("Complementary Return Flight [Y/N]> ");
        String response = sc.nextLine().trim().toUpperCase();
        boolean hasReturn = false;
        if (response.equals("Y")) {
            hasReturn = true;
        }

        FlightRoute route = new FlightRoute(hasReturn, false);
        Set<ConstraintViolation<FlightRoute>> constraintViolations = validator.validate(route);
        if (constraintViolations.isEmpty()) {
            try {
                FlightRoute fr = flightRouteSessionBean.createNewFlightRoute(route, origin, destination);
                System.out.println("Flight Route " + fr.getFlightRouteId() + " is created successfully!\n");
            } catch (AirportNotFoundException ex) {
                System.out.println("Airport does not exist\n");
            } catch (FlightRouteExistException ex) {
                System.out.println("Such flight route already exist\n");
            } catch (GeneralException ex) {
                System.out.println(ex.getMessage() + "\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForFlightRoute(constraintViolations);
        }
    }

    private void doViewAllFlightRoutes(Scanner sc) {
        List<FlightRoute> routes = flightRouteSessionBean.retrieveAllFlightRoutes();
        if (!routes.isEmpty()) {
            int size = routes.size();
            for (int i = 0; i < size; i++) {
                FlightRoute fr = routes.get(i);
                System.out.println("ID: " + fr.getFlightRouteId()
                        + ", Origin: " + fr.getOriginAirport().getAirportCode()
                        + ", Destination: " + fr.getDestinationAirport().getAirportCode());

                if (i == size - 1) {
                    System.out.println("");
                }
            }
        } else {
            System.out.println("There is no existing Aircraft Configuration record\n");
        }
    }

    private void doDeleteFlightRoute(Scanner sc) {
        System.out.print("Please input the Flight Route ID you wish to remove> ");
        Long id = sc.nextLong();
        sc.nextLine();

        try {
            flightRouteSessionBean.deleteFlightRoute(id);
            System.out.println("Flight Route " + id + " is successfully deleted\n");
        } catch (FlightRouteNotFoundException ex) {
            System.out.println("Flight route id: " + id + " does not exist\n");
        } catch (DeleteFlightRouteException ex) {
            System.out.println("Flight route id: " + id + " is in use and cannot be deleted! It will be disabled");
        }
    }

    private void showInputDataValidationErrorsForAircraftConfiguration(Set<ConstraintViolation<AircraftConfiguration>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

    private void showInputDataValidationErrorsForFlightRoute(Set<ConstraintViolation<FlightRoute>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}

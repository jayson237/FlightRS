/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.FareSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.CabinClass;
import entity.Employee;
import entity.Fare;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.math.BigDecimal;
import java.text.ParseException;
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
import util.enumeration.FlightScheduleType;
import util.exception.FareExistException;
import util.exception.FlightNotFoundException;

/**
 *
 * @author jayso
 */
public class FlightOperationModule {

    private Employee employee;
    private FareSessionBeanRemote fareSessionBean;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBean;
    private FlightSessionBeanRemote flightSessionBean;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private MainApp mainApp;

    public FlightOperationModule(MainApp mainApp, Employee employee, FareSessionBeanRemote fareSessionBean,
            FlightScheduleSessionBeanRemote flightScheduleSessionBean, FlightSessionBeanRemote flightSessionBean, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean) {
        this.mainApp = mainApp;
        this.employee = employee;
        this.flightScheduleSessionBean = flightScheduleSessionBean;
        this.fareSessionBean = fareSessionBean;
        this.flightSessionBean = flightSessionBean;
        this.flightSchedulePlanSessionBean = flightSchedulePlanSessionBean;
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public void menuScheduleManager() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n=== Merlion FlightRS Flight Operation Module ===\n");
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
                doViewAllFlights();
                break;
            case 3:
                doViewFlightDetails(sc);
                break;
            case 4:
                doCreateFlightSchedulePlan(sc);
                break;
            case 5:
                doViewAllFlightSchedulePlan();
                break;
            case 6:
                doViewFlightSchedulePlanDetails(sc);
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
                    System.out.print("This route has a complementary return route, do you want to create one [Y/N]> ");
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
                            System.out.println("Flight " + flight.getFlightNumber() + " and " + newReturnFlight.getFlightNumber() + " are created successfully!\n");
                        }
                    } else {
                        Flight flight = flightSessionBean.createNewFlight(newFlight, origin, destination, configName);
                        if (flight.getFlightRoute() == null) {
                            System.out.println("Flight Route " + origin + " to " + destination + " is disabled\n");
                        } else {
                            System.out.println("Flight " + flight.getFlightNumber() + " is created successfully!\n");
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForFlight(constraintViolations);
        }
    }

    private void doViewAllFlights() {
        List<Flight> flights = flightSessionBean.retrieveAllFlights();
        if (!flights.isEmpty()) {
            System.out.printf("%10s%15s\n", "ID", "Flight Number");
            for (Flight fr : flights) {
                System.out.printf("%10s%15s\n", fr.getFlightId(), fr.getFlightNumber());
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
                System.out.print("Would you like to enable this flight [Y/N]> ");
            } else {
                System.out.print("Would you like to disable this flight [Y/N]> ");
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
    }

    private void doCreateFlightSchedulePlan(Scanner sc) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yy");
            FlightSchedulePlan plan = new FlightSchedulePlan();
            List<Pair<Date, Double>> flightInfo = new ArrayList<>();
            Pair<Date, Double> infoPair = null;
            int recurrentDay = 0;
            int layOver = 0;
            int days = 0;

            System.out.println("=== Merlion Flight RS System :: Create Schedule for Plan ===");
            System.out.print("Enter flight number> ");
            Flight flight = flightSessionBean.retrieveFlightByNumber(sc.nextLine().toUpperCase());
            plan.setFlight(flight);
            plan.setFlightNumber(flight.getFlightNumber());
            plan.setIsDisabled(false);

            System.out.println("Select Flight Schedule Type");
            System.out.println("1: Manual Single");
            System.out.println("2: Manual Multiple");
            System.out.println("3: Recurrent N-Day");
            System.out.println("4: Recurrent Weekly");
            System.out.print("> ");
            int input = sc.nextInt();
            sc.nextLine();

            // Ask users if they want to have the complementary return plan
            String returnFlightNumber = flight.getReturnFlightNumber();
            String needsReturnSchedulePlan = "";
            if (returnFlightNumber != null) {
                System.out.println("Complementary return flight has been found for flight " + flight.getFlightNumber());
                while (true) {
                    System.out.print("Would you like to create the complementary return flight schedule plan for " + returnFlightNumber + " [Y/N]> ");
                    needsReturnSchedulePlan = sc.next().trim().toUpperCase();
                    if (needsReturnSchedulePlan.equals("Y") || needsReturnSchedulePlan.equals("N")) {
                        break;
                    } else {
                        System.out.println("Invalid input. Please enter either 'Y' or 'N'");
                    }
                }
            }

            // For Outbound Schedule Plan
            switch (input) {
                case 1:
                    plan.setType(FlightScheduleType.MANUALSINGLE);
                    sc.nextLine();
                    infoPair = getFlightSchedule(sc, false);
                    break;
                case 2:
                    plan.setType(FlightScheduleType.MANUALMULTIPLE);
                    String res = sc.nextLine().toUpperCase();
                    while (!res.equals("N")) {
                        Pair newPair = getFlightSchedule(sc, false);
                        flightInfo.add(newPair);
                        System.out.print("Enter another schedule [Y/N]> ");
                        res = sc.nextLine().trim().toUpperCase();
                    }
                    break;
                case 3:
                    plan.setType(FlightScheduleType.RECURRENTNDAY);
                    System.out.print("Enter interval of recurrence [1-6]> ");
                    days = sc.nextInt();
                    sc.nextLine();
                    infoPair = getFlightSchedule(sc, false);
                    System.out.print("Enter recurrent End Date (e.g., 1 Dec 23)> ");
                    String date = sc.nextLine().trim();
                    sc.nextLine();
                    Date dailyEnd = dateFormat.parse(date);
                    plan.setRecurrentEndDate(dailyEnd);
                    break;
                case 4:
                    plan.setType(FlightScheduleType.RECURRENTWEEKLY);
                    System.out.print("Enter recurrent day (e.g, Monday)> ");
                    sc.nextLine();
                    String day = sc.nextLine().trim().toLowerCase();
                    switch (day) {
                        case "sunday":
                            recurrentDay = 1;
                            break;
                        case "monday":
                            recurrentDay = 2;
                            break;
                        case "tuesday":
                            recurrentDay = 3;
                            break;
                        case "wednesday":
                            recurrentDay = 4;
                            break;
                        case "thursday":
                            recurrentDay = 5;
                            break;
                        case "friday":
                            recurrentDay = 6;
                            break;
                        case "saturday":
                            recurrentDay = 7;
                            break;
                    }

                    infoPair = getFlightSchedule(sc, true);
                    System.out.print("Enter recurrent end date (e.g., 1 Dec 23)> ");
                    String endDate = sc.nextLine().trim();
                    Date weekEnd = dateFormat.parse(endDate);
                    plan.setRecurrentEndDate(weekEnd);
                    break;
            }

            // For Return Schedule Plan
            FlightSchedulePlan returnPlan = new FlightSchedulePlan();
            Flight returnFlight = null;
            List<Pair<Date, Double>> returnFlightInfo = new ArrayList<>();
            Pair<Date, Double> returnInfoPair = null;

            if (needsReturnSchedulePlan.equals("Y")) {
                returnFlight = flightSessionBean.retrieveFlightByNumber(returnFlightNumber);
                System.out.print("Enter layover time for return flight " + returnFlightNumber + " (in hours)> ");
                layOver = sc.nextInt();
                sc.nextLine();
                returnPlan.setFlight(returnFlight);
                returnPlan.setFlightNumber(returnFlightNumber);
                returnPlan.setType(plan.getType());
                returnPlan.setIsDisabled(false);

                if (plan.getRecurrentEndDate() != null) {
                    returnPlan.setRecurrentEndDate(plan.getRecurrentEndDate());
                }
            }

            if (needsReturnSchedulePlan.equals("Y") && (plan.getType().equals(FlightScheduleType.MANUALSINGLE) || plan.getType().equals(FlightScheduleType.RECURRENTNDAY))) {
                Calendar c = Calendar.getInstance();
                c.setTime(infoPair.getKey());
                double duration = infoPair.getValue();
                int hour = (int) duration;
                int min = (int) (duration % 1 * 60);
                c.add(Calendar.HOUR_OF_DAY, hour);
                c.add(Calendar.MINUTE, min);
                c.add(Calendar.HOUR_OF_DAY, layOver);
                Date newDeparture = c.getTime();
                returnInfoPair = new Pair<>(newDeparture, infoPair.getValue());
            } else if (needsReturnSchedulePlan.equals("Y") && plan.getType().equals(FlightScheduleType.MANUALMULTIPLE)) {
                for (Pair<Date, Double> fs : flightInfo) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(fs.getKey());
                    double duration = fs.getValue();
                    int hour = (int) duration;
                    int min = (int) (duration % 1 * 60);
                    c.add(Calendar.HOUR_OF_DAY, hour);
                    c.add(Calendar.MINUTE, min);
                    c.add(Calendar.HOUR_OF_DAY, layOver);
                    Date newDeparture = c.getTime();
                    returnFlightInfo.add(new Pair<>(newDeparture, fs.getValue()));
                }
            }

            // Create fare for each cabin class
            List<CabinClass> cabinClass = flight.getAircraftConfiguration().getCabinClasses();
            System.out.println("Aircraft Configuration for flight " + flight.getFlightNumber() + " contains " + cabinClass.size() + " cabins");
            System.out.println("Please enter fares for each cabin class\n");

            List<Fare> fares = new ArrayList<>();
            for (CabinClass cc : cabinClass) {
                while (true) {
                    fares.add(createFare(cc, sc));
                    System.out.print("Would you like to add more fares to cabin class " + cc.getType() + " [Y/N]> ");
                    String reply = sc.nextLine().trim().toUpperCase();

                    if (!reply.equals("Y")) {
                        break;
                    }
                }
            }

            Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations = validator.validate(plan);
            if (constraintViolations.isEmpty()) {
                if (plan.getType().equals(FlightScheduleType.MANUALSINGLE)) {

                    plan = flightSchedulePlanSessionBean.createNewFlightSchedulePlan(plan, fares, flight.getFlightId(), infoPair, 0);
                    System.out.println("New Flight Schedule Plan for Flight " + plan.getFlightNumber() + " created successfully!\n");
                    if (needsReturnSchedulePlan.equals("Y")) {
                        flightSchedulePlanSessionBean.createNewReturnFlightSchedulePlan(returnPlan, plan, returnFlight.getFlightId(), returnInfoPair, 0);
                        System.out.println("New Flight Schedule Plan for Return Flight " + returnFlightNumber + " created successfully!\n");
                    }

                } else if (plan.getType().equals(FlightScheduleType.MANUALMULTIPLE)) {

                    plan = flightSchedulePlanSessionBean.createNewFlightSchedulePlanMultiple(plan, fares, flight.getFlightId(), flightInfo);
                    System.out.println("New Flight Schedule Plan for Flight " + plan.getFlightNumber() + " created successfully!\n");
                    if (needsReturnSchedulePlan.equals("Y")) {
                        flightSchedulePlanSessionBean.createNewReturnFlightSchedulePlanMultiple(returnPlan, plan, returnFlight.getFlightId(), returnFlightInfo);
                        System.out.println("New Flight Schedule Plan for Return Flight " + returnFlightNumber + " created successfully!\n");
                    }

                } else if (plan.getType().equals(FlightScheduleType.RECURRENTNDAY)) {

                    plan = flightSchedulePlanSessionBean.createNewFlightSchedulePlan(plan, fares, flight.getFlightId(), infoPair, days);
                    System.out.println("New Flight Schedule Plan for Flight " + plan.getFlightNumber() + " created successfully!\n");
                    if (needsReturnSchedulePlan.equals("Y")) {
                        flightSchedulePlanSessionBean.createNewReturnFlightSchedulePlan(returnPlan, plan, returnFlight.getFlightId(), returnInfoPair, days);
                        System.out.println("New Flight Schedule Plan for Return Flight " + returnFlightNumber + " created successfully!\n");
                    }

                } else {

                    plan = flightSchedulePlanSessionBean.createNewFlightSchedulePlanWeekly(plan, fares, flight.getFlightId(), infoPair, recurrentDay);
                    System.out.println("New Flight Schedule Plan for Flight " + plan.getFlightNumber() + " created successfully!\n");
                    if (needsReturnSchedulePlan.equals("Y")) {
                        List<FlightSchedule> fss = plan.getFlightSchedules();

                        for (FlightSchedule fs : fss) {
                            Calendar c = Calendar.getInstance();
                            c.setTime(fs.getDepartureDateTime());
                            double duration = fs.getEstimatedDuration();
                            int hour = (int) duration;
                            int min = (int) (duration % 1 * 60);
                            c.add(Calendar.HOUR_OF_DAY, hour);
                            c.add(Calendar.MINUTE, min);
                            c.add(Calendar.HOUR_OF_DAY, layOver);
                            Date newDeparture = c.getTime();
                            returnFlightInfo.add(new Pair<>(newDeparture, fs.getEstimatedDuration()));
                        }
                        flightSchedulePlanSessionBean.createNewReturnFlightSchedulePlanMultiple(returnPlan, plan, returnFlight.getFlightId(), returnFlightInfo);
                        System.out.println("New Flight Schedule Plan for Return Flight " + returnFlightNumber + " created successfully!\n");
                    }

                }
            } else {
                showInputDataValidationErrorsForSchedulePlan(constraintViolations);
            }
        } catch (FlightNotFoundException ex) {
            System.out.println("Flight does not exist");
        } catch (FareExistException ex) {
            System.out.println("There exist Fare/s that already exist");
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }

    private void doViewAllFlightSchedulePlan() {
        List<FlightSchedulePlan> plans = flightSchedulePlanSessionBean.retrieveAllFlightSchedulePlan();
        if (!plans.isEmpty()) {
            System.out.printf("%10s%15s%20s%30s\n", "ID", "Flight Number", "Plan Type", "Number of Flight Schedule");
            for (FlightSchedulePlan plan : plans) {
                System.out.printf("%10s%15s%20s%30s\n", plan.getFlightSchedulePlanId(), plan.getFlightNumber(), plan.getType(), plan.getFlightSchedules().size());
            }
        } else {
            System.out.println("There is no existing Flight Schedule Plan record");
        }
    }

    private void doViewFlightSchedulePlanDetails(Scanner sc) {
        try {
            System.out.println("=== Merlion Flight RS System :: View Plan Details ===");
            System.out.print("Enter Flight Number> ");
            String flightNumber = sc.nextLine().trim().toUpperCase();

            List<FlightSchedulePlan> plans = flightSchedulePlanSessionBean.retrieveFlightSchedulePlanByFlightNumber(flightNumber);
            System.out.println("\nShowing schedule plans for flight number " + flightNumber + "...");

            System.out.printf("\n%20s%25s%30s%25s%40s%40s%20s%30s\n", "Type Plan", "Flight Schedule ID", "Departure Date", "Duration (h)", "Origin", "Destination", "Cabin Class Type", "Fare");

            for (FlightSchedulePlan plan : plans) {

                Flight flight = plan.getFlight();
                FlightRoute route = flight.getFlightRoute();
                List<FlightSchedule> schedule = plan.getFlightSchedules();

                List<Fare> fare = plan.getFares();

                for (FlightSchedule list : schedule) {
                    for (Fare fares : fare) {
                        System.out.printf("%20s%25s%30s%25s%40s%40s%20s%30s\n", plan.getType(), list.getFlightScheduleId(), list.getDepartureDateTime().toString().substring(0, 19), list.getEstimatedDuration(), route.getOriginAirport().getAirportName(), route.getDestinationAirport().getAirportName(), fares.getCabinClassType(), fares.getAmount());
                    }
                }
                System.out.printf("\n      Plan ID: %5s\n", plan.getFlightSchedulePlanId());
                System.out.println("      =========================================\n");
            }

            Long planId;
            System.out.println("\n1: Update Flight Schedule Plan");
            System.out.println("2: Delete Flight Schedule Plan");
            System.out.println("3: Back");
            System.out.print("> ");
            int response = sc.nextInt();
            sc.nextLine();
            switch (response) {
                case 1:
                    System.out.print("Enter plan ID to be updated> ");
                    planId = sc.nextLong();
                    sc.nextLine();
                    doUpdateFlightSchedulePlan(planId, sc);
                    break;
                case 2:
                    System.out.print("Enter plan ID to be deleted> ");
                    planId = sc.nextLong();
                    sc.nextLine();
                    doDeleteFlightSchedulePlan(planId, sc);
                    break;
                case 3:
                    menuScheduleManager();
                    break;
                default:
                    System.out.println("Invalid input. Please try again!");
                    doViewFlightSchedulePlanDetails(sc);
                    break;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            menuScheduleManager();
        }
    }

    private void doUpdateFlightSchedulePlan(Long planId, Scanner sc) {
        try {
            FlightSchedulePlan plan = flightSchedulePlanSessionBean.retrieveFlightSchedulePlanById(planId);
            System.out.println("\n1: Update Fares");
            System.out.println("2: Update Flight Schedules");
            System.out.println("3: Back");

            System.out.print("> ");
            int response = sc.nextInt();

            switch (response) {
                case 1:
                    doUpdateFares(plan, sc);
                    break;
                case 2:
                    doUpdateFlightSchedule(plan, sc);
                    break;
                case 3:
                    menuScheduleManager();
                    break;
                default:
                    System.out.println("Invalid input. Please try again!");
                    doUpdateFlightSchedulePlan(planId, sc);
                    break;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }

    private void doUpdateFares(FlightSchedulePlan plan, Scanner sc) {
        try {
            int i = 1;
            for (Fare fare : plan.getFares()) {
                System.out.println(i + ") " + fare.getFareCode() + ", $" + fare.getAmount());
                i++;
            }
            System.out.print("Which fare would you like to update (index no)> ");
            int choice = sc.nextInt();
            sc.nextLine();
            if (choice < 1 || choice > plan.getFares().size()) {
                System.out.println("Invalid option\nPlease try again!\n");
                return;
            }
            Fare fare = plan.getFares().get(choice - 1);
            System.out.print("Enter new fare amount> ");
            BigDecimal newAmt = sc.nextBigDecimal();
            fareSessionBean.updateFare(fare.getFareId(), newAmt);
            System.out.println("Fare updated successfully!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }

    private void doUpdateFlightSchedule(FlightSchedulePlan plan, Scanner sc) {
        System.out.printf("%15s%30s%20s\n", "ID", "Departure Date Time", "Duration");
        for (FlightSchedule flightSchedule : plan.getFlightSchedules()) {
            System.out.printf("%15s%30s%20s\n", flightSchedule.getFlightScheduleId(), flightSchedule.getDepartureDateTime().toString().substring(0, 19), flightSchedule.getEstimatedDuration());
        }
        System.out.print("Which flight schedule would you like to update (ID)> ");
        Long flightScheduleId = sc.nextLong();
        sc.nextLine();
        System.out.println();
        System.out.println("1: Update information");
        System.out.println("2: Delete flight schedule");
        System.out.println("3: Cancel");

        System.out.print("> ");
        int response = sc.nextInt();
        sc.nextLine();

        switch (response) {
            case 1:
                try {
                Date departure;
                double duration;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy hh:mm a");
                try {
                    System.out.print("Enter new departure Date and Time (dd/mm/yyyy hh:mm AM/PM)> ");
                    String input = sc.nextLine().trim();
                    departure = formatter.parse(input);
                } catch (ParseException ex) {
                    System.out.println("Invalid date and time\n");
                    return;
                }
                System.out.print("Enter new estimated flight duration (in hours)> ");
                duration = sc.nextDouble();
                flightScheduleSessionBean.updateFlightSchedule(flightScheduleId, departure, duration);
                System.out.println("Flight Schedule " + flightScheduleId + " successfully updated!\n");
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + "\n");
            }
            break;
            case 2:
                try {
                flightScheduleSessionBean.deleteFlightSchedule(flightScheduleId);
                System.out.println("Flight Schedule " + flightScheduleId + " successfully removed!\n");
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + "\n");
            }
            break;
            case 3:
                menuScheduleManager();
                break;
            default:
                System.out.println("Invalid input. Please try again!");
                doUpdateFlightSchedule(plan, sc);
                break;
        }
    }

    private void doDeleteFlightSchedulePlan(Long planId, Scanner sc) {
        try {
            FlightSchedulePlan plan = flightSchedulePlanSessionBean.retrieveFlightSchedulePlanById(planId);
            System.out.print("Are you sure you want to delete (Y/N)> ");
            String response = sc.nextLine().trim();

            if (response.equalsIgnoreCase("Y")) {
                try {
                    if (flightSchedulePlanSessionBean.deleteFlightSchedulePlan(plan.getFlightSchedulePlanId())) {;
                        System.out.println("Deletion successful!");
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage() + "\n");
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }

    private Pair<Date, Double> getFlightSchedule(Scanner sc, boolean recurrentWeekly) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");

        // Get departure date
        if (!recurrentWeekly) {
            System.out.print("Enter departure Date (e.g., 1 Jan 23)> ");
        } else {
            System.out.print("Enter start Date (e.g., 1 Jan 23)> ");
        }

        String dateInput = sc.nextLine().trim();
        Date departureDate = dateFormat.parse(dateInput);

        // Get departure time
        System.out.print("Enter departure Time (e.g., 2:30 PM)> ");
        String timeInput = sc.nextLine().trim();
        Date departureTime = timeFormat.parse(timeInput);

        // Combine date and time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(departureDate);
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTime(departureTime);
        calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));
        Date departure = calendar.getTime();

        // Get estimated flight duration
        System.out.print("Enter estimated flight duration (e.g., 6 hours 30 minutes)> ");
        String durationInput = sc.nextLine().trim();
        double duration = parseDurationToHours(durationInput);

        return new Pair<>(departure, duration);
    }

    private Fare createFare(CabinClass cabinClass, Scanner sc) {
        System.out.print("Enter fare basis code for cabin class " + cabinClass.getType() + "> ");
        String code = sc.next().trim().toUpperCase();
        System.out.print("Enter fare amount> ");
        BigDecimal cost = sc.nextBigDecimal();
        sc.nextLine();
        Fare fare = new Fare(code, cost, cabinClass.getType());
        return fare;
    }

    private double parseDurationToHours(String durationInput) {
        String[] parts = durationInput.split(" ");
        double hours = 0;

        for (int i = 0; i < parts.length; i += 1) {
            if (isHour(parts[i])) {
                hours += Double.parseDouble(parts[i - 1]);
            } else if (isMinute(parts[i])) {
                hours += Double.parseDouble(parts[i - 1]) / 60;
            }
        }
        return hours;
    }

    private boolean isHour(String unit) {
        return unit.equalsIgnoreCase("hours") || unit.equalsIgnoreCase("hour");
    }

    private boolean isMinute(String unit) {
        return unit.equalsIgnoreCase("minutes") || unit.equalsIgnoreCase("minute");
    }

    private void showInputDataValidationErrorsForFlight(Set<ConstraintViolation<Flight>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

    private void showInputDataValidationErrorsForSchedulePlan(Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations) {
        System.out.println("\nInput data validation error!:");
        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }
        System.out.println("\nPlease try again......\n");
    }

}

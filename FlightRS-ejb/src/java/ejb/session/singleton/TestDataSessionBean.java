/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftConfigurationSessionBeanLocal;
import ejb.session.stateless.FlightRouteSessionBeanLocal;
import ejb.session.stateless.FlightSchedulePlanSessionBeanLocal;
import ejb.session.stateless.FlightSessionBeanLocal;
import entity.Aircraft;
import entity.AircraftConfiguration;
import entity.Airport;
import entity.CabinClass;
import entity.Employee;
import entity.Fare;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.Partner;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CabinClassType;
import util.enumeration.EmployeeRole;
import util.enumeration.FlightScheduleType;

/**
 *
 * @author jayso
 */
@Singleton
@LocalBean
@Startup
public class TestDataSessionBean {

    @EJB
    private FlightSchedulePlanSessionBeanLocal flightSchedulePlanSessionBean;

    @EJB
    private FlightSessionBeanLocal flightSessionBean;

    @EJB
    private FlightRouteSessionBeanLocal flightRouteSessionBean;

    @EJB
    private AircraftConfigurationSessionBeanLocal aircraftConfigurationSessionBean;

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    public TestDataSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {

        if (em.find(Partner.class, 1l) == null) {
            initPartner();
        }

        if (em.find(Airport.class, 1l) == null) {
            initAirport();
        }

        if (em.find(Aircraft.class, 1l) == null) {
            initAircraft();
        }

        if (em.find(Employee.class, 1l) == null) {
            initEmployee();
            doDataInit();
        }
    }

    private void initEmployee() {
        Employee employee = new Employee("Fleet Manager", "fleetmanager@mlair.com.sg", "password", EmployeeRole.FLEETMANAGER);
        em.persist(employee);
        em.flush();
        employee = new Employee("Route Planner", "routeplanner@mlair.com.sg", "password", EmployeeRole.ROUTEPLANNER);
        em.persist(employee);
        em.flush();
        employee = new Employee("Schedule Manager", "schedulemanager@mlair.com.sg", "password", EmployeeRole.SCHEDULEMANAGER);
        em.persist(employee);
        em.flush();
        employee = new Employee("Sales Manager", "salesmanager@mlair.com.sg", "password", EmployeeRole.SALESMANAGER);
        em.persist(employee);
        em.flush();
        employee = new Employee("System Administrator", "admin@mlair.com.sg", "password", EmployeeRole.SYSTEMADMIN);
        em.persist(employee);
        em.flush();
    }

    private void initPartner() {
        Partner partner = new Partner("Holiday.com", "mlair@holiday.com", "password");
        em.persist(partner);
        em.flush();
    }

    private void initAirport() {
        Airport airport = new Airport("Changi Airport", "SIN", "Changi", "Singapore", "Singapore");
        em.persist(airport);
        em.flush();

        airport = new Airport("Hong Kong International Airport", "HKG", "Chek Lap Kok", "Hong Kong", "China");
        em.persist(airport);
        em.flush();

        airport = new Airport("Taoyuan International Airport", "TPE", "Taoyuan", "Taipei", "Taiwan R.O.C.");
        em.persist(airport);
        em.flush();

        airport = new Airport("Narita International Airport", "NRT", "Narita", "Chiba", "Japan");
        em.persist(airport);
        em.flush();

        airport = new Airport("Sydney Airport", "SYD", "Sydney", "New South Wales", "Australia");
        em.persist(airport);
        em.flush();
    }

    private void initAircraft() {
        Aircraft aircraft = new Aircraft("Boeing 737", 200);
        em.persist(aircraft);
        em.flush();
        aircraft = new Aircraft("Boeing 747", 400);
        em.persist(aircraft);
        em.flush();
    }

    private void doDataInit() {
        try {
            // Aircraft Configuration
            AircraftConfiguration config = new AircraftConfiguration("BOEING 737 ALL ECONOMY", 1, 180);
            List<CabinClass> list1 = new ArrayList<>();
            CabinClass cabin = new CabinClass(CabinClassType.Y, 1, 30, 6, "3-3", 180);
            list1.add(cabin);
            aircraftConfigurationSessionBean.createNewAircraftConfig(config, list1);

            AircraftConfiguration config1 = new AircraftConfiguration("BOEING 737 THREE CLASSES", 3, 180);
            list1 = new ArrayList<>();
            CabinClass cabin1 = new CabinClass(CabinClassType.F, 1, 5, 2, "1-1", 10);
            list1.add(cabin1);
            cabin1 = new CabinClass(CabinClassType.J, 1, 5, 4, "2-2", 20);
            list1.add(cabin1);
            cabin1 = new CabinClass(CabinClassType.Y, 1, 25, 6, "3-3", 150);
            list1.add(cabin1);
            aircraftConfigurationSessionBean.createNewAircraftConfig(config1, list1);

            AircraftConfiguration config2 = new AircraftConfiguration("BOEING 747 ALL ECONOMY", 1, 380);
            List<CabinClass> list2 = new ArrayList<>();
            CabinClass cabin2 = new CabinClass(CabinClassType.Y, 2, 38, 10, "3-4-3", 380);
            list2.add(cabin2);
            aircraftConfigurationSessionBean.createNewAircraftConfig(config2, list2);

            AircraftConfiguration config3 = new AircraftConfiguration("BOEING 747 THREE CLASSES", 3, 360);
            List<CabinClass> list3 = new ArrayList<>();
            CabinClass cabin3 = new CabinClass(CabinClassType.F, 1, 5, 2, "1-1", 10);
            list3.add(cabin3);
            cabin3 = new CabinClass(CabinClassType.J, 2, 5, 6, "2-2-2", 30);
            list3.add(cabin3);
            cabin3 = new CabinClass(CabinClassType.Y, 2, 32, 10, "3-4-3", 320);
            list3.add(cabin3);
            aircraftConfigurationSessionBean.createNewAircraftConfig(config3, list3);


            // Flight Route
            FlightRoute flightRoute = new FlightRoute(true, false);
            flightRouteSessionBean.createNewFlightRoute(flightRoute, "SIN", "HKG");
            flightRoute = new FlightRoute(true, false);
            flightRouteSessionBean.createNewFlightRoute(flightRoute, "HKG", "SIN");
            flightRoute = new FlightRoute(true, false);
            flightRouteSessionBean.createNewFlightRoute(flightRoute, "SIN", "TPE");
            flightRoute = new FlightRoute(true, false);
            flightRouteSessionBean.createNewFlightRoute(flightRoute, "TPE", "SIN");
            flightRoute = new FlightRoute(true, false);
            flightRouteSessionBean.createNewFlightRoute(flightRoute, "SIN", "NRT");
            flightRoute = new FlightRoute(true, false);
            flightRouteSessionBean.createNewFlightRoute(flightRoute, "NRT", "SIN");
            flightRoute = new FlightRoute(true, false);
            flightRouteSessionBean.createNewFlightRoute(flightRoute, "HKG", "NRT");
            flightRoute = new FlightRoute(true, false);
            flightRouteSessionBean.createNewFlightRoute(flightRoute, "NRT", "HKG");
            flightRoute = new FlightRoute(true, false);
            flightRouteSessionBean.createNewFlightRoute(flightRoute, "TPE", "NRT");
            flightRoute = new FlightRoute(true, false);
            flightRouteSessionBean.createNewFlightRoute(flightRoute, "NRT", "TPE");
            flightRoute = new FlightRoute(true, false);
            flightRouteSessionBean.createNewFlightRoute(flightRoute, "SIN", "SYD");
            flightRoute = new FlightRoute(true, false);
            flightRouteSessionBean.createNewFlightRoute(flightRoute, "SYD", "SIN");
            flightRoute = new FlightRoute(true, false);
            flightRouteSessionBean.createNewFlightRoute(flightRoute, "SYD", "NRT");
            flightRoute = new FlightRoute(true, false);
            flightRouteSessionBean.createNewFlightRoute(flightRoute, "NRT", "SYD");

            // Flight
            Flight flight = new Flight("ML111", false, "ML112");
            flightSessionBean.createNewFlight(flight, "SIN", "HKG", "BOEING 737 THREE CLASSES");
            flight = new Flight("ML112", false, "ML111");
            flightSessionBean.createNewFlight(flight, "HKG", "SIN", "BOEING 737 THREE CLASSES");

            flight = new Flight("ML211", false, "ML212");
            flightSessionBean.createNewFlight(flight, "SIN", "TPE", "BOEING 737 THREE CLASSES");
            flight = new Flight("ML212", false, "ML211");
            flightSessionBean.createNewFlight(flight, "TPE", "SIN", "BOEING 737 THREE CLASSES");

            flight = new Flight("ML311", false, "ML312");
            flightSessionBean.createNewFlight(flight, "SIN", "NRT", "BOEING 747 THREE CLASSES");
            flight = new Flight("ML312", false, "ML311");
            flightSessionBean.createNewFlight(flight, "NRT", "SIN", "BOEING 747 THREE CLASSES");

            flight = new Flight("ML411", false, "ML412");
            flightSessionBean.createNewFlight(flight, "HKG", "NRT", "BOEING 737 THREE CLASSES");
            flight = new Flight("ML412", false, "ML411");
            flightSessionBean.createNewFlight(flight, "NRT", "HKG", "BOEING 737 THREE CLASSES");

            flight = new Flight("ML511", false, "ML512");
            flightSessionBean.createNewFlight(flight, "TPE", "NRT", "Boeing 737 THREE CLASSES");
            flight = new Flight("ML512", false, "ML511");
            flightSessionBean.createNewFlight(flight, "NRT", "TPE", "Boeing 737 THREE CLASSES");

            flight = new Flight("ML611", false, "ML612");
            flightSessionBean.createNewFlight(flight, "SIN", "SYD", "BOEING 737 THREE CLASSES");
            flight = new Flight("ML612", false, "ML611");
            flightSessionBean.createNewFlight(flight, "SYD", "SIN", "BOEING 737 THREE CLASSES");

            flight = new Flight("ML621", false, "ML622");
            flightSessionBean.createNewFlight(flight, "SIN", "SYD", "BOEING 737 ALL ECONOMY");
            flight = new Flight("ML622", false, "ML621");
            flightSessionBean.createNewFlight(flight, "SYD", "SIN", "BOEING 737 ALL ECONOMY");

            flight = new Flight("ML711", false, "ML712");
            flightSessionBean.createNewFlight(flight, "SYD", "NRT", "BOEING 747 THREE CLASSES");
            flight = new Flight("ML712", false, "ML711");
            flightSessionBean.createNewFlight(flight, "NRT", "SYD", "BOEING 747 THREE CLASSES");

            // Flight Schedule Plan
            SimpleDateFormat formatter = new SimpleDateFormat("d MMM yy, h:mm a");
            SimpleDateFormat recFormatter = new SimpleDateFormat("d MMM yy");

            List<Fare> fares = new ArrayList<>();
            fares.add(new Fare(BigDecimal.valueOf(6000), CabinClassType.F));
            fares.add(new Fare(BigDecimal.valueOf(3000), CabinClassType.J));
            fares.add(new Fare(BigDecimal.valueOf(1000), CabinClassType.Y));

            FlightSchedulePlan plan = new FlightSchedulePlan("ML711", FlightScheduleType.RECURRENTWEEKLY, recFormatter.parse("31 Dec 23"), false);
            Flight findFlight = flightSessionBean.retrieveFlightByNumber("ML711");
            plan.setFlight(findFlight);
            Date startDateTime = formatter.parse("1 Dec 23, 9:00 AM");
            Pair<Date, Double> outBoundInfo = new Pair<>(startDateTime, 14.0);
            FlightSchedulePlan newPlan = flightSchedulePlanSessionBean.createNewFlightSchedulePlanWeekly(plan, fares, findFlight.getFlightId(), outBoundInfo, 2);

            FlightSchedulePlan returnPlan = new FlightSchedulePlan("ML712", FlightScheduleType.RECURRENTWEEKLY, recFormatter.parse("31 Dec 23"), false);
            Flight returnFindFlight = flightSessionBean.retrieveFlightByNumber("ML712");
            returnPlan.setFlight(returnFindFlight);

            List<Pair<Date, Double>> returnInfo = new ArrayList<>();
            for (FlightSchedule fs : newPlan.getFlightSchedules()) {
                Calendar c = Calendar.getInstance();
                c.setTime(fs.getDepartureDateTime());
                double duration = fs.getEstimatedDuration();
                int hour = (int) duration;
                int min = (int) (duration % 1 * 60);
                c.add(Calendar.HOUR_OF_DAY, hour);
                c.add(Calendar.MINUTE, min);
                c.add(Calendar.HOUR_OF_DAY, 2);
                Date newDeparture = c.getTime();
                returnInfo.add(new Pair<>(newDeparture, fs.getEstimatedDuration()));
            }
            flightSchedulePlanSessionBean.createNewReturnFlightSchedulePlanMultiple(returnPlan, newPlan, returnFindFlight.getFlightId(), returnInfo);

            List<Fare> fares611 = new ArrayList<>();
            fares611.add(new Fare(BigDecimal.valueOf(3000), CabinClassType.F));
            fares611.add(new Fare(BigDecimal.valueOf(1500), CabinClassType.J));
            fares611.add(new Fare(BigDecimal.valueOf(500), CabinClassType.Y));

            FlightSchedulePlan plan611 = new FlightSchedulePlan("ML611", FlightScheduleType.RECURRENTWEEKLY, recFormatter.parse("31 Dec 23"), false);
            Flight findFlight611 = flightSessionBean.retrieveFlightByNumber("ML611");
            plan611.setFlight(findFlight611);

            Date startDateTime611 = formatter.parse("1 Dec 23, 12:00 PM");
            Pair<Date, Double> outBoundInfo611 = new Pair<>(startDateTime611, 8.0);
            FlightSchedulePlan newPlan611 = flightSchedulePlanSessionBean.createNewFlightSchedulePlanWeekly(plan611, fares611, findFlight611.getFlightId(), outBoundInfo611, 1);

            FlightSchedulePlan returnPlan611 = new FlightSchedulePlan("ML612", FlightScheduleType.RECURRENTWEEKLY, recFormatter.parse("31 Dec 23"), false);
            findFlight611 = flightSessionBean.retrieveFlightByNumber("ML612");
            returnPlan611.setFlight(findFlight611);

            List<Pair<Date, Double>> returnInfo611 = new ArrayList<>();
            for (FlightSchedule fs611 : newPlan611.getFlightSchedules()) {
                Calendar c611 = Calendar.getInstance();
                c611.setTime(fs611.getDepartureDateTime());
                double duration611 = fs611.getEstimatedDuration();
                int hour611 = (int) duration611;
                int min611 = (int) (duration611 % 1 * 60);
                c611.add(Calendar.HOUR_OF_DAY, hour611);
                c611.add(Calendar.MINUTE, min611);
                c611.add(Calendar.HOUR_OF_DAY, 2);
                Date newDeparture611 = c611.getTime();
                returnInfo611.add(new Pair<>(newDeparture611, fs611.getEstimatedDuration()));
            }
            flightSchedulePlanSessionBean.createNewReturnFlightSchedulePlanMultiple(returnPlan611, newPlan611, findFlight611.getFlightId(), returnInfo611);

            List<Fare> fares621 = new ArrayList<>();
            fares621.add(new Fare(BigDecimal.valueOf(700), CabinClassType.Y));

            FlightSchedulePlan plan621 = new FlightSchedulePlan("ML621", FlightScheduleType.RECURRENTWEEKLY, recFormatter.parse("31 Dec 23"), false);
            Flight findFlight621 = flightSessionBean.retrieveFlightByNumber("ML621");
            plan621.setFlight(findFlight621);
            Date startDateTime621 = formatter.parse("1 Dec 23, 10:00 AM");
            Pair<Date, Double> outBoundInfo621 = new Pair<>(startDateTime621, 8.0);
            FlightSchedulePlan newPlan621 = flightSchedulePlanSessionBean.createNewFlightSchedulePlanWeekly(plan621, fares621, findFlight621.getFlightId(), outBoundInfo621, 3);

            FlightSchedulePlan returnPlan621 = new FlightSchedulePlan("ML622", FlightScheduleType.RECURRENTWEEKLY, recFormatter.parse("31 Dec 23"), false);
            findFlight621 = flightSessionBean.retrieveFlightByNumber("ML622");
            returnPlan621.setFlight(findFlight621);

            List<Pair<Date, Double>> returnInfo621 = new ArrayList<>();
            for (FlightSchedule fs621 : newPlan621.getFlightSchedules()) {
                Calendar c621 = Calendar.getInstance();
                c621.setTime(fs621.getDepartureDateTime());
                double duration621 = fs621.getEstimatedDuration();
                int hour621 = (int) duration621;
                int min621 = (int) (duration621 % 1 * 60);
                c621.add(Calendar.HOUR_OF_DAY, hour621);
                c621.add(Calendar.MINUTE, min621);
                c621.add(Calendar.HOUR_OF_DAY, 2);
                Date newDeparture621 = c621.getTime();
                returnInfo621.add(new Pair<>(newDeparture621, fs621.getEstimatedDuration()));
            }
            flightSchedulePlanSessionBean.createNewReturnFlightSchedulePlanMultiple(returnPlan621, newPlan621, findFlight621.getFlightId(), returnInfo621);

            List<Fare> fares311 = new ArrayList<>();
            fares311.add(new Fare(BigDecimal.valueOf(3100), CabinClassType.F));
            fares311.add(new Fare(BigDecimal.valueOf(1600), CabinClassType.J));
            fares311.add(new Fare(BigDecimal.valueOf(600), CabinClassType.Y));

            FlightSchedulePlan plan311 = new FlightSchedulePlan("ML311", FlightScheduleType.RECURRENTWEEKLY, recFormatter.parse("31 Dec 23"), false);
            Flight findFlight311 = flightSessionBean.retrieveFlightByNumber("ML311");
            plan311.setFlight(findFlight311);
            Date startDateTime311 = formatter.parse("1 Dec 23, 10:00 AM");
            Pair<Date, Double> outBoundInfo311 = new Pair<>(startDateTime311, 6.5);
            FlightSchedulePlan newPlan311 = flightSchedulePlanSessionBean.createNewFlightSchedulePlanWeekly(plan311, fares311, findFlight311.getFlightId(), outBoundInfo311, 2);

            FlightSchedulePlan returnPlan311 = new FlightSchedulePlan("ML312", FlightScheduleType.RECURRENTWEEKLY, recFormatter.parse("31 Dec 23"), false);
            findFlight311 = flightSessionBean.retrieveFlightByNumber("ML312");
            returnPlan311.setFlight(findFlight311);

            List<Pair<Date, Double>> returnInfo311 = new ArrayList<>();
            for (FlightSchedule fs311 : newPlan311.getFlightSchedules()) {
                Calendar c311 = Calendar.getInstance();
                c311.setTime(fs311.getDepartureDateTime());
                double duration311 = fs311.getEstimatedDuration();
                int hour311 = (int) duration311;
                int min311 = (int) (duration311 % 1 * 60);
                c311.add(Calendar.HOUR_OF_DAY, hour311);
                c311.add(Calendar.MINUTE, min311);
                c311.add(Calendar.HOUR_OF_DAY, 3);
                Date newDeparture311 = c311.getTime();
                returnInfo311.add(new Pair<>(newDeparture311, fs311.getEstimatedDuration()));
            }
            flightSchedulePlanSessionBean.createNewReturnFlightSchedulePlanMultiple(returnPlan311, newPlan311, findFlight311.getFlightId(), returnInfo311);

            List<Fare> fares411 = new ArrayList<>();
            fares311.add(new Fare(BigDecimal.valueOf(2900), CabinClassType.F));
            fares311.add(new Fare(BigDecimal.valueOf(1400), CabinClassType.J));
            fares311.add(new Fare(BigDecimal.valueOf(400), CabinClassType.Y));
            FlightSchedulePlan plan411 = new FlightSchedulePlan("ML411", FlightScheduleType.RECURRENTNDAY, recFormatter.parse("31 Dec 23"), false);
            Flight findFlight411 = flightSessionBean.retrieveFlightByNumber("ML411");
            plan411.setFlight(findFlight411);
            Date startDateTime411 = formatter.parse("1 Dec 23, 10:00 PM");
            Pair<Date, Double> outBoundInfo411 = new Pair<>(startDateTime411, 4.0);
            FlightSchedulePlan newPlan411 = flightSchedulePlanSessionBean.createNewFlightSchedulePlan(plan411, fares411, findFlight411.getFlightId(), outBoundInfo411, 2);

            FlightSchedulePlan returnPlan411 = new FlightSchedulePlan("ML412", FlightScheduleType.RECURRENTNDAY, recFormatter.parse("31 Dec 23"), false);
            Flight findReturnFlight411 = flightSessionBean.retrieveFlightByNumber("ML412");
            returnPlan411.setFlight(findReturnFlight411);
            List<Pair<Date, Double>> returnInfo411 = new ArrayList<>();
            for (FlightSchedule fs : newPlan411.getFlightSchedules()) {
                Calendar c = Calendar.getInstance();
                c.setTime(fs.getDepartureDateTime());
                double duration = fs.getEstimatedDuration();
                int hour = (int) duration;
                int min = (int) (duration % 1 * 60);
                c.add(Calendar.HOUR_OF_DAY, hour);
                c.add(Calendar.MINUTE, min);
                c.add(Calendar.HOUR_OF_DAY, 4);
                Date newDeparture = c.getTime();
                returnInfo411.add(new Pair<>(newDeparture, duration));
            }
            flightSchedulePlanSessionBean.createNewReturnFlightSchedulePlanMultiple(returnPlan411, newPlan411, findReturnFlight411.getFlightId(), returnInfo411);

            List<Fare> fares511 = new ArrayList<>();
            fares511.add(new Fare(BigDecimal.valueOf(3100), CabinClassType.F));
            fares511.add(new Fare(BigDecimal.valueOf(1600), CabinClassType.J));
            fares511.add(new Fare(BigDecimal.valueOf(600), CabinClassType.Y));

            FlightSchedulePlan plan511 = new FlightSchedulePlan("ML511", FlightScheduleType.MANUALMULTIPLE, null, false);
            Flight findFlight511 = flightSessionBean.retrieveFlightByNumber("ML511");
            plan511.setFlight(findFlight511);
            Date startDate511 = formatter.parse("7 Dec 23, 5:00 PM");
            Date secondDate511 = formatter.parse("8 Dec 23, 5:00 PM");
            Date lastDate511 = formatter.parse("9 Dec 23, 5:00 PM");
            List<Pair<Date, Double>> outBoundInfo511 = new ArrayList<>();
            outBoundInfo511.add(new Pair<>(startDate511, 3.0));
            outBoundInfo511.add(new Pair<>(secondDate511, 3.0));
            outBoundInfo511.add(new Pair<>(lastDate511, 3.0));
            FlightSchedulePlan newPlan511 = flightSchedulePlanSessionBean.createNewFlightSchedulePlanMultiple(plan511, fares511, findFlight511.getFlightId(), outBoundInfo511);

            FlightSchedulePlan returnPlan511 = new FlightSchedulePlan("ML512", FlightScheduleType.MANUALMULTIPLE, null, false);
            Flight findReturnFlight511 = flightSessionBean.retrieveFlightByNumber("ML512");
            returnPlan511.setFlight(findReturnFlight511);

            List<Pair<Date, Double>> returnInfo511 = new ArrayList<>();
            for (Pair<Date, Double> fs : outBoundInfo511) {
                Calendar c2 = Calendar.getInstance();
                c2.setTime(fs.getKey());
                double duration2 = fs.getValue();
                int hour2 = (int) duration2;
                int min2 = (int) (duration2 % 1 * 60);
                c2.add(Calendar.HOUR_OF_DAY, hour2);
                c2.add(Calendar.MINUTE, min2);
                c2.add(Calendar.HOUR_OF_DAY, 2);
                Date newDeparture2 = c2.getTime();
                returnInfo511.add(new Pair<>(newDeparture2, fs.getValue()));
            }
            flightSchedulePlanSessionBean.createNewReturnFlightSchedulePlanMultiple(returnPlan511, newPlan511, findReturnFlight511.getFlightId(), returnInfo511);

        } catch (Exception ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import entity.Aircraft;
import entity.Airport;
import entity.Employee;
import entity.Partner;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.EmployeeRole;

/**
 *
 * @author jayso
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        if (em.find(Employee.class, 1l) == null) {
            initEmployee();
        }

        if (em.find(Partner.class, 1l) == null) {
            initPartner();
        }

        if (em.find(Airport.class, 1l) == null) {
            initAirport();
        }

        if (em.find(Aircraft.class, 1l) == null) {
            initAircraft();
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

//        // United States
//        Airport airport = new Airport("Los Angeles International Airport", "LAX", "Los Angeles", "California", "United States");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Chicago O'Hare International Airport", "ORD", "Chicago", "Illinois", "United States");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("John F. Kennedy International Airport", "JFK", "New York", "New York", "United States");
//        em.persist(airport);
//        em.flush();
//
//        // United Kingdom
//        airport = new Airport("Heathrow Airport", "LHR", "London", "England", "United Kingdom");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Manchester Airport", "MAN", "Manchester", "England", "United Kingdom");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Edinburgh Airport", "EDI", "Edinburgh", "Scotland", "United Kingdom");
//        em.persist(airport);
//        em.flush();
//
//        // France
//        airport = new Airport("Charles de Gaulle Airport", "CDG", "Paris", "Île-de-France", "France");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Nice Côte d'Azur Airport", "NCE", "Nice", "Provence-Alpes-Côte d'Azur", "France");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Lyon-Saint-Exupéry Airport", "LYS", "Lyon", "Auvergne-Rhône-Alpes", "France");
//        em.persist(airport);
//        em.flush();
//
//        // Japan
//        airport = new Airport("Tokyo Haneda Airport", "HND", "Tokyo", "Kanto", "Japan");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Osaka Kansai International Airport", "KIX", "Osaka", "Kansai", "Japan");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Narita International Airport", "NRT", "Tokyo", "Kanto", "Japan");
//        em.persist(airport);
//        em.flush();
//
//        // Australia
//        airport = new Airport("Sydney Airport", "SYD", "Sydney", "New South Wales", "Australia");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Melbourne Airport", "MEL", "Melbourne", "Victoria", "Australia");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Brisbane Airport", "BNE", "Brisbane", "Queensland", "Australia");
//        em.persist(airport);
//        em.flush();
//
//        // United Arab Emirates
//        airport = new Airport("Dubai International Airport", "DXB", "Dubai", null, "United Arab Emirates");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Abu Dhabi International Airport", "AUH", "Abu Dhabi", null, "United Arab Emirates");
//        em.persist(airport);
//        em.flush();
//
//        // Indonesia
//        airport = new Airport("Soekarno-Hatta International Airport", "CGK", "Jakarta", "Jakarta", "Indonesia");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Kualanamu International Airport", "KNO", "Medan", "North Sumatra", "Indonesia");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Ngurah Rai International Airport", "DPS", "Denpasar", "Bali", "Indonesia");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Juanda International Airport", "SUB", "Surabaya", "East Java", "Indonesia");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Adisucipto International Airport", "JOG", "Yogyakarta", "Special Region of Yogyakarta", "Indonesia");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Sultan Hasanuddin International Airport", "UPG", "Makassar", "South Sulawesi", "Indonesia");
//        em.persist(airport);
//        em.flush();
//
//        // Singapore
//        airport = new Airport("Changi Airport", "SIN", "Singapore", null, "Singapore");
//        em.persist(airport);
//        em.flush();
//
//        // China
//        airport = new Airport("Beijing Capital International Airport", "PEK", "Beijing", null, "China");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Shanghai Pudong International Airport", "PVG", "Shanghai", null, "China");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Guangzhou Baiyun International Airport", "CAN", "Guangzhou", "Guangdong", "China");
//        em.persist(airport);
//        em.flush();
//
//        // South Korea
//        airport = new Airport("Incheon International Airport", "ICN", "Seoul", null, "South Korea");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Jeju International Airport", "CJU", "Jeju", "Jeju", "South Korea");
//        em.persist(airport);
//        em.flush();
//
//        airport = new Airport("Daegu International Airport", "TAE", "Daegu", "North Gyeongsang", "South Korea");
//        em.persist(airport);
//        em.flush();
    }

    private void initAircraft() {
        Aircraft aircraft = new Aircraft("Boeing 737", 200);
        em.persist(aircraft);
        em.flush();
        aircraft = new Aircraft("Boeing 747", 400);
        em.persist(aircraft);
        em.flush();
    }

}

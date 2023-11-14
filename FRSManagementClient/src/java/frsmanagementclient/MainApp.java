/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginException;

/**
 *
 * @author jayso
 */
public class MainApp {

    private EmployeeSessionBeanRemote employeeSessionBean;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean;
    private FlightRouteSessionBeanRemote flightRouteSessionBean;
    private FlightSessionBeanRemote flightSessionBean;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean;
    private Employee employee;
    private FlightPlanningModule flightPlanning;
    private FlightOperationModule flightOperation;
    private SalesManagementModule salesManagement;
    public boolean exitRequested = false;

    public MainApp() {

    }

    public MainApp(EmployeeSessionBeanRemote employeeSessionBean,
            AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean,
            FlightRouteSessionBeanRemote flightRouteSessionBean,
            FlightSessionBeanRemote flightSessionBean,
            FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean) {
        this.employeeSessionBean = employeeSessionBean;
        this.aircraftConfigurationSessionBean = aircraftConfigurationSessionBean;
        this.flightRouteSessionBean = flightRouteSessionBean;
        this.flightSessionBean = flightSessionBean;
        this.flightSchedulePlanSessionBean = flightSchedulePlanSessionBean;
    }

    public void run() throws EmployeeNotFoundException, InvalidLoginException {
        Scanner sc = new Scanner(System.in);
        while (!exitRequested) {
            System.out.println("*** Welcome to Merlion Flight Management System ***");
            if (employee == null) {
                doLogin(sc);
                System.out.println();
            } else {
                if (employee.getemployeeRole().name().equals("FLEETMANAGER")) {
                    flightPlanning = new FlightPlanningModule(this, employee, aircraftConfigurationSessionBean);
                    flightPlanning.menuFleetManager();
                } else if (employee.getemployeeRole().name().equals("ROUTEPLANNER")) {
                    flightPlanning = new FlightPlanningModule(this, employee, flightRouteSessionBean);
                    flightPlanning.menuRoutePlanner();
                } else if (employee.getemployeeRole().name().equals("SCHEDULEMANAGER")) {
                    flightOperation = new FlightOperationModule(this, employee, flightSessionBean, flightSchedulePlanSessionBean);
                    flightOperation.menuScheduleManager();
                } else if (employee.getemployeeRole().name().equals("SALESMANAGER")) {
//                    salesManagement = new SalesManagementModule(this, employee, flightReservationSessionBean);
//                    salesManagement.menuSalesManagement();
                }
            }
        }

    }

    private void doLogin(Scanner sc) throws EmployeeNotFoundException, InvalidLoginException {
        System.out.println("Please login before continuing..\n");
        System.out.print("email: ");
        String email = sc.nextLine();

        try {
            Employee currEmployee = employeeSessionBean.retrieveEmployeeByEmail(email);
            System.out.print("password: ");
            String password = sc.nextLine();
            if (employeeSessionBean.checkEmployeeCredentials(email, password)) {
                employee = currEmployee;
                System.out.println("You are logged in as " + employee.getName() + "\n");
            }
        } catch (EmployeeNotFoundException ex) {
            throw new EmployeeNotFoundException("Such employee email is not found");
        } catch (InvalidLoginException ex) {
            throw new InvalidLoginException("Invalid login credentials");
        }
    }

    public void doLogOut() {
        employee = null;
        System.out.println("You have exited. Goodbye!");
        exitRequested = true;
    }

}

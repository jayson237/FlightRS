/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateful.FlightReservationSessionBeanRemote;
import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FareSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.SeatInventorySessionBeanRemote;
import javax.ejb.EJB;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginException;

/**
 *
 * @author jayso
 */
public class Main {

    @EJB
    private static SeatInventorySessionBeanRemote seatInventorySessionBean;

    @EJB
    private static FareSessionBeanRemote fareSessionBean;

    @EJB
    private static FlightScheduleSessionBeanRemote flightScheduleSessionBean;

    @EJB
    private static FlightReservationSessionBeanRemote flightReservationSessionBean;

    @EJB
    private static FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean;

    @EJB
    private static FlightSessionBeanRemote flightSessionBean;

    @EJB
    private static FlightRouteSessionBeanRemote flightRouteSessionBean;

    @EJB
    private static AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBean;

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBean;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws EmployeeNotFoundException, InvalidLoginException {
        MainApp mainApp = new MainApp(employeeSessionBean, aircraftConfigurationSessionBean, fareSessionBean, flightRouteSessionBean, flightSessionBean, flightSchedulePlanSessionBean, flightScheduleSessionBean, flightReservationSessionBean, seatInventorySessionBean);
        mainApp.run();
    }

}

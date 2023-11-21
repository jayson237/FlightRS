/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package frsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateful.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.SeatInventorySessionBeanRemote;
import ejb.session.stateless.TransactionSessionBeanRemote;
import entity.Customer;
import javax.ejb.EJB;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginException;

/**
 *
 * @author jayso
 */
public class Main {

    @EJB
    private static FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean;

    @EJB
    private static SeatInventorySessionBeanRemote seatInventorySessionBean;

    
    @EJB
    private static FlightReservationSessionBeanRemote flightReservationSessionBean;

    @EJB
    private static TransactionSessionBeanRemote transactionSessionBean;

    @EJB
    private static FlightScheduleSessionBeanRemote flightScheduleSessionBean;

    
    @EJB
    private static FlightSessionBeanRemote flightSessionBean;

    @EJB
    private static CustomerSessionBeanRemote customerSessionBean;
    
    private static Customer customer;

    
    public static void main(String[] args) throws CustomerNotFoundException, InvalidLoginException {
        MainApp mainApp = new MainApp(flightSchedulePlanSessionBean, customerSessionBean, flightSessionBean, flightScheduleSessionBean, seatInventorySessionBean, flightReservationSessionBean, transactionSessionBean, customer);
        mainApp.run();
    }

}

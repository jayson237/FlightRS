/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateful.FlightReservationSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author jayso
 */
public class SalesManagementModule {

    private Employee employee;
    private FlightReservationSessionBeanRemote flightReservationSessionBean;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private MainApp mainApp;

    public SalesManagementModule(MainApp mainApp, Employee employee, FlightReservationSessionBeanRemote flightReservationSessionBean) {
        this.mainApp = mainApp;
        this.employee = employee;
        this.flightReservationSessionBean = flightReservationSessionBean;
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public void menuSalesManagement() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Merlion FlightRS Sales Management Module ===\n");
        System.out.println("1. View Seats Inventory");
        System.out.println("2. View Flight Reservations");
        System.out.println("3. Log out");
        System.out.print("> ");
        int input = sc.nextInt();
        sc.nextLine();

        switch (input) {
            case 1:
//                doViewSeatsInventory(sc);
                break;
            case 2:
//                doViewReservations(sc);
                break;
            case 3:
                mainApp.doLogOut();
                break;
            default:
                System.out.println("Invalid input, please try again");
                menuSalesManagement();
                break;
        }
    }

    private void doViewSeatsInventory(Scanner sc) {

    }

    private void doViewReservations(Scanner sc) {

    }
}

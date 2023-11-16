/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.Customer;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginException;

/**
 *
 * @author jayso
 */
public class MainApp {

    private CustomerSessionBeanRemote customerSessionBean;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Customer customer;

    public MainApp(CustomerSessionBeanRemote customerSessionBean) {
        this.customerSessionBean = customerSessionBean;
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    public void run() throws CustomerNotFoundException, InvalidLoginException {

        while (true) {
            Scanner sc = new Scanner(System.in);
            if (customer == null) {
                System.out.println("*** Welcome to Merlion Flight Reservation System ***\n");
                System.out.println("1. Login");
                System.out.println("2. Sign up");
                System.out.print("> ");
                int input = sc.nextInt();
                sc.nextLine();

                switch (input) {
                    case 1:
                        doLogin(sc);
                        break;
                    case 2:
                        doSignUp(sc);
                        break;
                    default:
                        System.out.println("Invalid Input please try again");
                        run();
                        break;
                }
            } else {
                doReservationMenu(sc);

            }

        }
    }

    private void doLogin(Scanner sc) throws CustomerNotFoundException, InvalidLoginException {
        System.out.println("=== Merlion Flight RS :: Customer Login ===\n");
        System.out.print("email: ");
        String email = sc.nextLine().trim();

        try {
            Customer currCustomer = customerSessionBean.retrieveCustomerByEmail(email);
            System.out.print("password: ");
            String password = sc.nextLine();
            if (customerSessionBean.checkCustomerCredentials(email, password)) {
                this.customer = currCustomer;
                System.out.println("You are logged in as " + customer.getFirstName() + "\n");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + "\n");
            doLogin(sc);

        }
    }

    private void doSignUp(Scanner sc) {
        Customer c = new Customer();
        System.out.println("=== Merlion Flight RS :: Customer Registration ===\n");
        System.out.print("Enter First Name> ");
        c.setFirstName(sc.nextLine().trim());
        System.out.print("Enter Last Name> ");
        c.setLastName(sc.nextLine().trim());
        System.out.print("Enter Email> ");
        c.setEmail(sc.nextLine().trim());
        System.out.print("Enter Mobile Number> ");
        c.setMobileNumber(sc.nextLine().trim());
        System.out.print("Enter address> ");
        c.setAddress(sc.nextLine().trim());
        System.out.print("Enter password> ");
        c.setPassword(sc.nextLine().trim());
        Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(c);
        if (constraintViolations.isEmpty()) {
            try {
                Customer newCustomer = customerSessionBean.registerCustomer(c);
                System.out.println("Customer " + newCustomer.getCustomerId() + " is registered successfully\n");
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        } else {
            showInputDataValidationErrorsForCustomer(constraintViolations);
        }
    }

    private void doReservationMenu(Scanner sc) {
        System.out.println("=== Merlion Flight RS :: Main Menu ===\n");
        System.out.println("1. Reserve Flight");
        System.out.println("2. View My Flight Reservations");
        System.out.println("3. View My Flight Reservation Details");
        System.out.println("4. Log out");
        System.out.print("> ");
        int input = sc.nextInt();
        sc.nextLine();

        switch (input) {
            case 1:
//                doReserveFlight();
                break;
            case 2:
//                doViewMyReservations();
                break;
            case 3:
//                doViewMyFlightReservationDetails();
                break;
            case 4:
                doLogout();
                break;
            default:
                System.out.println("Invalid Input please try again");
                doReservationMenu(sc);
                break;
        }
    }

    public void doLogout() {
        customer = null;
        System.out.println("You have exited. Goodbye!\n");
    }

    private void showInputDataValidationErrorsForCustomer(Set<ConstraintViolation<Customer>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}

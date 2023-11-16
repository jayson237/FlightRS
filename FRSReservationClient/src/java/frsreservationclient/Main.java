/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package frsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import javax.ejb.EJB;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginException;

/**
 *
 * @author jayso
 */
public class Main {

    @EJB
    private static CustomerSessionBeanRemote customerSessionBean;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws CustomerNotFoundException, InvalidLoginException {
        MainApp mainApp = new MainApp(customerSessionBean);
        mainApp.run();
    }

}

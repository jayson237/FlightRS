/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Remote;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginException;

/**
 *
 * @author jayso
 */
@Remote
public interface CustomerSessionBeanRemote {

    public boolean checkCustomerCredentials(String email, String password) throws CustomerNotFoundException, InvalidLoginException;

    public Customer registerCustomer(Customer customer) throws CustomerExistException, GeneralException, InputDataValidationException;

    public Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException;

}

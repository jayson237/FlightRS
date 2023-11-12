/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author jayso
 */
@Local
public interface CustomerSessionBeanLocal {

    public Customer retrieveCustomerById(Long employeeId) throws CustomerNotFoundException;

    
    
}

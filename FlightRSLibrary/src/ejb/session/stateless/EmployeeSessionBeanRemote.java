/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Remote;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginException;

/**
 *
 * @author jayso
 */
@Remote
public interface EmployeeSessionBeanRemote {

    public boolean checkEmployeeCredentials(String email, String password) throws EmployeeNotFoundException, InvalidLoginException;

    public Employee retrieveEmployeeByEmail(String email) throws EmployeeNotFoundException;

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginException;

/**
 *
 * @author jayso
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    public EmployeeSessionBean() {

    }

    @Override
    public boolean checkEmployeeCredentials(String email, String password) throws EmployeeNotFoundException, InvalidLoginException {
        Employee e = retrieveEmployeeByEmail(email);
        if (e == null) {
            throw new EmployeeNotFoundException("Employee with the email " + email + " does not exist");
        }

        if (!e.getPassword().equals(password)) {
            throw new InvalidLoginException("Incorrect password for the provided email");
        }
        return true;
    }

    @Override
    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException {
        Employee e = em.find(Employee.class, employeeId);
        if (e != null) {
            return e;
        }
        throw new EmployeeNotFoundException("Employee with the id: " + employeeId + " does not exist\n");
    }

    @Override
    public Employee retrieveEmployeeByEmail(String email) throws EmployeeNotFoundException {
        try {
            Query q = em.createQuery("SELECT e FROM Employee e WHERE e.email = :email");
            q.setParameter("email", email);
            Employee e = (Employee) q.getSingleResult();
            return e;
        } catch (NoResultException ex) {
            throw new EmployeeNotFoundException("Employee with the email " + email + " does not exist");
        }
    }

}

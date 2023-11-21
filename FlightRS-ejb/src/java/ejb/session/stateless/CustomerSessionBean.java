/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginException;

/**
 *
 * @author jayso
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Customer registerCustomer(Customer customer) throws CustomerExistException, GeneralException, InputDataValidationException {
        Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(customer);
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(customer);
                em.flush();
                return customer;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CustomerExistException("Such customer account already exist");
                } else {
                    throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public boolean checkCustomerCredentials(String email, String password) throws CustomerNotFoundException, InvalidLoginException {
        Customer c = retrieveCustomerByEmail(email);
        if (c == null) {
            throw new CustomerNotFoundException("Customer with the email " + email + " does not exist");
        }

        if (!c.getPassword().equals(password)) {
            throw new InvalidLoginException("Incorrect password for the provided email");
        }
        return true;
    }

    @Override
    public Customer retrieveCustomerById(Long customerId) throws CustomerNotFoundException {
        Customer c = em.find(Customer.class, customerId);
        if (c != null) {
            return c;
        }
        throw new CustomerNotFoundException("Customer with the id: " + customerId + " does not exist\n");
    }

    @Override
    public Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException {
        try {
            Query q = em.createQuery("SELECT c FROM Customer c WHERE c.email = :email");
            q.setParameter("email", email);
            Customer e = (Customer) q.getSingleResult();
            return e;
        } catch (NoResultException ex) {
            throw new CustomerNotFoundException("Customer with the email " + email + " does not exist");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Customer>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}

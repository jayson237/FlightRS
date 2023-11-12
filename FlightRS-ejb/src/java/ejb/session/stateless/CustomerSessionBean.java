/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
    public void registerCustomer(Customer customer) throws CustomerExistException, GeneralException, InputDataValidationException {
        Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(customer);
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(customer);
                em.flush();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CustomerExistException("Atm card with same card number already exist");
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
        if (c != null) {
            return c.getPassword().equals(password);
        }
        throw new InvalidLoginException("Invalid login credentials");
    }

    @Override
    public Customer retrieveCustomerById(Long customerId) throws CustomerNotFoundException {
        Customer c = em.find(Customer.class, customerId);
        if (c != null) {
            return c;
        }
        throw new CustomerNotFoundException("Customer with the id: " + customerId + " does not exist");
    }

    private Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.email = :email");
        query.setParameter("email", email);

        try {
            Customer customer = (Customer) query.getSingleResult();
            return customer;
        } catch (Exception ex) {
            throw new CustomerNotFoundException("Customer with the eamil " + email + " does not exist");
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

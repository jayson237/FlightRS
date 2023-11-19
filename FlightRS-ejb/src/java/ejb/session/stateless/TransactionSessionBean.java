/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Transaction;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.TransactionExistException;
import util.exception.TransactionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author jayso
 */
@Stateless
public class TransactionSessionBean implements TransactionSessionBeanRemote, TransactionSessionBeanLocal {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public TransactionSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Transaction createNewTransaction(Transaction transaction, long customerId) throws UnknownPersistenceException, InputDataValidationException, CustomerNotFoundException, TransactionExistException {
        Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(transaction);
        Customer customer = customerSessionBean.retrieveCustomerById(customerId);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(transaction);

                transaction.setCustomer(customer);
                customer.getTransactions().add(transaction);

                em.flush();
                return transaction;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new TransactionExistException("Transaction already exists");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<Transaction> retrieveTransactionsByCustomerId(Long customerId) {
        Query query = em.createQuery("SELECT r FROM Transaction r WHERE r.customer.customerId = :id");
        query.setParameter("id", customerId);
        return query.getResultList();
    }

    @Override
    public Transaction retrieveTransactionById(long transactionId) throws TransactionNotFoundException {
        Transaction transaction = em.find(Transaction.class, transactionId);
        if (transaction == null) {
            throw new TransactionNotFoundException("Error: Transaction not found");
        } else {
            return transaction;
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Transaction>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}

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
 * @author timothy
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

        Customer customer = customerSessionBean.retrieveCustomerById(customerId);

        em.persist(transaction);

        transaction.setCustomer(customer);
        customer.getTransactions().add(transaction);

        em.flush();
        return transaction;

    }

    @Override
    public Transaction retrieveTransactionByIdUnmanaged(long transactionId) throws TransactionNotFoundException {
        Transaction transaction = em.find(Transaction.class, transactionId);
        if (transaction == null) {
            throw new TransactionNotFoundException("Transaction not found");
        } else {
            return transaction;
        }
    }

    @Override
    public List<Transaction> retrieveTransactionByCustomerIdUnmanaged(Long customerID) {
        Query query = em.createQuery("SELECT r FROM Transaction r WHERE r.customer.customerId = :id");
        query.setParameter("id", customerID);

        List<Transaction> list = query.getResultList();
        for (Transaction it : list) {
            em.detach(it);
        }
        return list;
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

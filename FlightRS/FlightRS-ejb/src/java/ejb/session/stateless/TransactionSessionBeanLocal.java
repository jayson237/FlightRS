/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Transaction;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.TransactionExistException;
import util.exception.TransactionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author timothy
 */
@Local
public interface TransactionSessionBeanLocal {

    public Transaction createNewTransaction(Transaction transaction, long customerId) throws UnknownPersistenceException, InputDataValidationException, CustomerNotFoundException, TransactionExistException;

    public Transaction retrieveTransactionById(long transactionId) throws TransactionNotFoundException;

    public List<Transaction> retrieveTransactionByCustomerIdUnmanaged(Long customerID);

    public Transaction retrieveTransactionByIdUnmanaged(long transactionId) throws TransactionNotFoundException;

}

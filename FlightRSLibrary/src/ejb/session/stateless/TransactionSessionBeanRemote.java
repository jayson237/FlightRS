/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Transaction;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.TransactionExistException;
import util.exception.TransactionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author jayso
 */
@Remote
public interface TransactionSessionBeanRemote {

    public Transaction createNewTransaction(Transaction transaction, long customerId) throws UnknownPersistenceException, InputDataValidationException, CustomerNotFoundException, TransactionExistException;

    public List<Transaction> retrieveTransactionsByCustomerId(Long customerId);

    public Transaction retrieveTransactionById(long transactionId) throws TransactionNotFoundException;
}

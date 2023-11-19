/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Transaction;
import java.util.List;
import javax.ejb.Local;
import util.exception.TransactionNotFoundException;

/**
 *
 * @author jayso
 */
@Local
public interface TransactionSessionBeanLocal {

    public Transaction retrieveTransactionById(long transactionId) throws TransactionNotFoundException;

}

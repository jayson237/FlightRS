/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class TransactionExistException extends Exception {

    /**
     * Creates a new instance of <code>TransactionExistException</code> without
     * detail message.
     */
    public TransactionExistException() {
    }

    /**
     * Constructs an instance of <code>TransactionExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TransactionExistException(String msg) {
        super(msg);
    }
}

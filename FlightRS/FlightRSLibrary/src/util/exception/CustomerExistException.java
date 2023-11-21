/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class CustomerExistException extends Exception {

    /**
     * Creates a new instance of <code>CustomerExistException</code> without
     * detail message.
     */
    public CustomerExistException() {
    }

    /**
     * Constructs an instance of <code>CustomerExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomerExistException(String msg) {
        super(msg);
    }
}

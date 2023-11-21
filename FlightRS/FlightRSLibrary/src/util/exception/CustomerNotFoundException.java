/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class CustomerNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>CustomerNotFoundException</code> without
     * detail message.
     */
    public CustomerNotFoundException() {
    }

    /**
     * Constructs an instance of <code>CustomerNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomerNotFoundException(String msg) {
        super(msg);
    }
}

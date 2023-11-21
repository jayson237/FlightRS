/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class DeleteFlightException extends Exception {

    /**
     * Creates a new instance of <code>DeleteFlightException</code> without
     * detail message.
     */
    public DeleteFlightException() {
    }

    /**
     * Constructs an instance of <code>DeleteFlightException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteFlightException(String msg) {
        super(msg);
    }
}

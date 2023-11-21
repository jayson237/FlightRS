/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class FlightExistException extends Exception {

    /**
     * Creates a new instance of <code>FlightExistException</code> without
     * detail message.
     */
    public FlightExistException() {
    }

    /**
     * Constructs an instance of <code>FlightExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightExistException(String msg) {
        super(msg);
    }
}

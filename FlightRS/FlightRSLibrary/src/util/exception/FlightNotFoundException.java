/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class FlightNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>FlightNotFoundException</code> without
     * detail message.
     */
    public FlightNotFoundException() {
    }

    /**
     * Constructs an instance of <code>FlightNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightNotFoundException(String msg) {
        super(msg);
    }
}

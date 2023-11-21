/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class FlightDisabledException extends Exception {

    /**
     * Creates a new instance of <code>FlightDisabledException</code> without
     * detail message.
     */
    public FlightDisabledException() {
    }

    /**
     * Constructs an instance of <code>FlightDisabledException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightDisabledException(String msg) {
        super(msg);
    }
}

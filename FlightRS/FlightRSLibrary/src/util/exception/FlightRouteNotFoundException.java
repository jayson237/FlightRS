/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class FlightRouteNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>FlightRouteNotFoundException</code>
     * without detail message.
     */
    public FlightRouteNotFoundException() {
    }

    /**
     * Constructs an instance of <code>FlightRouteNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightRouteNotFoundException(String msg) {
        super(msg);
    }
}

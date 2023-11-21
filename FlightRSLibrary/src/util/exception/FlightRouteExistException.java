/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class FlightRouteExistException extends Exception {

    /**
     * Creates a new instance of <code>FlightRouteExistException</code> without
     * detail message.
     */
    public FlightRouteExistException() {
    }

    /**
     * Constructs an instance of <code>FlightRouteExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightRouteExistException(String msg) {
        super(msg);
    }
}

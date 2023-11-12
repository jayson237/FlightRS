/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class DeleteFlightRouteException extends Exception {

    /**
     * Creates a new instance of <code>DeleteFlightRouteException</code> without
     * detail message.
     */
    public DeleteFlightRouteException() {
    }

    /**
     * Constructs an instance of <code>DeleteFlightRouteException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteFlightRouteException(String msg) {
        super(msg);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class FlightReservationNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>FlightReservationNotFoundException</code>
     * without detail message.
     */
    public FlightReservationNotFoundException() {
    }

    /**
     * Constructs an instance of <code>FlightReservationNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightReservationNotFoundException(String msg) {
        super(msg);
    }
}
